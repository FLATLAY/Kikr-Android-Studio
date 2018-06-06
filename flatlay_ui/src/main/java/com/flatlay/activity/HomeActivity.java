package com.flatlay.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.Braintree;
import com.flatlay.KikrApp;
import com.flatlay.KikrApp.TrackerName;
import com.flatlay.R;
import com.flatlay.dialog.LogoutDialogWithTab;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.fragment.FragmentPostUploadTab;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.fragment.FragmentSettings;
import com.flatlay.post_upload.CameraFragment;
import com.flatlay.post_upload.FragmentPostUploadTag;
import com.flatlay.post_upload.ProductSearchTagging;
import com.flatlay.sessionstore.SessionStore;
import com.flatlay.twitter.OauthItem;
import com.flatlay.twitter.TwitterOAuthActivity;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.MarshmallowPermissions;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.api.BraintreePaymentApi;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.ConnectWithFacebookApi;
import com.flatlaylib.api.ConnectWithTwitterApi;
import com.flatlaylib.api.FbTwFriendsApi;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.api.GetConnectedWithSocialApi;
import com.flatlaylib.api.GetUUIDApi;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.api.KikrCreditsApi;
import com.flatlaylib.api.TwoTapApi;
import com.flatlaylib.api.UpdateCurrentScreenApi;
import com.flatlaylib.api.WalletPinApi;
import com.flatlaylib.bean.FbUser;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProductFeedItem;
import com.flatlaylib.bean.TwitterFriendList;
import com.flatlaylib.bean.Uuid;
import com.flatlaylib.db.DatabaseHelper;
import com.flatlaylib.db.HelpPreference;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.db.dao.UuidDAO;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.BraintreePaymentRes;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.ConnectWithFacebookRes;
import com.flatlaylib.service.res.ConnectWithTwitterRes;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.service.res.GetConnectedWithSocialRes;
import com.flatlaylib.service.res.GetUUIDRes;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.service.res.KikrCreditsRes;
import com.flatlaylib.service.res.WalletPinRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.Utils;
import com.soundcloud.android.crop.Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.BranchShortLinkBuilder;
import twitter4j.User;


public class HomeActivity extends FragmentActivity implements OnClickListener, OnLoginCompleteListener {
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    public final static String TAG = "HomeActivity";
    public static int SHARING_CODE = 64206;
    public static PDKClient pdkClient;
    public static Spinner mstatus;
    public static TextView uploadphoto, menuBackTextView, postuploadtagbackTextView;
    public static ImageView photouploadnext, homeImageView, menuRightImageView, crossarrow, postUploadWithTag;
    public static TextView gallery, camera, instagram;
    public static TextView menuTextCartCount;
    public static ImageView menuSearchImageView;
    public static ArrayList<Activity> activities = new ArrayList<Activity>();
    private static ArrayList<Activity> homeActivtyList = new ArrayList<Activity>();
    public List<ProductFeedItem> list;
    public Stack<String> mFragmentStack;
    ProgressBar feed;
    double localCount;
    Runnable runnable;
    // ================================= Context Menu==========================================
    float lastX = 0, lastY = 0;
    View centarlView;
    View centarlView2;
    TextView lableTextView;
    int menuIndex = 0;
    private FragmentActivity context, mActivity;
    private View viewHeader;
    private android.support.v7.app.ActionBar actionBar;
    private LinearLayout menuSearchLayout, menuProfileLayout, menuConnectWithTwitterLayout,
            menuConnectWithFacebookLayout, menuKikrCreditsLayout, menuActivityLayout,
            kikerWalletLayout, kikrGuideLayout, menuMyFriendsLayout, menuInviteFriendsLayout,
            menuCheckInLayout, menuSupportLayout, menuSettingsLayout, menuLogoutoptionLayout,
            menuProfileOptionLayout, inspirationLayout, discoverLayout, menuDealLayout,
            menuMyFriendsOptionLayout, menuOrdersLayout, menuChatLayout,
            menuSettingsOptionLayout, menuDealOptionLayout, menuConnectWithInstagramLayout,
            menuInterestsLayout;
    private ImageView menuMyFriendsLayoutImageView, menuProfileLayoutImageView,
            menuDealImageView, menuSettingsLayoutImageView;
    private ArrayList<LinearLayout> layouts = new ArrayList<LinearLayout>();
    private Fragment mContent;
    private HomeActivity homeActivity;
    private int PAYPAL_REQUEST_CODE = 0, creditsCounter = 0;
    private Braintree braintree;
    private BroadcastReceiver receiver;
    private View twitterView, fbView;
    private ScrollView menuScrollView;
    private boolean isProfile = false, firstTime = false, isFirstTime, backPressedToExitOnce = false;
    private double kikrCredit = 0;
    private List<TextView> textViews = new ArrayList<>();
    private TextView logoutTextView, supportTextView, settingsTextView, inviteTextView,
            kikrChatTextView, checkInTextView, dealTextview, orderTextView, walletTextView,
            kikrCreditTextView, viewProfileTextView, kikrGuideTextView, viewSearchTextView,
            totalCredits, txtShop, txtFeed, menuRightTextView;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Uri mDestinationUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "HomeActivity");
        setContentView(R.layout.activity_home);
        if (homeActivtyList != null) {
            homeActivtyList.add(this);
        }
        UserPreference.getInstance().setCurrentScreen(Screen.HomeScreen);
        feed = (ProgressBar) findViewById(R.id.feed_load);

        // Branch Init
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            // Branch Link Data
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    Log.i("BRANCH SDK", referringParams.toString());
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);


        context = this;
        homeActivity = this;
        CommonUtility.printKeyHash(context);
        mFragmentStack = new Stack<String>();

        setActionBar();
        if (UserPreference.getInstance().getIsCheckBluetooth())

            inItMethods();
        addMenuLayouts();
        if (AppConstants.isFromTutorial) {
            AppConstants.isFromTutorial = false;
            if (CommonUtility.isOnline(context))
                updateScreen(Screen.HomeScreen);
        }
        new registerHomeReceiver(feed).execute();

        Tracker t = ((KikrApp) getApplication()).getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("HomeActivity");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        SharedPreferences settings = getSharedPreferences("CheckFirstTime", 0);

        if (settings.getBoolean("my_first_time", true)) {
            firstTime = true;
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

        Product prod = (Product) getIntent().getSerializableExtra("productobj");
        if (prod != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", prod);
        }

        if (getIntent().getStringExtra("profile_collection") != null) {
////////////change
            Bundle args = new Bundle();
            args.putString("key", getIntent().getStringExtra("profile_collection"));
            args.putString("key1", "no");
            FragmentProfileView f = new FragmentProfileView();
            f.setArguments(args);
            addFragment(f);

        }
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        isFirstTime = true;


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public double getCredits() {
        return kikrCredit;
    }

    private void getKikrCredits() {
        final KikrCreditsApi creditsApi = new KikrCreditsApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                final KikrCreditsRes kikrCreditsRes = (KikrCreditsRes) object;
                kikrCredit = StringUtils.getDoubleValue(kikrCreditsRes.getAmount());
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    KikrCreditsRes response = (KikrCreditsRes) object;
                    AlertUtils.showToast(context, response.getMessage());
                } else {
                    AlertUtils.showToast(context, R.string.invalid_response);
                }
            }
        });
        creditsApi.getKikrCredits(UserPreference.getInstance().getUserID());
        creditsApi.execute();
    }

    protected void showKikrCredit() {
        totalCredits.setVisibility(View.VISIBLE);
        localCount = 0;
        final Handler handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
//				totalCredits.setText(localCount+" Cr");
                totalCredits.setText(Math.round(localCount) + " Cr");
                localCount += kikrCredit / 20;
                if (localCount <= kikrCredit)
                    handler.postDelayed(runnable, 50);
                else
                    totalCredits.setText(Math.round(kikrCredit) + " Cr");
//                Syso.info("===================== kikrCredit: "+kikrCredit+",localCount:"+localCount);

            }
        };
        handler.postDelayed(runnable, 1);
    }

    public void inItMethods() {
        initLayout();
        setupData();
        setUpTextType();
        setClickListener();
    }

    private void addMenuLayouts() {
        layouts.add(menuConnectWithTwitterLayout);
        layouts.add(menuConnectWithFacebookLayout);
        layouts.add(menuKikrCreditsLayout);
        layouts.add(menuActivityLayout);
        layouts.add(menuInviteFriendsLayout);
        layouts.add(menuCheckInLayout);
        layouts.add(menuSupportLayout);
        layouts.add(kikerWalletLayout);
        layouts.add(menuLogoutoptionLayout);
        layouts.add(menuConnectWithInstagramLayout);
        layouts.add(discoverLayout);
        layouts.add(inspirationLayout);
        layouts.add(menuInterestsLayout);
        layouts.add(menuSettingsLayout);
        layouts.add(menuDealLayout);
        layouts.add(menuProfileLayout);
        layouts.add(menuSearchLayout);
        layouts.add(menuMyFriendsOptionLayout);
        layouts.add(menuOrdersLayout);
        layouts.add(menuProfileOptionLayout);
        layouts.add(menuChatLayout);
        layouts.add(kikrGuideLayout);
        textViews.add(txtShop);
        textViews.add(logoutTextView);
        textViews.add(supportTextView);
        textViews.add(settingsTextView);
        textViews.add(inviteTextView);
        textViews.add(kikrChatTextView);
        textViews.add(checkInTextView);
        textViews.add(dealTextview);
        textViews.add(orderTextView);
        textViews.add(walletTextView);
        textViews.add(kikrCreditTextView);
        textViews.add(viewProfileTextView);
        textViews.add(kikrGuideTextView);

    }

    private void setActionBar() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewHeader = (View) inflater.inflate(R.layout.app_discover_header, (ViewGroup) null, false);
        crossarrow = (ImageView) findViewById(R.id.crossarrow);
        postUploadWithTag = (ImageView) viewHeader.findViewById(R.id.postUploadWithTag);
        mstatus = (Spinner) viewHeader.findViewById(R.id.mstatus);
        uploadphoto = (TextView) viewHeader.findViewById(R.id.uploadphoto);
        photouploadnext = (ImageView) viewHeader.findViewById(R.id.photouploadnext);
        menuBackTextView = (TextView) viewHeader.findViewById(R.id.leftTextView);
        postuploadtagbackTextView = (TextView) viewHeader.findViewById(R.id.postuploadtagbackTextView);
        gallery = (TextView) viewHeader.findViewById(R.id.gallery);
        camera = (TextView) viewHeader.findViewById(R.id.camera);
        instagram = (TextView) viewHeader.findViewById(R.id.instagram);
        menuBackTextView = (TextView) viewHeader.findViewById(R.id.leftTextView);
        menuRightTextView = (TextView) viewHeader.findViewById(R.id.menuRightText);

        menuRightImageView = (ImageView) viewHeader.findViewById(R.id.menuRightImageView);
        homeImageView = (ImageView) viewHeader.findViewById(R.id.homeImageView);
        menuSearchImageView = (ImageView) viewHeader.findViewById(R.id.menuSearchImageView);
        menuTextCartCount = (TextView) viewHeader.findViewById(R.id.txtCartCount);

        menuRightImageView.setOnClickListener(this);
        homeImageView.setOnClickListener(this);
        menuBackTextView.setOnClickListener(this);
        postuploadtagbackTextView.setOnClickListener(this);
        menuSearchImageView.setOnClickListener(this);
        menuRightTextView.setOnClickListener(this);
    }

    public void showActionBar() {
        menuRightImageView.setVisibility(View.VISIBLE);
        menuTextCartCount.setVisibility(View.VISIBLE);
    }

    public void hideActionBar() {
    }

    private void startUploadPostSection() {
        addFragment(new CameraFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    public void checkPermissions() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MarshmallowPermissions.CAMERA_PERMISSION_REQUEST_CODE);
            } else if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MarshmallowPermissions.CAMERA_PERMISSION_REQUEST_CODE);
            } else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MarshmallowPermissions.CAMERA_PERMISSION_REQUEST_CODE);
            } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MarshmallowPermissions.CAMERA_PERMISSION_REQUEST_CODE);
            } else {
                startUploadPostSection();

            }
        } else
            startUploadPostSection();

    }

    public void fbLogIn() {
        Intent i = new Intent(context, FbSignActivity.class);
        i.putExtra("getFriendList", false);
        i.putExtra("getProfilePic", false);
        startActivityForResult(i, AppConstants.REQUEST_CODE_FB_LOGIN);
    }

    private void openProfileHelpScreen() {
        if (HelpPreference.getInstance().getHelpSideMenu().equals("yes")) {
            HelpPreference.getInstance().setHelpSideMenu("no");
        }
    }

    private void openFriendsHelpScreen() {
        if (HelpPreference.getInstance().getHelpFriendsSideMenu().equals("yes")) {
            HelpPreference.getInstance().setHelpFriendsSideMenu("no");
        }
    }

    public void initLayout() {
        menuSearchLayout = (LinearLayout) findViewById(R.id.menuSearchLayout);
        menuProfileLayout = (LinearLayout) findViewById(R.id.menuProfileLayout);
        menuConnectWithTwitterLayout = (LinearLayout) findViewById(R.id.menuConnectWithTwitterLayout);
        menuConnectWithFacebookLayout = (LinearLayout) findViewById(R.id.menuConnectWithFacebookLayout);
        menuKikrCreditsLayout = (LinearLayout) findViewById(R.id.menuKikrCreditsLayout);
        menuActivityLayout = (LinearLayout) findViewById(R.id.menuActivityLayout);
        menuMyFriendsLayout = (LinearLayout) findViewById(R.id.menuMyFriendsLayout);
        menuInviteFriendsLayout = (LinearLayout) findViewById(R.id.menuInviteFriendsLayout);
        menuCheckInLayout = (LinearLayout) findViewById(R.id.menuCheckInLayout);
        menuSupportLayout = (LinearLayout) findViewById(R.id.menuSupportLayout);
        menuSettingsLayout = (LinearLayout) findViewById(R.id.menuSettingsLayout);
        menuProfileLayoutImageView = (ImageView) findViewById(R.id.menuProfileLayoutImageView);
        menuLogoutoptionLayout = (LinearLayout) findViewById(R.id.menuLogoutoptionLayout);
        kikerWalletLayout = (LinearLayout) findViewById(R.id.kikerWalletLayout);
        menuProfileOptionLayout = (LinearLayout) findViewById(R.id.menuProfileOptionLayout);
        menuInterestsLayout = (LinearLayout) findViewById(R.id.menuInterestsLayout);
        twitterView = findViewById(R.id.twitterView);
        fbView = findViewById(R.id.fbView);
        menuDealImageView = (ImageView) findViewById(R.id.menuDealImageView);
        discoverLayout = (LinearLayout) findViewById(R.id.discoverLayout);
        inspirationLayout = (LinearLayout) findViewById(R.id.inspirationLayout);
        menuSettingsLayoutImageView = (ImageView) findViewById(R.id.menuSettingsLayoutImageView);
        menuSettingsOptionLayout = (LinearLayout) findViewById(R.id.menuSettingsOptionLayout);
        menuDealOptionLayout = (LinearLayout) findViewById(R.id.menuDealOptionLayout);
        menuDealLayout = (LinearLayout) findViewById(R.id.menuDealLayout);
        menuConnectWithInstagramLayout = (LinearLayout) findViewById(R.id.menuConnectWithInstagramLayout);
        menuScrollView = (ScrollView) findViewById(R.id.menuScrollView);
        menuOrdersLayout = (LinearLayout) findViewById(R.id.menuOrdersLayout);
        menuMyFriendsOptionLayout = (LinearLayout) findViewById(R.id.menuMyFriendsOptionLayout);
        menuMyFriendsLayoutImageView = (ImageView) findViewById(R.id.menuMyFriendsLayoutImageView);
        menuChatLayout = (LinearLayout) findViewById(R.id.menuChatLayout);
        kikrGuideLayout = (LinearLayout) findViewById(R.id.kikrGuideLayout);
        totalCredits = (TextView) findViewById(R.id.totalCredits);
        txtShop = (TextView) findViewById(R.id.txtShop);
        txtFeed = (TextView) findViewById(R.id.txtFeed);
        logoutTextView = (TextView) findViewById(R.id.logoutTextView);
        supportTextView = (TextView) findViewById(R.id.supportTextView);
        settingsTextView = (TextView) findViewById(R.id.settingsTextView);
        inviteTextView = (TextView) findViewById(R.id.inviteTextView);
        kikrChatTextView = (TextView) findViewById(R.id.kikrChatTextView);
        checkInTextView = (TextView) findViewById(R.id.checkInTextView);
        dealTextview = (TextView) findViewById(R.id.dealTextview);
        orderTextView = (TextView) findViewById(R.id.orderTextView);
        walletTextView = (TextView) findViewById(R.id.walletTextView);
        kikrCreditTextView = (TextView) findViewById(R.id.kikrCreditTextView);
        viewProfileTextView = (TextView) findViewById(R.id.viewProfileTextView);
        kikrGuideTextView = (TextView) findViewById(R.id.kikrGuideTextView);
        viewSearchTextView = (TextView) findViewById(R.id.viewSearchTextView);

        CommonUtility.hideSoftKeyboard(this);
    }

    public void setupData() {
        if (getIntent().getStringExtra("inspiration_id") != null) {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("inspiration_id")) && getIntent().getStringExtra("section").equals("likeinsp") || getIntent().getStringExtra("section").equals("commentinsp")) {
                String id = getIntent().getStringExtra("inspiration_id");
                addFragment(new FragmentInspirationDetail(id));
                cleanActivity();
            }
        } else if (getIntent().getStringExtra("section") != null) {
            if (getIntent().getStringExtra("section").equals("follow")) {
                addFragment(new FragmentInspirationSection(true));
                cleanActivity();
            } else if (getIntent().getStringExtra("section").equals("placeorder")) {
                UserPreference.getInstance().setIsNotificationClicked(true);
                String otherdata = getIntent().getStringExtra("otherdata");
                cleanActivity();
            } else if (getIntent().getStringExtra("section").equals("commission") || getIntent().getStringExtra("section").equals("collectionwithfiveproducts") || getIntent().getStringExtra("section").equals("invite")) {
                cleanActivity();
            } else if (getIntent().getStringExtra("section").equals("twotap")) {
                String otherdata = getIntent().getStringExtra("otherdata");
                try {
                    JSONObject jsonObject = new JSONObject(otherdata);
                    String msg = jsonObject.getString("message");
                    showMessage(msg, true);
                    cleanActivity();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (getIntent().getStringExtra("section").equals("cancel")) {
                String otherdata = getIntent().getStringExtra("message");
                showMessage(otherdata, false);
                cleanActivity();
            }
        } else {
            addFragment(new FragmentInspirationSection());
            if (UserPreference.getInstance().getCheckedIsConnected()
                    && CommonUtility.isOnline(context)) {
                checkStatusOFSocial();
            }
        }
        if (checkInternet()) {
        }
    }

    private void showMessage(String otherdata, boolean isLoadOrderDeatil) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                mFragmentStack.peek());

    }

    private void cleanActivity() {
        if (homeActivtyList != null) {
            for (int i = 0; i < homeActivtyList.size(); i++) {
                Activity a = homeActivtyList.get(i);
                if (a != this && !a.isFinishing()) {
                    a.finish();
                    homeActivtyList.remove(i);
                }
            }
        }
    }

    public void setClickListener() {

    }

    public void setUpTextType() {
    }

    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            hideAllFragment();
            addFragment(fragment);
        } else {
            Log.e(TAG, "Error in creating fragment.");
        }
    }

    public void inviteFriends() {

        String uriPath = "android.resource://com.flatlay/" + R.drawable.flatlayhomeimage;
        Uri uri = Uri.parse("android.resource://com.flatlay/" + R.drawable.flatlayhomeimage);
        String logo = uri.toString();
        System.out.print(logo);
        System.out.print(logo);

        Bitmap bitmapImageLocal = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.flatlayhomeimage);

        final String app_title = getResources().getString(R.string.app_name);
        BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(this)
                .addTag("referral")
                .addParameters("userid", UserPreference.getInstance().getUserID())
                .addParameters("usermail", UserPreference.getInstance().getEmail())
                .addParameters("username", UserPreference.getInstance().getUserName())
                .addParameters("userprofilepic", UserPreference.getInstance().getProfilePic())
                .addParameters("$og_title", getResources().getString(R.string.come_join))
                .addParameters("$og_description", getResources().getString(R.string.text_join) + "#" + app_title);

        shortUrlBuilder.generateShortUrl(new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {

                if (error != null) {
                    Log.e("Branch Error", "Branch create short url failed. Caused by -" + error.getMessage());
                    AlertUtils.showToast(context, "Failed to generate referral link. Try again.");
                } else {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.come_join));
                    intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.text_join) + " #" + app_title + " \n" + url);

                    startActivity(Intent.createChooser(intent, "Earn 25 credits for each sign up !"));
                    Log.i("Branch", "Got a Branch URL 1 " + url);
                }
            }
        });

    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void openWalletList() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG, "onActivityResult" + CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

        Log.e("home act", requestCode + "wefwe" + resultCode);
        if (requestCode == AppConstants.WALLETLIST && resultCode == RESULT_OK) {
        } else if (requestCode == AppConstants.REQUEST_CODE_FB_LOGIN && resultCode == RESULT_OK) {
            String id = data.getStringExtra("id");
            String email = data.getStringExtra("email");
            String gender = data.getStringExtra("gender");
            String name = data.getStringExtra("name");
            String username = data.getStringExtra("username");
            String birthday = data.getStringExtra("birthday");
            String profile_link = data.getStringExtra("profile_link");
            String location = data.getStringExtra("location");
            connectWithFacebook(id, email, gender, name, username, birthday, profile_link, location);
        } else if (requestCode == AppConstants.REQUEST_CODE_TWIT_LOGIN && resultCode == RESULT_OK) {
            String id = String.valueOf(data.getLongExtra("id", 0));
            String description = data.getStringExtra("description");
            String language = data.getStringExtra("language");
            String location = data.getStringExtra("location");
            String profile_image_url = data.getStringExtra("profile_image_url");
            String name = data.getStringExtra("name");
            String screen_name = data.getStringExtra("screen_name");
            String status = data.getStringExtra("status");
            String time_zone = data.getStringExtra("time_zone");

            connectWithTwitter(id, description, language, location, name, profile_image_url, screen_name, status, time_zone);

        } else if (requestCode == AppConstants.REQUEST_CODE_FB_FRIEND_LIST) {
            ArrayList<FbUser> fbUsers = (ArrayList<FbUser>) data.getSerializableExtra("friend_list");
            if (fbUsers != null && fbUsers.size() > 0) {
                uploadFbFriends(fbUsers);
            } else {
                UserPreference.getInstance().setIsFbConnected(true);

            }
        } else if (requestCode == AppConstants.REQUEST_CODE_TWIT_FRIEND_LIST) {
            ArrayList<OauthItem> twitUsers = (ArrayList<OauthItem>) data.getSerializableExtra("friend_list");
            showTwitterFriendList(twitUsers);
        } else if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                System.out.println("in result ok");
                AlertUtils.showToast(context, "Please wait...");
                braintree
                        .addListener(new Braintree.PaymentMethodNonceListener() {
                            public void onPaymentMethodNonce(
                                    String paymentMethodNonce) {
                                if (checkInternet())
                                    saveCheckout(paymentMethodNonce);
                            }
                        });
                braintree.finishPayWithPayPal(this, resultCode, data);
            }
        } else if (requestCode == AppConstants.REQUEST_CODE_MASKED_WALLET) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    mFragmentStack.peek());

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.w(TAG, "Going to handleCropResult2" + resultUri);
                handleCropResult(data);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                handleCropError2(data);

            }
        }

        if (requestCode == UCrop.REQUEST_CROP) {
            handleCropResult(data);
        }
        if (requestCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }


        if (requestCode == Crop.REQUEST_CROP) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((CameraFragment) fragment).onActivityResult(requestCode,
                    resultCode, data);
        }

        if (requestCode == Crop.REQUEST_PICK) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentPostUploadTab) fragment).onActivityResult(requestCode,
                    resultCode, data);

        }
        if (requestCode == AppConstants.REQUEST_CODE_INSTAGRAM) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentPostUploadTab) fragment).onActivityResult(requestCode,
                    resultCode, data);
        }

        if (requestCode == AppConstants.REQUEST_CODE_TWIT_TO_FRIEND) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());

        }
        if (requestCode == AppConstants.REQUEST_CODE_EMAIL_CHANGE) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentSettings) fragment).onActivityResult(requestCode,
                    resultCode, data);
        }
        if (requestCode == HomeActivity.SHARING_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {

            final TwitterAuthClient twitterAuthClient = new TwitterAuthClient();
            if (twitterAuthClient.getRequestCode() == requestCode) {
                twitterAuthClient.onActivityResult(requestCode, resultCode, data);
            }

        } else if (requestCode == PDKClient.PDKCLIENT_REQUEST_CODE) {
            PDKClient.getInstance().onOauthResponse(requestCode, resultCode, data);
        } else {

            if (data != null) {
                Uri dataUri = data.getData();
                if (dataUri != null) {
                    String urlString = dataUri.toString();
                    if (urlString.contains("oauth://ASNE?oauth_token")) {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
                        if (fragment != null) {
                            fragment.onActivityResult(requestCode, resultCode, data);
                        }
                    } else if (urlString.contains("www.tychotechnologies.in")) {
                        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
                        if (fragment != null) {
                            fragment.onActivityResult(requestCode, resultCode, data);
                        }
                    }
                }
            }
        }

        if (requestCode == ProductSearchTagging.PRODUCT_SEARCH_TAG) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentPostUploadTag) fragment).onActivityResult(requestCode,
                    resultCode, data);
            System.out.print("ProductSearchTagging item clicked");
        }


    }

    private void uploadFbFriends(List<FbUser> fbusers) {
        final FbTwFriendsApi service = new FbTwFriendsApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        //progressBarDialog.dismiss();
                        AlertUtils.showToast(context, R.string.alert_connected_with_fb);
                        UserPreference.getInstance().setIsFbConnected(true);

                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {

                    }
                });
        service.addFacebookFriend(UserPreference.getInstance().getUserID(),
                fbusers);
        service.execute();
    }

    private void getFBFriendList() {
        Intent i = new Intent(context, FbSignActivity.class);
        i.putExtra("getFriendList", true);
        i.putExtra("getProfilePic", false);
        startActivityForResult(i, AppConstants.REQUEST_CODE_FB_FRIEND_LIST);
    }

    public void twitterLoogedIn() {
        boolean logedIn = SessionStore.isTwitterLogedIn(context);
        if (logedIn) {
            new GetTwitterInfo().execute();
        } else {
            Intent intent = new Intent(context, TwitterOAuthActivity.class);
            intent.putExtra("is_get_list", false);
            startActivityForResult(intent, AppConstants.REQUEST_CODE_TWIT_LOGIN);
        }
    }

    private void getTwitterFriendList() {
        boolean logedIn = SessionStore.isTwitterLogedIn(context);
        if (logedIn) {
            new GetTwitterFriends().execute();
        } else {
            Intent intent = new Intent(context, TwitterOAuthActivity.class);
            intent.putExtra("is_get_list", true);
            startActivityForResult(intent,
                    AppConstants.REQUEST_CODE_TWIT_FRIEND_LIST);
        }
    }

    @Override
    public void onLoginSuccess(int socialNetworkID) {

    }

    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {

    }

    private void connectWithTwitter(String userId, String description, String language, String location, String name, String profile_image_url, String screen_name, String status,
                                    String time_zone) {

        final ConnectWithTwitterApi service = new ConnectWithTwitterApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {

                        if (object != null) {
                            getTwitterFriendList();
                        } else {
                            AlertUtils.showToast(context,
                                    R.string.invalid_response);
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {

                        if (object != null) {
                            ConnectWithTwitterRes facebookRes = (ConnectWithTwitterRes) object;
                            String message = facebookRes.getMessage();
                            AlertUtils.showToast(context, message);
                            getTwitterFriendList();
                        } else {
                            AlertUtils.showToast(context,
                                    R.string.invalid_response);
                        }
                    }
                });
        service.connectWithTwitter(userId, description, language, location,
                profile_image_url, name, screen_name, status, time_zone);
        service.execute();

    }

    private void checkStatusOFSocial() {

        final GetConnectedWithSocialApi service = new GetConnectedWithSocialApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        UserPreference.getInstance().setcheckedIsConnected(
                                false);
                        if (object != null) {
                            GetConnectedWithSocialRes connectedWithSocialRes = (GetConnectedWithSocialRes) object;
                            String fbStatus = connectedWithSocialRes.getFacebookid();
                            String twitterStatus = connectedWithSocialRes.getTwitter();
                            if (fbStatus.equalsIgnoreCase("yes")) {
                                UserPreference.getInstance().setIsFbConnected(
                                        true);
                                UserPreference.getInstance().setPassword();
                            } else {
                                UserPreference.getInstance().setIsFbConnected(
                                        false);
                            }
                            if (twitterStatus.equalsIgnoreCase("yes")) {
                                UserPreference.getInstance()
                                        .setIsTwitterConnected(true);
                                UserPreference.getInstance().setPassword();
                            } else {
                                UserPreference.getInstance()
                                        .setIsTwitterConnected(false);
                            }

                        } else {
                            AlertUtils.showToast(context,
                                    R.string.invalid_response);
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {

                        if (object != null) {
                        } else {
                            AlertUtils.showToast(context,
                                    R.string.invalid_response);
                        }
                    }
                });
        service.getStatusOfSocial(UserPreference.getInstance().getUserID());
        service.execute();

    }

    public void getTwitterInfo(User user) {
        try {
            long userId = user.getId();
            String description = user.getDescription();
            String language = user.getLang();
            String location = user.getLocation();
            String name = user.getName();
            String profile_image_url = user.getOriginalProfileImageURL();
            String screen_name = user.getScreenName();
            String status = "";
            if (user.getStatus() != null)
                status = user.getStatus().getText();
            String time_zone = user.getTimeZone();
            connectWithTwitter(String.valueOf(userId), description, language, location, name, profile_image_url, screen_name, status, time_zone);
        } catch (Exception e) {
            AlertUtils.showToast(context, "Unable to connect with twitter");
            e.printStackTrace();
        }
    }

    public void showTwitterFriendList(ArrayList<OauthItem> twitUsers) {
        if (twitUsers.size() > 0) {
            List<TwitterFriendList> data = new ArrayList<TwitterFriendList>();
            for (int i = 0; i < twitUsers.size(); i++) {
                TwitterFriendList list = new TwitterFriendList();
                list.setTwitter_id(twitUsers.get(i).getFriendId());
                list.setName((twitUsers.get(i).getFriendName()));
                list.setProfile_pic(twitUsers.get(i).getFriendImage());
                data.add(list);
            }
            uploadTwitterFriends(data);
        } else {
            AlertUtils.showToast(context, R.string.alert_twit_friend_not_found);
        }
    }

    private void uploadTwitterFriends(List<TwitterFriendList> data) {

        final FbTwFriendsApi service = new FbTwFriendsApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        AlertUtils.showToast(context, R.string.alert_connected_with_twitter);
                        UserPreference.getInstance().setIsTwitterConnected(true);

                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {

                    }
                });
        service.addTwitterFriend(UserPreference.getInstance().getUserID(), data);
        service.execute();
    }

    private void connectWithFacebook(String id, String email, String gender,
                                     String name, String username, String birthday, String profile_link,
                                     String location) {


        final ConnectWithFacebookApi service = new ConnectWithFacebookApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {

                        if (object != null) {
                            getFBFriendList();
                        } else {
                            AlertUtils.showToast(context,
                                    R.string.invalid_response);
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {

                        if (object != null) {
                            ConnectWithFacebookRes facebookRes = (ConnectWithFacebookRes) object;
                            String message = facebookRes.getMessage();
                            AlertUtils.showToast(context, message);
                            getFBFriendList();
                        } else {
                            AlertUtils.showToast(context,
                                    R.string.invalid_response);
                        }
                    }
                });
        service.connectWithFacebook(id, gender, birthday, profile_link, location, name, username);
        service.execute();
    }

    public void shareProduct(final Product product, final boolean isOther) {
        AlertUtils.showToast(context, "Please wait...");
        BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(this)
                .addParameters("id", product.getId())
                .addParameters("productname", product.getProductname())
                .addParameters("skunumber", product.getSkunumber())
                .addParameters("primarycategory", product.getPrimarycategory())
                .addParameters("secondarycategory", product.getSecondarycategory())
                .addParameters("product_url", product.getProducturl())
                .addParameters("productimageurl", product.getProductimageurl())
                .addParameters("shortproductdesc", product.getShortproductdesc())
                .addParameters("longproductdesc", product.getLongproductdesc())
                .addParameters("discount", product.getDiscount())
                .addParameters("discounttype", product.getDiscounttype())
                .addParameters("saleprice", product.getSaleprice())
                .addParameters("retailprice", product.getRetailprice())
                .addParameters("shippingcost", product.getShippingcost())
                .addParameters("keywords", product.getKeywords())
                .addParameters("manufacturename", product.getManufacturename())
                .addParameters("availability", product.getAvailability())
                .addParameters("shippinginfo", product.getShippinginfo())
                .addParameters("pixel", product.getPixel())
                .addParameters("merchantid", product.getMerchantid())
                .addParameters("merchantname", product.getMerchantname())
                .addParameters("quantity", product.getQuantity())
                .addParameters("color", product.getColor())
                .addParameters("cart_id", product.getCart_id())
                .addParameters("from_user_id", product.getFrom_user_id())
                .addParameters("from_collection_id", product.getFrom_collection_id())
                .addParameters("productcart_id", product.getProductcart_id())
                .addParameters("tbl_product_id", product.getTbl_product_id())
                .addParameters("size", product.getSize())
                .addParameters("selected_size", product.getSelected_size())
                .addParameters("selected_color", product.getSelected_color())
                .addParameters("brand_image", product.getBrand_image())
                .addParameters("like_count", product.getLike_info().getLike_count())
                .addParameters("like_id", product.getLike_info().getLike_id())
                .addParameters("affiliateurl", product.getAffiliateurlforsharing())
                .addParameters("option", product.getOption())
                .addParameters("fit", product.getFit())
                .addParameters("siteId", product.getSiteId())
                .addParameters("$og_title", product.getProductname())
                .addParameters("$og_description", product.getShortproductdesc())
                .addParameters("$og_image_url", product.getProductimageurl());
        // Get URL Asynchronously
        shortUrlBuilder.generateShortUrl(new Branch.BranchLinkCreateListener() {

            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error != null) {
                    Log.e("Branch Error", "Branch create short url failed. Caused by -" + error.getMessage());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    String link = AppConstants.SHARE_MESSAGE + " " + AppConstants.APP_LINK + "\nItem: " + product.getProducturl();
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    if (isOther)
                        startActivity(Intent.createChooser(intent, "Share"));
                    else {

                    }
                } else {
                    Log.i("Branch", "Got a Branch URL 2 " + url);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String link = "Check out the " + product.getProductname() + " on #Flatlay  " + url;
                    Log.w("link: ", "" + link);
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    if (isOther)
                        startActivity(Intent.createChooser(intent, "Share"));
                    else {
//
                    }
                }
            }
        });
    }

    public void shareImage(final String imageUrl, final boolean isOther, final String shareimagename) {
        AlertUtils.showToast(context, "Please wait...");
        BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(this)

                .addParameters("product_url", imageUrl);
        Log.w("temp", "CREATING BRANCH LINK!" + imageUrl);
        Log.w("temp", "CREATING BRANCH LINK!" + shareimagename);

        // Get URL Asynchronously
        shortUrlBuilder.generateShortUrl(new Branch.BranchLinkCreateListener() {

            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error != null) {
                    Log.e("Branch Error", "Branch create short url failed. Caused by -" + error.getMessage());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    String link = AppConstants.SHARE_MESSAGE + " " + AppConstants.APP_LINK + "\nItem: ";
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    if (isOther)
                        startActivity(Intent.createChooser(intent, "Share"));
                    else {
                    }
                } else {
                    Log.i("Branch", "Got a Branch URL 3 " + url);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String link = shareimagename;
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    if (isOther) {
                        startActivity(Intent.createChooser(intent, "Share"));
                    } else {
                    }
                }
            }
        });
    }

    public void shareProductCollection(final String collectionname) {


        BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(this)
                .addParameters("user_id", UserPreference.getInstance().getUserID())
                .addParameters("sharecollection", "1")
                .addParameters("$og_title", collectionname)
                .addParameters("$og_description", UserPreference.getInstance().getUserName() + " would like to share their " + collectionname + " collections with you.")
                .addParameters("$og_image_url", UserPreference.getInstance().getProfilePic());

        // Get URL Asynchronously
        shortUrlBuilder.generateShortUrl(new Branch.BranchLinkCreateListener() {

            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error != null) {
                    Log.e("Branch Error", "Branch create short url failed. Caused by -" + error.getMessage());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_TEXT, AppConstants.SHARE_MESSAGE + " "
                            + AppConstants.APP_LINK);
                    startActivity(Intent.createChooser(intent, "Share"));
                } else {
                    Log.i("Branch", "Got a Branch URL 4 " + url);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    Log.w("Collection Name:", "" + collectionname);
                    intent.putExtra(Intent.EXTRA_TEXT, "Check out the " + collectionname + " collections of " + UserPreference.getInstance().getUserName() + " on #FLATLAY " + url);

                    startActivity(Intent.createChooser(intent, "Share"));
                }
            }
        });
    }

    private void getUUIDList() {
        final GetUUIDApi service = new GetUUIDApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                GetUUIDRes uuidRes = (GetUUIDRes) object;
                List<Uuid> uuidLists = uuidRes.getUuid_list();
                for (int i = 0; i < uuidLists.size(); i++) {
                    UuidDAO dao = new UuidDAO(DatabaseHelper.getDatabase());
                    dao.insertOrUpdate(uuidLists.get(i));
                }
                UuidDAO dao = new UuidDAO(DatabaseHelper.getDatabase());
                System.out.println("List>>" + dao.getList());
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        service.getUUIDList(AppConstants.APP_ID);
        service.execute();
    }

    public void addFragment(Fragment fragment) {
        try {
            mContent = fragment;
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (mFragmentStack.size() > 0) {
                Fragment currentFragment = getSupportFragmentManager()
                        .findFragmentByTag(mFragmentStack.peek());
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                mFragmentStack.add(mContent.toString());
                transaction.add(R.id.frame_container, fragment, mContent.toString());
                transaction.addToBackStack(mContent.toString());
                if (fragment instanceof FragmentPostUploadTab)
                    transaction.commitAllowingStateLoss();
                else
                    transaction.commit();
            } else {
                mFragmentStack.add(mContent.toString());
                transaction.replace(R.id.frame_container, fragment, mContent.toString());
                transaction.addToBackStack(mContent.toString());
                transaction.commit();
            }
            checkBackButton(fragment);
            checkRightButton(fragment);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Fragment getCurrentFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.commit();
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentByTag(mFragmentStack.peek());
        return currentFragment;
    }
/*

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }
*/

    public void myAddFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getSupportFragmentManager()
                    .findFragmentByTag(mFragmentStack.peek());
            if (mFragmentStack.size() > 0 && !(currentFragment instanceof FragmentInspirationSection)) {
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                mFragmentStack.add(fragment.toString());
                transaction.replace(R.id.frame_container_section_1, fragment, fragment.toString());
                transaction.addToBackStack(fragment.toString());
                transaction.commit();

            } else {
                mFragmentStack.add(fragment.toString());
                transaction.replace(R.id.frame_container_section_1, fragment, fragment.toString());
                transaction.addToBackStack(fragment.toString());
                transaction.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFragmentRequestResult(Fragment fragment) {
        try {
            mContent = fragment;
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            System.out.println("1111 added>>" + mContent.toString());
            if (mFragmentStack.size() > 0) {
                Fragment currentFragment = getSupportFragmentManager()
                        .findFragmentByTag(mFragmentStack.peek());
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                mFragmentStack.add(mContent.toString());
                transaction.add(R.id.frame_container, fragment, mContent.toString());
                transaction.addToBackStack(mContent.toString());
                transaction.commit();
            } else {
                mFragmentStack.add(mContent.toString());
                transaction.replace(R.id.frame_container, fragment, mContent.toString());
                transaction.addToBackStack(mContent.toString());
                transaction.commit();
            }
            checkBackButton(fragment);
            checkRightButton(fragment);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishInspirationSection() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
        if (mFragmentStack.size() == 1 && fragment instanceof FragmentInspirationSection) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            hideFutter();
            CommonUtility.hideSoftKeyboard(this);
            if (mFragmentStack.size() == 1) {
                if (fragment instanceof FragmentInspirationSection) {
                } else if (fragment instanceof LandingActivity) {

                } else {

                    hideAllFragment();
                    addFragment(new FragmentInspirationSection());
                }
            } else {
                removeFragment();
                super.onBackPressed();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void backpostuploadtad() {

        showActionBar();
        mstatus.setVisibility(View.VISIBLE);
        crossarrow.setVisibility(View.VISIBLE);
        menuBackTextView.setVisibility(View.GONE);
        postuploadtagbackTextView.setVisibility(View.GONE);
        menuRightImageView.setVisibility(View.GONE);
        menuTextCartCount.setVisibility(View.GONE);
        camera.setVisibility(View.GONE);
        instagram.setVisibility(View.GONE);
        homeImageView.setVisibility(View.GONE);
        menuRightImageView.setVisibility(View.GONE);
        uploadphoto.setVisibility(View.GONE);
        removeFragment();
        super.onBackPressed();
    }

    public void removeFragment() {
        try {
            String fold = mFragmentStack.pop();
            Fragment f2 = getSupportFragmentManager().findFragmentByTag(fold);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.commit();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            checkBackButton(fragment);
            checkRightButton(fragment);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideAllFragment() {
        try {
            for (int i = 0; i < mFragmentStack.size(); i++) {
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentByTag(mFragmentStack.peek());
                if (fragment != null)
                    transaction.remove(fragment);
                transaction.commit();
            }
            mFragmentStack.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showFooter() {
    }

    public TextView getReloadFotter() {
        TextView textView = (TextView) findViewById(R.id.reloadTextView);
        textView.setVisibility(View.VISIBLE);
        textView.setText(Html.fromHtml(getResources().getString(R.string.no_internet)));
        return textView;
    }

    public void hideFutter() {
    }

    public void updateScreen(String current_screen) {
        final UpdateCurrentScreenApi updateCurrentScreenApi = new UpdateCurrentScreenApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        Syso.info("response  " + object);
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {
                        Syso.info("response  " + object);
                    }
                });
        updateCurrentScreenApi.updateScreen(UserPreference.getInstance()
                .getUserID(), current_screen);
        updateCurrentScreenApi.execute();
    }

    public boolean checkInternet() {
        if (CommonUtility.isOnline(context)) {
            return true;
        } else {
            CommonUtility.showNoInternetAlert(context);
            return false;
        }
    }

    public void startPaypal() {
        if (checkInternet()) {
            getTokenfromClient();
        }
    }

    private void getTokenfromClient() {

        final BraintreePaymentApi braintreePaymentApi = new BraintreePaymentApi(
                new ServiceCallback() {
                    @Override
                    public void handleOnSuccess(Object object) {
                        BraintreePaymentRes braintreePaymentRes = (BraintreePaymentRes) object;
                        String clientToken = braintreePaymentRes.getClient_token();
                        braintree = Braintree.getInstance(context, clientToken);
                        braintree.startPayWithPayPal(context, PAYPAL_REQUEST_CODE);
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                    }
                });
        braintreePaymentApi.getClientToken(UserPreference.getInstance().getUserID(), CommonUtility.getDeviceId(context));
        braintreePaymentApi.execute();
    }

    private void saveCheckout(String paymentMethodNonce) {

        final BraintreePaymentApi braintreePaymentApi = new BraintreePaymentApi(
                new ServiceCallback() {
                    @Override
                    public void handleOnSuccess(Object object) {
                        AlertUtils.showToast(context, R.string.payment_success_text);
                        createCart();
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        AlertUtils.showToast(context, R.string.payment_fail_text);
                    }
                });
        braintreePaymentApi.saveCheckout(UserPreference.getInstance()
                        .getUserID(), CommonUtility.getDeviceId(context),
                paymentMethodNonce);
        braintreePaymentApi.execute();

    }

    public void createCart() {
        final CartApi cartApi = new CartApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CartRes cartRes = (CartRes) object;
                UserPreference.getInstance().setCartID(cartRes.getCart_id());
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {
            }
        });
        cartApi.createCart(UserPreference.getInstance().getUserID());
        cartApi.execute();

    }

    public void showBackButton() {
        menuBackTextView.setVisibility(View.VISIBLE);
        postuploadtagbackTextView.setVisibility(View.GONE);
        menuRightImageView.setVisibility(View.VISIBLE);
        uploadphoto.setVisibility(View.GONE);
        menuTextCartCount.setVisibility(View.VISIBLE);
        homeImageView.setVisibility(View.VISIBLE);
        photouploadnext.setVisibility(View.GONE);
        postUploadWithTag.setVisibility(View.GONE);

    }

    private void checkBackButton(Fragment fragment) {

    }

    private void checkRightButton(Fragment fragment) {

    }

    private void showRightButton() {
        menuRightTextView.setVisibility(View.VISIBLE);
        menuSearchImageView.setVisibility(View.GONE);
        menuRightImageView.setVisibility(View.GONE);
    }

    private void hideRightButton() {
        menuRightTextView.setVisibility(View.GONE);
        menuSearchImageView.setVisibility(View.GONE);
        menuRightImageView.setVisibility(View.VISIBLE);
    }

    private void registerHomeReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        loadFragment(new FragmentInspirationSection());
                    }
                };
                handler.postDelayed(runnable, 100);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.ACTION_GO_TO_HOME);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            try {
                unregisterReceiver(receiver);
            } catch (Exception e) {
            }
        }
    }

    private void checkKikrWalletPin() {


        final WalletPinApi service = new WalletPinApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    WalletPinRes response = (WalletPinRes) object;
                    if (!TextUtils.isEmpty(response.getPin_created()) && response.getPin_created().equals("yes")) {
                        UserPreference.getInstance().setIsCreateWalletPin(false);

                    } else {

                    }
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {
                if (object != null) {
                    WalletPinRes response = (WalletPinRes) object;
                    AlertUtils.showToast(context, response.getMessage());
                } else {
                    AlertUtils.showToast(context, R.string.invalid_response);
                }
            }
        });
        service.checkKikrWalletPin(UserPreference.getInstance().getUserID());
        service.execute();
    }

    public void setimage(Bitmap bitmap) {

    }

    public void likeInspiration(final Product product, final UiUpdate uiUpdate) {
        Syso.info("123 like id: " + product.getLike_info().getLike_id()
                + ", count:" + product.getLike_info().getLike_count());
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {

                        InspirationRes inspirationRes = (InspirationRes) object;
                        String likeId = inspirationRes.getLike_id();

                        if (TextUtils.isEmpty(likeId)) {
                            product.getLike_info().setLike_id("");
                            product.getLike_info()
                                    .setLike_count(
                                            (CommonUtility.getInt(product
                                                    .getLike_info()
                                                    .getLike_count()) - 1)
                                                    + "");
                            AlertUtils.showToast(context, "Unliked");
                        } else {
                            product.getLike_info().setLike_id(likeId);
                            product.getLike_info()
                                    .setLike_count(
                                            (CommonUtility.getInt(product
                                                    .getLike_info()
                                                    .getLike_count()) + 1)
                                                    + "");
                            AlertUtils.showToast(context, "Liked");
                        }
                        if (uiUpdate != null) {
                            uiUpdate.updateUi();
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {
                        if (object != null) {
                            InspirationRes inspirationRes = (InspirationRes) object;
                            String message = inspirationRes.getMessage();
                            AlertUtils.showToast(context, message);
                        } else {
                            AlertUtils.showToast(context, R.string.invalid_response);
                        }
                    }
                });
        if (TextUtils.isEmpty(product.getLike_info().getLike_id())) {
            inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), product.getId(), "product");
        } else {
            inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), product.getLike_info().getLike_id());
        }
        inspirationSectionApi.execute();
    }

    public void likeInspiration2(final Inspiration inspiration, final UiUpdate uiUpdate) {

        Syso.info("123 like id: " + inspiration.getLike_id()
                + ", count:" + inspiration.getLike_count());
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {

                        InspirationRes inspirationRes = (InspirationRes) object;
                        String likeId = inspirationRes.getLike_id();

                        if (TextUtils.isEmpty(likeId)) {
                            inspiration.setLike_id("");
                            inspiration.setLike_count(
                                    (CommonUtility.getInt(inspiration.getLike_count()) - 1)
                                            + "");
                        } else {
                            inspiration.setLike_id(likeId);
                            inspiration.setLike_count(
                                    (CommonUtility.getInt(inspiration
                                            .getLike_count()) + 1)
                                            + "");
                        }
                        if (uiUpdate != null) {
                            uiUpdate.updateUi();
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {
                        if (object != null) {
                            InspirationRes inspirationRes = (InspirationRes) object;
                            String message = inspirationRes.getMessage();
                            AlertUtils.showToast(context, message);
                        } else {
                            AlertUtils.showToast(context, R.string.invalid_response);
                        }
                    }
                });
        if (TextUtils.isEmpty(inspiration.getLike_id())) {
            inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), inspiration.getInspiration_id(), "inspiration");
        } else {
            inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), inspiration.getLike_id());
        }
        inspirationSectionApi.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
        checkForUpdates();
    }

    private void checkForCrashes() {
    }

    private void checkForUpdates() {
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        Branch branch = Branch.getInstance();
        branch.initSession(new Branch.BranchReferralInitListener() {

            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    Branch.getInstance().setIdentity(UserPreference.getInstance().getUserID());
                } else {
                    Log.e("MyApp", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    public void refreshCartCount() {
        menuTextCartCount.setText(UserPreference.getInstance().getCartCount());
        menuTextCartCount.setVisibility(View.VISIBLE);
    }

    public void purchaseStatus(String purchase_id) {

        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    if (jsonObject.getString("message").equalsIgnoreCase("done")) {
                        UserPreference.getInstance().setPurchaseId("");
                        Log.w(TAG, "FragmentUserCart()");
                    } else {
                        AlertUtils.showToast(context, "Your previous order is still being processed. You should receive confirmation on the status shortly.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        twoTapApi.purchaseStatus(purchase_id);
        twoTapApi.execute();
    }

    public void followUser(String user_id) {

        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(context, response.getMessage());
                } else {
                    AlertUtils.showToast(context, R.string.invalid_response);
                }
            }
        });
        followUserApi.followUser(UserPreference.getInstance().getUserID(), user_id);
        followUserApi.execute();

    }

    public void unFollowUser(String user_id) {

        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {

                Syso.info("In handleOnSuccess>>" + object);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                // mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(context, response.getMessage());
                } else {
                    AlertUtils.showToast(context, R.string.invalid_response);
                }
            }
        });
        followUserApi.unFollowUser(UserPreference.getInstance().getUserID(), user_id);
        followUserApi.execute();

    }

    public void startCropActivity(@NonNull Uri uri) {
        Log.w(TAG, "startCropActivity()");

        mDestinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
        String filePath = Environment.getExternalStorageDirectory()
                + "/temporary_holder.jpg";

        Log.w("startCropActHA", "****2: " + uri.toString());

        /*
        UCrop uCrop = UCrop.of(uri, mDestinationUri);
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);
        uCrop.withMaxResultSize(500,500);
        uCrop.start(context);
        */

        CropImage.activity(uri)
                .setRequestedSize(800, 800, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .setAllowCounterRotation(true)
                .setAllowRotation(true)
                .setOutputUri(mDestinationUri)
                .start(context);

    }

    public void startCropActivityForMedia(@NonNull Uri uri) {
        Log.w(TAG, "startCropActivityForMedia()");
        mDestinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
        CropImage.activity(uri)
                .setRequestedSize(800, 800, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setFixAspectRatio(true)
                .setAllowCounterRotation(true)
                .setAllowRotation(true)
                .setAspectRatio(1, 1)
                .setOutputUri(mDestinationUri)
                .start(context);
    }

    //  cropping functions

    private UCrop basisConfig(@NonNull UCrop uCrop) {

        //uCrop = uCrop.withAspectRatio(1, 1);
        try {
            float ratioX = Float.valueOf(1);
            float ratioY = Float.valueOf(1);
            if (ratioX > 0 && ratioY > 0) {
                uCrop = uCrop.withAspectRatio(ratioX, ratioY);
            }
        } catch (NumberFormatException e) {
            Log.d("UCrop", "Number please");
        }
        return uCrop;
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {

        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setMaxBitmapSize(500);

        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {

        if (result != null) {
            Log.w("handleCropResultHA", "Here");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            if (fragment instanceof FragmentPostUploadTab)
                ((FragmentPostUploadTab) fragment).onActivityResult(UCrop.REQUEST_CROP, RESULT_OK
                        , result);
            else if (fragment instanceof CameraFragment)
                ((CameraFragment) fragment).onActivityResult(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK
                        , result);
        } else
            AlertUtils.showToast(HomeActivity.this, R.string.toast_cannot_retrieve_cropped_image);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e("UCrop Error", "handleCropError: ", cropError);
            Toast.makeText(HomeActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(HomeActivity.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropError2(@NonNull Intent result) {
        Toast.makeText(HomeActivity.this, "handleCropError2" + result, Toast.LENGTH_LONG).show();
        /*
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e("UCrop Error", "handleCropError: ", cropError);
            Toast.makeText(HomeActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(HomeActivity.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
        */
    }

    public void onSavePin(String imageUrl, String boardId, String text, String linkUrl) {

        if (!Utils.isEmpty(text) && !Utils.isEmpty(boardId) && !Utils.isEmpty(imageUrl)) {
            PDKClient.getInstance().createPin(text, boardId, imageUrl, linkUrl, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    Log.d(getClass().getName(), response.getData().toString());
                    Syso.info("12345678 >>> output" + response.getData().toString());
                    AlertUtils.showToast(context, "Shared Successfully");
                    // finish();
                }

                @Override
                public void onFailure(PDKException exception) {
                    Log.e(getClass().getName(), exception.getDetailMessage());
                    Syso.info("12345678 >>> output" + exception.getDetailMessage());
                    try {
                        JSONObject jsonObject = new JSONObject(exception.getDetailMessage());
                        AlertUtils.showToast(context, jsonObject.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertUtils.showToast(context, exception.getDetailMessage());
                    }
                    finish();
                }
            });
        } else {
            Toast.makeText(this, "Required fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void logoutscreen() {
        LogoutDialogWithTab logoutDialog = new LogoutDialogWithTab(context, homeActivity);
        logoutDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshmallowPermissions.CAMERA_PERMISSION_REQUEST_CODE) {
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i("Camera permission", "Received response for Camera permission request.");

            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i("Camera permission", "CAMERA permission has now been granted. Showing preview.");
                startUploadPostSection();

            } else if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startUploadPostSection();
            } else if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                startUploadPostSection();
            } else {
                Log.i("Camera permission", "CAMERA permission was not granted.");
                AlertUtils.showToast(this, "Camera or read/write storage permission was not granted.");

            }
            // END_INCLUDE(permission_result)

        }
    }

    public boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public class registerHomeReceiver extends AsyncTask<Void, Void, Void> {
        ProgressBar f;

        registerHomeReceiver(ProgressBar b) {
            this.f = b;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            loadFragment(new FragmentInspirationSection());
                        }
                    };
                    handler.postDelayed(runnable, 100);
                }
            };
            IntentFilter filter = new IntentFilter();
            filter.addAction(AppConstants.ACTION_GO_TO_HOME);
            registerReceiver(receiver, filter);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            f.setVisibility(View.GONE);
            SharedPreferences sharedpreferences = getSharedPreferences("flatlay_video", Context.MODE_PRIVATE);
            if (!sharedpreferences.getBoolean("download", false))
                new VideoAsyncTask().execute();

            super.onPostExecute(aVoid);
        }
    }

    private class GetTwitterInfo extends AsyncTask<Void, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(Void... params) {
            return TwitterOAuthActivity.getUserInfo(context);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            System.out.println("Twitter id>>" + user);
            if (user != null) {
                getTwitterInfo(user);
            } else {
                AlertUtils.showToast(context, "Failed to connect with twitter. Please try again.");
            }
        }
    }

    private class GetTwitterFriends extends
            AsyncTask<Void, Void, ArrayList<OauthItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<OauthItem> doInBackground(Void... params) {
            return TwitterOAuthActivity.getFollowerList(context);
        }

        @Override
        protected void onPostExecute(ArrayList<OauthItem> result) {
            super.onPostExecute(result);
            System.out.println("Twitter friend list>>" + result);
            showTwitterFriendList(result);
        }

    }

    public class VideoAsyncTask extends AsyncTask<Void, Void, Void> {

        private final int TIMEOUT_CONNECTION = 5000;//5sec
        private final int TIMEOUT_SOCKET = 30000;//30sec

        @Override
        protected Void doInBackground(Void... voids) {
            String imageURL = "https://drive.google.com/uc?export=download&id=139gxIRPKkl51_8oEFk-w4ZgujsaDWiXg";
            try {

                URL url = new URL(imageURL);
                long startTime = System.currentTimeMillis();
                Log.i(TAG, "image download beginning: " + imageURL);
                URLConnection ucon = url.openConnection();
                ucon.setReadTimeout(TIMEOUT_CONNECTION);
                ucon.setConnectTimeout(TIMEOUT_SOCKET);
                File file = new File(getCacheDir(), "Video.mp4");
                InputStream is = ucon.getInputStream();
                BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);
                FileOutputStream outStream = new FileOutputStream(file);
                byte[] buff = new byte[5 * 1024];
                int len;
                while ((len = inStream.read(buff)) != -1) {
                    outStream.write(buff, 0, len);
                }

                outStream.flush();
                outStream.close();
                inStream.close();
                Log.i(TAG, "download completed in "
                        + ((System.currentTimeMillis() - startTime) / 1000)
                        + " sec");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            SharedPreferences sharedpreferences = getSharedPreferences("flatlay_video", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("download", true);
            editor.commit();
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onCancelled() {
            SharedPreferences sharedpreferences = getSharedPreferences("flatlay_video", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("download", false);
            editor.commit();
            super.onCancelled();
        }
    }

}
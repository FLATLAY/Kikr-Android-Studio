package com.flatlay.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.braintreepayments.api.Braintree;
import com.flatlay.KikrApp;
import com.flatlay.KikrApp.TrackerName;
import com.flatlay.R;
import com.flatlay.dialog.LogoutDialogWithTab;
import com.flatlay.dialog.PostUploadTagDialog;

import com.flatlay.menu.ArcLayout;
import com.flatlay.menu.ContextMenuView;
import com.flatlay.post_upload.FragmentPostUploadTag;
import com.flatlay.sessionstore.SessionStore;
import com.flatlay.twitter.OauthItem;
import com.flatlay.twitter.TwitterOAuthActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.MarshmallowPermissions;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.api.BraintreePaymentApi;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.ConnectWithTwitterApi;
import com.flatlaylib.api.FbTwFriendsApi;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.api.UpdateCurrentScreenApi;
import com.flatlaylib.api.WalletPinApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProductFeedItem;
import com.flatlaylib.bean.TwitterFriendList;
import com.flatlaylib.bean.Uuid;
import com.flatlaylib.db.DatabaseHelper;
import com.flatlaylib.db.HelpPreference;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.BraintreePaymentRes;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.ConnectWithTwitterRes;

import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.service.res.WalletPinRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pinterest.android.pdk.PDKClient;
;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.BranchShortLinkBuilder;
import twitter4j.User;


public class HomeActivity extends FragmentActivity implements OnClickListener, OnLoginCompleteListener {
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    public static int SHARING_CODE = 64206;
    public static PDKClient pdkClient;

    public static Spinner mstatus;
    public static TextView uploadphoto, menuBackTextView, postuploadtagbackTextView;
    public static ImageView photouploadnext, homeImageView, menuRightImageView, crossarrow, postUploadWithTag;
    public static TextView gallery, camera, instagram;
    private FragmentActivity context;
    View viewHeader;

    private ActionBar actionBar;
    private TextView menuRightTextView;
    public static TextView menuTextCartCount;
    public static ImageView menuSearchImageView;
    private LinearLayout menuSearchLayout, menuProfileLayout, menuConnectWithTwitterLayout, menuConnectWithFacebookLayout, menuKikrCreditsLayout, menuActivityLayout, kikerWalletLayout, kikrGuideLayout;
    private LinearLayout menuMyFriendsLayout, menuInviteFriendsLayout, menuCheckInLayout, menuSupportLayout, menuSettingsLayout, menuLogoutoptionLayout, menuProfileOptionLayout;
    private LinearLayout inspirationLayout, discoverLayout, menuDealLayout;
    private LinearLayout menuMyFriendsOptionLayout, menuOrdersLayout, menuChatLayout;
    private LinearLayout menuSettingsOptionLayout, menuDealOptionLayout, menuConnectWithInstagramLayout, menuInterestsLayout;
    private ImageView menuMyFriendsLayoutImageView;
    private ImageView menuProfileLayoutImageView, menuDealImageView, menuSettingsLayoutImageView;
    private ProgressBarDialog progressBarDialog;
    public static ArrayList<Activity> activities = new ArrayList<Activity>();
    public List<ProductFeedItem> list;
    private ArrayList<LinearLayout> layouts = new ArrayList<LinearLayout>();
    public Stack<String> mFragmentStack;
    private Fragment mContent;
    private boolean backPressedToExitOnce = false;
    private HomeActivity homeActivity;
    int PAYPAL_REQUEST_CODE = 0;
    private Braintree braintree;
    private BroadcastReceiver receiver;
    private View twitterView, fbView;
    private ScrollView menuScrollView;
    private ContextMenuView contextMenuBg;
    private boolean isProfile = false;
    private boolean firstTime = false;
    private static ArrayList<Activity> homeActivtyList = new ArrayList<Activity>();
    private TextView totalCredits;
    private int creditsCounter = 0;
    private TextView txtShop;
    private TextView txtFeed;
    private double kikrCredit = 0;
    private List<TextView> textViews = new ArrayList<>();
    private TextView logoutTextView, supportTextView, settingsTextView, inviteTextView, kikrChatTextView, checkInTextView;
    private TextView dealTextview, orderTextView, walletTextView, kikrCreditTextView, viewProfileTextView, kikrGuideTextView, viewSearchTextView;

    LinearLayout tab_layout, cart_tab, search_tab, upload_post_tab, profile_tab, message_tab;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("Activity","HomeActivity");
        setContentView(R.layout.activity_home);
        if (homeActivtyList != null) {
            homeActivtyList.add(this);
        }
        UserPreference.getInstance().setCurrentScreen(Screen.HomeScreen);
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
        registerHomeReceiver();
        Tracker t = ((KikrApp) getApplication()).getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("HomeActivity");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        SharedPreferences settings = getSharedPreferences("CheckFirstTime", 0);

        if (settings.getBoolean("my_first_time", true)) {
            firstTime = true;
            settings.edit().putBoolean("my_first_time", false).commit();
        }

        Product prod = (Product) getIntent().getSerializableExtra("productobj");
        if (prod != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", prod);
            Log.w("HomeActivity","FragmentDiscoverDetail()");
        }

        if (getIntent().getStringExtra("profile_collection") != null) {
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


    double localCount;
    Runnable runnable;

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
        actionBar = getActionBar();
        actionBar.hide();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewHeader = inflater.inflate(R.layout.app_discover_header, null);
        actionBar.setCustomView(viewHeader, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        crossarrow = (ImageView) findViewById(R.id.crossarrow);
        postUploadWithTag = (ImageView) viewHeader.findViewById(R.id.postUploadWithTag);
        mstatus = (Spinner) findViewById(R.id.mstatus);
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
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        menuRightImageView.setOnClickListener(this);
        homeImageView.setOnClickListener(this);
        menuBackTextView.setOnClickListener(this);
        postuploadtagbackTextView.setOnClickListener(this);
        menuSearchImageView.setOnClickListener(this);
        menuRightTextView.setOnClickListener(this);
    }


    public void showActionBar() {
        actionBar.show();
        menuRightImageView.setVisibility(View.VISIBLE);
        menuTextCartCount.setVisibility(View.VISIBLE);
    }

    public void hideActionBar() {
        actionBar.hide();
    }


    private void startUploadPostSection() {

    }

    @Override
    public void onClick(View v) {

    }

    public void initLayout() {
        tab_layout = (LinearLayout) findViewById(R.id.tabLayout);
        cart_tab = (LinearLayout) findViewById(R.id.cart_tab);
        profile_tab = (LinearLayout) findViewById(R.id.profile_tab);
        upload_post_tab = (LinearLayout) findViewById(R.id.upload_post_tab);
        message_tab = (LinearLayout) findViewById(R.id.message_tab);
        search_tab = (LinearLayout) findViewById(R.id.search_tab);
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
        menuInterestsLayout.setVisibility(View.INVISIBLE);
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
        contextMenuBg = (ContextMenuView) findViewById(R.id.contextMenuBg);
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

        cart_tab.setOnClickListener(this);
        search_tab.setOnClickListener(this);
        message_tab.setOnClickListener(this);
        profile_tab.setOnClickListener(this);
        upload_post_tab.setOnClickListener(this);
        CommonUtility.hideSoftKeyboard(this);
    }

    public void setupData() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

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
        crossarrow.setOnClickListener(this);
        postUploadWithTag.setOnClickListener(this);


    }

    public void setUpTextType() {
    }

    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            hideAllFragment();
            addFragment(fragment);
        } else {
            Log.e("MainActivity", "Error in creating fragment.");
        }
    }

    public void showLoginHome() {
        Intent i = new Intent(context, LandingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    private class GetTwitterInfo extends AsyncTask<Void, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarDialog = new ProgressBarDialog(context);
            progressBarDialog.show();
        }

        @Override
        protected User doInBackground(Void... params) {
            return TwitterOAuthActivity.getUserInfo(context);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            progressBarDialog.dismiss();
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
            progressBarDialog = new ProgressBarDialog(context);
            progressBarDialog.show();
        }

        @Override
        protected ArrayList<OauthItem> doInBackground(Void... params) {
            return TwitterOAuthActivity.getFollowerList(context);
        }

        @Override
        protected void onPostExecute(ArrayList<OauthItem> result) {
            super.onPostExecute(result);
            progressBarDialog.dismiss();
            System.out.println("Twitter friend list>>" + result);
            showTwitterFriendList(result);
        }

    }

    private void connectWithTwitter(String userId, String description, String language, String location, String name, String profile_image_url, String screen_name, String status,
                                    String time_zone) {
        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();

        final ConnectWithTwitterApi service = new ConnectWithTwitterApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {

                        progressBarDialog.dismiss();
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

                        progressBarDialog.dismiss();
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

        progressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                service.cancel();
            }
        });
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
        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();
        final FbTwFriendsApi service = new FbTwFriendsApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        progressBarDialog.dismiss();
                        AlertUtils.showToast(context, R.string.alert_connected_with_twitter);
                        UserPreference.getInstance().setIsTwitterConnected(true);

                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        progressBarDialog.dismiss();

                    }
                });
        service.addTwitterFriend(UserPreference.getInstance().getUserID(), data);
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
                    Log.w("link: ",""+link);
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    if (isOther)
                        startActivity(Intent.createChooser(intent, "Share"));
                    else {

                    }
                }
            }
        });
    }


    public void shareImage(final String imageUrl, final boolean isOther, final String shareimagename) {
        AlertUtils.showToast(context, "Please wait...");
        BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(this)

                .addParameters("product_url", imageUrl);
        Log.w("temp","CREATING BRANCH LINK!"+imageUrl);
        Log.w("temp","CREATING BRANCH LINK!"+shareimagename);

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
                    Log.w("shareimagename", "" + shareimagename);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String link = shareimagename;
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    if (isOther) {
                        Log.w("inside if", "inside if"+link);
                        startActivity(Intent.createChooser(intent, "Share"));
                    }
                    else {

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
                    Log.w("Collection Name:",""+collectionname);
                    intent.putExtra(Intent.EXTRA_TEXT, "Check out the " + collectionname + " collections of " + UserPreference.getInstance().getUserName() + " on #FLATLAY " + url);

                    startActivity(Intent.createChooser(intent, "Share"));
                }
            }
        });
    }

    public void addFragment(Fragment fragment) {
        Log.w("HomeActivity","addFragment()");
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Fragment fragment;
            fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            if (fragment instanceof FragmentPostUploadTag) {

                postuploadtagbackTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PostUploadTagDialog welcome = new PostUploadTagDialog(homeActivity);
                        welcome.show();
                    }
                });
            } else {
                hideActionBar();
                tab_layout.setVisibility(View.VISIBLE);
            }


            hideFutter();
            CommonUtility.hideSoftKeyboard(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void backpostuploadtad() {

        showActionBar();
        tab_layout.setVisibility(View.GONE);

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

    }

    public void hideAllFragment() {
        try {
            for (int i = 0; i < mFragmentStack.size(); i++) {
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentByTag(mFragmentStack.peek());
                // transaction.remove(fragment);
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
        LinearLayout footerLayout = (LinearLayout) findViewById(R.id.footer);
        footerLayout.setVisibility(View.VISIBLE);
        LinearLayout footerLayout1 = (LinearLayout) findViewById(R.id.footer_layout);
        TextView textView = (TextView) findViewById(R.id.reloadTextView);
        footerLayout1.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }

    public TextView getReloadFotter() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.footer);
        layout.setVisibility(View.VISIBLE);
        LinearLayout footerLayout = (LinearLayout) findViewById(R.id.footer_layout);
        TextView textView = (TextView) findViewById(R.id.reloadTextView);
        footerLayout.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(Html.fromHtml(getResources().getString(R.string.no_internet)));
        return textView;
    }

    public void hideFutter() {
        LinearLayout footerLayout = (LinearLayout) findViewById(R.id.footer);
        footerLayout.setVisibility(View.GONE);
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
        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();
        final BraintreePaymentApi braintreePaymentApi = new BraintreePaymentApi(
                new ServiceCallback() {
                    @Override
                    public void handleOnSuccess(Object object) {
                        progressBarDialog.dismiss();
                        BraintreePaymentRes braintreePaymentRes = (BraintreePaymentRes) object;
                        String clientToken = braintreePaymentRes.getClient_token();
                        braintree = Braintree.getInstance(context, clientToken);
                        braintree.startPayWithPayPal(context, PAYPAL_REQUEST_CODE);
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        progressBarDialog.dismiss();
                    }
                });
        braintreePaymentApi.getClientToken(UserPreference.getInstance().getUserID(), CommonUtility.getDeviceId(context));
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

    private void registerHomeReceiver() {

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

        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();

        final WalletPinApi service = new WalletPinApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                progressBarDialog.dismiss();
                if (object != null) {
                    WalletPinRes response = (WalletPinRes) object;

                }
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {
                progressBarDialog.dismiss();
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

        progressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                service.cancel();
            }
        });
    }

    public void setimage(Bitmap bitmap) {

    }

    float lastX = 0, lastY = 0;
    View centarlView;
    TextView lableTextView;

    public void showContextMenu(Product product, UiUpdate uiUpdate) {
        contextMenuBg.setVisibility(View.VISIBLE);
        contextMenuBg.setProduct(product, this);
        contextMenuBg.setUiUpdate(uiUpdate);
        showCentralOption();
    }

    public void showContextMenuNoMidCircle(Product product, UiUpdate uiUpdate) {
        contextMenuBg.setVisibility(View.VISIBLE);
        contextMenuBg.setProduct(product, this);
        contextMenuBg.setUiUpdate(uiUpdate);
        contextMenuBg.unSelectIcons();
        showCentralOptionNoMidCircle();
    }

    public void showCentralOptionNoMidCircle() {
        if (centarlView == null) {
            ImageView view = new ImageView(context);
            LayoutParams params = new LayoutParams(
                    (int) getResources().getDimension(R.dimen.menuChildSize),
                    (int) getResources().getDimension(R.dimen.menuChildSize));
            view.setLayoutParams(params);
            centarlView = view;
            contextMenuBg.addView(centarlView);
        }
        centarlView.setVisibility(View.INVISIBLE);
        centarlView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        float widht = centarlView.getMeasuredWidth();
        float height = centarlView.getMeasuredHeight();
        Syso.info("9999999 centarlView>>>>> lastX:" + lastX + ", lastY:"
                + lastY + ", width:" + widht + ", height:" + height);
        centarlView.setX((float) (lastX - (0.3 * widht)));
        centarlView.setY((float) (lastY - 1.2 * height));
        Syso.info("9999999 centarlView 2>>>>> XX:" + centarlView.getX()
                + ", YY:" + centarlView.getY());
        contextMenuBg.setXY(lastX, lastY);
        Syso.info("Touch point>>> lastX:" + lastX + ", lastY:" + lastY
                + " , ViewX:" + centarlView.getX() + ", ViewY:"
                + centarlView.getY() + ",widht:" + widht + ",height:" + height);
    }

    public void hideContextMenu() {
        contextMenuBg.setVisibility(View.GONE);
    }

    public boolean isMenuShowing() {
        return contextMenuBg.getVisibility() == View.VISIBLE ? true : false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        float x = ev.getX();
        float y = ev.getY();
        lastX = x;
        lastY = y;
        if (isMenuShowing()) {
            return contextMenuBg.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    public void showCentralOption() {
        if (centarlView == null) {
            ImageView view = new ImageView(context);
            LayoutParams params = new LayoutParams(
                    (int) getResources().getDimension(R.dimen.menuChildSize),
                    (int) getResources().getDimension(R.dimen.menuChildSize));
            view.setLayoutParams(params);
            view.setImageResource(R.drawable.contextual_origin_pulser);
            centarlView = view;
            contextMenuBg.addView(centarlView);
        }
        centarlView.setVisibility(View.VISIBLE);
        centarlView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        float widht = centarlView.getMeasuredWidth();
        float height = centarlView.getMeasuredHeight();
        Syso.info("9999999 centarlView>>>>> lastX:" + lastX + ", lastY:"
                + lastY + ", width:" + widht + ", height:" + height);

        centarlView.setX((float) (lastX - (0.3 * widht)));
        centarlView.setY((float) (lastY - 1.2 * height));
        Syso.info("9999999 centarlView 2>>>>> XX:" + centarlView.getX()
                + ", YY:" + centarlView.getY());
        contextMenuBg.setXY(lastX, lastY);
        Syso.info("Touch point>>> lastX:" + lastX + ", lastY:" + lastY
                + " , ViewX:" + centarlView.getX() + ", ViewY:"
                + centarlView.getY() + ",widht:" + widht + ",height:" + height);
    }

    public void showLableTextView(String value, float x, float y) {
        if (lableTextView == null) {
            lableTextView = new TextView(context);
            LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lableTextView.setLayoutParams(params);
            lableTextView.setBackgroundResource(R.drawable.black_round_ract);
            lableTextView.setTextColor(Color.WHITE);
            contextMenuBg.addView(lableTextView);
        }
        lableTextView.setText(value);
        lableTextView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int widht = lableTextView.getMeasuredWidth();
        int height = lableTextView.getMeasuredHeight();
        ArcLayout arcLayout = contextMenuBg.getArcLayout();
        if (arcLayout != null) {
            lableTextView.setX(arcLayout.getX() + x - widht / 2);
            lableTextView.setY(arcLayout.getY() + y
                    - getResources().getDimension(R.dimen.lable_distance));
            Syso.info("987654321  Touch point>>> value:" + value + ", x:" + x
                    + ", y:" + y + ", arcLayout.getLeft():"
                    + arcLayout.getLeft() + ", arcLayout.getTop():"
                    + arcLayout.getTop());
        }

        lableTextView.setVisibility(View.VISIBLE);
    }

    public void hideLableTextView() {
        if (lableTextView != null) {
            lableTextView.setVisibility(View.GONE);
        }
    }

    private ProgressBarDialog mProgressBarDialog;

    public void addProductToCart(Product product) {
        mProgressBarDialog = new ProgressBarDialog(context);
        mProgressBarDialog.show();
        final CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                mProgressBarDialog.dismiss();
                if (object != null) {
                    CartRes response = (CartRes) object;
                    UserPreference.getInstance().incCartCount();
                    refreshCartCount();
                    AlertUtils.showToast(context, response.getMessage());
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {
                mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(context, response.getMessage());
                } else {
                    AlertUtils.showToast(context, R.string.invalid_response);
                }
            }
        });
        String fromUserId = product.getFrom_user_id() == null ? "" : product.getFrom_user_id();
        String fromCollection = product.getFrom_collection_id() == null ? "" : product.getFrom_collection_id();
        Log.w("HomeActivity",""+product.getQuantity());
        cartApi.addToCart(UserPreference.getInstance().getUserID(), product.getId(), "1", UserPreference.getInstance().getCartID(), fromUserId, fromCollection, "", "", product.getSelected_values());
        cartApi.execute();
    }

    public void likeInspiration(final Product product, final UiUpdate uiUpdate) {
        mProgressBarDialog = new ProgressBarDialog(context);
        mProgressBarDialog.show();
        Syso.info("123 like id: " + product.getLike_info().getLike_id()
                + ", count:" + product.getLike_info().getLike_count());
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        mProgressBarDialog.dismiss();

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
                        } else {
                            product.getLike_info().setLike_id(likeId);
                            product.getLike_info()
                                    .setLike_count(
                                            (CommonUtility.getInt(product
                                                    .getLike_info()
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
                        mProgressBarDialog.dismiss();
                    }
                });
        if (TextUtils.isEmpty(product.getLike_info().getLike_id()))
        {
            Log.w("HomeActivity","postLike()");
            inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), product.getId(), "product");
        }
        else
        {
            Log.w("HomeActivity","removeLike()");
            inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), product.getLike_info().getLike_id());
        }
        inspirationSectionApi.execute();
    }

    public void setDescription() {

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
                    Log.e("userid identity", UserPreference.getInstance().getUserID());
                    Branch.getInstance().setIdentity(UserPreference.getInstance().getUserID());
                    Log.e("init on start home", "init on start home");

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

    private Uri mDestinationUri;

    public void startCropActivity(@NonNull Uri uri) {
        Log.w("HomeActivity","startCropActivity()");

        mDestinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
        String filePath = Environment.getExternalStorageDirectory()
                + "/temporary_holder.jpg";

        Log.w("startCropActHA","****2: "+uri.toString());


        CropImage.activity(uri)
                .setRequestedSize(800, 800, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setFixAspectRatio(true)
                .setAspectRatio(1,1)
                .setAllowCounterRotation(true)
                .setAllowRotation(true)
                .setOutputUri(mDestinationUri)
                .start(context);

    }

    public void startCropActivityForMedia(@NonNull Uri uri) {
        Log.w("HomeActivity","startCropActivityForMedia()");
        mDestinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
        CropImage.activity(uri)
                .setRequestedSize(700, 700, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setFixAspectRatio(true)
                .setAllowCounterRotation(true)
                .setAllowRotation(true)
                .setAspectRatio(1,1)
                .setOutputUri(mDestinationUri)
                .start(context);

    }


    public void logoutscreen() {
        LogoutDialogWithTab logoutDialog = new LogoutDialogWithTab(context, homeActivity);
        logoutDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MarshmallowPermissions.CAMERA_PERMISSION_REQUEST_CODE) {
            Log.i("Camera permission", "Received response for Camera permission request.");

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

        }
    }
}
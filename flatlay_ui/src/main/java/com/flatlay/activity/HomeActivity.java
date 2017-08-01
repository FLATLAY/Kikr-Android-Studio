package com.flatlay.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
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
import android.widget.Toast;

import com.braintreepayments.api.Braintree;
import com.flatlay.KikrApp;
import com.flatlay.KikrApp.TrackerName;
import com.flatlay.R;
import com.flatlay.dialog.HelpInviteFriendsDialog;
import com.flatlay.dialog.HelpKikrCardDialog;
import com.flatlay.dialog.HelpSocialMediaDialog;
import com.flatlay.dialog.LogoutDialogWithTab;
import com.flatlay.dialog.PostUploadTagDialog;
import com.flatlay.dialog.TwotapMessageDialog;
import com.flatlay.fragment.CartFragmentTab;
import com.flatlay.fragment.FollowingkikrTab;
import com.flatlay.fragment.FragmentActivityCollectionDetail;
import com.flatlay.fragment.FragmentActivityMonths;
import com.flatlay.fragment.FragmentActivityPage;
import com.flatlay.fragment.FragmentAllOrders;
import com.flatlay.fragment.FragmentCardInfo;
import com.flatlay.fragment.FragmentCategories;
import com.flatlay.fragment.FragmentCreditRedeemDetailPage;
import com.flatlay.fragment.FragmentDiscover;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.fragment.FragmentDiscoverNew;
import com.flatlay.fragment.FragmentEditPurchaseItem;
import com.flatlay.fragment.FragmentFeatured;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentInspirationImage;
import com.flatlay.fragment.FragmentInspirationImageTag;
import com.flatlay.fragment.FragmentInspirationPost;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.fragment.FragmentInterestSection;
import com.flatlay.fragment.FragmentKikrCreditMonthBreakdown;
import com.flatlay.fragment.FragmentKikrCreditsScreen;
import com.flatlay.fragment.FragmentKikrGuide;
import com.flatlay.fragment.FragmentKikrWalletCard;
import com.flatlay.fragment.FragmentLearnMore;
import com.flatlay.fragment.FragmentLearnMoreOutsideUS;
import com.flatlay.fragment.FragmentLogout;
import com.flatlay.fragment.FragmentMyFriends;
import com.flatlay.fragment.FragmentPendingCreditDetails;
import com.flatlay.fragment.FragmentPlaceMyOrder;
import com.flatlay.fragment.FragmentPostUploadTab;
import com.flatlay.fragment.FragmentProductBasedOnInspiration;
import com.flatlay.fragment.FragmentProductBasedOnType;
import com.flatlay.fragment.FragmentProductDetailWebView;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.fragment.FragmentPurchaseGuarantee;
import com.flatlay.fragment.FragmentSearch;
import com.flatlay.fragment.FragmentSearchAll;
import com.flatlay.fragment.FragmentSearchProduct;
import com.flatlay.fragment.FragmentSearchResults;
import com.flatlay.fragment.FragmentSearchSubCategories;
import com.flatlay.fragment.FragmentSettings;
import com.flatlay.fragment.FragmentShippingInfo;
import com.flatlay.fragment.FragmentStoreDeals;
import com.flatlay.fragment.FragmentSupport;
import com.flatlay.fragment.FragmentTagList;
import com.flatlay.fragment.FragmentTrackOrder;
import com.flatlay.fragment.FragmentUserCart;
import com.flatlay.fragment.SettingFragmentTab;
import com.flatlay.menu.ArcLayout;
import com.flatlay.menu.ContextMenuView;
import com.flatlay.post_upload.FragmentPostUploadTag;
import com.flatlay.post_upload.ProductSearchTagging;
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
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.Utils;
import com.soundcloud.android.crop.Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import com.yalantis.ucrop.UCrop;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
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
    private FragmentInterestSection fragmentInterestSection;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("Activity","HomeActivity");
        setContentView(R.layout.activity_home);
        if (homeActivtyList != null) {
            homeActivtyList.add(this);
        }
        UserPreference.getInstance().setCurrentScreen(Screen.HomeScreen);


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
        registerHomeReceiver();
        // Syso.info("IP address:"+CommonUtility.getIpAddress(context));

        Tracker t = ((KikrApp) getApplication()).getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("HomeActivity");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        SharedPreferences settings = getSharedPreferences("CheckFirstTime", 0);

        if (settings.getBoolean("my_first_time", true)) {
            firstTime = true;
            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }

        // getKikrCredits();
        Product prod = (Product) getIntent().getSerializableExtra("productobj");
        if (prod != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", prod);
            FragmentDiscoverDetail detail = new FragmentDiscoverDetail(null);
            detail.setArguments(bundle);
            Log.w("HomeActivity","FragmentDiscoverDetail()");
            addFragment(detail);
        }

        if (getIntent().getStringExtra("profile_collection") != null) {
            addFragment(new FragmentProfileView(getIntent().getStringExtra("profile_collection"), "no"));
        }

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

    double localCount;
    Runnable runnable;

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
        //layouts.add(menuMyFriendsLayout);
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

    public void hideBackButtonvalue() {
        menuBackTextView.setVisibility(View.GONE);

        camera.setVisibility(View.GONE);
        gallery.setVisibility(View.GONE);
        instagram.setVisibility(View.GONE);
        mstatus.setVisibility(View.GONE);
        crossarrow.setVisibility(View.GONE);
        homeImageView.setVisibility(View.VISIBLE);
    }

    private void startUploadPostSection() {
        cart_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
        profile_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
        upload_post_tab.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
        message_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
        search_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
        addFragment(new FragmentPostUploadTab());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.cart_tab:
                cart_tab.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                profile_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                upload_post_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                message_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                search_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                addFragment(new FragmentDiscoverNew());
                break;
            case R.id.profile_tab:
                cart_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                profile_tab.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                upload_post_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                message_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                search_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                addFragment(new FragmentProfileView(UserPreference.getInstance().getUserID(), "yes"));
                break;
            case R.id.upload_post_tab:
                Log.w("HomeActivity","Clicked on Upload Post Tab");
               checkPermissions();
                break;
            case R.id.message_tab:
                cart_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                profile_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                upload_post_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                message_tab.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                search_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                addFragment(new FollowingkikrTab());
                break;
            case R.id.search_tab:
                cart_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                profile_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                upload_post_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                message_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                search_tab.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                addFragment(new FragmentSearchProduct());
                break;


            case R.id.homeImageView:
                CommonUtility.hideSoftKeyboard(context);
                loadFragment(new FragmentDiscoverNew());
                break;

            case R.id.crossarrow:
                //  hideBackButtonvalue();
             //   onBackPressed();
                loadFragment(new FragmentDiscoverNew());
                // ontagBack();
                break;
            case R.id.leftTextView:
                // hideBackButton();

//                        PostUploadTagDialog welcome = new PostUploadTagDialog(homeActivity);
//                        welcome.show();

                onBackPressed();
                break;
            case R.id.postuploadtagbackTextView:
                PostUploadTagDialog welcome = new PostUploadTagDialog(homeActivity);
                welcome.show();
                onBackPressed();
                break;
            case R.id.postUploadWithTag:
                Fragment fragmentupload = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
                ((FragmentPostUploadTag) fragmentupload).uploadPost();

                break;


            case R.id.menuRightImageView:
                CommonUtility.hideSoftKeyboard(context);
                Fragment fragment2 = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
                if (fragment2 instanceof CartFragmentTab) {
                    // do nothing
                } else {
                    if (TextUtils.isEmpty(UserPreference.getInstance().getPurchaseId())) {
                        Log.w("HomeActivity","CartFragmentTab()");
                        addFragment(new CartFragmentTab());
                    } else if (!TextUtils.isEmpty(UserPreference.getInstance().getPurchaseId())) {
                        purchaseStatus(UserPreference.getInstance().getPurchaseId());
                    }
                }
                break;
            case R.id.menuSearchImageView:
                CommonUtility.hideSoftKeyboard(context);
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
                if (fragment instanceof FragmentCategories || fragment instanceof FragmentInterestSection || fragment instanceof FragmentSearch || fragment instanceof FragmentSearchResults) {
                    onBackPressed();
                } else {
                    if (fragmentInterestSection == null) {
                        fragmentInterestSection = new FragmentInterestSection();
                        addFragment(fragmentInterestSection);
                    } else {
                        loadFragment(new FragmentInterestSection());
                    }
                }
                break;


            case R.id.menuProfileLayoutImageView:
                openProfileHelpScreen();
                if (menuProfileOptionLayout.getVisibility() == View.VISIBLE) {
                    menuProfileOptionLayout.setVisibility(View.GONE);
                    menuProfileLayoutImageView.setImageResource(R.drawable.ic_menu_arrow_down);
                } else {
                    menuProfileOptionLayout.setVisibility(View.VISIBLE);
                    menuProfileLayoutImageView.setImageResource(R.drawable.ic_menu_arrow);
                }
                break;
            case R.id.menuMyFriendsLayoutImageView:
                openFriendsHelpScreen();
                if (menuMyFriendsOptionLayout.getVisibility() == View.VISIBLE) {
                    menuMyFriendsOptionLayout.setVisibility(View.GONE);
                    menuMyFriendsLayoutImageView.setImageResource(R.drawable.ic_menu_arrow_down);
                } else {
                    menuMyFriendsOptionLayout.setVisibility(View.VISIBLE);
                    menuMyFriendsLayoutImageView.setImageResource(R.drawable.ic_menu_arrow);
                }
                break;
            case R.id.menuSettingsLayoutImageView:
                if (menuSettingsOptionLayout.getVisibility() == View.VISIBLE) {
                    menuSettingsOptionLayout.setVisibility(View.GONE);
                    menuSettingsLayoutImageView.setImageResource(R.drawable.ic_menu_arrow_down);
                } else {
                    menuSettingsOptionLayout.setVisibility(View.VISIBLE);
                    menuSettingsLayoutImageView.setImageResource(R.drawable.ic_menu_arrow);
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            menuScrollView.scrollTo(0, menuScrollView.getBottom());
                        }
                    };
                    handler.postDelayed(runnable, 10);
                }
                break;
            case R.id.menuDealImageView:
                if (menuDealOptionLayout.getVisibility() == View.VISIBLE) {
                    menuDealOptionLayout.setVisibility(View.GONE);
                    menuDealImageView.setImageResource(R.drawable.ic_menu_arrow_down);
                } else {
                    menuDealOptionLayout.setVisibility(View.VISIBLE);
                    menuDealImageView.setImageResource(R.drawable.ic_menu_arrow);
                }
                break;

            case R.id.menuRightText:
                CommonUtility.hideSoftKeyboard(context);
                if (mContent instanceof FragmentInspirationImageTag) {
                    ((FragmentInspirationImageTag) mContent).done();
                    hideBackButton();
                    hideRightButton();
                    onBackPressed();
                } else if (mContent instanceof FragmentCategories) {
                    onBackPressed();
                } else if (mContent instanceof FragmentInspirationImage) {
                    ((FragmentInspirationImage) mContent).goToNext();
                } else if (mContent instanceof FragmentTagList) {
                    ((FragmentTagList) mContent).done();
                    onBackPressed();
                }
                break;

            default:
                break;
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
                Log.w("HomeActivity","Here1");
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
            HelpSocialMediaDialog helpSocialMediaDialog = new HelpSocialMediaDialog(context);
            helpSocialMediaDialog.show();
        }
    }

    private void openFriendsHelpScreen() {
        if (HelpPreference.getInstance().getHelpFriendsSideMenu().equals("yes")) {
            HelpPreference.getInstance().setHelpFriendsSideMenu("no");
            HelpInviteFriendsDialog helpInviteFriendsDialog = new HelpInviteFriendsDialog(context);
            helpInviteFriendsDialog.show();
        }
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
        if (getIntent().getStringExtra("inspiration_id") != null) {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("inspiration_id")) && getIntent().getStringExtra("section").equals("likeinsp") || getIntent().getStringExtra("section").equals("commentinsp")) {
                String id = getIntent().getStringExtra("inspiration_id");
                addFragment(new FragmentInspirationDetail(id));
                cleanActivity();
            } else if (getIntent().getStringExtra("section").equals("follow")) {
                addFragment(new FragmentProfileView(getIntent().getStringExtra("inspiration_id"), "no"));
                cleanActivity();
            }
        } else if (getIntent().getStringExtra("section") != null) {
            if (getIntent().getStringExtra("section").equals("placeorder")) {
                UserPreference.getInstance().setIsNotificationClicked(true);
                String otherdata = getIntent().getStringExtra("otherdata");
                Log.w("HomeActivity","FragmentPlaceMyOrder()");
                addFragment(new FragmentPlaceMyOrder(otherdata, true));
                cleanActivity();
            } else if (getIntent().getStringExtra("section").equals("commission") || getIntent().getStringExtra("section").equals("collectionwithfiveproducts") || getIntent().getStringExtra("section").equals("invite")) {
                addFragment(new FragmentKikrCreditsScreen());
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
            addFragment(new FragmentDiscoverNew());

            if (UserPreference.getInstance().getCheckedIsConnected()
                    && CommonUtility.isOnline(context)) {
                checkStatusOFSocial();
            }
        }
        if (checkInternet()) {
            // getCartList();
        }
    }

    private void showMessage(String otherdata, boolean isLoadOrderDeatil) {
        try {
            TwotapMessageDialog twotapMessageDialog = new TwotapMessageDialog(context, otherdata, isLoadOrderDeatil);
            twotapMessageDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                mFragmentStack.peek());
        if (fragment instanceof FragmentSearchAll) {
            ((FragmentSearchAll) fragment).onWindowFocusChanged(hasFocus);
        }
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

    private void getCartList() {
        CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                CartRes cartRes = (CartRes) object;
                List<Product> productLists = cartRes.getData();
                if (productLists.size() > 0) {
                    UserPreference.getInstance().setCartCount(
                            String.valueOf(productLists.size()));
                    refreshCartCount();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {

            }
        });
        cartApi.getCartList(UserPreference.getInstance().getUserID());
        cartApi.execute();
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
        // .addParameters("$og_description", getResources().getString(R.string.text_join) + "#" + app_title + "@MY" + app_title)
        //.
        // addParameters("$og_image_url", "https://pbs.twimg.com/profile_images/541046285873053696/WmdnfQRo_400x400.png");
        //  .addParameters("$og_image_url", UserPreference.getInstance().getProfilePic());
        //.addParameters("$og_image_url", getResources().getString(R.string.text_join) );
        // Get URL Asynchronously
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
        if (HelpPreference.getInstance().getHelpKikrCards().equals("yes")) {
            HelpPreference.getInstance().setHelpKikrCards("no");
            HelpKikrCardDialog helpKikrCardDialog = new HelpKikrCardDialog(context);
            helpKikrCardDialog.show();
            helpKikrCardDialog.setOnCancelListener(new OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    if (UserPreference.getInstance().getIsCreateWalletPin()) {
                        checkKikrWalletPin();
                    } else {
                        Intent i = new Intent(HomeActivity.this, WalletPinActivity.class);
                        i.putExtra("create", false);
                        startActivityForResult(i, AppConstants.WALLETLIST);
                    }
                }
            });
        } else {
            if (UserPreference.getInstance().getIsCreateWalletPin()) {
                checkKikrWalletPin();
            } else {
                Intent i = new Intent(HomeActivity.this, WalletPinActivity.class);
                i.putExtra("create", false);
                startActivityForResult(i, AppConstants.WALLETLIST);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("HomeActivity","onActivityResult"+CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

        Log.e("home act", requestCode + "wefwe" + resultCode);
        if (requestCode == AppConstants.WALLETLIST && resultCode == RESULT_OK) {
            //Log.w("onActivityResultHA","1");
            loadFragment(new FragmentKikrWalletCard());
        }
        else if (requestCode == AppConstants.REQUEST_CODE_FB_LOGIN && resultCode == RESULT_OK) {
            //Log.w("onActivityResultHA","2");
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
            //Log.w("onActivityResultHA","3");
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
            //Log.w("onActivityResultHA","4");
            ArrayList<FbUser> fbUsers = (ArrayList<FbUser>) data.getSerializableExtra("friend_list");
            if (fbUsers != null && fbUsers.size() > 0) {
                uploadFbFriends(fbUsers);
            } else {
                UserPreference.getInstance().setIsFbConnected(true);

            }
        } else if (requestCode == AppConstants.REQUEST_CODE_TWIT_FRIEND_LIST) {
            //Log.w("onActivityResultHA","5");
            ArrayList<OauthItem> twitUsers = (ArrayList<OauthItem>) data.getSerializableExtra("friend_list");
            showTwitterFriendList(twitUsers);
        } else if (requestCode == PAYPAL_REQUEST_CODE) {
            //Log.w("onActivityResultHA","6");
            if (resultCode == RESULT_OK) {
                System.out.println("in result ok");
                AlertUtils.showToast(context, "Please wait...");
                braintree
                        .addListener(new Braintree.PaymentMethodNonceListener() {
                            public void onPaymentMethodNonce(
                                    String paymentMethodNonce) {
                                // Syso.info("aaaaaaaaaaaaaaa payment nonce::"+paymentMethodNonce);
                                if (checkInternet())
                                    saveCheckout(paymentMethodNonce);
                            }
                        });
                braintree.finishPayWithPayPal(this, resultCode, data);
            }
        } else if (requestCode == AppConstants.REQUEST_CODE_MASKED_WALLET) {
            //Log.w("onActivityResultHA","7");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(
                    mFragmentStack.peek());
            ((FragmentPlaceMyOrder) fragment).onActivityResult(requestCode,
                    resultCode, data);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.w("onActivityResultHA","30");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.w("HomeActivity","Going to handleCropResult2"+resultUri);

                handleCropResult2(data, resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                handleCropError2(data);

            }
        }

        if (requestCode == UCrop.REQUEST_CROP) {
            Log.w("onActivityResultHA","8");
            handleCropResult(data);
        }
        if (requestCode == UCrop.RESULT_ERROR) {
            //Log.w("onActivityResultHA","9");
            handleCropError(data);
        }


        if (requestCode == Crop.REQUEST_CROP) {
            Log.w("onActivityResultHA","10");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentPostUploadTab) fragment).onActivityResult(requestCode,
                    resultCode, data);
        }

        if (requestCode == Crop.REQUEST_PICK) {
            //Log.w("onActivityResultHA","16");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentPostUploadTab) fragment).onActivityResult(requestCode,
                    resultCode, data);
        }
        if (requestCode == AppConstants.REQUEST_CODE_INSTAGRAM) {
            //Log.w("onActivityResultHA","11");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentPostUploadTab) fragment).onActivityResult(requestCode,
                    resultCode, data);
        }

        if (requestCode == AppConstants.REQUEST_CODE_TWIT_TO_FRIEND) {
            //Log.w("onActivityResultHA","12");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentMyFriends) fragment).onActivityResult(requestCode,
                    resultCode, data);
        }
        if (requestCode == AppConstants.REQUEST_CODE_EMAIL_CHANGE) {
            //Log.w("onActivityResultHA","13");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentSettings) fragment).onActivityResult(requestCode,
                    resultCode, data);
        }
        if (requestCode == HomeActivity.SHARING_CODE) {
            //Log.w("onActivityResultHA","14");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(SOCIAL_NETWORK_TAG);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            Log.w("onActivityResultHA","15");

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

        //PDKCLIENT_REQUEST_CODE
        if (requestCode == PDKClient.PDKCLIENT_REQUEST_CODE) {

            PDKClient.getInstance().onOauthResponse(requestCode, resultCode, data);
            System.out.print("ajhsdgfagd fkaj");
        }

        if (requestCode == ProductSearchTagging.PRODUCT_SEARCH_TAG) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentPostUploadTag) fragment).onActivityResult(requestCode,
                    resultCode, data);
            System.out.print("ProductSearchTagging item clicked");
        }


    }

    private void uploadFbFriends(List<FbUser> fbusers) {
        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();
        final FbTwFriendsApi service = new FbTwFriendsApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        progressBarDialog.dismiss();
                        AlertUtils.showToast(context, R.string.alert_connected_with_fb);
                        UserPreference.getInstance().setIsFbConnected(true);

                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        progressBarDialog.dismiss();

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

    private void connectWithFacebook(String id, String email, String gender,
                                     String name, String username, String birthday, String profile_link,
                                     String location) {
        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();

        final ConnectWithFacebookApi service = new ConnectWithFacebookApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {

                        progressBarDialog.dismiss();
                        if (object != null) {
                            getFBFriendList();
                        } else {
                            AlertUtils.showToast(context,
                                    R.string.invalid_response);
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {

                        progressBarDialog.dismiss();
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
        progressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                service.cancel();
            }
        });
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
                //.addParameters("brand", product.getBrand())
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
//							loginPinterest(link,product.getProducturl(),product.getProductimageurl());
                        Intent i = new Intent(context, PinterestLoginActivity.class);
                        i.putExtra("link", link);
                        i.putExtra("link_url", product.getProducturl());
                        i.putExtra("image_url", product.getProductimageurl());
                        startActivity(i);
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
//							loginPinterest(link, url, product.getProductimageurl());
                        Intent i = new Intent(context, PinterestLoginActivity.class);
                        i.putExtra("link", link);
                        i.putExtra("link_url", url);
                        i.putExtra("image_url", product.getProductimageurl());
                        startActivity(i);
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
//							loginPinterest(link,product.getProducturl(),product.getProductimageurl());
                        Intent i = new Intent(context, PinterestLoginActivity.class);
                        i.putExtra("link", link);
                        i.putExtra("link_url", imageUrl);
                        i.putExtra("image_url", imageUrl);
                        startActivity(i);
                    }
                } else {
                    Log.i("Branch", "Got a Branch URL 3 " + url);
                    Log.w("shareimagename", "" + shareimagename);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String link = shareimagename;
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    if (isOther) {
                        Log.w("inside if", "inside if");
                        startActivity(Intent.createChooser(intent, "Share"));
                    }
                    else {
                        Log.w("inside else", "inside else");
//							loginPinterest(link, url, product.getProductimageurl());
                        Log.w("inside else", ""+link);
                        Log.w("inside else", ""+url);
                        Log.w("inside else", ""+imageUrl);
                        Intent i = new Intent(context, PinterestLoginActivity.class);
                        i.putExtra("link", link);
                        i.putExtra("link_url", url);
                        i.putExtra("image_url", imageUrl);
                        startActivity(i);
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
                    Log.w("Collection Name:",""+collectionname);
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
        Log.w("HomeActivity","addFragment()");
        //Thread.dumpStack();
        try {

            if (fragment instanceof FragmentPostUploadTab || fragment instanceof FragmentPostUploadTag) {
                showActionBar();
                tab_layout.setVisibility(View.GONE);
            } else {
                hideActionBar();
                tab_layout.setVisibility(View.VISIBLE);
            }

            if (fragment instanceof FragmentDiscoverNew) {
                cart_tab.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                profile_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                upload_post_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                message_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                search_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
            }
            if (fragment instanceof CartFragmentTab) {
                cart_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                profile_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                upload_post_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                message_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                search_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
            }

            mContent = fragment;
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            System.out.println("1111 added>>" + mContent.toString());
            if (mFragmentStack.size() > 0) {
                Fragment currentFragment = getSupportFragmentManager()
                        .findFragmentByTag(mFragmentStack.peek());
                if (currentFragment != null)
                    transaction.hide(currentFragment);
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

            if (fragment instanceof FragmentDiscoverDetail || fragment instanceof FragmentProductDetailWebView || fragment instanceof FragmentEditPurchaseItem
                    || fragment instanceof FragmentPlaceMyOrder || fragment instanceof FragmentInspirationSection ||
                    fragment instanceof FragmentCardInfo || fragment instanceof FragmentShippingInfo || fragment instanceof FragmentSearchSubCategories
                    || fragment instanceof FragmentProductBasedOnType || fragment instanceof FragmentKikrCreditMonthBreakdown ||
                    fragment instanceof FragmentCreditRedeemDetailPage || fragment instanceof FragmentSupport || fragment instanceof FragmentProductBasedOnInspiration) {
                showActionBar();
                menuRightImageView.setVisibility(View.VISIBLE);
                menuTextCartCount.setVisibility(View.VISIBLE);
                crossarrow.setVisibility(View.GONE);
                camera.setVisibility(View.GONE);
            }

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

//                showActionBar();
//                tab_layout.setVisibility(View.GONE);
            } else {
                hideActionBar();
                tab_layout.setVisibility(View.VISIBLE);
            }


            hideFutter();
            CommonUtility.hideSoftKeyboard(this);
            if (mFragmentStack.size() == 1) {

                if (fragment instanceof FragmentDiscoverNew) {
                    if (backPressedToExitOnce) {
                        finish();
                    } else {
                        this.backPressedToExitOnce = true;
                        AlertUtils.showToast(context, "Press again to exit");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                backPressedToExitOnce = false;
                            }
                        }, 2000);
                    }
                } else {
                    hideAllFragment();
                    addFragment(new FragmentDiscoverNew());
                }
            } else {


                if (fragment instanceof FragmentPostUploadTag) {
                    //  gallery.setVisibility(View.VISIBLE);
                    postuploadtagbackTextView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PostUploadTagDialog welcome = new PostUploadTagDialog(homeActivity);
                            welcome.show();
                        }
                    });

                } else {
                    removeFragment();
                    super.onBackPressed();

                }
            }
            fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            if (fragment instanceof FragmentProductDetailWebView || fragment instanceof FragmentFeatured || fragment instanceof FragmentEditPurchaseItem || fragment instanceof FragmentDiscoverDetail
                    ||fragment instanceof FragmentCreditRedeemDetailPage || fragment instanceof FragmentProductBasedOnInspiration ||
                    fragment instanceof FragmentProductBasedOnType) {
                crossarrow.setVisibility(View.GONE);
                camera.setVisibility(View.GONE);
                showActionBar();
            } else if (fragment instanceof CartFragmentTab || fragment instanceof FragmentUserCart) {
                hideActionBar();
            } else if (fragment instanceof FragmentPostUploadTab) {
                cart_tab.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                profile_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                upload_post_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                message_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                search_tab.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                hideActionBar();
                tab_layout.setVisibility(View.VISIBLE);
                removeFragment();
                super.onBackPressed();
                addFragment(new FragmentDiscoverNew());
                //  addFragment(new FragmentDiscoverNew());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


//    public void ontagBack() {
//        try {
//
//            left.closeMenu();
//            hideFutter();
//            if (mFragmentStack.size() == 1) {
//                Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
//                if (fragment instanceof FragmentPostUploadTab) {
//                    if (backPressedToExitOnce) {
//                        finish();
//                    } else {
//                        this.backPressedToExitOnce = true;
//                        AlertUtils.showToast(context, "Press again to exit");
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                backPressedToExitOnce = false;
//                            }
//                        }, 2000);
//                    }
//                } else {
//                   // hideAllFragment();
//                    addFragment(new FragmentPostUploadTab());
//                }
//            } else {
//               // removeFragment();
//                super.onBackPressed();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//


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
        try {
            // remove the current fragment from the stack.
            String fold = mFragmentStack.pop();
            Fragment f2 = getSupportFragmentManager().findFragmentByTag(fold);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // get fragment that is to be shown (in our case fragment1).
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            // This time I set an animation with no fade in, so the user doesn't
            // wait for the animation in back press
            // transaction.setCustomAnimations(R.anim.slide_right_in,
            // R.anim.slide_right_out);
            // We must use the show() method.

            mContent = fragment;
            checkBackButton(fragment);
            checkRightButton(fragment);
            if (fragment instanceof FragmentDiscover
                    && f2 instanceof FragmentInterestSection) {
                if (fragment != null)
                    transaction.show(fragment);
                transaction.commit();
                if (fragment != null)
                    ((FragmentDiscover) fragment).refresh();
                // loadFragment(new FragmentDiscover());
            } else if (fragment instanceof FragmentPostUploadTab) {
                if (FragmentPostUploadTab.tabposition == 0) {
                    // gallery.setVisibility(View.VISIBLE);
                    mstatus.setVisibility(View.VISIBLE);
                    crossarrow.setVisibility(View.VISIBLE);
                    menuBackTextView.setVisibility(View.GONE);

                    camera.setVisibility(View.GONE);
                    instagram.setVisibility(View.GONE);
                    homeImageView.setVisibility(View.GONE);
                    menuRightImageView.setVisibility(View.GONE);
                    uploadphoto.setVisibility(View.GONE);
                    transaction.show(fragment);
                    transaction.commit();
                } else if (FragmentPostUploadTab.tabposition == 1) {
                    gallery.setVisibility(View.GONE);
                    mstatus.setVisibility(View.GONE);
                    crossarrow.setVisibility(View.VISIBLE);
                    menuBackTextView.setVisibility(View.GONE);

                    camera.setVisibility(View.VISIBLE);
                    instagram.setVisibility(View.GONE);
                    homeImageView.setVisibility(View.GONE);
                    menuRightImageView.setVisibility(View.GONE);
                    uploadphoto.setVisibility(View.GONE);
                    transaction.show(fragment);
                    transaction.commit();
                } else if (FragmentPostUploadTab.tabposition == 2) {
                    gallery.setVisibility(View.GONE);
                    mstatus.setVisibility(View.GONE);
                    crossarrow.setVisibility(View.VISIBLE);
                    menuBackTextView.setVisibility(View.GONE);

                    camera.setVisibility(View.GONE);
                    instagram.setVisibility(View.VISIBLE);
                    homeImageView.setVisibility(View.GONE);
                    menuRightImageView.setVisibility(View.GONE);
                    uploadphoto.setVisibility(View.GONE);
                    transaction.show(fragment);
                    transaction.commit();
                }

            } else {
                if (fragment != null)
                    transaction.show(fragment);
                transaction.commit();
            }

            if (fragment instanceof FragmentPostUploadTab || fragment instanceof FragmentPostUploadTag) {
                showActionBar();
                tab_layout.setVisibility(View.GONE);
            } else {
                hideActionBar();
                tab_layout.setVisibility(View.VISIBLE);
            }

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

    private void saveCheckout(String paymentMethodNonce) {
        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();
        final BraintreePaymentApi braintreePaymentApi = new BraintreePaymentApi(
                new ServiceCallback() {
                    @Override
                    public void handleOnSuccess(Object object) {
                        progressBarDialog.dismiss();
                        AlertUtils.showToast(context, R.string.payment_success_text);
                        createCart();
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        progressBarDialog.dismiss();
                        AlertUtils.showToast(context, R.string.payment_fail_text);
                    }
                });
        braintreePaymentApi.saveCheckout(UserPreference.getInstance()
                        .getUserID(), CommonUtility.getDeviceId(context),
                paymentMethodNonce);
        braintreePaymentApi.execute();

    }

    public void createCart() {
//		progressBarDialog = new ProgressBarDialog(context);
//		progressBarDialog.show();
        final CartApi cartApi = new CartApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
//				progressBarDialog.dismiss();
                CartRes cartRes = (CartRes) object;
                UserPreference.getInstance().setCartID(cartRes.getCart_id());
//				loadFragment(new FragmentDiscover());
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {
//				progressBarDialog.dismiss();
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

    public void hideBackButton() {
        postuploadtagbackTextView.setVisibility(View.GONE);
        menuBackTextView.setVisibility(View.GONE);
        menuRightImageView.setVisibility(View.VISIBLE);
        camera.setVisibility(View.GONE);
        gallery.setVisibility(View.GONE);
        menuTextCartCount.setVisibility(View.VISIBLE);
        mstatus.setVisibility(View.GONE);
        photouploadnext.setVisibility(View.GONE);
        instagram.setVisibility(View.GONE);
        crossarrow.setVisibility(View.GONE);
        postUploadWithTag.setVisibility(View.GONE);
        homeImageView.setVisibility(View.VISIBLE);
    }

    private void checkBackButton(Fragment fragment) {
        if (fragment instanceof FragmentDiscoverDetail
                || fragment instanceof FragmentProductBasedOnType
                || fragment instanceof FragmentShippingInfo
                || fragment instanceof FragmentEditPurchaseItem
                || fragment instanceof FragmentProductDetailWebView
                || fragment instanceof FragmentCardInfo
                || fragment instanceof FragmentPlaceMyOrder
                || fragment instanceof FragmentUserCart
                || fragment instanceof FragmentSearch
                || fragment instanceof FragmentKikrWalletCard
                || fragment instanceof FragmentActivityPage
                || fragment instanceof FragmentActivityCollectionDetail
                || fragment instanceof FragmentTagList
                || fragment instanceof FragmentInspirationImage
                || fragment instanceof FragmentInspirationPost
                || fragment instanceof FragmentInspirationDetail
                || fragment instanceof FragmentProductBasedOnInspiration
                || fragment instanceof FragmentTrackOrder
                || fragment instanceof FragmentAllOrders
                || fragment instanceof FragmentProfileView
                || fragment instanceof FragmentLearnMore
                || fragment instanceof FragmentSearchResults
                || fragment instanceof FragmentInspirationImageTag
                || fragment instanceof FragmentPurchaseGuarantee
                || fragment instanceof FragmentLearnMoreOutsideUS
                || fragment instanceof FragmentStoreDeals
                || fragment instanceof FragmentInterestSection
                || fragment instanceof FragmentCategories
                || fragment instanceof FragmentInspirationSection
                || fragment instanceof FragmentKikrCreditMonthBreakdown
                || fragment instanceof FragmentActivityMonths
                || fragment instanceof FragmentPendingCreditDetails
                || fragment instanceof FragmentCreditRedeemDetailPage
                || fragment instanceof FragmentKikrGuide

                || fragment instanceof FragmentSearchSubCategories
                || fragment instanceof FragmentSearchAll
                || fragment instanceof FragmentPostUploadTab
                || fragment instanceof SettingFragmentTab
                || fragment instanceof FragmentLogout
                || fragment instanceof FragmentPurchaseGuarantee
                || fragment instanceof FragmentSupport
                || fragment instanceof CartFragmentTab
                || fragment instanceof FragmentUserCart
                || fragment instanceof FragmentKikrWalletCard
                || fragment instanceof FragmentKikrCreditsScreen
                || fragment instanceof FragmentAllOrders
                ) {

            showBackButton();
        } else {
            hideBackButton();
        }
        if (fragment instanceof FragmentProfileView && isProfile) {

            isProfile = false;
            hideBackButton();
        } else if (fragment instanceof FragmentPostUploadTag) {
            postuploadtagbackTextView.setVisibility(View.VISIBLE);
        }

    }

    private void checkRightButton(Fragment fragment) {
        if (fragment instanceof FragmentInspirationImageTag) {
            showRightButton();
            menuRightTextView.setText("Done");
        } else if (fragment instanceof FragmentInspirationImage) {
            showRightButton();
            menuRightTextView.setText("Next");
        } else if (fragment instanceof FragmentTagList) {
            showRightButton();
            menuRightTextView.setText("Done");
        } else if (fragment instanceof FragmentCategories) {
            showRightButton();
            menuRightTextView.setText("Done");
        } else {
            hideRightButton();
        }
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
                        loadFragment(new FragmentDiscoverNew());
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

        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();

        final WalletPinApi service = new WalletPinApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                progressBarDialog.dismiss();
                if (object != null) {
                    WalletPinRes response = (WalletPinRes) object;
                    if (!TextUtils.isEmpty(response.getPin_created()) && response.getPin_created().equals("yes")) {
                        UserPreference.getInstance().setIsCreateWalletPin(false);
                        Intent i = new Intent(context, WalletPinActivity.class);
                        i.putExtra("create", false);
                        startActivityForResult(i, AppConstants.WALLETLIST);
                    } else {
                        Intent i = new Intent(context, WalletPinActivity.class);
                        i.putExtra("create", true);
                        startActivityForResult(i, AppConstants.WALLETLIST);
                    }
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
        if (mContent instanceof FragmentInspirationImage) {
            ((FragmentInspirationImage) mContent).setImage(bitmap);
        }
    }

    // ================================= Context Menu==========================================
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
            //view.setImageResource(R.drawable.contextual_origin_pulser);
            centarlView = view;
            contextMenuBg.addView(centarlView);
        }
        centarlView.setVisibility(View.INVISIBLE);
        centarlView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        float widht = centarlView.getMeasuredWidth();
        float height = centarlView.getMeasuredHeight();
        Syso.info("9999999 centarlView>>>>> lastX:" + lastX + ", lastY:"
                + lastY + ", width:" + widht + ", height:" + height);
        // centarlView.setX((float) (lastX-(widht/3)));
        // centarlView.setY((float) (lastY-1.1*height));

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
//		Syso.info("1111111111111111   Dispatch>>>>>  X :" + x + ", y:" + y);
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
        // centarlView.setX((float) (lastX-(widht/3)));
        // centarlView.setY((float) (lastY-1.1*height));

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
        if (mContent instanceof FragmentInspirationDetail) {
            ((FragmentInspirationDetail) mContent).setDescription();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
        checkForUpdates();
    }

    private void checkForCrashes() {
        //CrashManager.register(this, "3ec40bcb125d3ad0f770b3d424bcf913");
    }

    private void checkForUpdates() {
        // Remove this for store builds!
       // UpdateManager.register(this, "3ec40bcb125d3ad0f770b3d424bcf913");
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

    public void purchaseStatus(String purchase_id) {
        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                progressBarDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    if (jsonObject.getString("message").equalsIgnoreCase("done")) {
                        UserPreference.getInstance().setPurchaseId("");
                        Log.w("HomeActivity","FragmentUserCart()");
                        addFragment(new FragmentUserCart());
                    } else {
                        AlertUtils.showToast(context, "Your previous order is still being processed. You should receive confirmation on the status shortly.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                progressBarDialog.dismiss();
            }
        });
        twoTapApi.purchaseStatus(purchase_id);
        twoTapApi.execute();
    }

    public void followUser(String user_id) {
        mProgressBarDialog = new ProgressBarDialog(context);
        mProgressBarDialog.show();

        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                mProgressBarDialog.dismiss();
                Syso.info("In handleOnSuccess>>" + object);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mProgressBarDialog.dismiss();
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

        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                followUserApi.cancel();
            }
        });
    }

    public void unFollowUser(String user_id) {
        mProgressBarDialog = new ProgressBarDialog(context);
        mProgressBarDialog.show();

        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                mProgressBarDialog.dismiss();

                Syso.info("In handleOnSuccess>>" + object);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mProgressBarDialog.dismiss();
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

        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                followUserApi.cancel();
            }
        });
    }

    //  cropping functions

    private Uri mDestinationUri;

    public void startCropActivity(@NonNull Uri uri) {
        Log.w("HomeActivity","startCropActivity()");

        mDestinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
        String filePath = Environment.getExternalStorageDirectory()
                + "/temporary_holder.jpg";

        Log.w("startCropActHA","****2: "+uri.toString());

        /*
        UCrop uCrop = UCrop.of(uri, mDestinationUri);
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);
        uCrop.withMaxResultSize(500,500);
        uCrop.start(context);
        */

        CropImage.activity(uri)
                .setRequestedSize(800, 800, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                //.setMaxCropResultSize(2000, 2000)
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
                //.setMaxCropResultSize(2000, 2000)
                .setFixAspectRatio(true)
                .setAllowCounterRotation(true)
                .setAllowRotation(true)
                .setAspectRatio(1,1)
                .setOutputUri(mDestinationUri)
                .start(context);

    }

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
            Log.w("handleCropResultHA","Here");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentPostUploadTab) fragment).onActivityResult(UCrop.REQUEST_CROP, RESULT_OK
                    , result);
        } else
            AlertUtils.showToast(HomeActivity.this, R.string.toast_cannot_retrieve_cropped_image);
//        final Uri resultUri = UCrop.getOutput(result);
//        if (resultUri != null) {
//            ResultActivity.startWithUri(SampleActivity.this, resultUri);
//        } else {
//            Toast.makeText(SampleActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
//        }
    }


    private void handleCropResult2(@NonNull Intent result, Uri resultUri) {

        if (result != null) {
            Log.w("HomeActivity","handleCropResult2()");
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
            ((FragmentPostUploadTab) fragment).onActivityResult(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK
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
        Toast.makeText(HomeActivity.this, "handleCropError2"+result, Toast.LENGTH_LONG).show();
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

}
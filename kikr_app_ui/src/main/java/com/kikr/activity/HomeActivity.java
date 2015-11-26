package com.kikr.activity;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.BranchShortLinkBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnDrawerStateChangeListener;
import net.simonvt.menudrawer.Position;

import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.User;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.braintreepayments.api.Braintree;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kikr.KikrApp;
import com.kikr.KikrApp.TrackerName;
import com.kikr.R;
import com.kikr.dialog.CreateAccountDialog;
import com.kikr.dialog.HelpInviteFriendsDialog;
import com.kikr.dialog.HelpKikrCardDialog;
import com.kikr.dialog.HelpSocialMediaDialog;
import com.kikr.dialog.LogoutDialog;
import com.kikr.dialog.TwotapMessageDialog;
import com.kikr.fragment.FragmentActivityCollectionDetail;
import com.kikr.fragment.FragmentActivityMonths;
import com.kikr.fragment.FragmentActivityPage;
import com.kikr.fragment.FragmentAllOrders;
import com.kikr.fragment.FragmentCardInfo;
import com.kikr.fragment.FragmentDeals;
import com.kikr.fragment.FragmentDiscover;
import com.kikr.fragment.FragmentDiscoverDetail;
import com.kikr.fragment.FragmentDiscoverNew;
import com.kikr.fragment.FragmentEditPurchaseItem;
import com.kikr.fragment.FragmentInspirationDetail;
import com.kikr.fragment.FragmentInspirationImage;
import com.kikr.fragment.FragmentInspirationImageTag;
import com.kikr.fragment.FragmentInspirationPost;
import com.kikr.fragment.FragmentInspirationSection;
import com.kikr.fragment.FragmentInterestSection;
import com.kikr.fragment.FragmentKikrCreditsScreen;
import com.kikr.fragment.FragmentKikrWalletCard;
import com.kikr.fragment.FragmentLearnMore;
import com.kikr.fragment.FragmentLearnMoreOutsideUS;
import com.kikr.fragment.FragmentMyFriends;
import com.kikr.fragment.FragmentPlaceMyOrder;
import com.kikr.fragment.FragmentProductBasedOnInspiration;
import com.kikr.fragment.FragmentProductBasedOnType;
import com.kikr.fragment.FragmentProductDetailWebView;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.fragment.FragmentPurchaseGuarantee;
import com.kikr.fragment.FragmentSearch;
import com.kikr.fragment.FragmentSearchResults;
import com.kikr.fragment.FragmentSettings;
import com.kikr.fragment.FragmentShippingInfo;
import com.kikr.fragment.FragmentStoreDeals;
import com.kikr.fragment.FragmentSupport;
import com.kikr.fragment.FragmentTagList;
import com.kikr.fragment.FragmentTrackOrder;
import com.kikr.fragment.FragmentUserCart;
import com.kikr.ibeacon.BeaconMonitorService;
import com.kikr.menu.ArcLayout;
import com.kikr.menu.ContextMenuView;
import com.kikr.sessionstore.SessionStore;
import com.kikr.twitter.OauthItem;
import com.kikr.twitter.TwitterOAuthActivity;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants;
import com.kikr.utility.AppConstants.Screen;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.UiUpdate;
import com.kikrlib.api.BraintreePaymentApi;
import com.kikrlib.api.CartApi;
import com.kikrlib.api.ConnectWithFacebookApi;
import com.kikrlib.api.ConnectWithTwitterApi;
import com.kikrlib.api.FbTwFriendsApi;
import com.kikrlib.api.GetConnectedWithSocialApi;
import com.kikrlib.api.GetUUIDApi;
import com.kikrlib.api.InspirationSectionApi;
import com.kikrlib.api.KikrCreditsApi;
import com.kikrlib.api.TwoTapApi;
import com.kikrlib.api.UpdateCurrentScreenApi;
import com.kikrlib.api.WalletPinApi;
import com.kikrlib.bean.FbUser;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProductFeedItem;
import com.kikrlib.bean.TwitterFriendList;
import com.kikrlib.bean.Uuid;
import com.kikrlib.db.DatabaseHelper;
import com.kikrlib.db.HelpPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.db.dao.UuidDAO;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.BraintreePaymentRes;
import com.kikrlib.service.res.CartRes;
import com.kikrlib.service.res.ConnectWithFacebookRes;
import com.kikrlib.service.res.ConnectWithTwitterRes;
import com.kikrlib.service.res.GetConnectedWithSocialRes;
import com.kikrlib.service.res.GetUUIDRes;
import com.kikrlib.service.res.InspirationRes;
import com.kikrlib.service.res.KikrCreditsRes;
import com.kikrlib.service.res.WalletPinRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.StringUtils;
import com.kikrlib.utils.Syso;
import com.personagraph.api.PGAgent;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.soundcloud.android.crop.Crop;

public class HomeActivity extends FragmentActivity implements OnClickListener {
	private FragmentActivity context;
	private MenuDrawer left;
	private ActionBar actionBar;
	private TextView menuBackTextView, menuRightTextView;
	public static TextView menuTextCartCount;
	private ImageView menuLeftImageView, menuRightImageView, homeImageView,menuSearchImageView;
	private LinearLayout menuProfileLayout, menuConnectWithTwitterLayout,menuConnectWithFacebookLayout, menuKikrCreditsLayout,menuActivityLayout, kikerWalletLayout;
	private LinearLayout menuMyFriendsLayout, menuInviteFriendsLayout,menuCheckInLayout, menuSupportLayout, menuSettingsLayout,menuLogoutoptionLayout, menuProfileOptionLayout;
	private LinearLayout inspirationLayout, discoverLayout, menuDealLayout;
	private LinearLayout menuMyFriendsOptionLayout,menuOrdersLayout, menuChatLayout;
	private LinearLayout menuSettingsOptionLayout, menuDealOptionLayout,menuConnectWithInstagramLayout, menuInterestsLayout;
	private ImageView menuMyFriendsLayoutImageView;
	private ImageView menuProfileLayoutImageView, menuDealImageView,menuSettingsLayoutImageView;
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
	private FragmentSearch fragmentSearch;
	private ContextMenuView contextMenuBg;
	private boolean isProfile = false;
	private boolean firstTime = false;
	private static ArrayList<Activity> homeActivtyList = new ArrayList<Activity>();
	private TextView totalCredits;
	private int creditsCounter = 0;
	private TextView txtShop;
	private TextView txtFeed;
	private double kikrCredit = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (homeActivtyList!=null) {
			homeActivtyList.add(this);
		}
		UserPreference.getInstance().setCurrentScreen(Screen.HomeScreen);
		context = this;
		homeActivity = this;
		mFragmentStack = new Stack<String>();
		setMenuDrawer();
		setActionBar();
		if (UserPreference.getInstance().getIsCheckBluetooth())
			verifyBluetooth(false);
		if (CommonUtility.isOnline(context))
			getUUIDList();
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
	
		getKikrCredits();
		Product prod = (Product) getIntent().getSerializableExtra("productobj");
		if(prod!= null) {
			Bundle bundle=new Bundle();
			bundle.putSerializable("data",prod);
			FragmentDiscoverDetail detail=new FragmentDiscoverDetail(null);
			detail.setArguments(bundle);
			addFragment(detail);
		}
		
		if(getIntent().getStringExtra("profile_collection") != null) {
			addFragment(new FragmentProfileView(getIntent().getStringExtra("profile_collection"), "no"));
		}
		PGAgent.shareExternalUserId(UserPreference.getInstance().getUserID());
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
		runnable =  new Runnable() {
			@Override
			public void run() {
				totalCredits.setText((int)localCount+" Cr");
				localCount+=kikrCredit/20;
				if(localCount<=kikrCredit)
					handler.postDelayed(runnable, 50);
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

	private void setMenuDrawer() {
		left = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.LEFT,MenuDrawer.MENU_DRAG_WINDOW);
		left.setContentView(R.layout.activity_home);
		left.setMenuView(R.layout.menu_left);
		left.setOnDrawerStateChangeListener(new OnDrawerStateChangeListener() {
			
			@Override
			public void onDrawerStateChange(int oldState, int newState) {
				if(newState==MenuDrawer.STATE_OPEN){
					showKikrCredit();
				}
			}
			
			@Override
			public void onDrawerSlide(float openRatio, int offsetPixels) {
				
			}
		});
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
		layouts.add(menuMyFriendsOptionLayout);
		layouts.add(menuOrdersLayout);
		layouts.add(menuProfileOptionLayout);
		layouts.add(menuChatLayout);
	}

	private void setActionBar() {
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.app_discover_header, null);
		actionBar.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		menuBackTextView = (TextView) view.findViewById(R.id.leftTextView);
		menuRightTextView = (TextView) view.findViewById(R.id.menuRightText);
		menuLeftImageView = (ImageView) view.findViewById(R.id.menuLeftImageView);
		menuRightImageView = (ImageView) view.findViewById(R.id.menuRightImageView);
		homeImageView = (ImageView) view.findViewById(R.id.homeImageView);
		menuSearchImageView = (ImageView) view.findViewById(R.id.menuSearchImageView);
		menuTextCartCount = (TextView) view.findViewById(R.id.txtCartCount);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		menuLeftImageView.setOnClickListener(this);
		menuRightImageView.setOnClickListener(this);
		homeImageView.setOnClickListener(this);
		menuBackTextView.setOnClickListener(this);
		menuSearchImageView.setOnClickListener(this);
		menuRightTextView.setOnClickListener(this);
	}

	private void handleLeftButtonClick() {
		boolean drawerOpen = left.isSelected();
		if (drawerOpen) 
			left.closeMenu();
		else
			left.openMenu();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.homeImageView:
			CommonUtility.hideSoftKeyboard(context);
			loadFragment(new FragmentDiscoverNew());
			break;
		case R.id.menuLeftImageView:
			CommonUtility.hideSoftKeyboard(context);
			handleLeftButtonClick();
			break;
		case R.id.menuRightImageView:
			CommonUtility.hideSoftKeyboard(context);
			Fragment fragment2 = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
			if (fragment2 instanceof FragmentUserCart) {
				// do nothing
			} else {
				if(TextUtils.isEmpty(UserPreference.getInstance().getPurchaseId())){
					addFragment(new FragmentUserCart());
				}else if(!TextUtils.isEmpty(UserPreference.getInstance().getPurchaseId())){
					purchaseStatus(UserPreference.getInstance().getPurchaseId());
				}
			}
			break;
		case R.id.menuSearchImageView:
			CommonUtility.hideSoftKeyboard(context);
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
			if (fragment instanceof FragmentSearch || fragment instanceof FragmentSearchResults) {
				onBackPressed();
			} else {
				if (fragmentSearch == null){
					fragmentSearch = new FragmentSearch();
					addFragment(fragmentSearch);
				}else{
					loadFragment(new FragmentSearch());
				}
			}
			break;
		case R.id.menuCheckInLayout:
			left.closeMenu();
			changeBackground(menuCheckInLayout);
			verifyBluetooth(true);
			break;
		case R.id.menuOrdersLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(menuOrdersLayout);
				loadFragment(new FragmentAllOrders());
			}
			break;
		case R.id.menuInviteFriendsLayout:
			if(firstTime) {
				openFriendsHelpScreen();
				if (checkInternet()) {
					left.closeMenu();
					changeBackground(menuInviteFriendsLayout);
					inviteFriends();
				}
			} else {
				if (checkInternet()) {
					left.closeMenu();
					changeBackground(menuInviteFriendsLayout);
					inviteFriends();
				}
			}
			
			break;
		case R.id.menuInterestsLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(menuInterestsLayout);
				loadFragment(new FragmentInterestSection());
			}
			break;
		case R.id.menuKikrCreditsLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(menuKikrCreditsLayout);
				loadFragment(new FragmentKikrCreditsScreen());
			}
			break;
		case R.id.menuMyFriendsLayout:
			if(firstTime) {
				openFriendsHelpScreen();
				if (menuMyFriendsOptionLayout.getVisibility() == View.VISIBLE) {
					menuMyFriendsOptionLayout.setVisibility(View.GONE);
					menuMyFriendsLayoutImageView.setImageResource(R.drawable.ic_menu_arrow_down);
				} else {
					menuMyFriendsOptionLayout.setVisibility(View.VISIBLE);
					menuMyFriendsLayoutImageView.setImageResource(R.drawable.ic_menu_arrow);
				}
				if (checkInternet()) {
					changeBackground(menuMyFriendsLayout);
					loadFragment(new FragmentMyFriends());

					left.closeMenu();
				}
			} else {
				if (checkInternet()) {
					left.closeMenu();
					changeBackground(menuMyFriendsLayout);
					loadFragment(new FragmentMyFriends());
				}
			}
			break;
		case R.id.menuActivityLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(menuActivityLayout);
				loadFragment(new FragmentActivityMonths());
			}
			break;
		case R.id.discoverLayout:
			left.closeMenu();
			changeBackground(discoverLayout);
			loadFragment(new FragmentDiscoverNew());
			break;
		case R.id.inspirationLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(inspirationLayout);
				loadFragment(new FragmentInspirationSection(true,UserPreference.getInstance().getUserID()));
			}
			break;
		case R.id.menuProfileLayout:
			if (checkInternet()) {
			 if(firstTime) {
					openProfileHelpScreen();
						left.closeMenu();
						changeBackground(menuProfileLayout);
						isProfile = true;
						loadFragment(new FragmentProfileView(UserPreference.getInstance().getUserID(), "yes"));
			 } else {
					left.closeMenu();
					changeBackground(menuProfileLayout);
					isProfile = true;
					loadFragment(new FragmentProfileView(UserPreference.getInstance().getUserID(), "yes"));
			 }
			}
			break;
		case R.id.menuSettingsLayout:
			if (checkInternet()) {
				if(firstTime) {
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
					left.closeMenu();
					changeBackground(menuSettingsLayout);
					loadFragment(new FragmentSettings());
				} else {
						left.closeMenu();
						changeBackground(menuSettingsLayout);
						loadFragment(new FragmentSettings());
				}
			}
			
			break;
		case R.id.menuDealLayout:
			if (checkInternet()) {
				if(firstTime) {
					if (menuDealOptionLayout.getVisibility() == View.VISIBLE) {
						menuDealOptionLayout.setVisibility(View.GONE);
						menuDealImageView.setImageResource(R.drawable.ic_menu_arrow_down);
					} else {
						menuDealOptionLayout.setVisibility(View.VISIBLE);
						menuDealImageView.setImageResource(R.drawable.ic_menu_arrow);
					}
					left.closeMenu();
					if (!(mContent instanceof FragmentDeals)) {
						changeBackground(menuDealLayout);
						loadFragment(new FragmentDeals());
					}
				} else {
					left.closeMenu();
					if (!(mContent instanceof FragmentDeals)) {
						changeBackground(menuDealLayout);
						loadFragment(new FragmentDeals());
					}
				}
					
			}
			break;
		case R.id.menuConnectWithInstagramLayout:
			left.closeMenu();
			changeBackground(menuConnectWithTwitterLayout);
			break;
		case R.id.menuConnectWithTwitterLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(menuConnectWithTwitterLayout);
				twitterLoogedIn();
			}
			break;
		case R.id.menuConnectWithFacebookLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(menuConnectWithFacebookLayout);
				Intent i = new Intent(context, FbSignActivity.class);
				i.putExtra("getFriendList", false);
				i.putExtra("getProfilePic", false);
				startActivityForResult(i, AppConstants.REQUEST_CODE_FB_LOGIN);
			}
			break;
		case R.id.menuSupportLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(menuSupportLayout);
				loadFragment(new FragmentSupport());
			}
			break;
		case R.id.kikerWalletLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(kikerWalletLayout);
				if( UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == ""){
						CreateAccountDialog createAccountDialog = new CreateAccountDialog(context);
						createAccountDialog.show();
				}else{
					openWalletList();
				}
			}
			break;
		case R.id.menuLogoutoptionLayout:
			left.closeMenu();
			changeBackground(menuLogoutoptionLayout);
			LogoutDialog logoutDialog = new LogoutDialog(context, homeActivity);
			logoutDialog.show();
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
		case R.id.leftTextView:
				hideBackButton();
				onBackPressed();
			break;
		case R.id.menuRightText:
			CommonUtility.hideSoftKeyboard(context);
			if (mContent instanceof FragmentInspirationImageTag) {
				((FragmentInspirationImageTag) mContent).done();
				hideBackButton();
				hideRightButton();
				onBackPressed();
			} else if (mContent instanceof FragmentInspirationImage) {
				((FragmentInspirationImage) mContent).goToNext();
            }else if(mContent instanceof FragmentTagList){
                ((FragmentTagList) mContent).done();
                onBackPressed();
           }
			break;
		case R.id.menuChatLayout:
			if (checkInternet()) {
				left.closeMenu();
				changeBackground(menuChatLayout);
				loadFragment(new FragmentProductDetailWebView("https://tlk.io/kikrsocialshopping"));
			}
			break;
		default:
			break;
		}
	}

	public void fbLogIn() {
		Intent i = new Intent(context, FbSignActivity.class);
		i.putExtra("getFriendList", false);
		i.putExtra("getProfilePic", false);
		startActivityForResult(i, AppConstants.REQUEST_CODE_FB_LOGIN);
	}
	
	public void emailLogIn() {
		left.closeMenu();
		changeBackground(menuSettingsLayout);
		loadFragment(new FragmentSettings());
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

	private void startActivity(Class class1) {
		Intent i = new Intent(this, class1);
		startActivity(i);
	}

	protected void changeBackground(View v) {
		if(v == menuKikrCreditsLayout)
			totalCredits.setTextColor(this.getResources().getColor(R.color.white));
		else
			totalCredits.setTextColor(this.getResources().getColor(R.color.btn_green));
		
		txtShop.setTextColor(this.getResources().getColor(R.color.btn_green));
		txtFeed.setTextColor(this.getResources().getColor(R.color.btn_green));
		
		if(v == discoverLayout) {
			txtShop.setTextColor(this.getResources().getColor(R.color.white));
		}
			
		if(v == inspirationLayout) {
			txtFeed.setTextColor(this.getResources().getColor(R.color.white));
		}
		for (int i = 0; i < layouts.size(); i++) {
			if (layouts.get(i) == v) {
				Syso.info("selected:  "+ v+"  "+layouts.get(i));
				layouts.get(i).setBackgroundColor(getResources().getColor(R.color.menu_option_background_selected));
			} else {
				Syso.info(" abcd  " + layouts.get(i));
				layouts.get(i).setBackgroundColor(getResources().getColor(R.color.menu_option_background));
			}
		}
	}

	public void initLayout() {
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
		totalCredits = (TextView) findViewById(R.id.totalCredits);
		txtShop = (TextView) findViewById(R.id.txtShop);
		txtFeed = (TextView) findViewById(R.id.txtFeed);
	}

	public void setupData() {
		if (getIntent().getStringExtra("inspiration_id")!=null) {
			if (!TextUtils.isEmpty(getIntent().getStringExtra("inspiration_id")) && getIntent().getStringExtra("section").equals("likeinsp") || getIntent().getStringExtra("section").equals("commentinsp")) {
				String id = getIntent().getStringExtra("inspiration_id");
				addFragment(new FragmentInspirationDetail(id));
				cleanActivity();
			} else if (getIntent().getStringExtra("section").equals("follow")) {
					addFragment(new FragmentProfileView(getIntent().getStringExtra("inspiration_id"), "no"));
					cleanActivity();
			} 
		} else  if (getIntent().getStringExtra("section")!=null) {
			if (getIntent().getStringExtra("section").equals("placeorder")) {
				UserPreference.getInstance().setIsNotificationClicked(true);
				String otherdata = getIntent().getStringExtra("otherdata");
				addFragment(new FragmentPlaceMyOrder(otherdata,true));
				cleanActivity();
			} else if (getIntent().getStringExtra("section").equals("commission")) {
				addFragment(new FragmentKikrCreditsScreen());
				cleanActivity();
			}else if (getIntent().getStringExtra("section").equals("twotap")) {
				String otherdata = getIntent().getStringExtra("otherdata");
				try{
					JSONObject jsonObject = new JSONObject(otherdata);
					String msg = jsonObject.getString("message");
					showMessage(msg,true);
					cleanActivity();
				}catch(Exception e){
					e.printStackTrace();
				}
			}if (getIntent().getStringExtra("section").equals("cancel")) {
				String otherdata = getIntent().getStringExtra("message");
				showMessage(otherdata,false);
				cleanActivity();
			}
		 }else {
			addFragment(new FragmentDiscoverNew());
			changeConnectedBy();
			if (UserPreference.getInstance().getCheckedIsConnected()
					&& CommonUtility.isOnline(context)) {
				checkStatusOFSocial();
			}
		}
		if (checkInternet()) {
			getCartList();
		}
	}

	private void showMessage(String otherdata,boolean isLoadOrderDeatil) {
		try {
			TwotapMessageDialog twotapMessageDialog = new TwotapMessageDialog(context, otherdata,isLoadOrderDeatil);
			twotapMessageDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
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
		menuProfileLayout.setOnClickListener(this);
		menuConnectWithTwitterLayout.setOnClickListener(this);
		menuConnectWithFacebookLayout.setOnClickListener(this);
		menuKikrCreditsLayout.setOnClickListener(this);
		menuActivityLayout.setOnClickListener(this);
		menuMyFriendsLayout.setOnClickListener(this);
		menuInviteFriendsLayout.setOnClickListener(this);
		menuCheckInLayout.setOnClickListener(this);
		menuSupportLayout.setOnClickListener(this);
		menuSettingsLayout.setOnClickListener(this);
		menuLogoutoptionLayout.setOnClickListener(this);
		menuInterestsLayout.setOnClickListener(this);
		kikerWalletLayout.setOnClickListener(this);
		menuDealImageView.setOnClickListener(this);
		menuProfileLayoutImageView.setOnClickListener(this);
		menuSettingsLayoutImageView.setOnClickListener(this);
		menuDealLayout.setOnClickListener(this);
		menuConnectWithInstagramLayout.setOnClickListener(this);
		discoverLayout.setOnClickListener(this);
		inspirationLayout.setOnClickListener(this);
		menuMyFriendsLayoutImageView.setOnClickListener(this);
		menuOrdersLayout.setOnClickListener(this);
		menuChatLayout.setOnClickListener(this);
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

	private void inviteFriends() {
		
		BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(this)
				.addTag("referral")
		        .addParameters("userid", UserPreference.getInstance().getUserID())
		        .addParameters("usermail", UserPreference.getInstance().getEmail())
		        .addParameters("username",UserPreference.getInstance().getUserName())
		        .addParameters("userprofilepic",UserPreference.getInstance().getProfilePic())
		        .addParameters("$og_title", getResources().getString(R.string.come_join))
		        .addParameters("$og_description",getResources().getString(R.string.text_join) + "#Kikr @myKikr")
		        .addParameters("$og_image_url", "https://pbs.twimg.com/profile_images/541046285873053696/WmdnfQRo_400x400.png");

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
		    	//	intent.putExtra(Intent.EXTRA_SUBJECT,  context.getResources().getString(R.string.come_join));
		    		intent.putExtra(Intent.EXTRA_TEXT,context.getResources().getString(R.string.text_join) + " #Kikr @MyKikr " +url);
		    		
		    		startActivity(Intent.createChooser(intent, "Earn 25 credits for each Kikr sign up!"));
		            Log.i("Branch", "Got a Branch URL " + url);
		        }
		    }
		});
		
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
		}else{
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

    	Log.e("home act",requestCode + "wefwe" + resultCode);
		if (requestCode == AppConstants.WALLETLIST && resultCode == RESULT_OK) {
			loadFragment(new FragmentKikrWalletCard());
		} else if (requestCode == AppConstants.REQUEST_CODE_FB_LOGIN&& resultCode == RESULT_OK) {
			String id = data.getStringExtra("id");
			String email = data.getStringExtra("email");
			String gender = data.getStringExtra("gender");
			String name = data.getStringExtra("name");
			String username = data.getStringExtra("username");
			String birthday = data.getStringExtra("birthday");
			String profile_link = data.getStringExtra("profile_link");
			String location = data.getStringExtra("location");
			connectWithFacebook(id, email, gender, name, username, birthday,profile_link, location);
		} else if (requestCode == AppConstants.REQUEST_CODE_TWIT_LOGIN&& resultCode == RESULT_OK) {
			String id = String.valueOf(data.getLongExtra("id", 0));
			String description = data.getStringExtra("description");
			String language = data.getStringExtra("language");
			String location = data.getStringExtra("location");
			String profile_image_url = data.getStringExtra("profile_image_url");
			String name = data.getStringExtra("name");
			String screen_name = data.getStringExtra("screen_name");
			String status = data.getStringExtra("status");
			String time_zone = data.getStringExtra("time_zone");

			connectWithTwitter(id, description, language, location, name,profile_image_url, screen_name, status, time_zone);

		} else if (requestCode == AppConstants.REQUEST_CODE_FB_FRIEND_LIST) {
			ArrayList<FbUser> fbUsers = (ArrayList<FbUser>) data.getSerializableExtra("friend_list");
			if (fbUsers != null && fbUsers.size() > 0) {
				uploadFbFriends(fbUsers);
			} else {
				UserPreference.getInstance().setIsFbConnected(true);
				changeConnectedBy();
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
								// Syso.info("aaaaaaaaaaaaaaa payment nonce::"+paymentMethodNonce);
								if (checkInternet()) 
									saveCheckout(paymentMethodNonce);
							}
						});
				braintree.finishPayWithPayPal(this, resultCode, data);
			}
		} else if (requestCode == AppConstants.REQUEST_CODE_MASKED_WALLET) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(
					mFragmentStack.peek());
			((FragmentPlaceMyOrder) fragment).onActivityResult(requestCode,
					resultCode, data);
		}
		
		if(requestCode == Crop.REQUEST_CROP) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
			((FragmentInspirationImage) fragment).onActivityResult(requestCode,
					resultCode, data);
		}
		
		if(requestCode == Crop.REQUEST_PICK) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
			((FragmentInspirationImage) fragment).onActivityResult(requestCode,
					resultCode, data);
		}
		
		if (requestCode == AppConstants.REQUEST_CODE_TWIT_TO_FRIEND) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
			((FragmentMyFriends) fragment).onActivityResult(requestCode,
					resultCode, data);
		}
		if (requestCode == AppConstants.REQUEST_CODE_EMAIL_CHANGE) {
			Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
			((FragmentSettings) fragment).onActivityResult(requestCode,
					resultCode, data);
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
						AlertUtils.showToast(context,R.string.alert_connected_with_fb);
						UserPreference.getInstance().setIsFbConnected(true);
						changeConnectedBy();
					}

					@Override
					public void handleOnFailure(ServiceException exception,Object object) {
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
				AlertUtils.showToast(context,"Failed to connect with twitter. Please try again.");
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

	private void connectWithTwitter(String userId, String description,String language, String location, String name,String profile_image_url, String screen_name, String status,
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
							changeConnectedBy();
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
			connectWithTwitter(String.valueOf(userId), description, language,location, name, profile_image_url, screen_name, status,time_zone);
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
						AlertUtils.showToast(context,R.string.alert_connected_with_twitter);
						UserPreference.getInstance().setIsTwitterConnected(true);
						changeConnectedBy();
					}

					@Override
					public void handleOnFailure(ServiceException exception,Object object) {
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
					public void handleOnFailure(ServiceException exception,Object object) {

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
		service.connectWithFacebook(id, gender, birthday, profile_link,location, name, username);
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}

	private void changeConnectedBy() {
		if (UserPreference.getInstance().getIsFbConnected()) {
			menuConnectWithFacebookLayout.setVisibility(View.GONE);
			fbView.setVisibility(View.GONE);
		}
		if (UserPreference.getInstance().getIsTwitterConnected()) {
			menuConnectWithTwitterLayout.setVisibility(View.GONE);
			twitterView.setVisibility(View.GONE);
		}
		if (UserPreference.getInstance().getIsFbConnected()
				&& UserPreference.getInstance().getIsTwitterConnected()) {
//			menuProfileLayoutImageView.setVisibility(View.GONE);
		}
	}

	private void verifyBluetooth(boolean showDialog) {
		try {
			if (!IBeaconManager.getInstanceForApplication(this)
					.checkAvailability()) {
				if (showDialog)
					showBluetoothDialog();
			} else {
				if (showDialog)
					AlertUtils.showToast(context, "Searching iBeacon...");
				startIBeaconService();
			}
		} catch (RuntimeException e) {
			if (showDialog) {
				e.printStackTrace();
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						this);
				builder.setTitle("Bluetooth LE not available");
				builder.setMessage("Sorry, this device does not support Bluetooth LE.");
				builder.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								builder.create().dismiss();
							}
						});
				builder.show();
			}
		}

	}

	private void showBluetoothDialog() {
		// TODO Auto-generated method stub
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Bluetooth not enabled");
		builder.setMessage("Please enable bluetooth in settings to receive exclusive in-store deals.");

		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intentOpenBluetoothSettings = new Intent();
						intentOpenBluetoothSettings
								.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
						startActivity(intentOpenBluetoothSettings);
						builder.create().dismiss();
					}
				});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						builder.create().dismiss();
					}
				});
		builder.show();
	}

	private void startIBeaconService() {
		Intent intent = new Intent(context, BeaconMonitorService.class);
		startService(intent);
	}

	public void shareProduct(final Product product) {
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
		.addParameters("brand", product.getBrand())
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
			    		intent.putExtra(Intent.EXTRA_TEXT, AppConstants.SHARE_MESSAGE + " "
			    				 + AppConstants.APP_LINK + "\nItem: " + product.getProducturl());
			    		startActivity(Intent.createChooser(intent, "Share"));
			        } else {
			            Log.i("Branch", "Got a Branch URL " + url);
			            Intent intent = new Intent(Intent.ACTION_SEND);
			    		intent.setType("text/plain");
			    		//intent.putExtra(Intent.EXTRA_SUBJECT,  "Check out this " + product.getProductname() + " on Kikr!");
			    		intent.putExtra(Intent.EXTRA_TEXT,"Check out the " + product.getProductname() + " on #Kikr @myKikr " + url);
			    		
			    		startActivity(Intent.createChooser(intent, "Share"));
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
			            Log.i("Branch", "Got a Branch URL " + url);
			            Intent intent = new Intent(Intent.ACTION_SEND);
			    		intent.setType("text/plain");
			    		//intent.putExtra(Intent.EXTRA_SUBJECT,  "Check out this " + product.getProductname() + " on Kikr!");
			    		intent.putExtra(Intent.EXTRA_TEXT,"Check out the " + collectionname + " collections of " +  UserPreference.getInstance().getUserName() +  "on #Kikr @myKikr " + url);
			    		
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
//			if (fragment instanceof FragmentProfileView) {
//				hideAllFragment();
//			}
			checkBackButton(fragment);
			checkRightButton(fragment);
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
				transaction.add(R.id.frame_container, fragment,mContent.toString());
				transaction.addToBackStack(mContent.toString());
				transaction.commit();
				if (fragment instanceof FragmentDiscoverNew)
					changeBackground(discoverLayout);
			} else {
				mFragmentStack.add(mContent.toString());
				transaction.replace(R.id.frame_container, fragment,mContent.toString());
				transaction.addToBackStack(mContent.toString());
				transaction.commit();
				if (fragment instanceof FragmentDiscoverNew)
					changeBackground(discoverLayout);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		try {
			left.closeMenu();
			hideFutter();
			if (mFragmentStack.size() == 1) {
				Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
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
				removeFragment();
				super.onBackPressed();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void removeFragment() {
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
			} else {
				if (fragment != null)
					transaction.show(fragment);
				transaction.commit();
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
					transaction.hide(fragment);
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
		textView.setText(Html.fromHtml(getResources().getString(
				R.string.no_internet)));
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
						braintree.startPayWithPayPal(context,PAYPAL_REQUEST_CODE);
					}

					@Override
					public void handleOnFailure(ServiceException exception,Object object) {
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
						AlertUtils.showToast(context,R.string.payment_success_text);
						createCart();
					}

					@Override
					public void handleOnFailure(ServiceException exception,Object object) {
						progressBarDialog.dismiss();
						AlertUtils.showToast(context,R.string.payment_fail_text);
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
		menuLeftImageView.setVisibility(View.GONE);
	}

	public void hideBackButton() {
		menuBackTextView.setVisibility(View.GONE);
		menuLeftImageView.setVisibility(View.VISIBLE);
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
				|| fragment instanceof FragmentActivityMonths) {
			showBackButton();
		} else {
			hideBackButton();
		}
		if (fragment instanceof FragmentProfileView && isProfile) {
			isProfile = false;
			hideBackButton();
		}
	}

	private void checkRightButton(Fragment fragment) {
		if (fragment instanceof FragmentInspirationImageTag) {
			showRightButton();
			menuRightTextView.setText("Done");
		} else if (fragment instanceof FragmentInspirationImage) {
			showRightButton();
			menuRightTextView.setText("Next");
        } else if(fragment instanceof FragmentTagList){
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
		menuSearchImageView.setVisibility(View.VISIBLE);
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
					if (!TextUtils.isEmpty(response.getPin_created())&& response.getPin_created().equals("yes")) {
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
			LayoutParams params = new FrameLayout.LayoutParams(
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
			LayoutParams params = new FrameLayout.LayoutParams(
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
			LayoutParams params = new FrameLayout.LayoutParams(
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
		String fromCollection = product.getFrom_collection_id() == null ? "": product.getFrom_collection_id();
		cartApi.addToCart(UserPreference.getInstance().getUserID(),product.getId(), "1", UserPreference.getInstance().getCartID(),fromUserId, fromCollection, "", "",null);
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
							AlertUtils.showToast(context,R.string.invalid_response);
						}
						mProgressBarDialog.dismiss();
					}
				});
		if (TextUtils.isEmpty(product.getLike_info().getLike_id()))
			inspirationSectionApi.postLike(UserPreference.getInstance()
					.getUserID(), product.getId(), "product");
		else
			inspirationSectionApi.removeLike(UserPreference.getInstance()
					.getUserID(), product.getLike_info().getLike_id());
		inspirationSectionApi.execute();
	}
	
	public void setDescription() {
		if (mContent instanceof FragmentInspirationDetail) {
			((FragmentInspirationDetail)mContent).setDescription();
		}
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkForCrashes();
	    checkForUpdates();
	}
	
	private void checkForCrashes() {
	    CrashManager.register(this, "3ec40bcb125d3ad0f770b3d424bcf913");
	}

	private void checkForUpdates() {
	    // Remove this for store builds!
	    UpdateManager.register(this, "3ec40bcb125d3ad0f770b3d424bcf913");
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
					
				}
				else {
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
	
	public void refreshCartCount(){
		menuTextCartCount.setText(UserPreference.getInstance().getCartCount());
		menuTextCartCount.setVisibility(View.VISIBLE);
	}
	
	public void purchaseStatus(String purchase_id) {
		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("success:   "+ object);
				progressBarDialog.dismiss();
				try {
					JSONObject jsonObject = new JSONObject(object.toString());
					if (jsonObject.getString("message").equalsIgnoreCase("done")) {
						UserPreference.getInstance().setPurchaseId("");
						addFragment(new FragmentUserCart());
					}else{
						AlertUtils.showToast(context,"Your previous order in process, please try again later.");
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
	
}

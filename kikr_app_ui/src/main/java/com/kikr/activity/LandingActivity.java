package com.kikr.activity;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import twitter4j.User;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.sessionstore.SessionStore;
import com.kikr.twitter.OauthItem;
import com.kikr.twitter.TwitterOAuthActivity;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.ui.RoundImageView;
import com.kikr.utility.AppConstants;
import com.kikr.utility.AppConstants.Screen;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.FontUtility;
import com.kikrlib.api.ConnectWithFacebookApi;
import com.kikrlib.api.ConnectWithTwitterApi;
import com.kikrlib.api.FbTwFriendsApi;
import com.kikrlib.api.RegisterUserApi;
import com.kikrlib.bean.FbUser;
import com.kikrlib.bean.TwitterFriendList;
import com.kikrlib.db.HelpPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ConnectWithFacebookRes;
import com.kikrlib.service.res.ConnectWithTwitterRes;
import com.kikrlib.service.res.RegisterUserResponse;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.DeviceUtils;
import com.kikrlib.utils.Syso;
import com.personagraph.api.PGAgent;

public class LandingActivity extends BaseActivity implements OnClickListener,ServiceCallback{
	private ProgressBarDialog progressBarDialog;
	private Button mFacebookButton,mTwitterButton,mEmailButton,mSkipButton;
	private final int REQUEST_CODE_FB_LOGIN = 1000;
	private final int REQUEST_CODE_TWIT_LOGIN = 1001;
	private final String DEFAULT_GENDER = "male";
	private String social ;
	private String mEmail ;
	private String mProfilePic ;
	private String mUsername ;
	private String name;
	private String birthday;
	private String location;
	private String gender;
	private String id;
	private String profileLink;
	private boolean isFromFacebook = false;
	private boolean isFromTwitter = false;
	private LinearLayout layoutReferred, optionLayout;
	private RoundImageView user_profile_image;
	private TextView user_profile_name;
	private ImageView imgOrFBTwitter;
	private ImageView imgOr;
	public static String referred_userid = "-1";
	private String referred_username;
	private String referred_usermail;
	private String referred_userprofilepic;
	private TextView earn250;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_landing); 
		
		
		if(getIntent().getStringExtra("referred_userid") !=null) {
			referred_userid = getIntent().getStringExtra("referred_userid");
			referred_username = getIntent().getStringExtra("referred_username");
			referred_usermail = getIntent().getStringExtra("referred_usermail");
		    referred_userprofilepic = getIntent().getStringExtra("referred_userprofilepic");
			layoutReferred.setVisibility(View.VISIBLE);
			mSkipButton.setVisibility(View.GONE);
			imgOrFBTwitter.setVisibility(View.VISIBLE);
			imgOr.setVisibility(View.GONE);
			user_profile_name.setTypeface(null, Typeface.BOLD);
			user_profile_name.setTypeface(FontUtility.setProximanovaLight(this));
			CommonUtility.setImage(context, referred_userprofilepic, user_profile_image,R.drawable.dum_user);
			user_profile_name.setText(referred_username);
			earn250.setTypeface(FontUtility.setProximanovaLight(this));
			earn250.setVisibility(View.VISIBLE);
		} else {
			earn250.setVisibility(View.GONE);
			layoutReferred.setVisibility(View.GONE);
			mEmailButton.setVisibility(View.VISIBLE);
	//		mSkipButton.setVisibility(View.VISIBLE);
			imgOrFBTwitter.setVisibility(View.GONE);
	//		imgOr.setVisibility(View.VISIBLE);
		}
	}	

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.emailButton:
			if(CommonUtility.isOnline(context))
			PGAgent.logEvent("SIGNUP_VIA_EMAIL_CLICKED");
			startActivity(SignUpActivity.class);
			finish();
			break;
		case R.id.facebookButton:
			if(checkInternet()){
				isFromTwitter=false;
				isFromFacebook=true;
				PGAgent.logEvent("SIGNUP_VIA_FACEBOOK_CLICKED");
				social=UserPreference.FACEBOOK;
				Intent i = new Intent(context, FbSignActivity.class);
				i.putExtra("getFriendList", false);
				i.putExtra("getProfilePic", false);
				startActivityForResult(i, REQUEST_CODE_FB_LOGIN);
			}
			break;
		case R.id.twitterButton:
			if(checkInternet()){
				isFromTwitter=true;
				isFromFacebook=false;
				PGAgent.logEvent("SIGNUP_VIA_TWITTER_CLICKED");
				social=UserPreference.TWIITER;
				twitterLoogedIn();
			}
			break;
		case R.id.skipButton:
			if(checkInternet()){
				skip();
			}
			break;
		}
	}

	@Override
	public void initLayout() {
		mFacebookButton = (Button) findViewById(R.id.facebookButton);
		mTwitterButton = (Button) findViewById(R.id.twitterButton);
		mEmailButton = (Button) findViewById(R.id.emailButton);
		mSkipButton=(Button) findViewById(R.id.skipButton);
		layoutReferred = (LinearLayout) findViewById(R.id.layoutReferred);
		user_profile_image = (RoundImageView) findViewById(R.id.user_profile_image);
		user_profile_name = (TextView) findViewById(R.id.user_profile_name);
		optionLayout = (LinearLayout) findViewById(R.id.optionLayout);
		imgOrFBTwitter = (ImageView) findViewById(R.id.imgOrFBTwitter);
		imgOr = (ImageView) findViewById(R.id.imgOr);
		earn250 = (TextView) findViewById(R.id.earn250);
	} 

	@Override
	public void setupData() {
	}

	@Override
	public void headerView() {
		hideHeader();
	}

	@Override
	public void setUpTextType() {
		mFacebookButton.setTypeface(FontUtility.setProximanovaLight(context));
		mTwitterButton.setTypeface(FontUtility.setProximanovaLight(context));
		mEmailButton.setTypeface(FontUtility.setProximanovaLight(context));
		mSkipButton.setTypeface(FontUtility.setProximanovaLight(context));
	}

	@Override
	public void setClickListener() {
		mFacebookButton.setOnClickListener(this);
		mTwitterButton.setOnClickListener(this);
		mEmailButton.setOnClickListener(this);
		mSkipButton.setOnClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_FB_LOGIN && resultCode == RESULT_OK) {
			isFromFacebook=true;
			id=data.getStringExtra("id");
			String email=data.getStringExtra("email");
			String gender=data.getStringExtra("gender");
			mUsername = data.getStringExtra("name");
			mProfilePic = data.getStringExtra("profile_pic");
			String g=gender!=null?gender:DEFAULT_GENDER;
			mEmail=email;
			name = data.getStringExtra("name");
			birthday =data.getStringExtra("birthday");
			location =data.getStringExtra("location");
			profileLink = data.getStringExtra("profile_link");
			registerViaSocial(id,g);
		}else if(requestCode ==REQUEST_CODE_TWIT_LOGIN && resultCode == RESULT_OK){
			isFromTwitter=true;
			id=String.valueOf(data.getLongExtra("id", 0));
			mProfilePic = data.getStringExtra("profile_image_url");
			mUsername = data.getStringExtra("screen_name");
			registerViaSocial(id, DEFAULT_GENDER);
		}else if (requestCode == AppConstants.REQUEST_CODE_FB_FRIEND_LIST) {
			ArrayList<FbUser> fbUsers = (ArrayList<FbUser>) data.getSerializableExtra("friend_list");
			System.out.println("123456789 "+fbUsers);
			if (fbUsers!=null&&fbUsers.size() > 0) 
				uploadFbFriends(fbUsers);
			else {
				Bundle bundle = new Bundle();
				bundle.putString("email", mEmail);
				bundle.putString("username", mUsername);
				bundle.putString("profilePic", mProfilePic);
				gotoFirstScreen(bundle);
			}
		} else if (requestCode == AppConstants.REQUEST_CODE_TWIT_FRIEND_LIST) {
			ArrayList<OauthItem> twitUsers = (ArrayList<OauthItem>) data.getSerializableExtra("friend_list");
			showTwitterFriendList(twitUsers);
		} 
	}

	private void twitterLoogedIn() {
		boolean logedIn = SessionStore.isTwitterLogedIn(context);
		if (logedIn) {
			new GetTwitterInfo().execute();
		} else {
			Intent intent = new Intent(context, TwitterOAuthActivity.class);
			startActivityForResult(intent, REQUEST_CODE_TWIT_LOGIN);
		}
	}
	
	private class GetTwitterInfo extends AsyncTask<Void, Void, User>{

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
			System.out.println("Twitter id>>"+user);
			if(user!=null){
				mProfilePic = user.getOriginalProfileImageURL();
				mUsername = user.getScreenName();
				registerViaSocial(String.valueOf(user.getId()),DEFAULT_GENDER);
			}else{
				AlertUtils.showToast(context, "Failed to connect with twitter. Please try again.");
			}
		}
	}
	
	private void skip() {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		
		final RegisterUserApi service = new RegisterUserApi(
				new ServiceCallback() {

					@Override
					public void handleOnSuccess(Object object) {
						if(object!=null){
							RegisterUserResponse response=(RegisterUserResponse) object;
							UserPreference.getInstance().setUserID(response.getId());
							UserPreference.getInstance().setCurrentScreen(Screen.CategoryScreen);
							UserPreference.getInstance().setcheckedIsConnected(true);
							UserPreference.getInstance().setIsCreateWalletPin(true);
							UserPreference.getInstance().setCartID(response.getCart_id());
							setHelpPreference();
							startActivity(FollowCategoriesNewActivity.class);
							finish();
						}
					}
					@Override
					public void handleOnFailure(ServiceException exception,
							Object object) {
						// TODO Auto-generated method stub
						progressBarDialog.dismiss();
						Syso.info("In handleOnFailure>>" + object);
						if (object != null) {
							RegisterUserResponse response = (RegisterUserResponse) object;
							AlertUtils.showToast(context, response.getMessage());
						} else {
							AlertUtils.showToast(context,R.string.invalid_response);
						}
					}
				});
		service.registerGeustUser(CommonUtility.getDeviceTocken(context), Screen.CategoryScreen, CommonUtility.getDeviceId(context));
		service.execute();
		
		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}

	
	private void registerViaSocial(String id, String gender) {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		
		final RegisterUserApi service = new RegisterUserApi(this);
		service.registerViaSocial(social, id, gender,DeviceUtils.getPhoneModel(), CommonUtility.getDeviceTocken(context),Screen.EmailScreen,"android", CommonUtility.getDeviceId(context));
		service.execute();
		
		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}

	@Override
	public void handleOnSuccess(Object object) {
		progressBarDialog.dismiss();
		Syso.info("In handleOnSuccess>>"+object);
		if(object!=null){
			RegisterUserResponse response=(RegisterUserResponse) object;
			UserPreference.getInstance().setUserID(response.getId());
			UserPreference.getInstance().setCurrentScreen(Screen.EmailScreen);
			UserPreference.getInstance().setcheckedIsConnected(true);
			UserPreference.getInstance().setIsCreateWalletPin(true);
			UserPreference.getInstance().setCartID(response.getCart_id());
			setHelpPreference();
			Bundle bundle=new Bundle();
			bundle.putString("email",mEmail);
			bundle.putString("username",mUsername);
			bundle.putString("profilePic",mProfilePic);
			String current_screen = response.getCurrent_screen();
			if (!TextUtils.isEmpty(current_screen)) {
				showHome(current_screen);
			} else {
				if (isFromFacebook && checkInternet()) {
					connectWithFacebook(id, mEmail, gender, name, mUsername, birthday, profileLink, location);
				} else if (isFromTwitter && checkInternet()){
					twitterInfoUpload();
				}
//				startActivity(EditProfileActivity.class,bundle);
			}
			UserPreference.getInstance().setPassword();
		}
//		finish();
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		
		progressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>"+object);
		if(object!=null){
			RegisterUserResponse response=(RegisterUserResponse) object;
			if(response.getMessage().equals("userexist")&&response.getId()!=null){
				UserPreference.getInstance().setUserID(response.getId());
				 UserPreference.getInstance().setEmail(response.getEmail());
				 UserPreference.getInstance().setUserName(response.getUsername());
				 UserPreference.getInstance().setCartID(response.getCart_id());
				 UserPreference.getInstance().setProfilePic(response.getProfile_pic());
				 UserPreference.getInstance().setBgImage(response.getBackground_pic());
				
				if(social.equals(UserPreference.FACEBOOK))
					UserPreference.getInstance().setIsFbConnected(true);
				if(social.equals(UserPreference.TWIITER))
					UserPreference.getInstance().setIsTwitterConnected(true);
				UserPreference.getInstance().setIsCreateWalletPin(true);
				UserPreference.getInstance().setcheckedIsConnected(true);
				String current_screen = response.getCurrent_screen();
				if (!TextUtils.isEmpty(current_screen)) {
					showHome(current_screen);
				}else
					startActivity(HomeActivity.class);
//				if (isFromFacebook && checkInternet()) {
//					connectWithFacebook(id, mEmail, gender, name, mUsername, birthday, profileLink, location);
//				} else if (isFromTwitter && checkInternet()){
//					twitterInfoUpload();
//				}
				
			}else{
				AlertUtils.showToast(context,response.getMessage());
			}
		}else{
			AlertUtils.showToast(context,R.string.invalid_response);
		}
	}
	
	private void setHelpPreference() {
		HelpPreference.getInstance().setHelpCart("yes");
		HelpPreference.getInstance().setHelpCollection("yes");
		HelpPreference.getInstance().setHelpInspiration("yes");
		HelpPreference.getInstance().setHelpKikrCards("yes");
		HelpPreference.getInstance().setHelpPinMenu("yes");
		HelpPreference.getInstance().setHelpSideMenu("yes");
		HelpPreference.getInstance().setHelpFriendsSideMenu("yes");
	}

	public void test(View v){
//		startActivity(FollowCategoriesActivity.class);
	}
	
	private void showHome(String currentScreen) {
		Intent i=null;
		if(currentScreen.equals(Screen.HomeScreen)){
			i = new Intent(context,HomeActivity.class);
		}else if(currentScreen.equals(Screen.EmailScreen)){
			i = new Intent(context,EmailActivity.class);
		}else if(currentScreen.equals(Screen.UserNameScreen)){
			i = new Intent(context,EditProfileActivity.class);
		}else if(currentScreen.equals(Screen.CategoryScreen)){
			i = new Intent(context,FollowCategoriesActivity.class);
		}else if(currentScreen.equals(Screen.BrandScreen)){
			i = new Intent(context,FollowBrandsActivity.class);
		}else if(currentScreen.equals(Screen.StoreScreen)){
			i = new Intent(context,FollowStoreActivity.class);
		}else if(currentScreen.equals(Screen.CardScreen)){
			i = new Intent(context,KikrTutorialActivity.class);
		}else{
			i = new Intent(context,HomeActivity.class);
		}
		startActivity(i);
		finish();
	}
	
	
	private void connectWithFacebook(final String id,final String email,final String gender,final String name,final String username,final String birthday,final String profile_link,final String location) {
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
							AlertUtils.showToast(context,R.string.invalid_response);
						}
					}

					@Override
					public void handleOnFailure(ServiceException exception,Object object) {
						progressBarDialog.dismiss();
						if (object != null) {
							ConnectWithFacebookRes facebookRes = (ConnectWithFacebookRes) object;
							String message = facebookRes.getMessage();
//							AlertUtils.showToast(context, message);
//							showDialog("Failed to Fetch Information", new Method() {
//								public void execute() {
									getFBFriendList();
//								}
//							}, new Method() {
//								public void execute() {
//									connectWithFacebook(id, email, gender, name, username, birthday, profile_link, location);
//								}
//							});
//						} else {
//							AlertUtils.showToast(context,R.string.invalid_response);
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
	
	private void getFBFriendList() {
		Intent i = new Intent(context, FbSignActivity.class);
		i.putExtra("getFriendList", true);
		i.putExtra("getProfilePic", false);
		startActivityForResult(i, AppConstants.REQUEST_CODE_FB_FRIEND_LIST);
	}
	
	private void twitterInfoUpload() {
		boolean logedIn = SessionStore.isTwitterLogedIn(context);
		if (logedIn) {
			new GetTwitterInformation().execute();
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
			startActivityForResult(intent,AppConstants.REQUEST_CODE_TWIT_FRIEND_LIST);
		}
	}

	private class GetTwitterInformation extends AsyncTask<Void, Void, User> {

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

	private void connectWithTwitter(String userId, String description,
			String language, String location, String name,
			String profile_image_url, String screen_name, String status,
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

	private void uploadTwitterFriends(final List<TwitterFriendList> data) {
		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final FbTwFriendsApi service = new FbTwFriendsApi(
				new ServiceCallback() {

					@Override
					public void handleOnSuccess(Object object) {
						progressBarDialog.dismiss();
						Bundle bundle=new Bundle();
						bundle.putString("email",mEmail);
						bundle.putString("username",mUsername);
						bundle.putString("profilePic",mProfilePic);
						gotoFirstScreen(bundle);
					}

					@Override
					public void handleOnFailure(ServiceException exception,Object object) {
						progressBarDialog.dismiss();
						showDialog("Failed to upload Twitter friends", new Method() {
							public void execute() {
								Bundle bundle = new Bundle();
								bundle.putString("email", mEmail);
								bundle.putString("username", mUsername);
								bundle.putString("profilePic", mProfilePic);
								gotoFirstScreen(bundle);
							}
						}, new Method() {
							public void execute() {
								uploadTwitterFriends(data);
							}
						});
					}
				});
		service.addTwitterFriend(UserPreference.getInstance().getUserID(), data);
		service.execute();
	}

	private void uploadFbFriends(final List<FbUser> fbusers) {
		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final FbTwFriendsApi service = new FbTwFriendsApi(
				new ServiceCallback() {

					@Override
					public void handleOnSuccess(Object object) {
						progressBarDialog.dismiss();
						Bundle bundle=new Bundle();
						bundle.putString("email",mEmail);
						bundle.putString("username",mUsername);
						bundle.putString("profilePic",mProfilePic);
						gotoFirstScreen(bundle);
					}

					@Override
					public void handleOnFailure(ServiceException exception,
							Object object) {
						progressBarDialog.dismiss();
						showDialog("Failed to upload Facebook friends", new Method() {
							public void execute() {
								Bundle bundle = new Bundle();
								bundle.putString("email", mEmail);
								bundle.putString("username", mUsername);
								bundle.putString("profilePic", mProfilePic);
								gotoFirstScreen(bundle);
							}
						}, new Method() {
							public void execute() {
								uploadFbFriends(fbusers);
							}
						});
					}
				});
		service.addFacebookFriend(UserPreference.getInstance().getUserID(),fbusers);
		service.execute();
	}
	
	private void gotoFirstScreen(Bundle bundle){
		startActivity(EmailActivity.class,bundle);
	}
	
	private void showDialog(String message,final Method method1,final Method method2){
		new AlertDialog.Builder(context)
	    .setTitle("Alert")
	    .setMessage(message)
	    .setPositiveButton("Next", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	           method1.execute();
	        }
	     })
	    .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        	method2.execute();
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
	
	public static interface Method {
		void execute();
	}
	
}

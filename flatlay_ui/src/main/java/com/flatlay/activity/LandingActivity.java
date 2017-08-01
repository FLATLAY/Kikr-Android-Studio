package com.flatlay.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.util.Log;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.ConnectWithFacebookApi;
import com.flatlaylib.api.FbTwFriendsApi;
import com.flatlaylib.api.RegisterUserApi;
import com.flatlaylib.bean.FbUser;
import com.flatlaylib.db.HelpPreference;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.ConnectWithFacebookRes;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.DeviceUtils;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;


public class LandingActivity extends BaseActivity implements OnClickListener, ServiceCallback {
    private ProgressBarDialog progressBarDialog;
    private Button mFacebookButton, mEmailButton, mSkipButton, mLoginButton;
    private final int REQUEST_CODE_FB_LOGIN = 1000;
    private final int REQUEST_CODE_TWIT_LOGIN = 1001;
    private final String DEFAULT_GENDER = "male";
    private String social;
    private String mEmail;
    private String mProfilePic;
    private String mUsername;
    private String name;
    private VideoView vedio;
    private String birthday;
    private String location;
    private String gender;
    private String id;
    private String profileLink;
    private boolean isFromFacebook = false;
    private LinearLayout layoutReferred, optionLayout;
    private RoundImageView user_profile_image;
    private TextView user_profile_name;
    private ImageView imgOr;
    public static String referred_userid = "-1";
    private String referred_username;
    private String referred_usermail;
    private String referred_userprofilepic;
    private TextView earn250, kikrIntroductionTextView;
    SharedPreferences userSettings;
    SharedPreferences temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("Activity","LandingActivity");
        CommonUtility.noTitleActivity(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_landing);
        Log.w("myApp", "In LandingActivity");

        temp = getSharedPreferences("fromRemove", 0);
        userSettings = getSharedPreferences("UserSettings", 0);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString("isSet", "False");
        editor.apply();


        if (getIntent().getStringExtra("referred_userid") != null) {
            referred_userid = getIntent().getStringExtra("referred_userid");
            referred_username = getIntent().getStringExtra("referred_username");
            referred_usermail = getIntent().getStringExtra("referred_usermail");
            referred_userprofilepic = getIntent().getStringExtra("referred_userprofilepic");
            layoutReferred.setVisibility(View.VISIBLE);
            mSkipButton.setVisibility(View.GONE);
            imgOr.setVisibility(View.GONE);
            user_profile_name.setTypeface(null, Typeface.BOLD);
            user_profile_name.setTypeface(FontUtility.setProximanovaLight(this));
            CommonUtility.setImage(context, referred_userprofilepic, user_profile_image, R.drawable.profile_icon);
            user_profile_name.setText(referred_username);
            earn250.setTypeface(FontUtility.setProximanovaLight(this));
            earn250.setVisibility(View.VISIBLE);
        } else {
            earn250.setVisibility(View.GONE);
            layoutReferred.setVisibility(View.INVISIBLE);
            mEmailButton.setVisibility(View.VISIBLE);
            //		mSkipButton.setVisibility(View.VISIBLE);
            //		imgOr.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emailButton:
                startActivity(SignUpActivity.class);
                finish();
                break;
            case R.id.facebookButton:
                if (checkInternet()) {
                    isFromFacebook = true;
                    //Log.w("myApp", "Clicked here!");
                    social = UserPreference.FACEBOOK;
                    Intent i = new Intent(context, FbSignActivity.class);
                    i.putExtra("getFriendList", false);
                    i.putExtra("getProfilePic", false);
                    startActivityForResult(i, REQUEST_CODE_FB_LOGIN);
                }
                break;
            case R.id.loginButton:
                CommonUtility.hideSoftKeyboard(context);
                startActivity(LoginActivity.class);
                finish();
                break;
            case R.id.kikrIntroductionTextView:
                startActivity(IntroductionPagerActivity.class);
                break;
        }

    }

    @Override
    public void initLayout() {
        mFacebookButton = (Button) findViewById(R.id.facebookButton);
        mEmailButton = (Button) findViewById(R.id.emailButton);
        mSkipButton = (Button) findViewById(R.id.skipButton);
        layoutReferred = (LinearLayout) findViewById(R.id.layoutReferred);
        user_profile_image = (RoundImageView) findViewById(R.id.user_profile_image);
        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        optionLayout = (LinearLayout) findViewById(R.id.optionLayout);
        imgOr = (ImageView) findViewById(R.id.imgOr);
        earn250 = (TextView) findViewById(R.id.earn250);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        kikrIntroductionTextView = (TextView) findViewById(R.id.kikrIntroductionTextView);
        vedio = (VideoView) findViewById(R.id.vedio);

    }

    @Override
    public void setupData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        videostart();
    }

    public void videostart() {

        try {
            vedio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.setLooping(true);
                    vedio.start(); //TODO: need to make transition seamless.
                    //need to be replaced by youtube
                }
            });

            String uriPath = "android.resource://com.flatlay/" + R.raw.flatlay_guide;
            vedio.setVideoPath(uriPath);
            Uri uri = Uri.parse(uriPath);
            vedio.setVideoURI(uri);
            vedio.requestFocus();
            vedio.start();
        } catch (Exception e) {

        }
    }

    @Override
    public void headerView() {
        hideHeader();
    }

    @Override
    public void setUpTextType() {
        mFacebookButton.setTypeface(FontUtility.setProximanovaLight(context));
         mEmailButton.setTypeface(FontUtility.setProximanovaLight(context));
        mSkipButton.setTypeface(FontUtility.setProximanovaLight(context));
    }

    @Override
    public void setClickListener() {
        mFacebookButton.setOnClickListener(this);
        mEmailButton.setOnClickListener(this);
        mSkipButton.setOnClickListener(this);
        kikrIntroductionTextView.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FB_LOGIN && resultCode == RESULT_OK) {
            isFromFacebook = true;
            id = data.getStringExtra("id");
            String email = data.getStringExtra("email");
            String gender = data.getStringExtra("gender");
            mUsername = data.getStringExtra("name");
            mProfilePic = data.getStringExtra("profile_pic");
            String g = gender != null ? gender : DEFAULT_GENDER;
            mEmail = email;
            name = data.getStringExtra("name");
            birthday = data.getStringExtra("birthday");
            location = data.getStringExtra("location");
            profileLink = data.getStringExtra("profile_link");
            UserPreference.getInstance().setmIsFacebookSignedIn(true);
            registerViaFbSocial(id, g);
        }
//        else if (requestCode == REQUEST_CODE_TWIT_LOGIN && resultCode == RESULT_OK) {
//            isFromTwitter = true;
//            id = String.valueOf(data.getLongExtra("id", 0));
//            mProfilePic = data.getStringExtra("profile_image_url");
//            mUsername = data.getStringExtra("screen_name");
//            registerViaSocial(id, DEFAULT_GENDER);
//        }
        else if (requestCode == AppConstants.REQUEST_CODE_FB_FRIEND_LIST) {
            ArrayList<FbUser> fbUsers = (ArrayList<FbUser>) data.getSerializableExtra("friend_list");
            System.out.println("123456789 " + fbUsers);
            if (fbUsers != null && fbUsers.size() > 0)
                uploadFbFriends(fbUsers);
            else {
                Bundle bundle = new Bundle();
                bundle.putString("email", mEmail);
                bundle.putString("username", mUsername);
                bundle.putString("profilePic", mProfilePic);
                gotoFirstScreen(bundle);
            }
        }
    }


    private void registerViaFbSocial(String id, String g) {
        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();

        final RegisterUserApi service = new RegisterUserApi(this);
        service.registerViaFbSocial(mEmail, social, id, g, DeviceUtils.getPhoneModel(), CommonUtility.getDeviceTocken(context), "Dummy", "android", CommonUtility.getDeviceId(context));
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
        Syso.info("In handleOnSuccess>>" + object);
        if (object != null) {
            RegisterUserResponse response = (RegisterUserResponse) object;
            UserPreference.getInstance().setUserID(response.getId());
            UserPreference.getInstance().setCurrentScreen(Screen.EmailScreen);
            UserPreference.getInstance().setcheckedIsConnected(true);
            UserPreference.getInstance().setIsCreateWalletPin(true);
            UserPreference.getInstance().setCartID(response.getCart_id());
            UserPreference.getInstance().setAccessToken(response.gettoken());
            setHelpPreference();
            Bundle bundle = new Bundle();
            bundle.putString("email", mEmail);
            bundle.putString("username", mUsername);
            bundle.putString("profilePic", mProfilePic);
            String current_screen = response.getCurrent_screen();

            if (!TextUtils.isEmpty(current_screen)) {
                showHome(current_screen);
            } else {
                if (isFromFacebook && checkInternet()) {
                    connectWithFacebook(id, mEmail, gender, name, mUsername, birthday, profileLink, location);
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
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            RegisterUserResponse response = (RegisterUserResponse) object;
            if (response.getMessage().equals("userexist") && response.getId() != null) {
                UserPreference.getInstance().setUserID(response.getId());
                UserPreference.getInstance().setEmail(response.getEmail());
                UserPreference.getInstance().setUserName(response.getUsername());
                UserPreference.getInstance().setCartID(response.getCart_id());
                UserPreference.getInstance().setProfilePic(response.getProfile_pic());
                UserPreference.getInstance().setBgImage(response.getBackground_pic());
                UserPreference.getInstance().setAccessToken(response.gettoken());
                if (social.equals(UserPreference.FACEBOOK))
                    UserPreference.getInstance().setIsFbConnected(true);
                UserPreference.getInstance().setIsCreateWalletPin(true);
                UserPreference.getInstance().setcheckedIsConnected(true);
                String current_screen = response.getCurrent_screen();
                if (!TextUtils.isEmpty(current_screen)) {
                    showHome(current_screen);
                } else
                    startActivity(HomeActivity.class);
//				if (isFromFacebook && checkInternet()) {
//					connectWithFacebook(id, mEmail, gender, name, mUsername, birthday, profileLink, location);
//				} else if (isFromTwitter && checkInternet()){
//					twitterInfoUpload();
//				}

            } else {
                AlertUtils.showToast(context, response.getMessage());
            }
        } else {
            AlertUtils.showToast(context, R.string.invalid_response);
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

    public void test(View v) {
//		startActivity(FollowCategoriesActivity.class);
    }

    private void showHome(String currentScreen) {
        Intent i = null;
        if (currentScreen.equals(Screen.HomeScreen)) {
            i = new Intent(context, HomeActivity.class);
        } else if (currentScreen.equals(Screen.EmailScreen)) {
            i = new Intent(context, EmailActivity.class);
        } else if (currentScreen.equals(Screen.UserNameScreen)) {
            i = new Intent(context, EditProfileActivity.class);
        } else if (currentScreen.equals(Screen.CategoryScreen)) {
            i = new Intent(context, FollowCategoriesActivity.class);
        } else if (currentScreen.equals(Screen.BrandScreen)) {
            i = new Intent(context, FollowBrandsActivity.class);
        } else if (currentScreen.equals(Screen.StoreScreen)) {
            i = new Intent(context, FollowStoreActivity.class);
        } else if (currentScreen.equals(Screen.CardScreen)) {
            i = new Intent(context, KikrTutorialActivity.class);
        } else {
            i = new Intent(context, HomeActivity.class);
        }
        startActivity(i);
        finish();
    }


    private void connectWithFacebook(final String id, final String email, final String gender, final String name, final String username, final String birthday, final String profile_link, final String location) {
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
                            AlertUtils.showToast(context, R.string.invalid_response);
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
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
        service.connectWithFacebook(id, gender, birthday, profile_link, location, name, username);
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

    private void uploadFbFriends(final List<FbUser> fbusers) {
        progressBarDialog = new ProgressBarDialog(context);
        progressBarDialog.show();
        final FbTwFriendsApi service = new FbTwFriendsApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        progressBarDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("email", mEmail);
                        bundle.putString("username", mUsername);
                        bundle.putString("profilePic", mProfilePic);
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
        service.addFacebookFriend(UserPreference.getInstance().getUserID(), fbusers);
        service.execute();
    }

    private void gotoFirstScreen(Bundle bundle) {
        startActivity(EmailActivity.class, bundle);
    }

    private void showDialog(String message, final Method method1, final Method method2) {
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

package com.flatlay.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import com.flatlay.BaseFragment;
import com.flatlay.R;

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
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.DeviceUtils;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class LandingActivity extends BaseFragment implements OnClickListener, ServiceCallback {
    private Button mFacebookButton, mEmailButton, mLoginButton;
    private final int REQUEST_CODE_FB_LOGIN = 1000;
    private final String DEFAULT_GENDER = "male";
    private String social, mEmail, mProfilePic, mUsername, name, birthday, location, gender, id,
            profileLink, referred_username, referred_userprofilepic;
    private boolean isFromFacebook = false;
    private LinearLayout layoutReferred;
    private RoundImageView user_profile_image;
    public final static String TAG="LandingActivity";
    private TextView user_profile_name, earn250;
    public static String referred_userid = "-1";
    private SharedPreferences userSettings, temp;
    private View mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainView = inflater.inflate(R.layout.landing2, container, false);
        Log.w(TAG,"LandingActivity");
        temp = getActivity().getSharedPreferences("fromRemove", 0);
        userSettings = getActivity().getSharedPreferences("UserSettings", 0);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString("isSet", "False");
        editor.apply();
        return mainView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emailButton:
                CommonUtility.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.baseFrameLayout, new SignUpActivity(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.facebookButton:
                if (checkInternet()) {
                    isFromFacebook = true;
                    social = UserPreference.FACEBOOK;
                    Intent i = new Intent(getActivity(), FbSignActivity.class);
                    i.putExtra("getFriendList", false);
                    i.putExtra("getProfilePic", false);
                    startActivityForResult(i, REQUEST_CODE_FB_LOGIN);
                }
                break;
            case R.id.loginButton:
                CommonUtility.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.baseFrameLayout, new LoginActivity(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.earn250:
                CommonUtility.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.baseFrameLayout, new IntroductionActivity(), null)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        temp = getActivity().getSharedPreferences("fromRemove", 0);
        userSettings = getActivity().getSharedPreferences("UserSettings", 0);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString("isSet", "False");
        editor.apply();
        mFacebookButton = (Button) mainView.findViewById(R.id.facebookButton);
        mEmailButton = (Button) mainView.findViewById(R.id.emailButton);
        layoutReferred = (LinearLayout) mainView.findViewById(R.id.layoutReferred);
        user_profile_image = (RoundImageView) mainView.findViewById(R.id.user_profile_image);
        user_profile_name = (TextView) mainView.findViewById(R.id.user_profile_name);
        earn250 = (TextView) mainView.findViewById(R.id.earn250);
        mLoginButton = (Button) mainView.findViewById(R.id.loginButton);

        if (getActivity().getIntent().getStringExtra("referred_userid") != null) {
            referred_userid = getActivity().getIntent().getStringExtra("referred_userid");
            referred_username = getActivity().getIntent().getStringExtra("referred_username");
            referred_userprofilepic = getActivity().getIntent().getStringExtra("referred_userprofilepic");
            layoutReferred.setVisibility(View.VISIBLE);
            user_profile_name.setTypeface(null, Typeface.BOLD);
            CommonUtility.setImage(getActivity(), referred_userprofilepic, user_profile_image, R.drawable.profile_icon);
            user_profile_name.setText(referred_username);
            earn250.setVisibility(View.VISIBLE);
        } else {
            mEmailButton.setVisibility(View.VISIBLE);
        }
        setUpTextType();
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    public void setUpTextType() {
        mFacebookButton.setTypeface(FontUtility.setMontserratRegular(getActivity()));
        mEmailButton.setTypeface(FontUtility.setMontserratRegular(getActivity()));
        mLoginButton.setTypeface(FontUtility.setMontserratRegular(getActivity()));
        earn250.setTypeface(FontUtility.setMontserratLight(getActivity()));
    }

    @Override
    public void setClickListener() {
        mFacebookButton.setOnClickListener(this);
        mEmailButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        earn250.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FB_LOGIN && resultCode == RESULT_OK) {
            isFromFacebook = true;
            id = data.getStringExtra("id");
            String email = data.getStringExtra("email");
            String gender = data.getStringExtra("gender");
            mUsername = data.getStringExtra("name");
            mProfilePic = data.getStringExtra("profile_pic");
            if(mProfilePic!=null){
            Log.e("Imaggeeeee-on",mProfilePic);
            UserPreference.getInstance().setProfilePic(mProfilePic);}
            String g = gender != null ? gender : DEFAULT_GENDER;
            mEmail = email;
            name = data.getStringExtra("name");
            birthday = data.getStringExtra("birthday");
            location = data.getStringExtra("location");
            profileLink = data.getStringExtra("profile_link");
            UserPreference.getInstance().setmIsFacebookSignedIn(true);
            registerViaFbSocial(id, g);
        } else if (requestCode == AppConstants.REQUEST_CODE_FB_FRIEND_LIST) {
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
        final RegisterUserApi service = new RegisterUserApi(this);
        service.registerViaFbSocial(mEmail, social, id, g, DeviceUtils.getPhoneModel(), CommonUtility.getDeviceTocken(getActivity()), "Dummy", "android", CommonUtility.getDeviceId(getActivity()));
        service.execute();
    }


    @Override
    public void handleOnSuccess(Object object) {
        if (object != null) {
            RegisterUserResponse response = (RegisterUserResponse) object;
            UserPreference.getInstance().setUserID(response.getId());
            UserPreference.getInstance().setCurrentScreen(Screen.EmailScreen);
            UserPreference.getInstance().setcheckedIsConnected(true);
            UserPreference.getInstance().setIsCreateWalletPin(true);
            UserPreference.getInstance().setCartID(response.getCart_id());
            UserPreference.getInstance().setAccessToken(response.gettoken());
            if(mProfilePic!=null) {
                Log.e("Imaggeeeee-s", response.getProfile_pic());
                UserPreference.getInstance().setProfilePic(response.getProfile_pic());
            }
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
            }
            UserPreference.getInstance().setPassword();
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (object != null) {
            RegisterUserResponse response = (RegisterUserResponse) object;
            if (response.getMessage().equals("userexist") && response.getId() != null) {
                UserPreference.getInstance().setUserID(response.getId());
                UserPreference.getInstance().setEmail(response.getEmail());
                UserPreference.getInstance().setUserName(response.getUsername());
                UserPreference.getInstance().setCartID(response.getCart_id());
                //  UserPreference.getInstance().setProfilePic(response.getProfile_pic());
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
            } else {
                AlertUtils.showToast(getActivity(), response.getMessage());
            }
        } else {
            AlertUtils.showToast(getActivity(), R.string.invalid_response);
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
    }


    private void showHome(String currentScreen) {

//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.baseFrameLayout, new LandingActivity(), null)
//                .addToBackStack(null)
//                .commit();
        startActivity(HomeActivity.class);
    }


    private void connectWithFacebook(final String id, final String email, final String gender, final String name, final String username, final String birthday, final String profile_link, final String location) {

        final ConnectWithFacebookApi service = new ConnectWithFacebookApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        if (object != null) {
                            getFBFriendList();
                        } else {
                            AlertUtils.showToast(getActivity(), R.string.invalid_response);
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        if (object != null) {
                            getFBFriendList();
                        }
                    }
                });
        service.connectWithFacebook(id, gender, birthday, profile_link, location, name, username);
        service.execute();
    }

    private void getFBFriendList() {
        Intent i = new Intent(getActivity(), FbSignActivity.class);
        i.putExtra("getFriendList", true);
        i.putExtra("getProfilePic", false);
        startActivityForResult(i, AppConstants.REQUEST_CODE_FB_FRIEND_LIST);
    }

    private void uploadFbFriends(final List<FbUser> fbusers) {
        final FbTwFriendsApi service = new FbTwFriendsApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        Bundle bundle = new Bundle();
                        bundle.putString("email", mEmail);
                        bundle.putString("username", mUsername);
                        bundle.putString("profilePic", mProfilePic);
                        gotoFirstScreen(bundle);
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {
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
        new AlertDialog.Builder(getActivity())
                .setTitle("Alert")
                .setMessage(message)
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        method1.execute();
                    }
                })
                .setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
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


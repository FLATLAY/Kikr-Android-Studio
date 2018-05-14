package com.flatlay.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.FbUser;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FbSignActivity extends BaseActivity {

    public static final String KEY_USER = "KEY_USER";
    public final static String TAG = "FbSignActivity";

    private LoginButton btnFacebookLogin;
    private CallbackManager callbackManager;
    private boolean isFirstTime = true;
    private boolean isGetFriendList = false;
    private boolean isGetProfilePic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "FbSignActivity");
        FacebookSdk.sdkInitialize(getApplicationContext());
        CommonUtility.noTitleActivity(context);
        isGetFriendList = getIntent().getBooleanExtra("getFriendList", false);
        isGetProfilePic = getIntent().getBooleanExtra("getProfilePic", false);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_fb_sign_in);
        btnFacebookLogin = (LoginButton) findViewById(R.id.authButton);
        hideHeader();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, "onResume()");
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null && !token.isExpired()) {
            getFacebookMeInfo(token);

        } else if (isFirstTime) {
            isFirstTime = false;
            btnFacebookLogin.setReadPermissions(Arrays.asList("public_profile", "email"));
            btnFacebookLogin.performClick();
            btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            String accessToken = loginResult.getAccessToken().getToken();
                            if (loginResult.getAccessToken() != null && !loginResult.getAccessToken().isExpired()) {
                                getFacebookMeInfo(loginResult.getAccessToken());
                            }
                        }

                        @Override
                        public void onCancel() {
                            finish();
                        }

                        @Override
                        public void onError(FacebookException e) {
                            e.printStackTrace();
                            AlertUtils.showToast(context, "Facebook login failed.");
                            finish();
                            return;
                        }
                    }
            );
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getFacebookMeInfo(AccessToken accessToken) {
        Log.w(TAG, "getFacebookMeInfo");
        GraphRequest me = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject user, GraphResponse response) {
                if (user != null) {
                    if (isGetFriendList) {
                        AccessToken token = AccessToken.getCurrentAccessToken();
                        if (token != null && !token.isExpired()) {
                            requestMyAppFacebookFriends(token);
                        }
                    } else if (isGetProfilePic) {
                        getProfilePic(user);
                    } else {
                        buildUserDataAndDoSignIn(user);
                    }
                } else {
                    AlertUtils.showToast(context,
                            "Error in getting Facebook Profile Data.");
                    finish();
                }
            }
        });
        Bundle params = me.getParameters();
        params.putString("fields", "email,name");
        me.setParameters(params);
        me.executeAsync();
    }

    private void getProfilePic(JSONObject user) {
        Log.w("FbSignActivity", "getProfilePic");
        URL url = null;
        String fb_id = "";
        try {
            fb_id = user.getString("id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            url = new URL("http://graph.facebook.com/" + fb_id + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.putExtra("profile_pic", url.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void buildUserDataAndDoSignIn(JSONObject graphUser) {
        Log.w("FbSignActivity", "buildUserDataAndDoSignIn:" + graphUser.toString());

        String fb_id = "", name = "", email = "", username = "", gender = "", birthday = "", link = "", location = "";

        try {
            fb_id = graphUser.getString("id");
            name = graphUser.getString("name");
            Log.w("FbSignActivity", "Name: " + name);
            email = graphUser.getString("email");
            UserPreference.getInstance().setEmail(name);
            UserPreference.getInstance().setEmail(email);

            if (graphUser.has("username")) {
                username = graphUser.getString("username");
            }
            if (graphUser.has("gender")) {
                gender = graphUser.getString("username");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        URL url = null;
        try {
            url = new URL("http://graph.facebook.com/" + fb_id + "/picture?type=large");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String address = "";

        /*
        String birthday = graphUser.getBirthday() != null ? graphUser.getBirthday() : "";
        String link = graphUser.getLink() != null ? graphUser.getLink() : "";
        GraphPlace graphPlace = graphUser.getLocation();
        GraphLocation location = graphPlace != null ? graphPlace.getLocation() : null;

        if (location != null) {
            String street = location.getStreet() != null ? location.getStreet() : "";
            String city = location.getCity() != null ? location.getCity() : "";
            String state = location.getState() != null ? location.getState() : "";
            String country = location.getCountry() != null ? location.getCountry() : "";
            String zipCode = location.getZip() != null ? location.getZip() : "";
            address = street + " " + city + " " + state + " " + country + " " + zipCode;
        }
*/

        Intent intent = new Intent();
        intent.putExtra("id", fb_id);
        intent.putExtra("email", email);
        intent.putExtra("gender", gender);
        intent.putExtra("name", name);
        intent.putExtra("username", username);
        intent.putExtra("birthday", birthday);
        intent.putExtra("profile_link", link);
        intent.putExtra("location", address);
        intent.putExtra("profile_pic", url.toString());
        setResult(RESULT_OK, intent);
        Syso.info("address>>" + address);
        finish();

    }

    @Override
    public void initLayout() {
        btnFacebookLogin = (LoginButton) findViewById(R.id.authButton);
        btnFacebookLogin.setReadPermissions(Arrays.asList("email", "user_friends"));
    }

    @Override
    public void headerView() {
    }

    @Override
    public void setUpTextType() {
    }

    @Override
    public void setClickListener() {
    }

    @Override
    public void setupData() {
    }

    private GraphRequest createRequest(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newGraphPathRequest(accessToken, "me/friends", null);

        Set<String> fields = new HashSet<String>();
        String[] requiredFields = new String[]{"id", "name", "picture",
                "installed"};
        fields.addAll(Arrays.asList(requiredFields));

        Bundle parameters = request.getParameters();
        parameters.putString("fields", TextUtils.join(",", fields));
        request.setParameters(parameters);

        return request;
    }

    private void requestMyAppFacebookFriends(AccessToken accessToken) {

        GraphRequest friendsRequest = createRequest(accessToken);
        friendsRequest.setCallback(new GraphRequest.Callback() {

            @Override
            public void onCompleted(GraphResponse response) {

                ArrayList<FbUser> friends = getResults(response);
                Intent intent = new Intent();
                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("friend_list", friends);
                intent.putExtras(bundleObject);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        friendsRequest.executeAsync();
    }

    private ArrayList<FbUser> getResults(GraphResponse response) {
        return null;
    }

}

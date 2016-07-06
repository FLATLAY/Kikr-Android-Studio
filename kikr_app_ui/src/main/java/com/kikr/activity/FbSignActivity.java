package com.kikr.activity;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.FbUser;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FbSignActivity extends BaseActivity {
	
	public static final String KEY_USER = "KEY_USER";
	
	private LoginButton btnFacebookLogin;
	private UiLifecycleHelper uiHelper;
	
	private boolean isFirstTime = true;
	private boolean isGetFriendList = false;
	private boolean isGetProfilePic = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    CommonUtility.noTitleActivity(context);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		isGetFriendList=getIntent().getBooleanExtra("getFriendList", false);
		isGetProfilePic=getIntent().getBooleanExtra("getProfilePic", false);
		setContentView(R.layout.activity_fb_sign_in);
		hideHeader();
	}

	@Override
	public void onResume() {
	    super.onResume();
	    Session session = Session.getActiveSession();
	    if (session != null && session.isOpened()) {
	    	Syso.info("Facebook Logged in...Token = "+session.getAccessToken());
	    	getFacebookMeInfo(session);
	    } 
	    else if(isFirstTime) {
	    	isFirstTime = false;
	    	btnFacebookLogin.performClick();
	    }
	    uiHelper.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state, final Exception exception) {
			if(exception != null){
				Syso.info("Facebook exception = "+exception.getMessage());
				AlertUtils.showToast(context, "Facebook login failed.");
				finish();
				return;
			}
			if (session != null && state.isOpened()) {
		    	Syso.info("Facebook Logged in...Token = "+session.getAccessToken());
		    	getFacebookMeInfo(session);
		    }
		}
	};
	
	private void getFacebookMeInfo(Session session) {

		Request.newMeRequest(session, new GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					if (isGetFriendList) {
						Session session = Session.getActiveSession();
						if (session != null && session.isOpened()) {
							requestMyAppFacebookFriends(session);
						}
					}else if (isGetProfilePic) {
						getProfilePic(user);
					} else {
//						 sendRequestDialog();
						buildUserDataAndDoSignIn(user);
					}
				} else {
					AlertUtils.showToast(context,
							"Error in getting Facebook Profile Data.");
					finish();
				}
			}
		}).executeAsync();
	}
	

	private void getProfilePic(GraphUser user) {
		URL url = null;
		String fb_id = user.getId();
		try {
			url = new URL("http://graph.facebook.com/"+fb_id+"/picture?type=large");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		Intent intent = new Intent();
		intent.putExtra("profile_pic",url.toString());
		setResult(RESULT_OK, intent);
		finish();
	}

	private void buildUserDataAndDoSignIn(GraphUser graphUser) {
		
		String fb_id = graphUser.getId();
		String name=graphUser.getName();
		String username = graphUser.getUsername()!=null?graphUser.getUsername():"";		
		String email = (String) graphUser.getProperty("email");
		String gender = (String) graphUser.getProperty("gender");
		URL url = null;
		try {
			url = new URL("http://graph.facebook.com/"+fb_id+"/picture?type=large");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String birthday = graphUser.getBirthday()!=null?graphUser.getBirthday():"";
		String link = graphUser.getLink()!=null?graphUser.getLink():"";
		GraphPlace graphPlace = graphUser.getLocation();
		GraphLocation location =graphPlace!=null? graphPlace.getLocation():null;
		String address="";
		if(location!=null){
			String street=location.getStreet()!=null?location.getStreet():"";
			String city=location.getCity()!=null?location.getCity():"";
			String state=location.getState()!=null?location.getState():"";
			String country=location.getCountry()!=null?location.getCountry():"";
			String zipCode=location.getZip()!=null?location.getZip():"";
			address=street+" "+city+" "+state+" "+country+" "+zipCode;
		}

		Intent intent = new Intent();
		intent.putExtra("id", fb_id);
		intent.putExtra("email", email);
		intent.putExtra("gender", gender);
		intent.putExtra("name", name);
		intent.putExtra("username",username);
		intent.putExtra("birthday",birthday);
		intent.putExtra("profile_link",link);
		intent.putExtra("location",address);
		intent.putExtra("profile_pic",url.toString());
		setResult(RESULT_OK, intent);
		Syso.info("address>>"+address);
		getDetailsFromFacebook(graphUser);
		finish();
	}
	private void getDetailsFromFacebook(GraphUser graphUser) {
		  String id = graphUser.getId();
		  String Birthday = graphUser.getBirthday();
		  String Link = graphUser.getLink();
		  GraphPlace Location = graphUser.getLocation();
		  String Name = graphUser.getName();
		  String Username = graphUser.getUsername();
		  String Gender = (String) graphUser.getProperty("gender");
		  Syso.info(id+"\n");
		  Syso.info(Birthday+"\n");
		  Syso.info(Link+"\n");
		  Syso.info("Location>>"+Location+"\n");
		  Syso.info(Name+"\n");
		  Syso.info(Username+"\n");
		  Syso.info(Gender+"\n");
		 }
	


	@Override
	public void initLayout() {
		btnFacebookLogin = (LoginButton) findViewById(R.id.authButton);
		btnFacebookLogin.setReadPermissions(Arrays.asList("email","user_friends"));
	}

	@Override
	public void headerView() {
		
	}

	@Override
	public void setUpTextType() {}

	@Override
	public void setClickListener() {}

	@Override
	public void setupData() {}
	
	private Request createRequest(Session session) {
		
	    Request request = Request.newGraphPathRequest(session, "me/friends", null);

	    Set<String> fields = new HashSet<String>();
	    String[] requiredFields = new String[] { "id", "name", "picture",
	            "installed" };
	    fields.addAll(Arrays.asList(requiredFields));

	    Bundle parameters = request.getParameters();
	    parameters.putString("fields", TextUtils.join(",", fields));
	    request.setParameters(parameters);

	    return request;
	}
	
	
	private void requestMyAppFacebookFriends(Session session) {
		
	    Request friendsRequest = createRequest(session);
	    friendsRequest.setCallback(new Request.Callback() {

	        @Override
	        public void onCompleted(Response response) {
	        	
	        	ArrayList<FbUser> friends =  getResults(response);
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
	
	private ArrayList<FbUser> getResults(Response response) {
	    GraphMultiResult multiResult = response.getGraphObjectAs(GraphMultiResult.class);
	    GraphObjectList<GraphObject> data = multiResult.getData();
	    List<GraphUser> graphUsers=data.castToListOf(GraphUser.class);
	    ArrayList<FbUser> friendList=new ArrayList<FbUser>();
	    try{
		    for(int i=0;i<graphUsers.size();i++){
		    	FbUser fbUser=new FbUser();
		    	GraphUser graphUser=graphUsers.get(i);
		    	
		    	String id=graphUser.getId();
		    	JSONObject json=graphUser.getInnerJSONObject();
				String  url=json.getJSONObject("picture").getJSONObject("data").getString("url");
				String friendName=graphUser.getName();
				fbUser.setFb_id(id);
				fbUser.setName(friendName);
				fbUser.setProfile_pic(url);
				friendList.add(fbUser);
		    }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return friendList;
	}
		
	private void sendRequestDialog() {

	    Bundle params = new Bundle();
	    params.putString("message", "Learn how to make your Android apps social");

	    WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(context,Session.getActiveSession(),params))
	            .setOnCompleteListener(new OnCompleteListener() {

	                @Override
	                public void onComplete(Bundle values,
	                    FacebookException error) {
	                    if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                            Toast.makeText(context, "Request cancelled", Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(context,  "Network Error", Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        if (requestId != null) {
	                            Toast.makeText(context,  "Request sent",  Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(context,   "Request cancelled",  Toast.LENGTH_SHORT).show();
	                        }
	                    }   
	                }
	            })
	            .build();
	    requestsDialog.show();
	}
}

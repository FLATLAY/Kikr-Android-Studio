package com.kikr.twitter;


import java.util.ArrayList;

import twitter4j.PagableResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.kikr.R;
import com.kikr.sessionstore.SessionStore;
import com.kikr.twitter.TwitterOAuthView.Result;
import com.kikr.utility.AppConstants;
import com.kikr.utility.CommonUtility;
import com.kikrlib.utils.Syso;


public class TwitterOAuthActivity extends Activity implements TwitterOAuthView.Listener
{

	public static final String CONSUMER_KEY ="5tQwAp0Z802BLmdF709OEsnLD";
	public static final String CONSUMER_SECRET = "1RLaG0VSYdTXHfSRppPvVp3K7cE1T5q4QiUGLxrtbiFgv3t0W8";

    private static final String CALLBACK_URL = "http://www.flat-lay.com";
    private static final boolean DUMMY_CALLBACK_URL = true;
    private TwitterOAuthView view;
    private boolean oauthStarted;
	private ArrayList<OauthItem> twitterFriendlist;
	private long cursor=-1;
	private Activity context;
	private boolean isGetFollowers=false;
	private boolean isGetProfilePic = false;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = (TwitterOAuthActivity) this;
        CommonUtility.fullScreenActivity(context);
        view = new TwitterOAuthView(this);
        view.setDebugEnabled(true);
        setContentView(view);
        oauthStarted = false;
        isGetFollowers=getIntent().getBooleanExtra("is_get_list", false);
        isGetProfilePic=getIntent().getBooleanExtra("getProfilePic", false);
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        if (oauthStarted)
        {
            return;
        }

        oauthStarted = true;
        view.start(CONSUMER_KEY, CONSUMER_SECRET, CALLBACK_URL, DUMMY_CALLBACK_URL, this);
    }


    public void onSuccess(TwitterOAuthView view, final AccessToken accessToken)
    {

        SessionStore.saveTwitterAccessToken(accessToken.getToken(), this);
        SessionStore.saveTwitterSecretToken(accessToken.getTokenSecret(), this);
        SessionStore.saveScreenName(accessToken.getScreenName(), this);
        SessionStore.saveTwitterLogedIn(true, this);
        
        Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if(isGetFollowers)
						getFollowerList(accessToken);
					else if (isGetProfilePic) {
						getProfilePic(accessToken);
					}else
						getUserInfo(accessToken);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		thread.start();
    }

    private void getUserInfo(AccessToken accessToken) {


		ConfigurationBuilder confbuilder = new ConfigurationBuilder();

		confbuilder.setOAuthAccessToken(accessToken.getToken())
				.setOAuthAccessTokenSecret(accessToken.getTokenSecret())
				.setOAuthConsumerKey(TwitterOAuthActivity.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterOAuthActivity.CONSUMER_SECRET);
		Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();

		try {

			long id=twitter.getId();
			User user=twitter.showUser(id);
			long userId=user.getId();
			String description = user.getDescription();
			String language = user.getLang();
			String location = user.getLocation();
			String name = user.getName();
			String profile_image_url = user.getOriginalProfileImageURL();
			String screen_name = user.getScreenName();
			String status = user.getStatus().getText();
			String time_zone = user.getTimeZone();
			
			Intent intent = new Intent();
			intent.putExtra("id", userId);
			intent.putExtra("description", description);
			intent.putExtra("language", language);
			intent.putExtra("location", location);
			intent.putExtra("profile_image_url", profile_image_url);
			intent.putExtra("name", name);
			intent.putExtra("screen_name", screen_name);
			intent.putExtra("status", status);
			intent.putExtra("time_zone", time_zone);

			setResult(RESULT_OK, intent);
			finish();
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}

	
	}
    
    private void getProfilePic(AccessToken accessToken) {
		ConfigurationBuilder confbuilder = new ConfigurationBuilder();
		confbuilder.setOAuthAccessToken(accessToken.getToken())
				.setOAuthAccessTokenSecret(accessToken.getTokenSecret())
				.setOAuthConsumerKey(TwitterOAuthActivity.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterOAuthActivity.CONSUMER_SECRET);
		Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();
		try {
			long id=twitter.getId();
			User user=twitter.showUser(id);
			String profile_image_url = user.getOriginalProfileImageURL();
			Intent intent=new Intent();
			intent.putExtra("profile_image_url", profile_image_url);
			setResult(RESULT_OK, intent);
			finish();
		} catch (TwitterException e) {
			e.printStackTrace();
		}

	
	}
    
    
    public static User getUserInfo(FragmentActivity context) {


		ConfigurationBuilder confbuilder = new ConfigurationBuilder();

		confbuilder.setOAuthAccessToken(SessionStore.getTwitterAccessToken(context))
				.setOAuthAccessTokenSecret(SessionStore.getTwitterSecretToken(context))
				.setOAuthConsumerKey(TwitterOAuthActivity.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterOAuthActivity.CONSUMER_SECRET);
		Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();

		try {

			long id=twitter.getId();
			User user=twitter.showUser(id);
			long userId=user.getId();
			return user;
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}

	
	}
    
    public static ArrayList<OauthItem> getFollowerList(FragmentActivity context) {

		ConfigurationBuilder confbuilder = new ConfigurationBuilder();

		confbuilder.setOAuthAccessToken(SessionStore.getTwitterAccessToken(context))
				.setOAuthAccessTokenSecret(SessionStore.getTwitterSecretToken(context))
				.setOAuthConsumerKey(TwitterOAuthActivity.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterOAuthActivity.CONSUMER_SECRET);
		Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();

		PagableResponseList<User> followersList;

		ArrayList<OauthItem> twitterFriendlist = new ArrayList<OauthItem>();
		long cursor=-1;
		try {
			followersList = twitter.getFollowersList(SessionStore.getScreenName(context), cursor);

			for (int i = 0; i < followersList.size(); i++) {
				User user = followersList.get(i);
				String name = user.getName();
				String twitterId = Long.toString(user.getId());
				String userImage=user.getBiggerProfileImageURL();
				OauthItem item=new OauthItem();
				item.setFriendName(name);
				item.setFriendId(twitterId);
				item.setFriendImage(userImage);
				twitterFriendlist.add(item);
			}
			Syso.info("Twitter friend list>"+followersList);
			return twitterFriendlist;
		} catch (TwitterException e) {
			e.printStackTrace();
			return null;
		}
	}
    
    private void getFollowerList(AccessToken accessToken) {

		ConfigurationBuilder confbuilder = new ConfigurationBuilder();

		confbuilder.setOAuthAccessToken(accessToken.getToken())
				.setOAuthAccessTokenSecret(accessToken.getTokenSecret())
				.setOAuthConsumerKey(TwitterOAuthActivity.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterOAuthActivity.CONSUMER_SECRET);
		Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();

		PagableResponseList<User> followersList;

		twitterFriendlist = new ArrayList<OauthItem>();
		try {
			followersList = twitter.getFollowersList(accessToken.getScreenName(), cursor);

			for (int i = 0; i < followersList.size(); i++) {
				User user = followersList.get(i);
				String name = user.getName();
				String twitterId = Long.toString(user.getId());
				
				OauthItem item=new OauthItem();
				item.setFriendName(name);
				item.setFriendId(twitterId);
				
				twitterFriendlist.add(item);
			}
			Syso.info("Twitter friend list>"+followersList);
			Intent twitterIntent = new Intent();
			Bundle bundleObject = new Bundle();
			bundleObject.putSerializable("friend_list", twitterFriendlist);
			twitterIntent.putExtras(bundleObject);
			setResult(RESULT_OK, twitterIntent);
			finish();
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
   
	public void onFailure(TwitterOAuthView view, Result result)
    {
        showMessage("Failed due to " + result);
    }


    private void showMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    
    private void getDetailsFromTwitter(User user) {
		Syso.info("in details");
		long userId = user.getId();
		String Description = user.getDescription();
		String Lang = user.getLang();
		String Location = user.getLocation();
		String Name = user.getName();
		String OriginalProfileImageURL = user.getOriginalProfileImageURL();
		String ScreenName = user.getScreenName();
		Status Status = user.getStatus();
		String TimeZone = user.getTimeZone();
		Syso.info(userId+"\n");
		Syso.info(Description+"\n");
		Syso.info(Lang+"\n");
		Syso.info(Location+"\n");
		Syso.info(Name+"\n");
		Syso.info(OriginalProfileImageURL+"\n");
		Syso.info(ScreenName+"\n");
		Syso.info(Status+"\n");
		Syso.info(TimeZone+"\n");
	}
    
    public static String sendMessageToTwitterFriends(FragmentActivity context,String followerId) {
    	  
    	  ConfigurationBuilder confbuilder = new ConfigurationBuilder();
    	        confbuilder.setOAuthAccessToken(SessionStore.getTwitterAccessToken(context))
    	                .setOAuthAccessTokenSecret(SessionStore.getTwitterSecretToken(context))
    	                .setOAuthConsumerKey(TwitterOAuthActivity.CONSUMER_KEY)
    	                .setOAuthConsumerSecret(TwitterOAuthActivity.CONSUMER_SECRET);
    	        Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance();

    	        try {
    	          twitter.sendDirectMessage(Long.parseLong(followerId), AppConstants.INVITE_FRIEND_MESSAGE2);
    	            return  "Invite sent successfully";
    	        }
    	        catch (TwitterException e) { 
    	            e.printStackTrace();
    	            return e.getErrorMessage();
    	        }
    	 }
}
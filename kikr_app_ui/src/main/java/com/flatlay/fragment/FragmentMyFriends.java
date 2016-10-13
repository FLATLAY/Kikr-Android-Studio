package com.flatlay.fragment;

import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.FacebookTwitterFriendListAdapter;
import com.flatlay.sessionstore.SessionStore;
import com.flatlay.twitter.TwitterOAuthActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants;
import com.kikrlib.api.FbTwFriendsApi;
import com.kikrlib.bean.SocialFriend;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.FbTwFriendsRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentMyFriends extends BaseFragment implements OnClickListener,ServiceCallback {

	private View mainView;
	private ListView myFriends_List;
	private ProgressBarDialog mProgressBarDialog;
	private List<SocialFriend> fbFriendLists;
	private List<SocialFriend> twitterFriendLists;
	private List<SocialFriend> followersList;
	private ProgressBarDialog progressBarDialog;
	private String selectedTweetId="";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_myfriends, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		myFriends_List=(ListView) mainView.findViewById(R.id.myFriends_List);
	}
	
	private void getFriendsList() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final FbTwFriendsApi fbTwFriendsApi = new FbTwFriendsApi(this);
		fbTwFriendsApi.getFriendsList(UserPreference.getInstance().getUserID());
		fbTwFriendsApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				fbTwFriendsApi.cancel();
			}
		});
	}
	
	@Override
	public void handleOnSuccess(Object object) {
		mProgressBarDialog.dismiss();
		hideDataNotFound();
		Syso.info("In handleOnSuccess>>" + object);
		FbTwFriendsRes fbTwFriendsRes = (FbTwFriendsRes) object;
		fbFriendLists = fbTwFriendsRes.getFb_friend_list();
		twitterFriendLists = fbTwFriendsRes.getTwitter_friend_list();
		followersList = fbTwFriendsRes.getFollowers_list();
		if (twitterFriendLists.size()>0) 
			followersList.addAll(fbFriendLists);
		if (followersList.size()>0) 
			followersList.addAll(twitterFriendLists);
		if (followersList.size() == 0)
			showDataNotFound();
		else
		if (followersList.size()>0) {
			myFriends_List.setAdapter(new FacebookTwitterFriendListAdapter(mContext, followersList,this));
		} else{
			AlertUtils.showToast(mContext, R.string.no_friends_found_msg);
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			FbTwFriendsRes response = (FbTwFriendsRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}

	@Override
	public void onClick(View v) {
		
		
	}

	@Override
	public void setData(Bundle bundle) {
		if(checkInternet())
			getFriendsList();
		else
			showReloadOption();
		
	}

	@Override
	public void refreshData(Bundle bundle) {
		
		
	}

	@Override
	public void setClickListener() {
		
		
	}

	
	private void showReloadOption() {
		showDataNotFound();
		TextView textView=getDataNotFound();
		if(textView!=null){
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternet())
						getFriendsList();
				}
			});
		}
	}
	
	public void tweetBackFromLogin(){
		if(!selectedTweetId.equals("")){
			tweet(selectedTweetId);
		}else{
			AlertUtils.showToast(mContext, "Unable to tweet please try again!");
		}
	}
	
	public void tweet(String twitter_id){
		if(TextUtils.isEmpty(twitter_id)){
			AlertUtils.showToast(mContext, "No user id found, unable to invite");
			return;
		}
		
		selectedTweetId=twitter_id;
		boolean logedIn = SessionStore.isTwitterLogedIn(mContext);
		if (logedIn) {
			new GetTwitterInfo().execute(twitter_id);
		} else {
			Intent intent = new Intent(mContext, TwitterOAuthActivity.class);
			intent.putExtra("is_get_list", false);
			startActivityForResult(intent, AppConstants.REQUEST_CODE_TWIT_TO_FRIEND);
		}
	}
	
	private class GetTwitterInfo extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBarDialog = new ProgressBarDialog(mContext);
			progressBarDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return TwitterOAuthActivity.sendMessageToTwitterFriends(mContext,params[0]);
		}

		@Override
		protected void onPostExecute(String message) {
			super.onPostExecute(message);
			progressBarDialog.dismiss();
			AlertUtils.showToast(mContext,message);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			tweetBackFromLogin();
		}else{
			AlertUtils.showToast(mContext, "User canceled");
		}
	}
}

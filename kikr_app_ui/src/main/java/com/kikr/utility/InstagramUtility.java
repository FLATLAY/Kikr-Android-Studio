package com.kikr.utility;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramRequest;
import net.londatiga.android.instagram.InstagramSession;
import net.londatiga.android.instagram.InstagramUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.AsyncTask;

import com.kikr.ui.ProgressBarDialog;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class InstagramUtility {
	private Context mContext;
	private InstagramSession mInstagramSession;
	private Instagram mInstagram;
	boolean isProfile; //true: profile, false: list
	String count = "10";
	InstagramCallBack instagramCallBack;
	boolean isShowLoader;
	
	public InstagramUtility(Context context,boolean isProfile,String count,boolean isShowLoader,InstagramCallBack instagramCallBack) {
		mContext = context;
		this.isProfile = isProfile;
		this.instagramCallBack = instagramCallBack;
		this.count = count;
		this.isShowLoader = isShowLoader;
	}

	public void inItInstgram(){
		mInstagram  		= new Instagram(mContext, AppConstants.INSTAGRAM_CLIENT_ID, AppConstants.INSTAGRAM_CLIENT_SECRET, AppConstants.INSTAGRAM_REDIRECT_URI);
		mInstagramSession	= mInstagram.getSession();
		
		if (mInstagramSession.isActive()) {			
			InstagramUser instagramUser = mInstagramSession.getUser();		
//			mInstagramSession.reset(); for logout
			doNextstep(instagramUser);
		} else {				
			mInstagram.authorize(new Instagram.InstagramAuthListener() {			
				@Override
				public void onSuccess(InstagramUser user) {
					doNextstep(user);
				}
							
				@Override
				public void onError(String error) {		
					AlertUtils.showToast(mContext, error);
				}

				@Override
				public void onCancel() {
					AlertUtils.showToast(mContext, "OK. Maybe later?");
				}
			});	
		}
	}
	
	private void doNextstep(InstagramUser instagramUser) {
		if(isProfile){
//			String fullName = instagramUser.fullName;
//			String userName = instagramUser.username;
			String profilePicUrl = instagramUser.profilPicture;
			instagramCallBack.setProfilePic(profilePicUrl);
		}else{
			new DownloadTask().execute();
		}
	}

	public class DownloadTask extends AsyncTask<URL, Integer, Long> {
		ArrayList<String> photoList;
		ProgressBarDialog barDialog;
		
		protected void onCancelled() {
			
		}
		
    	protected void onPreExecute() {
//    		if(isShowLoader){
    			barDialog = new ProgressBarDialog(mContext);
    			barDialog.show();
//    		}
    	}
    
        protected Long doInBackground(URL... urls) {         
            long result = 0;
      
    		try {
    			List<NameValuePair> params = new ArrayList<NameValuePair>(1);
    			
    			params.add(new BasicNameValuePair("count", count));
    			InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
    			String response	= request.createRequest("GET", "/users/self/media/recent", params);
    			if (!response.equals("")) {
    				JSONObject jsonObj  = (JSONObject) new JSONTokener(response).nextValue();    				
    				JSONArray jsonData	= jsonObj.getJSONArray("data");
    				int length = jsonData.length();
    				
    				if (length > 0) {
    					photoList = new ArrayList<String>();
    					for (int i = 0; i < length; i++) {
    						JSONObject jsonPhoto = jsonData.getJSONObject(i).getJSONObject("images").getJSONObject("low_resolution");
    						photoList.add(jsonPhoto.getString("url"));
    					}
    				}
    			}
    		} catch (Exception e) { 
    			e.printStackTrace();
    		}
    		
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {              	
        }

        protected void onPostExecute(Long result) {
        	if(barDialog!=null)
        		barDialog.dismiss();
        	if (photoList == null) {
        		AlertUtils.showToast(mContext, "No Photos Available");
        	} else {
        		instagramCallBack.setPictureList(photoList);
        		Syso.info("Photo list>>>>>>>>"+photoList);
        	}
        }                
    }

}

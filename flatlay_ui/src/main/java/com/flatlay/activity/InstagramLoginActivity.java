package com.flatlay.activity;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flatlay.R;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class InstagramLoginActivity extends Activity {
	private InstagramSession mInstagramSession;
	private Instagram mInstagram;
	
	private ProgressBar mLoadingPb;
	private GridView mGridView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("Activity","InstagramLoginActivity");
		
		mInstagram  		= new Instagram(this, AppConstants.INSTAGRAM_CLIENT_ID, AppConstants.INSTAGRAM_CLIENT_SECRET, AppConstants.INSTAGRAM_REDIRECT_URI);
		
		mInstagramSession	= mInstagram.getSession();
		
		if (mInstagramSession.isActive()) {
			setContentView(R.layout.activity_user);
			
			InstagramUser instagramUser = mInstagramSession.getUser();
			
			mLoadingPb 	= (ProgressBar) findViewById(R.id.pb_loading);
			mGridView	= (GridView) findViewById(R.id.gridView);
			
			((TextView) findViewById(R.id.tv_name)).setText(instagramUser.fullName);
			((TextView) findViewById(R.id.tv_username)).setText(instagramUser.username);
			
			((Button) findViewById(R.id.btn_logout)).setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View arg0) {
					mInstagramSession.reset();
					
					startActivity(new Intent(InstagramLoginActivity.this, InstagramLoginActivity.class));
					
					finish();
				}
			});
			
			ImageView userIv = (ImageView) findViewById(R.id.iv_user);
			
//			DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
//					.showImageOnLoading(R.drawable.ic_user)
//					.showImageForEmptyUri(R.drawable.ic_user)
//					.showImageOnFail(R.drawable.ic_user)
//					.cacheInMemory(true)
//					.cacheOnDisc(false)
//					.considerExifParams(true)
//					.build();
//    
//			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)                		                       
//			        .writeDebugLogs()
//			        .defaultDisplayImageOptions(displayOptions)		        
//			        .build();
//		
//			ImageLoader imageLoader = ImageLoader.getInstance();
//			imageLoader.init(config);
//			
//			AnimateFirstDisplayListener animate  = new AnimateFirstDisplayListener();
//			
//			imageLoader.displayImage(instagramUser.profilPicture, userIv, animate); 
			CommonUtility.setImage(this, instagramUser.profilPicture, userIv, R.drawable.profile_icon);
			
			new DownloadTask().execute();
			
		} else {
//			setContentView(R.layout.activity_main);
//			
//			((Button) findViewById(R.id.btn_connect)).setOnClickListener(new View.OnClickListener() {			
//				@Override
//				public void onClick(View arg0) {					
//					mInstagram.authorize(mAuthListener);	
//				}
//			});
		}
	}
	
	private void showToast(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
	}
	
	private Instagram.InstagramAuthListener mAuthListener = new Instagram.InstagramAuthListener() {			
		@Override
		public void onSuccess(InstagramUser user) {
			finish();
			AlertUtils.showToast(InstagramLoginActivity.this, "User Authrized");
			startActivity(new Intent(InstagramLoginActivity.this, InstagramLoginActivity.class));
		}
					
		@Override
		public void onError(String error) {		
			showToast(error);
		}

		@Override
		public void onCancel() {
			showToast("OK. Maybe later?");
		}
	};
			
	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	public class DownloadTask extends AsyncTask<URL, Integer, Long> {
		ArrayList<String> photoList;
		
		protected void onCancelled() {
			
		}
		
    	protected void onPreExecute() {
    		
    	}
    
        protected Long doInBackground(URL... urls) {         
            long result = 0;
      
    		try {
    			List<NameValuePair> params = new ArrayList<NameValuePair>(1);
    			
    			params.add(new BasicNameValuePair("count", "10"));
    			
//    			https://api.instagram.com/v1/users/self/media/recent/?access_token=ACCESS-TOKEN
    			InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
//    			String response			 = request.createRequest("GET", "/users/self/feed", params);
    			String response			 = request.createRequest("GET", "/users/self/media/recent", params);

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
        	mLoadingPb.setVisibility(View.GONE);
        	
        	if (photoList == null) {
        		Toast.makeText(getApplicationContext(), "No Photos Available", Toast.LENGTH_LONG).show();
        	} else {
        		DisplayMetrics dm = new DisplayMetrics();
        		
        		getWindowManager().getDefaultDisplay().getMetrics(dm);
        	       
        		int width 	= (int) Math.ceil((double) dm.widthPixels / 2);
        		width=width-50;
        		int height	= width;
        		Syso.info("Photo list>>>>>>>>"+photoList);
        	
//        		PhotoListAdapter adapter = new PhotoListAdapter(mContext);
        		
//        		adapter.setData(photoList);
//        		adapter.setLayoutParam(width, height);
//        	
//        		mGridView.setAdapter(adapter);
        	}
        }                
    }
}
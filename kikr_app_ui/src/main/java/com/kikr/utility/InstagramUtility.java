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

import com.kikr.model.InstagramImage;
import com.kikr.ui.ProgressBarDialog;
import com.kikrlib.db.UserPreference;
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

    public InstagramUtility(Context context, boolean isProfile, String count, boolean isShowLoader, InstagramCallBack instagramCallBack) {
        mContext = context;
        this.isProfile = isProfile;
        this.instagramCallBack = instagramCallBack;
        this.count = count;
        this.isShowLoader = isShowLoader;


    }

    public void inItInstgram() {
        mInstagram = new Instagram(mContext, AppConstants.INSTAGRAM_CLIENT_ID, AppConstants.INSTAGRAM_CLIENT_SECRET, AppConstants.INSTAGRAM_REDIRECT_URI);
        mInstagramSession = mInstagram.getSession();

        if (mInstagramSession.isActive()) {
            InstagramUser instagramUser = mInstagramSession.getUser();
//   mInstagramSession.reset(); for logout
            doNextstep(instagramUser);
        } else {
            mInstagram.authorize(new Instagram.InstagramAuthListener() {
                @Override
                public void onSuccess(InstagramUser user) {
                    UserPreference.getInstance().setmIsInstagramSignedIn(true);
                    doNextstep(user);
                }

                @Override
                public void onError(String error) {
                    AlertUtils.showToast(mContext, error);
                }

                @Override
                public void onCancel() {
                    AlertUtils.showToast(mContext, "OK. Maybe later?");
                    mInstagramSession.reset();
                }
            });
        }
    }

    private void doNextstep(InstagramUser instagramUser) {
        if (isProfile) {
//			String fullName = instagramUser.fullName;
//			String userName = instagramUser.username;
            String profilePicUrl = instagramUser.profilPicture;
            instagramCallBack.setProfilePic(profilePicUrl);
        } else {
            new DownloadTask().execute();
        }
    }

    public class DownloadTask extends AsyncTask<URL, Integer, Long> {
        ArrayList<InstagramImage> photoList;
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
               // params.add(new BasicNameValuePair("max_id", "9999999"));
                InstagramRequest request = new InstagramRequest(mInstagramSession.getAccessToken());
                String response = request.createRequest("GET", "/users/self/media/recent", params);
                if (!response.equals("")) {
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
                    JSONArray jsonData = jsonObj.getJSONArray("data");
                    int length = jsonData.length();

                    if (length > 0) {
                        photoList = new ArrayList<InstagramImage>();
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonPhoto = jsonData.getJSONObject(i).getJSONObject("images");
                            InstagramImage image = new InstagramImage();
                            image.setHigh_resolution_url(jsonPhoto.getJSONObject("standard_resolution").getString("url"));
                            image.setLow_resolution_url(jsonPhoto.getJSONObject("low_resolution").getString("url"));
                            image.setThumbnail_url(jsonPhoto.getJSONObject("thumbnail").getString("url"));
                            photoList.add(image);
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
            if (barDialog != null)
                barDialog.dismiss();
            if (photoList == null) {
                AlertUtils.showToast(mContext, "No Photos Available");
            } else {
                instagramCallBack.setPictureListPost(photoList);
                Syso.info("Photo list>>>>>>>>" + photoList);
            }
        }
    }

}

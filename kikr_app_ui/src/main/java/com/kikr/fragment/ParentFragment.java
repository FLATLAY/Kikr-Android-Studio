package com.kikr.fragment;

import android.os.Bundle;
import android.widget.GridView;

import com.kikr.BaseFragment;
import com.kikr.adapter.LifestyleImageAdapter;
import com.kikr.model.InstagramImage;
import com.kikr.utility.InstagramCallBack;
import com.kikr.utility.InstagramUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12/27/2015.
 */
public class ParentFragment extends BaseFragment {

    InstagramUtility instagramUtility;
    List<String> bgImageList = new ArrayList<String>();
    boolean isLoadDefault = true;
    GridView imagesList;
    private LifestyleImageAdapter lifestyleImageAdapter;
    boolean isLoading = false;
    boolean userLoggedIn = false;
    boolean isProcessingLogin = false;


    public void getInstagramList(int count, boolean isShowLoader)
    {

            //isLoading = true;
            instagramUtility = new InstagramUtility(mContext, false, count + "", isShowLoader, new InstagramCallBack() {
                @Override
                public void setProfilePic(String url) {

                }

                @Override
                public void setPictureList(ArrayList<String> photoList) {
                    if (photoList.size() == 10)
                        isLoading = false;
                    bgImageList.addAll(photoList);
                    if (lifestyleImageAdapter == null) {
                        lifestyleImageAdapter = new LifestyleImageAdapter(mContext, bgImageList);
                        imagesList.setAdapter(lifestyleImageAdapter);
                    } else {
                        lifestyleImageAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void setPictureListPost(ArrayList<InstagramImage> photoList) {

                }
            });

// instagramUtility.inItInstgram();
//       instagramUtility.login();

    }





    @Override
    public void initUI(Bundle savedInstanceState) {

    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {

    }
}

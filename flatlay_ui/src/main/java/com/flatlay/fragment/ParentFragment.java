package com.flatlay.fragment;

import android.os.Bundle;
import android.widget.GridView;

import com.flatlay.BaseFragment;
import com.flatlay.adapter.LifestyleImageAdapter;
import com.flatlay.model.InstagramImage;
import com.flatlay.utility.InstagramCallBack;
import com.flatlay.utility.InstagramUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RachelDi on 4/17/18.
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


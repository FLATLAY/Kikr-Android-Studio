package com.flatlay;

import android.app.FragmentManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.flatlay.activity.LandingActivity;
import com.flatlay.activity.LoginActivity;
import com.flatlay.activity.SignUpActivity;
import com.flatlay.utility.CommonUtility;

import java.util.Stack;

/**
 * Created by RachelDi on 1/12/18.
 */

public class BaseActivityWithVideo extends BaseActivity{
    protected VideoView vedio;
    public Stack<String> mFragmentStack;
    private Fragment mContent;

    private FragmentManager manager;
    public FragmentTransaction transaction = getSupportFragmentManager()
            .beginTransaction();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtility.noTitleActivity(context);
        context = this;
        manager = getFragmentManager();
        mDisplayMetrics = new DisplayMetrics();
        mFragmentStack = new Stack<String>();
        setContentView(R.layout.activity_base_video);
        findViewById(R.id.baseHeader).setVisibility(View.GONE);
        vedio = (VideoView) findViewById(R.id.vedio);
        loadFragment(new LandingActivity());
    }


    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            hideAllFragment();
            addFragment(fragment);
        } else {
            Log.e("MainActivity", "Error in creating fragment.");
        }
    }

    public void hideAllFragment() {
        try {
            for (int i = 0; i < mFragmentStack.size(); i++) {
                transaction = getSupportFragmentManager()
                        .beginTransaction();
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentByTag(mFragmentStack.peek());
//                transaction.remove(fragment);
                if (fragment != null)
                    transaction.remove(fragment);
                transaction.commit();
            }
            mFragmentStack.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addFragment(Fragment fragment) {
        try {

            mContent = fragment;
            transaction = getSupportFragmentManager()
                    .beginTransaction();
//            if (mFragmentStack.size() > 0) {
//                Fragment currentFragment = getSupportFragmentManager()
//                        .findFragmentByTag(mFragmentStack.peek());
//                if (currentFragment != null)
//                    transaction.hide(currentFragment);
//                mFragmentStack.add(mContent.toString());
//                transaction.add(R.id.baseFrameLayout, fragment, mContent.toString());
//                transaction.addToBackStack(mContent.toString());
//                if (fragment instanceof FragmentPostUploadTab)
//                    transaction.commitAllowingStateLoss();
//                else
//                    transaction.commit();
//
//            } else {
                mFragmentStack.add(mContent.toString());
                transaction.replace(R.id.baseFrameLayout, fragment, mContent.toString());
                transaction.addToBackStack(mContent.toString());
                transaction.commit();
            //}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initLayout() {

    }

    @Override
    public void setupData() {

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
    protected void onResume() {
        super.onResume();
        videostart();
    }

    public void videostart() {

        try {
            vedio.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.setLooping(true);
                    vedio.start(); //TODO: need to make transition seamless.
                    //need to be replaced by youtube
                }
            });

            String uriPath = "android.resource://com.flatlay/" + R.raw.flatlay_guide;
            vedio.setVideoPath(uriPath);
            Uri uri = Uri.parse(uriPath);
            vedio.setVideoURI(uri);
            vedio.requestFocus();
            vedio.start();
        } catch (Exception e) {

        }
    }

}

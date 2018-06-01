package com.flatlay;

import android.app.FragmentManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.facebook.FacebookSdk;
import com.flatlay.activity.EditProfileActivity;
import com.flatlay.activity.HomeActivity;
import com.flatlay.activity.LandingActivity;
import com.flatlay.activity.LoginActivity;
import com.flatlay.activity.SignUpActivity;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.utils.AlertUtils;

import java.util.Stack;

/**
 * Created by RachelDi on 1/12/18.
 */

public class BaseActivityWithVideo extends BaseActivity {
    protected VideoView vedio;
    public Stack<String> mFragmentStack;
    private Fragment mContent;
    private boolean backPressedToExitOnce = false;
    private String inspiration_id;
    public static final String TAG = "BaseActivityWithVideo";
    private FragmentManager manager;
    public FragmentTransaction transaction = getSupportFragmentManager()
            .beginTransaction();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        Log.e(TAG, "BaseActivityWithVideo");
        Bundle bundle = getIntent().getExtras();
        System.out.println(bundle);

        if (bundle != null && bundle.get("otherdata") != null) {
            String otherdata = bundle.get("otherdata").toString();
            if (otherdata.contains("inspiration_id")) {
                inspiration_id = otherdata.substring(19, otherdata.length() - 2);
            }
        }
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Object value = bundle.get(key);
                Log.d(TAG, String.format("%s %s (%s)", key,
                        value.toString(), value.getClass().getName()));
            }
        }
        if (bundle != null && bundle.getString("title") != null && (bundle.getString("title").equals("New Like") || bundle.getString("title").equals("New Comment"))) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("inspiration_id", inspiration_id.toString());
            intent.putExtra("section", bundle.getString("section").toString());
            startActivity(intent);
        }
        if (bundle != null && bundle.getString("title") != null && bundle.getString("title").equals("New Follower")) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("section", bundle.getString("section").toString());
            startActivity(intent);
        }
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        CommonUtility.noTitleActivity(context);
        context = this;
        manager = getFragmentManager();
        mDisplayMetrics = new DisplayMetrics();
        mFragmentStack = new Stack<String>();
        setContentView(R.layout.activity_base_video);

        findViewById(R.id.baseHeader).setVisibility(View.GONE);
        vedio = (VideoView) findViewById(R.id.vedio);
        Handler handler = new Handler();
        if (UserPreference.getInstance().getUserID().equals("")) {
            findViewById(R.id.top_logo).setVisibility(View.GONE);
            loadFragment(new LandingActivity());
        }
        else
        {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(HomeActivity.class);
                }
            },1500);
        }

    }


    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            hideAllFragment();
            addFragment(fragment);
        } else {
            Log.e(TAG, "Error in creating fragment.");
        }
    }

    public void hideAllFragment() {
        try {
            for (int i = 0; i < mFragmentStack.size(); i++) {
                transaction = getSupportFragmentManager()
                        .beginTransaction();
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentByTag(mFragmentStack.peek());
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

            mFragmentStack.add(mContent.toString());
            transaction.replace(R.id.baseFrameLayout, fragment, mContent.toString());
            transaction.addToBackStack(mContent.toString());
            transaction.commit();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());

        if (fragment instanceof LandingActivity) {
            if (backPressedToExitOnce) {
                finish();
            } else {
                this.backPressedToExitOnce = true;
                AlertUtils.showToast(context, "Press again to exit");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backPressedToExitOnce = false;
                    }
                }, 2000);
            }
        } else {
            super.onBackPressed();
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

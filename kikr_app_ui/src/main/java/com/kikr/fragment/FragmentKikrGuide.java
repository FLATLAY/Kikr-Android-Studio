package com.kikr.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.IntroductionPagerActivity;

/**
 * Created by Ujjwal on 12/16/2015.
 */
public class FragmentKikrGuide extends BaseFragment{
    TextView kikrGuideTextView,kikrVideoTextView;
    ImageView kikrLogo,kikrVideoImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kikr_guide,null);
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        kikrGuideTextView = (TextView) getView().findViewById(R.id.kikrGuideTextView);
        kikrVideoTextView = (TextView) getView().findViewById(R.id.kikrVideoTextView);
        kikrLogo = (ImageView) getView().findViewById(R.id.kikrLogo);
        kikrVideoImageView = (ImageView) getView().findViewById(R.id.kikrVideoImageView);
        Animation aa =  AnimationUtils.loadAnimation(mContext, R.anim.kikr_logo_anim);
        kikrLogo.startAnimation(aa);
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        kikrGuideTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, IntroductionPagerActivity.class);
                i.putExtra("from","inside");
                startActivity(i);
            }
        });
        kikrLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, IntroductionPagerActivity.class);
                i.putExtra("from","inside");
                startActivity(i);
            }
        });
        kikrVideoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideo();
                }
        });
        kikrVideoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideo();
            }
        });
    }

    private void showVideo() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=H4co_Pyo_cg&app=desktop")));
    }
}

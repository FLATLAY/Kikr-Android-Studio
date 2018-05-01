package com.flatlay.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.IntroductionPageAdapter2;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyCirclePageIndicator;

/**
 * Created by RachelDi on 1/10/18.
 */

public class IntroductionActivity extends BaseFragment {
    private View mainView;
    public final static String TAG="IntroductionActivity";

    ViewPager tutorialImageView;
    TextView closeButton;
    MyCirclePageIndicator indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.introduction, container, false);
        Log.w(TAG,"IntroductionActivity");
        return mainView;
    }


    @Override
    public void initUI(Bundle savedInstanceState) {
        tutorialImageView = (ViewPager) mainView.findViewById(R.id.tutorialImageView);
        closeButton = (TextView) mainView.findViewById(R.id.closeButton);
        indicator = (MyCirclePageIndicator) mainView.findViewById(R.id.indicator);
        setUpTextType();
        setUpData();
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

    private void setUpData() {

        IntroductionPageAdapter2 pagerActivity = new IntroductionPageAdapter2(getActivity());
        tutorialImageView.setAdapter(pagerActivity);
        indicator.setViewPager(tutorialImageView);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeGuide();
            }
        });

    }

    private void closeGuide() {
        CommonUtility.hideSoftKeyboard(getActivity());
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.baseFrameLayout, new LandingActivity(), null)
                .addToBackStack(null)
                .commit();
    }

    public void setUpTextType() {
        closeButton.setTypeface(FontUtility.setMontserratLight(getActivity()));
    }

}

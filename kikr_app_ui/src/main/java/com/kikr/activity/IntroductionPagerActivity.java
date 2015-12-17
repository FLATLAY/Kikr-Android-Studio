package com.kikr.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.adapter.IntroductionPagerAdapter;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * Created by Ujjwal on 12/15/2015.
 */
public class IntroductionPagerActivity extends FragmentActivity{
    ViewPager tutorialImageView;
    TextView skipButton,nextButton;
    CirclePageIndicator indicator;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_introduction_pager);
        context = this;
        inItUI();
        setUpData();
    }


    private void inItUI() {
        tutorialImageView = (ViewPager) findViewById(R.id.tutorialImageView);
        skipButton = (TextView) findViewById(R.id.skipButton);
        nextButton = (TextView) findViewById(R.id.nextButton);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
    }

    private void setUpData() {
        IntroductionPagerAdapter pagerActivity =  new IntroductionPagerAdapter(this);
        tutorialImageView.setAdapter(pagerActivity);
        indicator.setViewPager(tutorialImageView);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextPage();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextPos = tutorialImageView.getCurrentItem()+1;
                if(nextPos<4)
                 tutorialImageView.setCurrentItem(nextPos);
                else
                 goToNextPage();
            }
        });
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(tutorialImageView.getCurrentItem()==3){
                    skipButton.setText("DONE");
                }else{
                    skipButton.setText("SKIP");
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void goToNextPage() {
        if(getIntent().hasExtra("from")&&getIntent().getStringExtra("from").equals("splash")){
            Intent ii = new Intent(context, LandingActivity.class);
            startActivity(ii);
            finish();
        }else {
            finish();
        }
    }
}

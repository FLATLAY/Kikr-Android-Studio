package com.flatlay.googlewallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.flatlay.KikrApp;
import com.flatlay.R;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.utils.Syso;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ConfirmationActivity extends BikestoreFragmentActivity implements OnClickListener{

    private static final int REQUEST_CODE_CHANGE_MASKED_WALLET = 1002;
    private TextView mLoyaltyLabel;
    private TextView mLoyaltyText;

    Tracker mTracker = null;
    public static final String SCREEN_NAME = "Confirmation Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtility.noTitleActivity(this);
        mTracker = ((KikrApp) getApplication())
                .getTracker(KikrApp.TrackerName.GLOBAL_TRACKER);
        mTracker.setScreenName(SCREEN_NAME);
        mTracker.send(new HitBuilders.AppViewBuilder().build());

        setContentView(R.layout.activity_confirmation);
        mLoyaltyText = (TextView) findViewById(R.id.text_loyalty);
        mLoyaltyLabel = (TextView) findViewById(R.id.label_loyalty);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // no need to show login2 menu on confirmation screen
        return false;
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	try{
    	Syso.info("In onActivityResult>>"+requestCode+","+resultCode+","+data);
    	//FirebaseMsgService.dumpIntent(data);


            super.onActivityResult(requestCode, resultCode, data);

    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }


	@Override
    public Fragment getResultTargetFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.full_wallet_confirmation_button_fragment);
    }

	@Override
	public void initLayout() {
		
		
	}

	@Override
	public void setupData() {
		
		
	}

	@Override
	public void headerView() {
		setBackHeader();
		getLeftTextView().setOnClickListener(this);
		getHomeImageView().setOnClickListener(this);
	}

	@Override
	public void setUpTextType() {
		
		
	}

	@Override
	public void setClickListener() {
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch(v.getId()){
	      case R.id.leftTextView:
	    	  finish();
	    	  break;
	      case R.id.homeImageView:
	    	  goToHome();
	    	  break;
	      }
	}
}

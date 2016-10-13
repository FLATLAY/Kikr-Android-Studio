package com.kikr.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.kikr.BaseActivity;
import com.flatlay.R;
import com.kikr.utility.CommonUtility;

public class KikrLearnMoreActivity extends BaseActivity implements OnClickListener{


	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_kikr_learn_more);
	}
	

	@Override
	public void initLayout() {
	}
	
	@Override
	public void setupData() {
	}

	@Override
	public void headerView() {

		getLeftTextView().setVisibility(View.VISIBLE);
		getLeftTextView().setOnClickListener(this);
	}

	@Override
	public void setUpTextType() {
//		kikrInfoTextView.setTypeface(FontUtility.setProximanovaLight(context));
//		kikrRewordTextView.setTypeface(FontUtility.setProximanovaLight(context));
	}

	@Override
	public void setClickListener() {
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.leftTextView:
			finish();
			break;
		}
	}

	@Override
	public void onBackPressed() {
	}
}

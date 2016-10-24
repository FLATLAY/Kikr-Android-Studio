package com.flatlay.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.utility.CommonUtility;

public class LearnMoreOutsideUSActivity extends BaseActivity implements OnClickListener {

	private TextView txtLearnMoreDesc;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_learn_more_outside_us);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftTextView:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		txtLearnMoreDesc = (TextView) findViewById(R.id.txtLearnMoreDesc);
	}

	@Override
	public void setupData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void headerView() {
		setBackHeader();
		getLeftTextView().setOnClickListener(this);
	}

	@Override
	public void setUpTextType() {
		
	}

	@Override
	public void setClickListener() {
		// TODO Auto-generated method stub
		
	}
	
}


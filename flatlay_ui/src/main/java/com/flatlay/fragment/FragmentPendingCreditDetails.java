package com.flatlay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;

public class FragmentPendingCreditDetails extends BaseFragment implements OnClickListener {
	private View mainView;
	private Button back;
	public FragmentPendingCreditDetails() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_pending_credit_detail, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		back=(Button)mainView.findViewById(R.id.backButton);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		back.setOnClickListener(this);
	}


	@Override
	public void setData(Bundle bundle) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				((HomeActivity)mContext).onBackPressed();
				break;
		}
	}

}

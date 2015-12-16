package com.kikr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kikr.BaseFragment;
import com.kikr.R;

public class FragmentPendingCreditDetails extends BaseFragment implements OnClickListener {
	private View mainView;
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
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
	}

	@Override
	public void setData(Bundle bundle) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}
	}

}

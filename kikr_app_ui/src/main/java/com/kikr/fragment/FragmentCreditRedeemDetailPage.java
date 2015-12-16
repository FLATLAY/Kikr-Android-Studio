package com.kikr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kikr.BaseFragment;
import com.kikr.R;

public class FragmentCreditRedeemDetailPage extends BaseFragment implements OnClickListener {
	private View mainView;
	public FragmentCreditRedeemDetailPage() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_credit_redeem_detail_page, null);
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

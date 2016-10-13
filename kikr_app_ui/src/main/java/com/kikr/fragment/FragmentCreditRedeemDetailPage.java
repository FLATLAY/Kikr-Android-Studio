package com.kikr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.flatlay.R;
import com.kikr.activity.HomeActivity;

public class FragmentCreditRedeemDetailPage extends BaseFragment implements OnClickListener {
	private View mainView;
	private TextView credit_textview,contact_us;
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
		credit_textview = (TextView) mainView.findViewById(R.id.credit_textview);
		contact_us = (TextView) mainView.findViewById(R.id.contact_us);
		double credits = ((HomeActivity)mContext).getCredits();
		credit_textview.setText("Your current Flatlay Credit Balance: "+ Math.round(credits));
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		contact_us.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.contact_us:
				addFragment(new FragmentSupport());
				break;
		}
	}

}

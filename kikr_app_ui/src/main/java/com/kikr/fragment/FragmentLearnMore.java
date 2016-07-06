package com.kikr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kikr.BaseFragment;
import com.kikr.R;

public class FragmentLearnMore extends BaseFragment implements OnClickListener {
	private View mainView;
	private LinearLayout learnMore;
	public FragmentLearnMore() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mainView = inflater.inflate(R.layout.activity_kikr_learn_more, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		learnMore = (LinearLayout) mainView.findViewById(R.id.learnmorelayout);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		learnMore.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.learnmorelayout:
			addFragment(new FragmentPurchaseGuarantee());
			break;
		}
	}

}

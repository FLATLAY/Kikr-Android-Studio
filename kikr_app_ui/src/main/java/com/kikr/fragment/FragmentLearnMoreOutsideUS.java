package com.kikr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.utility.FontUtility;

public class FragmentLearnMoreOutsideUS extends BaseFragment implements OnClickListener {
	private View mainView;

	private TextView txtLearnMoreDesc;
	
	public FragmentLearnMoreOutsideUS() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.activity_learn_more_outside_us, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		txtLearnMoreDesc = (TextView) mainView.findViewById(R.id.txtLearnMoreDesc);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		txtLearnMoreDesc.setTypeface(FontUtility.setProximanovaLight(mContext));
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

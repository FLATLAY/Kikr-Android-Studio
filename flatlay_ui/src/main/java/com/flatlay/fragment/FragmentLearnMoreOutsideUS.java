package com.flatlay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.FontUtility;

public class FragmentLearnMoreOutsideUS extends BaseFragment implements OnClickListener {
	private View mainView;

	private TextView txtLearnMoreDesc;
	private Button back;
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
		back=(Button) mainView.findViewById(R.id.backButton);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		txtLearnMoreDesc.setTypeface(FontUtility.setProximanovaLight(mContext));
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

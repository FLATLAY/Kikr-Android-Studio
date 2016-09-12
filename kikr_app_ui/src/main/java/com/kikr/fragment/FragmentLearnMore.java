package com.kikr.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;

public class FragmentLearnMore extends BaseFragment implements OnClickListener {
	private View mainView;
	private LinearLayout learnMore;
	private TextView learnMoretext;
	private Button back;
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
		//learnMore = (LinearLayout) mainView.findViewById(R.id.learnmorelayout);
		learnMoretext=(TextView) mainView.findViewById(R.id.learnMoretext);
		learnMoretext.setPaintFlags(learnMoretext.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		learnMoretext.setMovementMethod(LinkMovementMethod.getInstance());
        back=(Button)mainView.findViewById(R.id.back);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		//learnMore.setOnClickListener(this);
		learnMoretext.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.learnMoretext:
				addFragment(new FragmentPurchaseGuarantee());
				break;

			case R.id.back:
				((HomeActivity)mContext).onBackPressed();
				break;
		}
	}



}

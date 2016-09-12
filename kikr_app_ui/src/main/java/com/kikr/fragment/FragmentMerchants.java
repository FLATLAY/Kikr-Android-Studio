package com.kikr.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.kikr.BaseFragment;
import com.kikr.R;

public class FragmentMerchants extends BaseFragment implements OnClickListener {
	private View mainView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_merchants, null);
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

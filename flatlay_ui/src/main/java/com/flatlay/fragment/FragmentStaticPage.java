package com.flatlay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;

public class FragmentStaticPage extends BaseFragment{
	TextView staticTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_static_page, null);
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		staticTextView=(TextView) getView().findViewById(R.id.staticTextView);
	}

	@Override
	public void setData(Bundle bundle) {
		String message=bundle.getString("message");
		staticTextView.setText(message);
	}

	@Override
	public void refreshData(Bundle bundle) {}

	@Override
	public void setClickListener() {}

}

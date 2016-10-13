package com.kikr.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kikr.BaseFragment;
import com.flatlay.R;
import com.kikr.adapter.FragmentCouponSearchAdapter;

public class FragmentCouponSearch extends BaseFragment implements OnClickListener
{
	private View mainView;
	private ListView coupon_search_list;
	
	public FragmentCouponSearch() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_coupon_search, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		coupon_search_list=(ListView) mainView.findViewById(R.id.coupon_search_list);		
	}


	@Override
	public void refreshData(Bundle bundle) {
		
	}

	@Override
	public void setClickListener() {
		
	}
	
	@Override
	public void setData(Bundle bundle) {
		coupon_search_list.setAdapter(new FragmentCouponSearchAdapter(mContext));
	}

	@Override
	public void onClick(View v) {
		
	}


}

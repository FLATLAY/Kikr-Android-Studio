package com.flatlay.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.flatlay.R;

public class FragmentCouponSearchAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;

	public FragmentCouponSearchAdapter(Activity context) {
		super();
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return 10;
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.adapter_coupon_search,null);
		return convertView;
	}


}

package com.flatlay.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlaylib.bean.Product;

public class ActivityCollectionAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	private List<Product> data = new ArrayList<Product>();

	public ActivityCollectionAdapter(Activity context, List<Product> data) {
		super();
		this.mContext = context;
		this.data = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.w("Activity","ActivityCollectionAdapter");
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Product getItem(int index) {
		return data.get(index);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_collection_product, null);
			viewHolder = new ViewHolder();
			viewHolder.product_name = (TextView) convertView.findViewById(R.id.product_name);
			viewHolder.views_count = (TextView) convertView.findViewById(R.id.views_count);
			viewHolder.buys_text = (TextView) convertView.findViewById(R.id.buys_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(getItem(position).getProductname()))
			viewHolder.product_name.setText(getItem(position).getProductname());
			if(!TextUtils.isEmpty(getItem(position).getViews()))
			viewHolder.views_count.setText(getItem(position).getViews()+" Views");
			if(!TextUtils.isEmpty(getItem(position).getBuys()))
			viewHolder.buys_text.setText(getItem(position).getBuys()+" Buys");
		return convertView;
	}

	public class ViewHolder {
		TextView product_name,views_count,buys_text;
	}
}

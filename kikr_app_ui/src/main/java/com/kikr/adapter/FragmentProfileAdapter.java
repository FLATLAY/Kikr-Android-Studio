package com.kikr.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;

public class FragmentProfileAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;

	public FragmentProfileAdapter(Activity context) {
		super();
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public Object getItem(int index) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_profile, null);
			viewHolder = new ViewHolder();
			viewHolder.collection_image_1 =  (ImageView) convertView.findViewById(R.id.collection_image_1);
			viewHolder.collection_image_2 =  (ImageView) convertView.findViewById(R.id.collection_image_2);
			viewHolder.collection_image_3 =  (ImageView) convertView.findViewById(R.id.collection_image_3);
			viewHolder.collection_image_4 =  (ImageView) convertView.findViewById(R.id.collection_image_4);
			viewHolder.collection_name = (TextView) convertView.findViewById(R.id.collection_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.collection_name.setText("abc");
//		viewHolder.bank_image.setImageResource(mContext.getResources().getIdentifier(images.get(position), "drawable",mContext.getPackageName()));
		return convertView;
	}

	public class ViewHolder {
		ImageView collection_image_1;
		ImageView collection_image_2;
		ImageView collection_image_3;
		ImageView collection_image_4;
		TextView collection_name;
	}

}

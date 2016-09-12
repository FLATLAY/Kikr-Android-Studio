package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.twitter.OauthItem;
import com.kikr.utility.CommonUtility;

public class TwitterFriendListAdapter extends BaseAdapter {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private ArrayList<OauthItem> data;

	public TwitterFriendListAdapter(FragmentActivity context, List<OauthItem> data) {
		super();
		this.mContext = context;
		this.data = (ArrayList<OauthItem>) data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public OauthItem getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_twitter_friends, null);
			viewHolder = new ViewHolder();
			viewHolder.user_image = (com.kikr.ui.RoundImageView) convertView.findViewById(R.id.user_image);
			viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.user_name.setText(getItem(position).getFriendName());
		if(getItem(position).getFriendImage()!=null)
			CommonUtility.setImage(mContext, getItem(position).getFriendImage(), viewHolder.user_image, R.drawable.dum_user);
		return convertView;
	}

	public class ViewHolder {
		ImageView user_image;
		TextView user_name;
	}

}

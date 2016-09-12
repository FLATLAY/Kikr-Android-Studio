package com.kikr.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.FbUser;

public class FacebookFriendListAdapter extends BaseAdapter {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private ArrayList<FbUser> data;

	public FacebookFriendListAdapter(FragmentActivity context, ArrayList<FbUser> data) {
		super();
		this.mContext = context;
		this.data =  data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public FbUser getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_fb_friends, null);
			viewHolder = new ViewHolder();
			viewHolder.user_image = (com.kikr.ui.RoundImageView) convertView.findViewById(R.id.user_image);
			viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.user_name.setText(getItem(position).getName());
		if(getItem(position).getProfile_pic()!=null)
		CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewHolder.user_image, R.drawable.dum_user);
		return convertView;
	}

	public class ViewHolder {
		ImageView user_image;
		TextView user_name;
	}

}

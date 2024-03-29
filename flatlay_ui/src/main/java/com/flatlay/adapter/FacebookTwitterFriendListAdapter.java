package com.flatlay.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentMyFriends;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.SocialFriend;

public class FacebookTwitterFriendListAdapter extends BaseAdapter {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<SocialFriend> data;
	FragmentMyFriends fragmentMyFriends;

	public FacebookTwitterFriendListAdapter(FragmentActivity context, List<SocialFriend> data, FragmentMyFriends fragmentMyFriends) {
		super();
		this.mContext = context;
		this.data =  data;
		this.fragmentMyFriends=fragmentMyFriends;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.w("Activity","FacebookTwitterFriendListAdapter");
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public SocialFriend getItem(int index) {
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
			viewHolder.user_image = (com.flatlay.ui.RoundImageView) convertView.findViewById(R.id.user_image);
			viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.user_name.setText(getItem(position).getName());
		CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewHolder.user_image, R.drawable.profile_icon);
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((HomeActivity) mContext).checkInternet()){
					if(getItem(position).getUser_id().equals("")){
						fragmentMyFriends.tweet(getItem(position).getTwitter_id());
					}else{
						addFragment(new FragmentProfileView(getItem(position).getUser_id(),getItem(position).getIs_followed()));		
					}
				}
			}
		});
		return convertView;
	}
	

	public class ViewHolder {
		ImageView user_image;
		TextView user_name;
	}
	
	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}

}

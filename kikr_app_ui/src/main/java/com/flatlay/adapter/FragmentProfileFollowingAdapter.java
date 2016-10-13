package com.flatlay.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.bean.FollowerList;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfileFollowingAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	List<FollowerList> data=new ArrayList<FollowerList>();

	public FragmentProfileFollowingAdapter(Activity context,List<FollowerList> data) {
		super();
		this.mContext = context;
		this.data = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public FollowerList getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_follwer_list, null);
			viewHolder = new ViewHolder();
			viewHolder.follower_user_image =  (ImageView) convertView.findViewById(R.id.follower_user_image);
			viewHolder.follower_user_name = (TextView) convertView.findViewById(R.id.follower_user_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(getItem(position).getUsername()!=null&&!getItem(position).getUsername().equals(""))
			viewHolder.follower_user_name.setText(getItem(position).getUsername());
		else
			viewHolder.follower_user_name.setText("Unknown");
		
		CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewHolder.follower_user_image, R.drawable.dum_user);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((HomeActivity) mContext).checkInternet())
					addFragment( new FragmentProfileView(getItem(position).getId(),getItem(position).getIs_followed()));
			}
		});
		return convertView;
	}

	public class ViewHolder {
		ImageView follower_user_image;
		TextView follower_user_name;
	}

	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
	
}

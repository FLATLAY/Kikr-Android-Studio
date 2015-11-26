package com.kikr.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentInterestSection;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.InterestSection;

public class InterestPeopleListAdapter extends BaseAdapter{
	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	public boolean[] mSelectedItems;
	private List<InterestSection> followUsers=new ArrayList<InterestSection>();
	FragmentInterestSection fragmentInterestSection;
	
	public InterestPeopleListAdapter(FragmentActivity mContext,List<InterestSection> stores, FragmentInterestSection fragmentInterestSection) {
		this.mContext=mContext;
		this.followUsers = stores;
		this.fragmentInterestSection = fragmentInterestSection;
		mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSelectedItems=new boolean[stores.size()];
		Arrays.fill(mSelectedItems, false);
	}
	
	public void setData(List<InterestSection> data){
		this.followUsers.addAll(data);
	}
	
	public boolean[] getSelectedItems(){
		return mSelectedItems;
	}
	
	@Override
	public int getCount() {
		return followUsers.size();
	}

	@Override
	public InterestSection getItem(int index) {
		return followUsers.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_interest_people_list, null);
			viewHolder = new ViewHolder();
			viewHolder.user_image = (ImageView) convertView.findViewById(R.id.user_image);
			viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (!TextUtils.isEmpty(getItem(position).getUsername())) {
			viewHolder.user_name.setText(getItem(position).getUsername());
		} else {
			viewHolder.user_name.setText("Unknown");
		}
		CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewHolder.user_image, R.drawable.dum_user);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((HomeActivity) mContext).checkInternet())
					addFragment(new FragmentProfileView(getItem(position).getId(),getItem(position).getIs_followed()));			}
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

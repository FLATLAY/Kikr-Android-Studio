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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentInterestSection;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.ui.RoundImageView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.InterestSection;
import com.kikrlib.db.UserPreference;

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
			viewHolder.user_image = (RoundImageView) convertView.findViewById(R.id.user_image);
			viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			viewHolder.checkImageView = (ImageView) convertView.findViewById(R.id.checkImageView);
			viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(getItem(position).getUsername())) {
			viewHolder.user_name.setText(getItem(position).getUsername());
		} else {
			viewHolder.user_name.setText("Unknown");
		}
		if(getItem(position).getIs_followed().equalsIgnoreCase("yes")){
			viewHolder.checkImageView.setVisibility(View.VISIBLE);
			viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
		}else{
			viewHolder.checkImageView.setVisibility(View.VISIBLE);
			viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
		}
		if(getItem(position).getId().equals(UserPreference.getInstance().getUserID())){
			viewHolder.checkImageView.setVisibility(View.GONE);
		}else{
			viewHolder.checkImageView.setVisibility(View.VISIBLE);
		}
		CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewHolder.user_image, R.drawable.dum_user);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((HomeActivity) mContext).checkInternet())
					addFragment(new FragmentProfileView(getItem(position).getId(), getItem(position).getIs_followed()));
			}
		});

		viewHolder.checkImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(getItem(position).getIs_followed()) && fragmentInterestSection.checkInternet()) {
					if (getItem(position).getIs_followed().equalsIgnoreCase("yes")) {
						getItem(position).setIs_followed("no");
						fragmentInterestSection.unFollowUser(getItem(position).getId(), v.getRootView());
						notifyDataSetChanged();
					} else {
						getItem(position).setIs_followed("yes");
						fragmentInterestSection.followUser(getItem(position).getId(), v.getRootView());
						notifyDataSetChanged();
					}
				}
			}
		});
		return convertView;
	}

	public class ViewHolder {
		RoundImageView user_image;
		TextView user_name;
		ImageView checkImageView;
		ProgressBar progressBar_follow_brand;
	}
	
	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
}

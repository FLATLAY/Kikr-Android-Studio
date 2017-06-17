package com.flatlay.adapter;

import java.util.ArrayList;
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
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentTagList;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.InterestSection;

public class InspirationPeopleListAdapter extends BaseAdapter{
	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<InterestSection> followUsers=new ArrayList<InterestSection>();
	FragmentTagList fragmentInterestSection;
	
	public InspirationPeopleListAdapter(FragmentActivity mContext,List<InterestSection> stores, FragmentTagList fragmentInterestSection) {
		this.mContext=mContext;
		this.followUsers = stores;
		this.fragmentInterestSection = fragmentInterestSection;
		mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.w("Activity","InspirationPeopleListAdapter");
	}
	
	public void setData(List<InterestSection> data){
		this.followUsers.addAll(data);
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
			viewHolder.checkImageView=(ImageView) convertView.findViewById(R.id.checkImageView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(getItem(position).getUsername())) {
			viewHolder.user_name.setText(getItem(position).getUsername());
		} else {
			viewHolder.user_name.setText("Unknown");
		}
		viewHolder.checkImageView.setVisibility(View.VISIBLE);
		if(followUsers.get(position).getIs_selected()!=null&&followUsers.get(position).getIs_selected().equals("yes")){
			viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
		}else{
			viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
		}
		CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewHolder.user_image, R.drawable.dum_user);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for (int i = 0; i < followUsers.size(); i++) {
					if (followUsers.get(i).equals(getItem(position))) {
						followUsers.get(i).setIs_selected("yes");
						fragmentInterestSection.addBrand(getItem(position));
					}else{
						followUsers.get(i).setIs_selected("no");
					}
					notifyDataSetChanged();
				}
//				if(followUsers.get(position).getIs_selected().equals("yes")){
//					followUsers.get(position).setIs_selected("no");
//					fragmentInterestSection.deleteBrand(getItem(position));
//					notifyDataSetChanged();
//				}else{
//					followUsers.get(position).setIs_selected("yes");
//					fragmentInterestSection.addBrand(getItem(position));
//					notifyDataSetChanged();
//				}
				}
		});
		return convertView;
	}

	public class ViewHolder {
		ImageView user_image,checkImageView;
		TextView user_name;
	}
	
	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
}

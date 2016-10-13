package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentInterestSection;
import com.kikr.fragment.FragmentProductBasedOnType;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.InterestSection;

public class InterestBrandListAdapter extends BaseAdapter{
	private FragmentActivity mContext;
	private LayoutInflater inflater;
	private List<InterestSection> brands=new ArrayList<InterestSection>();
	private FragmentInterestSection fragmentInterestSection;
	private FragmentProfileView fragmentProfileView;
	private boolean fromProfile = false;
	
	public InterestBrandListAdapter(FragmentActivity mContext,List<InterestSection> stores, FragmentInterestSection fragmentInterestSection) {
		this.mContext=mContext;
		this.brands = stores;
		this.fragmentInterestSection = fragmentInterestSection;
		fromProfile = false;
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public InterestBrandListAdapter(FragmentActivity mContext,List<InterestSection> stores, FragmentProfileView fragmentProfileView) {
		this.mContext=mContext;
		this.brands = stores;
		this.fragmentProfileView = fragmentProfileView;
		fromProfile = true;
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void setData(List<InterestSection> data){
		this.brands.addAll(data);
	}
	
	public void addData(InterestSection data){
		this.brands.add(data);
	}
	
	@Override
	public int getCount() {
		return brands.size();
	}

	@Override
	public InterestSection getItem(int index) {
		return brands.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder viewHolder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.adapter_follow_brands, null);
			viewHolder=new ViewHolder();
			viewHolder.searchImageView=(ImageView) convertView.findViewById(R.id.searchImageView);
			viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
			viewHolder.brandNameTextView=(TextView) convertView.findViewById(R.id.brandNametextView);
			viewHolder.checkImageView  = (ImageView) convertView.findViewById(R.id.checkImageView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if(fromProfile) 
			if(brands.get(position).getIs_followedbyviewer()!=null&&brands.get(position).getIs_followedbyviewer().equals("yes"))
				viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
			else
				viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
		else
			if(brands.get(position).getIs_followed()!=null&&brands.get(position).getIs_followed().equals("yes")){
				viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
			}else{
				viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
			}
		
		CommonUtility.setImage(mContext, getItem(position).getLogo(), viewHolder.searchImageView, R.drawable.ic_placeholder_brand);
		if (!TextUtils.isEmpty(brands.get(position).getName())) {
			viewHolder.brandNameTextView.setText(capitalizeFirstLetter(brands.get(position).getName()));
		}
		
		viewHolder.checkImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!fromProfile) {
					if (!TextUtils.isEmpty(brands.get(position).getIs_followed())&&fragmentInterestSection.checkInternet()) {
						if(brands.get(position).getIs_followed().equals("yes")){
							brands.get(position).setIs_followed("no");
							fragmentInterestSection.deleteBrand(getItem(position).getId(),viewHolder.checkImageView.getRootView());
							notifyDataSetChanged();
						}else{
							brands.get(position).setIs_followed("yes");
							fragmentInterestSection.addBrand(getItem(position).getId(),viewHolder.checkImageView.getRootView());
							notifyDataSetChanged();
						}
					}
				}else {
					if (!TextUtils.isEmpty(brands.get(position).getIs_followedbyviewer())&&fragmentProfileView.checkInternet()) {
						if(brands.get(position).getIs_followedbyviewer().equals("yes")){
							brands.get(position).setIs_followedbyviewer("no");
							fragmentProfileView.deleteBrand(getItem(position).getId(),viewHolder.checkImageView.getRootView());
							notifyDataSetChanged();
						}else{
							brands.get(position).setIs_followedbyviewer("yes");
							fragmentProfileView.addBrand(getItem(position).getId(),viewHolder.checkImageView.getRootView());
							notifyDataSetChanged();
						}
					}
				}
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			((HomeActivity)	mContext).addFragment(new FragmentProductBasedOnType("brand", getItem(position).getName(), getItem(position).getId()));
			}
		});
		return convertView;
	}

	public String capitalizeFirstLetter(String original){
	    if(original.length() == 0)
	        return original;
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
	
	public class ViewHolder {
		private ImageView searchImageView;
		private ImageView checkImageView;
		private ProgressBar progressBar_follow_brand;
		private TextView brandNameTextView;
	}
}

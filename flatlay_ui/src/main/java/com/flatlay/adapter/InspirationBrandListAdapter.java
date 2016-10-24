package com.flatlay.adapter;

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
import com.flatlay.fragment.FragmentTagList;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.InterestSection;

public class InspirationBrandListAdapter extends BaseAdapter{
	private FragmentActivity mContext;
	private LayoutInflater inflater;
	private List<InterestSection> brands=new ArrayList<InterestSection>();
	private FragmentTagList fragmentInterestSection;
	
	public InspirationBrandListAdapter(FragmentActivity mContext,List<InterestSection> stores, FragmentTagList fragmentInterestSection) {
		this.mContext=mContext;
		this.brands = stores;
		this.fragmentInterestSection = fragmentInterestSection;
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public void setData(List<InterestSection> data){
		this.brands.addAll(data);
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
			convertView=inflater.inflate(R.layout.adapter_interest_brand_list, null);
			viewHolder=new ViewHolder();
			viewHolder.brandImageView=(ImageView) convertView.findViewById(R.id.searchImageView);
			viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
			viewHolder.brandNameTextView=(TextView) convertView.findViewById(R.id.brandNameTextView);
			viewHolder.checkImageView=(ImageView) convertView.findViewById(R.id.checkImageView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if(brands.get(position).getIs_selected()!=null&&brands.get(position).getIs_selected().equals("yes")){
			viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
		}else{
			viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
		}
		CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.brandImageView, R.drawable.dum_list_item_brand);
		if (!TextUtils.isEmpty(brands.get(position).getName())) {
			viewHolder.brandNameTextView.setText(brands.get(position).getName());
		}
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(brands.get(position).getIs_selected())) {
				if(brands.get(position).getIs_selected().equals("yes")){
					brands.get(position).setIs_selected("no");
					fragmentInterestSection.deleteBrand(getItem(position));
					notifyDataSetChanged();
				}else{
					brands.get(position).setIs_selected("yes");
					fragmentInterestSection.addBrand(getItem(position));
					notifyDataSetChanged();
				}
				}
			}
		});
		
		return convertView;
	}

	public class ViewHolder {
		private ImageView brandImageView,checkImageView;
		private TextView brandNameTextView;
		private ProgressBar progressBar_follow_brand;
	}
}

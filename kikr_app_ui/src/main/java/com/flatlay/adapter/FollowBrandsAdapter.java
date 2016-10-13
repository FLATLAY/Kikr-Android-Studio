package com.flatlay.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.bean.BrandList;

public class FollowBrandsAdapter extends BaseAdapter{
	private FragmentActivity mContext;
	private LayoutInflater inflater;
	public List<BrandList> brands= new ArrayList<BrandList>();
	
	public FollowBrandsAdapter(FragmentActivity mContext,List<BrandList> brands) {
		this.mContext=mContext;
		this.brands = brands;
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setData(List<BrandList> data){
		this.brands.addAll(data);
	}
	
	@Override
	public int getCount() {
		return brands.size();
	}

	@Override
	public BrandList getItem(int index) {
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
			convertView=inflater.inflate(R.layout.adapter_follow_brands_customize, null);
			viewHolder = new ViewHolder();
			viewHolder.searchImageView=(ImageView) convertView.findViewById(R.id.searchImageView);
			viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
			viewHolder.brandNameTextView=(TextView) convertView.findViewById(R.id.brandNametextView);
			viewHolder.checkImageView  = (ImageView) convertView.findViewById(R.id.checkImageView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if(brands.get(position).getIs_followed()!=null&&brands.get(position).getIs_followed().equals("yes")){
			viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
		}else{
			viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
		}
		CommonUtility.setImage(mContext, getItem(position).getLogo(), viewHolder.searchImageView, R.drawable.ic_placeholder_brand);
		if (!TextUtils.isEmpty(brands.get(position).getName())) {
			viewHolder.brandNameTextView.setText(brands.get(position).getName());
		}
		return convertView;
	}

	public class ViewHolder {
		private ImageView searchImageView;
		private ImageView checkImageView;
		private TextView brandNameTextView;
		private ProgressBar progressBar_follow_brand;
	}
	
}

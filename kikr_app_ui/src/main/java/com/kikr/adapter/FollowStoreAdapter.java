package com.kikr.adapter;

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

import com.kikr.R;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.InterestSection;

public class FollowStoreAdapter extends BaseAdapter{
	private FragmentActivity mContext;
	private LayoutInflater inflater;
	public List<InterestSection> stores= new ArrayList<InterestSection>();
	
	public FollowStoreAdapter(FragmentActivity mContext,List<InterestSection> stores) {
		this.mContext=mContext;
		this.stores = stores;
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setData(List<InterestSection> data){
		this.stores.addAll(data);
	}
	
	@Override
	public int getCount() {
		return stores.size();
	}

	@Override
	public InterestSection getItem(int index) {
		return stores.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder viewHolder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.adapter_interest_store_list_cutomize, null);
			viewHolder=new ViewHolder();
			viewHolder.storeImageView=(ImageView) convertView.findViewById(R.id.storeImageView);
			viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
			viewHolder.storeNameTextView=(TextView) convertView.findViewById(R.id.storeNameTextView);
			viewHolder.checkImageView  = (ImageView) convertView.findViewById(R.id.checkImageView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if(stores.get(position).getIs_followed()!=null&&stores.get(position).getIs_followed().equals("yes")){
			viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
		}else{
			viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
		}
		CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.storeImageView, R.drawable.linesplaceholder);
		if (!TextUtils.isEmpty(stores.get(position).getName())) {
			viewHolder.storeNameTextView.setText(stores.get(position).getName());
		}
		return convertView;
	}

	public class ViewHolder {
		private ImageView checkImageView,storeImageView;
		private TextView storeNameTextView;
		private ProgressBar progressBar_follow_brand;
	}
}

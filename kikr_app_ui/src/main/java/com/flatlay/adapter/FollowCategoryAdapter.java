package com.flatlay.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.bean.Category;

public class FollowCategoryAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	public ArrayList<Category> catList=new ArrayList<Category>();
	private Activity mContext;
	
	
	public FollowCategoryAdapter(Activity mContext,List<Category> categories) {
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		catList=(ArrayList<Category>) categories;
		this.mContext = mContext;
	}
	
	public void setData(List<Category> data){
		this.catList.addAll(data);
	}
	
	@Override
	public int getCount() {
		return catList.size();
	}

	@Override
	public Category getItem(int index) {
		return catList.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.adapter_follow_category, null);
			viewHolder=new ViewHolder();
			viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
			viewHolder.categoryImageView=(ImageView) convertView.findViewById(R.id.categoryImageView);
			viewHolder.checkImageView=(ImageView) convertView.findViewById(R.id.checkImageView);
			viewHolder.categoryNameTextView=(TextView) convertView.findViewById(R.id.categoryNameTextView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.categoryNameTextView.setText(getItem(position).getName());
		CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.categoryImageView, R.drawable.dum_list_item_brand);
		if(getItem(position).getIs_followed().equalsIgnoreCase("yes")){
			viewHolder.checkImageView.setVisibility(View.VISIBLE);
			viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
		}else{
			viewHolder.checkImageView.setVisibility(View.VISIBLE);
			viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
		}
		return convertView;
	}

	public class ViewHolder {
		ImageView categoryImageView,checkImageView;
		TextView categoryNameTextView;
		private ProgressBar progressBar_follow_brand;
	}
}

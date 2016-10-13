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
import com.kikr.fragment.FragmentTagList;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.InterestSection;

public class InspirationStoreListAdapter extends BaseAdapter{
	private FragmentActivity mContext;
	private LayoutInflater inflater;
//	public boolean[] mSelectedItems;
	private List<InterestSection> stores=new ArrayList<InterestSection>();
	private FragmentTagList fragmentInterestSection;
	
	public InspirationStoreListAdapter(FragmentActivity mContext,List<InterestSection> stores,FragmentTagList fragmentInterestSection) {
		this.mContext=mContext;
		this.fragmentInterestSection = fragmentInterestSection;
		this.stores = stores;
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		mSelectedItems=new boolean[stores.size()];
//		Arrays.fill(mSelectedItems, false);
	}
	
	public void setData(List<InterestSection> data){
		this.stores.addAll(data);
	}
	
//	public boolean[] getSelectedItems(){
//		return mSelectedItems;
//	}
	
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
			viewHolder.checkImageView=(ImageView) convertView.findViewById(R.id.checkImageView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		if(stores.get(position).getIs_selected()!=null&&stores.get(position).getIs_selected().equals("yes")){
			viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
		}else{
			viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
		}
		CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.storeImageView, R.drawable.dum_list_item_brand);
		viewHolder.storeNameTextView.setText(stores.get(position).getName());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(stores.get(position).getIs_selected())&&fragmentInterestSection.checkInternet()) {
					if(stores.get(position).getIs_selected().equalsIgnoreCase("yes")){
					stores.get(position).setIs_selected("no");
					fragmentInterestSection.unFollowStore(getItem(position).getId());
					notifyDataSetChanged();
				}else{
					stores.get(position).setIs_selected("yes");
					fragmentInterestSection.followStore(getItem(position));
					notifyDataSetChanged();
				}
			}
				
			}
		});
		return convertView;
	}

	public class ViewHolder {
		private ImageView storeImageView,checkImageView;
		private TextView storeNameTextView;
		private ProgressBar progressBar_follow_brand;
	}
}

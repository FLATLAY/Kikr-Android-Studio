package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentActivityCollectionDetail;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.CollectionMonthDetailList;
import com.kikrlib.bean.User;

public class ActivityPageAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	private List<CollectionMonthDetailList> data = new ArrayList<CollectionMonthDetailList>();
	private String month,year;
	private User user;

	public ActivityPageAdapter(Activity context, List<CollectionMonthDetailList> data, String month, String year, User user) {
		super();
		this.mContext = context;
		this.data = data;
		this.month = month;
		this.year = year;
		this.user = user;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public CollectionMonthDetailList getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_activity_page, null);
			viewHolder = new ViewHolder();
			LayoutParams layoutParams = new LayoutParams(CommonUtility.getDeviceWidthActivity(mContext)/4, CommonUtility.getDeviceWidthActivity(mContext)/4);
			viewHolder.view1 =  (View) convertView.findViewById(R.id.view1);
			viewHolder.view2 =  (View) convertView.findViewById(R.id.view2);
			viewHolder.view3 =  (View) convertView.findViewById(R.id.view3);
			viewHolder.view_bottom =  (View) convertView.findViewById(R.id.view_bottom);
			viewHolder.collection_image_1 = (ImageView) convertView.findViewById(R.id.collection_image_1);
			viewHolder.collection_image_2 = (ImageView) convertView.findViewById(R.id.collection_image_2);
			viewHolder.collection_image_3 = (ImageView) convertView.findViewById(R.id.collection_image_3);
			viewHolder.collection_image_4 = (ImageView) convertView.findViewById(R.id.collection_image_4);
			viewHolder.collection_image_1.setLayoutParams(layoutParams);
			viewHolder.collection_image_2.setLayoutParams(layoutParams);
			viewHolder.collection_image_3.setLayoutParams(layoutParams);
			viewHolder.collection_image_4.setLayoutParams(layoutParams);
			viewHolder.collection_name = (TextView) convertView.findViewById(R.id.collection_name);
			viewHolder.collection_created_at = (TextView) convertView.findViewById(R.id.collection_created_at);
			viewHolder.collection_views = (TextView) convertView.findViewById(R.id.collection_views);
			viewHolder.collection_payout_text = (TextView) convertView.findViewById(R.id.collection_payout_text);
			viewHolder.detail_collection_img = (ImageView) convertView.findViewById(R.id.detail_collection_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		List<ImageView> images = new ArrayList<ImageView>();
		images.add(viewHolder.collection_image_1);
		images.add(viewHolder.collection_image_2);
		images.add(viewHolder.collection_image_3);
		images.add(viewHolder.collection_image_4);
		for(int i=0;i<images.size();i++){
			images.get(i).setVisibility(View.GONE);
		}
		List<View> views = new ArrayList<View>();
		views.add(viewHolder.view1);
		views.add(viewHolder.view2);
		views.add(viewHolder.view3);
		for(int i=0;i<views.size();i++){
			views.get(i).setVisibility(View.GONE);
		}
		for (int i = 0; i < images.size()&&i<getItem(position).getCollection_images().size(); i++) {
			CommonUtility.setImage(mContext, getItem(position).getCollection_images().get(i).getProductimageurl(), images.get(i), R.drawable.dum_list_item_product);
			images.get(i).setVisibility(View.VISIBLE);
			if (i <=2) {
				views.get(i).setVisibility(View.VISIBLE);
			}
		}
		viewHolder.collection_name.setText(getItem(position).getCollection_name());
		if (!TextUtils.isEmpty(getItem(position).getPayout())&&!TextUtils.isEmpty(getItem(position).getCollection_views())) {
			viewHolder.collection_payout_text.setText("Collection Views: "+getItem(position).getCollection_views()+"\nCollection Payout: $"+CommonUtility.getFormatedNum(getItem(position).getPayout()));
		} else {
			viewHolder.collection_payout_text.setText("Collection Views: 0\nCollection Payout: $0");
		}
		if (!TextUtils.isEmpty(getItem(position).getPayout())&&!TextUtils.isEmpty(getItem(position).getCollection_views())) {
			viewHolder.collection_views.setText("Product Views: "+getItem(position).getProduct_views()+"\nItem Saves: "+getItem(position).getItems_save());
		} else {
			viewHolder.collection_views.setText("Product Views: 0\n Item Saves: 0");
		}
		if (!TextUtils.isEmpty(getItem(position).getDate())) {
			viewHolder.collection_created_at.setText(CommonUtility.setChangeDateFormat(getItem(position).getDate()));
		}
		if(getItem(position).getCollection_images().size()>0)
			viewHolder.view_bottom.setVisibility(View.VISIBLE);
		else
			viewHolder.view_bottom.setVisibility(View.GONE);
		viewHolder.detail_collection_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFragment(new FragmentActivityCollectionDetail(month,year,getItem(position).getCollection_id(),user));
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView collection_name,collection_views,collection_payout_text,collection_created_at;
		ImageView collection_image_1;
		ImageView collection_image_2;
		ImageView collection_image_3;
		ImageView collection_image_4;
		ImageView detail_collection_img;
		View view1,view2,view3,view_bottom;
	}

	public void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
	
}

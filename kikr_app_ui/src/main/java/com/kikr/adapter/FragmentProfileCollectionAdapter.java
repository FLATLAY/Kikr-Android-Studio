package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.dialog.CollectionOptionsDialog;
import com.kikr.dialog.InspirationCollectionListDialog;
import com.kikr.fragment.FragmentProductBasedOnType;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.ProfileCollectionList;
import com.kikrlib.bean.TaggedItem;
import com.kikrlib.db.UserPreference;

public class FragmentProfileCollectionAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	List<ProfileCollectionList> data;
	private String user_id;
	private FragmentProfileView fragmentProfileView;
	private TaggedItem taggedItem;
	private InspirationCollectionListDialog inspirationCollectionListDialog;

	public FragmentProfileCollectionAdapter(Activity context,List<ProfileCollectionList> data,String user_id, FragmentProfileView fragmentProfileView,TaggedItem taggedItem,InspirationCollectionListDialog  inspirationCollectionListDialog) {
		super();
		this.mContext = context;
		this.data = data;
		this.user_id=user_id;
		this.taggedItem = taggedItem;
		this.fragmentProfileView=fragmentProfileView;
		this.inspirationCollectionListDialog = inspirationCollectionListDialog;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public ProfileCollectionList getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_profile, null);
			viewHolder = new ViewHolder();
			LayoutParams layoutParams =new LinearLayout.LayoutParams(CommonUtility.getDeviceWidthActivity(mContext)/4, CommonUtility.getDeviceWidthActivity(mContext)/4);
			viewHolder.view1 =  (View) convertView.findViewById(R.id.view1);
			viewHolder.view2 =  (View) convertView.findViewById(R.id.view2);
			viewHolder.view3 =  (View) convertView.findViewById(R.id.view3);
			viewHolder.view5 =  (View) convertView.findViewById(R.id.view5);
			viewHolder.view6 =  (View) convertView.findViewById(R.id.view6);
			viewHolder.view7 =  (View) convertView.findViewById(R.id.view7);
			viewHolder.view_first_bottom =  (View) convertView.findViewById(R.id.view_first_bottom);
			viewHolder.view_bottom =  (View) convertView.findViewById(R.id.view_bottom);
			viewHolder.collection_image_1 =  (ImageView) convertView.findViewById(R.id.collection_image_1);
			viewHolder.collection_image_2 =  (ImageView) convertView.findViewById(R.id.collection_image_2);
			viewHolder.collection_image_3 =  (ImageView) convertView.findViewById(R.id.collection_image_3);
			viewHolder.collection_image_4 =  (ImageView) convertView.findViewById(R.id.collection_image_4);
			viewHolder.collection_image_5 =  (ImageView) convertView.findViewById(R.id.collection_image_5);
			viewHolder.collection_image_6 =  (ImageView) convertView.findViewById(R.id.collection_image_6);
			viewHolder.collection_image_7 =  (ImageView) convertView.findViewById(R.id.collection_image_7);
			viewHolder.collection_image_8 =  (ImageView) convertView.findViewById(R.id.collection_image_8);
			viewHolder.detail_collection_img =  (ImageView) convertView.findViewById(R.id.detail_collection_img);
			viewHolder.collection_created_at =  (TextView) convertView.findViewById(R.id.collection_created_at);
			viewHolder.collection_name = (TextView) convertView.findViewById(R.id.collection_name);
			viewHolder.collection_name_inspiration = (TextView) convertView.findViewById(R.id.collection_name_inspiration);
			viewHolder.name_layout = (RelativeLayout) convertView.findViewById(R.id.name_layout);
			viewHolder.collection_image_1.setLayoutParams(layoutParams);
			viewHolder.collection_image_2.setLayoutParams(layoutParams);
			viewHolder.collection_image_3.setLayoutParams(layoutParams);
			viewHolder.collection_image_4.setLayoutParams(layoutParams);
			viewHolder.collection_image_5.setLayoutParams(layoutParams);
			viewHolder.collection_image_6.setLayoutParams(layoutParams);
			viewHolder.collection_image_7.setLayoutParams(layoutParams);
			viewHolder.collection_image_8.setLayoutParams(layoutParams);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.collection_name.setText(getItem(position).getName());
		viewHolder.collection_name_inspiration.setText(getItem(position).getName());
		viewHolder.collection_created_at.setText(CommonUtility.setChangeDateFormat(getItem(position).getDate()));
		List<ImageView> images = new ArrayList<ImageView>();
		images.add(viewHolder.collection_image_1);
		images.add(viewHolder.collection_image_2);
		images.add(viewHolder.collection_image_3);
		images.add(viewHolder.collection_image_4);
		images.add(viewHolder.collection_image_5);
		images.add(viewHolder.collection_image_6);
		images.add(viewHolder.collection_image_7);
		images.add(viewHolder.collection_image_8);
		for(int i=0;i<images.size();i++){
			images.get(i).setVisibility(View.GONE);
		}
		List<View> views = new ArrayList<View>();
		views.add(viewHolder.view1);
		views.add(viewHolder.view2);
		views.add(viewHolder.view3);
		views.add(viewHolder.view5);
		views.add(viewHolder.view6);
		views.add(viewHolder.view7);
		for(int i=0;i<views.size();i++){
			views.get(i).setVisibility(View.INVISIBLE);
		}
		
		OnClickListener listener=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(fragmentProfileView!=null) {
					if (((HomeActivity) mContext).checkInternet()) {
						boolean canDeleteProduct = user_id.equals(UserPreference.getInstance().getUserID());
						addFragment(new FragmentProductBasedOnType(user_id, getItem(position).getId(), true));
					}
				}else if(fragmentProfileView==null){
					taggedItem.setSelectedItem(getItem(position).getId());
					taggedItem.setSelectedItemName(getItem(position).getName());
					inspirationCollectionListDialog.dismiss();
				}
			}
		};
		
		
		for (int i = 0; i < images.size()&&i<getItem(position).getCollection_images().size(); i++) {
			CommonUtility.setImage(mContext, getItem(position).getCollection_images().get(i).getProductimageurl(), images.get(i), R.drawable.dum_list_item_product);
			images.get(i).setVisibility(View.VISIBLE);
			if (i <= 2) {
				views.get(i).setVisibility(View.VISIBLE);
			}
			if (i == 4) {
				views.get(3).setVisibility(View.VISIBLE);
			}
			if (i == 5) {
				views.get(4).setVisibility(View.VISIBLE);
			}
			if (i == 6) {
				views.get(5).setVisibility(View.VISIBLE);
			}
			images.get(i).setOnClickListener(listener);
		}
		if(getItem(position).getCollection_images().size()>0)
			viewHolder.view_first_bottom.setVisibility(View.VISIBLE);
		else
			viewHolder.view_first_bottom.setVisibility(View.GONE);
		if(getItem(position).getCollection_images().size()>4)
			viewHolder.view_bottom.setVisibility(View.VISIBLE);
		else
			viewHolder.view_bottom.setVisibility(View.GONE);
		if(fragmentProfileView==null){
			viewHolder.name_layout.setVisibility(View.GONE);
			viewHolder.collection_name_inspiration.setVisibility(View.VISIBLE);
		}
		viewHolder.detail_collection_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(((HomeActivity)mContext).checkInternet()){
					if(UserPreference.getInstance().getUserID().equals(user_id)){
						CollectionOptionsDialog dialog=new CollectionOptionsDialog(mContext,user_id,getItem(position).getId(),fragmentProfileView,getItem(position).getName());
						dialog.show();
					}else{
						addFragment(new FragmentProductBasedOnType(user_id, getItem(position).getId(),true));
					}
				}
			}
		});

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(fragmentProfileView==null){
					taggedItem.setSelectedItem(getItem(position).getId());
					taggedItem.setSelectedItemName(getItem(position).getName());
					inspirationCollectionListDialog.dismiss();
				}
			}
		});
		return convertView;
	}

	public class ViewHolder {
		ImageView collection_image_1;
		ImageView collection_image_2;
		ImageView collection_image_3;
		ImageView collection_image_4;
		ImageView collection_image_5;
		ImageView collection_image_6;
		ImageView collection_image_7;
		ImageView collection_image_8;
		ImageView detail_collection_img;
		TextView collection_name;
		TextView collection_created_at;
		View view1,view2,view3,view_bottom,view_first_bottom,view5,view6,view7;
		RelativeLayout name_layout;
		TextView collection_name_inspiration;
	}

	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}

}

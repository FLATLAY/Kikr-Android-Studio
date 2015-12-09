
package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.TextUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentDiscover;
import com.kikr.fragment.FragmentDiscoverDetail;
import com.kikr.fragment.FragmentFeatured;
import com.kikr.fragment.FragmentInspirationDetail;
import com.kikr.fragment.FragmentInspirationSection;
import com.kikr.fragment.FragmentProductBasedOnType;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.ui.ProductListUI;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.UiUpdate;
import com.kikrlib.api.BrandListApi;
import com.kikrlib.api.ProductBasedOnBrandApi;
import com.kikrlib.bean.FeaturedTabData;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProductFeedItem;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.BrandListRes;
import com.kikrlib.service.res.ProductBasedOnBrandRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FeaturedTabAdapter extends BaseAdapter{

	private FragmentActivity mContext;
	private FragmentFeatured fragmentFeatured;
	private LayoutInflater mInflater;
	private List<FeaturedTabData> brandsArray;
//	List<Product> data;
//	enum ItemType{brand,store,user};
	String BRAND="brand";
	String STORE="store";
	String USER="user";
	
	public FeaturedTabAdapter(FragmentActivity context, List<FeaturedTabData> brandsArray,FragmentFeatured fragmentFeatured) {
		super();
		this.mContext = context;
		this.fragmentFeatured = fragmentFeatured;
		this.brandsArray = brandsArray;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setData(List<FeaturedTabData> data){
		brandsArray.addAll(data);
	}
	
	@Override
	public int getCount() {
		return brandsArray.size();
	}

	@Override
	public FeaturedTabData getItem(int index) {
		return brandsArray.get(index);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewholder;
		if (convertView==null) {
			convertView = mInflater.inflate(R.layout.adapter_featured_large,null);
			viewholder = new ViewHolder();
			viewholder.userNameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);
			viewholder.userLargeImageView = (ImageView) convertView.findViewById(R.id.userLargeImageView);
			viewholder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
			viewholder.viewAllTextView = (TextView) convertView.findViewById(R.id.viewAllTextView);
			viewholder.imageLayout1 = (LinearLayout) convertView.findViewById(R.id.imageLayout1);
			viewholder.imageLayout2 = (LinearLayout) convertView.findViewById(R.id.imageLayout2);
			viewholder.image1 = (ImageView) convertView.findViewById(R.id.image1);
			viewholder.image2 = (ImageView) convertView.findViewById(R.id.image2);
			viewholder.image3 = (ImageView) convertView.findViewById(R.id.image3);
			viewholder.image4 = (ImageView) convertView.findViewById(R.id.image4);
			viewholder.image5 = (ImageView) convertView.findViewById(R.id.image5);
			viewholder.image6 = (ImageView) convertView.findViewById(R.id.image6);
			viewholder.list.add(viewholder.image1);
			viewholder.list.add(viewholder.image2);
			viewholder.list.add(viewholder.image3);
			viewholder.list.add(viewholder.image4);
			viewholder.list.add(viewholder.image5);
			viewholder.list.add(viewholder.image6);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		viewholder.imageLayout1.setVisibility(View.VISIBLE);
		viewholder.imageLayout2.setVisibility(View.VISIBLE);
		viewholder.userNameTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (getItem(position).getType() != null) {
					if (getItem(position).getType().equals(USER))
						addFragment(new FragmentProfileView(getItem(position).getItem_id(), "no"));
					else
						addFragment(new FragmentProductBasedOnType(getItem(position).getType(), getItem(position).getItem_name(), getItem(position).getItem_id()));
				}
			}
		});
		viewholder.viewAllTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (getItem(position).getType() != null) {
					if (getItem(position).getType().equals(USER))
						addFragment(new FragmentInspirationSection(false, getItem(position).getItem_id()));
					else
						addFragment(new FragmentProductBasedOnType(getItem(position).getType(), getItem(position).getItem_name(), getItem(position).getItem_id()));
				}
			}
		});
		String name=getItem(position).getItem_name();
		if(!TextUtils.isEmpty(name))
			viewholder.userNameTextView.setText(name);
		else
			viewholder.userNameTextView.setText("Unknown");
		if(!TextUtils.isEmpty(getItem(position).getDescription()))
			viewholder.descriptionTextView.setText(getItem(position).getDescription());
		else
			viewholder.descriptionTextView.setVisibility(View.GONE);
//		if(getItem(position).getType().equals(ItemType.user))
//			CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewholder.userLargeImageView, R.drawable.linesplaceholder);
//		else
			CommonUtility.setImage(mContext, getItem(position).getItem_image(), viewholder.userLargeImageView, R.drawable.linesplaceholder);

		final List<Product> data = getItem(position).getProducts();
		final List<Inspiration> feed = getItem(position).getInspiration_feed();

		if((data!=null&&data.size()==0)||(feed!=null&&feed.size()==0)){
			viewholder.imageLayout1.setVisibility(View.GONE);
			viewholder.imageLayout2.setVisibility(View.GONE);
		}else if((data!=null&&data.size()<4)||(feed!=null&&feed.size()<4)){
			viewholder.imageLayout2.setVisibility(View.GONE);
		}
		for(int j=0;j<viewholder.list.size();j++){
			if((data!=null&&data.size()>j)||(feed!=null&&feed.size()>j)){
				if(data!=null)
			    	CommonUtility.setImage(mContext, data.get(j).getProductimageurl(), viewholder.list.get(j), R.drawable.dum_list_item_product);	
				else if(feed!=null)
			    	CommonUtility.setImage(mContext, feed.get(j).getInspiration_image(), viewholder.list.get(j), R.drawable.dum_list_item_product);
				viewholder.list.get(j).setTag(j);
				viewholder.list.get(j).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int position = (Integer) v.getTag();
						if(data!=null){
							if (((HomeActivity) mContext).checkInternet()) {
								Bundle bundle=new Bundle();
								bundle.putSerializable("data", data.get(position));
								FragmentDiscoverDetail detail=new FragmentDiscoverDetail(new UiUpdate(){
	
									@Override
									public void updateUi() {
	//									TextView likeCountTextView=(TextView) v.findViewById(R.id.likeCountTextView);
	//									likeCountTextView.setText(TextUtils.isEmpty(data.get((Integer) v.getTag()).getLike_info().getLike_count())?"0":data.get((Integer) v.getTag()).getLike_info().getLike_count());
									}});
								detail.setArguments(bundle);
								addFragment(detail);
							}
						}else if(feed!=null){
							if (((HomeActivity) mContext).checkInternet()) {
								addFragment(new FragmentInspirationDetail(feed.get(position),false));
							}
						}
					}
				});
			}else{
				viewholder.list.get(j).setImageResource(R.drawable.white_image);
			}	
		}
		return convertView;
	}
	
	public class ViewHolder {
		TextView userNameTextView,viewAllTextView;
		ImageView userLargeImageView,image1,image2,image3,image4,image5,image6;
		TextView descriptionTextView;
		LinearLayout imageLayout1,imageLayout2;
		List<ImageView> list = new ArrayList<ImageView>();
	}

	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
	
}

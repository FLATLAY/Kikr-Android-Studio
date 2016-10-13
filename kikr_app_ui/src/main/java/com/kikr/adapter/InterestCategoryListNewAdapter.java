package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.flatlay.R;
import com.kikr.activity.FollowCategoriesNewActivity;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentInterestSection;
import com.kikr.fragment.FragmentProductBasedOnType;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.BrandStoreBasedOnCategoryApi;
import com.kikrlib.api.CategoryListApi;
import com.kikrlib.api.InterestSectionApi;
import com.kikrlib.bean.BrandStoreItem;
import com.kikrlib.bean.InterestSection;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.BrandStoreRes;
import com.kikrlib.service.res.CategoryRes;
import com.kikrlib.service.res.InterestSectionRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class InterestCategoryListNewAdapter extends BaseAdapter{
	private FragmentActivity mContext;
	private LayoutInflater inflater;
//	public boolean[] mSelectedItems;
	private List<InterestSection> categories=new ArrayList<InterestSection>();
	private FragmentInterestSection fragmentInterestSection;

	public InterestCategoryListNewAdapter(FragmentActivity mContext,List<InterestSection> stores, FragmentInterestSection fragmentInterestSection) {
		inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.categories=(ArrayList<InterestSection>) stores;
		this.fragmentInterestSection = fragmentInterestSection;
//		mSelectedItems=new boolean[stores.size()];
//		Arrays.fill(mSelectedItems, false);
		this.mContext = mContext;
	}
	
	public void setData(List<InterestSection> data){
		this.categories.addAll(data);
	}
	
	@Override
	public int getCount() {
		return categories.size();
	}

	@Override
	public InterestSection getItem(int index) {
		return categories.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder viewHolder;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.adapter_interest_category_list_new, null);
			viewHolder=new ViewHolder();
			viewHolder.categoryNameTextView=(TextView) convertView.findViewById(R.id.categoryNameTextView);
			viewHolder.followBtn = (Button) convertView.findViewById(R.id.followBtn);
			viewHolder.horizontalScroll = (HorizontalScrollView) convertView.findViewById(R.id.horizontalScroll);
			viewHolder.linearLayout =  (LinearLayout) convertView.findViewById(R.id.linearLayout);
			viewHolder.linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			viewHolder.pagenum = 0;
			viewHolder.mutex = false;
			
			
			final BrandStoreBasedOnCategoryApi brandStoreApi = new BrandStoreBasedOnCategoryApi(new ServiceCallback() {
				
				@Override
				public void handleOnSuccess(Object object) {
					Syso.info("In handleOnSuccess>>" + object);
					BrandStoreRes brandStoreRes = (BrandStoreRes) object;
					final List<BrandStoreItem> stores = brandStoreRes.getStores();
					final List<BrandStoreItem> brands = brandStoreRes.getBrands();
					
					for (int i = 0; i < stores.size(); i++){
			        	
			            View layout2 = LayoutInflater.from(mContext).inflate(R.layout.brand_store_category, null);

			            ImageView imgView = (ImageView) layout2.findViewById(R.id.imgBrandStore);
			            final Button button = (Button) layout2.findViewById(R.id.btnBrandStore);
			            
			            button.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								if(button.isSelected()) {
									button.setTextColor(mContext.getResources().getColor(R.color.black));
									button.setSelected(false);
								} else {
									button.setTextColor(mContext.getResources().getColor(R.color.white));
									button.setSelected(true);
								}
							}
						});
			            
			            final String storeName =  stores.get(i).getItem_name();
			            final String storeID = stores.get(i).getItem_id();
			            
			            imgView.setOnClickListener(new OnClickListener() {
			    			
			    			@Override
			    			public void onClick(View arg0) {
			    				((HomeActivity)	mContext).addFragment(new FragmentProductBasedOnType("store", storeName, storeID));
			    			}
			    		});

			            CommonUtility.setImage(mContext,stores.get(i).getItem_image(), imgView, R.drawable.ic_placeholder_brand);
			            viewHolder.linearLayout.addView(layout2);
			            notifyDataSetChanged();
			        }
					
					for (int i = 0; i < brands.size(); i++){
			        	
						View layout2 = LayoutInflater.from(mContext).inflate(R.layout.brand_store_category, null);


			            ImageView imgView = (ImageView) layout2.findViewById(R.id.imgBrandStore);
						final Button button = (Button) layout2.findViewById(R.id.btnBrandStore);
									            
						button.setOnClickListener(new OnClickListener() {
													
							@Override
							public void onClick(View v) {
								if(button.isSelected()) {
									button.setTextColor(mContext.getResources().getColor(R.color.black));
									button.setSelected(false);
								} else {
									button.setTextColor(mContext.getResources().getColor(R.color.white));
									button.setSelected(true);
								}
							}
						});

						final String brandName =  brands.get(i).getItem_name();
			            final String brandID = brands.get(i).getItem_id();
			            
						
						imgView.setOnClickListener(new OnClickListener() {
			    			
			    			@Override
			    			public void onClick(View arg0) {
			    				((HomeActivity)	mContext).addFragment(new FragmentProductBasedOnType("brand", brandName,  brandID));
			    			}
			    		});
						
			            CommonUtility.setImage(mContext,brands.get(i).getItem_image(), imgView, R.drawable.ic_placeholder_brand);
			            viewHolder.linearLayout.addView(layout2);
			            notifyDataSetChanged();
			        }
					
					viewHolder.mutex = false;
				}
				
				@Override
				public void handleOnFailure(ServiceException exception, Object object) {
					Syso.info("In handleOnFailure>>" + object);
					if (object != null) {
						BrandStoreRes response = (BrandStoreRes) object;
						AlertUtils.showToast(mContext, response.getMessage());
					} else {
						AlertUtils.showToast(mContext, R.string.invalid_response);
					}
				}
			});
	        brandStoreApi.getBrandsStoresByCategory(getItem(position).getId(), Integer.toString(viewHolder.pagenum));
	        brandStoreApi.execute();
	        
	        viewHolder.horizontalScroll.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int maxScrollX =  viewHolder.horizontalScroll.getChildAt(0).getMeasuredWidth()- viewHolder.horizontalScroll.getMeasuredWidth(); 
					
				//	Log.e("current and x coor", viewHolder.horizontalScroll.getScrollX() + "--" + maxScrollX);
					
					if(viewHolder.horizontalScroll.getScrollX() >= maxScrollX) {
						
						if(viewHolder.mutex == false) {
							viewHolder.mutex = true;
							viewHolder.pagenum++;
							Log.e("id and pagenum", getItem(position).getId()  + viewHolder.pagenum);
							if(getItem(position).getId().equals("4724")  && viewHolder.pagenum <= 18 || //womens
							   getItem(position).getId().equals("4730") && viewHolder.pagenum <= 15 || //mens
							   getItem(position).getId().equals("4778") && viewHolder.pagenum <= 2 || //unisex
							   getItem(position).getId().equals("4702") && viewHolder.pagenum <= 6 || //acessories
							   getItem(position).getId().equals("4733") && viewHolder.pagenum <= 2 || //health and beauty
							   getItem(position).getId().equals("4734") && viewHolder.pagenum <= 5 || //sports and fitness
							   getItem(position).getId().equals("4824") && viewHolder.pagenum <= 4 || //kids fashion
							   getItem(position).getId().equals("4720") && viewHolder.pagenum <= 3 || //electronics
							   getItem(position).getId().equals("4735") && viewHolder.pagenum <= 0 || //entertainment
							   getItem(position).getId().equals("4725") && viewHolder.pagenum <= 1) //homegoods
							{
								Log.e("pagenum" , viewHolder.pagenum+ "");
								brandStoreApi.getBrandsStoresByCategory(getItem(position).getId(), Integer.toString(viewHolder.pagenum));
						        brandStoreApi.execute();
							}
							
						}
						
					}
					
					return false;
				}

	            });

			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}

		viewHolder.categoryNameTextView.setText(getItem(position).getName());
		viewHolder.followBtn.setTag(position);
		viewHolder.horizontalScroll.setTag(position);		
		viewHolder.linearLayout.setTag(position);
        viewHolder.followBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(getItem(position).getIs_followed().equalsIgnoreCase("no")) {
					addCategory(getItem(position).getId(), position);
					viewHolder.followBtn.setText(" Followed ");
					viewHolder.followBtn.setTextColor(mContext.getResources().getColor(R.color.white));
					viewHolder.followBtn.setSelected(true);
					
				} else {
					deleteCategory(getItem(position).getId(), position);
					viewHolder.followBtn.setText(" Follow ");
					viewHolder.followBtn.setTextColor(mContext.getResources().getColor(R.color.black));
					viewHolder.followBtn.setSelected(false);
				}
				
			}
		});
		
        if(getItem(position).getIs_followed().equalsIgnoreCase("yes")){
			viewHolder.followBtn.setText(" Followed ");
			viewHolder.followBtn.setTextColor(mContext.getResources().getColor(R.color.white));
			viewHolder.followBtn.setSelected(true);
		}else{
			viewHolder.followBtn.setText(" Follow ");
			viewHolder.followBtn.setTextColor(mContext.getResources().getColor(R.color.black));
			viewHolder.followBtn.setSelected(false);
		}
        
        
		return convertView;
	}

	public class ViewHolder {
		TextView categoryNameTextView;
		Button followBtn;
		HorizontalScrollView horizontalScroll;
		LinearLayout linearLayout;
		int pagenum;
		boolean mutex; 
	}
	
	public void addCategory(String categoryID, final int position) {
		final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);

				getItem(position).setIs_followed("yes");
				Log.e("followed successfully","followed successfully");
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					CategoryRes response = (CategoryRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		listApi.addCategory(UserPreference.getInstance().getUserID(), categoryID);
		listApi.execute();
	}
	public void deleteCategory(String categoryID, final int position) {
		final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				Log.e("unfollowed","unfollowed");

				getItem(position).setIs_followed("no");
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					CategoryRes response = (CategoryRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		listApi.deleteCategory(UserPreference.getInstance().getUserID(), categoryID);
		listApi.execute();
	}
}

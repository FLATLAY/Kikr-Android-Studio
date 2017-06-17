package com.flatlay.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.CollectionListDialog;
import com.flatlay.dialog.DeleteCollectiontProductDialog;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.fragment.FragmentProductBasedOnInspiration;
import com.flatlay.fragment.FragmentProductBasedOnType;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class FragmentProductBasedOnTypeAdapter extends BaseAdapter {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<Product> data;
	private boolean isShowStar;
	private String collectionId;
	private ProgressBarDialog mProgressBarDialog;
	private FragmentProductBasedOnType productBasedOnType;
	private FragmentProductBasedOnInspiration productBasedOnInspiration;
	private FragmentProductBasedOnTypeAdapter adapter;

	public FragmentProductBasedOnTypeAdapter(FragmentActivity context, FragmentProductBasedOnType productBasedOnType, List<Product> data, boolean isShowStar,String collectionId) {
		super();
		this.mContext = context;
		this.data =  data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isShowStar=isShowStar;
		this.collectionId=collectionId;
		this.productBasedOnType=productBasedOnType;
		adapter=this;
	}
	
	public FragmentProductBasedOnTypeAdapter(FragmentActivity context, FragmentProductBasedOnInspiration productBasedOnInspiration, List<Product> data, boolean isShowStar,String collectionId) {
		super();
		this.mContext = context;
		this.data =  data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isShowStar=isShowStar;
		this.collectionId=collectionId;
		this.productBasedOnInspiration=productBasedOnInspiration;
		adapter=this;
		Log.w("Activity","FragmentProductBasedOnTypeAdapter");
	}
	
	public void setData(List<Product> data){
		this.data = data;
	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public Product getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_product_based_on_brand, null);
			viewHolder = new ViewHolder();
			viewHolder.activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);
			viewHolder.activity_product_list_product_name = (TextView) convertView.findViewById(R.id.activity_product_list_product_name);
			viewHolder.activity_product_list_star = (ImageView) convertView.findViewById(R.id.activity_product_list_star);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.activity_product_list_product_name.setText(data.get(position).getProductname());
		CommonUtility.setImage(mContext, data.get(position).getProductimageurl(), viewHolder.activity_product_list_product_image, R.drawable.dum_list_item_product);
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(isShowStar){
				Bundle bundle=new Bundle();
				bundle.putSerializable("data",data.get(position));
				FragmentDiscoverDetail detail=new FragmentDiscoverDetail(null);
				detail.setArguments(bundle);
				addFragment(detail);
				}else {
				DeleteCollectiontProductDialog dialog=new DeleteCollectiontProductDialog(mContext, adapter, data.get(position));
				dialog.show();
				}
//				addFragment(new FragmentDiscoverDetail(data.get(position)));
//				FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
//				fragmentManager.beginTransaction().replace(R.id.frame_container, new FragmentDiscoverDetail(data.get(position))).commit();
			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(isShowStar){
					if(((HomeActivity)mContext).isMenuShowing()){
						((HomeActivity)mContext).hideContextMenu();
					}else{
						((HomeActivity)mContext).showContextMenu(data.get(position),null);
					}
				}else{
//					DeleteCollectiontProductDialog dialog=new DeleteCollectiontProductDialog(mContext, adapter, data.get(position));
//					dialog.show();
				}
				return true;
//				Syso.info("In setOnLongClickListener");
//				if(((HomeActivity) mContext).checkInternet()){
//					if(isShowStar){
//						CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, data.get(position));
//						collectionListDialog.show();
//					}else{
//						DeleteCollectiontProductDialog dialog=new DeleteCollectiontProductDialog(mContext, adapter, data.get(position));
//						dialog.show();
//					}
//				}
//				return true;
				
			}
		});
		if(!isShowStar){
			viewHolder.activity_product_list_star.setImageResource(R.drawable.ic_delete);
		}
		viewHolder.activity_product_list_star.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(((HomeActivity) mContext).checkInternet()){
					if(isShowStar){
						CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, data.get(position));
						collectionListDialog.show();
					}else{
						DeleteCollectiontProductDialog dialog=new DeleteCollectiontProductDialog(mContext, adapter, data.get(position));
						dialog.show();
					}
				}
			}
		});
		return convertView;
	}

	public void deleteProductFromCollection(final Product productList) {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				AlertUtils.showToast(mContext, mContext.getResources().getString(R.string.alert_collection_product_deleted));
				productBasedOnType.data.remove(productList);
				notifyDataSetChanged();
				if(productBasedOnType.data.size()==0){
					productBasedOnType.showDataNotFound();
				}
				UserPreference.getInstance().setIsRefreshProfile(true);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					CollectionApiRes response = (CollectionApiRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		collectionApi.deleteProductFromCollection(UserPreference.getInstance().getUserID(), collectionId, productList.getId());
		collectionApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				collectionApi.cancel();
			}
		});		
	}

	public class ViewHolder {
		ImageView activity_product_list_product_image,activity_product_list_star;
		TextView activity_product_list_product_name;
	}

	private void addFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		((HomeActivity) mContext).addFragment(fragment);
	}
}

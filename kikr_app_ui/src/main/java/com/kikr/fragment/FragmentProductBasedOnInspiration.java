package com.kikr.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.adapter.FragmentProductBasedOnTypeAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikrlib.api.ActivityApi;
import com.kikrlib.api.InspirationSectionApi;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.InspirationRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentProductBasedOnInspiration extends BaseFragment implements OnClickListener,ServiceCallback{
	private View mainView;
	private GridView productBasedOnBrandList;
	private ProgressBarDialog mProgressBarDialog;
	private FragmentProductBasedOnTypeAdapter basedOnTypeAdapter;
	private int pageno=0;
	private boolean isFirstTime = true;
	private boolean isLoading=false;
	public List<Product> data=new ArrayList<Product>();
	private String item_name;
	private String item_id;
	private String item_type="none";
//	private FragmentDiscover mFragmentDiscover;
	private boolean isLoadProductBasedOnType;
	private boolean isShowStar=true;
	private String user_id;
	private String collection_id;
	private View loaderView;
	private FragmentProductBasedOnInspiration basedOnInspiration;

	public FragmentProductBasedOnInspiration(String item_type,String item_name,String item_id) {
		this.item_name= item_name;
		this.item_type=item_type;
		this.item_id=item_id;
		isLoadProductBasedOnType=true;
		basedOnInspiration=this;
	}
	
	public FragmentProductBasedOnInspiration(String user_id,String collection_id,boolean isShowStar) {
		this.user_id= user_id;
		this.collection_id=collection_id;
		isLoadProductBasedOnType=false;
		this.isShowStar=isShowStar;
		basedOnInspiration=this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.activity_product_based_on_brand, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		productBasedOnBrandList = (GridView) mainView.findViewById(R.id.productBasedOnBrandList);
		loaderView = View.inflate(mContext, R.layout.footer, null);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
	}

	@Override
	public void setData(Bundle bundle) {
		if(checkInternet())
			getProductList();
		else
			showReloadOption();
		
		productBasedOnBrandList.setOnScrollListener(new OnScrollListener() {
			   @Override
			   public void onScrollStateChanged(AbsListView view, 
			     int scrollState) {
			    // Do nothing
			   }

			   @Override
			   public void onScroll(AbsListView view, int firstVisibleItem, 
			     int visibleItemCount, int totalItemCount) {
//				   System.out.println("1234 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
			    if(!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
//			    	System.out.println("1234 inside if ");
			    	if(checkInternet2()){
				    	pageno++;
				    	isFirstTime=false;
				    	getProductList();
			    	}else{
			    		showReloadFotter();
			    	}
			    }
			   }
			  });
		if(checkInternet2()&&!isLoadProductBasedOnType&&!user_id.equals(UserPreference.getInstance().getUserID()))
			addCollectionView(); 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}
	
		private void getProductList() {
			if (!isFirstTime) {
				showFotter();
			} else {
				mProgressBarDialog = new ProgressBarDialog(mContext);
				mProgressBarDialog.show();
			}
			isLoading=!isLoading;
			
			final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(this);
			inspirationSectionApi.getProductsByInspiration(UserPreference.getInstance().getUserID(), item_id, Integer.toString(pageno));
			inspirationSectionApi.execute();
			
			mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					isLoading=!isLoading;
					inspirationSectionApi.cancel();
				}
			});		
		}

		
	
	@Override
	public void handleOnSuccess(Object object) {
		if (!isFirstTime) {
			hideFotter();
		} else {
			mProgressBarDialog.dismiss();
		}
		hideDataNotFound();
		isLoading=!isLoading;
		Syso.info("In handleOnSuccess>>" + object);
		InspirationRes inspirationRes = (InspirationRes) object;
		List<Product> productLists = inspirationRes.getData();
		
		if(!isLoadProductBasedOnType){
			for(int i=0;i<productLists.size();i++){
				productLists.get(i).setFrom_user_id(user_id);
				productLists.get(i).setFrom_collection_id(collection_id);
			}
		}
		
		if(productLists.size()>0){
			data.addAll(productLists);
		}
		//no need to load data if data in one page is less than 10
		if(productLists.size()<10){
			isLoading=true;
		}
		System.out.println("1234 data size "+data.size());
		if(data.size()==0&&isFirstTime){
			showDataNotFound();
		}else if (data.size() > 0 && isFirstTime) {
			basedOnTypeAdapter = new FragmentProductBasedOnTypeAdapter(mContext,basedOnInspiration, data,isShowStar,collection_id);
			productBasedOnBrandList.setAdapter(basedOnTypeAdapter);
		}  else{
			basedOnTypeAdapter.setData(data);
			basedOnTypeAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		if (!isFirstTime) {
//			productBasedOnBrandList.removeFooterView(loaderView);
		} else {
			mProgressBarDialog.dismiss();
		}
		isLoading=!isLoading;
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			InspirationRes response = (InspirationRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}

	private void showReloadOption() {
		showDataNotFound();
		TextView textView=getDataNotFound();
		if(textView!=null){
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternet())
						getProductList();
				}
			});
		}
	}
	
	protected void showReloadFotter() {
		TextView textView=getReloadFotter();
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkInternet()){
					pageno++;
			    	isFirstTime=false;
			    	getProductList();
				}
			}
		});
	}
	
	private void addCollectionView() {
		final ActivityApi activityApi = new ActivityApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				
			}

			@Override
			public void handleOnFailure(ServiceException exception,
					Object object) {
				
			}
		});
		activityApi.addCollectionView(user_id, collection_id);
		activityApi.execute();
	}
}

package com.kikr.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.DiscoverAdapter;
import com.kikr.adapter.FeaturedTabAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.FeaturedTabApi;
import com.kikrlib.api.ProductFeedApi;
import com.kikrlib.bean.FeaturedTabData;
import com.kikrlib.bean.ProductFeedItem;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.FeaturedTabApiRes;
import com.kikrlib.service.res.ProductFeedRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.personagraph.api.PGAgent;

public class FragmentFeatured extends BaseFragment{
	private View mainView;
	private ListView featuredList;
	public boolean isOpen = false;
	private ProgressBarDialog mProgressBarDialog;
	int page=0;
	private List<FeaturedTabData> product_list=new ArrayList<FeaturedTabData>();
	private boolean isLoading=false;
	private boolean isFirstTime = true;
	private FragmentFeatured fragmentFeatured;
	private FeaturedTabAdapter featuredTabAdapter;
	private int firstVisibleItem=0,visibleItemCount=0,totalItemCount=0;
	private View loaderView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(CommonUtility.isOnline(mContext))
			PGAgent.logEvent("FEATURED_SCREEN_OPEND");
			fragmentFeatured = this;
			mainView = inflater.inflate(R.layout.fragment_featured, null);
			return mainView;
	}
	
	@Override
	public void initUI(Bundle savedInstanceState) {
		featuredList=(ListView) mainView.findViewById(R.id.featuredList);		
		loaderView = View.inflate(mContext, R.layout.footer, null);
		
	}

	@Override
	public void setData(Bundle bundle) {
		if(checkInternet())	
			getFeaturedTabData();
		else
			showReloadOption();
		
		featuredList.setOnScrollListener(new OnScrollListener() {
			   @Override
			   public void onScrollStateChanged(AbsListView view, int scrollState) {}
			   @Override
			   public void onScroll(AbsListView view, int firstVisibleItem,  int visibleItemCount, int totalItemCount) {
				   FragmentFeatured.this.firstVisibleItem=firstVisibleItem;
				   FragmentFeatured.this.visibleItemCount=visibleItemCount;
				   FragmentFeatured.this.totalItemCount=totalItemCount;
			    if(!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
			    	if(checkInternet2()){
				    	page++;
				    	isFirstTime=false;
				    	getFeaturedTabData();
			    	}else{
			    		showReloadFotter();
			    	}
			    }
			   }
			  });
	}
	
	private void getFeaturedTabData() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		isLoading = !isLoading;
		if (!isFirstTime) {
			showFotter();
		} else {
			mProgressBarDialog.show();
		}
		
		
		final FeaturedTabApi listApi = new FeaturedTabApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				try{
					if (!isFirstTime) {
						hideFotter();
					} else {
						mProgressBarDialog.dismiss();
					}
					hideDataNotFound();
					isLoading=!isLoading;
					Syso.info("In handleOnSuccess>>" + object);
					FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;
					product_list.addAll(featuredTabApiRes.getData());
					if(featuredTabApiRes.getData().size()<1){
						isLoading=true;
					}
					if(product_list.size()==0&&isFirstTime){
						showDataNotFound();
					}else if (product_list.size() > 0 && isFirstTime) {
						featuredTabAdapter = new FeaturedTabAdapter(mContext, product_list,fragmentFeatured);
						featuredList.setAdapter(featuredTabAdapter);
					}  else if(featuredTabAdapter!=null){
						featuredTabAdapter.setData(product_list);
						featuredTabAdapter.notifyDataSetChanged();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if (!isFirstTime) {
					featuredList.removeFooterView(loaderView);
				} else {
					mProgressBarDialog.dismiss();
				}
				isLoading=!isLoading;
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					FeaturedTabApiRes response = (FeaturedTabApiRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		listApi.getFeaturedTabData(UserPreference.getInstance().getUserID(), String.valueOf(page));
		listApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				showDataNotFound();
				listApi.cancel();
			}
		});
	}

	private void showReloadOption() {
		showDataNotFound();
		TextView textView=getDataNotFound();
		Syso.info("text view>>"+textView);
		if(textView!=null){
			Syso.info("12233 inside text view text view>>"+textView);
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternet())
						getFeaturedTabData();
					Syso.info("text view>>");
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
					page++;
			    	isFirstTime=false;
			    	getFeaturedTabData();
				}
			}
		});
	}

	@Override
	public void refreshData(Bundle bundle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClickListener() {
		// TODO Auto-generated method stub
		
	}

}

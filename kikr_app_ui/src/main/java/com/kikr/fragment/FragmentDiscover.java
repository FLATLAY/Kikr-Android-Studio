package com.kikr.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.DiscoverAdapter;
import com.kikr.dialog.HelpPressMenuDialog;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.ProductFeedApi;
import com.kikrlib.bean.ProductFeedItem;
import com.kikrlib.db.HelpPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ProductFeedRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.personagraph.api.PGAgent;

public class FragmentDiscover extends BaseFragment implements OnClickListener,ServiceCallback{
	private View mainView;
	private ListView discoverList;
	public boolean isOpen = false;
	private ProgressBarDialog mProgressBarDialog;
	int page=0;
	private List<ProductFeedItem> product_list=new ArrayList<ProductFeedItem>();
	private HomeActivity homeActivity;
	private boolean isLoading=false;
	private boolean isFirstTime = true;
	private DiscoverAdapter discoverAdapter;
	private FragmentDiscover fragmentDiscover;
	private int firstVisibleItem=0,visibleItemCount=0,totalItemCount=0;
	private View loaderView;
	private TextView loadingTextView;
	
	public FragmentDiscover() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if(CommonUtility.isOnline(mContext))
		PGAgent.logEvent("DISCOVER_SCREEN_OPEND");
		homeActivity=(HomeActivity) getActivity();
		fragmentDiscover = this;
		mainView = inflater.inflate(R.layout.fragment_discover, null);
		return mainView;
	}
	
	@Override
	public void onClick(View v) {}

	@Override
	public void initUI(Bundle savedInstanceState) {
		discoverList=(ListView) mainView.findViewById(R.id.discoverList);		
		loaderView = View.inflate(mContext, R.layout.footer, null);
		loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
	}

	@Override
	public void setData(Bundle bundle) {
		if(checkInternet())	
			getBrandAndProductList();
		else
			showReloadOption();
		
		discoverList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				FragmentDiscover.this.firstVisibleItem = firstVisibleItem;
				FragmentDiscover.this.visibleItemCount = visibleItemCount;
				FragmentDiscover.this.totalItemCount = totalItemCount;
				if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
					if (checkInternet2()) {
						page++;
						isFirstTime = false;
						getBrandAndProductList();
					} else {
						showReloadFotter();
					}
				}
			}
		});
		
//		if (HelpPreference.getInstance().getHelpPinMenu().equals("yes")) {
//			HelpPreference.getInstance().setHelpPinMenu("no");
//			HelpPressMenuDialog helpPressMenuDialog = new HelpPressMenuDialog(mContext);
//			helpPressMenuDialog.show();
//		}
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if(menuVisible){
			if (HelpPreference.getInstance().getHelpPinMenu().equals("yes")) {
				HelpPreference.getInstance().setHelpPinMenu("no");
				HelpPressMenuDialog helpPressMenuDialog = new HelpPressMenuDialog(mContext);
				helpPressMenuDialog.show();
			}
		}
	}

	private void getBrandAndProductList() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		isLoading = !isLoading;
		if (!isFirstTime) {
			showFotter();
		} else {
			loadingTextView.setVisibility(View.VISIBLE);
//			mProgressBarDialog.show();
		}
		
		
		final ProductFeedApi listApi = new ProductFeedApi(this);
		listApi.getProductList(UserPreference.getInstance().getUserID(), String.valueOf(page));
		listApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				showDataNotFound();
				listApi.cancel();
			}
		});
	}
	
	@Override
	public void handleOnSuccess(Object object) {
		try{
			if (!isFirstTime) {
				hideFotter();
			} else {
				loadingTextView.setVisibility(View.GONE);
//				mProgressBarDialog.dismiss();
			}
			hideDataNotFound();
			isLoading=!isLoading;
			Syso.info("In handleOnSuccess>>" + object);
			ProductFeedRes productAndBrandListRes = (ProductFeedRes) object;
			product_list.addAll(productAndBrandListRes.getData());
			homeActivity.list=product_list;
			if(productAndBrandListRes.getData().size()<1){
				isLoading=true;
			}
			if(product_list.size()==0&&isFirstTime){
				showDataNotFound();
			}else if (homeActivity.list.size() > 0 && isFirstTime) {
				discoverAdapter = new DiscoverAdapter(mContext, product_list,fragmentDiscover);
				discoverList.setAdapter(discoverAdapter);
			}  else if(discoverAdapter!=null){
				discoverAdapter.notifyDataSetChanged();
			}else{
				discoverAdapter = new DiscoverAdapter(mContext, product_list,fragmentDiscover);
				discoverList.setAdapter(discoverAdapter);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		if (!isFirstTime) {
			discoverList.removeFooterView(loaderView);
		} else {
			loadingTextView.setVisibility(View.GONE);
//			mProgressBarDialog.dismiss();
		}
		isLoading=!isLoading;
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			ProductFeedRes response = (ProductFeedRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}


	@Override
	public void refreshData(Bundle bundle) {
	}

	@Override
	public void setClickListener() {}
	

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
						getBrandAndProductList();
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
			    	getBrandAndProductList();
				}
			}
		});
	}
	
	public void refresh(){
		Handler handler=new Handler();
		Runnable runnable=new Runnable() {
			
			@Override
			public void run() {
				((HomeActivity)mContext).loadFragment(new FragmentDiscover());
			}
		};
		handler.postDelayed(runnable, 100);
	}

}

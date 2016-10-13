package com.flatlay.activity;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.adapter.FollowStoreAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.api.InterestSectionApi;
import com.kikrlib.bean.InterestSection;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.InterestSectionRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FollowStoreActivity extends BaseActivity implements OnItemClickListener,OnClickListener{
	private ListView mStoreListView;
	private FollowStoreAdapter followStoreAdapter;
	private TextView mRightText;
	private ProgressBarDialog mProgressBarDialog;
	private int pagenum = 0;
	private boolean isLoading=false;
	private boolean isFirstTime = true;
	private int selectedCount = 0;
	private TextView followText;
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		UserPreference.getInstance().setCurrentScreen(Screen.StoreScreen);
		setContentView(R.layout.activity_follow_store);
		if(checkInternet())
			getStoreList();
		else
			showReloadOption();
		updateScreen(Screen.StoreScreen);
	}

	@Override
	public void initLayout() {
		mStoreListView=(ListView) findViewById(R.id.storeListView);
		followText=(TextView) findViewById(R.id.followText);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		followText.setText("Now follow some stores " + selectedCount + "/3");
	}

	@Override
	public void setupData() {
		mStoreListView.setOnScrollListener(new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view,int scrollState) {}
		 @Override
		   public void onScroll(AbsListView view, int firstVisibleItem, 
		     int visibleItemCount, int totalItemCount) {
			   System.out.println("123456 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
		    if(!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
		    	if(checkInternet()){
			    	pagenum++;
			    	isFirstTime = false;
			    	getStoreList();
		    	}
		    }
		   }
		  });
	}

	@Override
	public void headerView() {
		setBackHeader();
		mRightText = getRightText();
		mRightText.setVisibility(View.VISIBLE);
		mRightText.setText("Next");
		mRightText.setOnClickListener(this);
		mRightText.setClickable(false);
		mRightText.setTextColor(getResources().getColor(R.color.btn_gray));
		getLeftTextView().setVisibility(View.GONE);
	}

	@Override
	public void setUpTextType() {
		
	}

	@Override
	public void setClickListener() {
		mStoreListView.setOnItemClickListener(this);
	}

	private void showReloadOption() {
		showDataNotFound();
		TextView textView=getDataNotFound();
		if(textView!=null){
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternet())
						getStoreList();
				}
			});
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		if(checkInternet()){
			if(followStoreAdapter.stores.get(position).getIs_followed().equalsIgnoreCase("no"))
				followStore(followStoreAdapter.getItem(position).getId(),v,position);
			else
				unFollowStore(followStoreAdapter.getItem(position).getId(),v,position);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.menuRightText:
			goToNextScreen();
			break;
		}
		
	}

	private void getStoreList() {
		isLoading=!isLoading;
		mProgressBarDialog = new ProgressBarDialog(context);
		if(pagenum>0)
			showFooter();
		else
			mProgressBarDialog.show();
		
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFutter();
				hideDataNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				isLoading=!isLoading;
				InterestSectionRes interestSectionRes = (InterestSectionRes) object;
				List<InterestSection> storeList=interestSectionRes.getData();
				if(storeList.size()<10){
					isLoading=true;
				}
				if (storeList.size() == 0 && isFirstTime)
					showDataNotFound();
				else if (storeList.size() > 0 && isFirstTime) {
					hideDataNotFound();
					followStoreAdapter = new FollowStoreAdapter(context,storeList);
					mStoreListView.setAdapter(followStoreAdapter);
					
					
					for(int i = 0; i < followStoreAdapter.getCount(); i++) {
						if(followStoreAdapter.getItem(i).getIs_followed().equalsIgnoreCase("yes"))
							selectedCount++;
					}
					
					followText.setText("Now follow some stores " + selectedCount + "/3");
					
					if(selectedCount == 0)
						progressBar.setProgress(0);
					else if(selectedCount == 1)
						progressBar.setProgress(33);
					else if(selectedCount == 2)
						progressBar.setProgress(66);
					else if(selectedCount == 3)
						progressBar.setProgress(100);					
				} else{
					followStoreAdapter.setData(storeList);
					followStoreAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFutter();
				Syso.info("In handleOnFailure>>" + object);
				isLoading=!isLoading;
				if (object != null) {
					InterestSectionRes response = (InterestSectionRes) object;
					AlertUtils.showToast(context, response.getMessage());
				} else {
					AlertUtils.showToast(context, R.string.invalid_response);
				}
			}
		});
		interestSectionApi.getStoreList(Integer.toString(pagenum));
		interestSectionApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isLoading=!isLoading;
				interestSectionApi.cancel();
			}
		});
	}
	
	public void followStore(String id,final View v, final int position) {
		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				if(followStoreAdapter.stores.get(position).getIs_followed().equalsIgnoreCase("no")){
					selectedCount++;
					followText.setText("Now follow some stores " + selectedCount + "/3");
					if(selectedCount == 1)
						progressBar.setProgress(33);
					else if(selectedCount == 2)
						progressBar.setProgress(66);
					else if(selectedCount == 3)
						progressBar.setProgress(100);
					followStoreAdapter.stores.get(position).setIs_followed("yes");
				}else{
					selectedCount--;
					followText.setText("Now follow some stores " + selectedCount + "/3");
					if(selectedCount == 0)
						progressBar.setProgress(0);
					else if(selectedCount == 1)
						progressBar.setProgress(33);
					else if(selectedCount == 2)
						progressBar.setProgress(66);
					else if(selectedCount == 3)
						progressBar.setProgress(100);
					followStoreAdapter.stores.get(position).setIs_followed("no");
				}
				if (selectedCount>=3) {
					mRightText.setTextColor(getResources().getColor(R.color.btn_green));
					mRightText.setClickable(true);
				} else {
					mRightText.setTextColor(getResources().getColor(R.color.btn_gray));
					mRightText.setClickable(false);
				}
				followStoreAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					InterestSectionRes response = (InterestSectionRes) object;
					AlertUtils.showToast(context, response.getMessage());
				} else {
					AlertUtils.showToast(context, R.string.invalid_response);
				}	
			}
		});
		interestSectionApi.followStore(UserPreference.getInstance().getUserID(),id);
		interestSectionApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				interestSectionApi.cancel();
			}
		});
	}
	
	public void unFollowStore(String id,final View v,final int position) {
		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				if(followStoreAdapter.stores.get(position).getIs_followed().equalsIgnoreCase("no")){
					selectedCount++;
					followText.setText("Now follow some stores " + selectedCount + "/3");
					if(selectedCount == 1)
						progressBar.setProgress(33);
					else if(selectedCount == 2)
						progressBar.setProgress(66);
					else if(selectedCount == 3)
						progressBar.setProgress(100);
					followStoreAdapter.stores.get(position).setIs_followed("yes");
				}else{
					selectedCount--;
					followText.setText("Now follow some stores " + selectedCount + "/3");
					if(selectedCount == 0)
						progressBar.setProgress(0);
					else if(selectedCount == 1)
						progressBar.setProgress(33);
					else if(selectedCount == 2)
						progressBar.setProgress(66);
					else if(selectedCount == 3)
						progressBar.setProgress(100);
					followStoreAdapter.stores.get(position).setIs_followed("no");
				}
				if (selectedCount>=3) {
					mRightText.setTextColor(getResources().getColor(R.color.btn_green));
					mRightText.setClickable(true);
				} else {
					mRightText.setTextColor(getResources().getColor(R.color.btn_gray));
					mRightText.setClickable(false);
				}
				followStoreAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					InterestSectionRes response = (InterestSectionRes) object;
					AlertUtils.showToast(context, response.getMessage());
				} else {
					AlertUtils.showToast(context, R.string.invalid_response);
				}	
			}
		});
		interestSectionApi.unFollowStore(UserPreference.getInstance().getUserID(),id);
		interestSectionApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				interestSectionApi.cancel();
			}
		});
	}
	
	private void goToNextScreen() {
		startActivity(FollowBrandsActivity.class);
		finish();
	}

//	@Override
//	public void onBackPressed() {}
}

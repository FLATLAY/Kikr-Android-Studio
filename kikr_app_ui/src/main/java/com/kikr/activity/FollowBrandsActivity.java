package com.kikr.activity;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kikr.BaseActivity;
import com.flatlay.R;
import com.kikr.adapter.FollowBrandsAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants.Screen;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.BrandListApi;
import com.kikrlib.bean.BrandList;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.BrandListRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FollowBrandsActivity extends BaseActivity implements OnItemClickListener,OnClickListener, ServiceCallback{
	private ListView mBrandsListView;
	private FollowBrandsAdapter brandsAdapter;
	private TextView mRightText;
	private ProgressBarDialog mProgressBarDialog;
	private int pagenum = 0;
	private boolean isLoading=false;
	private boolean isFirstTime = true;
	private int selectedCount = 0;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		UserPreference.getInstance().setCurrentScreen(Screen.BrandScreen);
		setContentView(R.layout.activity_follow_brands);
		if(checkInternet())
			getBrandList();
		else
			showReloadOption();
		updateScreen(Screen.BrandScreen);
	}

	@Override
	public void initLayout() {
		mBrandsListView=(ListView) findViewById(R.id.brandsListView);
	}

	@Override
	public void setupData() {
		mBrandsListView.setOnScrollListener(new OnScrollListener() {
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
			    	getBrandList();
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
		mRightText.setText("Skip");
		mRightText.setOnClickListener(this);
		mRightText.setTextColor(getResources().getColor(R.color.btn_green));
		getLeftTextView().setVisibility(View.GONE);
	}

	@Override
	public void setUpTextType() {
		
	}

	@Override
	public void setClickListener() {
		mBrandsListView.setOnItemClickListener(this);
	}

	private void showReloadOption() {
		showDataNotFound();
		TextView textView=getDataNotFound();
		if(textView!=null){
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternet())
						getBrandList();
				}
			});
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
//		currentPosition = position;
		if(checkInternet()){
			if(brandsAdapter.brands.get(position).getIs_followed().equalsIgnoreCase("no"))
				addBrand(brandsAdapter.getItem(position).getId(),v,position);
			else
				deleteBrand(brandsAdapter.getItem(position).getId(),v,position);
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

	private void getBrandList() {
		isLoading=!isLoading;
		mProgressBarDialog = new ProgressBarDialog(context);
		if(pagenum>0)
			showFooter();
		else
			mProgressBarDialog.show();
		final BrandListApi listApi = new BrandListApi(this);
		listApi.getBrandList(Integer.toString(pagenum));
		listApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				listApi.cancel();
			}
		});
	}
	
	public void addBrand(String brand_id,final View v, final int position) {
		final ImageView checkImageView = (ImageView)v.findViewById(R.id.checkImageView);
		checkImageView.setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final BrandListApi listApi = new BrandListApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				checkImageView.setVisibility(View.VISIBLE);
				checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				Syso.info("In handleOnSuccess>>" + object);
				if(brandsAdapter.brands.get(position).getIs_followed().equalsIgnoreCase("no")){
					selectedCount++;
					brandsAdapter.brands.get(position).setIs_followed("yes");
				}else{
					selectedCount--;
					brandsAdapter.brands.get(position).setIs_followed("no");
				}
				if (selectedCount>0) {
					mRightText.setText("Next");
				} else {
					mRightText.setText("Skip");
				}
				brandsAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				checkImageView.setVisibility(View.VISIBLE);
				checkImageView.setImageResource(R.drawable.ic_add_collection);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					BrandListRes response = (BrandListRes) object;
					AlertUtils.showToast(context, response.getMessage());
				} else {
					AlertUtils.showToast(context, R.string.invalid_response);
				}
			}
		});
		listApi.addBrands(brand_id);
		listApi.execute();
	}
	
	public void deleteBrand(String brand_id,final View v, final int position) {
		final ImageView checkImageView = (ImageView)v.findViewById(R.id.checkImageView);
		checkImageView.setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final BrandListApi listApi = new BrandListApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				checkImageView.setVisibility(View.VISIBLE);
				checkImageView.setImageResource(R.drawable.ic_add_collection);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				Syso.info("In handleOnSuccess>>" + object);
				if(brandsAdapter.brands.get(position).getIs_followed().equalsIgnoreCase("no")){
					selectedCount++;
					brandsAdapter.brands.get(position).setIs_followed("yes");
				}else{
					selectedCount--;
					brandsAdapter.brands.get(position).setIs_followed("no");
				}
				if (selectedCount>0) {
					mRightText.setText("Next");
				} else {
					mRightText.setText("Skip");
				}
				brandsAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				checkImageView.setVisibility(View.VISIBLE);
				checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					BrandListRes response = (BrandListRes) object;
					AlertUtils.showToast(context, response.getMessage());
				} else {
					AlertUtils.showToast(context, R.string.invalid_response);
				}
			}
		});
		listApi.deleteBrand(brand_id);
		listApi.execute();
	}
	
	private void goToNextScreen() {
		startActivity(KikrTutorialActivity.class);
		finish();
	}

	@Override
	public void handleOnSuccess(Object object) {
		if(mProgressBarDialog.isShowing())
			mProgressBarDialog.dismiss();
		else
			hideFutter();
		hideDataNotFound();
		Syso.info("In handleOnSuccess>>" + object);
		isLoading=!isLoading;
			BrandListRes brandListRes = (BrandListRes) object;
			List<BrandList> brandLists = brandListRes.getData();
			if(brandLists.size()<10){
				isLoading=true;
			}
			if(brandLists.size()==0&&isFirstTime){
				showDataNotFound();
			}else if (brandLists.size() > 0 && isFirstTime) {
				brandsAdapter = new FollowBrandsAdapter(context, brandLists);
				mBrandsListView.setAdapter(brandsAdapter);
			} else{
				brandsAdapter.setData(brandLists);
				brandsAdapter.notifyDataSetChanged();
			}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		if(mProgressBarDialog.isShowing())
			mProgressBarDialog.dismiss();
		else
			hideFutter();
		isLoading = !isLoading;
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			BrandListRes response = (BrandListRes) object;
			AlertUtils.showToast(context, response.getMessage());
		} else {
			AlertUtils.showToast(context, R.string.invalid_response);
		}
	}
//	@Override
//	public void onBackPressed() {}
}

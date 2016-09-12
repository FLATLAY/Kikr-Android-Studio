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
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.adapter.FollowCategoryAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants.Screen;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.CategoryListApi;
import com.kikrlib.bean.Category;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CategoryRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FollowCategoriesActivity extends BaseActivity implements OnItemClickListener, OnClickListener, ServiceCallback{
	private GridView gridView;
	private FollowCategoryAdapter categoryAdapter;
	private TextView mRightText;
	private ProgressBarDialog mProgressBarDialog;
	private TextView mDataNotFoundTextView;
	private int pagenum = 0;
	private boolean isLoading=false;
	private boolean isFirstTime = true;
	private int selectedCount=0;
	private TextView followText;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		UserPreference.getInstance().setCurrentScreen(Screen.CategoryScreen);
		setContentView(R.layout.activity_follow_categories);
		if(checkInternet())
			getCategoryList();
		else
			showReloadOption();
		updateScreen(Screen.CategoryScreen);
	}

	@Override
	public void initLayout() {
		gridView=(GridView) findViewById(R.id.categoryGridView);
		followText=(TextView) findViewById(R.id.followText);
		mDataNotFoundTextView=(TextView) findViewById(R.id.dataNotFoundTextView);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
	}

	@Override
	public void setupData() {
		gridView.setOnScrollListener(new OnScrollListener() {
			   @Override
			   public void onScrollStateChanged(AbsListView view, 
			     int scrollState) {
			    // Do nothing
			   }

			   @Override
			   public void onScroll(AbsListView view, int firstVisibleItem, 
			     int visibleItemCount, int totalItemCount) {
				   System.out.println("123456 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
			    if(!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
			    	if(checkInternet()){
				    	pagenum++;
				    	isFirstTime = false;
						getCategoryList();
			    	}
			    }
			   }
			  });
		
	}

	@Override
	public void headerView() {
		setBackHeader();
		mRightText = getRightText();
		mRightText.setText("Next");
		mRightText.setVisibility(View.VISIBLE);
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
		gridView.setOnItemClickListener(this);
	}
	
	private void showReloadOption() {
		showDataNotFound();
		TextView textView=getDataNotFound();
		if(textView!=null){
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternet())
						getCategoryList();
				}
			});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
		if(checkInternet()){
			if(categoryAdapter.catList.get(position).getIs_followed().equalsIgnoreCase("no"))
				addCategory(categoryAdapter.getItem(position).getId(),v,position);
			else
				deleteCategory(categoryAdapter.getItem(position).getId(),v,position);
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

	private void getCategoryList() {
		isLoading = !isLoading;
		mProgressBarDialog = new ProgressBarDialog(context);
		if(pagenum>0)
			showFooter();
		else
			mProgressBarDialog.show();
		
		final CategoryListApi listApi = new CategoryListApi(this);
		listApi.getCategoryList(Integer.toString(pagenum));
		listApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isLoading = !isLoading;
				listApi.cancel();
			}
		});
	}
	
	
	
	
	public void addCategory(String catList,final View v, final int position) {
		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				if(categoryAdapter.catList.get(position).getIs_followed().equalsIgnoreCase("no")){
					selectedCount++;
					followText.setText("Follow some categories " + selectedCount + "/3");
					if(selectedCount == 1)
						progressBar.setProgress(33);
					else if(selectedCount == 2)
						progressBar.setProgress(66);
					else if(selectedCount == 3)
						progressBar.setProgress(100);
					categoryAdapter.catList.get(position).setIs_followed("yes");
				}else{
					selectedCount--;
					followText.setText("Follow some categories " + selectedCount + "/3");
					if(selectedCount == 0)
						progressBar.setProgress(0);
					else if(selectedCount == 1)
						progressBar.setProgress(33);
					else if(selectedCount == 2)
						progressBar.setProgress(66);
					else if(selectedCount == 3)
						progressBar.setProgress(100);
					categoryAdapter.catList.get(position).setIs_followed("no");
				}
				if (selectedCount>=3) {
					mRightText.setTextColor(getResources().getColor(R.color.btn_green));
					mRightText.setClickable(true);
				} else {
					mRightText.setTextColor(getResources().getColor(R.color.btn_gray));
					mRightText.setClickable(false);
				}
				categoryAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				if (object != null) {
					CategoryRes response = (CategoryRes) object;
					AlertUtils.showToast(context, response.getMessage());
				} else {
					AlertUtils.showToast(context, R.string.invalid_response);
				}
			}
		});
		listApi.addCategory(UserPreference.getInstance().getUserID(), catList);
		listApi.execute();
	}

	public void deleteCategory(String category_id,final View v, final int position) {
		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				if(categoryAdapter.catList.get(position).getIs_followed().equalsIgnoreCase("no")){
					selectedCount++;
					followText.setText("Follow some categories " + selectedCount + "/3");
					if(selectedCount == 1)
						progressBar.setProgress(33);
					else if(selectedCount == 2)
						progressBar.setProgress(66);
					else if(selectedCount == 3)
						progressBar.setProgress(100);
					categoryAdapter.catList.get(position).setIs_followed("yes");
				}else{
					selectedCount--;
					followText.setText("Follow some categories " + selectedCount + "/3");
					if(selectedCount == 0)
						progressBar.setProgress(0);
					else if(selectedCount == 1)
						progressBar.setProgress(33);
					else if(selectedCount == 2)
						progressBar.setProgress(66);
					else if(selectedCount == 3)
						progressBar.setProgress(100);
					categoryAdapter.catList.get(position).setIs_followed("no");
				}
				if (selectedCount>=3) {
					mRightText.setTextColor(getResources().getColor(R.color.btn_green));
					mRightText.setClickable(true);
				} else {
					mRightText.setTextColor(getResources().getColor(R.color.btn_gray));
					mRightText.setClickable(false);
				}
				categoryAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				if (object != null) {
					CategoryRes response = (CategoryRes) object;
					AlertUtils.showToast(context, response.getMessage());
				} else {
					AlertUtils.showToast(context, R.string.invalid_response);
				}
			}
		});
		listApi.deleteCategory(UserPreference.getInstance().getUserID(),category_id);
		listApi.execute();
	}
	
	@Override
	public void handleOnSuccess(Object object) {
		if(mProgressBarDialog.isShowing())
			mProgressBarDialog.dismiss();
		else
			hideFutter();
		hideDataNotFound();
		isLoading = !isLoading;
		Syso.info("In handleOnSuccess>>" + object);
			CategoryRes categoryRes=(CategoryRes) object;
			List<Category> categories=categoryRes.getData();
			if (categories.size()<10) {
				isLoading = true;
			}
			if(categories.size()==0&&isFirstTime){
				mDataNotFoundTextView.setVisibility(View.VISIBLE);
			}else if(categories.size()>0 && isFirstTime){
//				followText.setVisibility(View.VISIBLE);
				mDataNotFoundTextView.setVisibility(View.GONE);
				categoryAdapter=new FollowCategoryAdapter(context,categories);
				gridView.setAdapter(categoryAdapter);

				for(int i = 0; i < categoryAdapter.getCount(); i++) {
					if(categoryAdapter.getItem(i).getIs_followed().equalsIgnoreCase("yes"))
						selectedCount++;
				}
				
				followText.setText("Follow some categories " + selectedCount + "/3");
				
				if(selectedCount == 0)
					progressBar.setProgress(0);
				else if(selectedCount == 1)
					progressBar.setProgress(33);
				else if(selectedCount == 2)
					progressBar.setProgress(66);
				else if(selectedCount == 3)
					progressBar.setProgress(100);
				
			} else {
				categoryAdapter.setData(categories);
				categoryAdapter.notifyDataSetChanged();
			}
	}

	private void goToNextScreen() {
		startActivity(KikrTutorialActivity.class);
		finish();
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
			CategoryRes response = (CategoryRes) object;
			AlertUtils.showToast(context, response.getMessage());
		} else {
			AlertUtils.showToast(context, R.string.invalid_response);
		}
	}

//	@Override
//	public void onBackPressed() {}
}

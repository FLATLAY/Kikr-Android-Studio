package com.flatlay.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.adapter.FollowCategoryNewAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.api.CategoryListApi;
import com.kikrlib.bean.Category;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CategoryRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FollowCategoriesNewActivity extends BaseActivity implements  OnClickListener, ServiceCallback{
	private ListView listView;
	private FollowCategoryNewAdapter categoryAdapter;
	public static TextView mRightText;
	private ProgressBarDialog mProgressBarDialog;
	private TextView mDataNotFoundTextView;
	private int pagenum = 0;
	private boolean isLoading=false;
	private boolean isFirstTime = true;
	public static int selectedCount = 0;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		UserPreference.getInstance().setCurrentScreen(Screen.CategoryScreen);
		setContentView(R.layout.activity_follow_categories_new);
		if(checkInternet())
			getCategoryList();
		else
			showReloadOption();
		updateScreen(Screen.CategoryScreen);
		selectedCount = 0;
	}

	@Override
	public void initLayout() {
		listView=(ListView) findViewById(R.id.categoryGridView);
		mDataNotFoundTextView=(TextView) findViewById(R.id.dataNotFoundTextView);
	}

	@Override
	public void setupData() {
		listView.setOnScrollListener(new OnScrollListener() {
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
		//mRightText.setClickable(false);
		mRightText.setTextColor(getResources().getColor(R.color.btn_gray));
		getLeftTextView().setVisibility(View.GONE);
	}


	@Override
	public void setUpTextType() {
		
	}

	@Override
	public void setClickListener() {
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
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.menuRightText:
			if(selectedCount > 0)
				goToNextScreen();
			else
				AlertUtils.showToast(context, "Follow at least 1 category.");
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
	
	public static void checkFollowAtLeastOne() {
		if (selectedCount > 0) {
			mRightText.setTextColor(Color.parseColor("#5bbaad"));
		//	mRightText.setClickable(true);
		} else {
			mRightText.setTextColor(Color.parseColor("#ABABAB"));
		//	mRightText.setClickable(false);
		}
		
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
				mDataNotFoundTextView.setVisibility(View.GONE);
				
				List<Category> reorderedCategories = new ArrayList<Category>();
				List<int[]> imgList = new ArrayList<int[]>();
				int[] womensImage = {
			            R.drawable.womens1,
			            R.drawable.womens2,
			            R.drawable.womens3,
			            R.drawable.womens4
			    };
				
				int[] mensImage = {
			            R.drawable.mens1,
			            R.drawable.mens2,
			            R.drawable.mens3,
			            R.drawable.mens4
			    };
				
				int[] unisexImage = {
			            R.drawable.unisex1,
			            R.drawable.unisex2,
			            R.drawable.unisex3,
			            R.drawable.unisex4
			    };
				
				int[] accessoryImage = {
			            R.drawable.accessories1,
			            R.drawable.accessories2,
			            R.drawable.accessories3,
			            R.drawable.accessories4
			    };
				
				int[] healthImage = {
			            R.drawable.health1,
			            R.drawable.health2,
			            R.drawable.health3,
			            R.drawable.health4
			    };
				
				int[] sportsImage = {
			            R.drawable.sports1,
			            R.drawable.sports2,
			            R.drawable.sports3,
			            R.drawable.sports4
			    };
				
				int[] kidsImage = {
			            R.drawable.kids1,
			            R.drawable.kids2,
			            R.drawable.kids3,
			            R.drawable.kids4
			    };
				
				int[] electronicsImage = {
			            R.drawable.electronics1,
			            R.drawable.electronics2,
			            R.drawable.electronics3,
			            R.drawable.electronics4
			    };
				
				int[] entertainmentImage = {
			            R.drawable.entertainment1,
			            R.drawable.entertainment2,
			            R.drawable.entertainment3,
			            R.drawable.entertainment4
			    };
				
				int[] homeImage = {
			            R.drawable.home1,
			            R.drawable.home2,
			            R.drawable.home3,
			            R.drawable.home4
			    };

				reorderedCategories.add(categories.get(9));
				reorderedCategories.add(categories.get(6));
				reorderedCategories.add(categories.get(8));
				reorderedCategories.add(categories.get(0));
				reorderedCategories.add(categories.get(3));
				reorderedCategories.add(categories.get(7));
				reorderedCategories.add(categories.get(5));
				reorderedCategories.add(categories.get(1));
				reorderedCategories.add(categories.get(2));
				reorderedCategories.add(categories.get(4));
				
				imgList.add(womensImage);
				imgList.add(mensImage);
				imgList.add(unisexImage);
				imgList.add(accessoryImage);
				imgList.add(healthImage);
				imgList.add(sportsImage);
				imgList.add(kidsImage);
				imgList.add(electronicsImage);
				imgList.add(entertainmentImage);
				imgList.add(homeImage);
				
				categoryAdapter=new FollowCategoryNewAdapter(context,reorderedCategories, imgList);
				listView.setAdapter(categoryAdapter);

				for(int i = 0; i < categoryAdapter.getCount(); i++) {
					if(categoryAdapter.getItem(i).getIs_followed().equalsIgnoreCase("yes"))
						selectedCount++;
				}
				
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

}

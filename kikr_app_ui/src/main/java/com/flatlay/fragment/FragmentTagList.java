package com.flatlay.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.InspirationBrandListAdapter;
import com.flatlay.adapter.InspirationPeopleListAdapter;
import com.flatlay.adapter.InspirationStoreListAdapter;
import com.flatlay.adapter.InterestCategoryListAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.api.CategoryListApi;
import com.kikrlib.api.FollowUserApi;
import com.kikrlib.api.InterestSectionApi;
import com.kikrlib.bean.InterestSection;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CategoryRes;
import com.kikrlib.service.res.FollowUserRes;
import com.kikrlib.service.res.InterestSectionRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentTagList extends BaseFragment implements OnClickListener, ServiceCallback,OnItemClickListener,OnKeyListener {

	private View mainView;
	private ListView interestSectionList;
	private ProgressBarDialog mProgressBarDialog;
	private List<InterestSection> interestList;
	private int pagenum = 0;
//	private Button interest_store_button, interest_brand_button,interest_people_button;
//	private ImageView interest_category_button;
	private InspirationBrandListAdapter interestBrandListAdapter;
	private InterestCategoryListAdapter interestCategoryListAdapter;
	private InspirationStoreListAdapter interestStoreListAdapter;
	private FragmentTagList fragmentInterestSection;
	private InspirationPeopleListAdapter interestPeopleListAdapter;
//	private View peopleHeaderView;
//	private LinearLayout trendingGalsLinearLayout,trendingGuysLinearLayout;
	private boolean isShown = false;
	private EditText searchYourItemEditText;
	private String isSelected = "store";
	private boolean isLoading=false;
//	private boolean isGuys=false;
//	private boolean isGals=false;
	private boolean isFirstTime = true;
	private boolean isSearchActive = false;
//	private GridView categoryGridView;
	
	public static String TAB_PEOPLE ="people";
	public static String TAB_BRAND ="brand";
	public static String TAB_STORE  ="store";
	private List<InterestSection> selectedItems;
	private List<InterestSection> localSelectedItems=new ArrayList<InterestSection>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_tag_list, null);
		fragmentInterestSection = this;
//		peopleHeaderView = View.inflate(mContext, R.layout.interest_header, null);
		return mainView;
	}
	
	public FragmentTagList(String tab_option, List<InterestSection> selectedItems) {
		isSelected=tab_option;
		this.selectedItems=selectedItems;
		localSelectedItems.addAll(selectedItems);
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		interestSectionList = (ListView) mainView.findViewById(R.id.interestSectionList);
//		interest_store_button = (Button) mainView.findViewById(R.id.interest_store_button);
//		interest_brand_button = (Button) mainView.findViewById(R.id.interest_brand_button);
//		interest_people_button = (Button) mainView.findViewById(R.id.interest_people_button);
//		interest_category_button = (ImageView) mainView.findViewById(R.id.interest_category_imageview);
//		trendingGalsLinearLayout = (LinearLayout) peopleHeaderView.findViewById(R.id.trendingGalsLinearLayout);
//		trendingGuysLinearLayout = (LinearLayout) peopleHeaderView.findViewById(R.id.trendingGuysLinearLayout);
		searchYourItemEditText = (EditText) mainView.findViewById(R.id.searchYourItemEditText);
//		categoryGridView = (GridView) mainView.findViewById(R.id.categoryGridView);
	}
	
	public void done(){
		selectedItems.clear();
		selectedItems.addAll(localSelectedItems);
//		selectedItems=localSelectedItems;
		Syso.info("In done>>>"+localSelectedItems.size()+selectedItems.size());
	}
	
	@Override
	public void setData(Bundle bundle) {
		if(checkInternet()){
			loadData();
		}
		else
			showReloadOption();
		
		interestSectionList.setOnScrollListener(new OnScrollListener() {
			   @Override
			   public void onScrollStateChanged(AbsListView view, 
			     int scrollState) {
			    // Do nothing
			   }

			   @Override
			   public void onScroll(AbsListView view, int firstVisibleItem, 
			     int visibleItemCount, int totalItemCount) {
			    if(!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
			    	if(checkInternet2()){
				    	pagenum++;
				    	isFirstTime = false;
				    	loadData();
			    	}else {
						showReloadFotter();
					}
			    }
			   }
			  });
	}

	@Override
	public void refreshData(Bundle bundle) {

	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			CommonUtility.hideSoftKeyboard(mContext);
			if(checkInternet()){
				interestSectionList.setAdapter(null);
//				if(interestSectionList.getHeaderViewsCount()!=0)
//					interestSectionList.removeHeaderView(peopleHeaderView);
				pagenum = 0;
				isSearchActive = true;
				isFirstTime = true;
				isLoading=false;
				search();
				return true;
			}
		}
		return false;
	}

	@Override
	public void setClickListener() {
//		interest_store_button.setOnClickListener(this);
//		interest_brand_button.setOnClickListener(this);
//		interest_people_button.setOnClickListener(this);
//		interest_category_button.setOnClickListener(this);
//		trendingGalsLinearLayout.setOnClickListener(this);
//		trendingGuysLinearLayout.setOnClickListener(this);
		searchYourItemEditText.setOnKeyListener(this);
	}
	
	@Override
	public void onClick(View v) {
		hideFotter();
		switch (v.getId()) {
		case R.id.interest_store_button:
			isFirstTime = true;
			pagenum = 0;
			isSelected ="store";
			isLoading=false;
			isSearchActive = false;
			searchYourItemEditText.setText("");
			searchYourItemEditText.setFocusableInTouchMode(true);
			searchYourItemEditText.setFocusable(true);
//			if(interestSectionList.getHeaderViewsCount()!=0)
//				interestSectionList.removeHeaderView(peopleHeaderView);
			interestSectionList.setAdapter(null);
//			interest_store_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
//			interest_brand_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
//			interest_people_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			if(checkInternet())
			{
				getStoreList();
//				categoryGridView.setVisibility(View.GONE);
				interestSectionList.setVisibility(View.VISIBLE);
			}
			else
				showReloadOption();
			break;
		case R.id.interest_brand_button:
			pagenum = 0;
			isFirstTime = true;
			isSelected ="brand";
			isLoading=false;
			isSearchActive = false;
			searchYourItemEditText.setText("");
			interestSectionList.setAdapter(null);
			searchYourItemEditText.setFocusableInTouchMode(true);
			searchYourItemEditText.setFocusable(true);
//			if(interestSectionList.getHeaderViewsCount()!=0)
//				interestSectionList.removeHeaderView(peopleHeaderView);
//			interest_store_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
//			interest_brand_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
//			interest_people_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			if(checkInternet())
			{
//				categoryGridView.setVisibility(View.GONE);
				interestSectionList.setVisibility(View.VISIBLE);
				getBrandList();
			}
			else
				showReloadOption();
			break;
		case R.id.interest_people_button:
			isFirstTime = true;
//			isGuys = false;
//			isGals = false;
			pagenum = 0;
			isLoading=false;
			isSearchActive = false;
			isSelected ="people";
			searchYourItemEditText.setText("");
			searchYourItemEditText.setFocusableInTouchMode(true);
			searchYourItemEditText.setFocusable(true);
			interestSectionList.setAdapter(null);
//			if(interestSectionList.getHeaderViewsCount()==0)
//				interestSectionList.addHeaderView(peopleHeaderView);
//			interest_store_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
//			interest_brand_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
//			interest_people_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
//			trendingGuysLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
//			trendingGalsLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
			if(checkInternet()){
//				categoryGridView.setVisibility(View.GONE);
				interestSectionList.setVisibility(View.VISIBLE);
				getUserList("all");
			}
			else
				showReloadOption();
			break;
		case R.id.interest_category_imageview:
			pagenum = 0;
			isFirstTime = true;
			isLoading=false;
			isSearchActive = false;
			searchYourItemEditText.setText("");
//			if(interestSectionList.getHeaderViewsCount()!=0)
//				interestSectionList.removeHeaderView(peopleHeaderView);
			if (!isShown) {
				isShown = true;
				interestSectionList.setAdapter(null);
				searchYourItemEditText.setFocusableInTouchMode(false);
				searchYourItemEditText.setFocusable(false);
				isSelected ="category";
//				interest_store_button.setVisibility(View.GONE);
//				interest_brand_button.setVisibility(View.GONE);
//				interest_people_button.setVisibility(View.GONE);
				if(checkInternet()){
//					categoryGridView.setVisibility(View.VISIBLE);
					interestSectionList.setVisibility(View.GONE);
					getCategoryList();
				}
				else
					showReloadOption();
			} else{
				isShown = false;
				interestSectionList.setAdapter(null);
				searchYourItemEditText.setFocusableInTouchMode(true);
				searchYourItemEditText.setFocusable(true);
				isSelected ="store";
//				interest_store_button.setVisibility(View.VISIBLE);
//				interest_brand_button.setVisibility(View.VISIBLE);
//				interest_people_button.setVisibility(View.VISIBLE);
//				interest_store_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
//				interest_brand_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
//				interest_people_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
				if(checkInternet()){
//					categoryGridView.setVisibility(View.GONE);
					interestSectionList.setVisibility(View.VISIBLE);
					getStoreList();
				}
				else
					showReloadOption();
			}
			break;
//		case R.id.trendingGalsLinearLayout:
//			isFirstTime = true;
//			pagenum = 0;
////			isGuys = false;
////			isGals = true;
//			isLoading=false;
//			isSearchActive = false;
//			searchYourItemEditText.setText("");
//			searchYourItemEditText.setFocusableInTouchMode(true);
//			searchYourItemEditText.setFocusable(true);
//			interestSectionList.setAdapter(null);
////			trendingGalsLinearLayout.setBackgroundColor(getResources().getColor(R.color.app_background));
////			trendingGuysLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
//			if(checkInternet())
//				getUserList("female");
//			else
//				showReloadOption();
//			break;
//		case R.id.trendingGuysLinearLayout:
//			isFirstTime = true;
//			pagenum = 0;
////			isGuys = true;
////			isGals = false;
//			isLoading=false;
//			isSearchActive = false;
//			searchYourItemEditText.setText("");
//			searchYourItemEditText.setFocusableInTouchMode(true);
//			searchYourItemEditText.setFocusable(true);
//			interestSectionList.setAdapter(null);
////			trendingGalsLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
////			trendingGuysLinearLayout.setBackgroundColor(getResources().getColor(R.color.app_background));
//			if(checkInternet())
//				getUserList("male");
//			else
//				showReloadOption();
//			break;
		default:
			break;
		}
	}


	private void getBrandList() {
		isLoading=!isLoading;
		mProgressBarDialog = new ProgressBarDialog(mContext);
		if(pagenum>0)
			showFotter();
		else
			mProgressBarDialog.show();
		final InterestSectionApi interestSectionApi = new InterestSectionApi(this);
		interestSectionApi.getBrandList(Integer.toString(pagenum));
		interestSectionApi.execute();
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isLoading=!isLoading;
				interestSectionApi.cancel();
			}
		});
	}
	
	private void getStoreList() {
		isLoading=!isLoading;
		mProgressBarDialog = new ProgressBarDialog(mContext);
		if(pagenum>0)
			showFotter();
		else
			mProgressBarDialog.show();
		
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				hideDataNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				isLoading=!isLoading;
				InterestSectionRes interestSectionRes = (InterestSectionRes) object;
				interestList = interestSectionRes.getData();
				setSelectedItems();
				setSelectedItems();
				if(interestList.size()<10){
					isLoading=true;
				}
				if (interestList.size() == 0 && isFirstTime)
					showDataNotFound();
				else if (interestList.size() > 0 && isFirstTime) {
					hideDataNotFound();
					interestStoreListAdapter = new InspirationStoreListAdapter(mContext,interestList,fragmentInterestSection);
					interestSectionList.setAdapter(interestStoreListAdapter);
				} else{
					interestStoreListAdapter.setData(interestList);
					interestStoreListAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				Syso.info("In handleOnFailure>>" + object);
				isLoading=!isLoading;
				if (object != null) {
					InterestSectionRes response = (InterestSectionRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
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
	
	private void getCategoryList() {
		isLoading=!isLoading;
		mProgressBarDialog = new ProgressBarDialog(mContext);
		if(pagenum>0)
			showFotter();
		else
			mProgressBarDialog.show();
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				hideDataNotFound();
				isLoading=!isLoading;
				Syso.info("In handleOnSuccess>>" + object);
				InterestSectionRes interestSectionRes = (InterestSectionRes) object;
				interestList = interestSectionRes.getData();
				setSelectedItems();
				setSelectedItems();
				if(interestList.size()<10){
					isLoading=true;
				}
				if (interestList.size() == 0 && isFirstTime)
					showDataNotFound();
				else if (interestList.size() > 0 && isFirstTime) {
					hideDataNotFound();
//					interestCategoryListAdapter = new InterestCategoryListAdapter(mContext,interestList,fragmentInterestSection);
//					categoryGridView.setAdapter(interestCategoryListAdapter);
				} else{
					interestCategoryListAdapter.setData(interestList);
					interestCategoryListAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				Syso.info("In handleOnFailure>>" + object);
				isLoading=!isLoading;
				if (object != null) {
					CategoryRes response = (CategoryRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		interestSectionApi.getCategoryList(Integer.toString(pagenum));
		interestSectionApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isLoading=!isLoading;
				interestSectionApi.cancel();
			}
		});
	}

	@Override
	public void handleOnSuccess(Object object) {
		if(mProgressBarDialog.isShowing())
			mProgressBarDialog.dismiss();
		else
			hideFotter();
		hideDataNotFound();
		isLoading=!isLoading;
		Syso.info("In handleOnSuccess>>" + object);
		InterestSectionRes interestSectionApi = (InterestSectionRes) object;
		interestList = interestSectionApi.getData();
		setSelectedItems();
		if(interestList.size()<10){
			isLoading=true;
		}
		if (interestList.size() == 0 && isFirstTime)
			showDataNotFound();
		else if (interestList.size() > 0 && isFirstTime) {
			hideDataNotFound();
			interestBrandListAdapter = new InspirationBrandListAdapter(mContext,interestList,fragmentInterestSection);
			interestSectionList.setAdapter(interestBrandListAdapter);
		} else {
			interestBrandListAdapter.setData(interestList);
			interestBrandListAdapter.notifyDataSetChanged();
		}
	}

	private void setSelectedItems() {
		// TODO Auto-generated method stub
		for(int i=0;i<localSelectedItems.size();i++){
			for(int j=0;j<interestList.size();j++){
				if(localSelectedItems.get(i).getId().equals(interestList.get(j).getId())){
					interestList.get(j).setIs_selected("yes");
				}
			}
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		if(mProgressBarDialog.isShowing())
			mProgressBarDialog.dismiss();
		else
			hideFotter();
		Syso.info("In handleOnFailure>>" + object);
		isLoading=!isLoading;
		if (object != null) {
			InterestSectionRes response = (InterestSectionRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {}
	
	public void addBrand(InterestSection interestSection) {
		if (localSelectedItems.size()>0) {
			localSelectedItems.remove(0);
			localSelectedItems.add(interestSection);
		}else
			localSelectedItems.add(interestSection);
		
//		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
//		final BrandListApi listApi = new BrandListApi(new ServiceCallback() {
//			
//			@Override
//			public void handleOnSuccess(Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				Syso.info("In handleOnSuccess>>" + object);
//			}
//			
//			@Override
//			public void handleOnFailure(ServiceException exception, Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				Syso.info("In handleOnFailure>>" + object);
//				if (object != null) {
//					BrandListRes response = (BrandListRes) object;
//					AlertUtils.showToast(mContext, response.getMessage());
//				} else {
//					AlertUtils.showToast(mContext, R.string.invalid_response);
//				}
//			}
//		});
//		listApi.addBrands(brand_id);
//		listApi.execute();
	}
	
	public void deleteBrand(InterestSection interestSection) {
		removeSelectedItem(interestSection.getId());
//		selectedItems.remove(interestSection);
//		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
//		final BrandListApi listApi = new BrandListApi(new ServiceCallback() {
//			
//			@Override
//			public void handleOnSuccess(Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				Syso.info("In handleOnSuccess>>" + object);
//			}
//			
//			@Override
//			public void handleOnFailure(ServiceException exception, Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				Syso.info("In handleOnFailure>>" + object);
//				if (object != null) {
//					BrandListRes response = (BrandListRes) object;
//					AlertUtils.showToast(mContext, response.getMessage());
//				} else {
//					AlertUtils.showToast(mContext, R.string.invalid_response);
//				}
//			}
//		});
//		listApi.deleteBrand(brand_id);
//		listApi.execute();
	}
	
	public void addCategory(String catList,final View v) {
		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				if (object != null) {
					CategoryRes response = (CategoryRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		listApi.addCategory(UserPreference.getInstance().getUserID(), catList);
		listApi.execute();
	}

	public void deleteCategory(String category_id,final View v) {
		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				if (object != null) {
					CategoryRes response = (CategoryRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		listApi.deleteCategory(UserPreference.getInstance().getUserID(),category_id);
		listApi.execute();
	}
	
	public void followUser(String id) {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>" + object);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					FollowUserRes response = (FollowUserRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}	
			}
		});
		followUserApi.followUser(UserPreference.getInstance().getUserID(),id);
		followUserApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				followUserApi.cancel();
			}
		});
	}
	
	public void unFollowUser(String id) {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					FollowUserRes response = (FollowUserRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}	
			}
		});
		followUserApi.unFollowUser(UserPreference.getInstance().getUserID(),id);
		followUserApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				followUserApi.cancel();
			}
		});
	}
	
	public void followStore(InterestSection object) {
		localSelectedItems.add(object);
//		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
//		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
//			
//			@Override
//			public void handleOnSuccess(Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//			}
//			
//			@Override
//			public void handleOnFailure(ServiceException exception, Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				Syso.info("In handleOnFailure>>" + object);
//				if (object != null) {
//					InterestSectionRes response = (InterestSectionRes) object;
//					AlertUtils.showToast(mContext, response.getMessage());
//				} else {
//					AlertUtils.showToast(mContext, R.string.invalid_response);
//				}	
//			}
//		});
//		interestSectionApi.followStore(UserPreference.getInstance().getUserID(),id);
//		interestSectionApi.execute();
//		
//		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				interestSectionApi.cancel();
//			}
//		});
	}
	
	public void unFollowStore(String id) {
		removeSelectedItem(id);
//		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
//		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
//			
//			@Override
//			public void handleOnSuccess(Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//			}
//			
//			@Override
//			public void handleOnFailure(ServiceException exception, Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				Syso.info("In handleOnFailure>>" + object);
//				if (object != null) {
//					InterestSectionRes response = (InterestSectionRes) object;
//					AlertUtils.showToast(mContext, response.getMessage());
//				} else {
//					AlertUtils.showToast(mContext, R.string.invalid_response);
//				}	
//			}
//		});
//		interestSectionApi.unFollowStore(UserPreference.getInstance().getUserID(),id);
//		interestSectionApi.execute();
//		
//		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				interestSectionApi.cancel();
//			}
//		});
	}
	
	private void removeSelectedItem(String id) {
		for(int i=0;i<localSelectedItems.size();i++){
			if(localSelectedItems.get(i).getId().equals(id)){
				localSelectedItems.remove(i);
				break;
			}
		}
	}

	public void getUserList(String gender) {
		isLoading=!isLoading;
		mProgressBarDialog = new ProgressBarDialog(mContext);
		if(pagenum>0)
			showFotter();
		else
			mProgressBarDialog.show();
		final InterestSectionApi followUserApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				hideDataNotFound();
				isLoading=!isLoading;
				Syso.info("In handleOnSuccess>>" + object);
				InterestSectionRes followUserRes = (InterestSectionRes) object;
				interestList = followUserRes.getData();
				setSelectedItems();
				if(interestList.size()<10){
					isLoading=true;
				}
				if (interestList.size() == 0&& isFirstTime)
					showDataNotFound();
				else if (interestList.size() > 0 && isFirstTime) {
					hideDataNotFound();
					interestPeopleListAdapter = new InspirationPeopleListAdapter(mContext,interestList,fragmentInterestSection);
					interestSectionList.setAdapter(interestPeopleListAdapter);
				} else {
					interestPeopleListAdapter.setData(interestList);
					interestPeopleListAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				isLoading=!isLoading;
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					InterestSectionRes response = (InterestSectionRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}	
			}
		});
		followUserApi.getAllKikrUserList(UserPreference.getInstance().getUserID(),Integer.toString(pagenum), gender);
		followUserApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isLoading=!isLoading;
				followUserApi.cancel();
			}
		});
	}
	

	private void search() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		if(pagenum>0)
			showFotter();
		else
			mProgressBarDialog.show();
		isLoading=!isLoading;
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				hideDataNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				isLoading=!isLoading;
				InterestSectionRes interestSectionRes = (InterestSectionRes) object;
				if(interestSectionRes.getData().size()<10){
					isLoading=true;
				}
				if (interestSectionRes.getData().size() == 0 && isFirstTime){
					showDataNotFound();
				}else{
					hideDataNotFound();
				if (isSelected.equalsIgnoreCase("store")) {
					if (interestSectionRes.getData().size() > 0 && isFirstTime) {
						interestList = interestSectionRes.getData();
						setSelectedItems();
						interestStoreListAdapter = new InspirationStoreListAdapter(mContext,interestList,fragmentInterestSection);
						interestSectionList.setAdapter(interestStoreListAdapter);
					} else{
						interestList = interestSectionRes.getData();
						setSelectedItems();
						interestStoreListAdapter.setData(interestList);
						interestStoreListAdapter.notifyDataSetChanged();
					}
				} else if (isSelected.equalsIgnoreCase("brand")) {
					if (interestSectionRes.getData().size() > 0 && isFirstTime) {
						interestList = interestSectionRes.getData();
						setSelectedItems();
						interestBrandListAdapter = new InspirationBrandListAdapter(mContext,interestList,fragmentInterestSection);
						interestSectionList.setAdapter(interestBrandListAdapter);
					}else{
						interestList = interestSectionRes.getData();
						setSelectedItems();
						interestBrandListAdapter.setData(interestList);
						interestBrandListAdapter.notifyDataSetChanged();
					}
				} else if (isSelected.equalsIgnoreCase("people")) {
					if (interestSectionRes.getData().size() > 0 && isFirstTime) {
//						if(interestSectionList.getHeaderViewsCount()==0)
//							interestSectionList.addHeaderView(peopleHeaderView);
						interestList = interestSectionRes.getData();
						setSelectedItems();
						interestPeopleListAdapter = new InspirationPeopleListAdapter(mContext,interestList,fragmentInterestSection);
						interestSectionList.setAdapter(interestPeopleListAdapter);
					}else{
						interestList = interestSectionRes.getData();
						setSelectedItems();
						interestPeopleListAdapter.setData(interestList);
						interestPeopleListAdapter.notifyDataSetChanged();
					}
				} else {
					if (interestSectionRes.getData().size() > 0 && isFirstTime) {
						interestList = interestSectionRes.getData();
						setSelectedItems();
//						interestCategoryListAdapter = new InterestCategoryListAdapter(mContext,interestList,fragmentInterestSection);
//						categoryGridView.setAdapter(interestCategoryListAdapter);
					} else{
						interestList = interestSectionRes.getData();
						setSelectedItems();
						interestCategoryListAdapter.setData(interestList);
						interestCategoryListAdapter.notifyDataSetChanged();
					}
				}
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				isLoading=!isLoading;
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					InterestSectionRes response = (InterestSectionRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}	
			}
		});
		if (isSelected.equalsIgnoreCase("store")) {
			interestSectionApi.searchStore(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(),Integer.toString(pagenum));
		} else if (isSelected.equalsIgnoreCase("brand")) {
			interestSectionApi.searchBrand(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(),Integer.toString(pagenum));
		} else if (isSelected.equalsIgnoreCase("people")) {
			interestSectionApi.searchUser(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(),Integer.toString(pagenum));
		} else {
			interestSectionApi.searchCategory(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(),Integer.toString(pagenum));
		}
		interestSectionApi.execute();
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isLoading=!isLoading;
				interestSectionApi.cancel();
			}
		});
	}

	public void loadData(){
		if (isSelected.equalsIgnoreCase("store") && !isSearchActive) {
			getStoreList();
		} else if (isSelected.equalsIgnoreCase("brand") && !isSearchActive) {
			getBrandList();
		}else if (isSelected.equalsIgnoreCase("people") && !isSearchActive) {
			getUserList("all");
		}  else if (isSearchActive) {
			search();
		} 
	}
	
	private void showReloadOption() {
		showDataNotFound();
		TextView textView=getDataNotFound();
		if(textView!=null){
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternet()){
						loadData();
					}
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
					pagenum++;
			    	isFirstTime = false;
			    	loadData();
				}
			}
		});
	}
}

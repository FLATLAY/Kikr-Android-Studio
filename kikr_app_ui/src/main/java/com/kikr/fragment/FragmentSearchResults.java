package com.kikr.fragment;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.adapter.FragmentProductBasedOnTypeAdapter;
import com.kikr.adapter.SearchAdapter;
import com.kikr.ui.HeaderGridView;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.ProductBasedOnBrandApi;
import com.kikrlib.api.SearchApi;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ProductBasedOnBrandRes;
import com.kikrlib.service.res.SearchRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentSearchResults extends BaseFragment implements OnClickListener,OnKeyListener {

	private View mainView;
	private ProgressBarDialog mProgressBarDialog;
	private boolean isLoading=false;
	private boolean isFirstTime = true;
	private int pagenum = 0;
	private SearchAdapter searchAdapter;
	private String searchString;
	private HeaderGridView searchResultList;
	
	private ImageButton imgButtonFilter;
	private RelativeLayout transparentOverlay;
	
	private CheckBox checkMen, checkWomen, checkAccessories, checkOnSale;
	private CheckBox check50, check100, check200 , check500, check750, check750More;
	private TextView applyButton;
	
	private RelativeLayout checkMenLayout, checkWomenLayout, checkAccessoriesLayout, checkOnSaleLayout;
	private RelativeLayout check50Layout, check100Layout, check200Layout, check500Layout, check750Layout, check750MoreLayout;
	private TextView txtMen, txtWomen, txtAccessories, txtOnSale;
	private TextView txtCheck50, txtCheck100, txtCheck200, txtCheck500, txtCheck750, txtCheck750More;
	
	
	private String filterUserID;
	private String filterType;
	private String filterName;
	private String filterCategoryType;
	private String filterPriceMin;
	private String filterPriceMax;
	
	private boolean filterOn = false;
	private List<Product> data = new ArrayList<Product>();

	
	public FragmentSearchResults(String searchString) {
		this.searchString = searchString;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.activity_product_based_on_brand, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		searchResultList = (HeaderGridView) mainView.findViewById(R.id.productBasedOnBrandList);
		imgButtonFilter = (ImageButton) mainView.findViewById(R.id.imgButtonFilter);
		transparentOverlay = (RelativeLayout) mainView.findViewById(R.id.transparentOverlay);
		checkMen = (CheckBox) mainView.findViewById(R.id.checkMen);
		checkWomen = (CheckBox) mainView.findViewById(R.id.checkWomen);
		checkAccessories = (CheckBox) mainView.findViewById(R.id.checkAccessories);
		checkOnSale = (CheckBox) mainView.findViewById(R.id.checkOnSale);
		check50 = (CheckBox) mainView.findViewById(R.id.check50);
		check100 = (CheckBox) mainView.findViewById(R.id.check100);
		check200 = (CheckBox) mainView.findViewById(R.id.check200);
		check500 = (CheckBox) mainView.findViewById(R.id.check500);
		check750 = (CheckBox) mainView.findViewById(R.id.check750);
		check750More = (CheckBox) mainView.findViewById(R.id.check750More);
		applyButton = (TextView) mainView.findViewById(R.id.applyButton);
		
		checkMenLayout = (RelativeLayout) mainView.findViewById(R.id.checkMenLayout);
		txtMen = (TextView) mainView.findViewById(R.id.txtMen);
		checkWomenLayout = (RelativeLayout) mainView.findViewById(R.id.checkWomenLayout);
		txtWomen = (TextView) mainView.findViewById(R.id.txtWomen);
		checkAccessoriesLayout = (RelativeLayout) mainView.findViewById(R.id.checkAccessoriesLayout);
		txtAccessories = (TextView) mainView.findViewById(R.id.txtAccessories);
		checkOnSaleLayout = (RelativeLayout) mainView.findViewById(R.id.checkOnSaleLayout);
		txtOnSale = (TextView) mainView.findViewById(R.id.txtOnSale);
		
		check50Layout = (RelativeLayout) mainView.findViewById(R.id.check50Layout);
		txtCheck50 = (TextView) mainView.findViewById(R.id.txtCheck50);
		check100Layout = (RelativeLayout) mainView.findViewById(R.id.check100Layout);
		txtCheck100 = (TextView) mainView.findViewById(R.id.txtCheck100);
		check200Layout = (RelativeLayout) mainView.findViewById(R.id.check200Layout);
		txtCheck200 = (TextView) mainView.findViewById(R.id.txtCheck200);
		check500Layout = (RelativeLayout) mainView.findViewById(R.id.check500Layout);
		txtCheck500 = (TextView) mainView.findViewById(R.id.txtCheck500);
		check750Layout = (RelativeLayout) mainView.findViewById(R.id.check750Layout);
		txtCheck750 = (TextView) mainView.findViewById(R.id.txtCheck750);
		check750MoreLayout = (RelativeLayout) mainView.findViewById(R.id.check750MoreLayout);
		txtCheck750More = (TextView) mainView.findViewById(R.id.txtCheck750More);
		if(checkInternet()){
			pagenum = 0;
			isFirstTime = true;
			isLoading=false;
			search();
		}
		
		filterUserID = UserPreference.getInstance().getUserID();
		filterType = "category";
		filterName = searchString;
		filterCategoryType = "";
		filterOn = false;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgButtonFilter:
			if(transparentOverlay.getVisibility() == View.VISIBLE)
				transparentOverlay.setVisibility(View.GONE);
			else {
				transparentOverlay.setVisibility(View.VISIBLE);
				transparentOverlay.requestFocus();
			}
			break;
		case R.id.applyButton:
			if(checkOnSale.isChecked())
				if(!(check50.isChecked() || check100.isChecked() || check200.isChecked() || check500.isChecked() ||
						check750.isChecked() || check750More.isChecked())) {
					CommonUtility.showAlert(mContext, "Price range required for On Sale categories!");
					break;
				}
			if(!(checkMen.isChecked() || checkWomen.isChecked() || checkAccessories.isChecked() || checkOnSale.isChecked() || 
					check50.isChecked() || check100.isChecked() || check200.isChecked() || check500.isChecked() ||
					check750.isChecked() || check750More.isChecked())) {
				CommonUtility.showAlert(mContext, "No filters selected.");
					break;
			}
			isFirstTime = true;
			filterOn = true;
			pagenum = 0;
			data.clear();
			transparentOverlay.setVisibility(View.GONE);
			searchAdapter.notifyDataSetChanged();
			getFilteredProductList();
			
			break;
		case R.id.checkMenLayout:
			
			if(!checkMen.isChecked()) {
				txtMen.setTextColor(this.getResources().getColor(R.color.btn_green));
				checkMen.setChecked(true);	
				
				txtWomen.setTextColor(this.getResources().getColor(R.color.white));
				checkWomen.setChecked(false);	
				txtAccessories.setTextColor(this.getResources().getColor(R.color.white));
				checkAccessories.setChecked(false);	
				txtOnSale.setTextColor(this.getResources().getColor(R.color.white));
				checkOnSale.setChecked(false);	
				
				filterCategoryType = "PROSP_MEN_FILTER";
				
			} else {
				txtMen.setTextColor(this.getResources().getColor(R.color.white));
				checkMen.setChecked(false);	
			}
			changeApplyColor();
			
			break;
		case R.id.checkWomenLayout:
			
			if(!checkWomen.isChecked()) {
				txtWomen.setTextColor(this.getResources().getColor(R.color.btn_green));
				checkWomen.setChecked(true);	
				
				txtMen.setTextColor(this.getResources().getColor(R.color.white));
				checkMen.setChecked(false);	
				txtAccessories.setTextColor(this.getResources().getColor(R.color.white));
				checkAccessories.setChecked(false);	
				txtOnSale.setTextColor(this.getResources().getColor(R.color.white));
				checkOnSale.setChecked(false);	
				
				filterCategoryType = "PROSP_WOMEN_FILTER";
			} else {
				txtWomen.setTextColor(this.getResources().getColor(R.color.white));
				checkWomen.setChecked(false);	
			}
			changeApplyColor();
			break;
		case R.id.checkAccessoriesLayout:
			
			if(!checkAccessories.isChecked()) {
				txtAccessories.setTextColor(this.getResources().getColor(R.color.btn_green));
				checkAccessories.setChecked(true);	
				
				txtMen.setTextColor(this.getResources().getColor(R.color.white));
				checkMen.setChecked(false);	
				txtWomen.setTextColor(this.getResources().getColor(R.color.white));
				checkWomen.setChecked(false);	
				txtOnSale.setTextColor(this.getResources().getColor(R.color.white));
				checkOnSale.setChecked(false);	
				
				filterCategoryType = "PROSP_ACCESS_FILTER";
			} else {
				txtAccessories.setTextColor(this.getResources().getColor(R.color.white));
				checkAccessories.setChecked(false);	
			}
			changeApplyColor();
			break;
		case R.id.checkOnSaleLayout:
			
			if(!checkOnSale.isChecked()) {
				txtOnSale.setTextColor(this.getResources().getColor(R.color.btn_green));
				checkOnSale.setChecked(true);	
				
				txtMen.setTextColor(this.getResources().getColor(R.color.white));
				checkMen.setChecked(false);	
				txtWomen.setTextColor(this.getResources().getColor(R.color.white));
				checkWomen.setChecked(false);	
				txtAccessories.setTextColor(this.getResources().getColor(R.color.white));
				checkAccessories.setChecked(false);	
				
				filterCategoryType = "onsale";
			} else {
				txtOnSale.setTextColor(this.getResources().getColor(R.color.white));
				checkOnSale.setChecked(false);	
			}
			changeApplyColor();
			break;
		case R.id.check50Layout:
			
			if(!check50.isChecked()) {
				txtCheck50.setTextColor(this.getResources().getColor(R.color.btn_green));
				check50.setChecked(true);	
				
				txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
				check100.setChecked(false);	
				txtCheck200.setTextColor(this.getResources().getColor(R.color.white));
				check200.setChecked(false);	
				txtCheck500.setTextColor(this.getResources().getColor(R.color.white));
				check500.setChecked(false);	
				txtCheck750.setTextColor(this.getResources().getColor(R.color.white));
				check750.setChecked(false);	
				txtCheck750More.setTextColor(this.getResources().getColor(R.color.white));
				check750More.setChecked(false);
				
				filterPriceMin = "0";
				filterPriceMax = "50";
				
			} else {
				txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
				check50.setChecked(false);	
			}
			changeApplyColor();
			break;
		case R.id.check100Layout:
			
			if(!check100.isChecked()) {
				txtCheck100.setTextColor(this.getResources().getColor(R.color.btn_green));
				check100.setChecked(true);	
				
				txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
				check50.setChecked(false);	
				txtCheck200.setTextColor(this.getResources().getColor(R.color.white));
				check200.setChecked(false);	
				txtCheck500.setTextColor(this.getResources().getColor(R.color.white));
				check500.setChecked(false);	
				txtCheck750.setTextColor(this.getResources().getColor(R.color.white));
				check750.setChecked(false);	
				txtCheck750More.setTextColor(this.getResources().getColor(R.color.white));
				check750More.setChecked(false);	
				
				filterPriceMin = "50";
				filterPriceMax = "100";
				
			} else {
				txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
				check100.setChecked(false);	
			}
			changeApplyColor();
			break;
		case R.id.check200Layout:
			
			if(!check200.isChecked()) {
				txtCheck200.setTextColor(this.getResources().getColor(R.color.btn_green));
				check200.setChecked(true);	
				
				txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
				check50.setChecked(false);	
				txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
				check100.setChecked(false);	
				txtCheck500.setTextColor(this.getResources().getColor(R.color.white));
				check500.setChecked(false);	
				txtCheck750.setTextColor(this.getResources().getColor(R.color.white));
				check750.setChecked(false);	
				txtCheck750More.setTextColor(this.getResources().getColor(R.color.white));
				check750More.setChecked(false);	
				
				filterPriceMin = "100";
				filterPriceMax = "200";
				
			} else {
				txtCheck200.setTextColor(this.getResources().getColor(R.color.white));
				check200.setChecked(false);	
			}
			changeApplyColor();
			break;
		case R.id.check500Layout:
			
			if(!check500.isChecked()) {
				txtCheck500.setTextColor(this.getResources().getColor(R.color.btn_green));
				check500.setChecked(true);	
				
				txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
				check50.setChecked(false);	
				txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
				check100.setChecked(false);	
				txtCheck200.setTextColor(this.getResources().getColor(R.color.white));
				check200.setChecked(false);	
				txtCheck750.setTextColor(this.getResources().getColor(R.color.white));
				check750.setChecked(false);	
				txtCheck750More.setTextColor(this.getResources().getColor(R.color.white));
				check750More.setChecked(false);	
				
				filterPriceMin = "200";
				filterPriceMax = "500";
				
			} else {
				txtCheck500.setTextColor(this.getResources().getColor(R.color.white));
				check500.setChecked(false);	
			}
			changeApplyColor();
			break;
		case R.id.check750Layout:
			
			if(!check750.isChecked()) {
				txtCheck750.setTextColor(this.getResources().getColor(R.color.btn_green));
				check750.setChecked(true);	
				
				txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
				check50.setChecked(false);	
				txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
				check100.setChecked(false);	
				txtCheck200.setTextColor(this.getResources().getColor(R.color.white));
				check200.setChecked(false);	
				txtCheck500.setTextColor(this.getResources().getColor(R.color.white));
				check500.setChecked(false);	
				txtCheck750More.setTextColor(this.getResources().getColor(R.color.white));
				check750More.setChecked(false);	
				
				filterPriceMin = "500";
				filterPriceMax = "750";
				
			} else {
				txtCheck750.setTextColor(this.getResources().getColor(R.color.white));
				check750.setChecked(false);	
			}
			changeApplyColor();
			break;
		case R.id.check750MoreLayout:
			
			if(!check750More.isChecked()) {
				txtCheck750More.setTextColor(this.getResources().getColor(R.color.btn_green));
				check750More.setChecked(true);	
				
				txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
				check50.setChecked(false);	
				txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
				check100.setChecked(false);	
				txtCheck200.setTextColor(this.getResources().getColor(R.color.white));
				check200.setChecked(false);	
				txtCheck500.setTextColor(this.getResources().getColor(R.color.white));
				check500.setChecked(false);	
				txtCheck750.setTextColor(this.getResources().getColor(R.color.white));
				check750.setChecked(false);	
				
				filterPriceMin = "750";
				filterPriceMax = "100000";
				
			} else {
				txtCheck750More.setTextColor(this.getResources().getColor(R.color.white));
				check750More.setChecked(false);	
			}
			
			changeApplyColor();
			break;
		default:
			break;
		}
	}

	@Override
	public void setData(Bundle bundle) {
		
		searchResultList.setOnScrollListener(new OnScrollListener() {
			   @Override
			   public void onScrollStateChanged(AbsListView view, 
			     int scrollState) {
			   }

			   @Override
			   public void onScroll(AbsListView view, int firstVisibleItem, 
			     int visibleItemCount, int totalItemCount) {
//				   System.out.println("123456 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
			    if(!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
					// System.out.println("123456 inside if page"+pagenum+" ,"+isSelected);
					if (checkInternet2()) {
						pagenum++;
						isFirstTime = false;
						if(filterOn)
				    		getFilteredProductList();
						else
							search();
					} else {
						showReloadFotter();
					}
			    }
			   }
			  });
	}
	
	protected void showReloadFotter() {
		TextView textView=getReloadFotter();
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkInternet()){
					pagenum++;
			    	isFirstTime = false;
			    	search();
				}
			}
		});
	}

	@Override
	public void refreshData(Bundle bundle) {
	}

	@Override
	public void setClickListener() {
		searchResultList.setOnKeyListener(this);
		imgButtonFilter.setOnClickListener(this);
		applyButton.setOnClickListener(this);
		checkMenLayout.setOnClickListener(this);
		checkWomenLayout.setOnClickListener(this);
		checkAccessoriesLayout.setOnClickListener(this);
		checkOnSaleLayout.setOnClickListener(this);
		check50Layout.setOnClickListener(this);
		check100Layout.setOnClickListener(this);
		check200Layout.setOnClickListener(this);
		check500Layout.setOnClickListener(this);
		check750Layout.setOnClickListener(this);
		check750MoreLayout.setOnClickListener(this);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			CommonUtility.hideSoftKeyboard(mContext);
			if(checkInternet()){
				pagenum = 0;
				isFirstTime = true;
				isLoading=false;
				search();
				return true;
			}
		}
		return false;
	}
	
	
	private void search() {
		isLoading=!isLoading;
		mProgressBarDialog = new ProgressBarDialog(mContext);
		if(pagenum>0)
			showFotter();
		else
			mProgressBarDialog.show();
		
		final SearchApi searchApi = new SearchApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				hideDataNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				isLoading=!isLoading;
				SearchRes searchRes = (SearchRes) object;
				List<Product> data2 = searchRes.getData();
				if(data2.size()<10){
					isLoading=true;
				}
				if (data2.size() == 0 && isFirstTime)
					showDataNotFound();
				else if (data2.size() > 0 && isFirstTime) {
					hideDataNotFound();
					data = data2;
					searchAdapter = new SearchAdapter(mContext,data);
					searchResultList.setAdapter(searchAdapter);
				} else{
					data.addAll(data2);
					searchAdapter.notifyDataSetChanged();
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
					SearchRes response = (SearchRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		searchApi.getprouctsbycategory(UserPreference.getInstance().getUserID(), searchString, Integer.toString(pagenum));
		searchApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isLoading=!isLoading;
				searchApi.cancel();
			}
		});
	}
	
	private void getFilteredProductList() {
		if (!isFirstTime) {
			showFotter();
		} else {
			mProgressBarDialog = new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
		}
		isLoading = true;
		
		final ProductBasedOnBrandApi productBasedOnBrandApi = new ProductBasedOnBrandApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				filterOn = true;
				if (!isFirstTime) {
					hideFotter();
				} else {
					mProgressBarDialog.dismiss();
				}
				hideDataNotFound();
				isLoading=!isLoading;
				Syso.info("In handleOnSuccess>>" + object);
				ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
				List<Product> productLists = productBasedOnBrandRes.getData();
				
				if(productLists.size() == 0 && pagenum == 0)
					CommonUtility.showAlert(mContext, "Sorry! No Products match that criteria.");
				
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
					searchAdapter = new SearchAdapter(mContext,data);
					searchResultList.setAdapter(searchAdapter);
				}  else{
					searchAdapter.notifyDataSetChanged();
				}
				transparentOverlay.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if (!isFirstTime) {
//					productBasedOnBrandList.removeFooterView(loaderView);
				} else {
					mProgressBarDialog.dismiss();
				}
				isLoading=!isLoading;
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					ProductBasedOnBrandRes response = (ProductBasedOnBrandRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		productBasedOnBrandApi.getProductsBasedOnFilter(filterUserID, filterType , filterName, Integer.toString(pagenum),  filterCategoryType, filterPriceMin, filterPriceMax,filterUserID);
		productBasedOnBrandApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isLoading=!isLoading;
				productBasedOnBrandApi.cancel();
			}
		});		
	}
	
	private void changeApplyColor(){
		if(checkMen.isChecked() || checkWomen.isChecked() || checkAccessories.isChecked() || checkOnSale.isChecked() || 
				check50.isChecked() || check100.isChecked() || check200.isChecked() || check500.isChecked() ||
				check750.isChecked() || check750More.isChecked()) {
			applyButton.setTextColor(this.getResources().getColor(R.color.btn_green));
			applyButton.setClickable(true);
		} else {
			applyButton.setTextColor(this.getResources().getColor(R.color.gray2));
			applyButton.setClickable(false);
		}
	}
	
}
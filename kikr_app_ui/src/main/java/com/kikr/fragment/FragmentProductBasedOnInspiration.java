package com.kikr.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.adapter.FragmentProductBasedOnTypeAdapter;
import com.kikr.ui.HeaderGridView;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.ActivityApi;
import com.kikrlib.api.InspirationSectionApi;
import com.kikrlib.api.ProductBasedOnBrandApi;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.InspirationRes;
import com.kikrlib.service.res.ProductBasedOnBrandRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

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
	RelativeLayout transparentOverlay;
//	private FragmentDiscover mFragmentDiscover;
	private boolean isLoadProductBasedOnType;
	private boolean isShowStar=true;
	private String user_id;
	private String collection_id;
	private View loaderView;
	private FragmentProductBasedOnType productBasedOnBrand;
	ImageButton imgButtonFilter;
	private FragmentProductBasedOnInspiration basedOnInspiration;
	private CheckBox checkMen, checkWomen, checkAccessories, checkOnSale;
	private CheckBox check25, check50, check100, check150, check200, check500, check750, check750More;
	private TextView applyButton;

	private RelativeLayout checkMenLayout, checkWomenLayout, checkAccessoriesLayout, checkOnSaleLayout;
	private RelativeLayout check25Layout, check50Layout, check150Layout, check100Layout, check200Layout, check500Layout, check750Layout, check750MoreLayout;
	private TextView txtMen, txtWomen, txtAccessories, txtOnSale;
	private TextView txtCheck25, txtCheck50, txtCheck100, txtCheck150, txtCheck200, txtCheck500, txtCheck750, txtCheck750More;

	private String filterUserID;
	private String filterType;
	private String filterName;
	private String filterCategoryType;
	private String filterPriceMin;
	private String filterPriceMax;
	private String filterId;
	private boolean filterOn = false;

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
		transparentOverlay= (RelativeLayout) mainView.findViewById(R.id.transparentOverlay);
//		imgButtonFilter= (ImageButton) mainView.findViewById(R.id.imgButtonFilter);
//		imgButtonFilter.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				transparentOverlay.setVisibility(View.VISIBLE);
//				imgButtonFilter.setVisibility(View.GONE);
//
//
//
//			}
//		});
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		productBasedOnBrandList = (HeaderGridView) mainView.findViewById(R.id.productBasedOnBrandList);
		imgButtonFilter = (ImageButton) mainView.findViewById(R.id.imgButtonFilter);
		transparentOverlay = (RelativeLayout) mainView.findViewById(R.id.transparentOverlay);
		checkMen = (CheckBox) mainView.findViewById(R.id.checkMen);
		checkWomen = (CheckBox) mainView.findViewById(R.id.checkWomen);
		checkAccessories = (CheckBox) mainView.findViewById(R.id.checkAccessories);
		checkOnSale = (CheckBox) mainView.findViewById(R.id.checkOnSale);
		check25 = (CheckBox) mainView.findViewById(R.id.check25);
		check150 = (CheckBox) mainView.findViewById(R.id.check150);
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

		check25Layout = (RelativeLayout) mainView.findViewById(R.id.check25Layout);
		txtCheck25 = (TextView) mainView.findViewById(R.id.txtCheck25);
		check50Layout = (RelativeLayout) mainView.findViewById(R.id.check50Layout);
		txtCheck50 = (TextView) mainView.findViewById(R.id.txtCheck50);
		check100Layout = (RelativeLayout) mainView.findViewById(R.id.check100Layout);
		txtCheck100 = (TextView) mainView.findViewById(R.id.txtCheck100);
		check150Layout = (RelativeLayout) mainView.findViewById(R.id.check150Layout);
		txtCheck150 = (TextView) mainView.findViewById(R.id.txtCheck150);
		check200Layout = (RelativeLayout) mainView.findViewById(R.id.check200Layout);
		txtCheck200 = (TextView) mainView.findViewById(R.id.txtCheck200);
		check500Layout = (RelativeLayout) mainView.findViewById(R.id.check500Layout);
		txtCheck500 = (TextView) mainView.findViewById(R.id.txtCheck500);
		check750Layout = (RelativeLayout) mainView.findViewById(R.id.check750Layout);
		txtCheck750 = (TextView) mainView.findViewById(R.id.txtCheck750);
		check750MoreLayout = (RelativeLayout) mainView.findViewById(R.id.check750MoreLayout);
		txtCheck750More = (TextView) mainView.findViewById(R.id.txtCheck750More);


//        header = (ViewGroup) getLayoutInflater(savedInstanceState).inflate(R.layout.header_listview_basedproduct, productBasedOnBrandList,
//                false);
//        productBasedOnBrandList.addHeaderView(header);
		loaderView = View.inflate(mContext, R.layout.footer, null);

//        follow_btn = (Button) header.findViewById(R.id.follow_btn);
//        txtBrand = (TextView) header.findViewById(R.id.txtBrand);
//        imgBrand = (ImageView) header.findViewById(R.id.brandLogoImageView);
//        progressBarProductBasedType = (ProgressBar) header.findViewById(R.id.progressBarProductBasedType);

//        txtBrand.setText(item_name);
//        follow_btn.setVisibility(View.GONE);
//        txtBrand.setVisibility(View.GONE);
		imgButtonFilter.setVisibility(View.VISIBLE);
//        if (!TextUtils.isEmpty(collection_id)) {
//            follow_btn.setVisibility(View.GONE);
//            txtBrand.setVisibility(View.GONE);
//        }
//        if (checkInternet() && !TextUtils.isEmpty(item_id)) {
//            if (!item_type.equals("people"))
//                getStatus();
//            else {
//                follow_btn.setText("Following");
//                follow_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
//            }
//        }
		filterUserID = UserPreference.getInstance().getUserID();
		filterType = item_type.equals("people") ? "user" : item_type;
		filterName = item_name;
		filterCategoryType = "";
		filterId = item_id;
		filterOn = false;
	}

	@Override
	public void refreshData(Bundle bundle) {

	}
	public void setClickListener() {
		// follow_btn.setOnClickListener(this);
		imgButtonFilter.setOnClickListener(this);
		applyButton.setOnClickListener(this);
		checkMenLayout.setOnClickListener(this);
		checkWomenLayout.setOnClickListener(this);
		checkAccessoriesLayout.setOnClickListener(this);
		checkOnSaleLayout.setOnClickListener(this);
		check25Layout.setOnClickListener(this);
		check50Layout.setOnClickListener(this);
		check100Layout.setOnClickListener(this);
		check150Layout.setOnClickListener(this);
		check200Layout.setOnClickListener(this);
		check500Layout.setOnClickListener(this);
		check750Layout.setOnClickListener(this);
		check750MoreLayout.setOnClickListener(this);
	}


	public void onClick(View v) {
		switch (v.getId()) {
//            case R.id.follow_btn:
//                // if user is currently following brand, change text to "Following" and do follow function
//                // else change text to "Follow" and do unfollow function
//                if (item_type.equals("store")) {
//                    changeFollowStoreStatus();
//                } else {
//                    changeFollowBrandStatus();
//                }
//                break;
			case R.id.imgButtonFilter:
				if (transparentOverlay.getVisibility() == View.VISIBLE)
					transparentOverlay.setVisibility(View.GONE);
				else {
					transparentOverlay.setVisibility(View.VISIBLE);
					transparentOverlay.requestFocus();
				}
				break;
			case R.id.applyButton:
//			if(checkOnSale.isChecked())
//				if(!(check50.isChecked() || check100.isChecked() || check200.isChecked() || check500.isChecked() ||
//						check750.isChecked() || check750More.isChecked())) {
//					CommonUtility.showAlert(mContext, "Price range required for On Sale categories!");
//					break;
//				}
				if (!(checkMen.isChecked() || checkWomen.isChecked() || checkAccessories.isChecked() || checkOnSale.isChecked() ||
						check25.isChecked() || check50.isChecked() || check100.isChecked() || check150.isChecked() || check200.isChecked() || check500.isChecked() ||
						check750.isChecked() || check750More.isChecked())) {
					CommonUtility.showAlert(mContext, "No filters selected.");
					break;
				}
				isFirstTime = true;
				filterOn = true;
				pageno = 0;
				data.clear();
				transparentOverlay.setVisibility(View.GONE);
				basedOnTypeAdapter.notifyDataSetChanged();
				getFilteredProductList();

				break;
			case R.id.checkMenLayout:

				if (!checkMen.isChecked()) {
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

				if (!checkWomen.isChecked()) {
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

				if (!checkAccessories.isChecked()) {
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

				if (!checkOnSale.isChecked()) {
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

			case R.id.check25Layout:

				if (!check25.isChecked()) {
					txtCheck25.setTextColor(this.getResources().getColor(R.color.btn_green));
					check25.setChecked(true);

					txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
					check50.setChecked(false);
					txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
					check100.setChecked(false);
					txtCheck150.setTextColor(this.getResources().getColor(R.color.white));
					check150.setChecked(false);
					txtCheck200.setTextColor(this.getResources().getColor(R.color.white));
					check200.setChecked(false);
					txtCheck500.setTextColor(this.getResources().getColor(R.color.white));
					check500.setChecked(false);
					txtCheck750.setTextColor(this.getResources().getColor(R.color.white));
					check750.setChecked(false);
					txtCheck750More.setTextColor(this.getResources().getColor(R.color.white));
					check750More.setChecked(false);

					filterPriceMin = "0";
					filterPriceMax = "25";

				} else {
					txtCheck25.setTextColor(this.getResources().getColor(R.color.white));
					check25.setChecked(false);
				}
				changeApplyColor();
				break;
			case R.id.check50Layout:

				if (!check50.isChecked()) {
					txtCheck50.setTextColor(this.getResources().getColor(R.color.btn_green));
					check50.setChecked(true);

					txtCheck25.setTextColor(this.getResources().getColor(R.color.white));
					check25.setChecked(false);
					txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
					check100.setChecked(false);
					txtCheck150.setTextColor(this.getResources().getColor(R.color.white));
					check150.setChecked(false);
					txtCheck200.setTextColor(this.getResources().getColor(R.color.white));
					check200.setChecked(false);
					txtCheck500.setTextColor(this.getResources().getColor(R.color.white));
					check500.setChecked(false);
					txtCheck750.setTextColor(this.getResources().getColor(R.color.white));
					check750.setChecked(false);
					txtCheck750More.setTextColor(this.getResources().getColor(R.color.white));
					check750More.setChecked(false);

					filterPriceMin = "25";
					filterPriceMax = "50";

				} else {
					txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
					check50.setChecked(false);
				}
				changeApplyColor();
				break;
			case R.id.check100Layout:

				if (!check100.isChecked()) {
					txtCheck100.setTextColor(this.getResources().getColor(R.color.btn_green));
					check100.setChecked(true);

					txtCheck25.setTextColor(this.getResources().getColor(R.color.white));
					check25.setChecked(false);
					txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
					check50.setChecked(false);
					txtCheck150.setTextColor(this.getResources().getColor(R.color.white));
					check150.setChecked(false);
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
			case R.id.check150Layout:

				if (!check150.isChecked()) {
					txtCheck150.setTextColor(this.getResources().getColor(R.color.btn_green));
					check150.setChecked(true);

					txtCheck25.setTextColor(this.getResources().getColor(R.color.white));
					check25.setChecked(false);
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
					txtCheck750More.setTextColor(this.getResources().getColor(R.color.white));
					check750More.setChecked(false);

					filterPriceMin = "100";
					filterPriceMax = "150";

				} else {
					txtCheck150.setTextColor(this.getResources().getColor(R.color.white));
					check150.setChecked(false);
				}
				changeApplyColor();
				break;
			case R.id.check200Layout:

				if (!check200.isChecked()) {
					txtCheck200.setTextColor(this.getResources().getColor(R.color.btn_green));
					check200.setChecked(true);

					txtCheck25.setTextColor(this.getResources().getColor(R.color.white));
					check25.setChecked(false);
					txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
					check50.setChecked(false);
					txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
					check100.setChecked(false);
					txtCheck150.setTextColor(this.getResources().getColor(R.color.white));
					check150.setChecked(false);
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

				if (!check500.isChecked()) {
					txtCheck500.setTextColor(this.getResources().getColor(R.color.btn_green));
					check500.setChecked(true);

					txtCheck25.setTextColor(this.getResources().getColor(R.color.white));
					check25.setChecked(false);
					txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
					check50.setChecked(false);
					txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
					check100.setChecked(false);
					txtCheck150.setTextColor(this.getResources().getColor(R.color.white));
					check150.setChecked(false);
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

				if (!check750.isChecked()) {
					txtCheck750.setTextColor(this.getResources().getColor(R.color.btn_green));
					check750.setChecked(true);

					txtCheck25.setTextColor(this.getResources().getColor(R.color.white));
					check25.setChecked(false);
					txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
					check50.setChecked(false);
					txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
					check100.setChecked(false);
					txtCheck150.setTextColor(this.getResources().getColor(R.color.white));
					check150.setChecked(false);
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

				if (!check750More.isChecked()) {
					txtCheck750More.setTextColor(this.getResources().getColor(R.color.btn_green));
					check750More.setChecked(true);

					txtCheck25.setTextColor(this.getResources().getColor(R.color.white));
					check25.setChecked(false);
					txtCheck50.setTextColor(this.getResources().getColor(R.color.white));
					check50.setChecked(false);
					txtCheck100.setTextColor(this.getResources().getColor(R.color.white));
					check100.setChecked(false);
					txtCheck150.setTextColor(this.getResources().getColor(R.color.white));
					check150.setChecked(false);
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

	private void changeApplyColor() {
		if (checkMen.isChecked() || checkWomen.isChecked() || checkAccessories.isChecked() || checkOnSale.isChecked() ||
				check25.isChecked() || check50.isChecked() || check100.isChecked() || check150.isChecked() || check200.isChecked() || check500.isChecked() ||
				check750.isChecked() || check750More.isChecked()) {
			applyButton.setTextColor(this.getResources().getColor(R.color.btn_green));
			applyButton.setClickable(true);
		} else {
			applyButton.setTextColor(this.getResources().getColor(R.color.gray2));
			applyButton.setClickable(false);
		}
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
				hideProductNotFound();
				isLoading = !isLoading;
				Syso.info("In handleOnSuccess>>" + object);
				ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
				List<Product> productLists = productBasedOnBrandRes.getData();

				if (productLists.size() == 0 && pageno == 0)
					CommonUtility.showAlert(mContext, "Sorry! No Products match that criteria.");

				if (productLists.size() > 0) {
					data.addAll(productLists);
				}
				//no need to load data if data in one page is less than 10
				if (productLists.size() < 10) {
					isLoading = true;
				}
				System.out.println("1234 data size " + data.size());
				if (data.size() == 0 && isFirstTime) {
					showProductNotFound();
				} else if (data.size() > 0 && isFirstTime) {
					basedOnTypeAdapter = new FragmentProductBasedOnTypeAdapter(mContext, productBasedOnBrand, data, isShowStar, collection_id);
					productBasedOnBrandList.setAdapter(basedOnTypeAdapter);
				} else {
					basedOnTypeAdapter.setData(data);
					basedOnTypeAdapter.notifyDataSetChanged();
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
				isLoading = !isLoading;
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					ProductBasedOnBrandRes response = (ProductBasedOnBrandRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		productBasedOnBrandApi.getProductsBasedOnFilter(filterUserID, filterType, filterName, Integer.toString(pageno), filterCategoryType, filterPriceMin, filterPriceMax, filterUserID);
		productBasedOnBrandApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isLoading = !isLoading;
				productBasedOnBrandApi.cancel();
			}
		});
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
						if (filterOn)
							getFilteredProductList();
						else
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


	
//		private void getProductList() {
//			if (!isFirstTime) {
//				showFotter();
//			} else {
//				mProgressBarDialog = new ProgressBarDialog(mContext);
//				mProgressBarDialog.show();
//			}
//			isLoading=!isLoading;
//
//			final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(this);
//			inspirationSectionApi.getProductsByInspiration(UserPreference.getInstance().getUserID(), item_id, Integer.toString(pageno));
//			inspirationSectionApi.execute();
//
//			mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
//				@Override
//				public void onCancel(DialogInterface dialog) {
//					isLoading=!isLoading;
//					inspirationSectionApi.cancel();
//				}
//			});
//		}
private void getProductList() {
	if (!isFirstTime) {
		showFotter();
	} else {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
	}
	isLoading = !isLoading;

	final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(this);

		inspirationSectionApi.getProductsByInspiration(UserPreference.getInstance().getUserID(), item_id, Integer.toString(pageno));
//			getProductsBasedOnFilter
	inspirationSectionApi.execute();

	mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			isLoading = !isLoading;
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
		System.out.println("1234 data size " + data.size());
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
					if (checkInternet())
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
				if (checkInternet()) {
					pageno++;
					isFirstTime = false;
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
	public void showProductNotFound(){
		try{
			LinearLayout layout=(LinearLayout) mainView.findViewById(R.id.itemNotFound);
			layout.setVisibility(View.VISIBLE);
			TextView textView=(TextView) layout.findViewById(R.id.noDataFoundTextView);
			textView.setText("Result not found.");
		}catch(NullPointerException exception){
			exception.printStackTrace();
		}
	}

	public void hideProductNotFound(){
		try{
			LinearLayout layout=(LinearLayout) mainView.findViewById(R.id.itemNotFound);
			layout.setVisibility(View.GONE);
			TextView textView=(TextView) layout.findViewById(R.id.noDataFoundTextView);
			textView.setText("Result not found.");
		}catch(NullPointerException exception){
			exception.printStackTrace();
		}
	}
}

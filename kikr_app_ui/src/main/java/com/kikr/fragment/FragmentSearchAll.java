package com.kikr.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.FragmentProductBasedOnTypeAdapter;
import com.kikr.adapter.SearchAdapter;
import com.kikr.adapter.SearchAllAdapter;
import com.kikr.adapter.SearchAllAdapterSection;
import com.kikr.bubble.ChipBubbleText;
import com.kikr.bubble.ChipBubbleTextWatcher;
import com.kikr.bubble.GeneralFunctions;
import com.kikr.chip.AutoSuggestApi;
import com.kikr.chip.IOnFocusListenable;
import com.kikr.chip.TagsEditText;
import com.kikr.model.Item;
import com.kikr.model.SearchResult;
import com.kikr.model.SectionItem;
import com.kikr.ui.HeaderGridView;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.ActivityApi;
import com.kikrlib.api.BrandListApi;
import com.kikrlib.api.CheckBrandStoreFollowStatusApi;
import com.kikrlib.api.FollowUserApi;
import com.kikrlib.api.InterestSectionApi;
import com.kikrlib.api.ProductBasedOnBrandApi;
import com.kikrlib.api.SearchAllApi;
import com.kikrlib.api.SearchApi;
import com.kikrlib.bean.BrandList;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.SearchStoreBrandUserRes;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.BrandListRes;
import com.kikrlib.service.res.CheckBrandStoreFollowStatusRes;
import com.kikrlib.service.res.FollowUserRes;
import com.kikrlib.service.res.InterestSectionRes;
import com.kikrlib.service.res.ProductBasedOnBrandRes;
import com.kikrlib.service.res.SearchRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by anshumaan on 2/19/2016.
 */
public class FragmentSearchAll extends BaseFragment implements View.OnClickListener, MultiAutoCompleteTextView.OnEditorActionListener, IOnFocusListenable {

    private int pagenum = 0;
    ListView listSearch;
    MultiAutoCompleteTextView multiAutoCompleteTextView;
    SearchAllAdapterSection searchAllAdapter;
    SectionItem sectionItem;
    SearchResult searchResult;
    ArrayList<Item> itemArrayList;
    FragmentSearchAll fragmentSearchAll;
    String searchString;
    ImageButton imgSearchButton;
    boolean isProductFromBubble = false;
    //Products variables
    private View mainView;
    private HeaderGridView searchResultList;
    private ProgressBarDialog mProgressBarDialog;
    private FragmentProductBasedOnTypeAdapter basedOnTypeAdapter;
    private int pageno = 0;
    private boolean isFirstTime = true;
    private boolean isLoading = false;
    public List<Product> data = new ArrayList<Product>();
    private String item_name;
    private String item_id;
    private String item_type = "none";
    //	private FragmentDiscover mFragmentDiscover;
    private boolean isLoadProductBasedOnType;
    private boolean isShowStar = true;
    private String user_id;
    private String collection_id;
    private View loaderView;
    private FragmentProductBasedOnType productBasedOnBrand;
    private Button follow_btn;
    private TextView txtBrand;
    private ViewGroup header;
    private String status = "";
    private ProgressBar progressBarProductBasedType;
    private ImageView imgBrand;
    private ImageButton imgButtonFilter;
    private RelativeLayout transparentOverlay;
    FrameLayout product_layout;

    private CheckBox checkMen, checkWomen, checkAccessories, checkOnSale;
    private CheckBox check25, check50, check100, check150, check200, check500, check750, check750More;
    private TextView applyButton;

    private RelativeLayout checkMenLayout, checkWomenLayout, checkAccessoriesLayout, checkOnSaleLayout;
    private RelativeLayout check25Layout, check50Layout, check150Layout, check100Layout, check200Layout, check500Layout, check750Layout, check750MoreLayout;
    private TextView txtMen, txtWomen, txtAccessories, txtOnSale;
    private TextView txtCheck25, txtCheck50, txtCheck100, txtCheck150, txtCheck200, txtCheck500, txtCheck750, txtCheck750More;
    private ImageView imgSearch;

    private String filterUserID;
    private String filterType;
    private String filterName;
    private String filterCategoryType;
    private String filterPriceMin;
    private String filterPriceMax;
    private String filterId;


    private boolean filterOn = false;
    private boolean isFilterApply = false;
    String completeSearchString = "";
    SearchAdapter searchAdapter;
    ChipBubbleText chipBubbleText;
    // AutoSuggestApi autoSuggestApi;
    Button txtDone;
    LinearLayout layoutDone;

    public FragmentSearchAll(String searchString) {
        this.searchString = searchString;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.search_all_main, null);
        fragmentSearchAll = this;
        pagenum = 0;

        isFirstTime = true;
        isLoading = false;
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        product_layout = (FrameLayout) mainView.findViewById(R.id.product_layout_main);
        listSearch = (ListView) mainView.findViewById(R.id.listView_main);
        multiAutoCompleteTextView = (MultiAutoCompleteTextView) mainView.findViewById(R.id.searchMultiText);
        //imgSearchButton=(ImageButton)mainView.findViewById(R.id.button1);
        initProductUI(savedInstanceState);

    }


    public void initProductUI(Bundle savedInstanceState) {
        showKeypad();
        layoutDone = (LinearLayout) mainView.findViewById(R.id.layout_done);
        txtDone = (Button) mainView.findViewById(R.id.txtDone);
        imgSearch = (ImageView) mainView.findViewById(R.id.imgSearch);
        searchResultList = (HeaderGridView) mainView.findViewById(R.id.productBasedOnBrandList);
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


        header = (ViewGroup) getLayoutInflater(savedInstanceState).inflate(R.layout.header_listview_basedproduct, searchResultList,
                false);
        // productBasedOnBrandList.addHeaderView(header);
        loaderView = View.inflate(mContext, R.layout.footer, null);

        follow_btn = (Button) header.findViewById(R.id.follow_btn);
        txtBrand = (TextView) header.findViewById(R.id.txtBrand);
        imgBrand = (ImageView) header.findViewById(R.id.brandLogoImageView);
        progressBarProductBasedType = (ProgressBar) header.findViewById(R.id.progressBarProductBasedType);

        txtBrand.setText(item_name);
        imgButtonFilter.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(collection_id)) {
            follow_btn.setVisibility(View.GONE);
            txtBrand.setVisibility(View.GONE);
        }
        if (checkInternet() && !TextUtils.isEmpty(item_id)) {
            if (!item_type.equals("people"))
                getStatus();
            else {
                follow_btn.setText("Following");
                follow_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
            }
        }
        filterUserID = UserPreference.getInstance().getUserID();
        // filterType = item_type.equals("people") ? "user" : item_type;
        filterType = "category";
        filterName = item_name;
        filterCategoryType = "";
        filterId = item_id;
        filterOn = false;
    }

    private void getStatus() {
        final CheckBrandStoreFollowStatusApi activityApi = new CheckBrandStoreFollowStatusApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    CheckBrandStoreFollowStatusRes followStatusRes = (CheckBrandStoreFollowStatusRes) object;
                    status = followStatusRes.getData().getIs_followed();
                    if (status.equals("yes")) {
                        follow_btn.setText("Following");
                        follow_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
                    } else {
                        follow_btn.setText("Follow");
                        follow_btn.setBackgroundColor(mContext.getResources().getColor(R.color.btn_gray));
                    }
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {
                if (object != null) {
                    CheckBrandStoreFollowStatusRes response = (CheckBrandStoreFollowStatusRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        if (item_type.equals("brand")) {
            activityApi.getbrandbyid(UserPreference.getInstance().getUserID(), item_id);
            BrandListApi listApi = new BrandListApi(new ServiceCallback() {

                @Override
                public void handleOnSuccess(Object object) {
                    BrandListRes brandListRes = (BrandListRes) object;
                    List<BrandList> brandLists = brandListRes.getData();

                    for (BrandList brand : brandLists) {
                        if (brand.getName().equalsIgnoreCase(item_name)) {
                            CommonUtility.setImage(mContext, brand.getLogo(), imgBrand, R.drawable.ic_placeholder_brand);
                            break;
                        }
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    // TODO Auto-generated method stub

                }
            });
            listApi.getBrandList(Integer.toString(0));
            listApi.execute();


        } else if (item_type.equals("store")) {
            activityApi.getstorebyid(UserPreference.getInstance().getUserID(), item_id);
        }
        activityApi.execute();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (hasFocus) {

            //multiAutoCompleteTextView.showDropDown();
        }
    }

    @Override
    public void setData(Bundle bundle) {

        multiAutoCompleteTextView.setHint("Search for People, Store & Brands.");


        listSearch.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                //  AlertUtils.showToast(mContext, scrollState + "");
                // Do nothing
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
//				   System.out.println("123456 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
//			    	System.out.println("123456 inside if page"+pagenum+" ,"+isSelected);
                    if (checkInternet()) {
                        //pagenum++;
                        isFirstTime = false;
                        //getAll();
                    }
                }
            }
        });

        searchResultList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                // Do nothing
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
//				   System.out.println("1234 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
//			    	System.out.println("1234 inside if ");
                    if (checkInternet2()) {

                        if (pageno > 0) {
                            isFirstTime = false;
                            if (filterOn)
                                getFilteredProductList();
                            else
                                search();
                        }
                        pageno++;
                    } else {
                        showReloadFotter();
                    }
                }
            }
        });
    }

    private void showKeypad() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        multiAutoCompleteTextView.requestFocus();
        imm.showSoftInput(multiAutoCompleteTextView, 0);
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        String[] values = {"Ali", "Zak", "Anshumaan", "anshu", "Morteza", "sanchit", "brunette"};


//        if(AppConstants.chipBubbleText==null)
//            AppConstants.chipBubbleText = new ChipBubbleText(mContext, multiAutoCompleteTextView, values, 1);
//        AppConstants.chipBubbleText.setChipColor("#07948c");
//        AppConstants.chipBubbleText.setChipTextColor(R.color.app_text_color);
//        AppConstants.chipBubbleText.setChipTextSize(18);
//
//        AppConstants.chipBubbleText.initialize();

        multiAutoCompleteTextView.setOnEditorActionListener(this);
        follow_btn.setOnClickListener(this);
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
        imgSearch.setOnClickListener(this);
        txtDone.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.imgSearch || v.getId() == R.id.txtDone) {
            searchModule();

        }
        switch (v.getId()) {
            case R.id.follow_btn:
                // if user is currently following brand, change text to "Following" and do follow function
//                // else change text to "Follow" and do unfollow function
//                if (item_type.equals("store")) {
//                    changeFollowStoreStatus();
//                } else {
//                    changeFollowBrandStatus();
//                }
                break;

            case R.id.imgButtonFilter:
                filterName = TextUtils.isEmpty(multiAutoCompleteTextView.getText()) ? "" : multiAutoCompleteTextView.getText().toString();
                if (transparentOverlay.getVisibility() == View.VISIBLE)
                    transparentOverlay.setVisibility(View.GONE);
                else {
                    transparentOverlay.setVisibility(View.VISIBLE);
                    transparentOverlay.requestFocus();
                }
                break;
            case R.id.applyButton:
                if (checkOnSale.isChecked())
                    if (!(check50.isChecked() || check25.isChecked() || check150.isChecked() || check100.isChecked() || check200.isChecked() || check500.isChecked() ||
                            check750.isChecked() || check750More.isChecked())) {
                        CommonUtility.showAlert(mContext, "Price range required for On Sale categories!");
                        break;
                    }
                if (!(checkMen.isChecked() || checkWomen.isChecked() || checkAccessories.isChecked() || checkOnSale.isChecked())) {
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

    private void searchModule() {

        String searchProduct = "";
        isProductFromBubble = false;


        if (checkInternet()) {
            searchString = multiAutoCompleteTextView.getText().toString();
            if (searchString.length() == 0) {
                AlertUtils.showToast(mContext, "Please enter search text.");
                return;
            }

            searchString = searchString.trim().replaceAll(",", " ");


            product_layout.setVisibility(View.VISIBLE);
            listSearch.setVisibility(View.GONE);
            pagenum = 0;
            search();

        }


    }

    public void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

    public void getProductListApi() {
        listSearch.setVisibility(View.GONE);
        product_layout.setVisibility(View.VISIBLE);

        if (checkInternet())
            search();
        else
            showReloadOption();


//        if (checkInternet2() && !isLoadProductBasedOnType && !user_id.equals(UserPreference.getInstance().getUserID()))
//            addCollectionView();
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


    public void getAll() {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        if (pagenum > 0) {
            showFotter();
        } else


            mProgressBarDialog.show();
        isLoading = !isLoading;

        final SearchAllApi searchAllApi = new SearchAllApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFotter();
                }
                CommonUtility.hideSoftKeyboard(mContext);
                hideProductNotFound();
                Syso.info("In handleOnSuccess>>" + object);
                isLoading = !isLoading;
                itemArrayList = new ArrayList<>();
                SearchStoreBrandUserRes searchStoreBrandUserRes = (SearchStoreBrandUserRes) object;
                sectionItem = new SectionItem("Brands");
                itemArrayList.add(sectionItem);
                for (int i = 0; i < searchStoreBrandUserRes.getBrands().size(); i++) {
                    searchResult = new SearchResult();
                    searchResult.setId(searchStoreBrandUserRes.getBrands().get(i).getId());
                    searchResult.setName(searchStoreBrandUserRes.getBrands().get(i).getName());
                    searchResult.setImg(searchStoreBrandUserRes.getBrands().get(i).getImg());
                    searchResult.setDescription(searchStoreBrandUserRes.getBrands().get(i).getDescription());
                    searchResult.setIs_followed(searchStoreBrandUserRes.getBrands().get(i).getIs_followed());
                    searchResult.setLogo(searchStoreBrandUserRes.getBrands().get(i).getLogo());
                    searchResult.setSection_name("brands");
                    itemArrayList.add(searchResult);
                }
                sectionItem = new SectionItem("Stores");
                itemArrayList.add(sectionItem);
                for (int i = 0; i < searchStoreBrandUserRes.getStores().size(); i++) {
                    searchResult = new SearchResult();
                    searchResult.setId(searchStoreBrandUserRes.getStores().get(i).getId());
                    searchResult.setName(searchStoreBrandUserRes.getStores().get(i).getName());
                    searchResult.setImg(searchStoreBrandUserRes.getStores().get(i).getImg());
                    searchResult.setDescription(searchStoreBrandUserRes.getStores().get(i).getDescription());
                    searchResult.setIs_followed(searchStoreBrandUserRes.getStores().get(i).getIs_followed());
                    searchResult.setSection_name("stores");
                    itemArrayList.add(searchResult);
                }
                sectionItem = new SectionItem("People");
                itemArrayList.add(sectionItem);
                for (int i = 0; i < searchStoreBrandUserRes.getUsers().size(); i++) {
                    searchResult = new SearchResult();
                    searchResult.setId(searchStoreBrandUserRes.getUsers().get(i).getId());
                    searchResult.setName(searchStoreBrandUserRes.getUsers().get(i).getName());
                    searchResult.setImg(searchStoreBrandUserRes.getUsers().get(i).getImg());
                    searchResult.setDescription(searchStoreBrandUserRes.getUsers().get(i).getDescription());
                    searchResult.setIs_followed(searchStoreBrandUserRes.getUsers().get(i).getIs_followed());
                    searchResult.setSection_name("peoples");
                    itemArrayList.add(searchResult);
                }

                if (itemArrayList.size() == 0 && isFirstTime)
                    showProductNotFound();

                searchAllAdapter = new SearchAllAdapterSection(mContext, itemArrayList, fragmentSearchAll);
                listSearch.setAdapter(searchAllAdapter);


//                } else {
//                    searchAllAdapter.setData(itemArrayList);
//                    searchAllAdapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFotter();
                }
                CommonUtility.hideSoftKeyboard(mContext);
                Syso.info("In handleOnFailure>>" + object);
                isLoading = !isLoading;
                if (object != null) {
//                    InterestSectionRes response = (InterestSectionRes) object;
//                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    // AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        searchAllApi.searchStoreBrandPeople(UserPreference.getInstance().getUserID(), searchString, Integer.toString(pagenum));
        searchAllApi.execute();


    }

    public void showProductNotFound() {
        try {
            LinearLayout layout = (LinearLayout) mainView.findViewById(R.id.itemNotFound);
            layout.setVisibility(View.VISIBLE);
            TextView textView = (TextView) layout.findViewById(R.id.noDataFoundTextView);
            textView.setText("Result not found.");
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    public void hideProductNotFound() {
        try {
            LinearLayout layout = (LinearLayout) mainView.findViewById(R.id.itemNotFound);
            layout.setVisibility(View.GONE);
            TextView textView = (TextView) layout.findViewById(R.id.noDataFoundTextView);
            textView.setText("Result not found.");
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    private void showReloadOption() {
        showProductNotFound();
        TextView textView = getDataNotFound();
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInternet()) {
                        getAll();
                    }
                }
            });
        }
    }

    protected void showReloadFotter() {
        TextView textView = getReloadFotter();
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkInternet()) {
                    // pagenum++;
                    isFirstTime = false;
                    // getAll();
                }
            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        isProductFromBubble = false;


        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            if (checkInternet()) {
                searchString = multiAutoCompleteTextView.getText().toString();
                if (searchString.length() == 0) {
                    AlertUtils.showToast(mContext, "Please enter search text.");
                    return true;
                }

                searchString = searchString.trim().replaceAll(",", " ");

                data.clear();
                product_layout.setVisibility(View.VISIBLE);
                listSearch.setVisibility(View.GONE);
                pageno = 0;
                search();
                return true;
            }
        }
        return false;
    }

    private void search() {
        isLoading = !isLoading;

        mProgressBarDialog = new ProgressBarDialog(mContext);
        if (pageno > 0)
            showFotter();
        else
            mProgressBarDialog.show();

        final SearchApi searchApi = new SearchApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFotter();
                layoutDone.setVisibility(View.GONE);
                CommonUtility.hideSoftKeyboard(mContext);
                hideProductNotFound();
                Syso.info("In handleOnSuccess>>" + object);
                isLoading = !isLoading;
                SearchRes searchRes = (SearchRes) object;
                List<Product> data2 = searchRes.getData();
                if (data2.size() < 10) {
                    isLoading = true;
                }
                if (data2.size() == 0 && isFirstTime) {
                    showProductNotFound();
                    imgButtonFilter.setVisibility(View.GONE);
                } else if (data2.size() > 0 && isFirstTime) {
                    imgButtonFilter.setVisibility(View.VISIBLE);
                    hideProductNotFound();
                    data = data2;
                    searchAdapter = new SearchAdapter(mContext, data);
                    searchResultList.setAdapter(searchAdapter);
                } else {
                    data.addAll(data2);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFotter();
                CommonUtility.hideSoftKeyboard(mContext);

                Syso.info("In handleOnFailure>>" + object);
                isLoading = !isLoading;
                if (object != null) {
                    SearchRes response = (SearchRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        searchApi.searchProduct(UserPreference.getInstance().getUserID(), searchString, Integer.toString(pageno));
        searchApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
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
                hideProductNotFound();
                isLoading = !isLoading;
                layoutDone.setVisibility(View.GONE);
                Syso.info("In handleOnSuccess>>" + object);
                ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
                List<Product> productLists = productBasedOnBrandRes.getData();

                if (productLists.size() == 0 && pagenum == 0)
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
                    //imgButtonFilter.setVisibility(View.GONE);
                } else if (data.size() > 0 && isFirstTime) {

                    searchAdapter = new SearchAdapter(mContext, data);
                    searchResultList.setAdapter(searchAdapter);
                } else {
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
        productBasedOnBrandApi.getProductsBasedOnFilter(filterUserID, filterType, filterName, Integer.toString(pagenum), filterCategoryType, filterPriceMin, filterPriceMax, filterUserID);
        productBasedOnBrandApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                productBasedOnBrandApi.cancel();
            }
        });
    }


}

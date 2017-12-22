package com.flatlay.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.adapter.CustomizeFeedAllAdapter;
import com.flatlay.adapter.CustomizeInterestProductListAdapter;
import com.flatlay.adapter.CustomizeInterestCategoryListAdapter;
import com.flatlay.adapter.CustomizeInterestPeopleListAdapter;
import com.flatlay.adapter.CustomizeInterestStoreListAdapter;
import com.flatlay.bubble.ChipBubbleText;
import com.flatlay.fragment.FragmentFeatured;
import com.flatlay.model.Item;
import com.flatlay.model.SearchResult;
import com.flatlay.model.SectionItem;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.BrandListApi;
import com.flatlaylib.api.ProductListApi;
import com.flatlaylib.api.CategoryListApi;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.api.InterestSectionApi;
import com.flatlaylib.api.SearchAllApi;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.SearchStoreProductUserRes;
//import com.flatlaylib.bean.SearchStoreBrandUserRes;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.ProductListRes;
import com.flatlaylib.service.res.CategoryRes;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.service.res.InterestSectionRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;


public class CustomizeFeedActivity extends FragmentActivity implements View.OnClickListener, ServiceCallback, AdapterView.OnItemClickListener, View.OnKeyListener {

    private View mainView;
    private ListView interestSectionList;
    private ProgressBarDialog mProgressBarDialog;
    private List<InterestSection> interestList;
    private int pagenum = 0;
    private Button interest_store_button, interest_product_button, interest_people_button, all_button;
    // private TextView interest_category_button;
    private CustomizeInterestProductListAdapter interestProductListAdapter;
    private CustomizeInterestCategoryListAdapter interestCategoryListAdapter;
    private CustomizeInterestStoreListAdapter interestStoreListAdapter;
    private CustomizeFeedActivity fragmentInterestSection;
    private CustomizeFeedAllAdapter searchAllAdapter;
    private CustomizeInterestPeopleListAdapter interestPeopleListAdapter;
    private View peopleHeaderView;
    private RoundImageView trendingGalsLinearLayout, trendingGuysLinearLayout;
    private boolean isShown = false;
    private MultiAutoCompleteTextView searchYourItemEditText;
    private String isSelected = "people";
    private boolean isLoading = false;
    private boolean isGuys = false;
    private boolean isGals = false;
    private boolean isFirstTime = true;
    SearchResult searchResult;
    ArrayList<Item> itemArrayList;
    private boolean isSearchActive = true;
    private GridView categoryGridView;
    private TextView noDataGalGuy;
    FragmentActivity context;
    ImageView imgDelete;
    SectionItem sectionItem;

    View all_button_active, interest_people_button_active, interest_store_button_active, interest_product_button_active;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.w("Activity:","CustomizeFeedActivity");
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.customize_fragment_interest_section2);
        this.context = (FragmentActivity) CustomizeFeedActivity.this;
        initLayout();

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            CommonUtility.hideSoftKeyboard(context);
            if (checkInternet()) {
                interestSectionList.setAdapter(null);
                if (interestSectionList.getHeaderViewsCount() != 0)
                    interestSectionList.removeHeaderView(peopleHeaderView);
                pagenum = 0;
                isSearchActive = true;
                isFirstTime = true;
                isLoading = false;
                search();
                return true;
            }
        }
        return false;
    }


    public void initLayout() {
        peopleHeaderView = getLayoutInflater().inflate(R.layout.interest_header, null);
        fragmentInterestSection = this;
        all_button_active = (View) findViewById(R.id.all_button_active);
        //interest_product_button_active = (View) findViewById(R.id.interest_product_button_active);
        interest_people_button_active = (View) findViewById(R.id.interest_people_button_active);
        interest_store_button_active = (View) findViewById(R.id.interest_store_button_active);
        imgDelete = (ImageView) findViewById(R.id.imgDelete);
        interestSectionList = (ListView) findViewById(R.id.interestSectionList);
        interest_store_button = (Button) findViewById(R.id.interest_store_button);
        all_button = (Button) findViewById(R.id.all_button);
       // interest_product_button = (Button) findViewById(R.id.interest_product_button);
        interest_people_button = (Button) findViewById(R.id.interest_people_button);
        // interest_category_button = (TextView) findViewById(R.id.interest_category_imageview);
        trendingGalsLinearLayout = (RoundImageView) peopleHeaderView.findViewById(R.id.trendingGalsLinearLayout);
        trendingGuysLinearLayout = (RoundImageView) peopleHeaderView.findViewById(R.id.trendingGuysLinearLayout);
        searchYourItemEditText = (MultiAutoCompleteTextView) findViewById(R.id.searchYourItemEditText);
        categoryGridView = (GridView) findViewById(R.id.categoryGridView);
        noDataGalGuy = (TextView) peopleHeaderView.findViewById(R.id.layoutNoDataGalGuy);
        setClickListener();
        setupData();


    }


    public void setupData() {
        if (checkInternet()) {

            getAll();
        } else
            showReloadOption();

        interestSectionList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                // Do nothing
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (checkInternet()) {
                        pagenum++;
                        isFirstTime = false;
                        loadData();
                    } else {
                        showReloadFotter();
                    }
                }
            }
        });
    }


    public void setClickListener() {
        interest_store_button.setOnClickListener(this);
        //interest_product_button.setOnClickListener(this);
        interest_people_button.setOnClickListener(this);
        // interest_category_button.setOnClickListener(this);
        trendingGalsLinearLayout.setOnClickListener(this);
        trendingGuysLinearLayout.setOnClickListener(this);
        searchYourItemEditText.setOnKeyListener(this);
        imgDelete.setOnClickListener(this);
        all_button.setOnClickListener(this);
        String[] values = {"All", "Zeeshan", "Anshumaan", "Ajay", "Vinod", "himanshu", "Abhishek"};


        ChipBubbleText cp = new ChipBubbleText(context, searchYourItemEditText, values, 1);
        cp.setChipColor("#07948c");
        cp.setChipTextColor(R.color.white);
        cp.setChipTextSize(20);

        cp.initialize();

    }

    @Override
    public void onClick(View v) {
        //hideFotter();
        switch (v.getId()) {
            case R.id.imgDelete:
                finish();
                break;
            case R.id.interest_store_button:
                isFirstTime = true;
                pagenum = 0;
                isSelected = "store";
                isLoading = false;
                isSearchActive = false;
                //searchYourItemEditText.setText("");
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                if (interestSectionList.getHeaderViewsCount() != 0)
                    interestSectionList.removeHeaderView(peopleHeaderView);
                interestSectionList.setAdapter(null);
                // FragmentFeatured mFragmentFeatured = new FragmentFeatured();
                // FragmentManager fragmentManager =getFragmentManager();
                // FragmentTransaction transaction = fragmentManager.beginTransaction();


                all_button_active.setVisibility(View.INVISIBLE);
                interest_store_button_active.setVisibility(View.VISIBLE);
                interest_people_button_active.setVisibility(View.INVISIBLE);
                //interest_product_button_active.setVisibility(View.INVISIBLE);
                if (searchYourItemEditText.getText().toString().equals(""))
                    if (checkInternet()) {
                        getStoreList();
                        categoryGridView.setVisibility(View.GONE);
                        interestSectionList.setVisibility(View.VISIBLE);
                    } else
                        showReloadOption();
                else {
                    if (checkInternet()) {
                        interestSectionList.setAdapter(null);
                        if (interestSectionList.getHeaderViewsCount() != 0)
                            interestSectionList.removeHeaderView(peopleHeaderView);
                        pagenum = 0;
                        isSearchActive = true;
                        isFirstTime = true;
                        isLoading = false;
                        search();
                    }
                }

                break;
//            case R.id.interest_product_button:
//                pagenum = 0;
//                isFirstTime = true;
//                isSelected = "product";
//                isLoading = false;
//                isSearchActive = false;
//                //searchYourItemEditText.setText("");
//                interestSectionList.setAdapter(null);
//                searchYourItemEditText.setFocusableInTouchMode(true);
//                searchYourItemEditText.setFocusable(true);
//                if (interestSectionList.getHeaderViewsCount() != 0)
//                    interestSectionList.removeHeaderView(peopleHeaderView);
//                all_button_active.setVisibility(View.INVISIBLE);
//                interest_store_button_active.setVisibility(View.INVISIBLE);
//                interest_people_button_active.setVisibility(View.INVISIBLE);
//                interest_product_button_active.setVisibility(View.VISIBLE);
//
//                if (searchYourItemEditText.getText().toString().equals(""))
//                    if (checkInternet()) {
//                        categoryGridView.setVisibility(View.GONE);
//                        interestSectionList.setVisibility(View.VISIBLE);
//                        getProductList();
//                    } else
//                        showReloadOption();
//                else {
//                    if (checkInternet()) {
//                        interestSectionList.setAdapter(null);
//                        if (interestSectionList.getHeaderViewsCount() != 0)
//                            interestSectionList.removeHeaderView(peopleHeaderView);
//                        pagenum = 0;
//                        isSearchActive = true;
//                        isFirstTime = true;
//                        isLoading = false;
//                        search();
//                    }
//                }
//                break;
            case R.id.interest_people_button:
                isFirstTime = true;
                isGuys = false;
                isGals = false;
                pagenum = 0;
                isLoading = false;
                isSearchActive = false;
                isSelected = "people";
                //searchYourItemEditText.setText("");
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                interestSectionList.setAdapter(null);
                if (interestSectionList.getHeaderViewsCount() == 0)
                    interestSectionList.removeHeaderView(peopleHeaderView);
                all_button_active.setVisibility(View.INVISIBLE);
                interest_store_button_active.setVisibility(View.INVISIBLE);
                interest_people_button_active.setVisibility(View.VISIBLE);
               // interest_product_button_active.setVisibility(View.INVISIBLE);
//			trendingGuysLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
//			trendingGalsLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                if (searchYourItemEditText.getText().toString().equals(""))
                    if (checkInternet()) {
                        categoryGridView.setVisibility(View.GONE);
                        interestSectionList.setVisibility(View.VISIBLE);
                       // getUserList("all");
                    } else
                        showReloadOption();
                else {
                    if (checkInternet()) {
                        interestSectionList.setAdapter(null);
                        if (interestSectionList.getHeaderViewsCount() != 0)
                            interestSectionList.removeHeaderView(peopleHeaderView);
                        pagenum = 0;
                        isSearchActive = true;
                        isFirstTime = true;
                        isLoading = false;
                        search();
                    }
                }
                break;
            case R.id.all_button:
                isFirstTime = true;
                isGuys = false;
                isGals = false;
                pagenum = 0;
                isLoading = false;
                isSearchActive = false;
                isSelected = "people";
                //searchYourItemEditText.setText("");
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                interestSectionList.setAdapter(null);
                if (interestSectionList.getHeaderViewsCount() == 0)
                    interestSectionList.removeHeaderView(peopleHeaderView);
                all_button_active.setVisibility(View.VISIBLE);
                interest_store_button_active.setVisibility(View.INVISIBLE);
                interest_people_button_active.setVisibility(View.INVISIBLE);
              //  interest_product_button_active.setVisibility(View.INVISIBLE);
//			trendingGuysLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
//			trendingGalsLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                if (searchYourItemEditText.getText().toString().equals(""))
                    if (checkInternet()) {
                        categoryGridView.setVisibility(View.GONE);
                        interestSectionList.setVisibility(View.VISIBLE);
                        getAll();
                    } else
                        showReloadOption();
                else {
                    if (checkInternet()) {
                        interestSectionList.setAdapter(null);
                        if (interestSectionList.getHeaderViewsCount() != 0)
                            interestSectionList.removeHeaderView(peopleHeaderView);
                        pagenum = 0;
                        isSearchActive = true;
                        isFirstTime = true;
                        isLoading = false;
                        getAll();
                    }
                }
                break;
            case R.id.interest_category_imageview:
//			pagenum = 0;
//			isFirstTime = true;
//			isLoading=false;
//			isSearchActive = false;
//			searchYourItemEditText.setText("");
//			if(interestSectionList.getHeaderViewsCount()!=0)
//				interestSectionList.removeHeaderView(peopleHeaderView);
//			if (!isShown) {
//				isShown = true;
//				interestSectionList.setAdapter(null);
//				searchYourItemEditText.setFocusableInTouchMode(false);
//				searchYourItemEditText.setFocusable(false);
//				isSelected ="category";
//				interest_store_button.setVisibility(View.GONE);
//				interest_product_button.setVisibility(View.GONE);
//				interest_people_button.setVisibility(View.GONE);
//				if(checkInternet()){
//					categoryGridView.setVisibility(View.VISIBLE);
//					interestSectionList.setVisibility(View.GONE);
//					getCategoryList();
//				}
//				else
//					showReloadOption();
//			} else{
//				isShown = false;
//				interestSectionList.setAdapter(null);
//				searchYourItemEditText.setFocusableInTouchMode(true);
//				searchYourItemEditText.setFocusable(true);
//				isSelected ="store";
//				interest_store_button.setVisibility(View.VISIBLE);
//				interest_product_button.setVisibility(View.VISIBLE);
//				interest_people_button.setVisibility(View.VISIBLE);
//				interest_store_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
//				interest_product_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
//				interest_people_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
//				if(checkInternet()){
//					categoryGridView.setVisibility(View.GONE);
//					interestSectionList.setVisibility(View.VISIBLE);
//					getStoreList();
//				}
//				else
//					showReloadOption();
//			}
                //addFragment(new FragmentSearch());
                break;
            case R.id.trendingGalsLinearLayout:
                isFirstTime = true;
                pagenum = 0;
                isGuys = false;
                isGals = true;
                isLoading = false;
                isSearchActive = false;
                searchYourItemEditText.setText("");
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                interestSectionList.setAdapter(null);
//			trendingGalsLinearLayout.setBackgroundColor(getResources().getColor(R.color.app_background));
//			trendingGuysLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                //if (checkInternet())
                 //   getUserList("female");
                //else
                    showReloadOption();
                break;
            case R.id.trendingGuysLinearLayout:
                isFirstTime = true;
                pagenum = 0;
                isGuys = true;
                isGals = false;
                isLoading = false;
                isSearchActive = false;
                searchYourItemEditText.setText("");
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                interestSectionList.setAdapter(null);
//			trendingGalsLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
//			trendingGuysLinearLayout.setBackgroundColor(getResources().getColor(R.color.app_background));
               // if (checkInternet())
                    //getUserList("male");
               // else
                    showReloadOption();
                break;
            default:
                break;
        }
    }

    public void getAll() {
        isLoading = !isLoading;
        mProgressBarDialog = new ProgressBarDialog(context);
        if (pagenum > 0) {
            showFooter();

        } else
            mProgressBarDialog.show();

        final SearchAllApi searchAllApi = new SearchAllApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFutter();
                }
                hideDataNotFound();
                Syso.info("In handleOnSuccess>>" + object);
                isLoading = !isLoading;
                itemArrayList = new ArrayList<>();
                SearchStoreProductUserRes searchStoreProductUserRes = (SearchStoreProductUserRes) object;
                sectionItem = new SectionItem("Products");
                itemArrayList.add(sectionItem);
                for (int i = 0; i < searchStoreProductUserRes.getProducts().size(); i++) {
                    searchResult = new SearchResult();
                    searchResult.setId(searchStoreProductUserRes.getProducts().get(i).getId());
                    searchResult.setName(searchStoreProductUserRes.getProducts().get(i).getName());
                    searchResult.setImg(searchStoreProductUserRes.getProducts().get(i).getImg());
                    searchResult.setDescription(searchStoreProductUserRes.getProducts().get(i).getDescription());
                    searchResult.setIs_followed(searchStoreProductUserRes.getProducts().get(i).getIs_followed());
                    searchResult.setLogo(searchStoreProductUserRes.getProducts().get(i).getLogo());
                    searchResult.setSection_name("products");
                    itemArrayList.add(searchResult);
                }
                sectionItem = new SectionItem("Stores");
                itemArrayList.add(sectionItem);
                for (int i = 0; i < searchStoreProductUserRes.getStores().size(); i++) {
                    searchResult = new SearchResult();
                    searchResult.setId(searchStoreProductUserRes.getStores().get(i).getId());
                    searchResult.setName(searchStoreProductUserRes.getStores().get(i).getName());
                    searchResult.setImg(searchStoreProductUserRes.getStores().get(i).getImg());
                    searchResult.setDescription(searchStoreProductUserRes.getStores().get(i).getDescription());
                    searchResult.setIs_followed(searchStoreProductUserRes.getStores().get(i).getIs_followed());
                    searchResult.setSection_name("stores");
                    itemArrayList.add(searchResult);
                }
                sectionItem = new SectionItem("People");
                itemArrayList.add(sectionItem);
                for (int i = 0; i < searchStoreProductUserRes.getUsers().size(); i++) {
                    searchResult = new SearchResult();
                    searchResult.setId(searchStoreProductUserRes.getUsers().get(i).getId());
                    searchResult.setName(searchStoreProductUserRes.getUsers().get(i).getName());
                    searchResult.setImg(searchStoreProductUserRes.getUsers().get(i).getImg());
                    searchResult.setDescription(searchStoreProductUserRes.getUsers().get(i).getDescription());
                    searchResult.setIs_followed(searchStoreProductUserRes.getUsers().get(i).getIs_followed());
                    searchResult.setSection_name("peoples");
                    itemArrayList.add(searchResult);
                }
                if (itemArrayList.size() < 10) {
                    isLoading = true;
                }
                if (itemArrayList.size() == 0 && isFirstTime)
                    showDataNotFound();
                else if (itemArrayList.size() > 0 && isFirstTime) {
                    hideDataNotFound();
                    searchAllAdapter = new CustomizeFeedAllAdapter(CustomizeFeedActivity.this, itemArrayList);
                    interestSectionList.setAdapter(searchAllAdapter);
                } else {
                    searchAllAdapter.setData(itemArrayList);
                    searchAllAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFutter();
                }
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
        searchAllApi.searchStoreBrandPeople(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString(), Integer.toString(pagenum));
        searchAllApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                searchAllApi.cancel();
            }
        });
    }

    private void getProductList() {
        isLoading = !isLoading;
        mProgressBarDialog = new ProgressBarDialog(CustomizeFeedActivity.this);
        if (pagenum > 0) {
            //showFotter();
        } else
            mProgressBarDialog.show();
        final InterestSectionApi interestSectionApi = new InterestSectionApi(this);
        interestSectionApi.getProductList(Integer.toString(pagenum));
        interestSectionApi.execute();
        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                interestSectionApi.cancel();
            }
        });
    }

    private void getStoreList() {
        isLoading = !isLoading;
        mProgressBarDialog = new ProgressBarDialog(CustomizeFeedActivity.this);
        if (pagenum > 0) {
            //showFotter();

        } else
            mProgressBarDialog.show();

        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFutter();
                }
                hideDataNotFound();
                Syso.info("In handleOnSuccess>>" + object);
                isLoading = !isLoading;
                InterestSectionRes interestSectionRes = (InterestSectionRes) object;
                interestList = interestSectionRes.getData();
                if (interestList.size() < 10) {
                    isLoading = true;
                }
                if (interestList.size() == 0 && isFirstTime)
                    showDataNotFound();
                else if (interestList.size() > 0 && isFirstTime) {
                    hideDataNotFound();
                  //  interestStoreListAdapter = new CustomizeInterestStoreListAdapter(context, interestList, fragmentFeatured);
                    interestSectionList.setAdapter(interestStoreListAdapter);
                } else {
                    interestStoreListAdapter.setData(interestList);
                    interestStoreListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFutter();
                }
                Syso.info("In handleOnFailure>>" + object);
                isLoading = !isLoading;
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.getStoreList(Integer.toString(pagenum));
        interestSectionApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                interestSectionApi.cancel();
            }
        });
    }

    private void getCategoryList() {
        isLoading = !isLoading;
        mProgressBarDialog = new ProgressBarDialog(CustomizeFeedActivity.this);
        if (pagenum > 0) {
            // showFotter();
        } else
            mProgressBarDialog.show();
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFutter();
                }
                hideDataNotFound();
                isLoading = !isLoading;
                Syso.info("In handleOnSuccess>>" + object);
                InterestSectionRes interestSectionRes = (InterestSectionRes) object;
                interestList = interestSectionRes.getData();
                if (interestList.size() < 10) {
                    isLoading = true;
                }
                if (interestList.size() == 0 && isFirstTime)
                    showDataNotFound();
                else if (interestList.size() > 0 && isFirstTime) {
                    hideDataNotFound();
                    interestCategoryListAdapter = new CustomizeInterestCategoryListAdapter(context, interestList, fragmentInterestSection);
                    categoryGridView.setAdapter(interestCategoryListAdapter);
                } else {
                    interestCategoryListAdapter.setData(interestList);
                    interestCategoryListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    // hideFotter();
                }
                Syso.info("In handleOnFailure>>" + object);
                isLoading = !isLoading;
                if (object != null) {
                    CategoryRes response = (CategoryRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.getCategoryList(Integer.toString(pagenum));
        interestSectionApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                interestSectionApi.cancel();
            }
        });
    }

    @Override
    public void handleOnSuccess(Object object) {
        if (mProgressBarDialog.isShowing())
            mProgressBarDialog.dismiss();
        else {
            hideFutter();
        }
        hideDataNotFound();
        isLoading = !isLoading;
        Syso.info("In handleOnSuccess>>" + object);
        InterestSectionRes interestSectionApi = (InterestSectionRes) object;
        interestList = interestSectionApi.getData();
        if (interestList.size() < 10) {
            isLoading = true;
        }
        if (interestList.size() == 0 && isFirstTime)
            showDataNotFound();
        else if (interestList.size() > 0 && isFirstTime) {
            hideDataNotFound();
            // interestProductListAdapter = new CustomizeInterestProductListAdapter(context, interestList, fragmentInterestSection);
            interestSectionList.setAdapter(interestProductListAdapter);
        } else {
            interestProductListAdapter.setData(interestList);
            interestProductListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (mProgressBarDialog.isShowing())
            mProgressBarDialog.dismiss();
        else {
            // hideFotter();
        }
        Syso.info("In handleOnFailure>>" + object);
        isLoading = !isLoading;
        if (object != null) {
            InterestSectionRes response = (InterestSectionRes) object;
            AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
        } else {
            AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

    public void addProduct(String product_id, final View v) {
//		v.findViewById(R.id.followButton).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final ProductListApi listApi = new ProductListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        listApi.addProducts(product_id);
        listApi.execute();
    }

    public void deleteProduct(String product_id, final View v) {
//		v.findViewById(R.id.followButton).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final ProductListApi listApi = new ProductListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        listApi.deleteProduct(product_id);
        listApi.execute();
    }

    public void addBrand(String brand_id, final View v) {
//		v.findViewById(R.id.followButton).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        listApi.addBrands(brand_id);
        listApi.execute();
    }

    public void deleteBrand(String brand_id, final View v) {
//		v.findViewById(R.id.followButton).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        listApi.deleteBrand(brand_id);
        listApi.execute();
    }


    public void addCategory(String catList, final View v) {
        v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
                if (object != null) {
                    CategoryRes response = (CategoryRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        listApi.addCategory(UserPreference.getInstance().getUserID(), catList);
        listApi.execute();
    }

    public void deleteCategory(String category_id, final View v) {
        v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
                if (object != null) {
                    CategoryRes response = (CategoryRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        listApi.deleteCategory(UserPreference.getInstance().getUserID(), category_id);
        listApi.execute();
    }

    public void followUser(String id, final View v) {
//		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        followUserApi.followUser(UserPreference.getInstance().getUserID(), id);
        followUserApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                followUserApi.cancel();
            }
        });
    }

    public void unFollowUser(String id, final View v) {
//		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        followUserApi.unFollowUser(UserPreference.getInstance().getUserID(), id);
        followUserApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                followUserApi.cancel();
            }
        });
    }

    public void followStore(String id, final View v) {
        v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.followStore(UserPreference.getInstance().getUserID(), id);
        interestSectionApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                interestSectionApi.cancel();
            }
        });
    }

    public void unFollowStore(String id, final View v) {
        v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.unFollowStore(UserPreference.getInstance().getUserID(), id);
        interestSectionApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                interestSectionApi.cancel();
            }
        });
    }


    //public void getUserList(String gender) {
//        Log.w("CustomizeFeedActivity","getUserList()");
//        isLoading = !isLoading;
//        mProgressBarDialog = new ProgressBarDialog(CustomizeFeedActivity.this);
//        if (pagenum > 0) {
//            //showFotter();
//        } else
//            mProgressBarDialog.show();
//        final InterestSectionApi followUserApi = new InterestSectionApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                if (mProgressBarDialog.isShowing())
//                    mProgressBarDialog.dismiss();
//                else {
//                    hideFutter();
//                }
//                hideDataNotFound();
//                isLoading = !isLoading;
//                Syso.info("In handleOnSuccess>>" + object);
//                InterestSectionRes followUserRes = (InterestSectionRes) object;
//                interestList = followUserRes.getData();
//                if (interestList.size() < 10) {
//                    isLoading = true;
//                }
//                if (interestList.size() == 0 && isFirstTime) {
//                    //hideDataNotFound();
//                    noDataGalGuy.setVisibility(View.VISIBLE);
//                    if (interestSectionList.getHeaderViewsCount() == 0)
//                       // interestSectionList.removeHeaderView(peopleHeaderView);
//                        showDataNotFound();
//
//                  //   interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(context, interestList, fragmentInterestSection);
//
//                    interestSectionList.setAdapter(interestPeopleListAdapter);
//                    //showDataNotFound();
//                } else if (interestList.size() > 0 && isFirstTime) {
//                    //hideDataNotFound();
//                    noDataGalGuy.setVisibility(View.GONE);
//                    if (interestSectionList.getHeaderViewsCount() == 0)
//                        interestSectionList.removeHeaderView(peopleHeaderView);
//                    showDataNotFound();
//
//                    // interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(context, interestList, fragmentInterestSection);
//                    interestSectionList.setAdapter(interestPeopleListAdapter);
//                } else {
//                    if (interestSectionList.getHeaderViewsCount() == 0)
//                        interestSectionList.removeHeaderView(peopleHeaderView);
//                    interestPeopleListAdapter.setData(interestList);
//                    interestPeopleListAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (mProgressBarDialog.isShowing())
//                    mProgressBarDialog.dismiss();
//                else {
//                    //hideFotter();
//                }
//                isLoading = !isLoading;
//                Syso.info("In handleOnFailure>>" + object);
//                if (object != null) {
//                    InterestSectionRes response = (InterestSectionRes) object;
//                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
//                } else {
//                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
//                }
//            }
//        });
//        followUserApi.getAllKikrUserList(UserPreference.getInstance().getUserID(), Integer.toString(pagenum), gender);
//        followUserApi.execute();
//
//        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                isLoading = !isLoading;
//                followUserApi.cancel();
//            }
//        });
//    }


    private void search() {
        mProgressBarDialog = new ProgressBarDialog(CustomizeFeedActivity.this);
        if (pagenum > 0) {
            showFooter();
        } else
            mProgressBarDialog.show();
        isLoading = !isLoading;
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFutter();
                }
                hideDataNotFound();
                Syso.info("In handleOnSuccess>>" + object);
                isLoading = !isLoading;
                InterestSectionRes interestSectionRes = (InterestSectionRes) object;
                if (interestSectionRes.getData().size() < 10) {
                    isLoading = true;
                }
                if (interestSectionRes.getData().size() == 0 && isFirstTime) {
                    showDataNotFound();
                } else {
                    hideDataNotFound();
                    if (isSelected.equalsIgnoreCase("store")) {
                        if (interestSectionRes.getData().size() > 0 && isFirstTime) {
                            interestList = interestSectionRes.getData();
                            //interestStoreListAdapter = new CustomizeInterestStoreListAdapter(context, interestList, fragmentInterestSection);
                            interestSectionList.setAdapter(interestStoreListAdapter);
                        } else {
                            interestList = interestSectionRes.getData();
                            interestStoreListAdapter.setData(interestList);
                            interestStoreListAdapter.notifyDataSetChanged();
                        }
                    } else if (isSelected.equalsIgnoreCase("product")) {
                        if (interestSectionRes.getData().size() > 0 && isFirstTime) {
                            interestList = interestSectionRes.getData();
                            interestProductListAdapter = new CustomizeInterestProductListAdapter(context, interestList, fragmentInterestSection);
                            interestSectionList.setAdapter(interestProductListAdapter);
                        } else {
                            interestList = interestSectionRes.getData();
                            interestProductListAdapter.setData(interestList);
                            interestProductListAdapter.notifyDataSetChanged();
                        }
                    } else if (isSelected.equalsIgnoreCase("people")) {
                        if (interestSectionRes.getData().size() > 0 && isFirstTime) {
                            showDataNotFound();
                            if (interestSectionList.getHeaderViewsCount() == 0)
                                interestSectionList.removeHeaderView(peopleHeaderView);
                            interestList = interestSectionRes.getData();
                            //interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(context, interestList, fragmentInterestSection);
                            interestSectionList.setAdapter(interestPeopleListAdapter);
                        } else {
                            interestList = interestSectionRes.getData();
                            interestPeopleListAdapter.setData(interestList);
                            interestPeopleListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        if (interestSectionRes.getData().size() > 0 && isFirstTime) {
                            interestList = interestSectionRes.getData();
                            interestCategoryListAdapter = new CustomizeInterestCategoryListAdapter(context, interestList, fragmentInterestSection);
                            categoryGridView.setAdapter(interestCategoryListAdapter);
                        } else {
                            interestList = interestSectionRes.getData();
                            interestCategoryListAdapter.setData(interestList);
                            interestCategoryListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    //hideFotter();
                }
                isLoading = !isLoading;
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });


        if (isSelected.equalsIgnoreCase("store")) {
            interestSectionApi.searchStore(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
        } else if (isSelected.equalsIgnoreCase("product")) {
            interestSectionApi.searchProduct(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
        } else if (isSelected.equalsIgnoreCase("people")) {
            interestSectionApi.searchUser(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
        } else {
            interestSectionApi.searchCategory(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
        }
        interestSectionApi.execute();
        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                interestSectionApi.cancel();
            }
        });
    }

    public void loadData() {
        if (isSelected.equalsIgnoreCase("store") && !isSearchActive) {
            getStoreList();
        } else if (isSelected.equalsIgnoreCase("product") && !isSearchActive) {
            getProductList();
        } else if (isSelected.equalsIgnoreCase("people") && isGuys && !isGals && !isSearchActive) {
          //  getUserList("male");
        } else if (isSelected.equalsIgnoreCase("people") && !isGuys && isGals && !isSearchActive) {
         //   getUserList("female");
        } else if (isSelected.equalsIgnoreCase("people") && !isGuys && !isGals && !isSearchActive) {
          //  getUserList("all");
        } else if (isSearchActive) {
            search();
        } else if (!isSearchActive) {
            getCategoryList();
        }
    }

    private void showReloadOption() {
        showDataNotFound();
        TextView textView = getDataNotFound();
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInternet()) {
                        loadData();
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
                    pagenum++;
                    isFirstTime = false;
                    loadData();
                }
            }
        });
    }

    public boolean checkInternet() {
        if (CommonUtility.isOnline(context)) {
            return true;
        } else {
            CommonUtility.showNoInternetAlert(context);
            return false;
        }
    }

    public void hideHeader() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.baseHeader);
        layout.setVisibility(View.GONE);
    }

    public void setTextHeader() {
        RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseHeader);
        RelativeLayout textLayout = (RelativeLayout) findViewById(R.id.textHeader);
        baseLayout.setVisibility(View.GONE);
        textLayout.setVisibility(View.VISIBLE);
    }

    public void showDataNotFound() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.itemNotFound);
        layout.setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.noDataFoundTextView);
        textView.setText(getResources().getString(R.string.no_data_found));
    }

    public TextView getDataNotFound() {
        TextView textView = (TextView) findViewById(R.id.noDataFoundTextView);
        textView.setText(Html.fromHtml(getResources().getString(R.string.no_internet)));
        return textView;
    }

    public void hideDataNotFound() {
        try {
            LinearLayout layout = (LinearLayout) findViewById(R.id.itemNotFound);
            if (layout != null)
                layout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextView getReloadFotter() {
        return ((HomeActivity) context).getReloadFotter();
    }

    public void showFooter() {
        LinearLayout footerLayout = (LinearLayout) findViewById(R.id.footer);
        footerLayout.setVisibility(View.VISIBLE);
    }

    public void hideFutter() {
        LinearLayout footerLayout = (LinearLayout) findViewById(R.id.footer);
        footerLayout.setVisibility(View.GONE);
    }
}
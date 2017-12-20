package com.flatlay.fragment;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.CustomizeInterestBrandListAdapter;
import com.flatlay.adapter.FeaturedTabAdapter;
import com.flatlay.feed_adapter_copy.CustomizeFeedAllAdapter;
import com.flatlay.feed_adapter_copy.CustomizeInterestProductListAdapter;
import com.flatlay.feed_adapter_copy.CustomizeInterestCategoryListAdapter;
import com.flatlay.feed_adapter_copy.CustomizeInterestPeopleListAdapter;
import com.flatlay.feed_adapter_copy.CustomizeInterestStoreListAdapter;
import com.flatlay.model.Item;
import com.flatlay.model.SearchResult;
import com.flatlay.model.SectionItem;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.BrandListApi;
import com.flatlaylib.api.FeaturedTabApi;
import com.flatlaylib.api.ProductListApi;
import com.flatlaylib.api.CategoryListApi;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.api.InterestSectionApi;
import com.flatlaylib.api.SearchAllApi;
import com.flatlaylib.bean.FeaturedTabData;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.SearchStoreProductUserRes;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.FeaturedTabApiRes;
import com.flatlaylib.service.res.ProductListRes;
import com.flatlaylib.service.res.CategoryRes;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.service.res.InterestSectionRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;


public class CustomizeFeedFragment extends FragmentFeatured implements View.OnClickListener, ServiceCallback, AdapterView.OnItemClickListener, MultiAutoCompleteTextView.OnEditorActionListener {


    private ListView interestSectionList;
    private ProgressBarDialog mProgressBarDialog;
    private List<FeaturedTabData> interestList;
    private int pagenum = 0;
    private Button interest_store_button, interest_people_button, people_all_button, people_guys_button, people_gals_button;

    private FeaturedTabAdapter interestProductListAdapter;
    private FeaturedTabAdapter interestCategoryListAdapter;
    private FeaturedTabAdapter interestStoreListAdapter;
    private CustomizeFeedFragment fragmentFeedFragment;
    private CustomizeFeedAllAdapter searchAllAdapter;
    private FeaturedTabAdapter interestPeopleListAdapter;
    private View peopleHeaderView;
    private RoundImageView trendingGalsLinearLayout, trendingGuysLinearLayout;
    private boolean isShown = false;
    public static MultiAutoCompleteTextView searchYourItemEditText;
    private String isSelected = "All";
    private boolean isLoading = false;
    private boolean isGuys = false;
    private boolean isGals = false;
    private boolean isFirstTime = true;
    SearchResult searchResult;
    ArrayList<Item> itemArrayList, peopleList, productList, storeList;

    private boolean isSearchActive = false;
    private GridView categoryGridView;
    private TextView noDataGalGuy;
    SectionItem sectionItem;
    private View loaderView;
    private int firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;

    private FragmentFeatured fragmentFeatured;

    private FeaturedTabAdapter featuredTabAdapter;

    private List<FeaturedTabData> product_list = new ArrayList<FeaturedTabData>();
    View interest_people_button_active, interest_store_button_active, interest_product_button_active, people_all_button_active, people_guys_button_active, people_gals_button_active;

    View mainView;
    LinearLayout layoutPeople, threetab;
    private TextView loadingTextView;

    public static CustomizeFeedFragment customizeFeedFragment;

    public CustomizeFeedFragment() {
        customizeFeedFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.customize_fragment_interest_section2, null);
        fragmentFeatured = this;
        return mainView;
    }


    @Override
    public void initUI(Bundle savedInstanceState) {
        peopleHeaderView = mContext.getLayoutInflater().inflate(R.layout.interest_header, null);
        fragmentFeedFragment = this;
        interest_people_button_active = (View) mainView.findViewById(R.id.interest_people_button_active);
        interest_store_button_active = (View) mainView.findViewById(R.id.interest_store_button_active);

        people_all_button_active = (View) mainView.findViewById(R.id.people_all_button_active);
        people_guys_button_active = (View) mainView.findViewById(R.id.people_guys_button_active);
        people_gals_button_active = (View) mainView.findViewById(R.id.people_gals_button_active);

        people_all_button = (Button) mainView.findViewById(R.id.people_all_button);
        people_guys_button = (Button) mainView.findViewById(R.id.people_guys_button);
        people_gals_button = (Button) mainView.findViewById(R.id.people_gals_button);

        layoutPeople = (LinearLayout) mainView.findViewById(R.id.layoutPeopleStoreBrand);
        threetab = (LinearLayout) mainView.findViewById(R.id.threetab);
        interest_store_button = (Button) mainView.findViewById(R.id.interest_store_button);
        interest_people_button = (Button) mainView.findViewById(R.id.interest_people_button);
        trendingGalsLinearLayout = (RoundImageView) peopleHeaderView.findViewById(R.id.trendingGalsLinearLayout);
        trendingGuysLinearLayout = (RoundImageView) peopleHeaderView.findViewById(R.id.trendingGuysLinearLayout);
        searchYourItemEditText = (MultiAutoCompleteTextView) mainView.findViewById(R.id.searchYourItemEditText);
        categoryGridView = (GridView) mainView.findViewById(R.id.categoryGridView);
        noDataGalGuy = (TextView) peopleHeaderView.findViewById(R.id.layoutNoDataGalGuy);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        loaderView = View.inflate(mContext, R.layout.footer, null);
        interestSectionList = (ListView) mainView.findViewById(R.id.interestSectionList);
    }

    public void initData() {
        isFirstTime = true;
        isGuys = false;
        isGals = false;
        pagenum = 0;
        isLoading = false;
        isSearchActive = false;
        isSelected = "All";
        searchYourItemEditText.setFocusableInTouchMode(true);
        searchYourItemEditText.setFocusable(true);
        interestSectionList.setAdapter(featuredTabAdapter);
        if (interestSectionList.getHeaderViewsCount() == 0)
            interestSectionList.removeHeaderView(peopleHeaderView);
        interest_store_button_active.setVisibility(View.INVISIBLE);
        interest_people_button_active.setVisibility(View.INVISIBLE);
        layoutPeople.setVisibility(View.GONE);
    }

    @Override
    public void setData(Bundle bundle) {

        if (checkInternet()) {
            pagenum++;
            isFirstTime = false;
            getFeaturedTabData();
        } else {
            showReloadFotter();
        }

//        if (checkInternet()) {
//
//            getAll();
//        } else
//            showReloadOption();

//        interestSectionList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view,
//                                             int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                CustomizeFeedFragment.this.firstVisibleItem=firstVisibleItem;
//                CustomizeFeedFragment.this.visibleItemCount=visibleItemCount;
//                CustomizeFeedFragment.this.totalItemCount=totalItemCount;
//                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
//
//                    if (checkInternet()) {
//                        pagenum++;
//                        isFirstTime = false;
//                        getFeaturedTabData();
//                    } else {
//                        showReloadFotter();
//                    }
//                }
//            }
//        });

//        interest_people_button_active.setOnClickListener(new AbsListView.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (checkInternet()) {
//                        pagenum++;
//                        isFirstTime = false;
//                        getFeaturedTabData();
//                    } else {
//                        showReloadFotter();
//                    }
//            }
//        });
    }

    private void getFeaturedTabData() {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        isLoading = !isLoading;
        if (!isFirstTime) {
            showFotter();
        } else {
            loadingTextView.setVisibility(View.VISIBLE);
            mProgressBarDialog.show();
        }


        final FeaturedTabApi listApi = new FeaturedTabApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                try {
                    if (!isFirstTime) {
                        hideFotter();
                    } else {
                        loadingTextView.setVisibility(View.GONE);
                        mProgressBarDialog.dismiss();
                    }
                    hideDataNotFound();
                    isLoading = !isLoading;
                    FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;
                    product_list.addAll(featuredTabApiRes.getData());
                    if (featuredTabApiRes.getData().size() < 1) {
                        isLoading = true;
                    }
                    if (product_list.size() == 0 && isFirstTime) {
                        showDataNotFound();
                    } else if (product_list.size() > 0 && isFirstTime) {
                        featuredTabAdapter = new FeaturedTabAdapter(mContext, product_list, fragmentFeatured);
                        interestSectionList.setAdapter(featuredTabAdapter);
                    } else if (featuredTabAdapter != null) {
                        featuredTabAdapter = new FeaturedTabAdapter(mContext, product_list, fragmentFeatured);
                        interestSectionList.setAdapter(featuredTabAdapter);
                    } else {
                        featuredTabAdapter = new FeaturedTabAdapter(mContext, product_list, fragmentFeatured);
                        interestSectionList.setAdapter(featuredTabAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (!isFirstTime) {
                    interestSectionList.removeFooterView(loaderView);
                } else {
                    loadingTextView.setVisibility(View.GONE);
                }
                isLoading = !isLoading;
                if (object != null) {
                    FeaturedTabApiRes response = (FeaturedTabApiRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        listApi.getFeaturedTabData(UserPreference.getInstance().getUserID(), String.valueOf(pagenum));
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
    public void refreshData(Bundle bundle) {

    }

    public void setClickListener() {
        interest_store_button.setOnClickListener(this);
        interest_people_button.setOnClickListener(this);
        trendingGalsLinearLayout.setOnClickListener(this);
        trendingGuysLinearLayout.setOnClickListener(this);
        searchYourItemEditText.setOnEditorActionListener(this);
        people_all_button.setOnClickListener(this);
        people_gals_button.setOnClickListener(this);
        people_guys_button.setOnClickListener(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
        }
    }

    @Override
    public void onClick(View v) {
        hideProductNotFound();
        switch (v.getId()) {

            case R.id.interest_store_button:
                isFirstTime = true;
                pagenum = 0;
                isSelected = "store";
                isLoading = false;
                isSearchActive = false;
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                if (interestSectionList.getHeaderViewsCount() != 0)
                    interestSectionList.removeHeaderView(peopleHeaderView);
                interestSectionList.setAdapter(featuredTabAdapter);
                interest_store_button_active.setVisibility(View.VISIBLE);
                interest_people_button_active.setVisibility(View.INVISIBLE);
                layoutPeople.setVisibility(View.GONE);
                if (searchYourItemEditText.getText().toString().equals(""))
                    if (checkInternet()) {
                        getStoreList();
                        categoryGridView.setVisibility(View.GONE);
                        interestSectionList.setVisibility(View.VISIBLE);
                    }else
                     showReloadOption();
                    else {
                        if (checkInternet()) {
                            interestSectionList.setAdapter(featuredTabAdapter);
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
            case R.id.interest_people_button:
                isFirstTime = true;
                isGuys = false;
                isGals = false;
                pagenum = 0;
                isLoading = false;
                isSearchActive = false;
                isSelected = "people";
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                interestSectionList.setAdapter(featuredTabAdapter);
                if (interestSectionList.getHeaderViewsCount() == 0)
                    interestSectionList.removeHeaderView(peopleHeaderView);
                interest_store_button_active.setVisibility(View.INVISIBLE);
                interest_people_button_active.setVisibility(View.VISIBLE);
                layoutPeople.setVisibility(View.VISIBLE);
                threetab.setVisibility(View.GONE);
                if (searchYourItemEditText.getText().toString().equals(""))
                    if (checkInternet()) {
                        categoryGridView.setVisibility(View.GONE);
                        interestSectionList.setVisibility(View.VISIBLE);
                        //getUserList("all");
                    } else
                        showReloadOption();
                else {
                    if (checkInternet()) {
                        interestSectionList.setAdapter(featuredTabAdapter);
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
//            case R.id.all_button:
//                isFirstTime = true;
//                isGuys = false;
//                isGals = false;
//                pagenum = 0;
//                isLoading = false;
//                isSearchActive = false;
//                isSelected = "All";
//                searchYourItemEditText.setFocusableInTouchMode(true);
//                searchYourItemEditText.setFocusable(true);
//                interestSectionList.setAdapter(featuredTabAdapter);
//                if (interestSectionList.getHeaderViewsCount() == 0)
//                    interestSectionList.removeHeaderView(peopleHeaderView);
//                interest_store_button_active.setVisibility(View.INVISIBLE);
//                interest_people_button_active.setVisibility(View.INVISIBLE);
//                layoutPeople.setVisibility(View.GONE);
//                if (searchYourItemEditText.getText().toString().equals(""))
//                    if (checkInternet()) {
//                        categoryGridView.setVisibility(View.GONE);
//                        interestSectionList.setVisibility(View.VISIBLE);
//                        getAll();
//                    } else
//                        showReloadOption();
//                else {
//                    if (checkInternet()) {
//                        interestSectionList.setAdapter(featuredTabAdapter);
//                        if (interestSectionList.getHeaderViewsCount() != 0)
//                            interestSectionList.removeHeaderView(peopleHeaderView);
//                        pagenum = 0;
//                        isSearchActive = true;
//                        isFirstTime = true;
//                        isLoading = false;
//                        getAll();
//                    }
//                }
//                break;
//            case R.id.interest_category_imageview:
//                break;
            case R.id.people_gals_button:
                isFirstTime = true;
                pagenum = 0;
                isGuys = false;
                isGals = true;
                isLoading = false;
                isSearchActive = false;
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                interestSectionList.setAdapter(featuredTabAdapter);
                people_all_button_active.setVisibility(View.INVISIBLE);
                people_guys_button_active.setVisibility(View.INVISIBLE);
                people_gals_button_active.setVisibility(View.VISIBLE);
                if (checkInternet())
                    getUserList("female");
                else
                    showReloadOption();
                break;
            case R.id.people_guys_button:
                isFirstTime = true;
                pagenum = 0;
                isGuys = true;
                isGals = false;
                isLoading = false;
                isSearchActive = false;
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                interestSectionList.setAdapter(featuredTabAdapter);
                people_all_button_active.setVisibility(View.INVISIBLE);
                people_guys_button_active.setVisibility(View.VISIBLE);
                people_gals_button_active.setVisibility(View.INVISIBLE);
                if (checkInternet())
                    getUserList("male");
                else
                    showReloadOption();
                break;

            case R.id.people_all_button:
                isFirstTime = true;
                pagenum = 0;
                isGuys = true;
                isGals = false;
                isLoading = false;
                isSearchActive = false;
                searchYourItemEditText.setFocusableInTouchMode(true);
                searchYourItemEditText.setFocusable(true);
                interestSectionList.setAdapter(featuredTabAdapter);
                people_all_button_active.setVisibility(View.VISIBLE);
                people_guys_button_active.setVisibility(View.INVISIBLE);
                people_gals_button_active.setVisibility(View.INVISIBLE);
                if (checkInternet())
                    getUserList("all");
                //else
                    showReloadOption();
                break;
            default:
                break;
        }
    }

//    public void getAll() {
//        isLoading = !isLoading;
//        mProgressBarDialog = new ProgressBarDialog(mContext);
//        if (pagenum > 0) {
//            showFotter();
//
//        } else {
//            peopleList = new ArrayList<>();
//            storeList = new ArrayList<>();
//            productList = new ArrayList<>();
//            itemArrayList = new ArrayList<>();
//            sectionItem = new SectionItem("Products");
//
//            productList.add(sectionItem);
//            sectionItem = new SectionItem("Stores");
//            storeList.add(sectionItem);
//            sectionItem = new SectionItem("People");
//            peopleList.add(sectionItem);
//
//            mProgressBarDialog.show();
//        }
//
//        final SearchAllApi searchAllApi = new SearchAllApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                if (mProgressBarDialog.isShowing())
//                    mProgressBarDialog.dismiss();
//                else {
//                    hideFotter();
//                }
//                hideProductNotFound();
//                Syso.info("In handleOnSuccess>>" + object);
//                isLoading = !isLoading;
//                itemArrayList = new ArrayList<>();
//                peopleList = new ArrayList<>();
//                storeList = new ArrayList<>();
//                productList = new ArrayList<>();
//
//                SearchStoreProductUserRes searchStoreProductUserRes = (SearchStoreProductUserRes) object;
//
//                for (int i = 0; i < searchStoreProductUserRes.getUsers().size(); i++) {
//                    searchResult = new SearchResult();
//                    searchResult.setId(searchStoreProductUserRes.getUsers().get(i).getId());
//                    searchResult.setName(searchStoreProductUserRes.getUsers().get(i).getName());
//                    searchResult.setImg(searchStoreProductUserRes.getUsers().get(i).getImg());
//                    searchResult.setDescription(searchStoreProductUserRes.getUsers().get(i).getDescription());
//                    searchResult.setIs_followed(searchStoreProductUserRes.getUsers().get(i).getIs_followed());
//                    searchResult.setSection_name("peoples");
//                    peopleList.add(searchResult);
//                }
//
//                for (int i = 0; i < searchStoreProductUserRes.getStores().size(); i++) {
//                    searchResult = new SearchResult();
//                    searchResult.setId(searchStoreProductUserRes.getStores().get(i).getId());
//                    searchResult.setName(searchStoreProductUserRes.getStores().get(i).getName());
//                    searchResult.setImg(searchStoreProductUserRes.getStores().get(i).getImg());
//                    searchResult.setDescription(searchStoreProductUserRes.getStores().get(i).getDescription());
//                    searchResult.setIs_followed(searchStoreProductUserRes.getStores().get(i).getIs_followed());
//                    searchResult.setSection_name("stores");
//                    storeList.add(searchResult);
//                }
//
//                itemArrayList.addAll(peopleList);
//                itemArrayList.addAll(productList);
//                itemArrayList.addAll(storeList);
//                if (itemArrayList.size() < 10) {
//                    isLoading = true;
//                }
//                if (itemArrayList.size() == 3 && isFirstTime)
//                    showProductNotFound();
//                else if (itemArrayList.size() > 3 && isFirstTime) {
//                    hideProductNotFound();
//                    searchAllAdapter = new CustomizeFeedAllAdapter(mContext, itemArrayList);
//                    interestSectionList.setAdapter(searchAllAdapter);
//                } else {
//
//                    searchAllAdapter.setData(itemArrayList);
//                    searchAllAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (mProgressBarDialog.isShowing())
//                    mProgressBarDialog.dismiss();
//                else {
//                    hideFotter();
//                }
//                isLoading = !isLoading;
//            }
//        });
//        searchAllApi.searchStoreBrandPeople(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
//        searchAllApi.execute();
//
//        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                isLoading = !isLoading;
//                searchAllApi.cancel();
//            }
//        });
//    }

//    private void getProductList() {
//        isLoading = !isLoading;
//        mProgressBarDialog = new ProgressBarDialog(mContext);
//        if (pagenum > 0) {
//        } else
//            mProgressBarDialog.show();
//        final InterestSectionApi interestSectionApi = new InterestSectionApi(this);
//        interestSectionApi.getProductList(Integer.toString(pagenum));
//        interestSectionApi.execute();
//        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                isLoading = !isLoading;
//                interestSectionApi.cancel();
//            }
//        });
//    }

    private void getStoreList() {
        isLoading = !isLoading;
        mProgressBarDialog = new ProgressBarDialog(mContext);
        if (pagenum > 0) {
            //showFotter();

        } else
            mProgressBarDialog.show();

//        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
            final FeaturedTabApi listApi = new FeaturedTabApi(new ServiceCallback() {


                @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFotter();
                }
                hideProductNotFound();
                Syso.info("In handleOnSuccess>>" + object);
                isLoading = !isLoading;
//                InterestSectionRes interestSectionRes = (InterestSectionRes) object;
                    FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;

                    interestList = featuredTabApiRes.getData();
                if (interestList.size() < 10) {
                    isLoading = true;
                }
                if (interestList.size() == 0 && isFirstTime)
                    showProductNotFound();
                else if (interestList.size() > 0 && isFirstTime) {
                    hideProductNotFound();
//                    interestStoreListAdapter = new CustomizeInterestStoreListAdapter(mContext, interestList, fragmentFeedFragment);
                    featuredTabAdapter = new FeaturedTabAdapter(mContext, product_list, fragmentFeatured);

//                    interestSectionList.setAdapter(interestStoreListAdapter);
                    interestSectionList.setAdapter(featuredTabAdapter);

                } else {
//                    interestStoreListAdapter.setData(interestList);
//                    interestStoreListAdapter.notifyDataSetChanged();

                    featuredTabAdapter = new FeaturedTabAdapter(mContext, product_list, fragmentFeatured);

//                    interestSectionList.setAdapter(interestStoreListAdapter);
                    interestSectionList.setAdapter(featuredTabAdapter);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFotter();
                }
                Syso.info("In handleOnFailure>>" + object);
                isLoading = !isLoading;
                if (object != null) {
//                    InterestSectionRes response = (InterestSectionRes) object;
                    FeaturedTabApiRes response = (FeaturedTabApiRes) object;

                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    //AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
//        interestSectionApi.getStoreList(Integer.toString(pagenum));
//        interestSectionApi.execute();

        listApi.getFeaturedTabData(UserPreference.getInstance().getUserID(), String.valueOf(pagenum));
        listApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
//                interestSectionApi.cancel();
                listApi.cancel();

            }
        });
    }

    @Override
    public void handleOnSuccess(Object object) {
        if (mProgressBarDialog.isShowing())
            mProgressBarDialog.dismiss();
        else {
            hideFotter();
        }
        hideProductNotFound();
        isLoading = !isLoading;
//        InterestSectionRes interestSectionApi = (InterestSectionRes) object;
        FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;

        interestList = featuredTabApiRes.getData();
        if (interestList.size() < 10) {
            isLoading = true;
        }
        if (interestList.size() == 0 && isFirstTime)
            showProductNotFound();
        else if (interestList.size() > 0 && isFirstTime) {
            hideProductNotFound();
            //interestProductListAdapter = new CustomizeInterestProductListAdapter(mContext, interestList, fragmentFeedFragment);
            interestSectionList.setAdapter(featuredTabAdapter);
        } else {
            interestProductListAdapter.setData(interestList);
            interestProductListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (mProgressBarDialog.isShowing())
            mProgressBarDialog.dismiss();

        isLoading = !isLoading;
        if (object != null) {
//            InterestSectionRes response = (InterestSectionRes) object;
            FeaturedTabApiRes response = (FeaturedTabApiRes) object;

            AlertUtils.showToast(mContext, response.getMessage());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    }

    public void addProduct(String product_id, final View v) {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final ProductListApi listApi = new ProductListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                }
            }
        });
        listApi.addProducts(product_id);
        listApi.execute();
    }

    public void deleteProduct(String product_id, final View v) {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final ProductListApi listApi = new ProductListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                }
            }
        });
        listApi.deleteProduct(product_id);
        listApi.execute();
    }

    public void addBrand(String brand_id, final View v) {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                }
            }
        });
        listApi.addBrands(brand_id);
        listApi.execute();
    }

    public void deleteBrand(String brand_id, final View v) {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
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
                if (object != null) {
                    CategoryRes response = (CategoryRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
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
                if (object != null) {
                    CategoryRes response = (CategoryRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                }
            }
        });
        listApi.deleteCategory(UserPreference.getInstance().getUserID(), category_id);
        listApi.execute();
    }

    public void followUser(String id, final View v) {
        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
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
        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
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
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
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
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
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


    public void getUserList(String gender) {
        isLoading = !isLoading;
        mProgressBarDialog = new ProgressBarDialog(mContext);
        if (pagenum > 0) {
        } else
            mProgressBarDialog.show();
//        final InterestSectionApi followUserApi = new InterestSectionApi(new ServiceCallback() {
            final FeaturedTabApi listApi = new FeaturedTabApi(new ServiceCallback() {


                @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                    hideFotter();
                }
                hideProductNotFound();
                isLoading = !isLoading;
//                InterestSectionRes followUserRes = (InterestSectionRes) object;
                FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;

                interestList = featuredTabApiRes.getData();
                if (interestList.size() < 10) {
                    isLoading = true;
                }
                if (interestList.size() == 0 && isFirstTime) {
                    hideProductNotFound();
                    noDataGalGuy.setVisibility(View.VISIBLE);
                    if (interestSectionList.getHeaderViewsCount() == 0)
                        interestSectionList.removeHeaderView(peopleHeaderView);
                    interestPeopleListAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeatured);
                    interestSectionList.setAdapter(interestPeopleListAdapter);
                    showProductNotFound();
                } else if (interestList.size() > 0 && isFirstTime) {
                    hideProductNotFound();
                    noDataGalGuy.setVisibility(View.GONE);
                    if (interestSectionList.getHeaderViewsCount() == 0)
                        interestSectionList.removeHeaderView(peopleHeaderView);
                    interestPeopleListAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeatured);
                    interestSectionList.setAdapter(interestPeopleListAdapter);
                } else {
                    if (interestSectionList.getHeaderViewsCount() == 0)
                        interestSectionList.removeHeaderView(peopleHeaderView);
                    interestPeopleListAdapter.setData(interestList);
                    interestPeopleListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                }
                isLoading = !isLoading;
                if (object != null) {
//                    InterestSectionRes response = (InterestSectionRes) object;
                    FeaturedTabApiRes response = (FeaturedTabApiRes) object;

                    AlertUtils.showToast(mContext, response.getMessage());
                }
            }
        });
        listApi.getAllKikrUserList(UserPreference.getInstance().getUserID(), Integer.toString(pagenum), gender);
        listApi.execute();
        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                listApi.cancel();
            }
        });
    }

    private void search() {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        if (pagenum > 0) {
            showFotter();
        } else
            mProgressBarDialog.show();
        isLoading = !isLoading;
//        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
        final FeaturedTabApi listApi = new FeaturedTabApi(new ServiceCallback() {

        @Override
            public void handleOnSuccess(Object object) {
                try {
                    if (mProgressBarDialog.isShowing())
                        mProgressBarDialog.dismiss();
                    else {
                        hideFotter();
                    }
                    hideProductNotFound();
                    isLoading = !isLoading;

//                    InterestSectionRes interestSectionRes = (InterestSectionRes) object;
                    FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;
                    //product_list.addAll(featuredTabApiRes.getData());

                    interestList = new ArrayList<FeaturedTabData>();

                    interestList.addAll(featuredTabApiRes.getData());

                    if (interestList.size() < 10) {
                        isLoading = true;
                    }
                    if (interestList.size() == 0 && isFirstTime) {
                        showProductNotFound();
                    } else {
                        hideProductNotFound();
                        if (isSelected.equalsIgnoreCase("store")) {
                            if (interestList.size() > 0 && isFirstTime) {
                               // interestStoreListAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeedFragment);
                                featuredTabAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeatured);

                                interestSectionList.setAdapter(featuredTabAdapter);
                            } else {
                            //    interestStoreListAdapter.setData(interestList);
                             //   interestStoreListAdapter.notifyDataSetChanged();
                                featuredTabAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeatured);
                                interestSectionList.setAdapter(featuredTabAdapter);

                            }
//                        } else if (isSelected.equalsIgnoreCase("product")) {
//                            if (featuredTabApiRes.getData().size() > 0 && isFirstTime) {
//                                interestList = featuredTabApiRes.getData();
//                                interestProductListAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeedFragment);
//                                interestSectionList.setAdapter(interestProductListAdapter);
//                            } else {
//                                interestList = featuredTabApiRes.getData();
//                                interestProductListAdapter.setData(interestList);
//                                interestProductListAdapter.notifyDataSetChanged();
//                            }
                        } else if (isSelected.equalsIgnoreCase("people")) {
                            if (featuredTabApiRes.getData().size() > 0 && isFirstTime) {
                                if (interestSectionList.getHeaderViewsCount() == 0)
                                    interestSectionList.removeHeaderView(peopleHeaderView);
                                interestList = featuredTabApiRes.getData();
                                interestPeopleListAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeatured);
                                interestSectionList.setAdapter(interestPeopleListAdapter);
                            } else {
                                interestPeopleListAdapter.setData(interestList);
                                interestPeopleListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            if (featuredTabApiRes.getData().size() > 0 && isFirstTime) {
                                interestCategoryListAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeatured);
                                categoryGridView.setAdapter(interestCategoryListAdapter);
                            } else {
                                interestCategoryListAdapter.setData(interestList);
                                interestCategoryListAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else {
                }
                isLoading = !isLoading;
                if (object != null) {
                   // InterestSectionRes response = (InterestSectionRes) object;
                    FeaturedTabApiRes response = (FeaturedTabApiRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());

                }
            }
        });

        if (isSelected.equalsIgnoreCase("store")) {
         //   listApi.getFeaturedTabData(UserPreference.getInstance().getUserID(),Integer.toString(pagenum));
             listApi.searchStore(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
//        } else if (isSelected.equalsIgnoreCase("product")) {
//            interestSectionApi.searchProduct(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
        } else if (isSelected.equalsIgnoreCase("people")) {
           // listApi.searchUser(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
        }
        listApi.execute();
        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                listApi.cancel();
            }
        });
    }

    public void loadData() {
        if (isSelected.equalsIgnoreCase("store") && !isSearchActive) {
            getStoreList();
//        } else if (isSelected.equalsIgnoreCase("product") && !isSearchActive) {
//            getProductList();
        } else if (isSelected.equalsIgnoreCase("people") && isGuys && !isGals && !isSearchActive) {
            getUserList("male");
        } else if (isSelected.equalsIgnoreCase("people") && !isGuys && isGals && !isSearchActive) {
            getUserList("female");
        } else if (isSelected.equalsIgnoreCase("people") && !isGuys && !isGals && !isSearchActive) {
            getUserList("all");
//        } else if (isSelected.equalsIgnoreCase("all") && !isSearchActive) {
//            getAll();
        } else if (isSearchActive) {
            search();
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
                        loadData();
                    }
                }
            });
        }
    }

    public void showProductNotFound() {
        try {
            LinearLayout layout = (LinearLayout) mainView.findViewById(R.id.itemNotFound);
            layout.setVisibility(View.VISIBLE);
            TextView textView = (TextView) layout.findViewById(R.id.noDataFoundTextView);
            textView.setTextColor(Color.parseColor("#ffffff"));
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
        if (CommonUtility.isOnline(mContext)) {
            return true;
        } else {
            CommonUtility.showNoInternetAlert(mContext);
            return false;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            CommonUtility.hideSoftKeyboard(mContext);
            if (checkInternet()) {
                interestSectionList.setAdapter(featuredTabAdapter);
                if (interestSectionList.getHeaderViewsCount() != 0)
                    interestSectionList.removeHeaderView(peopleHeaderView);
                pagenum = 0;
                isSearchActive = true;
                isFirstTime = true;
                isLoading = false;
//                if (isSelected.equalsIgnoreCase("All"))
//                    getAll();
//                else
                    search();
                return true;
            }
        }
        return false;
    }
}
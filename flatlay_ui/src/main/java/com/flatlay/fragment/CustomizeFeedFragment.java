package com.flatlay.fragment;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
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
    private List<InterestSection> interestList;
    private int pagenum = 0;
    //private Button interest_store_button, interest_people_button, people_all_button, people_guys_button, people_gals_button;
    private FeaturedTabAdapter interestProductListAdapter;
    private CustomizeInterestStoreListAdapter interestStoreListAdapter;
    private CustomizeFeedFragment fragmentFeedFragment;
    private CustomizeInterestPeopleListAdapter interestPeopleListAdapter;
    private View peopleHeaderView;
    private RoundImageView trendingGalsLinearLayout, trendingGuysLinearLayout;
    public static MultiAutoCompleteTextView searchYourItemEditText;
    private String isSelected = "All";
    private boolean isLoading = false;
    private boolean isGuys = false;
    private boolean isGals = false;
    private boolean isFirstTime = true;
    private boolean isSearchActive = false;
    private GridView categoryGridView;
    private TextView noDataGalGuy;
    private View loaderView;
    private FragmentFeatured fragmentFeatured;
    private FeaturedTabAdapter featuredTabAdapter;
    private List<InterestSection> product_list = new ArrayList<InterestSection>();
    private List<InterestSection> product_list2 = new ArrayList<InterestSection>();

    View interest_people_button_active, interest_store_button_active, people_all_button_active, people_guys_button_active, people_gals_button_active;
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
        //interest_people_button_active = (View) mainView.findViewById(R.id.interest_people_button_active);
        //interest_store_button_active = (View) mainView.findViewById(R.id.interest_store_button_active);
        //people_all_button_active = (View) mainView.findViewById(R.id.people_all_button_active);
        // people_guys_button_active = (View) mainView.findViewById(R.id.people_guys_button_active);
        //people_gals_button_active = (View) mainView.findViewById(R.id.people_gals_button_active);
        // people_all_button = (Button) mainView.findViewById(R.id.people_all_button);
        //  people_guys_button = (Button) mainView.findViewById(R.id.people_guys_button);
        //people_gals_button = (Button) mainView.findViewById(R.id.people_gals_button);
        layoutPeople = (LinearLayout) mainView.findViewById(R.id.layoutPeopleStoreBrand);
        //threetab = (LinearLayout) mainView.findViewById(R.id.threetab);
        // interest_store_button = (Button) mainView.findViewById(R.id.interest_store_button);
        // interest_people_button = (Button) mainView.findViewById(R.id.interest_people_button);
        trendingGalsLinearLayout = (RoundImageView) peopleHeaderView.findViewById(R.id.trendingGalsLinearLayout);
        trendingGuysLinearLayout = (RoundImageView) peopleHeaderView.findViewById(R.id.trendingGuysLinearLayout);
        searchYourItemEditText = (MultiAutoCompleteTextView) mainView.findViewById(R.id.searchYourItemEditText);
        categoryGridView = (GridView) mainView.findViewById(R.id.categoryGridView);
        noDataGalGuy = (TextView) peopleHeaderView.findViewById(R.id.layoutNoDataGalGuy);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        loaderView = View.inflate(mContext, R.layout.footer, null);
        interestSectionList = (ListView) mainView.findViewById(R.id.interestSectionList);
        // getStoreList();

    }

    public void initData() {
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
        //interest_store_button_active.setVisibility(View.INVISIBLE);
        //  interest_people_button_active.setVisibility(View.INVISIBLE);
        layoutPeople.setVisibility(View.GONE);
        getStoreList();

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
        //getStoreList();

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
                    Syso.info("In handleOnSuccess>>" + object);
                    FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;
                    product_list.addAll(featuredTabApiRes.getData());
                    List<InterestSection> list = new ArrayList<>();
                    list.addAll(product_list);
                    if (featuredTabApiRes.getData().size() < 1) {
                        isLoading = true;
                    }
                    if (product_list.size() == 0 && isFirstTime) {
                        showDataNotFound();
                    } else if (product_list.size() > 0 && isFirstTime) {
                        featuredTabAdapter = new FeaturedTabAdapter(mContext, list, fragmentFeatured);
                        interestSectionList.setAdapter(featuredTabAdapter);
                    } else if (featuredTabAdapter != null) {
                        //  featuredTabAdapter.setData(product_list);
                        //featuredTabAdapter.notifyDataSetChanged();
                        featuredTabAdapter = new FeaturedTabAdapter(mContext, list, fragmentFeatured);
                        interestSectionList.setAdapter(featuredTabAdapter);
                    } else {
                        featuredTabAdapter = new FeaturedTabAdapter(mContext, list, fragmentFeatured);
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
//					mProgressBarDialog.dismiss();
                }
                isLoading = !isLoading;
                Syso.info("In handleOnFailure>>" + object);
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

    private void getFeaturedTabData2() {
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
                    Syso.info("In handleOnSuccess>>" + object);
                    FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;
                    product_list2.addAll(featuredTabApiRes.getData());
                    List<InterestSection> list = new ArrayList<>();
                    list.addAll(product_list2);
                    if (featuredTabApiRes.getData().size() < 1) {
                        isLoading = true;
                    }
                    if (product_list2.size() == 0 && isFirstTime) {
                        showDataNotFound();
                    } else if (product_list.size() > 0 && isFirstTime) {
                        featuredTabAdapter = new FeaturedTabAdapter(mContext, list, fragmentFeatured);
                        interestSectionList.setAdapter(featuredTabAdapter);
                    } else if (featuredTabAdapter != null) {
                        //  featuredTabAdapter.setData(product_list);
                        //featuredTabAdapter.notifyDataSetChanged();
                        featuredTabAdapter = new FeaturedTabAdapter(mContext, list, fragmentFeatured);
                        interestSectionList.setAdapter(featuredTabAdapter);
                    } else {
                        featuredTabAdapter = new FeaturedTabAdapter(mContext, list, fragmentFeatured);
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
//					mProgressBarDialog.dismiss();
                }
                isLoading = !isLoading;
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    FeaturedTabApiRes response = (FeaturedTabApiRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        listApi.searchUser(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
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

    private void removeFocus() {
        searchYourItemEditText.setText("Search for People, Stores, Products.");
        searchYourItemEditText.clearFocus();
    }

    public void setClickListener() {
        //interest_store_button.setOnClickListener(this);
        //interest_product_button.setOnClickListener(this);
        //interest_people_button.setOnClickListener(this);
        // interest_category_button.setOnClickListener(this);
        trendingGalsLinearLayout.setOnClickListener(this);
        trendingGuysLinearLayout.setOnClickListener(this);
        searchYourItemEditText.setOnEditorActionListener(this);
        //people_all_button.setOnClickListener(this);
        //people_gals_button.setOnClickListener(this);
        //people_guys_button.setOnClickListener(this);

//        all_button.setOnClickListener(this);
//        String[] values = {"All", "Zeeshan", "Anshumaan", "Ajay", "Vinod", "himanshu", "Abhishek"};
//
//
//        ChipBubbleText cp = new ChipBubbleText(mContext, searchYourItemEditText, values, 1);
//        cp.setChipColor("#07948c");
//        cp.setChipTextColor(R.color.white);
//        cp.setChipTextSize(20);
//
//        cp.initialize();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            //Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            //removeFocus();
        }
    }

    @Override
    public void onClick(View v) {
        hideProductNotFound();
    }
    // removeFocus();
    //hideFotter();
//        switch (v.getId()) {

//            case R.id.interest_store_button:
//                isFirstTime = true;
//                pagenum = 0;
//                isSelected = "store";
//                isLoading = false;
//                isSearchActive = false;
//                //searchYourItemEditText.setText("");
//                searchYourItemEditText.setFocusableInTouchMode(true);
//                searchYourItemEditText.setFocusable(true);
//                if (interestSectionList.getHeaderViewsCount() != 0)
//                    interestSectionList.removeHeaderView(peopleHeaderView);
//                // interestSectionList.setAdapter(featuredTabAdapter);
//                interest_store_button_active.setVisibility(View.VISIBLE);
//                interest_people_button_active.setVisibility(View.INVISIBLE);
//                // interest_product_button_active.setVisibility(View.INVISIBLE);
//                layoutPeople.setVisibility(View.GONE);
//                if (searchYourItemEditText.getText().toString().equals(""))
//                    if (checkInternet()) {
//                        getStoreList();
//                        categoryGridView.setVisibility(View.GONE);
//                        interestSectionList.setVisibility(View.VISIBLE);
//                    } else
//                        showReloadOption();
//                else {
//                    if (checkInternet()) {
//                        //interestSectionList.setAdapter(featuredTabAdapter);
//                        if (interestSectionList.getHeaderViewsCount() != 0)
//                            interestSectionList.removeHeaderView(peopleHeaderView);
//                        pagenum = 0;
//                        isSearchActive = true;
//                        isFirstTime = true;
//                        isLoading = false;
//                        search();
//                    }
//                }
//
//                break;

//
//            case R.id.interest_people_button:
//                isFirstTime = true;
//                isGuys = false;
//                isGals = false;
//                pagenum = 0;
//                isLoading = false;
//                isSearchActive = false;
//                isSelected = "people";
//                //searchYourItemEditText.setText("");
//                searchYourItemEditText.setFocusableInTouchMode(true);
//                searchYourItemEditText.setFocusable(true);
//                interestSectionList.setAdapter(null);
//                if (interestSectionList.getHeaderViewsCount() == 0)
//                    interestSectionList.removeHeaderView(peopleHeaderView);
//                //all_button_active.setVisibility(View.INVISIBLE);
//                interest_store_button_active.setVisibility(View.INVISIBLE);
//                interest_people_button_active.setVisibility(View.VISIBLE);
//                //interest_product_button_active.setVisibility(View.INVISIBLE);
//                layoutPeople.setVisibility(View.VISIBLE);
//                threetab.setVisibility(View.GONE);
////			trendingGuysLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
////			trendingGalsLinearLayout.setBackgroundColor(getResources().getColor(R.color.white));
//                if (!searchYourItemEditText.getText().toString().equals(""))
////                    if (checkInternet()) {
////                        categoryGridView.setVisibility(View.GONE);
////                        interestSectionList.setVisibility(View.VISIBLE);
////                        getUserList("all");
////                    } else
////                        showReloadOption();
////               else {
////                    if (checkInternet()) {
////                        interestSectionList.setAdapter(null);
////                        if (interestSectionList.getHeaderViewsCount() != 0)
////                            interestSectionList.removeHeaderView(peopleHeaderView);
////                        pagenum = 0;
////                        isSearchActive = true;
////                        isFirstTime = true;
////                        isLoading = false;
////                        search();
////                    }
////                }
//                    break;
//            default:
//                break;
//        }
//    }


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
//            itemArrayList=new ArrayList<>();
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
//
////                for (int i = 0; i < searchStoreProductUserRes.getProducts().size(); i++) {
////                    searchResult = new SearchResult();
////                    searchResult.setId(searchStoreProductUserRes.getProducts().get(i).getId());
////                    searchResult.setName(searchStoreProductUserRes.getProducts().get(i).getName());
////                    searchResult.setImg(searchStoreProductUserRes.getProducts().get(i).getImg());
////                    searchResult.setDescription(searchStoreProductUserRes.getProducts().get(i).getDescription());
////                    searchResult.setIs_followed(searchStoreProductUserRes.getProducts().get(i).getIs_followed());
////                    searchResult.setLogo(searchStoreProductUserRes.getProducts().get(i).getLogo());
////                    searchResult.setSection_name("products");
////                    productList.add(searchResult);
////                }
//
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
//                Syso.info("In handleOnFailure>>" + object);
//                isLoading = !isLoading;
//                if (object != null) {
////                    InterestSectionRes response = (InterestSectionRes) object;
////                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
//                } else {
//                    // AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
//                }
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
//            //showFotter();
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
                // FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;
                // interestList.addAll(featuredTabApiRes.getData());
                List<InterestSection> list = new ArrayList<>();
                list.addAll(product_list);
                if (list.size() < 10) {
                    isLoading = true;
                }
                if (list.size() == 0 && isFirstTime)
                    showProductNotFound();
                else if (list.size() > 0 && isFirstTime) {
                    hideProductNotFound();
                    interestStoreListAdapter = new CustomizeInterestStoreListAdapter(mContext, list, fragmentFeedFragment);
                    interestSectionList.setAdapter(interestStoreListAdapter);
                } else {
                    interestStoreListAdapter = new CustomizeInterestStoreListAdapter(mContext, list, fragmentFeedFragment);
//                    interestSectionList.setAdapter(interestStoreListAdapter);
                    interestSectionList.setAdapter(featuredTabAdapter);
//                    interestStoreListAdapter.setData(list);
//                    interestStoreListAdapter.notifyDataSetChanged();
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
                    FeaturedTabApiRes response = (FeaturedTabApiRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    //AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        listApi.getStoreList(Integer.toString(pagenum));
        listApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                listApi.cancel();
            }
        });
    }

    @Override
    public void handleOnSuccess(Object object) {
//        if (mProgressBarDialog.isShowing())
//            mProgressBarDialog.dismiss();
//        else {
//            hideFotter();
//        }
//        hideProductNotFound();
//        isLoading = !isLoading;
//        Syso.info("In handleOnSuccess>>" + object);
////        InterestSectionRes interestSectionApi = (InterestSectionRes) object;
//        FeaturedTabApiRes FeaturedTabApiRes = (FeaturedTabApiRes) object;
//        interestList.addAll(FeaturedTabApiRes.getData());
//        List<InterestSection> list = new ArrayList<>();
//        list.addAll(interestList);
//        if (list.size() < 10) {
//            isLoading = true;
//        }
//        if (list.size() == 0 && isFirstTime)
//            showProductNotFound();
//        else if (list.size() > 0 && isFirstTime) {
//            hideProductNotFound();
////            interestProductListAdapter = new CustomizeInterestProductListAdapter(mContext, interestList, fragmentFeedFragment);
//            interestProductListAdapter = new FeaturedTabAdapter(mContext, list, fragmentFeedFragment);
//
//            interestSectionList.setAdapter(interestProductListAdapter);
//        } else {
//            interestProductListAdapter.setData(interestList);
//            interestProductListAdapter.notifyDataSetChanged();
        //      }
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
            //InterestSectionRes response = (InterestSectionRes) object;
            FeaturedTabApiRes response = (FeaturedTabApiRes) object;

            AlertUtils.showToast(mContext, response.getMessage());
        } else {
            //AlertUtils.showToast(mContext, R.string.invalid_response);
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
                Syso.info("In handleOnSuccess>>" + object);
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    // AlertUtils.showToast(mContext, R.string.invalid_response);
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
                Syso.info("In handleOnSuccess>>" + object);
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    // AlertUtils.showToast(mContext, R.string.invalid_response);
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
                Syso.info("In handleOnSuccess>>" + object);
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    // AlertUtils.showToast(mContext, R.string.invalid_response);
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
                Syso.info("In handleOnSuccess>>" + object);
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    // AlertUtils.showToast(mContext, R.string.invalid_response);
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
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    // AlertUtils.showToast(mContext, R.string.invalid_response);
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
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    //AlertUtils.showToast(mContext, R.string.invalid_response);
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
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    // AlertUtils.showToast(mContext, R.string.invalid_response);
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
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    // AlertUtils.showToast(mContext, R.string.invalid_response);
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
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    //AlertUtils.showToast(mContext, R.string.invalid_response);
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
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    //AlertUtils.showToast(mContext, R.string.invalid_response);
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


//    public void getUserList(String gender) {
//        Log.w("CustomizeFeedFragment", "getUserList()");
//        isLoading = !isLoading;
//        mProgressBarDialog = new ProgressBarDialog(mContext);
//        if (pagenum > 0) {
//            //showFotter();
//        } else
//            mProgressBarDialog.show();
//        final InterestSectionApi followUserApi = new InterestSectionApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                Log.w("CustomizeFeedFragment", "handleOnSuccess()");
//                if (mProgressBarDialog.isShowing())
//                    mProgressBarDialog.dismiss();
//                else {
//                    hideFotter();
//                }
//                Log.w("CustomizeFeedFragment", "handleOnSuccess() 2");
//                hideProductNotFound();
//                isLoading = !isLoading;
//                Syso.info("In handleOnSuccess>>" + object);
//                InterestSectionRes followUserRes = (InterestSectionRes) object;
//
//                interestList = followUserRes.getData();
//                if (interestList.size() < 10) {
//                    isLoading = true;
//                }
//                if (interestList.size() == 0 && isFirstTime) {
//                    Log.w("CustomizeFeedFragment", "handleOnSuccess() 3");
//                    hideProductNotFound();
//                    noDataGalGuy.setVisibility(View.VISIBLE);
//                    if (interestSectionList.getHeaderViewsCount() == 0)
//                        interestSectionList.removeHeaderView(peopleHeaderView);
////                    interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(mContext, interestList, fragmentFeedFragment);
//                    interestPeopleListAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeedFragment);
//                    interestSectionList.setAdapter(interestPeopleListAdapter);
//                    showProductNotFound();
//                } else if (interestList.size() > 0 && isFirstTime) {
//                    Log.w("CustomizeFeedFragment", "handleOnSuccess() 4");
//                    hideProductNotFound();
//                    noDataGalGuy.setVisibility(View.GONE);
//                    if (interestSectionList.getHeaderViewsCount() == 0)
//                        interestSectionList.removeHeaderView(peopleHeaderView);
////                    interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(mContext, interestList, fragmentFeedFragment);
//                    interestPeopleListAdapter = new FeaturedTabAdapter(mContext, interestList, fragmentFeedFragment);
//
//                    interestSectionList.setAdapter(interestPeopleListAdapter);
//                } else {
//                    Log.w("CustomizeFeedFragment", "handleOnSuccess() 5");
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
//                    AlertUtils.showToast(mContext, response.getMessage());
//                } else {
//                    //AlertUtils.showToast(mContext, R.string.invalid_response);
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

        mProgressBarDialog = new ProgressBarDialog(mContext);
        if (pagenum > 0) {
            showFotter();
        } else
            mProgressBarDialog.show();
        isLoading = !isLoading;

        //getFeaturedTabData2();
    //    final FeaturedTabApi listApi = new FeaturedTabApi(new ServiceCallback() {
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                try {
                    if (mProgressBarDialog.isShowing())
                        mProgressBarDialog.dismiss();
                    else {
                        hideFotter();
                    }

                    hideProductNotFound();
                    Syso.info("In handleOnSuccess>>" + object);
                    isLoading = !isLoading;

                    InterestSectionRes interestSectionRes = (InterestSectionRes) object;
                   // FeaturedTabApiRes interestSectionRes = (FeaturedTabApiRes) object;


                    List<InterestSection> list = new ArrayList<>();

                    list.addAll(interestSectionRes.getData());
                    if (interestSectionRes.getData().size() < 1) {
                        isLoading = true;
                    }
                    if (interestSectionRes.getData().size() == 0 && isFirstTime) {
                        showDataNotFound();
                    } else {
//                        if (isSelected.equalsIgnoreCase("store")) {
                            if (interestSectionRes.getData().size() > 0 && isFirstTime) {
                                interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(mContext, list, fragmentFeedFragment);
                                interestSectionList.setAdapter(interestPeopleListAdapter);

                            } else {
                                interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(mContext, list, fragmentFeedFragment);
                                interestSectionList.setAdapter(interestPeopleListAdapter);
                            }
//                        } else if (isSelected.equalsIgnoreCase("people")) {
//                            if (interestSectionRes.getData().size() > 0 && isFirstTime) {
//
//                                if (interestSectionList.getHeaderViewsCount() == 0)
//                                    interestSectionList.removeHeaderView(peopleHeaderView);
//                                interestPeopleListAdapter = new FeaturedTabAdapter(mContext, list, fragmentFeedFragment);
//                                interestSectionList.setAdapter(interestPeopleListAdapter);
//
//                            } else {
//
//                                interestPeopleListAdapter.setData(list);
//                                interestPeopleListAdapter.notifyDataSetChanged();
//                            }
//                        }
                    }
                } catch (Exception ex) {

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
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                     AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });


//        if (isSelected.equalsIgnoreCase("store")) {
        interestSectionApi.searchUser(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
//        } else if (isSelected.equalsIgnoreCase("people")) {
//            interestSectionApi.searchUser(UserPreference.getInstance().getUserID(), searchYourItemEditText.getText().toString().trim(), Integer.toString(pagenum));
//        }
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
//        if (isSelected.equalsIgnoreCase("store") && !isSearchActive) {
            getStoreList();
//        } else if (isSelected.equalsIgnoreCase("people") && isGuys && !isGals && !isSearchActive) {
//            getUserList("male");
//        } else if (isSelected.equalsIgnoreCase("people") && !isGuys && isGals && !isSearchActive) {
//            getUserList("female");
//        } else if (isSelected.equalsIgnoreCase("people") && !isGuys && !isGals && !isSearchActive) {
//            getUserList("all");
//        } else if (isSelected.equalsIgnoreCase("all") && !isSearchActive) {
//            //   getAll();
//        }
//        else if (isSearchActive) {
//            search();
//        }
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
            // interestSectionList.setVisibility(View.GONE);
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

            if (searchYourItemEditText.getText().toString().equals(""))
                if (checkInternet()) {
                    getStoreList();
                    categoryGridView.setVisibility(View.GONE);
                    interestSectionList.setVisibility(View.VISIBLE);
                } else
                    showReloadOption();
            else {

                if (checkInternet()) {
                    if (interestSectionList.getHeaderViewsCount() != 0)
                        interestSectionList.removeHeaderView(peopleHeaderView);
                    pagenum = 0;
                    isSearchActive = true;
                    isFirstTime = true;
                    isLoading = false;
                    search();
                }
            }
            return true;

        }


//            if (checkInternet()) {
//                interestSectionList.setAdapter(null);
//                if (interestSectionList.getHeaderViewsCount() != 0)
//                    interestSectionList.removeHeaderView(peopleHeaderView);
//                pagenum = 0;
//                isSearchActive = true;
//                isFirstTime = true;
//                isLoading = false;
//                if (isSelected.equalsIgnoreCase("All"))
//                    // getAll();
//                    // else
//                    search();
//                return true;
//
//            }


        return false;
    }
}
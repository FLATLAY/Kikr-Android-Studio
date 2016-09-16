package com.kikr.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.DiscoverAdapter;
import com.kikr.dialog.HelpPressMenuDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.ProductFeedApi;
import com.kikrlib.bean.ProductFeedItem;
import com.kikrlib.db.HelpPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ProductFeedRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.personagraph.api.PGAgent;

import java.util.ArrayList;
import java.util.List;

public class FragmentDiscover extends BaseFragment implements OnClickListener, ServiceCallback {
    private View mainView;
    private ListView discoverList;
    public boolean isOpen = false;
    // private ProgressBarDialog mProgressBarDialog;
    int page = 0;
    private List<ProductFeedItem> product_list = new ArrayList<ProductFeedItem>();
    private HomeActivity homeActivity;
    private boolean isLoading = false;
    private boolean isFirstTime = true;
    private DiscoverAdapter discoverAdapter;
    private FragmentDiscover fragmentDiscover;
    private int firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;
    private View loaderView;
    private TextView loadingTextView;
    RelativeLayout layoutCustomize;
    FrameLayout customization_view;
    ImageView hide_customize_view;
    CustomizeFeedFragment customizeFeedFragment;
    ImageView imgDelete;

    public FragmentDiscover() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (CommonUtility.isOnline(mContext))
            PGAgent.logEvent("DISCOVER_SCREEN_OPEND");
        homeActivity = (HomeActivity) getActivity();
        fragmentDiscover = this;
        try {
            mainView = inflater.inflate(R.layout.fragment_discover, container, false);
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }

        return mainView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_customize:
                // startActivity(CustomizeFeedActivity.class);
                //addFragment(new CustomizeFeedFragment());
                customization_view.setVisibility(View.VISIBLE);
                CustomizeFeedFragment.customizeFeedFragment.initData();
                break;
            case R.id.imgDelete:
                customization_view.setVisibility(View.GONE);
                if (CustomizeFeedFragment.searchYourItemEditText != null)
                    CustomizeFeedFragment.searchYourItemEditText.setText("");
                break;

            default:
                return;


        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        hide_customize_view = (ImageView) mainView.findViewById(R.id.imgDelete);
        customization_view = (FrameLayout) mainView.findViewById(R.id.layout_customization);
        layoutCustomize = (RelativeLayout) mainView.findViewById(R.id.layout_customize);
        discoverList = (ListView) mainView.findViewById(R.id.discoverList);
        loaderView = View.inflate(mContext, R.layout.footer, null);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        customizeFeedFragment = (CustomizeFeedFragment) getFragmentManager().findFragmentById(R.id.interest_fragment);
        // imgDelete = (ImageView)mainView.findViewById(R.id.imgDelete);
    }

    public void initData() {
        if (checkInternet())
            getBrandAndProductList();
        else
            showReloadOption();

    }

    @Override
    public void setData(Bundle bundle) {
//        if (checkInternet())
//            getBrandAndProductList();
//        else
//            showReloadOption();

        discoverList.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                FragmentDiscover.this.firstVisibleItem = firstVisibleItem;
                FragmentDiscover.this.visibleItemCount = visibleItemCount;
                FragmentDiscover.this.totalItemCount = totalItemCount;
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (checkInternet2()) {
                        page++;
                        isFirstTime = false;
                        getBrandAndProductList();
                    } else {
                        showReloadFotter();
                    }
                }
            }
        });

//		if (HelpPreference.getInstance().getHelpPinMenu().equals("yes")) {
//			HelpPreference.getInstance().setHelpPinMenu("no");
//			HelpPressMenuDialog helpPressMenuDialog = new HelpPressMenuDialog(mContext);
//			helpPressMenuDialog.show();
//		}
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            if (HelpPreference.getInstance().getHelpPinMenu().equals("yes")) {
                HelpPreference.getInstance().setHelpPinMenu("no");
                HelpPressMenuDialog helpPressMenuDialog = new HelpPressMenuDialog(mContext);
                helpPressMenuDialog.show();
            }
        }
    }

    private void getBrandAndProductList() {

        isLoading = !isLoading;
        if (!isFirstTime) {
            showFotter();
        } else {
            loadingTextView.setVisibility(View.VISIBLE);

        }


        final ProductFeedApi listApi = new ProductFeedApi(this);
        listApi.getProductList(UserPreference.getInstance().getUserID(), String.valueOf(page));
        listApi.execute();

    }

    @Override
    public void handleOnSuccess(Object object) {
        try {
            if (!isFirstTime) {
                hideFotter();
            } else {
                loadingTextView.setVisibility(View.GONE);

            }
            hideDataNotFound();
            isLoading = !isLoading;
            Syso.info("In handleOnSuccess>>" + object);
            ProductFeedRes productAndBrandListRes = (ProductFeedRes) object;
            product_list.addAll(productAndBrandListRes.getData());
            homeActivity.list = product_list;
            if (productAndBrandListRes.getData().size() < 1) {
                isLoading = true;
            }
            if (product_list.size() == 0 && isFirstTime) {
                showDataNotFound();
            } else if (homeActivity.list.size() > 0 && isFirstTime) {
                discoverAdapter = new DiscoverAdapter(mContext, product_list, fragmentDiscover);
                discoverList.setAdapter(discoverAdapter);
            } else if (discoverAdapter != null) {
                discoverAdapter.notifyDataSetChanged();
            } else {
                discoverAdapter = new DiscoverAdapter(mContext, product_list, fragmentDiscover);
                discoverList.setAdapter(discoverAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (!isFirstTime) {
            discoverList.removeFooterView(loaderView);
        } else {
            loadingTextView.setVisibility(View.GONE);

//			mProgressBarDialog.dismiss();
        }
        isLoading = !isLoading;
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            ProductFeedRes response = (ProductFeedRes) object;
            AlertUtils.showToast(mContext, response.getMessage());
        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }


    @Override
    public void refreshData(Bundle bundle) {
    }

    @Override
    public void setClickListener() {
        layoutCustomize.setOnClickListener(this);
        hide_customize_view.setOnClickListener(this);
        //  imgDelete.setOnClickListener(this);
    }


    private void showReloadOption() {
        showDataNotFound();
        TextView textView = getDataNotFound();
        Syso.info("text view>>" + textView);
        if (textView != null) {
            Syso.info("12233 inside text view text view>>" + textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInternet())
                        getBrandAndProductList();
                    Syso.info("text view>>");
                }
            });
        }
    }

    protected void showReloadFotter() {
        TextView textView = getReloadFotter();
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkInternet()) {
                    page++;
                    isFirstTime = false;
                    getBrandAndProductList();
                }
            }
        });
    }

    public void refresh() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                ((HomeActivity) mContext).loadFragment(new FragmentDiscover());
            }
        };
        handler.postDelayed(runnable, 100);
    }

}

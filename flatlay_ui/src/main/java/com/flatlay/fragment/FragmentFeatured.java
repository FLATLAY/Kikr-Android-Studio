package com.flatlay.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.FeaturedTabAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.FeaturedTabApi;
import com.flatlaylib.bean.FeaturedTabData;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.FeaturedTabApiRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;




public class FragmentFeatured extends BaseFragment {
    private View mainView;
    private ListView featuredList;
    public boolean isOpen = false;
    private ProgressBarDialog mProgressBarDialog;
    int page = 0;
    private List<FeaturedTabData> product_list = new ArrayList<FeaturedTabData>();
    private boolean isLoading = false;
    private boolean isFirstTime = true;
    private FragmentFeatured fragmentFeatured;
    private FeaturedTabAdapter featuredTabAdapter;
    private int firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;
    private View loaderView;
    private TextView loadingTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_featured, null);
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        featuredList = (ListView) mainView.findViewById(R.id.featuredList);
        loaderView = View.inflate(mContext, R.layout.footer, null);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
    }

    public void initData() {
        if (checkInternet())
            //   System.out.print("jhghejhrf");
            getFeaturedTabData();
        else
            showReloadOption();
    }

    @Override
    public void setData(Bundle bundle) {
        //if(checkInternet())
        // System.out.print("jhghejhrf");
        //   getFeaturedTabData();
//		else
//			showReloadOption();

        featuredList.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,  int visibleItemCount, int totalItemCount) {
                FragmentFeatured.this.firstVisibleItem=firstVisibleItem;
                FragmentFeatured.this.visibleItemCount=visibleItemCount;
                FragmentFeatured.this.totalItemCount=totalItemCount;
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if(checkInternet2()){
                        page++;
                        isFirstTime=false;
                        getFeaturedTabData();
                    }else{
                        showReloadFotter();
                    }
                }
            }
        });
    }

    private void getFeaturedTabData() {

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
                        getFeaturedTabData();
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
                    getFeaturedTabData();
                }
            }
        });
    }

    @Override
    public void refreshData(Bundle bundle) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setClickListener() {
        // TODO Auto-generated method stub

    }

}
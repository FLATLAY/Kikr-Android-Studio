package com.flatlay.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.InspirationAdapter;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RachelDi on 2/7/18.
 */

public class AllInspirationSectionFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ServiceCallback {
    private View mainView;
    private ListView inspirationList;
    int page = 0;
    boolean isFirstTimeFromMain = false;
    private String userId;
    private ListView inspirationlist;
    private boolean isLoading = false;
    private boolean isFirstTime = true;
    private List<Inspiration> product_list = new ArrayList<Inspiration>();
    private int firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;
    private TextView loadingTextView;
    private boolean isViewAll;
    private InspirationAdapter inspirationAdapter;

    public AllInspirationSectionFragment() {
        isFirstTimeFromMain = true;
        this.isViewAll = true;
        this.userId = UserPreference.getInstance().getUserID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.all_inspection_fragment, null);
        return mainView;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        inspirationlist = (ListView) mainView.findViewById(R.id.inspirationlist);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        loadingTextView.setTypeface(FontUtility.setMontserratLight(getActivity()));
    }

    @Override
    public void setData(Bundle bundle) {
        if (isFirstTimeFromMain)
            initData();
        inspirationlist.setOnItemClickListener(this);
        inspirationlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                AllInspirationSectionFragment.this.firstVisibleItem = firstVisibleItem;
                AllInspirationSectionFragment.this.visibleItemCount = visibleItemCount;
                AllInspirationSectionFragment.this.totalItemCount = totalItemCount;
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (checkInternet2()) {
                        page++;
                        isFirstTime = false;
                        getInspirationFeedList();
                    }
                } else
                    showReloadFotter();

            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getInspirationFeedList();
        }
    }


    protected void showReloadFotter() {
        TextView textView = getReloadFotter();
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkInternet()) {
                    page++;
                    isFirstTime = false;
                    getInspirationFeedList();
                }
            }
        });
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {

    }

    public void initData() {
        if (checkInternet())
            getInspirationFeedList();
        else {
            showReloadOption();
        }
    }

    private void showReloadOption() {
        showDataNotFound();
        TextView textView = getDataNotFound();
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInternet())
                        getInspirationFeedList();
                }
            });
        }
    }

    private void getInspirationFeedList() {
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.getInspirationFeed(userId, isViewAll, String.valueOf(page), UserPreference.getInstance().getUserID());
        inspirationFeedApi.execute();
    }

    @Override
    public void handleOnSuccess(Object object) {
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.INVISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        hideDataNotFound();
        isLoading = !isLoading;
        InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
        product_list.addAll(inspirationFeedRes.getData());

        if (inspirationFeedRes.getData().size() < 10) {
            isLoading = true;
        }

        if (product_list.size() == 0 && isFirstTime) {
            showDataNotFound();
        }
        if (product_list.size() > 0 && isFirstTime) {
            inspirationlist.setAdapter(inspirationAdapter);
            inspirationAdapter.notifyDataSetChanged();

        } else if (inspirationAdapter != null) {
            inspirationAdapter.setData(product_list);
            inspirationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        loadingTextView.setVisibility(View.VISIBLE);
        isLoading = !isLoading;
        if (object != null) {
            InspirationFeedRes response = (InspirationFeedRes) object;
            AlertUtils.showToast(mContext, response.getMessage());
        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }
}

package com.flatlay.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.InspirationAdapter;
import com.flatlay.adapter.YouFollowingAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.kikrlib.bean.YouFollowingModel;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.ProfileCollectionList;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;

import java.util.ArrayList;
import java.util.List;

public class YoufollowingKikr extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ServiceCallback {
    private View mainView;
    RecyclerView recyclerView;
    private ListView inspirationList;
    private ProgressBarDialog mProgressBarDialog;
    int page = 0;
    String inspiration_id;
    String user_id;
    String viewer_id;
    private LinearLayout uploadButton;
    private LinearLayout kikrfallow;
    private List<Inspiration> product_list = new ArrayList<Inspiration>();
    private boolean isLoading = false;
    private boolean isFirstTime = true;
    private int firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;
    private View loaderView;
    private InspirationAdapter inspirationAdapter;
    private TextView loadingTextView;
    private boolean isViewAll;
    private String userId;
    FrameLayout create_collection_alert;

    private List<ProfileCollectionList> collectionLists = new ArrayList<ProfileCollectionList>();
    public static boolean isPostUpload = false;
    private final String hotel_image[] = {

            "http://api.learn2crack.com/android/images/jellybean.png",
            "http://api.learn2crack.com/android/images/kitkat.png",
            "http://api.learn2crack.com/android/images/lollipop.png",
            "http://api.learn2crack.com/android/images/marshmallow.png"

    };

    private final String hotel_price[] = {

            "3",
            "4",
            "6",
            "8",
            "9",
            "12",
            "13",
            "15",
            "23"

    };
    public YoufollowingKikr(boolean isViewAll, String userId) {

        this.isViewAll = isViewAll;
        this.userId = userId;


    }

    public YoufollowingKikr() {
        this.isViewAll = true;
        this.userId = UserPreference.getInstance().getUserID();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_youfollowing_kikr, null);
        return mainView;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }


    @Override
    public void initUI(Bundle savedInstanceState) {
        recyclerView = (RecyclerView) mainView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<YouFollowingModel> hotelListing = prepareData();
        YouFollowingAdapter adapter = new YouFollowingAdapter(mContext, hotelListing);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<YouFollowingModel> prepareData() {

        ArrayList<YouFollowingModel> hotel_listing = new ArrayList<>();
        for (int i = 0; i < hotel_image.length; i++) {
            YouFollowingModel hotelListing = new YouFollowingModel();
            hotelListing.setHotel_image(hotel_image[i]);
            hotelListing.setHotel_price(hotel_price[i]);
            hotel_listing.add(hotelListing);
        }
        return hotel_listing;

    }

    public void initData() {
        if (checkInternet())
            getInspirationFeedList();
        else
            showReloadOption();
    }

    @Override
    public void setData(Bundle bundle) {

    }


    private void getInspirationFeedList() {
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {

    }


    private void showReloadOption() {


    }

    protected void showReloadFotter() {

    }

    public void refresh() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                ((HomeActivity) mContext).loadFragment(new FragmentInspirationSection(true, UserPreference.getInstance().getUserID()));
            }
        };
        handler.postDelayed(runnable, 100);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getInspirationFeedList();
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        HomeActivity.menuTextCartCount.setVisibility(View.VISIBLE);
    }


    @Override
    public void handleOnSuccess(Object object) {

    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {

    }
}
package com.kikr.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.KikrFollowingAdapter;
import com.kikrlib.api.MessageCenterApi;
import com.kikrlib.bean.FollowingKikrModel;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tycho on 6/3/2016.
 */
public class FollowinginstagramScreen extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ServiceCallback {
    private View mainView;
    RecyclerView recyclerView;
    int page = 0;

    private List<Inspiration> product_list = new ArrayList<Inspiration>();
    private boolean isViewAll;
    private String userId;
    FrameLayout create_collection_alert;
    MessageCenterApi messageCenterApi;

    private List<FollowingKikrModel.DataBean> followinglist = new ArrayList<>();
    public static boolean isPostUpload = false;

    public FollowinginstagramScreen(boolean isViewAll, String userId) {

        this.isViewAll = isViewAll;
        this.userId = userId;


    }

    public FollowinginstagramScreen() {
        this.isViewAll = true;
        this.userId = UserPreference.getInstance().getUserID();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.kikrfollowingscreen, null);
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
        getFollowingInstagramList();

    }


    public void initData() {
        if (checkInternet())
            getFollowingInstagramList();
        else
            showReloadOption();
    }

    @Override
    public void setData(Bundle bundle) {

    }




    private void getFollowingInstagramList() {
        final MessageCenterApi messageCenterApi = new MessageCenterApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
//                if (mProgressBarDialog.isShowing())
//                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnSuccess>>" + object);

//                String jsonResponseString  =  R.string.notification_response;
//
            //    String mystring = getResources().getString(R.string.notification_response);

                FollowingKikrModel followingKikrModel = (FollowingKikrModel) object;
                followinglist.addAll(followingKikrModel.getData());
                followinglist.size();
                System.out.print("list"+followinglist);
               // ArrayList<FollowingKikrModel> followingList = prepareData();
                KikrFollowingAdapter adapter = new KikrFollowingAdapter(mContext, (ArrayList<FollowingKikrModel.DataBean>) followinglist);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

            }
        });

        messageCenterApi.followinginstagram("0", "1");
        messageCenterApi.execute();


    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {

    }


    private void showReloadOption() {
//        showDataNotFound();
//        TextView textView = getDataNotFound();
//        Syso.info("text view>>" + textView);

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
            getFollowingInstagramList();
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
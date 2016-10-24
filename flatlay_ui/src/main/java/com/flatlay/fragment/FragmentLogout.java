package com.flatlay.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.LogoutDialog;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlaylib.bean.FollowingKikrModel;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tycho on 6/25/2016.
 */
public class FragmentLogout extends BaseFragment implements View.OnClickListener, ServiceCallback
{
    private boolean firstTime = false;
    private View mainView;
    RecyclerView recyclerView;
    int page = 0;
    LogoutDialog logoutDialog;
    ProgressBarDialog progressBarDialog;
    HomeActivity homeActivity;

    private List<Inspiration> product_list = new ArrayList<Inspiration>();
    private boolean isViewAll;
    private String userId;

    private List<FollowingKikrModel.DataBean> followinglist = new ArrayList<>();
    public static boolean isPostUpload = false;

    public FragmentLogout(boolean isViewAll, String userId) {

        this.isViewAll = isViewAll;
        this.userId = userId;


    }

    public FragmentLogout() {
        this.isViewAll = true;
        HomeActivity.menuTextCartCount.setVisibility(View.INVISIBLE);
        this.userId = UserPreference.getInstance().getUserID();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        LogoutDialog logoutDialog = new LogoutDialog(mContext, homeActivity);
//        logoutDialog.show();


        HomeActivity.menuTextCartCount.setVisibility(View.INVISIBLE);
    ((HomeActivity) mContext).logoutscreen();
        mainView = inflater.inflate(R.layout.fragment_logout, null);
        return mainView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

    }


    public void initData() {
//        ((HomeActivity) mContext).logoutscreen();
    }

    @Override
    public void setData(Bundle bundle) {
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


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
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

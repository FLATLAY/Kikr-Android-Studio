package com.flatlay.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.Log;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.KikrFollowingAdapter;
import com.flatlaylib.api.MessageCenterApi;
import com.flatlaylib.bean.FollowingKikrModel;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;


public class FollowinginstagramScreen extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ServiceCallback {
    private View mainView;
    RecyclerView recyclerView;
    TextView loadingTextView;
    int page = 0;
    Button invite;
    private List<Inspiration> product_list = new ArrayList<Inspiration>();
    private boolean isViewAll;
    private String userId;
    FrameLayout create_collection_alert;
    MessageCenterApi messageCenterApi;
    RelativeLayout followerNotFound;
    private List<FollowingKikrModel.DataBean> followinglist = new ArrayList<>();
    private List<FollowingKikrModel.DataBean> followinglistRefined = new ArrayList<>();
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
            case R.id.invite:
            ((HomeActivity)mContext).inviteFriends();
            break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        followerNotFound=(RelativeLayout)mainView.findViewById(R.id.followerNotFound);
        invite=(Button)mainView.findViewById(R.id.invite);
       recyclerView = (RecyclerView) mainView.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        loadingTextView.setVisibility(View.VISIBLE);
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
                Log.w("FIScreen","getFollowingInstagramList() Success");
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
                if (followinglist.size() == 0) {
                    followerNotFound.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    loadingTextView.setVisibility(View.GONE);
                }
                else {
                    followerNotFound.setVisibility(View.GONE);
                    loadingTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (int i = 0; i < followinglist.size(); i++) {
                        Log.w("Here: ",followinglist.get(i).getMessage() + UserPreference.getInstance().getUserName());

                        String userString = followinglist.get(i).getMessage();
                        String notificationType = followinglist.get(i).getType();
                        String userName = "";
                        if(userString.contains("commented") && notificationType.equals("commentinsp"))
                        {
                            userName = userString.split(" commented")[0];
                        }
                        else if(userString.contains("liked") && notificationType.equals("likeinsp"))
                        {
                            userName = userString.split(" liked")[0];
                        }
                        else if(userString.contains("following") && notificationType.equals("follow"))
                        {
                            userName = userString.split(" is following")[0];
                        }

                        if(!userName.equals(UserPreference.getInstance().getUserName()))
                        {
                            followinglistRefined.add(followinglist.get(i));
                        }
                    }
                    KikrFollowingAdapter adapter = new KikrFollowingAdapter(mContext, (ArrayList<FollowingKikrModel.DataBean>) followinglistRefined);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                loadingTextView.setVisibility(View.GONE);

            }
        });
        Log.w("FIScreen","getFollowingInstagramList() ");
        messageCenterApi.followinginstagram("0", "1");
        messageCenterApi.execute();


    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        invite.setOnClickListener(this);


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
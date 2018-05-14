package com.flatlay.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.KikrFollowingAdapter;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.api.MessageCenterApi;
import com.flatlaylib.bean.FollowingKikrModel;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RachelDi on 4/27/18.
 */

public class NotificationListFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private TextView notification_text, message_text;
    private ImageView infoicon2;
    private MyMaterialContentOverflow3 overflow2;
    private boolean isOnFeedPage = true;
    private List<FollowingKikrModel.DataBean> followinglist = new ArrayList<>(), followinglistRefined = new ArrayList<>();
    private RecyclerView notificationlist;

    public NotificationListFragment(MyMaterialContentOverflow3 overflow2, boolean isOnFeedPage) {
        this.overflow2 = overflow2;
        this.isOnFeedPage = isOnFeedPage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.notification_list_layout, null);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infoicon2:
                ((HomeActivity) mContext).myAddFragment(new MyFeedCollectionFragment(overflow2, false, isOnFeedPage));
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        notification_text = (TextView) mainView.findViewById(R.id.notification_text);
        notification_text.setTypeface(FontUtility.setMontserratLight(mContext));
        message_text = (TextView) mainView.findViewById(R.id.message_text);
        message_text.setTypeface(FontUtility.setMontserratLight(mContext));
        infoicon2 = (ImageView) mainView.findViewById(R.id.infoicon2);
        notificationlist = (RecyclerView) mainView.findViewById(R.id.notificationlist);
        notificationlist.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        notificationlist.setLayoutManager(layoutManager);
    }

    @Override
    public void setData(Bundle bundle) {
        getFollowingInstagramList();
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        infoicon2.setOnClickListener(this);

    }

    private void getFollowingInstagramList() {
        final MessageCenterApi messageCenterApi = new MessageCenterApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                FollowingKikrModel followingKikrModel = (FollowingKikrModel) object;
                followinglist = followingKikrModel.getData();
                followinglist.size();
                followinglistRefined.clear();
                if (followinglist.size() == 0) {
                } else {
                    notificationlist.setVisibility(View.VISIBLE);
                    for (int i = 0; i < followinglist.size(); i++) {
                        String userString = followinglist.get(i).getMessage();
                        String notificationType = followinglist.get(i).getType();
                        String userName = "";
                        if (userString.contains("commented") && notificationType.equals("commentinsp"))
                            userName = userString.split(" commented")[0];
                        else if (userString.contains("liked") && notificationType.equals("likeinsp"))
                            userName = userString.split(" liked")[0];
                        else if (userString.contains("following") && notificationType.equals("follow"))
                            userName = userString.split(" is following")[0];
                        if (!userName.equals(UserPreference.getInstance().getUserName()))
                            followinglistRefined.add(followinglist.get(i));

                    }
                    KikrFollowingAdapter adapter = new KikrFollowingAdapter(mContext, (ArrayList<FollowingKikrModel.DataBean>) followinglistRefined);
                    notificationlist.setAdapter(adapter);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        messageCenterApi.followinginstagram("0", "1");
        messageCenterApi.execute();
    }
}

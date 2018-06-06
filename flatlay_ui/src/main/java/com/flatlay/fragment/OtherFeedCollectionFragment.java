package com.flatlay.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.FragmentProfileCollectionAdapter;
import com.flatlay.adapter.InspirationGridAdapter;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.FollowerList;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RachelDi on 4/27/18.
 */

public class OtherFeedCollectionFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private CircleImageView other_profile_pic;
    private String other_user_id, other_user_name, image, otherUserId = "";
    private GridView other_imagesList;
    private MyMaterialContentOverflow3 overflow2;
    private TextView other_nameText, other_followertext1, other_followingtext1, other_followertext2, other_followingtext2;
    private Button other_button11, other_button22;
    private ListView other_collectionList;
    private InspirationGridAdapter other_inspirationGridAdapter;
    private boolean isother_Loading_feed = false, is_other_FirstTime_feed = true, is_other_FirstTime_coll = true;
    private List<Inspiration> other_product_list = new ArrayList<Inspiration>();
    private List<FollowerList> other_followersLists = new ArrayList<FollowerList>(), other_followingLists = new ArrayList<FollowerList>();
    private int other_firstVisibleItem = 0, other_visibleItemCount = 0, other_totalItemCount = 0, page7 = 0;
    private FragmentProfileCollectionAdapter other_collectionAdapter;
    private List<CollectionList> other_collectionLists2;
    private FragmentProfileView fragmentProfileView;

    public OtherFeedCollectionFragment(MyMaterialContentOverflow3 overflow2, String other_user_id, String other_user_name, String image) {
        this.other_user_id = other_user_id;
        this.other_user_name = other_user_name;
        this.image = image;
        this.overflow2 = overflow2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.other_feed_collection, container,false);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.other_button11:
                overflow2.setOpen();
                other_button22.setTextColor(Color.BLACK);
                other_button11.setTextColor(Color.WHITE);
                other_button22.setBackgroundResource(R.drawable.white_button_noborder);
                other_button11.setBackgroundResource(R.drawable.green_corner_button);
                other_imagesList.setVisibility(View.VISIBLE);
                other_collectionList.setVisibility(View.GONE);
                break;
            case R.id.other_button22:
                overflow2.setOpen();
                other_button11.setTextColor(Color.BLACK);
                other_button22.setTextColor(Color.WHITE);
                other_button11.setBackgroundResource(R.drawable.white_button_noborder);
                other_button22.setBackgroundResource(R.drawable.green_corner_button);
                other_collectionList.setVisibility(View.VISIBLE);
                other_imagesList.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        other_profile_pic = (CircleImageView) mainView.findViewById(R.id.other_profile_pic);
        other_imagesList = (GridView) mainView.findViewById(R.id.other_imagesList);
        other_nameText = (TextView) mainView.findViewById(R.id.other_nameText);
        other_nameText.setTypeface(FontUtility.setMontserratLight(mContext));
        other_button11 = (Button) mainView.findViewById(R.id.other_button11);
        other_button11.setTypeface(FontUtility.setMontserratLight(mContext));
        other_button22 = (Button) mainView.findViewById(R.id.other_button22);
        other_button22.setTypeface(FontUtility.setMontserratLight(mContext));
        other_button11.setTextColor(Color.WHITE);
        other_button11.setBackgroundResource(R.drawable.green_corner_button);
        other_collectionList = (ListView) mainView.findViewById(R.id.other_collectionList);
        other_followertext1 = (TextView) mainView.findViewById(R.id.other_followertext1);
        other_followertext1.setTypeface(FontUtility.setMontserratLight(mContext));
        other_followingtext1 = (TextView) mainView.findViewById(R.id.other_followingtext1);
        other_followingtext1.setTypeface(FontUtility.setMontserratLight(mContext));
        other_followertext2 = (TextView) mainView.findViewById(R.id.other_followertext2);
        other_followertext2.setTypeface(FontUtility.setMontserratLight(mContext));
        other_followingtext2 = (TextView) mainView.findViewById(R.id.other_followingtext2);
        other_followingtext2.setTypeface(FontUtility.setMontserratLight(mContext));
        other_imagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int other_firstVisibleItem, int other_visibleItemCount, int other_totalItemCount) {
                OtherFeedCollectionFragment.this.other_firstVisibleItem = other_firstVisibleItem;
                OtherFeedCollectionFragment.this.other_visibleItemCount = other_visibleItemCount;
                OtherFeedCollectionFragment.this.other_totalItemCount = other_totalItemCount;
                if (!isother_Loading_feed && other_firstVisibleItem + other_visibleItemCount == other_totalItemCount
                        && other_totalItemCount != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page7++;
                        is_other_FirstTime_feed = false;
                        getOtherInspirationFeedList(otherUserId);
                    } else {
                    }
                }
            }
        });

    }

    @Override
    public void setData(Bundle bundle) {
        if (!other_user_id.equals(otherUserId)) {
            otherUserId = other_user_id;
            is_other_FirstTime_feed = true;
            isother_Loading_feed = false;
            page7 = 0;
            other_product_list.clear();
            CommonUtility.setImage(mContext, image, other_profile_pic, R.drawable.profile_icon);
            other_nameText.setText(other_user_name);
            other_button22.setTextColor(Color.BLACK);
            other_button11.setTextColor(Color.WHITE);
            other_button22.setBackgroundResource(R.drawable.white_button_noborder);
            other_button11.setBackgroundResource(R.drawable.green_corner_button);
            getOtherInspirationFeedList(other_user_id);
            getOtherCollectionList(other_user_id);
            final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    MyProfileRes myProfileRes = (MyProfileRes) object;
                    other_followersLists = myProfileRes.getFollowers_list();
                    other_followingLists = myProfileRes.getFollowing_list();
                    other_followertext1.setText(String.valueOf(other_followingLists.size()));
                    other_followingtext1.setText(String.valueOf(other_followersLists.size()));
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {

                }
            });
            myProfileApi.getUserProfileDetail(other_user_id, UserPreference.getInstance().getUserID());
            myProfileApi.execute();
        }
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        other_button11.setOnClickListener(this);
        other_button22.setOnClickListener(this);

    }

    private void getOtherInspirationFeedList(String user_id) {
        isother_Loading_feed = !isother_Loading_feed;
        final InspirationFeedApi other_inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {

                isother_Loading_feed = !isother_Loading_feed;
                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;

                other_product_list.addAll(inspirationFeedRes.getData());
                if (inspirationFeedRes.getData().size() < 10) {

                    isother_Loading_feed = true;
                }

                if (other_product_list.size() > 0 && is_other_FirstTime_feed) {
                    other_inspirationGridAdapter = new InspirationGridAdapter(mContext, other_product_list, 0);
                    other_imagesList.setAdapter(other_inspirationGridAdapter);
                    other_imagesList.setVisibility(View.VISIBLE);
                } else if (other_inspirationGridAdapter != null) {
                    other_inspirationGridAdapter.setData(other_product_list);
                    other_inspirationGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                isother_Loading_feed = !isother_Loading_feed;
                if (object != null) {
                    InspirationFeedRes response = (InspirationFeedRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        other_inspirationFeedApi.getInspirationFeed(user_id, false, String.valueOf(page7), UserPreference.getInstance().getUserID());
        other_inspirationFeedApi.execute();
    }

    private void getOtherCollectionList(final String user_id) {
        final CollectionApi other_collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                other_collectionLists2 = collectionApiRes.getCollection();

                if (other_collectionLists2.size() > 0 && is_other_FirstTime_coll) {

                    other_collectionAdapter = new FragmentProfileCollectionAdapter(mContext,
                            other_collectionLists2, user_id,
                            fragmentProfileView, null, 0,
                            new FragmentProfileCollectionAdapter.ListAdapterListener() {
                                @Override
                                public void onClickAtOKButton(int position) {
                                }
                            });
                    other_collectionList.setAdapter(other_collectionAdapter);
                    other_collectionList.setVisibility(View.VISIBLE);
                } else if (other_collectionAdapter != null) {
                    other_collectionAdapter.setData(other_collectionLists2);
                    other_collectionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, collectionApiRes.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        other_collectionApi.getCollectionList(user_id);
        other_collectionApi.execute();
    }


}

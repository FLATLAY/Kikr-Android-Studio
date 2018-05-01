package com.flatlay.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.FragmentProfileCollectionAdapter;
import com.flatlay.adapter.InspirationGridAdapter;
import com.flatlay.adapter.KikrFollowingAdapter;
import com.flatlay.adapter.ProductDetailGridAdapter;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.api.MessageCenterApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.api.ProductBasedOnBrandApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.FollowerList;
import com.flatlaylib.bean.FollowingKikrModel;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.service.res.ProductBasedOnBrandRes;
import com.flatlaylib.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RachelDi on 4/26/18.
 */

public class MyFeedCollectionFragment extends BaseFragment implements View.OnClickListener {

    private View mainView;
    private ImageView info_image;
    private MyMaterialContentOverflow3 overflow2;
    private RelativeLayout bell_icon,info_icon;
    private TextView performanceText, nameText,followingtext1,followertext2,followertext1,followingtext2;
    private FragmentProfileCollectionAdapter collectionAdapter;
    private ListView collectionList;
    private List<UserData> userDetails;
    private List<Product> product_data;
    private List<Inspiration> myDetailProductList = new ArrayList<Inspiration>(),myProductList = new ArrayList<Inspiration>();
    private boolean isFirstTime_myCol = true,mycoll_isloading = false,myins_isloding = false,
            showPerformance = false, isOnFeedPage = true,isInfo=false,my_detail_ins_isloding = false,
            isFirstTime_mypos_detail = true,isFirstTime_mypos = true;
    private int collAdapterIndex = -1, feedAdapterIndex = -1, page3 = 0,firstVisibleItem2 = 0,
            visibleItemCount2 = 0, totalItemCount2 = 0,firstVisibleItem4 = 0, visibleItemCount4 = 0,
            totalItemCount4 = 0,page1 = 0;
    private FragmentProfileView fragmentProfileView;
    private Button button1, button2;
    private ProductDetailGridAdapter productDetailGridAdapter;
    private InspirationGridAdapter inspirationGridAdapter,inspirationGridAdapter2;
    private List<FollowerList> followersLists = new ArrayList<FollowerList>(),followingLists = new ArrayList<FollowerList>();
    private String userId=UserPreference.getInstance().getUserID();
    private List<CollectionList> collectionLists2;
    private GridView imagesList,feedDetail,productDetail;
    public MyFeedCollectionFragment(MyMaterialContentOverflow3 overflow2, boolean showPerformance, boolean isOnFeedPage) {
        this.overflow2 = overflow2;
        this.showPerformance = showPerformance;
        this.isOnFeedPage = isOnFeedPage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.my_feed_collection, null);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bell_icon:
                overflow2.setOpen();
                ((HomeActivity)mContext).myAddFragment(new NotificationListFragment(overflow2,isOnFeedPage));
                break;
            case R.id.button11:
                Log.e("button1","button1");
                overflow2.setOpen();
                isOnFeedPage = true;
                button2.setTextColor(Color.BLACK);
                button1.setTextColor(Color.WHITE);
                button2.setBackgroundResource(R.drawable.white_button_noborder);
                button1.setBackgroundResource(R.drawable.green_corner_button);
                if (isInfo) {
                    Log.e("button1","button1-1");
                    imagesList.setVisibility(View.GONE);
                    feedDetail.setVisibility(View.VISIBLE);
                } else {
                    Log.e("button1","button1-2");
                    imagesList.setVisibility(View.VISIBLE);
                    feedDetail.setVisibility(View.GONE);
                }
                collectionList.setVisibility(View.GONE);
                break;
            case R.id.button22:
                Log.e("button2","button2");
                overflow2.setOpen();
                isOnFeedPage = false;
                button1.setTextColor(Color.BLACK);
                button2.setTextColor(Color.WHITE);
                button1.setBackgroundResource(R.drawable.white_button_noborder);
                button2.setBackgroundResource(R.drawable.green_corner_button);
                collectionList.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
                break;
            case R.id.info_icon:
                overflow2.setOpen();
                ((HomeActivity)mContext).myAddFragment(new ProfileChoiceFragment(overflow2,isOnFeedPage));
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        Log.e("button1","button1-start");

        bell_icon = (RelativeLayout) mainView.findViewById(R.id.bell_icon);
        performanceText = (TextView) mainView.findViewById(R.id.performance_text);
        performanceText.setTypeface(FontUtility.setMontserratLight(mContext));
        nameText = (TextView) mainView.findViewById(R.id.nameText);
        nameText.setTypeface(FontUtility.setMontserratLight(mContext));
        nameText.setText(UserPreference.getInstance().getUserName());
        button1 = (Button) mainView.findViewById(R.id.button11);
        button1.setTypeface(FontUtility.setMontserratLight(mContext));
        button2 = (Button) mainView.findViewById(R.id.button22);
        button2.setTypeface(FontUtility.setMontserratLight(mContext));
        button1.setTextColor(Color.WHITE);
        button1.setBackgroundResource(R.drawable.green_corner_button);
        imagesList = (GridView) mainView.findViewById(R.id.imagesList);
        productDetail = (GridView) mainView.findViewById(R.id.productDetail);
        collectionList = (ListView) mainView.findViewById(R.id.collectionList);
        info_icon = (RelativeLayout) mainView.findViewById(R.id.info_icon);
        followingtext1 = (TextView) mainView.findViewById(R.id.followingtext1);
        followingtext1.setTypeface(FontUtility.setMontserratLight(mContext));
        followertext2 = (TextView) mainView.findViewById(R.id.followertext2);
        followertext2.setTypeface(FontUtility.setMontserratLight(mContext));
        followertext1 = (TextView) mainView.findViewById(R.id.followertext1);
        followertext1.setTypeface(FontUtility.setMontserratLight(mContext));
        followingtext2 = (TextView) mainView.findViewById(R.id.followingtext2);
        followingtext2.setTypeface(FontUtility.setMontserratLight(mContext));
        info_image = (ImageView) mainView.findViewById(R.id.info_image);
        if (showPerformance == true) {
            Log.e("button1","button1-true");

            info_image.setImageResource(R.drawable.info_teal);
            collAdapterIndex = 1;
            feedAdapterIndex = 1;
            performanceText.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.GONE);
            myDetailProductList.clear();
            page3 = 0;
        } else {
            Log.e("button1","button1-false");

            collAdapterIndex = 0;
            feedAdapterIndex = 0;
            nameText.setVisibility(View.VISIBLE);
            performanceText.setVisibility(View.GONE);
            info_image.setImageResource(R.drawable.info_white);

        }
        final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                MyProfileRes myProfileRes = (MyProfileRes) object;
                userDetails = myProfileRes.getUser_data();
                followersLists = myProfileRes.getFollowers_list();
                followingLists = myProfileRes.getFollowing_list();
                followingtext1.setText(String.valueOf(followersLists.size()));
                followertext1.setText(String.valueOf(followingLists.size()));
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        myProfileApi.getUserProfileDetail(UserPreference.getInstance().getUserID(),
                UserPreference.getInstance().getUserID());
        myProfileApi.execute();
        getMyInspirationFeedList();
        getCollectionList();
        feedDetail = (GridView) mainView.findViewById(R.id.feedDetail);
        feedDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem4, int visibleItemCount4, int totalItemCount4) {
                MyFeedCollectionFragment.this.firstVisibleItem4 = firstVisibleItem4;
                MyFeedCollectionFragment.this.visibleItemCount4 = visibleItemCount4;
                MyFeedCollectionFragment.this.totalItemCount4 = totalItemCount4;
                if (!my_detail_ins_isloding && firstVisibleItem4 + visibleItemCount4 == totalItemCount4
                        && totalItemCount4 != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page3++;
                        isFirstTime_mypos_detail = false;
                        getMyInspirationFeedList();
                    } else {
                    }
                }
            }
        });
        if (isOnFeedPage) {
            Log.e("button1","button1-on");

            imagesList.setVisibility(View.VISIBLE);
            collectionList.setVisibility(View.GONE);
            feedDetail.setVisibility(View.GONE);
            productDetail.setVisibility(View.GONE);
        } else {
            Log.e("button1","button1-not on");

            imagesList.setVisibility(View.GONE);
            collectionList.setVisibility(View.VISIBLE);
            feedDetail.setVisibility(View.GONE);
            productDetail.setVisibility(View.GONE);
        }

        imagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem2, int visibleItemCount2, int totalItemCount2) {
                MyFeedCollectionFragment.this.firstVisibleItem2 = firstVisibleItem2;
                MyFeedCollectionFragment.this.visibleItemCount2 = visibleItemCount2;
                MyFeedCollectionFragment.this.totalItemCount2 = totalItemCount2;
                if (!myins_isloding && firstVisibleItem2 + visibleItemCount2 == totalItemCount2
                        && totalItemCount2 != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page1++;
                        isFirstTime_mypos = false;
                        getMyInspirationFeedList();

                    } else {
                    }
                }
            }
        });
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        bell_icon.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        info_icon.setOnClickListener(this);
    }

    private void displayCollectionList() {
        if (collectionLists2.size() < 10) mycoll_isloading = true;
        if (collectionLists2.size() > 0 && isFirstTime_myCol) {
            Log.e("button1","button1-firstTimeColl");
            collectionAdapter = new FragmentProfileCollectionAdapter(mContext, collectionLists2,
                    UserPreference.getInstance().getUserID(), fragmentProfileView, null, collAdapterIndex, new FragmentProfileCollectionAdapter.ListAdapterListener() {
                @Override
                public void onClickAtOKButton(int position) {
                    final String coll_id = collectionLists2.get(position).getId();
                    imagesList.setVisibility(View.GONE);
                    collectionList.setVisibility(View.GONE);
                    feedDetail.setVisibility(View.GONE);
                    productDetail.setVisibility(View.VISIBLE);
                    final ProductBasedOnBrandApi productBasedOnBrandApi = new ProductBasedOnBrandApi(new ServiceCallback() {
                        @Override
                        public void handleOnSuccess(Object object) {
                            ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
                            product_data = productBasedOnBrandRes.getData();
                            productDetailGridAdapter = new ProductDetailGridAdapter(mContext, product_data, 0);
                            productDetail.setAdapter(productDetailGridAdapter);
                        }

                        @Override
                        public void handleOnFailure(ServiceException exception, Object object) {
                        }
                    });
                    productBasedOnBrandApi.getProductsBasedOnCollectionList(userId, String.valueOf(0), coll_id);
                    productBasedOnBrandApi.execute();
                }
            });
            if (overflow2.isOpen()) overflow2.setOpen();
            collectionList.setAdapter(collectionAdapter);
        } else if (collectionAdapter != null) {
            if (overflow2.isOpen()) overflow2.setOpen();
            collectionAdapter.setData(collectionLists2);
            if (overflow2.isOpen()) overflow2.setOpen();
            collectionAdapter.notifyDataSetChanged();
        }
    }

    private void getMyInspirationFeedList() {
        if (feedAdapterIndex == 0) myins_isloding = !myins_isloding;
        else my_detail_ins_isloding = !my_detail_ins_isloding;

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                Log.e("button1","button1-getMyInspirationFeedList");

                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
                if (feedAdapterIndex == 0) {
                    myins_isloding = !myins_isloding;
                    myProductList.addAll(inspirationFeedRes.getData());
                    if (inspirationFeedRes.getData().size() < 10) myins_isloding = true;
                } else {
                    my_detail_ins_isloding = !my_detail_ins_isloding;
                    myDetailProductList.addAll(inspirationFeedRes.getData());
                    if (myDetailProductList.size() < 10) my_detail_ins_isloding = true;
                }
                displayMyInspirationFeedList();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    if (feedAdapterIndex == 0) myins_isloding = !myins_isloding;
                    else my_detail_ins_isloding = !my_detail_ins_isloding;
                    InspirationFeedRes response = (InspirationFeedRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else AlertUtils.showToast(mContext, R.string.invalid_response);

            }
        });
        if (feedAdapterIndex == 0)
            inspirationFeedApi.getInspirationFeed(userId, false, String.valueOf(page1), userId);
        else inspirationFeedApi.getInspirationFeed(userId, false, String.valueOf(page3), userId);
        inspirationFeedApi.execute();
    }

    private void displayMyInspirationFeedList() {
        Log.e("button1","button1-displayMyInspirationFeedList");

        if (overflow2.isOpen()) overflow2.setOpen();
        if (feedAdapterIndex == 0) {
            if (myProductList.size() > 0 && isFirstTime_mypos) {
                inspirationGridAdapter = new InspirationGridAdapter(mContext, myProductList, 0);
                if (overflow2.isOpen()) overflow2.setOpen();
                imagesList.setAdapter(inspirationGridAdapter);
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setVisibility(View.GONE);
                imagesList.setVisibility(View.VISIBLE);
            } else if (inspirationGridAdapter != null) {
                if (overflow2.isOpen()) overflow2.setOpen();
                inspirationGridAdapter.setData(myProductList);
                if (overflow2.isOpen()) overflow2.setOpen();
                inspirationGridAdapter.notifyDataSetChanged();
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setVisibility(View.GONE);
                imagesList.setVisibility(View.VISIBLE);
            }
        } else if (feedAdapterIndex == 1) {
            Log.e("button1","button1-ll"+myDetailProductList.size());
            Log.e("button1","button1-tt"+isFirstTime_mypos_detail);

            if (myDetailProductList.size() > 0 && isFirstTime_mypos_detail) {
                Log.e("button1","button1-frirstTime");
                inspirationGridAdapter2 = new InspirationGridAdapter(mContext, myDetailProductList, 1);
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setAdapter(inspirationGridAdapter2);
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
            } else if (inspirationGridAdapter2 != null) {
                if (overflow2.isOpen()) overflow2.setOpen();
                inspirationGridAdapter2.setData(myDetailProductList);
                if (overflow2.isOpen()) overflow2.setOpen();
                inspirationGridAdapter2.notifyDataSetChanged();
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
            }
        }
    }

    private void getCollectionList() {
        if (overflow2.isOpen()) overflow2.setOpen();
        mycoll_isloading = !mycoll_isloading;
        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                collectionLists2 = collectionApiRes.getCollection();
                displayCollectionList();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mycoll_isloading = !mycoll_isloading;
                if (object != null) {
                    CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, collectionApiRes.getMessage());
                } else AlertUtils.showToast(mContext, R.string.invalid_response);
            }
        });
        collectionApi.getCollectionList(UserPreference.getInstance().getUserID());
        collectionApi.execute();
    }

}

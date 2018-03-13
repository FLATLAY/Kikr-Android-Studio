package com.flatlay.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flatlay.BaseActivityWithVideo;
import com.flatlay.BaseFragment;
import com.flatlay.KikrApp;
import com.flatlay.R;
import com.flatlay.activity.EditProfileActivity;
import com.flatlay.activity.FollowCategoriesNewActivity;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.FragmentProfileCollectionAdapter;
import com.flatlay.adapter.InspirationAdapter;
import com.flatlay.adapter.InspirationGridAdapter;
import com.flatlay.adapter.KikrFollowingAdapter;
import com.flatlay.adapter.ProductDetailGridAdapter;
import com.flatlay.dialog.ShareProfileDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.api.DeletePostApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.api.LogoutApi;
import com.flatlaylib.api.MessageCenterApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.api.ProductBasedOnBrandApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.FollowerList;
import com.flatlaylib.bean.FollowingKikrModel;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProfileCollectionList;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.service.res.ProductBasedOnBrandRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RachelDi on 1/22/18.
 */

public class FragmentInspirationSection extends BaseFragment
        implements View.OnClickListener, AdapterView.OnItemClickListener,
        ServiceCallback, MultiAutoCompleteTextView.OnEditorActionListener {
    private View mainView, loaderView;
    private ListView inspirationlist, collectionList;
    private RecyclerView notificationlist;
    private String inspiration_id, userId, user_id, viewer_id;
    private EditText searchText;
    private List<Inspiration> product_list = new ArrayList<Inspiration>(),
            myProductList = new ArrayList<Inspiration>(), myDetailProductList = new ArrayList<Inspiration>();
    private TextView followertext1, followingtext1, followertext2, followingtext2, notification_text,
            message_text;
    private boolean isLoading = false, myins_isloding = false, my_detail_ins_isloding = false, mycoll_isloading = false,
            isFirstTime = true, isFirstTimeFromMain = false, isFirstTime_mypos = true, isFirstTime_mypos_detail = true,
            isFirstTime_myCol = true, isOnFeedPage = true;
    private int page = 0, page1 = 0, page2 = 0, page3 = 0, index = 0, firstVisibleItem = 0,
            visibleItemCount = 0, totalItemCount = 0, firstVisibleItem2 = 0,
            visibleItemCount2 = 0, totalItemCount2 = 0, firstVisibleItem3 = 0,
            visibleItemCount3 = 0, totalItemCount3 = 0, firstVisibleItem4 = 0,
            visibleItemCount4 = 0, totalItemCount4 = 0, collAdapterIndex = -1, feedAdapterIndex = -1;
    private TextView loadingTextView, nameText, text1, text2, text3, text4, text5, performanceText;
    private boolean isViewAll, isInfo = false, isBell = false;
    private DeletePostApi deletePostApi;
    private List<ProfileCollectionList> collectionLists = new ArrayList<ProfileCollectionList>();
    private List<CollectionList> collectionLists2;
    public static boolean isPostUpload = false;
    private ImageView image_upload_post, image_upload_post2, image_camera, image_camera2,
            info_image, bell_image, info_image2, infoicon2;
    private RelativeLayout tab_layout1, bottomLayout, uploadChoice, bell_icon, info_icon,
            overflow_layout1, overflow_layout2, overflow_layout3;
    private MyMaterialContentOverflow3 overflow2;
    private CircleImageView profile_pic, prof2;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout fashioniconlayout, beautyiconlayout, fitnessiconlayout, foodiconlayout,
            techiconlayout, photographyiconlayoutlayout, homeiconlayout, occasioniconlayout,
            traveliconlayout, blanklayout, cart_tab, search_tab, profile_tab;
    private GridView imagesList, feedDetail, productDetail;
    private Button button1, button2;
    private InspirationAdapter inspirationAdapter;
    private FragmentProfileCollectionAdapter collectionAdapter;
    private FragmentProfileView fragmentProfileView;
    private InspirationGridAdapter inspirationGridAdapter, inspirationGridAdapter2;
    private ProductDetailGridAdapter productDetailGridAdapter;
    private List<FollowerList> followersLists = new ArrayList<FollowerList>(), followingLists = new ArrayList<FollowerList>();
    private List<UserData> userDetails;
    private List<Product> product_data;
    private List<FollowingKikrModel.DataBean> followinglist = new ArrayList<>();
    private List<FollowingKikrModel.DataBean> followinglistRefined = new ArrayList<>();
    private ShareProfileDialog shareProfileDialog;
//    public FragmentInspirationSection(boolean isViewAll, String userId) {
//        this.isViewAll = isViewAll;
//        this.userId = userId;
//        //FragmentDiscoverNew.isCreateCollection = false;
//        this.isFirstTimeFromMain = false;
//
//    }

//    public FragmentInspirationSection(boolean isViewAll, String userId, boolean isFirstTime) {
//        this.isFirstTimeFromMain = true;
//        this.isViewAll = isViewAll;
//        this.userId = userId;
//        //FragmentDiscoverNew.isCreateCollection = false;
//    }

    public FragmentInspirationSection() {

        this.isViewAll = true;
        isFirstTimeFromMain = true;
        this.userId = UserPreference.getInstance().getUserID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_inspiration_section, null);
        return mainView;
    }

    // Boolean clickLeft = false;

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    }

//    private void removeFocus() {
//        searchText.clearFocus();
//    }

    public void initData() {

        index = 0;
        if (((HomeActivity) mContext).checkInternet()) {
            getInspirationFeedList();
        }
        //displayFitnessFeed();
        else {
            showReloadOption();
        }
    }

    private void getInspirationFeedList() {
        Log.e("loading-ins-feed-1",""+isLoading);
        isLoading = !isLoading;
        Log.e("loading-ins-feed-2",""+isLoading);
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.getInspirationFeed(userId, isViewAll, String.valueOf(page), userId);
        inspirationFeedApi.execute();
    }

    private List<Inspiration> fashionList = new ArrayList<>();
    private List<Inspiration> beautyList = new ArrayList<>();
    private List<Inspiration> fitnessList = new ArrayList<>();
    private List<Inspiration> foodList = new ArrayList<>();
    private List<Inspiration> travelList = new ArrayList<>();
    private List<Inspiration> techList = new ArrayList<>();
    private List<Inspiration> photoList = new ArrayList<>();
    private List<Inspiration> homeList = new ArrayList<>();
    private List<Inspiration> occasioList = new ArrayList<>();
    private List<Inspiration> resultList = new ArrayList<>();

    List<Inspiration> currentList = new ArrayList<>();

    @Override
    public void handleOnSuccess(Object object) {

        if (!isFirstTime) {
            loadingTextView.setVisibility(View.INVISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        hideDataNotFound();
        Log.e("loading-feed-3",""+isLoading);
        isLoading = !isLoading;
        Log.e("loading-feed-4",""+isLoading);
        InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
        if (index == 0) {
            product_list.addAll(inspirationFeedRes.getData());
            currentList = product_list;
        } else if (index == 1) {
            fashionList.addAll(inspirationFeedRes.getData());
            currentList = fashionList;
        } else if (index == 2) {
            foodList.addAll(inspirationFeedRes.getData());
            currentList = foodList;
        } else if (index == 3) {
            travelList.addAll(inspirationFeedRes.getData());
            currentList = travelList;
        } else if (index == 4) {
            occasioList.addAll(inspirationFeedRes.getData());
            currentList = occasioList;
        } else if (index == 5) {
            fashionList.addAll(inspirationFeedRes.getData());
            currentList = homeList;
        } else if (index == 6) {
            photoList.addAll(inspirationFeedRes.getData());
            currentList = photoList;
        } else if (index == 7) {
            techList.addAll(inspirationFeedRes.getData());
            currentList = techList;
        } else if (index == 8) {
            fitnessList.addAll(inspirationFeedRes.getData());
            currentList = fitnessList;
        } else if (index == 9) {
            beautyList.addAll(inspirationFeedRes.getData());
            currentList = beautyList;
        } else if (index == 10) {
            resultList.addAll(inspirationFeedRes.getData());
            currentList = resultList;
        }
        if (inspirationFeedRes.getData().size() < 10) {
            isLoading = true;
            Log.e("loading-beauty-feed-5",""+isLoading);
        }
        if (currentList.size() == 0 && isFirstTime) {
            showDataNotFound();
        }
        if (currentList.size() > 0 && isFirstTime) {
            inspirationAdapter = new InspirationAdapter(mContext, currentList, isViewAll,
                    FragmentInspirationSection.this);
            inspirationlist.setAdapter(inspirationAdapter);
            inspirationAdapter.notifyDataSetChanged();
        } else if (inspirationAdapter != null) {
            inspirationAdapter.setData(currentList);
            inspirationAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (!isFirstTime) {
            inspirationlist.removeFooterView(loaderView);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        isLoading = !isLoading;
        if (object != null) {
            InspirationFeedRes response = (InspirationFeedRes) object;
            AlertUtils.showToast(mContext, response.getMessage());
        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    private void showReloadOption() {
        showDataNotFound();
        TextView textView = getDataNotFound();
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((HomeActivity) mContext).checkInternet())
                        getInspirationFeedList();
                }
            });
        }
    }

    protected void showReloadFotter() {
        TextView textView = getReloadFotter();
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((HomeActivity) mContext).checkInternet()) {
                    page++;
                    isFirstTime = false;
                    getInspirationFeedList();
                }
            }
        });
    }

//    public void refresh() {
//        Handler handler = new Handler();
//        Runnable runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                ((HomeActivity) mContext).loadFragment(new FragmentInspirationSection());
//            }
//        };
//        handler.postDelayed(runnable, 100);
//    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            Log.e("inspp","11");
//            getInspirationFeedList();
//        }
//    }

    @Override
    public void onResume() {

        // TODO Auto-generated method stub
        super.onResume();
//        Log.e("likeid++like count", "NOTIFY!!!!!!");
//        if (inspirationAdapter != null)
//            inspirationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onHiddenChanged(boolean hide){
        Log.e("likeid++like count", "hide -NOTIFY!!!!!!");

        if (inspirationAdapter != null)
            inspirationAdapter.notifyDataSetChanged();
    }

    public void removePost(final String inspiration_id, final String user_id) {
        this.inspiration_id = inspiration_id;
        this.user_id = user_id;

        deletePostApi = new DeletePostApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object.toString().equals(Constants.WebConstants.SUCCESS_CODE)) {
                    for (int i = 0; i < product_list.size(); i++) {
                        if (inspiration_id.equals(product_list.get(i).getInspiration_id())) {
                            product_list.remove(i);
                        }
                    }
                    inspirationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        deletePostApi.removePost(inspiration_id, user_id);
        deletePostApi.execute();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            CommonUtility.hideSoftKeyboard(mContext);
            searchText.setCursorVisible(false);
            if (!searchText.getText().toString().equals("")) {
                displaySearchResult();
            } else {
                Toast.makeText(mContext, "Please enter keywords", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.profile_pic:
//                Log.e("profile","profile");
//                overflow_layout3.setVisibility(View.GONE);
//                overflow_layout2.setVisibility(View.GONE);
//                overflow_layout1.setVisibility(View.VISIBLE);
//                imagesList.setVisibility(View.VISIBLE);
//                collectionList.setVisibility(View.GONE);
//                feedDetail.setVisibility(View.GONE);
//                productDetail.setVisibility(View.GONE);
//                break;

            case R.id.image_upload_post:
                bottomLayout.setBackgroundResource(R.drawable.topdrawer2);
                image_upload_post.setVisibility(View.GONE);
                image_camera.setVisibility(View.GONE);
                image_upload_post2.setVisibility(View.VISIBLE);
                uploadChoice.setVisibility(View.VISIBLE);
                break;

            case R.id.image_upload_post2:
                image_upload_post2.setVisibility(View.GONE);
                image_upload_post.setVisibility(View.VISIBLE);
                uploadChoice.setVisibility(View.INVISIBLE);
                image_camera.setVisibility(View.VISIBLE);
                bottomLayout.setBackgroundResource(0);
                break;

            case R.id.image_camera:
                image_camera.setVisibility(View.GONE);
                image_camera2.setVisibility(View.VISIBLE);
                break;

            case R.id.image_camera2:
                image_camera2.setVisibility(View.GONE);
                image_camera.setVisibility(View.VISIBLE);
                break;

            case R.id.fashioniconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                fashionList.clear();
                if (index != 1) {
                    fashioniconlayout.setBackgroundResource(R.drawable.tabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayFashionFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.beautyiconlayout:
                Log.e("loading-beauty-1",""+isLoading);
                isLoading = false;
                Log.e("loading-beauty-2",""+isLoading);

                inspirationlist.setSelection(0);
                page = 0;
                beautyList.clear();
                if (index != 9) {
                    beautyiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayBeautyFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.fitnessiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                fitnessList.clear();
                if (index != 8) {
                    fitnessiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayFitnessFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.infoicon2:
                overflow_layout3.setVisibility(View.GONE);
                overflow_layout2.setVisibility(View.GONE);
                overflow_layout1.setVisibility(View.VISIBLE);
                break;

            case R.id.techiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                techList.clear();
                if (index != 7) {
                    techiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayTechFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.photographyiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                photoList.clear();
                if (index != 6) {
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayPhotographyFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.homeiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                homeList.clear();
                if (index != 5) {
                    homeiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayHomeFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.occasioniconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                occasioList.clear();
                if (index != 4) {
                    occasioniconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayOccasionFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.traveliconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                travelList.clear();
                if (index != 3) {
                    traveliconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayTravelFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.foodiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                foodList.clear();
                if (index != 2) {
                    foodiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayFoodFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.bell_icon:
                overflow2.setOpen();
                overflow_layout1.setVisibility(View.GONE);
                overflow_layout2.setVisibility(View.GONE);
                overflow_layout3.setVisibility(View.VISIBLE);
                getFollowingInstagramList();
                break;

            case R.id.info_icon:
                overflow2.setOpen();
                overflow_layout1.setVisibility(View.GONE);
                overflow_layout3.setVisibility(View.GONE);
                overflow_layout2.setVisibility(View.VISIBLE);
                isInfo = true;
                break;

            case R.id.info_image2:
                overflow2.setOpen();
                overflow_layout2.setVisibility(View.GONE);
                overflow_layout3.setVisibility(View.GONE);
                overflow_layout1.setVisibility(View.VISIBLE);
                isInfo = false;
                break;

            case R.id.text1:
                overflow2.setOpen();
                shareProfileDialog.show();
                break;

            case R.id.text2:
                Bundle bundle = new Bundle();
//                bundle.putString("email",UserPreference.getInstance().getEmail());
//                bundle.putString("username",UserPreference.getInstance().getUserName());
//                bundle.putString("profilePic",UserPreference.getInstance().getProfilePic());
                bundle.putBoolean("is_edit_profile", true);
                startActivity(EditProfileActivity.class, bundle);
                break;

            case R.id.text3:
                overflow2.setOpen();
                info_image.setImageResource(R.drawable.info_teal);
                overflow_layout2.setVisibility(View.GONE);
                overflow_layout3.setVisibility(View.GONE);
                overflow_layout1.setVisibility(View.VISIBLE);
                collAdapterIndex = 1;
                feedAdapterIndex = 1;
                performanceText.setVisibility(View.VISIBLE);
                nameText.setVisibility(View.GONE);
                myDetailProductList.clear();
                page3 = 0;
                getMyInspirationFeedList();
                getCollectionList();
                isInfo = true;
                break;

            case R.id.text4:
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("isFromHome", true);
                startActivity(FollowCategoriesNewActivity.class, bundle2);
                break;

            case R.id.text5:
                logoutUser();
                startActivity(BaseActivityWithVideo.class);
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        shareProfileDialog = new ShareProfileDialog(mContext);
        tab_layout1 = (RelativeLayout) mainView.findViewById(R.id.tabLayout1);
        bell_icon = (RelativeLayout) mainView.findViewById(R.id.bell_icon);
        info_icon = (RelativeLayout) mainView.findViewById(R.id.info_icon);
        overflow_layout1 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout1);
        overflow_layout2 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout2);
        overflow_layout3 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout3);
        inspirationlist = (ListView) mainView.findViewById(R.id.inspirationlist);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        loadingTextView.setTypeface(FontUtility.setMontserratLight(getActivity()));
        image_upload_post = (ImageView) mainView.findViewById(R.id.image_upload_post);
        image_upload_post2 = (ImageView) mainView.findViewById(R.id.image_upload_post2);
        uploadChoice = (RelativeLayout) mainView.findViewById(R.id.uploadChoice);
        image_camera = (ImageView) mainView.findViewById(R.id.image_camera);
        image_camera2 = (ImageView) mainView.findViewById(R.id.image_camera);
        info_image = (ImageView) mainView.findViewById(R.id.info_image);
        bell_image = (ImageView) mainView.findViewById(R.id.bell_image);
        info_image2 = (ImageView) mainView.findViewById(R.id.info_image2);
        infoicon2 = (ImageView) mainView.findViewById(R.id.infoicon2);
        bottomLayout = (RelativeLayout) mainView.findViewById(R.id.bottomLayout);
        profile_tab = (LinearLayout) mainView.findViewById(R.id.profile_tab);
        profile_pic = (CircleImageView) mainView.findViewById(R.id.profile_pic);
        search_tab = (LinearLayout) mainView.findViewById(R.id.search_tab);
        searchText = (EditText) mainView.findViewById(R.id.search_tab_text);
        searchText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        searchText.setOnEditorActionListener(this);
        searchText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    searchText.setCursorVisible(true);
//                    overflow2.getFocus();
                }
                return false;
            }
        });

        nameText = (TextView) mainView.findViewById(R.id.nameText);
        nameText.setTypeface(FontUtility.setMontserratLight(mContext));
        performanceText = (TextView) mainView.findViewById(R.id.performance_text);
        performanceText.setTypeface(FontUtility.setMontserratLight(mContext));
        text1 = (TextView) mainView.findViewById(R.id.text1);
        text1.setTypeface(FontUtility.setMontserratLight(mContext));
        text2 = (TextView) mainView.findViewById(R.id.text2);
        text2.setTypeface(FontUtility.setMontserratLight(mContext));
        text3 = (TextView) mainView.findViewById(R.id.text3);
        text3.setTypeface(FontUtility.setMontserratLight(mContext));
        text4 = (TextView) mainView.findViewById(R.id.text4);
        text4.setTypeface(FontUtility.setMontserratLight(mContext));
//        text4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("hehe","hehe");
//                Bundle bundle2=new Bundle();
//                bundle2.putBoolean("isFromHome",true);
//                startActivity(FollowCategoriesNewActivity.class,bundle2);
//            }
//        });
        text5 = (TextView) mainView.findViewById(R.id.text5);
        text5.setTypeface(FontUtility.setMontserratLight(mContext));

        cart_tab = (LinearLayout) mainView.findViewById(R.id.cart_layout);
        fashioniconlayout = (LinearLayout) mainView.findViewById(R.id.fashioniconlayout);
        beautyiconlayout = (LinearLayout) mainView.findViewById(R.id.beautyiconlayout);
        fitnessiconlayout = (LinearLayout) mainView.findViewById(R.id.fitnessiconlayout);
        foodiconlayout = (LinearLayout) mainView.findViewById(R.id.foodiconlayout);
        techiconlayout = (LinearLayout) mainView.findViewById(R.id.techiconlayout);
        photographyiconlayoutlayout = (LinearLayout) mainView.findViewById(R.id.photographyiconlayout);
        homeiconlayout = (LinearLayout) mainView.findViewById(R.id.homeiconlayout);
        occasioniconlayout = (LinearLayout) mainView.findViewById(R.id.occasioniconlayout);
        traveliconlayout = (LinearLayout) mainView.findViewById(R.id.traveliconlayout);
        swipeLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_container);
        overflow2 = (MyMaterialContentOverflow3) mainView.findViewById(R.id.overflow2);
        Tracker t = ((KikrApp) mContext.getApplication()).getTracker(KikrApp.TrackerName.APP_TRACKER);
        t.setScreenName("Fragment Inspiration Section");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        followingtext1 = (TextView) mainView.findViewById(R.id.followingtext1);
        followingtext1.setTypeface(FontUtility.setMontserratLight(mContext));
        followertext1 = (TextView) mainView.findViewById(R.id.followertext1);
        followertext1.setTypeface(FontUtility.setMontserratLight(mContext));
        followingtext2 = (TextView) mainView.findViewById(R.id.followingtext2);
        followingtext2.setTypeface(FontUtility.setMontserratLight(mContext));
        notification_text = (TextView) mainView.findViewById(R.id.notification_text);
        notification_text.setTypeface(FontUtility.setMontserratLight(mContext));
        message_text = (TextView) mainView.findViewById(R.id.message_text);
        message_text.setTypeface(FontUtility.setMontserratLight(mContext));
        followertext2 = (TextView) mainView.findViewById(R.id.followertext2);
        followertext2.setTypeface(FontUtility.setMontserratLight(mContext));
        prof2 = (CircleImageView) mainView.findViewById(R.id.prof2);
        button1 = (Button) mainView.findViewById(R.id.button11);
        button1.setTypeface(FontUtility.setMontserratLight(mContext));
        button2 = (Button) mainView.findViewById(R.id.button22);
        button2.setTypeface(FontUtility.setMontserratLight(mContext));
        button1.setTextColor(Color.WHITE);
        button1.setBackgroundResource(R.drawable.green_corner_button);

        imagesList = (GridView) mainView.findViewById(R.id.imagesList);
        imagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem2, int visibleItemCount2,
                                 int totalItemCount2) {
                FragmentInspirationSection.this.firstVisibleItem2 = firstVisibleItem2;
                FragmentInspirationSection.this.visibleItemCount2 = visibleItemCount2;
                FragmentInspirationSection.this.totalItemCount2 = totalItemCount2;

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


        feedDetail = (GridView) mainView.findViewById(R.id.feedDetail);
        feedDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem4, int visibleItemCount4,
                                 int totalItemCount4) {
                FragmentInspirationSection.this.firstVisibleItem4 = firstVisibleItem4;
                FragmentInspirationSection.this.visibleItemCount4 = visibleItemCount4;
                FragmentInspirationSection.this.totalItemCount4 = totalItemCount4;
                if (!my_detail_ins_isloding && firstVisibleItem4 + visibleItemCount4 == totalItemCount4
                        && totalItemCount2 != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page3++;
                        isFirstTime_mypos_detail = false;
                        getMyInspirationFeedList();
                    } else {
                    }
                }
            }
        });

        productDetail = (GridView) mainView.findViewById(R.id.productDetail);

        nameText.setText(UserPreference.getInstance().getUserName());
        notificationlist = (RecyclerView) mainView.findViewById(R.id.notificationlist);
        notificationlist.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        notificationlist.setLayoutManager(layoutManager);
        collectionList = (ListView) mainView.findViewById(R.id.collectionList);
//        collectionList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem3, int visibleItemCount3,
//                                 int totalItemCount3) {
//
//                FragmentInspirationSection.this.firstVisibleItem3 = firstVisibleItem3;
//                FragmentInspirationSection.this.visibleItemCount3 = visibleItemCount3;
//                FragmentInspirationSection.this.totalItemCount3 = totalItemCount3;
//                if (!mycoll_isloading && firstVisibleItem3 + visibleItemCount3 == totalItemCount3
//                        && totalItemCount3 != 0) {
//
//                    if (((HomeActivity) mContext).checkInternet()) {
//                        page2++;
//                        isFirstTime_myCol = false;
//                        getCollectionList();
//                    } else {
//                    }
//                }
//            }
//        });

        if (((HomeActivity) mContext).checkInternet()) {
            collAdapterIndex = 0;
            feedAdapterIndex = 0;
            getMyInspirationFeedList();
            getCollectionList();
        }
    }

    private void displayFashionFeed() {
        index = 1;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "fashion,style,sexy");
        inspirationFeedApi.execute();
    }

    private void displayFoodFeed() {
        index = 2;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "dish,delicious,yum,food");
        inspirationFeedApi.execute();
    }

    private void displayTravelFeed() {
        index = 3;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "travel,travels,traveling");
        inspirationFeedApi.execute();
    }

    private void displayOccasionFeed() {
        index = 4;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page),
                "celebrate, friends, occasion");
        inspirationFeedApi.execute();
    }

    private void displayHomeFeed() {
        index = 5;

        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "home,furniture,kitchen");
        inspirationFeedApi.execute();
    }

    private void displayPhotographyFeed() {
        index = 6;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "camera,lens, photo");
        inspirationFeedApi.execute();
    }

    private void displayTechFeed() {
        index = 7;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page),
                "music,headphones,iphone,electronic");
        inspirationFeedApi.execute();
    }

    private void displayFitnessFeed() {
        index = 8;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page),
                "fitness,fitnessflatlay,fitnessfirl");
        inspirationFeedApi.execute();
    }

    private void displayBeautyFeed() {
        index = 9;
        Log.e("loading-beauty-feed-1",""+isLoading);
        isLoading = !isLoading;
        Log.e("loading-beauty-feed-1",""+isLoading);

        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "beauty,beautiful,makeup");
        inspirationFeedApi.execute();
    }

    private void displaySearchResult() {
        index = 10;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page),
                searchText.getText().toString().trim());
        inspirationFeedApi.execute();
    }

    @Override
    public void setClickListener() {
        image_upload_post.setOnClickListener(this);
        image_upload_post2.setOnClickListener(this);
        image_camera.setOnClickListener(this);
        tab_layout1.setOnClickListener(this);
        profile_tab.setOnClickListener(this);
        search_tab.setOnClickListener(this);
        cart_tab.setOnClickListener(this);
        fashioniconlayout.setOnClickListener(this);
        photographyiconlayoutlayout.setOnClickListener(this);
        traveliconlayout.setOnClickListener(this);
        beautyiconlayout.setOnClickListener(this);
        techiconlayout.setOnClickListener(this);
        homeiconlayout.setOnClickListener(this);
        info_icon.setOnClickListener(this);
        info_image2.setOnClickListener(this);
        bell_icon.setOnClickListener(this);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        text5.setOnClickListener(this);
        infoicon2.setOnClickListener(this);
        foodiconlayout.setOnClickListener(this);
        fitnessiconlayout.setOnClickListener(this);
        occasioniconlayout.setOnClickListener(this);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (overflow2.isOpen()) {
                    isInfo = false;
                    nameText.setVisibility(View.VISIBLE);
                    performanceText.setVisibility(View.GONE);
                    info_image.setImageResource(R.drawable.info_white);
                    collAdapterIndex = 0;
                    displayMyInspirationFeedList();
                    displayCollectionList();
                    overflow_layout3.setVisibility(View.GONE);
                    overflow_layout2.setVisibility(View.GONE);
                    overflow_layout1.setVisibility(View.VISIBLE);
                    if (isOnFeedPage) {
                        imagesList.setVisibility(View.VISIBLE);
                        collectionList.setVisibility(View.GONE);
                        feedDetail.setVisibility(View.GONE);
                        productDetail.setVisibility(View.GONE);
                    } else {
                        imagesList.setVisibility(View.GONE);
                        collectionList.setVisibility(View.VISIBLE);
                        feedDetail.setVisibility(View.GONE);
                        productDetail.setVisibility(View.GONE);
                    }
                } else
                    overflow2.triggerSlide();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overflow2.setOpen();
                isOnFeedPage = true;
                button2.setTextColor(Color.BLACK);
                button1.setTextColor(Color.WHITE);
                button2.setBackgroundResource(R.drawable.white_button_noborder);
                button1.setBackgroundResource(R.drawable.green_corner_button);
                if (isInfo) {
                    imagesList.setVisibility(View.GONE);
                    feedDetail.setVisibility(View.VISIBLE);
                } else {
                    imagesList.setVisibility(View.VISIBLE);
                    feedDetail.setVisibility(View.GONE);
                }
                collectionList.setVisibility(View.GONE);
//                getMyInspirationFeedList();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overflow2.setOpen();
                isOnFeedPage = false;
                button1.setTextColor(Color.BLACK);
                button2.setTextColor(Color.WHITE);
                button1.setBackgroundResource(R.drawable.white_button_noborder);
                button2.setBackgroundResource(R.drawable.green_corner_button);
                collectionList.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
//                getCollectionList();
            }
        });

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
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                FragmentInspirationSection.this.firstVisibleItem = firstVisibleItem;
                FragmentInspirationSection.this.visibleItemCount = visibleItemCount;
                FragmentInspirationSection.this.totalItemCount = totalItemCount;

                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    Log.e("loading-isloading",""+isLoading);
                    Log.e("loading-page",""+page);
                    if (((HomeActivity) mContext).checkInternet()) {
                        page++;
                        isFirstTime = false;
                        if (index == 0)
                            getInspirationFeedList();
                        else if (index == 1)
                            displayFashionFeed();
                        else if (index == 9)
                            displayBeautyFeed();
                        else if (index == 8)
                            displayFitnessFeed();
                        else if (index == 7)
                            displayTechFeed();
                        else if (index == 6)
                            displayPhotographyFeed();
                        else if (index == 5)
                            displayHomeFeed();
                        else if (index == 4)
                            displayOccasionFeed();
                        else if (index == 3)
                            displayTravelFeed();
                        else if (index == 2)
                            displayFoodFeed();

                    } else {
                        showReloadFotter();
                    }
                }
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingTextView.setVisibility(View.VISIBLE);
                getInspirationFeedList();
                if (swipeLayout.isRefreshing()) {
                    swipeLayout.setRefreshing(false);
                    loadingTextView.setVisibility(View.INVISIBLE);
                }

            }
        });
        CommonUtility.setImage(getActivity(), UserPreference.getInstance().getProfilePic(),
                profile_pic, R.drawable.profile_icon);
        CommonUtility.setImage(mContext, UserPreference.getInstance().getProfilePic(), prof2,
                R.drawable.profile_icon);

        final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                Log.e("ttttt", object.toString());
                MyProfileRes myProfileRes = (MyProfileRes) object;
                userDetails = myProfileRes.getUser_data();
                followersLists = myProfileRes.getFollowers_list();
                followingLists = myProfileRes.getFollowing_list();
                Log.e("ttttt", String.valueOf(followingLists.size()));

                followingtext1.setText(String.valueOf(followersLists.size()));
                followertext1.setText(String.valueOf(followingLists.size()));
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Log.e("ttttt", "fail");

            }
        });
        myProfileApi.getUserProfileDetail(UserPreference.getInstance().getUserID(), UserPreference.getInstance().getUserID());
        myProfileApi.execute();
    }

    private void getMyInspirationFeedList() {
        if (feedAdapterIndex == 0)
            myins_isloding = !myins_isloding;
        else
            my_detail_ins_isloding = !my_detail_ins_isloding;

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
                if (feedAdapterIndex == 0) {
                    myins_isloding = !myins_isloding;
                    myProductList.addAll(inspirationFeedRes.getData());
                } else {
                    my_detail_ins_isloding = !my_detail_ins_isloding;
                    myDetailProductList.addAll(inspirationFeedRes.getData());
                }

                displayMyInspirationFeedList();
//                if (myProductList.size() < 10) {
//                    myins_isloding = true;
//                }
//                if (feedAdapterIndex == 0) {
//                    if (myProductList.size() > 0 && isFirstTime_mypos) {
//                        inspirationGridAdapter = new InspirationGridAdapter(mContext, myProductList, 0);
//                        imagesList.setAdapter(inspirationGridAdapter);
//                        feedDetail.setVisibility(View.GONE);
//                        imagesList.setVisibility(View.VISIBLE);
//                    } else if (inspirationGridAdapter != null) {
//                        inspirationGridAdapter.setData(myProductList);
//                        inspirationGridAdapter.notifyDataSetChanged();
//                        feedDetail.setVisibility(View.GONE);
//                        imagesList.setVisibility(View.VISIBLE);
//                    }
//
//                } else if (feedAdapterIndex == 1) {
//                    Log.e("yaya", "yaya");
//                    if (myProductList.size() > 0 && isFirstTime_mypos) {
//                        inspirationGridAdapter = new InspirationGridAdapter(mContext, myProductList, 1);
//                        feedDetail.setAdapter(inspirationGridAdapter);
//                        feedDetail.setVisibility(View.VISIBLE);
//                        imagesList.setVisibility(View.GONE);
//                    } else if (inspirationGridAdapter != null) {
//                        inspirationGridAdapter.setData(myProductList);
//                        inspirationGridAdapter.notifyDataSetChanged();
//                        feedDetail.setVisibility(View.VISIBLE);
//                        imagesList.setVisibility(View.GONE);
//                    }
//                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    if (feedAdapterIndex == 0)
                        myins_isloding = !myins_isloding;
                    else
                        my_detail_ins_isloding = !my_detail_ins_isloding;

                    InspirationFeedRes response = (InspirationFeedRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
//        inspirationFeedApi.getInspirationFeed(UserPreference.getInstance().getUserID(), false, String.valueOf(page1), UserPreference.getInstance().getUserID());
        if (feedAdapterIndex == 0)
            inspirationFeedApi.getInspirationFeed(userId, false, String.valueOf(page1), userId);
        else
            inspirationFeedApi.getInspirationFeed(userId, false, String.valueOf(page3), userId);

        inspirationFeedApi.execute();
    }

    private void getCollectionList() {
        mycoll_isloading = !mycoll_isloading;
        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                collectionLists2 = collectionApiRes.getCollection();
                displayCollectionList();
//                if (collectionLists2.size() < 10) {
//                    mycoll_isloading = true;
//                }
//                if (collectionLists2.size() > 0 && isFirstTime_myCol) {
//                    collectionAdapter = new FragmentProfileCollectionAdapter(mContext,
//                            collectionLists2, userId, fragmentProfileView, null, collAdapterIndex,
//                            new FragmentProfileCollectionAdapter.ListAdapterListener() {
//                                @Override
//                                public void onClickAtOKButton(int position) {
//                                    final String coll_id = collectionLists2.get(position).getId();
//                                    imagesList.setVisibility(View.GONE);
//                                    collectionList.setVisibility(View.GONE);
//                                    feedDetail.setVisibility(View.GONE);
//                                    productDetail.setVisibility(View.VISIBLE);
//                                    final ProductBasedOnBrandApi productBasedOnBrandApi = new ProductBasedOnBrandApi(new ServiceCallback() {
//                                        @Override
//                                        public void handleOnSuccess(Object object) {
//                                            ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
//                                            product_data = productBasedOnBrandRes.getData();
//                                            productDetailGridAdapter = new ProductDetailGridAdapter(mContext, product_data);
//                                            productDetail.setAdapter(productDetailGridAdapter);
//                                        }
//
//                                        @Override
//                                        public void handleOnFailure(ServiceException exception, Object object) {
//
//                                        }
//                                    });
//                                    productBasedOnBrandApi.getProductsBasedOnCollectionList
//                                            (UserPreference.getInstance().getUserID(),
//                                                    String.valueOf(0), coll_id);
//                                    productBasedOnBrandApi.execute();
//                                }
//                            });
//                    collectionList.setAdapter(collectionAdapter);
////                    collectionList.setVisibility(View.VISIBLE);
//                } else if (collectionAdapter != null) {
//                    collectionAdapter.setData(collectionLists2);
//                    collectionAdapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mycoll_isloading = !mycoll_isloading;
                if (object != null) {
                    CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, collectionApiRes.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });

        collectionApi.getCollectionList(UserPreference.getInstance().getUserID());
        collectionApi.execute();


//        final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                mycoll_isloading = !mycoll_isloading;
//                MyProfileRes myProfileRes = (MyProfileRes) object;
//                collectionLists = myProfileRes.getCollection_list();
////                collectionLists.addAll(collectionLists);
//                if (collectionLists.size() < 10) {
//                    mycoll_isloading = true;
//                }
//                if (collectionLists.size() > 0 && isFirstTime_myCol) {
//                    Log.e("listttt","1");
//                    collectionAdapter = new FragmentProfileCollectionAdapter(mContext,
//                            collectionLists, userId, fragmentProfileView, null,collAdapterIndex,
//                            new FragmentProfileCollectionAdapter.ListAdapterListener() {
//                        @Override
//                        public void onClickAtOKButton(int position) {
//                            Log.e("listttt","2");
//                            imagesList.setVisibility(View.GONE);
//                            collectionList.setVisibility(View.GONE);
//                            feedDetail.setVisibility(View.GONE);
//                            productDetail.setVisibility(View.VISIBLE);
////                            showProductDetail(collectionLists.get(position));
//                        }
//                    });
//                    collectionList.setAdapter(collectionAdapter);
//                    collectionList.setVisibility(View.VISIBLE);
//                } else if (collectionAdapter != null) {
//                    collectionAdapter.setData(collectionLists);
//                    collectionAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                mycoll_isloading = !mycoll_isloading;
//                if (object != null) {
//                    MyProfileRes myProfileRes = (MyProfileRes) object;
//                    AlertUtils.showToast(mContext, myProfileRes.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//        });
//        myProfileApi.getUserProfileDetail(userId, userId);
//        myProfileApi.execute();
    }


    private void logoutUser() {
        LogoutApi logoutApi = new LogoutApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

            }
        });
        logoutApi.logoutUser(UserPreference.getInstance().getUserID(),
                CommonUtility.getDeviceTocken(mContext));
        logoutApi.execute();
    }

    private void getFollowingInstagramList() {

        final MessageCenterApi messageCenterApi = new MessageCenterApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Log.e("success", "success");
                FollowingKikrModel followingKikrModel = (FollowingKikrModel) object;
                followinglist = followingKikrModel.getData();
                followinglist.size();
                Log.e("success", "11--" + followinglist.size());
                followinglistRefined.clear();
                // ArrayList<FollowingKikrModel> followingList = prepareData();
                if (followinglist.size() == 0) {
//                    followerNotFound.setVisibility(View.VISIBLE);
//                    notificationlist.setVisibility(View.GONE);
//                    loadingTextView.setVisibility(View.GONE);
                } else {
//                    followerNotFound.setVisibility(View.GONE);
//                    loadingTextView.setVisibility(View.GONE);
                    overflow_layout3.setVisibility(View.VISIBLE);
                    overflow_layout1.setVisibility(View.GONE);
                    overflow_layout2.setVisibility(View.GONE);
                    notificationlist.setVisibility(View.VISIBLE);
                    for (int i = 0; i < followinglist.size(); i++) {
//                        Log.w("Here: ", followinglist.get(i).getMessage() + UserPreference.getInstance().getUserName());
                        //   new FirebaseMsgService().sendNotification2("hello","yes");
                        String userString = followinglist.get(i).getMessage();
                        String notificationType = followinglist.get(i).getType();
                        String userName = "";
                        if (userString.contains("commented") && notificationType.equals("commentinsp")) {
                            userName = userString.split(" commented")[0];
                        } else if (userString.contains("liked") && notificationType.equals("likeinsp")) {
                            userName = userString.split(" liked")[0];
                        } else if (userString.contains("following") && notificationType.equals("follow")) {
                            userName = userString.split(" is following")[0];
                        }

                        Log.e("success", "44--" + userName);


                        if (!userName.equals(UserPreference.getInstance().getUserName())) {
                            Log.e("success", "33--" + userName);
                            followinglistRefined.add(followinglist.get(i));
                        }
                    }
                    Log.e("success", "22--" + followinglistRefined.size());
                    KikrFollowingAdapter adapter = new KikrFollowingAdapter(mContext,
                            (ArrayList<FollowingKikrModel.DataBean>) followinglistRefined);
                    notificationlist.setAdapter(adapter);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//                loadingTextView.setVisibility(View.GONE);
                Log.e("success", "fail");

            }
        });
        messageCenterApi.followinginstagram("0", "1");
//        messageCenterApi.followinginstagram("0", UserPreference.getInstance().getUserID());
        messageCenterApi.execute();
    }


    private void displayMyInspirationFeedList() {
        if (feedAdapterIndex == 0 && myProductList.size() < 10)
            myins_isloding = true;
        else if (feedAdapterIndex == 1 && myDetailProductList.size() < 10)
            my_detail_ins_isloding = true;

        if (feedAdapterIndex == 0) {
            if (myProductList.size() > 0 && isFirstTime_mypos) {
                inspirationGridAdapter = new InspirationGridAdapter(mContext, myProductList, 0);
                imagesList.setAdapter(inspirationGridAdapter);
                feedDetail.setVisibility(View.GONE);
                imagesList.setVisibility(View.VISIBLE);
            } else if (inspirationGridAdapter != null) {
                inspirationGridAdapter.setData(myProductList);
                inspirationGridAdapter.notifyDataSetChanged();
                feedDetail.setVisibility(View.GONE);
                imagesList.setVisibility(View.VISIBLE);
            }

        } else if (feedAdapterIndex == 1) {
            Log.e("myDetailProductList", "" + myDetailProductList.size());
            if (myDetailProductList.size() > 0 && isFirstTime_mypos_detail) {
                Log.e("myDetailProductList", "yes");
                inspirationGridAdapter2 = new InspirationGridAdapter(mContext, myDetailProductList, 1);
                feedDetail.setAdapter(inspirationGridAdapter2);
                feedDetail.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
            } else if (inspirationGridAdapter2 != null) {
                Log.e("myDetailProductList", "no");
                inspirationGridAdapter2.setData(myDetailProductList);
                inspirationGridAdapter2.notifyDataSetChanged();
                feedDetail.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
            }
        }
    }

    private void displayCollectionList() {
        if (collectionLists2.size() < 10) {
            mycoll_isloading = true;
        }
        if (collectionLists2.size() > 0 && isFirstTime_myCol) {
            collectionAdapter = new FragmentProfileCollectionAdapter(mContext,
                    collectionLists2, userId, fragmentProfileView, null, collAdapterIndex,
                    new FragmentProfileCollectionAdapter.ListAdapterListener() {
                        @Override
                        public void onClickAtOKButton(int position) {
                            final String coll_id = collectionLists2.get(position).getId();
                            imagesList.setVisibility(View.GONE);
                            collectionList.setVisibility(View.GONE);
                            feedDetail.setVisibility(View.GONE);
                            productDetail.setVisibility(View.VISIBLE);
                            final ProductBasedOnBrandApi productBasedOnBrandApi =
                                    new ProductBasedOnBrandApi(new ServiceCallback() {
                                        @Override
                                        public void handleOnSuccess(Object object) {
                                            ProductBasedOnBrandRes productBasedOnBrandRes
                                                    = (ProductBasedOnBrandRes) object;
                                            product_data = productBasedOnBrandRes.getData();
                                            productDetailGridAdapter =
                                                    new ProductDetailGridAdapter(mContext, product_data, 0);
                                            productDetail.setAdapter(productDetailGridAdapter);
                                        }

                                        @Override
                                        public void handleOnFailure(ServiceException exception, Object object) {

                                        }
                                    });
                            productBasedOnBrandApi.getProductsBasedOnCollectionList
                                    (UserPreference.getInstance().getUserID(),
                                            String.valueOf(0), coll_id);
                            productBasedOnBrandApi.execute();
                        }
                    });
            collectionList.setAdapter(collectionAdapter);
//                    collectionList.setVisibility(View.VISIBLE);
        } else if (collectionAdapter != null) {
            collectionAdapter.setData(collectionLists2);
            collectionAdapter.notifyDataSetChanged();
        }
    }
}
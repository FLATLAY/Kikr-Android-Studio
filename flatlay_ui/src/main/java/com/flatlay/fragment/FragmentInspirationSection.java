package com.flatlay.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.KikrApp;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.InspirationAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.DeletePostApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.ProfileCollectionList;
import com.flatlaylib.db.HelpPreference;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.Syso;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RachelDi on 1/22/18.
 */

public class FragmentInspirationSection extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ServiceCallback {
    private View mainView;
    private ListView inspirationList;
    private ProgressBarDialog mProgressBarDialog;
    int page = 0;
    String inspiration_id;
    String user_id;
    String viewer_id;
    private LinearLayout uploadButton;
    private RelativeLayout bottomLayout;
    private RelativeLayout uploadChoice;
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
    ImageView imgDelete;
    Button create_my_collection;
    DeletePostApi deletePostApi;
    private List<ProfileCollectionList> collectionLists = new ArrayList<ProfileCollectionList>();
    public static boolean isPostUpload = false;
    boolean isFirstTimeFromMain = false;
    private ImageView image_upload_post;
    private ImageView image_upload_post2;
    private ImageView image_camera;
    RelativeLayout tab_layout1;
    LinearLayout cart_tab, search_tab, profile_tab;
    private CircleImageView profile_pic;
    private LinearLayout fashioniconlayout, beautyiconlayout, fitnessiconlayout, foodiconlayout, techiconlayout, photographyiconlayoutlayout, homeiconlayout, occasioniconlayout, traveliconlayout;

    public FragmentInspirationSection(boolean isViewAll, String userId) {

        this.isViewAll = isViewAll;
        this.userId = userId;
        //FragmentDiscoverNew.isCreateCollection = false;
        this.isFirstTimeFromMain = false;

    }

    public FragmentInspirationSection(boolean isViewAll, String userId, boolean isFirstTime) {
        this.isFirstTimeFromMain = true;
        this.isViewAll = isViewAll;
        this.userId = userId;
        //FragmentDiscoverNew.isCreateCollection = false;

    }


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadButton:
                ((HomeActivity) mContext).inviteFriends();
                break;
            case R.id.imgDelete:
                if (create_collection_alert.getVisibility() == View.VISIBLE)
                    uploadButton.setVisibility(View.VISIBLE);
                create_collection_alert.setVisibility(View.GONE);
                break;

            case R.id.btnCreateMyCollection:
                // addFragment(new FragmentDiscoverNew(1, false));
                break;

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
                image_camera.setBackgroundResource(R.drawable.tabborder);
                break;

            case R.id.fashioniconlayout:
                fashioniconlayout.setBackgroundResource(R.drawable.tabborder);
                beautyiconlayout.setBackgroundResource(0);
                fitnessiconlayout.setBackgroundResource(0);
                techiconlayout.setBackgroundResource(0);
                photographyiconlayoutlayout.setBackgroundResource(0);
                homeiconlayout.setBackgroundResource(0);
                traveliconlayout.setBackgroundResource(0);
                foodiconlayout.setBackgroundResource(0);
                techiconlayout.setBackgroundResource(0);
                break;

            case R.id.beautyiconlayout:
                beautyiconlayout.setBackgroundResource(R.drawable.tabborder);
                fashioniconlayout.setBackgroundResource(0);
                fitnessiconlayout.setBackgroundResource(0);
                techiconlayout.setBackgroundResource(0);
                photographyiconlayoutlayout.setBackgroundResource(0);
                homeiconlayout.setBackgroundResource(0);
                occasioniconlayout.setBackgroundResource(0);
                traveliconlayout.setBackgroundResource(0);
                foodiconlayout.setBackgroundResource(0);
                break;

            case R.id.fitnessiconlayout:
                fitnessiconlayout.setBackgroundResource(R.drawable.tabborder);
                fashioniconlayout.setBackgroundResource(0);
                beautyiconlayout.setBackgroundResource(0);
                techiconlayout.setBackgroundResource(0);
                photographyiconlayoutlayout.setBackgroundResource(0);
                homeiconlayout.setBackgroundResource(0);
                occasioniconlayout.setBackgroundResource(0);
                traveliconlayout.setBackgroundResource(0);
                foodiconlayout.setBackgroundResource(0);
                break;

            case R.id.techiconlayout:
                techiconlayout.setBackgroundResource(R.drawable.tabborder);
                fashioniconlayout.setBackgroundResource(0);
                beautyiconlayout.setBackgroundResource(0);
                fitnessiconlayout.setBackgroundResource(0);
                photographyiconlayoutlayout.setBackgroundResource(0);
                homeiconlayout.setBackgroundResource(0);
                occasioniconlayout.setBackgroundResource(0);
                traveliconlayout.setBackgroundResource(0);
                foodiconlayout.setBackgroundResource(0);
                break;

            case R.id.photographyiconlayout:
                photographyiconlayoutlayout.setBackgroundResource(R.drawable.tabborder);
                fashioniconlayout.setBackgroundResource(0);
                beautyiconlayout.setBackgroundResource(0);
                fitnessiconlayout.setBackgroundResource(0);
                techiconlayout.setBackgroundResource(0);
                homeiconlayout.setBackgroundResource(0);
                occasioniconlayout.setBackgroundResource(0);
                traveliconlayout.setBackgroundResource(0);
                foodiconlayout.setBackgroundResource(0);
                break;

            case R.id.homeiconlayout:
                homeiconlayout.setBackgroundResource(R.drawable.tabborder);
                fashioniconlayout.setBackgroundResource(0);
                beautyiconlayout.setBackgroundResource(0);
                fitnessiconlayout.setBackgroundResource(0);
                techiconlayout.setBackgroundResource(0);
                photographyiconlayoutlayout.setBackgroundResource(0);
                occasioniconlayout.setBackgroundResource(0);
                traveliconlayout.setBackgroundResource(0);
                foodiconlayout.setBackgroundResource(0);
                break;

            case R.id.occasioniconlayout:
                occasioniconlayout.setBackgroundResource(R.drawable.tabborder);
                fashioniconlayout.setBackgroundResource(0);
                fitnessiconlayout.setBackgroundResource(0);
                techiconlayout.setBackgroundResource(0);
                photographyiconlayoutlayout.setBackgroundResource(0);
                homeiconlayout.setBackgroundResource(0);
                traveliconlayout.setBackgroundResource(0);
                beautyiconlayout.setBackgroundResource(0);
                foodiconlayout.setBackgroundResource(0);
                break;

            case R.id.traveliconlayout:
                traveliconlayout.setBackgroundResource(R.drawable.tabborder);
                fashioniconlayout.setBackgroundResource(0);
                beautyiconlayout.setBackgroundResource(0);
                fitnessiconlayout.setBackgroundResource(0);
                techiconlayout.setBackgroundResource(0);
                photographyiconlayoutlayout.setBackgroundResource(0);
                homeiconlayout.setBackgroundResource(0);
                occasioniconlayout.setBackgroundResource(0);
                foodiconlayout.setBackgroundResource(0);
                break;

            case R.id.foodiconlayout:
                foodiconlayout.setBackgroundResource(R.drawable.tabborder);
                fashioniconlayout.setBackgroundResource(0);
                beautyiconlayout.setBackgroundResource(0);
                fitnessiconlayout.setBackgroundResource(0);
                techiconlayout.setBackgroundResource(0);
                photographyiconlayoutlayout.setBackgroundResource(0);
                homeiconlayout.setBackgroundResource(0);
                occasioniconlayout.setBackgroundResource(0);
                traveliconlayout.setBackgroundResource(0);
                break;
        }
    }

    private int getCollectionCount() {
        try {
            mProgressBarDialog = new ProgressBarDialog(mContext);
            mProgressBarDialog.show();
            final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {

                @Override
                public void handleOnSuccess(Object object) {
                    if (mProgressBarDialog.isShowing())
                        mProgressBarDialog.dismiss();
                    Syso.info("In handleOnSuccess>>" + object);
                    MyProfileRes myProfileRes = (MyProfileRes) object;
                    collectionLists = myProfileRes.getCollection_list();
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {

                    mProgressBarDialog = new ProgressBarDialog(mContext);
                    mProgressBarDialog.show();
                    Syso.info("In handleOnFailure>>" + object);
                    if (object != null) {
                        MyProfileRes response = (MyProfileRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                }
            });
            myProfileApi.getUserProfileDetail(UserPreference.getInstance().getUserID(), UserPreference.getInstance().getUserID());
            myProfileApi.execute();
        } catch (Exception ex) {
            return 0;
        }
        return collectionLists.size();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        tab_layout1 = (RelativeLayout) mainView.findViewById(R.id.tabLayout1);
        create_my_collection = (Button) mainView.findViewById(R.id.btnCreateMyCollection);
        imgDelete = (ImageView) mainView.findViewById(R.id.imgDelete);
        create_collection_alert = (FrameLayout) mainView.findViewById(R.id.createcollection_alert);
        inspirationList = (ListView) mainView.findViewById(R.id.inspirationList);
        loaderView = View.inflate(mContext, R.layout.footer, null);
        uploadButton = (LinearLayout) mainView.findViewById(R.id.uploadButton);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        image_upload_post = (ImageView) mainView.findViewById(R.id.image_upload_post);
        image_upload_post2 = (ImageView) mainView.findViewById(R.id.image_upload_post2);
        uploadChoice = (RelativeLayout) mainView.findViewById(R.id.uploadChoice);
        image_camera = (ImageView) mainView.findViewById(R.id.image_camera);
        bottomLayout = (RelativeLayout) mainView.findViewById(R.id.bottomLayout);
        profile_tab = (LinearLayout) mainView.findViewById(R.id.profile_tab);
        profile_pic = (CircleImageView) mainView.findViewById(R.id.profile_pic);
        search_tab = (LinearLayout) mainView.findViewById(R.id.search_tab);
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
        Tracker t = ((KikrApp) mContext.getApplication()).getTracker(KikrApp.TrackerName.APP_TRACKER);
        t.setScreenName("Fragment Inspiration Section");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        if (!isViewAll)
            uploadButton.setVisibility(View.GONE);
    }

    public void initData() {
        if (checkInternet())
            getInspirationFeedList();
        else {
            Log.w("FragmentInspSection", "showReloadOption()");
            showReloadOption();
        }
    }


    @Override
    public void setData(Bundle bundle) {

        if (!isViewAll || isFirstTimeFromMain)
            initData();

        inspirationList.setOnItemClickListener(this);
        inspirationList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                FragmentInspirationSection.this.firstVisibleItem = firstVisibleItem;
                FragmentInspirationSection.this.visibleItemCount = visibleItemCount;
                FragmentInspirationSection.this.totalItemCount = totalItemCount;
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (checkInternet2()) {
                        page++;
                        isFirstTime = false;
                        getInspirationFeedList();
                    } else {
                        showReloadFotter();
                    }
                }
            }
        });
        CommonUtility.setImage(getActivity(), UserPreference.getInstance().getProfilePic(), profile_pic, R.drawable.profile_icon);

    }

    private void getInspirationFeedList() {
        isLoading = !isLoading;
        if (!isFirstTime) {
            showFotter();
        } else {
            loadingTextView.setVisibility(View.VISIBLE);
        }

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.getInspirationFeed(userId, isViewAll, String.valueOf(page), UserPreference.getInstance().getUserID());
        inspirationFeedApi.execute();

    }

    @Override
    public void handleOnSuccess(Object object) {
        if (!isFirstTime) {
            hideFotter();
        } else {
            loadingTextView.setVisibility(View.GONE);
//			mProgressBarDialog.dismiss();
        }
        hideDataNotFound();
        isLoading = !isLoading;
        Syso.info("In handleOnSuccess>>" + object);
        InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
        product_list.addAll(inspirationFeedRes.getData());
        if (inspirationFeedRes.getData().size() < 10) {
            isLoading = true;
        }
        if (product_list.size() == 0 && isFirstTime) {
            showDataNotFound();
        }
        if (product_list.size() > 0 && isFirstTime) {
            inspirationAdapter = new InspirationAdapter(mContext, product_list, isViewAll, FragmentInspirationSection.this);
            inspirationList.setAdapter(inspirationAdapter);
            inspirationAdapter.notifyDataSetChanged();

        } else if (inspirationAdapter != null) {
            inspirationAdapter.setData(product_list);
            inspirationAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (!isFirstTime) {
            inspirationList.removeFooterView(loaderView);
        } else {
            loadingTextView.setVisibility(View.GONE);
        }
        isLoading = !isLoading;
        Syso.info("In handleOnFailure>>" + object);
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

    @Override
    public void setClickListener() {
        uploadButton.setOnClickListener(this);
        imgDelete.setOnClickListener(this);
        create_my_collection.setOnClickListener(this);
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
        foodiconlayout.setOnClickListener(this);
        fitnessiconlayout.setOnClickListener(this);
        occasioniconlayout.setOnClickListener(this);
    }

    private void showReloadOption() {
        showDataNotFound();
        TextView textView = getDataNotFound();
        Log.w("showReloadOption", "Text is: " + textView.getText().toString());
        Syso.info("text view>>" + textView);
        if (textView != null) {
            Syso.info("12233 inside text view text view>>" + textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInternet())
                        getInspirationFeedList();
                    Syso.info("text view>>");
                }
            });
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


    public void removePost(final String inspiration_id, final String user_id) {

        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        this.inspiration_id = inspiration_id;
        this.user_id = user_id;

        deletePostApi = new DeletePostApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
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
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
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

}
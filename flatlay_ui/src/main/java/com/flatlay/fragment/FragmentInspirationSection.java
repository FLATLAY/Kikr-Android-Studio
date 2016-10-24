package com.flatlay.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.flatlay.BaseFragment;
import com.flatlay.KikrApp;
import com.flatlay.KikrApp.TrackerName;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.InspirationAdapter;
import com.flatlay.dialog.HelpInspirationDialog;
import com.flatlay.ui.ProgressBarDialog;
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

import java.util.ArrayList;
import java.util.List;

public class FragmentInspirationSection extends BaseFragment implements OnClickListener, OnItemClickListener, ServiceCallback {
    private View mainView;
    private ListView inspirationList;
    private ProgressBarDialog mProgressBarDialog;
    int page = 0;
    String inspiration_id;
    String user_id;
    String viewer_id;
    private LinearLayout uploadButton;
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
    boolean isFirstTimeFromMain=false;

    public FragmentInspirationSection(boolean isViewAll, String userId) {

        this.isViewAll = isViewAll;
        this.userId = userId;
        FragmentDiscoverNew.isCreateCollection = false;
        this.isFirstTimeFromMain = false;

    }

    public FragmentInspirationSection(boolean isViewAll, String userId, boolean isFirstTime) {
        this.isFirstTimeFromMain = true;
        this.isViewAll = isViewAll;
        this.userId = userId;
        FragmentDiscoverNew.isCreateCollection = false;

    }


    public FragmentInspirationSection() {
        this.isViewAll = true;
       isFirstTimeFromMain=true;
        this.userId = UserPreference.getInstance().getUserID();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_inspiration_section, null);
        return mainView;
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadButton:
//                if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
//                    CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
//                    createAccountDialog.show();
//                } else {
////                    if (!checkPermission()) {
////
////                        requestPermission();
////
////                    } else {
////
////                        getCollectionCount();
////
////                    }
//                    //getCollectionCount();
//
//
//                }
//                inspirationList.setVisibility(View.GONE);
//                loadingTextView.setVisibility(View.VISIBLE);
                ((HomeActivity)mContext).inviteFriends();
                break;
            case R.id.imgDelete:
                if (create_collection_alert.getVisibility() == View.VISIBLE)
                    uploadButton.setVisibility(View.VISIBLE);
                create_collection_alert.setVisibility(View.GONE);
                break;

            case R.id.btnCreateMyCollection:
                addFragment(new FragmentDiscoverNew(1, false));
        }
    }


//    private boolean checkPermission(){
//        int result = mContext.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (result == PackageManager.PERMISSION_GRANTED){
//
//            return true;
//
//        } else {
//
//            return false;
//
//        }
//    }


//    private void requestPermission(){
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.ACCESS_FINE_LOCATION)){
//
//            Toast.makeText(mContext, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
//
//        } else {
//
//            ActivityCompat.requestPermissions(mContext,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_REQUEST_CODE);
//        }
//    }

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
                    if (collectionLists.size() > 0)
                        addFragment(new FragmentPostUploadTab());
                    else {
                        uploadButton.setVisibility(View.GONE);
                        create_collection_alert.setVisibility(View.VISIBLE);
                    }

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


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        mContext.shouldShowRequestPermissionRationale()
//    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        create_my_collection = (Button) mainView.findViewById(R.id.btnCreateMyCollection);
        imgDelete = (ImageView) mainView.findViewById(R.id.imgDelete);
        create_collection_alert = (FrameLayout) mainView.findViewById(R.id.createcollection_alert);
        inspirationList = (ListView) mainView.findViewById(R.id.inspirationList);
        loaderView = View.inflate(mContext, R.layout.footer, null);
        uploadButton = (LinearLayout) mainView.findViewById(R.id.uploadButton);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);

        Tracker t = ((KikrApp) mContext.getApplication()).getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("Fragment Inspiration Section");
        t.send(new HitBuilders.ScreenViewBuilder().build());

        if (!isViewAll)
            uploadButton.setVisibility(View.GONE);
    }

    public void initData() {
        if (checkInternet())
            getInspirationFeedList();
        else
            showReloadOption();
    }

    @Override
    public void setData(Bundle bundle) {

        if (FragmentDiscoverNew.isCreateCollection || !isViewAll ||isFirstTimeFromMain)
            initData();

        inspirationList.setOnItemClickListener(this);
        inspirationList.setOnScrollListener(new OnScrollListener() {
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

        ////////////////////////////////
        if (HelpPreference.getInstance().getHelpInspiration().equals("yes")) {
            HelpPreference.getInstance().setHelpInspiration("no");
            HelpInspirationDialog helpInspirationDialog = new HelpInspirationDialog(mContext);
            helpInspirationDialog.show();
        }

    }

//	@Override
//	public void setMenuVisibility(boolean menuVisible) {
//		super.setMenuVisibility(menuVisible);
//		if(menuVisible){
//			if (HelpPreference.getInstance().getHelpInspiration().equals("yes")) {
//				HelpPreference.getInstance().setHelpInspiration("no");
//				HelpInspirationDialog helpInspirationDialog = new HelpInspirationDialog(mContext);
//				helpInspirationDialog.show();
//			}
//		}
//	}

    private void getInspirationFeedList() {
       // mProgressBarDialog = new ProgressBarDialog(mContext);
        isLoading = !isLoading;
        if (!isFirstTime) {
            showFotter();
        } else {
            loadingTextView.setVisibility(View.VISIBLE);
//			mProgressBarDialog.show();
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
        } else if (inspirationAdapter != null) {
            inspirationAdapter.setData(product_list);
            inspirationAdapter.notifyDataSetChanged();


        }

        if (FragmentDiscoverNew.isCreateCollection && !isPostUpload)
            getCollectionCount();
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (!isFirstTime) {
            inspirationList.removeFooterView(loaderView);
        } else {
            loadingTextView.setVisibility(View.GONE);
//			mProgressBarDialog.dismiss();
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
    }


    private void showReloadOption() {
        showDataNotFound();
        TextView textView = getDataNotFound();
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
        textView.setOnClickListener(new OnClickListener() {

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

                    //    AlertUtils.showToast(mContext, "Post removed successfully");
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
        //  deletePostApi.deletepost(inspiration_id, UserPreference.getInstance().getUserID());
        deletePostApi.removePost(inspiration_id, user_id);
        deletePostApi.execute();

        // cartApi.removeFromCart(UserPreference.getInstance().getUserID(), id);
        //cartApi.execute();
    }

}

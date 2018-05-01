package com.flatlay.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.EditProfileActivity;
import com.flatlay.activity.HomeActivity;

import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.BrandListApi;
import com.flatlaylib.api.CheckPointsStatusApi;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.api.InterestSectionApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.bean.FollowerList;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.ProfileCollectionList;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.db.HelpPreference;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.BrandListRes;
import com.flatlaylib.service.res.CheckPointStatusRes;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.service.res.InterestSectionRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.squareup.picasso.Picasso;

import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;


public class FragmentProfileView extends BaseFragment implements OnClickListener, ServiceCallback {
    private View mainView;
    private ListView collection_list;
    private ImageView collection_button, follower_button, following_button, photo_button;
    TextView follow_btn;
    Button invite;
    private LinearLayout follow_btn_layout;
    private ImageView up_arrow_image_first, up_arrow_image_second, up_arrow_image_third, user_profile_image, up_arrow_image_photos;
    private ProgressBarDialog mProgressBarDialog;
    private List<UserData> userDetails;
    private List<ProfileCollectionList> collectionLists = new ArrayList<ProfileCollectionList>();
    private List<FollowerList> followersLists = new ArrayList<FollowerList>();
    private List<FollowerList> followingLists = new ArrayList<FollowerList>();
    private TextView user_profile_name, descriptionTextView;
    private LinearLayout settings_btn_layout, backarrowlayout;
    private String user_id;
    private boolean isFollowed;
    private ImageView editProfileImageView, bgProfileLayout, bg_nocollection;
    private FragmentProfileView fragmentProfileView;
    private LinearLayout noCollectionText;
    private String status = "";
    RelativeLayout followerNotFound;
    private int pagenum = 0;
    private String isSelected = "people";
    private LinearLayout layoutPeopleStoreBrand;
    private Button interest_store_button, interest_brand_button, interest_people_button;
    private List<InterestSection> interestList;
   // private InterestBrandListAdapter interestBrandListAdapter;
   // private InterestStoreListAdapter interestStoreListAdapter;
    public static String lastUserId;

    private TextView myActivityButton, btn_photos;
    private GridView imagesList;
    private LinearLayout profile_btn_layout;
    private boolean isLoading = false;
    private boolean isFirstTime = true;
    int page = 0;
    private int firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;
    private List<Inspiration> product_list = new ArrayList<Inspiration>();
   // private InspirationGridAdapter inspirationAdapter;
    private TextView loadingTextView, tvEarnCreditsText;
    private View loaderView;
    private Button photos_button, btnCreateCollection, btnUpload;
    RelativeLayout photo_not_found, photosotherusernotfound;
    TextView otheruserclick;
    LinearLayout txtfollowers;
    boolean isFragmentContainer = true;
    LinearLayout photolayout, collectionlayout, followinglayout, followerlayout;
    public static final String TAG = "FragmentProfileView";

    public FragmentProfileView() {
        try {

            if (lastUserId != null) {
                this.user_id = lastUserId;
                fragmentProfileView = this;
            } else {
                ((HomeActivity) getActivity()).onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FragmentProfileView(String user_id, String isFollowed) {

        this.user_id = user_id;
        lastUserId = user_id;
        fragmentProfileView = this;
        if (isFollowed != null && isFollowed.equals("yes")) {
            this.isFollowed = true;
        } else {
            this.isFollowed = false;
        }
    }

    public FragmentProfileView(String user_id, String isFollowed, boolean isFragmentContainer) {

        this.user_id = user_id;
        lastUserId = user_id;
        fragmentProfileView = this;
        if (isFollowed != null && isFollowed.equals("yes")) {
            this.isFollowed = true;
        } else {
            this.isFollowed = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_profile, null);
        fragmentProfileView = this;
        Log.w(TAG, "onCreateView");
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        photo_not_found = (RelativeLayout) mainView.findViewById(R.id.photosNotFound);
        photosotherusernotfound = (RelativeLayout) mainView.findViewById(R.id.photos_otheruser_not_found);
        otheruserclick = (TextView) mainView.findViewById(R.id.otheruserclick);
        bg_nocollection = (ImageView) mainView.findViewById(R.id.bg_nocollection);
        invite = (Button) mainView.findViewById(R.id.invite);
        // photos_button = (Button) mainView.findViewById(R.id.photos_button);
        collection_list = (ListView) mainView.findViewById(R.id.collection_list);
        photo_button = (ImageView) mainView.findViewById(R.id.photo_button);
        following_button = (ImageView) mainView.findViewById(R.id.following_button);
        follower_button = (ImageView) mainView.findViewById(R.id.follower_button);
        collection_button = (ImageView) mainView.findViewById(R.id.collection_button);
        follow_btn = (TextView) mainView.findViewById(R.id.follow_btn);
        follow_btn_layout = (LinearLayout) mainView.findViewById(R.id.follow_btn_layout);
        up_arrow_image_first = (ImageView) mainView.findViewById(R.id.up_arrow_image_first);
        up_arrow_image_photos = (ImageView) mainView.findViewById(R.id.up_arrow_image_photos);
        up_arrow_image_second = (ImageView) mainView.findViewById(R.id.up_arrow_image_second);
        up_arrow_image_third = (ImageView) mainView.findViewById(R.id.up_arrow_image_third);
        user_profile_image = (ImageView) mainView.findViewById(R.id.user_profile_image);
        user_profile_name = (TextView) mainView.findViewById(R.id.user_profile_name);
        settings_btn_layout = (LinearLayout) mainView.findViewById(R.id.settings_btn_layout);
        editProfileImageView = (ImageView) mainView.findViewById(R.id.editProfileImageView);
        bgProfileLayout = (ImageView) mainView.findViewById(R.id.bgProfileLayout);
        noCollectionText = (LinearLayout) mainView.findViewById(R.id.noCollectionText);
        followerNotFound = (RelativeLayout) mainView.findViewById(R.id.followerNotFound);
        tvEarnCreditsText = (TextView) mainView.findViewById(R.id.tvEarnCreditText);
        layoutPeopleStoreBrand = (LinearLayout) mainView.findViewById(R.id.layoutPeopleStoreBrand);
        interest_store_button = (Button) mainView.findViewById(R.id.interest_store_button);
        interest_brand_button = (Button) mainView.findViewById(R.id.interest_brand_button);
        interest_people_button = (Button) mainView.findViewById(R.id.interest_people_button);
        myActivityButton = (TextView) mainView.findViewById(R.id.btn_activity);
        //btn_photos = (TextView) mainView.findViewById(R.id.btn_photos);
        //myActivityButton.setText("Activity");
        descriptionTextView = (TextView) mainView.findViewById(R.id.descriptionTextView);
        imagesList = (GridView) mainView.findViewById(R.id.imagesList);
        profile_btn_layout = (LinearLayout) mainView.findViewById(R.id.profile_btn_layout);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        loaderView = View.inflate(mContext, R.layout.footer, null);
        btnCreateCollection = (Button) mainView.findViewById(R.id.btnCreateCollection);
        btnUpload = (Button) mainView.findViewById(R.id.btnUpload);
        photolayout = (LinearLayout) mainView.findViewById(R.id.photolayout);
        collectionlayout = (LinearLayout) mainView.findViewById(R.id.collectionlayout);
        followinglayout = (LinearLayout) mainView.findViewById(R.id.followinglayout);
        followerlayout = (LinearLayout) mainView.findViewById(R.id.followerlayout);
        txtfollowers = (LinearLayout) mainView.findViewById(R.id.txtfollowers);
        backarrowlayout = (LinearLayout) mainView.findViewById(R.id.backarrowlayout);
        CommonUtility.hideSoftKeyboard(mContext);
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void handleOnSuccess(Object object) {

    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {

    }
}

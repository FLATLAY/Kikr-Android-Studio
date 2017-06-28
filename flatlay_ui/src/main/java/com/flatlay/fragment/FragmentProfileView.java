package com.flatlay.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
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
import com.flatlay.adapter.FragmentProfileCollectionAdapter;
import com.flatlay.adapter.FragmentProfileFollowersAdapter;
import com.flatlay.adapter.FragmentProfileFollowingAdapter;
import com.flatlay.adapter.InspirationGridAdapter;
import com.flatlay.adapter.InterestBrandListAdapter;
import com.flatlay.adapter.InterestStoreListAdapter;
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
    private InterestBrandListAdapter interestBrandListAdapter;
    private InterestStoreListAdapter interestStoreListAdapter;
    public static String lastUserId;

    private TextView myActivityButton, btn_photos;
    private GridView imagesList;
    private LinearLayout profile_btn_layout;
    private boolean isLoading = false;
    private boolean isFirstTime = true;
    int page = 0;
    private int firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;
    private List<Inspiration> product_list = new ArrayList<Inspiration>();
    private InspirationGridAdapter inspirationAdapter;
    private TextView loadingTextView, tvEarnCreditsText;
    private View loaderView;
    private Button photos_button, btnCreateCollection, btnUpload;
    RelativeLayout photo_not_found, photosotherusernotfound;
    TextView otheruserclick;
    LinearLayout txtfollowers;
    boolean isFragmentContainer = true;
    LinearLayout photolayout, collectionlayout, followinglayout, followerlayout;

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
        Log.w("FragmentProfileView","onCreateView");
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
    public void refreshData(Bundle bundle) {
    }

    @Override
    public void setClickListener() {
        btnCreateCollection.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        photolayout.setOnClickListener(this);
        collectionlayout.setOnClickListener(this);
        followerlayout.setOnClickListener(this);
        followinglayout.setOnClickListener(this);
        editProfileImageView.setOnClickListener(this);
        interest_store_button.setOnClickListener(this);
        interest_brand_button.setOnClickListener(this);
        interest_people_button.setOnClickListener(this);
        myActivityButton.setOnClickListener(this);
        settings_btn_layout.setOnClickListener(this);
        backarrowlayout.setOnClickListener(this);
        invite.setOnClickListener(this);
        follow_btn.setOnClickListener(this);
        //btn_photos.setOnClickListener(this);
    }

    public void initData() {
        if (checkInternet()) {
            getInspirationFeedList();
            getCollectionStatus();
        } else
            showReloadOption();
    }

    @Override
    public void setData(Bundle bundle) {

        initData();


        collection_list.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                // Do nothing
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
//				   System.out.println("123456 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
//			    	System.out.println("123456 inside if page"+pagenum+" ,"+isSelected);
                    if (checkInternet2()) {
                        pagenum++;
                        isFirstTime = false;
                        loadData();
                    } else {
                        showReloadFotter();
                    }
                }
            }
        });

        imagesList.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                FragmentProfileView.this.firstVisibleItem = firstVisibleItem;
                FragmentProfileView.this.visibleItemCount = visibleItemCount;
                FragmentProfileView.this.totalItemCount = totalItemCount;
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
    }

    public void loadData() {
        if (isSelected.equalsIgnoreCase("store")) {
            getStoreList();
        } else if (isSelected.equalsIgnoreCase("brand")) {
            getBrandList();
        }
    }

    @Override
    public void onClick(View v) {
        Log.w("FragmemtProfileView","onClick()");
        switch (v.getId()) {
            case R.id.btnUpload:
                ((HomeActivity)mContext).checkPermissions();
                break;


            case R.id.settings_btn_layout:
                addFragment(new SettingFragmentTab());
                break;
            case R.id.btnCreateCollection:
                addFragment(new FragmentSearchProduct());
                break;
            case R.id.btn_activity:
                noCollectionText.setVisibility(View.GONE);
                addFragment(new FragmentActivityMonths());
                break;
            case R.id.backarrowlayout:
                Log.w("FragmentProfileView","Back Button Pressed!");
                //((HomeActivity) mContext).onBackPressed();
                Intent intent = new Intent(mContext, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.invite:
                ((HomeActivity) mContext).inviteFriends();
                break;
        /*	case R.id.btn_photos:
                noCollectionText.setVisibility(View.GONE);
				if (product_list.size() == 0)
					showDataNotFound();
				else {
					hideDataNotFound();
					collection_list.setVisibility(View.GONE);
					imagesList.setVisibility(View.VISIBLE);
				}
				//if (imagesList.getVisibility() == View.VISIBLE) {
				// profile_btn_layout.setVisibility(View.VISIBLE);
				// imagesList.setVisibility(View.GONE);
				//collection_list.setVisibility(View.VISIBLE);
				// btn_photos.setText("Photos");
				// } else {
				//profile_btn_layout.setVisibility(View.GONE);


				//btn_photos.setText("collections");
				// }
				up_arrow_image_first.setVisibility(View.INVISIBLE);
				up_arrow_image_second.setVisibility(View.INVISIBLE);
				up_arrow_image_third.setVisibility(View.INVISIBLE);
				layoutPeopleStoreBrand.setVisibility(View.GONE);
				break;*/

            case R.id.photolayout:
                followerNotFound.setVisibility(View.GONE);
                txtfollowers.setVisibility(View.GONE);
                noCollectionText.setVisibility(View.GONE);

                if (product_list.size() == 0) {
                    if (user_id.equals(UserPreference.getInstance().getUserID())) {

                        ((TextView) photo_not_found.findViewById(R.id.tvNoPhotos)).setText("You have yet to upload any posts, create one now !");
                        ((Button) photo_not_found.findViewById(R.id.btnUpload)).setVisibility(View.VISIBLE);
                    } else {
                        ((TextView) photo_not_found.findViewById(R.id.tvNoPhotos)).setText(user_profile_name.getText() + " hasn't uploaded any photos.");
                        ((Button) photo_not_found.findViewById(R.id.btnUpload)).setVisibility(View.GONE);
                    }
                    photo_not_found.setVisibility(View.VISIBLE);
                    hideDataNotFound();
                } else {
                    photo_not_found.setVisibility(View.GONE);
                    hideDataNotFound();
                    collection_list.setVisibility(View.GONE);
                    imagesList.setVisibility(View.VISIBLE);
                }
                //if (imagesList.getVisibility() == View.VISIBLE) {
                // profile_btn_layout.setVisibility(View.VISIBLE);
                // imagesList.setVisibility(View.GONE);
                //collection_list.setVisibility(View.VISIBLE);
                // btn_photos.setText("Photos");
                // } else {
                //profile_btn_layout.setVisibility(View.GONE);


                //btn_photos.setText("collections");
                // }

                layoutPeopleStoreBrand.setVisibility(View.GONE);
                //collection_list.setVisibility(View.VISIBLE);
                follower_button.setImageResource(R.drawable.followeruser);
                following_button.setImageResource(R.drawable.followinguser);
                collection_button.setImageResource(R.drawable.profile_collection_tab);
                photo_button.setImageResource(R.drawable.photoselected);
//                followerlayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
//                followinglayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
//                collectionlayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
//                photolayout.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                break;
            case R.id.collectionlayout:
                followerNotFound.setVisibility(View.GONE);
                txtfollowers.setVisibility(View.GONE);
                photo_not_found.setVisibility(View.GONE);
                noCollectionText.setVisibility(View.VISIBLE);
                hideDataNotFound();
                layoutPeopleStoreBrand.setVisibility(View.GONE);
                if (!HelpPreference.getInstance().getHelpCollection().equals("yes") && collectionLists.size() == 0) {
                    noCollectionText.setVisibility(View.VISIBLE);
                    collection_list.setVisibility(View.GONE);
                } else if (status.equals("yes") && collectionLists.size() == 0
                        && user_id.equals(UserPreference.getInstance().getUserID())) {
                    tvEarnCreditsText.setVisibility(View.VISIBLE);
                    btnCreateCollection.setVisibility(View.VISIBLE);
                    noCollectionText.setVisibility(View.VISIBLE);
                    collection_list.setVisibility(View.GONE);
                }
                if (collectionLists.size() == 0 && user_id.equals(UserPreference.getInstance().getUserID())) {
                    tvEarnCreditsText.setVisibility(View.VISIBLE);
                    btnCreateCollection.setVisibility(View.VISIBLE);
                    bg_nocollection.setVisibility(View.VISIBLE);
                } else if(collectionLists.size()>0){
                    noCollectionText.setVisibility(View.GONE);
                    collection_list.setVisibility(View.VISIBLE);
                    collection_list.setAdapter(new FragmentProfileCollectionAdapter(mContext, collectionLists, user_id, fragmentProfileView, null, null));
                }


                imagesList.setVisibility(View.GONE);

                follower_button.setImageResource(R.drawable.followeruser);
                following_button.setImageResource(R.drawable.followinguser);
                collection_button.setImageResource(R.drawable.collectionselected);
                photo_button.setImageResource(R.drawable.profile_photo_tab);
//                followerlayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
//                followinglayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
//                collectionlayout.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
//                photolayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                break;
            case R.id.followerlayout:
                txtfollowers.setVisibility(View.VISIBLE);
                noCollectionText.setVisibility(View.GONE);
                photo_not_found.setVisibility(View.GONE);
                if (followersLists.size() == 0 && (user_id.equals(UserPreference.getInstance().getUserID()))) {
                    followerNotFound.setVisibility(View.VISIBLE);
                    ((TextView) photo_not_found.findViewById(R.id.tvNoPhotos)).setVisibility(View.INVISIBLE);
                }
                else if(followersLists.size()>0) {
                    hideDataNotFound();
                    collection_list.setAdapter(new FragmentProfileFollowersAdapter(mContext, followersLists));
                    collection_list.setVisibility(View.VISIBLE);
                }
                else
                {
                    ((TextView) photo_not_found.findViewById(R.id.tvNoPhotos)).setText(user_profile_name.getText() + " has no followers.");
                    ((Button) photo_not_found.findViewById(R.id.btnUpload)).setVisibility(View.GONE);
                    photo_not_found.setVisibility(View.VISIBLE);
                }


                imagesList.setVisibility(View.GONE);
                txtfollowers.setVisibility(View.VISIBLE);
                layoutPeopleStoreBrand.setVisibility(View.GONE);
              //  collection_list.setVisibility(View.VISIBLE);
                follower_button.setImageResource(R.drawable.followerselected);
                following_button.setImageResource(R.drawable.followinguser);
                collection_button.setImageResource(R.drawable.profile_collection_tab);
                photo_button.setImageResource(R.drawable.profile_photo_tab);

//                followerlayout.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
//                followinglayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
//                collectionlayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
//                photolayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                break;
            case R.id.followinglayout:
                followerNotFound.setVisibility(View.GONE);
                txtfollowers.setVisibility(View.GONE);
                noCollectionText.setVisibility(View.GONE);
                photo_not_found.setVisibility(View.GONE);
                if (followingLists.size() == 0) {
                    collection_list.setVisibility(View.GONE);
                    noFollowingFound();
                    //showDataNotFound();
                } else if(followingLists.size()>0){
                    hideDataNotFound();
                    collection_list.setVisibility(View.VISIBLE);
                    collection_list.setAdapter(new FragmentProfileFollowingAdapter(mContext, followingLists));
                }
                else
                {
                    ((TextView) photo_not_found.findViewById(R.id.tvNoPhotos)).setText(user_profile_name.getText() + " haven't any following list.");
                    ((Button) photo_not_found.findViewById(R.id.btnUpload)).setVisibility(View.GONE);
                    photo_not_found.setVisibility(View.VISIBLE);
                }

                if (isSelected.equalsIgnoreCase("store")) {
                    collection_list.setAdapter(null);
                    isFirstTime = true;
                    pagenum = 0;
                    isSelected = "store";
                    isLoading = false;
                    getStoreList();
                    collection_list.setVisibility(View.VISIBLE);
                }
                if (isSelected.equalsIgnoreCase("brand")) {
                    collection_list.setAdapter(null);
                    isFirstTime = true;
                    pagenum = 0;
                    isSelected = "brand";
                    isLoading = false;
                    getBrandList();
                    collection_list.setVisibility(View.VISIBLE);
                }


                layoutPeopleStoreBrand.setVisibility(View.VISIBLE);

                imagesList.setVisibility(View.GONE);
                follower_button.setImageResource(R.drawable.followeruser);
                following_button.setImageResource(R.drawable.followingselected);
                collection_button.setImageResource(R.drawable.profile_collection_tab);
                photo_button.setImageResource(R.drawable.profile_photo_tab);


//                followerlayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
//                followinglayout.setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
//                collectionlayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
//                photolayout.setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                break;
            case R.id.follow_btn:
                if (checkInternet()) {
                    if (follow_btn.getText().toString().trim().equalsIgnoreCase("Follow"))
                        followUser();
                    else
                        unFollowUser();
                }
                break;
            case R.id.editProfileImageView:
                Bundle bundle = new Bundle();
                bundle.putString("username", userDetails.get(0).getUsername());
                bundle.putString("profilePic", userDetails.get(0).getProfile_pic());
                bundle.putString("bgPic", userDetails.get(0).getBackground_pic());
                bundle.putString("description", userDetails.get(0).getDescription());
                bundle.putBoolean("is_edit_profile", true);
                bundle.putString("from", AppConstants.FROM_PROFILE);
                startActivityForResult(EditProfileActivity.class, bundle, 1000);
                break;
            case R.id.interest_store_button:
                isFirstTime = true;
                pagenum = 0;
                isSelected = "store";
                isLoading = false;
                collection_list.setAdapter(null);
                collection_list.setVisibility(View.VISIBLE);
                noCollectionText.setVisibility(View.GONE);
                interest_store_button.setTextColor(getResources().getColor(R.color.btn_green));
                interest_brand_button.setTextColor(getResources().getColor(R.color.white));
                interest_people_button.setTextColor(getResources().getColor(R.color.white));

                if (checkInternet()) {
                    getStoreList();
                } else
                    showReloadOption();

                break;
            case R.id.interest_brand_button:
                pagenum = 0;
                isFirstTime = true;
                isSelected = "brand";
                isLoading = false;
                collection_list.setAdapter(null);
                collection_list.setVisibility(View.VISIBLE);
                noCollectionText.setVisibility(View.GONE);
                interest_store_button.setTextColor(getResources().getColor(R.color.white));
                interest_brand_button.setTextColor(getResources().getColor(R.color.btn_green));
                interest_people_button.setTextColor(getResources().getColor(R.color.white));

                if (checkInternet()) {
                    getBrandList();
                } else
                    showReloadOption();

                break;
            case R.id.interest_people_button:
                isFirstTime = true;
                pagenum = 0;
                isLoading = false;
                isSelected = "people";
                collection_list.setAdapter(null);
                noCollectionText.setVisibility(View.GONE);
                interest_store_button.setTextColor(getResources().getColor(R.color.white));
                interest_brand_button.setTextColor(getResources().getColor(R.color.white));
                interest_people_button.setTextColor(getResources().getColor(R.color.btn_green));
                if (checkInternet()) {
                    if (followingLists.size() == 0)
                        noFollowingFound();
                    else {
                        hideDataNotFound();
                        collection_list.setAdapter(new FragmentProfileFollowingAdapter(mContext, followingLists));
                    }
                } else
                    showReloadOption();

                break;
            default:
                break;
        }
    }

    private void followUser() {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();

        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                mProgressBarDialog.dismiss();
                Syso.info("In handleOnSuccess>>" + object);
                AlertUtils.showToast(mContext, R.string.user_followed_successfully);
                isFollowed = true;
                follow_btn.setText("FOLLOWING");
                follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.followgreen));

                follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
                refreshProfile();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        followUserApi.followUser(UserPreference.getInstance().getUserID(), user_id);
        followUserApi.execute();

        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                followUserApi.cancel();
            }
        });
    }

    private void unFollowUser() {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();

        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                mProgressBarDialog.dismiss();
                Syso.info("In handleOnSuccess>>" + object);
                isFollowed = false;
                follow_btn.setText("FOLLOW ");
                follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_borderbg));
                refreshProfile();
                follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        followUserApi.unFollowUser(UserPreference.getInstance().getUserID(), user_id);
        followUserApi.execute();

        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                followUserApi.cancel();
            }
        });
    }


    private void getUserProfileDetail() {
//        mProgressBarDialog = new ProgressBarDialog(mContext);
//        mProgressBarDialog.show();

        final MyProfileApi myProfileApi = new MyProfileApi(this);
        myProfileApi.getUserProfileDetail(user_id, UserPreference.getInstance().getUserID());
        myProfileApi.execute();

//        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                myProfileApi.cancel();
//            }
//        });
    }

    @Override
    public void handleOnSuccess(Object object) {
        if (mProgressBarDialog.isShowing())
            mProgressBarDialog.dismiss();
        // hideDataNotFound();
        UserPreference.getInstance().setIsRefreshProfile(false);
        Syso.info("In handleOnSuccess>>" + object);
        MyProfileRes myProfileRes = (MyProfileRes) object;
        userDetails = myProfileRes.getUser_data();
        collectionLists = myProfileRes.getCollection_list();
        followersLists = myProfileRes.getFollowers_list();
        followingLists = myProfileRes.getFollowing_list();

        setDetails();
    }

    private void getCollectionStatus() {
        // mProgressBarDialog = new ProgressBarDialog(mContext);
        //mProgressBarDialog.show();
        CheckPointsStatusApi checkPointsStatusApi = new CheckPointsStatusApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                // mProgressBarDialog.dismiss();
                Syso.info("In handleOnSuccess>>" + object);
                CheckPointStatusRes pointStatusRes = (CheckPointStatusRes) object;
                status = pointStatusRes.getStatus();

            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                //   mProgressBarDialog.dismiss();
                getUserProfileDetail();
            }
        });
        checkPointsStatusApi.checkcollectionpointstatus(UserPreference.getInstance().getUserID());
        checkPointsStatusApi.execute();
    }

    private void setDetails() {


        System.out.println("userDetails.get(0).getProfile_pic()    " + userDetails.get(0).getProfile_pic());
        if (!userDetails.get(0).getProfile_pic().equals(""))
            CommonUtility.setImage(mContext, userDetails.get(0).getProfile_pic(), user_profile_image, R.drawable.dum_user);
        if (!userDetails.get(0).getBackground_pic().equals(""))
            CommonUtility.setImage(mContext, userDetails.get(0).getBackground_pic(), bgProfileLayout, R.drawable.flatlay_profile_bg_gradient_rect);

        if (userDetails.get(0).getUsername() != null && !userDetails.get(0).getUsername().equals(""))
            user_profile_name.setText(userDetails.get(0).getUsername());
        else
            user_profile_name.setText("Unknown");

        if (!TextUtils.isEmpty(userDetails.get(0).getDescription())) {
            descriptionTextView.setText(userDetails.get(0).getDescription());
        } else if (product_list.size() == 0) {

            if (user_id.equals(UserPreference.getInstance().getUserID())) {

                ((TextView) photo_not_found.findViewById(R.id.tvNoPhotos)).setText("You have yet to upload any posts, create one now !");
                ((Button) photo_not_found.findViewById(R.id.btnUpload)).setVisibility(View.VISIBLE);
            } else {
                ((TextView) photo_not_found.findViewById(R.id.tvNoPhotos)).setText(user_profile_name.getText() + " hasn't uploaded any photos.");
                ((Button) photo_not_found.findViewById(R.id.btnUpload)).setVisibility(View.GONE);
            }
            photo_not_found.setVisibility(View.VISIBLE);
        } else {
            // System.out.print("abhishek kumar verma");

            collection_list.setAdapter(new FragmentProfileCollectionAdapter(mContext, collectionLists, user_id, fragmentProfileView, null, null));

        }
        if(product_list.size()>0)
        {
           imagesList.setVisibility(View.VISIBLE);
            photo_not_found.setVisibility(View.GONE);
        }
//        collection_button.setText(collectionLists.size() + "\nCollections");
//        follower_button.setText(followersLists.size() + "\nFollowers");
//        following_button.setText(followingLists.size() + "\nFollowing");
//        photos_button.setText(product_list.size() + "\nPhotos");
        if (userDetails.get(0).getIs_followed() != null && userDetails.get(0).getIs_followed().equals("yes")) {
            this.isFollowed = true;
        } else {
            this.isFollowed = false;
        }
        if (userDetails.get(0).getId().equalsIgnoreCase(UserPreference.getInstance().getUserID())) {
            myActivityButton.setVisibility(View.VISIBLE);
            settings_btn_layout.setVisibility(View.VISIBLE);
            follow_btn_layout.setVisibility(View.GONE);
            editProfileImageView.setVisibility(View.VISIBLE);
//            photo_not_found.setVisibility(View.VISIBLE);
//            photosotherusernotfound.setVisibility(View.GONE);
        } else if (isFollowed) {
            myActivityButton.setVisibility(View.INVISIBLE);
            follow_btn_layout.setVisibility(View.VISIBLE);
            settings_btn_layout.setVisibility(View.GONE);
            backarrowlayout.setVisibility(View.VISIBLE);
//            photosotherusernotfound.setVisibility(View.VISIBLE);
//            photo_not_found.setVisibility(View.GONE);
            editProfileImageView.setVisibility(View.GONE);
            follow_btn.setText("FOLLOWING");
            follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.followgreen));
            follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            myActivityButton.setVisibility(View.INVISIBLE);
            follow_btn_layout.setVisibility(View.VISIBLE);
//            photosotherusernotfound.setVisibility(View.VISIBLE);
//            photo_not_found.setVisibility(View.GONE);
            editProfileImageView.setVisibility(View.GONE);
            settings_btn_layout.setVisibility(View.GONE);
            backarrowlayout.setVisibility(View.VISIBLE);
            follow_btn.setText("FOLLOW ");
            follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_borderbg));
           // refreshProfile();
            follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (mProgressBarDialog.isShowing())
            mProgressBarDialog.dismiss();
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            MyProfileRes response = (MyProfileRes) object;
            AlertUtils.showToast(mContext, response.getMessage());
        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            refreshProfile();
        }
    }

    public void refreshProfile() {
        up_arrow_image_first.setVisibility(View.VISIBLE);
        up_arrow_image_second.setVisibility(View.INVISIBLE);
        up_arrow_image_third.setVisibility(View.INVISIBLE);
        if (checkInternet())
            getUserProfileDetail();
    }

    private void showReloadOption() {
        showDataNotFound();
        TextView textView = getDataNotFound();
        if (textView != null) {
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInternet()) {
                        if (profile_btn_layout.getVisibility() == View.VISIBLE) {
                            getUserProfileDetail();
                        } else {
                            getInspirationFeedList();
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (UserPreference.getInstance().getIsRefreshProfile()) {
            refreshProfile();
        }
    }

    private void getStoreList() {
        isLoading = !isLoading;
        mProgressBarDialog = new ProgressBarDialog(mContext);
        if (pagenum > 0)
            showFotter();
        else
            mProgressBarDialog.show();

        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFotter();
                //hideDataNotFound();
                Syso.info("In handleOnSuccess>>" + object);
                isLoading = !isLoading;
                InterestSectionRes interestSectionRes = (InterestSectionRes) object;
                interestList = interestSectionRes.getData();
                if (interestList.size() < 10) {
                    isLoading = true;
                }
                if (interestList.size() == 0 && isFirstTime) {
                }
                //showDataNotFound();
                else if (interestList.size() > 0 && isFirstTime) {
                    // hideDataNotFound();
                    interestStoreListAdapter = new InterestStoreListAdapter(mContext, interestList, fragmentProfileView);
                    collection_list.setAdapter(interestStoreListAdapter);
                } else {
                    interestStoreListAdapter.setData(interestList);
                    interestStoreListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFotter();
                Syso.info("In handleOnFailure>>" + object);
                isLoading = !isLoading;
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.getStoreListFollowed(user_id, Integer.toString(pagenum), UserPreference.getInstance().getUserID());
        interestSectionApi.execute();

        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                interestSectionApi.cancel();
            }
        });
    }

    public void unFollowStore(String id, final View v) {
        v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.unFollowStore(UserPreference.getInstance().getUserID(), id);
        interestSectionApi.execute();

        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                interestSectionApi.cancel();
            }
        });
    }

    public void followStore(String id, final View v) {
        v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (v.findViewById(R.id.checkImageView) != null) {
                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                }
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.followStore(UserPreference.getInstance().getUserID(), id);
        interestSectionApi.execute();

        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                interestSectionApi.cancel();
            }
        });
    }

    private void getBrandList() {
        isLoading = !isLoading;
        mProgressBarDialog = new ProgressBarDialog(mContext);
        if (pagenum > 0)
            showFotter();
        else
            mProgressBarDialog.show();
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFotter();
                hideDataNotFound();
                isLoading = !isLoading;
                Syso.info("In handleOnSuccess>>" + object);
                InterestSectionRes interestSectionApi = (InterestSectionRes) object;
                interestList = interestSectionApi.getData();
                if (interestList.size() < 10) {
                    isLoading = true;
                }
                if (interestList.size() == 0 && isFirstTime) {
                }
                //  showDataNotFound();
                else if (interestList.size() > 0 && isFirstTime) {
                    // hideDataNotFound();
                    interestBrandListAdapter = new InterestBrandListAdapter(mContext, interestList, fragmentProfileView);
                    collection_list.setAdapter(interestBrandListAdapter);
                } else {
                    interestBrandListAdapter.setData(interestList);
                    interestBrandListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFotter();
                Syso.info("In handleOnFailure>>" + object);
                isLoading = !isLoading;
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.getBrandListFollowed(user_id, Integer.toString(pagenum), UserPreference.getInstance().getUserID());
        interestSectionApi.execute();
        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                interestSectionApi.cancel();
            }
        });
    }

    public void deleteBrand(String brand_id, final View v) {
//		v.findViewById(R.id.followButton).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    BrandListRes response = (BrandListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        listApi.deleteBrand(brand_id);
        listApi.execute();
    }

    public void addBrand(String brand_id, final View v) {
//		v.findViewById(R.id.followButton).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    BrandListRes response = (BrandListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        listApi.addBrands(brand_id);
        listApi.execute();
    }

    private void getInspirationFeedList() {

        mProgressBarDialog = new ProgressBarDialog(mContext);
        isLoading = !isLoading;


        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                if (!isFirstTime) {
                    hideFotter();
                } else {
                    loadingTextView.setVisibility(View.GONE);
                    mProgressBarDialog.dismiss();
                }
                // hideDataNotFound();
                isLoading = !isLoading;
                Syso.info("In handleOnSuccess>>" + object);
                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
                product_list.addAll(inspirationFeedRes.getData());
                if (inspirationFeedRes.getData().size() < 10) {
                    isLoading = true;
                }
                if (product_list.size() == 0 && isFirstTime) {
                    // showDataNotFound();
                }
                if (product_list.size() > 0 && isFirstTime) {
                    inspirationAdapter = new InspirationGridAdapter(mContext, product_list);
                    imagesList.setAdapter(inspirationAdapter);
                    collection_list.setVisibility(View.GONE);
                    imagesList.setVisibility(View.VISIBLE);
                    photo_not_found.setVisibility(View.GONE);
                } else if (inspirationAdapter != null) {
                    inspirationAdapter.setData(product_list);
                    inspirationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (!isFirstTime) {
                    imagesList.removeView(loaderView);
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
        });
        inspirationFeedApi.getInspirationFeed(user_id, false, String.valueOf(page), UserPreference.getInstance().getUserID());
        inspirationFeedApi.execute();

//		mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
////				showDataNotFound();
//				inspirationFeedApi.cancel();
//			}
//		});
        if (!isFirstTime) {
            showFotter();
        }
        else {
            loadingTextView.setVisibility(View.VISIBLE);
            mProgressBarDialog.show();
            getUserProfileDetail();
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
}
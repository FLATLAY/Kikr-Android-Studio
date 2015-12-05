package com.kikr.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.TextUtils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.EditProfileActivity;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.FragmentProfileCollectionAdapter;
import com.kikr.adapter.FragmentProfileFollowersAdapter;
import com.kikr.adapter.FragmentProfileFollowingAdapter;
import com.kikr.adapter.InterestBrandListAdapter;
import com.kikr.adapter.InterestCategoryListAdapter;
import com.kikr.adapter.InterestStoreListAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.BrandListApi;
import com.kikrlib.api.CheckBrandStoreFollowStatusApi;
import com.kikrlib.api.CheckPointsStatusApi;
import com.kikrlib.api.FollowUserApi;
import com.kikrlib.api.InterestSectionApi;
import com.kikrlib.api.MyProfileApi;
import com.kikrlib.bean.BrandList;
import com.kikrlib.bean.FollowerList;
import com.kikrlib.bean.InterestSection;
import com.kikrlib.bean.ProfileCollectionList;
import com.kikrlib.bean.UserData;
import com.kikrlib.db.HelpPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.BrandListRes;
import com.kikrlib.service.res.CheckBrandStoreFollowStatusRes;
import com.kikrlib.service.res.CheckPointStatusRes;
import com.kikrlib.service.res.FollowUserRes;
import com.kikrlib.service.res.InterestSectionRes;
import com.kikrlib.service.res.MyProfileRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentProfileView extends BaseFragment implements OnClickListener,ServiceCallback
{
	private View mainView;
	private ListView collection_list;
	private Button collection_button,follower_button,following_button,follow_btn;
	private ImageView up_arrow_image_first,up_arrow_image_second,up_arrow_image_third,user_profile_image;
	private ProgressBarDialog mProgressBarDialog;
	private List<UserData> userDetails;
	private List<ProfileCollectionList> collectionLists=new ArrayList<ProfileCollectionList>();
	private List<FollowerList> followersLists=new ArrayList<FollowerList>();
	private List<FollowerList> followingLists=new ArrayList<FollowerList>();
	private TextView user_profile_name,descriptionTextView;
	private LinearLayout follow_btn_layout;
	private String user_id;
	private boolean isFollowed;
	private ImageView editProfileImageView,bgProfileLayout;
	private FragmentProfileView fragmentProfileView;
	private TextView noCollectionText;
	private String status="";
	
	private boolean isLoading=false;
	private boolean isFirstTime = true;
	private int pagenum = 0;
	private String isSelected = "people";
	private LinearLayout layoutPeopleStoreBrand;
	private Button interest_store_button, interest_brand_button,interest_people_button;
	private List<InterestSection> interestList;
	private InterestBrandListAdapter interestBrandListAdapter;
	private InterestStoreListAdapter interestStoreListAdapter;
	public static String lastUserId;

	private TextView myActivityButton,btn_photos;
	
	public FragmentProfileView(){
		try{
			if(lastUserId!=null){
				this.user_id=lastUserId;
				fragmentProfileView=this;
			}else{
				((HomeActivity)getActivity()).onBackPressed();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public FragmentProfileView(String user_id,String isFollowed) {
		this.user_id=user_id;
		lastUserId = user_id;
		fragmentProfileView=this;
		if(isFollowed!=null&&isFollowed.equals("yes")){
			this.isFollowed=true;
		}else{
			this.isFollowed=false;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_profile, null);
		fragmentProfileView = this;
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		collection_list=(ListView) mainView.findViewById(R.id.collection_list);
		following_button = (Button) mainView.findViewById(R.id.following_button);
		follower_button = (Button) mainView.findViewById(R.id.follower_button);
		collection_button = (Button) mainView.findViewById(R.id.collection_button);
		follow_btn = (Button) mainView.findViewById(R.id.follow_btn);
		up_arrow_image_first = (ImageView) mainView.findViewById(R.id.up_arrow_image_first);
		up_arrow_image_second = (ImageView) mainView.findViewById(R.id.up_arrow_image_second);
		up_arrow_image_third = (ImageView) mainView.findViewById(R.id.up_arrow_image_third);
		user_profile_image = (ImageView) mainView.findViewById(R.id.user_profile_image);
		user_profile_name = (TextView) mainView.findViewById(R.id.user_profile_name);
		follow_btn_layout = (LinearLayout) mainView.findViewById(R.id.follow_btn_layout);
		editProfileImageView=(ImageView) mainView.findViewById(R.id.editProfileImageView);
		bgProfileLayout = (ImageView) mainView.findViewById(R.id.bgProfileLayout);
		noCollectionText = (TextView) mainView.findViewById(R.id.noCollectionText);
		layoutPeopleStoreBrand = (LinearLayout) mainView.findViewById(R.id.layoutPeopleStoreBrand);
		interest_store_button = (Button) mainView.findViewById(R.id.interest_store_button);
		interest_brand_button = (Button) mainView.findViewById(R.id.interest_brand_button);
		interest_people_button = (Button) mainView.findViewById(R.id.interest_people_button);
		myActivityButton = (TextView) mainView.findViewById(R.id.btn_activity);
		btn_photos = (TextView) mainView.findViewById(R.id.btn_photos);
		myActivityButton.setText("Activity");
		descriptionTextView = (TextView) mainView.findViewById(R.id.descriptionTextView);
	}


	@Override
	public void refreshData(Bundle bundle) {
		
	}

	@Override
	public void setClickListener() {
		collection_button.setOnClickListener(this);
		follow_btn.setOnClickListener(this);
		follower_button.setOnClickListener(this);
		following_button.setOnClickListener(this);
		editProfileImageView.setOnClickListener(this);
		interest_store_button.setOnClickListener(this);
		interest_brand_button.setOnClickListener(this);
		interest_people_button.setOnClickListener(this);
		myActivityButton.setOnClickListener(this);
		btn_photos.setOnClickListener(this);
	}
	
	@Override
	public void setData(Bundle bundle) {
		if(checkInternet()){
			getUserProfileDetail();
		}else
			showReloadOption(); 
		
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
			    if(!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
//			    	System.out.println("123456 inside if page"+pagenum+" ,"+isSelected);
			    	if(checkInternet2()){
				    	pagenum++;
				    	isFirstTime = false;
				    	loadData();
			    	}else {
						showReloadFotter();
					}
			    }
			   }
			  });
	}
	
	public void loadData(){
		if (isSelected.equalsIgnoreCase("store")) {
			getStoreList();
		} else if (isSelected.equalsIgnoreCase("brand")) {
			getBrandList();
		}
	}
	
	protected void showReloadFotter() {
		TextView textView=getReloadFotter();
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkInternet()){
					pagenum++;
			    	isFirstTime = false;
			    	loadData();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_activity:
				addFragment(new FragmentActivityMonths());
			break;
		case R.id.btn_photos:
				addFragment(new FragmentInspirationSection(false,UserPreference.getInstance().getUserID()));
				break;
		case R.id.collection_button:
			if (!HelpPreference.getInstance().getHelpCollection().equals("yes") && collectionLists.size() == 0)
				showDataNotFound();
			else if (status.equals("no") && collectionLists.size() == 0
					&& user_id.equals(UserPreference.getInstance().getUserID())) {
				hideDataNotFound();
				noCollectionText.setVisibility(View.VISIBLE);
			}
			else {
				hideDataNotFound();
				collection_list.setAdapter(new FragmentProfileCollectionAdapter(mContext,collectionLists,user_id,fragmentProfileView));
			}
			up_arrow_image_first.setVisibility(View.VISIBLE);
			up_arrow_image_second.setVisibility(View.INVISIBLE);
			up_arrow_image_third.setVisibility(View.INVISIBLE);
			layoutPeopleStoreBrand.setVisibility(View.GONE);
			break;
		case R.id.follower_button:
			if (followersLists.size() == 0)
				showDataNotFound();
			else{
				hideDataNotFound();
				collection_list.setAdapter(new FragmentProfileFollowersAdapter(mContext, followersLists));
			}
			up_arrow_image_first.setVisibility(View.INVISIBLE);
			up_arrow_image_second.setVisibility(View.VISIBLE);
			up_arrow_image_third.setVisibility(View.INVISIBLE);
			layoutPeopleStoreBrand.setVisibility(View.GONE);
			break;
		case R.id.following_button:
			if (followingLists.size() == 0)
				showDataNotFound();
			else{
				hideDataNotFound();
				collection_list.setAdapter(new FragmentProfileFollowingAdapter(mContext, followingLists));
			}
			up_arrow_image_first.setVisibility(View.INVISIBLE);
			up_arrow_image_second.setVisibility(View.INVISIBLE);
			up_arrow_image_third.setVisibility(View.VISIBLE);
			layoutPeopleStoreBrand.setVisibility(View.VISIBLE);
			break;
		case R.id.follow_btn:
			if(checkInternet()){
				if(follow_btn.getText().toString().equals("Follow"))
					followUser();
				else
					unFollowUser();
			}
			break;
		case R.id.editProfileImageView:
			Bundle bundle=new Bundle();
			bundle.putString("username", userDetails.get(0).getUsername());
			bundle.putString("profilePic", userDetails.get(0).getProfile_pic());
			bundle.putString("bgPic", userDetails.get(0).getBackground_pic());
			bundle.putString("description", userDetails.get(0).getDescription());
			bundle.putBoolean("is_edit_profile", true);
			bundle.putString("from", AppConstants.FROM_PROFILE);
			startActivityForResult(EditProfileActivity.class,bundle, 1000);
			break;
		case R.id.interest_store_button:
			isFirstTime = true;
			pagenum = 0;
			isSelected ="store";
			isLoading=false;
			collection_list.setAdapter(null);
			interest_store_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
			interest_brand_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			interest_people_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));

			if(checkInternet())
			{
				getStoreList();
			}
			else
				showReloadOption();
				
			break;
		case R.id.interest_brand_button:
			pagenum = 0;
			isFirstTime = true;
			isSelected ="brand";
			isLoading=false;
			collection_list.setAdapter(null);
			interest_store_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			interest_brand_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
			interest_people_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			
			if(checkInternet())
			{
				getBrandList();
			}
			else
				showReloadOption();

			break;
		case R.id.interest_people_button:
			isFirstTime = true;
			pagenum = 0;
			isLoading=false;
			isSelected ="people";
			collection_list.setAdapter(null);
			interest_store_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			interest_brand_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			interest_people_button.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
	
			if(checkInternet()){
				if (followingLists.size() == 0)
					showDataNotFound();
				else{
					hideDataNotFound();
					collection_list.setAdapter(new FragmentProfileFollowingAdapter(mContext, followingLists));
				}
			}
			else
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
					isFollowed=true;
					follow_btn.setText("Following");
					follow_btn.setBackground(getResources().getDrawable(R.drawable.blue_corner_button));
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
			followUserApi.followUser(UserPreference.getInstance().getUserID(),user_id);
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
					isFollowed=false;
					follow_btn.setText("Follow");
					follow_btn.setBackground(getResources().getDrawable(R.drawable.gray_corner_button));
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
			followUserApi.unFollowUser(UserPreference.getInstance().getUserID(),user_id);
			followUserApi.execute();
			
			mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					followUserApi.cancel();
				}
			});
		}
		

	private void getUserProfileDetail() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final MyProfileApi myProfileApi = new MyProfileApi(this);
		myProfileApi.getUserProfileDetail(user_id,UserPreference.getInstance().getUserID());
		myProfileApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				myProfileApi.cancel();
			}
		});
	}
	
	@Override
	public void handleOnSuccess(Object object) {
		mProgressBarDialog.dismiss();
		getCollectionStatus();
		hideDataNotFound();
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
		CheckPointsStatusApi checkPointsStatusApi = new CheckPointsStatusApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				CheckPointStatusRes pointStatusRes = (CheckPointStatusRes) object;
				status = pointStatusRes.getStatus();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				
			}
		});
		checkPointsStatusApi.checkcollectionpointstatus(UserPreference.getInstance().getUserID());
		checkPointsStatusApi.execute();
	}

	private void setDetails() {
		System.out.println("userDetails.get(0).getProfile_pic()    "+userDetails.get(0).getProfile_pic());
		if(!userDetails.get(0).getProfile_pic().equals(""))
			CommonUtility.setImage(mContext, userDetails.get(0).getProfile_pic(), user_profile_image, R.drawable.dum_user);
		if(!userDetails.get(0).getBackground_pic().equals(""))
			CommonUtility.setImage(mContext, userDetails.get(0).getBackground_pic(), bgProfileLayout, R.drawable.dum_list_item_product);
		
		if(userDetails.get(0).getUsername()!=null&&!userDetails.get(0).getUsername().equals(""))
			user_profile_name.setText(userDetails.get(0).getUsername());
		else
			user_profile_name.setText("Unknown");
		
		if(!TextUtils.isEmpty(userDetails.get(0).getDescription())){
			descriptionTextView.setText(userDetails.get(0).getDescription());
		}
		
		 if (status.equals("no") && 
				collectionLists.size() == 0
				&& user_id.equals(UserPreference.getInstance().getUserID())) {
			hideDataNotFound();
			noCollectionText.setVisibility(View.VISIBLE);
		}else if (collectionLists.size() == 0)
			showDataNotFound();
		 else
		collection_list.setAdapter(new FragmentProfileCollectionAdapter(mContext,collectionLists,user_id,fragmentProfileView));
		
		collection_button.setText(collectionLists.size()+"\nCollections");
		follower_button.setText(followersLists.size()+"\nFollowers");
		following_button.setText(followingLists.size()+"\nFollowing");
		if(userDetails.get(0).getIs_followed()!=null&&userDetails.get(0).getIs_followed().equals("yes")){
			this.isFollowed=true;
		}else{
			this.isFollowed=false;
		}
		if (userDetails.get(0).getId().equalsIgnoreCase(UserPreference.getInstance().getUserID())) {
			myActivityButton.setVisibility(View.VISIBLE);
			btn_photos.setVisibility(View.VISIBLE);
			follow_btn_layout.setVisibility(View.GONE);
			editProfileImageView.setVisibility(View.VISIBLE);
		} else if (isFollowed) {
			btn_photos.setVisibility(View.GONE);
			myActivityButton.setVisibility(View.GONE);
			follow_btn_layout.setVisibility(View.VISIBLE);
			follow_btn.setText("Following");
			follow_btn.setBackground(getResources().getDrawable(R.drawable.blue_corner_button));
		}else{
			btn_photos.setVisibility(View.GONE);
			myActivityButton.setVisibility(View.GONE);
			follow_btn_layout.setVisibility(View.VISIBLE);
			follow_btn.setText("Follow");
			follow_btn.setBackground(getResources().getDrawable(R.drawable.gray_corner_button));
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
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
		if(resultCode==Activity.RESULT_OK&&requestCode==1000){
			refreshProfile();
		}
	}
	
	public void refreshProfile(){
		up_arrow_image_first.setVisibility(View.VISIBLE);
		up_arrow_image_second.setVisibility(View.INVISIBLE);
		up_arrow_image_third.setVisibility(View.INVISIBLE);
		if(checkInternet())
			getUserProfileDetail();
	}
	
	private void showReloadOption() {
		showDataNotFound();
		TextView textView=getDataNotFound();
		if(textView!=null){
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternet())
						getUserProfileDetail();
				}
			});
		}
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(UserPreference.getInstance().getIsRefreshProfile()){
			refreshProfile();
		}
	}
	
	private void getStoreList() {
		isLoading=!isLoading;
		mProgressBarDialog = new ProgressBarDialog(mContext);
		if(pagenum>0)
			showFotter();
		else
			mProgressBarDialog.show();
		
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				hideDataNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				isLoading=!isLoading;
				InterestSectionRes interestSectionRes = (InterestSectionRes) object;
				interestList = interestSectionRes.getData();
				if(interestList.size()<10){
					isLoading=true;
				}
				if (interestList.size() == 0 && isFirstTime)
					showDataNotFound();
				else if (interestList.size() > 0 && isFirstTime) {
					hideDataNotFound();
					interestStoreListAdapter = new InterestStoreListAdapter(mContext,interestList,fragmentProfileView);
					collection_list.setAdapter(interestStoreListAdapter);
				} else{
					interestStoreListAdapter.setData(interestList);
					interestStoreListAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				Syso.info("In handleOnFailure>>" + object);
				isLoading=!isLoading;
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
				isLoading=!isLoading;
				interestSectionApi.cancel();
			}
		});
	}
	
	public void unFollowStore(String id,final View v) {
		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if (v.findViewById(R.id.checkImageView)!=null) {
				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if (v.findViewById(R.id.checkImageView)!=null) {
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
		interestSectionApi.unFollowStore(UserPreference.getInstance().getUserID(),id);
		interestSectionApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				interestSectionApi.cancel();
			}
		});
	}
	
	public void followStore(String id,final View v) {
		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if (v.findViewById(R.id.checkImageView)!=null) {
					v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
				}
				}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if (v.findViewById(R.id.checkImageView)!=null) {
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
		interestSectionApi.followStore(UserPreference.getInstance().getUserID(),id);
		interestSectionApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				interestSectionApi.cancel();
			}
		});
	}
	
	private void getBrandList() {
		isLoading=!isLoading;
		mProgressBarDialog = new ProgressBarDialog(mContext);
		if(pagenum>0)
			showFotter();
		else
			mProgressBarDialog.show();
		final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				hideDataNotFound();
				isLoading=!isLoading;
				Syso.info("In handleOnSuccess>>" + object);
				InterestSectionRes interestSectionApi = (InterestSectionRes) object;
				interestList = interestSectionApi.getData();
				if(interestList.size()<10){
					isLoading=true;
				}
				if (interestList.size() == 0 && isFirstTime)
					showDataNotFound();
				else if (interestList.size() > 0 && isFirstTime) {
					hideDataNotFound();
					interestBrandListAdapter = new InterestBrandListAdapter(mContext,interestList,fragmentProfileView);
					collection_list.setAdapter(interestBrandListAdapter);
				} else {
					interestBrandListAdapter.setData(interestList);
					interestBrandListAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if(mProgressBarDialog.isShowing())
					mProgressBarDialog.dismiss();
				else
					hideFotter();
				Syso.info("In handleOnFailure>>" + object);
				isLoading=!isLoading;
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
				isLoading=!isLoading;
				interestSectionApi.cancel();
			}
		});
	}
	
	public void deleteBrand(String brand_id,final View v) {
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
	
	public void addBrand(String brand_id,final View v) {
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
	
}

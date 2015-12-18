package com.kikr.fragment;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kikr.BaseFragment;
import com.kikr.KikrApp;
import com.kikr.KikrApp.TrackerName;
import com.kikr.R;
import com.kikr.adapter.AutocompleteCustomArrayAdapter;
import com.kikr.dialog.CreateAccountDialog;
import com.kikr.dialog.DeleteCommentDialog;
import com.kikr.dialog.EditInspirationDialog;
import com.kikr.ui.CustomAutoCompleteView;
import com.kikr.ui.InspirationCommentsUI;
import com.kikr.ui.InspirationProductUI;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.ui.RoundImageView;
import com.kikr.ui.TagView;
import com.kikr.utility.AppConstants;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.InspirationSectionApi;
import com.kikrlib.bean.Comment;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.SearchUser;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.InspirationRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentInspirationDetail extends BaseFragment implements OnClickListener{
	
	private View mainView;
	private ImageView inspirationImage;
	private RoundImageView userImage;
	private TextView userName,inspirationTime,commentCount,likeCount,descriptionText;
	private LinearLayout commentList;
	private Button commentBtn;
	private ProgressBarDialog mProgressBarDialog;
	private Inspiration inspiration;
	private CustomAutoCompleteView commentEditText;
	private boolean isFirstTime = true;
	private String likeId = "";
	private List<Comment> list = new ArrayList<Comment>();
	private FragmentInspirationDetail fragmentInspirationDetail;
	private AutocompleteCustomArrayAdapter adapter;
	private ArrayList<SearchUser> usersList = new ArrayList<SearchUser>();
	public ArrayList<String> temp_usersList = new ArrayList<String>();
	private RelativeLayout imageLayout;
	private ProgressBar progressBar;
	private ScrollView scrollView;
	private Bitmap bitmap;
	private FrameLayout frameLayout;
	private ImageView tagImageView;
	private ProgressBar progressBar2;
	private float scaleFactor=1;
	private ProgressBar likeProgressBar;
	private LinearLayout user_profile_layout,productInflaterLayout;
	private ImageView edit_post;
	private String inspiration_id;
	private boolean mHasDoubleClicked = false;
	boolean isShowProducts;
	
	public FragmentInspirationDetail(Inspiration inspiration,boolean isShowProducts) {
		this.inspiration = inspiration;
		this.isShowProducts = isShowProducts;
	}
	
	public FragmentInspirationDetail(String id) {
		this.inspiration_id = id;
	}
	
	String lastSelected="";
	String lastSelected2="";
	String lastUserInput="";
	String lastUserInput2="";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_inspiration_detail, null);
		fragmentInspirationDetail = this;
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		edit_post = (ImageView) mainView.findViewById(R.id.edit_post);
		imageLayout = (RelativeLayout) mainView.findViewById(R.id.imageLayout);
		inspirationImage = (ImageView) mainView.findViewById(R.id.inspirationImage);
		userImage = (RoundImageView) mainView.findViewById(R.id.userImage);
		userName = (TextView) mainView.findViewById(R.id.userName);
		inspirationTime = (TextView) mainView.findViewById(R.id.inspirationTime);
		commentCount = (TextView) mainView.findViewById(R.id.commentCount);
		likeCount = (TextView) mainView.findViewById(R.id.likeCount);
		descriptionText = (TextView) mainView.findViewById(R.id.descriptionText);
		commentList = (LinearLayout) mainView.findViewById(R.id.commentList);
		commentBtn = (Button) mainView.findViewById(R.id.commentBtn);
		commentEditText = (CustomAutoCompleteView) mainView.findViewById(R.id.commentEditText);
		progressBar=(ProgressBar) mainView.findViewById(R.id.progressBar);
		scrollView=(ScrollView) mainView.findViewById(R.id.scrollView);
		likeProgressBar=(ProgressBar) mainView.findViewById(R.id.likeProgressBar);
		user_profile_layout = (LinearLayout) mainView.findViewById(R.id.user_profile_layout);
		frameLayout=(FrameLayout) mainView.findViewById(R.id.overlayView);
		productInflaterLayout = (LinearLayout) mainView.findViewById(R.id.productInflaterLayout);
		Tracker t = ((KikrApp) mContext.getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Fragment Inspiration Detail");
		t.send(new HitBuilders.ScreenViewBuilder().build());
	}

	@Override
	public void setData(Bundle bundle) {
		if (checkInternet()) {
			getInspirationDetails();
		}
		inspirationImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findDoubleClick();
				if( UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == ""){
					CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
					createAccountDialog.show();
				}else{
				    if (mHasDoubleClicked) {
				    	if(checkInternet()){
							if(likeProgressBar.getVisibility()==View.GONE){
								likeProgressBar.setVisibility(View.VISIBLE);
								likeCount.setVisibility(View.GONE);
								likeInspiration();
							}
						}
				    }
				}
				
			}
		});
	}
	
	
	long lastPressTime = 0;
	private boolean findDoubleClick() {
        // Get current time in nano seconds.
    long pressTime = System.currentTimeMillis();
        // If double click...
        if (pressTime - lastPressTime <= 200) {
            mHasDoubleClicked = true;           

            // double click event....
        } else { // If not double click....
            mHasDoubleClicked = false;
            Handler myHandler = new Handler() {
                public void handleMessage(Message m) {

                    if (!mHasDoubleClicked) {
                    	Bitmap b = ((BitmapDrawable)inspirationImage.getDrawable()).getBitmap();
        				int w = b.getWidth();
        				int h = b.getHeight();
        				Syso.info("12345678 Image width:"+w+", height:"+h);
        				
        				if(getInt(inspiration.getImage_width())>w||getInt(inspiration.getImage_height())>h){
        					if(bitmap==null){
        						new GetBitmapFromUrl().execute(inspiration.getInspiration_image());
        					}else{
        						//progressBar2.setVisibility(View.GONE);
        						showDataOnImage();
        					}
        				}else{
        					//progressBar2.setVisibility(View.GONE);
        					showTagOnImage();
        				}
                    }
                }
            };
            Message m = new Message();
            myHandler.sendMessageDelayed(m, 200);
        }
        lastPressTime = pressTime;
        return mHasDoubleClicked;
    }
	
	private void setProductPoints(FrameLayout frameLayout) {
		if (!TextUtils.isEmpty(inspiration.getProduct_xy())) {
		String[] points = inspiration.getProduct_xy().split("-");
		for (int i = 0; i < points.length; i++) {
			String[] xy = points[i].split(",");
//			TextView textView = new TextView(mContext);
			TagView tagView=new TagView(mContext);
//			textView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			frameLayout.addView(tagView);
//			textView.setBackgroundColor(getResources().getColor(R.color.black));
//			textView.setTextColor(getResources().getColor(R.color.white));
//			textView.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
//			int widht = textView.getMeasuredWidth();
//			textView.setX(Float.parseFloat(xy[0])-widht/2);
//			textView.setY(Float.parseFloat(xy[1]));
			try{
			tagView.setXY(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
			}catch(Exception e){
				e.printStackTrace();
			}
//			tagView.setText(inspiration.getProduct_name().split(",")[i]);
			if (inspiration.getProduct_name().split(AppConstants.NAME_SEPRATER).length>i&&!TextUtils.isEmpty(inspiration.getProduct_name().split(AppConstants.NAME_SEPRATER)[i])) {
//				if (inspiration.getProduct_name().split(",")[i].length()>15) 
//					tagView.setText(inspiration.getProduct_name().split(",")[i].substring(0,15)+"...");
//				else
				tagView.setTagText(inspiration.getProduct_name().split(AppConstants.NAME_SEPRATER)[i]);
			}
		}
		}
	}

	private void setItemPoint(FrameLayout frameLayout) {
//		TextView textView= new TextView(mContext);
		TagView tagView=new TagView(mContext);
//		textView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		if (!TextUtils.isEmpty(inspiration.getItem_name())) 
			tagView.setTagText(inspiration.getItem_name());
		else
			tagView.setTagText("Unknown");
		if (!TextUtils.isEmpty(inspiration.getItem_xy())) {
//			textView.measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
//			int widht = textView.getMeasuredWidth();
//			textView.setX(Float.parseFloat(inspiration.getItem_xy().split(",")[0])-widht/2);
//			textView.setY(Float.parseFloat(inspiration.getItem_xy().split(",")[1]));
//			textView.setBackgroundColor(getResources().getColor(R.color.black));
//			textView.setTextColor(getResources().getColor(R.color.white));
			tagView.setXY(Float.parseFloat(inspiration.getItem_xy().split(",")[0])/scaleFactor, Float.parseFloat(inspiration.getItem_xy().split(",")[1])/scaleFactor);
			frameLayout.addView(tagView);
		}	
		
	}

	private void getUsersList() {
		
		final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				if (object!=null) {
					InspirationRes inspirationRes = (InspirationRes) object;
					setDetails(inspirationRes);
					list = inspirationRes.getComment();
					for (int i = 0; i < list.size(); i++) {
						SearchUser searchUser=new SearchUser();
						searchUser.setUserId(list.get(i).getUser_id());
						searchUser.setUsername(list.get(i).getUser_name());
						addSearchUser(searchUser);
					}
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					InspirationRes response = (InspirationRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		inspirationSectionApi.getInspirationDetail(UserPreference.getInstance().getUserID(),inspiration.getInspiration_id(),"inspiration");
		inspirationSectionApi.execute();
	}

	protected void addSearchUser(SearchUser searchUser) {
		boolean isContain=false;
		for(int i=0;i<usersList.size();i++){
			if(usersList.get(i).getUserId().equals(searchUser.getUserId()))
				isContain=true;
		}
		if(!isContain)
			usersList.add(searchUser);
	}

	private void getInspirationDetails() {
//		mProgressBarDialog = new ProgressBarDialog(mContext);
		if (isFirstTime) {
//			mProgressBarDialog.show();
			progressBar.setVisibility(View.VISIBLE);
		}
		final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				progressBar.setVisibility(View.GONE);
				Syso.info("In handleOnSuccess>>" + object);
				if (object!=null) {
					InspirationRes inspirationRes = (InspirationRes) object;
					setDetails(inspirationRes);
					list = inspirationRes.getComment();
					commentList.removeAllViews();
					if (list.size()>=0) {
						commentList.addView(new InspirationCommentsUI(mContext,list,fragmentInspirationDetail).getView());
					}
					if (inspiration==null) {
						inspiration = inspirationRes.getInspiration();
					}
					setDetails();
				}
			
				if (isFirstTime) {
					isFirstTime = false;
//					mProgressBarDialog.dismiss();
				}else{
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						scrollView.fullScroll(View.FOCUS_DOWN);
					}
				}, 10);
				}
				
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if (isFirstTime) {
					isFirstTime = false;
//					mProgressBarDialog.dismiss();
					progressBar.setVisibility(View.GONE);
				}
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					InspirationRes response = (InspirationRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		if (inspiration!=null) 
			inspirationSectionApi.getInspirationDetail(UserPreference.getInstance().getUserID(),inspiration.getInspiration_id(),"inspiration");
		else
			inspirationSectionApi.getInspirationDetail(UserPreference.getInstance().getUserID(),inspiration_id,"inspiration");
		inspirationSectionApi.execute();
	}
	
	protected void setDetails() {
		if (!TextUtils.isEmpty(inspiration.getUsername())) {
			userName.setText(inspiration.getUsername());
		} else {
			userName.setText("Unknown");
		}
		descriptionText.setText(inspiration.getDescription());
		CommonUtility.setImage(mContext, inspiration.getProfile_pic(), userImage, R.drawable.dum_user);
		CommonUtility.setImage(mContext, inspiration.getInspiration_image(), inspirationImage, R.drawable.dum_list_item_product);
		if (inspiration.getUser_id().equals(UserPreference.getInstance().getUserID())) {
			edit_post.setVisibility(View.VISIBLE);
		} else {
			edit_post.setVisibility(View.GONE);
		}
		Calendar calLocal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		Calendar calServer  = Calendar.getInstance();
		try {
			Date dd = df.parse(inspiration.getDateadded());
			calServer.setTime(dd);
//			calServer.setTime(df.parse(inspiration.getDateadded()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		inspirationTime.setText(CommonUtility.calculateTimeDiff(calServer, calLocal));
		getUsersList();
		commentEditText.addTextChangedListener(new CustomAutoCompleteTextChangedListener(mContext));
		adapter = new AutocompleteCustomArrayAdapter(mContext, R.layout.list_item, temp_usersList);
		commentEditText.setAdapter(adapter);
		
		commentEditText.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				Syso.info("In onItemClick :"+arg2);
				String selectedText=temp_usersList.get(arg2);
//				Syso.info("last selected: "+lastSelected+" ,selectedText: "+selectedText);
				commentEditText.setText(lastSelected2.trim()+" @"+selectedText);
				commentEditText.setSelection(commentEditText.getText().toString().length());
				temp_usersList.clear();
				adapter.notifyDataSetChanged();
			}
		});		
//		if(isShowProducts)
		if(inspiration.getPoducts()!=null && inspiration.getPoducts().size()>0)
			productInflaterLayout.addView(new InspirationProductUI(mContext, inspiration,null).getView());
	}

	private void setDetails(InspirationRes inspirationRes) {
		commentCount.setText(inspirationRes.getComment_count());
		likeCount.setText(inspirationRes.getLike_count());
		likeId = "";
		if (!TextUtils.isEmpty(inspirationRes.getLike_id())) 
		likeId = inspirationRes.getLike_id();
		if (TextUtils.isEmpty(likeId))
		likeCount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flame_logo_36, 0, 0);
		else 
		likeCount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flame_logo_36, 0, 0);
	}

	@Override
	public void refreshData(Bundle bundle) {
	}

	@Override
	public void setClickListener() {
		userImage.setOnClickListener(this);
		commentBtn.setOnClickListener(this);
		likeCount.setOnClickListener(this);
		user_profile_layout.setOnClickListener(this);
		edit_post.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commentBtn:
			if( UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == ""){
				CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
				createAccountDialog.show();
			}else{
				CommonUtility.hideSoftKeyboard(mContext);
				validateUserInput();
			}
			break;
		case R.id.likeCount:
			if( UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == ""){
				CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
				createAccountDialog.show();
			}else{
				if(checkInternet()){
					if(likeProgressBar.getVisibility()==View.GONE){
						likeProgressBar.setVisibility(View.VISIBLE);
						likeCount.setVisibility(View.GONE);
						likeInspiration();
					}
				}
			}
			break;
		case R.id.user_profile_layout:
			if(checkInternet()){
				addFragment(new FragmentProfileView(inspiration.getUser_id(), "no"));
			}
			break;
		case R.id.edit_post:
			EditInspirationDialog editInspirationDialog = new EditInspirationDialog(mContext, inspiration);
			editInspirationDialog.show();
			break;
		default:
			break;
		}
	}
	
	public void setDescription() {
		descriptionText.setText(inspiration.getDescription());
	}
	
	private void likeInspiration() {
//		mProgressBarDialog = new ProgressBarDialog(mContext);
//		mProgressBarDialog.show();
//		progressBar.setVisibility(View.VISIBLE);
		final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
//				mProgressBarDialog.dismiss();
//				progressBar.setVisibility(View.GONE);
				
				likeProgressBar.setVisibility(View.GONE);
				likeCount.setVisibility(View.VISIBLE);
				
				InspirationRes inspirationRes=(InspirationRes) object;
				likeId=inspirationRes.getLike_id();
				
				if (TextUtils.isEmpty(likeId)){
					likeCount.setText((getInt(likeCount.getText().toString().trim())-1)+"");
					likeCount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flame_logo_36, 0, 0);
				}
				else {
					likeCount.setText((getInt(likeCount.getText().toString().trim())+1)+"");
					likeCount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flame_logo_36, 0, 0);
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
//				mProgressBarDialog.dismiss();
//				progressBar.setVisibility(View.GONE);
				likeProgressBar.setVisibility(View.GONE);
				likeCount.setVisibility(View.VISIBLE);
			}
		});
		if (TextUtils.isEmpty(likeId))
		inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), inspiration.getInspiration_id(), "inspiration");
		else 
		inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), likeId);
		inspirationSectionApi.execute();
	}

	public void goToNext(){
	}
	
	private void validateUserInput() {
		boolean isValid = true;
		String comment = commentEditText.getText().toString().trim();
			if (TextUtils.isEmpty(comment)) {
			isValid = false;
			commentEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_no_comment_entered);
		} 
		if (isValid&&checkInternet()) {
			if(inspiration!=null)
				postComment(comment);
			else
				AlertUtils.showToast(mContext, "Data loading, please wait");
		}
	}
	
	
	private void postComment(final String comment) {
//		mProgressBarDialog = new ProgressBarDialog(mContext);
//		mProgressBarDialog.show();
		progressBar.setVisibility(View.VISIBLE);
		final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
//				mProgressBarDialog.dismiss();
				progressBar.setVisibility(View.GONE);
				commentEditText.setText("");
				if (checkInternet()) {
					getInspirationDetails();
					getUsersList();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
//				mProgressBarDialog.dismiss();
				progressBar.setVisibility(View.GONE);
			}
		});
		inspirationSectionApi.postComment(UserPreference.getInstance().getUserID(), inspiration.getInspiration_id(), "inspiration", comment);
		inspirationSectionApi.execute();
	}
	
	public void removeComment(String commentId) {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				if (checkInternet()) {
					getInspirationDetails();
					getUsersList();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
			}
		});
		inspirationSectionApi.removeComment(UserPreference.getInstance().getUserID(), commentId);
		inspirationSectionApi.execute();
	}

	public void showRemovePopup(String commentId) {
		DeleteCommentDialog deleteCommentDialog = new DeleteCommentDialog(mContext, fragmentInspirationDetail,commentId);
		deleteCommentDialog.show();
	}
	
	public class CustomAutoCompleteTextChangedListener implements TextWatcher {

		public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
		Context context;

		public CustomAutoCompleteTextChangedListener(Context context) {
			this.context = context;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence i, int start, int before, int count) {
			String input=i+"$";
			String[] data= input.toString().split(" ");
			String userInput;
			if(data[data.length-1].equals("$")){
				userInput="";
			}else{
				userInput=data[data.length-1];
				userInput=userInput.substring(0, userInput.length()-1);
			}
			lastUserInput2=lastUserInput;
			lastUserInput=userInput;
			lastSelected2=lastSelected;
			lastSelected=input.toString().substring(0, input.length()-userInput.length()-1);
//			Syso.info("Input: "+input+" ,userInput:"+userInput+", lastSelected:"+lastSelected);
			if (userInput.toString().startsWith("@")) {
				try {
					userInput = userInput.toString().replace("@", "").trim();
					// if you want to see in the logcat what the user types
					Log.e(TAG, "User input: " + userInput); // update the
															// adapater
					temp_usersList.clear();
					Syso.info("temp_usersList :  "+temp_usersList);
					adapter.notifyDataSetChanged();
					for (SearchUser ss : usersList) {
							if (ss != null) {
								if (ss.getUsername().toUpperCase().contains(
										userInput.toString().toUpperCase())) {
									temp_usersList.add(ss.getUsername());
								}
						}
					}
//					Syso.info("temp_usersList :  "+temp_usersList);
					adapter.notifyDataSetChanged();
					adapter = new AutocompleteCustomArrayAdapter(mContext,
							R.layout.list_item, temp_usersList);
					commentEditText.setAdapter(adapter);
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(lastUserInput2.length()>0&&!lastUserInput2.contains("@")){
				temp_usersList.clear();
				adapter.notifyDataSetChanged();
			}
		}

	}
	
	
	protected void showFullImage() {
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawableResource(R.color.lightshadow);
		dialog.setContentView(R.layout.user_image_view);
		tagImageView = (ImageView) dialog.findViewById(R.id.imageView);
		frameLayout=(FrameLayout) dialog.findViewById(R.id.overlayView);
		progressBar2=(ProgressBar) dialog.findViewById(R.id.progressBar);
		CommonUtility.setImage(mContext, inspiration.getInspiration_image(), tagImageView, R.drawable.dum_list_item_product);
		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.parantDialog);
		layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		Bitmap b = ((BitmapDrawable)tagImageView.getDrawable()).getBitmap();
		int w = b.getWidth();
		int h = b.getHeight();
		Syso.info("12345678 Image width:"+w+", height:"+h);
		
		if(getInt(inspiration.getImage_width())>w||getInt(inspiration.getImage_height())>h){
			if(bitmap==null){
				new GetBitmapFromUrl().execute(inspiration.getInspiration_image());
			}else{
				progressBar2.setVisibility(View.GONE);
				showDataOnImage();
			}
		}else{
			progressBar2.setVisibility(View.GONE);
			showTagOnImage();
		}
//		setItemPoint(frameLayout);
//		setProductPoints(frameLayout);
		dialog.show();
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = dialog.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		window.setAttributes(lp);
	}
		
	
	private int getInt(String image_width) {
		try{
			return Integer.parseInt(image_width);
		}catch(Exception e){
			return 0;
		}
	}

	private class GetBitmapFromUrl extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			try{
				URL url = new URL(params[0]);
				return  BitmapFactory.decodeStream(url.openConnection().getInputStream());
			}catch(Exception e){
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progressBar2!=null) {
				progressBar2.setVisibility(View.GONE);
			}
			if(result!=null){
//				int w = result.getWidth();
//				int h = result.getHeight();
//				Syso.info("12345678 Image width:"+w+", height:"+h);
//				view.setImageBitmap(result);
				bitmap=result;
				showDataOnImage();
			}
		}
	}
	
	private void showDataOnImage() {
//		tagImageView.setImageBitmap(bitmap);
		inspirationImage.setImageBitmap(bitmap);
		setScalingFactor();
		showTagOnImage();
	}
	
	private void showTagOnImage(){
		setItemPoint(frameLayout);
		setProductPoints(frameLayout);
	}
	
	private void setScalingFactor() {
		float dWidth=CommonUtility.getDeviceWidth(mContext);
		float dHeight=CommonUtility.getDeviceHeight(mContext);
		float imgWidth=bitmap.getWidth();
		float imgHeight=bitmap.getHeight();
		if(imgWidth>dWidth){
			scaleFactor=imgWidth/dWidth;
		}
	}

}
package com.kikr.activity;

import java.io.File;
import java.util.ArrayList;

import twitter4j.User;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.dialog.EditUserDescriptionDialog;
import com.kikr.sessionstore.SessionStore;
import com.kikr.twitter.TwitterOAuthActivity;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.ui.RoundImageView;
import com.kikr.utility.AppConstants;
import com.kikr.utility.AppConstants.Screen;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.FontUtility;
import com.kikr.utility.InstagramCallBack;
import com.kikr.utility.InstagramUtility;
import com.kikr.utility.PictureUtils;
import com.kikrlib.api.EditProfileApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.utils.AlertUtils;

public class EditProfileActivity extends BaseActivity implements OnClickListener, OnKeyListener{
	private ProgressBarDialog mProgressBarDialog;
	private Button mDoneButton;
	private RoundImageView user_profile_image;
	private EditText usernameEditText;
	private Bitmap mImageBitmap;
	private Bitmap mBgImageBitmap;
	private String profilePic="" ;
	private String bgPic="" ;
	private String oldUsername="" ;
	private String newUsername="" ;
	private boolean isEditProfile=false;
	private ImageView bgImageView;
	private String currentSelectedImage="profile";
	private EditProfileActivity editProfileActivity;
	private boolean needToUpdateName=false;
	private boolean needToUpdatePic=false;
	private boolean needToUpdateBg=false;
	private final static int REQUEST_CODE_FB_PIC = 1004;
	private final static int REQUEST_CODE_TWIT_PIC = 1005;
	private ProgressBarDialog progressBarDialog;
	private String from="";
	private Uri picUri;
	final int CROP_PIC = 1006;
	private TextView kikr_learn_more;
	private TextView textViewOutside,descriptionTextView;
	private RelativeLayout layoutPictures;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent().hasExtra("is_edit_profile")){
			isEditProfile=getIntent().getBooleanExtra("is_edit_profile", false);
		}else{
			UserPreference.getInstance().setCurrentScreen(Screen.UserNameScreen);
			updateScreen(Screen.UserNameScreen);
		}
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_edit_profile);
		editProfileActivity=this;
		if(getIntent().hasExtra("from")){
			from=getIntent().getStringExtra("from");
		}
	}

	public void gotoDefaultList(){
		startActivityForResult(new Intent(context,DefaultLifestyleImagesActivity.class).putExtra("isDefault", true), PictureUtils.REQUEST_CODE_DEFAULT_LIST);
	}
	public void gotoInstgramList(){
		startActivityForResult(new Intent(context,DefaultLifestyleImagesActivity.class).putExtra("isDefault", false), PictureUtils.REQUEST_CODE_DEFAULT_LIST);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.user_profile_image:
			currentSelectedImage="profile";
			PictureUtils.showAddPictureAlert(context,editProfileActivity);
			break;
		case R.id.usernameEditText:
			break;
		case R.id.mDoneButton:
			validateUserInput();
			break;
		case R.id.leftTextView:
			CommonUtility.hideSoftKeyboard(context);
			finish();
			break;
		case R.id.bgImageView:
			currentSelectedImage="bg";
			PictureUtils.showAddPictureAlert2(context,editProfileActivity);
			break;
		case R.id.kikr_learn_more:
			Intent intent = new Intent(this, LearnMoreOutsideUSActivity.class);
			startActivity(intent);
			break;
		case R.id.descriptionTextView:
			String text = descriptionTextView.getText().toString();
			String desc = text.equalsIgnoreCase(getResources().getString(R.string.add_description))?"":text;
			EditUserDescriptionDialog dialog = new EditUserDescriptionDialog(context,desc,descriptionTextView);
			dialog.show();
			break;
		}
	}
	
	@Override
	public void initLayout() {
		mDoneButton = (Button) findViewById(R.id.mDoneButton);
		usernameEditText = (EditText) findViewById(R.id.usernameEditText);
		user_profile_image = (RoundImageView) findViewById(R.id.user_profile_image);
		bgImageView=(ImageView) findViewById(R.id.bgImageView);
		oldUsername = usernameEditText.getText().toString().trim();
		descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
		kikr_learn_more = (TextView) findViewById(R.id.kikr_learn_more);
		kikr_learn_more.setPaintFlags(kikr_learn_more.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		textViewOutside = (TextView) findViewById(R.id.textViewOutside);
		layoutPictures =(RelativeLayout) findViewById(R.id.layoutPictures);
	}

	@Override
	public void setupData() {
		if (getIntent().hasExtra("profilePic")) {
		profilePic = getIntent().getStringExtra("profilePic");
			if (!TextUtils.isEmpty(profilePic)) {
				CommonUtility.setImage(context, profilePic, user_profile_image,R.drawable.dum_user);
				if(!from.equals(AppConstants.FROM_PROFILE))
					needToUpdatePic=true;
			}
		}
		if (getIntent().hasExtra("username")) {
			String username = getIntent().getStringExtra("username");
			if (username != null) {
				usernameEditText.setText(username);
				usernameEditText.setSelection(username.length());
				oldUsername=username;
				if(!from.equals(AppConstants.FROM_PROFILE))
					needToUpdateName=true;
			}
		}
		if (getIntent().hasExtra("bgPic")) {
			bgPic = getIntent().getStringExtra("bgPic");
			if (!TextUtils.isEmpty(bgPic)) {
				CommonUtility.setImage(context, bgPic, bgImageView,R.drawable.dum_list_item_product);
			}
		}
		if(getIntent().hasExtra("is_edit_profile")){
			isEditProfile=getIntent().getBooleanExtra("is_edit_profile", false);
		}
		if (getIntent().hasExtra("description")) {
			String description = getIntent().getStringExtra("description");
			if (description != null) {
				descriptionTextView.setText(description);
			}
		}
	}

	@Override
	public void headerView() {
		if(isEditProfile){
			setBackHeader();
			getLeftTextView().setOnClickListener(this);
			setGoToHome();
		}else{
			hideHeader();
		}
	}

	@Override
	public void setUpTextType() {
		usernameEditText.setTypeface(FontUtility.setProximanovaLight(context));
		mDoneButton.setTypeface(FontUtility.setProximanovaLight(context));
		kikr_learn_more.setTypeface(FontUtility.setProximanovaLight(context));
		textViewOutside.setTypeface(FontUtility.setProximanovaLight(context));
	}

	@Override
	public void setClickListener() {
		mDoneButton.setOnClickListener(this);
		usernameEditText.setOnKeyListener(this);
		user_profile_image.setOnClickListener(this);
		bgImageView.setOnClickListener(this);
		kikr_learn_more.setOnClickListener(this);
		descriptionTextView.setOnClickListener(this);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == PictureUtils.REQUEST_CODE_CAMERA) {
					File file = new File(PictureUtils.createDirectory(),
							PictureUtils.FILE_NAME + String.valueOf(UserPreference.getInstance().getChatImage() + ".png"));
					if (file!=null) {
						if (Build.VERSION.SDK_INT < 22)
						performCrop();
					else {
						Bitmap thePic = PictureUtils.getBitmap(context, file);
						if (thePic != null) {
							mImageBitmap=thePic;
							setImage(thePic);
						}
					}
					}
			} else if (requestCode == PictureUtils.REQUEST_CODE_GALLERY) {
				Uri uri = data.getData();
				if (uri != null) {
					PictureUtils.showGalleryImage(context, uri);
				}
			} else if (requestCode == PictureUtils.REQUEST_CODE_DEFAULT_LIST) {
				needToUpdateBg = true;
				String url = data.getStringExtra("url");
				if (url != null) {
					setDefaultImage(url);
				} 
			}else if (requestCode == REQUEST_CODE_FB_PIC) {
				needToUpdatePic = true;
				String url = data.getStringExtra("profile_pic");
				if (url != null) {
					profilePic = url;
					mImageBitmap=null;
					CommonUtility.setImage(context, url, user_profile_image, R.drawable.dum_user);
				}
			}else if (requestCode == REQUEST_CODE_TWIT_PIC) {
				needToUpdatePic = true;
				profilePic = data.getStringExtra("profile_image_url");
				mImageBitmap=null;
				if (profilePic != null) {
					CommonUtility.setImage(context, profilePic, user_profile_image, R.drawable.dum_user);
				}
			}else if (requestCode == CROP_PIC) {
				// get the returned data
				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic = extras.getParcelable("data");
				setImage(thePic);
			}
		}

//			if (bitmap != null) {
//				//				mImgProfilePic.setScaleType(ScaleType.CENTER_CROP);
//				user_profile_image.setImageBitmap(bitmap);
//				user_profile_image.setTag(bitmap);
//				mImageBitmap = bitmap;
//			} 
//		}
	}

	private void performCrop() {
		// take care of exceptions
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 2);
			cropIntent.putExtra("aspectY", 2);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 512);
			cropIntent.putExtra("outputY", 512);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, CROP_PIC);
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			Toast toast = Toast.makeText(this,
					"This device doesn't support the crop action!",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private void validateUserInput() {
		boolean isValid = true;
		newUsername= usernameEditText.getText().toString().trim();
		if (TextUtils.isEmpty(newUsername)) {
			isValid = false;
			usernameEditText.requestFocus();
			AlertUtils.showToast(context,R.string.alert_blank_username);
		}else if(newUsername.length()>AppConstants.USERNAME_MAX_LENGTH){
			isValid = false;
			usernameEditText.requestFocus();
			AlertUtils.showToast(context,R.string.alert_username_length);
		}
		if (isValid&&checkInternet()) {
			if (newUsername.equals(oldUsername)) {
				needToUpdateName=false;
				upload();
			} else {
				needToUpdateName=true;
				upload();
			}
		}
	}
	
	private void upload(){
		if(needToUpdateName) {
			setResult(RESULT_OK);
			doRegister();
		} else if (needToUpdatePic) {
			setResult(RESULT_OK);
			uploadImage();
		} else if (needToUpdateBg) {
			setResult(RESULT_OK);
			uploadBgImage();
		} else {
			goToNextScreen();
		}
	}

	private void doRegister() {
		mProgressBarDialog = new ProgressBarDialog(context);
		mProgressBarDialog.show();
		final EditProfileApi uploadProfileImage = new EditProfileApi(
				new ServiceCallback() {

					@Override
					public void handleOnSuccess(Object object) {
						mProgressBarDialog.dismiss();
						needToUpdateName=false;
						UserPreference.getInstance().setUserName(newUsername);
						upload();
					}
					@Override
					public void handleOnFailure(ServiceException exception,
							Object object) {
						mProgressBarDialog.dismiss();
					}
				});
			uploadProfileImage.addUsername(UserPreference.getInstance().getUserID(), newUsername);
			uploadProfileImage.execute();
	}
	
	private void uploadImage() {
		final byte[] profileImage = PictureUtils.getByteArray(mImageBitmap);
		mProgressBarDialog = new ProgressBarDialog(context);
		mProgressBarDialog.show();
		final EditProfileApi editProfileApi = new EditProfileApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				needToUpdatePic=false;
				upload();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
			}
		});
		if (profileImage!=null) {
			editProfileApi.uploadImage(UserPreference.getInstance().getUserID(), profileImage);		
		} else if (!TextUtils.isEmpty(profilePic)) {
			editProfileApi.updateProfileImage(UserPreference.getInstance().getUserID(),"", profilePic);
		} else {
			needToUpdatePic=false;
			upload();
		}
		editProfileApi.execute();
	}
	
	private void uploadBgImage() {
		final byte[] bgImage = PictureUtils.getByteArray(mBgImageBitmap);
		mProgressBarDialog = new ProgressBarDialog(context);
		mProgressBarDialog.show();
		final EditProfileApi editProfileApi = new EditProfileApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				needToUpdateBg=false;
				upload();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
			}
		});
		if (bgImage!=null) {
			editProfileApi.uploadBgImage(UserPreference.getInstance().getUserID(), bgImage);		
		} else if (!TextUtils.isEmpty(bgPic)) {
			editProfileApi.updateBgImageUrl(UserPreference.getInstance().getUserID(), bgPic);
		} else {
			needToUpdateBg=false;
			upload();
		}
		editProfileApi.execute();
	}
	
	private void goToNextScreen() {
		if(isEditProfile){
			finish();
			String email = UserPreference.getInstance().getEmail();
			Bundle bundle = new Bundle();
			if (!email.equals("Email")) {
				bundle.putString("email", email);
			} else {
				bundle.putString("email", "");
			}
			if(email == "") {
				Intent intent=new Intent(this,ChangeEmailActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, AppConstants.REQUEST_CODE_EMAIL_CHANGE);
			}
		}else{
			startActivity(FollowCategoriesNewActivity.class);
			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		if(isEditProfile){
			finish();
		}else{
			//
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN)&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
			CommonUtility.hideSoftKeyboard(context);
//			validateUserInput();
			return true;
		}
		return false;
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		try{
		state.putParcelable("bitmap", ((Bitmap) user_profile_image.getTag()));
		}catch(Exception e){
			e.printStackTrace();
		}
		state.putString("user_name", usernameEditText.getText().toString().trim());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Bitmap bitmap = savedInstanceState.getParcelable("bitmap");
		if (bitmap != null) {
			user_profile_image.setTag(bitmap);
			mImageBitmap=bitmap;
			user_profile_image.setImageBitmap(bitmap);
		}
		String username = savedInstanceState.getString("user_name");

		if (!TextUtils.isEmpty(username)) {
			usernameEditText.setText(username);
		}
	}
	
	public void setImage(Bitmap bitmap) {
		if (bitmap != null) {
			if (currentSelectedImage.equalsIgnoreCase("bg")) {
				needToUpdateBg=true;
				mBgImageBitmap=bitmap;
				bgImageView.setImageBitmap(bitmap);
			} else {
				needToUpdatePic=true;
				user_profile_image.setImageBitmap(bitmap);
				mImageBitmap = bitmap;
				user_profile_image.setTag(bitmap);
			}
		}
	}
	
	public void setDefaultImage(String url){
			bgPic = url;
			mBgImageBitmap=null;
			CommonUtility.setImage(context, url, bgImageView, R.drawable.dum_list_item_product);
	}
	
	public void getFBProfilePic(){
		if(checkInternet()){
			Intent i = new Intent(context, FbSignActivity.class);
			i.putExtra("getProfilePic", true);
			i.putExtra("getFriendList", false);
			startActivityForResult(i, REQUEST_CODE_FB_PIC);
		}
	}
	
	
	
	public void twitterLoogedIn() {
		if(checkInternet()){
			boolean logedIn = SessionStore.isTwitterLogedIn(context);
			if (logedIn) {
				new GetTwitterInfo().execute();
			} else {
				Intent intent = new Intent(context, TwitterOAuthActivity.class);
				intent.putExtra("getProfilePic", true);
				startActivityForResult(intent, REQUEST_CODE_TWIT_PIC);
			}
		}
	}
	
	
	public void instagramLoogedIn() {
		if(checkInternet()){
			InstagramUtility instagramUtility =  new InstagramUtility(context, true, "", false, new InstagramCallBack() {
				@Override
				public void setProfilePic(String url) {
					needToUpdatePic=true;
					mImageBitmap=null;
					profilePic = url;
					CommonUtility.setImage(context, profilePic, user_profile_image, R.drawable.dum_user);
				}
				@Override
				public void setPictureList(ArrayList<String> photoList) {}
			});
			instagramUtility.inItInstgram();
		}
	}
	
	private class GetTwitterInfo extends AsyncTask<Void, Void, User>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBarDialog = new ProgressBarDialog(context);
			progressBarDialog.show();
		}
		@Override
		protected User doInBackground(Void... params) {
			return TwitterOAuthActivity.getUserInfo(context);
		}
		
		@Override
		protected void onPostExecute(User user) {
			super.onPostExecute(user);
			progressBarDialog.dismiss();
			System.out.println("Twitter id>>"+user);
			if(user!=null){
				needToUpdatePic=true;
				mImageBitmap=null;
				profilePic = user.getOriginalProfileImageURL();
				CommonUtility.setImage(context, profilePic, user_profile_image, R.drawable.dum_user);
			}else{
				AlertUtils.showToast(context, "Failed to connect with twitter. Please try again.");
			}
		}
	}
}

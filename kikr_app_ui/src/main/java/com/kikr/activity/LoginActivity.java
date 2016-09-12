package com.kikr.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants.Screen;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.LoginUserApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.RegisterUserResponse;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.DeviceUtils;
import com.kikrlib.utils.StringUtils;
import com.kikrlib.utils.Syso;

public class LoginActivity extends BaseActivity implements OnClickListener, OnKeyListener, ServiceCallback{
	
	private ImageView mBackButton;
	private TextView mForgotPassword;
	private EditText mEmailEditText, mPasswordEditText;
	private ProgressBarDialog progressBarDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_login);
	}

	@Override
	public void initLayout() {
		mBackButton=(ImageView) findViewById(R.id.backArrowImageView);
		mForgotPassword=(TextView) findViewById(R.id.forgotPasswordTextView);
		mEmailEditText = (EditText) findViewById(R.id.emailEditText);
		mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
	}

	@Override
	public void setupData() {}

	@Override
	public void headerView() {
		hideHeader();
	}

	@Override
	public void setUpTextType() {
		
		
	}

	@Override
	public void setClickListener() {
		mBackButton.setOnClickListener(this);
		mForgotPassword.setOnClickListener(this);
		mPasswordEditText.setOnKeyListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.backArrowImageView:
			CommonUtility.hideSoftKeyboard(context);
			goBack();
			break;
		case R.id.forgotPasswordTextView:
			CommonUtility.hideSoftKeyboard(context);
			mEmailEditText.setText("");
			mPasswordEditText.setText("");
			startActivity(ForgotPassword.class);
			break;
		}
	}
	
	
	private void validateUserInput() {

		boolean isValid = true;
		String email = mEmailEditText.getText().toString().trim();
		String password = mPasswordEditText.getText().toString().trim();

		if (email.length() == 0) {
			isValid = false;
			mEmailEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_register_email);
		} else if (!StringUtils.isEmailValid(email)) {
			isValid = false;
			mEmailEditText.requestFocus();
			AlertUtils.showToast(context,R.string.alert_register_email_verification);
		} else if (password.length() == 0) {
			isValid = false;
			mPasswordEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_register_password);
		}

		if (isValid&&checkInternet()) {
			loginViaEamil(email,password);
		}

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN)&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
			CommonUtility.hideSoftKeyboard(context);
			validateUserInput();
			return true;
		}
		return false;
	}
	private void loginViaEamil(String email,String password) {
		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		
		final LoginUserApi service = new LoginUserApi(this);
		service.loginViaEmail(email, password, DeviceUtils.getPhoneModel(),CommonUtility.getDeviceTocken(context),"android",CommonUtility.getDeviceId(context));
		service.execute();
		
		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}
	
	


	@Override
	public void handleOnSuccess(Object object) {
		progressBarDialog.dismiss();
		Syso.info("In handleOnSuccess>>"+object);
		if(object!=null){
			RegisterUserResponse response=(RegisterUserResponse) object;
			String currentScreen=response.getCurrent_screen();
			UserPreference.getInstance().setUserID(response.getId());
			UserPreference.getInstance().setIsCreateWalletPin(true);
			UserPreference.getInstance().setcheckedIsConnected(true);
			
			UserPreference.getInstance().setEmail(response.getEmail());
			UserPreference.getInstance().setAccessToken(response.gettoken());
			UserPreference.getInstance().setUserName(response.getUsername());
			UserPreference.getInstance().setCartID(response.getCart_id());
			UserPreference.getInstance().setProfilePic(response.getProfile_pic());
			UserPreference.getInstance().setBgImage(response.getBackground_pic());
			
			if (!TextUtils.isEmpty(currentScreen)) {
				showHome(currentScreen);
			} else {
				startActivity(HomeActivity.class);
			}
			UserPreference.getInstance().setPassword();
		}
		finish();
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		progressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>"+object);
		if(object!=null){
			RegisterUserResponse response=(RegisterUserResponse) object;
			AlertUtils.showToast(context,response.getMessage());
		}else{
			String invaild= "Entered Invalid Email Id/Password";
			AlertUtils.showToast(context,invaild);

		//	AlertUtils.showToast(context,exception.getErrorMessage());

		}
	}
	
	private void goBack() {
		startActivity(LandingActivity.class);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		goBack();
	}
	
	private void showHome(String currentScreen) {
		Intent i=null;
		if(currentScreen.equals(Screen.HomeScreen)){
			i = new Intent(context,HomeActivity.class);
		}else if(currentScreen.equals(Screen.EmailScreen)){
			i = new Intent(context,EmailActivity.class);
		}else if(currentScreen.equals(Screen.UserNameScreen)){
			i = new Intent(context,EditProfileActivity.class);
		}else if(currentScreen.equals(Screen.CategoryScreen)){
			i = new Intent(context,FollowCategoriesActivity.class);
		}else if(currentScreen.equals(Screen.BrandScreen)){
			i = new Intent(context,FollowBrandsActivity.class);
		}else if(currentScreen.equals(Screen.StoreScreen)){
			i = new Intent(context,FollowStoreActivity.class);
		}else if(currentScreen.equals(Screen.CardScreen)){
			i = new Intent(context,KikrTutorialActivity.class);
		}else{
			i = new Intent(context,HomeActivity.class);
		}
		startActivity(i);
		finish();
	}
	

}

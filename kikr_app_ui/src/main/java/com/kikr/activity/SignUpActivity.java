package com.kikr.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants;
import com.kikr.utility.AppConstants.Screen;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.FontUtility;
import com.kikrlib.api.RegisterUserApi;
import com.kikrlib.db.HelpPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.RegisterUserResponse;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.DeviceUtils;
import com.kikrlib.utils.Syso;
import com.personagraph.api.PGAgent;

public class SignUpActivity extends BaseActivity implements OnClickListener, OnKeyListener ,ServiceCallback{
	private ImageView mBackArrowImageView;
	private EditText mEmailEditText, mPasswordEditText;
	private ProgressBarDialog mProgressBarDialog;
	private Button mNextButton;
	private TextView mMaleTextView,mFemaleTextView,mLoginButton, mRatherNotTextView;
	private boolean mIsMaleSelected=true;
	private boolean ratherNotSelected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_signup);
		setOnKeyListener();
	}

	private void setOnKeyListener() {
		mPasswordEditText.setOnKeyListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.backArrowImageView:
			CommonUtility.hideSoftKeyboard(context);
			goBack();
			break;
		case R.id.nextButton:
			if(CommonUtility.isOnline(context))
			PGAgent.logEvent("SIGNUP_VIA_EMAIL_NEXT_CLICKED");
			CommonUtility.hideSoftKeyboard(context);
			validateUserInput();
			break;
		case R.id.maleTextView:
			mIsMaleSelected=true;
			ratherNotSelected = false;
			mRatherNotTextView.setTextColor(getResources().getColor(R.color.white));
			resetGender();
			break;
		case R.id.femaleTextView:
			mIsMaleSelected=false;
			ratherNotSelected = false;
			mRatherNotTextView.setTextColor(getResources().getColor(R.color.white));
			resetGender();
			break;
		case R.id.ratherNotTextView:
			ratherNotSelected = true;
			resetGender();
			break;
		case R.id.loginButton:
			CommonUtility.hideSoftKeyboard(context);
			startActivity(LoginActivity.class);
			finish();
			break;
		}
	}

	private void goBack() {
		startActivity(LandingActivity.class);
		finish();
	}

	private void resetGender() {
		// TODO Auto-generated method stub
		if(ratherNotSelected) {
			mRatherNotTextView.setTextColor(getResources().getColor(R.color.btn_green));
			
			mFemaleTextView.setTextColor(getResources().getColor(R.color.black));
			mFemaleTextView.setBackgroundResource(R.drawable.simple_ract);
			
			mMaleTextView.setTextColor(getResources().getColor(R.color.black));
			mMaleTextView.setBackgroundResource(R.drawable.simple_ract);
		}
		else if(mIsMaleSelected){
			mMaleTextView.setTextColor(getResources().getColor(R.color.btn_green));
			mFemaleTextView.setTextColor(getResources().getColor(R.color.black));
			mMaleTextView.setBackgroundResource(R.drawable.selected_ract);
			mFemaleTextView.setBackgroundResource(R.drawable.simple_ract);
		}else{
			mMaleTextView.setTextColor(getResources().getColor(R.color.black));
			mFemaleTextView.setTextColor(getResources().getColor(R.color.btn_green));
			mMaleTextView.setBackgroundResource(R.drawable.simple_ract);
			mFemaleTextView.setBackgroundResource(R.drawable.selected_ract);
		}
	}

	@Override
	public void initLayout() {
		mBackArrowImageView = (ImageView) findViewById(R.id.backArrowImageView);
		mEmailEditText = (EditText) findViewById(R.id.emailEditText);
		mPasswordEditText = (EditText) findViewById(R.id.passwordEditText);
		mNextButton=(Button) findViewById(R.id.nextButton);
		mMaleTextView=(TextView) findViewById(R.id.maleTextView);
		mFemaleTextView=(TextView) findViewById(R.id.femaleTextView);
		mLoginButton=(TextView) findViewById(R.id.loginButton);
		mRatherNotTextView = (TextView) findViewById(R.id.ratherNotTextView);
	}

	@Override
	public void setupData() {

	}

	@Override
	public void headerView() {
		hideHeader();
	}

	@Override
	public void setUpTextType() {
//		signUpHeaderTextView.setTypeface(FontUtility.setProximanovaLight(context));
		mEmailEditText.setTypeface(FontUtility.setProximanovaLight(context));
		mPasswordEditText.setTypeface(FontUtility.setProximanovaLight(context));
		mMaleTextView.setTypeface(FontUtility.setProximanovaLight(context));
		mFemaleTextView.setTypeface(FontUtility.setProximanovaLight(context));
		mRatherNotTextView.setTypeface(FontUtility.setProximanovaLight(context));
	}

	@Override
	public void setClickListener() {
		mBackArrowImageView.setOnClickListener(this);
		mNextButton.setOnClickListener(this);
		mMaleTextView.setOnClickListener(this);
		mFemaleTextView.setOnClickListener(this);
		mLoginButton.setOnClickListener(this);
		mRatherNotTextView.setOnClickListener(this);
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

	private void validateUserInput() {

		boolean isValid = true;
		String email = mEmailEditText.getText().toString().trim();
		String password = mPasswordEditText.getText().toString().trim();
		String gender = mIsMaleSelected?"male":"female";
		if(ratherNotSelected)
			gender = "rather not";
		if (email.length() == 0) {
			isValid = false;
			mEmailEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_register_email);
		} else if (!CommonUtility.isEmailValid(email)) {
			isValid = false;
			mEmailEditText.requestFocus();
			AlertUtils.showToast(context,
					R.string.alert_register_email_verification);
		} else if (password.length() == 0) {
			isValid = false;
			mPasswordEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_register_password);
		} else if(password.length()<AppConstants.PASSWORD_MIN_LENGTH){
			isValid = false;
			mPasswordEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_register_password_length);
		}

		if (isValid&&checkInternet()) {
			registerViaEamil(email, password, gender);
		}

	}


	protected void showPinScreen() {
		startActivity(PinAuthActivity.class);
	}
	
	private void registerViaEamil(String email,String password,String gender) {

		mProgressBarDialog = new ProgressBarDialog(context);
		mProgressBarDialog.show();
		
		final RegisterUserApi service = new RegisterUserApi(this);
		service.registerViaEmail(email, password,gender, DeviceUtils.getPhoneModel(),CommonUtility.getDeviceTocken(context),Screen.UserNameScreen,"android", CommonUtility.getDeviceId(context));
		service.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}

	@Override
	public void handleOnSuccess(Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnSuccess>>"+object);
		if(object!=null){
			RegisterUserResponse response=(RegisterUserResponse) object;
			setUserPreferences(response);
			setHelpPreference();
			Bundle bundle=new Bundle();
			bundle.putString("email","");
			startActivity(EditProfileActivity.class,bundle);
		}
		finish();
	}

	private void setUserPreferences(RegisterUserResponse response) {
		UserPreference.getInstance().setUserID(response.getId());
		UserPreference.getInstance().setCurrentScreen(Screen.UserNameScreen);
		UserPreference.getInstance().setAccessToken(response.gettoken());
		UserPreference.getInstance().setIsCreateWalletPin(true);
		UserPreference.getInstance().setcheckedIsConnected(true);
		UserPreference.getInstance().setEmail(mEmailEditText.getText().toString().trim());
		UserPreference.getInstance().setCartID(response.getCart_id());		
		UserPreference.getInstance().setPassword();
	}

	private void setHelpPreference() {
		HelpPreference.getInstance().setHelpCart("yes");
		HelpPreference.getInstance().setHelpCollection("yes");
		HelpPreference.getInstance().setHelpInspiration("yes");
		HelpPreference.getInstance().setHelpKikrCards("yes");
		HelpPreference.getInstance().setHelpPinMenu("yes");
		HelpPreference.getInstance().setHelpSideMenu("yes");
		HelpPreference.getInstance().setHelpFriendsSideMenu("yes");
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>"+object);
		if(object!=null){
			RegisterUserResponse response=(RegisterUserResponse) object;
			AlertUtils.showToast(context,response.getMessage());
		}else{
			AlertUtils.showToast(context,R.string.invalid_response);
		}
	}

	@Override
	public void onBackPressed() {
		goBack();
	}
	
}

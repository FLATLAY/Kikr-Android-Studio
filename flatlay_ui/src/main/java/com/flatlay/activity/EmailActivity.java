package com.flatlay.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlaylib.api.RegisterUserApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.flatlaylib.utils.StringUtils;

public class EmailActivity extends BaseActivity implements OnKeyListener,
		OnClickListener {
	private EditText mEmailEditText;
	private ProgressBarDialog progressBarDialog;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Log.w("Activity:","EmailActivity");
		UserPreference.getInstance().setCurrentScreen(Screen.EmailScreen);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_email);
		checkEmail();
		if(CommonUtility.isOnline(context))
			updateScreen(Screen.EmailScreen);



	}

	@Override
	public void initLayout() {
		mEmailEditText = (EditText) findViewById(R.id.emailEditText);
	}

	@Override
	public void setupData() {}

	@Override
	public void headerView() {
		hideHeader();
	}

	@Override
	public void setUpTextType() {
		mEmailEditText.setTypeface(FontUtility.setProximanovaSemibold(context));
	}

	@Override
	public void setClickListener() {
		mEmailEditText.setOnKeyListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		if ((event.getAction() == KeyEvent.ACTION_DOWN)&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
			validateUserInput();
			return true;
		}
		return false;
	}


	private void checkEmail() {
		if(getIntent().hasExtra("email")){
			String email=getIntent().getStringExtra("email");
			if(email!=null){
				mEmailEditText.setText(email);
				validateUserInput();
			}
		}
	}
	
	private void validateUserInput() {

		boolean isValid = true;
		String email = mEmailEditText.getText().toString().trim();

		if (email.length() == 0) {
			isValid = false;
			mEmailEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_register_email);
		} else if (!StringUtils.isEmailValid(email)) {
			isValid = false;
			mEmailEditText.requestFocus();
			AlertUtils.showToast(context,R.string.alert_register_email_verification);
		} 
		if (isValid&&checkInternet()) {
			doAuthanticate(email);
		}

	}

	private void doAuthanticate(String email) {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final RegisterUserApi service = new RegisterUserApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				String email="",username="",profilepic="";
				if(getIntent().hasExtra("email")){
					email = getIntent().getStringExtra("email");
				}	
				if(getIntent().hasExtra("username")){
					username = getIntent().getStringExtra("username");
				}	
				if(getIntent().hasExtra("profilePic")){
					profilepic = getIntent().getStringExtra("profilePic");
				}	
				Bundle bundle=new Bundle();
				bundle.putString("email",email);
				bundle.putString("username",username);
				bundle.putString("profilePic",profilepic);
				UserPreference.getInstance().setEmail(email);
				startActivity(EditProfileActivity.class,bundle);
				finish();
			}

			@Override
			public void handleOnFailure(ServiceException exception,Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>"+object);
				if(object!=null){
					RegisterUserResponse response=(RegisterUserResponse) object;
					AlertUtils.showToast(context,response.getMessage());
				}else{
					AlertUtils.showToast(context,R.string.invalid_response);
				}
				
			}
		});
		service.updateEmail(UserPreference.getInstance().getUserID(), email);
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}
	
	@Override
	public void onBackPressed() {}
	
}

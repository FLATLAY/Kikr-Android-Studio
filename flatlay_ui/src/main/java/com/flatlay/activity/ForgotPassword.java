package com.flatlay.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.RegisterUserApi;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.flatlaylib.utils.StringUtils;

public class ForgotPassword extends BaseActivity implements OnKeyListener,
		OnClickListener,ServiceCallback {
	private EditText mEmailEditText;
	private ProgressBarDialog progressBarDialog;
	private ImageView backArrowImageView;
	private RelativeLayout signUpHeaderRelativeLayout;
	private String email;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Log.w("Activity:","ForgotPasswordActivity");
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_email);
	}

	@Override
	public void initLayout() {
		mEmailEditText = (EditText) findViewById(R.id.emailEditText);
		backArrowImageView=(ImageView) findViewById(R.id.backArrowImageView);
		signUpHeaderRelativeLayout=(RelativeLayout) findViewById(R.id.signUpHeaderRelativeLayout);
	}

	@Override
	public void setupData() {}

	@Override
	public void headerView() {
		hideHeader();
		signUpHeaderRelativeLayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void setUpTextType() {
		mEmailEditText.setTypeface(FontUtility.setProximanovaSemibold(context));
	}

	@Override
	public void setClickListener() {
		mEmailEditText.setOnKeyListener(this);
		backArrowImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.backArrowImageView:
			CommonUtility.hideSoftKeyboard(context);
			finish();
			break;
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
	
	private void validateUserInput() {

		boolean isValid = true;
		 email = mEmailEditText.getText().toString().trim();

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
		final RegisterUserApi service = new RegisterUserApi(this);
		service.forgotPassword(email);
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
		startActivity(new Intent(this,ResetPassword.class).putExtra("email", email));
		finish();
		AlertUtils.showToast(context, R.string.alert_email_has_sent);
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
}

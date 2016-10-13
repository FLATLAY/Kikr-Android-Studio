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

import com.kikr.BaseActivity;
import com.flatlay.R;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.FontUtility;
import com.kikrlib.api.RegisterUserApi;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.RegisterUserResponse;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class ResetPassword extends BaseActivity implements OnKeyListener,
		OnClickListener, ServiceCallback {

	private EditText resetPinEditText, passwordEditText,confirmPasswordEditText;
	private ProgressBarDialog progressBarDialog;
	private Button resendPinButton;
	boolean resend= false;
	private ImageView backArrowImageView;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_reset_password);
	}

	@Override
	public void initLayout() {
		resetPinEditText = (EditText) findViewById(R.id.resetPinEditText);
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		confirmPasswordEditText = (EditText) findViewById(R.id.confirmPasswordEditText);
		resendPinButton = (Button) findViewById(R.id.resendPinButton);
		backArrowImageView = (ImageView) findViewById(R.id.backArrowImageView);
	}

	@Override
	public void setClickListener() {
		confirmPasswordEditText.setOnKeyListener(this);
		resendPinButton.setOnClickListener(this);
		backArrowImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.resendPinButton:
			if(checkInternet()){
				resend = true;
				doAuthanticate();
			}
			break;
		case R.id.backArrowImageView:
			CommonUtility.hideSoftKeyboard(context);
			finish();
			break;
		}
	}
	
	private void doAuthanticate() {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final RegisterUserApi service = new RegisterUserApi(this);
		service.forgotPassword(getIntent().getStringExtra("email"));
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			validateUserInput();
			return true;
		}
		return false;
	}

	private void validateUserInput() {

		boolean isValid = true;
		String resetpin = resetPinEditText.getText().toString().trim();
		String password = passwordEditText.getText().toString().trim();
		String confirmpassword = confirmPasswordEditText.getText().toString().trim();

		if (resetpin.length() == 0) {
			isValid = false;
			resetPinEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_blank_pin);
		} else if (password.length() == 0) {
			isValid = false;
			passwordEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_blank_password);
		}else if(password.length()<AppConstants.PASSWORD_MIN_LENGTH){
			isValid = false;
			passwordEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_register_password_length);
		} else if (confirmpassword.length() == 0) {
			isValid = false;
			confirmPasswordEditText.requestFocus();
			AlertUtils.showToast(context,R.string.alert_register_confirm_password);
		} else if (!password.equals(confirmpassword)) {
			isValid = false;
			passwordEditText.requestFocus();
			AlertUtils.showToast(context, R.string.alert_password_not_matched);
		}
		if (isValid&&checkInternet()) {
			doAuthanticate(resetpin, password);
		}
	}

	private void doAuthanticate(String resetpin, String password) {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final RegisterUserApi service = new RegisterUserApi(this);
		service.resetPassword(getIntent().getStringExtra("email"), resetpin,
				password);
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
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
		resetPinEditText.setTypeface(FontUtility.setProximanovaLight(context));
		passwordEditText.setTypeface(FontUtility.setProximanovaLight(context));
		confirmPasswordEditText.setTypeface(FontUtility.setProximanovaLight(context));
		resendPinButton.setTypeface(FontUtility.setProximanovaLight(context));
	}

	@Override
	public void handleOnSuccess(Object object) {
		progressBarDialog.dismiss();
		Syso.info("In handleOnSuccess>>" + object);
		if (resend) {
			resend = false;
			AlertUtils.showToast(context, R.string.alert_pin_resent);
		}
		else{
			AlertUtils.showToast(context, R.string.alert_password_reset);
			finish();
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		progressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			RegisterUserResponse response = (RegisterUserResponse) object;
			AlertUtils.showToast(context, response.getMessage());
		} else {
			AlertUtils.showToast(context, R.string.invalid_response);
		}
	}

}

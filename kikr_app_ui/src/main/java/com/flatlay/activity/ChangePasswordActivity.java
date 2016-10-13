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
import com.flatlay.fragment.FragmentSettings;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.kikrlib.api.ChangePasswordApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ChangePasswordRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class ChangePasswordActivity extends BaseActivity implements OnKeyListener,OnClickListener {
	private EditText passwordEditText;
	private ProgressBarDialog progressBarDialog;
	boolean isCurrentPassChecked = false;
	boolean isNewPassChecked = false;
	private String newPassword;
	boolean isCreatePassword = false;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_change_password);
		if(getIntent().hasExtra("isCreatePassword")&&getIntent().getBooleanExtra("isCreatePassword", false)){
			isCurrentPassChecked=true;
			isCreatePassword=true;
			passwordEditText.setHint(getResources().getString(R.string.change_password_newpass_text));
		}
	}

	@Override
	public void initLayout() {
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
	}

	@Override
	public void setupData() {}

	@Override
	public void headerView() {
		setBackHeader();
		getLeftTextView().setOnClickListener(this);
		setGoToHome();
	}

	@Override
	public void setUpTextType() {
		passwordEditText.setTypeface(FontUtility.setProximanovaSemibold(context));
	}

	@Override
	public void setClickListener() {
		passwordEditText.setOnKeyListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.leftTextView:
			back();
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


	private void validate() {
		if (passwordEditText.getText().toString().trim().equals(newPassword)) {
			updateLoginPass(passwordEditText.getText().toString().trim());
		} else {
			AlertUtils.showToast(context, R.string.alert_password_not_matched);
		}		
	}
	
	private void updateLoginPass(String password){
		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final ChangePasswordApi service = new ChangePasswordApi(new ServiceCallback() {

					@Override
					public void handleOnSuccess(Object object) {
						progressBarDialog.dismiss();
						Syso.info("In handleOnSuccess>>" + object);
						if (object != null) {
							ChangePasswordRes response = (ChangePasswordRes) object;
							UserPreference.getInstance().setPassword();
							AlertUtils.showToast(context, response.getMessage());
						}
						Log.e("value first", getIntent().getBooleanExtra("firsttime", false) + "");
						if(getIntent().getBooleanExtra("firsttime", false)) {
							Log.e("value first", getIntent().getBooleanExtra("firsttime", false) + "");
							if(getIntent().getBooleanExtra("firsttime", false)) {
								FragmentSettings.cameFromPassword = 1;
							}
						}
						finish();
					}

					@Override
					public void handleOnFailure(ServiceException exception,Object object) {
						progressBarDialog.dismiss();
						Syso.info("In handleOnFailure>>" + object);
						if (object != null) {
							ChangePasswordRes response = (ChangePasswordRes) object;
							AlertUtils.showToast(context, response.getMessage());
						} else {
							AlertUtils.showToast(context,R.string.invalid_response);
						}

			}
		});
		service.changeLoginPass(UserPreference.getInstance().getUserID(), password);
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}

	private void validateUserInput() {
		boolean isValid = true;
		String password = passwordEditText.getText().toString().trim();

		if (password.length() == 0) {
			isValid = false;
			passwordEditText.requestFocus();
			
			if(isCurrentPassChecked)
				AlertUtils.showToast(context, R.string.alert_password_new_blank);
			else if(isNewPassChecked)
				AlertUtils.showToast(context, R.string.alert_password_confirm_blank);
			else
				AlertUtils.showToast(context, R.string.alert_password_current_blank);
			
		}else if(password.length()<AppConstants.PASSWORD_MIN_LENGTH&&!isNewPassChecked){
			isValid = false;
			passwordEditText.requestFocus();
			if(isCurrentPassChecked)
				AlertUtils.showToast(context, R.string.alert_register_password__new_length);
			else if(isNewPassChecked)
				AlertUtils.showToast(context, R.string.alert_register_password__confirm_length);
			else
				AlertUtils.showToast(context, R.string.alert_register_password_current_length);
		}
		
		if (isValid&&checkInternet()) {
			if (isCurrentPassChecked) {
				 newPassword = passwordEditText.getText().toString().trim();
				 passwordEditText.setText("");
				 passwordEditText.setHint(getResources().getString(R.string.change_password_confirmnewpass_text));
				 passwordEditText.requestFocus();
				 isCurrentPassChecked = false;
				 isNewPassChecked = true;
				} else if(isNewPassChecked){
					validate();
				}else if (!isCurrentPassChecked && !isNewPassChecked){
					doAuthanticate(password);
				}
		}
	}

	private void doAuthanticate(String password) {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final ChangePasswordApi service = new ChangePasswordApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				isCurrentPassChecked = true;
				isNewPassChecked = false;
				passwordEditText.setText("");
				passwordEditText.setHint(getResources().getString(R.string.change_password_newpass_text));
			}

			@Override
			public void handleOnFailure(ServiceException exception,Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>"+object);
				if(object!=null){
					ChangePasswordRes response=(ChangePasswordRes) object;
					AlertUtils.showToast(context,response.getMessage());
				}else{
					AlertUtils.showToast(context,R.string.invalid_response);
				}
			}
		});
		service.checkCurrent(UserPreference.getInstance().getUserID(), password);
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		back(); 
	}
	
	private void back() {
		CommonUtility.hideSoftKeyboard(context);
		if(!isCurrentPassChecked&&!isNewPassChecked){
			finish();
		}else if(isNewPassChecked){
			isNewPassChecked=false;
			isCurrentPassChecked=true;
			passwordEditText.setText("");
			passwordEditText.setText(newPassword);
			passwordEditText.setSelection(newPassword.length());
			newPassword="";
			passwordEditText.setHint(getResources().getString(R.string.change_password_newpass_text));
		}else if(!isCreatePassword){
			isCurrentPassChecked=false;
			passwordEditText.setText("");
			passwordEditText.setHint(getResources().getString(R.string.change_password_currentpass_text));
		}else{
			finish();
		}
	}
}

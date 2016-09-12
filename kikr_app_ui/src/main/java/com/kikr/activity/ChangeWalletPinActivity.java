package com.kikr.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.FontUtility;
import com.kikrlib.api.ChangeWalletPinApi;
import com.kikrlib.api.WalletPinApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ChangeWalletPinRes;
import com.kikrlib.service.res.CommonRes;
import com.kikrlib.service.res.WalletPinRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class ChangeWalletPinActivity extends BaseActivity implements OnKeyListener,OnClickListener {
	private EditText passwordEditText;
	private ProgressBarDialog progressBarDialog;
	boolean isCurrentPinChecked = false;
	boolean isNewPinChecked = false;
	private String newPinEntered;
	boolean isCreatePin = false;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_change_wallet_pin);
		if(getIntent().hasExtra("isCreatePin")&&getIntent().getBooleanExtra("isCreatePin", false)){
			isCurrentPinChecked=true;
			isCreatePin=true;
			passwordEditText.setHint(getResources().getString(R.string.change_walletpin_newpin_text));
		}
	}

	@Override
	public void initLayout() {
		passwordEditText = (EditText) findViewById(R.id.passwordEditText);
		passwordEditText.setHint(getResources().getString(R.string.change_walletpin_currentpin_text));
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
//			if (isCurrentPinChecked) {
//			 newPinEntered = passwordEditText.getText().toString().trim();
//			 passwordEditText.setText("");
//			 passwordEditText.setHint(getResources().getString(R.string.change_walletpin_confirmnewpin_text));
//			 isCurrentPinChecked = false;
//			 isNewPinChecked = true;
//			} else if(isNewPinChecked){
//				validate();
//			}else if (!isCurrentPinChecked && !isNewPinChecked)  {
				validateUserInput();
//			}
			return true;
		}
		return false;
	}


	private void validate() {
		if (passwordEditText.getText().toString().trim().equals(newPinEntered)) {
			updateWalletPin(passwordEditText.getText().toString().trim());
		} else {
			AlertUtils.showToast(context, R.string.alert_pin_not_matched);
		}		
	}
	
	private void updateWalletPin(String pin){
		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final WalletPinApi service = new WalletPinApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				if(object!=null){
//					WalletPinRes response=(WalletPinRes) object;
					AlertUtils.showToast(context,"Pin updated successfully");
					finish();
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception,Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>"+object);
				if(object!=null){
					CommonRes response=(CommonRes) object;
					AlertUtils.showToast(context,response.getMessage());
				}else{
					AlertUtils.showToast(context,R.string.invalid_response);
				}
				
			}
		});
		service.createKikrWalletPin(UserPreference.getInstance().getUserID(), pin);
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
		String pin = passwordEditText.getText().toString().trim();

		if (pin.length() == 0) {
			isValid = false;
			passwordEditText.requestFocus();
			if(isCurrentPinChecked)
				AlertUtils.showToast(context, R.string.alert_pin_new_blank);
			else if(isNewPinChecked)
				AlertUtils.showToast(context, R.string.alert_pin_confirm_new_blank);
			else
				AlertUtils.showToast(context, R.string.alert_pin_current_blank);
		} else if(pin.length()<4&&!isNewPinChecked){
			isValid = false;
			passwordEditText.requestFocus();
			
			if(isCurrentPinChecked)
				AlertUtils.showToast(context, R.string.alert_wallent_new_pin_length);
			else if(isNewPinChecked)
				AlertUtils.showToast(context, R.string.alert_wallent_confirm_pin_length);
			else
				AlertUtils.showToast(context, R.string.alert_wallent_current_pin_length);
		}
		
		if (isValid&&checkInternet()) {
			if (isCurrentPinChecked) {
				newPinEntered = passwordEditText.getText().toString().trim();
				 passwordEditText.setText("");
				 passwordEditText.setHint(getResources().getString(R.string.change_walletpin_confirmnewpin_text));
				 isCurrentPinChecked = false;
				 isNewPinChecked = true;
				} else if(isNewPinChecked){
					validate();
				}else if (!isCurrentPinChecked && !isNewPinChecked){
					doAuthanticate(pin);
				}
		}
	}

	private void doAuthanticate(String pin) {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final ChangeWalletPinApi service = new ChangeWalletPinApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				isCurrentPinChecked = true;
				isNewPinChecked = false;
				passwordEditText.setText("");
				passwordEditText.setHint(getResources().getString(R.string.change_walletpin_newpin_text));
			}

			@Override
			public void handleOnFailure(ServiceException exception,Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>"+object);
				if(object!=null){
					ChangeWalletPinRes response=(ChangeWalletPinRes) object;
					AlertUtils.showToast(context,response.getMessage());
				}else{
					AlertUtils.showToast(context,R.string.invalid_response);
				}
				
			}
		});
		service.checkCurrentPin(UserPreference.getInstance().getUserID(), pin);
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
		if(!isCurrentPinChecked&&!isNewPinChecked){
			finish();
		}else if(isNewPinChecked){
			isNewPinChecked=false;
			isCurrentPinChecked=true;
			passwordEditText.setText("");
			passwordEditText.setText(newPinEntered);
			passwordEditText.setSelection(newPinEntered.length());
			newPinEntered="";
			passwordEditText.setHint(getResources().getString(R.string.change_walletpin_newpin_text));
		}else if(!isCreatePin){
			isCurrentPinChecked=false;
			passwordEditText.setText("");
			passwordEditText.setHint(getResources().getString(R.string.change_walletpin_currentpin_text));
		}else{
			finish();
		}
		
	}

	
}

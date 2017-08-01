package com.flatlay.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.RegisterUserApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class PhoneNumberActivity extends BaseActivity implements OnKeyListener {
	private EditText mPhoneNumberEditText;
	private ProgressBarDialog progressBarDialog;


	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Log.w("Activity","PhoneNumberActivity");
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_phone_number);
		updateScreen(Screen.PhoneNumberScreen);
	}

	@Override
	public void initLayout() {
		mPhoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
	}

	@Override
	public void setupData() {
		if(getIntent().hasExtra("number")){
			String number=getIntent().getStringExtra("number");
			mPhoneNumberEditText.setText(number);
		}
	}

	@Override
	public void headerView() {
		hideHeader();
	}

	@Override
	public void setUpTextType() {

	}

	@Override
	public void setClickListener() {
		mPhoneNumberEditText.setOnKeyListener(this);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		if ((event.getAction() == KeyEvent.ACTION_DOWN)&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
			String number=mPhoneNumberEditText.getText().toString().trim();
			if(number.length()==0){
				AlertUtils.showToast(context, R.string.alert_phone_enter_number);
			}else{
//				startActivity(PinAuthActivity.class);
//				finish();
				if(checkInternet())
					registerViaPhone(number);
			}
			return true;
		}
		return false;
	}

	private void registerViaPhone(String phone_no) {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		
		final RegisterUserApi service = new RegisterUserApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				if(object!=null){
//					AlertUtils.showToast(context,R.string.alert_register_success);
					Bundle bundle=new Bundle();
					bundle.putString("number",mPhoneNumberEditText.getText().toString().trim());
					startActivity(PinAuthActivity.class,bundle);
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
					AlertUtils.showToast(context,R.string.invalid_response);
				}
			}
		});
		service.registerViaNumber(UserPreference.getInstance().getUserID(),phone_no);
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
		AlertUtils.showToast(context, R.string.alert_back_disabled);
	}
}

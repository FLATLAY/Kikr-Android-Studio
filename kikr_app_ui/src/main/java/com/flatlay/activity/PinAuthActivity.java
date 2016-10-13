package com.flatlay.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.kikrlib.api.PinAuthenticationApi;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.PinAuthResponse;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.personagraph.api.PGAgent;

public class PinAuthActivity extends BaseActivity implements OnKeyListener,
		OnClickListener {
	private EditText pinEditText;
	private TextView resendPinTextView;
	private ProgressBarDialog progressBarDialog;
	private ImageView backArrowImageView;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_pin_auth);
	}
	
	@Override
	public void initLayout() {
		pinEditText = (EditText) findViewById(R.id.pinEditText);
		resendPinTextView = (TextView) findViewById(R.id.resendPinTextView);
		backArrowImageView=(ImageView) findViewById(R.id.backArrowImageView);
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
		resendPinTextView.setTypeface(FontUtility.setProximanovaRegular(context));
		pinEditText.setTypeface(FontUtility.setProximanovaSemibold(context));
	}

	@Override
	public void setClickListener() {
		resendPinTextView.setOnClickListener(this);
		pinEditText.setOnKeyListener(this);
		backArrowImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.resendPinTextView:
			CommonUtility.hideSoftKeyboard(context);
			if(checkInternet())
				resendPin();
			break;
		case R.id.backArrowImageView:
			String number="";
			if(getIntent().hasExtra("number")){
				number=getIntent().getStringExtra("number");
			}
			Bundle bundle=new Bundle();
			bundle.putString("number", number);
			startActivity(PhoneNumberActivity.class,bundle);
			finish();
			break;
		}

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		
		if ((event.getAction() == KeyEvent.ACTION_DOWN)
				&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
			CommonUtility.hideSoftKeyboard(context);
			validateUserInput();
			return true;
		}
		return false;
	}

	private void validateUserInput() {

		boolean isValid = true;
		String pin = pinEditText.getText().toString().trim();

		if (pin.length() == 0) {
			isValid = false;
			AlertUtils.showToast(context, R.string.alert_pin_aut_enter_pin);
		}

		if (isValid) {
			if(CommonUtility.isOnline(context))
			PGAgent.logEvent("PIN_FOR_AUTHENTICATE_SENT");
//			startActivity(WalletPinActivity.class);
//			finish();
			if(checkInternet())
				doAuthanticate(pin);
		}

	}

	private void doAuthanticate(String pin) {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final PinAuthenticationApi service = new PinAuthenticationApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				startActivity(FollowCategoriesActivity.class);
				finish();
			}

			@Override
			public void handleOnFailure(ServiceException exception,
					Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>"+object);
				if(object!=null){
					PinAuthResponse response=(PinAuthResponse) object;
					AlertUtils.showToast(context,response.getMessage());
				}else{
					AlertUtils.showToast(context,R.string.invalid_response);
				}
				
			}
		});
		service.authanticatPin(pin);
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}
	
	private void resendPin() {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final PinAuthenticationApi service = new PinAuthenticationApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				AlertUtils.showToast(context,R.string.alert_pin_sent);
			}

			@Override
			public void handleOnFailure(ServiceException exception,
					Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>"+object);
				if(object!=null){
					PinAuthResponse response=(PinAuthResponse) object;
					AlertUtils.showToast(context,response.getMessage());
				}else{
					AlertUtils.showToast(context,R.string.invalid_response);
				}
				
			}
		});
		service.resendPin();
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

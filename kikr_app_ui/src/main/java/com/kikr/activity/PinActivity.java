package com.kikr.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants.Screen;
import com.kikr.utility.CommonUtility;

public class PinActivity extends BaseActivity implements OnKeyListener {

	private EditText firstDigitOfPin, secondDigitOfPin, thirdDigitOfPin,lastDigitOfPin;
	private PinTextWatcher firstTextWatcher, secondTextWatcher,thirdTextWatcher, fourthTextWatcher;
	private Dialog alertDialog;
	private ProgressBarDialog progressBarDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_pin);
		updateScreen(Screen.PinScreen);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
								| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		resetTextBox();
	}

	private class PinTextWatcher implements TextWatcher {

		private View view;

		private PinTextWatcher(View view) {
			this.view = view;
		}

		@Override
		public void beforeTextChanged(CharSequence charSequence, int start,
				int count, int arg2) {
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int arg, int arg1,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable editable) {

			switch (view.getId()) {
			case R.id.first_digit:
				firstDigitOfPin.removeTextChangedListener(this);
				if (editable.length() == 1) {
					secondDigitOfPin.requestFocus();
				}
				firstDigitOfPin.addTextChangedListener(this);
				break;

			case R.id.second_digit:
				secondDigitOfPin.removeTextChangedListener(this);
				if (editable.length() == 1) {
					thirdDigitOfPin.requestFocus();
				}
				secondDigitOfPin.addTextChangedListener(this);
				break;

			case R.id.third_digit:
				thirdDigitOfPin.removeTextChangedListener(this);
				if (editable.length() == 1) {
					lastDigitOfPin.requestFocus();
				}
				thirdDigitOfPin.addTextChangedListener(this);
				break;

			case R.id.last_digit:
				lastDigitOfPin.removeTextChangedListener(this);
				if (editable.length() == 1) {
					String pin=getPinCodeValue();
//					if (getPinCodeValue().equalsIgnoreCase("9999")) {
//						hideKeypad(lastDigitOfPin);
//						startActivityUsingPin();
//					}else 
					if(pin.length()==4){
						hideKeypad(lastDigitOfPin);
						startActivityUsingPin();
//						loginViaPin(pin);
					}
//					else{
//						showWrongPinAlert();
//					}
				}
				lastDigitOfPin.addTextChangedListener(this);
				break;

			default:
				break;
			}

		}
	}

	private String getPinCodeValue() {
		String pinCode = firstDigitOfPin.getText().toString()
				+ secondDigitOfPin.getText().toString()
				+ thirdDigitOfPin.getText().toString()
				+ lastDigitOfPin.getText().toString();
		return pinCode.trim();
	}

	private void startActivityUsingPin() {
		Intent intent = new Intent(context, HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	private void showWrongPinAlert() {
		alertDialog = CommonUtility.showAlert(context, "Wrong PIN",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						resetTextBox();
						alertDialog.dismiss();
					}
				});
	}

	private void resetTextBox() {
		firstDigitOfPin.setText("");
		secondDigitOfPin.setText("");
		thirdDigitOfPin.setText("");
		lastDigitOfPin.setText("");
		firstDigitOfPin.requestFocus();
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		System.out.println("aaaa in onkey press>> view " + view + " keyCode>"
				+ keyCode + " event>" + event);
		if (keyCode != KeyEvent.KEYCODE_DEL
				|| event.getAction() != KeyEvent.ACTION_DOWN) {
			return false;
		}
		switch (view.getId()) {
		case R.id.last_digit:
			if (keyCode == KeyEvent.KEYCODE_DEL
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				lastDigitOfPin.setText("");
				thirdDigitOfPin.requestFocus();
				return true;
			}
			break;
		case R.id.third_digit:
			if (keyCode == KeyEvent.KEYCODE_DEL
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				thirdDigitOfPin.setText("");
				secondDigitOfPin.requestFocus();
				return true;
			}
			break;
		case R.id.second_digit:
			if (keyCode == KeyEvent.KEYCODE_DEL
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				secondDigitOfPin.setText("");
				firstDigitOfPin.requestFocus();
				return true;
			}
		case R.id.first_digit:
			if (keyCode == KeyEvent.KEYCODE_DEL
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				firstDigitOfPin.setText("");
				firstDigitOfPin.requestFocus();
				return true;
			}
			break;
		default:
			break;
		}

		return false;
	}

	@Override
	public void initLayout() {
		firstDigitOfPin = (EditText) findViewById(R.id.first_digit);
		secondDigitOfPin = (EditText) findViewById(R.id.second_digit);
		thirdDigitOfPin = (EditText) findViewById(R.id.third_digit);
		lastDigitOfPin = (EditText) findViewById(R.id.last_digit);
	}

	@Override
	public void setupData() {
		firstTextWatcher = new PinTextWatcher(firstDigitOfPin);
		secondTextWatcher = new PinTextWatcher(secondDigitOfPin);
		thirdTextWatcher = new PinTextWatcher(thirdDigitOfPin);
		fourthTextWatcher = new PinTextWatcher(lastDigitOfPin);

		firstDigitOfPin.addTextChangedListener(firstTextWatcher);
		secondDigitOfPin.addTextChangedListener(secondTextWatcher);
		thirdDigitOfPin.addTextChangedListener(thirdTextWatcher);
		lastDigitOfPin.addTextChangedListener(fourthTextWatcher);

		firstDigitOfPin.setOnKeyListener(this);
		secondDigitOfPin.setOnKeyListener(this);
		thirdDigitOfPin.setOnKeyListener(this);
		lastDigitOfPin.setOnKeyListener(this);
	}

	@Override
	public void headerView() {
		// TODO Auto-generated method stub
		hideHeader();
	}

	@Override
	public void setUpTextType() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setClickListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		firstDigitOfPin.removeTextChangedListener(firstTextWatcher);
		secondDigitOfPin.removeTextChangedListener(secondTextWatcher);
		thirdDigitOfPin.removeTextChangedListener(thirdTextWatcher);
		lastDigitOfPin.removeTextChangedListener(fourthTextWatcher);
		super.onDestroy();
	}

	public void hideKeypad(EditText editView) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editView.getWindowToken(), 0);
	}
	
	private void loginViaPin(String pin) {
		
//		progressBarDialog = new ProgressBarDialog(context);
//		progressBarDialog.show();
//		final LoginApi service = new LoginApi(new ServiceCallback() {
//
//			@Override
//			public void handleOnSuccess(Object object) {
//				progressBarDialog.dismiss();
//				LogUtils.info("In handleOnSuccess>>"+object);
//				startActivity(HomeActivity.class);
//				finish();
//			}
//
//			@Override
//			public void handleOnFailure(ServiceException exception,
//					Object object) {
//				progressBarDialog.dismiss();
//				resetTextBox();
//				LogUtils.info("In handleOnFailure>>"+object);
//				if(object!=null){
//					PinAuthResponse response=(PinAuthResponse) object;
//					AlertUtils.showToast(context,response.getMessage());
//				}else{
//					AlertUtils.showToast(context,R.string.invalid_response);
//				}
//				
//			}
//		});
//		service.loginViaPin(pin,"123");
//		service.execute();
//
//		progressBarDialog.setOnCancelListener(new OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				service.cancel();
//			}
//		});
	}
}

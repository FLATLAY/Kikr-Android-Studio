package com.flatlay.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.WalletPinApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.WalletPinRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class WalletPinActivity extends BaseActivity implements OnClickListener, ServiceCallback{
	private TextView mFirstDigit,mSecondDigit,mThirdDigit,mFourthDigit;
	private ImageView mBackArrowImageView;
	private String mPin="";
	private String mEnterdText="*",mBlankText="-";
	private ProgressBarDialog progressBarDialog;
	private boolean mIsCreatePinScreen=true;
	private TextView pin_header;
	private boolean pinEntered= false;
	private String pin="";
	private TextView forgotWalletPin;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Log.w("Activity","WalletPinActivity");
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_wallet_pin);
	}
	
	@Override
	public void initLayout() {
		pin_header=(TextView) findViewById(R.id.pin_header);
		mFirstDigit=(TextView) findViewById(R.id.firstDigit);
		mSecondDigit=(TextView) findViewById(R.id.secondDigit);
		mThirdDigit=(TextView) findViewById(R.id.thirdDigit);
		mFourthDigit=(TextView) findViewById(R.id.fourthDigit);
		forgotWalletPin=(TextView) findViewById(R.id.forgotWalletPin);
		mBackArrowImageView=(ImageView) findViewById(R.id.backArrowImageView);
		mBackArrowImageView.setOnClickListener(this);
	}

	@Override
	public void setupData() {
		if(getIntent().hasExtra("create"))
			mIsCreatePinScreen=getIntent().getBooleanExtra("create", true);
		if (!mIsCreatePinScreen) {
			pin_header.setText(R.string.enter_pin);
			forgotWalletPin.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void headerView() {
		setBackHeader();
		setGoToHome();
		getLeftTextView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				((HomeActivity)context).go
//				goToHome();
//				FragmentTransaction transaction = getSupportFragmentManager()
//						.beginTransaction();
//				transaction.replace(R.id.frame_container, new FragmentDiscover(),null);
//				transaction.addToBackStack(null);
//				transaction.commit();
//				finish();
				showDiscover();
			}
		});
	}

	@Override
	public void setUpTextType() {
		
		
	}

	@Override
	public void setClickListener() {
		forgotWalletPin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkInternet()){
					forgotKikrWalletPin();
				}
			}
		});
		
	}
	
	public void click(View v){
		String value=v.getTag().toString();
		int length=mPin.length();
		switch(length){
		case 0:
			mPin+=value;
			mFirstDigit.setText(mEnterdText);
			break;
		case 1:
			mPin+=value;
			mSecondDigit.setText(mEnterdText);
			break;
		case 2:
			mPin+=value;
			mThirdDigit.setText(mEnterdText);
			break;
		case 3:
			mPin+=value;
			mFourthDigit.setText(mEnterdText);
			Syso.info("Enterd Pin>>"+mPin);
			if(mIsCreatePinScreen && !pinEntered){
				pin_header.setText(R.string.confirm_pin);
				pin = mPin;
				pinEntered=true;
				clearPin();
			} else if(mIsCreatePinScreen && pinEntered){
				if(mPin.equals(pin)){
					if(checkInternet())
						createKikrWalletPin(mPin);
				}
				else
				     AlertUtils.showToast(context, R.string.alert_pin_not_matched);
			}
			else if(checkInternet())
				authenticateKikrWalletPin(mPin);
			break;
		}
	}

	private void clearPin() {
		mFirstDigit.setText(mBlankText);
		mSecondDigit.setText(mBlankText);
		mThirdDigit.setText(mBlankText);
		mFourthDigit.setText(mBlankText);
		mPin="";
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.backArrowImageView:
			if(mPin.length()>0)
				remove();
			break;
		}
	}

	private void remove() {
		
		int length=mPin.length();
		switch(length){
		case 1:
			mFirstDigit.setText(mBlankText);
			break;
		case 2:
			mSecondDigit.setText(mBlankText);
			break;
		case 3:
			mThirdDigit.setText(mBlankText);
			break;
		case 4:
			mFourthDigit.setText(mBlankText);
			break;
		}
		mPin=mPin.substring(0, mPin.length()-1);
	}

	private void createKikrWalletPin(String kikrpin) {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		
		final WalletPinApi service = new WalletPinApi(this);
		service.createKikrWalletPin(UserPreference.getInstance().getUserID(),kikrpin);
		service.execute();
		
		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}
	

	private void authenticateKikrWalletPin(String kikrpin) {
		
		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		
		final WalletPinApi service = new WalletPinApi(this);
		service.authenticateKikrWalletPin(UserPreference.getInstance().getUserID(),kikrpin);
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
			Intent returnIntent = new Intent();
			UserPreference.getInstance().setIsCreateWalletPin(false);
			setResult(RESULT_OK, returnIntent);
		}
		finish();
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		
		progressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>"+object);
		if(object!=null){
			WalletPinRes response=(WalletPinRes) object;
			resetPin();
			AlertUtils.showToast(context,response.getMessage());
		}else{
			AlertUtils.showToast(context,R.string.invalid_response);
		}
	}
	
	private void resetPin() {
		mPin = "";
		mFirstDigit.setText(mBlankText);
		mSecondDigit.setText(mBlankText);
		mThirdDigit.setText(mBlankText);
		mFourthDigit.setText(mBlankText);
	}

	private void forgotKikrWalletPin() {

		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		
		final WalletPinApi service = new WalletPinApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					WalletPinRes response=(WalletPinRes) object;
					AlertUtils.showToast(context,response.getMessage());
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					WalletPinRes response=(WalletPinRes) object;
					AlertUtils.showToast(context,response.getMessage());
				}else{
					AlertUtils.showToast(context,R.string.invalid_response);
				}
			}
		});
		service.forgotKikrWalletPin(UserPreference.getInstance().getUserID());
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
		showDiscover();
	}
}

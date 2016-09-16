package com.kikr.dialog;

import java.util.ArrayList;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.braintreepayments.api.Braintree;
import com.braintreepayments.api.models.CardBuilder;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.BraintreePaymentApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.BraintreePaymentRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class CreditCardDialog extends Dialog {
	private FragmentActivity mContext;
	private EditText cvvEditText;
	private Button doneButton;
	private ProgressBarDialog mProgressBarDialog;
	private Braintree braintree ;
	ArrayList<String> list = new ArrayList<String>();
	private String cardNumber;
	private String expiryDate;

	public CreditCardDialog(FragmentActivity context,String cardNumber,String expiryDate) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		this.expiryDate = expiryDate;
		this.cardNumber = cardNumber;
		init();
	}

	public CreditCardDialog(FragmentActivity context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}

	private void init() {
		
		setContentView(R.layout.dialog_credit_card);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		setCanceledOnTouchOutside(true);
		getWindow().setAttributes(lp);
		doneButton = (Button) findViewById(R.id.doneButton);
		cvvEditText = (EditText) findViewById(R.id.cvvEditText);
//		list.add("YYYY");
//		for (int i = 2015; i < 2049; i++) {
//			list.add(Integer.toString(i));
//		}
//		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext,
//				android.R.layout.simple_list_item_1, list);
//		adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
//		yearSpinner.setAdapter(adapter2);
		doneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CommonUtility.hideSoftKeyboard(mContext);
				validateUserInput();
			}
		});
	}
	
	private void validateUserInput() {
		boolean isValid = true;
		String cvv = cvvEditText.getText().toString().trim();
//		if (cardNumber.length() == 0) {
//			isValid = false;
//			cardNumberEditText.requestFocus();
//			AlertUtils.showToast(mContext, R.string.alert_cardnumber_blank);
//		}else if (cardNumber.length() < 12) {
//			isValid = false;
//			cardNumberEditText.requestFocus();
//			AlertUtils.showToast(mContext, R.string.alert_cardnumber_short);
//		} else if (expiryDate.length() == 0 || expiryDate.equalsIgnoreCase("MM/YYYY") || expiryDate.contains("MM") || expiryDate.contains("YYYY") ) {
//			isValid = false;
//			AlertUtils.showToast(mContext, R.string.alert_expiry_blank);
//		}else 
			if (cvv.length() < 3) {
			isValid = false;
			cvvEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_cvv_blank);
		} 
		if (isValid&&((HomeActivity)mContext).checkInternet()) {
			Syso.info("wadaw"+cardNumber+expiryDate);
			getTokenfromClient(cvv, cardNumber, expiryDate);
			dismiss();
		}
	}
	
	private void getTokenfromClient(final String cvv,final String cardnumber,final String expiry) {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final BraintreePaymentApi braintreePaymentApi = new BraintreePaymentApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				BraintreePaymentRes braintreePaymentRes = (BraintreePaymentRes)object;
				String clientToken = braintreePaymentRes.getClient_token();
				braintree = Braintree.getInstance(mContext,clientToken);
				AlertUtils.showToast(mContext, "Please wait...");
				braintree.addListener(new Braintree.PaymentMethodNonceListener() {
					public void onPaymentMethodNonce(String paymentMethodNonce) {
						// Communicate the nonce to your server
						saveCheckout(paymentMethodNonce);
					}
				});
				CardBuilder cardBuilder = new CardBuilder().cardNumber(cardnumber).expirationDate(expiry).cvv(cvv); 
				braintree.tokenize(cardBuilder);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
			}
		});
		braintreePaymentApi.getClientToken(UserPreference.getInstance().getUserID(), CommonUtility.getDeviceId(mContext));
		braintreePaymentApi.execute();
	}
	
	private void saveCheckout(String paymentMethodNonce) {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final BraintreePaymentApi braintreePaymentApi = new BraintreePaymentApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				AlertUtils.showToast(mContext,R.string.payment_success_text);
				((HomeActivity) mContext).createCart();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				AlertUtils.showToast(mContext, R.string.payment_fail_text);
			}
		});
		braintreePaymentApi.saveCheckout(UserPreference.getInstance().getUserID(), CommonUtility.getDeviceId(mContext) ,paymentMethodNonce);
		braintreePaymentApi.execute();
	}

}

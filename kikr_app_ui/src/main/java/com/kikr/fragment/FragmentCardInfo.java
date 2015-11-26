package com.kikr.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants;
import com.kikr.utility.CardCrypto;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.Luhn;
import com.kikrlib.api.CardInfoApi;
import com.kikrlib.bean.Card;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CardInfoRes;
import com.kikrlib.utils.AlertUtils;

public class FragmentCardInfo extends BaseFragment implements OnClickListener {
	private View mainView;
	private EditText cardNumberEditText,cardHolderNameEditText,cvvEditText;
	private Spinner monthSpinner,yearSpinner;
	private Button doneBtn;
	private ArrayList<String> list = new ArrayList<String>();
	private ProgressBarDialog progressBarDialog;
	private Card card;
	private BaseFragment baseFragment;
	private boolean isEditEnable=true;
	
	public FragmentCardInfo(BaseFragment baseFragment) {
		this.baseFragment=baseFragment;
		isEditEnable=true;
	}
	
	public FragmentCardInfo(Card card,BaseFragment baseFragment) {
		this.card = card;
		this.baseFragment=baseFragment;
		isEditEnable=false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_card_info, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		cardNumberEditText = (EditText) mainView.findViewById(R.id.cardNumberEditText);
		cardHolderNameEditText = (EditText) mainView.findViewById(R.id.cardHolderNameEditText);
		cvvEditText = (EditText) mainView.findViewById(R.id.cvvEditText);
		monthSpinner = (Spinner) mainView.findViewById(R.id.monthSpinner);
		yearSpinner = (Spinner) mainView.findViewById(R.id.yearSpinner);
		doneBtn = (Button) mainView.findViewById(R.id.doneBtn);
	}

	@Override
	public void refreshData(Bundle bundle) {
	}

	@Override
	public void setClickListener() {
		doneBtn.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
		list.add("YYYY");
		for (int i = 2015; i < 2049; i++) {
			list.add(Integer.toString(i));
		}
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1, list);
		adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
		yearSpinner.setAdapter(adapter2);
		if (card != null) {
			cardNumberEditText.setText(CommonUtility.DecryptCreditCard(card.getCard_number()));
			cardHolderNameEditText.setText(card.getName_on_card());
			cvvEditText.setText(card.getCvv());
			String[] expiry = card.getExpiration_date().split("/");
			if (expiry[0]!=null && expiry[1]!=null) {
				System.out.println(expiry[0]+"sbcjscjas"+expiry[1]);
				monthSpinner.setSelection(Integer.parseInt(expiry[0]));
				if (Integer.parseInt(expiry[1])-2014>=0) {
					yearSpinner.setSelection(Integer.parseInt(expiry[1])-2014);
				}
			}
		}
		if(!isEditEnable){
			doneBtn.setVisibility(View.GONE);
			cardNumberEditText.setFocusable(false);
			cardHolderNameEditText.setFocusable(false);
			cvvEditText.setFocusable(false);
			monthSpinner.setClickable(false);
			yearSpinner.setClickable(false);
			monthSpinner.setFocusable(false);
			yearSpinner.setFocusable(false);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.doneBtn:
			validateUserInput();
			break;
		default:
			break;
		}
	}
	
	private void validateUserInput() {
		boolean isValid = true;
		String cardNumber = cardNumberEditText.getText().toString().trim();
		String cvv = cvvEditText.getText().toString().trim();
		String cardHolderName = cardHolderNameEditText.getText().toString().trim();
		String expiryMonth = monthSpinner.getSelectedItem().toString();
		String expiryYear = yearSpinner.getSelectedItem().toString();
		String cardtype = Luhn.getCardTypeinString(cardNumber);
		if (cardNumber.length() == 0) {
			isValid = false;
			cardNumberEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_cardnumber_blank);
		}else if (!Luhn.isCardValid(cardNumber)) {
			isValid = false;
			cardNumberEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_cardnumber_short);
		}else if (cardHolderName.length() ==0) {
			isValid = false;
			cardHolderNameEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_cardholdername_blank);
		}else if (cvv.length() < 3) {
			isValid = false;
			cvvEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_cvv_blank);
		} else if (expiryMonth.length() == 0 || expiryMonth.contains("MM")) {
			isValid = false;
			AlertUtils.showToast(mContext, R.string.alert_expirymonth_blank);
		}else if (expiryYear.length() == 0 || expiryYear.contains("YYYY") ) {
			isValid = false;
			AlertUtils.showToast(mContext, R.string.alert_expiryyear_blank);
		}else if (TextUtils.isEmpty(cardtype)) {
			isValid = false;
			AlertUtils.showToast(mContext, R.string.alert_card_invalid_blank);
		}
		if (isValid&&((HomeActivity)mContext).checkInternet()) {
			String expiryDate = monthSpinner.getSelectedItem()+"/"+yearSpinner.getSelectedItem();
			CommonUtility.hideSoftKeyboard(mContext);
			addCard(cardNumber,cardHolderName,expiryDate,cvv,cardtype);
		}
	}
	
	private void addCard(String cardNumber,String cardHolderName,String expiryDate,String cvv, String cardtype) {
		progressBarDialog = new ProgressBarDialog(mContext);
		progressBarDialog.show();
		final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				if (object != null) {
					CardInfoRes cardInfoRes = (CardInfoRes) object;
					AlertUtils.showToast(mContext, cardInfoRes.getMessage());
					((HomeActivity)mContext).onBackPressed();
					if (baseFragment!=null&&baseFragment instanceof FragmentSettings) {
						((FragmentSettings) baseFragment).getCardList(false);
					}
					if (baseFragment!=null&&baseFragment instanceof FragmentKikrWalletCard) {
						((FragmentKikrWalletCard) baseFragment).getCardList();
					}
					if (baseFragment!=null&&baseFragment instanceof FragmentPlaceMyOrder) {
						((FragmentPlaceMyOrder) baseFragment).getCardList();
					}
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					CardInfoRes cardInfoRes=(CardInfoRes) object;
					AlertUtils.showToast(mContext, cardInfoRes.getMessage());
				}else{
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		cardInfoApi.addCard(UserPreference.getInstance().getUserID(),CommonUtility.EncryptCreditCard(cardNumber), cardHolderName, expiryDate, cvv,cardtype);
		cardInfoApi.execute();
	}
	

}

package com.flatlay.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlaylib.api.KikrCreditsApi;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.KikrCreditsRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class FragmentTransferKikrCredits extends BaseFragment implements OnClickListener{
	private View mainView;
	private TextView creditsTextView,userSelectBtn;
	private EditText amountEditText;
	private Button submitBtn;
	private int kikrCredits=0;
	private ProgressBarDialog mProgressBarDialog;
	private List<InterestSection> selectedItems=new ArrayList<InterestSection>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_kikr_credits, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		userSelectBtn = (TextView) mainView.findViewById(R.id.userSelectBtn);
		creditsTextView = (TextView) mainView.findViewById(R.id.creditsTextView);
		amountEditText = (EditText) mainView.findViewById(R.id.amountEditText);
		submitBtn = (Button) mainView.findViewById(R.id.submitBtn);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		userSelectBtn.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
		if (checkInternet()) {
			getCredits();
		}
		userSelectBtn.setText(getselectedUserName());
	}

	private void getCredits() {
		final KikrCreditsApi kikrCreditsApi = new KikrCreditsApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				KikrCreditsRes kikrCreditsRes = (KikrCreditsRes) object;
				if (!TextUtils.isEmpty(kikrCreditsRes.getAmount()))
					creditsTextView.setText(kikrCreditsRes.getAmount());
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					KikrCreditsRes response = (KikrCreditsRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		kikrCreditsApi.getKikrCredits(UserPreference.getInstance().getUserID());
		kikrCreditsApi.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.userSelectBtn:
			if (checkInternet()) {
				addFragment(new FragmentTagList("people", selectedItems));
			}
			break;
		case R.id.submitBtn:
			Validate();
			break;
		default:
			break;
		}
	}

	private void Validate() {
		boolean isValid = true;
		String amount = amountEditText.getText().toString().trim();
		if (amount.length()==0) {
			isValid=false;
			amountEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_amount_length);
		}else if (Integer.parseInt(amount)==0) {
			isValid=false;
			amountEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_amount_not_be_zero);
		}else if (Integer.parseInt(amount)>kikrCredits) {
			isValid=false;
			amountEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_amount_less_than_credit);
		}else if (userSelectBtn.getText().toString().trim().length()==0) {
			isValid=false;
			AlertUtils.showToast(mContext, R.string.alert_no_user_selected);
		}
		if (isValid&&checkInternet()) {
			transferMoney();
		}
	}

	private void transferMoney() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final KikrCreditsApi kikrCreditsApi = new KikrCreditsApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>" + object);
				KikrCreditsRes kikrCreditsRes = (KikrCreditsRes) object;
				if (!TextUtils.isEmpty(kikrCreditsRes.getAmount()))
					creditsTextView.setText(kikrCreditsRes.getAmount());
				AlertUtils.showToast(mContext, kikrCreditsRes.getMessage());
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					KikrCreditsRes response = (KikrCreditsRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		kikrCreditsApi.transferKikrCredits(UserPreference.getInstance().getUserID(),amountEditText.getText().toString().trim(), getselectedUserId());
		kikrCreditsApi.execute();
	}

	private String getselectedUserId() {
		String selectedUserId="";
		if (selectedItems.size()>0) {
			selectedUserId = selectedItems.get(0).getId();
		}
		return selectedUserId;
	}
	
	private String getselectedUserName() {
		String selectedUserId="";
		if (selectedItems.size()>0) {
			selectedUserId = selectedItems.get(0).getUsername();
		}
		return selectedUserId;
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		userSelectBtn.setText(getselectedUserName());
	}
	
}

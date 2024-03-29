package com.flatlay.activity;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.adapter.AddBankAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.BankListApi;
import com.flatlaylib.bean.BankList;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.BankListRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class AddBankActivity extends BaseActivity implements OnClickListener, ServiceCallback{

	EditText searchYourBankEditText;
	ListView addBank_List;
	private TextView mHeaderTextView;
	private ImageView backIcon;
	private ProgressBarDialog mProgressBarDialog;
	private AddBankAdapter bankAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("Activity:","AddBankActivity");
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_add_bank);
		if(checkInternet())
			getBankList();
	}
	
	
	private void getBankList() {
		mProgressBarDialog = new ProgressBarDialog(context);
		mProgressBarDialog.show();
		
		final BankListApi listApi = new BankListApi(this);
		listApi.getBankList();
		listApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				listApi.cancel();
			}
		});		
	}

	@Override
	public void initLayout() {
		searchYourBankEditText = (EditText) findViewById(R.id.searchYourBankEditText);
		addBank_List = (ListView) findViewById(R.id.addBank_List);
	}

	@Override
	public void setupData() {
	}
	
	@Override
	public void headerView() {
		setTextHeader();
		mHeaderTextView=getHeaderTextView();
		mHeaderTextView.setText("Popular Accounts");
		getRightTextView().setVisibility(View.GONE);
		backIcon= getLeftImageView();
		backIcon.setVisibility(View.VISIBLE);
		backIcon.setImageResource(R.drawable.ic_back);
	}

	@Override
	public void setUpTextType() {
		
	}

	@Override
	public void setClickListener() {
		backIcon.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIcon:
			super.onBackPressed();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void handleOnSuccess(Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnSuccess>>" + object);
			BankListRes bankResponse=(BankListRes) object;
			List<BankList> banksList=bankResponse.getData();
			if(banksList.size()>0){
				bankAdapter=new AddBankAdapter(context,banksList);
				addBank_List.setAdapter(bankAdapter);
			}
			}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			BankListRes response = (BankListRes) object;
			AlertUtils.showToast(context, response.getMessage());
		} else {
			AlertUtils.showToast(context, R.string.invalid_response);
		}
	}

}

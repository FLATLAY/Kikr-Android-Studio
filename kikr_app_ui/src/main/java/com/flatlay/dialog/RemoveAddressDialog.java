package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.SettingsAddressList;

public class RemoveAddressDialog extends Dialog{
	private TextView cancelTextView,okTextView;
	private FragmentActivity mContext;
	private ProgressBarDialog progressBarDialog;
	private String addressId;
	private SettingsAddressList addressList;

	public RemoveAddressDialog(FragmentActivity mContext,String addressId, SettingsAddressList addressList) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.addressList = addressList;
		this.addressId = addressId;
		init();
	}

	public RemoveAddressDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_remove_address);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		okTextView = (TextView) findViewById(R.id.okTextView);
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(((HomeActivity) mContext).checkInternet()){
					dismiss();
					addressList.deleteAddress(addressId);
				}
			}
		});
	}
}

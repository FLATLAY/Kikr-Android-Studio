package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentSettings;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.ui.SettingsAddressList;
import com.kikrlib.api.AddressApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.AddressRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

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

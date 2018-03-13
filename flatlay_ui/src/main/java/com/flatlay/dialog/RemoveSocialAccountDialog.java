package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlaylib.api.AddressApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.AddressRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class RemoveSocialAccountDialog extends Dialog{
	private TextView cancelTextView,okTextView;
	private Context mContext;
	private String addressId;

	public RemoveSocialAccountDialog(Context mContext) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		init();
	}

	public RemoveSocialAccountDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_remove_social_account);
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
//				deleteAddress();
				dismiss();
			}
		});
	}
	
	private void deleteAddress() {

//		progressBarDialog = new ProgressBarDialog(mContext);
//		progressBarDialog.show();
		final AddressApi service = new AddressApi(new ServiceCallback() {

		
			@Override
			public void handleOnSuccess(Object object) {
				//progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				if(object!=null){
					dismiss();
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception,Object object) {
				//progressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>"+object);
				if(object!=null){
					AddressRes response=(AddressRes) object;
					AlertUtils.showToast(mContext,response.getMessage());
				}else{
					AlertUtils.showToast(mContext,R.string.invalid_response);
				}
				
			}
		});
		service.removeAddress(UserPreference.getInstance().getUserID(), addressId);
		service.execute();

		//progressBarDialog.setOnCancelListener(new OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				service.cancel();
//			}
//		});
	}
	
	
}

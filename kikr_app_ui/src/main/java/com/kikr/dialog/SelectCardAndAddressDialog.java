package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kikr.R;

public class SelectCardAndAddressDialog extends Dialog{
	private TextView cancelTextView,okTextView,messageTextView;
	private Context mContext;

	public SelectCardAndAddressDialog(Context mContext) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		init();
	}

	public SelectCardAndAddressDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_logout);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		messageTextView= (TextView) findViewById(R.id.messageTextView);
		messageTextView.setText("Please select shipping address and card to get the final price of cart.");
		okTextView = (TextView) findViewById(R.id.okTextView);
		cancelTextView.setVisibility(View.GONE);
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}

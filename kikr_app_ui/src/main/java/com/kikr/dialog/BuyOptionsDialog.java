package com.kikr.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.flatlay.R;
import com.kikr.activity.HomeActivity;

public class BuyOptionsDialog extends Dialog{
	private FragmentActivity mContext;
	private TextView paypalText,creditCardText;
	private String productId;

	public BuyOptionsDialog(FragmentActivity context,String productId) {
		super(context, R.style.AdvanceDialogTheme);
		this.mContext = context;
		this.productId = productId;
		init();
	}

	public BuyOptionsDialog(FragmentActivity context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}
	
	private void init() {
		setContentView(R.layout.dialog_buy_options);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		paypalText = (TextView) findViewById(R.id.paypalText);
		creditCardText = (TextView) findViewById(R.id.creditCardText);
		paypalText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
//				((HomeActivity) mContext).startPaypal(productId);
			}
		});
		creditCardText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
//				CreditCardDialog creditCardDialog = new CreditCardDialog(mContext,productId);
//				creditCardDialog.show();
			}
		});
	}
	
}

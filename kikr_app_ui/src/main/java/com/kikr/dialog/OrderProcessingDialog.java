package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentDiscoverNew;
import com.kikr.fragment.FragmentPlaceMyOrder;
import com.kikr.service.PlaceOrderService;

public class OrderProcessingDialog extends Dialog{
	private TextView cancelTextView,okTextView,messageTextView;
	private Context mContext;
	private String message;
	private String purchaseId;
	private String cartId;
	private FragmentPlaceMyOrder fragmentPlaceMyOrder;

	public OrderProcessingDialog(Context mContext,String message, FragmentPlaceMyOrder fragmentPlaceMyOrder, String purchaseId, String cartId) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.purchaseId = purchaseId;
		this.message = message;
		this.fragmentPlaceMyOrder = fragmentPlaceMyOrder;
		this.cartId = cartId;
		init();
	}

	public OrderProcessingDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_logout);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		messageTextView= (TextView) findViewById(R.id.messageTextView);
		messageTextView.setText(message);
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
//				fragmentPlaceMyOrder.purchaseStatus(purchaseId);
				Intent i = new Intent(mContext, PlaceOrderService.class);
				i.putExtra("purchase_id",purchaseId);
				i.putExtra("cartId",cartId);
				mContext.startService(i);
				dismiss();
				((HomeActivity)mContext).loadFragment(new FragmentDiscoverNew());
			}
		});
	}
}

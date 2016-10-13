package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentDiscoverNew;
import com.kikr.fragment.FragmentPlaceMyOrder;
import com.kikr.utility.CommonUtility;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.StringUtils;

public class OrderReadyForConfirmDialog extends Dialog{
	private TextView cancelTextView,okTextView,messageTextView;
	private Context mContext;
	private String finalvalue,purchase_id;
	FragmentPlaceMyOrder fragmentPlaceMyOrder;

	public OrderReadyForConfirmDialog(Context mContext,String finalvalue,FragmentPlaceMyOrder fragmentPlaceMyOrder,String purchase_id) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.finalvalue = finalvalue;
		this.fragmentPlaceMyOrder = fragmentPlaceMyOrder;
		this.purchase_id = purchase_id;
		init();
	}

	public OrderReadyForConfirmDialog(Context context, int theme) {
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
		String oldPrice = CommonUtility.getFormatedNum(UserPreference.getInstance().getFinalPrice());
		messageTextView.setText("Turns out that the previously quoted total $"+oldPrice+" provided by the merchant was not right, the final cost is "+CommonUtility.getFormatedNum(finalvalue)+", Do you want to continue purchase?");
		okTextView = (TextView) findViewById(R.id.okTextView);
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				((HomeActivity)mContext).loadFragment(new FragmentDiscoverNew());
				UserPreference.getInstance().setPurchaseId("");
				UserPreference.getInstance().setFinalPrice("");
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				fragmentPlaceMyOrder.confirmPuchase(purchase_id);
//				((HomeActivity)mContext).loadFragment(new FragmentDiscover());
			}
		});
	}
}

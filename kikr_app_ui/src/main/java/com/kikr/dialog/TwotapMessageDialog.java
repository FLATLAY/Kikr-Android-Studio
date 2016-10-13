package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.CartFragmentTab;
import com.kikr.fragment.FragmentDiscoverNew;

public class TwotapMessageDialog extends Dialog{
	private FragmentActivity mContext;
	private TextView cancelTextView,okTextView;
	private TextView messageTextView;
	private String message;
	boolean isLoadOrderDeatil;
	
	public TwotapMessageDialog(FragmentActivity mContext, String message, boolean isLoadOrderDeatil) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.message = message;
		this.isLoadOrderDeatil = isLoadOrderDeatil;
		init();
	}

	public TwotapMessageDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_remove_old_tag);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		messageTextView= (TextView) findViewById(R.id.messageTextView);
		messageTextView.setText(message);
		setCanceledOnTouchOutside(false);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
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
				if(isLoadOrderDeatil)
					((HomeActivity)mContext).addFragment(new CartFragmentTab(true));
				else
					((HomeActivity)mContext).loadFragment(new FragmentDiscoverNew());
				dismiss();
			}
		});
	}
}

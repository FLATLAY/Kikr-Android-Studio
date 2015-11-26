package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;

public class WelcomeDialog extends Dialog{
	private TextView cancelTextView,okTextView;
	private HomeActivity homeActivity;
	private Context mContext;

	public WelcomeDialog(Context mContext) {
		super(mContext, R.style.AdvanceDialogTheme);
	//	this.homeActivity = homeActivity;
		this.mContext=mContext;
		init();
	}

	public WelcomeDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_welcome);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		okTextView = (TextView) findViewById(R.id.okTextView);
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 dismiss();
			}
		});
	}
	
}

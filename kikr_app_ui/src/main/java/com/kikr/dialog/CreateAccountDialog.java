package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentSettings;

public class CreateAccountDialog extends Dialog{
	private TextView fbTextView, twitterTextView, emailTextView;
	private HomeActivity homeActivity;
	private Context mContext;

	public CreateAccountDialog(Context mContext) {
		super(mContext, R.style.AdvanceDialogTheme);
		//this.homeActivity = homeActivity;
		this.mContext=mContext;
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_createaccount);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
	/*	fbTextView = (TextView) findViewById(R.id.fbTextView);
		fbTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((HomeActivity)mContext).fbLogIn();
				dismiss();
			}
		});
		twitterTextView = (TextView) findViewById(R.id.twitterTextView);
		twitterTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((HomeActivity)mContext).twitterLoogedIn();
				dismiss();
			}
		});*/
		emailTextView = (TextView) findViewById(R.id.emailTextView);
		emailTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//((HomeActivity)mContext).emailLogIn();
				((HomeActivity)mContext).addFragment(new FragmentSettings());
				dismiss();
			}
		});
	}
	
	
}

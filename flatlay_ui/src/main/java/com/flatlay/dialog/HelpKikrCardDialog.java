package com.flatlay.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.flatlay.R;

public class HelpKikrCardDialog extends Dialog{

	private FragmentActivity mContext;

	public HelpKikrCardDialog(FragmentActivity context) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}

	private void init() {
		setContentView(R.layout.helpscreen_kikrcards);
		setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCancelable(true);
	}

}

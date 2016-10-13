package com.kikr.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.flatlay.R;

public class HelpInviteFriendsDialog extends Dialog {

	private FragmentActivity mContext;

	public HelpInviteFriendsDialog(FragmentActivity context) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}

	private void init() {
		setContentView(R.layout.help_invite_friends);
		setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCancelable(true);
	}

}

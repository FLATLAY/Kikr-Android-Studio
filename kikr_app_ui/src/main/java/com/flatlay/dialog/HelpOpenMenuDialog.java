package com.flatlay.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.flatlay.R;

public class HelpOpenMenuDialog extends Dialog{

	private FragmentActivity mContext;
	private ImageView img1;

	public HelpOpenMenuDialog(FragmentActivity context) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}

	private void init() {
		setContentView(R.layout.helpscreen_openmenu);
		setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCancelable(true);
		img1 = (ImageView) findViewById(R.id.img1);
		img1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

}

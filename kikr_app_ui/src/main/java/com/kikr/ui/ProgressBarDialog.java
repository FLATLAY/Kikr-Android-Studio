package com.kikr.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.R;

public class ProgressBarDialog extends Dialog {

	private TextView mTxtMessage;
	private ImageView imageView1;
	boolean isGreen = false;
	private Handler handler = new Handler();
	private Runnable r;

	public ProgressBarDialog(Context context) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	public ProgressBarDialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	private void init() {

		setContentView(R.layout.dialog_progres_bar);
		setCancelable(true);
		
		WindowManager.LayoutParams lp = getWindow().getAttributes();  
		lp.dimAmount=0.45f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
		getWindow().setAttributes(lp);
		mTxtMessage = (TextView) findViewById(R.id.dialogProgress_txtMessage);
		mTxtMessage.setVisibility(View.GONE);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
	    animation.setDuration(300); // duration - half a second
	    animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
	    animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
	    animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
	    imageView1.startAnimation(animation);
	}

	public void setMessage(String message) {
		mTxtMessage.setText(message);
		mTxtMessage.setVisibility(View.VISIBLE);
	}

	public void setMessage(int resId) {
		mTxtMessage.setText(resId);
		mTxtMessage.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void dismiss() {
		try {
			if (this != null && isShowing()) {
				super.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

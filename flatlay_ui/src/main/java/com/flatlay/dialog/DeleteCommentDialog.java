package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentInspirationDetail;

public class DeleteCommentDialog extends Dialog{
	private TextView cancelTextView,okTextView;
	private FragmentActivity mContext;
	private FragmentInspirationDetail fragmentInspirationDetail;
	private String commentId;

	public DeleteCommentDialog(FragmentActivity mContext,FragmentInspirationDetail fragmentInspirationDetail, String commentId) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.commentId = commentId;
		this.fragmentInspirationDetail = fragmentInspirationDetail;
		init();
	}

	public DeleteCommentDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_delete_comment);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		okTextView = (TextView) findViewById(R.id.okTextView);
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(((HomeActivity) mContext).checkInternet()){
					dismiss();
					fragmentInspirationDetail.removeComment(commentId);
				}
			}
		});
	}
	
}

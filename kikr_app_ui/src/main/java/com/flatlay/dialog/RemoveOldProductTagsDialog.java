package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentInspirationImageTag;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.TaggedProducts;

public class RemoveOldProductTagsDialog extends Dialog{
	private FragmentActivity mContext;
	private TextView cancelTextView,okTextView;
	private Inspiration inspiration;

	public RemoveOldProductTagsDialog(FragmentActivity mContext, Inspiration inspiration) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.inspiration = inspiration;
		init();
	}

	public RemoveOldProductTagsDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_remove_old_tag);
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
				dismiss();
				((HomeActivity)mContext).loadFragment(new FragmentInspirationImageTag(inspiration, new TaggedProducts()));
			}
		});
	}
}

package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.SettingsCardList;

public class RemoveCardDialog extends Dialog{
	private TextView cancelTextView,okTextView;
	private Context mContext;
	private ProgressBarDialog progressBarDialog;
	private String cardId;
	private SettingsCardList settingsCardList;

	public RemoveCardDialog(Context mContext,String cardId, SettingsCardList settingsCardList) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.settingsCardList = settingsCardList;
		this.cardId = cardId;
		init();
	}

	public RemoveCardDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_remove_card);
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
				settingsCardList.deleteCard(cardId);
			}
		});
	}
}

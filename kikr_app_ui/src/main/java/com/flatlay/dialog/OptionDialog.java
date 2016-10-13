package com.flatlay.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.flatlay.R;
import com.flatlay.adapter.OptionListAdapter;

public class OptionDialog extends Dialog {
	private FragmentActivity mContext;
	private ListView editCartProductList;
	private OptionListAdapter optionListAdapter;
	private DialogCallback dialogCallback;
	private List<String> optionList = new ArrayList<String>();
	private String selectedOption;

	public OptionDialog(FragmentActivity context,DialogCallback dialogCallback, List<String> optionList, String selectedOption) {
		super(context, R.style.AdvanceDialogTheme);
		this.dialogCallback = dialogCallback;
		mContext = context;
		this.optionList=optionList;
		this.selectedOption = selectedOption;
		init();
	}

	public OptionDialog(FragmentActivity context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}
	
	private void init() {
		setContentView(R.layout.cart_dialog);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		editCartProductList= (ListView) findViewById(R.id.editCartProductList);
		optionListAdapter = new OptionListAdapter(mContext,dialogCallback,optionList,selectedOption);
		editCartProductList.setAdapter(optionListAdapter);
	}

}

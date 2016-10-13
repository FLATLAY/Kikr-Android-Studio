package com.kikr.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.flatlay.R;
import com.kikr.adapter.FitListAdapter;

public class FitDialog extends Dialog {
	private FragmentActivity mContext;
	private ListView editCartProductList;
	FitListAdapter fitListAdapter;
	private DialogCallback dialogCallback;
	private List<String> fitList = new ArrayList<String>();
	private String selectedFit;

	public FitDialog(FragmentActivity context,DialogCallback dialogCallback, List<String> fitList,String selectedFit) {
		super(context, R.style.AdvanceDialogTheme);
		this.dialogCallback = dialogCallback;
		mContext = context;
		this.fitList=fitList;
		this.selectedFit = selectedFit;
		init();
	}

	public FitDialog(FragmentActivity context, int theme) {
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
		fitListAdapter = new FitListAdapter(mContext,dialogCallback,fitList,selectedFit);
		editCartProductList.setAdapter(fitListAdapter);
	}

}

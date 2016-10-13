package com.flatlay.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.adapter.ProductMainOptionListAdapter;
import com.kikrlib.bean.ProductMainOption;

public class ProductMainOptionDialog extends Dialog {
	private FragmentActivity mContext;
	private ListView editCartProductList;
	private ProductMainOptionListAdapter optionListAdapter;
	private DialogCallback dialogCallback;
	private ProductMainOption  optionList;
	private String selectedOption;
	TextView optionTextValue;

	public ProductMainOptionDialog(FragmentActivity context,DialogCallback dialogCallback, ProductMainOption optionList, String selectedOption, TextView optionTextValue) {
		super(context, R.style.AdvanceDialogTheme);
		this.dialogCallback = dialogCallback;
		mContext = context;
		this.optionList=optionList;
		this.selectedOption = selectedOption;
		this.optionTextValue = optionTextValue;
		init();
	}

	public ProductMainOptionDialog(FragmentActivity context, int theme) {
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
		optionListAdapter = new ProductMainOptionListAdapter(this,mContext,dialogCallback,optionList,selectedOption,optionTextValue);
		editCartProductList.setAdapter(optionListAdapter);
	}

}

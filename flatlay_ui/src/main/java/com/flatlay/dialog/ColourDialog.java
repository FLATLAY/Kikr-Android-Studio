package com.flatlay.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.flatlay.R;
import com.flatlay.adapter.ColorListAdapter;

public class ColourDialog extends Dialog {
	private FragmentActivity mContext;
	private ListView editCartProductList;
	ColorListAdapter colorListAdapter;
	private DialogCallback dialogCallback;
	private List<String> colors = new ArrayList<String>();
	private String selectedColor;
	
	public ColourDialog(FragmentActivity context,DialogCallback dialogCallback, List<String> colors, String selectedColor) {
		super(context, R.style.AdvanceDialogTheme);
		this.dialogCallback = dialogCallback;
		mContext = context;
		this.selectedColor=selectedColor;
		this.colors=colors;
		init();
	}

	public ColourDialog(FragmentActivity context, int theme) {
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
		colorListAdapter = new ColorListAdapter(mContext,dialogCallback,colors,selectedColor);
		editCartProductList.setAdapter(colorListAdapter);
	}

}

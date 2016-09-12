package com.kikr.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ListView;

import com.kikr.R;
import com.kikr.adapter.SizeListAdapter;

public class SizeDialog extends Dialog{
	private FragmentActivity mContext;
	private ListView editCartProductList;
	private SizeListAdapter sizeListAdapter;
	private DialogCallback dialogCallback;
	private List<String> size = new ArrayList<String>();
	private String selelctedsize;

	public SizeDialog(FragmentActivity context) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}

	public SizeDialog(FragmentActivity context, DialogCallback dialogCallback, List<String> size,String selelctedsize) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		this.dialogCallback=dialogCallback;
		this.selelctedsize = selelctedsize;
		this.size=size;
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
		sizeListAdapter = new SizeListAdapter(mContext,dialogCallback,size,selelctedsize);
		editCartProductList.setAdapter(sizeListAdapter);
	}
	
}

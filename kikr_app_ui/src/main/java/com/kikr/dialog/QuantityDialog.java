package com.kikr.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.QuantityListAdapter;
import com.kikr.fragment.FragmentEditPurchaseItem;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.Product;
import com.kikrlib.utils.AlertUtils;

public class QuantityDialog extends Dialog{
	private FragmentActivity mContext;
	QuantityDialog collectionListDialog;
	DialogCallback dialogCallback;
	EditText editText;
	Product product;

//	public QuantityDialog(FragmentActivity context) {
//		super(context, R.style.AdvanceDialogTheme);
//		mContext = context;
//		init();
//	}

	public QuantityDialog(FragmentActivity context, DialogCallback dialogCallback, Product product) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		this.dialogCallback = dialogCallback;
		this.product=product;
		init();
	}
	
	private void init() {	
		setContentView(R.layout.dialog_quantity);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		editText= (EditText) findViewById(R.id.quantityEditText);
		TextView cancelTextView=(TextView) findViewById(R.id.cancelTextView);
		TextView okTextView=(TextView) findViewById(R.id.okTextView);
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CommonUtility.hideKeypad(mContext, editText);
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CommonUtility.hideKeypad(mContext, editText);
				validate();
			}
		});
		if(product!=null && !TextUtils.isEmpty(product.getQuantity())){
			editText.setText(product.getQuantity());
			editText.setSelection(product.getQuantity().length());
		}else{
			editText.setText("1");
			editText.setSelection(1);
		}
	}

	protected void validate() {
		// TODO Auto-generated method stub
		String quantity=editText.getText().toString().trim();
		if(quantity.length()==0){
			AlertUtils.showToast(mContext, "Enter quantity");
			return;
		}else if(!isValidNumber(quantity)){
			AlertUtils.showToast(mContext, "Enter valid quantity");
			return;
		}
		if(((HomeActivity)mContext).checkInternet())
			dialogCallback.setQuantity(quantity);
	}

	private boolean isValidNumber(String quantity) {
		// TODO Auto-generated method stub
		try{
			int value=Integer.parseInt(quantity);
			return value>0? true: false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
}

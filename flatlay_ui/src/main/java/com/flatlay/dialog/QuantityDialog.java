package com.flatlay.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.Product;
import com.flatlaylib.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

public class QuantityDialog extends Dialog{
	private FragmentActivity mContext;
	QuantityDialog collectionListDialog;
	DialogCallback dialogCallback;
	EditText editText;
	Product product;
	String item;
	ListView listquantity;
	private String selelctedquantity;

//	public QuantityDialog(FragmentActivity context) {
//		super(context, R.style.AdvanceDialogTheme);
//		mContext = context;
//		init();
//	}

	public QuantityDialog(FragmentActivity context, DialogCallback dialogCallback, Product product,String selelctedquantity) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		this.dialogCallback = dialogCallback;
		this.product=product;
		this.selelctedquantity=selelctedquantity;
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

		listquantity= (ListView) findViewById(R.id.listquantity);

		// Spinner click listener
		listquantity.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) getOwnerActivity());

		// Spinner Drop down elements
		List<String> categories = new ArrayList<String>();
		categories.add("1");
		categories.add("2");
		categories.add("3");
		categories.add("4");
		categories.add("5");
		categories.add("6");
		categories.add("7");
		categories.add("8");
		categories.add("9");
		categories.add("10");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, categories);

		// Drop down layout style - list view with radio button
		//dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		listquantity.setAdapter(dataAdapter);
		listquantity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				item = parent.getItemAtPosition(position).toString();
				if(position==0)
				{
					show();
				}
				else {
					dismiss();
					if(((HomeActivity)mContext).checkInternet())
						dialogCallback.setQuantity(item);
					// Showing selected spinner item
					//	  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
				}
			}
		});

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
//		if(((HomeActivity)mContext).checkInternet())
//			dialogCallback.setQuantity(item);
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

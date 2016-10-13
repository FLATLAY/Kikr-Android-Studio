package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.FragmentProductBasedOnTypeAdapter;
import com.kikrlib.bean.Product;

public class DeleteCollectiontProductDialog extends Dialog{
	private Context mContext;
	private TextView cancelTextView,okTextView,messageTextView;
	private FragmentProductBasedOnTypeAdapter adapter;
	private Product productList;
	
	public DeleteCollectiontProductDialog(Context context,FragmentProductBasedOnTypeAdapter adapter,Product productList) {
		super(context, R.style.AdvanceDialogTheme);
		mContext=context;
		this.adapter=adapter;
		this.productList=productList;
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_logout);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		okTextView = (TextView) findViewById(R.id.okTextView);
		messageTextView=(TextView) findViewById(R.id.messageTextView);
		okTextView.setText("Yes");
		messageTextView.setText(mContext.getResources().getString(R.string.collection_delete_product_message));
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(((HomeActivity)mContext).checkInternet()){
					dismiss();
					adapter.deleteProductFromCollection(productList);
				}
			}
			
		});
	}
}

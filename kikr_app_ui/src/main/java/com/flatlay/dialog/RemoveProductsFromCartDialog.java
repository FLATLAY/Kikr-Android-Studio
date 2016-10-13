package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.CartListAdapter;
import com.flatlay.fragment.FragmentUserCart;
import com.kikrlib.bean.Product;

import java.util.ArrayList;
import java.util.List;

public class RemoveProductsFromCartDialog extends Dialog{
	private TextView cancelTextView,okTextView,messageTextView;
	private FragmentActivity mContext;
	private List<Product> data = new ArrayList<Product>();
	private FragmentUserCart fragmentUserCart;
	private ListView cartItemsList;
	
	public RemoveProductsFromCartDialog(FragmentActivity mContext,List<Product> data, FragmentUserCart fragmentUserCart) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.fragmentUserCart = fragmentUserCart;
		this.data = data;
		init();
	}

	public RemoveProductsFromCartDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_overload_cart);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(false);
		setCancelable(false);
		messageTextView= (TextView) findViewById(R.id.messageTextView);
		cartItemsList = (ListView) findViewById(R.id.cartItemsList);
		if (data.size()>0) {
			messageTextView.setText("Unfortunately these items are no longer in stock");
			cartItemsList.setVisibility(View.VISIBLE);
			cartItemsList.setAdapter(new CartListAdapter(mContext, data, fragmentUserCart,true,null));
		} else {
			messageTextView.setVisibility(View.VISIBLE);
			cartItemsList.setVisibility(View.GONE);
		}
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		cancelTextView.setVisibility(View.GONE);
		okTextView = (TextView) findViewById(R.id.okTextView);
		okTextView.setText("Remove");
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(((HomeActivity) mContext).checkInternet()){
					dismiss();
					fragmentUserCart.removeProducts();
				}
			}
		});
	}
	
}

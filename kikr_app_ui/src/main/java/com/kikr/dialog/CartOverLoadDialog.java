package com.kikr.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.CartListAdapter;
import com.kikr.fragment.FragmentUserCart;
import com.kikr.ui.ProgressBarDialog;
import com.kikrlib.api.EditInspirationApi;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CommonRes;
import com.kikrlib.utils.AlertUtils;

public class CartOverLoadDialog extends Dialog{
	private TextView cancelTextView,okTextView,messageTextView;
	private FragmentActivity mContext;
	private List<Product> data = new ArrayList<Product>();
	private ListView cartItemsList;
	private FragmentUserCart fragmentUserCart;

	public CartOverLoadDialog(FragmentActivity mContext) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		init();
	}
	
	public CartOverLoadDialog(FragmentActivity mContext,List<Product> data, FragmentUserCart fragmentUserCart) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.fragmentUserCart = fragmentUserCart;
		this.data = data;
		init();
	}

	public CartOverLoadDialog(Context context, int theme) {
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
			messageTextView.setText("Select details for below products");
			cartItemsList.setVisibility(View.VISIBLE);
			cartItemsList.setAdapter(new CartListAdapter(mContext, data, fragmentUserCart,true,null));
		} else {
			messageTextView.setVisibility(View.VISIBLE);
			cartItemsList.setVisibility(View.GONE);
		}
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		cancelTextView.setVisibility(View.GONE);
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
				if(((HomeActivity) mContext).checkInternet()){
					dismiss();
				}
			}
		});
	}
	
}

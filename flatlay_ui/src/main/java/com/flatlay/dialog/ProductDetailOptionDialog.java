package com.flatlay.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentProductDetailWebView;
import com.flatlaylib.bean.Product;

public class ProductDetailOptionDialog extends Dialog implements View.OnClickListener{
	private Activity mContext;
	private TextView shareText,viewOnStoreSiteText;
	private Product product;

	public ProductDetailOptionDialog(FragmentActivity context,Product product) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		this.product=product;
		init();
	}

	public ProductDetailOptionDialog(FragmentActivity context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}
	
	private void init() {
		setContentView(R.layout.dialog_product_detail_option);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		shareText = (TextView) findViewById(R.id.shareText);
		viewOnStoreSiteText = (TextView) findViewById(R.id.viewOnStoreSiteText);
		shareText.setOnClickListener(this);
		viewOnStoreSiteText.setOnClickListener(this);
	}
	
	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.viewOnStoreSiteText:
			dismiss();
			addFragment(new FragmentProductDetailWebView(product.getAffiliateurlforsharing(), product));
			break;
		case R.id.shareText:
			dismiss();
			ShareDialog dialog = new ShareDialog(mContext,(HomeActivity)mContext,product);
			dialog.show();
			break;
		}
	}
}

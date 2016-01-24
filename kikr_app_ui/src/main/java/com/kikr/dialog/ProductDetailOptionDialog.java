package com.kikr.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentProductBasedOnType;
import com.kikr.fragment.FragmentProductDetailWebView;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.ui.ProgressBarDialog;
import com.kikrlib.api.ProductBasedOnBrandApi;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ProductBasedOnBrandRes;
import com.kikrlib.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

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

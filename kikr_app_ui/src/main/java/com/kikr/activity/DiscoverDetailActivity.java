package com.kikr.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.fragment.FragmentProductDetailWebView;
import com.kikr.googlewallet.CheckoutActivity;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.FontUtility;
import com.kikrlib.bean.Product;
import com.personagraph.api.PGAgent;

public class DiscoverDetailActivity extends BaseActivity implements OnClickListener{
	private Button mBuyButton; 
	private Product productList;
	private ImageView  productImageView,addImageView,addToCartImageView;
	private TextView productTitleTextView,productRegularPriceTextView,productYourPriceTextView,descriptionTextView, viewDetailsText;
	private boolean readCondition = true;
	private String readLess = "<font color=#5bbaad>...read less</font>";
	private String readMore ="<font color=#5bbaad>...read more</font>";
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if(CommonUtility.isOnline(context))
			PGAgent.logEvent("PRODUCT_DETAIL_PAGE_OPEN");
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_discover_detail);
		setDetails();
	}

	private void setDetails() {
		productList = (Product) getIntent().getSerializableExtra("data");
		productRegularPriceTextView.setText(productList.getRetailprice());
		productYourPriceTextView.setText(productList.getSaleprice());
		productTitleTextView.setText(productList.getProductname());
		CommonUtility.setImage(context, productList.getProductimageurl(), productImageView, R.drawable.dum_list_item_brand);
		descriptionTextView.setText(Html.fromHtml(productList.getShortproductdesc()+readMore));		
	}

	@Override
	public void initLayout() {
		mBuyButton = (Button) findViewById(R.id.checkoutButton);
		productImageView = (ImageView) findViewById(R.id.productImageView);
		addImageView = (ImageView) findViewById(R.id.addImageView);
		addToCartImageView = (ImageView) findViewById(R.id.addToCartTextView);
		productTitleTextView = (TextView) findViewById(R.id.productTitleTextView);
		productRegularPriceTextView = (TextView) findViewById(R.id.productRegularPriceTextView);
		productYourPriceTextView = (TextView) findViewById(R.id.productYourPriceTextView);
		descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
		viewDetailsText = (TextView) findViewById(R.id.viewDetailsText);
	}

	@Override
	public void setupData() {
	}

	@Override
	public void headerView() {
		setBackHeader();
		getLeftTextView().setOnClickListener(this);
		getHomeImageView().setOnClickListener(this);
	}

	@Override
	public void setUpTextType() {
		viewDetailsText.setTypeface(FontUtility.setProximanovaLight(context));
	}

	@Override
	public void setClickListener() {
		mBuyButton.setOnClickListener(this);
		productImageView.setOnClickListener(this);
		addImageView.setOnClickListener(this);
		addToCartImageView.setOnClickListener(this);
		descriptionTextView.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftTextView:
			finish();
			break;
		case R.id.homeImageView:
			goToHome();
			break;
		case R.id.checkoutButton:
			startActivity(CheckoutActivity.class);
			break;
		case R.id.descriptionTextView:
			if (readCondition) {
				readCondition = false;
				descriptionTextView.setText(Html.fromHtml(productList.getLongproductdesc()+readLess));
			} else {
				readCondition = true;
				descriptionTextView.setText(Html.fromHtml(productList.getShortproductdesc()+readMore));
			}
			break;
		case R.id.viewDetailsText:
			Log.e("daan", "dd");
			Intent intent = new Intent(this, ProductDetailWebViewActivity.class);
			intent.putExtra("product_url", productList.getProducturl());
			startActivity(intent);
			break;
		default:
			Log.e("default", v.getId() + "");
			break;
		}
	}

}

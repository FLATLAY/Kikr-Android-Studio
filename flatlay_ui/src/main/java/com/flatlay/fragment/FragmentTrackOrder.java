package com.flatlay.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.OrdersApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.OrderRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.util.List;

public class FragmentTrackOrder extends BaseFragment implements OnClickListener {
	private View mainView;
	private TextView orderItemsTextView,orderTaxTextView,orderShippingTextView,orderTotalTextView,orderStatusTextView;
	private TextView orderIdTextView;
	private ProgressBarDialog mProgressBarDialog;
	private String orderId;
	private Button back;
	private ImageView cartProductImage,deleteCartProductImage;
	private TextView brandName,productName,regularPriceText,quantityText,colorText;
	private TextView kikrDiscountText,sizeText,productNotAvailable;
	private LinearLayout colorLayout,discountLayout,sizeLayout,quantityLayout,priceLayout;
	
	public FragmentTrackOrder(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_track_order, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		orderStatusTextView = (TextView) mainView.findViewById(R.id.orderStatusTextView);
		orderTotalTextView = (TextView) mainView.findViewById(R.id.orderTotalTextView);
		orderShippingTextView = (TextView) mainView.findViewById(R.id.orderShippingTextView);
		orderTaxTextView = (TextView) mainView.findViewById(R.id.orderTaxTextView);
		orderItemsTextView = (TextView) mainView.findViewById(R.id.orderItemsTextView);
		orderIdTextView = (TextView) mainView.findViewById(R.id.orderIdTextView);
		cartProductImage = (ImageView) mainView.findViewById(R.id.cartProductImage);
		deleteCartProductImage = (ImageView) mainView.findViewById(R.id.deleteCartProductImage);
		brandName = (TextView) mainView.findViewById(R.id.brandName);
		productName = (TextView) mainView.findViewById(R.id.productName);
		regularPriceText = (TextView) mainView.findViewById(R.id.regularPriceText);
		quantityText = (TextView) mainView.findViewById(R.id.quantityText);
		colorText = (TextView) mainView.findViewById(R.id.colorText);
		kikrDiscountText = (TextView) mainView.findViewById(R.id.kikrDiscountText);
		sizeText = (TextView) mainView.findViewById(R.id.sizeText);
		productNotAvailable = (TextView) mainView.findViewById(R.id.selectProductOption);
		colorLayout=(LinearLayout) mainView.findViewById(R.id.colorLayout);
		discountLayout=(LinearLayout) mainView.findViewById(R.id.discountLayout);
		sizeLayout=(LinearLayout) mainView.findViewById(R.id.sizeLayout);
		quantityLayout=(LinearLayout) mainView.findViewById(R.id.quantityLayout);
		priceLayout=(LinearLayout) mainView.findViewById(R.id.priceLayout);
		back=(Button)mainView.findViewById(R.id.backButton);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		back.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
		if (checkInternet()) 
			getOrderDetails();
	}

	private void getOrderDetails() {

		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final OrdersApi ordersApi = new OrdersApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>" + object);
				OrderRes orderRes = (OrderRes) object;
				if (orderRes!=null)
					setDetails(orderRes);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					OrderRes response = (OrderRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		ordersApi.getOrderDetails(UserPreference.getInstance().getUserID(),orderId);
		ordersApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				ordersApi.cancel();
			}
		});
	
	}

	protected void setDetails(OrderRes orderRes) {
		if (!TextUtils.isEmpty(orderRes.getStatus())) {
			orderStatusTextView.setText(orderRes.getStatus());
		}
		if (!TextUtils.isEmpty(orderRes.getTotal())) {
			orderTotalTextView.setText("$" + CommonUtility.getFormatedNum(orderRes.getTotal()));
		}
		if (!TextUtils.isEmpty(orderRes.getShipping())) {
			orderShippingTextView.setText("$" + CommonUtility.getFormatedNum(orderRes.getShipping()));
		}
		if (!TextUtils.isEmpty(orderRes.getTax())) {
			orderTaxTextView.setText("$" + CommonUtility.getFormatedNum(orderRes.getTax()));
		}
		if (!TextUtils.isEmpty(orderRes.getQuantity())) {
			orderItemsTextView.setText(orderRes.getQuantity());
		}
		if (!TextUtils.isEmpty(orderRes.getId())) {
			orderIdTextView.setText(orderRes.getId());
		}
		List<Product> product = orderRes.getProduct();
		if (product!=null && product.size()>0) {
			setProductData(product.get(0));
		}
	}

	private void setProductData(Product product) {
		CommonUtility.setImage(mContext, product.getProductimageurl(), cartProductImage, R.drawable.dum_list_item_product);
		brandName.setText(product.getMerchantname());
		productName.setText(product.getProductname());
		if(!TextUtils.isEmpty(product.getSaleprice()) || product.getSaleprice().equals("0")){
			regularPriceText.setText(" $"+CommonUtility.getFormatedNum(product.getSaleprice()));
		}else{
			regularPriceText.setText(" $"+CommonUtility.getFormatedNum(product.getRetailprice()));
		}
		if (!TextUtils.isEmpty(product.getQuantity())) {
			quantityLayout.setVisibility(View.VISIBLE);
			quantityText.setText(" "+product.getQuantity());
		} else {
			quantityLayout.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(product.getSelected_color())){
			colorText.setText(" "+product.getSelected_color());
		}else{
			colorLayout.setVisibility(View.GONE);
		}
		
		if(!TextUtils.isEmpty(product.getSelected_size())){
			sizeText.setText(" "+product.getSelected_size());
		}else{
			sizeLayout.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				((HomeActivity)mContext).onBackPressed();
				break;
		}
	}

}

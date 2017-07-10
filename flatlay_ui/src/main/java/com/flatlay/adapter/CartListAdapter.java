package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.CartOverLoadDialog;
import com.flatlay.dialog.RemoveProductFromCartDialog;
import com.flatlay.fragment.FragmentEditPurchaseItem;
import com.flatlay.fragment.FragmentUserCart;
import com.flatlay.utility.CallBack;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.TwoTapProductDetails;
import com.flatlaylib.db.UserPreference;

import java.util.ArrayList;
import java.util.List;

public class CartListAdapter extends BaseAdapter {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private ArrayList<Product> data;
	private boolean isPopup = false;
	private FragmentUserCart fragmentUserCart;
	private List<TwoTapProductDetails> productDetails = new ArrayList<TwoTapProductDetails>();

	public CartListAdapter(FragmentActivity context, List<Product> data, FragmentUserCart fragmentUserCart,boolean isPopup, List<TwoTapProductDetails> productDetails) {
		super();
		this.mContext = context;
		this.data = (ArrayList<Product>) data;
		this.isPopup=isPopup;
		this.fragmentUserCart=fragmentUserCart;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.w("Activity","CartListAdapter");
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Product getItem(int index) {
		return data.get(index);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;



		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_cart_list, null);
			viewHolder = new ViewHolder();
			viewHolder.cartProductImage = (ImageView) convertView.findViewById(R.id.cartProductImage);
			viewHolder.deleteCartProductImage = (ImageView) convertView.findViewById(R.id.deleteCartProductImage);
			viewHolder.brandName = (TextView) convertView.findViewById(R.id.brandName);
			viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
			viewHolder.regularPriceText = (TextView) convertView.findViewById(R.id.regularPriceText);
			viewHolder.quantityText = (TextView) convertView.findViewById(R.id.quantityText);
			viewHolder.colorText = (TextView) convertView.findViewById(R.id.colorText);
			viewHolder.kikrDiscountText = (TextView) convertView.findViewById(R.id.kikrDiscountText);
			viewHolder.sizeText = (TextView) convertView.findViewById(R.id.sizeText);
			viewHolder.pricetext = (TextView) convertView.findViewById(R.id.pricetext);
			viewHolder.selectProductOption = (TextView) convertView.findViewById(R.id.selectProductOption);
			viewHolder.colorLayout=(LinearLayout) convertView.findViewById(R.id.colorLayout);
			viewHolder.discountLayout=(LinearLayout) convertView.findViewById(R.id.discountLayout);
			viewHolder.sizeLayout=(LinearLayout) convertView.findViewById(R.id.sizeLayout);
			viewHolder.quantityLayout=(LinearLayout) convertView.findViewById(R.id.quantityLayout);
			viewHolder.priceLayout=(LinearLayout) convertView.findViewById(R.id.priceLayout);
			/* ****************/
			//viewHolder.regularPriceText.setVisibility(View.GONE);
			//viewHolder.pricetext.setVisibility(View.GONE);

			viewHolder.shipping_status = (ImageView) convertView.findViewById(R.id.shipping_status);
			viewHolder.shippingLayout = (LinearLayout) convertView.findViewById(R.id.shippingLayout);

			viewHolder.btnStandard = (Button) convertView.findViewById(R.id.btnStandard);
			viewHolder.btnExpress = (Button) convertView.findViewById(R.id.btnExpress);
			viewHolder.shippingTxt = (TextView) convertView.findViewById(R.id.shippingText);
			viewHolder.shipLayout = (LinearLayout) convertView.findViewById(R.id.shipLayout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CommonUtility.setImage(mContext, getItem(position).getProductimageurl(), viewHolder.cartProductImage, R.drawable.dum_list_item_product);
		viewHolder.brandName.setText(getItem(position).getMerchantname());
		viewHolder.productName.setText(getItem(position).getProductname());




		if(!TextUtils.isEmpty(getItem(position).getSaleprice()) || getItem(position).getSaleprice().equals("0")){
			viewHolder.regularPriceText.setText(" $"+CommonUtility.getFormatedNum(getItem(position).getSaleprice()));
		}else{
			viewHolder.regularPriceText.setText(" $"+CommonUtility.getFormatedNum(getItem(position).getRetailprice()));
		}
		if (!TextUtils.isEmpty(getItem(position).getQuantity())) {
			viewHolder.quantityLayout.setVisibility(View.VISIBLE);
			viewHolder.quantityText.setText(" "+getItem(position).getQuantity());
		} else {
			viewHolder.quantityLayout.setVisibility(View.GONE);
		}
//		if(!TextUtils.isEmpty(getItem(position).getSelected_color())){
//			viewHolder.colorText.setText(" "+getItem(position).getSelected_color());
//		}else{
//			viewHolder.colorLayout.setVisibility(View.GONE);
//		}

//		if(!TextUtils.isEmpty(getItem(position).getSelected_size())){
//			viewHolder.sizeText.setText(" "+getItem(position).getSelected_size());
//		}else{
//			viewHolder.sizeLayout.setVisibility(View.GONE);
//		}
		if (getItem(position).isSelectdetails()) {
			viewHolder.selectProductOption.setVisibility(View.VISIBLE);

			viewHolder.selectProductOption.setText("Select");

			viewHolder.selectProductOption.setBackground(mContext.getResources().getDrawable(R.drawable.selectwhite));
			viewHolder.selectProductOption.setTextColor(mContext.getResources().getColor(R.color.tab_selected_new));
			viewHolder.quantityLayout.setVisibility(View.GONE);
			viewHolder.sizeLayout.setVisibility(View.GONE);
			viewHolder.colorLayout.setVisibility(View.GONE);
			viewHolder.priceLayout.setVisibility(View.GONE);
		}else if(getItem(position).isNoDetails()){
			viewHolder.selectProductOption.setVisibility(View.GONE);
//			viewHolder.quantityLayout.setVisibility(View.VISIBLE);
//			viewHolder.priceLayout.setVisibility(View.VISIBLE);
		}
		else if(getItem(position).isProductNotAvailable()){
			viewHolder.selectProductOption.setVisibility(View.GONE);
//			viewHolder.quantityLayout.setVisibility(View.GONE);
			//viewHolder.sizeLayout.setVisibility(View.GONE);
			//viewHolder.colorLayout.setVisibility(View.GONE);
//			viewHolder.priceLayout.setVisibility(View.GONE);
		} else if (!getItem(position).isSelectdetails()) {
			viewHolder.selectProductOption.setVisibility(View.GONE);
			viewHolder.quantityLayout.setVisibility(View.VISIBLE);
//			viewHolder.sizeLayout.setVisibility(View.VISIBLE);
//			viewHolder.colorLayout.setVisibility(View.VISIBLE);
			viewHolder.priceLayout.setVisibility(View.VISIBLE);
//			if (!TextUtils.isEmpty(getItem(position).getQuantity())) {
////				viewHolder.quantityLayout.setVisibility(View.VISIBLE);
//				viewHolder.quantityText.setText(" "+getItem(position).getQuantity());
//			} else {
//				viewHolder.quantityLayout.setVisibility(View.GONE);
////			}
//			if(!TextUtils.isEmpty(getItem(position).getSelected_color())){
//				//viewHolder.colorLayout.setVisibility(View.VISIBLE);
//				viewHolder.colorText.setText(" "+getItem(position).getSelected_color());
//			}else{
//				//viewHolder.colorLayout.setVisibility(View.GONE);
//			}
//
//			if(!TextUtils.isEmpty(getItem(position).getSelected_size())){
//				//viewHolder.sizeLayout.setVisibility(View.VISIBLE);
//				viewHolder.sizeText.setText(" "+getItem(position).getSelected_size());
//			}else{
//				//viewHolder.sizeLayout.setVisibility(View.GONE);
//			}
		}
		if (isPopup) {
			viewHolder.shipLayout.setVisibility(View.GONE);
			viewHolder.selectProductOption.setVisibility(View.GONE);
			viewHolder.quantityLayout.setVisibility(View.GONE);
			//viewHolder.sizeLayout.setVisibility(View.GONE);
			//viewHolder.colorLayout.setVisibility(View.GONE);
			viewHolder.priceLayout.setVisibility(View.GONE);
			viewHolder.deleteCartProductImage.setVisibility(View.GONE);
			if (getItem(position).getProductname().equals("Not Loaded") ) {
				viewHolder.productName.setText(getItem(position).getProductname());
			}
		}
		if (getItem(position).getProductname().equals("Not Loaded") && !isPopup) {
			viewHolder.shipLayout.setVisibility(View.GONE);
			viewHolder.selectProductOption.setVisibility(View.GONE);
			viewHolder.quantityLayout.setVisibility(View.GONE);
			//viewHolder.sizeLayout.setVisibility(View.GONE);
			//viewHolder.colorLayout.setVisibility(View.GONE);
			viewHolder.priceLayout.setVisibility(View.GONE);
			viewHolder.productName.setText(getItem(position).getProductname());
		}
		if(getItem(position).getShipping_option().equals("fastest")) {
			viewHolder.btnStandard.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
			viewHolder.btnExpress.setBackgroundColor(mContext.getResources().getColor(R.color.tab_selected_new));
		}
		//viewHolder.shipping_status.setImageResource(R.drawable.hdpi_on);
		else {
			viewHolder.btnStandard.setBackgroundColor(mContext.getResources().getColor(R.color.tab_selected_new));
			viewHolder.btnExpress.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
		}

		viewHolder.btnStandard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewHolder.btnStandard.setBackgroundColor(mContext.getResources().getColor(R.color.tab_selected_new));
				viewHolder.btnExpress.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
				getItem(position).setShipping_option("cheapest");
			}
		});

		viewHolder.btnExpress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewHolder.btnStandard.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
				viewHolder.btnExpress.setBackgroundColor(mContext.getResources().getColor(R.color.tab_selected_new));
				getItem(position).setShipping_option("fastest");
			}
		});

		//	viewHolder.shipping_status.setImageResource(R.drawable.hdpi_off);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!isPopup){
					if(productDetails.size()>0){
						for (int i = 0; i <productDetails.size(); i++) {
							if (getItem(position).getProducturl().equals(productDetails.get(i).getOriginal_url())) {
                                Log.w("CartListAdapter","1=>"+productDetails.get(i));
								((HomeActivity)mContext).addFragment(new FragmentEditPurchaseItem(fragmentUserCart,productDetails.get(i),getItem(position)));
								Log.w("CartListAdapter","Back1");
							}
						}
					}else{
                        Log.w("CartListAdapter","2=><=");
						((HomeActivity)mContext).addFragment(new FragmentEditPurchaseItem(getItem(position),fragmentUserCart));
						Log.w("CartListAdapter","Back2");
					}
				}
			}
		});
		viewHolder.deleteCartProductImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RemoveProductFromCartDialog removeProductFromCartDialog = new RemoveProductFromCartDialog(mContext,getItem(position).getProductcart_id(),fragmentUserCart,new CallBack() {

					@Override
					public void onSuccess() {
						data.remove(position);
						if (data.size()>10) {
							Log.w("CartListAdapter","CartOverLoadDialog");
							CartOverLoadDialog cartOverLoadDialog = new CartOverLoadDialog(mContext);
							cartOverLoadDialog.show();
						}
						notifyDataSetChanged();
						if(data.size()==0){
							fragmentUserCart.showEmptyCart();
						}
						UserPreference.getInstance().decCartCount();
						((HomeActivity)mContext).refreshCartCount();
					}

					@Override
					public void onFail() {

					}
				});
				removeProductFromCartDialog.show();
			}
		});
		return convertView;
	}

//	private String getDiscount(Product item) {
//		try{
//			double salePrice=Double.parseDouble(item.getSaleprice());
//			double retailPrice=Double.parseDouble(item.getRetailprice());
//			double dif=retailPrice-salePrice;
//			if(dif>0)
//				return CommonUtility.getFormatedNum(dif);
//			else
//				return "0";
//		}catch(Exception e){
//			e.printStackTrace();
//			return "0";
//		}
//	}

	public class ViewHolder {
		private ImageView cartProductImage,deleteCartProductImage,shipping_status;
		private TextView brandName,productName,regularPriceText,quantityText,colorText, pricetext;
		private TextView kikrDiscountText,sizeText,selectProductOption;
		private LinearLayout colorLayout,discountLayout,sizeLayout,quantityLayout,priceLayout,shippingLayout;
		private Button btnStandard, btnExpress;
		private TextView shippingTxt;
		private LinearLayout shipLayout;
	}
}
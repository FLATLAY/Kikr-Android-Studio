package com.flatlay.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.Product;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

public class CartProductsUI {
	FragmentActivity mContext;
	List<Product> data;
	LayoutInflater mInflater;

	public CartProductsUI(FragmentActivity context,List<Product> data) {
		super();
		this.mContext = context;
		this.data = (ArrayList<Product>) data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView() {
		LinearLayout ll =  new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams layoutParams =new LayoutParams(CommonUtility.getDeviceWidth(mContext), LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < data.size(); i++) {
			Syso.info("in getview");
			View convertView = mInflater.inflate(R.layout.adapter_cart_list, null);
			ImageView cartProductImage = (ImageView) convertView.findViewById(R.id.cartProductImage);
			ImageView deleteCartProductImage = (ImageView) convertView.findViewById(R.id.deleteCartProductImage);
		TextView brandName = (TextView) convertView.findViewById(R.id.brandName);
			TextView productName = (TextView) convertView.findViewById(R.id.productName);
			TextView regularPriceText = (TextView) convertView.findViewById(R.id.regularPriceText);
			TextView quantityText = (TextView) convertView.findViewById(R.id.quantityText);
			TextView colorText = (TextView) convertView.findViewById(R.id.colorText);
			TextView kikrDiscountText = (TextView) convertView.findViewById(R.id.kikrDiscountText);
			TextView sizeText = (TextView) convertView.findViewById(R.id.sizeText);
			TextView productNotAvailable = (TextView) convertView.findViewById(R.id.selectProductOption);
			LinearLayout colorLayout=(LinearLayout) convertView.findViewById(R.id.colorLayout);
			LinearLayout discountLayout=(LinearLayout) convertView.findViewById(R.id.discountLayout);
			LinearLayout sizeLayout=(LinearLayout) convertView.findViewById(R.id.sizeLayout);
			LinearLayout quantityLayout=(LinearLayout) convertView.findViewById(R.id.quantityLayout);
			LinearLayout priceLayout=(LinearLayout) convertView.findViewById(R.id.priceLayout);
			ImageView shipping_status = (ImageView) convertView.findViewById(R.id.shipping_status);
			LinearLayout shippingLayout = (LinearLayout) convertView.findViewById(R.id.shippingLayout);
			TextView freeShippingText = (TextView) convertView.findViewById(R.id.freeShippingText);
			Button btnStandard = (Button) convertView.findViewById(R.id.btnStandard);
			Button btnExpress = (Button) convertView.findViewById(R.id.btnExpress);
			freeShippingText.setTag(i);
			convertView.setLayoutParams(layoutParams);
			ll.addView(convertView);
			convertView.setTag(i);
			CommonUtility.setImage(mContext, data.get(i).getProductimageurl(), cartProductImage, R.drawable.dum_list_item_product);
			brandName.setText(data.get(i).getMerchantname());
			productName.setText(data.get(i).getProductname());
			if (data.get(i).isFreeShipping()) {
				freeShippingText.setVisibility(View.VISIBLE);
			}
			if(!TextUtils.isEmpty(data.get(i).getSaleprice()) || data.get(i).getSaleprice().equals("0")){
				regularPriceText.setText(" $"+CommonUtility.getFormatedNum(data.get(i).getSaleprice()));
			}else{
				regularPriceText.setText(" $"+CommonUtility.getFormatedNum(data.get(i).getRetailprice()));
			}
			if (!TextUtils.isEmpty(data.get(i).getQuantity())) {
				quantityLayout.setVisibility(View.VISIBLE);
				quantityText.setText(" "+data.get(i).getQuantity());
			} else {
				quantityLayout.setVisibility(View.GONE);
			}
			if(!TextUtils.isEmpty(data.get(i).getSelected_color())){
				colorText.setText(" "+data.get(i).getSelected_color());
			}else{
				colorLayout.setVisibility(View.GONE);
			}

			if(!TextUtils.isEmpty(data.get(i).getSelected_size())){
				sizeText.setText(" "+data.get(i).getSelected_size());
			}else{
				sizeLayout.setVisibility(View.GONE);
			}
			deleteCartProductImage.setVisibility(View.GONE);
			if (data.get(i).isSelectdetails()) {
				productNotAvailable.setVisibility(View.VISIBLE);
				quantityLayout.setVisibility(View.GONE);
				sizeLayout.setVisibility(View.GONE);
				colorLayout.setVisibility(View.GONE);
				priceLayout.setVisibility(View.GONE);
			}else if(data.get(i).isNoDetails()){
				productNotAvailable.setVisibility(View.GONE);
				quantityLayout.setVisibility(View.VISIBLE);
				priceLayout.setVisibility(View.VISIBLE);
			}
			else if(data.get(i).isProductNotAvailable()){
				productNotAvailable.setVisibility(View.GONE);
				quantityLayout.setVisibility(View.GONE);
				sizeLayout.setVisibility(View.GONE);
				colorLayout.setVisibility(View.GONE);
				priceLayout.setVisibility(View.GONE);
			} else if (!data.get(i).isSelectdetails()) {
				productNotAvailable.setVisibility(View.GONE);
				if (!TextUtils.isEmpty(data.get(i).getQuantity())) {
					quantityLayout.setVisibility(View.VISIBLE);
					quantityText.setText(" "+data.get(i).getQuantity());
				} else {
					quantityLayout.setVisibility(View.GONE);
				}
				if(!TextUtils.isEmpty(data.get(i).getSelected_color())){
					colorLayout.setVisibility(View.VISIBLE);
					colorText.setText(" "+data.get(i).getSelected_color());
				}else{
					colorLayout.setVisibility(View.GONE);
				}

				if(!TextUtils.isEmpty(data.get(i).getSelected_size())){
					sizeLayout.setVisibility(View.VISIBLE);
					sizeText.setText(" "+data.get(i).getSelected_size());
				}else{
					sizeLayout.setVisibility(View.GONE);
				}
			}
			if(data.get(i).getShipping_option().equals("fastest"))
				shipping_status.setImageResource(R.drawable.hdpi_on);
			else
				shipping_status.setImageResource(R.drawable.hdpi_off);

			if(data.get(i).getShipping_option().equals("fastest")) {
				btnStandard.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
				btnExpress.setBackgroundColor(mContext.getResources().getColor(R.color.tab_bg_new));
			}
			//viewHolder.shipping_status.setImageResource(R.drawable.hdpi_on);
			else {
				btnStandard.setBackgroundColor(mContext.getResources().getColor(R.color.tab_bg_new));
				btnExpress.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
			}


			shipping_status.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int i = (Integer) v.getTag();
					if(data.get(i).getShipping_option().equals("fastest")){
						((ImageView)v).setImageResource(R.drawable.hdpi_off);
						data.get(i).setShipping_option("cheapest");
					}
					else{
						data.get(i).setShipping_option("fastest");
						((ImageView)v).setImageResource(R.drawable.hdpi_on);
					}
				}
			});
		}
		return ll;
	}

//	 private void initArcMenu(final ArcMenu menu, int[] itemDrawables, final View v) {
//	        final int itemCount = itemDrawables.length;
//	        for (int i = 0; i < itemCount; i++) {
//	            ImageView item = new ImageView(mContext);
//	            item.setImageResource(itemDrawables[i]);
//
//	            final int i = i;
//	            menu.addItem(item, new OnClickListener() {
//
//	                @Override
//	                public void onClick(View vv) {
//	                	menu.setVisibility(View.INVISIBLE);
////	                    Toast.makeText(mContext, "i:" + i, Toast.LENGTH_SHORT).show();
//	                    switch(i){
//	                    case 0:
//	                    	break;
//	                    case 1:
//	                    	if(((HomeActivity)mContext).checkInternet()){
//								CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, data.get((Integer) v.getTag()));
//								collectionListDialog.show();
//							}
//	                    	break;
//	                    case 2:
//	                    	if(((HomeActivity)mContext).checkInternet())
//	                    		addProductToCart(data.get((Integer) v.getTag()));
//	                    	break;
//	                    case 3:
//	                    	((HomeActivity) mContext).shareProduct(data.get((Integer) v.getTag()).getProducturl());
//	                    	break;
//	                    }
//	                }
//	            });
//	        }
//	    }


	private void addFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		((HomeActivity) mContext).addFragment(fragment);
	}



}

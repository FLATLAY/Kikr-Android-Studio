package com.kikr.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentTrackOrder;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.OrdersApi;
import com.kikrlib.bean.Orders;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.OrderRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersAdapter extends BaseExpandableListAdapter  {

	private Context mContext;
	private LayoutInflater mInflater;
	List<String> cartHeaders = new ArrayList<String>();
	HashMap<String, List<Orders>> dataChild = new HashMap<String, List<Orders>>();
	HashMap<String, String> cartDataMap = new HashMap<String, String>();

	public OrdersAdapter(Activity context, List<String> cartHeaders, HashMap<String, List<Orders>> dataChild, HashMap<String, String> cartDataMap) {
		super();
		this.mContext = context;
		this.cartHeaders = cartHeaders;
		this.dataChild = dataChild;
		this.cartDataMap = cartDataMap;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addAll(List<String> cartHeaders, HashMap<String, List<Orders>> dataChild, HashMap<String, String> cartDataMap) {
		this.cartHeaders.addAll(cartHeaders);
		this.dataChild.putAll(dataChild);
		this.cartDataMap.putAll(cartDataMap);
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this.dataChild.get(this.cartHeaders.get(groupPosition)).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.dataChild.get(this.cartHeaders.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.cartHeaders.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.cartHeaders.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {

		String headerTitle = (String) getGroup(groupPosition);

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.order_group_item, null);
			viewHolder = new ViewHolder();
			viewHolder.orderCartID = (TextView) convertView.findViewById(R.id.orderCartID);
			viewHolder.orderDate = (TextView) convertView.findViewById(R.id.orderDate);
			viewHolder.orderPrice = (TextView) convertView.findViewById(R.id.orderPrice);
			viewHolder.orderShipping = (TextView) convertView.findViewById(R.id.orderShipping);
			viewHolder.orderStatus = (TextView) convertView.findViewById(R.id.orderStatus);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		for (Map.Entry<String, String> entry : cartDataMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if(key.equalsIgnoreCase(headerTitle)) {
				String[] temp = value.split("#");

				viewHolder.orderCartID.setText(temp[0]);
				viewHolder.orderDate.setText(CommonUtility.getDateFormat(temp[1]));
				viewHolder.orderPrice.setText(CommonUtility.getFormatedNum(temp[2]));
				viewHolder.orderShipping.setText(temp[3]);
				viewHolder.orderStatus.setText(temp[4]);
				break;
			}
		}


		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {

		final Orders childOrder = (Orders) getChild(groupPosition, childPosition);

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_orders, null);
			viewHolder = new ViewHolder();
			viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			viewHolder.progressBar.getIndeterminateDrawable().setColorFilter(mContext.getResources().getColor(R.color.btn_green), android.graphics.PorterDuff.Mode.MULTIPLY);
			viewHolder.loadingBar = (LinearLayout) convertView.findViewById(R.id.loadingBar);
			viewHolder.cartItemLayout = (LinearLayout) convertView.findViewById(R.id.cartItemLayout);

			viewHolder.cartProductImage = (ImageView) convertView.findViewById(R.id.cartProductImage);
			viewHolder.deleteCartProductImage = (ImageView) convertView.findViewById(R.id.deleteCartProductImage);
			viewHolder.brandName = (TextView) convertView.findViewById(R.id.brandName);
			viewHolder.productName = (TextView) convertView.findViewById(R.id.productName);
			viewHolder.regularPriceText = (TextView) convertView.findViewById(R.id.regularPriceText);
			viewHolder.quantityText = (TextView) convertView.findViewById(R.id.quantityText);
			viewHolder.colorText = (TextView) convertView.findViewById(R.id.colorText);
			viewHolder.kikrDiscountText = (TextView) convertView.findViewById(R.id.kikrDiscountText);
			viewHolder.sizeText = (TextView) convertView.findViewById(R.id.sizeText);
			viewHolder.productNotAvailable = (TextView) convertView.findViewById(R.id.selectProductOption);
			viewHolder.colorLayout=(LinearLayout) convertView.findViewById(R.id.colorLayout);
			viewHolder.discountLayout=(LinearLayout) convertView.findViewById(R.id.discountLayout);
			viewHolder.sizeLayout=(LinearLayout) convertView.findViewById(R.id.sizeLayout);
			viewHolder.quantityLayout=(LinearLayout) convertView.findViewById(R.id.quantityLayout);
			viewHolder.priceLayout=(LinearLayout) convertView.findViewById(R.id.priceLayout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		getOrderDetails(childOrder.getOrder_id(), viewHolder);

		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addFragment(new FragmentTrackOrder(childOrder.getOrder_id()));
			}
		});
		return convertView;
	}

	private void getOrderDetails(String orderId, final ViewHolder viewHolder) {

		final OrdersApi ordersApi = new OrdersApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {

				Syso.info("In handleOnSuccess>>" + object);
				OrderRes orderRes = (OrderRes) object;
				if (orderRes!=null) {
					setDetails(orderRes, viewHolder);
					viewHolder.loadingBar.setVisibility(View.GONE);
					viewHolder.cartItemLayout.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
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

	}

	protected void setDetails(OrderRes orderRes, ViewHolder viewHolder) {
		List<Product> product = orderRes.getProduct();
		if (product!=null && product.size()>0) {
			setProductData(product.get(0), viewHolder);
		}
	}

	private void setProductData(Product product, ViewHolder viewHolder) {
		UrlImageViewHelper.setUrlDrawable(viewHolder.cartProductImage, product.getProductimageurl(), R.drawable.dum_list_item_product);
		viewHolder.brandName.setText(product.getMerchantname());
		viewHolder.productName.setText(product.getProductname());
		if(!TextUtils.isEmpty(product.getSaleprice()) || product.getSaleprice().equals("0")){
			viewHolder.regularPriceText.setText(" $"+CommonUtility.getFormatedNum(product.getSaleprice()));
		}else{
			viewHolder.regularPriceText.setText(" $"+CommonUtility.getFormatedNum(product.getRetailprice()));
		}
		if (!TextUtils.isEmpty(product.getQuantity())) {
			viewHolder.quantityLayout.setVisibility(View.VISIBLE);
			viewHolder.quantityText.setText(" "+product.getQuantity());
		} else {
			viewHolder.quantityLayout.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(product.getSelected_color())){
			viewHolder.colorText.setText(" "+product.getSelected_color());
		}else{
			viewHolder.colorLayout.setVisibility(View.GONE);
		}

		if(!TextUtils.isEmpty(product.getSelected_size())){
			viewHolder.sizeText.setText(" "+product.getSelected_size());
		}else{
			viewHolder.sizeLayout.setVisibility(View.GONE);
		}
	}

	public class ViewHolder {
		private ImageView cartProductImage,deleteCartProductImage;
		private TextView brandName,productName,regularPriceText,quantityText,colorText;
		private TextView kikrDiscountText,sizeText,productNotAvailable;
		private LinearLayout colorLayout,discountLayout,sizeLayout,quantityLayout,priceLayout;
		private ProgressBar progressBar;
		private LinearLayout loadingBar, cartItemLayout;


		//first level viewholder
		private TextView orderDate, orderCartID, orderPrice, orderShipping, orderStatus;

	}

	public void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

}

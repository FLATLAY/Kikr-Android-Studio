package com.flatlay.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.OrdersApi;
import com.flatlaylib.bean.Orders;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.OrderRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by RachelDi on 4/9/18.
 */

public class FragmentTrackOrder extends BaseFragment {
    private View mainView;
    private TextView date_text, total_price_text, orders_text, deliver_text, order_num_text, items_list_text;
    private String orderId;
    private Orders order;
    private LinearLayout order_items_list, backIconLayout;

    public FragmentTrackOrder(String orderId, Orders order) {
        this.orderId = orderId;
        this.order = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_track_order, container,false);
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        date_text = (TextView) mainView.findViewById(R.id.date_text);
        date_text.setTypeface(FontUtility.setMontserratLight(mContext));
        orders_text = (TextView) mainView.findViewById(R.id.orders_text);
        orders_text.setTypeface(FontUtility.setMontserratLight(mContext));
        total_price_text = (TextView) mainView.findViewById(R.id.total_price_text);
        total_price_text.setTypeface(FontUtility.setMontserratLight(mContext));

        deliver_text = (TextView) mainView.findViewById(R.id.deliver_text);
        deliver_text.setTypeface(FontUtility.setMontserratLight(mContext));

        order_num_text = (TextView) mainView.findViewById(R.id.order_num_text);
        order_num_text.setTypeface(FontUtility.setMontserratLight(mContext));

        order_items_list = (LinearLayout) mainView.findViewById(R.id.order_items_list);
        backIconLayout = (LinearLayout) mainView.findViewById(R.id.backIconLayout);
        backIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.onBackPressed();
            }
        });

        items_list_text = (TextView) mainView.findViewById(R.id.items_list_text);
        items_list_text.setTypeface(FontUtility.setMontserratLight(mContext));

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {

    }


    @Override
    public void setData(Bundle bundle) {
        date_text.setText(CommonUtility.getDateFormat(order.getOrder_date()));
        if (checkInternet())
            getOrderDetails(order_items_list);
    }

    private void getOrderDetails(final LinearLayout order_items_list) {

        final OrdersApi ordersApi = new OrdersApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                OrderRes orderRes = (OrderRes) object;
                if (orderRes != null)
                    setDetails(orderRes, order_items_list);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    OrderRes response = (OrderRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        ordersApi.getOrderDetails(UserPreference.getInstance().getUserID(), orderId);
        ordersApi.execute();
    }

    protected void setDetails(OrderRes orderRes, LinearLayout order_items_list) {

        if (!TextUtils.isEmpty(orderRes.getTotal())) {
            total_price_text.setText("Total: $" + CommonUtility.getFormatedNum(orderRes.getTotal()));
        }

        if (!TextUtils.isEmpty(orderRes.getQuantity())) {
            items_list_text.setText("ITEMS (" + orderRes.getQuantity() + ")");
        }
        if (!TextUtils.isEmpty(orderRes.getId())) {
            order_num_text.setText("Order No.: " + orderRes.getId());
        }
        List<Product> product = orderRes.getProduct();
        setCartItemsList(product, order_items_list);
    }

    protected void setCartItemsList(final List<Product> data, LinearLayout order_items_list) {
        order_items_list.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < data.size(); i++) {
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.cart_item_layout, null);
            ImageView cancel_icon = (ImageView) layout.findViewById(R.id.cancel_icon);
            cancel_icon.setVisibility(View.INVISIBLE);
            RoundedImageView product_image = (RoundedImageView) layout.findViewById(R.id.product_image);
            final Product currentProduct = data.get(i);
            String productImage = currentProduct.getProductimageurl();
            if (productImage != null && productImage.length() > 0)
                CommonUtility.setImage(mContext, product_image, productImage);
            product_image.setTag(i);
            ((TextView) layout.findViewById(R.id.quantity_text)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout.findViewById(R.id.price_text)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout.findViewById(R.id.size_text)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout.findViewById(R.id.color_text)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout.findViewById(R.id.product_name)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout.findViewById(R.id.brand_name)).setTypeface(FontUtility.setMontserratRegular(mContext));
            if (currentProduct.getMerchantname() != null && currentProduct.getMerchantname().length() > 0) {
                ((TextView) layout.findViewById(R.id.brand_name)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.brand_name)).setText(currentProduct.getMerchantname());
            }
            if (currentProduct.getQuantity() != null && currentProduct.getQuantity().length() > 0) {
                ((TextView) layout.findViewById(R.id.quantity_text)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.quantity_text)).setText("Quantity: " + currentProduct.getQuantity());
            }
            if (currentProduct.getProductname() != null && currentProduct.getProductname().length() > 0) {
                ((TextView) layout.findViewById(R.id.product_name)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.product_name)).setText(currentProduct.getProductname());
            }
            if (currentProduct.getSelected_color() != null && currentProduct.getSelected_color().length() > 0) {
                ((TextView) layout.findViewById(R.id.color_text)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.color_text)).setText("Color: " + currentProduct.getSelected_color());
            }
            if (currentProduct.getSelected_size() != null && currentProduct.getSelected_size().length() > 0) {
                ((TextView) layout.findViewById(R.id.size_text)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.size_text)).setText("Size: " + currentProduct.getSelected_size());
            }
            if (currentProduct.getSaleprice() != null && currentProduct.getSaleprice().length() > 0) {
                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: $ " + currentProduct.getSaleprice());
            } else if (currentProduct.getRetailprice() != null && currentProduct.getRetailprice().length() > 0) {
                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: " + currentProduct.getRetailprice());
            }
            product_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", currentProduct);
                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
                    detail.setArguments(bundle);
                    ((HomeActivity) mContext).addFragment(detail);
                }
            });
            params3.setMargins(0, 10, 0, 10);
            order_items_list.addView(layout, params3);
        }
    }

    protected LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

}


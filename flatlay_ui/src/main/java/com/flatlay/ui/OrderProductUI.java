package com.flatlay.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.activity.ProductDetailWebViewActivity;
import com.flatlay.dialog.CollectionListDialog;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.dialog.ShareDialog;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.fragment.ViewInsProductFragment;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyBubbleActions;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.api.OrdersApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.service.res.OrderRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by RachelDi on 4/9/18.
 */

public class OrderProductUI {
    FragmentActivity mContext;
    List<Product> data;
    String order_id;
    LayoutInflater mInflater;
    int width, height;

    //	View view;
    int productWidth;

    public OrderProductUI(FragmentActivity context, String order_id,int width, int height) {
        super();
        this.mContext = context;
        this.width = width;
        this.order_id=order_id;
        this.height = height;
        mFragmentStack = new Stack<String>();
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        final LinearLayout ll = new LinearLayout(mContext);
        final OrdersApi ordersApi = new OrdersApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Log.e("OrdersAdapter", "success");
                OrderRes orderRes = (OrderRes) object;
                if (orderRes != null) {
                    data= orderRes.getProduct();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    for (int i = 0; i < data.size(); i++) {
                        View convertView = null;

                        layoutParams.setMargins(0, 5, 5, 0);
                        convertView = (LinearLayout) mInflater.inflate(R.layout.inspiration_product_list2, null);
                        productWidth = layoutParams.height;

                        ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);

                        final Product product = data.get(i);

                        CommonUtility.setImage(mContext, product.getProductimageurl(), activity_product_list_product_image, R.drawable.dum_list_item_product);
                        convertView.setLayoutParams(layoutParams);
                        ll.addView(convertView);
                        convertView.setTag(i);

                        convertView.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(final View v) {

                            }
                        });

                    }

                    if (data.size() > 3) {
                        RelativeLayout convertView = (RelativeLayout) mInflater.inflate(R.layout.layout_load_more, null);
                        TextView text = (TextView) convertView.findViewById(R.id.textView1);

                        RelativeLayout.LayoutParams layoutParams2 =
                                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT);
                        layoutParams2.setMargins(0, 15, 15, 15);
                        convertView.setLayoutParams(layoutParams2);

                        text.setTypeface(FontUtility.setMontserratLight(mContext));
                        convertView.setBackgroundResource(R.drawable.blackbordertextview);
                        convertView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        ll.addView(convertView);
                    }
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Log.e("OrdersAdapter", "fail");
                if (object != null) {
                    OrderRes response = (OrderRes) object;

                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        ordersApi.getOrderDetails(UserPreference.getInstance().getUserID(), order_id);
        ordersApi.execute();
        return ll;
    }

    public Stack<String> mFragmentStack;
    public FragmentTransaction transaction;
    private Fragment mContent;

}

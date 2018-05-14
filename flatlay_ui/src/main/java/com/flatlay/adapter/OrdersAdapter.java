package com.flatlay.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentTrackOrder;
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
import com.flatlaylib.utils.Syso;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RachelDi on 4/9/18.
 */

public class OrdersAdapter extends BaseAdapter {

    private FragmentActivity mContext;
    List<List<Product>> products_list = new ArrayList<>();
    private LayoutInflater mInflater;
    List<Orders> data = new ArrayList<>();
    Map<Integer, List<Product>> loadMap = new HashMap<>();


    public OrdersAdapter(FragmentActivity context, List<Orders> data) {
        super();
        this.mContext = context;
        this.data = data;

        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.w("Activity", "OrdersAdapter");
    }

    public class ViewHolder {
        private ImageView image1, image2, image3, image4;
        private TextView date_text, view_text, deliver_date, order_num, rest_text;
        private RelativeLayout image4_layout;
        private LinearLayout images_layout;
    }

    public void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

    @Override
    public int getCount() {
        return data.size();

    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setData(List<Orders> data) {
        this.data = data;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_orders, null);
            viewholder = new ViewHolder();
            viewholder.image1 = (ImageView) convertView.findViewById(R.id.image1);
            viewholder.date_text = (TextView) convertView.findViewById(R.id.date_text);
            viewholder.date_text.setTypeface(FontUtility.setMontserratRegular(mContext));
            viewholder.view_text = (TextView) convertView.findViewById(R.id.view_text);
            viewholder.view_text.setTypeface(FontUtility.setMontserratLight(mContext));
            viewholder.deliver_date = (TextView) convertView.findViewById(R.id.deliver_date);
            viewholder.deliver_date.setTypeface(FontUtility.setMontserratLight(mContext));
            viewholder.order_num = (TextView) convertView.findViewById(R.id.order_num);
            viewholder.order_num.setTypeface(FontUtility.setMontserratRegular(mContext));
            viewholder.rest_text = (TextView) convertView.findViewById(R.id.rest_text);
            viewholder.rest_text.setTypeface(FontUtility.setMontserratLight(mContext));
            viewholder.images_layout = (LinearLayout) convertView.findViewById(R.id.images_layout);
            viewholder.image3 = (ImageView) convertView.findViewById(R.id.image3);
            viewholder.image2 = (ImageView) convertView.findViewById(R.id.image2);
            viewholder.image4 = (ImageView) convertView.findViewById(R.id.image4);
            viewholder.image4_layout = (RelativeLayout) convertView.findViewById(R.id.image4_layout);
            final OrdersApi ordersApi = new OrdersApi(new ServiceCallback() {

                @Override
                public void handleOnSuccess(Object object) {
                    OrderRes orderRes = (OrderRes) object;
                    if (orderRes != null) {
                        List<Product> product = orderRes.getProduct();
                        loadMap.put(i, product);
                        if (product != null && product.size() > 0) {
                            viewholder.image1.setVisibility(View.VISIBLE);
                            CommonUtility.setImage(mContext, viewholder.image1, product.get(0).getProductimageurl());
                        }
                        if (product != null && product.size() > 1) {
                            viewholder.image2.setVisibility(View.VISIBLE);
                            CommonUtility.setImage(mContext, viewholder.image2, product.get(1).getProductimageurl());
                        }
                        if (product != null && product.size() > 2) {
                            viewholder.image3.setVisibility(View.VISIBLE);
                            CommonUtility.setImage(mContext, viewholder.image3, product.get(2).getProductimageurl());
                        }
                        if (product != null && product.size() > 3) {
                            viewholder.image4_layout.setVisibility(View.VISIBLE);
                            CommonUtility.setImage(mContext, viewholder.image4, product.get(3).getProductimageurl());
                            viewholder.rest_text.setText("+" + (product.size() - 3));

                        }
                    }
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
            ordersApi.getOrderDetails(UserPreference.getInstance().getUserID(), data.get(i).getOrder_id());
            ordersApi.execute();
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        List<Product> product = loadMap.get(i);
        if (product != null && product.size() > 0) {
            viewholder.image1.setVisibility(View.VISIBLE);
            CommonUtility.setImage(mContext, viewholder.image1, product.get(0).getProductimageurl());
        }
        if (product != null && product.size() > 1) {
            viewholder.image2.setVisibility(View.VISIBLE);
            CommonUtility.setImage(mContext, viewholder.image2, product.get(1).getProductimageurl());
        }
        if (product != null && product.size() > 2) {
            viewholder.image3.setVisibility(View.VISIBLE);
            CommonUtility.setImage(mContext, viewholder.image3, product.get(2).getProductimageurl());
        }
        if (product != null && product.size() > 3) {
            viewholder.image4_layout.setVisibility(View.VISIBLE);
            CommonUtility.setImage(mContext, viewholder.image4, product.get(3).getProductimageurl());
            viewholder.rest_text.setText("+" + (product.size() - 3));

        }
        viewholder.date_text.setText(CommonUtility.getDateFormat2(data.get(i).getOrder_date()));
        viewholder.order_num.setText("Order No.: " + data.get(i).getOrder_id());
        viewholder.image4_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new FragmentTrackOrder(data.get(i).getOrder_id(), data.get(i)));
            }
        });
        viewholder.view_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(new FragmentTrackOrder(data.get(i).getOrder_id(), data.get(i)));
            }
        });
        return convertView;
    }

}

package com.flatlay.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.OrdersAdapter;
import com.flatlay.ui.ProgressBarDialog;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by RachelDi on 4/9/18.
 */

public class FragmentAllOrders extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private ListView ordersList1, ordersList2;
    private OrdersAdapter ordersAdapter;
    private int pagenum = 0;
    private boolean isFirstTime = true;
    private boolean isLoading = false;
    private Button startshopping;
    private TextView progress_text, complete_text;
    private OrderListener listener;

    public FragmentAllOrders(OrderListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_all_orders, container,false);
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        ordersList1 = (ListView) mainView.findViewById(R.id.ordersList1);
        ordersList2 = (ListView) mainView.findViewById(R.id.ordersList2);
        progress_text = (TextView) mainView.findViewById(R.id.progress_text);
        progress_text.setTypeface(FontUtility.setMontserratLight(mContext));
        complete_text = (TextView) mainView.findViewById(R.id.complete_text);
        complete_text.setTypeface(FontUtility.setMontserratLight(mContext));
        startshopping = (Button) mainView.findViewById(R.id.startshopping);
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        startshopping.setOnClickListener(this);
    }

    public void initData() {
        pagenum = 0;
        isFirstTime = true;
        if (checkInternet())
            getOrdersList();
    }

    @Override
    public void setData(Bundle bundle) {
        initData();
    }


    private void getOrdersList() {
        final OrdersApi ordersApi = new OrdersApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                hideDataNotFound();
                OrderRes orderRes = (OrderRes) object;
                List<Orders> data = orderRes.getData();
                if (data.size() == 0 && isFirstTime) {
                    try {
                        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.itemNotFound);
                        layout.setVisibility(View.VISIBLE);
                        layout.setGravity(Gravity.CENTER);
                        TextView textView = (TextView) getView().findViewById(R.id.noDataFoundTextView);
                        textView.setTypeface(FontUtility.setMontserratLight(mContext));
                        TextView startshopping = (TextView) getView().findViewById(R.id.startshopping);
                        startshopping.setTypeface(FontUtility.setMontserratLight(mContext));
                        startshopping.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listener.onClickButton();
                            }
                        });
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    }
                } else if (data.size() > 0 && isFirstTime) {
                    hideDataNotFound();
                    Log.e("2nd else all orders", "2nd" + data.size());
                    ordersAdapter = new OrdersAdapter(mContext, data);
                    ordersList2.setAdapter(ordersAdapter);
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
        ordersApi.getOrdersList(UserPreference.getInstance().getUserID(), Integer.toString(pagenum));
        ordersApi.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public interface OrderListener { // create an interface

        void onClickButton();
    }


}

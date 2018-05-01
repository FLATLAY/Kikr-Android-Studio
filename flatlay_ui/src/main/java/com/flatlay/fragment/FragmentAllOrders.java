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


    //boolean isordered =false;
    public FragmentAllOrders(OrderListener listener) {
        this.listener=listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_all_orders, null);
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
        //  if(CartFragmentTab.isordered)
        // {
        initData();
        //  }
        // CartFragmentTab.isordered=false;
//        ordersList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view,
//                                             int scrollState) {
//                // Do nothing
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
////				   System.out.println("1234 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
//                if(!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
////			    	System.out.println("1234 inside if ");
//                    if(checkInternet2()){
//                        pagenum++;
//                        isFirstTime=false;
//                        getOrdersList();
//                    }else{
//                        showReloadFotter();
//                    }
//                }
//            }
//        });
    }

//    protected void showReloadFotter() {
//        TextView textView=getReloadFotter();
//        textView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if(checkInternet()){
//                    pagenum++;
//                    isFirstTime=false;
//                    getOrdersList();
//                }
//            }
//        });
//    }

    private void getOrdersList() {
//        isLoading=!isLoading;
        final OrdersApi ordersApi = new OrdersApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                hideDataNotFound();
//                isLoading=!isLoading;
                OrderRes orderRes = (OrderRes) object;
                List<Orders> data = orderRes.getData();
               // HashMap<String, List<Orders>> dataChild = new HashMap<String, List<Orders>>();
//                HashMap<String, List<Orders>> dataChild2 = new HashMap<String, List<Orders>>();

             //   List<String> cartHeaders = new ArrayList<String>();
//                List<String> cartHeaders2 = new ArrayList<String>();

            //    HashMap<String, String> cartDataMap = new HashMap<String, String>();


//                for (Orders order : data) {
//                    cartHeaders.add(order.getCartId());
//                    cartDataMap.put(order.getCartId(), order.getCartId() + "#" + order.getOrder_date() + "#" + order.getFinalcartprice() + "#" +
//                            order.getShipping() + "#" + order.getStatus());
//                }
//                Set<String> uniqueCartIDs = new HashSet<String>();
//                uniqueCartIDs.addAll(cartHeaders);
//                cartHeaders.clear();
//                cartHeaders.addAll(uniqueCartIDs);
//
//                List<Orders> dummyOrders = null;
//                //double totalPriceAllOrdersInCart = 0;
//                for (int i = 0; i < cartHeaders.size(); i++) {
//                    dummyOrders = new ArrayList<Orders>();
//                    //	totalPriceAllOrdersInCart = 0;
//                    for (int j = 0; j < data.size(); j++) {
//
//                        if (cartHeaders.get(i).equalsIgnoreCase(data.get(j).getCartId())) {
//                            dummyOrders.add(data.get(j));
//                            //		totalPriceAllOrdersInCart += Double.parseDouble(data.get(j).getFinalcartprice());
//                        }
//                    }
//
//                    dataChild.put(cartHeaders.get(i), dummyOrders);
//                }

//                if(data.size()<10){
//                    isLoading=true;
//                }
                if (data.size() == 0 && isFirstTime) {
//					showDataNotFound();
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
//                        textView.setText("No past orders");
                    } catch (NullPointerException exception) {
                        exception.printStackTrace();
                    }
                } else if (data.size() > 0 && isFirstTime) {
                    hideDataNotFound();
                    Log.e("2nd else all orders", "2nd"+data.size());
//                    ordersAdapter = new OrdersAdapter(mContext, cartHeaders, dataChild, cartDataMap);
                    ordersAdapter = new OrdersAdapter(mContext, data);
                    ordersList2.setAdapter(ordersAdapter);
                }
//                else {
//                    Log.e("3rd else all orders", "3rd");
////                    ordersAdapter.addAll(cartHeaders, dataChild, cartDataMap);
//                    ordersAdapter.addAll(cartHeaders, dataChild, cartDataMap);
//
//                    ordersAdapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//                isLoading = !isLoading;
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
//            case R.id.startshopping:
//                addFragment(new FragmentDiscoverNew());
//                break;
        }
    }

    public interface OrderListener { // create an interface

        void onClickButton();
    }


}

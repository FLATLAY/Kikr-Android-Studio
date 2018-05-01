package com.flatlay.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.OrderProcessingDialog;
import com.flatlay.service.PlaceOrderService;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.TwoTapApi;
import com.flatlaylib.api.UpdateCartApi;
import com.flatlaylib.bean.Address;
import com.flatlaylib.bean.CartProduct;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by RachelDi on 4/29/18.
 */

public class CartConfirmFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private TextView shipping_text5, shipping_text4, confirm_text, final_name_text, final_address_text1,
            final_address_text2, final_address_text3, final_total_item_count_text, subtotal_text2,
            sub_price_text2, tax_text2, tax_amount_text2, shipping_cost, shipping_cost_amount, total_text2,
            total_amount_text2, place_order_button;
    private LinearLayout item_list2;
    private String subtotal = "0", tax = "0", shipping = "0", finalprice = "0";
    private List<Product> my_productLists;
    private HashMap<String, List<Product>> cartList = new HashMap<String, List<Product>>();
    private String cartid;
    private CartProduct cardAndShippingDetail;
    private Runnable runnable;
    private Handler handler = new Handler();
    private static String purchaseId;
    boolean isLoadingCartStatus = false;
    private Address shippingAddress;
    private double TAX = 20.0;
    private double total_price;
    protected LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    public CartConfirmFragment(List<Product> my_productLists, CartProduct cardAndShippingDetail,Address shippingAddress,double total_price) {
        this.my_productLists = my_productLists;
        this.cardAndShippingDetail = cardAndShippingDetail;
        this.shippingAddress=shippingAddress;
        this.total_price=total_price;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.cart_confirm_layout, null);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.place_order_button:
                String msg = "Order confirmed?";
                OrderProcessingDialog orderProcessingDialog = new OrderProcessingDialog(mContext, msg, new OrderProcessingDialog.MyListener() {
                    @Override
                    public void onClickButton() {
                        getCartId();
                    }
                });
                orderProcessingDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                orderProcessingDialog.show();
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        shipping_text5 = (TextView) mainView.findViewById(R.id.shipping_text5);
        shipping_text5.setTypeface(FontUtility.setMontserratLight(mContext));
        shipping_text4 = (TextView) mainView.findViewById(R.id.shipping_text4);
        shipping_text4.setTypeface(FontUtility.setMontserratLight(mContext));
        confirm_text = (TextView) mainView.findViewById(R.id.confirm_text);
        confirm_text.setTypeface(FontUtility.setMontserratLight(mContext));
        final_name_text = (TextView) mainView.findViewById(R.id.final_name_text);
        final_name_text.setTypeface(FontUtility.setMontserratLight(mContext));
        final_address_text1 = (TextView) mainView.findViewById(R.id.final_address_text1);
        final_address_text1.setTypeface(FontUtility.setMontserratLight(mContext));
        final_address_text2 = (TextView) mainView.findViewById(R.id.final_address_text2);
        final_address_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        final_address_text3 = (TextView) mainView.findViewById(R.id.final_address_text3);
        final_address_text3.setTypeface(FontUtility.setMontserratLight(mContext));
        item_list2 = (LinearLayout) mainView.findViewById(R.id.item_list2);
        final_total_item_count_text = (TextView) mainView.findViewById(R.id.final_total_item_count_text);
        final_total_item_count_text.setTypeface(FontUtility.setMontserratLight(mContext));
        subtotal_text2 = (TextView) mainView.findViewById(R.id.subtotal_text2);
        subtotal_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        subtotal_text2.setText("Subtotal (" + UserPreference.getInstance().getCartCount() + " Items)");
        final_total_item_count_text.setText("ITEMS (" + UserPreference.getInstance().getCartCount() + " )");
        subtotal_text2.setText("Subtotal (" + UserPreference.getInstance().getCartCount() + " Items)");
        sub_price_text2 = (TextView) mainView.findViewById(R.id.sub_price_text2);
        sub_price_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        tax_text2 = (TextView) mainView.findViewById(R.id.tax_text2);
        tax_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        tax_amount_text2 = (TextView) mainView.findViewById(R.id.tax_amount_text2);
        tax_amount_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        shipping_cost = (TextView) mainView.findViewById(R.id.shipping_cost);
        shipping_cost.setTypeface(FontUtility.setMontserratLight(mContext));
        shipping_cost_amount = (TextView) mainView.findViewById(R.id.shipping_cost_amount);
        shipping_cost_amount.setTypeface(FontUtility.setMontserratLight(mContext));
        total_text2 = (TextView) mainView.findViewById(R.id.total_text2);
        total_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        total_amount_text2 = (TextView) mainView.findViewById(R.id.total_amount_text2);
        total_amount_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        total_amount_text2.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price + TAX)));
        place_order_button = (TextView) mainView.findViewById(R.id.place_order_button);
        place_order_button.setTypeface(FontUtility.setMontserratLight(mContext));
        final_name_text.setText(shippingAddress.getFirstname() + " " + shippingAddress.getLastname());
        final_address_text1.setText(shippingAddress.getStreet_address() + ", " + shippingAddress.getApartment());
        final_address_text2.setText(shippingAddress.getCity_town() + ", " + shippingAddress.getState() + " " + shippingAddress.getZip_code());
        final_address_text3.setText(shippingAddress.getCountry());
        getEstimate();
        setCartItemsList2(my_productLists);
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {

    }

    private void getEstimate() {
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                try {
                    subtotal = "0";
                    tax = "0";
                    shipping = "0";
                    finalprice = "0";
                    JSONObject jsonObject = new JSONObject(object.toString());
                    JSONObject estimates = jsonObject.getJSONObject("estimates");
                    Iterator keys = estimates.keys();
                    while (keys.hasNext()) {
                        String currentDynamicKey = (String) keys.next();
                        JSONObject currentDynamicValue = estimates.getJSONObject(currentDynamicKey);
                        JSONObject prices = currentDynamicValue.getJSONObject("prices");

                        subtotal = Double.toString(Double.parseDouble(subtotal) + Double.parseDouble(prices.getString("subtotal").replace("$", "")));
                        sub_price_text2.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(subtotal)));
                        shipping = Double.toString(Double.parseDouble(shipping) + Double.parseDouble(prices.getString("shipping_price").replace("$", "")));
                        shipping_cost_amount.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(shipping)));
                        tax = Double.toString(Double.parseDouble(tax) + Double.parseDouble(prices.getString("sales_tax").replace("$", "")));
                        tax_amount_text2.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(tax)));
                        finalprice = Double.toString(Double.parseDouble(finalprice) + Double.parseDouble(prices.getString("final_price").replace("$", "")));
                        total_amount_text2.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(finalprice)));
                        for (int i = 0; i < my_productLists.size(); i++) {
                            if (currentDynamicKey.equals(my_productLists.get(i).getSiteId())) {
                                updateProductPrice(my_productLists.get(i).getProductcart_id(), prices.getString("subtotal").replace("$", ""),
                                        prices.getString("shipping_price").replace("$", ""), prices.getString("sales_tax").replace("$", ""), my_productLists.get(i).getMd5(), my_productLists.get(i).getSiteId());
                                if (prices.getString("shipping_price").equals("$0.00"))
                                    my_productLists.get(i).setFreeShipping(true);
                            }
                        }
                    }
                    UserPreference.getInstance().setFinalPrice(finalprice);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        twoTapApi.getCartEstimates(cartList, cartid, cardAndShippingDetail);
        twoTapApi.execute();
    }

    private void getCartId() {
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {

                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    cartid = (String) jsonObject.get("cart_id");
                    callPurchase();
                    runnable = new Runnable() {

                        @Override
                        public void run() {
                            getStatus(cartid);
                        }
                    };
                    handler.postDelayed(runnable, 5000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        List<String> products = new ArrayList<String>();
        for (int i = 0; i < my_productLists.size(); i++) {
            products.add(my_productLists.get(i).getProducturl());
        }
        twoTapApi.getCartId(products);
        twoTapApi.execute();
    }

    public void callPurchase() {

        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    Syso.info("result:  " + jsonObject);
                    if (jsonObject.has("purchase_id")) {
                        purchaseId = jsonObject.getString("purchase_id");
                        UserPreference.getInstance().setPurchaseId(purchaseId);
                        if (((HomeActivity) mContext).checkInternet())
                            callServerForConfirmation(purchaseId, "pending");
                        String msg = "We sent the order to the merchants, we will send you a follow up notice once your order status is confirmed.";
                        OrderProcessingDialog orderProcessingDialog = new OrderProcessingDialog(mContext, msg, new OrderProcessingDialog.MyListener() {
                            @Override
                            public void onClickButton() {
                                Intent i = new Intent(mContext, PlaceOrderService.class);
                                i.putExtra("purchase_id", purchaseId);
                                i.putExtra("cartId", cartid);
                                mContext.startService(i);
                            }
                        });
                        orderProcessingDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                        orderProcessingDialog.show();
                    } else {
                        CommonUtility.showAlert(mContext, jsonObject.getString("description"));
                    }
                } catch (JSONException e) {
                    AlertUtils.showToast(mContext, "Failed to get status");
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        createCartList();
        twoTapApi.purchaseApi2(cartid, cartList, cardAndShippingDetail);
        twoTapApi.execute();
    }

    private void updateProductPrice(String productCartId, String price, String shiping, String productTax, String md5, String siteId) {
        CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {

            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        cartApi.updateproductPrice(UserPreference.getInstance().getUserID(), productCartId, price, shiping, productTax, md5, siteId);
        cartApi.execute();
    }

    public void callServerForConfirmation(final String purchase_id, final String status) {
        UpdateCartApi twoTapApi = new UpdateCartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (status.equals("confirmed")) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        my_productLists.clear();
                        UserPreference.getInstance().setCartCount("0");
                        purchaseId = "";
                        UserPreference.getInstance().setPurchaseId("");
                        UserPreference.getInstance().setFinalPrice("");
                        UserPreference.getInstance().setIsNotificationClicked(false);
                    }
                    CommonUtility.showAlert(mContext, mContext.getResources().getString(R.string.order_success_text));
                } else if (status.equals("cancel")) {
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                CommonUtility.showAlert(mContext, mContext.getResources().getString(R.string.order_fail_text));

                if (status.equals("confirmed")) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        my_productLists.clear();
                        UserPreference.getInstance().setCartCount("0");
                        purchaseId = "";
                        UserPreference.getInstance().setPurchaseId("");
                        UserPreference.getInstance().setFinalPrice("");
                        UserPreference.getInstance().setIsNotificationClicked(false);
                    }

                }
            }
        });
        twoTapApi.updatecarttwotapstatus(UserPreference.getInstance().getUserID(), purchase_id, cartid, finalprice, status);
        twoTapApi.execute();
    }

    private synchronized void getStatus(final String cart_id) {
        if (!isLoadingCartStatus)
            return;
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                try {
                    JSONObject object2 = new JSONObject(object.toString());
                    String message = object2.getString("message");
                    if (message.equalsIgnoreCase("has_failures") || message.equalsIgnoreCase("done")) {
                        isLoadingCartStatus = true;
                        AlertUtils.showToast(mContext, message);
                    } else {
                        getStatus(cart_id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                isLoadingCartStatus = true;
                String message = "Purchase failed";
                AlertUtils.showToast(mContext, message);
            }
        });
        twoTapApi.getCartStatus(cart_id);
        twoTapApi.execute();
    }

    private void createCartList() {
        for (int i = 0; i < my_productLists.size(); i++) {
            String siteId = my_productLists.get(i).getSiteId();
            if (cartList.containsKey(siteId)) {
                cartList.get(siteId).add(my_productLists.get(i));
            } else {
                List<Product> list = new ArrayList<Product>();
                list.add(my_productLists.get(i));
                cartList.put(siteId, list);
            }
        }
    }

    protected void setCartItemsList2(final List<Product> data) {
        item_list2.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < data.size(); i++) {
            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.cart_item_layout, null);
            ImageView cancel_icon = (ImageView) layout.findViewById(R.id.cancel_icon);
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
            double currentPrice = 0;
            if (currentProduct.getSaleprice() != null && currentProduct.getSaleprice().length() > 0) {
                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
                total_price += StringUtils.getDoubleValue(currentProduct.getSaleprice());
                currentPrice = StringUtils.getDoubleValue(currentProduct.getSaleprice());
                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: $ " + currentProduct.getSaleprice());
            } else if (currentProduct.getRetailprice() != null && currentProduct.getRetailprice().length() > 0) {
                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
                total_price += StringUtils.getDoubleValue(currentProduct.getRetailprice());
                currentPrice = StringUtils.getDoubleValue(currentProduct.getRetailprice());
                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: $ " + currentProduct.getRetailprice());
            }

            cancel_icon.setVisibility(View.INVISIBLE);
//            sub_price_text.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price)));
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
            item_list2.addView(layout, params3);
        }
    }
}

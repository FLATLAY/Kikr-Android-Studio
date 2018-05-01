package com.flatlay.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.CardGridAdapter;
import com.flatlay.dialog.OrderProcessingDialog;
import com.flatlay.service.PlaceOrderService;
import com.flatlay.ui.ShippingAddressList;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.api.AddressApi;
import com.flatlaylib.api.CardInfoApi;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.KikrCreditsApi;
import com.flatlaylib.api.TwoTapApi;
import com.flatlaylib.api.UpdateCartApi;
import com.flatlaylib.bean.Address;
import com.flatlaylib.bean.Card;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.AddressRes;
import com.flatlaylib.service.res.CardInfoRes;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.KikrCreditsRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by RachelDi on 4/29/18.
 */

public class CartListFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private LinearLayout layout_6_1, item_list, layout_6_2;
    private TextView items_text, items_count_text, shipping_text, payment_text, promo_text, add_text,
            subtotal_text, sub_price_text, tax_text, tax_amount_text, est_shipping_text, est_shipping_amount_text,
            total_item_count_text, est_shipping_text2, est_shipping_text3, guarantee_text, learn_more_text,
            checkoutButton, empty_cart_text, looks_like_text, earn_text, fillButton;
    private ImageView arrow1, arrow2;
    private EditText enter_promo_text;
    private MyMaterialContentOverflow3 overflow2;
//    private boolean firstTimeGetCard;
    private List<Product> my_productLists;
    private String promoCode = "";
    private double total_price = 0;
    protected LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    public CartListFragment(MyMaterialContentOverflow3 overflow2) {
        this.overflow2 = overflow2;
//        this.firstTimeGetCard = firstTimeGetCard;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.cart_list_fragment_layout, null);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_text:
                if (enter_promo_text.getVisibility() == View.GONE) {
                    add_text.setText("Close");
                    enter_promo_text.setVisibility(View.VISIBLE);
                } else {
                    add_text.setText("Add");
                    enter_promo_text.setVisibility(View.GONE);
                }
                break;
            case R.id.checkoutButton:
                //need to change!!!!!!!!!!!!!!!!
                ((HomeActivity) mContext).myAddFragment(new CartAddressFragment(my_productLists, total_price));
                break;
            case R.id.fillButton:
                overflow2.triggerClose();
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        layout_6_1 = (LinearLayout) mainView.findViewById(R.id.layout_6_1);
        items_text = (TextView) mainView.findViewById(R.id.items_text);
        items_text.setTypeface(FontUtility.setMontserratLight(mContext));
        items_count_text = (TextView) mainView.findViewById(R.id.items_count_text);
        items_count_text.setTypeface(FontUtility.setMontserratLight(mContext));
        shipping_text = (TextView) mainView.findViewById(R.id.shipping_text);
        shipping_text.setTypeface(FontUtility.setMontserratLight(mContext));
        arrow1 = (ImageView) mainView.findViewById(R.id.arrow1);
        payment_text = (TextView) mainView.findViewById(R.id.payment_text);
        payment_text.setTypeface(FontUtility.setMontserratLight(mContext));
        arrow2 = (ImageView) mainView.findViewById(R.id.arrow2);
        promo_text = (TextView) mainView.findViewById(R.id.promo_text);
        promo_text.setTypeface(FontUtility.setMontserratLight(mContext));
        add_text = (TextView) mainView.findViewById(R.id.add_text);
        add_text.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_promo_text = (EditText) mainView.findViewById(R.id.enter_promo_text);
        enter_promo_text.setTypeface(FontUtility.setMontserratLight(mContext));
        MyTextWatcher2 watcher2 = new MyTextWatcher2();
        enter_promo_text.addTextChangedListener(watcher2);
        subtotal_text = (TextView) mainView.findViewById(R.id.subtotal_text);
        subtotal_text.setTypeface(FontUtility.setMontserratLight(mContext));
        sub_price_text = (TextView) mainView.findViewById(R.id.sub_price_text);
        sub_price_text.setTypeface(FontUtility.setMontserratLight(mContext));
        tax_text = (TextView) mainView.findViewById(R.id.tax_text);
        tax_text.setTypeface(FontUtility.setMontserratLight(mContext));
        tax_amount_text = (TextView) mainView.findViewById(R.id.tax_amount_text);
        tax_amount_text.setTypeface(FontUtility.setMontserratLight(mContext));
        est_shipping_text = (TextView) mainView.findViewById(R.id.est_shipping_text);
        est_shipping_text.setTypeface(FontUtility.setMontserratLight(mContext));
        est_shipping_amount_text = (TextView) mainView.findViewById(R.id.est_shipping_amount_text);
        est_shipping_amount_text.setTypeface(FontUtility.setMontserratLight(mContext));
        total_item_count_text = (TextView) mainView.findViewById(R.id.total_item_count_text);
        total_item_count_text.setTypeface(FontUtility.setMontserratLight(mContext));
        est_shipping_text2 = (TextView) mainView.findViewById(R.id.est_shipping_text2);
        est_shipping_text2.setTypeface(FontUtility.setMontserratRegular(mContext));
        est_shipping_text3 = (TextView) mainView.findViewById(R.id.est_shipping_text3);
        est_shipping_text3.setTypeface(FontUtility.setMontserratLight(mContext));
        guarantee_text = (TextView) mainView.findViewById(R.id.guarantee_text);
        guarantee_text.setTypeface(FontUtility.setMontserratRegular(mContext));
        learn_more_text = (TextView) mainView.findViewById(R.id.learn_more_text);
        learn_more_text.setTypeface(FontUtility.setMontserratLight(mContext));
        learn_more_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        item_list = (LinearLayout) mainView.findViewById(R.id.item_list);
        checkoutButton = (TextView) mainView.findViewById(R.id.checkoutButton);
        checkoutButton.setTypeface(FontUtility.setMontserratLight(mContext));
        layout_6_2 = (LinearLayout) mainView.findViewById(R.id.layout_6_2);
        empty_cart_text = (TextView) mainView.findViewById(R.id.empty_cart_text);
        empty_cart_text.setTypeface(FontUtility.setMontserratRegular(mContext));
        looks_like_text = (TextView) mainView.findViewById(R.id.looks_like_text);
        looks_like_text.setTypeface(FontUtility.setMontserratLight(mContext));
        earn_text = (TextView) mainView.findViewById(R.id.earn_text);
        earn_text.setTypeface(FontUtility.setMontserratLight(mContext));
        fillButton = (TextView) mainView.findViewById(R.id.fillButton);
        fillButton.setTypeface(FontUtility.setMontserratLight(mContext));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                getCartList();
            }
        }, 700);
//        if (firstTimeGetCard) {
//            getKikrCredits();
//            firstTimeGetCard = false;
//        }
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        arrow1.setOnClickListener(this);
        arrow2.setOnClickListener(this);
        add_text.setOnClickListener(this);
        checkoutButton.setOnClickListener(this);
        fillButton.setOnClickListener(this);
    }

    public void getCartList() {
        CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                CartRes cartRes = (CartRes) object;
                List<Product> productLists = cartRes.getData();
                my_productLists = productLists;
                if (productLists.size() <= 0) {
                    layout_6_2.setVisibility(View.VISIBLE);
                    layout_6_1.setVisibility(View.GONE);
                } else {
                    layout_6_1.setVisibility(View.VISIBLE);
                    layout_6_2.setVisibility(View.GONE);
                    UserPreference.getInstance().setCartCount(
                            String.valueOf(productLists.size()));
                    items_count_text.setText(UserPreference.getInstance().getCartCount());
                    total_item_count_text.setText("Total (" + UserPreference.getInstance().getCartCount() + " Items)");
//                    final_total_item_count_text.setText("ITEMS (" + UserPreference.getInstance().getCartCount() + " )");
//                    subtotal_text2.setText("Subtotal (" + UserPreference.getInstance().getCartCount() + " Items)");
                    setCartItemsList(productLists);
//                    setCartItemsList2(productLists);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {

            }
        });
        cartApi.getCartList(UserPreference.getInstance().getUserID());
        cartApi.execute();
    }

    public void removeFromCart(final String id, final LinearLayout layout, final double price) {

        final CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                for (int i = 0; i < my_productLists.size(); i++) {
                    if (id.equals(my_productLists.get(i).getProductcart_id())) {
                        my_productLists.remove(i);
                    }
                    break;
                }
                total_price -= price;
                sub_price_text.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price)));
                UserPreference.getInstance().decCartCount();
                items_count_text.setText(UserPreference.getInstance().getCartCount());
                total_item_count_text.setText("Total (" + UserPreference.getInstance().getCartCount() + " Items)");
//                final_total_item_count_text.setText("ITEMS (" + UserPreference.getInstance().getCartCount() + " )");
//                subtotal_text2.setText("Subtotal (" + UserPreference.getInstance().getCartCount() + " Items)");
                AlertUtils.showToast(mContext, "Product removed successfully");
                layout.setVisibility(View.GONE);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        cartApi.removeFromCart(UserPreference.getInstance().getUserID(), id);
        cartApi.execute();
    }

    protected void setCartItemsList(final List<Product> data) {
        item_list.removeAllViews();
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
            final double currentPrice2 = currentPrice;
            final LinearLayout layout2 = layout;
            cancel_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeFromCart(currentProduct.getProductcart_id(), layout2, currentPrice2);
                }
            });

            sub_price_text.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price)));
//            total_amount_text2.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price + TAX)));
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
            item_list.addView(layout, params3);
        }
    }

//    protected void setCartItemsList2(final List<Product> data) {
//        item_list2.removeAllViews();
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for (int i = 0; i < data.size(); i++) {
//            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.cart_item_layout, null);
//            ImageView cancel_icon = (ImageView) layout.findViewById(R.id.cancel_icon);
//            RoundedImageView product_image = (RoundedImageView) layout.findViewById(R.id.product_image);
//            final Product currentProduct = data.get(i);
//            String productImage = currentProduct.getProductimageurl();
//            if (productImage != null && productImage.length() > 0)
//                CommonUtility.setImage(mContext, product_image, productImage);
//            product_image.setTag(i);
//            ((TextView) layout.findViewById(R.id.quantity_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.price_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.size_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.color_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.product_name)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.brand_name)).setTypeface(FontUtility.setMontserratRegular(mContext));
//            if (currentProduct.getMerchantname() != null && currentProduct.getMerchantname().length() > 0) {
//                ((TextView) layout.findViewById(R.id.brand_name)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.brand_name)).setText(currentProduct.getMerchantname());
//            }
//            if (currentProduct.getQuantity() != null && currentProduct.getQuantity().length() > 0) {
//                ((TextView) layout.findViewById(R.id.quantity_text)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.quantity_text)).setText("Quantity: " + currentProduct.getQuantity());
//            }
//            if (currentProduct.getProductname() != null && currentProduct.getProductname().length() > 0) {
//                ((TextView) layout.findViewById(R.id.product_name)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.product_name)).setText(currentProduct.getProductname());
//            }
//            if (currentProduct.getSelected_color() != null && currentProduct.getSelected_color().length() > 0) {
//                ((TextView) layout.findViewById(R.id.color_text)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.color_text)).setText("Color: " + currentProduct.getSelected_color());
//            }
//            if (currentProduct.getSelected_size() != null && currentProduct.getSelected_size().length() > 0) {
//                ((TextView) layout.findViewById(R.id.size_text)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.size_text)).setText("Size: " + currentProduct.getSelected_size());
//            }
//            double currentPrice = 0;
//            if (currentProduct.getSaleprice() != null && currentProduct.getSaleprice().length() > 0) {
//                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
//                total_price += StringUtils.getDoubleValue(currentProduct.getSaleprice());
//                currentPrice = StringUtils.getDoubleValue(currentProduct.getSaleprice());
//                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: $ " + currentProduct.getSaleprice());
//            } else if (currentProduct.getRetailprice() != null && currentProduct.getRetailprice().length() > 0) {
//                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
//                total_price += StringUtils.getDoubleValue(currentProduct.getRetailprice());
//                currentPrice = StringUtils.getDoubleValue(currentProduct.getRetailprice());
//                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: $ " + currentProduct.getRetailprice());
//            }
//
//            cancel_icon.setVisibility(View.INVISIBLE);
//            sub_price_text.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price)));
//            product_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("data", currentProduct);
//                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
//                    detail.setArguments(bundle);
//                    ((HomeActivity) mContext).addFragment(detail);
//                }
//            });
//            params3.setMargins(0, 10, 0, 10);
//            item_list2.addView(layout, params3);
//        }
//    }

        private void saveAddress() {
            if (enter_promo_text.getText().toString().trim() != null && enter_promo_text.getText().toString().trim().length() > 0) {
            this.promoCode = enter_promo_text.getText().toString().trim();
        }
}

    private class MyTextWatcher2 implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //need to change!!!!!!!!!!!!!!!
              saveAddress();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}

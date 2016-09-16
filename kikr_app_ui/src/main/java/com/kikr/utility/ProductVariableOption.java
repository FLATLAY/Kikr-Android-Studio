package com.kikr.utility;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kikr.activity.HomeActivity;
import com.kikr.adapter.ProductPagerAdapter;
import com.kikr.fragment.FragmentEditPurchaseItem;
import com.kikr.ui.ProgressBarDialog;
import com.kikrlib.api.TwoTapApi;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProductChildOption;
import com.kikrlib.bean.ProductMainOption;
import com.kikrlib.bean.ProductRequiredOption;
import com.kikrlib.bean.TwoTapProductDetails;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by anshumaan on 4/5/2016.
 */
public class ProductVariableOption {


    private String cartid, description;
    private static Product product;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int i = 0;
    private List<String> productMultipleImages = new ArrayList<String>();

    private List<TwoTapProductDetails> productDetails = new ArrayList<TwoTapProductDetails>();
    private List<Product> productLists = new ArrayList<Product>();
    boolean isLoadingData = true, isSelectOptionRequired = false, isProductExist = true;
    private static ProductVariableOption productVariableOption;
    static Context mContext;
    private ProgressBarDialog mProgressBarDialog;


    public static ProductVariableOption getInstance(Context context, Product product) {

        mContext = context;
        ProductVariableOption.product = product;
        if (productVariableOption == null)
            return new ProductVariableOption();
        else
            return productVariableOption;

    }

    public void getCartId() {
        // productLists.add(product);
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    cartid = (String) jsonObject.get("cart_id");
                    runnable = new Runnable() {

                        @Override
                        public void run() {
                            if (i <= 1) {
                                getStatus(cartid);
                            }
                        }
                    };
                    handler.postDelayed(runnable, 3000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

            }
        });
        List<String> products = new ArrayList<String>();
        products.add(product.getProducturl());
        twoTapApi.getCartId(products);
        twoTapApi.execute();
    }

    private void getStatus(final String cart_id) {
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
//				getData(object);
                try {
                    JSONObject object2 = new JSONObject(object.toString());
                    String message = object2.getString("message");
                    if (message.equalsIgnoreCase("has_failures") || message.equalsIgnoreCase("done")) {
                        getData(object);
                    } else {
                        getStatus(cart_id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("failed:   " + object);
                mProgressBarDialog.dismiss();
            }
        });
        twoTapApi.getCartStatus(cart_id);
        twoTapApi.execute();
    }

    protected void getData(Object object) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        try {
            JSONObject jsonObject = new JSONObject(object.toString());
            JSONObject sites;
            try {
                sites = jsonObject.getJSONObject("sites");
                Iterator keys = sites.keys();
                while (keys.hasNext()) {
                    // loop to get the dynamic key
                    String currentDynamicKey = (String) keys.next(); //site id
                    // get the value of the dynamic key
                    JSONObject currentDynamicValue = sites.getJSONObject(currentDynamicKey);
                    // do something here with the value...
                    JSONObject add_to_cart = null;
                    JSONObject failed_to_add_to_cart = null;
                    try {
                        add_to_cart = currentDynamicValue.getJSONObject("add_to_cart");
                        Syso.info("add_to_cart   " + add_to_cart);
                        Iterator data = add_to_cart.keys();
                        while (data.hasNext()) {
                            // loop to get the dynamic key
                            String currentKey = (String) data.next(); //product md5
                            Syso.info("currentKey   " + currentKey);
                            // get the value of the dynamic key
                            JSONObject value = add_to_cart.getJSONObject(currentKey);
                            // do something here with the value...
                            description = value.getString("description");

                            if (value.has("alt_images")) {
                                JSONArray alt_images = value.getJSONArray("alt_images");
                                for (int i = 0; i < alt_images.length(); i++) {
                                    productMultipleImages.add(alt_images.getString(i));
                                }
                            } else if (value.has("image")) {
                                productMultipleImages.add(value.getString("image"));
                            }

                            Syso.info("value   " + value);
                            try {
                                if (value.getString("status").equals("still_processing")) {
                                    list.clear();
                                    getStatus(cartid);
                                } else {
                                    value.put("site_id", currentDynamicKey);
                                    value.put("md5", currentKey);
                                    list.add(value);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                list.clear();
                                getStatus(cartid);
                            }
                        }
//						}
                    } catch (Exception e) {
                        e.printStackTrace();
                        failed_to_add_to_cart = currentDynamicValue.getJSONObject("failed_to_add_to_cart");
                        Iterator data = failed_to_add_to_cart.keys();

                        while (data.hasNext()) {
                            String currentKey = (String) data.next();
                            Syso.info("currentKey   " + currentKey);
                            JSONObject value = failed_to_add_to_cart.getJSONObject(currentKey);
                            Syso.info("value   " + value);
                            setFailedProduct();
                        }
                        e.printStackTrace();
                    }
                }
                try {
                    JSONArray array = jsonObject.getJSONArray("unknown_urls");
                    for (int i = 0; i < array.length(); i++) {
                        String url = array.getString(i);
                        setFailedProduct();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fetchData(list);
    }

    private void fetchData(List<JSONObject> list) {
        for (int i = 0; i < list.size(); i++) {
            TwoTapProductDetails product = new TwoTapProductDetails();
            if (list.get(i) != null) {
                JSONObject value = list.get(i);
                try {
                    product.setOriginal_url(value.getString("original_url"));
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
                try {
                    product.setSiteId(value.getString("site_id"));
                    product.setMd5(value.getString("md5"));
                    product.setUrl(value.getString("url"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray arr = value.getJSONArray("required_field_names");
                    List<ProductRequiredOption> requiredOptionList = new ArrayList<ProductRequiredOption>();
                    for (int j = 0; j < arr.length(); j++) {
                        ProductRequiredOption requiredOption = new ProductRequiredOption();
                        requiredOption.setName(arr.getString(j));
                        requiredOptionList.add(requiredOption);
                    }
                    product.setRequiredOptionList(requiredOptionList);
                    JSONObject required_field_values = value.getJSONObject("required_field_values");
                    product.setProductMainOptionList(getMainOptionList(required_field_values));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            productDetails.add(product);
        }
        List<String> productToSelectDetail = new ArrayList<String>();
        for (int i = 0; i < productDetails.size(); i++) {
            List<ProductRequiredOption> list2 = productDetails.get(i).getRequiredOptionList();
            if (list2.size() > 1) {  //assuming that quantity will always present so size should be > 1 for selection.
                productToSelectDetail.add(productDetails.get(i).getOriginal_url());
                Syso.info("123456 has data>" + productDetails.get(i).getOriginal_url());
            } else {
                setNoDetailsProduct();
                Syso.info("123456 has no data>" + productDetails.get(i).getOriginal_url());
            }
        }
        productLists.add(product);
        for (int i = 0; i < productDetails.size(); i++) {
            for (int j = 0; j < productLists.size(); j++) {
                if (productDetails.get(i).getOriginal_url().equals(productLists.get(j).getProducturl())) {
                    productLists.get(j).setRequiredOptions(productDetails.get(i).getRequiredOptionList());
                    productLists.get(j).setProductMainOptionList(productDetails.get(i).getProductMainOptionList());
                    productLists.get(j).setSiteId(productDetails.get(i).getSiteId());
                    productLists.get(j).setMd5(productDetails.get(i).getMd5());
                    productLists.get(j).setUrl(productDetails.get(i).getUrl());
                }
            }
        }
        isLoadingData = false;
        isSelectOptionRequired = productToSelectDetail.size() > 0 ? true : false;
        if (mProgressBarDialog.isShowing())
            mProgressBarDialog.dismiss();
        if (!isProductExist)
            AlertUtils.showToast(mContext, "Product is not available");
        else if(isSelectOptionRequired)
            ((HomeActivity) mContext).addFragment(new FragmentEditPurchaseItem(productLists.get(0), true));
        else
            ((HomeActivity) mContext).addProductToCart(productLists.get(0));
    }


    private List<ProductMainOption> getMainOptionList(JSONObject optJSONObject) {
        Iterator data = optJSONObject.keys();
        List<ProductMainOption> productMainOptionList = new ArrayList<ProductMainOption>();
        try {
            while (data.hasNext()) {
                // loop to get the dynamic key
                String currentKey = (String) data.next();
                ProductMainOption mainOption = new ProductMainOption();
                JSONArray mainOptionArray = optJSONObject.getJSONArray(currentKey);
                mainOption.setName(currentKey);
                List<ProductChildOption> optionList = new ArrayList<ProductChildOption>();
                for (int j = 0; j < mainOptionArray.length(); j++) {
                    ProductChildOption childOption = new ProductChildOption();
                    JSONObject option = mainOptionArray.getJSONObject(j);
                    childOption.setExtra_info(option.optString("extra_info"));
                    childOption.setImage(option.optString("image"));
                    childOption.setPrice(option.optString("price"));
                    childOption.setText(option.optString("text"));
                    childOption.setValue(option.optString("value"));
                    childOption.setDep(getMainOptionList(option.optJSONObject("dep")));
                    optionList.add(childOption);
                }
                mainOption.setOptionList(optionList);
                productMainOptionList.add(mainOption);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productMainOptionList;
    }

    private void setFailedProduct() {
        isProductExist = false;
    }

    private void setNoDetailsProduct() {
        isProductExist = true;
    }
}

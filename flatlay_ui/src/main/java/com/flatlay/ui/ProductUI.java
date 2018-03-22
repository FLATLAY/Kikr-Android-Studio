package com.flatlay.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.api.ProductBasedOnBrandApi;
import com.flatlaylib.bean.CollectionImages;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.service.res.ProductBasedOnBrandRes;
import com.flatlaylib.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by RachelDi on 2/13/18.
 */

public class ProductUI {
    private FragmentActivity mContext;
    private List<Product> data;
    private List<CollectionImages> images;

    private LayoutInflater mInflater;
    private int index;
    private String postlink;
    private int productWidth;
    private int width, height;
    private boolean isUrl, isDrawer;

    public ProductUI(FragmentActivity context, int width, int height, List<Product> data, boolean isDrawer) {
        super();
        this.mContext = context;
        this.data = data;
        this.width = width;
        this.height = height;
        this.isUrl = false;
        this.isDrawer = isDrawer;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

//    public ProductUI(FragmentActivity context, List<CollectionImages> images,int width, int height) {
//        super();
//        this.mContext = context;
//        this.images = images;
//        this.width = width;
//        this.height = height;
//        this.isUrl = true;
//        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }


    public View getView() {
        final LinearLayout ll = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (!isDrawer)
            for (int i = 0; i < data.size(); i++) {
                layoutParams.setMargins(0, 5, 5, 0);
                View convertView = (LinearLayout) mInflater.inflate(R.layout.inspiration_product_list3, null);
                ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);
                TextView cancelIcon = (TextView) convertView.findViewById(R.id.cancelIcon);
                cancelIcon.setTypeface(FontUtility.setMontserratLight(mContext));
                final Product product = data.get(i);
                CommonUtility.setImage(mContext, activity_product_list_product_image, product.getProductimageurl());
//                CommonUtility.setImage(mContext, product.getProductimageurl(), activity_product_list_product_image, R.drawable.dum_list_item_product);
                convertView.setLayoutParams(layoutParams);
                ll.addView(convertView);
                cancelIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteProductFromCollection(product);
                        ll.removeView(view);
                    }
                });
                convertView.setTag(i);
            }
        else
//            for (int i = 0; i < images.size(); i++) {
//                CollectionImages currentUrl =images.get(i);
//                View convertView = null;
//                convertView = (LinearLayout) mInflater.inflate(R.layout.inspiration_product_list4, null);
//                ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);
//                CommonUtility.setImage(mContext, currentUrl.getProductimageurl(), activity_product_list_product_image, R.drawable.dum_list_item_product);
//                convertView.setLayoutParams(layoutParams);
//                ll.addView(convertView);
//                convertView.setTag(i);
//            }
            for (int i = 0; i < data.size(); i++) {
                final Product currentProduct = data.get(i);
                View convertView = null;
                convertView = (LinearLayout) mInflater.inflate(R.layout.inspiration_product_list4, null);
                ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);
                final Product product = data.get(i);
                CommonUtility.setImage(mContext, activity_product_list_product_image, product.getProductimageurl());
//                CommonUtility.setImage(mContext, product.getProductimageurl(), activity_product_list_product_image, R.drawable.dum_list_item_product);
                convertView.setLayoutParams(layoutParams);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", currentProduct);
                        FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
                        detail.setArguments(bundle);
                        ((HomeActivity) mContext).addFragment(detail);
                    }
                });
                ll.addView(convertView);
                convertView.setTag(i);
            }
        return ll;
    }

    private void deleteProductFromCollection(Product product) {
        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                AlertUtils.showToast(mContext, "Product deleted");
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    CollectionApiRes response = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        collectionApi.deleteProductFromCollection(product.getFrom_user_id(), product.getFrom_collection_id(), product.getId());
        collectionApi.execute();
    }

    private void addNewProduct(Product product) {

    }
}


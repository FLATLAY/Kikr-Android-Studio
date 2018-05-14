package com.flatlay.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.activity.ProductDetailWebViewActivity;
import com.flatlay.dialog.CollectionListDialog;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.dialog.ShareDialog;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyBubbleActions;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProductResult;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by RachelDi on 3/15/18.
 */

public class ProductSearchGridAdapter extends BaseAdapter {

    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<ProductResult> products;
    private String postlink;
    private static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/product/";
    final String TAG = "ProductSearchGrid";
    private MyItemsListener listener;
    private boolean isUpload = false;

    public ProductSearchGridAdapter(FragmentActivity context, List<ProductResult> products) {
        super();
        this.mContext = context;
        this.products = products;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ProductSearchGridAdapter(FragmentActivity context, List<ProductResult> products, boolean isUpload, MyItemsListener listener) {
        super();
        this.mContext = context;
        this.isUpload = isUpload;
        this.products = products;
        this.listener = listener;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void setData(List<ProductResult> data) {
        this.products = data;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public ProductResult getItem(int index) {
        return products.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_product_detail, null);
            viewholder = new ViewHolder();
            viewholder.productImage = (ImageView) convertView.findViewById(R.id.inspirationImage);
            viewholder.layout1 = (LinearLayout) convertView.findViewById(R.id.layout1);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.layout1.setVisibility(View.GONE);
        ProductResult currentResult = getItem(position);
        if (products.size() > position)
            CommonUtility.setImage(mContext, viewholder.productImage, currentResult.getProductimageurl());
        final Product currentProduct = new Product();
        currentProduct.setAffiliateurl(currentResult.getAffiliateurl());
        currentProduct.setAffiliateurlforsharing(currentResult.getAffiliateurlforsharing());
        currentProduct.setDiscount(currentResult.getPercentOff());
        currentProduct.setId(currentResult.getId());
        currentProduct.setLongproductdesc(currentResult.getLongproductdesc());
        currentProduct.setMerchantname(currentResult.getMerchantname());
        currentProduct.setProducturl(currentResult.getProducturl());
        currentProduct.setSaleprice(currentResult.getSaleprice());
        currentProduct.setRetailprice(currentResult.getRetailprice());
        currentProduct.setProductname(currentResult.getProductname());
        currentProduct.setShortproductdesc(currentResult.getShortproductdesc());
        currentProduct.setLike_info(currentResult.getLikeInfo());
        currentProduct.setMerchantid(currentResult.getMerchantid());
        currentProduct.setProductimageurl(currentResult.getProductimageurl());
        if (isUpload) {
            viewholder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onOkButton(currentProduct);
                }
            });
        } else {
            viewholder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", currentProduct);
                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
                    detail.setArguments(bundle);
                    ((HomeActivity) mContext).addFragment(detail);
                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView productImage;
        private LinearLayout layout1;
    }

    public interface MyItemsListener { // create an interface

        void onOkButton(Product currentProduct);
    }
}



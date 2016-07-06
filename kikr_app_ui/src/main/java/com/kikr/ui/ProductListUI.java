package com.kikr.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.dialog.CollectionListDialog;
import com.kikr.fragment.FragmentDiscover;
import com.kikr.fragment.FragmentDiscoverDetail;
import com.kikr.fragment.FragmentProductBasedOnType;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.UiUpdate;
import com.kikrlib.api.CartApi;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProductFeedItem;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CartRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class ProductListUI {
    FragmentActivity mContext;
    List<Product> data;
    LayoutInflater mInflater;
    FragmentDiscover fragmentDiscover;
    ProductFeedItem productFeedItem;
    private ProgressBarDialog mProgressBarDialog;
    View view;
    private static final int[] ITEM_DRAWABLES = {R.drawable.ic_like_circle, R.drawable.ic_add_collection,
            R.drawable.ic_discover_btn_checkout, R.drawable.ic_share_circle};

    float lastX = 0, lastY = 0;
    boolean isProfile;

    public ProductListUI(FragmentActivity context, ProductFeedItem productFeedItem, FragmentDiscover fragmentDiscover, View convertView, boolean isProfile) {
        super();
        this.mContext = context;
        view = convertView;
        this.fragmentDiscover = fragmentDiscover;
        this.data = (ArrayList<Product>) productFeedItem.getProducts();
        this.productFeedItem = productFeedItem;
        this.isProfile = isProfile;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        LinearLayout ll = new LinearLayout(mContext);
//		LayoutParams layoutParams =new LinearLayout.LayoutParams((CommonUtility.getDeviceWidth(mContext)/2)-40, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 0, 5, 0);
        int size = data.size();
        if (data.size() >= 8) {
            size = 8;
        }
        for (int i = 0; i < size; i++) {
            View convertView = (LinearLayout) mInflater.inflate(R.layout.adapter_discover_small_collection, null);
            TextView activity_product_list_category_image = (TextView) convertView.findViewById(R.id.activity_product_list_category_image);
            ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);
            TextView activity_product_list_product_name = (TextView) convertView.findViewById(R.id.activity_product_list_product_name);
            ImageView activity_product_list_star = (ImageView) convertView.findViewById(R.id.activity_product_list_star);
            TextView activity_product_list_product_price1 = (TextView) convertView.findViewById(R.id.activity_product_list_product_price1);
            TextView activity_product_list_product_price2 = (TextView) convertView.findViewById(R.id.activity_product_list_product_price2);
            TextView likeCountTextView = (TextView) convertView.findViewById(R.id.likeCountTextView);
            LinearLayout layoutPrice = (LinearLayout) convertView.findViewById(R.id.layout_price);
            Product product = data.get(i);
            //activity_product_list_product_name.setText(TextUtils.isEmpty(product.getBrand()) ? data.get(i).getProductname() : product.getBrand());

            activity_product_list_product_name.setText(data.get(i).getProductname());
            activity_product_list_category_image.setText(product.getPrimarycategory());
            CommonUtility.setImage(mContext, product.getProductimageurl(), activity_product_list_product_image, R.drawable.dum_list_item_product);
            likeCountTextView.setText(TextUtils.isEmpty(product.getLike_info().getLike_count()) ? "0" : product.getLike_info().getLike_count());

            activity_product_list_product_price1.setText(" $" + CommonUtility.getFormatedNum(data.get(i).getRetailprice()));
            if (!TextUtils.isEmpty(product.getSaleprice()) && !product.getSaleprice().equals("0") && !product.getSaleprice().equals(product.getRetailprice())) {
                activity_product_list_product_price2.setVisibility(View.VISIBLE);
                activity_product_list_product_price2.setText(" $" + CommonUtility.getFormatedNum(product.getSaleprice()));
                activity_product_list_product_price1.setPaintFlags(activity_product_list_product_price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (isProfile) {
                layoutPrice.setBackgroundColor(Color.parseColor("#2e293c"));
                activity_product_list_product_name.setBackgroundColor(Color.parseColor("#2e293c"));

            } else {

                layoutPrice.setBackgroundColor(Color.parseColor("#2f7678"));
                activity_product_list_product_name.setBackgroundColor(Color.parseColor("#2f7678"));
            }

            convertView.setLayoutParams(layoutParams);
            ll.addView(convertView);
            convertView.setTag(i);

            activity_product_list_star.setTag(i);
            activity_product_list_star.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, data.get((Integer) view.getTag()));
                        collectionListDialog.show();
                    }
                }
            });
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(final View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data.get((Integer) v.getTag()));
                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail(new UiUpdate() {

                        @Override
                        public void updateUi() {
                            TextView likeCountTextView = (TextView) v.findViewById(R.id.likeCountTextView);
                            likeCountTextView.setText(TextUtils.isEmpty(data.get((Integer) v.getTag()).getLike_info().getLike_count()) ? "0" : data.get((Integer) v.getTag()).getLike_info().getLike_count());
                        }
                    });
                    detail.setArguments(bundle);
                    addFragment(detail);
                }
            });

            convertView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(final View v) {
                    if (((HomeActivity) mContext).isMenuShowing()) {
                        ((HomeActivity) mContext).hideContextMenu();
                    } else {
                        ((HomeActivity) mContext).showContextMenu(data.get((Integer) v.getTag()), new UiUpdate() {

                            @Override
                            public void updateUi() {
                                TextView likeCountTextView = (TextView) v.findViewById(R.id.likeCountTextView);
                                likeCountTextView.setText(TextUtils.isEmpty(data.get((Integer) v.getTag()).getLike_info().getLike_count()) ? "0" : data.get((Integer) v.getTag()).getLike_info().getLike_count());
                            }
                        });
                    }
                    return true;
                }
            });
        }

        if (data.size() > 5) {
            RelativeLayout convertView = (RelativeLayout) mInflater.inflate(R.layout.layout_load_more, null);
            LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((TextView) convertView.findViewById(R.id.textView1)).setTextColor(mContext.getResources().getColor(R.color.white));
            layoutParams2.setMargins(5, 0, 5, 0);
            convertView.setLayoutParams(layoutParams2);
            convertView.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_trans_shape));

            convertView.setPadding(5,0,5,0);
            ll.addView(convertView);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    addFragment(new FragmentProductBasedOnType(productFeedItem.getType(), productFeedItem.getItem_name(), productFeedItem.getItem_id()));
                }
            });
        }
        return ll;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }


    public void addProductToCart(Product product) {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                mProgressBarDialog.dismiss();
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        String fromUserId = product.getFrom_user_id() == null ? "" : product.getFrom_user_id();
        String fromCollection = product.getFrom_collection_id() == null ? "" : product.getFrom_collection_id();
        cartApi.addToCart(UserPreference.getInstance().getUserID(), product.getId(), "1", UserPreference.getInstance().getCartID(), fromUserId, fromCollection, "", "", null);
        cartApi.execute();
    }
}

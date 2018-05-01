package com.flatlay.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.fragment.ViewInsProductFragment;
import com.flatlay.ui.ProductUI;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.ProductBasedOnBrandApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProfileCollectionList;
import com.flatlaylib.bean.TaggedItem;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.ProductBasedOnBrandRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RachelDi on 2/5/18.
 */

public class FragmentProfileCollectionAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<CollectionList> data;
    private String user_id;
    private FragmentProfileView fragmentProfileView;
    private TaggedItem taggedItem;
    private int index;
    private List<Product> product_data;
    private Map<String, List<Product>> map = new HashMap<>();
    final String TAG = "FragmentProfileCo";

    public FragmentProfileCollectionAdapter(FragmentActivity context,
                                            List<CollectionList> data,
                                            String user_id,
                                            FragmentProfileView fragmentProfileView,
                                            TaggedItem taggedItem, int index,
                                            ListAdapterListener mListener) {
        super();
        this.mContext = context;
        this.data = data;
        this.user_id = user_id;
        this.taggedItem = taggedItem;
        this.index = index;
        this.mListener = mListener;
        this.fragmentProfileView = fragmentProfileView;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public void setData(List<CollectionList> data) {
        this.data = data;
    }

    @Override
    public CollectionList getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_profile, null);
            viewHolder = new ViewHolder();
            viewHolder.collection_name = (TextView) convertView.findViewById(R.id.collection_name);
            viewHolder.collection_name2 = (TextView) convertView.findViewById(R.id.collection_name2);
            viewHolder.viewAll_text = (TextView) convertView.findViewById(R.id.viewAll_text);
            viewHolder.view_text = (TextView) convertView.findViewById(R.id.view_text);
            viewHolder.productview_text = (TextView) convertView.findViewById(R.id.productview_text);
            viewHolder.productview_count = (TextView) convertView.findViewById(R.id.productview_count);
            viewHolder.itemsaves_text = (TextView) convertView.findViewById(R.id.itemsaves_text);
            viewHolder.itemsaves_count = (TextView) convertView.findViewById(R.id.itemsaves_count);
            viewHolder.collectionview_text = (TextView) convertView.findViewById(R.id.collectionview_text);
            viewHolder.collectionview_count = (TextView) convertView.findViewById(R.id.collectionview_count);
            viewHolder.payoutcredits_text = (TextView) convertView.findViewById(R.id.payoutcredits_text);
            viewHolder.payoutcredits_count = (TextView) convertView.findViewById(R.id.payoutcredits_count);
            viewHolder.collection_name_inspiration = (TextView) convertView.findViewById(R.id.collection_name_inspiration);
            viewHolder.name_layout = (LinearLayout) convertView.findViewById(R.id.name_layout);
            viewHolder.productInflaterLayout = (LinearLayout) convertView.findViewById(R.id.productInflaterLayout);
            viewHolder.productInflaterLayout2 = (LinearLayout) convertView.findViewById(R.id.productInflaterLayout2);
            viewHolder.detailLayout = (RelativeLayout) convertView.findViewById(R.id.detailLayout);
            viewHolder.generalLayout = (LinearLayout) convertView.findViewById(R.id.generalLayout);
            viewHolder.like_icon = (ImageView) convertView.findViewById(R.id.like_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.collection_name.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.collection_name2.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.viewAll_text.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.view_text.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.productview_text.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.productview_count.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.itemsaves_text.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.itemsaves_count.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.collectionview_text.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.collectionview_count.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.payoutcredits_text.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.payoutcredits_count.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.collection_name_inspiration.setTypeface(FontUtility.setMontserratLight(mContext));
        viewHolder.collection_name.setText(getItem(position).getName());
        viewHolder.collection_name2.setText(getItem(position).getName());
        viewHolder.collection_name_inspiration.setText(getItem(position).getName());
//        final LinearLayout productInflaterLayout = new LinearLayout(mContext);
//        final LinearLayout productInflaterLayout2 = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
//        productInflaterLayout.setLayoutParams(params);
//        productInflaterLayout2.setLayoutParams(params);
        viewHolder.productInflaterLayout.removeAllViews();
        viewHolder.productInflaterLayout2.removeAllViews();
        if (user_id.equals(UserPreference.getInstance().getUserID())) {
            viewHolder.like_icon.setVisibility(View.INVISIBLE);
        }
        viewHolder.view_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickAtOKButton(position);
            }
        });
        if (!map.containsKey(getItem(position).getId())) {
            Log.e(TAG,"nono"+getItem(position).getId());
            final ProductBasedOnBrandApi productBasedOnBrandApi = new ProductBasedOnBrandApi(new ServiceCallback() {

                @Override
                public void handleOnSuccess(Object object) {
                    ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
                    product_data = productBasedOnBrandRes.getData();
                    map.put(getItem(position).getId(), product_data);
                    if (index == 0) {
                        viewHolder.productInflaterLayout.removeAllViews();
                        viewHolder.detailLayout.setVisibility(View.GONE);
                        viewHolder.generalLayout.setVisibility(View.VISIBLE);
                        viewHolder.productInflaterLayout.addView(new ProductUI(mContext, 200, 200,
                                product_data, true).getView());
                        viewHolder.viewAll_text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ViewInsProductFragment detail =
                                        new ViewInsProductFragment(map.get(getItem(position).getId()),
                                                data.get(position).getName());
                                ((HomeActivity) mContext).addFragment(detail);
                            }
                        });
                    } else if (index == 1) {
                        viewHolder.productInflaterLayout2.removeAllViews();
                        viewHolder.detailLayout.setVisibility(View.VISIBLE);
                        viewHolder.generalLayout.setVisibility(View.GONE);
                        viewHolder.productInflaterLayout2.addView(new ProductUI(mContext, 200, 200,
                                product_data, true).getView());
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                }
            });
            productBasedOnBrandApi.getProductsBasedOnCollectionList(UserPreference.getInstance().getUserID(),
                    String.valueOf(0), data.get(position).getId());
            productBasedOnBrandApi.execute();
        } else {
            Log.e(TAG,"exists"+getItem(position).getId());
            if (index == 0) {
                viewHolder.productInflaterLayout.removeAllViews();
                viewHolder.productInflaterLayout.addView(new ProductUI(mContext, 200, 200,
                        map.get(getItem(position).getId()), true).getView());
                Log.e(TAG,"exists"+getItem(position).getId());
                viewHolder.viewAll_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ViewInsProductFragment detail =
                                new ViewInsProductFragment(map.get(getItem(position).getId()),
                                        data.get(position).getName());
                        ((HomeActivity) mContext).addFragment(detail);
//                        Log.e("areyou--222","exists"+getItem(position).getId());
                    }
                });
            } else if (index == 1) {
                viewHolder.productInflaterLayout2.removeAllViews();
                viewHolder.productInflaterLayout2.addView(new ProductUI(mContext, 200, 200,
                        map.get(getItem(position).getId()), true).getView());
            }
        }

        return convertView;
    }

    public class ViewHolder {
        private LinearLayout generalLayout, name_layout, productInflaterLayout2, productInflaterLayout;
        private RelativeLayout detailLayout;
        //        private HorizontalScrollView productLayout, productLayout2;
        private TextView collection_name, view_text, productview_text, productview_count, itemsaves_text,
                itemsaves_count, collectionview_text, collectionview_count, payoutcredits_text,
                payoutcredits_count, collection_name2, viewAll_text, collection_name_inspiration;
        private ImageView like_icon;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(int position);
    }
}

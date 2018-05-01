package com.flatlay.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.CustomizeInterestPeopleListAdapter;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.bean.FeaturedTabData;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;

import java.util.List;

/**
 * Created by RachelDi on 3/15/18.
 */

public class FeaturedTabUi {
    private FragmentActivity mContext;
    private List<Product> data;
//    private List<FeaturedTabData> brandsArray;
    private List<Inspiration> feed;
//    private List<String> feed2;
    private LayoutInflater mInflater;
    private FeaturedTabData featuredTabData;
    private ProgressBarDialog mProgressBarDialog;
    //    private View view;
    private String USER = "user";
    private float lastX = 0, lastY = 0;
    private TextView loadMore;
    private ListAdapterListener mListener;

    public FeaturedTabUi(FragmentActivity context, FeaturedTabData featuredTabData, ListAdapterListener mListener) {
        super();
        this.mContext = context;
//        this.brandsArray = brandsArray;
//        view = convertView;
        this.featuredTabData = featuredTabData;
        this.mListener = mListener;
        this.data = this.featuredTabData.getProducts();
        this.feed = this.featuredTabData.getInspiration_feed();
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FeaturedTabUi(FragmentActivity context, List<Inspiration> feed, ListAdapterListener mListener) {
        super();
        this.mContext = context;
//        view = convertView;
        this.feed = feed;
        this.mListener = mListener;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        LinearLayout ll = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (CommonUtility.getDeviceHeight(mContext) * 4 / 30));
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(5, 0, 5, 0);
        int size = 0;
        if (feed != null) {
            size = feed.size();
            for (int i = 0; i < size && i < 4; i++) {
                final int index=i;
                View convertView = mInflater.inflate(R.layout.adapter_featured_images_view, null);
                ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);
                CommonUtility.setImage(mContext, activity_product_list_product_image, feed.get(i).getInspiration_image());
                activity_product_list_product_image.setVisibility(View.VISIBLE);
                convertView.setLayoutParams(layoutParams);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addFragment(new FragmentInspirationDetail(feed.get(index),
                                feed.get(index).getLike_id(), false));
                    }
                });
                ll.addView(convertView);
                convertView.setTag(i);
            }
            if (size > 4) {
                RelativeLayout convertView = (RelativeLayout) mInflater.inflate(R.layout.layout_load_more2, null);
                loadMore = (TextView) convertView.findViewById(R.id.textView1);
                loadMore.setTypeface(FontUtility.setMontserratLight(mContext));
                loadMore.setTextSize(20);
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((CommonUtility.getDeviceHeight(mContext) * 4 / 30), (CommonUtility.getDeviceHeight(mContext) * 4 / 30));
                layoutParams2.setMargins(5, 0, 5, 0);
                convertView.setLayoutParams(layoutParams2);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onClickAtOKButton();
                    }
                });
                ll.addView(convertView);
            }
        } else {
            if (data != null)
                size = data.size();
//            if (feed != null)
//                size = feed.size();
            for (int i = 0; i < size && i < 4; i++) {
                final int index=i;
                View convertView = mInflater.inflate(R.layout.adapter_featured_images_view, null);
                ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);
                if (data != null && data.size() > i) {
                    if (data != null)
                        CommonUtility.setImage(mContext, data.get(i).getProductimageurl(), activity_product_list_product_image);
//                    if (feed != null) {
//                        CommonUtility.setImage(mContext, activity_product_list_product_image, feed.get(i).getInspiration_image());
//                    }
                    activity_product_list_product_image.setVisibility(View.VISIBLE);

                } else
                    activity_product_list_product_image.setImageResource(R.drawable.white_image);
                convertView.setLayoutParams(layoutParams);
                ll.addView(convertView);
                convertView.setTag(i);
//                ViewGroup.LayoutParams params = convertView.getLayoutParams();

            }

            if (size > 4) {
                RelativeLayout convertView = (RelativeLayout) mInflater.inflate(R.layout.layout_load_more2, null);
                loadMore = (TextView) convertView.findViewById(R.id.textView1);
                loadMore.setTypeface(FontUtility.setMontserratLight(mContext));
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams((CommonUtility.getDeviceHeight(mContext) * 4 / 30), (CommonUtility.getDeviceHeight(mContext) * 4 / 30));
                layoutParams2.setMargins(5, 0, 5, 0);
                loadMore.setTextSize(20);
                convertView.setLayoutParams(layoutParams2);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onClickAtOKButton();
                    }
                });
                ll.addView(convertView);
            }
        }
        return ll;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

    public interface ListAdapterListener { // create an interface

        void onClickAtOKButton();
    }

}


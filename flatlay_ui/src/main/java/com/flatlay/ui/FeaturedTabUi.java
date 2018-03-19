package com.flatlay.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
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
    private List<FeaturedTabData> brandsArray;
    private List<Inspiration> feed;
    private LayoutInflater mInflater;
    private FeaturedTabData featuredTabData;
    private ProgressBarDialog mProgressBarDialog;
    private View view;
    private String USER = "user";
    private float lastX = 0, lastY = 0;
    private TextView loadMore;

    public FeaturedTabUi(FragmentActivity context, List<FeaturedTabData> brandsArray,
                         FeaturedTabData featuredTabData, View convertView) {
        super();
        this.mContext = context;
        this.brandsArray = brandsArray;
        view = convertView;
        this.featuredTabData = featuredTabData;
        this.data = this.featuredTabData.getProducts();
        this.feed = this.featuredTabData.getInspiration_feed();
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        LinearLayout ll = new LinearLayout(mContext);
        // LayoutParams layoutParams = new LinearLayout.LayoutParams((CommonUtility.getDeviceWidth(mContext) / 3) - 40, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 0, 5, 0);
        int size = 0;
        if (data != null)
            size = data.size();
        if (feed != null)
            size = feed.size();
        for (int i = 0; i < size; i++) {
            View convertView = mInflater.inflate(R.layout.adapter_featured_images_view, null);

            ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);


            if ((data != null && data.size() > i) || (feed != null && feed.size() > i)) {
                if (data != null)
                    CommonUtility.setImage(mContext, data.get(i).getProductimageurl(), activity_product_list_product_image);
                if (feed != null) {
                    // CommonUtility.setImage(mContext, feed.get(i).getProfile_pic(), activity_product_list_product_image);
                    CommonUtility.setImage(mContext,activity_product_list_product_image, feed.get(i).getInspiration_image());
                }
                activity_product_list_product_image.setVisibility(View.VISIBLE);

            } else
                activity_product_list_product_image.setImageResource(R.drawable.white_image);
            convertView.setLayoutParams(layoutParams);
            ll.addView(convertView);
            convertView.setTag(i);
            ViewGroup.LayoutParams params = convertView.getLayoutParams();

        }

        if (size > 4) {
            RelativeLayout convertView = (RelativeLayout) mInflater.inflate(R.layout.layout_load_more2, null);
            loadMore =(TextView) convertView.findViewById(R.id.textView1);
            loadMore.setTypeface(FontUtility.setMontserratLight(mContext));
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams2.setMargins(5, 0, 5, 0);

            //convertView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
            convertView.setLayoutParams(layoutParams2);
            ll.addView(convertView);
        }
        return ll;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }


}


package com.flatlay.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.fragment.FragmentFeatured;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.fragment.FragmentProductBasedOnType;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.bean.FeaturedTabData;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.Product;

import java.util.HashMap;
import java.util.List;

public class FeaturedTabUi {
    FragmentActivity mContext;
    List<Product> data;
    private List<FeaturedTabData> brandsArray;
    List<Inspiration> feed;
    LayoutInflater mInflater;
    FragmentFeatured fragmentFeatured;
    FeaturedTabData featuredTabData;
    private ProgressBarDialog mProgressBarDialog;
    View view;
    String USER = "user";
    float lastX = 0, lastY = 0;

    public FeaturedTabUi(FragmentActivity context, List<FeaturedTabData> brandsArray, FeaturedTabData featuredTabData, FragmentFeatured fragmentFeatured, View convertView) {
        super();
        this.mContext = context;
        this.brandsArray = brandsArray;
        view = convertView;
        this.fragmentFeatured = fragmentFeatured;
        this.featuredTabData = featuredTabData;
        this.data = this.featuredTabData.getProducts();
        this.feed = this.featuredTabData.getInspiration_feed();
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        LinearLayout ll = new LinearLayout(mContext);
        // LayoutParams layoutParams = new LinearLayout.LayoutParams((CommonUtility.getDeviceWidth(mContext) / 3) - 40, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        int position = (Integer) v.getTag();

//                        FeaturedTabData inspirationsetvalue=new FeaturedTabData();
//                        inspirationsetvalue.setCollections_count(featuredTabData.getCollections_count());
//                        inspirationsetvalue.setInspiration_feed(featuredTabData.getInspiration_feed());


                        if (data != null) {
                            if (((HomeActivity) mContext).checkInternet()) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("data", data.get(position));
                                FragmentDiscoverDetail detail = new FragmentDiscoverDetail(new UiUpdate() {

                                    @Override
                                    public void updateUi() {
                                        //									TextView likeCountTextView=(TextView) v.findViewById(R.id.likeCountTextView);
                                        //									likeCountTextView.setText(TextUtils.isEmpty(data.get((Integer) v.getTag()).getLike_info().getLike_count())?"0":data.get((Integer) v.getTag()).getLike_info().getLike_count());
                                    }
                                });
                                detail.setArguments(bundle);
                                addFragment(detail);
                            }
                        } else if (feed != null) {
                            if (((HomeActivity) mContext).checkInternet()) {
                                addFragment(new FragmentInspirationDetail(feed.get(position), false));
                            }
                        }
                    }
                });
            } else
                activity_product_list_product_image.setImageResource(R.drawable.white_image);
            convertView.setLayoutParams(layoutParams);
            ll.addView(convertView);
            convertView.setTag(i);
            ViewGroup.LayoutParams params = convertView.getLayoutParams();
//
//            convertView.setOnLongClickListener(new OnLongClickListener() {
//
//                @Override
//                public boolean onLongClick(final View v) {
//                    if(((HomeActivity)mContext).isMenuShowing()){
//                        ((HomeActivity)mContext).hideContextMenu();
//                    }else{
//                        ((HomeActivity)mContext).showContextMenu(data.get((Integer) v.getTag()),new UiUpdate(){
//
//                            @Override
//                            public void updateUi() {
//                                TextView likeCountTextView=(TextView) v.findViewById(R.id.likeCountTextView);
//                                likeCountTextView.setText(TextUtils.isEmpty(data.get((Integer) v.getTag()).getLike_info().getLike_count())?"0":data.get((Integer) v.getTag()).getLike_info().getLike_count());
//                            }});
//                    }
//                    return true;
//                }
//            });
        }

        if (size > 0) {
            RelativeLayout convertView = (RelativeLayout) mInflater.inflate(R.layout.layout_load_more, null);
            LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams2.setMargins(5, 0, 5, 0);

            //convertView.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
            convertView.setBackground(mContext.getResources().getDrawable(R.drawable.featured_transparent));
            convertView.setLayoutParams(layoutParams2);
            ll.addView(convertView);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (featuredTabData.getType() != null) {
                        if (featuredTabData.getType().equals(USER))
                            addFragment(new FragmentInspirationSection(false, featuredTabData.getItem_id()));
                        else
                            addFragment(new FragmentProductBasedOnType(featuredTabData.getType(), featuredTabData.getItem_name(), featuredTabData.getItem_id(), true));
                    }
                }
            });

        }
        return ll;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }


}


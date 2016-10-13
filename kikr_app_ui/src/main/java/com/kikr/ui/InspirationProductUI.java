package com.kikr.ui;

import android.content.Context;
import android.graphics.Color;
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

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentDiscoverDetail;
import com.kikr.fragment.FragmentProductBasedOnInspiration;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.UiUpdate;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.Product;

import java.util.ArrayList;
import java.util.List;

public class InspirationProductUI {
    FragmentActivity mContext;
    List<Product> data;
    LayoutInflater mInflater;
    Inspiration inspiration;
//	View view;

    public InspirationProductUI(FragmentActivity context, Inspiration inspiration, View convertView)
    {
        super();
        this.mContext = context;
//		view=convertView;
        this.data = (ArrayList<Product>) inspiration.getProducts();
        this.inspiration = inspiration;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        LinearLayout ll = new LinearLayout(mContext);
        //LayoutParams layoutParams =new LinearLayout.LayoutParams(CommonUtility.getDeviceWidth(mContext)/2, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 0, 5, 0);
        for (int i = 0; i < data.size(); i++) {
            View convertView = (LinearLayout) mInflater.inflate(R.layout.inspiration_product_list, null);

            ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);

            Product product = data.get(i);


            CommonUtility.setImage(mContext, product.getProductimageurl(), activity_product_list_product_image, R.drawable.dum_list_item_product);
            convertView.setLayoutParams(layoutParams);
            ll.addView(convertView);
            convertView.setTag(i);

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
        if (data.size() > 3) {
            RelativeLayout convertView = (RelativeLayout) mInflater.inflate(R.layout.layout_load_more, null);

            LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            layoutParams2.setMargins(18, 10, 18, 10);
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
            ((TextView) convertView.findViewById(R.id.textView1)).setTextColor(Color.parseColor("#6c6c6c"));

            convertView.setLayoutParams(layoutParams2);
            ll.addView(convertView);
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    addFragment(new FragmentProductBasedOnInspiration("collection", inspiration.getItem_name(), inspiration.getInspiration_id()));
                }
            });
        }

        return ll;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }
}

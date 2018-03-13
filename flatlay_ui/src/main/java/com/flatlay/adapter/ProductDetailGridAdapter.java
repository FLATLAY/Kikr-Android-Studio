package com.flatlay.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.bean.Product;

import java.util.List;

/**
 * Created by RachelDi on 2/28/18.
 */

public class ProductDetailGridAdapter extends BaseAdapter {

    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<Product> products;
    private Animation aa, bb;
    private int index = -1;

    public ProductDetailGridAdapter(FragmentActivity context, List<Product> products, int index) {
        super();
        this.mContext = context;
        this.products = products;
        this.index = index;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Product> data) {
        this.products = data;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int index) {
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
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.layout1 = (LinearLayout) convertView.findViewById(R.id.layout1);
        viewholder.text1 = (TextView) convertView.findViewById(R.id.text1);
        viewholder.text1.setTypeface(FontUtility.setMontserratLight(mContext));
        viewholder.text2 = (TextView) convertView.findViewById(R.id.text2);
        viewholder.text2.setTypeface(FontUtility.setMontserratLight(mContext));
        viewholder.text3 = (TextView) convertView.findViewById(R.id.text3);
        viewholder.text3.setTypeface(FontUtility.setMontserratLight(mContext));
        viewholder.text4 = (TextView) convertView.findViewById(R.id.text4);
        viewholder.text4.setTypeface(FontUtility.setMontserratLight(mContext));

        viewholder.backarrow90 = (ImageView) convertView.findViewById(R.id.backarrow90);
        viewholder.detaillayout = (LinearLayout) convertView.findViewById(R.id.detaillayout);
        viewholder.detaillayout2 = (LinearLayout) convertView.findViewById(R.id.detaillayout2);
        viewholder.dummylayout = (LinearLayout) convertView.findViewById(R.id.dummylayout);

        aa = new RotateAnimation(0.0f, 180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        aa.setRepeatCount(0);
        aa.setFillAfter(true);
        aa.setDuration(400);


        bb = new RotateAnimation(180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        bb.setRepeatCount(0);
        bb.setFillAfter(true);
        bb.setDuration(400);
        final Product currentProduct = getItem(position);

//		CommonUtility.setImage(mContext, getItem(position).getInspiration_image(), viewholder.inspirationImage, R.drawable.dum_list_item_product);
        //	CommonUtility.setImage(mContext, getItem(position).getInspiration_image(), viewholder.inspirationImage);
        CommonUtility.setImage(mContext, viewholder.productImage, getItem(position).getProductimageurl());
        //Picasso.with(mContext).load(getItem(position).getInspiration_image()).transform(new RoundedTransformation(125, 4)).into(viewholder.inspirationImage);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternet()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", currentProduct);
                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
                    detail.setArguments(bundle);
                    ((HomeActivity) mContext).addFragment(detail);
                }
            }
        });
        viewholder.text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternet()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", currentProduct);
                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
                    detail.setArguments(bundle);
                    ((HomeActivity) mContext).addFragment(detail);
                }
            }
        });
        if (index == 1) {
            viewholder.layout1.setVisibility(View.GONE);
        }
        viewholder.layout1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (viewholder.detaillayout.getVisibility() == View.VISIBLE) {
                    viewholder.backarrow90.startAnimation(bb);
                    viewholder.detaillayout.setVisibility(View.GONE);
                    viewholder.detaillayout2.setVisibility(View.GONE);
                    viewholder.dummylayout.setVisibility(View.VISIBLE);

                } else {
                    viewholder.backarrow90.startAnimation(aa);
                    viewholder.detaillayout.setVisibility(View.VISIBLE);
                    viewholder.detaillayout2.setVisibility(View.VISIBLE);
                    viewholder.dummylayout.setVisibility(View.GONE);
                }
            }
        });
        return convertView;
    }

    public boolean checkInternet() {
        if (CommonUtility.isOnline(mContext)) {
            return true;
        } else {
            CommonUtility.showNoInternetAlert(mContext);
            return false;
        }
    }

    public class ViewHolder {
        private ImageView productImage, backarrow90;
        private LinearLayout layout1, detaillayout, detaillayout2, dummylayout;
        private TextView text1, text2, text3, text4;
    }


}


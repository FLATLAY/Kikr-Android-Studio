package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.CustomizeFeedActivity;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.InterestSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RachelDi on 12/8/17.
 */

public class CustomizeInterestProductListAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private LayoutInflater inflater;
    private List<InterestSection> products=new ArrayList<InterestSection>();
    private CustomizeFeedActivity customizeFeedActivity;
    private FragmentProfileView fragmentProfileView;
    private boolean fromProfile = false;

    public CustomizeInterestProductListAdapter(FragmentActivity mContext,List<InterestSection> stores, CustomizeFeedActivity customizeFeedActivity) {
        this.mContext=mContext;
        this.products = stores;
        this.customizeFeedActivity = customizeFeedActivity;
        fromProfile = false;
        inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.w("Activity","CustomizeInterestProductListAdapter");
    }

    public CustomizeInterestProductListAdapter(FragmentActivity mContext,List<InterestSection> stores, FragmentProfileView fragmentProfileView) {
        this.mContext=mContext;
        this.products = stores;
        this.fragmentProfileView = fragmentProfileView;
        fromProfile = true;
        inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void setData(List<InterestSection> data){
        this.products.addAll(data);
    }

    public void addData(InterestSection data){
        this.products.add(data);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public InterestSection getItem(int index) {
        return products.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.adapter_follow_brands_customize, null);
            viewHolder=new ViewHolder();
            viewHolder.searchImageView=(ImageView) convertView.findViewById(R.id.searchImageView);
            viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
            viewHolder.productNameTextView=(TextView) convertView.findViewById(R.id.brandNametextView);
            viewHolder.checkImageView  = (ImageView) convertView.findViewById(R.id.checkImageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        if(fromProfile)
            if(products.get(position).getIs_followedbyviewer()!=null&&products.get(position).getIs_followedbyviewer().equals("yes"))
                viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
            else
                viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
        else
        if(products.get(position).getIs_followed()!=null&&products.get(position).getIs_followed().equals("yes")){
            viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
        }else{
            viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
        }

        CommonUtility.setImage(mContext, getItem(position).getLogo(), viewHolder.searchImageView, R.drawable.ic_placeholder_brand);
        if (!TextUtils.isEmpty(products.get(position).getName())) {
            viewHolder.productNameTextView.setText(capitalizeFirstLetter(products.get(position).getName()));
        }

        viewHolder.checkImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!fromProfile) {
                    if (!TextUtils.isEmpty(products.get(position).getIs_followed())&&customizeFeedActivity.checkInternet()) {
                        if(products.get(position).getIs_followed().equals("yes")){
                            products.get(position).setIs_followed("no");
                            customizeFeedActivity.deleteProduct(getItem(position).getId(),viewHolder.checkImageView.getRootView());
                            notifyDataSetChanged();
                        }else{
                            products.get(position).setIs_followed("yes");
                            customizeFeedActivity.addProduct(getItem(position).getId(),viewHolder.checkImageView.getRootView());
                            notifyDataSetChanged();
                        }
                    }
                }else {
                    if (!TextUtils.isEmpty(products.get(position).getIs_followedbyviewer())&&fragmentProfileView.checkInternet()) {
                        if(products.get(position).getIs_followedbyviewer().equals("yes")){
                            products.get(position).setIs_followedbyviewer("no");
                            fragmentProfileView.deleteProduct(getItem(position).getId(),viewHolder.checkImageView.getRootView());
                            notifyDataSetChanged();
                        }else{
                            products.get(position).setIs_followedbyviewer("yes");
                            fragmentProfileView.addProduct(getItem(position).getId(),viewHolder.checkImageView.getRootView());
                            notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                ((HomeActivity)	mContext).addFragment(new FragmentProductBasedOnType("product", getItem(position).getName(), getItem(position).getId()));
            }
        });
        return convertView;
    }

    public String capitalizeFirstLetter(String original){
        if(original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public class ViewHolder {
        private ImageView searchImageView;
        private ImageView checkImageView;
        private ProgressBar progressBar_follow_brand;
        private TextView productNameTextView;
    }
}

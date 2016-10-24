package com.flatlay.feed_adapter_copy;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.CustomizeFeedFragment;
import com.flatlay.fragment.FragmentProductBasedOnType;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.ui.SquareImageView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.InterestSection;

import java.util.ArrayList;
import java.util.List;

public class CustomizeInterestBrandListAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private LayoutInflater inflater;
    private List<InterestSection> brands = new ArrayList<InterestSection>();
    private CustomizeFeedFragment customizeFeedActivity;
    private FragmentProfileView fragmentProfileView;
    private boolean fromProfile = false;

    public CustomizeInterestBrandListAdapter(FragmentActivity mContext, List<InterestSection> stores, CustomizeFeedFragment customizeFeedActivity) {
        this.mContext = mContext;
        this.brands = stores;
        this.customizeFeedActivity = customizeFeedActivity;
        fromProfile = false;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CustomizeInterestBrandListAdapter(FragmentActivity mContext, List<InterestSection> stores, FragmentProfileView fragmentProfileView) {
        this.mContext = mContext;
        this.brands = stores;
        this.fragmentProfileView = fragmentProfileView;
        fromProfile = true;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<InterestSection> data) {
        this.brands.addAll(data);
    }

    public void addData(InterestSection data) {
        this.brands.add(data);
    }

    @Override
    public int getCount() {
        return brands.size();
    }

    @Override
    public InterestSection getItem(int index) {
        return brands.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_follow_brands_customize, null);
            viewHolder = new ViewHolder();
            viewHolder.searchImageView = (SquareImageView) convertView.findViewById(R.id.searchImageView);
            viewHolder.progressBar_follow_brand = (ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
            viewHolder.brandNameTextView = (TextView) convertView.findViewById(R.id.brandNametextView);
            viewHolder.follow_btn = (TextView) convertView.findViewById(R.id.follow_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (fromProfile)
            if (brands.get(position).getIs_followedbyviewer() != null && brands.get(position).getIs_followedbyviewer().equals("yes")) {
                viewHolder.follow_btn.setText("FOLLOWING");
                viewHolder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.followgreen));
//                int imgResource = R.drawable.ic_check_following;
//                viewHolder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
                viewHolder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
            } else {
                viewHolder.follow_btn.setText("FOLLOW   ");
                viewHolder.follow_btn.setBackgroundResource(R.drawable.btn_borderbg);
//                int imgResource = R.drawable.ic_add_follow;
//                viewHolder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
                viewHolder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
            }
        else if (brands.get(position).getIs_followed() != null && brands.get(position).getIs_followed().equals("yes")) {
            viewHolder.follow_btn.setText("FOLLOWING");
            viewHolder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.followgreen));
//            int imgResource = R.drawable.ic_check_following;
//            viewHolder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
            viewHolder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            viewHolder.follow_btn.setText("FOLLOW   ");
            viewHolder.follow_btn.setBackgroundResource(R.drawable.btn_borderbg);
//            int imgResource = R.drawable.ic_add_follow;
//            viewHolder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
            viewHolder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        CommonUtility.setImage(mContext, getItem(position).getLogo(), viewHolder.searchImageView, R.drawable.ic_placeholder_brand);
        if (!TextUtils.isEmpty(brands.get(position).getName())) {
            viewHolder.brandNameTextView.setText(capitalizeFirstLetter(brands.get(position).getName()));
        }

        viewHolder.follow_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!fromProfile) {
                    if (!TextUtils.isEmpty(brands.get(position).getIs_followed()) && customizeFeedActivity.checkInternet()) {
                        if (brands.get(position).getIs_followed().equals("yes")) {
                            brands.get(position).setIs_followed("no");
                            customizeFeedActivity.deleteBrand(getItem(position).getId(), viewHolder.follow_btn);
                            notifyDataSetChanged();
                        } else {
                            brands.get(position).setIs_followed("yes");
                            customizeFeedActivity.addBrand(getItem(position).getId(), viewHolder.follow_btn);
                            notifyDataSetChanged();
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(brands.get(position).getIs_followedbyviewer()) && fragmentProfileView.checkInternet()) {
                        if (brands.get(position).getIs_followedbyviewer().equals("yes")) {
                            brands.get(position).setIs_followedbyviewer("no");
                            fragmentProfileView.deleteBrand(getItem(position).getId(), viewHolder.follow_btn);
                            notifyDataSetChanged();
                        } else {
                            brands.get(position).setIs_followedbyviewer("yes");
                            fragmentProfileView.addBrand(getItem(position).getId(), viewHolder.follow_btn);
                            notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ((HomeActivity) mContext).addFragment(new FragmentProductBasedOnType("brand", getItem(position).getName(), getItem(position).getId()));
            }
        });
        return convertView;
    }

    public String capitalizeFirstLetter(String original) {
        if (original.length() == 0)
            return original;
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    public class ViewHolder {
        private SquareImageView searchImageView;

        private ProgressBar progressBar_follow_brand;
        private TextView brandNameTextView, follow_btn;
    }
}


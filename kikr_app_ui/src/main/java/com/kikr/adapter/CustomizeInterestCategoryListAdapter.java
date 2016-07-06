package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.CustomizeFeedActivity;
import com.kikr.fragment.FragmentInterestSection;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.InterestSection;

public class CustomizeInterestCategoryListAdapter extends BaseAdapter{
    private FragmentActivity mContext;
    private LayoutInflater inflater;
    //	public boolean[] mSelectedItems;
    private List<InterestSection> categories=new ArrayList<InterestSection>();
    private CustomizeFeedActivity customizeFeedActivity;

    public CustomizeInterestCategoryListAdapter(FragmentActivity mContext,List<InterestSection> stores, CustomizeFeedActivity customizeFeedActivity) {
        inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categories=(ArrayList<InterestSection>) stores;
        this.customizeFeedActivity = customizeFeedActivity;
//		mSelectedItems=new boolean[stores.size()];
//		Arrays.fill(mSelectedItems, false);
        this.mContext = mContext;
    }

    public void setData(List<InterestSection> data){
        this.categories.addAll(data);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public InterestSection getItem(int index) {
        return categories.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView=inflater.inflate(R.layout.adapter_interest_category_list, null);
            viewHolder=new ViewHolder();
            viewHolder.categoryImageView=(ImageView) convertView.findViewById(R.id.categoryImageView);
            viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
            viewHolder.checkImageView=(ImageView) convertView.findViewById(R.id.checkImageView);
            viewHolder.categoryNameTextView=(TextView) convertView.findViewById(R.id.categoryNameTextView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.categoryNameTextView.setText(getItem(position).getName());
        CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.categoryImageView, R.drawable.dum_list_item_brand);
        if(getItem(position).getIs_followed().equalsIgnoreCase("yes")){
            viewHolder.checkImageView.setVisibility(View.VISIBLE);
            viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
        }else{
            viewHolder.checkImageView.setVisibility(View.VISIBLE);
            viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
        }
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(getItem(position).getIs_followed())&&customizeFeedActivity.checkInternet()) {
                    if(getItem(position).getIs_followed().equalsIgnoreCase("yes")){
                        getItem(position).setIs_followed("no");
                        customizeFeedActivity.deleteCategory(getItem(position).getId(),v);
                        notifyDataSetChanged();
                    }else{
                        getItem(position).setIs_followed("yes");
                        customizeFeedActivity.addCategory(getItem(position).getId(),v);
                        notifyDataSetChanged();
                    }
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView categoryImageView,checkImageView;
        TextView categoryNameTextView;
        ProgressBar progressBar_follow_brand;
    }
}


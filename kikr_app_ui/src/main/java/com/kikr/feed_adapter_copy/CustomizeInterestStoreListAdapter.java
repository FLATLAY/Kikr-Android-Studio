package com.kikr.feed_adapter_copy;

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

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.CustomizeFeedFragment;
import com.kikr.fragment.FragmentProductBasedOnType;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.ui.RoundImageView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.InterestSection;

import java.util.ArrayList;
import java.util.List;

public class CustomizeInterestStoreListAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private LayoutInflater inflater;
    //	public boolean[] mSelectedItems;
    private List<InterestSection> stores = new ArrayList<InterestSection>();
    private CustomizeFeedFragment customizeFeedActivity;
    private FragmentProfileView fragmentProfileView;
    private boolean fromProfile = false;

    public CustomizeInterestStoreListAdapter(FragmentActivity mContext, List<InterestSection> stores, CustomizeFeedFragment customizeFeedActivity) {
        this.mContext = mContext;
        this.customizeFeedActivity = customizeFeedActivity;
        this.stores = stores;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fromProfile = false;
//		mSelectedItems=new boolean[stores.size()];
//		Arrays.fill(mSelectedItems, false);
    }

    public CustomizeInterestStoreListAdapter(FragmentActivity mContext, List<InterestSection> stores, FragmentProfileView fragmentProfileView) {
        this.mContext = mContext;
        this.fragmentProfileView = fragmentProfileView;
        this.stores = stores;
        fromProfile = true;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		mSelectedItems=new boolean[stores.size()];
//		Arrays.fill(mSelectedItems, false);
    }

    public void setData(List<InterestSection> data) {
        this.stores.addAll(data);
    }

    public void addData(InterestSection data) {
        this.stores.add(data);
    }

//	public boolean[] getSelectedItems(){
//		return mSelectedItems;
//	}

    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public InterestSection getItem(int index) {
        return stores.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_interest_store_list_cutomize, null);
            viewHolder = new ViewHolder();
            viewHolder.storeImageView = (RoundImageView) convertView.findViewById(R.id.storeImageView);
            viewHolder.progressBar_follow_brand = (ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
            viewHolder.storeNameTextView = (TextView) convertView.findViewById(R.id.storeNameTextView);
            viewHolder.follow_btn = (TextView) convertView.findViewById(R.id.follow_btn);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (fromProfile)

            if (stores.get(position).getIs_followedbyviewer() != null && stores.get(position).getIs_followedbyviewer().equals("yes")) {
                viewHolder.follow_btn.setText("FOLLOWING");
                viewHolder.follow_btn.setBackgroundResource(R.drawable.followgreen);
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
        else if (stores.get(position).getIs_followed() != null && stores.get(position).getIs_followed().equals("yes")) {
            viewHolder.follow_btn.setText("FOLLOWING");
            viewHolder.follow_btn.setBackgroundResource(R.drawable.followgreen);
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
        CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.storeImageView, R.drawable.ic_add_collection);
       // CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.storeImageView, R.drawable.ic_placeholder_brand);
        viewHolder.storeNameTextView.setText(stores.get(position).getName());
        viewHolder.follow_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!fromProfile) {
                    if (!TextUtils.isEmpty(stores.get(position).getIs_followed()) && customizeFeedActivity.checkInternet()) {
                        if (stores.get(position).getIs_followed().equalsIgnoreCase("yes")) {
                            stores.get(position).setIs_followed("no");
                            customizeFeedActivity.unFollowStore(getItem(position).getId(), viewHolder.follow_btn);
                            notifyDataSetChanged();
                        } else {
                            stores.get(position).setIs_followed("yes");
                            customizeFeedActivity.followStore(getItem(position).getId(), viewHolder.follow_btn);
                            notifyDataSetChanged();
                        }
                    }
                } else {
                    if (!TextUtils.isEmpty(stores.get(position).getIs_followedbyviewer()) && fragmentProfileView.checkInternet()) {
                        if (stores.get(position).getIs_followedbyviewer().equalsIgnoreCase("yes")) {
                            stores.get(position).setIs_followedbyviewer("no");
                            fragmentProfileView.unFollowStore(getItem(position).getId(), viewHolder.follow_btn);
                            notifyDataSetChanged();
                        } else {
                            stores.get(position).setIs_followedbyviewer("yes");
                            fragmentProfileView.followStore(getItem(position).getId(), viewHolder.follow_btn);
                            notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ((HomeActivity) mContext).addFragment(new FragmentProductBasedOnType("store", getItem(position).getName(), getItem(position).getId()));
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private RoundImageView storeImageView;
        private TextView storeNameTextView, follow_btn;
        private ProgressBar progressBar_follow_brand;
    }
}


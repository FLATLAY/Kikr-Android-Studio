package com.flatlay.feed_adapter_copy;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
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
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.db.UserPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomizeInterestPeopleListAdapter extends BaseAdapter{
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    public boolean[] mSelectedItems;
    private List<InterestSection> followUsers=new ArrayList<InterestSection>();
    CustomizeFeedFragment customizeFeedActivity;

    public CustomizeInterestPeopleListAdapter(FragmentActivity mContext,List<InterestSection> stores, CustomizeFeedFragment customizeFeedActivity) {
        this.mContext=mContext;
        this.followUsers = stores;
        this.customizeFeedActivity = customizeFeedActivity;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedItems=new boolean[stores.size()];
        Arrays.fill(mSelectedItems, false);
    }

    public void setData(List<InterestSection> data){
        this.followUsers.addAll(data);
    }

    public boolean[] getSelectedItems(){
        return mSelectedItems;
    }

    @Override
    public int getCount() {
        return followUsers.size();
    }

    @Override
    public InterestSection getItem(int index) {
        return followUsers.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.customize_adapter_interest_people_list, null);
            viewHolder = new ViewHolder();
            viewHolder.user_image = (RoundImageView) convertView.findViewById(R.id.user_image);
            viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.follow_btn = (TextView) convertView.findViewById(R.id.follow_btn);
            viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(getItem(position).getUsername())) {
            viewHolder.user_name.setText(getItem(position).getUsername());
        } else {
            viewHolder.user_name.setText("Unknown");
        }
        if(getItem(position).getIs_followed().equalsIgnoreCase("yes")){
            viewHolder.follow_btn.setText("FOLLOWING");
            viewHolder.follow_btn.setBackgroundResource(R.drawable.followgreen);
//            int imgResource = R.drawable.ic_check_following;
//            viewHolder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
            viewHolder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
        }else{
            viewHolder.follow_btn.setText("FOLLOW   ");
            viewHolder.follow_btn.setBackgroundResource(R.drawable.btn_borderbg);
//            int imgResource = R.drawable.ic_add_follow;
//            viewHolder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
            viewHolder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        if(getItem(position).getId().equals(UserPreference.getInstance().getUserID())){
            viewHolder.follow_btn.setVisibility(View.GONE);
        }else{
            viewHolder.follow_btn.setVisibility(View.VISIBLE);
        }
        viewHolder.user_image.setVisibility(View.GONE);
        //CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewHolder.user_image, R.mipmap.dum_user2);
        viewHolder.user_image.setVisibility(View.VISIBLE);

        Log.w("CIPLA:",""+getItem(position).getProfile_pic());

        if (getItem(position).getProfile_pic().isEmpty()) {
            viewHolder.user_image.setImageResource(R.drawable.profile_icon);
        } else{
            Picasso.with(mContext).load(getItem(position).getProfile_pic()).into(viewHolder.user_image);
        }


        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
               if (((HomeActivity) mContext).checkInternet())
                   addFragment(new FragmentProfileView(getItem(position).getId(), getItem(position).getIs_followed()));
            }
        });

        viewHolder.follow_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(getItem(position).getIs_followed()) && customizeFeedActivity.checkInternet()) {
                    if (getItem(position).getIs_followed().equalsIgnoreCase("yes")) {
                        getItem(position).setIs_followed("no");
                        customizeFeedActivity.unFollowUser(getItem(position).getId(), v.getRootView());
                        notifyDataSetChanged();
                    } else {
                        getItem(position).setIs_followed("yes");
                        customizeFeedActivity.followUser(getItem(position).getId(), v.getRootView());
                        notifyDataSetChanged();
                    }
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        RoundImageView user_image;
        TextView user_name,follow_btn;

        ProgressBar progressBar_follow_brand;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }
}


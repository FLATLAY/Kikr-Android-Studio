package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.FeaturedTabUi;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.bean.FeaturedTabData;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.utils.AlertUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RachelDi on 3/15/18.
 */

public class FeaturedTabAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    final String TAG = "FeaturedTabAdapter";

    //    public boolean[] mSelectedItems;
    private List<FeaturedTabData> data = new ArrayList<>();
    //    private List<Inspiration> inspirationList = new ArrayList<>();
    //IMPORTANT: consider using a linked hash map?
//    private HashMap<Integer, List<Inspiration>> positionMap = new HashMap<>();
//    private HashMap<Integer, Boolean> followMap = new HashMap<>();
    private ListAdapterListener mListener;

    private List<UserData> userDetails;
    private Boolean isFollowing = false;

    public FeaturedTabAdapter(FragmentActivity mContext, List<FeaturedTabData> data, ListAdapterListener mListener) {
        this.mContext = mContext;
        this.data = data;
        this.mListener = mListener;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mSelectedItems = new boolean[followUsers.size()];
//        Arrays.fill(mSelectedItems, false);
    }

    public void setData(List<FeaturedTabData> data) {
        this.data = data;
    }

//    public boolean[] getSelectedItems() {
//        return mSelectedItems;
//    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FeaturedTabData getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        final FeaturedTabAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tab_adapter_layout, null);
            viewHolder = new FeaturedTabAdapter.ViewHolder();
            viewHolder.name_text = (TextView) convertView.findViewById(R.id.name_text);
            viewHolder.userImage = (CircleImageView) convertView.findViewById(R.id.userImage);
            viewHolder.follow_icon = (ImageView) convertView.findViewById(R.id.follow_icon);
            viewHolder.scroll_view = (HorizontalScrollView) convertView.findViewById(R.id.scroll_view);
            viewHolder.card_layout = (LinearLayout) convertView.findViewById(R.id.card_layout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FeaturedTabAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.name_text.setTypeface(FontUtility.setMontserratLight(mContext));

        String name = getItem(position).getItem_name();
        if (name != null && name.length() > 0)
            viewHolder.name_text.setText(name);
        else
            viewHolder.name_text.setText("User");
        String profileImage = getItem(position).getProfile_pic();

//        if (profileImage != null && profileImage.length() > 0)
//            Picasso.with(mContext).load(getItem(position).getProfile_pic()).resize(150, 150).into(viewHolder.userImage);
        Log.e(TAG, getItem(position).getProfile_pic());
        Log.e(TAG, getItem(position).getItem_image());
        Log.e(TAG, getItem(position).getItem_name());

        if (profileImage != null && profileImage.length() > 0)
            CommonUtility.setImage(mContext, viewHolder.userImage, getItem(position).getProfile_pic());

//        Picasso.with(mContext).load(getItem(position).getProfile_pic()).resize(150, 150).into(viewHolder.userImage);
        else
            Picasso.with(mContext).load(R.drawable.profile_icon).into(viewHolder.userImage);
//        else
//            Picasso.with(mContext).load(R.drawable.profile_icon).into(viewHolder.userImage);

        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickAtOKButton(position);
            }
        });
        viewHolder.name_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickAtOKButton(position);
            }
        });
        if (getItem(position).getIs_followed() != null && getItem(position).getIs_followed().equals("yes")) {
            isFollowing = true;
            viewHolder.follow_icon.setImageResource(R.drawable.checkedicon);

        } else {
            isFollowing = false;
            viewHolder.follow_icon.setImageResource(R.drawable.follow_white_transparent);
        }

        viewHolder.follow_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItem(position).getIs_followed() != null) {
                    if (getItem(position).getIs_followed().equals("no")) {
                        //add a notifications he
                        getItem(position).setIs_followed("yes");
                        notifyDataSetChanged();
                        ((HomeActivity) mContext).followUser(getItem(position).getItem_id());
                    } else {
                        getItem(position).setIs_followed("no");
                        notifyDataSetChanged();
                        ((HomeActivity) mContext).unFollowUser(getItem(position).getItem_id());
                    }
                }
            }
        });
        viewHolder.card_layout.removeAllViews();
        //????????
//        Log.e("ispirationnn",""+getItem(position).getInspiration_feed().get(0).getLike_count());
        View view = new FeaturedTabUi(mContext, getItem(position), new FeaturedTabUi.ListAdapterListener() {
            @Override
            public void onClickAtOKButton() {
                viewHolder.userImage.performClick();
            }
        }).getView();
        viewHolder.card_layout.addView(view);
        viewHolder.scroll_view.scrollTo(0, 0);

        return convertView;
    }

    public interface ListAdapterListener { // create an interface

        void onClickAtOKButton(int position);
    }

    public class ViewHolder {
        private TextView name_text;
        private CircleImageView userImage;
        private ImageView follow_icon;
        private HorizontalScrollView scroll_view;
        private LinearLayout card_layout;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

}



package com.flatlay.adapter;

import android.content.Context;
import android.os.Build;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.ui.FeaturedTabUi;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.bean.UserResult;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.paypal.android.sdk.P;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RachelDi on 3/14/18.
 */

public class CustomizeInterestPeopleListAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<UserResult> followUsers = new ArrayList<>();
    private List<Inspiration> inspirationList = new ArrayList<>();
    private List<Inspiration> temporarylist = new ArrayList<>();
    private ListAdapterListener mListener;
    final String TAG = "CustomizeInterestPe";

    //IMPORTANT: consider using a linked hash map?
    private LinkedHashMap<String, List<Inspiration>> positionMap = new LinkedHashMap<>();

    private List<UserData> userDetails;
    private Boolean isFollowing = false;

    public CustomizeInterestPeopleListAdapter(FragmentActivity mContext, List<UserResult> followUsers, ListAdapterListener mListener) {
        this.mContext = mContext;
        this.followUsers = followUsers;
        this.mListener = mListener;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void setData(List<UserResult> data) {
        this.followUsers = data;
    }


    @Override
    public int getCount() {
        return followUsers.size();
    }

    @Override
    public UserResult getItem(int index) {
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
            convertView = mInflater.inflate(R.layout.fragment_fetaured_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name_text = (TextView) convertView.findViewById(R.id.name_text);
            viewHolder.userImage = (CircleImageView) convertView.findViewById(R.id.userImage);
            viewHolder.follow_icon = (ImageView) convertView.findViewById(R.id.follow_icon);
            viewHolder.scroll_view = (HorizontalScrollView) convertView.findViewById(R.id.scroll_view);
            viewHolder.card_layout = (LinearLayout) convertView.findViewById(R.id.card_layout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name_text.setTypeface(FontUtility.setMontserratLight(mContext));

        if (position >= followUsers.size()) return convertView;
        String name = getItem(position).getName();
        if (name != null && name.length() > 0)
            viewHolder.name_text.setText(name);
        else
            viewHolder.name_text.setText("User");

        String profileImage = getItem(position).getImg();

        if (profileImage != null && profileImage.length() > 0)
            CommonUtility.setImage(mContext, viewHolder.userImage, getItem(position).getImg());

        else
            Picasso.with(mContext).load(R.drawable.profile_icon).into(viewHolder.userImage);

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

        if (!positionMap.containsKey(getItem(position).getId())) {
            final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
                    inspirationList = inspirationFeedRes.getData();

                    temporarylist = new ArrayList<>();
                    for (int i = 0; i < inspirationList.size(); i++) {
                        temporarylist.add(inspirationList.get(i));
                    }

                    viewHolder.card_layout.removeAllViews();
                    View view = new FeaturedTabUi(mContext, temporarylist, new FeaturedTabUi.ListAdapterListener() {
                        @Override
                        public void onClickAtOKButton() {
                            viewHolder.userImage.performClick();
                        }
                    }).getView();
                    viewHolder.card_layout.addView(view);
                    positionMap.put(getItem(position).getId(), temporarylist);
                    if (positionMap.size() > 200) {
                        Map.Entry<String, List<Inspiration>> mapEntry = positionMap.entrySet().iterator().next();
                        String key = mapEntry.getKey();
                        positionMap.remove(key);
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    if (object != null) {
                        InspirationFeedRes response = (InspirationFeedRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                }
            });
            inspirationFeedApi.getInspirationFeed(getItem(position).getId(), false, String.valueOf(0),
                    UserPreference.getInstance().getUserID());
            inspirationFeedApi.execute();

        } else {
            temporarylist = positionMap.get(getItem(position).getId());
            viewHolder.card_layout.removeAllViews();
            View view = new FeaturedTabUi(mContext, temporarylist, new FeaturedTabUi.ListAdapterListener() {
                @Override
                public void onClickAtOKButton() {
                    viewHolder.userImage.performClick();
                }
            }).getView();
            viewHolder.card_layout.addView(view);
        }

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
                        getItem(position).setIs_followed("yes");
                        notifyDataSetChanged();
                        ((HomeActivity) mContext).followUser(getItem(position).getId());
                    } else {
                        getItem(position).setIs_followed("no");
                        notifyDataSetChanged();
                        ((HomeActivity) mContext).unFollowUser(getItem(position).getId());
                    }
                }
            }
        });
        viewHolder.scroll_view.scrollTo(0, 0);
        return convertView;
    }

    public interface ListAdapterListener { // create an interface

        void onClickAtOKButton(int position);
    }

    public class ViewHolder {
        TextView name_text;
        CircleImageView userImage;
        ImageView follow_icon;
        HorizontalScrollView scroll_view;
        LinearLayout card_layout;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

}



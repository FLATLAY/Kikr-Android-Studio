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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RachelDi on 3/14/18.
 */

public class CustomizeInterestPeopleListAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    //    public boolean[] mSelectedItems;
    private List<UserResult> followUsers = new ArrayList<>();
    private List<Inspiration> inspirationList = new ArrayList<>();
    //IMPORTANT: consider using a linked hash map?
    private HashMap<Integer, List<Inspiration>> positionMap = new HashMap<>();
//    private HashMap<Integer, Boolean> followMap = new HashMap<>();

    private List<UserData> userDetails;
    private Boolean isFollowing = false;

    public CustomizeInterestPeopleListAdapter(FragmentActivity mContext, List<UserResult> followUsers) {
        this.mContext = mContext;
        this.followUsers = followUsers;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        mSelectedItems = new boolean[followUsers.size()];
//        Arrays.fill(mSelectedItems, false);
    }

    public void setData(List<UserResult> data) {
        this.followUsers = data;
    }

//    public boolean[] getSelectedItems() {
//        return mSelectedItems;
//    }

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
            viewHolder.image1 = (ImageView) convertView.findViewById(R.id.image1);
            viewHolder.image2 = (ImageView) convertView.findViewById(R.id.image2);
            viewHolder.image3 = (ImageView) convertView.findViewById(R.id.image3);
            viewHolder.image4 = (ImageView) convertView.findViewById(R.id.image4);
            viewHolder.image5 = (ImageView) convertView.findViewById(R.id.image5);
            viewHolder.image6 = (ImageView) convertView.findViewById(R.id.image6);
            viewHolder.image7 = (ImageView) convertView.findViewById(R.id.image7);
            viewHolder.image8 = (ImageView) convertView.findViewById(R.id.image8);
            viewHolder.image9 = (ImageView) convertView.findViewById(R.id.image9);
//            viewHolder.image10 = (ImageView) convertView.findViewById(R.id.image10);
            viewHolder.follow_icon = (ImageView) convertView.findViewById(R.id.follow_icon);
            viewHolder.scroll_view = (HorizontalScrollView) convertView.findViewById(R.id.scroll_view);
            viewHolder.card_layout = (LinearLayout) convertView.findViewById(R.id.card_layout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        viewHolder.userNameTextView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                startProfilePage(position);
//            }
//        });
//
////        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startProfilePage(position);
//            }
//        });

        String name = getItem(position).getName();
        if (name != null && name.length() > 0)
            viewHolder.name_text.setText(name);
        else
            viewHolder.name_text.setText("User");
        viewHolder.card_layout.setVisibility(View.GONE);
        viewHolder.image1.setVisibility(View.GONE);
        viewHolder.image2.setVisibility(View.GONE);
        viewHolder.image3.setVisibility(View.GONE);
        viewHolder.image4.setVisibility(View.GONE);
        viewHolder.image5.setVisibility(View.GONE);
        viewHolder.image6.setVisibility(View.GONE);
        viewHolder.image7.setVisibility(View.GONE);
        viewHolder.image8.setVisibility(View.GONE);
        viewHolder.image9.setVisibility(View.GONE);
//        viewHolder.image10.setVisibility(View.INVISIBLE);
        String profileImage = getItem(position).getImg();

        if (profileImage != null && profileImage.length() > 0)
            Picasso.with(mContext).load(getItem(position).getImg()).resize(150, 150).into(viewHolder.userImage);
        else
            Picasso.with(mContext).load(R.drawable.profile_icon).into(viewHolder.userImage);


//        viewHolder.product_inflater_layout.removeAllViews();
//        viewHolder.product_layout.post(new Runnable() {
//            @Override
//            public void run() {
//                viewHolder.product_layout.scrollTo(150, 0);
//            }
//        });
//
//        viewHolder.product_layout.scrollTo(0, 0);

//        if (!positionMap.containsKey(position)) {
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
                inspirationList = inspirationFeedRes.getData();
//                    positionMap.put(position, inspirationList);
                if (inspirationList.size() > 0) {
                    viewHolder.card_layout.setVisibility(View.VISIBLE);
                    CommonUtility.setImage(mContext, viewHolder.image1, inspirationList.get(0).getInspiration_image());
                    viewHolder.image1.setVisibility(View.VISIBLE);
                }

                if (inspirationList.size() > 1) {
                    CommonUtility.setImage(mContext, viewHolder.image2, inspirationList.get(1).getInspiration_image());

                    viewHolder.image2.setVisibility(View.VISIBLE);
                }

                if (inspirationList.size() > 2) {
                    CommonUtility.setImage(mContext, viewHolder.image3, inspirationList.get(2).getInspiration_image());

                    viewHolder.image3.setVisibility(View.VISIBLE);
                }

                if (inspirationList.size() > 3) {
                    CommonUtility.setImage(mContext, viewHolder.image4, inspirationList.get(3).getInspiration_image());

                    viewHolder.image4.setVisibility(View.VISIBLE);
                }
                if (inspirationList.size() > 4) {
                    CommonUtility.setImage(mContext, viewHolder.image5, inspirationList.get(4).getInspiration_image());

                    viewHolder.image5.setVisibility(View.VISIBLE);
                }
                if (inspirationList.size() > 5) {
                    CommonUtility.setImage(mContext, viewHolder.image6, inspirationList.get(5).getInspiration_image());

                    viewHolder.image6.setVisibility(View.VISIBLE);
                }
                if (inspirationList.size() > 6) {
                    CommonUtility.setImage(mContext, viewHolder.image7, inspirationList.get(6).getInspiration_image());

                    viewHolder.image7.setVisibility(View.VISIBLE);
                }
                if (inspirationList.size() > 7) {
                    CommonUtility.setImage(mContext, viewHolder.image8, inspirationList.get(7).getInspiration_image());

                    viewHolder.image8.setVisibility(View.VISIBLE);
                }
                if (inspirationList.size() > 8) {
                    CommonUtility.setImage(mContext, viewHolder.image9, inspirationList.get(8).getInspiration_image());

                    viewHolder.image9.setVisibility(View.VISIBLE);
                }
//                    if (inspirationList.size() > 9 && inspirationList.get(9).getInspiration_image().length() > 0) {
//                        CommonUtility.setImage(mContext, inspirationList.get(9).getInspiration_image(),
//                                viewHolder.image10);
//                        viewHolder.image10.setVisibility(View.VISIBLE);
//                    }
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

//            final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
//                @Override
//                public void handleOnSuccess(Object object) {
//
//                    MyProfileRes myProfileRes = (MyProfileRes) object;
//                    userDetails = myProfileRes.getUser_data();
//                    if (userDetails.get(0).getIs_followed() != null && userDetails.get(0).getIs_followed().equals("yes")) {
//                        isFollowing = true;
//                        viewHolder.follow_icon.setImageResource(R.drawable.checkedicon);
//                    } else {
//                        isFollowing = false;
//                        viewHolder.follow_icon.setImageResource(R.drawable.follow_white_transparent);
//                    }
////                    followMap.put(position, isFollowing);
//                }
//
//                @Override
//                public void handleOnFailure(ServiceException exception, Object object) {
//
//                }
//            });
//            myProfileApi.getUserProfileDetail(getItem(position).getId(), UserPreference.getInstance().getUserID());
//            myProfileApi.execute();

//        } else {
//            inspirationList = positionMap.get(position);
//            if (inspirationList.size() > 0) {
//                viewHolder.card_layout.setVisibility(View.VISIBLE);
//                CommonUtility.setImage(mContext, inspirationList.get(0).getInspiration_image(),
//                        viewHolder.image1);
//                viewHolder.image1.setVisibility(View.VISIBLE);
//            }
//
//            if (inspirationList.size() > 1) {
//                CommonUtility.setImage(mContext, inspirationList.get(1).getInspiration_image(),
//                        viewHolder.image2);
//                viewHolder.image2.setVisibility(View.VISIBLE);
//            }
//
//            if (inspirationList.size() > 2) {
//                CommonUtility.setImage(mContext, inspirationList.get(2).getInspiration_image(),
//                        viewHolder.image3);
//                viewHolder.image3.setVisibility(View.VISIBLE);
//            }
//
//            if (inspirationList.size() > 3) {
//                CommonUtility.setImage(mContext, inspirationList.get(3).getInspiration_image(),
//                        viewHolder.image4);
//                viewHolder.image4.setVisibility(View.VISIBLE);
//            }
//            if (inspirationList.size() > 4) {
//                CommonUtility.setImage(mContext, inspirationList.get(4).getInspiration_image(),
//                        viewHolder.image5);
//                viewHolder.image5.setVisibility(View.VISIBLE);
//            }
//            if (inspirationList.size() > 5) {
//                CommonUtility.setImage(mContext, inspirationList.get(5).getInspiration_image(),
//                        viewHolder.image6);
//                viewHolder.image6.setVisibility(View.VISIBLE);
//            }
//            if (inspirationList.size() > 6) {
//                CommonUtility.setImage(mContext, inspirationList.get(6).getInspiration_image(),
//                        viewHolder.image7);
//                viewHolder.image7.setVisibility(View.VISIBLE);
//            }
//            if (inspirationList.size() > 7) {
//                CommonUtility.setImage(mContext, inspirationList.get(7).getInspiration_image(),
//                        viewHolder.image8);
//                viewHolder.image8.setVisibility(View.VISIBLE);
//            }
//            if (inspirationList.size() > 8) {
//                CommonUtility.setImage(mContext, inspirationList.get(8).getInspiration_image(),
//                        viewHolder.image9);
//                viewHolder.image9.setVisibility(View.VISIBLE);
//            }
////                    if (inspirationList.size() > 9 && inspirationList.get(9).getInspiration_image().length() > 0) {
////                        CommonUtility.setImage(mContext, inspirationList.get(9).getInspiration_image(),
////                                viewHolder.image10);
////                        viewHolder.image10.setVisibility(View.VISIBLE);
////                    }
//
//
////
//        }

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

//    public void startProfilePage(int pos) {
//
//        addFragment(new FragmentProfileView(getItem(pos).getId(), "no"));
//
//    }

    public class ViewHolder {
        private TextView name_text;
        private CircleImageView userImage;
        private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9,
                follow_icon;
        private HorizontalScrollView scroll_view;
        private LinearLayout card_layout;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

}



package com.flatlay.feed_adapter_copy;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.CustomizeFeedFragment;
import com.flatlay.fragment.FragmentFeatured;
import com.flatlay.fragment.FragmentProductBasedOnType;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.ui.FeaturedTabUi;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.Product;

import java.util.ArrayList;
import java.util.List;

public class CustomizeInterestStoreListAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private LayoutInflater inflater;
    //	public boolean[] mSelectedItems;
    private List<InterestSection> stores = new ArrayList<InterestSection>();
    //private CustomizeFeedActivity customizeFeedActivity;
    private FragmentProfileView fragmentProfileView;
    private boolean fromProfile = false;
    private FragmentFeatured fragmentFeatured;



    public CustomizeInterestStoreListAdapter(FragmentActivity mContext, List<InterestSection> stores, FragmentFeatured fragmentFeatured) {
        this.mContext = mContext;
        //this.customizeFeedActivity = customizeFeedActivity;
        this.fragmentFeatured = fragmentFeatured;
        this.stores = stores;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fromProfile = false;
//		mSelectedItems=new boolean[stores.size()];
//		Arrays.fill(mSelectedItems, false);
        Log.w("Activity", "CustomizeInterestStoreListAdapter");
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
//            convertView=inflater.inflate(R.layout.adapter_interest_store_list_cutomize, null);
//            viewHolder=new ViewHolder();
//            viewHolder.storeImageView=(ImageView) convertView.findViewById(R.id.storeImageView);
//            viewHolder.progressBar_follow_brand=(ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
//            viewHolder.storeNameTextView=(TextView) convertView.findViewById(R.id.storeNameTextView);
//            viewHolder.checkImageView  = (ImageView) convertView.findViewById(R.id.checkImageView);
            convertView = inflater.inflate(R.layout.fragment_fetaured_item, null);
            viewHolder = new ViewHolder();
            viewHolder.userNameTextView = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.featuredLargeImage = (ImageView) convertView.findViewById(R.id.featuredImage);
            viewHolder.descriptionTextView = (TextView) convertView.findViewById(R.id.tvDescription);

            viewHolder.follow_btn_layout = (LinearLayout) convertView.findViewById(R.id.follow_btn_layout);
            viewHolder.userImage = (RoundImageView) convertView.findViewById(R.id.userImage);
            viewHolder.follow_btn = (TextView) convertView.findViewById(R.id.follow_btn);
            viewHolder.product_layout = (HorizontalScrollView) convertView.findViewById(R.id.productLayout);
            viewHolder.product_inflater_layout = (LinearLayout) convertView.findViewById(R.id.productInflaterLayout);
            viewHolder.collectionCount = (TextView) convertView.findViewById(R.id.collection_count);
            viewHolder.followersCount = (TextView) convertView.findViewById(R.id.follower_count);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.userNameTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startProfilePage(position);
            }
        });
        viewHolder.userImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfilePage(position);
            }
        });
        viewHolder.collectionCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfilePage(position);
            }
        });
        viewHolder.followersCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfilePage(position);
            }
        });



        String name = getItem(position).getName();
        if (!org.apache.http.util.TextUtils.isEmpty(name))
            viewHolder.userNameTextView.setText(name);
        else
            viewHolder.userNameTextView.setText("Unknown");
        if (!org.apache.http.util.TextUtils.isEmpty(getItem(position).getItem_description())) {
            viewHolder.descriptionTextView.setText(getItem(position).getItem_description());
            viewHolder.descriptionTextView.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.descriptionTextView.setVisibility(View.GONE);
        }

        CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.userImage, R.drawable.dum_list_item_product);
        CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewHolder.featuredLargeImage);

        final List<Product> data = getItem(position).getProducts();
        final List<Inspiration> feed = getItem(position).getInspiration_feed();
        if (getItem(position).getIs_followed() != null && getItem(position).getIs_followed().equals("yes")) {
            viewHolder.follow_btn.setText("Following");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_borderbg));
            }
//            int imgResource = R.drawable.ic_check_following;
//            viewholder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
            viewHolder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));

        } else {
            viewHolder.follow_btn.setText(" Follow + ");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewHolder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.followgreen));
            }
//            int imgResource = R.drawable.ic_add_follow;
//            viewholder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
            viewHolder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
        }

        viewHolder.follow_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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

        viewHolder.product_layout.scrollTo(0, 0);
        viewHolder.product_inflater_layout.removeAllViews();
       // viewHolder.product_inflater_layout.addView(new FeaturedTabUi(mContext,  stores, getItem(position), fragmentFeatured, convertView).getView());
        viewHolder.product_layout.post(new Runnable() {
            @Override
            public void run() {
                viewHolder.product_layout.scrollTo(150, 0);
            }
        });


//        viewHolder.followersCount.setText(getItem(position).getFollowers_count() + " Followers");
//        viewHolder.collectionCount.setText(getItem(position).getCollections_count() + " Collections");


//        if (fromProfile)
//            if (stores.get(position).getIs_followedbyviewer() != null && stores.get(position).getIs_followedbyviewer().equals("yes")) {
//                //   viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
//            }
//            else {
//                //  viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
//            }
//        else if (stores.get(position).getIs_followed() != null && stores.get(position).getIs_followed().equals("yes")) {
//           // viewHolder.checkImageView.setImageResource(R.drawable.ic_follow_category_tick);
//        } else {
//           // viewHolder.checkImageView.setImageResource(R.drawable.ic_add_collection);
//        }
        //CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.storeImageView, R.drawable.ic_placeholder_brand);
        // viewHolder.storeNameTextView.setText(stores.get(position).getName());
        // viewHolder.checkImageView.setOnClickListener(new OnClickListener() {

//            @Override
//            public void onClick(View v) {
//                if (!fromProfile) {
//                    if (!TextUtils.isEmpty(stores.get(position).getIs_followed()) && customizeFeedActivity.checkInternet()) {
//                        if (stores.get(position).getIs_followed().equalsIgnoreCase("yes")) {
//                            stores.get(position).setIs_followed("no");
//                            customizeFeedActivity.unFollowStore(getItem(position).getId(), viewHolder.checkImageView.getRootView());
//                            notifyDataSetChanged();
//                        } else {
//                            stores.get(position).setIs_followed("yes");
//                            customizeFeedActivity.followStore(getItem(position).getId(), viewHolder.checkImageView.getRootView());
//                            notifyDataSetChanged();
//                        }
//                    }
//                } else {
//                    if (!TextUtils.isEmpty(stores.get(position).getIs_followedbyviewer()) && fragmentProfileView.checkInternet()) {
//                        if (stores.get(position).getIs_followedbyviewer().equalsIgnoreCase("yes")) {
//                            stores.get(position).setIs_followedbyviewer("no");
//                            fragmentProfileView.unFollowStore(getItem(position).getId(), viewHolder.checkImageView.getRootView());
//                            notifyDataSetChanged();
//                        } else {
//                            stores.get(position).setIs_followedbyviewer("yes");
//                            fragmentProfileView.followStore(getItem(position).getId(), viewHolder.checkImageView.getRootView());
//                            notifyDataSetChanged();
//                        }
//                    }
//                }
//            }
//        });
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // ((HomeActivity)	mContext).addFragment(new FragmentProductBasedOnType("store", getItem(position).getName(), getItem(position).getId()));
            }
        });
        return convertView;
    }

    public void startProfilePage(int pos)
    {
        if (getItem(pos).getType() != null) {
                addFragment(new FragmentProfileView(getItem(pos).getId(), "no"));
                addFragment(new FragmentProductBasedOnType(getItem(pos).getType(), getItem(pos).getName(), getItem(pos).getId()));
        }
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

    public class ViewHolder {
        //            private ImageView checkImageView, storeImageView;
//            private TextView storeNameTextView;
//            private ProgressBar progressBar_follow_brand;
        TextView userNameTextView, viewAllTextView;
        ImageView featuredLargeImage;
        RoundImageView userImage;
        TextView descriptionTextView;
        LinearLayout imageLayout1, imageLayout2;
        List<ImageView> list = new ArrayList<ImageView>();
        TextView collectionCount, followersCount;
        ProgressBar progressBar_follow_brand;
        TextView follow_btn;
        HorizontalScrollView product_layout;
        LinearLayout product_inflater_layout, follow_btn_layout;
    }
}


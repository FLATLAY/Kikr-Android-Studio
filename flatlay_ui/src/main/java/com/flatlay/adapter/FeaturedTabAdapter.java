
package com.flatlay.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import com.flatlaylib.bean.FeaturedTabData;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;

import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class FeaturedTabAdapter extends BaseAdapter {

    private FragmentActivity mContext;
    private FragmentFeatured fragmentFeatured;
//private CustomizeFeedFragment fragmentFeatured;

    private LayoutInflater mInflater;
    private List<FeaturedTabData> brandsArray;
    private List<FeaturedTabData> check;
    //	List<Product> data;
//	enum ItemType{brand,store,user};
    String BRAND = "brand";
    String STORE = "store";
    String USER = "user";

    public FeaturedTabAdapter(FragmentActivity context, List<FeaturedTabData> brandsArray, FragmentFeatured fragmentFeatured) {
        super();
        this.mContext = context;
        this.fragmentFeatured = fragmentFeatured;
        this.brandsArray = brandsArray;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.w("Activity","FeaturedTabAdapter");
    }

    public void setData(List<FeaturedTabData> data) {
        brandsArray.addAll(data);
    }

    @Override
    public int getCount() {
        return brandsArray.size();
    }

    @Override
    public FeaturedTabData getItem(int index) {

        return brandsArray.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_fetaured_item, null);
            viewholder = new ViewHolder();
            viewholder.userNameTextView = (TextView) convertView.findViewById(R.id.userName);
            viewholder.featuredLargeImage = (ImageView) convertView.findViewById(R.id.featuredImage);
            viewholder.descriptionTextView = (TextView) convertView.findViewById(R.id.tvDescription);

            viewholder.follow_btn_layout = (LinearLayout) convertView.findViewById(R.id.follow_btn_layout);
            viewholder.userImage = (RoundImageView) convertView.findViewById(R.id.userImage);
            viewholder.follow_btn = (TextView) convertView.findViewById(R.id.follow_btn);
            viewholder.product_layout = (HorizontalScrollView) convertView.findViewById(R.id.productLayout);
            viewholder.product_inflater_layout = (LinearLayout) convertView.findViewById(R.id.productInflaterLayout);
            viewholder.collectionCount = (TextView) convertView.findViewById(R.id.collection_count);
            viewholder.followersCount = (TextView) convertView.findViewById(R.id.follower_count);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        FeaturedTabData featuredTabData = getItem(position);
        viewholder.userNameTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startProfilePage(position);
            }
        });
        viewholder.userImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfilePage(position);
            }
        });
        viewholder.collectionCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfilePage(position);
            }
        });
        viewholder.followersCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfilePage(position);
            }
        });

        String name = getItem(position).getItem_name();
        if (!TextUtils.isEmpty(name))
            viewholder.userNameTextView.setText(name);
        else
            viewholder.userNameTextView.setText("Unknown");
        if (!TextUtils.isEmpty(getItem(position).getItem_description())) {
            viewholder.descriptionTextView.setText(getItem(position).getItem_description());
            viewholder.descriptionTextView.setVisibility(View.VISIBLE);
        }
        else {
            viewholder.descriptionTextView.setVisibility(View.GONE);
        }
        viewholder.followersCount.setText(getItem(position).getFollowers_count() + " Followers");
        viewholder.collectionCount.setText(getItem(position).getCollections_count() + " Collections");


        if(getItem(position).getType().equals(USER))
            CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewholder.userImage, R.drawable.dum_list_item_product);
//        else
//            CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewholder.userImage, R.drawable.dum_list_item_product);

        CommonUtility.setImage(mContext, getItem(position).getItem_image(), viewholder.featuredLargeImage);

        final List<Product> data = getItem(position).getProducts();
        final List<Inspiration> feed = getItem(position).getInspiration_feed();


        if (getItem(position).getIs_followed() != null && getItem(position).getIs_followed().equals("yes")) {
            viewholder.follow_btn.setText("Following");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewholder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_borderbg));
            }
//            int imgResource = R.drawable.ic_check_following;
//            viewholder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
            viewholder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));

        } else {
            viewholder.follow_btn.setText(" Follow + ");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                viewholder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.followgreen));
            }
//            int imgResource = R.drawable.ic_add_follow;
//            viewholder.follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
            viewholder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        viewholder.follow_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(position).getIs_followed() != null) {
                    if (getItem(position).getIs_followed().equals("no")) {
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
        viewholder.product_inflater_layout.removeAllViews();

/*if(getItem(position).getProducts()==null){
   LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams2.setMargins(0, 0, 0, 15);
    convertView.setLayoutParams(layoutParams2);
}*/
        viewholder.product_inflater_layout.addView(new FeaturedTabUi(mContext,  brandsArray, getItem(position), fragmentFeatured, convertView).getView());


        viewholder.product_layout.post(new Runnable() {
            @Override
            public void run() {
                viewholder.product_layout.scrollTo(150, 0);
            }
        });


        viewholder.product_layout.scrollTo(0, 0);


        return convertView;
    }

    public void startProfilePage(int pos)
    {
        if (getItem(pos).getType() != null) {
            if (getItem(pos).getType().equals(USER))
                addFragment(new FragmentProfileView(getItem(pos).getItem_id(), "no"));
            else
                addFragment(new FragmentProductBasedOnType(getItem(pos).getType(), getItem(pos).getItem_name(), getItem(pos).getItem_id()));
        }
    }

    public class ViewHolder {
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

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

}

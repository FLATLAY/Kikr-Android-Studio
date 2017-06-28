package com.flatlay.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.dialog.RemoveProduct;
import com.flatlay.dialog.ShareDialog;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.ui.InspirationProductUI;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.AutoTextSize;
import com.flatlay.utility.CallBack;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class
InspirationAdapter extends BaseAdapter {

    static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/#/flatlay/";
    String postlink;
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<Inspiration> inspirations;
    private boolean isViewAll;
    String likeId = "";
    private ProgressBarDialog mProgressBarDialog;
    FragmentInspirationSection fragmentInspirationSection;

    public InspirationAdapter(FragmentActivity context, List<Inspiration> inspirations, boolean isViewAll, FragmentInspirationSection fragmentInspirationSection) {
        super();
        this.fragmentInspirationSection = fragmentInspirationSection;
        this.mContext = context;
        this.inspirations = inspirations;
        this.isViewAll = isViewAll;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.w("Activity","InspirationAdapter");
    }

    public void setData(List<Inspiration> data) {
        this.inspirations = data;
    }

    @Override
    public int getCount() {
        return inspirations.size();
    }

    @Override
    public Inspiration getItem(int index) {
        return inspirations.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_inspiration_large, null);
            viewholder = new ViewHolder();
            viewholder.inspirationImage = (ImageView) convertView.findViewById(R.id.inspirationImage);
            viewholder.inspirationImage1 = (ImageView) convertView.findViewById(R.id.inspirationImage1);

            viewholder.deletePost = (ImageView) convertView.findViewById(R.id.deletePost);
            viewholder.sharetofriend = (ImageView) convertView.findViewById(R.id.sharetofriend);
            viewholder.inspirationImage2 = (ImageView) convertView.findViewById(R.id.inspirationImage2);
            viewholder.inspirationImage3 = (ImageView) convertView.findViewById(R.id.inspirationImage3);
            viewholder.inspirationImage4 = (ImageView) convertView.findViewById(R.id.inspirationImage4);
            viewholder.inspirationImage5 = (ImageView) convertView.findViewById(R.id.inspirationImage5);
            viewholder.userImage = (RoundImageView) convertView.findViewById(R.id.userImage);
            viewholder.userName = (AutoTextSize) convertView.findViewById(R.id.userName);
            viewholder.inspirationTime = (TextView) convertView.findViewById(R.id.inspirationTime);
            viewholder.productLayout = (HorizontalScrollView) convertView.findViewById(R.id.productLayout);
            viewholder.productInflaterLayout = (LinearLayout) convertView.findViewById(R.id.productInflaterLayout);
            viewholder.user_profile_layout = (FrameLayout) convertView.findViewById(R.id.user_profile_layout);
            viewholder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            viewholder.noProductTextView = (TextView) convertView.findViewById(R.id.noProductTextView);
            viewholder.follower_count = (TextView) convertView.findViewById(R.id.follower_count);
            viewholder.collection_count = (TextView) convertView.findViewById(R.id.collection_count);
            viewholder.follow_btn_layout = (LinearLayout) convertView.findViewById(R.id.follow_btn_layout);
            viewholder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
            viewholder.likeCount = (TextView) convertView.findViewById(R.id.likeCount);
            viewholder.commentCount = (TextView) convertView.findViewById(R.id.commentCount);
            viewholder.descriptionArrow = (ImageView) convertView.findViewById(R.id.descriptionArrow);
            viewholder.follow_btn = (TextView) convertView.findViewById(R.id.follow_btn);
            viewholder.contentContainer = (FrameLayout) convertView.findViewById(R.id.content_container);
            viewholder.imgLayout = (LinearLayout) convertView.findViewById(R.id.imgLayout);
            viewholder.totalItemsText = (TextView) convertView.findViewById(R.id.totalItemsText);
            viewholder.likeCountImage = (ImageView) convertView.findViewById(R.id.likeCountTextImage);
            viewholder.productTitleTextView = (TextView) convertView.findViewById(R.id.productTitleTextView);
            viewholder.expand = (RelativeLayout) convertView.findViewById(R.id.expand);
            viewholder.CommentCountTextImage = (ImageView) convertView.findViewById(R.id.CommentCountTextImage);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        Syso.info(">>>>>>>>" + getItem(position));
        if (!isViewAll && position > 0) {
            viewholder.user_profile_layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(getItem(position).getUsername())) {
            viewholder.userName.setText(getItem(position).getUsername());
        } else {
            viewholder.userName.setText("Unknown");
        }
        if (!TextUtils.isEmpty(getItem(position).getFollower_count())) {
            viewholder.follower_count.setText(getItem(position).getFollower_count() + " Followers\n");
        } else {
            viewholder.follower_count.setText("0 Followers");
        }
        if (!TextUtils.isEmpty(getItem(position).getCollection_count())) {
            viewholder.collection_count.setText(getItem(position).getCollection_count() + " Collections");
        } else {
            viewholder.collection_count.setText("0 Collections");
        }
        if (!TextUtils.isEmpty(getItem(position).getIs_followed())) {
            if (getItem(position).getIs_followed().equals("yes")) {
            } else {
            }
        }
        String str = getItem(position).getItem_name();
        viewholder.productTitleTextView.setText(str);

        viewholder.descriptionArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewholder.contentContainer.getVisibility() == View.VISIBLE) {
                    viewholder.contentContainer.setVisibility(View.GONE);
                    viewholder.descriptionArrow.setImageResource(R.drawable.ic_down_gray_arrow);
                    viewholder.imgLayout.setVisibility(View.VISIBLE);
                    viewholder.totalItemsText.setVisibility(View.GONE);
                } else {
                    viewholder.contentContainer.setVisibility(View.VISIBLE);
                    viewholder.descriptionArrow.setImageResource(R.drawable.ic_up_gray_arrow);
                    viewholder.imgLayout.setVisibility(View.GONE);
                    viewholder.totalItemsText.setVisibility(View.VISIBLE);
                }
            }
        });
        viewholder.commentCount.setText(getItem(position).getComment_count());
        viewholder.likeCount.setText(getItem(position).getLike_count());
        if (UserPreference.getInstance().getUserName() != "")

            if (Integer.parseInt(getItem(position).getLike_count()) == 0) {

                viewholder.likeCountImage.setImageResource(R.drawable.ic_heart_outline_grey);
            } else {

                viewholder.likeCountImage.setImageResource(R.drawable.ic_heart_red);
            }
//        if (Integer.parseInt(getItem(position).getLike_count()) == 0) {
//
//            viewholder.sharetofriend.setImageResource(R.drawable.share);
//        }
//        else {
//
//          viewholder.sharetofriend.setImageResource(R.drawable.shareclicked);
//        }

        if (Integer.parseInt(getItem(position).getComment_count()) == 0) {

            viewholder.CommentCountTextImage.setImageResource(R.drawable.chat_icon);
        } else {

            viewholder.CommentCountTextImage.setImageResource(R.drawable.chat_icon_green);
        }

        if (!TextUtils.isEmpty(getItem(position).getDescription()))
            viewholder.descriptionTextView.setText(getItem(position).getDescription());
        viewholder.likeCountImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.w("InspirationAdapter","setOnClickListener()");
                if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                    CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                    createAccountDialog.show();
                } else {
                    if (((HomeActivity) mContext).checkInternet()) {
                        //Log.w("InspirationAdapter","position"+position);
                        likeId = getItem(position).getLike_id();
                        //Log.w("InspirationAdapter","likeId"+likeId);
                        likeInspiration(position);
                    }
                }
            }
        });
        if (getItem(position).getIs_followed() != null && getItem(position).getIs_followed().equals("yes")) {
            viewholder.follow_btn.setText("FOLLOWING");
            viewholder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.followgreen));
            viewholder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
            //Log.w("Inspiration Adapter","1");
            //notifyDataSetChanged();

        } else {
            viewholder.follow_btn.setText("  FOLLOW   ");
            viewholder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_borderbg_gray));
            viewholder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.app_text_color));
            //Log.w("Inspiration Adapter","2");
            //notifyDataSetChanged();
        }
        viewholder.follow_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(position).getIs_followed() != null) {
                    if (getItem(position).getIs_followed().equals("no")) {
                        getItem(position).setIs_followed("yes");
                        notifyDataSetChanged();
                        //Log.w("Inspiration Adapter","3");
                        ((HomeActivity) mContext).followUser(getItem(position).getUser_id());
                    } else {
                        getItem(position).setIs_followed("no");
                        notifyDataSetChanged();
                        //Log.w("Inspiration Adapter","4");
                        ((HomeActivity) mContext).unFollowUser(getItem(position).getUser_id());
                    }
                }
            }
        });
        viewholder.sharetofriend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                postlink = SHARE_POST_LINK + getItem(position).getInspiration_id();
                //  System.out.print(SHARE_POST_LINK);
                viewholder.sharetofriend.setImageResource(R.drawable.shareclicked);
                ShareDialog dialog = new ShareDialog(mContext, (HomeActivity) mContext, getItem(position).getInspiration_image(), postlink);
                dialog.show();

            }
        });


        Calendar calLocal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calServer = Calendar.getInstance();
        try {

            Log.e("date from server >>>", getItem(position).getDateadded() + "");
//			Log.e("date converted local >>>",df.parse(getItem(position).getDateadded())+ "");
            Date dd = df.parse(getItem(position).getDateadded());
//			Log.e("date converted local 222 >>>",df2.format(dd)+ "");
            calServer.setTime(dd);

//			calServer.setTime(df.parse(getItem(position).getDateadded()));
//			calServer.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //viewholder.expandablePanel.
        viewholder.inspirationTime.setText(CommonUtility.calculateTimeDiff(calServer, calLocal));
        CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewholder.userImage, R.drawable.dum_user);
        String image = getItem(position).getInspiration_image();
        System.out.print(image);
        CommonUtility.setImage(mContext, getItem(position).getInspiration_image(), viewholder.inspirationImage, R.drawable.dum_list_item_product);
        //Picasso.with(mContext).load(getItem(position).getInspiration_image()).resize(500, 500).into(viewholder.inspirationImage);


        //InspirationRes res=(InspirationRes)getItem(position);
        List<Product> data = getItem(position).getProducts();
        viewholder.productInflaterLayout.removeAllViews();
        if (data != null && data.size() > 0) {
            try {
                viewholder.totalItemsText.setText(getItem(position).getProducts().size() + " More Items");
                CommonUtility.setImage(mContext, getItem(position).getProducts().get(0).getProductimageurl(), viewholder.inspirationImage1, R.drawable.dum_list_item_product);
                viewholder.inspirationImage1.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                viewholder.inspirationImage1.setVisibility(View.GONE);
                Log.e("", ex.getMessage());
            }
            try {
                CommonUtility.setImage(mContext, getItem(position).getProducts().get(1).getProductimageurl(), viewholder.inspirationImage2, R.drawable.dum_list_item_product);
                viewholder.inspirationImage2.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                viewholder.inspirationImage2.setVisibility(View.GONE);
                Log.e("", ex.getMessage());
            }
            try {

                CommonUtility.setImage(mContext, getItem(position).getProducts().get(2).getProductimageurl(), viewholder.inspirationImage3, R.drawable.dum_list_item_product);

                viewholder.inspirationImage3.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                viewholder.inspirationImage3.setVisibility(View.GONE);
                Log.e("", ex.getMessage());
            }
            try {
                CommonUtility.setImage(mContext, getItem(position).getProducts().get(3).getProductimageurl(), viewholder.inspirationImage4, R.drawable.dum_list_item_product);
                viewholder.inspirationImage4.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                viewholder.inspirationImage4.setVisibility(View.GONE);
                Log.e("", ex.getMessage());
            }
            try {

                CommonUtility.setImage(mContext, getItem(position).getProducts().get(4).getProductimageurl(), viewholder.inspirationImage5, R.drawable.dum_list_item_product);

                viewholder.inspirationImage5.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                viewholder.inspirationImage5.setVisibility(View.GONE);
                Log.e("", ex.getMessage());
            }
            viewholder.noProductTextView.setVisibility(View.GONE);
            viewholder.totalItemsText.setVisibility(View.GONE);
            viewholder.imgLayout.setVisibility(View.VISIBLE);
            viewholder.productInflaterLayout.addView(new InspirationProductUI(mContext, getItem(position), convertView).getView());
            viewholder.descriptionArrow.setVisibility(View.VISIBLE);
        } else {
            viewholder.totalItemsText.setText("");
            viewholder.totalItemsText.setVisibility(View.VISIBLE);
            viewholder.imgLayout.setVisibility(View.GONE);
            viewholder.descriptionArrow.setVisibility(View.GONE);
        }

        if (StringUtils.isEmpty(getItem(position).getItem_name()) && (data == null || data.size() == 0) || getItem(position).getItem_name().trim().equals("(null)")) {
            viewholder.expand.setVisibility(View.GONE);
        } else
            viewholder.expand.setVisibility(View.VISIBLE);
        viewholder.productLayout.scrollTo(0, 0);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((HomeActivity) mContext).checkInternet()) {
                    addFragment(new FragmentInspirationDetail(getItem(position), true,true,false));
                }
            }
        });
        viewholder.user_profile_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (((HomeActivity) mContext).checkInternet()) {
                    addFragment(new FragmentProfileView(getItem(position).getUser_id(), "no"));
                }
            }
        });

        if (UserPreference.getInstance().getUserID().equals(getItem(position).getUser_id())) {
            viewholder.deletePost.setVisibility(View.VISIBLE);
            viewholder.follow_btn.setVisibility(View.GONE);
        } else {
            viewholder.deletePost.setVisibility(View.GONE);
            viewholder.follow_btn.setVisibility(View.VISIBLE);
        }


        viewholder.deletePost.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                RemoveProduct removeProduct = new RemoveProduct(mContext, getItem(position), fragmentInspirationSection, new CallBack() {

                    @Override
                    public void onSuccess() {
//                        data.remove(position);
//                        if (data.size()>10) {
//                            CartOverLoadDialog cartOverLoadDialog = new CartOverLoadDialog(mContext);
//                            cartOverLoadDialog.show();
//                        }
//                        notifyDataSetChanged();
////                        if(data.size()==0){
////                            fragmentUserCart.showEmptyCart();
////                        }
//                        UserPreference.getInstance().decCartCount();
//                        ((HomeActivity)mContext).refreshCartCount();
                    }

                    @Override
                    public void onFail() {

                    }
                });
                removeProduct.show();

//                Toast.makeText(mContext, "this is my Toast message!!! =)",
//                        Toast.LENGTH_LONG).show();
            }
        });
//        }
//        else
//        {
//            viewholder.deletePost.setVisibility(View.GONE);
//        }

        this.notifyDataSetChanged();
        return convertView;
    }

    public class ViewHolder {
        private ImageView inspirationImage, inspirationImage1, inspirationImage2, inspirationImage3, inspirationImage4, inspirationImage5, descriptionArrow, deletePost, sharetofriend;
        private RoundImageView userImage;
        private HorizontalScrollView productLayout;
        private LinearLayout productInflaterLayout;
        private TextView noProductTextView, inspirationTime;
        private AutoTextSize userName;
        private ProgressBar progressBar;
        private FrameLayout user_profile_layout;
        private TextView follower_count, collection_count, totalItemsText;
        private LinearLayout follow_btn_layout, imgLayout;
        private TextView descriptionTextView, likeCount, commentCount, follow_btn;
        FrameLayout contentContainer;
        ImageView likeCountImage, CommentCountTextImage;
        TextView productTitleTextView;
        RelativeLayout expand;

    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }


    private void likeInspiration(final int position) {
        // final TextView v = (TextView) likeCount.findViewById(R.id.likeCount);
        //Log.w("InspirationAdapter","likeInspiration()");
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                InspirationRes inspirationRes = (InspirationRes) object;
                likeId = inspirationRes.getLike_id();

                //Log.w("InspirationAdapater","In handle onSuccess"+likeId);
                if (TextUtils.isEmpty(likeId)) {
                    getItem(position).setLike_id(likeId);
                    //Log.w("InspirationAdapater",position+ "here1*"+ getItem(position).getLike_id());
                    getItem(position).setLike_count(((getInt(getItem(position).getLike_count()) - 1) < 0 ? 0 : (getInt(getItem(position).getLike_count()) - 1)) + "");

                } else {
                    //Log.w("InspirationAdapater","here2");
                    if (getItem(position).getLike_count() != null) {
                        //Log.w("InspirationAdapater","here3");
                        getItem(position).setLike_count((getInt(getItem(position).getLike_count()) + 1) + "");
                        getItem(position).setLike_id(likeId);
                        //Log.w("InspirationAdapater",position+ "here4*"+ getItem(position).getLike_id());
                    }
                }
                //Log.w("InspirationAdapater","In handle onSuccess"+likeId);
                notifyDataSetChanged();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }
        });
        if (TextUtils.isEmpty(likeId)) {
            //Log.w("InspirationAdapter","postLike()");
            inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), getItem(position).getInspiration_id(), "inspiration");
        }
        else {
            //Log.w("InspirationAdapter","removeLike()");
            inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), likeId);
        }
        inspirationSectionApi.execute();
    }

    private int getInt(String image_width) {
        try {

            return Integer.parseInt(image_width);
        } catch (Exception e) {
            return 0;
        }
    }
}
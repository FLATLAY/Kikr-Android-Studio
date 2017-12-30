package com.flatlay.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.flatlay.dialog.PinterestBoardDialog2;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.utility.AppConstants;
import android.text.TextUtils;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.dialog.RemoveProduct;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.ui.InspirationProductUI;
import com.flatlay.ui.ProgressBarDialog;
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
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.makeramen.roundedimageview.RoundedImageView;

import com.pinterest.android.pdk.Utils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;


import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class InspirationAdapter extends BaseAdapter {

    static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/flatlay/";
    static String SHARE_POST_LINK2 = "http://flat-lay.com/flatlay/";
    String postlink;
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<Inspiration> inspirations;
    private boolean isViewAll;
    String likeId = "";
    private ProgressBarDialog mProgressBarDialog;
    private ArrayList<Integer> networkidarray = new ArrayList<Integer>();
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private PDKClient pdkClient;
    private static final String TWITTER_KEY = "5tQwAp0Z802BLmdF709OEsnLD";
    private static final String TWITTER_SECRET = "1RLaG0VSYdTXHfSRppPvVp3K7cE1T5q4QiUGLxrtbiFgv3t0W8";


    private Animation a, b;
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    private static final String NETWORK_ID = "NETWORK_ID";
    private ImageView facebook, twitter, instagram, pintrest;
    private ImageView instagramselected, twittergreenimage, fbselected, pintrestselected;
    private static final String pinappID = "4839869969114082403";
    int networkId = 0;

    FragmentInspirationSection fragmentInspirationSection;
    HashMap<String, String> follow_map = new HashMap<String, String>();

    public InspirationAdapter(FragmentActivity context, List<Inspiration> inspirations, boolean isViewAll, FragmentInspirationSection fragmentInspirationSection) {
        super();
        this.fragmentInspirationSection = fragmentInspirationSection;
        this.mContext = context;
        this.inspirations = inspirations;
        this.isViewAll = isViewAll;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        follow_map.clear();

        FacebookSdk.sdkInitialize(mContext);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(mContext);

        Twitter.initialize(mContext);

        pdkClient = PDKClient.configureInstance(mContext, AppConstants.PINTEREST_APP_ID);
        pdkClient.onConnect(mContext);
        pdkClient.setDebugMode(true);
        PDKClient.getInstance().onConnect(mContext);

        a = new RotateAnimation(0.0f, 180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        a.setRepeatCount(0);
        a.setFillAfter(true);
        a.setDuration(400);

        b = new RotateAnimation(180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        b.setRepeatCount(0);
        b.setFillAfter(true);
        b.setDuration(400);

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
        Log.w("InspirationAdapter","getView()");
        final ViewHolder viewholder;
        if(!follow_map.containsKey(getItem(position).getUsername()))
        {
            follow_map.put(getItem(position).getUsername(), getItem(position).getIs_followed());
        }

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "Montserrat-Regular.ttf");

        if (convertView == null) {
            Log.w("InspirationAdapter","PART 1");
            convertView = mInflater.inflate(R.layout.adapter_inspiration_large, null);
            viewholder = new ViewHolder();
            viewholder.fb_share = (ImageView)  convertView.findViewById(R.id.fb_share);
            viewholder.tw_share = (ImageView)  convertView.findViewById(R.id.tw_share);
            viewholder.tw_real_share = (TwitterLoginButton) convertView.findViewById(R.id.tw_real_share);
            viewholder.pin_share = (ImageView)  convertView.findViewById(R.id.pin_share);
            viewholder.gen_share = (ImageView)  convertView.findViewById(R.id.gen_share);
            viewholder.inspirationImage = (RoundedImageView) convertView.findViewById(R.id.inspirationImage);
            viewholder.deletePost = (CircleImageView) convertView.findViewById(R.id.deletePost);
            viewholder.userImage = (CircleImageView) convertView.findViewById(R.id.userImage);
            viewholder.userName = (AutoTextSize) convertView.findViewById(R.id.userName);
            viewholder.productLayout = (HorizontalScrollView) convertView.findViewById(R.id.productLayout);
            viewholder.productInflaterLayout = (LinearLayout) convertView.findViewById(R.id.productInflaterLayout);
            viewholder.user_profile_layout = (FrameLayout) convertView.findViewById(R.id.user_profile_layout);
            viewholder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            viewholder.noProductTextView = (TextView) convertView.findViewById(R.id.noProductTextView);
            viewholder.follow_btn_layout = (LinearLayout) convertView.findViewById(R.id.follow_btn_layout);
            viewholder.likeCount = (TextView) convertView.findViewById(R.id.likeCount);
            viewholder.staticLikes = (TextView) convertView.findViewById(R.id.staticLikes);
            viewholder.descriptionArrow2 = (ImageView) convertView.findViewById(R.id.descriptionArrow2);
           // viewholder.descriptionArrow2.setVisibility(View.INVISIBLE);
            viewholder.follow_btn = (Button) convertView.findViewById(R.id.follow_btn);
            viewholder.contentContainer = (RelativeLayout) convertView.findViewById(R.id.content_contain);
            viewholder.overlay = (LinearLayout) convertView.findViewById(R.id.overlay);
            viewholder.likeCountImage = (ImageView) convertView.findViewById(R.id.likeCountTextImage);
            viewholder.productTitleTextView = (TextView) convertView.findViewById(R.id.productTitleTextView);
            viewholder.CommentCountTextImage = (ImageView) convertView.findViewById(R.id.CommentCountTextImage);
            viewholder.linearLayout2 = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
            convertView.setTag(viewholder);
        } else {
            Log.w("InspirationAdapter","PART 2");
            viewholder = (ViewHolder) convertView.getTag();
        }

        viewholder.inspirationImage.setClickable(true);
        viewholder.contentContainer.setVisibility(View.INVISIBLE);
        viewholder.overlay.animate().translationY(0).setDuration(10).setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewholder.contentContainer.setVisibility(View.INVISIBLE);

            }
        });


        viewholder.descriptionArrow2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ViewHolder holder = viewholder;

                    Log.w("InspirationAdapter","Clicked the arrow 1!"+getItem(position).getUsername()+"*****"+getItem(position).toString());


                    if(holder.contentContainer.getVisibility() == View.INVISIBLE) {

                    holder.overlay.animate().translationY(-380).setDuration(400).setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            holder.contentContainer.setVisibility(View.VISIBLE);
                            holder.descriptionArrow2.startAnimation(a);
                            Log.w("InspirationAdapter","Clicked the arrow 2!"+getItem(position).getUsername()+"*****"+getItem(position).toString());

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                             holder.inspirationImage.setClickable(false);
                            Log.w("InspirationAdapter","Clicked the arrow 3!"+getItem(position).getUsername()+"*****"+getItem(position).toString());

                        }
                    });
                }
                else
                {
                    Log.w("InspirationAdapter","Clicked the arrow 4!"+getItem(position).getUsername()+"*****"+getItem(position).toString());
                    holder.overlay.animate().translationY(0).setDuration(400).setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            holder.descriptionArrow2.startAnimation(b);
                            Log.w("InspirationAdapter","Clicked the arrow 5!"+getItem(position).getUsername()+"*****"+getItem(position).toString());
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                             holder.inspirationImage.setClickable(true);
                            holder.contentContainer.setVisibility(View.INVISIBLE);
                            Log.w("InspirationAdapter","Clicked the arrow 6!"+getItem(position).getUsername()+"*****"+getItem(position).toString());
                        }
                    });
                }
            }
        });



        viewholder.fb_share.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.w("InspirationAdapter","Facebook button clicked!");
                postlink = SHARE_POST_LINK2 + getItem(position).getInspiration_id();
                Log.w("InspirationAdapter","Facebook button clicked!"+postlink);
                if (com.facebook.share.widget.ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(postlink))
                            //.setContentTitle("Hi")
                            //.setContentDescription("Hello")
                            //.setImageUrl(Uri.parse(getItem(position).getInspiration_image()))
                            .build();
                    shareDialog.show(content);
                }

            }
        });


        viewholder.tw_real_share.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {
                Log.w("InspirationAdapter","Twitter Login Sucess");
                Toast.makeText(mContext, "Twitter Login Success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(TwitterException exception) {
                Log.w("InspirationAdapter","Twitter Login Failure");
                Toast.makeText(mContext, "Twitter Login Failed", Toast.LENGTH_SHORT).show();
            }
        });


        viewholder.tw_share.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.w("InspirationAdapter","Twitter button clicked!");
                if(TwitterCore.getInstance().getSessionManager().getActiveSession() == null)
                {
                    Log.w("InspirationAdapter","Twitter button clicked 1");
                    viewholder.tw_real_share.performClick();
                }
                else
                {

                    postlink = SHARE_POST_LINK2 + getItem(position).getInspiration_id();
                    Log.w("InspirationAdapter","Twitter button clicked 2"+postlink);

                    final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                            .getActiveSession();


                    try {
                        URL postlink_to_url = new URL(postlink);

                        /*
                        final Intent intent = new ComposerActivity.Builder(mContext)
                                .session(session)
                                .text(postlink)
                                .createIntent();
                        mContext.startActivity(intent);
                        */

                        TweetComposer.Builder builder = new TweetComposer.Builder(mContext)
                                .url(postlink_to_url)
                                .text(getItem(position).getDescription());
                        builder.show();


                    }
                    catch(Exception e)
                    {
                        Log.w("Exception:",""+e);
                    }
                }
            }
        });


        viewholder.pin_share.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.w("InspirationAdapter","Pinterest button clicked!");

                postlink = SHARE_POST_LINK2 + getItem(position).getInspiration_id();
                List scopes = new ArrayList<String>();
                scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
                scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);

                pdkClient.login(mContext, scopes, new PDKCallback() {
                    @Override
                    public void onSuccess(PDKResponse response) {
                        Log.d(getClass().getName(), response.getData().toString());
                        Log.w("InspirationAdapter","Pinterest User Logged In");
                        getBoardList(null, null, null);
                        onSavePin(mContext, getItem(position).getInspiration_image(), UserPreference.getInstance().getmPinterestBoardId(), getItem(position).getDescription()+". Find it at "+postlink, postlink);
                    }

                    @Override
                    public void onFailure(PDKException exception) {
                        Log.e(getClass().getName(), exception.getDetailMessage());
                        Log.w("InspirationAdapter","Pinterest User Logged In Failed");
                    }
                });

            }
        });

        viewholder.gen_share.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.w("InspirationAdapter","General button clicked!");
                postlink = SHARE_POST_LINK + getItem(position).getInspiration_id();
                com.flatlay.dialog.ShareDialog dialog = new com.flatlay.dialog.ShareDialog(mContext, (HomeActivity) mContext, getItem(position).getInspiration_image(), postlink);
                dialog.show();

            }
        });


        Syso.info(">>>>>>>>" + getItem(position));
        if (!isViewAll && position > 0) {
            viewholder.user_profile_layout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(getItem(position).getUsername())) {
            viewholder.userName.setText(getItem(position).getUsername());
        } else {
            viewholder.userName.setText("Unknown");
        }


        viewholder.follow_btn.setTypeface(font);
        if (getItem(position).getIs_followed() != null && follow_map.get(getItem(position).getUsername()).equals("yes")) {
            viewholder.follow_btn.setText("Following");
            viewholder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.btn_borderbg_gray));
            viewholder.follow_btn.setTextColor(Color.parseColor("#000000"));
            int paddingPixel = 5;
            float density = mContext.getResources().getDisplayMetrics().density;
            int paddingDp = (int)(paddingPixel * density);
            viewholder.follow_btn.setPadding(paddingDp,0,paddingDp,0);

        } else {
            viewholder.follow_btn.setText(" Follow + ");
            viewholder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.followgreen));
            viewholder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
            viewholder.follow_btn.setPadding(17,0,17,0);
        }



        // Likes and Comments

        viewholder.staticLikes.setTypeface(font);
        viewholder.likeCount.setTypeface(font);
        if(Integer.parseInt(getItem(position).getLike_count()) > 0) {
            if(Integer.parseInt(getItem(position).getLike_count()) == 1) {
                viewholder.staticLikes.setText("like");
            }
            else {
                viewholder.staticLikes.setText("likes");
            }
            viewholder.likeCount.setText(getItem(position).getLike_count());
            viewholder.likeCount.setVisibility(View.VISIBLE);
            viewholder.staticLikes.setVisibility(View.VISIBLE);
        }
        else {
            viewholder.likeCount.setVisibility(View.GONE);
            viewholder.staticLikes.setVisibility(View.GONE);
        }

        if (!liked(position) || getItem(position).getLike_id() == null ) {
            viewholder.likeCountImage.setImageResource(R.drawable.heartoutline);
        } else {
            viewholder.likeCountImage.setImageResource(R.drawable.heartoutlineteal);
        }

        if (Integer.parseInt(getItem(position).getComment_count()) == 0) {
            viewholder.CommentCountTextImage.setImageResource(R.drawable.speechbubblee);
        } else {
            viewholder.CommentCountTextImage.setImageResource(R.drawable.speechbubbleteal);
        }

        viewholder.likeCountImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("InspirationAdapter","setOnClickListener()");
                if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                    CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                    createAccountDialog.show();
                } else {
                    if (((HomeActivity) mContext).checkInternet()) {
                        likeId = getItem(position).getLike_id();
                        if(likeId == null)
                        {
                            Log.w("InspirationAdapter","11");
                            viewholder.likeCountImage.setImageResource(R.drawable.heartoutlineteal);
                            viewholder.likeCount.setVisibility(View.VISIBLE);
                            viewholder.staticLikes.setText("like");
                            viewholder.staticLikes.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            Log.w("InspirationAdapter","22");
                            viewholder.likeCountImage.setImageResource(R.drawable.heartoutline);
                        }
                        likeInspiration(position);
                    }
                }
            }
        });


        viewholder.follow_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Inspiration Adapter","CLICKED BUTTON:"+getItem(position).getUsername()+"-->"+getItem(position).getIs_followed());
                if (getItem(position).getIs_followed() != null) {
                    if (follow_map.get(getItem(position).getUsername()).equals("no")) {
                        // getItem(position).getIs_followed().equals("no")
                        follow_map.put(getItem(position).getUsername(),"yes");
                        getItem(position).setIs_followed("yes");
                        Log.w("InspirationAdapter","notifyDataSetChanged() 1");
                        notifyDataSetChanged();
                        ((HomeActivity) mContext).followUser(getItem(position).getUser_id());
                    } else {
                        follow_map.put(getItem(position).getUsername(),"no");
                        getItem(position).setIs_followed("no");
                        Log.w("InspirationAdapter","notifyDataSetChanged() 2");
                        notifyDataSetChanged();
                        ((HomeActivity) mContext).unFollowUser(getItem(position).getUser_id());
                    }
                }
            }
        });

        // TO BE CHANGED
        /*
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
        */


        CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewholder.userImage, R.drawable.profile_icon);
        CommonUtility.setImage(mContext, getItem(position).getInspiration_image(), viewholder.inspirationImage);
        List<Product> data = getItem(position).getProducts();
        viewholder.productInflaterLayout.removeAllViews();

        if (data == null || data.size() < 1) {
            viewholder.descriptionArrow2.setVisibility(View.INVISIBLE);
           // viewholder.noProductTextView.setVisibility(View.GONE);
            viewholder.productInflaterLayout.addView(new InspirationProductUI(mContext, getItem(position), convertView).getView());
        }else{
            viewholder.descriptionArrow2.setVisibility(View.VISIBLE);
            viewholder.noProductTextView.setVisibility(View.GONE);
            viewholder.productInflaterLayout.addView(new InspirationProductUI(mContext, getItem(position), convertView).getView());

        }

        viewholder.productLayout.scrollTo(0, 0);

        viewholder.inspirationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((HomeActivity) mContext).checkInternet()) {
                    addFragment(new FragmentInspirationDetail(getItem(position), true,true,false));
                }
            }
        });

        viewholder.userImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (((HomeActivity) mContext).checkInternet()) {
                    addFragment(new FragmentProfileView(getItem(position).getUser_id(), "no"));
                }
            }
        });

        viewholder.userName.setOnClickListener(new OnClickListener() {

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
            viewholder.userImage.setVisibility(View.GONE);
            viewholder.linearLayout2.setVisibility(View.GONE);
        } else {
            viewholder.deletePost.setVisibility(View.GONE);
            viewholder.follow_btn.setVisibility(View.VISIBLE);
            viewholder.userImage.setVisibility(View.VISIBLE);
            viewholder.linearLayout2.setVisibility(View.VISIBLE);
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
        Log.w("InspirationAdapter","notifyDataSetChanged() 3");
        this.notifyDataSetChanged();
        return convertView;
    }


    public class ViewHolder {
        private ImageView tw_share, fb_share, pin_share, gen_share, descriptionArrow2;
        private CircleImageView deletePost;
        public TwitterLoginButton tw_real_share;
        private RoundedImageView inspirationImage;
        private CircleImageView userImage;
        private HorizontalScrollView productLayout;
        private LinearLayout productInflaterLayout;
        private TextView noProductTextView, inspirationTime;
        private TextView staticLikes;
        private AutoTextSize userName;
        private ProgressBar progressBar;
        private FrameLayout user_profile_layout;
        private TextView follower_count, collection_count, totalItemsText;
        private LinearLayout follow_btn_layout, imgLayout;
        private Button follow_btn;
        private TextView likeCount;
        RelativeLayout contentContainer;
        LinearLayout overlay, linearLayout2;
        ImageView likeCountImage, CommentCountTextImage;
        TextView productTitleTextView;
        RelativeLayout expand;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }


    private void likeInspiration(final int position) {
        Log.w("InspirationAdapter","likeInspiration()");
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                InspirationRes inspirationRes = (InspirationRes) object;
                likeId = inspirationRes.getLike_id();
                Log.w("InspirationAdapater","In handle onSuccess"+likeId);
                if (TextUtils.isEmpty(likeId)) {
                    getItem(position).setLike_id(likeId);
                    getItem(position).setLike_count(((getInt(getItem(position).getLike_count()) - 1) < 0 ? 0 : (getInt(getItem(position).getLike_count()) - 1)) + "");

                }
                else {
                    if (getItem(position).getLike_count() != null) {
                        getItem(position).setLike_count((getInt(getItem(position).getLike_count()) + 1) + "");
                        getItem(position).setLike_id(likeId);
                    }
                }
                Log.w("InspirationAdapter","notifyDataSetChanged() 4");
                notifyDataSetChanged();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
            }
        });
        if (TextUtils.isEmpty(likeId)) {
            Log.w("InspirationAdapter","postLike()");
            inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), getItem(position).getInspiration_id(), "inspiration");
        }
        else {
            Log.w("InspirationAdapter","removeLike()");
            inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), likeId);
        }
        inspirationSectionApi.execute();
    }



    public boolean liked(int position)
    {
        return getItem(position).getLike_id() != "";
    }

    private int getInt(String image_width) {
        try {
            return Integer.parseInt(image_width);
        } catch (Exception e) {
            return 0;
        }
    }

    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";
    private void getBoardList(final String text, final String link, final String imageUrl) {
        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getName());
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getUid());
//               	onSavePin(imageUrl, response.getBoardList().get(0).getUid(), text, link);
                if (response.getBoardList() != null && response.getBoardList().size() > 0) {
                    Syso.info("1234567890  2>>>>>> inside condition");
                    PinterestBoardDialog2 boardDialog = new PinterestBoardDialog2(mContext, response.getBoardList(), imageUrl, text, link);
                    boardDialog.show();
                } else
                    AlertUtils.showToast(mContext, "No board found, please create board first");
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
                Syso.info("12345678 >>> output" + exception.getDetailMessage());
                try {
                    JSONObject jsonObject = new JSONObject(exception.getDetailMessage());
                    AlertUtils.showToast(mContext, jsonObject.getString("message"));
                } catch (Exception e) {
                    e.printStackTrace();
                    AlertUtils.showToast(mContext, exception.getDetailMessage());
                }
            }
        });
    }

    public void onSavePin(Context c, String imageUrl, String boardId, String text, String linkUrl) {

        if (!Utils.isEmpty(text) && !Utils.isEmpty(boardId) && !Utils.isEmpty(imageUrl)) {
            PDKClient.getInstance().createPin(text, boardId, imageUrl, linkUrl, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    Log.d(getClass().getName(), response.getData().toString());
                }

                @Override
                public void onFailure(PDKException exception) {
                    //Log.e(getClass().getName(), exception.getDetailMessage());
                    Syso.info("12345678 >>> output" + exception.getDetailMessage());
                    try {
                        JSONObject jsonObject = new JSONObject(exception.getDetailMessage());
                        AlertUtils.showToast(mContext, jsonObject.getString("message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertUtils.showToast(mContext, exception.getDetailMessage());
                    }
                }
            });
        } else {
            Toast.makeText(mContext, "Required fields cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
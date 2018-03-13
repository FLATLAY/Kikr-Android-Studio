package com.flatlay.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.ui.InspirationProductUI;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.AutoTextSize;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyBubbleActions2;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.Utils;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Handler;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;

/**
 * Created by RachelDi on 1/22/18.
 */

public class InspirationAdapter extends BaseAdapter {

    private static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/flatlay/",
            SHARE_POST_LINK2 = "http://flat-lay.com/flatlay/";
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<Inspiration> inspirations;
    private HashMap<String, Boolean> islikes = new HashMap<>();
    private boolean isViewAll;
    private String likeId = "", postlink;
    // private ProgressBarDialog mProgressBarDialog;
    private ArrayList<Integer> networkidarray = new ArrayList<Integer>();
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private PDKClient pdkClient;
    private static final String TWITTER_KEY = "5tQwAp0Z802BLmdF709OEsnLD",
            TWITTER_SECRET = "1RLaG0VSYdTXHfSRppPvVp3K7cE1T5q4QiUGLxrtbiFgv3t0W8",
            NETWORK_ID = "NETWORK_ID", pinappID = "4839869969114082403";
    private Animation a, b;
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    private ImageView facebook, twitter, instagram, pintrest, largeheart, instagramselected,
            twittergreenimage, fbselected, pintrestselected;
    private int networkId = 0, longClickDuration = 1000;
    private static final int ANIMATION_DURATION = 450;
    private Animation zoominout;
    private FragmentInspirationSection allInspirationSectionFragment;
    private HashMap<String, String> follow_map = new HashMap<String, String>();
    private long then, lastDownUp = -1, currentUp = -1;

    public InspirationAdapter(FragmentActivity context, List<Inspiration> inspirations, boolean isViewAll, FragmentInspirationSection allInspirationSectionFragment) {
        super();
        this.allInspirationSectionFragment = allInspirationSectionFragment;
        this.mContext = context;
        this.inspirations = inspirations;
        this.isViewAll = isViewAll;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
    }

    public void init() {
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
        if (!follow_map.containsKey(getItem(position).getUsername())) {
            follow_map.put(getItem(position).getUsername(), getItem(position).getIs_followed());
        }

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "Montserrat-Regular.ttf");
        zoominout = AnimationUtils.loadAnimation(mContext, R.anim.zoominout);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_inspiration_large, null);
            viewholder = new ViewHolder();
            viewholder.fb_share = (ImageView) convertView.findViewById(R.id.fb_share);
            viewholder.tw_share = (ImageView) convertView.findViewById(R.id.tw_share);
            viewholder.like_heart = (SmallBangView) convertView.findViewById(R.id.like_heart);
//            likeId = getItem(position).getLike_id();
//            inspirationId=getItem(position).getInspiration_id();

//            if (getItem(position).getLike_id() == null || getItem(position).getLike_id().length() == 0) {
//                viewholder.like_heart.setSelected(true);
//                islikes.put(getItem(position).getInspiration_id(), false);
//                Log.e("likeid########", String.valueOf(islikes.get(getItem(position).getInspiration_id()))+"----"+getItem(position).getInspiration_id());
//            } else {
//                Log.e("likeidddddddddd", getItem(position).getLike_id()+"----"+getItem(position).getInspiration_id()+getItem(position).getUsername());
//                viewholder.like_heart.setSelected(false);
//                islikes.put(getItem(position).getInspiration_id(), true);
//            }
            //  viewholder.tw_real_share = (TwitterLoginButton) convertView.findViewById(R.id.tw_real_share);
            viewholder.pin_share = (ImageView) convertView.findViewById(R.id.pin_share);
            viewholder.commentCount = (TextView) convertView.findViewById(R.id.commentcount);
            viewholder.gen_share = (ImageView) convertView.findViewById(R.id.gen_share);
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
            viewholder.follow_btn = (Button) convertView.findViewById(R.id.follow_btn);
            viewholder.contentContainer = (RelativeLayout) convertView.findViewById(R.id.content_contain);
            viewholder.descriptionArrow2_layout = (RelativeLayout) convertView.findViewById(R.id.descriptionArrow2_layout);
            viewholder.overlay = (LinearLayout) convertView.findViewById(R.id.overlay);
            viewholder.likeCountImage = (ImageView) convertView.findViewById(R.id.likeCountTextImage);
            //   viewholder.productTitleTextView = (TextView) convertView.findViewById(R.id.productTitleTextView);
            //   viewholder.productTitleTextView.setTypeface(FontUtility.setMontserratLight(mContext));
            viewholder.CommentCountTextImage = (ImageView) convertView.findViewById(R.id.CommentCountTextImage);
            viewholder.linearLayout2 = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
            viewholder.largeheart = (ImageView) convertView.findViewById(R.id.largeheart);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.commentCount.setTypeface(FontUtility.setMontserratLight(mContext));
        viewholder.userName.setTypeface(FontUtility.setMontserratLight(mContext));
        viewholder.noProductTextView.setTypeface(FontUtility.setMontserratLight(mContext));
        viewholder.likeCount.setTypeface(FontUtility.setMontserratLight(mContext));
        viewholder.staticLikes.setTypeface(FontUtility.setMontserratLight(mContext));
        viewholder.largeheart.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                60, 60);
        viewholder.descriptionArrow2.setLayoutParams(params);
        if (getItem(position).getLike_id() == null || getItem(position).getLike_id().length() == 0) {
            viewholder.like_heart.setSelected(true);
            islikes.put(getItem(position).getInspiration_id(), false);
        } else {
            viewholder.like_heart.setSelected(false);
            islikes.put(getItem(position).getInspiration_id(), true);
        }
        viewholder.contentContainer.setVisibility(View.INVISIBLE);
        viewholder.overlay.animate().translationY(0).setDuration(10).setListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                viewholder.contentContainer.setVisibility(View.INVISIBLE);
            }
        });

        viewholder.descriptionArrow2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewHolder holder = viewholder;

                if (holder.contentContainer.getVisibility() == View.INVISIBLE) {
                    int height = holder.contentContainer.getHeight();

                    holder.overlay.animate().translationY(0 - height).setDuration(400).setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            holder.contentContainer.setVisibility(View.VISIBLE);
                            holder.descriptionArrow2.startAnimation(a);

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            // holder.inspirationImage.setClickable(false);

                        }
                    });
                } else {
                    holder.overlay.animate().translationY(0).setDuration(400).setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            holder.descriptionArrow2.startAnimation(b);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            holder.inspirationImage.setClickable(true);
                            holder.contentContainer.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

        viewholder.fb_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewholder.fb_share.setImageResource(R.drawable.tealfb);
                viewholder.pin_share.setImageResource(R.drawable.pinterestlogoo);
                viewholder.tw_share.setImageResource(R.drawable.twitterlogoo);
                viewholder.gen_share.setImageResource(R.drawable.shareicon);

                postlink = SHARE_POST_LINK2 + getItem(position).getInspiration_id();
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


//        viewholder.tw_real_share.setCallback(new Callback<TwitterSession>() {
//
//            @Override
//            public void success(Result<TwitterSession> result) {
//                Log.w("InspirationAdapter", "Twitter Login Sucess");
//                Toast.makeText(mContext, "Twitter Login Success", Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.w("InspirationAdapter", "Twitter Login Failure");
//                Toast.makeText(mContext, "Twitter Login Failed", Toast.LENGTH_SHORT).show();
//            }
//        });


        viewholder.tw_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                viewholder.tw_share.setImageResource(R.drawable.tealtwiter);
                viewholder.pin_share.setImageResource(R.drawable.pinterestlogoo);
                viewholder.gen_share.setImageResource(R.drawable.shareicon);
                viewholder.fb_share.setImageResource(R.drawable.facebooklogoo);

                if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
                    //   viewholder.tw_real_share.performClick();
                } else {
                    postlink = SHARE_POST_LINK2 + getItem(position).getInspiration_id();

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


                    } catch (Exception e) {
                    }
                }
            }
        });

        viewholder.pin_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewholder.pin_share.setImageResource(R.drawable.tealpinterest);
                viewholder.tw_share.setImageResource(R.drawable.twitterlogoo);
                viewholder.gen_share.setImageResource(R.drawable.shareicon);
                viewholder.fb_share.setImageResource(R.drawable.facebooklogoo);

                postlink = SHARE_POST_LINK2 + getItem(position).getInspiration_id();
                List scopes = new ArrayList<String>();
                scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
                scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);

                pdkClient.login(mContext, scopes, new PDKCallback() {
                    @Override
                    public void onSuccess(PDKResponse response) {
                        Log.d(getClass().getName(), response.getData().toString());
                        getBoardList(null, null, null);
                        onSavePin(mContext, getItem(position).getInspiration_image(), UserPreference.getInstance().getmPinterestBoardId(), getItem(position).getDescription() + ". Find it at " + postlink, postlink);
                    }

                    @Override
                    public void onFailure(PDKException exception) {
                        Log.e(getClass().getName(), exception.getDetailMessage());
                    }
                });

            }
        });

        viewholder.gen_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewholder.gen_share.setImageResource(R.drawable.teal_gen);
                viewholder.pin_share.setImageResource(R.drawable.pinterestlogoo);
                viewholder.tw_share.setImageResource(R.drawable.twitterlogoo);
                viewholder.fb_share.setImageResource(R.drawable.facebooklogoo);
                postlink = SHARE_POST_LINK + getItem(position).getInspiration_id();
                com.flatlay.dialog.ShareDialog dialog = new com.flatlay.dialog.ShareDialog(mContext, (HomeActivity) mContext, getItem(position).getInspiration_image(), postlink);
                dialog.show();

            }
        });

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
            int paddingDp = (int) (paddingPixel * density);
            viewholder.follow_btn.setPadding(paddingDp, 0, paddingDp, 0);

        } else {
            viewholder.follow_btn.setText(" Follow + ");
            viewholder.follow_btn.setBackground(mContext.getResources().getDrawable(R.drawable.followgreen));
            viewholder.follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
            viewholder.follow_btn.setPadding(17, 0, 17, 0);
        }


        if (Integer.parseInt(getItem(position).getLike_count()) > 0) {
            if (Integer.parseInt(getItem(position).getLike_count()) == 1) {
                viewholder.staticLikes.setText("like");
            } else {
                viewholder.staticLikes.setText("likes");
            }
            viewholder.likeCount.setText(getItem(position).getLike_count());
            viewholder.likeCount.setVisibility(View.VISIBLE);
        } else {
            viewholder.likeCount.setVisibility(View.GONE);
            viewholder.staticLikes.setVisibility(View.GONE);
        }
        currentUp = System.currentTimeMillis();
        lastDownUp = System.currentTimeMillis();
        viewholder.like_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("likeid--adapter--like 1", "" + islikes.get(getItem(position).getInspiration_id()));
                if (UserPreference.getInstance().getPassword() == "" ||
                        UserPreference.getInstance().getEmail() == "" ||
                        UserPreference.getInstance().getUserName() == "") {
                    CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                    createAccountDialog.show();
                } else {
                    if (((HomeActivity) mContext).checkInternet()) {
                        lastDownUp = System.currentTimeMillis();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                currentUp = System.currentTimeMillis();
                                Log.e("likeid--difference", "" + (currentUp - lastDownUp));
                                if (currentUp - lastDownUp > 900) {
                                    likeInspiration(position);
                                }
                            }
                        }, 1000);
                        if (!islikes.get(getItem(position).getInspiration_id())) {
                            viewholder.like_heart.setSelected(false);
                            viewholder.like_heart.likeAnimation();
                            islikes.put(getItem(position).getInspiration_id(), true);
                            getItem(position).setLike_count((getInt(getItem(position).getLike_count()) + 1) + "");
                            viewholder.likeCount.setText(getItem(position).getLike_count());
                        } else {
                            viewholder.like_heart.setSelected(true);
                            islikes.put(getItem(position).getInspiration_id(), false);
                            getItem(position).setLike_count(((getInt(getItem(position).getLike_count()) - 1) < 0 ?
                                    0 : (getInt(getItem(position).getLike_count()) - 1)) + "");
                            viewholder.likeCount.setText(getItem(position).getLike_count());
                        }
                    }
                }
            }
        });

        viewholder.commentCount.setText(getItem(position).getComment_count());

        viewholder.follow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(position).getIs_followed() != null) {
                    if (follow_map.get(getItem(position).getUsername()).equals("no")) {
                        // getItem(position).getIs_followed().equals("no")
                        follow_map.put(getItem(position).getUsername(), "yes");
                        getItem(position).setIs_followed("yes");
                        notifyDataSetChanged();
                        ((HomeActivity) mContext).followUser(getItem(position).getUser_id());
                    } else {
                        follow_map.put(getItem(position).getUsername(), "no");
                        getItem(position).setIs_followed("no");
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
        // CommonUtility.setImage(mContext, getItem(position).getInspiration_image(), viewholder.inspirationImage);
        CommonUtility.setImageHigh(mContext, viewholder.inspirationImage, getItem(position).getInspiration_image());


        List<Product> data = getItem(position).getProducts();
        viewholder.productInflaterLayout.removeAllViews();
        //  viewholder.inspirationImage.setClickable(true);

        viewholder.inspirationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((HomeActivity) mContext).checkInternet()) {

                    addFragment(new FragmentInspirationDetail(getItem(position),
                            islikes.get(getItem(position).getInspiration_id())));
//                    Inspiration inspiration = getItem(position);
//                    Gson gson = new Gson();
//                    String inspirationAsAString = gson.toJson(inspiration);
//                    Intent intent = new Intent(mContext, FragmentInspirationDetail.class);
//                    intent.putExtra("InspirationAsString", inspirationAsAString);
//                    intent.putExtra("isShowProducts", true);
//                    intent.putExtra("showhide", true);
//                    intent.putExtra("isFromNotification", false);
//                    intent.putExtra("profileimg", getItem(position).getProfile_pic());
//                    intent.putExtra("inspirationimg",getItem(position).getInspiration_image());
//                    intent.putExtra("username", getItem(position).getUsername());
//
//                    mContext.startActivity(intent);
//                    Log.e("HomeActivity111", String.valueOf(getItem(position).getInspiration_id()));
                }
            }
        });

        viewholder.inspirationImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                MyBubbleActions2.on(v)
                        .addAction("Like", R.drawable.small_gray_heart, new MyBubbleActions2.Callback() {
                            @Override
                            public void doAction() {
                                Log.e("likeid--long", "onclick");

                                //  Toast.makeText(v.getContext(), "Like", Toast.LENGTH_SHORT).show();
                                if (UserPreference.getInstance().getPassword() == ""
                                        || UserPreference.getInstance().getEmail() == ""
                                        || UserPreference.getInstance().getUserName() == "") {
                                    CreateAccountDialog createAccountDialog =
                                            new CreateAccountDialog(mContext);
                                    createAccountDialog.show();
                                } else {
                                    if (((HomeActivity) mContext).checkInternet()) {
                                        lastDownUp = System.currentTimeMillis();
                                        new Handler().postDelayed(new Runnable() {
                                            public void run() {
                                                currentUp = System.currentTimeMillis();
                                                Log.e("likeid--difference", "" + (currentUp - lastDownUp));
                                                if (currentUp - lastDownUp > 600) {
                                                    likeInspiration(position);
                                                }
                                            }
                                        }, 100);
                                        Log.e("likeid-in click", "" + islikes.get(getItem(position).getInspiration_id()));
                                        if (!islikes.get(getItem(position).getInspiration_id())) {
                                            viewholder.like_heart.setSelected(false);
                                            viewholder.like_heart.likeAnimation();
                                            islikes.put(getItem(position).getInspiration_id(), true);
                                            Log.e("likeid-in click-now", "" + islikes.get(getItem(position).getInspiration_id()));
                                            getItem(position).setLike_count((getInt(getItem(position).getLike_count()) + 1) + "");
                                            viewholder.likeCount.setText(getItem(position).getLike_count());
                                        } else {
                                            viewholder.like_heart.setSelected(true);
                                            islikes.put(getItem(position).getInspiration_id(), false);
                                            Log.e("likeid-in click-now", "" + islikes.get(getItem(position).getInspiration_id()));
                                            getItem(position).setLike_count(((getInt(getItem(position).getLike_count()) - 1) < 0 ?
                                                    0 : (getInt(getItem(position).getLike_count()) - 1)) + "");
                                            viewholder.likeCount.setText(getItem(position).getLike_count());
                                        }
                                    }
                                }
                            }
                        })
                        .addAction("Share", R.drawable.small_gray_kopie, new MyBubbleActions2.Callback() {
                            @Override
                            public void doAction() {

                                postlink = SHARE_POST_LINK + getItem(position).getInspiration_id();
                                com.flatlay.dialog.ShareDialog dialog =
                                        new com.flatlay.dialog.ShareDialog(mContext,
                                                (HomeActivity) mContext,
                                                getItem(position).getInspiration_image(), postlink);
                                dialog.show();
                            }
                        })

                        .show();
                return true;
            }
        });
        if (data == null || data.size() < 1) {
            viewholder.descriptionArrow2.setVisibility(View.INVISIBLE);
            // viewholder.noProductTextView.setVisibility(View.GONE);
            viewholder.productInflaterLayout.addView(new InspirationProductUI(mContext, getItem(position), convertView, 160, 160, 0).getView());
        } else {
            viewholder.descriptionArrow2.setVisibility(View.VISIBLE);
            viewholder.noProductTextView.setVisibility(View.GONE);
            viewholder.productInflaterLayout.addView(new InspirationProductUI(mContext, getItem(position), convertView, 160, 160, 0).getView());

        }

        viewholder.productLayout.scrollTo(0, 0);


        viewholder.userImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (((HomeActivity) mContext).checkInternet()) {
                    //   addFragment(new FragmentProfileView(getItem(position).getUser_id(), "no"));
                }
            }
        });

        viewholder.userName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (((HomeActivity) mContext).checkInternet()) {
                    //  addFragment(new FragmentProfileView(getItem(position).getUser_id(), "no"));
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
            //viewholder.follow_btn.setVisibility(View.VISIBLE);
            viewholder.userImage.setVisibility(View.VISIBLE);
            viewholder.linearLayout2.setVisibility(View.VISIBLE);
        }

        this.notifyDataSetChanged();
        return convertView;
    }


    public class ViewHolder {
        private ImageView tw_share, fb_share, pin_share, gen_share, descriptionArrow2, largeheart;
        SmallBangView like_heart;
        private CircleImageView deletePost;
        // public TwitterLoginButton tw_real_share;
        private RoundedImageView inspirationImage;
        private CircleImageView userImage;
        private HorizontalScrollView productLayout;
        private LinearLayout productInflaterLayout;
        private TextView noProductTextView, inspirationTime, commentCount;
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
        RelativeLayout expand, descriptionArrow2_layout;

        private void addFragment(Fragment fragment) {
            ((HomeActivity) mContext).addFragment(fragment);
        }
    }


    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

    private void updateInspiration() {

    }


    private void likeInspiration(final int position) {
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                InspirationRes inspirationRes = (InspirationRes) object;
                likeId = inspirationRes.getLike_id();
                if (likeId == null || likeId.length() == 0) {
                    getItem(position).setLike_id(likeId);
//                    getItem(position).setLike_count(((getInt(getItem(position).getLike_count()) - 1) < 0 ?
//                            0 : (getInt(getItem(position).getLike_count()) - 1)) + "");
                    Log.e("likeid--like count", "" + getItem(position).getLike_count());

                } else {
//                    getItem(position).setLike_count((getInt(getItem(position).getLike_count()) + 1) + "");
                    getItem(position).setLike_id(likeId);
                    Log.e("likeid++like count", "" + getItem(position).getLike_count());
                }
                notifyDataSetChanged();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
//        currentUp = System.currentTimeMillis();
//        Log.e("likeid--currentUp", "" + currentUp);
//        Log.e("likeid--lastDownUp", "" + lastDownUp);

//        if (currentUp - lastDownUp > 8000) {
        if (islikes.get(getItem(position).getInspiration_id())) {
            Log.e("likeid", "like");
            inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), getItem(position).getInspiration_id(), "inspiration");
        } else {
            Log.e("likeid", "don't like");
            inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), getItem(position).getLike_id());
        }
        inspirationSectionApi.execute();
        //    }
//        lastDownUp = currentUp;
    }

//    public boolean liked(int position) {
//        return getItem(position).getLike_id() != "";
//    }

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
//               	onSavePin(imageUrl, response.getBoardList().get(0).getUid(), text, link);
                if (response.getBoardList() != null && response.getBoardList().size() > 0) {
                    //PinterestBoardDialog2 boardDialog = new PinterestBoardDialog2(mContext, response.getBoardList(), imageUrl, text, link);
                    // boardDialog.show();
                } else
                    AlertUtils.showToast(mContext, "No board found, please create board first");
            }

            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
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

//    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            final MyViewHolder holder = MyViewHolder.newInstance(parent, viewType);
//            holder.smallBang.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = holder.getAdapterPosition();
//                    Item item = data.get(position);
//                    if (item.liked) {
//                        item.liked = false;
//                        holder.smallBang.setSelected(false);
//                    } else {
//                        item.liked = true;
//                        holder.smallBang.setSelected(true);
//                        holder.smallBang.likeAnimation();
//                    }
//                }
//            });
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
//            Item item = data.get(position);
//            holder.tv_content.setText(item.content);
//            holder.smallBang.setSelected(item.liked);
//        }
//
//        @Override
//        public int getItemCount() {
//            return data.size();
//        }
//    }

}

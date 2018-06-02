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
import com.google.gson.internal.LinkedTreeMap;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.Utils;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.ComposerActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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
    private static final int ANIMATION_DURATION = 450, MAX_ENTRIES = 5;
    private Animation zoominout;
    private FragmentInspirationSection allInspirationSectionFragment;
    private HashMap<String, String> follow_map = new HashMap<String, String>();
    private long then, lastDownUp = -1, currentUp = -1;
    final String TAG = "InspirationAdapter";


    public InspirationAdapter(FragmentActivity context, List<Inspiration> inspirations,
                              boolean isViewAll,
                              FragmentInspirationSection allInspirationSectionFragment,
                              ListAdapterListener mListener) {
        super();
        this.allInspirationSectionFragment = allInspirationSectionFragment;
        this.mContext = context;
        this.inspirations = inspirations;
        this.isViewAll = isViewAll;
        this.mListener = mListener;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FacebookSdk.sdkInitialize(mContext);

        init();
    }

    public void init() {
        follow_map.clear();
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(mContext);
        FacebookSdk.sdkInitialize(mContext);
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_inspiration_large, null);
            viewholder = new ViewHolder();
            viewholder.fb_share = (ImageView) convertView.findViewById(R.id.fb_share);
            viewholder.tw_share = (ImageView) convertView.findViewById(R.id.tw_share);
            viewholder.like_heart = (SmallBangView) convertView.findViewById(R.id.like_heart);
            viewholder.tw_real_share = (TwitterLoginButton) convertView.findViewById(R.id.tw_real_share);
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
            viewholder.CommentCountTextImage = (ImageView) convertView.findViewById(R.id.CommentCountTextImage);
            viewholder.linearLayout2 = (LinearLayout) convertView.findViewById(R.id.linearLayout2);
            viewholder.largeheart = (ImageView) convertView.findViewById(R.id.largeheart);
            viewholder.overlay.animate().translationY(0).setDuration(400);
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
                70, 70);
        viewholder.descriptionArrow2.setLayoutParams(params);
        viewholder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickAtOKButton(position);
            }
        });
        viewholder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClickAtOKButton(position);
            }
        });
        if (getItem(position).getLike_id() == null || getItem(position).getLike_id().length() == 0) {
            viewholder.like_heart.setSelected(true);
            islikes.put(getItem(position).getInspiration_id(), false);
        } else {
            viewholder.like_heart.setSelected(false);
            islikes.put(getItem(position).getInspiration_id(), true);
        }
        if (convertView.getY() < 0 || convertView.getY() > 1000) {
            viewholder.descriptionArrow2.clearAnimation();
            viewholder.overlay.animate().translationY(0).setDuration(400);
        }

        viewholder.descriptionArrow2_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewHolder holder = viewholder;

                if (holder.overlay.getTranslationY() == 0) {

                    int height = holder.contentContainer.getHeight();

                    holder.overlay.animate().translationY(0 - height).setDuration(400).setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);

                            holder.descriptionArrow2.startAnimation(a);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
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
                            .setContentDescription("Find it @FLATLAY " + postlink)
                            .build();
                    shareDialog.show(content);
                }

            }
        });


        viewholder.tw_real_share.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {
                Log.w(TAG, "Twitter Login Sucess");
                Toast.makeText(mContext, "Twitter Login Success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "Twitter Login Failure");
                Toast.makeText(mContext, "Twitter Login Failed", Toast.LENGTH_SHORT).show();
            }
        });


        viewholder.tw_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                viewholder.tw_share.setImageResource(R.drawable.tealtwiter);
                viewholder.pin_share.setImageResource(R.drawable.pinterestlogoo);
                viewholder.gen_share.setImageResource(R.drawable.shareicon);
                viewholder.fb_share.setImageResource(R.drawable.facebooklogoo);

                if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
                    viewholder.tw_real_share.performClick();
                } else {
                    postlink = SHARE_POST_LINK2 + getItem(position).getInspiration_id();

                    final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                            .getActiveSession();
                    try {

                        final Intent intent = new ComposerActivity.Builder(mContext)
                                .session(session)
                                .image(Uri.parse(getItem(position).getInspiration_image()))
                                .text("Find it @FLATLAY " + postlink)
                                .createIntent();
                        mContext.startActivity(intent);
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
                        onSavePin(mContext, getItem(position).getInspiration_image(),
                                UserPreference.getInstance().getmPinterestBoardId(),
                                getItem(position).getDescription() + ". Find it @FLATLAY " + postlink,
                                postlink);
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
                                Log.e(TAG, "" + (currentUp - lastDownUp));
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

        if (getItem(position).getProfile_pic() != null && getItem(position).getProfile_pic().length() > 0)
            CommonUtility.setImage(mContext, viewholder.userImage, getItem(position).getProfile_pic());
        else
            Picasso.with(mContext).load(R.drawable.profile_icon).into(viewholder.userImage);

        CommonUtility.setImageHigh(mContext, viewholder.inspirationImage, getItem(position).getInspiration_image());


        List<Product> data = getItem(position).getProducts();
        viewholder.productInflaterLayout.removeAllViews();

        viewholder.inspirationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((HomeActivity) mContext).checkInternet()) {

                    addFragment(new FragmentInspirationDetail(getItem(position),
                            islikes.get(getItem(position).getInspiration_id()), false));
                }
            }
        });

        viewholder.CommentCountTextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((HomeActivity) mContext).checkInternet()) {
                    addFragment(new FragmentInspirationDetail(getItem(position),
                            islikes.get(getItem(position).getInspiration_id()), true));

                }
            }
        });

        viewholder.commentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (((HomeActivity) mContext).checkInternet()) {
                    addFragment(new FragmentInspirationDetail(getItem(position),
                            islikes.get(getItem(position).getInspiration_id()), true));
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
                                                if (currentUp - lastDownUp > 600) {
                                                    likeInspiration(position);
                                                }
                                            }
                                        }, 100);
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
            viewholder.productInflaterLayout.addView(new InspirationProductUI(mContext, getItem(position), convertView, 160, 160, 0).getView());
        } else {
            viewholder.descriptionArrow2.setVisibility(View.VISIBLE);
            viewholder.noProductTextView.setVisibility(View.GONE);
            viewholder.productInflaterLayout.addView(new InspirationProductUI(mContext, getItem(position), convertView, 160, 160, 0).getView());

        }

        viewholder.productLayout.scrollTo(0, 0);
        viewholder.deletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((HomeActivity) mContext).checkInternet()) {
                    com.flatlay.dialog.ChoiceOnPostDialog choiceOnPostDialog =
                            new com.flatlay.dialog.ChoiceOnPostDialog(mContext, getItem(position));
                    choiceOnPostDialog.getWindow().setBackgroundDrawableResource(R.color.real_transparent);
                    choiceOnPostDialog.show();
                }
            }
        });

        if (UserPreference.getInstance().getUserID().equals(getItem(position).getUser_id())) {
            viewholder.deletePost.setVisibility(View.VISIBLE);
            viewholder.userImage.setVisibility(View.GONE);
            viewholder.linearLayout2.setVisibility(View.GONE);

        } else {
            viewholder.deletePost.setVisibility(View.GONE);
            viewholder.userImage.setVisibility(View.VISIBLE);
            viewholder.linearLayout2.setVisibility(View.VISIBLE);
        }

        this.notifyDataSetChanged();
        return convertView;
    }


    public class ViewHolder {
        private ImageView tw_share, fb_share, pin_share, gen_share, descriptionArrow2, largeheart,
                likeCountImage, CommentCountTextImage;
        private SmallBangView like_heart;
        public TwitterLoginButton tw_real_share;
        private RoundedImageView inspirationImage;
        private CircleImageView userImage, deletePost;
        private HorizontalScrollView productLayout;
        private TextView noProductTextView, inspirationTime, commentCount, staticLikes, likeCount,
                productTitleTextView, follower_count, collection_count, totalItemsText;
        private AutoTextSize userName;
        private ProgressBar progressBar;
        private FrameLayout user_profile_layout;
        private LinearLayout follow_btn_layout, imgLayout, productInflaterLayout, overlay, linearLayout2;
        private Button follow_btn;
        private RelativeLayout expand, descriptionArrow2_layout, contentContainer;

        private void addFragment(Fragment fragment) {
            ((HomeActivity) mContext).addFragment(fragment);
        }

    }

    private ListAdapterListener mListener;

    public interface ListAdapterListener { // create an interface
        void onClickAtOKButton(int position);
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

                } else {
                    getItem(position).setLike_id(likeId);
                }
                notifyDataSetChanged();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        if (islikes.get(getItem(position).getInspiration_id())) {
            inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), getItem(position).getInspiration_id(), "inspiration");
        } else {
            inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), getItem(position).getLike_id());
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

    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";

    private void getBoardList(final String text, final String link, final String imageUrl) {
        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                if (response.getBoardList() != null && response.getBoardList().size() > 0) {
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
        Log.e(TAG, imageUrl);
        Log.e(TAG, boardId);
        Log.e(TAG, text);
        Log.e(TAG, linkUrl);

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

}

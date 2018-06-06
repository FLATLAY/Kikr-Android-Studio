package com.flatlay.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.flatlay.adapter.FragmentProfileCollectionAdapter;
import com.flatlay.adapter.InspirationGridAdapter;
import com.flatlay.post_upload.FragmentPostUploadTag;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyBubbleActions2;
import com.flatlay.utility.MyMaterialContentOverflow;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.FollowerList;
import com.flatlaylib.bean.ProfileCollectionList;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.flatlay.BaseFragment;
import com.flatlay.KikrApp;
import com.flatlay.KikrApp.TrackerName;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.AutocompleteCustomArrayAdapter;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.dialog.DeleteCommentDialog;
import com.flatlay.ui.CustomAutoCompleteView;
import com.flatlay.ui.InspirationCommentsUI;
import com.flatlay.ui.InspirationProductUI;
import com.flatlay.ui.TagView;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.DeletePostApi;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.bean.Comment;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.SearchUser;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.Syso;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.Utils;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.hanks.library.bang.SmallBangView;


public class FragmentInspirationDetail extends BaseFragment implements OnClickListener, TextView.OnEditorActionListener {
    private AutocompleteCustomArrayAdapter adapter;
    private ArrayList<SearchUser> usersList = new ArrayList<SearchUser>();
    private ArrayList<String> temp_usersList = new ArrayList<String>();
    private Animation a, b;
    public static final String TAG = "FragmentInspirationD";
    private Button other_button22, other_button11, commentBtn, closeButton, button1, button2;
    private Bitmap bitmap;
    private boolean is_other_FirstTime_coll = true, is_other_FirstTime_feed = true, isother_Loading_feed = false, isFirstTime_coll = true, isFirstTime_feed = true, isFirstTime = true, mHasDoubleClicked = false, isViewAllComments = false,
            isPostComment = false, isFromNotification, isMyPost = false, commentOpen = false,
            isChoosenTeal = false, isLoading_feed = false, isLoading_coll = false, isLike,
            triggerOpenComment;
    private CustomAutoCompleteView commentEditText;
    private CircleImageView other_profile_pic, prof, prof2;
    private DeletePostApi deletePostApi;
    private ImageView commentCountTextImage, descriptionArrow, inspirationImage,
            deletePost, deletePost2, tw_share, fb_share, pin_share, gen_share;
    private Inspiration inspiration;
    private int page7 = 0, other_visibleItemCount = 0, other_totalItemCount = 0, other_firstVisibleItem = 0, firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0, page = 0,
            firstVisibleItem1 = 0, visibleItemCount1 = 0, totalItemCount1 = 0, page1 = 0,
            collAdapterIndex = -1;
    private List<FollowerList> other_followersLists = new ArrayList<FollowerList>(), other_followingLists = new ArrayList<FollowerList>();
    private List<CollectionList> collectionLists2;
    private ListView other_collectionList, collectionList;
    private List<Inspiration> product_list = new ArrayList<Inspiration>(), other_product_list = new ArrayList<Inspiration>();
    private LinearLayout commentList, bottomLayout, productInflaterLayout, backIconLayout, layout2;
    private long lastDownUp = -1, currentUp = -1;
    private TextView other_nameText, other_followingtext1, other_followertext2, other_followertext1, other_followingtext2, tvViewAllComments, inspirationTime, likeCount, descriptionText, text1,
            text2, text3, text4, followertext1, followingtext1, followertext2, followingtext2,
            nameText, userText;
    private String otherUserId = "", lastUserInput2 = "", lastUserInput = "", lastSelected2 = "", lastSelected = "",
            user_id = UserPreference.getInstance().getUserID(), profileimg, username, inspiration_id,
            inspirationUserId, inspirationImageString, inspirationProductName,
            inspirationDes;
    private List<Comment> list = new ArrayList<Comment>(), list1 = new ArrayList<Comment>();
    private FragmentInspirationDetail fragmentInspirationDetail;
    private ScrollView scrollView;
    private ShareDialog shareDialog;
    private FrameLayout frameLayout, contentContainer;
    private float scaleFactor = 1;
    private HorizontalScrollView productLayout;
    private SmallBangView likeCountTextImage;
    private RelativeLayout overflow_layout5, expand_collection, layout_collection, imageLayout;
    private MyMaterialContentOverflow overflow1, overflow2, overflow3;
    private GridView other_imagesList, imagesList;
    private InspirationGridAdapter inspirationAdapter, other_inspirationGridAdapter;
    private FragmentProfileCollectionAdapter collectionAdapter, other_collectionAdapter;
    private FragmentProfileView fragmentProfileView;
    private View tvDescriptionline, mainView;
    private List<CollectionList> other_collectionLists2;


    public FragmentInspirationDetail(Inspiration inspiration, boolean isLike, boolean triggerOpenComment) {
        this.inspiration = inspiration;
        this.isFromNotification = false;
        this.isLike = isLike;
        this.triggerOpenComment = triggerOpenComment;
    }

    public FragmentInspirationDetail(Inspiration inspiration, String likeid, boolean triggerOpenComment) {
        this.inspiration = inspiration;
        this.isFromNotification = false;
        this.triggerOpenComment = triggerOpenComment;
        if (likeid == null || likeid == "") {
            isLike = false;
        } else {
            isLike = true;
        }
    }

    public FragmentInspirationDetail(String id) {
        this.inspiration_id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_inspiration_detail, container,false);
        Log.w(TAG, "FragmentInspirationDetail");
        fragmentInspirationDetail = this;
        return mainView;
    }

    @Override
    public void setClickListener() {
        commentBtn.setOnClickListener(this);
        likeCountTextImage.setOnClickListener(this);
        backIconLayout.setOnClickListener(this);
        userText.setOnClickListener(this);
        deletePost.setOnClickListener(this);
        deletePost2.setOnClickListener(this);
        commentCountTextImage.setOnClickListener(this);
        fb_share.setOnClickListener(this);
        pin_share.setOnClickListener(this);
        tw_share.setOnClickListener(this);
        gen_share.setOnClickListener(this);
        commentEditText.setOnEditorActionListener(this);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                overflow2.setOpen();
                overflow1.setOpen();
                button2.setTextColor(Color.BLACK);
                button1.setTextColor(Color.WHITE);
                button2.setBackgroundResource(R.drawable.white_button_noborder);
                button1.setBackgroundResource(R.drawable.green_corner_button);
                imagesList.setVisibility(View.VISIBLE);
                collectionList.setVisibility(View.GONE);

            }
        });

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                overflow2.setOpen();
                overflow1.setOpen();
                button1.setTextColor(Color.BLACK);
                button2.setTextColor(Color.WHITE);
                button1.setBackgroundResource(R.drawable.white_button_noborder);
                button2.setBackgroundResource(R.drawable.green_corner_button);
                collectionList.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
            }
        });

        prof.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMyPost) {
                } else {
                    overflow2.setVisibility(View.VISIBLE);
                    if (overflow3.isOpen()) {
                        overflow3.triggerClose();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                overflow2.triggerSlide();
                            }
                        }, 800);

                    } else
                        overflow2.triggerSlide();
                }
            }
        });

        other_button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overflow2.setOpen();
                other_button22.setTextColor(Color.BLACK);
                other_button11.setTextColor(Color.WHITE);
                other_button22.setBackgroundResource(R.drawable.white_button_noborder);
                other_button11.setBackgroundResource(R.drawable.green_corner_button);
                other_imagesList.setVisibility(View.VISIBLE);
                other_collectionList.setVisibility(View.GONE);
            }
        });

        other_button22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overflow2.setOpen();
                other_button11.setTextColor(Color.BLACK);
                other_button22.setTextColor(Color.WHITE);
                other_button11.setBackgroundResource(R.drawable.white_button_noborder);
                other_button22.setBackgroundResource(R.drawable.green_corner_button);
                other_collectionList.setVisibility(View.VISIBLE);
                other_imagesList.setVisibility(View.GONE);
            }
        });

        userText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMyPost) {
                    overflow1.setVisibility(View.VISIBLE);
                    if (overflow3.isOpen()) {
                        overflow3.triggerClose();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                overflow1.triggerSlide();
                            }
                        }, 800);

                    } else
                        overflow1.triggerSlide();
                } else {
                    overflow2.setVisibility(View.VISIBLE);
                    if (overflow3.isOpen()) {
                        overflow3.triggerClose();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                overflow2.triggerSlide();
                            }
                        }, 800);

                    } else
                        overflow2.triggerSlide();
                }
            }
        });

        text3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (((HomeActivity) mContext).checkInternet()) {
                    com.flatlay.dialog.ConfirmDeleteDialog confirmDeleteDialog =
                            new com.flatlay.dialog.ConfirmDeleteDialog(mContext, inspiration_id, inspirationUserId);
                    confirmDeleteDialog.getWindow().setBackgroundDrawableResource(R.color.real_transparent);
                    confirmDeleteDialog.show();
                } else
                    System.out.print("error");
            }
        });

        text2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ((HomeActivity) mContext).addFragment(new FragmentPostUploadTag(inspiration, true));
            }
        });

        text4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                deletePost2.performClick();
            }
        });

        inspirationImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                MyBubbleActions2.on(v)
                        .addAction("Like", R.drawable.small_gray_heart, new MyBubbleActions2.Callback() {
                            @Override
                            public void doAction() {
                                lastDownUp = System.currentTimeMillis();
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        currentUp = System.currentTimeMillis();
                                        Log.e(TAG, "" + (currentUp - lastDownUp));
                                        if (currentUp - lastDownUp > 700) {
                                            likeInspiration();
                                        }
                                    }
                                }, 800);

                                if (!isLike) {
                                    likeCountTextImage.setSelected(false);
                                    likeCountTextImage.likeAnimation();
                                    likeInspiration();
                                    isLike = true;
                                    inspiration.setLike_count((getInt(inspiration.getLike_count()) + 1) + "");
                                    likeCount.setText(inspiration.getLike_count());
                                } else {
                                    likeCountTextImage.setSelected(true);
                                    likeInspiration();
                                    isLike = false;
                                    inspiration.setLike_count(((getInt(inspiration.getLike_count()) - 1) < 0 ? 0 : (getInt(inspiration.getLike_count()) - 1)) + "");
                                    likeCount.setText(inspiration.getLike_count());
                                }
                            }
                        })
                        .addAction("Share", R.drawable.small_gray_kopie, new MyBubbleActions2.Callback() {
                            @Override
                            public void doAction() {

                                postlink = SHARE_POST_LINK + inspiration_id;
                                com.flatlay.dialog.ShareDialog dialog = new com.flatlay.dialog.ShareDialog(mContext, inspirationImageString, postlink);
                                dialog.show();
                            }
                        })

                        .show();
                return true;
            }
        });
    }


    private void getInspirationFeedList() {
        isLoading_feed = !isLoading_feed;
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                isLoading_feed = !isLoading_feed;
                Syso.info("In handleOnSuccess>>" + object);
                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
                product_list.addAll(inspirationFeedRes.getData());
                if (inspirationFeedRes.getData().size() < 10) {
                    isLoading_feed = true;
                }

                if (product_list.size() > 0 && isFirstTime_feed) {
                    inspirationAdapter = new InspirationGridAdapter(mContext, product_list, 0);
                    imagesList.setAdapter(inspirationAdapter);
                    imagesList.setVisibility(View.VISIBLE);
                } else if (inspirationAdapter != null) {
                    inspirationAdapter.setData(product_list);
                    inspirationAdapter.notifyDataSetChanged();
                }

                if (product_list.size() == 0 && isFirstTime_feed) {
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

                isLoading_feed = !isLoading_feed;
                if (object != null) {
                    InspirationFeedRes response = (InspirationFeedRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        inspirationFeedApi.getInspirationFeed(inspirationUserId, false, String.valueOf(page), UserPreference.getInstance().getUserID());
        inspirationFeedApi.execute();
    }

    @Override
    public void refreshData(Bundle bundle) {
    }

    private void getCollectionList() {
        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                collectionLists2 = collectionApiRes.getCollection();
                Log.e(TAG, String.valueOf(collectionLists2.size()));

                if (collectionLists2.size() > 0 && isFirstTime_coll) {

                    collectionAdapter = new FragmentProfileCollectionAdapter(mContext,
                            collectionLists2, inspirationUserId,
                            fragmentProfileView, null, 0,
                            new FragmentProfileCollectionAdapter.ListAdapterListener() {
                                @Override
                                public void onClickAtOKButton(int position) {
                                }
                            });
                    collectionList.setAdapter(collectionAdapter);
                    collectionList.setVisibility(View.VISIBLE);
                } else if (collectionAdapter != null) {
                    collectionAdapter.setData(collectionLists2);
                    collectionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, collectionApiRes.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        collectionApi.getCollectionList(inspirationUserId);
        collectionApi.execute();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        fb_share = (ImageView) mainView.findViewById(R.id.fb_share);
        tw_share = (ImageView) mainView.findViewById(R.id.tw_share);
        pin_share = (ImageView) mainView.findViewById(R.id.pin_share);
        gen_share = (ImageView) mainView.findViewById(R.id.gen_share);
        other_followingtext1 = (TextView) mainView.findViewById(R.id.other_followingtext1);
        other_followingtext1.setTypeface(FontUtility.setMontserratLight(mContext));
        other_profile_pic = (CircleImageView) mainView.findViewById(R.id.other_profile_pic);
        expand_collection = (RelativeLayout) mainView.findViewById(R.id.expand);
        likeCountTextImage = (SmallBangView) mainView.findViewById(R.id.likeCountTextImage);
        layout_collection = (RelativeLayout) mainView.findViewById(R.id.expand);
        imageLayout = (RelativeLayout) mainView.findViewById(R.id.imageLayout);
        other_button22 = (Button) mainView.findViewById(R.id.other_button22);
        other_button22.setTypeface(FontUtility.setMontserratLight(mContext));
        other_button11 = (Button) mainView.findViewById(R.id.other_button11);
        other_button11.setTypeface(FontUtility.setMontserratLight(mContext));
        other_button11.setTextColor(Color.WHITE);
        other_button11.setBackgroundResource(R.drawable.green_corner_button);
        inspirationImage = (ImageView) mainView.findViewById(R.id.inspirationImage);
        deletePost = (ImageView) mainView.findViewById(R.id.deletePost);
        deletePost2 = (ImageView) mainView.findViewById(R.id.deletePost2);
        likeCount = (TextView) mainView.findViewById(R.id.likeCount);
        descriptionArrow = (ImageView) mainView.findViewById(R.id.descriptionArrow);
        descriptionText = (TextView) mainView.findViewById(R.id.tvDescription);
        descriptionText.setTypeface(FontUtility.setMontserratLight(mContext));
        commentList = (LinearLayout) mainView.findViewById(R.id.commentList);
        commentList.setFocusable(false);
        other_followingtext2 = (TextView) mainView.findViewById(R.id.other_followingtext2);
        other_followingtext2.setTypeface(FontUtility.setMontserratLight(mContext));
        other_nameText = (TextView) mainView.findViewById(R.id.other_nameText);
        other_nameText.setTypeface(FontUtility.setMontserratLight(mContext));
        backIconLayout = (LinearLayout) mainView.findViewById(R.id.backIconLayout);
        layout2 = (LinearLayout) mainView.findViewById(R.id.layout2);
        contentContainer = (FrameLayout) mainView.findViewById(R.id.content_container);
        commentBtn = (Button) mainView.findViewById(R.id.commentBtn);
        commentBtn.setTypeface(FontUtility.setMontserratRegular(mContext));
        commentEditText = (CustomAutoCompleteView) mainView.findViewById(R.id.commentEditText);
        commentEditText.setTypeface(FontUtility.setMontserratLight(mContext));
        scrollView = (ScrollView) mainView.findViewById(R.id.scrollView);
        bottomLayout = (LinearLayout) mainView.findViewById(R.id.layout_bottom);
        productLayout = (HorizontalScrollView) mainView.findViewById(R.id.productLayout);
        frameLayout = (FrameLayout) mainView.findViewById(R.id.overlayView);
        commentCountTextImage = (ImageView) mainView.findViewById(R.id.commentCountTextImage);
        prof = (CircleImageView) mainView.findViewById(R.id.prof);
        userText = (TextView) mainView.findViewById(R.id.userText);
        userText.setTypeface(FontUtility.setMontserratLight(mContext));
        tvDescriptionline = (View) mainView.findViewById(R.id.tvDescriptionline);
        nameText = (TextView) mainView.findViewById(R.id.nameText);
        nameText.setTypeface(FontUtility.setMontserratLight(mContext));
        other_followertext2 = (TextView) mainView.findViewById(R.id.other_followertext2);
        other_followertext2.setTypeface(FontUtility.setMontserratLight(mContext));
        text1 = (TextView) mainView.findViewById(R.id.text1);
        text1.setTypeface(FontUtility.setMontserratLight(mContext));
        text2 = (TextView) mainView.findViewById(R.id.text2);
        text2.setTypeface(FontUtility.setMontserratLight(mContext));
        text3 = (TextView) mainView.findViewById(R.id.text3);
        text3.setTypeface(FontUtility.setMontserratLight(mContext));
        text4 = (TextView) mainView.findViewById(R.id.text4);
        text4.setTypeface(FontUtility.setMontserratLight(mContext));
        followingtext1 = (TextView) mainView.findViewById(R.id.followingtext1);
        followingtext1.setTypeface(FontUtility.setMontserratLight(mContext));
        button1 = (Button) mainView.findViewById(R.id.button11);
        button1.setTypeface(FontUtility.setMontserratLight(mContext));
        button2 = (Button) mainView.findViewById(R.id.button22);
        button2.setTypeface(FontUtility.setMontserratLight(mContext));
        button1.setTextColor(Color.WHITE);
        overflow_layout5 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout5);
        button1.setBackgroundResource(R.drawable.green_corner_button);
        followertext1 = (TextView) mainView.findViewById(R.id.followertext1);
        followertext1.setTypeface(FontUtility.setMontserratLight(mContext));
        followingtext2 = (TextView) mainView.findViewById(R.id.followingtext2);
        followingtext2.setTypeface(FontUtility.setMontserratLight(mContext));
        followertext2 = (TextView) mainView.findViewById(R.id.followertext2);
        followertext2.setTypeface(FontUtility.setMontserratLight(mContext));
        imagesList = (GridView) mainView.findViewById(R.id.imagesList);
        shareDialog = new ShareDialog(mContext);
        other_followertext1 = (TextView) mainView.findViewById(R.id.other_followertext1);
        other_followertext1.setTypeface(FontUtility.setMontserratLight(mContext));
        other_collectionList = (ListView) mainView.findViewById(R.id.other_collectionList);
        imagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                FragmentInspirationDetail.this.firstVisibleItem = firstVisibleItem;
                FragmentInspirationDetail.this.visibleItemCount = visibleItemCount;
                FragmentInspirationDetail.this.totalItemCount = totalItemCount;
                if (!isLoading_feed && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (checkInternet()) {
                        page++;
                        isFirstTime_feed = false;
                        getInspirationFeedList();
                    } else {
                    }
                }
            }
        });

        FragmentInspirationDetail.CommentTextWatcher watcher = new FragmentInspirationDetail.CommentTextWatcher();
        commentEditText.addTextChangedListener(watcher);

        collectionList = (ListView) mainView.findViewById(R.id.collectionList);

        productInflaterLayout = (LinearLayout) mainView.findViewById(R.id.productInflaterLayout);
        pdkClient = PDKClient.configureInstance(mContext, AppConstants.PINTEREST_APP_ID);
        pdkClient.onConnect(mContext);
        pdkClient.setDebugMode(true);
        PDKClient.getInstance().onConnect(mContext);
        overflow1 = (MyMaterialContentOverflow) mainView.findViewById(R.id.overflow1);
        overflow3 = (MyMaterialContentOverflow) mainView.findViewById(R.id.overflow3);
        overflow2 = (MyMaterialContentOverflow) mainView.findViewById(R.id.overflow2);
        other_imagesList = (GridView) mainView.findViewById(R.id.other_imagesList);
        other_imagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int other_firstVisibleItem, int other_visibleItemCount, int other_totalItemCount) {
                FragmentInspirationDetail.this.other_firstVisibleItem = other_firstVisibleItem;
                FragmentInspirationDetail.this.other_visibleItemCount = other_visibleItemCount;
                FragmentInspirationDetail.this.other_totalItemCount = other_totalItemCount;
                if (!isother_Loading_feed && other_firstVisibleItem + other_visibleItemCount == other_totalItemCount
                        && other_totalItemCount != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page7++;
                        is_other_FirstTime_feed = false;
                        getOtherInspirationFeedList(otherUserId);
                    } else {
                    }
                }
            }
        });
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
        Tracker t = ((KikrApp) mContext.getApplication()).getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("Fragment Inspiration Detail");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        if (triggerOpenComment) {

            descriptionText.setVisibility(View.VISIBLE);
            tvDescriptionline.setVisibility(View.VISIBLE);
            commentCountTextImage.setImageResource(R.drawable.black_cancel_icon);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    commentList.setVisibility(View.VISIBLE);
                    bottomLayout.setVisibility(View.VISIBLE);
                    productLayout.setVisibility(View.GONE);
                    productInflaterLayout.setVisibility(View.GONE);
                }
            }, 400);

            layout2.animate().translationY(0 - 150).setDuration(400);
            scrollView.animate().translationY(0 - 150).setDuration(400);
        }
    }

    public void removePost(final String inspiration_id, final String user_id) {
        this.inspiration_id = inspiration_id;
        this.user_id = user_id;

        deletePostApi = new DeletePostApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {

                if (object.toString().equals(Constants.WebConstants.SUCCESS_CODE)) {
                    addFragment(new FragmentInspirationSection());
                    ((HomeActivity) mContext).mFragmentStack.clear();
                    ((HomeActivity) mContext).addFragment(new FragmentDiscoverNew());
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        deletePostApi.removePost(inspiration_id, user_id);
        deletePostApi.execute();
    }


    public boolean checkInternet() {
        if (CommonUtility.isOnline(mContext)) {
            return true;
        } else {
            CommonUtility.showNoInternetAlert(mContext);
            return false;
        }
    }

    private void getOtherInspirationFeedList(String user_id) {
        isother_Loading_feed = !isother_Loading_feed;
        final InspirationFeedApi other_inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {

                isother_Loading_feed = !isother_Loading_feed;
                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;

                other_product_list.addAll(inspirationFeedRes.getData());
                if (inspirationFeedRes.getData().size() < 10) {
                    isother_Loading_feed = true;
                }

                if (other_product_list.size() > 0 && is_other_FirstTime_feed) {
                    other_inspirationGridAdapter = new InspirationGridAdapter(mContext, other_product_list, 0);
                    other_imagesList.setAdapter(other_inspirationGridAdapter);
                    other_imagesList.setVisibility(View.VISIBLE);
                } else if (other_inspirationGridAdapter != null) {
                    other_inspirationGridAdapter.setData(other_product_list);
                    other_inspirationGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                isother_Loading_feed = !isother_Loading_feed;
                if (object != null) {
                    InspirationFeedRes response = (InspirationFeedRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        other_inspirationFeedApi.getInspirationFeed(user_id, false, String.valueOf(page7), UserPreference.getInstance().getUserID());
        other_inspirationFeedApi.execute();
    }

    private void getOtherCollectionList(final String user_id) {
        final CollectionApi other_collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                other_collectionLists2 = collectionApiRes.getCollection();
                if (other_collectionLists2.size() > 0 && is_other_FirstTime_coll) {
                    other_collectionAdapter = new FragmentProfileCollectionAdapter(mContext,
                            other_collectionLists2, user_id,
                            fragmentProfileView, null, 0,
                            new FragmentProfileCollectionAdapter.ListAdapterListener() {
                                @Override
                                public void onClickAtOKButton(int position) {
                                }
                            });
                    other_collectionList.setAdapter(other_collectionAdapter);
                    other_collectionList.setVisibility(View.VISIBLE);
                } else if (other_collectionAdapter != null) {
                    other_collectionAdapter.setData(other_collectionLists2);
                    other_collectionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, collectionApiRes.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        other_collectionApi.getCollectionList(user_id);
        other_collectionApi.execute();
    }


    @Override
    public void setData(Bundle bundle) {
        user_id = UserPreference.getInstance().getUserID();
        if (checkInternet() && inspiration == null) {
            getInspirationDetails(inspiration_id);
        } else if (checkInternet()) {
            inspiration_id = inspiration.getInspiration_id();
            getInspirationDetails(inspiration_id);
            profileimg = inspiration.getProfile_pic();
            username = inspiration.getUsername();
            inspiration_id = inspiration.getInspiration_id();
            inspirationUserId = inspiration.getUser_id();
            inspirationImageString = inspiration.getInspiration_image();
            inspirationProductName = inspiration.getProduct_name();
            inspirationDes = inspiration.getDescription();
            if (inspiration != null) {
                if (user_id.equals(inspirationUserId)) {
                    isMyPost = true;
                    deletePost.setVisibility(View.GONE);
                    deletePost2.setVisibility(View.VISIBLE);
                    overflow2.setVisibility(View.GONE);
                    overflow1.setVisibility(View.VISIBLE);

                } else {
                    deletePost2.setVisibility(View.GONE);
                    deletePost.setVisibility(View.VISIBLE);
                    overflow1.setVisibility(View.GONE);
                    overflow2.setVisibility(View.VISIBLE);
                    isMyPost = false;
                    getInspirationFeedList();
                    getCollectionList();
                }
            }
            likeCount.setText(inspiration.getLike_count());
            setDetails();
        }
        likeCount.setTypeface(FontUtility.setMontserratLight(mContext));

        if (!isLike) {
            likeCountTextImage.setSelected(true);
        } else {
            likeCountTextImage.setSelected(false);
        }
        currentUp = System.currentTimeMillis();
        lastDownUp = System.currentTimeMillis();
        CommonUtility.setImage(mContext, profileimg, prof, R.drawable.profile_icon);
        userText.setText(username);
        nameText.setText(username);

        final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {

                MyProfileRes myProfileRes = (MyProfileRes) object;
                userDetails = myProfileRes.getUser_data();
                followersLists = myProfileRes.getFollowers_list();
                followingLists = myProfileRes.getFollowing_list();
                followingtext1.setText(String.valueOf(followingLists.size()));
                followertext1.setText(String.valueOf(followersLists.size()));
                if (userDetails.get(0).getIs_followed() != null && userDetails.get(0).getIs_followed().equals("yes")) {
                    isFollowing = true;
                    deletePost.setImageResource(R.drawable.checkedicon);
                } else {
                    isFollowing = false;
                    deletePost.setImageResource(R.drawable.follow_white_transparent);

                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

            }
        });
        myProfileApi.getUserProfileDetail(inspirationUserId, user_id);
        myProfileApi.execute();
    }


    private List<FollowerList> followersLists = new ArrayList<FollowerList>();
    private List<FollowerList> followingLists = new ArrayList<FollowerList>();
    private List<UserData> userDetails;

    private void setProductPoints(FrameLayout frameLayout) {
        if (!TextUtils.isEmpty(inspiration.getProduct_xy())) {
            String[] points = inspiration.getProduct_xy().split("-");
            for (int i = 0; i < points.length; i++) {
                String[] xy = points[i].split(",");
                TagView tagView = new TagView(mContext);
                frameLayout.addView(tagView);
                try {
                    tagView.setXY(Float.parseFloat(xy[0]), Float.parseFloat(xy[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (inspirationProductName.split(AppConstants.NAME_SEPRATER).length > i && !TextUtils.isEmpty(inspirationProductName.split(AppConstants.NAME_SEPRATER)[i])) {
                    tagView.setTagText(inspirationProductName.split(AppConstants.NAME_SEPRATER)[i]);
                }
            }
        }
    }

    private void setItemPoint(FrameLayout frameLayout) {
        TagView tagView = new TagView(mContext);
        if (!TextUtils.isEmpty(inspiration.getItem_xy())) {
            tagView.setXY(Float.parseFloat(inspiration.getItem_xy().split(",")[0]) / scaleFactor, Float.parseFloat(inspiration.getItem_xy().split(",")[1]) / scaleFactor);
        }

    }

    private void getUsersList() {

        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                if (object != null) {
                    InspirationRes inspirationRes = (InspirationRes) object;
                    list = inspirationRes.getComment();
                    for (int i = 0; i < list.size(); i++) {
                        SearchUser searchUser = new SearchUser();
                        searchUser.setUserId(list.get(i).getUser_id());
                        searchUser.setUsername(list.get(i).getUser_name());
                        addSearchUser(searchUser);
                    }
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InspirationRes response = (InspirationRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        inspirationSectionApi.getInspirationDetail(UserPreference.getInstance().getUserID(), inspiration_id, "inspiration");
        inspirationSectionApi.execute();
    }

    protected void addSearchUser(SearchUser searchUser) {
        boolean isContain = false;
        for (int i = 0; i < usersList.size(); i++) {
            if (usersList.get(i).getUserId().equals(searchUser.getUserId()))
                isContain = true;
        }
        if (!isContain)
            usersList.add(searchUser);
    }

    private void showOtherUserProfileHelper(final Comment comment) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                final String other_user_id = comment.getUser_id();
                final String other_user_name = comment.getUser_name();
                final String image = comment.getProfile_pic();
                if (!other_user_id.equals(otherUserId)) {
                    otherUserId = other_user_id;
                    is_other_FirstTime_feed = true;
                    isother_Loading_feed = false;
                    page7 = 0;
                    other_product_list.clear();
                    CommonUtility.setImage(mContext, image, other_profile_pic, R.drawable.profile_icon);
                    other_nameText.setText(other_user_name);
                    other_button22.setTextColor(Color.BLACK);
                    other_button11.setTextColor(Color.WHITE);
                    other_button22.setBackgroundResource(R.drawable.white_button_noborder);
                    other_button11.setBackgroundResource(R.drawable.green_corner_button);
                    getOtherInspirationFeedList(other_user_id);
                    getOtherCollectionList(other_user_id);
                    final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
                        @Override
                        public void handleOnSuccess(Object object) {
                            MyProfileRes myProfileRes = (MyProfileRes) object;
                            other_followersLists = myProfileRes.getFollowers_list();
                            other_followingLists = myProfileRes.getFollowing_list();
                            other_followertext1.setText(String.valueOf(other_followingLists.size()));
                            other_followingtext1.setText(String.valueOf(other_followersLists.size()));
                        }

                        @Override
                        public void handleOnFailure(ServiceException exception, Object object) {

                        }
                    });
                    myProfileApi.getUserProfileDetail(other_user_id, UserPreference.getInstance().getUserID());
                    myProfileApi.execute();
                }
            }
        }, 800);
    }

    private void showOtherUserProfile(final Comment comment) {
        if (!comment.getUser_id().equals(UserPreference.getInstance().getUserID())) {
            if (overflow1.isOpen()) {
                overflow1.triggerClose();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        overflow3.triggerSlide();
                        showOtherUserProfileHelper(comment);
                    }
                }, 800);
            } else if (overflow2.isOpen()) {
                overflow2.triggerClose();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        overflow3.triggerSlide();
                        showOtherUserProfileHelper(comment);
                    }
                }, 800);
            } else {
                overflow3.triggerSlide();
                showOtherUserProfileHelper(comment);
            }
        }
    }

    private void getInspirationDetails(String id) {
        list1.clear();
        list.clear();
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    InspirationRes inspirationRes = (InspirationRes) object;
                    list1 = inspirationRes.getComment();
                    list.addAll(list1);
                    if (isPostComment) {
                        list.clear();
                        list.addAll(list1);
                    }
                    commentList.removeAllViews();
                    if (list.size() >= 0) {
                        commentList.addView(new InspirationCommentsUI(mContext, list, fragmentInspirationDetail, new InspirationCommentsUI.CommentUIListener() {
                            @Override
                            public void onClickAtOKButton(int position) {
                                showOtherUserProfile(list.get(position));
                            }
                        }).getView(), 0);
                    }
                    if (inspiration == null || isFromNotification) {
                        inspiration = inspirationRes.getInspiration();
                        profileimg = inspiration.getProfile_pic();
                        username = inspiration.getUsername();
                        inspiration_id = inspiration.getInspiration_id();
                        inspirationUserId = inspiration.getUser_id();
                        inspirationImageString = inspiration.getInspiration_image();
                        inspirationProductName = inspiration.getProduct_name();
                        inspirationDes = inspiration.getDescription();
                        if (inspiration != null) {
                            if (user_id.equals(inspirationUserId)) {
                                isMyPost = true;
                                deletePost.setVisibility(View.GONE);
                                deletePost2.setVisibility(View.VISIBLE);
                                overflow2.setVisibility(View.GONE);
                                overflow1.setVisibility(View.VISIBLE);

                            } else {
                                deletePost2.setVisibility(View.GONE);
                                deletePost.setVisibility(View.VISIBLE);
                                overflow1.setVisibility(View.GONE);
                                overflow2.setVisibility(View.VISIBLE);
                                isMyPost = false;
                                getInspirationFeedList();
                                getCollectionList();
                            }
                        }
                        likeCount.setText(inspiration.getLike_count());
                    }

                    setDetails();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (isFirstTime) {
                    isFirstTime = false;
                }
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InspirationRes response = (InspirationRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        inspirationSectionApi.getInspirationDetail(UserPreference.getInstance().getUserID(), id, "inspiration");
        inspirationSectionApi.execute();
    }

    protected void setDetails() {
        if (!TextUtils.isEmpty(inspirationDes)) {
            descriptionText.setText(inspirationDes);
        } else {
            descriptionText.setText("");
        }
        if (inspiration.getPoducts() == null || inspiration.getPoducts().size() == 0) {
        } else {
            expand_collection.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.VISIBLE);
        }
        CommonUtility.setImage(mContext, inspirationImageString, inspirationImage, R.drawable.dum_list_item_product);


        try {

        } catch (Exception ex) {
        }
        try {

        } catch (Exception ex) {
        }
        try {

        } catch (Exception ex) {
        }
        try {

        } catch (Exception ex) {
        }
        Calendar calLocal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        Calendar calServer = Calendar.getInstance();
        try {
            Date dd = df.parse(inspiration.getDateadded());
            calServer.setTime(dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        getUsersList();
        commentEditText.addTextChangedListener(new CustomAutoCompleteTextChangedListener(mContext));
        adapter = new AutocompleteCustomArrayAdapter(mContext, R.layout.list_item, temp_usersList);
        commentEditText.setAdapter(adapter);

        commentEditText.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String selectedText = temp_usersList.get(arg2);
                commentEditText.setText(lastSelected2.trim() + " @" + selectedText);
                commentEditText.setSelection(commentEditText.getText().toString().length());
                temp_usersList.clear();
                adapter.notifyDataSetChanged();
            }
        });
        if (inspiration.getPoducts() != null && inspiration.getPoducts().size() > 0) {
            productInflaterLayout.removeAllViews();
            productInflaterLayout.addView(new InspirationProductUI(mContext, inspiration, null, 500, 500, 1).getView());

        }
    }

    String postlink;
    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";
    static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/flatlay/";
    static String SHARE_POST_LINK2 = "http://flat-lay.com/flatlay/";
    private PDKClient pdkClient;

    private void getBoardList(final String text, final String link, final String imageUrl) {
        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getName());
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getUid());
                if (response.getBoardList() != null && response.getBoardList().size() > 0) {
                    Syso.info("1234567890  2>>>>>> inside condition");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fb_share:
                fb_share.setImageResource(R.drawable.tealfb);
                pin_share.setImageResource(R.drawable.pinterestlogoo);
                tw_share.setImageResource(R.drawable.twitterlogoo);
                gen_share.setImageResource(R.drawable.shareicon);

                postlink = SHARE_POST_LINK2 + inspiration_id;
                Log.w(TAG, "Facebook button clicked!" + postlink);
                if (com.facebook.share.widget.ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(postlink))
                            .build();
                    shareDialog.show(content);
                }
                break;
            case R.id.pin_share:
                pin_share.setImageResource(R.drawable.tealpinterest);
                tw_share.setImageResource(R.drawable.twitterlogoo);
                gen_share.setImageResource(R.drawable.shareicon);
                fb_share.setImageResource(R.drawable.facebooklogoo);
                postlink = SHARE_POST_LINK2 + inspiration_id;
                List scopes = new ArrayList<String>();
                scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
                scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
                pdkClient.login(mContext, scopes, new PDKCallback() {
                    @Override
                    public void onSuccess(PDKResponse response) {
                        Log.d(getClass().getName(), response.getData().toString());
                        getBoardList(null, null, null);
                        onSavePin(mContext, inspirationImageString, UserPreference.getInstance().getmPinterestBoardId(), inspirationDes + ". Find it at " + postlink, postlink);
                    }

                    @Override
                    public void onFailure(PDKException exception) {
                        Log.e(getClass().getName(), exception.getDetailMessage());
                    }
                });
                break;
            case R.id.tw_share:
                tw_share.setImageResource(R.drawable.tealtwiter);
                pin_share.setImageResource(R.drawable.pinterestlogoo);
                gen_share.setImageResource(R.drawable.shareicon);
                fb_share.setImageResource(R.drawable.facebooklogoo);
                if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
                } else {
                    postlink = SHARE_POST_LINK2 + inspiration_id;
                    final TwitterSession session = TwitterCore.getInstance().getSessionManager()
                            .getActiveSession();
                    try {
                        URL postlink_to_url = new URL(postlink);

                        TweetComposer.Builder builder = new TweetComposer.Builder(mContext)
                                .url(postlink_to_url)
                                .text(inspirationDes);
                        builder.show();


                    } catch (Exception e) {
                    }
                }
                break;
            case R.id.gen_share:
                gen_share.setImageResource(R.drawable.teal_gen);
                pin_share.setImageResource(R.drawable.pinterestlogoo);
                tw_share.setImageResource(R.drawable.twitterlogoo);
                fb_share.setImageResource(R.drawable.facebooklogoo);
                postlink = SHARE_POST_LINK + inspiration_id;
                com.flatlay.dialog.ShareDialog dialog = new com.flatlay.dialog.ShareDialog(mContext, inspirationImageString, postlink);
                dialog.show();
                break;
            case R.id.commentCountTextImage:
                if (!commentOpen) {
                    descriptionText.setVisibility(View.VISIBLE);
                    tvDescriptionline.setVisibility(View.VISIBLE);
                    commentCountTextImage.setImageResource(R.drawable.black_cancel_icon);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            commentList.setVisibility(View.VISIBLE);
                            bottomLayout.setVisibility(View.VISIBLE);
                            productLayout.setVisibility(View.GONE);
                            productInflaterLayout.setVisibility(View.GONE);
                        }
                    }, 400);
                    layout2.animate().translationY(0 - 150).setDuration(400);
                    scrollView.animate().translationY(0 - 150).setDuration(400);
                    commentOpen = true;
                } else {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            descriptionText.setVisibility(View.GONE);
                            tvDescriptionline.setVisibility(View.GONE);
                        }
                    }, 400);
                    productLayout.setVisibility(View.VISIBLE);
                    commentList.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                    productLayout.setVisibility(View.VISIBLE);
                    productInflaterLayout.setVisibility(View.VISIBLE);
                    commentCountTextImage.setImageResource(R.drawable.speechbubble);
                    layout2.animate().translationY(0).setDuration(400);
                    scrollView.animate().translationY(0).setDuration(400);
                    commentOpen = false;
                }

                break;
            case R.id.commentBtn:
                commentBtn.setTextColor(getResources().getColor(R.color.btn_green));
                commentBtn.setBackgroundResource(R.drawable.white_corner_button);
                if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                    CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                    createAccountDialog.show();
                } else {
                    CommonUtility.hideSoftKeyboard(mContext);
                    validateUserInput();
                }
                break;

            case R.id.backIconLayout:
                mContext.onBackPressed();
                break;
            case R.id.likeCountTextImage:
                lastDownUp = System.currentTimeMillis();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        currentUp = System.currentTimeMillis();
                        if (currentUp - lastDownUp > 900) {
                            likeInspiration();
                        }
                    }
                }, 1000);
                if (!isLike) {
                    likeInspiration();
                    likeCountTextImage.setSelected(false);
                    likeCountTextImage.likeAnimation();
                    isLike = true;
                    inspiration.setLike_count((getInt(inspiration.getLike_count()) + 1) + "");
                    likeCount.setText(inspiration.getLike_count());
                } else {
                    likeInspiration();
                    likeCountTextImage.setSelected(true);
                    isLike = false;
                    inspiration.setLike_count(((getInt(inspiration.getLike_count()) - 1) < 0 ? 0 : (getInt(inspiration.getLike_count()) - 1)) + "");
                    likeCount.setText(inspiration.getLike_count());
                }
                break;

            case R.id.deletePost:
                followUnFollowUser();
                break;

            case R.id.deletePost2:
                if (!isChoosenTeal) {
                    overflow1.setVisibility(View.VISIBLE);
                    if (overflow3.isOpen()) {
                        overflow3.triggerClose();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                overflow1.triggerSlide();
                            }
                        }, 800);

                    } else
                        overflow1.triggerSlide();
                    deletePost2.setImageResource(R.drawable.deleteediticon2);
                    isChoosenTeal = true;
                } else {
                    overflow1.triggerClose();
                    deletePost2.setImageResource(R.drawable.deleteediticon);
                    isChoosenTeal = false;
                }
                break;

            default:
                break;
        }
    }


    boolean isFollowing = false;

    private void followUnFollowUser() {
        if (isFollowing) unfollowUser();
        else followUser();
        updateFollower();
    }

    private void followUser() {

        if (!isFollowing) {

            final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

                @Override
                public void handleOnSuccess(Object object) {
                    Syso.info("In handleOnSuccess>>" + object);
                    AlertUtils.showToast(mContext, R.string.user_followed_successfully);
                    deletePost.setImageResource(R.drawable.checkedicon);
                    isFollowing = true;
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    Syso.info("In handleOnFailure>>" + object);
                    if (object != null) {
                        FollowUserRes response = (FollowUserRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                }
            });
            followUserApi.followUser(user_id, inspirationUserId);
            followUserApi.execute();
        } else {

        }
    }

    private void unfollowUser() {

        if (isFollowing) {

            final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

                @Override
                public void handleOnSuccess(Object object) {
                    isFollowing = false;
                    deletePost.setImageResource(R.drawable.follow_white_transparent);
                    AlertUtils.showToast(mContext, R.string.user_unfollowed_successfully);
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    if (object != null) {
                        FollowUserRes response = (FollowUserRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                }
            });
            followUserApi.unFollowUser(user_id, inspirationUserId);
            followUserApi.execute();
        } else {

        }
    }

    public void updateFollower() {

        final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {

                MyProfileRes myProfileRes = (MyProfileRes) object;
                userDetails = myProfileRes.getUser_data();
                followersLists = myProfileRes.getFollowers_list();
                followertext1.setText(String.valueOf(followersLists.size()));
                if (userDetails.get(0).getIs_followed() != null && userDetails.get(0).getIs_followed().equals("yes")) {
                    isFollowing = true;
                    deletePost.setImageResource(R.drawable.checkedicon);
                } else {
                    isFollowing = false;
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

            }
        });
        myProfileApi.getUserProfileDetail(inspirationUserId, user_id);
        myProfileApi.execute();
    }

    private void likeInspiration() {
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                InspirationRes inspirationRes = (InspirationRes) object;
                if (TextUtils.isEmpty(inspirationRes.getLike_id())) {
                    inspiration.setLike_id(inspirationRes.getLike_id());
                } else {
                    inspiration.setLike_id(inspirationRes.getLike_id());
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        if (isLike) {
            inspirationSectionApi.postLike(user_id, inspiration_id, "inspiration");
        } else {
            inspirationSectionApi.removeLike(user_id, inspiration_id);
        }
        inspirationSectionApi.execute();
    }

    private void validateUserInput() {
        boolean isValid = true;
        String comment = commentEditText.getText().toString().trim();
        if (TextUtils.isEmpty(comment)) {
            isValid = false;
            commentEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_no_comment_entered);
        }
        if (isValid && checkInternet()) {
            if (inspiration != null)
                postComment(comment);
            else
                AlertUtils.showToast(mContext, "Data loading, please wait");
        }
    }

    private void postComment(final String comment) {
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                commentEditText.setText("");
                if (checkInternet()) {
                    isPostComment = true;
                    getInspirationDetails(inspiration_id);
                    getUsersList();

                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        inspirationSectionApi.postComment(user_id, inspiration_id, "inspiration", comment);
        inspirationSectionApi.execute();
    }

    public void removeComment(String commentId) {
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (checkInternet()) {
                    isPostComment = true;
                    getInspirationDetails(inspiration_id);
                    getUsersList();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        inspirationSectionApi.removeComment(user_id, commentId);
        inspirationSectionApi.execute();
    }

    public void showRemovePopup(String commentId) {
        DeleteCommentDialog deleteCommentDialog = new DeleteCommentDialog(mContext, fragmentInspirationDetail, commentId);
        deleteCommentDialog.getWindow().setBackgroundDrawableResource(R.color.real_transparent);
        deleteCommentDialog.show();
    }

    private class CommentTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String text = commentEditText.getText().toString();
            if (text != null && text.length() != 0) {
                commentBtn.setTextColor(Color.WHITE);
                commentBtn.setBackgroundResource(R.drawable.teal_corner_button);
            } else {
                commentBtn.setTextColor(getResources().getColor(R.color.btn_green));
                commentBtn.setBackgroundResource(R.drawable.white_corner_button);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
        }
        return false;
    }

    public class CustomAutoCompleteTextChangedListener implements TextWatcher {

        public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
        Context context;

        public CustomAutoCompleteTextChangedListener(Context context) {
            this.context = context;
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence i, int start, int before, int count) {

            String input = i + "$";
            String[] data = input.toString().split(" ");
            String userInput;
            if (data[data.length - 1].equals("$")) {
                userInput = "";
            } else {
                userInput = data[data.length - 1];
                userInput = userInput.substring(0, userInput.length() - 1);
            }
            lastUserInput2 = lastUserInput;
            lastUserInput = userInput;
            lastSelected2 = lastSelected;
            lastSelected = input.toString().substring(0, input.length() - userInput.length() - 1);
            if (userInput.toString().startsWith("@")) {
                try {
                    userInput = userInput.toString().replace("@", "").trim();
                    temp_usersList.clear();
                    Syso.info("temp_usersList :  " + temp_usersList);
                    adapter.notifyDataSetChanged();
                    for (SearchUser ss : usersList) {
                        if (ss != null) {
                            if (ss.getUsername().toUpperCase().contains(
                                    userInput.toString().toUpperCase())) {
                                temp_usersList.add(ss.getUsername());
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    adapter = new AutocompleteCustomArrayAdapter(mContext,
                            R.layout.list_item, temp_usersList);
                    commentEditText.setAdapter(adapter);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (lastUserInput2.length() > 0 && !lastUserInput2.contains("@")) {
                temp_usersList.clear();
                adapter.notifyDataSetChanged();
            }
        }

    }


    private int getInt(String image_width) {
        try {
            return Integer.parseInt(image_width);
        } catch (Exception e) {
            return 0;
        }
    }

    private class GetBitmapFromUrl extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL(params[0]);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result != null) {
                bitmap = result;
                showDataOnImage();
            }
        }
    }

    private void showDataOnImage() {
        inspirationImage.setImageBitmap(bitmap);
        setScalingFactor();
        showTagOnImage();
    }

    private void showTagOnImage() {
        setItemPoint(frameLayout);

        setProductPoints(frameLayout);
    }

    private void setScalingFactor() {
        float dWidth = CommonUtility.getDeviceWidth(mContext);
        float dHeight = CommonUtility.getDeviceHeight(mContext);
        float imgWidth = bitmap.getWidth();
        float imgHeight = bitmap.getHeight();
        if (imgWidth > dWidth) {
            scaleFactor = imgWidth / dWidth;
        }
    }

}
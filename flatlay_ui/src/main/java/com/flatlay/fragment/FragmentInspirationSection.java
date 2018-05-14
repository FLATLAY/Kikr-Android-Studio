package com.flatlay.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flatlay.BaseActivityWithVideo;
import com.flatlay.BaseFragment;
import com.flatlay.KikrApp;
import com.flatlay.R;
import com.flatlay.activity.EditProfileActivity;
import com.flatlay.activity.FollowCategoriesNewActivity;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.AllBrandGridAdapter;
import com.flatlay.adapter.CardGridAdapter;
import com.flatlay.adapter.CartItemsAdapter;
import com.flatlay.adapter.CustomizeInterestPeopleListAdapter;
import com.flatlay.adapter.FeaturedTabAdapter;
import com.flatlay.adapter.FragmentProfileCollectionAdapter;
import com.flatlay.adapter.InspirationAdapter;
import com.flatlay.adapter.InspirationGridAdapter;
import com.flatlay.adapter.KikrFollowingAdapter;
import com.flatlay.adapter.ProductDetailGridAdapter;
import com.flatlay.adapter.ProductSearchGridAdapter;
import com.flatlay.adapter.StoreGridAdapter;
import com.flatlay.dialog.ComingSoonDialog;
import com.flatlay.dialog.OrderProcessingDialog;
import com.flatlay.dialog.ShareProfileDialog;
import com.flatlay.service.PlaceOrderService;
import com.flatlay.ui.ShippingAddressList;
import com.flatlay.utility.CardType;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.Luhn;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.api.AddressApi;
import com.flatlaylib.api.BrandListApi;
import com.flatlaylib.api.CardInfoApi;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.api.DeletePostApi;
import com.flatlaylib.api.FeaturedTabApi;
import com.flatlaylib.api.GeneralSearchApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.api.InterestSectionApi;
import com.flatlaylib.api.KikrCreditsApi;
import com.flatlaylib.api.LogoutApi;
import com.flatlaylib.api.MessageCenterApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.api.OAuthWebService;
import com.flatlaylib.api.ProductBasedOnBrandApi;
import com.flatlaylib.api.TwoTapApi;
import com.flatlaylib.api.UpdateCartApi;
import com.flatlaylib.bean.Address;
import com.flatlaylib.bean.BillingAddress;
import com.flatlaylib.bean.BrandList;
import com.flatlaylib.bean.BrandResult;
import com.flatlaylib.bean.Card;
import com.flatlaylib.bean.CartProduct;
import com.flatlaylib.bean.CartTotalInfo;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.FeaturedTabData;
import com.flatlaylib.bean.FollowerList;
import com.flatlaylib.bean.FollowingKikrModel;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProductResult;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.bean.UserResult;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.AddressRes;
import com.flatlaylib.service.res.BrandListRes;
import com.flatlaylib.service.res.CardInfoRes;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.service.res.FeaturedTabApiRes;
import com.flatlaylib.service.res.GeneralSearchRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.service.res.KikrCreditsRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.service.res.ProductBasedOnBrandRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RachelDi on 1/22/18.
 */

public class FragmentInspirationSection extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ServiceCallback, MultiAutoCompleteTextView.OnEditorActionListener {
    private View mainView, loaderView, highlight4, highlight5, highlight6;
    private ListView inspirationlist;
    private String inspiration_id, userId, user_id, original_text = "";
    private EditText searchText;
    private List<Inspiration> product_list = new ArrayList<Inspiration>();
    private TextView orders_text, cards_text, credits_text, loadingTextView;
    private boolean isFromNotification = false, isViewAll, isLoading = false, isFirstTime = true,
            isFirstTimeFromMain = false, isOnFeedPage = true;
    private int page = 0, index = 0, firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0;
    private DeletePostApi deletePostApi;
    private Animation aa, bb;
    public static final String TAG = "FragmentInsSec";
    private ImageView image_upload_post,
            image_upload_post2, image_camera, image_camera2;
    private RelativeLayout credits_layout, cards_layout, orders_layout,
            tab_layout1, tab_layout2, bottomLayout, uploadChoice;
    private MyMaterialContentOverflow3 overflow2;
    private CircleImageView profile_pic;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout cart_layout2,
            backIconLayout, fashioniconlayout, beautyiconlayout, fitnessiconlayout, foodiconlayout,
            techiconlayout, photographyiconlayoutlayout, homeiconlayout, occasioniconlayout, traveliconlayout,
            cart_tab, profile_tab;
    private InspirationAdapter inspirationAdapter;
    private InputMethodManager imm;
    private FragmentInspirationSection fragmentInspirationSection;
    private List<Inspiration> fashionList = new ArrayList<>(), beautyList = new ArrayList<>(),
            fitnessList = new ArrayList<>(), foodList = new ArrayList<>(), travelList = new ArrayList<>(),
            techList = new ArrayList<>(), photoList = new ArrayList<>(), homeList = new ArrayList<>(),
            occasioList = new ArrayList<>(), currentList = new ArrayList<>();
    private SearchProductFragment searchProductFragment;

    public FragmentInspirationSection() {
        this.isViewAll = true;
        isFirstTimeFromMain = true;
        this.userId = UserPreference.getInstance().getUserID();
    }

    public FragmentInspirationSection(Boolean isFromNotification) {
        this.isViewAll = true;
        isFirstTimeFromMain = true;
        this.userId = UserPreference.getInstance().getUserID();
        this.isFromNotification = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_inspiration_section, null);
        fragmentInspirationSection = this;
        return mainView;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    }

    public void initData() {
        index = 0;
        if (((HomeActivity) mContext).checkInternet()) {
            getInspirationFeedList();
        } else {
            showReloadOption();
        }
    }

    private void getInspirationFeedList() {
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.getInspirationFeed(userId, isViewAll, String.valueOf(page), userId);
        inspirationFeedApi.execute();
    }

    @Override
    public void handleOnSuccess(Object object) {
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.INVISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        hideDataNotFound();
        isLoading = !isLoading;
        InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
        if (index == 0) {
            product_list.addAll(inspirationFeedRes.getData());
            currentList = product_list;
        } else if (index == 1) {
            fashionList.addAll(inspirationFeedRes.getData());
            currentList = fashionList;
        } else if (index == 2) {
            foodList.addAll(inspirationFeedRes.getData());
            currentList = foodList;
        } else if (index == 3) {
            travelList.addAll(inspirationFeedRes.getData());
            currentList = travelList;
        } else if (index == 4) {
            occasioList.addAll(inspirationFeedRes.getData());
            currentList = occasioList;
        } else if (index == 5) {
            homeList.addAll(inspirationFeedRes.getData());
            currentList = homeList;
        } else if (index == 6) {
            photoList.addAll(inspirationFeedRes.getData());
            currentList = photoList;
        } else if (index == 7) {
            techList.addAll(inspirationFeedRes.getData());
            currentList = techList;
        } else if (index == 8) {
            fitnessList.addAll(inspirationFeedRes.getData());
            currentList = fitnessList;
        } else if (index == 9) {
            beautyList.addAll(inspirationFeedRes.getData());
            currentList = beautyList;
        }
        if (inspirationFeedRes.getData().size() < 10) {
            isLoading = true;
        }
        if (currentList.size() == 0 && isFirstTime) {
            showDataNotFound();
        }
        if (currentList.size() > 0 && isFirstTime) {
            inspirationAdapter = new InspirationAdapter(mContext, currentList, isViewAll, FragmentInspirationSection.this, new InspirationAdapter.ListAdapterListener() {
                @Override
                public void onClickAtOKButton(int position) {
                    final String other_user_id = currentList.get(position).getUser_id();
                    final String other_user_name = currentList.get(position).getUsername();
                    final String image = currentList.get(position).getProfile_pic();
                    overflow2.triggerSlide();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ((HomeActivity) mContext).myAddFragment(new OtherFeedCollectionFragment(overflow2, other_user_id, other_user_name, image));
                        }
                    }, 800);
                }
            });
            inspirationlist.setAdapter(inspirationAdapter);
            inspirationAdapter.notifyDataSetChanged();
        } else if (inspirationAdapter != null) {
            inspirationAdapter.setData(currentList);
            inspirationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (!isFirstTime) {
            inspirationlist.removeFooterView(loaderView);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        isLoading = !isLoading;
        if (object != null) {
            InspirationFeedRes response = (InspirationFeedRes) object;
            AlertUtils.showToast(mContext, response.getMessage());
        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }

    @Override
    public void refreshData(Bundle bundle) {
    }

    private void showReloadOption() {
        showDataNotFound();
        TextView textView = getDataNotFound();
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((HomeActivity) mContext).checkInternet())
                        getInspirationFeedList();
                }
            });
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hide) {
        if (inspirationAdapter != null)
            inspirationAdapter.notifyDataSetChanged();
    }

    public void removePost(final String inspiration_id, final String user_id) {
        this.inspiration_id = inspiration_id;
        this.user_id = user_id;

        deletePostApi = new DeletePostApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object.toString().equals(Constants.WebConstants.SUCCESS_CODE)) {
                    for (int i = 0; i < product_list.size(); i++) {
                        if (inspiration_id.equals(product_list.get(i).getInspiration_id())) {
                            product_list.remove(i);
                        }
                    }
                    inspirationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
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

    public void setInitialSearchText() {
        searchText.setText("");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.orders_layout:
                highlight4.setVisibility(View.VISIBLE);
                highlight5.setVisibility(View.INVISIBLE);
                highlight6.setVisibility(View.INVISIBLE);
                ((HomeActivity) mContext).myAddFragment(new HistoryOrdersFragment(overflow2));
                break;

            case R.id.cards_layout:
                highlight4.setVisibility(View.INVISIBLE);
                highlight6.setVisibility(View.INVISIBLE);
                highlight5.setVisibility(View.VISIBLE);
                ((HomeActivity) mContext).myAddFragment(new WalletCardFragment());
                break;

            case R.id.cart_layout2:
                tab_layout2.setVisibility(View.GONE);
                tab_layout1.setVisibility(View.VISIBLE);
                overflow2.triggerClose();
                break;

            case R.id.backIconLayout:
                Log.e("CurrentActivity", "" + ((HomeActivity) mContext).getCurrentFragment().getClass());
                if (((HomeActivity) mContext).getCurrentFragment() == null || ((HomeActivity) mContext).getCurrentFragment() instanceof CartListFragment) {
                    overflow2.triggerClose();
                }
                ((HomeActivity) mContext).onBackPressed();
                break;

            case R.id.cart_layout:
                tab_layout1.setVisibility(View.GONE);
                tab_layout2.setVisibility(View.VISIBLE);
                if (!overflow2.isOpen())
                    overflow2.triggerSlide();
                ((HomeActivity) mContext).myAddFragment(new CartListFragment(overflow2));
                break;

            case R.id.invite_text:
                break;
            case R.id.image_upload_post:
                bottomLayout.setBackgroundResource(R.drawable.topdrawer2);
                image_upload_post.setVisibility(View.GONE);
                image_camera.setVisibility(View.GONE);
                image_upload_post2.setVisibility(View.VISIBLE);
                uploadChoice.setVisibility(View.VISIBLE);
                break;

            case R.id.image_upload_post2:
                image_upload_post2.setVisibility(View.GONE);
                image_upload_post.setVisibility(View.VISIBLE);
                uploadChoice.setVisibility(View.INVISIBLE);
                image_camera.setVisibility(View.VISIBLE);
                bottomLayout.setBackgroundResource(0);
                break;

            case R.id.image_camera:
                image_camera.setVisibility(View.GONE);
                image_camera2.setVisibility(View.VISIBLE);
                ((HomeActivity) mContext).checkPermissions();
                break;

            case R.id.image_camera2:
                image_camera2.setVisibility(View.GONE);
                image_camera.setVisibility(View.VISIBLE);
                break;

            case R.id.fashioniconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                fashionList.clear();
                if (index != 1) {
                    fashioniconlayout.setBackgroundResource(R.drawable.tabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayFashionFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.beautyiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                beautyList.clear();
                if (index != 9) {
                    beautyiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayBeautyFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.fitnessiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                fitnessList.clear();
                if (index != 8) {
                    fitnessiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayFitnessFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.infoicon2:
                overflow2.setOpen();
                ((HomeActivity) mContext).myAddFragment(new MyFeedCollectionFragment(overflow2, false, isOnFeedPage));
                break;

            case R.id.techiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                techList.clear();
                if (index != 7) {
                    techiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayTechFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.photographyiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                photoList.clear();
                if (index != 6) {
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayPhotographyFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.homeiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                homeList.clear();
                if (index != 5) {
                    homeiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayHomeFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.occasioniconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                occasioList.clear();
                if (index != 4) {
                    occasioniconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayOccasionFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.traveliconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                travelList.clear();
                if (index != 3) {
                    traveliconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayTravelFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;

            case R.id.foodiconlayout:
                isLoading = false;
                inspirationlist.setSelection(0);
                page = 0;
                foodList.clear();
                if (index != 2) {
                    foodiconlayout.setBackgroundResource(R.drawable.tabborder);
                    fashioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    beautyiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    fitnessiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    techiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    photographyiconlayoutlayout.setBackgroundResource(R.drawable.whitetabborder);
                    homeiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    occasioniconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    traveliconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    displayFoodFeed();
                } else {
                    index = 0;
                    product_list.clear();
                    foodiconlayout.setBackgroundResource(R.drawable.whitetabborder);
                    getInspirationFeedList();
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
            CommonUtility.hideSoftKeyboard(mContext);
            searchText.setCursorVisible(false);
            original_text = searchText.getText().toString().trim();
            if (!original_text.equals("")) {
                Log.e("SearchProduct", "on editor action");

                ((HomeActivity) mContext).myAddFragment(new SearchProductFragment(overflow2, fragmentInspirationSection, original_text, false));

//need to change!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                if (indexSearch == 2) {
//                    storeGridAdapter = null;
//                    shop_list_isloding = false;
//                    page5 = 0;
//                    interestShopList.clear();
//                    isFirstTime_shop_list = true;
//
//                    displaySearchShopResult();
//                }
//                if (indexSearch == 3) {
//                    product_list_isloding = false;
//                    page6 = 0;
//                    interestProductList.clear();
//                    isFirstTime_product_list = true;
//                    products_filter_layout.setVisibility(View.GONE);
//                    products_layout.setVisibility(View.GONE);
//                    products_result_layout.setVisibility(View.VISIBLE);
//                    displaySearchProductResult();
//                } else {
//                    interestPeopleListAdapter = null;
//                    user_list_isloding = false;
//                    page4 = 0;
//                    interestUserList.clear();
//                    isFirstTime_user_list = true;
//                    displaySearchPeopleResult();
//                }
            } else {
                Toast.makeText(mContext, "Please enter keywords", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        UserPreference.getInstance().setSearchIndex(0);
        aa = new RotateAnimation(0.0f, 180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        aa.setRepeatCount(0);
        aa.setFillAfter(true);
        aa.setDuration(400);
        bb = new RotateAnimation(180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        bb.setRepeatCount(0);
        bb.setFillAfter(true);
        bb.setDuration(400);
        imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        tab_layout1 = (RelativeLayout) mainView.findViewById(R.id.tabLayout1);
        tab_layout2 = (RelativeLayout) mainView.findViewById(R.id.tabLayout2);
        inspirationlist = (ListView) mainView.findViewById(R.id.inspirationlist);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        loadingTextView.setTypeface(FontUtility.setMontserratLight(mContext));
        orders_text = (TextView) mainView.findViewById(R.id.orders_text);
        orders_text.setTypeface(FontUtility.setMontserratLight(mContext));
        cards_text = (TextView) mainView.findViewById(R.id.cards_text);
        cards_text.setTypeface(FontUtility.setMontserratLight(mContext));
        credits_text = (TextView) mainView.findViewById(R.id.credits_text);
        credits_text.setTypeface(FontUtility.setMontserratLight(mContext));
        image_upload_post = (ImageView) mainView.findViewById(R.id.image_upload_post);
        image_upload_post2 = (ImageView) mainView.findViewById(R.id.image_upload_post2);
        uploadChoice = (RelativeLayout) mainView.findViewById(R.id.uploadChoice);
        image_camera = (ImageView) mainView.findViewById(R.id.image_camera);
        image_camera2 = (ImageView) mainView.findViewById(R.id.image_camera);
        bottomLayout = (RelativeLayout) mainView.findViewById(R.id.bottomLayout);
        profile_tab = (LinearLayout) mainView.findViewById(R.id.profile_tab);
        profile_pic = (CircleImageView) mainView.findViewById(R.id.profile_pic);
        searchText = (EditText) mainView.findViewById(R.id.search_tab_text);
        searchText.setTypeface(FontUtility.setMontserratLight(mContext));
        highlight4 = (View) mainView.findViewById(R.id.highlight4);
        highlight5 = (View) mainView.findViewById(R.id.highlight5);
        highlight6 = (View) mainView.findViewById(R.id.highlight6);
        searchText.setOnEditorActionListener(this);
        searchText.setCursorVisible(false);
        searchText.setFocusable(false);
        searchText.addTextChangedListener(new MyTextWatcher());
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    String search_text = searchText.getText().toString().trim();
                    Log.e("SearchProduct", "down");

                    ((HomeActivity) mContext).myAddFragment(new SearchProductFragment(overflow2, fragmentInspirationSection, search_text, false));
//                    searchText.setText("");
                    return true;
                }
                return false;
            }
        });

        searchText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (overflow2.isOpen()) {
                    searchText.setCursorVisible(true);
                    searchText.setFocusable(true);
                    searchText.setFocusableInTouchMode(true);
                    searchText.requestFocus();
                }
                if (!overflow2.isOpen()) {
                    if (searchProductFragment == null) {
                        searchProductFragment = new SearchProductFragment(overflow2, fragmentInspirationSection, "", true, 0);
                        ((HomeActivity) mContext).myAddFragment(searchProductFragment);
                    } else {
//                        mContext.getSupportFragmentManager().findFragmentByTag(searchProductFragment.toString()).onResume();
                        searchProductFragment.onResume();
                    }
//                    ((HomeActivity) mContext).myAddFragment(new SearchProductFragment(overflow2, fragmentInspirationSection, "", true, 0));
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Log.e("SearchProduct", "1" + "overflow2 not open");
                            searchText.setCursorVisible(true);
                            searchText.setFocusable(true);
                            searchText.setFocusableInTouchMode(true);
                            searchText.requestFocus();
                        }
                    }, 0);
                    overflow2.triggerSlide();
                }
//                else if (MotionEvent.ACTION_DOWN == event.getAction()) {
//                    String search_text = searchText.getText().toString().trim();
//                    Log.e("SearchProduct", "down");
//
//                    ((HomeActivity) mContext).myAddFragment(new SearchProductFragment(overflow2, fragmentInspirationSection, search_text, false));
//                    searchText.setText("");
//                }
                return false;
            }
        });
        backIconLayout = (LinearLayout) mainView.findViewById(R.id.backIconLayout);
        cart_layout2 = (LinearLayout) mainView.findViewById(R.id.cart_layout2);
        orders_layout = (RelativeLayout) mainView.findViewById(R.id.orders_layout);
        cards_layout = (RelativeLayout) mainView.findViewById(R.id.cards_layout);
        credits_layout = (RelativeLayout) mainView.findViewById(R.id.credits_layout);
        cart_tab = (LinearLayout) mainView.findViewById(R.id.cart_layout);
        fashioniconlayout = (LinearLayout) mainView.findViewById(R.id.fashioniconlayout);
        beautyiconlayout = (LinearLayout) mainView.findViewById(R.id.beautyiconlayout);
        fitnessiconlayout = (LinearLayout) mainView.findViewById(R.id.fitnessiconlayout);
        foodiconlayout = (LinearLayout) mainView.findViewById(R.id.foodiconlayout);
        techiconlayout = (LinearLayout) mainView.findViewById(R.id.techiconlayout);
        photographyiconlayoutlayout = (LinearLayout) mainView.findViewById(R.id.photographyiconlayout);
        homeiconlayout = (LinearLayout) mainView.findViewById(R.id.homeiconlayout);
        occasioniconlayout = (LinearLayout) mainView.findViewById(R.id.occasioniconlayout);
        traveliconlayout = (LinearLayout) mainView.findViewById(R.id.traveliconlayout);
        swipeLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_container);
        overflow2 = (MyMaterialContentOverflow3) mainView.findViewById(R.id.overflow2);
        overflow2.setOnCloseListener(new MyMaterialContentOverflow3.OnCloseListener() {
            @Override
            public void onClose() {
                tab_layout2.setVisibility(View.GONE);
                tab_layout1.setVisibility(View.VISIBLE);
            }
        });
        Tracker t = ((KikrApp) mContext.getApplication()).getTracker(KikrApp.TrackerName.APP_TRACKER);
        t.setScreenName("Fragment Inspiration Section");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        if (isFromNotification == true) {
            ((HomeActivity) mContext).myAddFragment(new NotificationListFragment(overflow2, true));
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    overflow2.triggerSlide();
                }
            }, 1300);
        }
    }

    private void displayFashionFeed() {
        index = 1;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "fashion,style,sexy");
        inspirationFeedApi.execute();
    }

    private void displayFoodFeed() {
        index = 2;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "dish,delicious,yum,food");
        inspirationFeedApi.execute();
    }

    private void displayTravelFeed() {
        index = 3;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "travel,travels,traveling");
        inspirationFeedApi.execute();
    }

    private void displayOccasionFeed() {
        index = 4;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "celebrate, friends, occasion");
        inspirationFeedApi.execute();
    }

    private void displayHomeFeed() {
        index = 5;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "home,furniture,kitchen");
        inspirationFeedApi.execute();
    }

    private void displayPhotographyFeed() {
        index = 6;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "camera,lens, photo");
        inspirationFeedApi.execute();
    }

    private void displayTechFeed() {
        index = 7;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "music,headphones,iphone,electronic");
        inspirationFeedApi.execute();
    }

    private void displayFitnessFeed() {
        index = 8;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "fitness,fitnessflatlay,fitnessfirl");
        inspirationFeedApi.execute();
    }

    private void displayBeautyFeed() {
        index = 9;
        isLoading = !isLoading;
        if (!isFirstTime) {
            loadingTextView.setVisibility(View.VISIBLE);
        } else {
            loadingTextView.setVisibility(View.INVISIBLE);
        }
        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
        inspirationFeedApi.searchFeedByCategory(userId, String.valueOf(page), "beauty,beautiful,makeup");
        inspirationFeedApi.execute();
    }

    @Override
    public void setClickListener() {
        image_upload_post.setOnClickListener(this);
        image_upload_post2.setOnClickListener(this);
        image_camera.setOnClickListener(this);
        tab_layout1.setOnClickListener(this);
        profile_tab.setOnClickListener(this);
        profile_tab.setOnClickListener(this);
        orders_layout.setOnClickListener(this);
        credits_layout.setOnClickListener(this);
        cards_layout.setOnClickListener(this);
        cart_layout2.setOnClickListener(this);
        cart_tab.setOnClickListener(this);
        fashioniconlayout.setOnClickListener(this);
        photographyiconlayoutlayout.setOnClickListener(this);
        traveliconlayout.setOnClickListener(this);
        beautyiconlayout.setOnClickListener(this);
        techiconlayout.setOnClickListener(this);
        homeiconlayout.setOnClickListener(this);
        backIconLayout.setOnClickListener(this);
        foodiconlayout.setOnClickListener(this);
        fitnessiconlayout.setOnClickListener(this);
        occasioniconlayout.setOnClickListener(this);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) mContext).myAddFragment(new MyFeedCollectionFragment(overflow2, false, isOnFeedPage));
                if (overflow2.isOpen()) {

                } else {

                    overflow2.triggerSlide();
                }
            }
        });
    }

    @Override
    public void setData(Bundle bundle) {
        if (isFirstTimeFromMain) initData();

        inspirationlist.setOnItemClickListener(this);
        inspirationlist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                FragmentInspirationSection.this.firstVisibleItem = firstVisibleItem;
                FragmentInspirationSection.this.visibleItemCount = visibleItemCount;
                FragmentInspirationSection.this.totalItemCount = totalItemCount;
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page++;
                        isFirstTime = false;
                        if (index == 0) getInspirationFeedList();
                        else if (index == 1) displayFashionFeed();
                        else if (index == 9) displayBeautyFeed();
                        else if (index == 8) displayFitnessFeed();
                        else if (index == 7) displayTechFeed();
                        else if (index == 6) displayPhotographyFeed();
                        else if (index == 5) displayHomeFeed();
                        else if (index == 4) displayOccasionFeed();
                        else if (index == 3) displayTravelFeed();
                        else if (index == 2) displayFoodFeed();
                    } else {
                    }
                }
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadingTextView.setVisibility(View.VISIBLE);
                page = 0;
                currentList.clear();
                getInspirationFeedList();
                if (swipeLayout.isRefreshing()) {
                    swipeLayout.setRefreshing(false);
                    loadingTextView.setVisibility(View.INVISIBLE);
                }

            }
        });
        if (UserPreference.getInstance().getProfilePic() != null && UserPreference.getInstance().getProfilePic().length() > 0)
            CommonUtility.setImage(mContext, UserPreference.getInstance().getProfilePic(), profile_pic, R.drawable.profile_icon);
        else
            CommonUtility.setImage(mContext, UserPreference.getInstance().getProfilePic(), profile_pic, R.drawable.profile_icon);
    }

    protected RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String text = searchText.getText().toString();
            if (text == null || text.length() == 0) {
                Log.e("SearchProduct", "text_change" + UserPreference.getInstance().getSearchIndex());
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
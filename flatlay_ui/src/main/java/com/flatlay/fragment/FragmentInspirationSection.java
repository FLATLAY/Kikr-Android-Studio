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
    //    private double TAX = 20.0;
    private static String purchaseId;
    //    private double lowprice = 0, highprice = Double.MAX_VALUE;
    private String inspiration_id, userId, user_id, original_text = "";
    private EditText searchText;
    private List<Inspiration> product_list = new ArrayList<Inspiration>();
    private boolean firstTimeGetCard = true;
    private TextView
            enter_address_text2, credit_debit_text, shipping_address2_text, orders_text, cards_text,
            credits_text,
            loadingTextView;
    private boolean isFromNotification = false, isViewAll,
            isLoading = false, isFirstTime_product_list = true, isFirstTime = true, isFirstTimeFromMain = false, isOnFeedPage = true;
    private int other_page_feed = 0, other_firstVisibleItem1 = 0, other_visibleItemCount1 = 0,
            other_totalItemCount1 = 0, other_page_coll = 0, page = 0, page2 = 0, page8 = 0, index = 0,
            firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0, firstVisibleItem3 = 0, visibleItemCount3 = 0,
            totalItemCount3 = 0, firstVisibleItem6 = 0,
            visibleItemCount6 = 0, totalItemCount6 = 0, firstVisibleItem7 = 0, visibleItemCount7 = 0,
            totalItemCount7 = 0;
    private DeletePostApi deletePostApi;
    private Animation aa, bb;
    public static final String TAG = "FragmentInsSec";
    //    private List<CollectionList> other_collectionLists2;
    private ImageView image_upload_post,
            image_upload_post2, image_camera, image_camera2, bell_image;
    private RelativeLayout credits_layout, cards_layout, orders_layout,
            tab_layout1, tab_layout2, bottomLayout, uploadChoice;
    private MyMaterialContentOverflow3 overflow2;
    private CircleImageView profile_pic;
    private SwipeRefreshLayout swipeLayout;
    //    private ScrollView cards_list_view;
    private LinearLayout cart_layout2,
            backIconLayout, fashioniconlayout, beautyiconlayout, fitnessiconlayout, foodiconlayout,
            techiconlayout, photographyiconlayoutlayout, homeiconlayout, occasioniconlayout, traveliconlayout,
            blanklayout, cart_tab, search_tab, profile_tab;
    //    private Button other_button11, other_button22;
    private InspirationAdapter inspirationAdapter;
    //    private FragmentProfileCollectionAdapter other_collectionAdapter;
    //    private FragmentProfileView fragmentProfileView;
//    private InspirationGridAdapter other_inspirationGridAdapter;
    //    private ProductDetailGridAdapter productDetailGridAdapter;
//    private ProductSearchGridAdapter productSearchdAdapter;
    //    private List<UserData> userDetails;
//    private List<ProductResult> interestProductList = new ArrayList<>(), temporaryProoducts = new ArrayList<>();
    //    private ShareProfileDialog shareProfileDialog;
    private InputMethodManager imm;
    //    private List<TextView> group1, group2;
//    private List<UserResult> interestUserList = new ArrayList<>();
//    private List<FeaturedTabData> interestList = new ArrayList<>();
//    private CustomizeInterestPeopleListAdapter interestPeopleListAdapter;
//    private FeaturedTabAdapter featuredTabAdapter;
//    private List<BrandResult> interestShopList = new ArrayList<>();
//    private StoreGridAdapter storeGridAdapter;
//    private AllBrandGridAdapter allBrandGridAdapter;
//    private List<BrandList> brandLists = new ArrayList<>();
    private FragmentInspirationSection fragmentInspirationSection;
    private List<Inspiration> fashionList = new ArrayList<>(), beautyList = new ArrayList<>(),
            fitnessList = new ArrayList<>(), foodList = new ArrayList<>(), travelList = new ArrayList<>(),
            techList = new ArrayList<>(), photoList = new ArrayList<>(), homeList = new ArrayList<>(),
            occasioList = new ArrayList<>(), currentList = new ArrayList<>();

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
//                    if (currentlayout != null)
//                        currentlayout.setVisibility(View.GONE);
//                    if (currentlayout2 != null)
//                        currentlayout2.setVisibility(View.GONE);
                    overflow2.triggerSlide();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ((HomeActivity) mContext).myAddFragment(new OtherFeedCollectionFragment(overflow2, other_user_id, other_user_name, image));

//                            if (!other_user_id.equals(otherUserId)) {
//                                otherUserId = other_user_id;
//                                is_other_FirstTime_feed = true;
//                                isother_Loading_feed = false;
//                                page7 = 0;
//                                other_product_list.clear();
//                                CommonUtility.setImage(mContext, image, other_profile_pic, R.drawable.profile_icon);
//                                other_nameText.setText(other_user_name);
//
//                                other_button22.setTextColor(Color.BLACK);
//                                other_button11.setTextColor(Color.WHITE);
//                                other_button22.setBackgroundResource(R.drawable.white_button_noborder);
//                                other_button11.setBackgroundResource(R.drawable.green_corner_button);
//                                getOtherInspirationFeedList(other_user_id);
//                                getOtherCollectionList(other_user_id);
//                                final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
//                                    @Override
//                                    public void handleOnSuccess(Object object) {
//                                        MyProfileRes myProfileRes = (MyProfileRes) object;
//                                        other_followersLists = myProfileRes.getFollowers_list();
//                                        other_followingLists = myProfileRes.getFollowing_list();
//                                        other_followertext1.setText(String.valueOf(other_followingLists.size()));
//                                        other_followingtext1.setText(String.valueOf(other_followersLists.size()));
//                                    }
//
//                                    @Override
//                                    public void handleOnFailure(ServiceException exception, Object object) {
//
//                                    }
//                                });
//                                myProfileApi.getUserProfileDetail(other_user_id, UserPreference.getInstance().getUserID());
//                                myProfileApi.execute();
//                            }
//                            overflow_layout5.setVisibility(View.VISIBLE);
//                            currentlayout = overflow_layout5;
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

//    public void updateFilterProducts(String category, String filter2, String filter3, double lowprice, double highprice) {
//        temporaryProoducts = buttonPriceFilter(interestProductList, category, filter2, filter3, lowprice, highprice);
//        if (productSearchdAdapter != null) {
//            productSearchdAdapter.setData(temporaryProoducts);
//            productSearchdAdapter.notifyDataSetChanged();
//        }
//    }

//    @Override
//    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
//        if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
//            CommonUtility.hideSoftKeyboard(mContext);
//            searchText.setCursorVisible(false);
//            original_text = searchText.getText().toString().trim();
//            if (!original_text.equals("")) {
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
//            } else {
//                Toast.makeText(mContext, "Please enter keywords", Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        }
//        return false;
//    }

//    public void getAuthTocken() {
//        boolean condition1 = UserPreference.getInstance().getAuthTimeStamp() == 0;
//        boolean condition2 = System.currentTimeMillis() > (UserPreference.getInstance().getAuthTimeStamp() + UserPreference.getInstance().getAuthExpireTime());
//        if (condition1 || condition2) {
//            if (checkInternet()) {
//                OAuthWebService authWebService = new OAuthWebService(Constants.WebConstants.PAYPAL_AUTH_URL, new ServiceCallback() {
//                    @Override
//                    public void handleOnSuccess(Object object) {
//                        try {
//                            JSONObject jsonObj = new JSONObject(object.toString());
//                            String access_token = jsonObj.getString("access_token");
//                            int expires_in = jsonObj.getInt("expires_in");
//                            UserPreference.getInstance().setExpireTime(expires_in * 1000);
//                            UserPreference.getInstance().setPaypalAccessToken(access_token);
//                            UserPreference.getInstance().setTimeStamp(System.currentTimeMillis());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void handleOnFailure(ServiceException exception, Object object) {
//                        if (object != null)
//                            AlertUtils.showToast(mContext, object.toString());
//                    }
//                });
//                authWebService.execute();
//            }
//        }
//    }

    public void setInitialSearchText() {
        searchText.setText("");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.add_new_address_text:
//                if (new_address_layout.getVisibility() == View.GONE) {
//                    new_address_layout.setVisibility(View.VISIBLE);
//                    add_new_address_text.setText("Cancel Adding New Address");
//                } else {
//                    new_address_layout.setVisibility(View.GONE);
//                    add_new_address_text.setText("Add New Address");
//                }
//                break;
//            case R.id.save_address_button:
//                addNewAddress();
//                break;
//            case R.id.fillButton:
//                overflow2.triggerClose();
//                break;
//            case R.id.check_icon:
//
//                break;
//            case R.id.save_card_button:
//                if (isValidPayment2) {
//                    getAuthTocken();
//                    addNewCard(newCard2.getCard_number(), newCard2.getName_on_card(), newCard2.getExpirationMonth() + "/" + newCard2.getExpirationYear(), newCard2.getCvv(), newCard2.getCardtype());
//                } else
//                    AlertUtils.showToast(mContext, "Please enter required information.");
//                break;
//            case R.id.add_paypal_layout:
//                ComingSoonDialog comingSoonDialog = new ComingSoonDialog(mContext);
//                comingSoonDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
//                comingSoonDialog.show();
//                break;
//            case R.id.add_card_done_button:
//                if (isValidPayment) {
//                    getAuthTocken();
//                    add_card_done_button.setVisibility(View.GONE);
//                    validateCard(newCard, billlingAddress);
//                } else
//                    AlertUtils.showToast(mContext, "Please enter required information.");
//                break;
//            case R.id.place_order_button:
//                String msg = "Order confirmed?";
//                OrderProcessingDialog orderProcessingDialog = new OrderProcessingDialog(mContext, msg, new OrderProcessingDialog.MyListener() {
//                    @Override
//                    public void onClickButton() {
//                        getCartId();
//                    }
//                });
//                orderProcessingDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
//                orderProcessingDialog.show();
//                break;
//            case R.id.same_as_icon:
//                if (billing_layout.getVisibility() == View.GONE) {
//                    same_as_icon.setImageResource(R.drawable.box_default);
//                    billing_layout.setVisibility(View.VISIBLE);
//                } else {
//                    same_as_icon.setImageResource(R.drawable.checkmark_white);
//                    billing_layout.setVisibility(View.GONE);
//                }
//                break;
//            case R.id.next_button2:
//                if (isValidBillingAddress && isValidPayment) {
//                    final_name_text.setText(shippingAddress.getFirstname() + " " + shippingAddress.getLastname());
//                    final_address_text1.setText(shippingAddress.getStreet_address() + ", " + shippingAddress.getApartment());
//                    final_address_text2.setText(shippingAddress.getCity_town() + ", " + shippingAddress.getState() + " " + shippingAddress.getZip_code());
//                    final_address_text3.setText(shippingAddress.getCountry());
//                    if (currentlayout2 != null)
//                        currentlayout2.setVisibility(View.GONE);
//                    getEstimate();
//                    overflow_layout14.setVisibility(View.VISIBLE);
//                    currentlayout2 = overflow_layout14;
//                } else {
//                    AlertUtils.showToast(mContext, "Please enter required information.");
//                }
//                break;
//            case R.id.next_button:
//                if (isValidAddress) {
//                    if (currentlayout2 != null)
//                        currentlayout2.setVisibility(View.GONE);
//                    overflow_layout13.setVisibility(View.VISIBLE);
//                    currentlayout2 = overflow_layout13;
//                } else {
//                    AlertUtils.showToast(mContext, "Please enter required information.");
//                }
//                break;
//            case R.id.checkoutButton:
//                getAddressList();
//                if (currentlayout2 != null)
//                    currentlayout2.setVisibility(View.GONE);
//                overflow_layout12.setVisibility(View.VISIBLE);
//                currentlayout2 = overflow_layout12;
//                break;
//            case R.id.add_text:
//                if (enter_promo_text.getVisibility() == View.GONE) {
//                    add_text.setText("Close");
//                    enter_promo_text.setVisibility(View.VISIBLE);
//                } else {
//                    add_text.setText("Add");
//                    enter_promo_text.setVisibility(View.GONE);
//                }
//                break;

//            case R.id.cancel_text:
//                if (currentlayout2 != null)
//                    currentlayout2.setVisibility(View.GONE);
//                overflow_layout10.setVisibility(View.VISIBLE);
//                currentlayout2 = overflow_layout10;
//                break;
//            case R.id.flatlay_card2:
//                flatlay_card.setVisibility(View.VISIBLE);
//                flatlay_card2.setVisibility(View.GONE);
//                break;
//            case R.id.flatlay_card:
//                flatlay_card.setVisibility(View.GONE);
//                flatlay_card2.setVisibility(View.VISIBLE);
//                break;
//            case R.id.credit_debit_layout:
//                if (currentlayout2 != null)
//                    currentlayout2.setVisibility(View.GONE);
//                overflow_layout11.setVisibility(View.VISIBLE);
//                currentlayout2 = overflow_layout11;
//                break;
//            case R.id.new_payment_text:
//                if (currentlayout2 != null)
//                    currentlayout2.setVisibility(View.GONE);
//                overflow_layout10.setVisibility(View.VISIBLE);
//                currentlayout2 = overflow_layout10;
//                break;
//            case R.id.new_payment_text2:
//                if (currentlayout2 != null)
//                    currentlayout2.setVisibility(View.GONE);
//                overflow_layout10.setVisibility(View.VISIBLE);
//                currentlayout2 = overflow_layout10;
//                break;
            case R.id.orders_layout:
                highlight4.setVisibility(View.VISIBLE);
                highlight5.setVisibility(View.INVISIBLE);
                highlight6.setVisibility(View.INVISIBLE);
                ((HomeActivity) mContext).myAddFragment(new HistoryOrdersFragment(overflow2));
//                if (currentlayout2 != null)
//                    currentlayout2.setVisibility(View.GONE);
//                overflow_layout8.setVisibility(View.VISIBLE);
//                currentlayout2 = overflow_layout8;
//                FragmentTransaction transaction = mContext.getSupportFragmentManager()
//                        .beginTransaction();
//                transaction.replace(R.id.frame_container2, new FragmentAllOrders(new FragmentAllOrders.OrderListener() {
//                    @Override
//                    public void onClickButton() {
//                        overflow2.triggerClose();
//                    }
//                }), null);
//                transaction.addToBackStack(null);
//                transaction.commit();
                break;
//            case R.id.credits_layout:
//                highlight4.setVisibility(View.INVISIBLE);
//                highlight6.setVisibility(View.VISIBLE);
//                highlight5.setVisibility(View.INVISIBLE);
//                if (currentlayout2 != null)
//                    currentlayout2.setVisibility(View.GONE);
//                overflow_layout12.setVisibility(View.VISIBLE);
//                currentlayout2 = overflow_layout12;
//                break;
            case R.id.cards_layout:
                highlight4.setVisibility(View.INVISIBLE);
                highlight6.setVisibility(View.INVISIBLE);
                highlight5.setVisibility(View.VISIBLE);
//                if (currentlayout2 != null)
//                    currentlayout2.setVisibility(View.GONE);
//                overflow_layout9.setVisibility(View.VISIBLE);
//                currentlayout2 = overflow_layout9;
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

//                if (currentlayout2 != null) {
//                    if (currentlayout2.equals(overflow_layout14)) {
//                        currentlayout2.setVisibility(View.GONE);
//                        overflow_layout13.setVisibility(View.VISIBLE);
//                        currentlayout2 = overflow_layout13;
//                    } else if (currentlayout2.equals(overflow_layout13)) {
//                        currentlayout2.setVisibility(View.GONE);
//                        overflow_layout12.setVisibility(View.VISIBLE);
//                        currentlayout2 = overflow_layout12;
//                    } else if (currentlayout2.equals(overflow_layout12)) {
//                        currentlayout2.setVisibility(View.GONE);
//                        overflow_layout6.setVisibility(View.VISIBLE);
//                        currentlayout2 = overflow_layout6;
//                    } else if (currentlayout2.equals(overflow_layout8)) {
//                        currentlayout2.setVisibility(View.GONE);
//                        overflow_layout6.setVisibility(View.VISIBLE);
//                        currentlayout2 = overflow_layout6;
//                    } else if (currentlayout2.equals(overflow_layout6)) {
//                        if (currentlayout == null) {
//                            overflow2.triggerClose();
//                        } else {
//                            currentlayout2.setVisibility(View.GONE);
//                            tab_layout2.setVisibility(View.GONE);
//                            tab_layout1.setVisibility(View.VISIBLE);
//                            currentlayout.setVisibility(View.VISIBLE);
//                        }
//                    }
//                } else if (currentlayout != null) {
//                    tab_layout2.setVisibility(View.GONE);
//                    tab_layout1.setVisibility(View.VISIBLE);
//                    currentlayout2.setVisibility(View.GONE);
//                    currentlayout.setVisibility(View.VISIBLE);
//                } else
//                    overflow2.triggerClose();
                break;
            case R.id.cart_layout:
                tab_layout1.setVisibility(View.GONE);
                tab_layout2.setVisibility(View.VISIBLE);
                if (!overflow2.isOpen())
                    overflow2.triggerSlide();
                ((HomeActivity) mContext).myAddFragment(new CartListFragment(overflow2));

//                if (currentlayout != null)
//                    currentlayout.setVisibility(View.GONE);
//                if (currentlayout2 != null)
//                    currentlayout2.setVisibility(View.VISIBLE);
//                if (currentlayout2 == null) {
//                    overflow_layout6.setVisibility(View.VISIBLE);
//                    currentlayout2 = overflow_layout6;
//
//                }
//                new Handler().postDelayed(new Runnable() {
//                    public void run() {
//                        getCartList();
//
//                    }
//                }, 700);
                //        if (firstTimeGetCard) {
//                    getKikrCredits();
//                    getCardList();
                //            firstTimeGetCard = false;
                //         }
                break;
//            case R.id.people_text_layout:
//                overflow2.setOpen();
//                searchText.setText("");
//                people_layout.setVisibility(View.VISIBLE);
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.GONE);
//                products_filter_layout.setVisibility(View.GONE);
//                shops_layout.setVisibility(View.GONE);
//                highlight1.setVisibility(View.VISIBLE);
//                highlight2.setVisibility(View.INVISIBLE);
//                highlight3.setVisibility(View.INVISIBLE);
//                indexSearch = 1;
//                ppsIndex = 0;
//                break;
//            case R.id.products_text_layout:
//                overflow2.setOpen();
//                ppsIndex = 1;
//                searchText.setText("");
//                people_layout.setVisibility(View.GONE);
//                products_layout.setVisibility(View.VISIBLE);
//                products_result_layout.setVisibility(View.GONE);
//                products_filter_layout.setVisibility(View.GONE);
//                shops_layout.setVisibility(View.GONE);
//                highlight1.setVisibility(View.INVISIBLE);
//                highlight2.setVisibility(View.VISIBLE);
//                highlight3.setVisibility(View.INVISIBLE);
//                indexSearch = 3;
//                break;
//            case R.id.shop_text_layout:
//                overflow2.setOpen();
//                ppsIndex = 2;
//                searchText.setText("");
//                people_layout.setVisibility(View.GONE);
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.GONE);
//                products_filter_layout.setVisibility(View.GONE);
//                shops_layout.setVisibility(View.VISIBLE);
//                highlight1.setVisibility(View.INVISIBLE);
//                highlight2.setVisibility(View.INVISIBLE);
//                highlight3.setVisibility(View.VISIBLE);
//                if (indexSearch != 2) displayAllShops();
//                indexSearch = 2;
//                break;
            case R.id.invite_text:
                break;
//            case R.id.clothing_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("clothing")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "clothing";
//                break;
//            case R.id.shoes_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//
//                if (productSearchdAdapter != null && !resultCategory.equals("shoe")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "shoe";
//                break;
//            case R.id.electronics_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("electronic")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "electronic";
//                break;
//            case R.id.jewelry_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("jewelry")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "jewelry";
//                break;
//            case R.id.health_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("health")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "health";
//                break;
//            case R.id.sports_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("sport")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "sport";
//                break;
//            case R.id.baby_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("baby")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "baby";
//                break;
//            case R.id.computer_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("computer")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "computer";
//                break;
//            case R.id.toy_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("toy")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "toy";
//                break;
//            case R.id.game_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("game")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "game";
//                break;
//            case R.id.pet_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("computer")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "computer";
//                break;
//            case R.id.music_layout:
//                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                if (productSearchdAdapter != null && !resultCategory.equals("music")) {
//                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
//                    productSearchdAdapter.notifyDataSetChanged();
//                } else if (productSearchdAdapter != null) {
//                    if (temporaryProoducts != null) {
//                        productSearchdAdapter.setData(temporaryProoducts);
//                        productSearchdAdapter.notifyDataSetChanged();
//                    }
//                }
//                category = "music";
//                break;
//            case R.id.women_text:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(men_text, accessories_text, sale_text));
//                group2 = new ArrayList<>(Arrays.asList(women_text));
//                setBackgroundByGroup(group1, R.drawable.white_button_noborder, group2, R.drawable.green_corner_button);
//                setTextColorByGroup(group1, Color.BLACK, group2, Color.WHITE);
//                filter2 = "women";
//                break;
//            case R.id.accessories_text:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(men_text, women_text, sale_text));
//                group2 = new ArrayList<>(Arrays.asList(accessories_text));
//                setBackgroundByGroup(group1, R.drawable.white_button_noborder, group2, R.drawable.green_corner_button);
//                setTextColorByGroup(group1, Color.BLACK, group2, Color.WHITE);
//                filter2 = "accesso";
//                break;
//            case R.id.men_text:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(accessories_text, women_text, sale_text));
//                group2 = new ArrayList<>(Arrays.asList(men_text));
//                setBackgroundByGroup(group1, R.drawable.white_button_noborder, group2, R.drawable.green_corner_button);
//                setTextColorByGroup(group1, Color.BLACK, group2, Color.WHITE);
//                filter2 = "men";
//                filter3 = "boy";
//                break;
//            case R.id.sale_text:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(accessories_text, women_text, men_text));
//                group2 = new ArrayList<>(Arrays.asList(sale_text));
//                setBackgroundByGroup(group1, R.drawable.white_button_noborder, group2, R.drawable.green_corner_button);
//                setTextColorByGroup(group1, Color.BLACK, group2, Color.WHITE);
//                filter2 = "sale";
//                break;
//            case R.id.price_text_0:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(price_text_25, price_text_50, price_text_100, price_text_150, price_text_200, price_text_500, price_text_750));
//                group2 = new ArrayList<>(Arrays.asList(price_text_0));
//                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
//                lowprice = 0;
//                highprice = 25;
//                break;
//            case R.id.price_text_25:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_50, price_text_100, price_text_150, price_text_200, price_text_500, price_text_750));
//                group2 = new ArrayList<>(Arrays.asList(price_text_25));
//                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
//                lowprice = 25;
//                highprice = 50;
//                break;
//            case R.id.price_text_50:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_100, price_text_150, price_text_200, price_text_500, price_text_750));
//                group2 = new ArrayList<>(Arrays.asList(price_text_50));
//                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
//                lowprice = 50;
//                highprice = 100;
//                break;
//            case R.id.price_text_100:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_150, price_text_200, price_text_500, price_text_750));
//                group2 = new ArrayList<>(Arrays.asList(price_text_100));
//                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
//                lowprice = 100;
//                highprice = 150;
//                break;
//            case R.id.price_text_150:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_100, price_text_200, price_text_500, price_text_750));
//                group2 = new ArrayList<>(Arrays.asList(price_text_150));
//                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
//                lowprice = 150;
//                highprice = 200;
//                break;
//            case R.id.price_text_200:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_100, price_text_150, price_text_500, price_text_750));
//                group2 = new ArrayList<>(Arrays.asList(price_text_200));
//                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
//                lowprice = 200;
//                highprice = 500;
//                break;
//            case R.id.price_text_500:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_100, price_text_150, price_text_200, price_text_750));
//                group2 = new ArrayList<>(Arrays.asList(price_text_500));
//                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
//                lowprice = 500;
//                highprice = 750;
//                break;
//            case R.id.price_text_750:
//                overflow2.setOpen();
//                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_100, price_text_150, price_text_200, price_text_500));
//                group2 = new ArrayList<>(Arrays.asList(price_text_750));
//                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
//                lowprice = 750;
//                highprice = Double.MAX_VALUE;
//                break;
//            case R.id.apply_text:
//                overflow2.setOpen();
//                products_filter_layout.setVisibility(View.GONE);
//                products_result_layout.setVisibility(View.VISIBLE);
//                updateFilterProducts(resultCategory, filter2, filter3, lowprice, highprice);
//                break;
//            case R.id.filter_icon:
//                overflow2.setOpen();
//                products_result_layout.setVisibility(View.GONE);
//                products_filter_layout.setVisibility(View.VISIBLE);
//                break;
//            case R.id.filter_icon2:
//                overflow2.setOpen();
//                products_result_layout.setVisibility(View.VISIBLE);
//                products_filter_layout.setVisibility(View.GONE);
//                break;
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
//                if (currentlayout != null)
//                    currentlayout.setVisibility(View.GONE);
//                overflow_layout1.setVisibility(View.VISIBLE);
//                currentlayout = overflow_layout1;
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

//            case R.id.bell_icon:
//                overflow2.setOpen();
//                if (currentlayout != null)
//                    currentlayout.setVisibility(View.GONE);
//                overflow_layout3.setVisibility(View.VISIBLE);
//                currentlayout = overflow_layout3;
//                getFollowingInstagramList();
//                break;

//            case R.id.info_icon:
//                overflow2.setOpen();
//                if (currentlayout != null)
//                    currentlayout.setVisibility(View.GONE);
//                overflow_layout2.setVisibility(View.VISIBLE);
//                currentlayout = overflow_layout2;
////                isInfo = true;
//                break;

//            case R.id.info_image2:
////                overflow2.setOpen();
////                if (currentlayout != null)
////                    currentlayout.setVisibility(View.GONE);
////                overflow_layout1.setVisibility(View.VISIBLE);
////                currentlayout = overflow_layout1;
////                isInfo = false;
//                break;

//            case R.id.text1:
//                overflow2.setOpen();
//                shareProfileDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
//                shareProfileDialog.show();
//                break;

//            case R.id.text2:
//                Bundle bundle = new Bundle();
//                bundle.putBoolean("is_edit_profile", true);
//                startActivity(EditProfileActivity.class, bundle);
//                break;

//            case R.id.text3:
//
////                overflow2.setOpen();
////                info_image.setImageResource(R.drawable.info_teal);
////                if (currentlayout != null)
////                    currentlayout.setVisibility(View.GONE);
////                overflow_layout1.setVisibility(View.VISIBLE);
////                currentlayout = overflow_layout1;
////                collAdapterIndex = 1;
////                feedAdapterIndex = 1;
////                performanceText.setVisibility(View.VISIBLE);
////                nameText.setVisibility(View.GONE);
////                myDetailProductList.clear();
////                page3 = 0;
////                getMyInspirationFeedList();
////                getCollectionList();
////                isInfo = true;
//                break;

//            case R.id.text4:
//                Bundle bundle2 = new Bundle();
//                bundle2.putBoolean("isFromHome", true);
//                startActivity(FollowCategoriesNewActivity.class, bundle2);
//                break;

//            case R.id.text5:
//                logoutUser();
//                mContext.finish();
//                break;
        }
    }

//    private Address newShippingAddress = new Address();

//    private String promoCode = "";
//    private boolean isValidPayment2 = false;
//    private BillingAddress billlingAddress = new BillingAddress();
//    private Card newCard2 = new Card();


//    private void saveCard() {
//        isValidPayment2 = true;
//        if (card_num_text2.getText().toString().trim().length() > 0) {
//            card_num_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//            newCard2.setCard_number(card_num_text2.getText().toString().trim());
//            newCard2.setCardtype(Luhn.getCardTypeinString(card_num_text2.getText().toString().trim()));
//        } else {
//            isValidPayment2 = false;
//            card_num_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//        }
//        if (name_card_text2.getText().toString().trim().length() <= 0) {
//            isValidPayment2 = false;
//            name_card_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//        } else {
//            name_card_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//            newCard2.setName_on_card(name_card_text2.getText().toString().trim());
//        }
//        String[] arr = expiration_text2.getText().toString().trim().split("/");
//        if (expiration_text2.getText().toString().trim().length() <= 0 || arr.length < 2) {
//            isValidPayment2 = false;
//            expiration_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//        } else {
//            expiration_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//            newCard2.setExpirationMonth(arr[0]);
//            if (arr[1].length() == 2)
//                newCard2.setExpirationYear("20" + arr[1]);
//            else
//                newCard2.setExpirationYear(arr[1]);
//        }
//        if (ccv_text2.getText().toString().trim().length() <= 0) {
//            isValidPayment2 = false;
//            ccv_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//        } else {
//            ccv_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//            newCard2.setCvv(ccv_text2.getText().toString().trim());
//        }
//    }

//    private void saveAddress() {
//        isValidAddress = true;
//        isValidPayment = true;
//        isValidBillingAddress = true;
//        if (new_address_layout.getVisibility() == View.VISIBLE) {
//            if (first_name.getText().toString().trim() != null && first_name.getText().toString().trim().length() > 0) {
//                shippingAddress.setFirstname(first_name.getText().toString().trim());
//                first_name.setBackgroundResource(R.drawable.topsearch_noborder);
//                cardAndShippingDetail.setShipping_first_name(shippingAddress.getFirstname());
//            } else {
//                isValidAddress = false;
//                first_name.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (last_name.getText().toString().trim() != null && last_name.getText().toString().trim().length() > 0) {
//                shippingAddress.setLastname(last_name.getText().toString().trim());
//                last_name.setBackgroundResource(R.drawable.topsearch_noborder);
//                cardAndShippingDetail.setShipping_last_name(shippingAddress.getLastname());
//            } else {
//                isValidAddress = false;
//                last_name.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (email.getText().toString().trim() != null && email.getText().toString().trim().length() > 0) {
//                this.emailString = email.getText().toString().trim();
//                cardAndShippingDetail.setEmail(emailString);
//            } else {
//                this.emailString = UserPreference.getInstance().getEmail();
//                cardAndShippingDetail.setEmail(emailString);
//            }
//            if (enter_address_text.getText().toString().trim() != null && enter_address_text.getText().toString().trim().length() > 0) {
//                this.addressLine1 = enter_address_text.getText().toString().trim();
//                enter_address_text.setBackgroundResource(R.drawable.topsearch_noborder);
//                shippingAddress.setStreet_address(addressLine1);
//                if (this.apartment.length() > 0)
//                    cardAndShippingDetail.setShipping_address(addressLine1 + ", " + this.apartment);
//                else
//                    cardAndShippingDetail.setShipping_address(addressLine1);
//            } else {
//                isValidAddress = false;
//                enter_address_text.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_apt_text.getText().toString().trim() != null && enter_apt_text.getText().toString().trim().length() > 0) {
//                this.apartment = enter_apt_text.getText().toString().trim();
//                shippingAddress.setApartment(apartment);
//                cardAndShippingDetail.setShipping_address(addressLine1 + ", " + this.apartment);
//            } else {
//            }
//            if (enter_country_text.getText().toString().trim() != null && enter_country_text.getText().toString().trim().length() > 0) {
//                shippingAddress.setCountry(enter_country_text.getText().toString().trim());
//                enter_country_text.setBackgroundResource(R.drawable.topsearch_noborder);
//                cardAndShippingDetail.setShipping_country(shippingAddress.getCountry());
//            } else {
//                isValidAddress = false;
//                enter_country_text.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_city_text.getText().toString().trim() != null && enter_city_text.getText().toString().trim().length() > 0) {
//                shippingAddress.setCity_town(enter_city_text.getText().toString().trim());
//                enter_city_text.setBackgroundResource(R.drawable.topsearch_noborder);
//                cardAndShippingDetail.setShipping_city(shippingAddress.getCity_town());
//            } else {
//                isValidAddress = false;
//                enter_city_text.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_zip_text.getText().toString().trim() != null && enter_zip_text.getText().toString().trim().length() > 0) {
//                shippingAddress.setZip_code(enter_zip_text.getText().toString().trim());
//                enter_zip_text.setBackgroundResource(R.drawable.topsearch_noborder);
//                cardAndShippingDetail.setShipping_zip(shippingAddress.getZip_code());
//            } else {
//                isValidAddress = false;
//                enter_zip_text.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_state_text.getText().toString().trim() != null && enter_state_text.getText().toString().trim().length() > 0) {
//                shippingAddress.setState(enter_state_text.getText().toString().trim());
//                enter_state_text.setBackgroundResource(R.drawable.topsearch_noborder);
//                cardAndShippingDetail.setShipping_state(shippingAddress.getState());
//            } else {
//                isValidAddress = false;
//                enter_state_text.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//        }
//        if (enter_promo_text.getText().toString().trim() != null && enter_promo_text.getText().toString().trim().length() > 0) {
//            this.promoCode = enter_promo_text.getText().toString().trim();
//        }
//        if (enter_card_num.getText().toString().trim() != null && enter_card_num.getText().toString().trim().length() > 0) {
//            enter_card_num.setBackgroundResource(R.drawable.topsearch_noborder);
//            cardAndShippingDetail.setCard_number(CommonUtility.DecryptCreditCard(enter_card_num.getText().toString().trim()));
//            newCard.setCard_number(enter_card_num.getText().toString().trim());
//            newCard.setCardtype(Luhn.getCardTypeinString(enter_card_num.getText().toString().trim()));
//        } else {
//            isValidPayment = false;
//            enter_card_num.setBackgroundResource(R.drawable.topsearch_red_border);
//        }
//        if (enter_name_on_card2.getVisibility() == View.VISIBLE && (enter_name_on_card2.getText().toString().trim() == null || enter_name_on_card2.getText().toString().trim().length() <= 0)) {
//            isValidPayment = false;
//            enter_name_on_card2.setBackgroundResource(R.drawable.topsearch_red_border);
//        } else {
//            enter_name_on_card2.setBackgroundResource(R.drawable.topsearch_noborder);
//            cardAndShippingDetail.setCard_name(enter_name_on_card2.getText().toString().trim());
//            newCard.setName_on_card(enter_name_on_card2.getText().toString().trim());
//        }
//        if (enter_expire_text2.getVisibility() == View.VISIBLE && (enter_expire_text2.getText().toString().trim() == null || enter_expire_text2.getText().toString().trim().length() <= 0)) {
//            isValidPayment = false;
//            enter_expire_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//        } else {
//            enter_expire_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//            String[] arr = enter_expire_text2.getText().toString().trim().split("/");
//            if (arr.length >= 2) {
//                cardAndShippingDetail.setExpiry_date_month(arr[0]);
//                newCard.setExpirationMonth(arr[0]);
//                if (arr[1].length() == 2) {
//                    cardAndShippingDetail.setExpiry_date_year("20" + arr[1]);
//                    newCard.setExpirationYear("20" + arr[1]);
//                } else {
//                    cardAndShippingDetail.setExpiry_date_year(arr[1]);
//                    newCard.setExpirationYear(arr[1]);
//                }
//            }
//        }
//        if (ccv_text3.getVisibility() == View.VISIBLE && (ccv_text3.getText().toString().trim() == null || ccv_text3.getText().toString().trim().length() <= 0)) {
//            isValidPayment = false;
//            ccv_text3.setBackgroundResource(R.drawable.topsearch_red_border);
//        } else {
//            ccv_text3.setBackgroundResource(R.drawable.topsearch_noborder);
//            cardAndShippingDetail.setCvv(ccv_text3.getText().toString().trim());
//            newCard.setCvv(ccv_text3.getText().toString().trim());
//        }
//        if (billing_layout.getVisibility() == View.GONE) {
//            isValidBillingAddress = true;
//            cardAndShippingDetail.setBilling_first_name(shippingAddress.getFirstname());
//            cardAndShippingDetail.setBilling_last_name(shippingAddress.getLastname());
//            if (this.apartment.length() > 0) {
//                cardAndShippingDetail.setBilling_address(addressLine1 + ", " + this.apartment);
//                billlingAddress.setLine1(addressLine1 + ", " + this.apartment);
//            } else {
//                cardAndShippingDetail.setBilling_address(addressLine1);
//                billlingAddress.setLine1(addressLine1);
//            }
//            cardAndShippingDetail.setBilling_country(shippingAddress.getCountry());
//            cardAndShippingDetail.setBilling_city(shippingAddress.getCity_town());
//            cardAndShippingDetail.setBilling_zip(shippingAddress.getZip_code());
//            cardAndShippingDetail.setBilling_state(shippingAddress.getState());
//            billlingAddress.setFirstName(shippingAddress.getFirstname());
//            billlingAddress.setLastName(shippingAddress.getLastname());
//            billlingAddress.setCity(shippingAddress.getCity_town());
//            billlingAddress.setCountry_code("US");
//            billlingAddress.setPostal_code(shippingAddress.getZip_code());
//            billlingAddress.setState(shippingAddress.getState());
//        } else {
//            if (billing_first_name.getText().toString().trim() != null && billing_first_name.getText().toString().trim().length() > 0) {
//                billing_first_name.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setFirstName(billing_first_name.getText().toString().trim());
//                cardAndShippingDetail.setBilling_first_name(billlingAddress.getFirstName());
//            } else {
//                isValidBillingAddress = false;
//                billing_first_name.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (billing_last_name.getText().toString().trim() != null && billing_last_name.getText().toString().trim().length() > 0) {
//                billing_last_name.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setLastName(billing_last_name.getText().toString().trim());
//                cardAndShippingDetail.setBilling_last_name(billlingAddress.getLastName());
//            } else {
//                isValidBillingAddress = false;
//                billing_last_name.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_country_text2.getText().toString().trim() != null && enter_country_text2.getText().toString().trim().length() > 0) {
//                enter_country_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setCountry_code("US");
//                cardAndShippingDetail.setBilling_country(billlingAddress.getCountry_code());
//            } else {
//                isValidBillingAddress = false;
//                enter_country_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_address_text2.getText().toString().trim() != null && enter_address_text2.getText().toString().trim().length() > 0) {
//                enter_address_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                this.billAddressLine1 = enter_address_text2.getText().toString().trim();
//                if (this.billApartment.length() > 0) {
//                    cardAndShippingDetail.setBilling_address(billAddressLine1 + ", " + this.billApartment);
//                    billlingAddress.setLine1(billAddressLine1 + ", " + this.billApartment);
//                } else {
//                    cardAndShippingDetail.setBilling_address(billAddressLine1);
//                    billlingAddress.setLine1(billAddressLine1);
//                }
//            } else {
//                isValidBillingAddress = false;
//                enter_address_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_apt_text2.getText().toString().trim() != null && enter_apt_text2.getText().toString().trim().length() > 0) {
//                this.billApartment = enter_apt_text2.getText().toString().trim();
//                cardAndShippingDetail.setBilling_address(billAddressLine1 + ", " + this.billApartment);
//                billlingAddress.setLine1(billAddressLine1 + ", " + this.billApartment);
//            } else {
//            }
//            if (enter_city_text2.getText().toString().trim() != null && enter_city_text2.getText().toString().trim().length() > 0) {
//                enter_city_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setCity(enter_city_text2.getText().toString().trim());
//                cardAndShippingDetail.setBilling_city(billlingAddress.getCity());
//            } else {
//                isValidBillingAddress = false;
//                enter_city_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_state_text2.getText().toString().trim() != null && enter_state_text2.getText().toString().trim().length() > 0) {
//                enter_state_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setState(enter_state_text2.getText().toString().trim());
//                cardAndShippingDetail.setBilling_state(billlingAddress.getState());
//            } else {
//                isValidBillingAddress = false;
//                enter_state_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_zip_text2.getText().toString().trim() != null && enter_zip_text2.getText().toString().trim().length() > 0) {
//                enter_zip_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setPostal_code(enter_zip_text2.getText().toString().trim());
//                cardAndShippingDetail.setBilling_zip(billlingAddress.getPostal_code());
//            } else {
//                isValidBillingAddress = false;
//                enter_zip_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//        }
//    }

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
//        shareProfileDialog = new ShareProfileDialog(mContext);
        tab_layout1 = (RelativeLayout) mainView.findViewById(R.id.tabLayout1);
        tab_layout2 = (RelativeLayout) mainView.findViewById(R.id.tabLayout2);
//        bell_icon = (RelativeLayout) mainView.findViewById(R.id.bell_icon);
//        info_icon = (RelativeLayout) mainView.findViewById(R.id.info_icon);
//        overflow_layout1 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout1);
//        overflow_layout2 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout2);
//        overflow_layout3 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout3);
//        overflow_layout4 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout4);
//        overflow_layout5 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout5);
//        overflow_layout6 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout6);
//        overflow_layout8 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout8);
//        overflow_layout9 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout9);
//        overflow_layout10 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout10);
//        overflow_layout11 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout11);
//        overflow_layout12 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout12);
//        overflow_layout13 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout13);
//        overflow_layout14 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout14);
//        add_paypal_layout = (RelativeLayout) mainView.findViewById(R.id.add_paypal_layout);
        inspirationlist = (ListView) mainView.findViewById(R.id.inspirationlist);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        loadingTextView.setTypeface(FontUtility.setMontserratLight(mContext));
//        other_followingtext1 = (TextView) mainView.findViewById(R.id.other_followingtext1);
//        other_followingtext1.setTypeface(FontUtility.setMontserratLight(mContext));
//        other_followertext2 = (TextView) mainView.findViewById(R.id.other_followertext2);
//        other_followertext2.setTypeface(FontUtility.setMontserratLight(mContext));
//        other_followertext1 = (TextView) mainView.findViewById(R.id.other_followertext1);
//        other_followertext1.setTypeface(FontUtility.setMontserratLight(mContext));
//        other_followingtext2 = (TextView) mainView.findViewById(R.id.other_followingtext2);
//        other_followingtext2.setTypeface(FontUtility.setMontserratLight(mContext));
        orders_text = (TextView) mainView.findViewById(R.id.orders_text);
        orders_text.setTypeface(FontUtility.setMontserratLight(mContext));
        cards_text = (TextView) mainView.findViewById(R.id.cards_text);
        cards_text.setTypeface(FontUtility.setMontserratLight(mContext));
        credits_text = (TextView) mainView.findViewById(R.id.credits_text);
        credits_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        learn_more_text = (TextView) mainView.findViewById(R.id.learn_more_text);
//        learn_more_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        learn_more_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        guarantee_text = (TextView) mainView.findViewById(R.id.guarantee_text);
//        guarantee_text.setTypeface(FontUtility.setMontserratRegular(mContext));
//        est_shipping_text3 = (TextView) mainView.findViewById(R.id.est_shipping_text3);
//        est_shipping_text3.setTypeface(FontUtility.setMontserratLight(mContext));
//        est_shipping_text2 = (TextView) mainView.findViewById(R.id.est_shipping_text2);
//        est_shipping_text2.setTypeface(FontUtility.setMontserratRegular(mContext));
//        total_item_count_text = (TextView) mainView.findViewById(R.id.total_item_count_text);
//        total_item_count_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        est_shipping_amount_text = (TextView) mainView.findViewById(R.id.est_shipping_amount_text);
//        est_shipping_amount_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        est_shipping_text = (TextView) mainView.findViewById(R.id.est_shipping_text);
//        est_shipping_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        tax_amount_text = (TextView) mainView.findViewById(R.id.tax_amount_text);
//        tax_amount_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        tax_text = (TextView) mainView.findViewById(R.id.tax_text);
//        tax_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        items_list_text = (TextView) mainView.findViewById(R.id.items_list_text);
//        items_list_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        new_payment_text = (TextView) mainView.findViewById(R.id.new_payment_text);
//        new_payment_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        cancel_text = (TextView) mainView.findViewById(R.id.cancel_text);
//        cancel_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        cancel_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        save_card_button = (TextView) mainView.findViewById(R.id.save_card_button);
//        save_card_button.setTypeface(FontUtility.setMontserratLight(mContext));
//        default_text = (TextView) mainView.findViewById(R.id.default_text);
//        default_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        expiration_text = (TextView) mainView.findViewById(R.id.expiration_text);
//        expiration_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        ccv_text = (TextView) mainView.findViewById(R.id.ccv_text);
//        ccv_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        card_num_text = (TextView) mainView.findViewById(R.id.card_num_text);
//        card_num_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        contact_text3 = (TextView) mainView.findViewById(R.id.contact_text3);
//        contact_text3.setTypeface(FontUtility.setMontserratLight(mContext));
//        add_card_done_button = (TextView) mainView.findViewById(R.id.add_card_done_button);
//        add_card_done_button.setTypeface(FontUtility.setMontserratLight(mContext));
//        looks_like_text = (TextView) mainView.findViewById(R.id.looks_like_text);
//        looks_like_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        earn_text = (TextView) mainView.findViewById(R.id.earn_text);
//        earn_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        empty_cart_text = (TextView) mainView.findViewById(R.id.empty_cart_text);
//        empty_cart_text.setTypeface(FontUtility.setMontserratRegular(mContext));
//        add_new_address_text = (TextView) mainView.findViewById(R.id.add_new_address_text);
//        add_new_address_text.setTypeface(FontUtility.setMontserratRegular(mContext));
//        save_address_button = (TextView) mainView.findViewById(R.id.save_address_button);
//        save_address_button.setTypeface(FontUtility.setMontserratRegular(mContext));
//        fillButton = (TextView) mainView.findViewById(R.id.fillButton);
//        fillButton.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_text9 = (TextView) mainView.findViewById(R.id.shipping_text9);
//        shipping_text9.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_text8 = (TextView) mainView.findViewById(R.id.shipping_text8);
//        shipping_text8.setTypeface(FontUtility.setMontserratLight(mContext));
//        confirm_text3 = (TextView) mainView.findViewById(R.id.confirm_text3);
//        confirm_text3.setTypeface(FontUtility.setMontserratLight(mContext));
//        email = (EditText) mainView.findViewById(R.id.email);
//        email.setTypeface(FontUtility.setMontserratLight(mContext));
//        contact_text2 = (TextView) mainView.findViewById(R.id.contact_text2);
//        contact_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_country_text = (EditText) mainView.findViewById(R.id.enter_country_text);
//        enter_country_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_address_text = (EditText) mainView.findViewById(R.id.enter_address_text);
//        enter_address_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_country_text2 = (EditText) mainView.findViewById(R.id.enter_country_text2);
//        enter_country_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_address_text2 = (EditText) mainView.findViewById(R.id.enter_address_text2);
//        enter_address_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_apt_text2 = (EditText) mainView.findViewById(R.id.enter_apt_text2);
//        enter_apt_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_city_text2 = (EditText) mainView.findViewById(R.id.enter_city_text2);
//        enter_city_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_state_text2 = (EditText) mainView.findViewById(R.id.enter_state_text2);
//        enter_state_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_zip_text2 = (EditText) mainView.findViewById(R.id.enter_zip_text2);
//        enter_zip_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        billing_last_name = (EditText) mainView.findViewById(R.id.billing_last_name);
//        billing_last_name.setTypeface(FontUtility.setMontserratLight(mContext));
//        billing_first_name = (EditText) mainView.findViewById(R.id.billing_first_name);
//        billing_first_name.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_apt_text = (EditText) mainView.findViewById(R.id.enter_apt_text);
//        enter_apt_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_city_text = (EditText) mainView.findViewById(R.id.enter_city_text);
//        enter_city_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_text7 = (TextView) mainView.findViewById(R.id.shipping_text7);
//        shipping_text7.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_text6 = (TextView) mainView.findViewById(R.id.shipping_text6);
//        shipping_text6.setTypeface(FontUtility.setMontserratLight(mContext));
//        confirm_text2 = (TextView) mainView.findViewById(R.id.confirm_text2);
//        confirm_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        payment_type_text = (TextView) mainView.findViewById(R.id.payment_type_text);
//        payment_type_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        payment_detail_text = (TextView) mainView.findViewById(R.id.payment_detail_text);
//        payment_detail_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_card_num = (EditText) mainView.findViewById(R.id.enter_card_num);
//        enter_card_num.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_name_on_card2 = (EditText) mainView.findViewById(R.id.enter_name_on_card2);
//        enter_name_on_card2.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_expire_text2 = (EditText) mainView.findViewById(R.id.enter_expire_text2);
//        enter_expire_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        ccv_text3 = (EditText) mainView.findViewById(R.id.ccv_text3);
//        ccv_text3.setTypeface(FontUtility.setMontserratLight(mContext));
//        billing_text = (TextView) mainView.findViewById(R.id.billing_text);
//        billing_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        same_as_text = (TextView) mainView.findViewById(R.id.same_as_text);
//        same_as_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_text5 = (TextView) mainView.findViewById(R.id.shipping_text5);
//        shipping_text5.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_text4 = (TextView) mainView.findViewById(R.id.shipping_text4);
//        shipping_text4.setTypeface(FontUtility.setMontserratLight(mContext));

//        confirm_text = (TextView) mainView.findViewById(R.id.confirm_text);
//        confirm_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        final_name_text = (TextView) mainView.findViewById(R.id.final_name_text);
//        final_name_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        final_address_text1 = (TextView) mainView.findViewById(R.id.final_address_text1);
//        final_address_text1.setTypeface(FontUtility.setMontserratLight(mContext));
//        final_address_text2 = (TextView) mainView.findViewById(R.id.final_address_text2);
//        final_address_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        final_address_text3 = (TextView) mainView.findViewById(R.id.final_address_text3);
//        final_address_text3.setTypeface(FontUtility.setMontserratLight(mContext));
//        final_total_item_count_text = (TextView) mainView.findViewById(R.id.final_total_item_count_text);
//        final_total_item_count_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        subtotal_text2 = (TextView) mainView.findViewById(R.id.subtotal_text2);
//        subtotal_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        sub_price_text2 = (TextView) mainView.findViewById(R.id.sub_price_text2);
//        sub_price_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        tax_text2 = (TextView) mainView.findViewById(R.id.tax_text2);
//        tax_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        tax_amount_text2 = (TextView) mainView.findViewById(R.id.tax_amount_text2);
//        tax_amount_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_cost = (TextView) mainView.findViewById(R.id.shipping_cost);
//        shipping_cost.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_cost_amount = (TextView) mainView.findViewById(R.id.shipping_cost_amount);
//        shipping_cost_amount.setTypeface(FontUtility.setMontserratLight(mContext));

//        total_text2 = (TextView) mainView.findViewById(R.id.total_text2);
//        total_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        total_amount_text2 = (TextView) mainView.findViewById(R.id.total_amount_text2);
//        total_amount_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        place_order_button = (TextView) mainView.findViewById(R.id.place_order_button);
//        place_order_button.setTypeface(FontUtility.setMontserratLight(mContext));

//        name_card_text = (TextView) mainView.findViewById(R.id.name_card_text);
//        name_card_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        title_credit_debit_text = (TextView) mainView.findViewById(R.id.title_credit_debit_text);
//        title_credit_debit_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        paypal_text = (TextView) mainView.findViewById(R.id.paypal_text);
//        paypal_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        or_text = (TextView) mainView.findViewById(R.id.or_text);
//        or_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        credit_debit_text = (TextView) mainView.findViewById(R.id.credit_debit_text);
//        credit_debit_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        credit_debit_layout = (RelativeLayout) mainView.findViewById(R.id.credit_debit_layout);
//        overflow_layout9_2 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout9_2);
//        balance_text = (TextView) mainView.findViewById(R.id.balance_text);
//        balance_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        new_payment_text2 = (TextView) mainView.findViewById(R.id.new_payment_text2);
//        new_payment_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        other_nameText = (TextView) mainView.findViewById(R.id.other_nameText);
//        other_nameText.setTypeface(FontUtility.setMontserratLight(mContext));
//        checkoutButton = (TextView) mainView.findViewById(R.id.checkoutButton);
//        checkoutButton.setTypeface(FontUtility.setMontserratLight(mContext));
//        next_button = (TextView) mainView.findViewById(R.id.next_button);
//        next_button.setTypeface(FontUtility.setMontserratLight(mContext));
//        next_button2 = (TextView) mainView.findViewById(R.id.next_button2);
//        next_button2.setTypeface(FontUtility.setMontserratLight(mContext));
//        contact_text = (TextView) mainView.findViewById(R.id.contact_text);
//        contact_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        redeem_text = (TextView) mainView.findViewById(R.id.redeem_text);
//        redeem_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        contact_button = (TextView) mainView.findViewById(R.id.contact_button);
//        contact_button.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_address2_text = (TextView) mainView.findViewById(R.id.shipping_address2_text);
//        shipping_address2_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_address1_text = (TextView) mainView.findViewById(R.id.shipping_address1_text);
//        shipping_address1_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_address_text_name = (TextView) mainView.findViewById(R.id.shipping_address_text_name);
//        shipping_address_text_name.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_address_text = (TextView) mainView.findViewById(R.id.shipping_address_text);
//        shipping_address_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        order_num_text = (TextView) mainView.findViewById(R.id.order_num_text);
//        order_num_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        deliver_text = (TextView) mainView.findViewById(R.id.deliver_text);
//        deliver_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        total_price_text = (TextView) mainView.findViewById(R.id.total_price_text);
//        total_price_text.setTypeface(FontUtility.setMontserratRegular(mContext));
//        date_text = (TextView) mainView.findViewById(R.id.date_text);
//        date_text.setTypeface(FontUtility.setMontserratRegular(mContext));
//        sub_price_text = (TextView) mainView.findViewById(R.id.sub_price_text);
//        sub_price_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        subtotal_text = (TextView) mainView.findViewById(R.id.subtotal_text);
//        subtotal_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        add_text = (TextView) mainView.findViewById(R.id.add_text);
//        add_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        promo_text = (TextView) mainView.findViewById(R.id.promo_text);
//        promo_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        payment_text = (TextView) mainView.findViewById(R.id.payment_text);
//        payment_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        shipping_text = (TextView) mainView.findViewById(R.id.shipping_text);
//        shipping_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        items_count_text = (TextView) mainView.findViewById(R.id.items_count_text);
//        items_count_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        items_text = (TextView) mainView.findViewById(R.id.items_text);
//        items_text.setTypeface(FontUtility.setMontserratLight(mContext));
        image_upload_post = (ImageView) mainView.findViewById(R.id.image_upload_post);
//        flatlay_card = (ImageView) mainView.findViewById(R.id.flatlay_card);
//        arrow1 = (ImageView) mainView.findViewById(R.id.arrow1);
//        same_as_icon = (ImageView) mainView.findViewById(R.id.same_as_icon);
//        check_icon = (ImageView) mainView.findViewById(R.id.check_icon);
//        arrow2 = (ImageView) mainView.findViewById(R.id.arrow2);
        image_upload_post2 = (ImageView) mainView.findViewById(R.id.image_upload_post2);
        uploadChoice = (RelativeLayout) mainView.findViewById(R.id.uploadChoice);
        image_camera = (ImageView) mainView.findViewById(R.id.image_camera);
        image_camera2 = (ImageView) mainView.findViewById(R.id.image_camera);
//        info_image = (ImageView) mainView.findViewById(R.id.info_image);
//        bell_image = (ImageView) mainView.findViewById(R.id.bell_image);
//        info_image2 = (ImageView) mainView.findViewById(R.id.info_image2);
//        filter_icon = (ImageView) mainView.findViewById(R.id.filter_icon);
//        filter_icon2 = (ImageView) mainView.findViewById(R.id.filter_icon2);
//        infoicon2 = (ImageView) mainView.findViewById(R.id.infoicon2);
//        MyTextWatcher2 watcher2 = new MyTextWatcher2();
//        MyTextWatcher3 watcher3 = new MyTextWatcher3();
        bottomLayout = (RelativeLayout) mainView.findViewById(R.id.bottomLayout);
        profile_tab = (LinearLayout) mainView.findViewById(R.id.profile_tab);
        profile_pic = (CircleImageView) mainView.findViewById(R.id.profile_pic);
//        other_profile_pic = (CircleImageView) mainView.findViewById(R.id.other_profile_pic);
//        search_tab = (LinearLayout) mainView.findViewById(R.id.search_tab);
        searchText = (EditText) mainView.findViewById(R.id.search_tab_text);
        searchText.setTypeface(FontUtility.setMontserratLight(mContext));
//        expiration_text2 = (EditText) mainView.findViewById(R.id.expiration_text2);
//        expiration_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        ccv_text2 = (EditText) mainView.findViewById(R.id.ccv_text2);
//        ccv_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        card_num_text2 = (EditText) mainView.findViewById(R.id.card_num_text2);
//        card_num_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        name_card_text2 = (EditText) mainView.findViewById(R.id.name_card_text2);
//        name_card_text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_promo_text = (EditText) mainView.findViewById(R.id.enter_promo_text);
//        enter_promo_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_promo_text.addTextChangedListener(watcher2);
//        enter_zip_text = (EditText) mainView.findViewById(R.id.enter_zip_text);
//        enter_zip_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_zip_text.addTextChangedListener(watcher2);
//        enter_state_text = (EditText) mainView.findViewById(R.id.enter_state_text);
//        enter_state_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_state_text.addTextChangedListener(watcher2);
//        enter_city_text.addTextChangedListener(watcher2);
//        enter_apt_text.addTextChangedListener(watcher2);
//        enter_address_text = (EditText) mainView.findViewById(R.id.enter_address_text);
//        enter_address_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        enter_address_text.addTextChangedListener(watcher2);
//        last_name = (EditText) mainView.findViewById(R.id.last_name);
//        last_name.setTypeface(FontUtility.setMontserratLight(mContext));
//        last_name.addTextChangedListener(watcher2);
//        enter_country_text2.addTextChangedListener(watcher2);
//        enter_country_text.addTextChangedListener(watcher2);
//        email.addTextChangedListener(watcher2);
//        enter_address_text2.addTextChangedListener(watcher2);
//        enter_apt_text2.addTextChangedListener(watcher2);
//        enter_city_text2.addTextChangedListener(watcher2);
//        enter_state_text2.addTextChangedListener(watcher2);
//        enter_zip_text2.addTextChangedListener(watcher2);
//        billing_last_name.addTextChangedListener(watcher2);
//        billing_first_name.addTextChangedListener(watcher2);
//        expiration_text2.addTextChangedListener(watcher3);
//        ccv_text2.addTextChangedListener(watcher3);
//        card_num_text2.addTextChangedListener(watcher3);
        highlight4 = (View) mainView.findViewById(R.id.highlight4);
        highlight5 = (View) mainView.findViewById(R.id.highlight5);
        highlight6 = (View) mainView.findViewById(R.id.highlight6);
//        name_card_text2.addTextChangedListener(watcher3);
//        first_name = (EditText) mainView.findViewById(R.id.first_name);
//        first_name.setTypeface(FontUtility.setMontserratLight(mContext));
//        first_name.addTextChangedListener(watcher2);
        searchText.setOnEditorActionListener(this);
        searchText.setCursorVisible(false);
        searchText.setFocusable(false);
        searchText.addTextChangedListener(new MyTextWatcher());
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
//                    searchText.setCursorVisible(false);
//                    searchText.setFocusable(false);
                    ((HomeActivity) mContext).myAddFragment(new SearchProductFragment(overflow2, fragmentInspirationSection, "", true, 0));
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
                } else if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    String search_text = searchText.getText().toString().trim();
//                    if (!overflow2.isOpen()) {
//                    searchText.setCursorVisible(false);
//                    searchText.setFocusable(false);
                    Log.e("SearchProduct", "down");

                    ((HomeActivity) mContext).myAddFragment(new SearchProductFragment(overflow2, fragmentInspirationSection, search_text, false));
                    searchText.setText("");

//                        searchText.setCursorVisible(true);
//                        searchText.setFocusableInTouchMode(true);
//                        searchText.requestFocus();
//
                }
                return false;
            }
        });
//        nameText = (TextView) mainView.findViewById(R.id.nameText);
//        nameText.setTypeface(FontUtility.setMontserratLight(mContext));
//        performanceText = (TextView) mainView.findViewById(R.id.performance_text);
//        performanceText.setTypeface(FontUtility.setMontserratLight(mContext));
//        peopletext = (TextView) mainView.findViewById(R.id.people_text);
//        peopletext.setTypeface(FontUtility.setMontserratLight(mContext));
//        productsText = (TextView) mainView.findViewById(R.id.products_text);
//        productsText.setTypeface(FontUtility.setMontserratLight(mContext));
//        shopText = (TextView) mainView.findViewById(R.id.shop_text);
//        shopText.setTypeface(FontUtility.setMontserratLight(mContext));
//        text1 = (TextView) mainView.findViewById(R.id.text1);
//        text1.setTypeface(FontUtility.setMontserratLight(mContext));
//        text2 = (TextView) mainView.findViewById(R.id.text2);
//        text2.setTypeface(FontUtility.setMontserratLight(mContext));
//        text3 = (TextView) mainView.findViewById(R.id.text3);
//        text3.setTypeface(FontUtility.setMontserratLight(mContext));
//        text4 = (TextView) mainView.findViewById(R.id.text4);
//        text4.setTypeface(FontUtility.setMontserratLight(mContext));
//        text5 = (TextView) mainView.findViewById(R.id.text5);
//        text5.setTypeface(FontUtility.setMontserratLight(mContext));
//        peopletext_layout = (RelativeLayout) mainView.findViewById(R.id.people_text_layout);
//        productsText_layout = (RelativeLayout) mainView.findViewById(R.id.products_text_layout);
//        shopText_layout = (RelativeLayout) mainView.findViewById(R.id.shop_text_layout);
        backIconLayout = (LinearLayout) mainView.findViewById(R.id.backIconLayout);
        cart_layout2 = (LinearLayout) mainView.findViewById(R.id.cart_layout2);
//        cards_scroll2 = (LinearLayout) mainView.findViewById(R.id.cards_scroll2);
//        flatlay_card2 = (LinearLayout) mainView.findViewById(R.id.flatlay_card2);
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
//        music_layout = (LinearLayout) mainView.findViewById(R.id.music_layout);
//        pet_layout = (LinearLayout) mainView.findViewById(R.id.pet_layout);
//        game_layout = (LinearLayout) mainView.findViewById(R.id.game_layout);
//        toy_layout = (LinearLayout) mainView.findViewById(R.id.toy_layout);
//        computer_layout = (LinearLayout) mainView.findViewById(R.id.computer_layout);
//        baby_layout = (LinearLayout) mainView.findViewById(R.id.baby_layout);
//        sports_layout = (LinearLayout) mainView.findViewById(R.id.sports_layout);
//        health_layout = (LinearLayout) mainView.findViewById(R.id.health_layout);
//        jewelry_layout = (LinearLayout) mainView.findViewById(R.id.jewelry_layout);
//        electronics_layout = (LinearLayout) mainView.findViewById(R.id.electronics_layout);
//        shoes_layout = (LinearLayout) mainView.findViewById(R.id.shoes_layout);
//        clothing_layout = (LinearLayout) mainView.findViewById(R.id.clothing_layout);
//        highlight1 = (View) mainView.findViewById(R.id.highlight1);
//        highlight2 = (View) mainView.findViewById(R.id.highlight2);
//        highlight3 = (View) mainView.findViewById(R.id.highlight3);
//        highlight4 = (View) mainView.findViewById(R.id.highlight4);
//        highlight5 = (View) mainView.findViewById(R.id.highlight5);
//        highlight6 = (View) mainView.findViewById(R.id.highlight6);
//        people_layout = (LinearLayout) mainView.findViewById(R.id.people_layout);
//        products_layout = (LinearLayout) mainView.findViewById(R.id.products_layout);
//        products_filter_layout = (RelativeLayout) mainView.findViewById(R.id.products_filter_layout);
//        products_result_layout = (RelativeLayout) mainView.findViewById(R.id.products_result_layout);
//        shops_layout = (LinearLayout) mainView.findViewById(R.id.shops_layout);
//        shoes_layout = (LinearLayout) mainView.findViewById(R.id.shoes_layout);
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
//        followingtext1 = (TextView) mainView.findViewById(R.id.followingtext1);
//        followingtext1.setTypeface(FontUtility.setMontserratLight(mContext));
//        followertext1 = (TextView) mainView.findViewById(R.id.followertext1);
//        followertext1.setTypeface(FontUtility.setMontserratLight(mContext));
//        followingtext2 = (TextView) mainView.findViewById(R.id.followingtext2);
//        followingtext2.setTypeface(FontUtility.setMontserratLight(mContext));
//        notification_text = (TextView) mainView.findViewById(R.id.notification_text);
//        notification_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        message_text = (TextView) mainView.findViewById(R.id.message_text);
//        message_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        invite_text = (TextView) mainView.findViewById(R.id.invite_text);
//        invite_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        clothing_text = (TextView) mainView.findViewById(R.id.clothing_text);
//        clothing_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        shoes_text = (TextView) mainView.findViewById(R.id.shoes_text);
//        shoes_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        electronics_text = (TextView) mainView.findViewById(R.id.electronics_text);
//        electronics_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        jewelry_text = (TextView) mainView.findViewById(R.id.jewelry_text);
//        jewelry_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        health_text = (TextView) mainView.findViewById(R.id.health_text);
//        health_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        sports_text = (TextView) mainView.findViewById(R.id.sports_text);
//        sports_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        baby_text = (TextView) mainView.findViewById(R.id.baby_text);
//        baby_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        computer_text = (TextView) mainView.findViewById(R.id.computer_text);
//        computer_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        toys_text = (TextView) mainView.findViewById(R.id.toys_text);
//        toys_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        games_text = (TextView) mainView.findViewById(R.id.games_text);
//        games_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        pet_text = (TextView) mainView.findViewById(R.id.pet_text);
//        pet_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        music_text = (TextView) mainView.findViewById(R.id.music_text);
//        music_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        followertext2 = (TextView) mainView.findViewById(R.id.followertext2);
//        followertext2.setTypeface(FontUtility.setMontserratLight(mContext));
//        filter_text = (TextView) mainView.findViewById(R.id.filter_text);
//        filter_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        women_text = (TextView) mainView.findViewById(R.id.women_text);
//        women_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        accessories_text = (TextView) mainView.findViewById(R.id.accessories_text);
//        accessories_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        men_text = (TextView) mainView.findViewById(R.id.men_text);
//        men_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        sale_text = (TextView) mainView.findViewById(R.id.sale_text);
//        sale_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        price_text = (TextView) mainView.findViewById(R.id.price_text);
//        price_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        price_text_0 = (TextView) mainView.findViewById(R.id.price_text_0);
//        price_text_0.setTypeface(FontUtility.setMontserratLight(mContext));
//        price_text_25 = (TextView) mainView.findViewById(R.id.price_text_25);
//        price_text_25.setTypeface(FontUtility.setMontserratLight(mContext));
//        price_text_50 = (TextView) mainView.findViewById(R.id.price_text_50);
//        price_text_50.setTypeface(FontUtility.setMontserratLight(mContext));
//        price_text_100 = (TextView) mainView.findViewById(R.id.price_text_100);
//        price_text_100.setTypeface(FontUtility.setMontserratLight(mContext));
//        price_text_150 = (TextView) mainView.findViewById(R.id.price_text_150);
//        price_text_150.setTypeface(FontUtility.setMontserratLight(mContext));
//        price_text_200 = (TextView) mainView.findViewById(R.id.price_text_200);
//        price_text_200.setTypeface(FontUtility.setMontserratLight(mContext));
//        price_text_500 = (TextView) mainView.findViewById(R.id.price_text_500);
//        price_text_500.setTypeface(FontUtility.setMontserratLight(mContext));
//        price_text_750 = (TextView) mainView.findViewById(R.id.price_text_750);
//        price_text_750.setTypeface(FontUtility.setMontserratLight(mContext));
//        apply_text = (TextView) mainView.findViewById(R.id.apply_text);
//        apply_text.setTypeface(FontUtility.setMontserratLight(mContext));
//        button1 = (Button) mainView.findViewById(R.id.button11);
//        button1.setTypeface(FontUtility.setMontserratLight(mContext));
//        button2 = (Button) mainView.findViewById(R.id.button22);
//        button2.setTypeface(FontUtility.setMontserratLight(mContext));
//        button1.setTextColor(Color.WHITE);
//        button1.setBackgroundResource(R.drawable.green_corner_button);
//        other_button11 = (Button) mainView.findViewById(R.id.other_button11);
//        other_button11.setTypeface(FontUtility.setMontserratLight(mContext));
//        other_button22 = (Button) mainView.findViewById(R.id.other_button22);
//        other_button22.setTypeface(FontUtility.setMontserratLight(mContext));
//        other_button11.setTextColor(Color.WHITE);
//        other_button11.setBackgroundResource(R.drawable.green_corner_button);
//        imagesList = (GridView) mainView.findViewById(R.id.imagesList);
//        enter_card_num.addTextChangedListener(watcher2);
//        enter_name_on_card2.addTextChangedListener(watcher2);
//        enter_expire_text2.addTextChangedListener(watcher2);
//        ccv_text3.addTextChangedListener(watcher2);
//        imagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem2, int visibleItemCount2, int totalItemCount2) {
//                FragmentInspirationSection.this.firstVisibleItem2 = firstVisibleItem2;
//                FragmentInspirationSection.this.visibleItemCount2 = visibleItemCount2;
//                FragmentInspirationSection.this.totalItemCount2 = totalItemCount2;
//                if (!myins_isloding && firstVisibleItem2 + visibleItemCount2 == totalItemCount2
//                        && totalItemCount2 != 0) {
//                    if (((HomeActivity) mContext).checkInternet()) {
//                        page1++;
//                        isFirstTime_mypos = false;
//                        getMyInspirationFeedList();
//
//                    } else {
//                    }
//                }
//            }
//        });

//        other_imagesList = (GridView) mainView.findViewById(R.id.other_imagesList);
//        card_gridview = (GridView) mainView.findViewById(R.id.card_gridview);
//        other_imagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int other_firstVisibleItem, int other_visibleItemCount, int other_totalItemCount) {
//                FragmentInspirationSection.this.other_firstVisibleItem = other_firstVisibleItem;
//                FragmentInspirationSection.this.other_visibleItemCount = other_visibleItemCount;
//                FragmentInspirationSection.this.other_totalItemCount = other_totalItemCount;
//                if (!isother_Loading_feed && other_firstVisibleItem + other_visibleItemCount == other_totalItemCount
//                        && other_totalItemCount != 0) {
//                    if (((HomeActivity) mContext).checkInternet()) {
//                        page7++;
//                        is_other_FirstTime_feed = false;
//                        getOtherInspirationFeedList(otherUserId);
//                    } else {
//                    }
//                }
//            }
//        });


//        feedDetail = (GridView) mainView.findViewById(R.id.feedDetail);
//        feedDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem4, int visibleItemCount4, int totalItemCount4) {
//                FragmentInspirationSection.this.firstVisibleItem4 = firstVisibleItem4;
//                FragmentInspirationSection.this.visibleItemCount4 = visibleItemCount4;
//                FragmentInspirationSection.this.totalItemCount4 = totalItemCount4;
//                if (!my_detail_ins_isloding && firstVisibleItem4 + visibleItemCount4 == totalItemCount4
//                        && totalItemCount4 != 0) {
//                    if (((HomeActivity) mContext).checkInternet()) {
//                        page3++;
//                        isFirstTime_mypos_detail = false;
//                        getMyInspirationFeedList();
//                    } else {
//                    }
//                }
//            }
//        });

//        productDetail = (GridView) mainView.findViewById(R.id.productDetail);
//        shopList = (GridView) mainView.findViewById(R.id.shopList);
//        product_result_list = (GridView) mainView.findViewById(R.id.product_result_list);
//        nameText.setText(UserPreference.getInstance().getUserName());
//        notificationlist = (RecyclerView) mainView.findViewById(R.id.notificationlist);
//        notificationlist.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
//        notificationlist.setLayoutManager(layoutManager);
//        collectionList = (ListView) mainView.findViewById(R.id.collectionList);
//        other_collectionList = (ListView) mainView.findViewById(R.id.other_collectionList);
//        item_list = (LinearLayout) mainView.findViewById(R.id.item_list);
//        item_list2 = (LinearLayout) mainView.findViewById(R.id.item_list2);
//        billing_layout = (LinearLayout) mainView.findViewById(R.id.billing_layout);
//        new_address_layout = (LinearLayout) mainView.findViewById(R.id.new_address_layout);
//        saved_address_layout = (LinearLayout) mainView.findViewById(R.id.saved_address_layout);
//        layout_6_2 = (LinearLayout) mainView.findViewById(R.id.layout_6_2);
//        layout_6_1 = (LinearLayout) mainView.findViewById(R.id.layout_6_1);
//        cards_list_view = (ScrollView) mainView.findViewById(R.id.cards_list_view);
//        cards_list_layout = (LinearLayout) mainView.findViewById(R.id.cards_list_layout);

//        user_result_list = (ListView) mainView.findViewById(R.id.user_result_list);
//        user_result_list.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem5, int visibleItemCount5, int totalItemCount5) {
//                FragmentInspirationSection.this.firstVisibleItem5 = firstVisibleItem5;
//                FragmentInspirationSection.this.visibleItemCount5 = visibleItemCount5;
//                FragmentInspirationSection.this.totalItemCount5 = totalItemCount5;
//
//                if (!user_list_isloding && firstVisibleItem5 + visibleItemCount5 == totalItemCount5
//                        && totalItemCount5 != 0) {
//                    if (((HomeActivity) mContext).checkInternet()) {
//                        page4++;
//                        isFirstTime_user_list = false;
//                        if (indexSearch == 0)
//                            displayAllPeopleResult();
//                        if (indexSearch == 1)
//                            displaySearchPeopleResult();
//                    } else {
//                    }
//                }
//            }
//        });

//        shopList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem5, int visibleItemCount5, int totalItemCount5) {
//                FragmentInspirationSection.this.firstVisibleItem5 = firstVisibleItem5;
//                FragmentInspirationSection.this.visibleItemCount5 = visibleItemCount5;
//                FragmentInspirationSection.this.totalItemCount5 = totalItemCount5;
//                if (!shop_list_isloding && firstVisibleItem5 + visibleItemCount5 == totalItemCount5
//                        && totalItemCount5 != 0) {
//                    if (((HomeActivity) mContext).checkInternet()) {
//                        page5++;
//                        isFirstTime_shop_list = false;
//                        if (searchText.getText().toString().trim().length() > 0) {
//                        } else displayAllShops();
//                    } else {
//                    }
//                }
//            }
//        });

        //   if (((HomeActivity) mContext).checkInternet()) {
//            collAdapterIndex = 0;
//            feedAdapterIndex = 0;
        //  getMyInspirationFeedList();
        //  getCollectionList();
        //  getKikrCredits();
//            indexSearch = 0;
        // displayAllPeopleResult();
        //    }
        if (isFromNotification == true) {
//            if (currentlayout != null)
//                currentlayout.setVisibility(View.GONE);
//            if (currentlayout2 != null)
//                currentlayout2.setVisibility(View.GONE);
//            overflow_layout3.setVisibility(View.VISIBLE);
//            currentlayout = overflow_layout3;
            ((HomeActivity) mContext).myAddFragment(new NotificationListFragment(overflow2, true));
//            getFollowingInstagramList();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    overflow2.triggerSlide();
                }
            }, 1300);
        }
    }

//    private void getOtherInspirationFeedList(String user_id) {
//        isother_Loading_feed = !isother_Loading_feed;
//        final InspirationFeedApi other_inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//
//                isother_Loading_feed = !isother_Loading_feed;
//                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
//
//                other_product_list.addAll(inspirationFeedRes.getData());
//                if (inspirationFeedRes.getData().size() < 10) {
//
//                    isother_Loading_feed = true;
//                }
//
//                if (other_product_list.size() > 0 && is_other_FirstTime_feed) {
//                    other_inspirationGridAdapter = new InspirationGridAdapter(mContext, other_product_list, 0);
//                    other_imagesList.setAdapter(other_inspirationGridAdapter);
//                    other_imagesList.setVisibility(View.VISIBLE);
//                } else if (other_inspirationGridAdapter != null) {
//                    other_inspirationGridAdapter.setData(other_product_list);
//                    other_inspirationGridAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                isother_Loading_feed = !isother_Loading_feed;
//                if (object != null) {
//                    InspirationFeedRes response = (InspirationFeedRes) object;
//                    AlertUtils.showToast(mContext, response.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//        });
//        other_inspirationFeedApi.getInspirationFeed(user_id, false, String.valueOf(page7), UserPreference.getInstance().getUserID());
//        other_inspirationFeedApi.execute();
//    }

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

//    private void displayAllPeopleResult() {
//        if (overflow2.isOpen())
//            overflow2.setOpen();
//        user_list_isloding = !user_list_isloding;
//        final FeaturedTabApi listApi = new FeaturedTabApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                if (overflow2.isOpen())
//                    overflow2.setOpen();
//                user_list_isloding = !user_list_isloding;
//                FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;
//                List<FeaturedTabData> list = featuredTabApiRes.getData();
//                if (list.size() < 7)
//                    user_list_isloding = true;
//                interestList.addAll(list);
//                if (interestList.size() > 0 && isFirstTime_user_list) {
//                    featuredTabAdapter = new FeaturedTabAdapter(mContext, interestList, new FeaturedTabAdapter.ListAdapterListener() {
//                        @Override
//                        public void onClickAtOKButton(int position) {
//                            final String other_user_id = interestList.get(position).getItem_id();
//                            final String other_user_name = interestList.get(position).getItem_name();
//                            final String image = interestList.get(position).getProfile_pic();
//                            if (currentlayout != null)
//                                currentlayout.setVisibility(View.GONE);
//                            if (currentlayout2 != null)
//                                currentlayout2.setVisibility(View.GONE);
//                            ((HomeActivity) mContext).myAddFragment(new OtherFeedCollectionFragment(overflow2,other_user_id,other_user_name,image));
//
////                            if (!other_user_id.equals(otherUserId)) {
////                                otherUserId = other_user_id;
////                                is_other_FirstTime_feed = true;
////                                isother_Loading_feed = false;
////                                page7 = 0;
////                                other_product_list.clear();
////                                CommonUtility.setImage(mContext, image, other_profile_pic, R.drawable.profile_icon);
////                                other_nameText.setText(other_user_name);
////                                other_button22.setTextColor(Color.BLACK);
////                                other_button11.setTextColor(Color.WHITE);
////                                other_button22.setBackgroundResource(R.drawable.white_button_noborder);
////                                other_button11.setBackgroundResource(R.drawable.green_corner_button);
////                                getOtherInspirationFeedList(other_user_id);
////                                getOtherCollectionList(other_user_id);
////                                final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
////                                    @Override
////                                    public void handleOnSuccess(Object object) {
////                                        MyProfileRes myProfileRes = (MyProfileRes) object;
////                                        other_followersLists = myProfileRes.getFollowers_list();
////                                        other_followingLists = myProfileRes.getFollowing_list();
////                                        other_followertext1.setText(String.valueOf(other_followingLists.size()));
////                                        other_followingtext1.setText(String.valueOf(other_followersLists.size()));
////                                    }
////
////                                    @Override
////                                    public void handleOnFailure(ServiceException exception, Object object) {
////
////                                    }
////                                });
////                                myProfileApi.getUserProfileDetail(other_user_id, UserPreference.getInstance().getUserID());
////                                myProfileApi.execute();
////                            }
////                            overflow_layout5.setVisibility(View.VISIBLE);
////                            currentlayout = overflow_layout5;
//                        }
//                    });
//                    if (overflow2.isOpen())
//                        overflow2.setOpen();
//                    user_result_list.setAdapter(featuredTabAdapter);
//                } else if (featuredTabAdapter != null) {
//                    if (overflow2.isOpen())
//                        overflow2.setOpen();
//                    featuredTabAdapter.setData(interestList);
//                    if (overflow2.isOpen())
//                        overflow2.setOpen();
//                    featuredTabAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                user_list_isloding = !user_list_isloding;
//            }
//        });
//        listApi.getFeaturedTabData(UserPreference.getInstance().getUserID(), String.valueOf(page4));
//        listApi.execute();
//    }

//    private void displaySearchPeopleResult() {
//        overflow2.setOpen();
//        user_list_isloding = !user_list_isloding;
//        final GeneralSearchApi generalSearchApi = new GeneralSearchApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                overflow2.setOpen();
//                user_list_isloding = !user_list_isloding;
//                GeneralSearchRes generalSearchRes = (GeneralSearchRes) object;
//                if (generalSearchRes.getUsers().size() < 10)
//                    user_list_isloding = true;
//                interestUserList.addAll(generalSearchRes.getUsers());
//                if (interestUserList.size() > 0 && isFirstTime_user_list) {
//                    interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(mContext, interestUserList, new CustomizeInterestPeopleListAdapter.ListAdapterListener() {
//                        @Override
//                        public void onClickAtOKButton(int position) {
//                            final String other_user_id = interestUserList.get(position).getId();
//                            final String other_user_name = interestUserList.get(position).getName();
//                            final String image = interestUserList.get(position).getImg();
////                            if (currentlayout != null)
////                                currentlayout.setVisibility(View.GONE);
////                            if (currentlayout2 != null)
////                                currentlayout2.setVisibility(View.GONE);
//                            ((HomeActivity) mContext).myAddFragment(new OtherFeedCollectionFragment(overflow2,other_user_id,other_user_name,image));
//
////                            if (!other_user_id.equals(otherUserId)) {
////                                otherUserId = other_user_id;
////                                is_other_FirstTime_feed = true;
////                                isother_Loading_feed = false;
////                                page7 = 0;
////                                other_product_list.clear();
////                                CommonUtility.setImage(mContext, image, other_profile_pic, R.drawable.profile_icon);
////                                other_nameText.setText(other_user_name);
////                                other_button22.setTextColor(Color.BLACK);
////                                other_button11.setTextColor(Color.WHITE);
////                                other_button22.setBackgroundResource(R.drawable.white_button_noborder);
////                                other_button11.setBackgroundResource(R.drawable.green_corner_button);
////                                getOtherInspirationFeedList(other_user_id);
////                                getOtherCollectionList(other_user_id);
////                                final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
////                                    @Override
////                                    public void handleOnSuccess(Object object) {
////                                        MyProfileRes myProfileRes = (MyProfileRes) object;
////                                        other_followersLists = myProfileRes.getFollowers_list();
////                                        other_followingLists = myProfileRes.getFollowing_list();
////                                        other_followertext1.setText(String.valueOf(other_followingLists.size()));
////                                        other_followingtext1.setText(String.valueOf(other_followersLists.size()));
////                                    }
////
////                                    @Override
////                                    public void handleOnFailure(ServiceException exception, Object object) {
////
////                                    }
////                                });
////                                myProfileApi.getUserProfileDetail(other_user_id, UserPreference.getInstance().getUserID());
////                                myProfileApi.execute();
////                            }
////                            overflow_layout5.setVisibility(View.VISIBLE);
////                            currentlayout = overflow_layout5;
//                        }
//                    });
//                    overflow2.setOpen();
//                    user_result_list.setAdapter(interestPeopleListAdapter);
//
//                } else if (interestPeopleListAdapter != null) {
//                    overflow2.setOpen();
//                    interestPeopleListAdapter.setData(interestUserList);
//                    overflow2.setOpen();
//                    interestPeopleListAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                user_list_isloding = !user_list_isloding;
//            }
//        });
//
//        if (page4 == 1) page4++;
//        generalSearchApi.generalSearch(searchText.getText().toString().trim(), page4);
//        generalSearchApi.execute();
//    }
//
//    private void displaySearchProductResult() {
//        overflow2.setOpen();
//        product_list_isloding = !product_list_isloding;
//        final GeneralSearchApi generalSearchApi = new GeneralSearchApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                overflow2.setOpen();
//                resultCategory = category;
//                product_list_isloding = !product_list_isloding;
//                GeneralSearchRes generalSearchRes = (GeneralSearchRes) object;
//                if (generalSearchRes.getProducts().size() < 10) product_list_isloding = true;
//                interestProductList.addAll(generalSearchRes.getProducts());
//                temporaryProoducts = categoryFilter(interestProductList, resultCategory);
//                if (temporaryProoducts.size() > 0 && isFirstTime_shop_list) {
//                    productSearchdAdapter = new ProductSearchGridAdapter(mContext, temporaryProoducts);
//                    overflow2.setOpen();
//                    product_result_list.setAdapter(productSearchdAdapter);
//                } else if (productSearchdAdapter != null) {
//                    overflow2.setOpen();
//                    productSearchdAdapter.setData(temporaryProoducts);
//                    overflow2.setOpen();
//                    productSearchdAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                product_list_isloding = !product_list_isloding;
//            }
//        });
//        generalSearchApi.generalSearch(searchText.getText().toString().trim(), page6);
//        generalSearchApi.execute();
//    }
//
//    private void displayAllShops() {
//        if (overflow2.isOpen()) overflow2.setOpen();
//        shop_list_isloding = !shop_list_isloding;
//        final BrandListApi brandListApi = new BrandListApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                shop_list_isloding = !shop_list_isloding;
//                BrandListRes brandListRes = (BrandListRes) object;
//                if (brandListRes.getData().size() < 10) shop_list_isloding = true;
//                brandLists.addAll(brandListRes.getData());
//                if (brandLists.size() > 0 && isFirstTime_shop_list) {
//                    allBrandGridAdapter = new AllBrandGridAdapter(mContext, brandLists);
//                    if (overflow2.isOpen()) overflow2.setOpen();
//                    shopList.setAdapter(allBrandGridAdapter);
//                } else if (allBrandGridAdapter != null) {
//                    if (overflow2.isOpen()) overflow2.setOpen();
//                    allBrandGridAdapter.setData(brandLists);
//                    if (overflow2.isOpen()) overflow2.setOpen();
//                    allBrandGridAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                shop_list_isloding = !shop_list_isloding;
//            }
//        });
//        brandListApi.getBrandList(String.valueOf(page5));
//        brandListApi.execute();
//    }
//
//    private void displaySearchShopResult() {
//        overflow2.setOpen();
//        shop_list_isloding = !shop_list_isloding;
//        final GeneralSearchApi generalSearchApi = new GeneralSearchApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                shop_list_isloding = !shop_list_isloding;
//                GeneralSearchRes generalSearchRes = (GeneralSearchRes) object;
//                if (generalSearchRes.getBrands().size() < 10) shop_list_isloding = true;
//                interestShopList.addAll(generalSearchRes.getBrands());
//                if (interestShopList.size() > 0 && isFirstTime_shop_list) {
//                    storeGridAdapter = new StoreGridAdapter(mContext, interestShopList);
//                    overflow2.setOpen();
//                    shopList.setAdapter(storeGridAdapter);
//                } else if (storeGridAdapter != null) {
//                    overflow2.setOpen();
//                    storeGridAdapter.setData(interestShopList);
//                    overflow2.setOpen();
//                    storeGridAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                shop_list_isloding = !shop_list_isloding;
//            }
//        });
//        if (page5 == 1) page5++;
//        generalSearchApi.generalSearch(searchText.getText().toString().trim(), page5);
//        generalSearchApi.execute();
//    }

    private boolean firstTimeProfile_pic = true, firstTimeSearch = true;
    public Stack<String> myFragmentStack;


    @Override
    public void setClickListener() {
//        add_new_address_text.setOnClickListener(this);
//        save_address_button.setOnClickListener(this);
//        fillButton.setOnClickListener(this);
//        check_icon.setOnClickListener(this);
//        save_card_button.setOnClickListener(this);
//        add_paypal_layout.setOnClickListener(this);
//        next_button2.setOnClickListener(this);
//        next_button.setOnClickListener(this);
//        checkoutButton.setOnClickListener(this);
        image_upload_post.setOnClickListener(this);
        image_upload_post2.setOnClickListener(this);
//        add_card_done_button.setOnClickListener(this);
        image_camera.setOnClickListener(this);
//        same_as_icon.setOnClickListener(this);
        tab_layout1.setOnClickListener(this);
        profile_tab.setOnClickListener(this);
//        add_text.setOnClickListener(this);
        profile_tab.setOnClickListener(this);
//        flatlay_card.setOnClickListener(this);
//        flatlay_card2.setOnClickListener(this);
//        new_payment_text2.setOnClickListener(this);
//        arrow1.setOnClickListener(this);
//        arrow2.setOnClickListener(this);
//        cancel_text.setOnClickListener(this);
        orders_layout.setOnClickListener(this);
//        new_payment_text.setOnClickListener(this);
        credits_layout.setOnClickListener(this);
        cards_layout.setOnClickListener(this);
//        credit_debit_layout.setOnClickListener(this);
//        search_tab.setOnClickListener(this);
        cart_layout2.setOnClickListener(this);
        cart_tab.setOnClickListener(this);
        fashioniconlayout.setOnClickListener(this);
        photographyiconlayoutlayout.setOnClickListener(this);
        traveliconlayout.setOnClickListener(this);
        beautyiconlayout.setOnClickListener(this);
        techiconlayout.setOnClickListener(this);
        homeiconlayout.setOnClickListener(this);
//        info_icon.setOnClickListener(this);
//        info_image2.setOnClickListener(this);
//        bell_icon.setOnClickListener(this);
//        text1.setOnClickListener(this);
//        text2.setOnClickListener(this);
//        text3.setOnClickListener(this);
//        text4.setOnClickListener(this);
//        text5.setOnClickListener(this);
//        infoicon2.setOnClickListener(this);
//        peopletext_layout.setOnClickListener(this);
//        productsText_layout.setOnClickListener(this);
//        shopText_layout.setOnClickListener(this);
//        invite_text.setOnClickListener(this);
//        filter_text.setOnClickListener(this);
//        women_text.setOnClickListener(this);
//        accessories_text.setOnClickListener(this);
//        men_text.setOnClickListener(this);
//        sale_text.setOnClickListener(this);
//        price_text.setOnClickListener(this);
//        price_text_0.setOnClickListener(this);
//        price_text_25.setOnClickListener(this);
//        price_text_50.setOnClickListener(this);
//        price_text_100.setOnClickListener(this);
//        price_text_150.setOnClickListener(this);
//        price_text_200.setOnClickListener(this);
//        price_text_500.setOnClickListener(this);
        backIconLayout.setOnClickListener(this);
//        price_text_750.setOnClickListener(this);
//        apply_text.setOnClickListener(this);
//        filter_icon.setOnClickListener(this);
//        filter_icon2.setOnClickListener(this);
//        clothing_layout.setOnClickListener(this);
//        shoes_layout.setOnClickListener(this);
//        electronics_layout.setOnClickListener(this);
//        jewelry_layout.setOnClickListener(this);
//        health_layout.setOnClickListener(this);
//        sports_layout.setOnClickListener(this);
//        baby_layout.setOnClickListener(this);
//        computer_layout.setOnClickListener(this);
//        toy_layout.setOnClickListener(this);
//        game_layout.setOnClickListener(this);
//        pet_layout.setOnClickListener(this);
//        music_layout.setOnClickListener(this);
        foodiconlayout.setOnClickListener(this);
        fitnessiconlayout.setOnClickListener(this);
        occasioniconlayout.setOnClickListener(this);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (firstTimeProfile_pic) {
                ((HomeActivity) mContext).myAddFragment(new MyFeedCollectionFragment(overflow2, false, isOnFeedPage));
//                    getMyInspirationFeedList();
//                    getCollectionList();
//                    firstTimeProfile_pic = false;
                //     }
                if (overflow2.isOpen()) {
//                    isInfo = false;
//                    nameText.setVisibility(View.VISIBLE);
//                    performanceText.setVisibility(View.GONE);
//                    info_image.setImageResource(R.drawable.info_white);
//                    collAdapterIndex = 0;
//                    displayMyInspirationFeedList();
//                    displayCollectionList();
//                    if (currentlayout != null)
//                        currentlayout.setVisibility(View.GONE);
//                    if (currentlayout2 != null)
//                        currentlayout2.setVisibility(View.GONE);
//                    overflow_layout1.setVisibility(View.VISIBLE);
//                    currentlayout = overflow_layout1;
//                    if (isOnFeedPage) {
//                        imagesList.setVisibility(View.VISIBLE);
//                        collectionList.setVisibility(View.GONE);
//                        feedDetail.setVisibility(View.GONE);
//                        productDetail.setVisibility(View.GONE);
//                    } else {
//                        imagesList.setVisibility(View.GONE);
//                        collectionList.setVisibility(View.VISIBLE);
//                        feedDetail.setVisibility(View.GONE);
//                        productDetail.setVisibility(View.GONE);
//                    }
                } else {
//                    if (currentlayout != null)
//                        currentlayout.setVisibility(View.GONE);
//                    if (currentlayout2 != null)
//                        currentlayout2.setVisibility(View.GONE);
                    overflow2.triggerSlide();
//                    new Handler().postDelayed(new Runnable() {
//                        public void run() {
////                            overflow_layout1.setVisibility(View.VISIBLE);
////                            currentlayout = overflow_layout1;
//                        }
//                    }, 800);
                }
            }
        });
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
        //                overflow2.setOpen();

//                isOnFeedPage = true;
//                button2.setTextColor(Color.BLACK);
//                button1.setTextColor(Color.WHITE);
//                button2.setBackgroundResource(R.drawable.white_button_noborder);
//                button1.setBackgroundResource(R.drawable.green_corner_button);
//                if (isInfo) {
//                    imagesList.setVisibility(View.GONE);
//                    feedDetail.setVisibility(View.VISIBLE);
//                } else {
//                    imagesList.setVisibility(View.VISIBLE);
//                    feedDetail.setVisibility(View.GONE);
//                }
//                collectionList.setVisibility(View.GONE);
//            }
//        });

//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                overflow2.setOpen();
//                isOnFeedPage = false;
//                button1.setTextColor(Color.BLACK);
//                button2.setTextColor(Color.WHITE);
//                button1.setBackgroundResource(R.drawable.white_button_noborder);
//                button2.setBackgroundResource(R.drawable.green_corner_button);
//                collectionList.setVisibility(View.VISIBLE);
//                imagesList.setVisibility(View.GONE);
//            }
//        });

//        other_button11.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                overflow2.setOpen();
//                other_button22.setTextColor(Color.BLACK);
//                other_button11.setTextColor(Color.WHITE);
//                other_button22.setBackgroundResource(R.drawable.white_button_noborder);
//                other_button11.setBackgroundResource(R.drawable.green_corner_button);
//                other_imagesList.setVisibility(View.VISIBLE);
//                other_collectionList.setVisibility(View.GONE);
//            }
//        });

//        other_button22.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                overflow2.setOpen();
//                other_button11.setTextColor(Color.BLACK);
//                other_button22.setTextColor(Color.WHITE);
//                other_button11.setBackgroundResource(R.drawable.white_button_noborder);
//                other_button22.setBackgroundResource(R.drawable.green_corner_button);
//                other_collectionList.setVisibility(View.VISIBLE);
//                other_imagesList.setVisibility(View.GONE);
//            }
//        });
    }

//    private void getOtherCollectionList(final String user_id) {
//        final CollectionApi other_collectionApi = new CollectionApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
//                other_collectionLists2 = collectionApiRes.getCollection();
//
//                if (other_collectionLists2.size() > 0 && is_other_FirstTime_coll) {
//
//                    other_collectionAdapter = new FragmentProfileCollectionAdapter(mContext,
//                            other_collectionLists2, user_id,
//                            fragmentProfileView, null, 0,
//                            new FragmentProfileCollectionAdapter.ListAdapterListener() {
//                                @Override
//                                public void onClickAtOKButton(int position) {
//                                }
//                            });
//                    other_collectionList.setAdapter(other_collectionAdapter);
//                    other_collectionList.setVisibility(View.VISIBLE);
//                } else if (other_collectionAdapter != null) {
//                    other_collectionAdapter.setData(other_collectionLists2);
//                    other_collectionAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (object != null) {
//                    CollectionApiRes collectionApiRes = (CollectionApiRes) object;
//                    AlertUtils.showToast(mContext, collectionApiRes.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//        });
//        other_collectionApi.getCollectionList(user_id);
//        other_collectionApi.execute();
//    }

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

//        final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                MyProfileRes myProfileRes = (MyProfileRes) object;
//                userDetails = myProfileRes.getUser_data();
//                followersLists = myProfileRes.getFollowers_list();
//                followingLists = myProfileRes.getFollowing_list();
//                followingtext1.setText(String.valueOf(followersLists.size()));
//                followertext1.setText(String.valueOf(followingLists.size()));
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//            }
//        });
//        myProfileApi.getUserProfileDetail(UserPreference.getInstance().getUserID(),
//                UserPreference.getInstance().getUserID());
//        myProfileApi.execute();
    }

//    private void getMyInspirationFeedList() {
//        if (feedAdapterIndex == 0) myins_isloding = !myins_isloding;
//        else my_detail_ins_isloding = !my_detail_ins_isloding;
//
//        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
//                if (feedAdapterIndex == 0) {
//                    myins_isloding = !myins_isloding;
//                    myProductList.addAll(inspirationFeedRes.getData());
//                    if (inspirationFeedRes.getData().size() < 10) myins_isloding = true;
//                } else {
//                    my_detail_ins_isloding = !my_detail_ins_isloding;
//                    myDetailProductList.addAll(inspirationFeedRes.getData());
//                    if (myDetailProductList.size() < 10) my_detail_ins_isloding = true;
//                }
//                displayMyInspirationFeedList();
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (object != null) {
//                    if (feedAdapterIndex == 0) myins_isloding = !myins_isloding;
//                    else my_detail_ins_isloding = !my_detail_ins_isloding;
//                    InspirationFeedRes response = (InspirationFeedRes) object;
//                    AlertUtils.showToast(mContext, response.getMessage());
//                } else AlertUtils.showToast(mContext, R.string.invalid_response);
//
//            }
//        });
//        if (feedAdapterIndex == 0)
//            inspirationFeedApi.getInspirationFeed(userId, false, String.valueOf(page1), userId);
//        else inspirationFeedApi.getInspirationFeed(userId, false, String.valueOf(page3), userId);
//        inspirationFeedApi.execute();
//    }

//    private void getCollectionList() {
//        if (overflow2.isOpen()) overflow2.setOpen();
//        mycoll_isloading = !mycoll_isloading;
//        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
//                collectionLists2 = collectionApiRes.getCollection();
//                displayCollectionList();
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                mycoll_isloading = !mycoll_isloading;
//                if (object != null) {
//                    CollectionApiRes collectionApiRes = (CollectionApiRes) object;
//                    AlertUtils.showToast(mContext, collectionApiRes.getMessage());
//                } else AlertUtils.showToast(mContext, R.string.invalid_response);
//            }
//        });
//        collectionApi.getCollectionList(UserPreference.getInstance().getUserID());
//        collectionApi.execute();
//    }


//    private void logoutUser() {
//        LogoutApi logoutApi = new LogoutApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//            }
//        });
//        logoutApi.logoutUser(UserPreference.getInstance().getUserID(), CommonUtility.getDeviceTocken(mContext));
//        logoutApi.execute();
//    }

//    private void getFollowingInstagramList() {
//        final MessageCenterApi messageCenterApi = new MessageCenterApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                FollowingKikrModel followingKikrModel = (FollowingKikrModel) object;
//                followinglist = followingKikrModel.getData();
//                followinglist.size();
//                followinglistRefined.clear();
//                if (followinglist.size() == 0) {
//                } else {
//                    if (currentlayout != null)
//                        currentlayout.setVisibility(View.GONE);
//                    if (currentlayout2 != null)
//                        currentlayout2.setVisibility(View.GONE);
//                    overflow_layout3.setVisibility(View.VISIBLE);
//                    currentlayout = overflow_layout3;
//                    notificationlist.setVisibility(View.VISIBLE);
//                    for (int i = 0; i < followinglist.size(); i++) {
//                        String userString = followinglist.get(i).getMessage();
//                        String notificationType = followinglist.get(i).getType();
//                        String userName = "";
//                        if (userString.contains("commented") && notificationType.equals("commentinsp"))
//                            userName = userString.split(" commented")[0];
//                        else if (userString.contains("liked") && notificationType.equals("likeinsp"))
//                            userName = userString.split(" liked")[0];
//                        else if (userString.contains("following") && notificationType.equals("follow"))
//                            userName = userString.split(" is following")[0];
//                        if (!userName.equals(UserPreference.getInstance().getUserName()))
//                            followinglistRefined.add(followinglist.get(i));
//
//                    }
//                    KikrFollowingAdapter adapter = new KikrFollowingAdapter(mContext, (ArrayList<FollowingKikrModel.DataBean>) followinglistRefined);
//                    notificationlist.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//            }
//        });
//        messageCenterApi.followinginstagram("0", "1");
//        messageCenterApi.execute();
//    }

//    private void displayMyInspirationFeedList() {
//        if (overflow2.isOpen()) overflow2.setOpen();
//        if (feedAdapterIndex == 0 && isOnFeedPage) {
//            if (myProductList.size() > 0 && isFirstTime_mypos) {
//                inspirationGridAdapter = new InspirationGridAdapter(mContext, myProductList, 0);
//                if (overflow2.isOpen()) overflow2.setOpen();
//                imagesList.setAdapter(inspirationGridAdapter);
//                if (overflow2.isOpen()) overflow2.setOpen();
//                feedDetail.setVisibility(View.GONE);
//                imagesList.setVisibility(View.VISIBLE);
//            } else if (inspirationGridAdapter != null) {
//                if (overflow2.isOpen()) overflow2.setOpen();
//                inspirationGridAdapter.setData(myProductList);
//                if (overflow2.isOpen()) overflow2.setOpen();
//                inspirationGridAdapter.notifyDataSetChanged();
//                if (overflow2.isOpen()) overflow2.setOpen();
//                feedDetail.setVisibility(View.GONE);
//                imagesList.setVisibility(View.VISIBLE);
//            }
//        } else if (feedAdapterIndex == 1 && isOnFeedPage) {
//            if (myDetailProductList.size() > 0 && isFirstTime_mypos_detail) {
//                inspirationGridAdapter2 = new InspirationGridAdapter(mContext, myDetailProductList, 1);
//                if (overflow2.isOpen()) overflow2.setOpen();
//                feedDetail.setAdapter(inspirationGridAdapter2);
//                if (overflow2.isOpen()) overflow2.setOpen();
//                feedDetail.setVisibility(View.VISIBLE);
//                imagesList.setVisibility(View.GONE);
//            } else if (inspirationGridAdapter2 != null) {
//                if (overflow2.isOpen()) overflow2.setOpen();
//                inspirationGridAdapter2.setData(myDetailProductList);
//                if (overflow2.isOpen()) overflow2.setOpen();
//                inspirationGridAdapter2.notifyDataSetChanged();
//                if (overflow2.isOpen()) overflow2.setOpen();
//                feedDetail.setVisibility(View.VISIBLE);
//                imagesList.setVisibility(View.GONE);
//            }
//        }
//    }

//    private void displayCollectionList() {
//
//        if (collectionLists2.size() < 10) mycoll_isloading = true;
//
//        if (collectionLists2.size() > 0 && isFirstTime_myCol) {
//            collectionAdapter = new FragmentProfileCollectionAdapter(mContext, collectionLists2, userId, fragmentProfileView, null, collAdapterIndex, new FragmentProfileCollectionAdapter.ListAdapterListener() {
//                @Override
//                public void onClickAtOKButton(int position) {
//                    final String coll_id = collectionLists2.get(position).getId();
//                    imagesList.setVisibility(View.GONE);
//                    collectionList.setVisibility(View.GONE);
//                    feedDetail.setVisibility(View.GONE);
//                    productDetail.setVisibility(View.VISIBLE);
//                    final ProductBasedOnBrandApi productBasedOnBrandApi = new ProductBasedOnBrandApi(new ServiceCallback() {
//                        @Override
//                        public void handleOnSuccess(Object object) {
//                            ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
//                            product_data = productBasedOnBrandRes.getData();
//                            productDetailGridAdapter = new ProductDetailGridAdapter(mContext, product_data, 0);
//                            productDetail.setAdapter(productDetailGridAdapter);
//                        }
//
//                        @Override
//                        public void handleOnFailure(ServiceException exception, Object object) {
//                        }
//                    });
//                    productBasedOnBrandApi.getProductsBasedOnCollectionList(UserPreference.getInstance().getUserID(), String.valueOf(0), coll_id);
//                    productBasedOnBrandApi.execute();
//                }
//            });
//            if (overflow2.isOpen()) overflow2.setOpen();
//            collectionList.setAdapter(collectionAdapter);
//        } else if (collectionAdapter != null) {
//            if (overflow2.isOpen()) overflow2.setOpen();
//            collectionAdapter.setData(collectionLists2);
//            if (overflow2.isOpen()) overflow2.setOpen();
//            collectionAdapter.notifyDataSetChanged();
//        }
//    }

//    private void setBackgroundByGroup(List<TextView> group1, int background1,
//                                      List<TextView> group2, int background2) {
//        if (group1 != null)
//            for (int i = 0; i < group1.size(); i++) {
//                group1.get(i).setBackgroundResource(background1);
//            }
//        if (group2 != null)
//            for (int i = 0; i < group2.size(); i++) {
//                group2.get(i).setBackgroundResource(background2);
//            }
//    }
//
//    private void setTextColorByGroup(List<TextView> group1, int color1,
//                                     List<TextView> group2, int color2) {
//        if (group1 != null)
//            for (int i = 0; i < group1.size(); i++) {
//                group1.get(i).setTextColor(color1);
//            }
//        if (group2 != null)
//            for (int i = 0; i < group2.size(); i++) {
//                group2.get(i).setTextColor(color2);
//            }
//    }
//
//    public List<ProductResult> categoryFilter(List<ProductResult> products, String filter) {
//        if (products == null) return products;
//        List<ProductResult> result = new ArrayList<>();
//        for (int i = 0; i < products.size(); i++) {
//            ProductResult current = products.get(i);
//            if (current.getPrimarycategory() != null && current.getPrimarycategory().toLowerCase().contains(filter)) {
//                result.add(current);
//            }
//        }
//        return result;
//    }
//
//    public List<ProductResult> buttonPriceFilter(List<ProductResult> products, String filter1, String filter2, String filter3, double lowPrice, double highPrice) {
//        if (products == null) return products;
//        List<ProductResult> result = new ArrayList<>();
//        for (int i = 0; i < products.size(); i++) {
//            ProductResult current = products.get(i);
//            String retailPrice = current.getRetailprice();
//            if (current.getPrimarycategory() != null && current.getPrimarycategory().toLowerCase().contains(filter1) && (current.getPrimarycategory().toLowerCase().contains(filter2) || current.getPrimarycategory().toLowerCase().contains(filter3))) {
//                if (retailPrice != null && retailPrice.length() > 0) {
//                    double price = Double.parseDouble(retailPrice);
//                    if (price >= lowPrice && price <= highPrice) result.add(current);
//                }
//            }
//        }
//        return result;
//    }

//    private class MyTextWatcher implements TextWatcher {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            String text = searchText.getText().toString();
//            if (text == null || text.length() == 0) {
//                if (indexSearch == 2 && ppsIndex == 2) {
//                    storeGridAdapter = null;
//                    shop_list_isloding = false;
//                    page5 = 0;
//                    interestShopList.clear();
//                    brandLists.clear();
//                    isFirstTime_shop_list = true;
//                    displayAllShops();
//                } else if (indexSearch == 3 && ppsIndex == 1) {
//                    product_list_isloding = false;
//                    page6 = 0;
//                    interestProductList.clear();
//                    isFirstTime_product_list = true;
//                    products_filter_layout.setVisibility(View.GONE);
//                    products_result_layout.setVisibility(View.GONE);
//                    products_layout.setVisibility(View.VISIBLE);
//                } else if ((indexSearch == 0 || indexSearch == 1) && ppsIndex == 0) {
//                    interestPeopleListAdapter = null;
//                    user_list_isloding = false;
//                    page4 = 0;
//                    interestList.clear();
//                    interestUserList.clear();
//                    isFirstTime_user_list = true;
//                    displayAllPeopleResult();
//                }
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    }

//    private void setCardImage(String card_number1, ImageView imageView) {
//        String card_number = CommonUtility.DecryptCreditCard(card_number1);
//        if (card_number1.equals("Add")) {
//            return;
//        } else if (card_number1.equals("paypal")) {
//            imageView.setImageResource(R.drawable.paypal_1);
//        } else if (!card_number1.equals("flatlay") && Luhn.isCardValid(card_number)) {
//            CardType cardtype = Luhn.getCardType(card_number);
//            if (cardtype == null) {
//                imageView.setImageResource(R.drawable.ic_connect_card_unknown);
//            } else if (cardtype.equals(CardType.VISA)) {
//                imageView.setImageResource(R.drawable.ic_connect_card_visa);
//            } else if (cardtype.equals(CardType.AMERICAN_EXPRESS)) {
//                imageView.setImageResource(R.drawable.ic_connect_card_american);
//            } else if (cardtype.equals(CardType.MASTER_CARD)) {
//                imageView.setImageResource(R.drawable.ic_connect_card_master);
//            } else if (cardtype.equals(CardType.DISCOVER)) {
//                imageView.setImageResource(R.drawable.ic_connect_card_discover);
//            }
//        } else
//            imageView.setImageResource(R.drawable.ic_connect_card_unknown);
//    }
//
//    private void setCardImage2(String card_number1, ImageView imageView, TextView detail1, TextView detail2, TextView detail3) {
//        String card_number = CommonUtility.DecryptCreditCard(card_number1);
//        if (card_number1.equals("Add")) {
//            return;
//        } else if (card_number1.equals("paypal")) {
//            imageView.setImageResource(R.drawable.paypal_small);
//        } else if (card_number1.equals("flatlay")) {
//            imageView.setImageResource(R.drawable.card1_small);
//        } else if (Luhn.isCardValid(card_number)) {
//            CardType cardtype = Luhn.getCardType(card_number);
//            if (cardtype == null) {
//                imageView.setImageResource(R.drawable.ic_connect_card_unknown);
//            } else if (cardtype.equals(CardType.VISA)) {
//                detail1.setText("VISA" + " (" + card_number.substring(card_number.length() - 4, card_number.length()) + ") ");
//                imageView.setImageResource(R.drawable.visa_small);
//            } else if (cardtype.equals(CardType.AMERICAN_EXPRESS)) {
//                detail1.setText("American Express" + " (" + card_number.substring(card_number.length() - 4, card_number.length()) + ") ");
//                imageView.setImageResource(R.drawable.ic_card_american);
//            } else if (cardtype.equals(CardType.MASTER_CARD)) {
//                detail1.setText("Mastercard" + " (" + card_number.substring(card_number.length() - 4, card_number.length()) + ") ");
//                imageView.setImageResource(R.drawable.mastercard_small);
//            } else if (cardtype.equals(CardType.DISCOVER)) {
//                detail1.setText("Discover" + " (" + card_number.substring(card_number.length() - 4, card_number.length()) + ") ");
//                imageView.setImageResource(R.drawable.ic_card_discover);
//            }
//        } else
//            imageView.setImageResource(R.drawable.ic_connect_card_unknown);
//    }

    protected RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//    protected LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

//    protected void setCartItemsList(final List<Product> data) {
//        item_list.removeAllViews();
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for (int i = 0; i < data.size(); i++) {
//            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.cart_item_layout, null);
//            ImageView cancel_icon = (ImageView) layout.findViewById(R.id.cancel_icon);
//            RoundedImageView product_image = (RoundedImageView) layout.findViewById(R.id.product_image);
//            final Product currentProduct = data.get(i);
//            String productImage = currentProduct.getProductimageurl();
//            if (productImage != null && productImage.length() > 0)
//                CommonUtility.setImage(mContext, product_image, productImage);
//            product_image.setTag(i);
//            ((TextView) layout.findViewById(R.id.quantity_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.price_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.size_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.color_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.product_name)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.brand_name)).setTypeface(FontUtility.setMontserratRegular(mContext));
//            if (currentProduct.getMerchantname() != null && currentProduct.getMerchantname().length() > 0) {
//                ((TextView) layout.findViewById(R.id.brand_name)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.brand_name)).setText(currentProduct.getMerchantname());
//            }
//            if (currentProduct.getQuantity() != null && currentProduct.getQuantity().length() > 0) {
//                ((TextView) layout.findViewById(R.id.quantity_text)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.quantity_text)).setText("Quantity: " + currentProduct.getQuantity());
//            }
//            if (currentProduct.getProductname() != null && currentProduct.getProductname().length() > 0) {
//                ((TextView) layout.findViewById(R.id.product_name)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.product_name)).setText(currentProduct.getProductname());
//            }
//            if (currentProduct.getSelected_color() != null && currentProduct.getSelected_color().length() > 0) {
//                ((TextView) layout.findViewById(R.id.color_text)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.color_text)).setText("Color: " + currentProduct.getSelected_color());
//            }
//            if (currentProduct.getSelected_size() != null && currentProduct.getSelected_size().length() > 0) {
//                ((TextView) layout.findViewById(R.id.size_text)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.size_text)).setText("Size: " + currentProduct.getSelected_size());
//            }
//            double currentPrice = 0;
//            if (currentProduct.getSaleprice() != null && currentProduct.getSaleprice().length() > 0) {
//                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
//                total_price += StringUtils.getDoubleValue(currentProduct.getSaleprice());
//                currentPrice = StringUtils.getDoubleValue(currentProduct.getSaleprice());
//                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: $ " + currentProduct.getSaleprice());
//            } else if (currentProduct.getRetailprice() != null && currentProduct.getRetailprice().length() > 0) {
//                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
//                total_price += StringUtils.getDoubleValue(currentProduct.getRetailprice());
//                currentPrice = StringUtils.getDoubleValue(currentProduct.getRetailprice());
//                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: $ " + currentProduct.getRetailprice());
//            }
//            final double currentPrice2 = currentPrice;
//            final LinearLayout layout2 = layout;
//            cancel_icon.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    removeFromCart(currentProduct.getProductcart_id(), layout2, currentPrice2);
//                }
//            });
//
//            sub_price_text.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price)));
//            total_amount_text2.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price + TAX)));
//            product_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("data", currentProduct);
//                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
//                    detail.setArguments(bundle);
//                    ((HomeActivity) mContext).addFragment(detail);
//                }
//            });
//            params3.setMargins(0, 10, 0, 10);
//            item_list.addView(layout, params3);
//        }
//    }
//
//    protected void setCartItemsList2(final List<Product> data) {
//        item_list2.removeAllViews();
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for (int i = 0; i < data.size(); i++) {
//            LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.cart_item_layout, null);
//            ImageView cancel_icon = (ImageView) layout.findViewById(R.id.cancel_icon);
//            RoundedImageView product_image = (RoundedImageView) layout.findViewById(R.id.product_image);
//            final Product currentProduct = data.get(i);
//            String productImage = currentProduct.getProductimageurl();
//            if (productImage != null && productImage.length() > 0)
//                CommonUtility.setImage(mContext, product_image, productImage);
//            product_image.setTag(i);
//            ((TextView) layout.findViewById(R.id.quantity_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.price_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.size_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.color_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.product_name)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout.findViewById(R.id.brand_name)).setTypeface(FontUtility.setMontserratRegular(mContext));
//            if (currentProduct.getMerchantname() != null && currentProduct.getMerchantname().length() > 0) {
//                ((TextView) layout.findViewById(R.id.brand_name)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.brand_name)).setText(currentProduct.getMerchantname());
//            }
//            if (currentProduct.getQuantity() != null && currentProduct.getQuantity().length() > 0) {
//                ((TextView) layout.findViewById(R.id.quantity_text)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.quantity_text)).setText("Quantity: " + currentProduct.getQuantity());
//            }
//            if (currentProduct.getProductname() != null && currentProduct.getProductname().length() > 0) {
//                ((TextView) layout.findViewById(R.id.product_name)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.product_name)).setText(currentProduct.getProductname());
//            }
//            if (currentProduct.getSelected_color() != null && currentProduct.getSelected_color().length() > 0) {
//                ((TextView) layout.findViewById(R.id.color_text)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.color_text)).setText("Color: " + currentProduct.getSelected_color());
//            }
//            if (currentProduct.getSelected_size() != null && currentProduct.getSelected_size().length() > 0) {
//                ((TextView) layout.findViewById(R.id.size_text)).setVisibility(View.VISIBLE);
//                ((TextView) layout.findViewById(R.id.size_text)).setText("Size: " + currentProduct.getSelected_size());
//            }
//            double currentPrice = 0;
//            if (currentProduct.getSaleprice() != null && currentProduct.getSaleprice().length() > 0) {
//                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
//                total_price += StringUtils.getDoubleValue(currentProduct.getSaleprice());
//                currentPrice = StringUtils.getDoubleValue(currentProduct.getSaleprice());
//                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: $ " + currentProduct.getSaleprice());
//            } else if (currentProduct.getRetailprice() != null && currentProduct.getRetailprice().length() > 0) {
//                ((TextView) layout.findViewById(R.id.price_text)).setVisibility(View.VISIBLE);
//                total_price += StringUtils.getDoubleValue(currentProduct.getRetailprice());
//                currentPrice = StringUtils.getDoubleValue(currentProduct.getRetailprice());
//                ((TextView) layout.findViewById(R.id.price_text)).setText("Price: $ " + currentProduct.getRetailprice());
//            }
//
//            cancel_icon.setVisibility(View.INVISIBLE);
//            sub_price_text.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price)));
//            product_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("data", currentProduct);
//                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
//                    detail.setArguments(bundle);
//                    ((HomeActivity) mContext).addFragment(detail);
//                }
//            });
//            params3.setMargins(0, 10, 0, 10);
//            item_list2.addView(layout, params3);
//        }
//    }

//    private List<Product> my_productLists;

//    public void removeFromCart(final String id, final LinearLayout layout, final double price) {
//
//        final CartApi cartApi = new CartApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                for (int i = 0; i < my_productLists.size(); i++) {
//                    if (id.equals(my_productLists.get(i).getProductcart_id())) {
//                        my_productLists.remove(i);
//                    }
//                    break;
//                }
//                total_price -= price;
//                sub_price_text.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(total_price)));
//                UserPreference.getInstance().decCartCount();
//                items_count_text.setText(UserPreference.getInstance().getCartCount());
//                total_item_count_text.setText("Total (" + UserPreference.getInstance().getCartCount() + " Items)");
//                final_total_item_count_text.setText("ITEMS (" + UserPreference.getInstance().getCartCount() + " )");
//                subtotal_text2.setText("Subtotal (" + UserPreference.getInstance().getCartCount() + " Items)");
//                AlertUtils.showToast(mContext, "Product removed successfully");
//                layout.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (object != null) {
//                    CartRes response = (CartRes) object;
//                    AlertUtils.showToast(mContext, response.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//        });
//        cartApi.removeFromCart(UserPreference.getInstance().getUserID(), id);
//        cartApi.execute();
//    }
//

//    protected void setCardList(final List<Card> data) {
//        cards_list_layout.removeAllViews();
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        flatlay_card2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (CommonUtility.getDeviceWidth(mContext)) * 51 / 100));
//        RelativeLayout layout3 = (RelativeLayout) inflater.inflate(R.layout.layout_card_detail, null);
//        ((TextView) layout3.findViewById(R.id.edit_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//        ((TextView) layout3.findViewById(R.id.detail1)).setTypeface(FontUtility.setMontserratLight(mContext));
//        ((TextView) layout3.findViewById(R.id.detail1)).setText("FLATLAY Card");
//        ((TextView) layout3.findViewById(R.id.detail3)).setTypeface(FontUtility.setMontserratLight(mContext));
//        if (kikrCredit.length() <= 5)
//            ((TextView) layout3.findViewById(R.id.detail3)).setText(kikrCredit + " credits = $ " + kikrCredit100String + "\n");
//        else
//            ((TextView) layout3.findViewById(R.id.detail3)).setText(kikrCredit + " credits\n=\n$ " + kikrCredit100String + "\n");
//
//        ((TextView) layout3.findViewById(R.id.bottom_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//        ((TextView) layout3.findViewById(R.id.bottom_text)).setText("Set as default payment method");
//        ((ImageView) layout3.findViewById(R.id.card_image)).setImageResource(R.drawable.card1_small);
//        cards_list_layout.addView(layout3, params2);
//        params2.setMargins(0, 40, 0, 0);
//        for (int i = 0; i < data.size(); i++) {
//            RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.layout_wallet_card, null);
//            ImageView cardImageView = (ImageView) layout.findViewById(R.id.cardImageView);
//            setCardImage(data.get(i).getCard_number(), cardImageView);
//            cardImageView.setTag(i);
//            RelativeLayout layout2 = (RelativeLayout) inflater.inflate(R.layout.layout_card_detail, null);
//            ((TextView) layout2.findViewById(R.id.bottom_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout2.findViewById(R.id.detail3)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout2.findViewById(R.id.detail2)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout2.findViewById(R.id.detail1)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout2.findViewById(R.id.edit_text)).setTypeface(FontUtility.setMontserratLight(mContext));
//            ((TextView) layout2.findViewById(R.id.detail2)).setText(data.get(i).getName_on_card());
//            String[] arr = data.get(i).getExpiration_date().split("/");
//            if (arr.length >= 2) {
//                ((TextView) layout2.findViewById(R.id.detail3)).setText(arr[0] + " / " + arr[1] + "\n");
//            }
//            ((TextView) layout2.findViewById(R.id.bottom_text)).setText("Set as default payment method");
//            setCardImage2(data.get(i).getCard_number(), (ImageView) layout2.findViewById(R.id.card_image), (TextView) layout2.findViewById(R.id.detail1), (TextView) layout2.findViewById(R.id.detail2), (TextView) layout2.findViewById(R.id.detail3));
//            cards_scroll2.addView(layout, params);
//            params2.setMargins(0, 40, 0, 0);
//            cards_list_layout.addView(layout2, params2);
//            final String card_id = data.get(i).getCard_id();
//            final View childView1 = layout;
//            final View childView2 = layout2;
//            layout2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    String msg = "Are you sure you want ot delete this card?";
//                    OrderProcessingDialog orderProcessingDialog = new OrderProcessingDialog(mContext, msg, new OrderProcessingDialog.MyListener() {
//                        @Override
//                        public void onClickButton() {
//                            deleteCard(card_id, childView1, childView2);
//                        }
//                    });
//                    orderProcessingDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
//                    orderProcessingDialog.show();
//                }
//            });
//            cardImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (currentlayout2 != null) {
//                        currentlayout2.setVisibility(View.GONE);
//                    }
//                    overflow_layout9_2.setVisibility(View.VISIBLE);
//                    currentlayout2 = overflow_layout9_2;
//                }
//            });
//        }
//    }

//    private String kikrCredit100String = "";
//    private double kikrCreditOriginal = 0, kikrCredit100 = 0;

//    private void getKikrCredits() {
//        final KikrCreditsApi creditsApi = new KikrCreditsApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                final KikrCreditsRes kikrCreditsRes = (KikrCreditsRes) object;
//                if (kikrCreditsRes != null) {
//                    kikrCreditOriginal = StringUtils.getDoubleValue(kikrCreditsRes.getAmount());
//                    kikrCredit = new DecimalFormat("#,##0").format(new Double(kikrCreditOriginal));
//                    kikrCredit100 = kikrCreditOriginal / 100;
//                    kikrCredit100String = new DecimalFormat("#,###0.00").format(new Double(kikrCredit100));
//                    balance_text.setText("Current Balance: " + kikrCredit + " credits");
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (object != null) {
//                    KikrCreditsRes response = (KikrCreditsRes) object;
//                    AlertUtils.showToast(mContext, response.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//        });
//        creditsApi.getKikrCredits(UserPreference.getInstance().getUserID());
//        creditsApi.execute();
//    }

    //    private double total_price = 0;
    private CartTotalInfo cartTotalInfo;
//    private String subtotal = "0", tax = "0", shipping = "0", finalprice = "0";

//    private void getEstimate() {
//        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                try {
//                    subtotal = "0";
//                    tax = "0";
//                    shipping = "0";
//                    finalprice = "0";
//                    JSONObject jsonObject = new JSONObject(object.toString());
//                    JSONObject estimates = jsonObject.getJSONObject("estimates");
//                    Iterator keys = estimates.keys();
//                    while (keys.hasNext()) {
//                        String currentDynamicKey = (String) keys.next();
//                        JSONObject currentDynamicValue = estimates.getJSONObject(currentDynamicKey);
//                        JSONObject prices = currentDynamicValue.getJSONObject("prices");
//
//                        subtotal = Double.toString(Double.parseDouble(subtotal) + Double.parseDouble(prices.getString("subtotal").replace("$", "")));
//                        sub_price_text2.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(subtotal)));
//                        shipping = Double.toString(Double.parseDouble(shipping) + Double.parseDouble(prices.getString("shipping_price").replace("$", "")));
//                        shipping_cost_amount.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(shipping)));
//                        tax = Double.toString(Double.parseDouble(tax) + Double.parseDouble(prices.getString("sales_tax").replace("$", "")));
//                        tax_amount_text2.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(tax)));
//                        finalprice = Double.toString(Double.parseDouble(finalprice) + Double.parseDouble(prices.getString("final_price").replace("$", "")));
//                        total_amount_text2.setText("$ " + new DecimalFormat("#,##0.00").format(new Double(finalprice)));
//                        for (int i = 0; i < my_productLists.size(); i++) {
//                            if (currentDynamicKey.equals(my_productLists.get(i).getSiteId())) {
//                                updateProductPrice(my_productLists.get(i).getProductcart_id(), prices.getString("subtotal").replace("$", ""),
//                                        prices.getString("shipping_price").replace("$", ""), prices.getString("sales_tax").replace("$", ""), my_productLists.get(i).getMd5(), my_productLists.get(i).getSiteId());
//                                if (prices.getString("shipping_price").equals("$0.00"))
//                                    my_productLists.get(i).setFreeShipping(true);
//                            }
//                        }
//                    }
//                    UserPreference.getInstance().setFinalPrice(finalprice);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//            }
//        });
//        twoTapApi.getCartEstimates(cartList, cartid, cardAndShippingDetail);
//        twoTapApi.execute();
//    }

//    private void updateProductPrice(String productCartId, String price, String shiping, String productTax, String md5, String siteId) {
//        CartApi cartApi = new CartApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//            }
//        });
//        cartApi.updateproductPrice(UserPreference.getInstance().getUserID(), productCartId, price, shiping, productTax, md5, siteId);
//        cartApi.execute();
//    }

//    public void getCartList() {
//        CartApi cartApi = new CartApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                CartRes cartRes = (CartRes) object;
//                List<Product> productLists = cartRes.getData();
//                my_productLists = productLists;
//                if (productLists.size() <= 0) {
//                    layout_6_2.setVisibility(View.VISIBLE);
//                    layout_6_1.setVisibility(View.GONE);
//                } else {
//                    layout_6_1.setVisibility(View.VISIBLE);
//                    layout_6_2.setVisibility(View.GONE);
//                    UserPreference.getInstance().setCartCount(
//                            String.valueOf(productLists.size()));
//                    items_count_text.setText(UserPreference.getInstance().getCartCount());
//                    total_item_count_text.setText("Total (" + UserPreference.getInstance().getCartCount() + " Items)");
//                    final_total_item_count_text.setText("ITEMS (" + UserPreference.getInstance().getCartCount() + " )");
//                    subtotal_text2.setText("Subtotal (" + UserPreference.getInstance().getCartCount() + " Items)");
//                    setCartItemsList(productLists);
//                    setCartItemsList2(productLists);
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception,
//                                        Object object) {
//
//            }
//        });
//        cartApi.getCartList(UserPreference.getInstance().getUserID());
//        cartApi.execute();
//    }

//    private class MyTextWatcher2 implements TextWatcher {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            saveAddress();
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    }

//    private class MyTextWatcher3 implements TextWatcher {
//        @Override
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            saveCard();
//        }
//
//        @Override
//        public void afterTextChanged(Editable editable) {
//
//        }
//    }

//    public void callPurchase() {
//
//        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                try {
//                    JSONObject jsonObject = new JSONObject(object.toString());
//                    Syso.info("result:  " + jsonObject);
//                    if (jsonObject.has("purchase_id")) {
//                        purchaseId = jsonObject.getString("purchase_id");
//                        UserPreference.getInstance().setPurchaseId(purchaseId);
//                        if (((HomeActivity) mContext).checkInternet())
//                            callServerForConfirmation(purchaseId, "pending");
//                        String msg = "We sent the order to the merchants, we will send you a follow up notice once your order status is confirmed.";
//                        OrderProcessingDialog orderProcessingDialog = new OrderProcessingDialog(mContext, msg, new OrderProcessingDialog.MyListener() {
//                            @Override
//                            public void onClickButton() {
//                                Intent i = new Intent(mContext, PlaceOrderService.class);
//                                i.putExtra("purchase_id", purchaseId);
//                                i.putExtra("cartId", cartid);
//                                mContext.startService(i);
//                            }
//                        });
//                        orderProcessingDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
//                        orderProcessingDialog.show();
//                    } else {
//                        CommonUtility.showAlert(mContext, jsonObject.getString("description"));
//                    }
//                } catch (JSONException e) {
//                    AlertUtils.showToast(mContext, "Failed to get status");
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//            }
//        });
//        createCartList();
//        twoTapApi.purchaseApi2(cartid, cartList, cardAndShippingDetail);
//        twoTapApi.execute();
//    }

//    HashMap<String, List<Product>> cartList = new HashMap<String, List<Product>>();

//    private void createCartList() {
//        for (int i = 0; i < my_productLists.size(); i++) {
//            String siteId = my_productLists.get(i).getSiteId();
//            if (cartList.containsKey(siteId)) {
//                cartList.get(siteId).add(my_productLists.get(i));
//            } else {
//                List<Product> list = new ArrayList<Product>();
//                list.add(my_productLists.get(i));
//                cartList.put(siteId, list);
//            }
//        }
//    }

//    public void callServerForConfirmation(final String purchase_id, final String status) {
//        UpdateCartApi twoTapApi = new UpdateCartApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                if (status.equals("confirmed")) {
//                    if (((HomeActivity) mContext).checkInternet()) {
//                        my_productLists.clear();
//                        UserPreference.getInstance().setCartCount("0");
//                        purchaseId = "";
//                        UserPreference.getInstance().setPurchaseId("");
//                        UserPreference.getInstance().setFinalPrice("");
//                        UserPreference.getInstance().setIsNotificationClicked(false);
//                    }
//                    CommonUtility.showAlert(mContext, mContext.getResources().getString(R.string.order_success_text));
//                } else if (status.equals("cancel")) {
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                CommonUtility.showAlert(mContext, mContext.getResources().getString(R.string.order_fail_text));
//
//                if (status.equals("confirmed")) {
//                    if (((HomeActivity) mContext).checkInternet()) {
//                        my_productLists.clear();
//                        UserPreference.getInstance().setCartCount("0");
//                        purchaseId = "";
//                        UserPreference.getInstance().setPurchaseId("");
//                        UserPreference.getInstance().setFinalPrice("");
//                        UserPreference.getInstance().setIsNotificationClicked(false);
//                    }
//
//                }
//            }
//        });
//        twoTapApi.updatecarttwotapstatus(UserPreference.getInstance().getUserID(), purchase_id, cartid, finalprice, status);
//        twoTapApi.execute();
//    }

//    private String cartid;
//    private Runnable runnable;
//    private Handler handler = new Handler();

//    private void getCartId() {
//        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(object.toString());
//                    cartid = (String) jsonObject.get("cart_id");
//                    callPurchase();
//                    runnable = new Runnable() {
//
//                        @Override
//                        public void run() {
//                            getStatus(cartid);
//                        }
//                    };
//                    handler.postDelayed(runnable, 5000);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//            }
//        });
//        List<String> products = new ArrayList<String>();
//        for (int i = 0; i < my_productLists.size(); i++) {
//            products.add(my_productLists.get(i).getProducturl());
//        }
//        twoTapApi.getCartId(products);
//        twoTapApi.execute();
//    }

    boolean isLoadingCartStatus = false;

//    private synchronized void getStatus(final String cart_id) {
//        if (!isLoadingCartStatus)
//            return;
//        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                try {
//                    JSONObject object2 = new JSONObject(object.toString());
//                    String message = object2.getString("message");
//                    if (message.equalsIgnoreCase("has_failures") || message.equalsIgnoreCase("done")) {
//                        isLoadingCartStatus = true;
//                        AlertUtils.showToast(mContext, message);
//                    } else {
//                        getStatus(cart_id);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                isLoadingCartStatus = true;
//                String message = "Purchase failed";
//                AlertUtils.showToast(mContext, message);
//            }
//        });
//        twoTapApi.getCartStatus(cart_id);
//        twoTapApi.execute();
//    }

//    private void addNewCard(String cardNumber, String cardHolderName, String expiryDate, String cvv, String cardtype) {
//        final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                if (object != null) {
//                    AlertUtils.showToast(mContext, "Card added");
//                    getCardList();
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (object != null) {
//                    CardInfoRes cardInfoRes = (CardInfoRes) object;
//                    AlertUtils.showToast(mContext, cardInfoRes.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//        });
//        cardInfoApi.addCard(UserPreference.getInstance().getUserID(), CommonUtility.EncryptCreditCard(cardNumber), cardHolderName, expiryDate, cvv, cardtype);
//        cardInfoApi.execute();
//    }

//    public void voidAuthorization(String authId) {
//        if (checkInternet2()) {
//            OAuthWebService authWebService = new OAuthWebService(Constants.WebConstants.PAYPAL_VOID_URL + authId + "/void", new ServiceCallback() {
//                @Override
//                public void handleOnSuccess(Object object) {
//                    try {
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void handleOnFailure(ServiceException exception, Object object) {
//                    if (object != null)
//                        AlertUtils.showToast(mContext, object.toString());
//                    else
//                        AlertUtils.showToast(mContext, "Unable to void payment");
//
//                }
//            });
//            authWebService.setRequest(null, null);
//            authWebService.execute();
//        }
//    }

//    public void validateCard(final Card card, BillingAddress address) {
//        if (checkInternet()) {
//            OAuthWebService authWebService = new OAuthWebService(Constants.WebConstants.PAYPAL_PAYMENT_URL, new ServiceCallback() {
//                @Override
//                public void handleOnSuccess(Object object) {
//                    try {
//                        JSONObject jsonObj = new JSONObject(object.toString());
//                        if (jsonObj.optString("state").equalsIgnoreCase("approved")) {
//                            JSONObject jsonObj2 = jsonObj.getJSONArray("transactions").getJSONObject(0);
//                            String id = jsonObj2.getJSONArray("related_resources").getJSONObject(0).getJSONObject("authorization").optString("id");
//                            addNewCard(card.getCard_number(), card.getName_on_card(), card.getExpirationMonth() + "/" + card.getExpirationYear(), card.getCvv(), card.getCardtype());
//                            voidAuthorization(id);
//                        } else {
//                            AlertUtils.showToast(mContext, "Please enter valid card");
//                            add_card_done_button.setVisibility(View.VISIBLE);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void handleOnFailure(ServiceException exception, Object object) {
//                    if (object != null)
//                        AlertUtils.showToast(mContext, object.toString());
//                    else
//                        AlertUtils.showToast(mContext, "Please enter valid card");
//                    add_card_done_button.setVisibility(View.VISIBLE);
//
//                }
//            });
//            authWebService.setRequest(card, address);
//            authWebService.execute();
//        }
//    }

//    public void deleteCard(String cardId, final View view1, final View view2) {
//
//        final CardInfoApi service = new CardInfoApi(new ServiceCallback() {
//            @Override
//            public void handleOnSuccess(Object object) {
//                if (object != null) {
//                    cards_scroll2.removeView(view1);
//                    cards_list_layout.removeView(view2);
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (object != null) {
//                    CardInfoRes response = (CardInfoRes) object;
//                    AlertUtils.showToast(mContext, response.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//
//            }
//        });
//        service.removeCard(UserPreference.getInstance().getUserID(), cardId);
//        service.execute();
//    }

//    public void getAddressList() {
//        new_address_layout.setVisibility(View.GONE);
//        saved_address_layout.setVisibility(View.VISIBLE);
//        add_new_address_text.setText("Add New Address");
//        final AddressApi addressApi = new AddressApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                if (object != null) {
//                    AddressRes addressRes = (AddressRes) object;
//                    List<Address> data = addressRes.getData();
//                    saved_address_layout.removeAllViews();
//                    if (data.size() > 0)
//                        saved_address_layout.addView(new ShippingAddressList(mContext, addressRes.getData(), fragmentInspirationSection).getView());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (object != null) {
//                    AddressRes addressRes = (AddressRes) object;
//                    AlertUtils.showToast(mContext, addressRes.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//        });
//        addressApi.getAddressList(UserPreference.getInstance().getUserID());
//        addressApi.execute();
//    }

//    private void addNewAddress() {
//        final AddressApi addressApi = new AddressApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                if (object != null) {
//                    AddressRes addressRes = (AddressRes) object;
//                    AlertUtils.showToast(mContext, addressRes.getMessage());
//                    getAddressList();
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (object != null) {
//                    AddressRes response = (AddressRes) object;
//                    AlertUtils.showToast(mContext, response.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//        });
//        //need to change "title" and "tel" if necessary
//        addressApi.addAddress(UserPreference.getInstance().getUserID(), "",
//                shippingAddress.getFirstname(), shippingAddress.getLastname(), shippingAddress.getState(), "", shippingAddress.getStreet_address(), shippingAddress.getCity_town(), shippingAddress.getZip_code(), shippingAddress.getCountry(), shippingAddress.getApartment());
//        addressApi.execute();
//    }

//    public void setAddress(Address address) {
//        this.shippingAddress = address;
//        isValidAddress = true;
//        cardAndShippingDetail.setShipping_title(shippingAddress.getTitle());
//        cardAndShippingDetail.setShipping_first_name(shippingAddress.getFirstname());
//        cardAndShippingDetail.setShipping_last_name(shippingAddress.getLastname());
//        cardAndShippingDetail.setShipping_city(shippingAddress.getCity_town());
//        cardAndShippingDetail.setShipping_country(shippingAddress.getCountry());
//        cardAndShippingDetail.setShipping_address(shippingAddress.getStreet_address() + " " + shippingAddress.getApartment());
//        cardAndShippingDetail.setShipping_zip(shippingAddress.getZip_code());
//        cardAndShippingDetail.setShipping_telephone(shippingAddress.getTel());
//        cardAndShippingDetail.setShipping_state(shippingAddress.getState());
//        cardAndShippingDetail.setBilling_title(shippingAddress.getTitle());
//        cardAndShippingDetail.setBilling_first_name(shippingAddress.getFirstname());
//        cardAndShippingDetail.setBilling_last_name(shippingAddress.getLastname());
//        cardAndShippingDetail.setBilling_city(shippingAddress.getCity_town());
//        cardAndShippingDetail.setBilling_country(shippingAddress.getCountry());
//        cardAndShippingDetail.setBilling_address(shippingAddress.getStreet_address() + " " + shippingAddress.getApartment());
//        cardAndShippingDetail.setBilling_zip(shippingAddress.getZip_code());
//        cardAndShippingDetail.setBilling_telephone(shippingAddress.getTel());
//        cardAndShippingDetail.setBilling_state(shippingAddress.getState());
//        cardAndShippingDetail.setEmail(UserPreference.getInstance().getEmail());
//        for (int i = 0; i < my_productLists.size(); i++) {
//            my_productLists.get(i).setFreeShipping(false);
//        }
//    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            String text = searchText.getText().toString();
            if (text == null || text.length() == 0) {
                Log.e("SearchProduct", "text_change" + UserPreference.getInstance().getSearchIndex());
//                if(!overflow2.isOpen()){
//                    searchText.setCursorVisible(false);
//                    searchText.setFocusable(false);
//                }
                //  ((HomeActivity) mContext).myAddFragment(new SearchProductFragment(overflow2, fragmentInspirationSection, original_text, false));

            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

//    public void getCardList() {
//        flatlay_card.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (CommonUtility.getDeviceWidth(mContext)) * 51 / 100));
//        final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {
//
//            @Override
//            public void handleOnSuccess(Object object) {
//                if (object != null) {
//                    CardInfoRes cardInfoRes = (CardInfoRes) object;
//                    List<Card> temp = new ArrayList<>();
//                    List<Card> temp2 = new ArrayList<>();
//                    temp = cardInfoRes.getData();
//                    setCardList(temp);
//                    temp2 = temp;
//                    temp2.add(new Card("flatlay", "flatlay", "flatlay", "", "", "", "", ""));
//                    temp2.add(new Card("Add", "Add", "Add", "", "", "", "", ""));
//                    final List<Card> temp3 = temp2;
//                    card_gridview.setAdapter(new CardGridAdapter(mContext, temp3, new CardGridAdapter.CardGridListener() {
//                        @Override
//                        public void onClickButton(int position) {
//                            String card_num = temp3.get(position).getCard_number();
//                            String card_name = temp3.get(position).getName_on_card();
//                            String exp_month = "";
//                            String exp_year = "";
//                            cardAndShippingDetail.setCard_number(CommonUtility.DecryptCreditCard(card_num));
//                            cardAndShippingDetail.setCard_type(temp3.get(position).getCardtype());
//                            cardAndShippingDetail.setCard_name(card_name);
//                            if (card_name.equals("flatlay")) {
//                                ccv_text3.setVisibility(View.GONE);
//                                enter_expire_text2.setVisibility(View.GONE);
//                                enter_card_num.setVisibility(View.GONE);
//                                add_card_done_button.setVisibility(View.GONE);
//                                enter_name_on_card2.setText(card_name);
//                                isValidPayment = true;
//                            } else if (card_name.equals("Add")) {
//                                ccv_text3.setVisibility(View.VISIBLE);
//                                ccv_text3.setText("");
//                                enter_expire_text2.setVisibility(View.VISIBLE);
//                                enter_expire_text2.setText("");
//                                enter_name_on_card2.setVisibility(View.VISIBLE);
//                                enter_name_on_card2.setText("");
//                                enter_card_num.setVisibility(View.VISIBLE);
//                                enter_card_num.setText("");
//                                add_card_done_button.setVisibility(View.VISIBLE);
//                            } else if (card_name.equals("paypal")) {
//                                ccv_text3.setVisibility(View.GONE);
//                                enter_expire_text2.setVisibility(View.GONE);
//                                enter_card_num.setVisibility(View.GONE);
//                                add_card_done_button.setVisibility(View.GONE);
//                                enter_name_on_card2.setText(card_name);
//                                isValidPayment = true;
//                            } else {
//                                add_card_done_button.setVisibility(View.GONE);
//                                ccv_text3.setVisibility(View.VISIBLE);
//                                exp_month = temp3.get(position).getExpiration_date().split("/")[0];
//                                exp_year = temp3.get(position).getExpiration_date().split("/")[1];
//                                enter_expire_text2.setVisibility(View.VISIBLE);
//                                enter_card_num.setVisibility(View.VISIBLE);
//                                enter_card_num.setText(card_num);
//                                enter_expire_text2.setText(exp_month + " / " + exp_year);
//                                enter_card_num.setText(CommonUtility.DecryptCreditCard(card_num));
//                                cardAndShippingDetail.setExpiry_date_month(exp_month);
//                                cardAndShippingDetail.setExpiry_date_year(exp_year);
//                                cardAndShippingDetail.setCvv(temp3.get(position).getCvv());
//                                enter_name_on_card2.setText(card_name);
//                                ccv_text3.setText("");
//                            }
//                        }
//                    }));
//
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//
//            @Override
//            public void handleOnFailure(ServiceException exception, Object object) {
//                if (object != null) {
//                    CardInfoRes cardInfoRes = (CardInfoRes) object;
//                    AlertUtils.showToast(mContext, cardInfoRes.getMessage());
//                } else {
//                    AlertUtils.showToast(mContext, R.string.invalid_response);
//                }
//            }
//        });
//        cardInfoApi.getCardList(UserPreference.getInstance().getUserID());
//        cardInfoApi.execute();
//    }
}
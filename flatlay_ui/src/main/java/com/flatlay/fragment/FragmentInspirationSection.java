package com.flatlay.fragment;

import android.graphics.Color;
import android.os.Bundle;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
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
import com.flatlay.adapter.CustomizeInterestPeopleListAdapter;
import com.flatlay.adapter.FeaturedTabAdapter;
import com.flatlay.adapter.FragmentProfileCollectionAdapter;
import com.flatlay.adapter.InspirationAdapter;
import com.flatlay.adapter.InspirationGridAdapter;
import com.flatlay.adapter.KikrFollowingAdapter;
import com.flatlay.adapter.ProductDetailGridAdapter;
import com.flatlay.adapter.ProductSearchGridAdapter;
import com.flatlay.adapter.StoreGridAdapter;
import com.flatlay.dialog.ShareProfileDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.api.BrandListApi;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.api.DeletePostApi;
import com.flatlaylib.api.FeaturedTabApi;
import com.flatlaylib.api.GeneralSearchApi;
import com.flatlaylib.api.InspirationFeedApi;
import com.flatlaylib.api.InterestSectionApi;
import com.flatlaylib.api.LogoutApi;
import com.flatlaylib.api.MessageCenterApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.api.ProductBasedOnBrandApi;
import com.flatlaylib.bean.BrandList;
import com.flatlaylib.bean.BrandResult;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.FeaturedTabData;
import com.flatlaylib.bean.FollowerList;
import com.flatlaylib.bean.FollowingKikrModel;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProductResult;
import com.flatlaylib.bean.ProfileCollectionList;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.bean.UserResult;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.BrandListRes;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.service.res.FeaturedTabApiRes;
import com.flatlaylib.service.res.GeneralSearchRes;
import com.flatlaylib.service.res.InspirationFeedRes;
import com.flatlaylib.service.res.InterestSectionRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.service.res.ProductBasedOnBrandRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.StringUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.plus.model.people.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RachelDi on 1/22/18.
 */

public class FragmentInspirationSection extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ServiceCallback, MultiAutoCompleteTextView.OnEditorActionListener {
    private View mainView, loaderView, highlight3, highlight2, highlight1;
    private ListView inspirationlist, collectionList, user_result_list;
    private RecyclerView notificationlist;
    private double lowprice = 0, highprice = Double.MAX_VALUE;
    private String filter2 = "", filter3 = "", category = "", inspiration_id, userId, user_id, viewer_id, original_text = "";
    private EditText searchText;
    private List<Inspiration> product_list = new ArrayList<Inspiration>(), myProductList = new ArrayList<Inspiration>(), myDetailProductList = new ArrayList<Inspiration>();
    private TextView loadingTextView, nameText, text1, text2, text3, text4, text5, performanceText, peopletext, productsText, shopText, followertext1, followingtext1, followertext2, followingtext2, notification_text, message_text, invite_text, clothing_text, music_text, pet_text, games_text, toys_text, computer_text, baby_text, sports_text, health_text, jewelry_text, electronics_text, filter_text, apply_text, price_text_750, price_text_500, price_text_200, shoes_text, price_text_150, price_text_50, price_text_25, price_text_0, price_text, sale_text, men_text, accessories_text, women_text, price_text_100;
    private boolean isViewAll, isInfo = false, isBell = false, isLoading = false, myins_isloding = false, my_detail_ins_isloding = false, mycoll_isloading = false, user_list_isloding = false, isFirstTime_user_list = true, isFirstTime_shop_list = true, isFirstTime_product_list = true, shop_list_isloding = false, product_list_isloding = false, needToUpdate_people = false, needToUpdate_product = false, needToUpdate_shop = false, isFirstTime = true, isFirstTimeFromMain = false, isFirstTime_mypos = true, isFirstTime_mypos_detail = true, isFirstTime_myCol = true, isOnFeedPage = true;
    private int ppsIndex = 0, page = 0, page1 = 0, page2 = 0, page3 = 0, page4 = 0, page5 = 0, page6 = 0, index = 0, firstVisibleItem = 0, visibleItemCount = 0, totalItemCount = 0, firstVisibleItem2 = 0, visibleItemCount2 = 0, totalItemCount2 = 0, firstVisibleItem3 = 0, visibleItemCount3 = 0, totalItemCount3 = 0, firstVisibleItem4 = 0, visibleItemCount4 = 0, totalItemCount4 = 0, firstVisibleItem5 = 0, visibleItemCount5 = 0, totalItemCount5 = 0, firstVisibleItem6 = 0, visibleItemCount6 = 0, totalItemCount6 = 0, firstVisibleItem7 = 0, visibleItemCount7 = 0, totalItemCount7 = 0, collAdapterIndex = -1, feedAdapterIndex = -1, indexSearch = 0;
    private DeletePostApi deletePostApi;
    private List<ProfileCollectionList> collectionLists = new ArrayList<ProfileCollectionList>();
    private List<CollectionList> collectionLists2;
    public static boolean isPostUpload = false;
    private ImageView image_upload_post, image_upload_post2, image_camera, image_camera2, info_image, bell_image, info_image2, infoicon2, filter_icon, filter_icon2;
    private RelativeLayout tab_layout1, bottomLayout, uploadChoice, bell_icon, info_icon, overflow_layout1, overflow_layout2, overflow_layout3, overflow_layout4, products_filter_layout, products_result_layout, peopletext_layout, productsText_layout, shopText_layout;
    private MyMaterialContentOverflow3 overflow2;
    private CircleImageView profile_pic, prof2;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout fashioniconlayout, beautyiconlayout, fitnessiconlayout, foodiconlayout, techiconlayout, photographyiconlayoutlayout, homeiconlayout, occasioniconlayout, traveliconlayout, blanklayout, cart_tab, search_tab, profile_tab, clothing_layout, shoes_layout, electronics_layout, jewelry_layout, health_layout, sports_layout, baby_layout, computer_layout, toy_layout, game_layout, pet_layout, music_layout, people_layout, products_layout, shops_layout;
    private GridView imagesList, feedDetail, productDetail, shopList, product_result_list;
    private Button button1, button2;
    private InspirationAdapter inspirationAdapter;
    private FragmentProfileCollectionAdapter collectionAdapter;
    private FragmentProfileView fragmentProfileView;
    private InspirationGridAdapter inspirationGridAdapter, inspirationGridAdapter2;
    private ProductDetailGridAdapter productDetailGridAdapter;
    private ProductSearchGridAdapter productSearchdAdapter;
    private List<FollowerList> followersLists = new ArrayList<FollowerList>(), followingLists = new ArrayList<FollowerList>();
    private List<UserData> userDetails;
    private List<Product> product_data;
    private List<ProductResult> interestProductList = new ArrayList<>(), temporaryProoducts = new ArrayList<>();
    private List<FollowingKikrModel.DataBean> followinglist = new ArrayList<>();
    private List<FollowingKikrModel.DataBean> followinglistRefined = new ArrayList<>();
    private ShareProfileDialog shareProfileDialog;
    private InputMethodManager imm;
    private List<LinearLayout> gones, visibles, invisibles;
    private List<TextView> group1, group2;
    private List<UserResult> interestUserList = new ArrayList<>();
    private List<FeaturedTabData> interestList = new ArrayList<>();
    private CustomizeInterestPeopleListAdapter interestPeopleListAdapter;
    private FeaturedTabAdapter featuredTabAdapter;
    private List<BrandResult> interestShopList = new ArrayList<>();
    private StoreGridAdapter storeGridAdapter;
    private AllBrandGridAdapter allBrandGridAdapter;
    private List<BrandList> brandLists = new ArrayList<>();
    private List<Inspiration> fashionList = new ArrayList<>(), beautyList = new ArrayList<>(), fitnessList = new ArrayList<>(), foodList = new ArrayList<>(), travelList = new ArrayList<>(), techList = new ArrayList<>(), photoList = new ArrayList<>(), homeList = new ArrayList<>(), occasioList = new ArrayList<>(), currentList = new ArrayList<>();

    public FragmentInspirationSection() {

        this.isViewAll = true;
        isFirstTimeFromMain = true;
        this.userId = UserPreference.getInstance().getUserID();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_inspiration_section, null);
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
            inspirationAdapter = new InspirationAdapter(mContext, currentList, isViewAll, FragmentInspirationSection.this);
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

//    protected void showReloadFotter() {
//        TextView textView = getReloadFotter();
//        textView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (((HomeActivity) mContext).checkInternet()) {
//                    page++;
//                    isFirstTime = false;
//                    getInspirationFeedList();
//                }
//            }
//        });
//    }

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

    public void updateCategoryProducts(String category) {
        lowprice = 0;
        highprice = Double.MAX_VALUE;
        temporaryProoducts = categoryFilter(interestProductList, category);
        if (productSearchdAdapter != null) {
            productSearchdAdapter.setData(temporaryProoducts);
            productSearchdAdapter.notifyDataSetChanged();
        }
    }

    public void updateFilterProducts(String category, String filter2, String filter3, double lowprice, double highprice) {
        temporaryProoducts = buttonPriceFilter(interestProductList, category, filter2, filter3, lowprice, highprice);
        if (productSearchdAdapter != null) {
            productSearchdAdapter.setData(temporaryProoducts);
            productSearchdAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
            CommonUtility.hideSoftKeyboard(mContext);
            searchText.setCursorVisible(false);
//            if (!original_text.equals(searchText.getText().toString().trim())) {
//                needToUpdate_people = true;
//                needToUpdate_product = true;
//                needToUpdate_shop = true;
//            }
            original_text = searchText.getText().toString().trim();
            if (!original_text.equals("")) {
                if (indexSearch == 2) {
                    storeGridAdapter = null;
                    shop_list_isloding = false;
                    page5 = 0;
                    interestShopList.clear();
                    isFirstTime_shop_list = true;

                    displaySearchShopResult();
                }
                if (indexSearch == 3) {
                    Log.e("here", "1");
//                    productSearchdAdapter = null;
                    product_list_isloding = false;
                    page6 = 0;
                    interestProductList.clear();
                    isFirstTime_product_list = true;
                    products_filter_layout.setVisibility(View.GONE);
                    products_layout.setVisibility(View.GONE);
                    products_result_layout.setVisibility(View.VISIBLE);
                    displaySearchProductResult();
                } else {
                    interestPeopleListAdapter = null;
                    user_list_isloding = false;
                    page4 = 0;
                    interestUserList.clear();
                    isFirstTime_user_list = true;
                    displaySearchPeopleResult();
                }
            } else {
                Toast.makeText(mContext, "Please enter keywords", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.people_text_layout:
                overflow2.setOpen();
                searchText.setText("");
                people_layout.setVisibility(View.VISIBLE);
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.GONE);
                products_filter_layout.setVisibility(View.GONE);
                shops_layout.setVisibility(View.GONE);
                highlight1.setVisibility(View.VISIBLE);
                highlight2.setVisibility(View.INVISIBLE);
                highlight3.setVisibility(View.INVISIBLE);
                indexSearch = 1;
                ppsIndex = 0;
//                if (needToUpdate_people) {
//                    user_list_isloding = false;
//                    page4 = 0;
//                    interestUserList.clear();
////                    indexSearch = 1;
//                    isFirstTime_user_list = true;
//                    displaySearchPeopleResult();
//                    needToUpdate_people = false;
//                }
                break;
            case R.id.products_text_layout:
                overflow2.setOpen();
                ppsIndex = 1;
                searchText.setText("");
                people_layout.setVisibility(View.GONE);
                products_layout.setVisibility(View.VISIBLE);
                products_result_layout.setVisibility(View.GONE);
                products_filter_layout.setVisibility(View.GONE);
                shops_layout.setVisibility(View.GONE);
                highlight1.setVisibility(View.INVISIBLE);
                highlight2.setVisibility(View.VISIBLE);
                highlight3.setVisibility(View.INVISIBLE);
                indexSearch = 3;
                Log.e("here", "4");
//                if (needToUpdate_product) {
//                    product_list_isloding = false;
//                    page6 = 0;
//                    interestProductList.clear();
//                    isFirstTime_product_list = true;
//                    Log.e("here", "5");
//                    displaySearchProductResult();
//                    needToUpdate_product = false;
//                }
                break;
            case R.id.shop_text_layout:
                overflow2.setOpen();
                ppsIndex = 2;
                searchText.setText("");
                people_layout.setVisibility(View.GONE);
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.GONE);
                products_filter_layout.setVisibility(View.GONE);
                shops_layout.setVisibility(View.VISIBLE);
                highlight1.setVisibility(View.INVISIBLE);
                highlight2.setVisibility(View.INVISIBLE);
                highlight3.setVisibility(View.VISIBLE);
                if (indexSearch != 2) displayAllShops();
                indexSearch = 2;
//                if (searchText.getText().toString().trim().length() <= 0) {
//                    page5 = 0;
////                    allBrandGridAdapter = null;
//                    brandLists.clear();
//                    isFirstTime_shop_list = true;
//                    displayAllShops();
//                }
//                if (needToUpdate_shop) {
//                    shop_list_isloding = false;
//                    page5 = 0;
//                    storeGridAdapter = null;
//                    interestShopList.clear();
//                    isFirstTime_shop_list = true;
//                    displaySearchShopResult();
//                    needToUpdate_shop = false;
//                }
                break;
            case R.id.invite_text:
                break;
            case R.id.clothing_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "clothing";
//                updateCategoryProducts(category);
                break;
            case R.id.shoes_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "shoe";
//                updateCategoryProducts(category);
                break;
            case R.id.electronics_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "electronic";
//                updateCategoryProducts(category);
                break;
            case R.id.jewelry_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "jewelry";
//                updateCategoryProducts(category);
                break;
            case R.id.health_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "health";
//                updateCategoryProducts(category);
                break;
            case R.id.sports_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "sport";
//                updateCategoryProducts(category);
                break;
            case R.id.baby_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "baby";
//                updateCategoryProducts(category);
                break;
            case R.id.computer_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "computer";
//                updateCategoryProducts(category);
                break;
            case R.id.toy_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "toy";
//                updateCategoryProducts(category);
                break;
            case R.id.game_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "game";
//                updateCategoryProducts(category);
                break;
            case R.id.pet_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "computer";
//                updateCategoryProducts(category);
                break;
            case R.id.music_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                category = "music";
//                updateCategoryProducts(category);
                break;
            case R.id.women_text:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(men_text, accessories_text, sale_text));
                group2 = new ArrayList<>(Arrays.asList(women_text));
                setBackgroundByGroup(group1, R.drawable.white_button_noborder, group2, R.drawable.green_corner_button);
                setTextColorByGroup(group1, Color.BLACK, group2, Color.WHITE);
                filter2 = "women";
                break;
            case R.id.accessories_text:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(men_text, women_text, sale_text));
                group2 = new ArrayList<>(Arrays.asList(accessories_text));
                setBackgroundByGroup(group1, R.drawable.white_button_noborder, group2, R.drawable.green_corner_button);
                setTextColorByGroup(group1, Color.BLACK, group2, Color.WHITE);
                filter2 = "accesso";
                break;
            case R.id.men_text:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(accessories_text, women_text, sale_text));
                group2 = new ArrayList<>(Arrays.asList(men_text));
                setBackgroundByGroup(group1, R.drawable.white_button_noborder, group2, R.drawable.green_corner_button);
                setTextColorByGroup(group1, Color.BLACK, group2, Color.WHITE);
                filter2 = "men";
                filter3 = "boy";
                break;
            case R.id.sale_text:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(accessories_text, women_text, men_text));
                group2 = new ArrayList<>(Arrays.asList(sale_text));
                setBackgroundByGroup(group1, R.drawable.white_button_noborder, group2, R.drawable.green_corner_button);
                setTextColorByGroup(group1, Color.BLACK, group2, Color.WHITE);
                filter2 = "sale";
                break;
            case R.id.price_text_0:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(price_text_25, price_text_50, price_text_100, price_text_150, price_text_200, price_text_500, price_text_750));
                group2 = new ArrayList<>(Arrays.asList(price_text_0));
                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
                lowprice = 0;
                highprice = 25;
                break;
            case R.id.price_text_25:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_50, price_text_100, price_text_150, price_text_200, price_text_500, price_text_750));
                group2 = new ArrayList<>(Arrays.asList(price_text_25));
                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
                lowprice = 25;
                highprice = 50;
                break;
            case R.id.price_text_50:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_100, price_text_150, price_text_200, price_text_500, price_text_750));
                group2 = new ArrayList<>(Arrays.asList(price_text_50));
                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
                lowprice = 50;
                highprice = 100;
                break;
            case R.id.price_text_100:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_150, price_text_200, price_text_500, price_text_750));
                group2 = new ArrayList<>(Arrays.asList(price_text_100));
                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
                lowprice = 100;
                highprice = 150;
                break;
            case R.id.price_text_150:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_100, price_text_200, price_text_500, price_text_750));
                group2 = new ArrayList<>(Arrays.asList(price_text_150));
                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
                lowprice = 150;
                highprice = 200;
                break;
            case R.id.price_text_200:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_100, price_text_150, price_text_500, price_text_750));
                group2 = new ArrayList<>(Arrays.asList(price_text_200));
                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
                lowprice = 200;
                highprice = 500;
                break;
            case R.id.price_text_500:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_100, price_text_150, price_text_200, price_text_750));
                group2 = new ArrayList<>(Arrays.asList(price_text_500));
                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
                lowprice = 500;
                highprice = 750;
                break;
            case R.id.price_text_750:
                overflow2.setOpen();
                group1 = new ArrayList<>(Arrays.asList(price_text_0, price_text_25, price_text_50, price_text_100, price_text_150, price_text_200, price_text_500));
                group2 = new ArrayList<>(Arrays.asList(price_text_750));
                setBackgroundByGroup(group1, 0, group2, R.drawable.green_corner_button);
                lowprice = 750;
                highprice = Double.MAX_VALUE;
                break;
            case R.id.apply_text:
                overflow2.setOpen();
//                products_layout.setVisibility(View.GONE);
                products_filter_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                updateFilterProducts(category, filter2, filter3, lowprice, highprice);
                break;
            case R.id.filter_icon:
                overflow2.setOpen();
                products_result_layout.setVisibility(View.GONE);
                products_filter_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.filter_icon2:
                overflow2.setOpen();
                products_result_layout.setVisibility(View.VISIBLE);
                products_filter_layout.setVisibility(View.GONE);
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
                overflow_layout3.setVisibility(View.GONE);
                overflow_layout2.setVisibility(View.GONE);
                overflow_layout4.setVisibility(View.GONE);
                overflow_layout1.setVisibility(View.VISIBLE);
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

            case R.id.bell_icon:
                overflow2.setOpen();
                overflow_layout1.setVisibility(View.GONE);
                overflow_layout2.setVisibility(View.GONE);
                overflow_layout4.setVisibility(View.GONE);
                overflow_layout3.setVisibility(View.VISIBLE);
                getFollowingInstagramList();
                break;

            case R.id.info_icon:
                overflow2.setOpen();
                overflow_layout1.setVisibility(View.GONE);
                overflow_layout3.setVisibility(View.GONE);
                overflow_layout4.setVisibility(View.GONE);
                overflow_layout2.setVisibility(View.VISIBLE);
                isInfo = true;
                break;

            case R.id.info_image2:
                overflow2.setOpen();
                overflow_layout2.setVisibility(View.GONE);
                overflow_layout3.setVisibility(View.GONE);
                overflow_layout4.setVisibility(View.GONE);
                overflow_layout1.setVisibility(View.VISIBLE);
                isInfo = false;
                break;

            case R.id.text1:
                overflow2.setOpen();
                shareProfileDialog.show();
                break;

            case R.id.text2:
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_edit_profile", true);
                startActivity(EditProfileActivity.class, bundle);
                break;

            case R.id.text3:
                overflow2.setOpen();
                info_image.setImageResource(R.drawable.info_teal);
                overflow_layout2.setVisibility(View.GONE);
                overflow_layout3.setVisibility(View.GONE);
                overflow_layout1.setVisibility(View.VISIBLE);
                overflow_layout4.setVisibility(View.GONE);
                collAdapterIndex = 1;
                feedAdapterIndex = 1;
                performanceText.setVisibility(View.VISIBLE);
                nameText.setVisibility(View.GONE);
                myDetailProductList.clear();
                page3 = 0;
                getMyInspirationFeedList();
                getCollectionList();
                isInfo = true;
                break;

            case R.id.text4:
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("isFromHome", true);
                startActivity(FollowCategoriesNewActivity.class, bundle2);
                break;

            case R.id.text5:
                logoutUser();
                startActivity(BaseActivityWithVideo.class);
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        shareProfileDialog = new ShareProfileDialog(mContext);
        tab_layout1 = (RelativeLayout) mainView.findViewById(R.id.tabLayout1);
        bell_icon = (RelativeLayout) mainView.findViewById(R.id.bell_icon);
        info_icon = (RelativeLayout) mainView.findViewById(R.id.info_icon);
        overflow_layout1 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout1);
        overflow_layout2 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout2);
        overflow_layout3 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout3);
        overflow_layout4 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout4);
        inspirationlist = (ListView) mainView.findViewById(R.id.inspirationlist);
        loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
        loadingTextView.setTypeface(FontUtility.setMontserratLight(getActivity()));
        image_upload_post = (ImageView) mainView.findViewById(R.id.image_upload_post);
        image_upload_post2 = (ImageView) mainView.findViewById(R.id.image_upload_post2);
        uploadChoice = (RelativeLayout) mainView.findViewById(R.id.uploadChoice);
        image_camera = (ImageView) mainView.findViewById(R.id.image_camera);
        image_camera2 = (ImageView) mainView.findViewById(R.id.image_camera);
        info_image = (ImageView) mainView.findViewById(R.id.info_image);
        bell_image = (ImageView) mainView.findViewById(R.id.bell_image);
        info_image2 = (ImageView) mainView.findViewById(R.id.info_image2);
        filter_icon = (ImageView) mainView.findViewById(R.id.filter_icon);
        filter_icon2 = (ImageView) mainView.findViewById(R.id.filter_icon2);
        infoicon2 = (ImageView) mainView.findViewById(R.id.infoicon2);
        bottomLayout = (RelativeLayout) mainView.findViewById(R.id.bottomLayout);
        profile_tab = (LinearLayout) mainView.findViewById(R.id.profile_tab);
        profile_pic = (CircleImageView) mainView.findViewById(R.id.profile_pic);
        search_tab = (LinearLayout) mainView.findViewById(R.id.search_tab);
        searchText = (EditText) mainView.findViewById(R.id.search_tab_text);
        searchText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        searchText.setOnEditorActionListener(this);
        searchText.setCursorVisible(false);
        searchText.setFocusable(false);
        searchText.addTextChangedListener(new MyTextWatcher());
        searchText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    if (overflow_layout4.getVisibility() == View.GONE || !overflow2.isOpen()) {
                        searchText.setCursorVisible(false);
                        searchText.setFocusable(false);
                        overflow2.triggerSlide();
                        overflow_layout1.setVisibility(View.GONE);
                        overflow_layout2.setVisibility(View.GONE);
                        overflow_layout3.setVisibility(View.GONE);
                        overflow_layout4.setVisibility(View.VISIBLE);
                    } else {
                        searchText.setCursorVisible(true);
                        searchText.setFocusableInTouchMode(true);
                        searchText.requestFocus();
                    }
                }
                return false;
            }
        });

        nameText = (TextView) mainView.findViewById(R.id.nameText);
        nameText.setTypeface(FontUtility.setMontserratLight(mContext));
        performanceText = (TextView) mainView.findViewById(R.id.performance_text);
        performanceText.setTypeface(FontUtility.setMontserratLight(mContext));
        peopletext = (TextView) mainView.findViewById(R.id.people_text);
        peopletext.setTypeface(FontUtility.setMontserratLight(mContext));
        productsText = (TextView) mainView.findViewById(R.id.products_text);
        productsText.setTypeface(FontUtility.setMontserratLight(mContext));
        shopText = (TextView) mainView.findViewById(R.id.shop_text);
        shopText.setTypeface(FontUtility.setMontserratLight(mContext));
        text1 = (TextView) mainView.findViewById(R.id.text1);
        text1.setTypeface(FontUtility.setMontserratLight(mContext));
        text2 = (TextView) mainView.findViewById(R.id.text2);
        text2.setTypeface(FontUtility.setMontserratLight(mContext));
        text3 = (TextView) mainView.findViewById(R.id.text3);
        text3.setTypeface(FontUtility.setMontserratLight(mContext));
        text4 = (TextView) mainView.findViewById(R.id.text4);
        text4.setTypeface(FontUtility.setMontserratLight(mContext));
        text5 = (TextView) mainView.findViewById(R.id.text5);
        text5.setTypeface(FontUtility.setMontserratLight(mContext));
        peopletext_layout = (RelativeLayout) mainView.findViewById(R.id.people_text_layout);
        productsText_layout = (RelativeLayout) mainView.findViewById(R.id.products_text_layout);
        shopText_layout = (RelativeLayout) mainView.findViewById(R.id.shop_text_layout);
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
        music_layout = (LinearLayout) mainView.findViewById(R.id.music_layout);
        pet_layout = (LinearLayout) mainView.findViewById(R.id.pet_layout);
        game_layout = (LinearLayout) mainView.findViewById(R.id.game_layout);
        toy_layout = (LinearLayout) mainView.findViewById(R.id.toy_layout);
        computer_layout = (LinearLayout) mainView.findViewById(R.id.computer_layout);
        baby_layout = (LinearLayout) mainView.findViewById(R.id.baby_layout);
        sports_layout = (LinearLayout) mainView.findViewById(R.id.sports_layout);
        health_layout = (LinearLayout) mainView.findViewById(R.id.health_layout);
        jewelry_layout = (LinearLayout) mainView.findViewById(R.id.jewelry_layout);
        electronics_layout = (LinearLayout) mainView.findViewById(R.id.electronics_layout);
        shoes_layout = (LinearLayout) mainView.findViewById(R.id.shoes_layout);
        clothing_layout = (LinearLayout) mainView.findViewById(R.id.clothing_layout);
        highlight1 = (View) mainView.findViewById(R.id.highlight1);
        highlight2 = (View) mainView.findViewById(R.id.highlight2);
        highlight3 = (View) mainView.findViewById(R.id.highlight3);
        people_layout = (LinearLayout) mainView.findViewById(R.id.people_layout);
        products_layout = (LinearLayout) mainView.findViewById(R.id.products_layout);
        products_filter_layout = (RelativeLayout) mainView.findViewById(R.id.products_filter_layout);
        products_result_layout = (RelativeLayout) mainView.findViewById(R.id.products_result_layout);
        shops_layout = (LinearLayout) mainView.findViewById(R.id.shops_layout);
        shoes_layout = (LinearLayout) mainView.findViewById(R.id.shoes_layout);
        swipeLayout = (SwipeRefreshLayout) mainView.findViewById(R.id.swipe_container);
        overflow2 = (MyMaterialContentOverflow3) mainView.findViewById(R.id.overflow2);
        Tracker t = ((KikrApp) mContext.getApplication()).getTracker(KikrApp.TrackerName.APP_TRACKER);
        t.setScreenName("Fragment Inspiration Section");
        t.send(new HitBuilders.ScreenViewBuilder().build());
        followingtext1 = (TextView) mainView.findViewById(R.id.followingtext1);
        followingtext1.setTypeface(FontUtility.setMontserratLight(mContext));
        followertext1 = (TextView) mainView.findViewById(R.id.followertext1);
        followertext1.setTypeface(FontUtility.setMontserratLight(mContext));
        followingtext2 = (TextView) mainView.findViewById(R.id.followingtext2);
        followingtext2.setTypeface(FontUtility.setMontserratLight(mContext));
        notification_text = (TextView) mainView.findViewById(R.id.notification_text);
        notification_text.setTypeface(FontUtility.setMontserratLight(mContext));
        message_text = (TextView) mainView.findViewById(R.id.message_text);
        message_text.setTypeface(FontUtility.setMontserratLight(mContext));
        invite_text = (TextView) mainView.findViewById(R.id.invite_text);
        invite_text.setTypeface(FontUtility.setMontserratLight(mContext));
        clothing_text = (TextView) mainView.findViewById(R.id.clothing_text);
        clothing_text.setTypeface(FontUtility.setMontserratLight(mContext));
        shoes_text = (TextView) mainView.findViewById(R.id.shoes_text);
        shoes_text.setTypeface(FontUtility.setMontserratLight(mContext));
        electronics_text = (TextView) mainView.findViewById(R.id.electronics_text);
        electronics_text.setTypeface(FontUtility.setMontserratLight(mContext));
        jewelry_text = (TextView) mainView.findViewById(R.id.jewelry_text);
        jewelry_text.setTypeface(FontUtility.setMontserratLight(mContext));
        health_text = (TextView) mainView.findViewById(R.id.health_text);
        health_text.setTypeface(FontUtility.setMontserratLight(mContext));
        sports_text = (TextView) mainView.findViewById(R.id.sports_text);
        sports_text.setTypeface(FontUtility.setMontserratLight(mContext));
        baby_text = (TextView) mainView.findViewById(R.id.baby_text);
        baby_text.setTypeface(FontUtility.setMontserratLight(mContext));
        computer_text = (TextView) mainView.findViewById(R.id.computer_text);
        computer_text.setTypeface(FontUtility.setMontserratLight(mContext));
        toys_text = (TextView) mainView.findViewById(R.id.toys_text);
        toys_text.setTypeface(FontUtility.setMontserratLight(mContext));
        games_text = (TextView) mainView.findViewById(R.id.games_text);
        games_text.setTypeface(FontUtility.setMontserratLight(mContext));
        pet_text = (TextView) mainView.findViewById(R.id.pet_text);
        pet_text.setTypeface(FontUtility.setMontserratLight(mContext));
        music_text = (TextView) mainView.findViewById(R.id.music_text);
        music_text.setTypeface(FontUtility.setMontserratLight(mContext));
        followertext2 = (TextView) mainView.findViewById(R.id.followertext2);
        followertext2.setTypeface(FontUtility.setMontserratLight(mContext));
        filter_text = (TextView) mainView.findViewById(R.id.filter_text);
        filter_text.setTypeface(FontUtility.setMontserratLight(mContext));
        women_text = (TextView) mainView.findViewById(R.id.women_text);
        women_text.setTypeface(FontUtility.setMontserratLight(mContext));
        accessories_text = (TextView) mainView.findViewById(R.id.accessories_text);
        accessories_text.setTypeface(FontUtility.setMontserratLight(mContext));
        men_text = (TextView) mainView.findViewById(R.id.men_text);
        men_text.setTypeface(FontUtility.setMontserratLight(mContext));
        sale_text = (TextView) mainView.findViewById(R.id.sale_text);
        sale_text.setTypeface(FontUtility.setMontserratLight(mContext));
        price_text = (TextView) mainView.findViewById(R.id.price_text);
        price_text.setTypeface(FontUtility.setMontserratLight(mContext));
        price_text_0 = (TextView) mainView.findViewById(R.id.price_text_0);
        price_text_0.setTypeface(FontUtility.setMontserratLight(mContext));
        price_text_25 = (TextView) mainView.findViewById(R.id.price_text_25);
        price_text_25.setTypeface(FontUtility.setMontserratLight(mContext));
        price_text_50 = (TextView) mainView.findViewById(R.id.price_text_50);
        price_text_50.setTypeface(FontUtility.setMontserratLight(mContext));
        price_text_100 = (TextView) mainView.findViewById(R.id.price_text_100);
        price_text_100.setTypeface(FontUtility.setMontserratLight(mContext));
        price_text_150 = (TextView) mainView.findViewById(R.id.price_text_150);
        price_text_150.setTypeface(FontUtility.setMontserratLight(mContext));
        price_text_200 = (TextView) mainView.findViewById(R.id.price_text_200);
        price_text_200.setTypeface(FontUtility.setMontserratLight(mContext));
        price_text_500 = (TextView) mainView.findViewById(R.id.price_text_500);
        price_text_500.setTypeface(FontUtility.setMontserratLight(mContext));
        price_text_750 = (TextView) mainView.findViewById(R.id.price_text_750);
        price_text_750.setTypeface(FontUtility.setMontserratLight(mContext));
        apply_text = (TextView) mainView.findViewById(R.id.apply_text);
        apply_text.setTypeface(FontUtility.setMontserratLight(mContext));
        prof2 = (CircleImageView) mainView.findViewById(R.id.prof2);
        button1 = (Button) mainView.findViewById(R.id.button11);
        button1.setTypeface(FontUtility.setMontserratLight(mContext));
        button2 = (Button) mainView.findViewById(R.id.button22);
        button2.setTypeface(FontUtility.setMontserratLight(mContext));
        button1.setTextColor(Color.WHITE);
        button1.setBackgroundResource(R.drawable.green_corner_button);
        imagesList = (GridView) mainView.findViewById(R.id.imagesList);
        imagesList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem2, int visibleItemCount2, int totalItemCount2) {
                FragmentInspirationSection.this.firstVisibleItem2 = firstVisibleItem2;
                FragmentInspirationSection.this.visibleItemCount2 = visibleItemCount2;
                FragmentInspirationSection.this.totalItemCount2 = totalItemCount2;
                if (!myins_isloding && firstVisibleItem2 + visibleItemCount2 == totalItemCount2
                        && totalItemCount2 != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page1++;
                        isFirstTime_mypos = false;
                        Log.e("areyou?", "areyou");
                        getMyInspirationFeedList();

                    } else {
                    }
                }
            }
        });


        feedDetail = (GridView) mainView.findViewById(R.id.feedDetail);
        feedDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem4, int visibleItemCount4, int totalItemCount4) {
                FragmentInspirationSection.this.firstVisibleItem4 = firstVisibleItem4;
                FragmentInspirationSection.this.visibleItemCount4 = visibleItemCount4;
                FragmentInspirationSection.this.totalItemCount4 = totalItemCount4;
                if (!my_detail_ins_isloding && firstVisibleItem4 + visibleItemCount4 == totalItemCount4
                        && totalItemCount4 != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page3++;
                        isFirstTime_mypos_detail = false;
                        Log.e("areyou?", "333");
                        getMyInspirationFeedList();
                    } else {
                    }
                }
            }
        });

        productDetail = (GridView) mainView.findViewById(R.id.productDetail);
        shopList = (GridView) mainView.findViewById(R.id.shopList);
        product_result_list = (GridView) mainView.findViewById(R.id.product_result_list);
        nameText.setText(UserPreference.getInstance().getUserName());
        notificationlist = (RecyclerView) mainView.findViewById(R.id.notificationlist);
        notificationlist.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        notificationlist.setLayoutManager(layoutManager);
        collectionList = (ListView) mainView.findViewById(R.id.collectionList);
        user_result_list = (ListView) mainView.findViewById(R.id.user_result_list);
        user_result_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem5, int visibleItemCount5, int totalItemCount5) {
                FragmentInspirationSection.this.firstVisibleItem5 = firstVisibleItem5;
                FragmentInspirationSection.this.visibleItemCount5 = visibleItemCount5;
                FragmentInspirationSection.this.totalItemCount5 = totalItemCount5;

                if (!user_list_isloding && firstVisibleItem5 + visibleItemCount5 == totalItemCount5
                        && totalItemCount5 != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page4++;
                        isFirstTime_user_list = false;
                        if (indexSearch == 0)
                            displayAllPeopleResult();
                        if (indexSearch == 1)
                            displaySearchPeopleResult();
                    } else {
                    }
                }
            }
        });

        shopList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem5, int visibleItemCount5, int totalItemCount5) {
                FragmentInspirationSection.this.firstVisibleItem5 = firstVisibleItem5;
                FragmentInspirationSection.this.visibleItemCount5 = visibleItemCount5;
                FragmentInspirationSection.this.totalItemCount5 = totalItemCount5;
                if (!shop_list_isloding && firstVisibleItem5 + visibleItemCount5 == totalItemCount5
                        && totalItemCount5 != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page5++;
                        isFirstTime_shop_list = false;
                        if (searchText.getText().toString().trim().length() > 0) {
                            Log.e("chmmmmm", "" + "chmmmmm");
//                            displaySearchShopResult();
                        } else displayAllShops();
                    } else {
                    }
                }
            }
        });

        if (((HomeActivity) mContext).checkInternet()) {
            collAdapterIndex = 0;
            feedAdapterIndex = 0;
            Log.e("areyou?", "444");
            getMyInspirationFeedList();
            getCollectionList();
            indexSearch = 0;
            displayAllPeopleResult();
//            displayAllShops();
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

    private void displayAllPeopleResult() {
        if (overflow2.isOpen())
            overflow2.setOpen();
        user_list_isloding = !user_list_isloding;
        final FeaturedTabApi listApi = new FeaturedTabApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                if (overflow2.isOpen())
                    overflow2.setOpen();
                user_list_isloding = !user_list_isloding;
                FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;
                List<FeaturedTabData> list = featuredTabApiRes.getData();
                if (list.size() < 7)
                    user_list_isloding = true;
                Log.e("current-origin-size", "" + interestList.size());
                interestList.addAll(list);
                Log.e("current-interest-size", "" + interestList.size());
                if (interestList.size() > 0 && isFirstTime_user_list) {
                    Log.e("current-origin-size-1", "" + interestList.size());
                    featuredTabAdapter = new FeaturedTabAdapter(mContext, interestList);
                    if (overflow2.isOpen())
                        overflow2.setOpen();
                    user_result_list.setAdapter(featuredTabAdapter);
                    Log.e("current-origin-size-2", "" + interestList.size());

                } else if (featuredTabAdapter != null) {
                    Log.e("current-origin-size-3", "" + interestList.size());
                    if (overflow2.isOpen())
                        overflow2.setOpen();
                    featuredTabAdapter.setData(interestList);
                    Log.e("current-origin-size-3.5", "" + interestList.size());
                    if (overflow2.isOpen())
                        overflow2.setOpen();
                    featuredTabAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                user_list_isloding = !user_list_isloding;
            }
        });
        Log.e("current-page4", "" + page4);
        listApi.getFeaturedTabData(UserPreference.getInstance().getUserID(), String.valueOf(page4));
        listApi.execute();
    }

    private void displaySearchPeopleResult() {
        overflow2.setOpen();
        Log.e("current", "displaySearchPeopleResult");
        user_list_isloding = !user_list_isloding;
        final GeneralSearchApi generalSearchApi = new GeneralSearchApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                overflow2.setOpen();
                user_list_isloding = !user_list_isloding;
                GeneralSearchRes generalSearchRes = (GeneralSearchRes) object;
                if (generalSearchRes.getUsers().size() < 10)
                    user_list_isloding = true;
                Log.e("current-origin-size", "" + interestUserList.size());
                interestUserList.addAll(generalSearchRes.getUsers());
                Log.e("current-interest-size", "" + interestUserList.size());
                if (interestUserList.size() > 0 && isFirstTime_user_list) {
                    Log.e("current-origin-size-1", "" + interestUserList.size());
                    interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(mContext, interestUserList);
                    overflow2.setOpen();
                    user_result_list.setAdapter(interestPeopleListAdapter);

                } else if (interestPeopleListAdapter != null) {
                    overflow2.setOpen();
                    Log.e("current-origin-size-2", "" + interestUserList.size());
                    interestPeopleListAdapter.setData(interestUserList);
                    overflow2.setOpen();
                    interestPeopleListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                user_list_isloding = !user_list_isloding;
                Log.e("current", "fail-success");
            }
        });

        if (page4 == 1) page4++;
        Log.e("current-page4", "" + page4);
        Log.e("current-page44", "" + searchText.getText().toString().trim());
        generalSearchApi.generalSearch(searchText.getText().toString().trim(), page4);
        generalSearchApi.execute();
    }

    private void displaySearchProductResult() {
        overflow2.setOpen();
        product_list_isloding = !product_list_isloding;
        final GeneralSearchApi generalSearchApi = new GeneralSearchApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                Log.e("here", "2");
                overflow2.setOpen();
                product_list_isloding = !product_list_isloding;
                GeneralSearchRes generalSearchRes = (GeneralSearchRes) object;
                if (generalSearchRes.getProducts().size() < 10) product_list_isloding = true;
                interestProductList.addAll(generalSearchRes.getProducts());
                Log.e("here-size", "" + interestProductList.size());
                temporaryProoducts = categoryFilter(interestProductList, category);
                Log.e("here-size2", "" + temporaryProoducts.size());
                if (temporaryProoducts.size() > 0 && isFirstTime_shop_list) {
                    productSearchdAdapter = new ProductSearchGridAdapter(mContext, temporaryProoducts);
                    overflow2.setOpen();
                    product_result_list.setAdapter(productSearchdAdapter);
                } else if (productSearchdAdapter != null) {
                    overflow2.setOpen();
                    productSearchdAdapter.setData(temporaryProoducts);
                    overflow2.setOpen();
                    productSearchdAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Log.e("here", "3");
                product_list_isloding = !product_list_isloding;
            }
        });
        generalSearchApi.generalSearch(searchText.getText().toString().trim(), page6);
        generalSearchApi.execute();
    }

    private void displayAllShops() {
        Log.e("chhm-all", "all");
        if (overflow2.isOpen()) overflow2.setOpen();
        shop_list_isloding = !shop_list_isloding;
        final BrandListApi brandListApi = new BrandListApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                shop_list_isloding = !shop_list_isloding;
                BrandListRes brandListRes = (BrandListRes) object;
                if (brandListRes.getData().size() < 10) shop_list_isloding = true;
                brandLists.addAll(brandListRes.getData());
                Log.e("chhm-all-first", "" + isFirstTime_shop_list);
                if (brandLists.size() > 0 && isFirstTime_shop_list) {
                    Log.e("chhm-error!", "error");
                    allBrandGridAdapter = new AllBrandGridAdapter(mContext, brandLists);
                    if (overflow2.isOpen()) overflow2.setOpen();
                    shopList.setAdapter(allBrandGridAdapter);
                } else if (allBrandGridAdapter != null) {
                    Log.e("chhm-error??", "error");
                    if (overflow2.isOpen()) overflow2.setOpen();
                    allBrandGridAdapter.setData(brandLists);
                    if (overflow2.isOpen()) overflow2.setOpen();
                    allBrandGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                shop_list_isloding = !shop_list_isloding;
            }
        });
        Log.e("chhm-all-page", "" + page5);
        brandListApi.getBrandList(String.valueOf(page5));
        brandListApi.execute();
    }

    private void displaySearchShopResult() {
        Log.e("chhm-display", "display");
        overflow2.setOpen();
        shop_list_isloding = !shop_list_isloding;
        final GeneralSearchApi generalSearchApi = new GeneralSearchApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                Log.e("chhm-display", "success");
                shop_list_isloding = !shop_list_isloding;
                GeneralSearchRes generalSearchRes = (GeneralSearchRes) object;
                if (generalSearchRes.getBrands().size() < 10) shop_list_isloding = true;
                interestShopList.addAll(generalSearchRes.getBrands());
                Log.e("chhm-display-size", "" + "" + generalSearchRes.getBrands().size());
                Log.e("chhm-display-first", "" + isFirstTime_shop_list);
                if (interestShopList.size() > 0 && isFirstTime_shop_list) {
                    Log.e("chhm-size", "" + "" + interestShopList.size());
                    storeGridAdapter = new StoreGridAdapter(mContext, interestShopList);
                    overflow2.setOpen();
                    shopList.setAdapter(storeGridAdapter);
                } else if (storeGridAdapter != null) {
                    Log.e("chhm-size2", "" + "" + interestShopList.size());
                    overflow2.setOpen();
                    storeGridAdapter.setData(interestShopList);
                    overflow2.setOpen();
                    storeGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Log.e("chhm-display", "fail");
                shop_list_isloding = !shop_list_isloding;
            }
        });
        Log.e("chhm-display-page", "" + page5);
        if (page5 == 1) page5++;
        generalSearchApi.generalSearch(searchText.getText().toString().trim(), page5);
        generalSearchApi.execute();
    }

    @Override
    public void setClickListener() {
        image_upload_post.setOnClickListener(this);
        image_upload_post2.setOnClickListener(this);
        image_camera.setOnClickListener(this);
        tab_layout1.setOnClickListener(this);
        profile_tab.setOnClickListener(this);
        search_tab.setOnClickListener(this);
        cart_tab.setOnClickListener(this);
        fashioniconlayout.setOnClickListener(this);
        photographyiconlayoutlayout.setOnClickListener(this);
        traveliconlayout.setOnClickListener(this);
        beautyiconlayout.setOnClickListener(this);
        techiconlayout.setOnClickListener(this);
        homeiconlayout.setOnClickListener(this);
        info_icon.setOnClickListener(this);
        info_image2.setOnClickListener(this);
        bell_icon.setOnClickListener(this);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        text5.setOnClickListener(this);
        infoicon2.setOnClickListener(this);
        peopletext_layout.setOnClickListener(this);
        productsText_layout.setOnClickListener(this);
        shopText_layout.setOnClickListener(this);
        invite_text.setOnClickListener(this);
        filter_text.setOnClickListener(this);
        women_text.setOnClickListener(this);
        accessories_text.setOnClickListener(this);
        men_text.setOnClickListener(this);
        sale_text.setOnClickListener(this);
        price_text.setOnClickListener(this);
        price_text_0.setOnClickListener(this);
        price_text_25.setOnClickListener(this);
        price_text_50.setOnClickListener(this);
        price_text_100.setOnClickListener(this);
        price_text_150.setOnClickListener(this);
        price_text_200.setOnClickListener(this);
        price_text_500.setOnClickListener(this);
        price_text_750.setOnClickListener(this);
        apply_text.setOnClickListener(this);
        filter_icon.setOnClickListener(this);
        filter_icon2.setOnClickListener(this);
        clothing_layout.setOnClickListener(this);
        shoes_layout.setOnClickListener(this);
        electronics_layout.setOnClickListener(this);
        jewelry_layout.setOnClickListener(this);
        health_layout.setOnClickListener(this);
        sports_layout.setOnClickListener(this);
        baby_layout.setOnClickListener(this);
        computer_layout.setOnClickListener(this);
        toy_layout.setOnClickListener(this);
        game_layout.setOnClickListener(this);
        pet_layout.setOnClickListener(this);
        music_layout.setOnClickListener(this);
        foodiconlayout.setOnClickListener(this);
        fitnessiconlayout.setOnClickListener(this);
        occasioniconlayout.setOnClickListener(this);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (overflow2.isOpen()) {
                    isInfo = false;
                    nameText.setVisibility(View.VISIBLE);
                    performanceText.setVisibility(View.GONE);
                    info_image.setImageResource(R.drawable.info_white);
                    collAdapterIndex = 0;
                    displayMyInspirationFeedList();
                    displayCollectionList();
                    overflow_layout3.setVisibility(View.GONE);
                    overflow_layout2.setVisibility(View.GONE);
                    overflow_layout4.setVisibility(View.GONE);
                    overflow_layout1.setVisibility(View.VISIBLE);
                    if (isOnFeedPage) {
                        imagesList.setVisibility(View.VISIBLE);
                        collectionList.setVisibility(View.GONE);
                        feedDetail.setVisibility(View.GONE);
                        productDetail.setVisibility(View.GONE);
                    } else {
                        imagesList.setVisibility(View.GONE);
                        collectionList.setVisibility(View.VISIBLE);
                        feedDetail.setVisibility(View.GONE);
                        productDetail.setVisibility(View.GONE);
                    }
                } else {
                    overflow_layout3.setVisibility(View.GONE);
                    overflow_layout2.setVisibility(View.GONE);
                    overflow_layout4.setVisibility(View.GONE);
                    overflow_layout1.setVisibility(View.VISIBLE);
                    overflow2.triggerSlide();
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overflow2.setOpen();
                isOnFeedPage = true;
                button2.setTextColor(Color.BLACK);
                button1.setTextColor(Color.WHITE);
                button2.setBackgroundResource(R.drawable.white_button_noborder);
                button1.setBackgroundResource(R.drawable.green_corner_button);
                if (isInfo) {
                    imagesList.setVisibility(View.GONE);
                    feedDetail.setVisibility(View.VISIBLE);
                } else {
                    imagesList.setVisibility(View.VISIBLE);
                    feedDetail.setVisibility(View.GONE);
                }
                collectionList.setVisibility(View.GONE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overflow2.setOpen();
                isOnFeedPage = false;
                button1.setTextColor(Color.BLACK);
                button2.setTextColor(Color.WHITE);
                button1.setBackgroundResource(R.drawable.white_button_noborder);
                button2.setBackgroundResource(R.drawable.green_corner_button);
                collectionList.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
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
                        // showReloadFotter();
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
        CommonUtility.setImage(getActivity(), UserPreference.getInstance().getProfilePic(), profile_pic, R.drawable.profile_icon);
        CommonUtility.setImage(mContext, UserPreference.getInstance().getProfilePic(), prof2, R.drawable.profile_icon);

        final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                MyProfileRes myProfileRes = (MyProfileRes) object;
                userDetails = myProfileRes.getUser_data();
                followersLists = myProfileRes.getFollowers_list();
                followingLists = myProfileRes.getFollowing_list();
                followingtext1.setText(String.valueOf(followersLists.size()));
                followertext1.setText(String.valueOf(followingLists.size()));
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        myProfileApi.getUserProfileDetail(UserPreference.getInstance().getUserID(),
                UserPreference.getInstance().getUserID());
        myProfileApi.execute();
    }

    private void getMyInspirationFeedList() {
        Log.e("areyou?", "2222222");
        if (feedAdapterIndex == 0) myins_isloding = !myins_isloding;
        else my_detail_ins_isloding = !my_detail_ins_isloding;

        final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                Log.e("areyou?", "66666");
                InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
                if (feedAdapterIndex == 0) {
                    myins_isloding = !myins_isloding;
                    myProductList.addAll(inspirationFeedRes.getData());
                    if (inspirationFeedRes.getData().size() < 10) myins_isloding = true;
                } else {
                    my_detail_ins_isloding = !my_detail_ins_isloding;
                    myDetailProductList.addAll(inspirationFeedRes.getData());
                    if (myDetailProductList.size() < 10) my_detail_ins_isloding = true;
                }
                displayMyInspirationFeedList();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    if (feedAdapterIndex == 0) myins_isloding = !myins_isloding;
                    else my_detail_ins_isloding = !my_detail_ins_isloding;
                    InspirationFeedRes response = (InspirationFeedRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else AlertUtils.showToast(mContext, R.string.invalid_response);

            }
        });
        if (feedAdapterIndex == 0)
            inspirationFeedApi.getInspirationFeed(userId, false, String.valueOf(page1), userId);
        else inspirationFeedApi.getInspirationFeed(userId, false, String.valueOf(page3), userId);
        inspirationFeedApi.execute();
    }

    private void getCollectionList() {
        Log.e("areyou?", "44444");
        if (overflow2.isOpen()) overflow2.setOpen();
        mycoll_isloading = !mycoll_isloading;
        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                Log.e("areyou?", "7777");
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                collectionLists2 = collectionApiRes.getCollection();
                displayCollectionList();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mycoll_isloading = !mycoll_isloading;
                if (object != null) {
                    CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, collectionApiRes.getMessage());
                } else AlertUtils.showToast(mContext, R.string.invalid_response);
            }
        });
        collectionApi.getCollectionList(UserPreference.getInstance().getUserID());
        collectionApi.execute();
    }


    private void logoutUser() {
        LogoutApi logoutApi = new LogoutApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        logoutApi.logoutUser(UserPreference.getInstance().getUserID(), CommonUtility.getDeviceTocken(mContext));
        logoutApi.execute();
    }

    private void getFollowingInstagramList() {
        final MessageCenterApi messageCenterApi = new MessageCenterApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                FollowingKikrModel followingKikrModel = (FollowingKikrModel) object;
                followinglist = followingKikrModel.getData();
                followinglist.size();
                followinglistRefined.clear();
                if (followinglist.size() == 0) {
                } else {
                    overflow_layout3.setVisibility(View.VISIBLE);
                    overflow_layout4.setVisibility(View.GONE);
                    overflow_layout1.setVisibility(View.GONE);
                    overflow_layout2.setVisibility(View.GONE);
                    notificationlist.setVisibility(View.VISIBLE);
                    for (int i = 0; i < followinglist.size(); i++) {
                        String userString = followinglist.get(i).getMessage();
                        String notificationType = followinglist.get(i).getType();
                        String userName = "";
                        if (userString.contains("commented") && notificationType.equals("commentinsp"))
                            userName = userString.split(" commented")[0];
                        else if (userString.contains("liked") && notificationType.equals("likeinsp"))
                            userName = userString.split(" liked")[0];
                        else if (userString.contains("following") && notificationType.equals("follow"))
                            userName = userString.split(" is following")[0];
                        if (!userName.equals(UserPreference.getInstance().getUserName()))
                            followinglistRefined.add(followinglist.get(i));

                    }
                    KikrFollowingAdapter adapter = new KikrFollowingAdapter(mContext, (ArrayList<FollowingKikrModel.DataBean>) followinglistRefined);
                    notificationlist.setAdapter(adapter);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        messageCenterApi.followinginstagram("0", "1");
        messageCenterApi.execute();
    }

    private void displayMyInspirationFeedList() {
        if (overflow2.isOpen()) overflow2.setOpen();
        if (feedAdapterIndex == 0 && isOnFeedPage) {
            if (myProductList.size() > 0 && isFirstTime_mypos) {
                inspirationGridAdapter = new InspirationGridAdapter(mContext, myProductList, 0);
                if (overflow2.isOpen()) overflow2.setOpen();
                imagesList.setAdapter(inspirationGridAdapter);
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setVisibility(View.GONE);
                imagesList.setVisibility(View.VISIBLE);
            } else if (inspirationGridAdapter != null) {
                if (overflow2.isOpen()) overflow2.setOpen();
                inspirationGridAdapter.setData(myProductList);
                if (overflow2.isOpen()) overflow2.setOpen();
                inspirationGridAdapter.notifyDataSetChanged();
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setVisibility(View.GONE);
                imagesList.setVisibility(View.VISIBLE);
            }
        } else if (feedAdapterIndex == 1 && isOnFeedPage) {
            if (myDetailProductList.size() > 0 && isFirstTime_mypos_detail) {
                inspirationGridAdapter2 = new InspirationGridAdapter(mContext, myDetailProductList, 1);
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setAdapter(inspirationGridAdapter2);
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
            } else if (inspirationGridAdapter2 != null) {
                if (overflow2.isOpen()) overflow2.setOpen();
                inspirationGridAdapter2.setData(myDetailProductList);
                if (overflow2.isOpen()) overflow2.setOpen();
                inspirationGridAdapter2.notifyDataSetChanged();
                if (overflow2.isOpen()) overflow2.setOpen();
                feedDetail.setVisibility(View.VISIBLE);
                imagesList.setVisibility(View.GONE);
            }
        }
    }

    private void displayCollectionList() {

        if (collectionLists2.size() < 10) mycoll_isloading = true;

        if (collectionLists2.size() > 0 && isFirstTime_myCol) {
            collectionAdapter = new FragmentProfileCollectionAdapter(mContext, collectionLists2, userId, fragmentProfileView, null, collAdapterIndex, new FragmentProfileCollectionAdapter.ListAdapterListener() {
                @Override
                public void onClickAtOKButton(int position) {
                    final String coll_id = collectionLists2.get(position).getId();
                    imagesList.setVisibility(View.GONE);
                    collectionList.setVisibility(View.GONE);
                    feedDetail.setVisibility(View.GONE);
                    productDetail.setVisibility(View.VISIBLE);
                    final ProductBasedOnBrandApi productBasedOnBrandApi = new ProductBasedOnBrandApi(new ServiceCallback() {
                        @Override
                        public void handleOnSuccess(Object object) {
                            ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
                            product_data = productBasedOnBrandRes.getData();
                            productDetailGridAdapter = new ProductDetailGridAdapter(mContext, product_data, 0);
                            productDetail.setAdapter(productDetailGridAdapter);
                        }

                        @Override
                        public void handleOnFailure(ServiceException exception, Object object) {
                        }
                    });
                    productBasedOnBrandApi.getProductsBasedOnCollectionList(UserPreference.getInstance().getUserID(), String.valueOf(0), coll_id);
                    productBasedOnBrandApi.execute();
                }
            });
            if (overflow2.isOpen()) overflow2.setOpen();
            collectionList.setAdapter(collectionAdapter);
        } else if (collectionAdapter != null) {
            if (overflow2.isOpen()) overflow2.setOpen();
            collectionAdapter.setData(collectionLists2);
            if (overflow2.isOpen()) overflow2.setOpen();
            collectionAdapter.notifyDataSetChanged();
        }
    }

    private void setVisibilityByGroup(List<LinearLayout> gones, List<LinearLayout> visibles,
                                      List<LinearLayout> invisibles) {
        if (gones != null)
            for (int i = 0; i < gones.size(); i++) {
                gones.get(i).setVisibility(View.GONE);
            }
        if (visibles != null)
            for (int i = 0; i < visibles.size(); i++) {
                visibles.get(i).setVisibility(View.VISIBLE);
            }
        if (invisibles != null)
            for (int i = 0; i < invisibles.size(); i++) {
                invisibles.get(i).setVisibility(View.INVISIBLE);
            }
    }

    private void setBackgroundByGroup(List<TextView> group1, int background1,
                                      List<TextView> group2, int background2) {
        if (group1 != null)
            for (int i = 0; i < group1.size(); i++) {
                group1.get(i).setBackgroundResource(background1);
            }
        if (group2 != null)
            for (int i = 0; i < group2.size(); i++) {
                group2.get(i).setBackgroundResource(background2);
            }
    }

    private void setTextColorByGroup(List<TextView> group1, int color1,
                                     List<TextView> group2, int color2) {
        if (group1 != null)
            for (int i = 0; i < group1.size(); i++) {
                group1.get(i).setTextColor(color1);
            }
        if (group2 != null)
            for (int i = 0; i < group2.size(); i++) {
                group2.get(i).setTextColor(color2);
            }
    }

    public List<ProductResult> categoryFilter(List<ProductResult> products, String filter) {
        if (products == null) return products;
        List<ProductResult> result = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ProductResult current = products.get(i);
            if (current.getPrimarycategory() != null && current.getPrimarycategory().toLowerCase().contains(filter)) {
                result.add(current);
            }
        }
        return result;
    }

    public List<ProductResult> buttonPriceFilter(List<ProductResult> products, String filter1, String filter2, String filter3, double lowPrice, double highPrice) {
        if (products == null) return products;
        List<ProductResult> result = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ProductResult current = products.get(i);
            Log.e("here-filter", current.getPrimarycategory());
            Log.e("here-price", current.getRetailprice());
            Log.e("here-??", filter1);
            Log.e("here-!!", filter2);
            String retailPrice = current.getRetailprice();
            if (current.getPrimarycategory() != null && current.getPrimarycategory().toLowerCase().contains(filter1) && (current.getPrimarycategory().toLowerCase().contains(filter2) || current.getPrimarycategory().toLowerCase().contains(filter3))) {
                if (retailPrice != null && retailPrice.length() > 0) {
                    Log.e("here-^^^", retailPrice);
                    double price = Double.parseDouble(retailPrice);
                    if (price >= lowPrice && price <= highPrice) result.add(current);
                }
            }
        }
        Log.e("here-resultsize", "" + result.size());
        return result;
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            Log.e("watcher-index", "" + indexSearch);
            Log.e("watcher-pps", "" + ppsIndex);

            String text = searchText.getText().toString();
            if (text == null || text.length() == 0) {
                if (indexSearch == 2 && ppsIndex == 2) {
                    storeGridAdapter = null;
                    shop_list_isloding = false;
                    page5 = 0;
                    interestShopList.clear();
                    brandLists.clear();
                    isFirstTime_shop_list = true;
                    Log.e("chhm-all-first", "" + isFirstTime_shop_list);
                    displayAllShops();
                } else if (indexSearch == 3 && ppsIndex == 1) {
                    Log.e("here", "1");
                    product_list_isloding = false;
                    page6 = 0;
                    interestProductList.clear();
                    isFirstTime_product_list = true;
                    products_filter_layout.setVisibility(View.GONE);
                    products_result_layout.setVisibility(View.GONE);
                    products_layout.setVisibility(View.VISIBLE);
                } else if ((indexSearch == 0 || indexSearch == 1) && ppsIndex == 0) {
                    Log.e("watcher", "" + indexSearch);
                    interestPeopleListAdapter = null;
                    user_list_isloding = false;
                    page4 = 0;
                    interestList.clear();
                    interestUserList.clear();
                    isFirstTime_user_list = true;
                    displayAllPeopleResult();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
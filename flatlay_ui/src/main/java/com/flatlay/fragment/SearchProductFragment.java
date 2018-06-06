package com.flatlay.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.AllBrandGridAdapter;
import com.flatlay.adapter.CustomizeInterestPeopleListAdapter;
import com.flatlay.adapter.FeaturedTabAdapter;
import com.flatlay.adapter.ProductSearchGridAdapter;
import com.flatlay.adapter.StoreGridAdapter;
import com.flatlay.post_upload.FragmentPostUploadTag;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.api.BrandListApi;
import com.flatlaylib.api.FeaturedTabApi;
import com.flatlaylib.api.GeneralSearchApi;
import com.flatlaylib.bean.BrandList;
import com.flatlaylib.bean.BrandResult;
import com.flatlaylib.bean.FeaturedTabData;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProductResult;
import com.flatlaylib.bean.UserResult;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.BrandListRes;
import com.flatlaylib.service.res.FeaturedTabApiRes;
import com.flatlaylib.service.res.GeneralSearchRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by RachelDi on 4/27/18.
 */

public class SearchProductFragment extends BaseFragment implements View.OnClickListener {
    private View mainView, highlight3, highlight2, highlight1, white_line;
    private MyMaterialContentOverflow3 overflow2;
    private ImageView filter_icon, filter_icon2;
    private TextView peopletext, productsText, shopText, invite_text, clothing_text, music_text, pet_text,
            games_text, toys_text, computer_text, baby_text, sports_text, health_text, jewelry_text,
            electronics_text, shoes_text, filter_text, women_text, apply_text, price_text_750,
            price_text_500, price_text_200,
            price_text_150, price_text_50, price_text_25, price_text_0, price_text, sale_text, men_text,
            accessories_text, price_text_100;
    private RelativeLayout productsText_layout, shopText_layout, peopletext_layout, products_filter_layout,
            products_result_layout;
    private LinearLayout people_layout, products_layout, clothing_layout, shoes_layout, electronics_layout,
            jewelry_layout, health_layout, sports_layout, baby_layout, computer_layout, toy_layout,
            game_layout, pet_layout, music_layout, shops_layout;
    private GridView product_result_list, shopList;
    private ListView user_result_list;
    private BaseFragment section;
    private ProductSearchGridAdapter productSearchdAdapter;
    private String resultCategory = "", filter2 = "", filter3 = "", category = "";
    private List<TextView> group1, group2;
    private double lowprice = 0, highprice = Double.MAX_VALUE;
    private int indexSearch = 0, ppsIndex = 0, page4 = 0, firstVisibleItem5 = 0, visibleItemCount5 = 0,
            totalItemCount5 = 0, page5 = 0, page6 = 0;
    private boolean shop_list_isloding = false, user_list_isloding = false, isFirstTime_user_list = true,
            isFirstTime_shop_list = true, product_list_isloding = false, displayAllPeople = true;
    private List<BrandList> brandLists = new ArrayList<>();
    private StoreGridAdapter storeGridAdapter;
    private List<FeaturedTabData> interestList = new ArrayList<>();
    private String search_text;
    private List<ProductResult> interestProductList = new ArrayList<>(), temporaryProoducts = new ArrayList<>();
    private FeaturedTabAdapter featuredTabAdapter;
    private List<BrandResult> interestShopList = new ArrayList<>();
    private List<UserResult> interestUserList = new ArrayList<>();
    private CustomizeInterestPeopleListAdapter interestPeopleListAdapter;
    private AllBrandGridAdapter allBrandGridAdapter;
    private ProgressBarDialog mProgressBarDialog;

    public SearchProductFragment(MyMaterialContentOverflow3 overflow2, BaseFragment section, String search_text, boolean displayAllPeople, int indexSearch) {

        this.overflow2 = overflow2;
        this.section = section;
        this.search_text = search_text;
        this.displayAllPeople = displayAllPeople;
        this.indexSearch = indexSearch;
    }


    public SearchProductFragment(MyMaterialContentOverflow3 overflow2, BaseFragment section, String search_text, boolean displayAllPeople) {

        this.overflow2 = overflow2;
        this.section = section;
        this.search_text = search_text;
        this.displayAllPeople = displayAllPeople;
        if (section instanceof FragmentInspirationSection)
            indexSearch = UserPreference.getInstance().getSearchIndex();
        if (section instanceof FragmentPostUploadTag)
            indexSearch = 3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.search_fragment_layout, container, false);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.people_text_layout:
                overflow2.setOpen();
                people_layout.setVisibility(View.VISIBLE);
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.GONE);
                products_filter_layout.setVisibility(View.GONE);
                shops_layout.setVisibility(View.GONE);
                highlight1.setVisibility(View.VISIBLE);
                highlight2.setVisibility(View.INVISIBLE);
                highlight3.setVisibility(View.INVISIBLE);
                indexSearch = 1;
                UserPreference.getInstance().setSearchIndex(indexSearch);
                ppsIndex = 0;
                UserPreference.getInstance().setPpsIndex(ppsIndex);
                break;
            case R.id.products_text_layout:
                overflow2.setOpen();
                ppsIndex = 1;
                UserPreference.getInstance().setPpsIndex(ppsIndex);
                if (section instanceof FragmentInspirationSection) {
                    people_layout.setVisibility(View.GONE);
                    products_layout.setVisibility(View.VISIBLE);
                    products_result_layout.setVisibility(View.GONE);
                    products_filter_layout.setVisibility(View.GONE);
                    shops_layout.setVisibility(View.GONE);
                    highlight1.setVisibility(View.INVISIBLE);
                    highlight2.setVisibility(View.VISIBLE);
                    highlight3.setVisibility(View.INVISIBLE);
                    indexSearch = 3;
                    UserPreference.getInstance().setSearchIndex(indexSearch);
                }
                break;
            case R.id.invite_text:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=com.flatlay\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                break;
            case R.id.shop_text_layout:
                overflow2.setOpen();
                ppsIndex = 2;
                UserPreference.getInstance().setPpsIndex(ppsIndex);
                people_layout.setVisibility(View.GONE);
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.GONE);
                products_filter_layout.setVisibility(View.GONE);
                shops_layout.setVisibility(View.VISIBLE);
                highlight1.setVisibility(View.INVISIBLE);
                highlight2.setVisibility(View.INVISIBLE);
                highlight3.setVisibility(View.VISIBLE);
                if (indexSearch != 2) {
                    displayAllShops();
                }
                indexSearch = 2;
                UserPreference.getInstance().setSearchIndex(indexSearch);
                break;
            case R.id.clothing_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("clothing")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "clothing";
                break;
            case R.id.shoes_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);

                if (productSearchdAdapter != null && !resultCategory.equals("shoe")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "shoe";
                break;
            case R.id.electronics_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("electronic")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "electronic";
                break;
            case R.id.jewelry_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("jewelry")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "jewelry";
                break;
            case R.id.health_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("health")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "health";
                break;
            case R.id.sports_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("sport")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "sport";
                break;
            case R.id.baby_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("baby")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "baby";
                break;
            case R.id.computer_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("computer")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "computer";
                break;
            case R.id.toy_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("toy")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "toy";
                break;
            case R.id.game_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("game")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "game";
                break;
            case R.id.pet_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("computer")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "computer";
                break;
            case R.id.music_layout:
                overflow2.setOpen();
                products_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                if (productSearchdAdapter != null && !resultCategory.equals("music")) {
                    productSearchdAdapter.setData(new ArrayList<ProductResult>());
                    productSearchdAdapter.notifyDataSetChanged();
                } else if (productSearchdAdapter != null) {
                    if (temporaryProoducts != null) {
                        productSearchdAdapter.setData(temporaryProoducts);
                        productSearchdAdapter.notifyDataSetChanged();
                    }
                }
                category = "music";
                break;
            case R.id.apply_text:
                overflow2.setOpen();
                products_filter_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.VISIBLE);
                updateFilterProducts(resultCategory, filter2, filter3, lowprice, highprice);
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
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mProgressBarDialog = new ProgressBarDialog(mContext);

        peopletext_layout = (RelativeLayout) mainView.findViewById(R.id.people_text_layout);
        peopletext = (TextView) mainView.findViewById(R.id.people_text);
        peopletext.setTypeface(FontUtility.setMontserratLight(mContext));
        white_line = (View) mainView.findViewById(R.id.white_line);
        highlight1 = (View) mainView.findViewById(R.id.highlight1);
        highlight2 = (View) mainView.findViewById(R.id.highlight2);
        highlight3 = (View) mainView.findViewById(R.id.highlight3);
        if (indexSearch == 1) {
            highlight1.setVisibility(View.VISIBLE);
        } else if (indexSearch == 3) {
            highlight2.setVisibility(View.VISIBLE);
        } else {
            highlight3.setVisibility(View.VISIBLE);
        }
        productsText_layout = (RelativeLayout) mainView.findViewById(R.id.products_text_layout);
        productsText = (TextView) mainView.findViewById(R.id.products_text);
        productsText.setTypeface(FontUtility.setMontserratLight(mContext));
        shopText = (TextView) mainView.findViewById(R.id.shop_text);
        shopText.setTypeface(FontUtility.setMontserratLight(mContext));
        shopText_layout = (RelativeLayout) mainView.findViewById(R.id.shop_text_layout);
        people_layout = (LinearLayout) mainView.findViewById(R.id.people_layout);
        invite_text = (TextView) mainView.findViewById(R.id.invite_text);
        invite_text.setTypeface(FontUtility.setMontserratLight(mContext));
        user_result_list = (ListView) mainView.findViewById(R.id.user_result_list);
        products_layout = (LinearLayout) mainView.findViewById(R.id.products_layout);
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
        shops_layout = (LinearLayout) mainView.findViewById(R.id.shops_layout);
        shoes_layout = (LinearLayout) mainView.findViewById(R.id.shoes_layout);
        products_filter_layout = (RelativeLayout) mainView.findViewById(R.id.products_filter_layout);
        products_result_layout = (RelativeLayout) mainView.findViewById(R.id.products_result_layout);
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
        filter_icon = (ImageView) mainView.findViewById(R.id.filter_icon);
        filter_icon2 = (ImageView) mainView.findViewById(R.id.filter_icon2);
        shopList = (GridView) mainView.findViewById(R.id.shopList);
        clothing_layout = (LinearLayout) mainView.findViewById(R.id.clothing_layout);
        electronics_layout = (LinearLayout) mainView.findViewById(R.id.electronics_layout);
        music_layout = (LinearLayout) mainView.findViewById(R.id.music_layout);
        pet_layout = (LinearLayout) mainView.findViewById(R.id.pet_layout);
        game_layout = (LinearLayout) mainView.findViewById(R.id.game_layout);
        toy_layout = (LinearLayout) mainView.findViewById(R.id.toy_layout);
        computer_layout = (LinearLayout) mainView.findViewById(R.id.computer_layout);
        baby_layout = (LinearLayout) mainView.findViewById(R.id.baby_layout);
        sports_layout = (LinearLayout) mainView.findViewById(R.id.sports_layout);
        health_layout = (LinearLayout) mainView.findViewById(R.id.health_layout);
        jewelry_layout = (LinearLayout) mainView.findViewById(R.id.jewelry_layout);
        product_result_list = (GridView) mainView.findViewById(R.id.product_result_list);
        if (section instanceof FragmentPostUploadTag) {
            peopletext_layout.setVisibility(View.GONE);
            productsText_layout.setVisibility(View.GONE);
            shopText_layout.setVisibility(View.GONE);
            people_layout.setVisibility(View.GONE);
            products_layout.setVisibility(View.VISIBLE);
            products_result_layout.setVisibility(View.GONE);
            products_filter_layout.setVisibility(View.GONE);
            shops_layout.setVisibility(View.GONE);
            white_line.setVisibility(View.GONE);
        }
        user_result_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem5, int visibleItemCount5, int totalItemCount5) {
                Log.d(TAG, "onScroll: userlist");
                SearchProductFragment.this.firstVisibleItem5 = firstVisibleItem5;
                SearchProductFragment.this.visibleItemCount5 = visibleItemCount5;
                SearchProductFragment.this.totalItemCount5 = totalItemCount5;

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
                SearchProductFragment.this.firstVisibleItem5 = firstVisibleItem5;
                SearchProductFragment.this.visibleItemCount5 = visibleItemCount5;
                SearchProductFragment.this.totalItemCount5 = totalItemCount5;
                if (!shop_list_isloding && firstVisibleItem5 + visibleItemCount5 == totalItemCount5
                        && totalItemCount5 != 0) {
                    if (((HomeActivity) mContext).checkInternet()) {
                        page5++;
                        isFirstTime_shop_list = false;
                        if (search_text.length() > 0) {
                        } else
                            displayAllShops();
                    } else {
                    }
                }
            }
        });

        if (search_text == null || search_text.length() == 0) {

            if (indexSearch == 2) {
                storeGridAdapter = null;
                shop_list_isloding = false;
                page5 = 0;
                interestShopList.clear();
                brandLists.clear();
                isFirstTime_shop_list = true;
                displayAllShops();
            } else if (indexSearch == 3) {

                product_list_isloding = false;
                page6 = 0;
                interestProductList.clear();
                people_layout.setVisibility(View.GONE);
                shops_layout.setVisibility(View.GONE);
                products_filter_layout.setVisibility(View.GONE);
                products_result_layout.setVisibility(View.GONE);
                products_layout.setVisibility(View.VISIBLE);
            } else if ((indexSearch == 0 || indexSearch == 1)) {

                interestPeopleListAdapter = null;
                user_list_isloding = false;
                page4 = 0;
                interestList.clear();
                interestUserList.clear();
                isFirstTime_user_list = true;
                displayAllPeopleResult();
            }
        } else {
            if (indexSearch == 2) {

                storeGridAdapter = null;
                shop_list_isloding = false;
                page5 = 0;
                interestShopList.clear();
                isFirstTime_shop_list = true;

                displaySearchShopResult();
            } else if (indexSearch == 3) {

                product_list_isloding = false;
                page6 = 0;
                interestProductList.clear();

                displaySearchProductResult();
            } else {

                interestPeopleListAdapter = null;
                user_list_isloding = false;
                page4 = 0;
                interestUserList.clear();
                isFirstTime_user_list = true;
                displaySearchPeopleResult();
            }
        }
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        peopletext_layout.setOnClickListener(this);
        productsText_layout.setOnClickListener(this);
        shopText_layout.setOnClickListener(this);
        invite_text.setOnClickListener(this);
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
    }

    private void displayAllPeopleResult() {
        highlight1.setVisibility(View.VISIBLE);
        highlight2.setVisibility(View.INVISIBLE);
        highlight3.setVisibility(View.INVISIBLE);
        people_layout.setVisibility(View.VISIBLE);
        products_layout.setVisibility(View.GONE);
        products_result_layout.setVisibility(View.GONE);
        products_filter_layout.setVisibility(View.GONE);
        shops_layout.setVisibility(View.GONE);
        if (overflow2.isOpen())
            overflow2.setOpen();
        user_list_isloding = !user_list_isloding;
       /* final FeaturedTabApi listApi = new FeaturedTabApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {

                if (overflow2.isOpen())
                    overflow2.setOpen();
                user_list_isloding = !user_list_isloding;

                FeaturedTabApiRes featuredTabApiRes = (FeaturedTabApiRes) object;
                List<FeaturedTabData> list = featuredTabApiRes.getData();

                if (list.size() < 7)
                    user_list_isloding = true;
                interestList.addAll(list);
                if (interestList.size() > 0 && isFirstTime_user_list) {
                    featuredTabAdapter = new FeaturedTabAdapter(mContext, interestList, new FeaturedTabAdapter.ListAdapterListener() {
                        @Override
                        public void onClickAtOKButton(int position) {
                            final String other_user_id = interestList.get(position).getItem_id();
                            final String other_user_name = interestList.get(position).getItem_name();
                            final String image = interestList.get(position).getProfile_pic();

                            ((HomeActivity) mContext).myAddFragment(new OtherFeedCollectionFragment(overflow2, other_user_id, other_user_name, image));

                        }
                    });
                    if (overflow2.isOpen())
                        overflow2.setOpen();
                    user_result_list.setAdapter(featuredTabAdapter);
                } else if (featuredTabAdapter != null) {
                    if (overflow2.isOpen())
                        overflow2.setOpen();
                    featuredTabAdapter.setData(interestList);
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
        listApi.getFeaturedTabData(UserPreference.getInstance().getUserID(), String.valueOf(page4));
        listApi.execute();*/
    }

    private void displaySearchPeopleResult() {
        highlight1.setVisibility(View.VISIBLE);
        highlight2.setVisibility(View.INVISIBLE);
        highlight3.setVisibility(View.INVISIBLE);
        people_layout.setVisibility(View.VISIBLE);
        products_layout.setVisibility(View.GONE);
        products_result_layout.setVisibility(View.GONE);
        products_filter_layout.setVisibility(View.GONE);
        shops_layout.setVisibility(View.GONE);
        overflow2.setOpen();
        user_list_isloding = !user_list_isloding;
        final GeneralSearchApi generalSearchApi = new GeneralSearchApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                overflow2.setOpen();
                user_list_isloding = !user_list_isloding;
                GeneralSearchRes generalSearchRes = (GeneralSearchRes) object;
                if (generalSearchRes.getUsers().size() < 10)
                    user_list_isloding = true;
                interestUserList.addAll(generalSearchRes.getUsers());
                if (interestUserList.size() > 0 && isFirstTime_user_list) {
                    interestPeopleListAdapter = new CustomizeInterestPeopleListAdapter(mContext, interestUserList, new CustomizeInterestPeopleListAdapter.ListAdapterListener() {
                        @Override
                        public void onClickAtOKButton(int position) {
                            final String other_user_id = interestUserList.get(position).getId();
                            final String other_user_name = interestUserList.get(position).getName();
                            final String image = interestUserList.get(position).getImg();
                            ((HomeActivity) mContext).myAddFragment(new OtherFeedCollectionFragment(overflow2, other_user_id, other_user_name, image));

                        }
                    });
                    overflow2.setOpen();
                    user_result_list.setAdapter(interestPeopleListAdapter);

                } else if (interestPeopleListAdapter != null) {
                    overflow2.setOpen();
                    interestPeopleListAdapter.setData(interestUserList);
                    overflow2.setOpen();
                    interestPeopleListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                user_list_isloding = !user_list_isloding;
            }
        });

        if (page4 == 1) page4++;
        generalSearchApi.generalSearch(search_text, page4);
        generalSearchApi.execute();
    }

    private void displaySearchProductResult() {
        highlight1.setVisibility(View.INVISIBLE);
        highlight2.setVisibility(View.VISIBLE);
        highlight3.setVisibility(View.INVISIBLE);
        people_layout.setVisibility(View.GONE);
        shops_layout.setVisibility(View.GONE);
        products_filter_layout.setVisibility(View.GONE);
        products_layout.setVisibility(View.GONE);
        products_result_layout.setVisibility(View.VISIBLE);

        overflow2.setOpen();
        product_list_isloding = !product_list_isloding;
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final GeneralSearchApi generalSearchApi = new GeneralSearchApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                overflow2.setOpen();
                resultCategory = category;
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                product_list_isloding = !product_list_isloding;
                GeneralSearchRes generalSearchRes = (GeneralSearchRes) object;
                if (generalSearchRes.getProducts().size() < 10) product_list_isloding = true;
                interestProductList.addAll(generalSearchRes.getProducts());
                temporaryProoducts = categoryFilter(interestProductList, resultCategory);
                if (temporaryProoducts.size() > 0 && isFirstTime_shop_list) {
                    if (section instanceof FragmentInspirationSection)
                        productSearchdAdapter = new ProductSearchGridAdapter(mContext, temporaryProoducts);
                    else
                        productSearchdAdapter = new ProductSearchGridAdapter(mContext, temporaryProoducts, true, new ProductSearchGridAdapter.MyItemsListener() {
                            @Override
                            public void onOkButton(Product currentProduct) {
                                ((FragmentPostUploadTag) section).chooseProduct(currentProduct);
                            }
                        });
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
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                product_list_isloding = !product_list_isloding;
            }
        });

        generalSearchApi.generalSearch(search_text, page6);
        generalSearchApi.execute();
    }

    private void displayAllShops() {
        highlight1.setVisibility(View.INVISIBLE);
        highlight2.setVisibility(View.INVISIBLE);
        highlight3.setVisibility(View.VISIBLE);
        people_layout.setVisibility(View.GONE);
        products_layout.setVisibility(View.GONE);
        products_result_layout.setVisibility(View.GONE);
        products_filter_layout.setVisibility(View.GONE);
        shops_layout.setVisibility(View.VISIBLE);
        if (overflow2.isOpen()) overflow2.setOpen();
        shop_list_isloding = !shop_list_isloding;
        final BrandListApi brandListApi = new BrandListApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {

                shop_list_isloding = !shop_list_isloding;
                BrandListRes brandListRes = (BrandListRes) object;
                if (brandListRes.getData().size() < 10) shop_list_isloding = true;
                brandLists.addAll(brandListRes.getData());
                if (brandLists.size() > 0 && isFirstTime_shop_list) {
                    allBrandGridAdapter = new AllBrandGridAdapter(mContext, brandLists);
                    if (overflow2.isOpen()) overflow2.setOpen();
                    shopList.setAdapter(allBrandGridAdapter);
                } else if (allBrandGridAdapter != null) {
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
        brandListApi.getBrandList(String.valueOf(page5));
        brandListApi.execute();
    }

    private void displaySearchShopResult() {
        highlight1.setVisibility(View.INVISIBLE);
        highlight2.setVisibility(View.INVISIBLE);
        highlight3.setVisibility(View.VISIBLE);
        people_layout.setVisibility(View.GONE);
        products_layout.setVisibility(View.GONE);
        products_result_layout.setVisibility(View.GONE);
        products_filter_layout.setVisibility(View.GONE);
        shops_layout.setVisibility(View.VISIBLE);
        overflow2.setOpen();
        shop_list_isloding = !shop_list_isloding;
        final GeneralSearchApi generalSearchApi = new GeneralSearchApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {

                shop_list_isloding = !shop_list_isloding;
                GeneralSearchRes generalSearchRes = (GeneralSearchRes) object;
                if (generalSearchRes.getBrands().size() < 10) shop_list_isloding = true;
                interestShopList.addAll(generalSearchRes.getBrands());
                if (interestShopList.size() > 0 && isFirstTime_shop_list) {
                    storeGridAdapter = new StoreGridAdapter(mContext, interestShopList);
                    overflow2.setOpen();
                    shopList.setAdapter(storeGridAdapter);
                } else if (storeGridAdapter != null) {
                    overflow2.setOpen();
                    storeGridAdapter.setData(interestShopList);
                    overflow2.setOpen();
                    storeGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                shop_list_isloding = !shop_list_isloding;
            }
        });
        if (page5 == 1) page5++;
        generalSearchApi.generalSearch(search_text, page5);
        generalSearchApi.execute();
    }

    public void updateFilterProducts(String category, String filter2, String filter3, double lowprice, double highprice) {
        temporaryProoducts = buttonPriceFilter(interestProductList, category, filter2, filter3, lowprice, highprice);
        if (productSearchdAdapter != null) {
            productSearchdAdapter.setData(temporaryProoducts);
            productSearchdAdapter.notifyDataSetChanged();
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
            String retailPrice = current.getRetailprice();
            if (current.getPrimarycategory() != null && current.getPrimarycategory().toLowerCase().contains(filter1) && (current.getPrimarycategory().toLowerCase().contains(filter2) || current.getPrimarycategory().toLowerCase().contains(filter3))) {
                if (retailPrice != null && retailPrice.length() > 0) {
                    double price = Double.parseDouble(retailPrice);
                    if (price >= lowPrice && price <= highPrice) result.add(current);
                }
            }
        }
        return result;
    }

}

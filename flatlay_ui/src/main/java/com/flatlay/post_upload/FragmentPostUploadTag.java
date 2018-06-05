package com.flatlay.post_upload;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.ChooseCollectionDialog;
import com.flatlay.dialog.RedirectToPlayStore;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.fragment.SearchProductFragment;
import com.flatlay.ui.ProductUI;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.TagView;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlay.utility.PictureUtils;
import com.flatlay.utility.PinterestUtility;
import com.flatlaylib.api.AddCollectionApi;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.api.EditInspirationApi;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.api.ProductBasedOnBrandApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.TaggedItem;
import com.flatlaylib.bean.TaggedProducts;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.service.res.CommonRes;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.service.res.ProductBasedOnBrandRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.Syso;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.instagram.InstagramSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.Utils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//import com.flatlay.fragment.FragmentInspirationImageTag;
//import com.flatlay.utility.PinterestUtility;

public class FragmentPostUploadTag extends BaseFragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, OnLoginCompleteListener, SocialNetworkManager.OnInitializationCompleteListener {
    final static String TAG = "FragmentPostUploadTag";
    public static SocialNetworkManager mSocialNetworkManager;
    public static boolean isUploadWithoutTag = false;
    static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/flatlay/";
    LinearLayout post_load, existing_load;
    CameraFragment cameraFragment;
    String imageServerUri;
    byte[] byteArray;
    CollectionApi collectionApi;
    Product currentProduct = new Product();
    private ProgressBarDialog mProgressBarDialog;
    private TextView collectionName, newCollectionName, addProducts, post_without_tag_text, add_collection_text, button1, textOR, button2, new_collection_name, ins_text, fb_text, twitter_text, pinterest_text, post_text;
    private View mainView;
    private Inspiration inspiration;
    private EditText nameYourCollection, description_text, search_tab_text;
    private RoundedImageView inspirationImage;
    private LinearLayout productInflaterLayout, addCollectionLayout2, addCollectionLayout1, checkLayout, backIconLayout, cancel_layout1;
    private Bitmap bmp;
    private boolean choseExisitng = false;
    private String isImage, imageUrl, choice, collection_id;
    private ListView choicelist;
    private int networkId = 0;
    private RelativeLayout main_content, share_pin_layout, share_twi_layout, share_fb_layout, share_ins_layout;
    private ArrayList<Integer> networkidarray = new ArrayList<Integer>();
    private SwitchCompat switchFacebook, switchTwitter, switchInstagram, switchPinterest;
    private MyMaterialContentOverflow3 overflow2;
    private SearchProductFragment searchProductFragment;
    private FragmentPostUploadTag fragmentPostUploadTag;
    private ChooseCollectionDialog collectionListDialog;
    private boolean isNewCollectionAdded;
    private int addProductNewCollection = 0;
    private ArrayList<Product> taggedProductList = new ArrayList<>();
    private float x = 0, y = 0;
    private int productAddedCount = 0;
    private ArrayList<Product> addProductListNew = new ArrayList<>();
    private boolean isAddProduct = false;
    private ArrayList<Product> addProductListNew1 = new ArrayList<>();
    private ArrayList<String> taggedProductIds = new ArrayList<>();
    private boolean isTaggingProduct = true;
    private TagView tagView;
    private InputMethodManager imm;
    private FrameLayout overlay;
    private String START_TEXT = "Tag Product", finalNewCollectionName;
    private TaggedItem taggedItemLocal = new TaggedItem(), selectedItem;
    private ImageView squareCheck2, squareCheck, cancelImage;
    private SocialNetwork socialNetwork;
    private int sucesssharecount = 0;
    private HorizontalScrollView productLayout;
    private boolean isAdd = true;
    private int page = 0;
    private int index = 0;
    private List<CollectionList> collectionLists;
    private List<Product> products = new ArrayList<Product>();
    private String description;
    private boolean isUpdate = false;
    private TaggedItem taggedItem = new TaggedItem();
    private TaggedProducts taggedProducts = new TaggedProducts();

    public FragmentPostUploadTag(Inspiration inspiration, boolean isUpdate) {
        this.inspiration = inspiration;
        this.isUpdate = isUpdate;
    }

    public FragmentPostUploadTag(Inspiration inspiration) {
        this.inspiration = inspiration;
        this.isUpdate = true;
        isUploadWithoutTag = false;
    }

    public FragmentPostUploadTag(CameraFragment cameraFragment, Bitmap bmp, String imageUrl, String isImage) {
        this.bmp = bmp;
        this.cameraFragment = cameraFragment;
        this.isUpdate = false;
        this.isImage = isImage;
        this.imageUrl = imageUrl;
    }

    public FragmentPostUploadTag(Bitmap bmp, String imageUrl, String isImage) {
        this.bmp = bmp;
        this.isUpdate = false;
        this.isImage = isImage;
        this.imageUrl = imageUrl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_post_upload_tag, null);
        Log.w(TAG, "FragmentPostUploadTag");
        fragmentPostUploadTag = this;
        if (cameraFragment != null) {
            cameraFragment.onStop();
        }
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.backIconLayout:
                if (overflow2.isOpen()) {
                    overflow2.triggerClose();
                } else {

                    mContext.onBackPressed();
                }
                break;

            case R.id.post_text:
                post_text.setClickable(false);
                validateInput();
                break;

            case R.id.cancel_layout1:
                addCollectionLayout2.setVisibility(View.GONE);
                addCollectionLayout1.setVisibility(View.VISIBLE);
                imm.hideSoftInputFromWindow(nameYourCollection.getApplicationWindowToken(), 0);
                break;


            case R.id.button1:
                isNewCollectionAdded = true;

                choseExisitng = false;
                cancel_layout1.setVisibility(View.VISIBLE);
                addProducts.setVisibility(View.INVISIBLE);
                addProducts.setText("Add Product");
                nameYourCollection.setVisibility(View.VISIBLE);
                newCollectionName.setVisibility(View.GONE);
                nameYourCollection.requestFocus();
                addCollectionLayout1.setVisibility(View.GONE);
                addCollectionLayout2.setVisibility(View.VISIBLE);

                squareCheck2.setVisibility(View.GONE);
                nameYourCollection.setVisibility(View.VISIBLE);
                collectionName.setVisibility(View.GONE);
                addProducts.setText("Add Product");
                productInflaterLayout.removeAllViews();
                products.clear();
                nameYourCollection.setText("");
                collection_id = null;

                break;
            case R.id.button2:
                choseExisitng = true;
                cancel_layout1.setVisibility(View.VISIBLE);
                checkLayout.setVisibility(View.VISIBLE);
                collectionListDialog = new ChooseCollectionDialog(mContext);
                collectionListDialog.getWindow().setBackgroundDrawableResource(R.color.real_transparent);
                collectionListDialog.show();
                choicelist = (ListView) collectionListDialog.findViewById(R.id.collection_listing);
                choicelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView textView = (TextView) view.findViewById(R.id.collection_name);
                        String text = textView.getText().toString().trim();
                        choice = text;
                        collectionListDialog.dismiss();
                        cancel_layout1.setVisibility(View.VISIBLE);
                        addCollectionLayout1.setVisibility(View.GONE);
                        addProducts.setVisibility(View.VISIBLE);
                        addCollectionLayout2.setVisibility(View.VISIBLE);
                        squareCheck.setVisibility(View.GONE);
                        squareCheck2.setVisibility(View.VISIBLE);
                        productInflaterLayout.setVisibility(View.VISIBLE);
                        nameYourCollection.setVisibility(View.GONE);
                        newCollectionName.setVisibility(View.GONE);
                        productLayout.setVisibility(View.VISIBLE);
                        collectionName.setVisibility(View.VISIBLE);
                        collectionName.setText(choice);
                        collectionName.setTextColor(Color.WHITE);
                        page = 0;
                        collection_id = collectionListDialog.geCheckedCollection(i).getId();
                        productInflaterLayout.removeAllViews();
                        products.clear();
                        initProducts();

                    }
                });
                break;
            case R.id.cancelImage:
                addCollectionLayout2.setVisibility(View.GONE);
                addCollectionLayout1.setVisibility(View.VISIBLE);
                addProducts.setText("");
                imm.hideSoftInputFromWindow(nameYourCollection.getApplicationWindowToken(), 0);
                break;
            case R.id.add_products_button:
                overflow2.triggerSlide();
                add_collection_text.setVisibility(View.GONE);
                post_text.setVisibility(View.GONE);
                search_tab_text.setVisibility(View.VISIBLE);
                FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
                searchProductFragment = new SearchProductFragment(overflow2, fragmentPostUploadTag, "", true, 3);
                transaction.replace(R.id.frame_upload_page, searchProductFragment, searchProductFragment.toString());
                transaction.addToBackStack(null);
                transaction.commit();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        search_tab_text.setCursorVisible(true);
                        search_tab_text.setFocusable(true);
                        search_tab_text.setFocusableInTouchMode(true);
                        search_tab_text.requestFocus();
                    }
                }, 0);

                if (!choseExisitng) {
                    if (products.size() > 0 && finalNewCollectionName != null) {
                        cancel_layout1.setVisibility(View.VISIBLE);
                        checkLayout.setVisibility(View.VISIBLE);
                        squareCheck.setVisibility(View.VISIBLE);
                        squareCheck.setImageResource(R.drawable.square_check);
                    } else {
                        checkLayout.setVisibility(View.GONE);
                        cancel_layout1.setVisibility(View.VISIBLE);
                    }
                } else {
                    cancel_layout1.setVisibility(View.VISIBLE);
                    checkLayout.setVisibility(View.VISIBLE);
                    squareCheck2.setVisibility(View.VISIBLE);
                    squareCheck2.setImageResource(R.drawable.square_check);
                }
                break;
        }
    }

    public void setInitialSearchText() {
        search_tab_text.setText("");
    }

    @Override
    public void onLoginSuccess(int socialNetworkID) {
/* SocialSharingActivity.hideProgress(); */
        networkidarray.add(networkId);
        if (networkId == FacebookSocialNetwork.ID)
            UserPreference.getInstance().setmIsFacebookSignedIn(true);
        if (networkId == TwitterSocialNetwork.ID)
            UserPreference.getInstance().setmIsTwitterSignedIn(true);
        if (networkId == InstagramSocialNetwork.ID)
            UserPreference.getInstance().setmIsInstagramSignedIn(true);
        checkedSocialSharingLogin();
        setShareClickable(true);

    }

    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
        if (networkId == TwitterSocialNetwork.ID) {
            ((TwitterSocialNetwork) mSocialNetworkManager.getSocialNetwork(networkId)).cancelLoginRequest();
        }
        checkedSocialSharingLogin();
        setShareClickable(true);
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        imm = (InputMethodManager)
                mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
        CollectionTextWatcher watcher = new CollectionTextWatcher();
        DescriptionTextWatcher watcher2 = new DescriptionTextWatcher();
        collectionName = (TextView) mainView.findViewById(R.id.collection_name_text);
        collectionName.setTypeface(FontUtility.setMontserratLight(mContext));
        search_tab_text = (EditText) mainView.findViewById(R.id.search_tab_text);
        search_tab_text.setTypeface(FontUtility.setMontserratLight(mContext));
        post_load = (LinearLayout) mainView.findViewById(R.id.post_load);
        existing_load = (LinearLayout) mainView.findViewById(R.id.existing_load);
        overflow2 = (MyMaterialContentOverflow3) mainView.findViewById(R.id.overflow2);
        switchFacebook = (SwitchCompat) mainView.findViewById(R.id.switchfacebook);
        switchTwitter = (SwitchCompat) mainView.findViewById(R.id.switchtwitter);
        switchInstagram = (SwitchCompat) mainView.findViewById(R.id.switchInstagram);
        switchPinterest = (SwitchCompat) mainView.findViewById(R.id.switchPinterest);
        cancelImage = (ImageView) mainView.findViewById(R.id.cancelImage);
        switchFacebook.setOnCheckedChangeListener(this);
        switchTwitter.setOnCheckedChangeListener(this);
        switchInstagram.setOnCheckedChangeListener(this);
        switchPinterest.setOnCheckedChangeListener(this);
        cancelImage.setOnClickListener(this);
        productInflaterLayout = (LinearLayout) mainView.findViewById(R.id.productInflaterLayout);
        addCollectionLayout2 = (LinearLayout) mainView.findViewById(R.id.add_collection_layout2);
        addCollectionLayout1 = (LinearLayout) mainView.findViewById(R.id.add_collection_layout);
        checkLayout = (LinearLayout) mainView.findViewById(R.id.square_check_layout);
        share_pin_layout = (RelativeLayout) mainView.findViewById(R.id.share_pin_layout);
        share_twi_layout = (RelativeLayout) mainView.findViewById(R.id.share_twi_layout);
        share_fb_layout = (RelativeLayout) mainView.findViewById(R.id.share_fb_layout);
        share_ins_layout = (RelativeLayout) mainView.findViewById(R.id.share_ins_layout);
        inspirationImage = (RoundedImageView) mainView.findViewById(R.id.inspirationImage);
        nameYourCollection = (EditText) mainView.findViewById(R.id.create_collection_text);
        nameYourCollection.setTypeface(FontUtility.setMontserratLight(mContext));
        nameYourCollection.addTextChangedListener(watcher);
        description_text = (EditText) mainView.findViewById(R.id.description_text);
        description_text.setTypeface(FontUtility.setMontserratLight(mContext));
        description_text.addTextChangedListener(watcher2);
        post_load.setVisibility(View.GONE);
        existing_load.setVisibility(View.GONE);

        main_content = (RelativeLayout) mainView.findViewById(R.id.main_content);

        main_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                description_text.clearFocus();
                description_text.setCursorVisible(false);
                imm.hideSoftInputFromWindow(nameYourCollection.getApplicationWindowToken(), 0);
                return false;
            }
        });
        add_collection_text = (TextView) mainView.findViewById(R.id.add_collection_text);
        add_collection_text.setTypeface(FontUtility.setMontserratLight(mContext));
        post_without_tag_text = (TextView) mainView.findViewById(R.id.post_without_tag_text);
        post_without_tag_text.setTypeface(FontUtility.setMontserratRegular(mContext));
        post_without_tag_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(TAG, "setOnClickListener");
                getConfirmation();
            }
        });
        productLayout = (HorizontalScrollView) mainView.findViewById(R.id.productLayout);
        overlay = (FrameLayout) mainView.findViewById(R.id.overlay);
        addProducts = (TextView) mainView.findViewById(R.id.add_products_button);
        addProducts.setTypeface(FontUtility.setMontserratLight(mContext));
        newCollectionName = (TextView) mainView.findViewById(R.id.new_collection_name);
        newCollectionName.setTypeface(FontUtility.setMontserratLight(mContext));
        button1 = (TextView) mainView.findViewById(R.id.button1);
        button1.setTypeface(FontUtility.setMontserratLight(mContext));
        textOR = (TextView) mainView.findViewById(R.id.textOR);
        textOR.setTypeface(FontUtility.setMontserratLight(mContext));
        button2 = (TextView) mainView.findViewById(R.id.button2);
        button2.setTypeface(FontUtility.setMontserratLight(mContext));
        new_collection_name = (TextView) mainView.findViewById(R.id.new_collection_name);
        new_collection_name.setTypeface(FontUtility.setMontserratLight(mContext));
        ins_text = (TextView) mainView.findViewById(R.id.ins_text);
        ins_text.setTypeface(FontUtility.setMontserratLight(mContext));
        fb_text = (TextView) mainView.findViewById(R.id.fb_text);
        fb_text.setTypeface(FontUtility.setMontserratLight(mContext));
        twitter_text = (TextView) mainView.findViewById(R.id.twitter_text);
        twitter_text.setTypeface(FontUtility.setMontserratLight(mContext));
        pinterest_text = (TextView) mainView.findViewById(R.id.pinterest_text);
        pinterest_text.setTypeface(FontUtility.setMontserratLight(mContext));
        post_text = (TextView) mainView.findViewById(R.id.post_text);
        post_text.setTypeface(FontUtility.setMontserratRegular(mContext));
        backIconLayout = (LinearLayout) mainView.findViewById(R.id.backIconLayout);
        post_text.setClickable(true);
        cancel_layout1 = (LinearLayout) mainView.findViewById(R.id.cancel_layout1);
        description_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description_text.setFocusable(true);
                description_text.requestFocus();
            }
        });
        search_tab_text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    String search_text = search_tab_text.getText().toString().trim();
                    Log.e(TAG, "down");
                    FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
                    searchProductFragment = new SearchProductFragment(overflow2, fragmentPostUploadTag, search_text, false);
                    transaction.replace(R.id.frame_upload_page, searchProductFragment, searchProductFragment.toString());
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
                return false;
            }
        });

        search_tab_text.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (overflow2.isOpen()) {
                    search_tab_text.setCursorVisible(true);
                    search_tab_text.setFocusable(true);
                    search_tab_text.setFocusableInTouchMode(true);
                    search_tab_text.requestFocus();
                }

                return false;
            }
        });
        overflow2.setOnCloseListener(new MyMaterialContentOverflow3.OnCloseListener() {
            @Override
            public void onClose() {
                search_tab_text.setVisibility(View.GONE);
                add_collection_text.setVisibility(View.VISIBLE);
                post_text.setVisibility(View.VISIBLE);
            }
        });
        nameYourCollection.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (validCollectionName()) {
                        checkLayout.setVisibility(View.GONE);
                        cancel_layout1.setVisibility(View.VISIBLE);
                        nameYourCollection.setCursorVisible(false);
                        nameYourCollection.setVisibility(View.GONE);
                        newCollectionName.setVisibility(View.VISIBLE);
                        newCollectionName.setText(finalNewCollectionName);
                        addProducts.setVisibility(View.VISIBLE);
                    } else
                        AlertUtils.showToast(mContext, "Please enter collection name");
                    return true;
                }
                return false;
            }
        });
        squareCheck = (ImageView) mainView.findViewById(R.id.square_check);
        squareCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm.hideSoftInputFromWindow(nameYourCollection.getApplicationWindowToken(), 0);
                nameYourCollection.setCursorVisible(false);
                if (validCollectionName() && addProducts.getVisibility() == View.INVISIBLE) {
                    addProducts.setVisibility(View.VISIBLE);
                    nameYourCollection.setVisibility(View.GONE);
                    newCollectionName.setVisibility(View.VISIBLE);
                    newCollectionName.setText(finalNewCollectionName);
                    squareCheck.setVisibility(View.GONE);
                    cancel_layout1.setVisibility(View.VISIBLE);
                    productInflaterLayout.setVisibility(View.VISIBLE);
                } else if (validCollectionName() && addProducts.getVisibility() == View.VISIBLE) {
                    if (products.size() > 0 && validCollectionName()) {
                        checkLayout.setVisibility(View.GONE);

                        if (products.size() > 1 && !TextUtils.isEmpty(collection_id)) {
                            addProductInCollection(collection_id, currentProduct);

                        } else {
                            addCollection();
                        }
                    }
                } else
                    AlertUtils.showToast(mContext, "Please enter collection name");
            }
        });
        squareCheck2 = (ImageView) mainView.findViewById(R.id.square_check2);
        squareCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                squareCheck2.setVisibility(View.GONE);
                cancel_layout1.setVisibility(View.VISIBLE);
                addProductInCollection(collection_id, currentProduct);
                finalNewCollectionName = collectionName.getText().toString();
                setTagInformation();

            }
        });
        setTouchEvent();
        callsocialsharing();
        checkedSocialSharingLogin();
    }

    @Override
    public void setData(Bundle bundle) {
        if (!isUpdate) {
            inspirationImage.setImageBitmap(bmp);
        } else {
            CommonUtility.setImageHigh(mContext, inspirationImage, inspiration.getInspiration_image());
            description_text.setText(inspiration.getDescription());
        }
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        backIconLayout.setOnClickListener(this);
        post_text.setOnClickListener(this);
        share_twi_layout.setOnClickListener(this);
        share_pin_layout.setOnClickListener(this);
        share_fb_layout.setOnClickListener(this);
        share_ins_layout.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        cancelImage.setOnClickListener(this);
        addProducts.setOnClickListener(this);

    }

    @Override
    public void onSocialNetworkManagerInitialized() {
//when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);

            initSocialNetwork(socialNetwork);
        }
    }

    private void setTagInformation() {
        taggedItem.setSelectedItem(collection_id);
        taggedItem.setSelectedItemName(finalNewCollectionName);

        taggedItem.setSelectedItemType("collection");

        taggedItem.setSelectedItemXY("");

        taggedProducts.setSelectedProductsId(collection_id);
        taggedProducts.setSelectedProducts(collection_id);

        taggedProducts.setSelectedProductsXY("");

    }

    private void validateInput() {
        description = description_text.getText().toString().trim();
        if (bmp == null && imageUrl == null && !isUpdate) {
//If an image has been selected
            AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
        } else if (TextUtils.isEmpty(description) && isUpdate) {

            AlertUtils.showToast(mContext, // If a description has been added
                    R.string.alert_no_description_entered);
        } else {
            if (TextUtils.isEmpty(description)) {
                AlertUtils.showToast(mContext, // If a description has been added
                        "Please enter description");
            } else if (checkInternet()) {

                if (isUpdate) {

                    updateDescription();
                } else {

                    CommonUtility.hideSoftKeyboard(mContext);

                    if (taggedProductList.size() > 0) {

                        productAddedCount = 0;
                        for (int i = 0; i < taggedProductList.size(); i++) {

                        }
                    } else {
                        Log.w(TAG, "Going in GetImage().execute(); **");
                        post_load.setVisibility(View.VISIBLE);
                        new GetImage().execute();
                    }
                }

            }
        }
    }

    private void addProductToTaggedCollection(Product product) {
        String from_user_id = TextUtils.isEmpty(product.getFrom_user_id()) ? "" : product.getFrom_user_id();
        String from_collection_id = TextUtils.isEmpty(product.getFrom_collection_id()) ? "" : product.getFrom_collection_id();

        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                productAddedCount++;
                Syso.info("In handleOnSuccess>>" + object);
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;


            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CollectionApiRes response = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        collectionApi.addProductInCollection(UserPreference.getInstance().getUserID(), taggedItem.getSelectedItem(), product.getId(), from_user_id, from_collection_id, product.getProductimageurl());
        collectionApi.execute();
    }

    public void updateDescription() {
        try {
            final EditInspirationApi editPostApi = new EditInspirationApi(new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    CommonRes serverResponse = (CommonRes) object;

                    if (serverResponse.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {

                        AlertUtils.showToast(mContext, "Post updated successfully");
                        inspiration.setDescription(description_text.getText().toString().trim());
                        description_text.setCursorVisible(false);

                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    Syso.info("In handleOnFailure>>" + object);
                    if (object != null) {
                        CommonRes response = (CommonRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                }
            });

            editPostApi.editDescription(inspiration.getUser_id(), inspiration.getInspiration_id(), description_text.getText().toString().trim());
            editPostApi.execute();
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }

    private void sharePost() {


        if (switchInstagram.isChecked()) {
            postToInstagram();
        }

        if (switchFacebook.isChecked() || switchTwitter.isChecked()) {
            for (int i = 0; i < networkidarray.size(); i++) {
                startProfile(networkidarray.get(i));
            }
        }

        /*
        if (switchInstagram.isChecked() || switchTwitter.isChecked() || switchFacebook.isChecked()) {
            for (int i = 0; i < networkidarray.size(); i++) {
                startProfile(networkidarray.get(i));
            }
        }
        */

        if (switchPinterest.isChecked()) {
            pintrestsharingimage();
            onSavePin(mContext, imageServerUri, UserPreference.getInstance().getmPinterestBoardId(), description_text.getText().toString() + " " + SHARE_POST_LINK, imageServerUri);
        }
    }

    private void pintrestsharingimage() {

        PinterestUtility.getInstance(mContext, this).init();
    }

    private void callSocialSharing() {

        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        if (!socialNetwork.isConnected()) {

            if (networkId != 0) {
                socialNetwork.requestLogin();
            } else {
                Toast.makeText(getActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
            }
        } else
            setShareClickable(true);
    }

    private void startProfile(final int networkId) {
        Log.w(TAG, "startProfile");

        String shareTEXT = description_text.getText().toString() + " " + SHARE_POST_LINK;
        socialNetwork = FragmentPostUploadTag.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.requestCurrentPerson();
        socialNetwork.requestPostPhoto(new File(imageUrl), shareTEXT, new OnPostingCompleteListener() {
            @Override
            public void onPostSuccessfully(int socialNetworkID) {
                Log.w(TAG, "You Posted Photo : " + networkId);
                sucesssharecount++;
                if (networkidarray.size() == sucesssharecount) {
                    Toast.makeText(getActivity(), "Image posted successfully", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();

            }
        });


    }

    private void postToInstagram() {

        Log.w(TAG, "shareToInstagram()" + imageUrl);
        File media = new File(imageUrl);
        Uri uri = Uri.fromFile(media);
        String shareTEXT = description_text.getText().toString() + " " + SHARE_POST_LINK;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareTEXT);
        shareIntent.setPackage("com.instagram.android");
        startActivity(Intent.createChooser(shareIntent, "Share to"));
    }

    public void onSavePin(Context c, String imageUrl, String boardId, String text, String linkUrl) {
        Log.w(TAG, "onSavePin()");
        if (!Utils.isEmpty(text) && !Utils.isEmpty(boardId) && !Utils.isEmpty(imageUrl)) {
            PDKClient.getInstance().createPin(text, boardId, imageUrl, linkUrl, new PDKCallback() {
                @Override
                public void onSuccess(PDKResponse response) {
                    Log.d(getClass().getName(), response.getData().toString());
                    Syso.info("12345678 >>> output" + response.getData().toString());
                    AlertUtils.showToast(mContext, "Shared Successfully");
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

    private void uploadInspiration(byte[] image) {
        Toast.makeText(mContext, "Uploading, you will be lead to main page", Toast.LENGTH_SHORT).show();

        Log.w(TAG, "uploadInspiration()");
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        InspirationRes inspirationRes = (InspirationRes) object;
                        if (inspirationRes != null) {
                            AlertUtils.showToast(mContext,
                                    inspirationRes.getMessage());
                            SHARE_POST_LINK = SHARE_POST_LINK + inspirationRes.getId();
                            imageServerUri = inspirationRes.getImage_url();
                            sharePost();
                            ((HomeActivity) mContext).mFragmentStack.clear();
                            ((HomeActivity) mContext).addFragment(new FragmentInspirationSection());
                            post_load.setVisibility(View.GONE);
                            post_text.setClickable(true);

                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        post_load.setVisibility(View.GONE);
                        post_text.setClickable(true);
                        if (object != null) {
                            InspirationRes response = (InspirationRes) object;
                            AlertUtils.showToast(mContext, response.getMessage());
                        } else {
                            AlertUtils.showToast(mContext, R.string.invalid_response);
                        }
                    }
                });
        if (image != null || imageUrl != null) {
            Log.w(TAG, "Image present");
            String width = bmp != null ? String.valueOf(bmp.getWidth()) : "";
            String height = bmp != null ? String.valueOf(bmp.getHeight()) : "";

            inspirationSectionApi.uploadImage(UserPreference.getInstance()
                            .getUserID(), image, "yes", description.trim(), taggedItem,
                    taggedProducts, width + ".000000", height + ".000000", imageUrl);
            inspirationSectionApi.execute();


        } else {
            AlertUtils.showToast(mContext, "No image found");
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.switchfacebook:
                if (isChecked && !UserPreference.getInstance().getmIsFacebookSignedIn()) {
                    switchFacebook.setChecked(false);
                    networkId = FacebookSocialNetwork.ID;
                    callSocialSharing();

                } else if (!isChecked) {
                    networkId = FacebookSocialNetwork.ID;
                    if (networkidarray.contains(networkId)) {
                        networkidarray.remove(new Integer(networkId));
                    }
                } else if (isChecked) {
                }
                break;

            case R.id.switchtwitter:

                if (isChecked && !UserPreference.getInstance().getmIsTwitterSignedIn()) {
                    networkId = TwitterSocialNetwork.ID;

                    switchTwitter.setChecked(false);
                    callSocialSharing();
                } else if (!isChecked) {
                } else if (isChecked) {
                }


                break;
            case R.id.switchInstagram:
                if (isChecked && !UserPreference.getInstance().getmIsInstagramSignedIn()) {
                    switchInstagram.setChecked(false);
                    if (((HomeActivity) mContext).appInstalledOrNot("com.instagram.android")) {

                        networkId = InstagramSocialNetwork.ID;
                        callSocialSharing();
                        switchInstagram.setChecked(true);
                    } else {
                        RedirectToPlayStore redirectToPlayStore = new RedirectToPlayStore(mContext, "instagram");
                        redirectToPlayStore.getWindow().setBackgroundDrawableResource(R.color.real_transparent);
                        redirectToPlayStore.show();
                    }

                } else if (!isChecked) {
                    networkId = InstagramSocialNetwork.ID;
                    if (networkidarray.contains(networkId)) {
                        networkidarray.remove(new Integer(networkId));
                    }
                } else if (isChecked) {
                }

                break;

            case R.id.switchPinterest:
                if (isChecked && !UserPreference.getInstance().getmIsPinterestSignedIn()) {
                    switchPinterest.setChecked(false);
                    if (((HomeActivity) mContext).appInstalledOrNot("com.pinterest")) {
                        pintrestsharingimage();
                        switchPinterest.setChecked(true);
                    } else {
                        RedirectToPlayStore redirectToPlayStore = new RedirectToPlayStore(mContext, "pinterest");
                        redirectToPlayStore.show();
                    }
                } else if (!isChecked) {
                } else if (isChecked) {
                }
                break;

        }
    }

    private void setShareClickable(boolean isClickable) {
        switchFacebook.setClickable(isClickable);
        switchInstagram.setClickable(isClickable);
        switchPinterest.setClickable(isClickable);
        switchTwitter.setClickable(isClickable);
    }

    public void checkedSocialSharingLogin() {
        Log.w(TAG, "checkedSocialSharingLogin");
        networkidarray.clear();
        if (UserPreference.getInstance().getmIsFacebookSignedIn()) {
            networkId = FacebookSocialNetwork.ID;
            networkidarray.add(networkId);
            switchFacebook.setChecked(true);
        } else {
            switchFacebook.setChecked(false);
        }

        if (UserPreference.getInstance().getmIsTwitterSignedIn()) {
            networkId = TwitterSocialNetwork.ID;
            networkidarray.add(networkId);
            switchTwitter.setChecked(true);
        } else {
            switchTwitter.setChecked(false);
        }

        if (UserPreference.getInstance().getmIsInstagramSignedIn()) {
            Log.w(TAG, "Instagram Signed In");
            networkId = InstagramSocialNetwork.ID;
            switchInstagram.setChecked(true);
        } else {
            Log.w(TAG, "Instagram Not Signed In");
            switchInstagram.setChecked(false);
        }

        if (UserPreference.getInstance().getmIsPinterestSignedIn()) {
            switchPinterest.setChecked(true);
        } else {
            switchPinterest.setChecked(false);
        }

    }

    private void callsocialsharing() {
        Log.w(TAG, "callsocialsharing");
        //Get Keys for initiate SocialNetworks
        String TWITTER_CONSUMER_KEY = getActivity().getString(R.string.twitter_consumer_key);
        String TWITTER_CONSUMER_SECRET = getActivity().getString(R.string.twitter_consumer_secret);
        String TWITTER_CALLBACK_URL = "oauth://ASNE";

        String INSTAGRAM_CLIENT_ID = AppConstants.INSTAGRAM_CLIENT_ID;
        String INSTAGRAM_CLIENT_SECRET = AppConstants.INSTAGRAM_CLIENT_SECRET;
        String INSTAGRAM_REDIRECT_URI = AppConstants.INSTAGRAM_REDIRECT_URI;
        String scope = "likes+comments";

        //Chose permissions
        ArrayList<String> fbScope = new ArrayList<String>();
        fbScope.addAll(Arrays.asList("public_profile, email, user_friends"));
        String linkedInScope = "r_basicprofile+r_fullprofile+rw_nus+r_network+w_messages+r_emailaddress+r_contactinfo";

        //Use manager to manage SocialNetworks
        mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(HomeActivity.SOCIAL_NETWORK_TAG);

        //Check if manager exist
        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

            //Init and add to manager FacebookSocialNetwork
            FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);
            mSocialNetworkManager.addSocialNetwork(fbNetwork);

            //Init and add to manager TwitterSocialNetwork
            TwitterSocialNetwork twNetwork = new TwitterSocialNetwork(this, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, TWITTER_CALLBACK_URL);
            mSocialNetworkManager.addSocialNetwork(twNetwork);

            InstagramSocialNetwork instaNetwork = new InstagramSocialNetwork(this, INSTAGRAM_CLIENT_ID, INSTAGRAM_CLIENT_SECRET, INSTAGRAM_REDIRECT_URI, scope);
            mSocialNetworkManager.addSocialNetwork(instaNetwork);

            getFragmentManager().beginTransaction().add(mSocialNetworkManager, HomeActivity.SOCIAL_NETWORK_TAG).commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } else {
            //if manager exist - get and setup login only for initialized SocialNetworks
            if (!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(((HomeActivity) mContext));
                    initSocialNetwork(socialNetwork);
                }
            }
        }
    }

    private void initSocialNetwork(SocialNetwork socialNetwork) {
        if (socialNetwork.isConnected()) {
            switch (socialNetwork.getID()) {
                case FacebookSocialNetwork.ID:

                    break;
                case TwitterSocialNetwork.ID:

                    break;
                case InstagramSocialNetwork.ID:

                    break;

            }
        }

    }

    private void getConfirmation() {
        Log.w(TAG, "getConfirmation()");
        final AlertDialog.Builder builder;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (checkInternet()) {
                            isNewCollectionAdded = false;
                            addProductListNew.clear();
                            taggedProductList.clear();
                            taggedItem = new TaggedItem();
                            taggedProducts = new TaggedProducts();
                            isUploadWithoutTag = true;

                            validateInput();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to continue without tagging?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void addProductToNewCollection(Product product) {
        Log.w(TAG, "addProductToNewCollection22()");

        addProductListNew1.add(product);

    }

    private void addProductInCollection(String collection_id, Product product) {

        String from_user_id = TextUtils.isEmpty(product.getFrom_user_id()) ? "" :
                product.getFrom_user_id();
        String from_collection_id = TextUtils.isEmpty(product.getFrom_collection_id()) ? "" :
                product.getFrom_collection_id();

        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                AlertUtils.showToast(mContext, collectionApiRes.getMessage());
                if (!choseExisitng) {
                    checkLayout.setVisibility(View.GONE);

                } else {
                    checkLayout.setVisibility(View.VISIBLE);
                }
                cancel_layout1.setVisibility(View.VISIBLE);

                finalNewCollectionName = nameYourCollection.getText().toString();
                setTagInformation();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    CollectionApiRes response = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        collectionApi.addProductInCollection(UserPreference.getInstance().getUserID(),
                collection_id, product.getId(), from_user_id, from_collection_id,
                product.getProductimageurl());
        collectionApi.execute();
    }

    public void chooseProduct(Product product) {

        isNewCollectionAdded = true;
        addProductListNew.add(product);
        addProductToNewCollection(product);
        this.currentProduct = product;

        updateListAdd(product);

        overflow2.triggerClose();
    }


    private void setTouchEvent() {
        Log.w(TAG, "setTouchEvent()");
        tagView = new TagView(mContext);

    }

    public void getProducts() {
        final ProductBasedOnBrandApi productBasedOnBrandApi =
                new ProductBasedOnBrandApi(new ServiceCallback() {
                    @Override
                    public void handleOnSuccess(Object object) {
                        ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
                        List<Product> data = productBasedOnBrandRes.getData();
                        products.addAll(data);
                        productInflaterLayout.removeAllViews();
                        productInflaterLayout.
                                addView(new ProductUI(mContext, 200, 200, products, false).getView());
                        //IMPORTANT: assume call api one time, we can get 15 products at most
                        if (data.size() == 15) {
                            page++;
                            getProducts();
                        }
                        existing_load.setVisibility(View.GONE);
                        addProducts.setText("Add Product");
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {

                        existing_load.setVisibility(View.GONE);
                    }
                });
        productBasedOnBrandApi.getProductsBasedOnCollectionList
                (UserPreference.getInstance().getUserID(), String.valueOf(page), collection_id);
        productBasedOnBrandApi.execute();
    }


    public boolean validCollectionName() {
        String name = nameYourCollection.getText().toString();
        if (name != null && name.length() != 0) {
            finalNewCollectionName = name.trim();
            return true;
        }
        return false;
    }

    public void getNewCollectionId() {
        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                collectionLists = collectionApiRes.getCollection();
                if (collectionLists != null && collectionLists.size() > 0) {
                    collection_id = collectionLists.get(collectionLists.size() - 1).getId();

                    checkLayout.setVisibility(View.GONE);
                    cancel_layout1.setVisibility(View.GONE);


                    newCollectionName.setText(collectionLists.get(collectionLists.size() - 1).getName());
                    addProductInCollection(collection_id, currentProduct);

                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {

                    CollectionApiRes response = (CollectionApiRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());

                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        collectionApi.getCollectionList(UserPreference.getInstance().getUserID());
        collectionApi.execute();
    }

    public void initProducts() {
        getProducts();
        productLayout.scrollTo(0, 0);
    }

    private void addCollection() {
        final AddCollectionApi collectionApi = new AddCollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                AlertUtils.showToast(mContext, "Collection added");
                nameYourCollection.clearFocus();
                getNewCollectionId();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

            }
        });
        if (finalNewCollectionName != null && finalNewCollectionName.length() > 0)
            collectionApi.addNewCollection(finalNewCollectionName);
        collectionApi.execute();
    }

    private void updateListAdd(Product product) {
        products.add(0, product);

        if (products.size() > 0 && validCollectionName()) {
            checkLayout.setVisibility(View.VISIBLE);
            squareCheck.setVisibility(View.VISIBLE);
            squareCheck.setImageResource(R.drawable.square_check);
            cancel_layout1.setVisibility(View.VISIBLE);

        }


        productInflaterLayout.removeAllViews();
        productInflaterLayout.addView(new ProductUI(mContext, 200, 200, products, false).getView());
    }

    private class GetImage extends AsyncTask<Void, Void, byte[]> {

        @Override
        protected void onPreExecute() {
            Log.w(TAG, "GetImage() onPreExecute()");
            super.onPreExecute();
        }

        @Override
        protected byte[] doInBackground(Void... params) {

            // TODO Auto-generated method stub
            Log.w(TAG, "GetImage() doInBackground()");
            if (isImage.equals("no")) {
                return byteArray;
            } else {
                return PictureUtils.getByteArray2(bmp);
            }

        }

        @Override
        protected void onPostExecute(byte[] result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.w(TAG, "Going in uploadInspiration() 1");
            uploadInspiration(result);
        }
    }

    private class CollectionTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (validCollectionName()) {
                cancel_layout1.setVisibility(View.GONE);
                checkLayout.setVisibility(View.VISIBLE);
                squareCheck.setVisibility(View.VISIBLE);
                squareCheck.setImageResource(R.drawable.square_check);
            } else {
                checkLayout.setVisibility(View.GONE);
                cancel_layout1.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class DescriptionTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String des = description_text.getText().toString().trim();
            if (TextUtils.isEmpty(des)) {
                post_text.setBackgroundResource(R.drawable.grey_corner_button);
            } else {
                post_text.setBackgroundResource(R.drawable.green_corner_button_post);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
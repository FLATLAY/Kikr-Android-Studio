package com.kikr.post_upload;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.instagram.InstagramSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;
import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.AutocompleteCustomArrayAdapter;
import com.kikr.chip.AutoSuggestProduct;
import com.kikr.chip.TagsEditText;
import com.kikr.dialog.PinterestBoardDialog;
import com.kikr.fragment.FragmentInspirationImageTag;
import com.kikr.fragment.FragmentInspirationSection;
import com.kikr.fragment.MainFragmentContainer;
import com.kikr.ui.CustomAutoCompleteView;
import com.kikr.ui.PostUploadCommentsUI;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.ui.TagView;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.PictureUtils;
import com.kikrlib.api.AddCollectionApi;
import com.kikrlib.api.CollectionApi;
import com.kikrlib.api.EditInspirationApi;
import com.kikrlib.api.InspirationSectionApi;
import com.kikrlib.api.MyProfileApi;
import com.kikrlib.api.SearchApi;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.InterestSection;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProfileCollectionList;
import com.kikrlib.bean.SearchUser;
import com.kikrlib.bean.TaggedItem;
import com.kikrlib.bean.TaggedProducts;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.AddCollectionApiRes;
import com.kikrlib.service.res.CollectionApiRes;
import com.kikrlib.service.res.CommonRes;
import com.kikrlib.service.res.InspirationRes;
import com.kikrlib.service.res.MyProfileRes;
import com.kikrlib.service.res.SearchRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Constants;
import com.kikrlib.utils.StringUtils;
import com.kikrlib.utils.Syso;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class FragmentPostUploadTag extends BaseFragment implements CompoundButton.OnCheckedChangeListener, TagsEditText.TagsEditListener, View.OnClickListener, OnLoginCompleteListener, SocialNetworkManager.OnInitializationCompleteListener {
    private ProgressBar progressBarCollection;
    int click;
    //social network variable----------
    public static SocialNetworkManager mSocialNetworkManager;
    public static final String SOCIAL_NETWORK_TAG = "SocialIntegrationMain.SOCIAL_NETWORK_TAG";
    private static final String NETWORK_ID = "NETWORK_ID";
    private SocialNetwork socialNetwork;
    private ImageView facebook, twitter, instagram, pintrest;
    private ImageView instagramselected,twittergreenimage,fbselected,pintrestselected;
    private static final String pinappID = "4839869969114082403";
    int networkId = 0;
    private PDKClient pdkClient;

    private List<ProfileCollectionList> collectionLists = new ArrayList<ProfileCollectionList>();
    private AdapterPostCollectionList collectionListAdapter;


    private int lastTopValueAssigned = 0;

    ListView listView;
    FragmentPostUploadTag cab;

    View view;

    private TextView uploadTextView, tagBrandText, tagPeopleText;
    private ImageView postImageView;
    private EditText descriptionEditText;

    private List<Inspiration> product_list = new ArrayList<Inspiration>();
    private TaggedItem taggedItem = new TaggedItem();


    private String description;
    byte[] byteArray;
    private String isImage;
    private String filePath;

    ImageView imageUploadView;


    LinearLayout progressbar;
    Boolean checkvalue = true;
    TextView tvTagWithoutTag, tvTagProduct;
    boolean isTagged = false;
    boolean isUpdate;
    public static boolean isUploadWithoutTag = false;


    FrameLayout productSearchLayout;
    private TagView tagView;

    private Bitmap bmp;
    private ImageView uploadImageView;
    private FrameLayout overlay;
    private CustomAutoCompleteView searchUserEditText;
    private List<InterestSection> list = new ArrayList<InterestSection>();
    private ArrayList<SearchUser> usersList = new ArrayList<SearchUser>();
    //	private TextView textView;

    private ArrayList<String> temp_usersList = new ArrayList<String>();
    private ArrayList<String> temp_usersListId = new ArrayList<String>();
    private ArrayList<Integer> networkidarray = new ArrayList<Integer>();
    private AutocompleteCustomArrayAdapter adapter;
    private float x = 0, y = 0;
    private RadioGroup tagRadioGroup;
    private RadioButton peopleBtn, storeBtn, brandBtn;
    //	private String PEOPLE = "user";
//	private String BRAND = "brand";
//	private String STORE = "store";
    private String PRODUCT = "product";
    private String COLLECTION = "collection";
    private String isSelected;

    ArrayList<Product> taggedProductList = new ArrayList<>();
    ArrayList<Product> addProductListNew = new ArrayList<>();
    private TaggedItem taggedItemLocal = new TaggedItem(), selectedItem;
    private TaggedProducts taggedProducts = new TaggedProducts();
    private boolean isTaggingProduct = true;
    private List<String> selectedProductsId = new ArrayList<String>();
    private List<String> selectedProducts = new ArrayList<String>();
    private List<String> selectedProductsXY = new ArrayList<String>();
    private String START_TEXT = "Tag Product";
    private float scaleFactor = 1;
    private Inspiration inspiration;
    private String url;
    private ProgressBarDialog mProgressBarDialog;
    private String imageUrl;
    private LinearLayout linearEditTextView;
    private TextView collection_text;
    boolean isLoading = false, isFirstTime = false;
    int pageno = 0;


    public List<Product> data = new ArrayList<Product>();
    String searchString;
    TextView txtDone, tvCreateNewCollection;
    ImageView imgSearch, imgAddProduct;
    private TagsEditText multiAutoCompleteTextView;

    AutoSuggestProduct autoSuggestProduct;
    ProfileCollectionList taggedCollection = null;
    int productAddedCount = 0;
    ProgressBar productProgressBar;

    EditText etCollectionName;
    RelativeLayout newCollectionLayout;
    HorizontalScrollView productLayout;
    LinearLayout productInflaterLayout;
    boolean isAddProduct = false;
    private boolean isNewCollectionAdded;
    SwitchCompat switchFacebook, switchTwitter, switchInstagram, switchPinterest;
    TextView facebooktext,twittertext,instagramtext,pintresttext;

    public FragmentPostUploadTag(Inspiration inspiration) {
        this.inspiration = inspiration;
        this.isUpdate = true;

        isUploadWithoutTag = false;
    }

    public FragmentPostUploadTag(Inspiration inspiration, String isImage) {
        this.inspiration = inspiration;
        this.isImage = isImage;
    }

    public FragmentPostUploadTag(Bitmap bmp, String imageUrl, String isImage) {
        this.bmp = bmp;
        this.isUpdate = false;
        this.isImage = isImage;
        this.imageUrl = imageUrl;
    }

    public FragmentPostUploadTag(byte[] byteArray, String isImage, String filePath) {
        this.byteArray = byteArray;
        this.filePath = filePath;
        this.isImage = isImage;
        this.isUpdate = false;
    }

    public FragmentPostUploadTag() {
        this.isUpdate = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post_upload_tag, null);
        return view;
    }


    @Override
    public void initUI(Bundle savedInstanceState) {
        cab = this;
        mProgressBarDialog = new ProgressBarDialog(mContext);
        autoSuggestProduct = new AutoSuggestProduct(mContext);
        multiAutoCompleteTextView = (TagsEditText) view.findViewById(R.id.searchMultiText);
        listView = (ListView) view.findViewById(R.id.collectionListView);
        progressBarCollection = (ProgressBar) view.findViewById(R.id.progressBarCollection);
        productProgressBar = (ProgressBar) view.findViewById(R.id.progressProductBar);
        progressbar = (LinearLayout) view.findViewById(R.id.progressBar);

        tvTagWithoutTag = (TextView) view.findViewById(R.id.tvTagWithoutTag);
        productSearchLayout = (FrameLayout) view.findViewById(R.id.product_layout_main);
        txtDone = (TextView) view.findViewById(R.id.txtDone);
        imgSearch = (ImageView) view.findViewById(R.id.imgSearch);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        tvTagWithoutTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConfirmation();
            }
        });

//        txtDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isFirstTime = true;
//                getProductListApi();
//            }
//        });
//        imgSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isFirstTime = true;
//                getProductListApi();
//            }
//        });


        setMultiAutoText();
    }

    private void setMultiAutoText() {


        multiAutoCompleteTextView.setHint("Search for products to add to post.");


        multiAutoCompleteTextView.setmTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] str = s.toString().split(" ");
                Syso.info("OnTextChanged", s.toString());
                if (str.length > 0)
                    if ((str[str.length - 1].length() == 5 || str[str.length - 1].length() == 3) && !autoSuggestProduct.isStringExist(str[str.length - 1])) {
                        autoSuggestProduct.search(s.toString(), multiAutoCompleteTextView, productProgressBar);
                    }
                if (s.length() == 0) {
                    autoSuggestProduct.clearPreviousStrings();


                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                multiAutoCompleteTextView.setTags();


            }
        });
        multiAutoCompleteTextView.setTagsListener(this);
        multiAutoCompleteTextView.setTagsWithSpacesEnabled(true);
//        multiAutoCompleteTextView.setAdapter(new ArrayAdapter<>(mContext,
//                android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.fruits)));
        multiAutoCompleteTextView.setThreshold(1);
        // multiAutoCompleteTextView.set

        multiAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoSuggestProduct.cancelExecution();
                Product product = (Product) multiAutoCompleteTextView.getAdapter().getItem(position);

                CommonUtility.hideSoftKeyboard(mContext);
                multiAutoCompleteTextView.setText("");

                if (isAddProduct) {

                    addProductToNewCollection(product);


                } else {
                    taggedProductList.add(product);
                    plotTagOnImage(x, y, false, product.getProductname());
                }

            }
        });
    }

    private void addProductToNewCollection(Product product) {

        setupHeader(false);
        addProductListNew.add(product);

        productInflaterLayout.removeAllViews();
        productInflaterLayout.addView(new PostUploadCommentsUI(mContext, null, true, addProductListNew).getView());
        productLayout.scrollTo(0, 0);
        if (addProductListNew.size() > 0) {
            newCollectionLayout.setBackgroundColor(getResources().getColor(R.color.header_background));
            hideSubmit();
            collectionListAdapter.clearSelection();
        }


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void addScrollListenerForSDKsAbove11() {
        if (Integer.valueOf(Build.VERSION.SDK_INT) > 11) {
            // setting onScroll listener
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    parallaxImage(imageUploadView);
                }
            });
        }
    }


    private void inflateHeader() {
        // inflate custom header and attach it to the list
        LayoutInflater inflater = mContext.getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.post_upload_listview_custom_header, listView, false);
        listView.addHeaderView(header);

        // we take the background image and button reference from the header

        imgAddProduct = (ImageView) header.findViewById(R.id.imgAddProduct);
        newCollectionLayout = (RelativeLayout) header.findViewById(R.id.newPostLayout);
        etCollectionName = (EditText) header.findViewById(R.id.etCollectionName);
        productLayout = (HorizontalScrollView) header.findViewById(R.id.productLayout);
        productInflaterLayout = (LinearLayout) header.findViewById(R.id.productInflaterLayout);
        tvCreateNewCollection = (TextView) header.findViewById(R.id.tvCreateNew);

        imageUploadView = (ImageView) header.findViewById(R.id.imageUploadView);
        descriptionEditText = (EditText) header.findViewById(R.id.etDescription);
        tvTagProduct = (TextView) header.findViewById(R.id.tvTagProduct);
        overlay = (FrameLayout) header.findViewById(R.id.overlay);
        switchFacebook = (SwitchCompat) header.findViewById(R.id.switchfacebook);
        switchTwitter = (SwitchCompat) header.findViewById(R.id.switchtwitter);

        switchInstagram = (SwitchCompat) header.findViewById(R.id.switchInstagram);
        switchPinterest = (SwitchCompat) header.findViewById(R.id.switchPinterest);
        facebooktext= (TextView) header.findViewById(R.id.facebooktext);
        twittertext= (TextView) header.findViewById(R.id.twittertext);
        instagramtext= (TextView) header.findViewById(R.id.instagramtext);
        pintresttext= (TextView) header.findViewById(R.id.pintresttext);
        facebook=(ImageView)header.findViewById(R.id.facebook);
        twitter=(ImageView)header.findViewById(R.id.twitter);
        instagram= (ImageView) header.findViewById(R.id.instagram);
        pintrest=(ImageView) header.findViewById(R.id.pintrest);
        switchFacebook.setOnCheckedChangeListener(this);
        switchTwitter.setOnCheckedChangeListener(this);
        switchInstagram.setOnCheckedChangeListener(this);
        switchPinterest.setOnCheckedChangeListener(this);


//        pintrest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pintrestsharingimage();
//                //    Toast.makeText(getActivity(), "click on pintrest", Toast.LENGTH_LONG).show();
//            }
//        });

        callsocialsharing();


        descriptionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    CommonUtility.hideSoftKeyboard(mContext);
                }
                return false;
            }
        });
        tvTagProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.hideSoftKeyboard(mContext);
                if (checkInternet()) {
                    addFragment(new FragmentInspirationImageTag(bmp, imageUrl, taggedProducts));
                }
            }
        });

        setTouchEvent();


    }

    private void setTouchEvent() {
        tagView = new TagView(mContext);
        imageUploadView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isAddProduct = false;
                showKeypad();
                plotTagOnImage(event.getX(), event.getY(), true, START_TEXT);
                return false;
            }
        });
    }


    private void plotTagOnImage(float x, float y, boolean isHideActionBar, String TAG_TEXT) {
        setupHeader(isHideActionBar);


        this.x = x;
        this.y = y;
        if (!isTaggingProduct || tagView.getText().equals(START_TEXT)) {
            if (overlay.getChildCount() > 1)
                overlay.removeViewAt(overlay.getChildCount() - 1);
        } else {
            tagView = new TagView(mContext);
            tagView.setBackgroundColor(Color.parseColor("#80000000"));
            tagView.setTextColor(Color.parseColor("#ffffff"));
        }
        Syso.info("12345678 >>>>>>> X" + x + ", Y:" + y);

        if (!isTaggingProduct && !TextUtils.isEmpty(taggedItemLocal.getSelectedItemName())) {
            tagView.setTagText(taggedItemLocal.getSelectedItemName());
        } else {
            tagView.setTagText(TAG_TEXT);
        }

        tagView.setXY(x, y);
        overlay.addView(tagView);

    }


    private void parallaxImage(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        if (lastTopValueAssigned != rect.top) {
            lastTopValueAssigned = rect.top;
            view.setY((float) (rect.top / 2.0));
        }
    }

    @Override
    public void setData(Bundle bundle) {
        try {

            //  mAdapter = new SelectionAdapter(this, R.layout.row_list_item, R.id.textView1, data);
            inflateHeader();
            addScrollListenerForSDKsAbove11();

            if (!isUpdate) {
                imageUploadView.setImageBitmap(bmp);


            } else {
                if (inspiration != null) {
                    descriptionEditText.setText(inspiration.getDescription());
                }
            }
            getCollectionList();
            listView.setAdapter(collectionListAdapter);
            // listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            if (Build.VERSION.SDK_INT >= 21) {
                listView.setNestedScrollingEnabled(true);
            }

        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }


    }

    public void tagitemshow(ProfileCollectionList item) {

        taggedCollection = item;
        String arr = collectionLists.get(click).toString();

        taggedItem.setSelectedItem(item.getId());
        taggedItem.setSelectedItemName(item.getName());

        //  taggedItem.setSelectedItemType(collectionLists.get(click).getId());
        taggedItem.setSelectedItemType("collection");
        //    taggedItem.setSelectedItemType(collectionLists.get(click).getDate());


        taggedItem.setSelectedItemXY(item.getDate());

        taggedProducts.setSelectedProductsId(item.getId());
        taggedProducts.setSelectedProducts(item.getId());

        //  taggedItem.setSelectedItemType(collectionLists.get(click).getId());
        taggedProducts.setSelectedProductsXY(item.getDate());
        //    taggedItem.setSelectedItemType(collectionLists.get(click).getDate());
        if (item != null) {
            listView.setSelection(0);
            //addProductListNew.clear();
            isNewCollectionAdded = false;
            checkNewCollectionSelection();
        }


        Syso.info("wwwwwwwwwww >>>>" + taggedItem.getSelectedItemType());
        Syso.info("wwwwwwwwwww >>>>" + taggedItem.getSelectedItemName());
        Syso.info("wwwwwwwwwww >>>>" + taggedItem.getSelectedItem());
        Syso.info("wwwwwwwwwww >>>>" + taggedItem.getSelectedItemXY());


        //    AlertUtils.showToast(getActivity(), "item "+click+" was Clicked " +  taggedItem.getSelectedItemType());
//        AlertUtils.showToast(getActivity(), "selectedItem = " + taggedItem.getSelectedItem() + "\n selectedItemType = " + taggedItem.getSelectedItemType() + "\n selectedItemXY = " + taggedItem.getSelectedItemXY() +
//                "\nselectedItemName = " + taggedItem.getSelectedItemName());
        // makeText(getApplicationContext(), "item was Clicked " +click+" " +show, Toast.LENGTH_LONG).show();
        //  Toast.makeText(mContext, "One Item Selected", Toast.LENGTH_LONG).show();
        //collectionListAdapter.clearSelection();
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    public void hideSubmit() {
        if (collectionListAdapter.isItemTagged() || addProductListNew.size() > 0) {
            HomeActivity.photouploadnext.setVisibility(View.GONE);
            HomeActivity.postUploadWithTag.setVisibility(View.VISIBLE);
        } else {
            HomeActivity.photouploadnext.setVisibility(View.VISIBLE);
            HomeActivity.postUploadWithTag.setVisibility(View.GONE);
        }

    }

    @Override
    public void setClickListener() {
        // tvTagWithoutTag.setOnClickListener(this);
        tvCreateNewCollection.setOnClickListener(this);
        imgAddProduct.setOnClickListener(this);
        newCollectionLayout.setOnClickListener(this);
        productLayout.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionBarButtons();
    }

    @Override
    public void onStart() {
        super.onStart();
        setActionBarButtons();

    }

    private void setActionBarButtons() {
        HomeActivity.menuRightImageView.setVisibility(View.GONE);
        HomeActivity.photouploadnext.setVisibility(View.VISIBLE);
        HomeActivity.camera.setVisibility(View.GONE);
        HomeActivity.homeImageView.setVisibility(View.GONE);
        HomeActivity.uploadphoto.setVisibility(View.VISIBLE);
        HomeActivity.gallery.setVisibility(View.GONE);
        HomeActivity.mstatus.setVisibility(View.GONE);
        HomeActivity.instagram.setVisibility(View.GONE);
        HomeActivity.crossarrow.setVisibility(View.GONE);
        HomeActivity.menuTextCartCount.setVisibility(View.GONE);
        //   HomeActivity.uploadphoto.setGravity(Gravity.CENTER);
        if (isUpdate)
            HomeActivity.uploadphoto.setText("Edit Post");
        else
            HomeActivity.uploadphoto.setText("Upload Post");

    }

    public void updateDescription() {
        try {
            progressbar.setVisibility(View.VISIBLE);
            CommonUtility.hideSoftKeyboard(mContext);
            final EditInspirationApi editPostApi = new EditInspirationApi(new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    CommonRes serverResponse = (CommonRes) object;

                    if (isUpdate && !collectionListAdapter.isItemTagged() || isUpdate && isUploadWithoutTag
                            ) {
                        if (!StringUtils.isEmpty(inspiration.getItem_id()))
                            removeGeneralTag();
                        else {
                            progressbar.setVisibility(View.GONE);
                            if (serverResponse.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {

                                AlertUtils.showToast(mContext, "Post updated successfully");
                                goToTrendingSection();
                            }
                        }


                    } else if (isUpdate && !taggedItem.getSelectedItem().equals(inspiration.getItem_id())) {
                        if (!StringUtils.isEmpty(inspiration.getItem_id()))
                            removeGeneralTag();
                        else
                            addTag();


                    } else {

                        progressbar.setVisibility(View.GONE);
                        if (serverResponse.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {

                            AlertUtils.showToast(mContext, "Post updated successfully");
                            goToTrendingSection();
                        }
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    progressbar.setVisibility(View.GONE);
                    Syso.info("In handleOnFailure>>" + object);
                    if (object != null) {
                        CommonRes response = (CommonRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                    goToTrendingSection();
                }
            });

            editPostApi.editDescription(inspiration.getUser_id(), inspiration.getInspiration_id(), descriptionEditText.getText().toString().trim());
            editPostApi.execute();
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }

    public void removeGeneralTag() {
        try {

            final EditInspirationApi editPostApi = new EditInspirationApi(new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    CommonRes response = (CommonRes) object;
                    if (response.getCode().equals(Constants.WebConstants.SUCCESS_CODE) && isUpdate && !taggedItem.getSelectedItem().equals(inspiration.getItem_id()))
                        addTag();
                    else if (response.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {

                        AlertUtils.showToast(mContext, "Post updated successfully");
                        goToTrendingSection();
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    progressbar.setVisibility(View.GONE);
                    Syso.info("In handleOnFailure>>" + object);
                    if (object != null) {
                        CommonRes response = (CommonRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }

                    goToTrendingSection();
                }
            });

            editPostApi.removeTag(inspiration.getUser_id(), inspiration.getInspiration_id());
            editPostApi.execute();
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }

    public void removeProductTag() {
        try {

            final EditInspirationApi editPostApi = new EditInspirationApi(new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    progressbar.setVisibility(View.GONE);
                    CommonRes response = (CommonRes) object;
                    if (response.getCode().equals(Constants.WebConstants.SUCCESS_CODE) && isUpdate && !taggedItem.getSelectedItem().equals(inspiration.getItem_id()))
                        addTag();
                    else if (response.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {

                        AlertUtils.showToast(mContext, "Post updated successfully");
                        goToTrendingSection();
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    progressbar.setVisibility(View.GONE);
                    Syso.info("In handleOnFailure>>" + object);
                    if (object != null) {
                        CommonRes response = (CommonRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                    goToTrendingSection();
                }
            });

            editPostApi.removeProductsTags(inspiration.getUser_id(), inspiration.getInspiration_id());
            editPostApi.execute();
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }

    public void addTag() {
        try {

            final EditInspirationApi editPostApi = new EditInspirationApi(new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    progressbar.setVisibility(View.GONE);
                    CommonRes response = (CommonRes) object;
                    if (response.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {

                        AlertUtils.showToast(mContext, "Post updated successfully");
                        goToTrendingSection();
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    progressbar.setVisibility(View.GONE);
                    Syso.info("In handleOnFailure>>" + object);
                    if (object != null) {
                        CommonRes response = (CommonRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                    goToTrendingSection();
                }
            });

            editPostApi.addTag(inspiration.getUser_id(), inspiration.getInspiration_id(), taggedItem.getSelectedItemType(), taggedItem.getSelectedItem(), taggedItem.getSelectedItemName(), "0,0");
            editPostApi.execute();
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }

    public void goToTrendingSection() {
        FragmentInspirationSection.isPostUpload = true;
        ((HomeActivity) mContext).loadFragment(new MainFragmentContainer());
    }

    public void getCollectionList() {
        try {
            mProgressBarDialog = new ProgressBarDialog(mContext);
            mProgressBarDialog.show();
            final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {

                @Override
                public void handleOnSuccess(Object object) {
                    Toast.makeText(mContext, "Tap on image to tag a product.", Toast.LENGTH_SHORT).show();

                    Syso.info("In handleOnSuccess>>" + object);
                    MyProfileRes myProfileRes = (MyProfileRes) object;
                    collectionLists = myProfileRes.getCollection_list();


                    collectionListAdapter = new AdapterPostCollectionList(mContext, collectionLists, UserPreference.getInstance().getUserID(), null, taggedItemLocal, cab);
                    listView.setAdapter(collectionListAdapter);

                    if (isUpdate) {
                        for (int i = 0; i < collectionLists.size(); i++) {
                            ProfileCollectionList collection = collectionLists.get(i);
                            if (collection.getId().equalsIgnoreCase(inspiration.getItem_id())) {
                                if (!collectionListAdapter.isPositionChecked(i)) {
                                    collectionLists.remove(i);
                                    collectionLists.add(0, collection);

                                    collectionListAdapter.setNewSelection(0, true);
                                    listView.setSelection(0);
                                    tagitemshow(collection);

                                } else {
                                    collectionListAdapter.removeSelection(i);
                                }
                                // collectionListAdapter.notifyDataSetChanged();
                                hideSubmit();
                                break;
                            }
                        }
                        if (collectionLists.size() > 0) {
                            etCollectionName.setHint("New Post");
                        } else {
                            etCollectionName.setHint("New Collection");
                        }
                        // imageUploadView.setVisibility(View.GONE);
                        new DownloadImage().execute(inspiration.getInspiration_image());
                        //CommonUtility.setImage(mContext, inspiration.getInspiration_image(), imageUploadView, R.drawable.dum_list_item_product);
                    } else if (mProgressBarDialog.isShowing())
                        mProgressBarDialog.dismiss();


                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {

                    if (mProgressBarDialog.isShowing())
                        mProgressBarDialog.dismiss();
                    Syso.info("In handleOnFailure>>" + object);
                    if (object != null) {
                        MyProfileRes response = (MyProfileRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                }
            });

            myProfileApi.getUserProfileDetail(UserPreference.getInstance().getUserID(), UserPreference.getInstance().getUserID());
            myProfileApi.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


//    private class SelectionAdapter extends ArrayAdapter<String> {
//
//        private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
//
//        public SelectionAdapter(Context context, int resource,
//                                int textViewResourceId, String[] objects) {
//            super(context, resource, textViewResourceId, objects);
//        }
//
//        public void setNewSelection(int position, boolean value) {
//            mSelection.put(position, value);
//            notifyDataSetChanged();
//        }
//
//        public boolean isPositionChecked(int position) {
//            Boolean result = mSelection.get(position);
//            return result == null ? false : result;
//        }
//
//        public Set<Integer> getCurrentCheckedPosition() {
//            return mSelection.keySet();
//        }
//
//        public void removeSelection(int position) {
//            mSelection.remove(position);
//            notifyDataSetChanged();
//        }
//
//        public void clearSelection() {
//            mSelection = new HashMap<Integer, Boolean>();
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View v = super.getView(position, convertView, parent);//let the adapter handle setting up the row views
//            v.setBackgroundColor(getResources().getColor(android.R.color.background_light)); //default color
//
//            if (mSelection.get(position) != null) {
//                v.setBackgroundColor(getResources().getColor(R.color.header_background));// this is a selected position so make it red
//            }
//            return v;
//        }
//    }

    private void validateInput() {

        description = descriptionEditText.getText().toString().trim();
        if (bmp == null && imageUrl == null && !isUpdate) {
            AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
        } else if (TextUtils.isEmpty(description)) {
            AlertUtils.showToast(mContext,
                    R.string.alert_no_description_entered);
        } else {
            if (checkInternet()) {
                if (isUpdate) {
                    updateDescription();
                } else {

                    CommonUtility.hideSoftKeyboard(mContext);
                    if (switchInstagram.isChecked() || switchTwitter.isChecked() || switchFacebook.isChecked()||switchPinterest.isChecked()) {
                        callSocialSharing();
                        pintrestsharingimage();
                    }
//                    else if (switchPinterest.isChecked()) {
//                        pintrestsharingimage();
//                    }
                    else if (isNewCollectionAdded) {
                        progressbar.setVisibility(View.VISIBLE);
                        createNewCollection();
                    } else if (taggedProductList.size() > 0) {
                        progressbar.setVisibility(View.VISIBLE);
                        productAddedCount = 0;
                        for (int i = 0; i < taggedProductList.size(); i++) {
                            addProductToTaggedCollection(taggedProductList.get(i));

                        }
                    } else {
                        progressbar.setVisibility(View.VISIBLE);
                        new GetImage().execute();
                    }


                }
                // uploadInspiration(description);
            }
        }
    }

    private void createNewCollection() {
        final AddCollectionApi collectionApi = new AddCollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                AddCollectionApiRes collectionApiRes = (AddCollectionApiRes) object;
                addProductNewCollection(collectionApiRes.getCollection().getId(), collectionApiRes.getCollection().getName());
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    AddCollectionApiRes response = (AddCollectionApiRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        collectionApi.addNewCollection(TextUtils.isEmpty(etCollectionName.getText().toString()) ? "New Post" : etCollectionName.getText().toString());
        collectionApi.execute();
    }

    int addProductNewCollection = 0;

    private void addProductNewCollection(final String id, final String name) {

        final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                addProductNewCollection++;
                Syso.info("In handleOnSuccess>>" + object);
                CollectionApiRes collectionApiRes = (CollectionApiRes) object;
                if (addProductNewCollection == addProductListNew.size()) {

                    taggedItem.setSelectedItem(id);
                    taggedItem.setSelectedItemName(name);

                    //  taggedItem.setSelectedItemType(collectionLists.get(click).getId());
                    taggedItem.setSelectedItemType("collection");
                    //    taggedItem.setSelectedItemType(collectionLists.get(click).getDate());


                    taggedItem.setSelectedItemXY("");

                    taggedProducts.setSelectedProductsId(id);
                    taggedProducts.setSelectedProducts(id);

                    //  taggedItem.setSelectedItemType(collectionLists.get(click).getId());
                    taggedProducts.setSelectedProductsXY("");

                    if (taggedProductList.size() > 0) {
                        productAddedCount = 0;
                        for (int i = 0; i < taggedProductList.size(); i++) {
                            addProductToTaggedCollection(taggedProductList.get(i));

                        }
                    } else
                        new GetImage().execute();
                }
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

        for (int i = 0; i < addProductListNew.size(); i++) {
            Product product = addProductListNew.get(i);
            String from_user_id = TextUtils.isEmpty(product.getFrom_user_id()) ? "" : product.getFrom_user_id();
            String from_collection_id = TextUtils.isEmpty(product.getFrom_collection_id()) ? "" : product.getFrom_collection_id();
            collectionApi.addProductInCollection(UserPreference.getInstance().getUserID(), id, product.getId(), from_user_id, from_collection_id, product.getProductimageurl());
            collectionApi.execute();
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
                //AlertUtils.showToast(mContext, collectionApiRes.getMessage());
                if (productAddedCount == taggedProductList.size())
                    new GetImage().execute();

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

    private void uploadInspiration(byte[] image) {
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        progressbar.setVisibility(View.GONE);
                        Syso.info("In handleOnSuccess>>" + object);
                        InspirationRes inspirationRes = (InspirationRes) object;
                        if (inspirationRes != null) {
                            AlertUtils.showToast(mContext,
                                    inspirationRes.getMessage());
                            goToTrendingSection();
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        progressbar.setVisibility(View.GONE);
                        Syso.info("In handleOnFailure>>" + object);
                        if (object != null) {
                            InspirationRes response = (InspirationRes) object;
                            AlertUtils.showToast(mContext, response.getMessage());
                        } else {
                            AlertUtils.showToast(mContext, R.string.invalid_response);
                        }
                    }
                });
        if (image != null || imageUrl != null) {
            String width = bmp != null ? String.valueOf(bmp.getWidth()) : "";
            String height = bmp != null ? String.valueOf(bmp.getHeight()) : "";
            inspirationSectionApi.uploadImage(UserPreference.getInstance()
                            .getUserID(), image, "yes", description.trim(), taggedItem,
                    taggedProducts, width + ".000000", height + ".000000", imageUrl);
            inspirationSectionApi.execute();


        } else {
            progressbar.setVisibility(View.GONE);
            AlertUtils.showToast(mContext, "No image found");
        }

    }

    public void uploadPost() {

        validateInput();
        HomeActivity.uploadphoto.setVisibility(View.GONE);
        HomeActivity.photouploadnext.setVisibility(View.GONE);

        //  AlertUtils.showToast(mContext, "You have post the value");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCreateNew:
                isNewCollectionAdded = true;
                newCollectionLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.imgAddProduct:
                isAddProduct = true;
                showKeypad();
                setupHeader(true);
                break;

            case R.id.newPostLayout:
                checkNewCollectionSelection();

                break;

            case R.id.productLayout:
                checkNewCollectionSelection();

                break;
        }
    }

    private void checkNewCollectionSelection() {
        if (addProductListNew.size() > 0 && isNewCollectionAdded) {
            newCollectionLayout.setBackgroundColor(getResources().getColor(R.color.header_background));
            productLayout.setBackgroundColor(getResources().getColor(R.color.header_background));
            collectionListAdapter.clearSelection();
        } else {
            newCollectionLayout.setBackgroundColor(getResources().getColor(R.color.postuploadlistcolor));
            productLayout.setBackgroundColor(getResources().getColor(R.color.postuploadlistcolor));
        }
        hideSubmit();
    }

    private void setupHeader(boolean isHeader) {
        if (isHeader) {
            ((HomeActivity) mContext).hideActionBar();
            productSearchLayout.setVisibility(View.VISIBLE);
            listView.setBackgroundColor(getResources().getColor(R.color.transparent));

        } else {
            ((HomeActivity) mContext).showActionBar();
            productSearchLayout.setVisibility(View.GONE);
            listView.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.switchfacebook:

                if (isChecked) {
                    networkId = FacebookSocialNetwork.ID;
//                    switchPinterest.setChecked(true);
//                    switchInstagram.setChecked(true);
//                    switchTwitter.setChecked(true);
                    networkidarray.add(networkId);
                    facebook.setImageResource(R.drawable.fb_selected);
                    facebooktext.setTextColor(Color.parseColor("#34BDC0"));
                }
                else
                {
                    facebook.setImageResource(R.drawable.facebook);
                    facebooktext.setTextColor(Color.parseColor("#979797"));
                }
                break;
            case R.id.switchtwitter:

                if (isChecked) {
                    networkId = TwitterSocialNetwork.ID;
//                    switchPinterest.setChecked(true);
//                    switchInstagram.setChecked(true);
//                    switchFacebook.setChecked(true);
                    networkidarray.add(networkId);
                    twitter.setImageResource(R.drawable.twittergreenicon);
                    twittertext.setTextColor(Color.parseColor("#34BDC0"));
                }
                else
                {
                    twitter.setImageResource(R.drawable.twitter);
                    twittertext.setTextColor(Color.parseColor("#979797"));
                }
                break;
            case R.id.switchInstagram:

                if (isChecked) {
                    networkId = InstagramSocialNetwork.ID;
//                    switchPinterest.setChecked(true);
//                    switchTwitter.setChecked(true);
//                    switchFacebook.setChecked(true);
                    instagram.setImageResource(R.drawable.ig_selected);
                    instagramtext.setTextColor(Color.parseColor("#34BDC0"));
                }
                else
                {
                    instagram.setImageResource(R.drawable.instagram);
                    instagramtext.setTextColor(Color.parseColor("#979797"));
                }

                break;
            case R.id.switchPinterest:

                if (isChecked) {
//                    switchInstagram.setChecked(false);
//                    switchTwitter.setChecked(false);
//                    switchFacebook.setChecked(false);
                    pintrest.setImageResource(R.drawable.pin_selected);
                    pintresttext.setTextColor(Color.parseColor("#34BDC0"));
                }
                else
                {
                    pintrest.setImageResource(R.drawable.pinterest);
                    pintresttext.setTextColor(Color.parseColor("#979797"));
                }
                break;

        }
    }

    private class GetImage extends AsyncTask<Void, Void, byte[]> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected byte[] doInBackground(Void... params) {
            // TODO Auto-generated method stub
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
            uploadInspiration(result);
        }

    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                bmp = resizeBitmapFitXY(500, 500, result);
                imageUploadView.setImageBitmap(bmp);
            }
            if (mProgressBarDialog.isShowing())
                mProgressBarDialog.dismiss();
        }
    }

    public Bitmap resizeBitmapFitXY(int width, int height, Bitmap bitmap) {
        try {
            Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            float originalWidth = bitmap.getWidth(), originalHeight = bitmap.getHeight();
            Canvas canvas = new Canvas(background);
            float scale, xTranslation = 0.0f, yTranslation = 0.0f;
            if (originalWidth > originalHeight) {
                scale = height / originalHeight;
                xTranslation = (width - originalWidth * scale) / 2.0f;
            } else {
                scale = width / originalWidth;
                yTranslation = (height - originalHeight * scale) / 2.0f;
            }
            Matrix transformation = new Matrix();
            transformation.postTranslate(xTranslation, yTranslation);
            transformation.preScale(scale, scale);
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            canvas.drawBitmap(bitmap, transformation, paint);

//            Uri uri = null;
//
//            File file = new File(getActivity().getFilesDir(), "Image"
//                    + new Random().nextInt() + ".jpeg");
//            FileOutputStream out = getActivity().openFileOutput(file.getName(),
//                    Context.MODE_WORLD_READABLE);
//            background.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            out.flush();
//            out.close();
//            //get absolute path
//            String realPath = file.getAbsolutePath();
//            File f = new File(realPath);
//            uri = Uri.fromFile(f);
            return background;
        } catch (Exception ex) {
            return null;
        }
    }

    private void getConfirmation() {
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
                            collectionListAdapter.clearSelection();
                            hideSubmit();
                            uploadPost();
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

    private void search() {
        isLoading = !isLoading;


        if (pageno > 0)
            showFotter();
        else
            mProgressBarDialog.show();

        final SearchApi searchApi = new SearchApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFotter();

                CommonUtility.hideSoftKeyboard(mContext);
                hideProductNotFound();
                Syso.info("In handleOnSuccess>>" + object);
                isLoading = !isLoading;
                SearchRes searchRes = (SearchRes) object;
                List<Product> data2 = searchRes.getData();
                if (data2.size() < 10) {
                    isLoading = true;
                }
                if (data2.size() == 0 && isFirstTime) {
                    showProductNotFound();

                } else if (data2.size() > 0 && isFirstTime) {

                    hideProductNotFound();
                    data = data2;
//                    searchAdapter = new SearchAdapter(mContext, data,true);
//                    searchResultList.setAdapter(searchAdapter);
                } else {
                    data.addAll(data2);
                    // searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFotter();
                CommonUtility.hideSoftKeyboard(mContext);

                Syso.info("In handleOnFailure>>" + object);
                isLoading = !isLoading;
                if (object != null) {
                    SearchRes response = (SearchRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        searchApi.searchProduct(UserPreference.getInstance().getUserID(), searchString.trim(), Integer.toString(pageno));
        searchApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                searchApi.cancel();
            }
        });
    }

    public void showProductNotFound() {
        try {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.itemNotFound);
            layout.setVisibility(View.VISIBLE);
            TextView textView = (TextView) layout.findViewById(R.id.noDataFoundTextView);
            textView.setText("Result not found.");
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    public void hideProductNotFound() {
        try {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.itemNotFound);
            layout.setVisibility(View.GONE);
            TextView textView = (TextView) layout.findViewById(R.id.noDataFoundTextView);
            textView.setText("Result not found.");
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        }
    }

    public void getProductListApi() {
        searchString = multiAutoCompleteTextView.getText().toString();
        if (checkInternet())
            search();
//        searchResultList.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view,
//                                             int scrollState) {
//                // Do nothing
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
////				   System.out.println("1234 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
//                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
////			    	System.out.println("1234 inside if ");
//                    if (checkInternet2()) {
//
//                        if (pageno > 0) {
//                            isFirstTime = false;
//
//                            search();
//                        }
//                        pageno++;
//                    } else {
//                        showReloadFotter();
//                    }
//                }
//            }
//        });
    }

    protected void showReloadFotter() {
        TextView textView = getReloadFotter();
        textView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkInternet()) {
                    // pagenum++;
                    isFirstTime = false;
                    // getAll();
                }
            }
        });
    }

    @Override
    public void onTagsChanged(Collection<String> tags) {
        Log.d("Multi", "Tags changed: ");
        Log.d("Multi", Arrays.toString(tags.toArray()));
        showKeypad();


    }

    @Override
    public void onEditingFinished() {
        Log.d("Multi", "OnEditing finished");
    }

    private void showKeypad() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        multiAutoCompleteTextView.requestFocus();
        imm.showSoftInput(multiAutoCompleteTextView, 0);
    }

    //social sharing fuctionality
    private void pintrestsharingimage() {

        pdkClient = PDKClient.configureInstance(mContext, pinappID);
        Boolean alreadyLoggedIn = pdkClient.onConnect(mContext);
        pdkClient.setDebugMode(true);

        if (alreadyLoggedIn == false) {
            onLogin(null);
        }


        //   onLogin();
    }

    public void onLogin(View view) {

        List scopes = new ArrayList<String>();

        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PRIVATE);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PRIVATE);

        pdkClient.login(mContext, scopes, new PDKCallback() {

            /**
             * It called, when Authentication success
             * @param response
             */
            @Override
            public void onSuccess(PDKResponse response) {
                Log.e(getClass().getName(), response.getData().toString());
                getBoardList(descriptionEditText, imageUrl, imageUrl);

                pintrest.setImageResource(R.drawable.pin_selected);
                pintresttext.setTextColor(Color.parseColor("#34BDC0"));
            }

            /**
             * It called, when Authentication failed
             * @param exception
             */
            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });
    }

    public void pinterestPostSuccess(PDKResponse response)
    {

    }

    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";

    private void getBoardList(final EditText text, final String link, final String imageUrl) {
        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, new PDKCallback() {
            @Override
            public void onSuccess(PDKResponse response) {
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getName());
                Syso.info("1234567890  2>>>>>>" + response.getBoardList().get(0).getUid());
                //  onSavePin(imageUrl, response.getBoardList().get(0).getUid(), text, link);
                if (response.getBoardList() != null && response.getBoardList().size() > 0) {
                    Syso.info("1234567890  2>>>>>> inside condition");
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("name", response.getBoardList().get(0).getName());
                    PinterestBoardDialog boardDialog = new PinterestBoardDialog(mContext, response.getBoardList(), imageUrl, descriptionEditText.getText().toString(), imageUrl);
                    boardDialog.show();
                    //  pdkClient.postPath(imageUrl, params, new PDKCallback());
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


    private void callsocialsharing() {
        //Get Keys for initiate SocialNetworks
        String TWITTER_CONSUMER_KEY = getActivity().getString(R.string.twitter_consumer_key);
        String TWITTER_CONSUMER_SECRET = getActivity().getString(R.string.twitter_consumer_secret);
        String TWITTER_CALLBACK_URL = "oauth://ASNE";

        String INSTAGRAM_CLIENT_ID = "7c501e9a64614ae9a294e3476b7f0532";
        String INSTAGRAM_CLIENT_SECRET = "122dbc40c3174aa6a1900b99e9f67bb4";
        String INSTAGRAM_REDIRECT_URI = "http://www.tychotechnologies.in/";
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
                    // facebook.setText("Show Facebook profile");
//                    facebook.setImageResource(R.drawable.fb_selected);
//                    facebooktext.setTextColor(Color.parseColor("#34BDC0"));
                    break;
                case TwitterSocialNetwork.ID:
//                    twitter.setImageResource(R.drawable.twittergreenicon);
//                    twittertext.setTextColor(Color.parseColor("#34BDC0"));
                    break;
                case InstagramSocialNetwork.ID:
//                    instagram.setImageResource(R.drawable.instagram_selected);
//                    instagramtext.setTextColor(Color.parseColor("#34BDC0"));
                    break;
//                case GooglePlusSocialNetwork.ID:
//                    googleplus.setImageResource(R.drawable.twitterlogin);
//                    break;
            }
        }

        //    mContext.startActivityForResult();
    }

    @Override
    public void onSocialNetworkManagerInitialized() {
        //when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
            initSocialNetwork(socialNetwork);
        }
    }


    private void callSocialSharing() {
        int i;
        //

        for(i=0;i<networkidarray.size();i++) {
            progressbar.setVisibility(View.VISIBLE);
            System.out.print(networkidarray.get(i));

            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkidarray.get(i));
            if (!socialNetwork.isConnected()) {
                if (networkId != 0) {
                    socialNetwork.requestLogin();
                    //    SocialSharingActivity.showProgress("Loading social person");
                } else {
                    Toast.makeText(getActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
                }
            }
            else {
                startProfile(socialNetwork.getID());
            }
        }


    }

    public void requestLoginForSocialNetwork() {
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(FacebookSocialNetwork.ID);
        socialNetwork.requestLogin(this);
    }

    @Override
    public void onLoginSuccess(int networkId) {
        // SocialSharingActivity.hideProgress();
        startProfile(networkId);

    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        // SocialSharingActivity.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }
    int sucesssharecount=0;
    private void startProfile(int networkId) {

//        ProfileFragment profile = ProfileFragment.newInstannce(networkId);
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .addToBackStack("profile")
//                .replace(R.id.container, profile)
//                .commit();
        Bundle post = new Bundle();
        post.putString("imageUrl", imageUrl);
        post.putString("descriptionText", descriptionEditText.getText().toString());
        // mProgressBarDialog = new ProgressBarDialog(mContext);
//        mProgressBarDialog.show();

        //    addFragment(new ProfileFragment(post, networkId));

        socialNetwork = FragmentPostUploadTag.mSocialNetworkManager.getSocialNetwork(networkId);
        //  socialNetwork.setOnRequestCurrentPersonCompleteListener((OnRequestSocialPersonCompleteListener) mContext);
        socialNetwork.requestCurrentPerson();
        socialNetwork.requestPostPhoto(new File(imageUrl), descriptionEditText.getText().toString(), new OnPostingCompleteListener() {
            @Override
            public void onPostSuccessfully(int socialNetworkID) {
                sucesssharecount++;

                if(networkidarray.size()==sucesssharecount) {
//                if (mProgressBarDialog.isShowing())
//                    mProgressBarDialog.dismiss();
//                progressbar.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "You Posted Photo", Toast.LENGTH_SHORT).show();
                    if (isNewCollectionAdded) {
                        createNewCollection();
                    } else if (taggedProductList.size() > 0) {
                        productAddedCount = 0;
                        for (int i = 0; i < taggedProductList.size(); i++) {
                            addProductToTaggedCollection(taggedProductList.get(i));

                        }
                    } else
                        new GetImage().execute();
                }
            }

            @Override
            public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {

                Toast.makeText(getActivity(), "Image post failed, Please try again. ", Toast.LENGTH_LONG).show();
            }
        });
//        if(descriptionEditText==null)
//          Toast.makeText(getActivity(),"Enter the Discrtion to Share Image",Toast.LENGTH_LONG).show();

    }




}
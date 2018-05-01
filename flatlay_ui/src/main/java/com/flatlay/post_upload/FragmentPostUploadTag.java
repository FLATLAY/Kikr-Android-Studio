package com.flatlay.post_upload;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.utility.FontUtility;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.instagram.InstagramSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;
import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.AutocompleteCustomArrayAdapter;
import com.flatlay.chip.AutoSuggestProduct;
import com.flatlay.chip.TagsEditText;
import com.flatlay.dialog.RedirectToPlayStore;
import com.flatlay.fragment.FragmentDiscoverNew;
//import com.flatlay.fragment.FragmentInspirationImageTag;
import com.flatlay.ui.CustomAutoCompleteView;
import com.flatlay.ui.PostUploadCommentsUI;
import com.flatlay.ui.TagView;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.PictureUtils;
//import com.flatlay.utility.PinterestUtility;
import com.flatlaylib.api.AddCollectionApi;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.api.EditInspirationApi;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProfileCollectionList;
import com.flatlaylib.bean.SearchUser;
import com.flatlaylib.bean.TaggedItem;
import com.flatlaylib.bean.TaggedProducts;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.AddCollectionApiRes;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.service.res.CommonRes;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;
import com.makeramen.roundedimageview.RoundedImageView;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;
import com.pinterest.android.pdk.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FragmentPostUploadTag extends BaseFragment implements View.OnClickListener, OnLoginCompleteListener, SocialNetworkManager.OnInitializationCompleteListener {

    private TextView add_collection_text, button1, textOR, button2, new_collection_name, ins_text, fb_text, twitter_text, pinterest_text, post_text;
    private View mainView;
    final static String TAG = "FragmentPostUploadTag";
    private Inspiration inspiration;
    private EditText create_collection_text, description_text;
    private RoundedImageView inspirationImage;
    private LinearLayout backIconLayout;
    private Bitmap bmp;
    private String isImage;
    private String imageUrl;

    public FragmentPostUploadTag(Inspiration inspiration, boolean isUpdate) {
        this.inspiration = inspiration;
        this.isUpdate = isUpdate;
    }

    public FragmentPostUploadTag(Inspiration inspiration) {
        this.inspiration = inspiration;
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
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.backIconLayout: {
                Log.e(TAG, "backIconLayout");
                mContext.onBackPressed();
            }
            break;

            case R.id.post_text: {
                validateInput();
            }
            break;
        }
    }

    @Override
    public void onLoginSuccess(int socialNetworkID) {

    }

    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {

    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        inspirationImage = (RoundedImageView) mainView.findViewById(R.id.inspirationImage);
        create_collection_text = (EditText) mainView.findViewById(R.id.create_collection_text);
        create_collection_text.setTypeface(FontUtility.setMontserratLight(mContext));
        description_text = (EditText) mainView.findViewById(R.id.description_text);
        description_text.setTypeface(FontUtility.setMontserratLight(mContext));
        add_collection_text = (TextView) mainView.findViewById(R.id.add_collection_text);
        add_collection_text.setTypeface(FontUtility.setMontserratLight(mContext));
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
        post_text.setTypeface(FontUtility.setMontserratLight(mContext));
        backIconLayout = (LinearLayout) mainView.findViewById(R.id.backIconLayout);
        description_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description_text.setCursorVisible(true);
            }
        });
    }

    @Override
    public void setData(Bundle bundle) {
        if(!isUpdate){
            inspirationImage.setImageBitmap(bmp);
        }else {
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

    }

    @Override
    public void onSocialNetworkManagerInitialized() {

    }

    private String description;
    private boolean isUpdate=false;

    private void validateInput() {
        description = description_text.getText().toString().trim();
        if (bmp == null && imageUrl == null && !isUpdate) { //If an image has been selected
            AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
        } else if (TextUtils.isEmpty(description)&&isUpdate) {
            AlertUtils.showToast(mContext, // If a description has been added
                    R.string.alert_no_description_entered);
        } else {
            if (checkInternet()) {
                if (isUpdate && !description.equals(inspiration.getDescription())) {
                    updateDescription();
                }else if(!isUpdate){
                    new GetImage().execute();
                }
                else {

                    CommonUtility.hideSoftKeyboard(mContext);

//                    if (isNewCollectionAdded) {
//                        createNewCollection();
//                    } else if (taggedProductList.size() > 0) {
//                        productAddedCount = 0;
//                        for (int i = 0; i < taggedProductList.size(); i++) {
//                            addProductToTaggedCollection(taggedProductList.get(i));
//
//                        }
//                    } else {
                        new GetImage().execute();
                   // }
                }
            }
        }
    }

//    private void uploadInspiration(byte[] image) {
//        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(
//                new ServiceCallback() {
//
//                    @Override
//                    public void handleOnSuccess(Object object) {
//                        Syso.info("In handleOnSuccess>>" + object);
//                        InspirationRes inspirationRes = (InspirationRes) object;
//                        if (inspirationRes != null) {
//                            AlertUtils.showToast(mContext,
//                                    inspirationRes.getMessage());
//                            SHARE_POST_LINK = SHARE_POST_LINK + inspirationRes.getId();
//                            imageServerUri = inspirationRes.getImage_url();
//                            sharePost();
//                            goToTrendingSection();
//                        }
//                    }
//
//                    @Override
//                    public void handleOnFailure(ServiceException exception, Object object) {
//                        Syso.info("In handleOnFailure>>" + object);
//                        if (object != null) {
//                            InspirationRes response = (InspirationRes) object;
//                            AlertUtils.showToast(mContext, response.getMessage());
//                        } else {
//                            AlertUtils.showToast(mContext, R.string.invalid_response);
//                        }
//                    }
//                });
//        if (image != null || imageUrl != null) {
//            String width = bmp != null ? String.valueOf(bmp.getWidth()) : "";
//            String height = bmp != null ? String.valueOf(bmp.getHeight()) : "";
//
//            inspirationSectionApi.uploadImage(UserPreference.getInstance()
//                            .getUserID(), image, "yes", description.trim(), taggedItem,
//                    taggedProducts, width + ".000000", height + ".000000", imageUrl);
//            inspirationSectionApi.execute();
//
//
//        } else {
//            AlertUtils.showToast(mContext, "No image found");
//        }
//
//    }

    public void updateDescription() {
        try {
//            CommonUtility.hideSoftKeyboard(mContext);
            final EditInspirationApi editPostApi = new EditInspirationApi(new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    CommonRes serverResponse = (CommonRes) object;
//
//                    if (isUpdate && !collectionListAdapter.isItemTagged() || isUpdate && isUploadWithoutTag
//                            ) {
//                        if (!StringUtils.isEmpty(inspiration.getItem_id()))
//                            removeGeneralTag();
//                        else {
//                            progressbar.setVisibility(View.GONE);
//                            if (serverResponse.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {
//
//                                AlertUtils.showToast(mContext, "Post updated successfully");
//                                goToTrendingSection();
//                            }
//                        }
//
//
//                    } else if (isUpdate && !taggedItem.getSelectedItem().equals(inspiration.getItem_id())) {
//                        if (!StringUtils.isEmpty(inspiration.getItem_id()))
//                            removeGeneralTag();
//                        else
//                            addTag();
//
//
//                    } else {

                    if (serverResponse.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {

                        AlertUtils.showToast(mContext, "Post updated successfully");
                        inspiration.setDescription(description_text.getText().toString().trim());
                        description_text.setCursorVisible(false);

//                            goToTrendingSection();
                        // }
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
//                    goToTrendingSection();
                }
            });

            editPostApi.editDescription(inspiration.getUser_id(), inspiration.getInspiration_id(), description_text.getText().toString().trim());
            editPostApi.execute();
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }

    static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/flatlay/";
    String imageServerUri;

//    private void sharePost() {
//
//        Log.w("FPUTag","sharePost()");
//
//        if(switchInstagram.isChecked()) {
//            postToInstagram();
//        }
//
//        if(switchFacebook.isChecked() || switchTwitter.isChecked()){
//            for (int i = 0; i < networkidarray.size(); i++) {
//                startProfile(networkidarray.get(i));
//            }
//        }
//
//        /*
//        if (switchInstagram.isChecked() || switchTwitter.isChecked() || switchFacebook.isChecked()) {
//            for (int i = 0; i < networkidarray.size(); i++) {
//                startProfile(networkidarray.get(i));
//            }
//        }
//        */
//
//        if (switchPinterest.isChecked()) {
//            pintrestsharingimage();
//            onSavePin(mContext, imageServerUri, UserPreference.getInstance().getmPinterestBoardId(), descriptionEditText.getText().toString() + " " + SHARE_POST_LINK, imageServerUri);
//        }
//    }

    private TaggedItem taggedItem = new TaggedItem();
    private TaggedProducts taggedProducts = new TaggedProducts();


    private void uploadInspiration(byte[] image) {
        Log.w("FragmentPUTag","uploadInspiration()");
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
//                            sharePost();
//                            goToTrendingSection();
                            ((HomeActivity)mContext).addFragment(new FragmentInspirationSection());
                            ((HomeActivity) mContext).mFragmentStack.clear();
                            ((HomeActivity) mContext).addFragment(new FragmentDiscoverNew());
                            ((HomeActivity) mContext).onBackPressed();
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        if (object != null) {
                            InspirationRes response = (InspirationRes) object;
                            AlertUtils.showToast(mContext, response.getMessage());
                        } else {
                            AlertUtils.showToast(mContext, R.string.invalid_response);
                        }
                    }
                });
        if (image != null || imageUrl != null) {
            Log.w("FragmentPUTag","Image present");
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
    byte[] byteArray;


    private class GetImage extends AsyncTask<Void, Void, byte[]> {

        @Override
        protected void onPreExecute() {
            Log.w("FragmentPUTag","GetImage() onPreExecute()");
//            imageUploadView.setImageBitmap(bmp);
            super.onPreExecute();
        }

        @Override
        protected byte[] doInBackground(Void... params) {

            // TODO Auto-generated method stub
            Log.w("FragmentPUTag","GetImage() doInBackground()");
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
            Log.w("FragmentPUTag","Going in uploadInspiration() 1");
            uploadInspiration(result);
        }
    }
}
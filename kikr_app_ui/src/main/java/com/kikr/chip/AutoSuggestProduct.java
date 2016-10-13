package com.kikr.chip;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.flatlay.R;
import com.kikr.adapter.SearchAdapter;
import com.kikr.bubble.ChipBubbleText;
import com.kikr.model.Item;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.SearchAllApi;
import com.kikrlib.api.SearchApi;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.SearchStoreBrandUserRes;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.SearchRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anshumaan on 6/5/2016.
 */
public class AutoSuggestProduct {
    ProgressBarDialog mProgressBarDialog;
    Context mContext;
    ArrayList<Item> itemArrayList;
    String[] autoSuggestArray = null;
    String[] previousSearchedText = new String[15];
    TagsEditText tagsEditText;
    int j = 0;
    List<Product> data2;
    SearchApi searchApi;
    ProgressBar progressBar;

    public boolean isStringExist(String str) {
        for (String s : previousSearchedText) {
            if (s != null)
                if (s.equalsIgnoreCase(str)) {
                    return true;

                }

        }
        return false;
    }

    public void clearPreviousStrings() {
        previousSearchedText = new String[15];
        j = 0;
    }

    public AutoSuggestProduct(Context context) {
        this.mContext = context;
    }


    public List<Product> getProducts() {
        return data2;
    }


    public void search(final String searchString, final TagsEditText tagsEditText, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        this.progressBar = progressBar;

        searchApi = new SearchApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {


                previousSearchedText[j++] = searchString;
                Syso.info("In handleOnSuccess>>" + object);

                SearchRes searchRes = (SearchRes) object;
                data2 = searchRes.getData();

                if (data2.size() > 0) {
                    tagsEditText.setAdapter(new ProductAutoCompleteAdapter(mContext, R.layout.product_auto_suggest_items, data2));
                    tagsEditText.showDropDown();
                }
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);


            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);

                Syso.info("In handleOnFailure>>" + object);

                if (object != null) {
                    SearchRes response = (SearchRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        searchApi.searchProduct(UserPreference.getInstance().getUserID(), searchString, Integer.toString(0));
        searchApi.execute();


    }

    public void cancelExecution() {
        if (progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.GONE);
        searchApi.cancel();

    }

}


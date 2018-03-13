package com.flatlay.chip;

import android.content.Context;
import android.view.View;

import com.flatlay.R;
import com.flatlay.model.Item;
import com.flatlaylib.api.SearchApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.SearchRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;


public class AutoSuggestProduct {
    Context mContext;
    ArrayList<Item> itemArrayList;
    String[] autoSuggestArray = null;
    String[] previousSearchedText = new String[15];
    TagsEditText tagsEditText;
    int j = 0;
    List<Product> data2;
    SearchApi searchApi;

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


    public void search(final String searchString, final TagsEditText tagsEditText) {

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
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
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
        searchApi.cancel();

    }

}


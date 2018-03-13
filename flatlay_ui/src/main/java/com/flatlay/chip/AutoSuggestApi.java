package com.flatlay.chip;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import com.flatlay.model.Item;
import com.flatlaylib.api.SearchAllApi;
import com.flatlaylib.bean.SearchStoreBrandUserRes;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;

/**
 * Created by anshumaan on 6/5/2016.
 */
public class AutoSuggestApi {
    Context mContext;
    ArrayList<Item> itemArrayList;
    String[] autoSuggestArray = null;
    String[] previousSearchedText = new String[15];
    TagsEditText tagsEditText;
    int i = 0;

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
        i = 0;
    }

    public AutoSuggestApi(Context context) {
        this.mContext = context;
    }


    public String[] getAutoSuggestArray() {
        return autoSuggestArray;
    }

    private void showKeypad() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        tagsEditText.requestFocus();
        imm.showSoftInput(tagsEditText, 0);
    }

    public void getAll(String searchString, final TagsEditText tagsEditText) {
        this.tagsEditText = tagsEditText;
        Syso.info("Auto suggest api calling.", searchString);
        previousSearchedText[i++] = searchString;
        //mProgressBarDialog = new ProgressBarDialog(mContext);
        //mProgressBarDialog.show();
        final SearchAllApi searchAllApi = new SearchAllApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {

                Syso.info("In handleOnSuccess>>" + object);
                int j = 0;
                itemArrayList = new ArrayList<>();
                SearchStoreBrandUserRes searchStoreBrandUserRes = (SearchStoreBrandUserRes) object;
                autoSuggestArray = new String[searchStoreBrandUserRes.getBrands().size() + searchStoreBrandUserRes.getStores().size() + searchStoreBrandUserRes.getUsers().size()];
                for (int i = 0; i < searchStoreBrandUserRes.getBrands().size(); i++) {

                    autoSuggestArray[j++] = searchStoreBrandUserRes.getBrands().get(i).getName();

                }
                for (int i = 0; i < searchStoreBrandUserRes.getStores().size(); i++) {

                    autoSuggestArray[j++] = searchStoreBrandUserRes.getStores().get(i).getName();

                }
                for (int i = 0; i < searchStoreBrandUserRes.getUsers().size(); i++) {

                    autoSuggestArray[j++] = searchStoreBrandUserRes.getUsers().get(i).getName();

                }

                if (autoSuggestArray.length > 0) {
                    tagsEditText.setAdapter(new ArrayAdapter<>(mContext,
                            android.R.layout.simple_dropdown_item_1line, autoSuggestArray));
                    tagsEditText.showDropDown();
                }
                else {

                    showKeypad();

                }


//                if (mProgressBarDialog.isShowing())
//                    mProgressBarDialog.dismiss();
//



            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//                if (mProgressBarDialog.isShowing())
//                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);



                if (object != null) {
//                    InterestSectionRes response = (InterestSectionRes) object;
//                    AlertUtils.showToast(CustomizeFeedActivity.this, response.getMessage());
                } else {
                    // AlertUtils.showToast(CustomizeFeedActivity.this, R.string.invalid_response);
                }
            }
        });
        searchAllApi.searchStoreBrandPeople(UserPreference.getInstance().getUserID(), searchString, Integer.toString(0));
        searchAllApi.execute();


    }
}

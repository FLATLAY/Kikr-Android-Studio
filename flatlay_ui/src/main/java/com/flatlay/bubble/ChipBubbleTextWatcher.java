package com.flatlay.bubble;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;

import com.flatlay.R;
import com.flatlay.model.Item;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants;
import com.flatlaylib.api.SearchAllApi;
import com.flatlaylib.bean.SearchStoreBrandUserRes;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;

public class ChipBubbleTextWatcher implements TextWatcher {

    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private Context context;
    private ChipPropery chipPropery;
    public static boolean isBackspacePressed;
    private String original = "";
    private int error = 0;
    ArrayList<Item> itemArrayList;
    String[] autoSuggestArray = null;
    private ProgressBarDialog mProgressBarDialog;
    String checkApiCall = "";

    public ChipBubbleTextWatcher(Context context, MultiAutoCompleteTextView multiAutoCompleteTextView, ChipPropery chipPropery) {
        super();
        Log.w("Activity","ChipBubbleTextWatcher");
        this.multiAutoCompleteTextView = multiAutoCompleteTextView;
        this.context = context;
        this.chipPropery = chipPropery;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Syso.info(s.toString() + " length :" + s.length());
        String[] temparr = s.toString().split(" ");
        String temp = s.toString().replaceAll("\\s+$", " ");
        int backspaceFlag = 0;
        if ((original.length() - 1) == temp.length()) {
            backspaceFlag = 1;
        }

        if (temparr.length == 0) {
            if (s.length() == 3 && !checkApiCall.contains(s)) {
                getAll(s.toString().trim());
                checkApiCall = checkApiCall + s.toString().trim() + " ";
            }
        } else {
            if (temparr[temparr.length - 1].length() == 3 && !checkApiCall.contains(temparr[temparr.length - 1].trim())) {
                getAll(temparr[temparr.length - 1].trim());
                checkApiCall = checkApiCall + temparr[temparr.length - 1].trim() + " ";
            }
        }
        // TODO Auto-generated method stub

        if (original.equals(temp)) {
            error = 1;
        } else {
            original = temp;
            error = 0;
        }
        if (temp.length() > 0) {
            if (temp.charAt(temp.length() - 1) == ' ') {
                if (error == 0) {
                    CharSequence chiptxt = s.toString().replaceAll("\\s+$", " ");
                    GeneralFunctions.setChips(context, chiptxt, multiAutoCompleteTextView, chipPropery);
                    multiAutoCompleteTextView.setSelection(multiAutoCompleteTextView.length());
                }
            } else {

                if (backspaceFlag == 1) {
                    int lastComma = original.lastIndexOf(" ");
                    String chipPart = original.substring(0, lastComma + 1);
                    String nonChipPart = original.substring(lastComma + 1, original.length());
                    multiAutoCompleteTextView.setText(chipPart);
                    multiAutoCompleteTextView.append(nonChipPart);
                    multiAutoCompleteTextView.setSelection(multiAutoCompleteTextView.length());
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

        int flag = 0;
        for (int i = s.length(); i > 0; i--) {

            if (s.subSequence(i - 1, i).toString().equals("\n")) {
                s.replace(i - 1, i, " ");
                flag = 1;
            }

            if (i == 1) {
                if (s.subSequence(i - 1, i).toString().equals(" ")) {
                    s.replace(i - 1, i, " ");
                    flag = 1;
                }
            }
            if ((i > 1) && (i < (s.length() + 1))) {
                if ((s.subSequence(i - 1, i).toString().equals(" ")) && (s.subSequence(i - 2, i - 1).toString().equals(" "))) {
                    s.replace(i - 1, i, " ");
                    flag = 1;
                }
            }

        }
        if (flag == 1) {
            CharSequence chiptxt = s.toString().replaceAll("\\s+$", "");
            ;
            if (chiptxt.length() > 0) {
                GeneralFunctions.setChips(context, chiptxt, multiAutoCompleteTextView, chipPropery);
            } else {
                multiAutoCompleteTextView.setText("");
            }
            multiAutoCompleteTextView.setSelection(multiAutoCompleteTextView.length());
            flag = 0;
        }

    }


    public void getAll(String searchString) {
        mProgressBarDialog = new ProgressBarDialog(context);
        mProgressBarDialog.show();
        final SearchAllApi searchAllApi = new SearchAllApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {

                Syso.info("In handleOnSuccess>>" + object);
                int j = 0;
                if (AppConstants.isBubbleApiCall)
                    AppConstants.isBubbleApiCall = false;
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
                ChipBubbleText chipBubbleText = new ChipBubbleText(context, multiAutoCompleteTextView, autoSuggestArray, 0);
                ChipBubbleText.setAdapterValue(autoSuggestArray);
                chipBubbleText.setChipColor("#07948c");
                chipBubbleText.setChipTextColor(R.color.app_text_color);
                chipBubbleText.setChipTextSize(18);
                multiAutoCompleteTextView.showDropDown();
                multiAutoCompleteTextView.requestFocus();
                chipBubbleText.initialize();
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();


            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
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

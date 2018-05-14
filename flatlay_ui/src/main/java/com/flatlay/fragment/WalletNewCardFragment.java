package com.flatlay.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.dialog.ComingSoonDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.Luhn;
import com.flatlaylib.api.CardInfoApi;
import com.flatlaylib.api.OAuthWebService;
import com.flatlaylib.bean.Card;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CardInfoRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;

import org.json.JSONObject;

/**
 * Created by RachelDi on 4/30/18.
 */

public class WalletNewCardFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private RelativeLayout overflow_layout10, overflow_layout11, add_paypal_layout, credit_debit_layout;
    private TextView credit_debit_text, or_text, paypal_text, title_credit_debit_text, name_card_text,
            card_num_text, ccv_text, expiration_text, default_text, save_card_button, cancel_text;
    private EditText name_card_text2, card_num_text2, ccv_text2, expiration_text2;
    private ImageView check_icon;
    private boolean isValidPayment2 = false;
    private Card newCard2 = new Card();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.wallet_new_card_layout, null);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.credit_debit_layout:
                overflow_layout11.setVisibility(View.VISIBLE);
                overflow_layout10.setVisibility(View.GONE);
                break;
            case R.id.add_paypal_layout:
                ComingSoonDialog comingSoonDialog = new ComingSoonDialog(mContext);
                comingSoonDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                comingSoonDialog.show();
                break;
            case R.id.check_icon:

                break;
            case R.id.save_card_button:
                if (isValidPayment2) {
                    getAuthTocken();
                    addNewCard(newCard2.getCard_number(), newCard2.getName_on_card(), newCard2.getExpirationMonth() + "/" + newCard2.getExpirationYear(), newCard2.getCvv(), newCard2.getCardtype());
                    overflow_layout11.setVisibility(View.GONE);
                    overflow_layout10.setVisibility(View.VISIBLE);
                } else
                    AlertUtils.showToast(mContext, "Please enter required information.");
                break;
            case R.id.cancel_text:
                overflow_layout11.setVisibility(View.GONE);
                overflow_layout10.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        overflow_layout10 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout10);
        overflow_layout11 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout11);
        credit_debit_layout = (RelativeLayout) mainView.findViewById(R.id.credit_debit_layout);
        credit_debit_text = (TextView) mainView.findViewById(R.id.credit_debit_text);
        credit_debit_text.setTypeface(FontUtility.setMontserratLight(mContext));
        or_text = (TextView) mainView.findViewById(R.id.or_text);
        or_text.setTypeface(FontUtility.setMontserratLight(mContext));
        add_paypal_layout = (RelativeLayout) mainView.findViewById(R.id.add_paypal_layout);
        paypal_text = (TextView) mainView.findViewById(R.id.paypal_text);
        paypal_text.setTypeface(FontUtility.setMontserratLight(mContext));
        title_credit_debit_text = (TextView) mainView.findViewById(R.id.title_credit_debit_text);
        title_credit_debit_text.setTypeface(FontUtility.setMontserratLight(mContext));
        name_card_text = (TextView) mainView.findViewById(R.id.name_card_text);
        name_card_text.setTypeface(FontUtility.setMontserratLight(mContext));
        name_card_text2 = (EditText) mainView.findViewById(R.id.name_card_text2);
        name_card_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        MyTextWatcher3 watcher3 = new MyTextWatcher3();
        name_card_text2.addTextChangedListener(watcher3);
        card_num_text = (TextView) mainView.findViewById(R.id.card_num_text);
        card_num_text.setTypeface(FontUtility.setMontserratLight(mContext));
        ccv_text = (TextView) mainView.findViewById(R.id.ccv_text);
        ccv_text.setTypeface(FontUtility.setMontserratLight(mContext));
        card_num_text2 = (EditText) mainView.findViewById(R.id.card_num_text2);
        card_num_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        ccv_text2 = (EditText) mainView.findViewById(R.id.ccv_text2);
        ccv_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        ccv_text2.addTextChangedListener(watcher3);
        card_num_text2.addTextChangedListener(watcher3);
        expiration_text = (TextView) mainView.findViewById(R.id.expiration_text);
        expiration_text.setTypeface(FontUtility.setMontserratLight(mContext));
        expiration_text2 = (EditText) mainView.findViewById(R.id.expiration_text2);
        expiration_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        expiration_text2.addTextChangedListener(watcher3);
        check_icon = (ImageView) mainView.findViewById(R.id.check_icon);
        default_text = (TextView) mainView.findViewById(R.id.default_text);
        default_text.setTypeface(FontUtility.setMontserratLight(mContext));
        save_card_button = (TextView) mainView.findViewById(R.id.save_card_button);
        save_card_button.setTypeface(FontUtility.setMontserratLight(mContext));
        cancel_text = (TextView) mainView.findViewById(R.id.cancel_text);
        cancel_text.setTypeface(FontUtility.setMontserratLight(mContext));
        cancel_text.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        credit_debit_layout.setOnClickListener(this);
        add_paypal_layout.setOnClickListener(this);
        check_icon.setOnClickListener(this);
        save_card_button.setOnClickListener(this);
        cancel_text.setOnClickListener(this);

    }

    private void saveCard() {
        isValidPayment2 = true;
        if (card_num_text2.getText().toString().trim().length() > 0) {
            card_num_text2.setBackgroundResource(R.drawable.topsearch_noborder);
            newCard2.setCard_number(card_num_text2.getText().toString().trim());
            newCard2.setCardtype(Luhn.getCardTypeinString(card_num_text2.getText().toString().trim()));
        } else {
            isValidPayment2 = false;
            card_num_text2.setBackgroundResource(R.drawable.topsearch_red_border);
        }
        if (name_card_text2.getText().toString().trim().length() <= 0) {
            isValidPayment2 = false;
            name_card_text2.setBackgroundResource(R.drawable.topsearch_red_border);
        } else {
            name_card_text2.setBackgroundResource(R.drawable.topsearch_noborder);
            newCard2.setName_on_card(name_card_text2.getText().toString().trim());
        }
        String[] arr = expiration_text2.getText().toString().trim().split("/");
        if (expiration_text2.getText().toString().trim().length() <= 0 || arr.length < 2) {
            isValidPayment2 = false;
            expiration_text2.setBackgroundResource(R.drawable.topsearch_red_border);
        } else {
            expiration_text2.setBackgroundResource(R.drawable.topsearch_noborder);
            newCard2.setExpirationMonth(arr[0]);
            if (arr[1].length() == 2)
                newCard2.setExpirationYear("20" + arr[1]);
            else
                newCard2.setExpirationYear(arr[1]);
        }
        if (ccv_text2.getText().toString().trim().length() <= 0) {
            isValidPayment2 = false;
            ccv_text2.setBackgroundResource(R.drawable.topsearch_red_border);
        } else {
            ccv_text2.setBackgroundResource(R.drawable.topsearch_noborder);
            newCard2.setCvv(ccv_text2.getText().toString().trim());
        }
    }

    private class MyTextWatcher3 implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            saveCard();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public void getAuthTocken() {
        boolean condition1 = UserPreference.getInstance().getAuthTimeStamp() == 0;
        boolean condition2 = System.currentTimeMillis() > (UserPreference.getInstance().getAuthTimeStamp() + UserPreference.getInstance().getAuthExpireTime());
        if (condition1 || condition2) {
            if (checkInternet()) {
                OAuthWebService authWebService = new OAuthWebService(Constants.WebConstants.PAYPAL_AUTH_URL, new ServiceCallback() {
                    @Override
                    public void handleOnSuccess(Object object) {
                        try {
                            JSONObject jsonObj = new JSONObject(object.toString());
                            String access_token = jsonObj.getString("access_token");
                            int expires_in = jsonObj.getInt("expires_in");
                            UserPreference.getInstance().setExpireTime(expires_in * 1000);
                            UserPreference.getInstance().setPaypalAccessToken(access_token);
                            UserPreference.getInstance().setTimeStamp(System.currentTimeMillis());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception, Object object) {
                        if (object != null)
                            AlertUtils.showToast(mContext, object.toString());
                    }
                });
                authWebService.execute();
            }
        }
    }

    private void addNewCard(String cardNumber, String cardHolderName, String expiryDate, String cvv, String cardtype) {
        final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    AlertUtils.showToast(mContext, "Card added");
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    CardInfoRes cardInfoRes = (CardInfoRes) object;
                    AlertUtils.showToast(mContext, cardInfoRes.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        cardInfoApi.addCard(UserPreference.getInstance().getUserID(), CommonUtility.EncryptCreditCard(cardNumber), cardHolderName, expiryDate, cvv, cardtype);
        cardInfoApi.execute();
    }

}

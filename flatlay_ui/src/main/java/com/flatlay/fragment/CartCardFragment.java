package com.flatlay.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.CardGridAdapter;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.Luhn;
import com.flatlaylib.api.CardInfoApi;
import com.flatlaylib.api.OAuthWebService;
import com.flatlaylib.bean.Address;
import com.flatlaylib.bean.BillingAddress;
import com.flatlaylib.bean.Card;
import com.flatlaylib.bean.CartProduct;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CardInfoRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RachelDi on 4/29/18.
 */

public class CartCardFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private TextView shipping_text7, shipping_text6, confirm_text2, payment_type_text, payment_detail_text,
            enter_card_num, enter_name_on_card2, enter_expire_text2, billing_text, same_as_text, contact_text3,
            enter_country_text2, enter_apt_text2, enter_city_text2, enter_state_text2, add_card_done_button,
            next_button2;
    private GridView card_gridview;
    private EditText ccv_text3, billing_first_name, billing_last_name, enter_address_text2, enter_zip_text2;
    private ImageView same_as_icon;
    private LinearLayout billing_layout;
    private boolean isValidPayment = false, isValidBillingAddress = false;
    private Card newCard = new Card();
    private BillingAddress billlingAddress = new BillingAddress();
    private CartProduct cardAndShippingDetail;
    private Address shippingAddress;
    private String billAddressLine1 = "", billApartment = "", apartment = "", addressLine1 = "";
    private List<Product> my_productLists;
    private double total_price;


    public CartCardFragment(Address shippingAddress, List<Product> my_productLists, CartProduct cardAndShippingDetail, double total_price) {
        this.my_productLists = my_productLists;
        this.shippingAddress = shippingAddress;
        this.cardAndShippingDetail = cardAndShippingDetail;
        this.total_price = total_price;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.cart_card_layout, container,false);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.same_as_icon:
                if (billing_layout.getVisibility() == View.GONE) {
                    same_as_icon.setImageResource(R.drawable.box_default);
                    billing_layout.setVisibility(View.VISIBLE);
                } else {
                    same_as_icon.setImageResource(R.drawable.checkmark_white);
                    billing_layout.setVisibility(View.GONE);
                }
                break;
            case R.id.add_card_done_button:
                if (isValidPayment) {
                    getAuthTocken();
                    add_card_done_button.setVisibility(View.GONE);
                    validateCard(newCard, billlingAddress);
                } else
                    AlertUtils.showToast(mContext, "Please enter required information.");
                break;
            case R.id.next_button2:
                if (isValidBillingAddress && isValidPayment) {
                    ((HomeActivity) mContext).myAddFragment(new CartConfirmFragment(my_productLists, cardAndShippingDetail, shippingAddress, total_price));
                } else {
                    AlertUtils.showToast(mContext, "Please enter required information.");
                }
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        shipping_text7 = (TextView) mainView.findViewById(R.id.shipping_text7);
        shipping_text7.setTypeface(FontUtility.setMontserratLight(mContext));
        shipping_text6 = (TextView) mainView.findViewById(R.id.shipping_text6);
        shipping_text6.setTypeface(FontUtility.setMontserratLight(mContext));
        confirm_text2 = (TextView) mainView.findViewById(R.id.confirm_text2);
        confirm_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        payment_type_text = (TextView) mainView.findViewById(R.id.payment_type_text);
        payment_type_text.setTypeface(FontUtility.setMontserratLight(mContext));
        card_gridview = (GridView) mainView.findViewById(R.id.card_gridview);
        payment_detail_text = (TextView) mainView.findViewById(R.id.payment_detail_text);
        payment_detail_text.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_card_num = (EditText) mainView.findViewById(R.id.enter_card_num);
        enter_card_num.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_name_on_card2 = (EditText) mainView.findViewById(R.id.enter_name_on_card2);
        enter_name_on_card2.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_expire_text2 = (EditText) mainView.findViewById(R.id.enter_expire_text2);
        enter_expire_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        MyTextWatcher2 watcher2 = new MyTextWatcher2();
        enter_card_num.addTextChangedListener(watcher2);
        enter_name_on_card2.addTextChangedListener(watcher2);
        enter_expire_text2.addTextChangedListener(watcher2);
        ccv_text3 = (EditText) mainView.findViewById(R.id.ccv_text3);
        ccv_text3.setTypeface(FontUtility.setMontserratLight(mContext));
        ccv_text3.addTextChangedListener(watcher2);
        billing_text = (TextView) mainView.findViewById(R.id.billing_text);
        billing_text.setTypeface(FontUtility.setMontserratLight(mContext));
        same_as_icon = (ImageView) mainView.findViewById(R.id.same_as_icon);
        same_as_text = (TextView) mainView.findViewById(R.id.same_as_text);
        same_as_text.setTypeface(FontUtility.setMontserratLight(mContext));
        billing_layout = (LinearLayout) mainView.findViewById(R.id.billing_layout);
        contact_text3 = (TextView) mainView.findViewById(R.id.contact_text3);
        contact_text3.setTypeface(FontUtility.setMontserratLight(mContext));
        billing_first_name = (EditText) mainView.findViewById(R.id.billing_first_name);
        billing_first_name.setTypeface(FontUtility.setMontserratLight(mContext));
        billing_first_name.addTextChangedListener(watcher2);
        billing_last_name = (EditText) mainView.findViewById(R.id.billing_last_name);
        billing_last_name.setTypeface(FontUtility.setMontserratLight(mContext));
        billing_last_name.addTextChangedListener(watcher2);
        enter_country_text2 = (EditText) mainView.findViewById(R.id.enter_country_text2);
        enter_country_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_country_text2.addTextChangedListener(watcher2);
        enter_address_text2 = (EditText) mainView.findViewById(R.id.enter_address_text2);
        enter_address_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_address_text2.addTextChangedListener(watcher2);
        enter_apt_text2 = (EditText) mainView.findViewById(R.id.enter_apt_text2);
        enter_apt_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_apt_text2.addTextChangedListener(watcher2);
        enter_city_text2 = (EditText) mainView.findViewById(R.id.enter_city_text2);
        enter_city_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_city_text2.addTextChangedListener(watcher2);
        enter_state_text2 = (EditText) mainView.findViewById(R.id.enter_state_text2);
        enter_state_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_state_text2.addTextChangedListener(watcher2);
        enter_zip_text2 = (EditText) mainView.findViewById(R.id.enter_zip_text2);
        enter_zip_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_zip_text2.addTextChangedListener(watcher2);
        add_card_done_button = (TextView) mainView.findViewById(R.id.add_card_done_button);
        add_card_done_button.setTypeface(FontUtility.setMontserratLight(mContext));
        next_button2 = (TextView) mainView.findViewById(R.id.next_button2);
        next_button2.setTypeface(FontUtility.setMontserratLight(mContext));
        getCardList();
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        same_as_icon.setOnClickListener(this);
        add_card_done_button.setOnClickListener(this);
        next_button2.setOnClickListener(this);


    }

    public void getCardList() {
        final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    CardInfoRes cardInfoRes = (CardInfoRes) object;
                    List<Card> temp = new ArrayList<>();
                    List<Card> temp2 = new ArrayList<>();
                    temp = cardInfoRes.getData();
                    temp2 = temp;
                    temp2.add(new Card("flatlay", "flatlay", "flatlay", "", "", "", "", ""));
                    temp2.add(new Card("Add", "Add", "Add", "", "", "", "", ""));
                    final List<Card> temp3 = temp2;
                    card_gridview.setAdapter(new CardGridAdapter(mContext, temp3, new CardGridAdapter.CardGridListener() {
                        @Override
                        public void onClickButton(int position) {
                            String card_num = temp3.get(position).getCard_number();
                            String card_name = temp3.get(position).getName_on_card();
                            String exp_month = "";
                            String exp_year = "";
                            cardAndShippingDetail.setCard_number(CommonUtility.DecryptCreditCard(card_num));
                            cardAndShippingDetail.setCard_type(temp3.get(position).getCardtype());
                            cardAndShippingDetail.setCard_name(card_name);
                            if (card_name.equals("flatlay")) {
                                ccv_text3.setVisibility(View.GONE);
                                enter_expire_text2.setVisibility(View.GONE);
                                enter_card_num.setVisibility(View.GONE);
                                add_card_done_button.setVisibility(View.GONE);
                                enter_name_on_card2.setText(card_name);
                                isValidPayment = true;
                            } else if (card_name.equals("Add")) {
                                ccv_text3.setVisibility(View.VISIBLE);
                                ccv_text3.setText("");
                                enter_expire_text2.setVisibility(View.VISIBLE);
                                enter_expire_text2.setText("");
                                enter_name_on_card2.setVisibility(View.VISIBLE);
                                enter_name_on_card2.setText("");
                                enter_card_num.setVisibility(View.VISIBLE);
                                enter_card_num.setText("");
                                add_card_done_button.setVisibility(View.VISIBLE);
                            } else if (card_name.equals("paypal")) {
                                ccv_text3.setVisibility(View.GONE);
                                enter_expire_text2.setVisibility(View.GONE);
                                enter_card_num.setVisibility(View.GONE);
                                add_card_done_button.setVisibility(View.GONE);
                                enter_name_on_card2.setText(card_name);
                                isValidPayment = true;
                            } else {
                                add_card_done_button.setVisibility(View.GONE);
                                ccv_text3.setVisibility(View.VISIBLE);
                                exp_month = temp3.get(position).getExpiration_date().split("/")[0];
                                exp_year = temp3.get(position).getExpiration_date().split("/")[1];
                                enter_expire_text2.setVisibility(View.VISIBLE);
                                enter_card_num.setVisibility(View.VISIBLE);
                                enter_card_num.setText(card_num);
                                enter_expire_text2.setText(exp_month + " / " + exp_year);
                                enter_card_num.setText(CommonUtility.DecryptCreditCard(card_num));
                                cardAndShippingDetail.setExpiry_date_month(exp_month);
                                cardAndShippingDetail.setExpiry_date_year(exp_year);
                                cardAndShippingDetail.setCvv(temp3.get(position).getCvv());
                                enter_name_on_card2.setText(card_name);
                                ccv_text3.setText("");
                            }
                        }
                    }));

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
        cardInfoApi.getCardList(UserPreference.getInstance().getUserID());
        cardInfoApi.execute();
    }

    public void validateCard(final Card card, BillingAddress address) {
        if (checkInternet()) {
            OAuthWebService authWebService = new OAuthWebService(Constants.WebConstants.PAYPAL_PAYMENT_URL, new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    try {
                        JSONObject jsonObj = new JSONObject(object.toString());
                        if (jsonObj.optString("state").equalsIgnoreCase("approved")) {
                            JSONObject jsonObj2 = jsonObj.getJSONArray("transactions").getJSONObject(0);
                            String id = jsonObj2.getJSONArray("related_resources").getJSONObject(0).getJSONObject("authorization").optString("id");
                            addNewCard(card.getCard_number(), card.getName_on_card(), card.getExpirationMonth() + "/" + card.getExpirationYear(), card.getCvv(), card.getCardtype());
                            voidAuthorization(id);
                        } else {
                            AlertUtils.showToast(mContext, "Please enter valid card");
                            add_card_done_button.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    if (object != null)
                        AlertUtils.showToast(mContext, object.toString());
                    else
                        AlertUtils.showToast(mContext, "Please enter valid card");
                    add_card_done_button.setVisibility(View.VISIBLE);

                }
            });
            authWebService.setRequest(card, address);
            authWebService.execute();
        }
    }

    private void saveAddress() {
        isValidPayment = true;
        isValidBillingAddress = true;
        if (enter_card_num.getText().toString().trim() != null && enter_card_num.getText().toString().trim().length() > 0) {
            enter_card_num.setBackgroundResource(R.drawable.topsearch_noborder);
            cardAndShippingDetail.setCard_number(CommonUtility.DecryptCreditCard(enter_card_num.getText().toString().trim()));
            newCard.setCard_number(enter_card_num.getText().toString().trim());
            newCard.setCardtype(Luhn.getCardTypeinString(enter_card_num.getText().toString().trim()));
        } else {
            isValidPayment = false;
            enter_card_num.setBackgroundResource(R.drawable.topsearch_red_border);
        }
        if (enter_name_on_card2.getVisibility() == View.VISIBLE && (enter_name_on_card2.getText().toString().trim() == null || enter_name_on_card2.getText().toString().trim().length() <= 0)) {
            isValidPayment = false;
            enter_name_on_card2.setBackgroundResource(R.drawable.topsearch_red_border);
        } else {
            enter_name_on_card2.setBackgroundResource(R.drawable.topsearch_noborder);
            cardAndShippingDetail.setCard_name(enter_name_on_card2.getText().toString().trim());
            newCard.setName_on_card(enter_name_on_card2.getText().toString().trim());
        }
        if (enter_expire_text2.getVisibility() == View.VISIBLE && (enter_expire_text2.getText().toString().trim() == null || enter_expire_text2.getText().toString().trim().length() <= 0)) {
            isValidPayment = false;
            enter_expire_text2.setBackgroundResource(R.drawable.topsearch_red_border);
        } else {
            enter_expire_text2.setBackgroundResource(R.drawable.topsearch_noborder);
            String[] arr = enter_expire_text2.getText().toString().trim().split("/");
            if (arr.length >= 2) {
                cardAndShippingDetail.setExpiry_date_month(arr[0]);
                newCard.setExpirationMonth(arr[0]);
                if (arr[1].length() == 2) {
                    cardAndShippingDetail.setExpiry_date_year("20" + arr[1]);
                    newCard.setExpirationYear("20" + arr[1]);
                } else {
                    cardAndShippingDetail.setExpiry_date_year(arr[1]);
                    newCard.setExpirationYear(arr[1]);
                }
            }
        }
        if (ccv_text3.getVisibility() == View.VISIBLE && (ccv_text3.getText().toString().trim() == null || ccv_text3.getText().toString().trim().length() <= 0)) {
            isValidPayment = false;
            ccv_text3.setBackgroundResource(R.drawable.topsearch_red_border);
        } else {
            ccv_text3.setBackgroundResource(R.drawable.topsearch_noborder);
            cardAndShippingDetail.setCvv(ccv_text3.getText().toString().trim());
            newCard.setCvv(ccv_text3.getText().toString().trim());
        }
        if (billing_layout.getVisibility() == View.GONE) {
            isValidBillingAddress = true;
            cardAndShippingDetail.setBilling_first_name(shippingAddress.getFirstname());
            cardAndShippingDetail.setBilling_last_name(shippingAddress.getLastname());
            if (this.apartment.length() > 0) {
                cardAndShippingDetail.setBilling_address(addressLine1 + ", " + this.apartment);
                billlingAddress.setLine1(addressLine1 + ", " + this.apartment);
            } else {
                cardAndShippingDetail.setBilling_address(addressLine1);
                billlingAddress.setLine1(addressLine1);
            }
            cardAndShippingDetail.setBilling_country(shippingAddress.getCountry());
            cardAndShippingDetail.setBilling_city(shippingAddress.getCity_town());
            cardAndShippingDetail.setBilling_zip(shippingAddress.getZip_code());
            cardAndShippingDetail.setBilling_state(shippingAddress.getState());
            billlingAddress.setFirstName(shippingAddress.getFirstname());
            billlingAddress.setLastName(shippingAddress.getLastname());
            billlingAddress.setCity(shippingAddress.getCity_town());
            billlingAddress.setCountry_code("US");
            billlingAddress.setPostal_code(shippingAddress.getZip_code());
            billlingAddress.setState(shippingAddress.getState());
        } else {
            if (billing_first_name.getText().toString().trim() != null && billing_first_name.getText().toString().trim().length() > 0) {
                billing_first_name.setBackgroundResource(R.drawable.topsearch_noborder);
                billlingAddress.setFirstName(billing_first_name.getText().toString().trim());
                cardAndShippingDetail.setBilling_first_name(billlingAddress.getFirstName());
            } else {
                isValidBillingAddress = false;
                billing_first_name.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (billing_last_name.getText().toString().trim() != null && billing_last_name.getText().toString().trim().length() > 0) {
                billing_last_name.setBackgroundResource(R.drawable.topsearch_noborder);
                billlingAddress.setLastName(billing_last_name.getText().toString().trim());
                cardAndShippingDetail.setBilling_last_name(billlingAddress.getLastName());
            } else {
                isValidBillingAddress = false;
                billing_last_name.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (enter_country_text2.getText().toString().trim() != null && enter_country_text2.getText().toString().trim().length() > 0) {
                enter_country_text2.setBackgroundResource(R.drawable.topsearch_noborder);
                billlingAddress.setCountry_code("US");
                cardAndShippingDetail.setBilling_country(billlingAddress.getCountry_code());
            } else {
                isValidBillingAddress = false;
                enter_country_text2.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (enter_address_text2.getText().toString().trim() != null && enter_address_text2.getText().toString().trim().length() > 0) {
                enter_address_text2.setBackgroundResource(R.drawable.topsearch_noborder);
                this.billAddressLine1 = enter_address_text2.getText().toString().trim();
                if (this.billApartment.length() > 0) {
                    cardAndShippingDetail.setBilling_address(billAddressLine1 + ", " + this.billApartment);
                    billlingAddress.setLine1(billAddressLine1 + ", " + this.billApartment);
                } else {
                    cardAndShippingDetail.setBilling_address(billAddressLine1);
                    billlingAddress.setLine1(billAddressLine1);
                }
            } else {
                isValidBillingAddress = false;
                enter_address_text2.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (enter_apt_text2.getText().toString().trim() != null && enter_apt_text2.getText().toString().trim().length() > 0) {
                this.billApartment = enter_apt_text2.getText().toString().trim();
                cardAndShippingDetail.setBilling_address(billAddressLine1 + ", " + this.billApartment);
                billlingAddress.setLine1(billAddressLine1 + ", " + this.billApartment);
            } else {
            }
            if (enter_city_text2.getText().toString().trim() != null && enter_city_text2.getText().toString().trim().length() > 0) {
                enter_city_text2.setBackgroundResource(R.drawable.topsearch_noborder);
                billlingAddress.setCity(enter_city_text2.getText().toString().trim());
                cardAndShippingDetail.setBilling_city(billlingAddress.getCity());
            } else {
                isValidBillingAddress = false;
                enter_city_text2.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (enter_state_text2.getText().toString().trim() != null && enter_state_text2.getText().toString().trim().length() > 0) {
                enter_state_text2.setBackgroundResource(R.drawable.topsearch_noborder);
                billlingAddress.setState(enter_state_text2.getText().toString().trim());
                cardAndShippingDetail.setBilling_state(billlingAddress.getState());
            } else {
                isValidBillingAddress = false;
                enter_state_text2.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (enter_zip_text2.getText().toString().trim() != null && enter_zip_text2.getText().toString().trim().length() > 0) {
                enter_zip_text2.setBackgroundResource(R.drawable.topsearch_noborder);
                billlingAddress.setPostal_code(enter_zip_text2.getText().toString().trim());
                cardAndShippingDetail.setBilling_zip(billlingAddress.getPostal_code());
            } else {
                isValidBillingAddress = false;
                enter_zip_text2.setBackgroundResource(R.drawable.topsearch_red_border);
            }
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

    private class MyTextWatcher2 implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            saveAddress();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void addNewCard(String cardNumber, String cardHolderName, String expiryDate, String cvv, String cardtype) {
        final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    AlertUtils.showToast(mContext, "Card added");
                    getCardList();
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

    public void voidAuthorization(String authId) {
        if (checkInternet2()) {
            OAuthWebService authWebService = new OAuthWebService(Constants.WebConstants.PAYPAL_VOID_URL + authId + "/void", new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    if (object != null)
                        AlertUtils.showToast(mContext, object.toString());
                    else
                        AlertUtils.showToast(mContext, "Unable to void payment");

                }
            });
            authWebService.setRequest(null, null);
            authWebService.execute();
        }
    }
}

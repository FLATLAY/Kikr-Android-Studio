package com.flatlay.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ShippingAddressList;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.Luhn;
import com.flatlaylib.api.AddressApi;
import com.flatlaylib.bean.Address;
import com.flatlaylib.bean.CartProduct;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.AddressRes;
import com.flatlaylib.utils.AlertUtils;

import java.util.List;

/**
 * Created by RachelDi on 4/29/18.
 */

public class CartAddressFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private TextView shipping_text9, shipping_text8, confirm_text3, add_new_address_text, contact_text,
            contact_text2, save_address_button, next_button;
    private LinearLayout saved_address_layout, new_address_layout;
    private EditText first_name, last_name, email, enter_country_text, enter_address_text, enter_apt_text,
            enter_city_text, enter_state_text, enter_zip_text;
    private boolean isValidAddress = false;
    private FragmentInspirationSection fragmentInspirationSection;
    private Address shippingAddress = new Address();
    private CartProduct cardAndShippingDetail = new CartProduct();
    private String apartment = "", addressLine1 = "", emailString = "";
    private CartAddressFragment cartAddressFragment;
    private List<Product> my_productLists;
    private double total_price;
//    private boolean firstTimeGetCard;

    public CartAddressFragment(List<Product> my_productLists,double total_price) {
        this.my_productLists=my_productLists;
        this.total_price=total_price;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.cart_address_layout, null);
        cartAddressFragment = this;
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_address_text:
                if (new_address_layout.getVisibility() == View.GONE) {
                    new_address_layout.setVisibility(View.VISIBLE);
                    add_new_address_text.setText("Cancel Adding New Address");
                } else {
                    new_address_layout.setVisibility(View.GONE);
                    add_new_address_text.setText("Add New Address");
                }
                break;
            case R.id.save_address_button:
                addNewAddress();
                break;
            case R.id.next_button:
                if (isValidAddress) {
                    ((HomeActivity) mContext).myAddFragment(new CartCardFragment(shippingAddress,my_productLists,cardAndShippingDetail,total_price));
                } else {
                    AlertUtils.showToast(mContext, "Please enter required information.");
                }
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        shipping_text9 = (TextView) mainView.findViewById(R.id.shipping_text9);
        shipping_text9.setTypeface(FontUtility.setMontserratLight(mContext));
        shipping_text8 = (TextView) mainView.findViewById(R.id.shipping_text8);
        shipping_text8.setTypeface(FontUtility.setMontserratLight(mContext));
        confirm_text3 = (TextView) mainView.findViewById(R.id.confirm_text3);
        confirm_text3.setTypeface(FontUtility.setMontserratLight(mContext));
        saved_address_layout = (LinearLayout) mainView.findViewById(R.id.saved_address_layout);
        add_new_address_text = (TextView) mainView.findViewById(R.id.add_new_address_text);
        add_new_address_text.setTypeface(FontUtility.setMontserratRegular(mContext));
        new_address_layout = (LinearLayout) mainView.findViewById(R.id.new_address_layout);
        contact_text = (TextView) mainView.findViewById(R.id.contact_text);
        contact_text.setTypeface(FontUtility.setMontserratLight(mContext));
        first_name = (EditText) mainView.findViewById(R.id.first_name);
        first_name.setTypeface(FontUtility.setMontserratLight(mContext));
        MyTextWatcher2 watcher2 = new MyTextWatcher2();
        first_name.addTextChangedListener(watcher2);
        last_name = (EditText) mainView.findViewById(R.id.last_name);
        last_name.setTypeface(FontUtility.setMontserratLight(mContext));
        last_name.addTextChangedListener(watcher2);
        email = (EditText) mainView.findViewById(R.id.email);
        email.setTypeface(FontUtility.setMontserratLight(mContext));
        email.addTextChangedListener(watcher2);
        contact_text2 = (TextView) mainView.findViewById(R.id.contact_text2);
        contact_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_country_text = (EditText) mainView.findViewById(R.id.enter_country_text);
        enter_country_text.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_country_text.addTextChangedListener(watcher2);
        enter_address_text = (EditText) mainView.findViewById(R.id.enter_address_text);
        enter_address_text.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_address_text.addTextChangedListener(watcher2);
        enter_apt_text = (EditText) mainView.findViewById(R.id.enter_apt_text);
        enter_apt_text.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_apt_text.addTextChangedListener(watcher2);
        enter_city_text = (EditText) mainView.findViewById(R.id.enter_city_text);
        enter_city_text.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_city_text.addTextChangedListener(watcher2);
        enter_state_text = (EditText) mainView.findViewById(R.id.enter_state_text);
        enter_state_text.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_state_text.addTextChangedListener(watcher2);
        enter_zip_text = (EditText) mainView.findViewById(R.id.enter_zip_text);
        enter_zip_text.setTypeface(FontUtility.setMontserratLight(mContext));
        enter_zip_text.addTextChangedListener(watcher2);
        save_address_button = (TextView) mainView.findViewById(R.id.save_address_button);
        save_address_button.setTypeface(FontUtility.setMontserratRegular(mContext));
        next_button = (TextView) mainView.findViewById(R.id.next_button);
        next_button.setTypeface(FontUtility.setMontserratLight(mContext));
        getAddressList();
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        add_new_address_text.setOnClickListener(this);
        save_address_button.setOnClickListener(this);
        next_button.setOnClickListener(this);

    }

    public void getAddressList() {
        new_address_layout.setVisibility(View.GONE);
        saved_address_layout.setVisibility(View.VISIBLE);
        add_new_address_text.setText("Add New Address");
        final AddressApi addressApi = new AddressApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    AddressRes addressRes = (AddressRes) object;
                    List<Address> data = addressRes.getData();
                    saved_address_layout.removeAllViews();
                    if (data.size() > 0)
                        saved_address_layout.addView(new ShippingAddressList(mContext, addressRes.getData(), cartAddressFragment).getView());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    AddressRes addressRes = (AddressRes) object;
                    AlertUtils.showToast(mContext, addressRes.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        addressApi.getAddressList(UserPreference.getInstance().getUserID());
        addressApi.execute();
    }

    private void saveAddress() {
        isValidAddress = true;
//        isValidPayment = true;
//        isValidBillingAddress = true;
        if (new_address_layout.getVisibility() == View.VISIBLE) {
            if (first_name.getText().toString().trim() != null && first_name.getText().toString().trim().length() > 0) {
                shippingAddress.setFirstname(first_name.getText().toString().trim());
                first_name.setBackgroundResource(R.drawable.topsearch_noborder);
                cardAndShippingDetail.setShipping_first_name(shippingAddress.getFirstname());
            } else {
                isValidAddress = false;
                first_name.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (last_name.getText().toString().trim() != null && last_name.getText().toString().trim().length() > 0) {
                shippingAddress.setLastname(last_name.getText().toString().trim());
                last_name.setBackgroundResource(R.drawable.topsearch_noborder);
                cardAndShippingDetail.setShipping_last_name(shippingAddress.getLastname());
            } else {
                isValidAddress = false;
                last_name.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (email.getText().toString().trim() != null && email.getText().toString().trim().length() > 0) {
                this.emailString = email.getText().toString().trim();
                cardAndShippingDetail.setEmail(emailString);
            } else {
                this.emailString = UserPreference.getInstance().getEmail();
                cardAndShippingDetail.setEmail(emailString);
            }
            if (enter_address_text.getText().toString().trim() != null && enter_address_text.getText().toString().trim().length() > 0) {
                this.addressLine1 = enter_address_text.getText().toString().trim();
                enter_address_text.setBackgroundResource(R.drawable.topsearch_noborder);
                shippingAddress.setStreet_address(addressLine1);
                if (this.apartment.length() > 0)
                    cardAndShippingDetail.setShipping_address(addressLine1 + ", " + this.apartment);
                else
                    cardAndShippingDetail.setShipping_address(addressLine1);
            } else {
                isValidAddress = false;
                enter_address_text.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (enter_apt_text.getText().toString().trim() != null && enter_apt_text.getText().toString().trim().length() > 0) {
                this.apartment = enter_apt_text.getText().toString().trim();
                shippingAddress.setApartment(apartment);
                cardAndShippingDetail.setShipping_address(addressLine1 + ", " + this.apartment);
            } else {
            }
            if (enter_country_text.getText().toString().trim() != null && enter_country_text.getText().toString().trim().length() > 0) {
                shippingAddress.setCountry(enter_country_text.getText().toString().trim());
                enter_country_text.setBackgroundResource(R.drawable.topsearch_noborder);
                cardAndShippingDetail.setShipping_country(shippingAddress.getCountry());
            } else {
                isValidAddress = false;
                enter_country_text.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (enter_city_text.getText().toString().trim() != null && enter_city_text.getText().toString().trim().length() > 0) {
                shippingAddress.setCity_town(enter_city_text.getText().toString().trim());
                enter_city_text.setBackgroundResource(R.drawable.topsearch_noborder);
                cardAndShippingDetail.setShipping_city(shippingAddress.getCity_town());
            } else {
                isValidAddress = false;
                enter_city_text.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (enter_zip_text.getText().toString().trim() != null && enter_zip_text.getText().toString().trim().length() > 0) {
                shippingAddress.setZip_code(enter_zip_text.getText().toString().trim());
                enter_zip_text.setBackgroundResource(R.drawable.topsearch_noborder);
                cardAndShippingDetail.setShipping_zip(shippingAddress.getZip_code());
            } else {
                isValidAddress = false;
                enter_zip_text.setBackgroundResource(R.drawable.topsearch_red_border);
            }
            if (enter_state_text.getText().toString().trim() != null && enter_state_text.getText().toString().trim().length() > 0) {
                shippingAddress.setState(enter_state_text.getText().toString().trim());
                enter_state_text.setBackgroundResource(R.drawable.topsearch_noborder);
                cardAndShippingDetail.setShipping_state(shippingAddress.getState());
            } else {
                isValidAddress = false;
                enter_state_text.setBackgroundResource(R.drawable.topsearch_red_border);
            }
        }
//        if (enter_promo_text.getText().toString().trim() != null && enter_promo_text.getText().toString().trim().length() > 0) {
//            this.promoCode = enter_promo_text.getText().toString().trim();
//        }
//        if (enter_card_num.getText().toString().trim() != null && enter_card_num.getText().toString().trim().length() > 0) {
//            enter_card_num.setBackgroundResource(R.drawable.topsearch_noborder);
//            cardAndShippingDetail.setCard_number(CommonUtility.DecryptCreditCard(enter_card_num.getText().toString().trim()));
//            newCard.setCard_number(enter_card_num.getText().toString().trim());
//            newCard.setCardtype(Luhn.getCardTypeinString(enter_card_num.getText().toString().trim()));
//        } else {
//            isValidPayment = false;
//            enter_card_num.setBackgroundResource(R.drawable.topsearch_red_border);
//        }
//        if (enter_name_on_card2.getVisibility() == View.VISIBLE && (enter_name_on_card2.getText().toString().trim() == null || enter_name_on_card2.getText().toString().trim().length() <= 0)) {
//            isValidPayment = false;
//            enter_name_on_card2.setBackgroundResource(R.drawable.topsearch_red_border);
//        } else {
//            enter_name_on_card2.setBackgroundResource(R.drawable.topsearch_noborder);
//            cardAndShippingDetail.setCard_name(enter_name_on_card2.getText().toString().trim());
//            newCard.setName_on_card(enter_name_on_card2.getText().toString().trim());
//        }
//        if (enter_expire_text2.getVisibility() == View.VISIBLE && (enter_expire_text2.getText().toString().trim() == null || enter_expire_text2.getText().toString().trim().length() <= 0)) {
//            isValidPayment = false;
//            enter_expire_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//        } else {
//            enter_expire_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//            String[] arr = enter_expire_text2.getText().toString().trim().split("/");
//            if (arr.length >= 2) {
//                cardAndShippingDetail.setExpiry_date_month(arr[0]);
//                newCard.setExpirationMonth(arr[0]);
//                if (arr[1].length() == 2) {
//                    cardAndShippingDetail.setExpiry_date_year("20" + arr[1]);
//                    newCard.setExpirationYear("20" + arr[1]);
//                } else {
//                    cardAndShippingDetail.setExpiry_date_year(arr[1]);
//                    newCard.setExpirationYear(arr[1]);
//                }
//            }
//        }
//        if (ccv_text3.getVisibility() == View.VISIBLE && (ccv_text3.getText().toString().trim() == null || ccv_text3.getText().toString().trim().length() <= 0)) {
//            isValidPayment = false;
//            ccv_text3.setBackgroundResource(R.drawable.topsearch_red_border);
//        } else {
//            ccv_text3.setBackgroundResource(R.drawable.topsearch_noborder);
//            cardAndShippingDetail.setCvv(ccv_text3.getText().toString().trim());
//            newCard.setCvv(ccv_text3.getText().toString().trim());
//        }
//        if (billing_layout.getVisibility() == View.GONE) {
//            isValidBillingAddress = true;
//            cardAndShippingDetail.setBilling_first_name(shippingAddress.getFirstname());
//            cardAndShippingDetail.setBilling_last_name(shippingAddress.getLastname());
//            if (this.apartment.length() > 0) {
//                cardAndShippingDetail.setBilling_address(addressLine1 + ", " + this.apartment);
//                billlingAddress.setLine1(addressLine1 + ", " + this.apartment);
//            } else {
//                cardAndShippingDetail.setBilling_address(addressLine1);
//                billlingAddress.setLine1(addressLine1);
//            }
//            cardAndShippingDetail.setBilling_country(shippingAddress.getCountry());
//            cardAndShippingDetail.setBilling_city(shippingAddress.getCity_town());
//            cardAndShippingDetail.setBilling_zip(shippingAddress.getZip_code());
//            cardAndShippingDetail.setBilling_state(shippingAddress.getState());
//            billlingAddress.setFirstName(shippingAddress.getFirstname());
//            billlingAddress.setLastName(shippingAddress.getLastname());
//            billlingAddress.setCity(shippingAddress.getCity_town());
//            billlingAddress.setCountry_code("US");
//            billlingAddress.setPostal_code(shippingAddress.getZip_code());
//            billlingAddress.setState(shippingAddress.getState());
//        } else {
//            if (billing_first_name.getText().toString().trim() != null && billing_first_name.getText().toString().trim().length() > 0) {
//                billing_first_name.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setFirstName(billing_first_name.getText().toString().trim());
//                cardAndShippingDetail.setBilling_first_name(billlingAddress.getFirstName());
//            } else {
//                isValidBillingAddress = false;
//                billing_first_name.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (billing_last_name.getText().toString().trim() != null && billing_last_name.getText().toString().trim().length() > 0) {
//                billing_last_name.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setLastName(billing_last_name.getText().toString().trim());
//                cardAndShippingDetail.setBilling_last_name(billlingAddress.getLastName());
//            } else {
//                isValidBillingAddress = false;
//                billing_last_name.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_country_text2.getText().toString().trim() != null && enter_country_text2.getText().toString().trim().length() > 0) {
//                enter_country_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setCountry_code("US");
//                cardAndShippingDetail.setBilling_country(billlingAddress.getCountry_code());
//            } else {
//                isValidBillingAddress = false;
//                enter_country_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_address_text2.getText().toString().trim() != null && enter_address_text2.getText().toString().trim().length() > 0) {
//                enter_address_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                this.billAddressLine1 = enter_address_text2.getText().toString().trim();
//                if (this.billApartment.length() > 0) {
//                    cardAndShippingDetail.setBilling_address(billAddressLine1 + ", " + this.billApartment);
//                    billlingAddress.setLine1(billAddressLine1 + ", " + this.billApartment);
//                } else {
//                    cardAndShippingDetail.setBilling_address(billAddressLine1);
//                    billlingAddress.setLine1(billAddressLine1);
//                }
//            } else {
//                isValidBillingAddress = false;
//                enter_address_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_apt_text2.getText().toString().trim() != null && enter_apt_text2.getText().toString().trim().length() > 0) {
//                this.billApartment = enter_apt_text2.getText().toString().trim();
//                cardAndShippingDetail.setBilling_address(billAddressLine1 + ", " + this.billApartment);
//                billlingAddress.setLine1(billAddressLine1 + ", " + this.billApartment);
//            } else {
//            }
//            if (enter_city_text2.getText().toString().trim() != null && enter_city_text2.getText().toString().trim().length() > 0) {
//                enter_city_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setCity(enter_city_text2.getText().toString().trim());
//                cardAndShippingDetail.setBilling_city(billlingAddress.getCity());
//            } else {
//                isValidBillingAddress = false;
//                enter_city_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_state_text2.getText().toString().trim() != null && enter_state_text2.getText().toString().trim().length() > 0) {
//                enter_state_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setState(enter_state_text2.getText().toString().trim());
//                cardAndShippingDetail.setBilling_state(billlingAddress.getState());
//            } else {
//                isValidBillingAddress = false;
//                enter_state_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
//            if (enter_zip_text2.getText().toString().trim() != null && enter_zip_text2.getText().toString().trim().length() > 0) {
//                enter_zip_text2.setBackgroundResource(R.drawable.topsearch_noborder);
//                billlingAddress.setPostal_code(enter_zip_text2.getText().toString().trim());
//                cardAndShippingDetail.setBilling_zip(billlingAddress.getPostal_code());
//            } else {
//                isValidBillingAddress = false;
//                enter_zip_text2.setBackgroundResource(R.drawable.topsearch_red_border);
//            }
        // }
    }

    private void addNewAddress() {
        final AddressApi addressApi = new AddressApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    AddressRes addressRes = (AddressRes) object;
                    AlertUtils.showToast(mContext, addressRes.getMessage());
                    getAddressList();
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    AddressRes response = (AddressRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        //need to change "title" and "tel" if necessary
        addressApi.addAddress(UserPreference.getInstance().getUserID(), "",
                shippingAddress.getFirstname(), shippingAddress.getLastname(), shippingAddress.getState(), "", shippingAddress.getStreet_address(), shippingAddress.getCity_town(), shippingAddress.getZip_code(), shippingAddress.getCountry(), shippingAddress.getApartment());
        addressApi.execute();
    }

    public void setAddress(Address address) {
        this.shippingAddress = address;
        isValidAddress = true;
        cardAndShippingDetail.setShipping_title(shippingAddress.getTitle());
        cardAndShippingDetail.setShipping_first_name(shippingAddress.getFirstname());
        cardAndShippingDetail.setShipping_last_name(shippingAddress.getLastname());
        cardAndShippingDetail.setShipping_city(shippingAddress.getCity_town());
        cardAndShippingDetail.setShipping_country(shippingAddress.getCountry());
        cardAndShippingDetail.setShipping_address(shippingAddress.getStreet_address() + " " + shippingAddress.getApartment());
        cardAndShippingDetail.setShipping_zip(shippingAddress.getZip_code());
        cardAndShippingDetail.setShipping_telephone(shippingAddress.getTel());
        cardAndShippingDetail.setShipping_state(shippingAddress.getState());
        cardAndShippingDetail.setBilling_title(shippingAddress.getTitle());
        cardAndShippingDetail.setBilling_first_name(shippingAddress.getFirstname());
        cardAndShippingDetail.setBilling_last_name(shippingAddress.getLastname());
        cardAndShippingDetail.setBilling_city(shippingAddress.getCity_town());
        cardAndShippingDetail.setBilling_country(shippingAddress.getCountry());
        cardAndShippingDetail.setBilling_address(shippingAddress.getStreet_address() + " " + shippingAddress.getApartment());
        cardAndShippingDetail.setBilling_zip(shippingAddress.getZip_code());
        cardAndShippingDetail.setBilling_telephone(shippingAddress.getTel());
        cardAndShippingDetail.setBilling_state(shippingAddress.getState());
        cardAndShippingDetail.setEmail(UserPreference.getInstance().getEmail());
        for (int i = 0; i < my_productLists.size(); i++) {
            my_productLists.get(i).setFreeShipping(false);
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

}

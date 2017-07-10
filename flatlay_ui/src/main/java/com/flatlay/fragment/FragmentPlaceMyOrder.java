package com.flatlay.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flatlay.GCMAlarmReceiver;
import com.flatlay.KikrApp;
import com.flatlay.KikrApp.TrackerName;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.AddressVerificationDialog;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.dialog.OrderProcessingDialog;
import com.flatlay.dialog.OrderReadyForConfirmDialog;
import com.flatlay.dialog.SelectCardAndAddressDialog;
import com.flatlay.ui.CartProductsUI;
import com.flatlay.ui.PaymentCardsList;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.ShippingAddressList;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.Luhn;
import com.flatlay.utility.ValidZipcode;
import com.flatlaylib.api.AddressApi;
import com.flatlaylib.api.CardInfoApi;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.LOBApi;
import com.flatlaylib.api.OAuthWebService;
import com.flatlaylib.api.TwoTapApi;
import com.flatlaylib.api.UpdateCartApi;
import com.flatlaylib.bean.Address;
import com.flatlaylib.bean.BillingAddress;
import com.flatlaylib.bean.Card;
import com.flatlaylib.bean.CartProduct;
import com.flatlaylib.bean.CartTotalInfo;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.AddressRes;
import com.flatlaylib.service.res.CardInfoRes;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.ProductAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.lob.Lob;
import com.lob.client.AsyncLobClient;
import com.lob.client.LobClient;
import com.lob.protocol.request.USVerificationRequest;
import com.lob.protocol.response.USVerificationResponse;

public class FragmentPlaceMyOrder extends FragmentBaseGoogleWallet implements OnClickListener {
    private View mainView, cardViewLine;
    private TextView itemsCountText, subtotalText, taxText, shippingText, apartmentEditText, handlingText, totalItemsText, totalText, learnmoreText;
    private LinearLayout addPaymentMethodLayout, addCardLayout, shippingAddressLayout, promoCodeLayout, placemyOrderLayout, addAddressLayout;
    private LinearLayout cardListLayout, addressListLayout, paypalLayout;
    private ImageView paymentMethodDropImage, address_arrow;
    private ProgressBarDialog progressBarDialog;
    private boolean isLoadAddressList = true, isLoadCardList = true;
    public boolean isAddressSelected = false, isCardSelected = false;
    private CartTotalInfo cartTotalInfo;
    private FragmentPlaceMyOrder fragmentPlaceMyOrder;
    private Card card;
    private Address address;
    private String subtotal = "0", tax = "0", shipping = "0", finalprice = "0";
    private LinearLayout addNewCardLayout, addNewAddressLayout;
    private EditText cardNumberEditText, cardHolderNameEditText, cvvEditText;
    private Spinner monthSpinner, yearSpinner;
    private Button doneBtn;
    private ArrayList<String> list = new ArrayList<String>();
    private EditText streetNameEditText, cityEditText, zipEditText, firstNameEditText, lastNameEditText, telephoneEditText;
    private EditText b_streetNameEditText, b_cityEditText, b_zipEditText, b_firstNameEditText, b_lastNameEditText;
    private Spinner countryEditText, titleSpinner, stateSpinnerUS, stateSpinnerCanada;
    private Spinner b_countryEditText, b_stateSpinnerUS, b_stateSpinnerCanada;
    private Button doneAddressBtn;
    private String streetnumber = "", streetname, city, zipcode, country, countryForLob, firstname, lastname, state, telephone, title;
    private String b_streetname, b_city, b_zipcode, b_country, b_countryForLob, b_firstname, b_lastname, b_state;
    private List<CartProduct> cartProducts = new ArrayList<CartProduct>();
    private String cartid;
    private Runnable runnable;
    private Handler handler = new Handler();
    private int i = 0;
    private List<Product> productList = new ArrayList<Product>();
    private List<String> productUrls = new ArrayList<String>();
    private ProgressBar progressBarPlaceOrder;
    private LinearLayout cartProductList;
    private ProgressDialog dialog;
    private FrameLayout loadingBar;
    private TextView txtMessageBar;
    private TextView whyTextView;
    private boolean isPurchaseDone;
    private boolean isConfirmClicked;
    private static String purchaseId;
    private int statusApi = 0;
    private String otherdata;
    HashMap<String, List<Product>> cartList = new HashMap<String, List<Product>>();
    CartProduct cardAndShippingDetail;
    boolean isShowAlerts = true;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    //	commit
    public FragmentPlaceMyOrder(List<Product> productList, String cartid) {
        cardAndShippingDetail = new CartProduct();
        this.productList = productList;
        this.cartid = cartid;
//		for(int i=0;i<productList.size();i++){
//			productList.get(i).setAffiliateurl(productList.get(i).getAffiliateurl()+"/"+cartid);
//			productList.get(i).setAffiliateurlforsharing(productList.get(i).getAffiliateurlforsharing()+"/"+cartid);
//		}
    }


    public FragmentPlaceMyOrder(String otherdata, boolean isShowAlerts) {
        this.otherdata = otherdata;
        this.isShowAlerts = isShowAlerts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_place_my_order, null);
        fragmentPlaceMyOrder = this;
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        Log.w("FragmentPlaceMyOrder","initUI");
        itemsCountText = (TextView) mainView.findViewById(R.id.itemsCountText);
        subtotalText = (TextView) mainView.findViewById(R.id.subtotalText);
        taxText = (TextView) mainView.findViewById(R.id.taxText);
        shippingText = (TextView) mainView.findViewById(R.id.shippingText);
        handlingText = (TextView) mainView.findViewById(R.id.handlingText);
        totalItemsText = (TextView) mainView.findViewById(R.id.totalItemsText);
        totalText = (TextView) mainView.findViewById(R.id.totalText);
        learnmoreText = (TextView) mainView.findViewById(R.id.learnmoreText);
        paymentMethodDropImage = (ImageView) mainView.findViewById(R.id.paymentMethodDropImage);
        addPaymentMethodLayout = (LinearLayout) mainView.findViewById(R.id.addPaymentMethodLayout);
        addCardLayout = (LinearLayout) mainView.findViewById(R.id.addCardLayout);
        shippingAddressLayout = (LinearLayout) mainView.findViewById(R.id.shippingAddressLayout);
        promoCodeLayout = (LinearLayout) mainView.findViewById(R.id.promoCodeLayout);
        placemyOrderLayout = (LinearLayout) mainView.findViewById(R.id.placemyOrderLayout);
        address_arrow = (ImageView) mainView.findViewById(R.id.address_arrow);
        addAddressLayout = (LinearLayout) mainView.findViewById(R.id.addAddressLayout);
        cardListLayout = (LinearLayout) mainView.findViewById(R.id.cardListLayout);
        addressListLayout = (LinearLayout) mainView.findViewById(R.id.addressListLayout);
        paypalLayout = (LinearLayout) mainView.findViewById(R.id.paypalLayout);
        cardViewLine = mainView.findViewById(R.id.cardViewLine);
        cardNumberEditText = (EditText) mainView.findViewById(R.id.cardNumberEditText);
        cardHolderNameEditText = (EditText) mainView.findViewById(R.id.cardHolderNameEditText);
        cvvEditText = (EditText) mainView.findViewById(R.id.cvvEditText);
        monthSpinner = (Spinner) mainView.findViewById(R.id.monthSpinner);
        yearSpinner = (Spinner) mainView.findViewById(R.id.yearSpinner);
        doneBtn = (Button) mainView.findViewById(R.id.doneBtn);
        addNewCardLayout = (LinearLayout) mainView.findViewById(R.id.addNewCardLayout);
        addNewAddressLayout = (LinearLayout) mainView.findViewById(R.id.addNewAddressLayout);
        streetNameEditText = (EditText) mainView.findViewById(R.id.streetNameEditText);
        cityEditText = (EditText) mainView.findViewById(R.id.cityEditText);
        zipEditText = (EditText) mainView.findViewById(R.id.zipEditText);
        firstNameEditText = (EditText) mainView.findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) mainView.findViewById(R.id.lastNameEditText);
        stateSpinnerUS = (Spinner) mainView.findViewById(R.id.stateSpinnerUS);
        stateSpinnerCanada = (Spinner) mainView.findViewById(R.id.stateSpinnerCanada);
        telephoneEditText = (EditText) mainView.findViewById(R.id.telephoneEditText);
        countryEditText = (Spinner) mainView.findViewById(R.id.countryEditText);
        titleSpinner = (Spinner) mainView.findViewById(R.id.titleSpinner);
        doneAddressBtn = (Button) mainView.findViewById(R.id.doneAddressBtn);
        cartProductList = (LinearLayout) mainView.findViewById(R.id.cartProductList);
        progressBarPlaceOrder = (ProgressBar) mainView.findViewById(R.id.progressBarPlaceOrder);
        loadingBar = (FrameLayout) mainView.findViewById(R.id.loadingBar);
        txtMessageBar = (TextView) mainView.findViewById(R.id.txtMessageBar);
        apartmentEditText = (EditText) mainView.findViewById(R.id.apartmentEditText);

        b_streetNameEditText = (EditText) mainView.findViewById(R.id.b_streetNameEditText);
        b_cityEditText = (EditText) mainView.findViewById(R.id.b_cityEditText);
        b_zipEditText = (EditText) mainView.findViewById(R.id.b_zipEditText);
        b_firstNameEditText = (EditText) mainView.findViewById(R.id.b_firstNameEditText);
        b_lastNameEditText = (EditText) mainView.findViewById(R.id.b_lastNameEditText);
        b_stateSpinnerUS = (Spinner) mainView.findViewById(R.id.b_stateSpinnerUS);
        b_stateSpinnerCanada = (Spinner) mainView.findViewById(R.id.b_stateSpinnerCanada);
        b_countryEditText = (Spinner) mainView.findViewById(R.id.b_countryEditText);

        changePlaceOrderColor();
        learnmoreText.setPaintFlags(learnmoreText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        whyTextView = (TextView) mainView.findViewById(R.id.whyTextView);
        whyTextView.setPaintFlags(learnmoreText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT)
                .setCheckoutStep(2);
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .setProductAction(productAction);

        Tracker t = ((KikrApp) mContext.getApplication()).getTracker(TrackerName.APP_TRACKER);
        t.setScreenName("Fragment Place My Order");
        t.send(builder.build());
    }

    private void createCartList() {
        Log.w("FragmentPlaceMO","validateCard()");
        for (int i = 0; i < productList.size(); i++) {
            String siteId = productList.get(i).getSiteId();
            if (cartList.containsKey(siteId)) {
                cartList.get(siteId).add(productList.get(i));
            } else {
                List<Product> list = new ArrayList<Product>();
                list.add(productList.get(i));
                cartList.put(siteId, list);
            }
        }
        for (int i = 0; i < productList.size(); i++) {
            Syso.info("123456789  main>>>>>>>>" + productList.get(i).getSelected_values());
        }
        for (String key : cartList.keySet()) {
            for (Product product : cartList.get(key)) {
                Syso.info("123456789  second>>>>>>>>" + product.getSelected_values());
            }
        }
        setOtherData();
    }


    private void setOtherData() {
        Log.w("FragmentPlaceMO","setOtherData()");
        if (!isAddressSelected && !isCardSelected) {
            shippingAddressLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            addPaymentMethodLayout.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
        }

        if (otherdata == null) {
            SelectCardAndAddressDialog cardAndAddressDialog = new SelectCardAndAddressDialog(mContext);
            cardAndAddressDialog.show();
        }
    }


    @Override
    public void refreshData(Bundle bundle) {
    }

    @Override
    public void setClickListener() {
        addPaymentMethodLayout.setOnClickListener(this);
        addCardLayout.setOnClickListener(this);
        shippingAddressLayout.setOnClickListener(this);
        promoCodeLayout.setOnClickListener(this);
        placemyOrderLayout.setOnClickListener(this);
        addAddressLayout.setOnClickListener(this);
        paypalLayout.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        doneAddressBtn.setOnClickListener(this);
        learnmoreText.setOnClickListener(this);
        countryEditText.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    // USA
                    stateSpinnerUS.setVisibility(View.VISIBLE);
                    stateSpinnerCanada.setVisibility(View.GONE);
                } else if (position == 1) {
                    // Canada
                    stateSpinnerUS.setVisibility(View.GONE);
                    stateSpinnerCanada.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        b_countryEditText.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    // USA
                    b_stateSpinnerUS.setVisibility(View.VISIBLE);
                    b_stateSpinnerCanada.setVisibility(View.GONE);
                } else if (position == 1) {
                    // Canada
                    b_stateSpinnerUS.setVisibility(View.GONE);
                    b_stateSpinnerCanada.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        whyTextView.setOnClickListener(this);
    }

    @Override
    public void setData(Bundle bundle) {
        Log.w("FragmentPlaceMO","setData()");
        createCartList();
        if (otherdata != null) {
            //	JSONObject jsonObject = new JSONObject(otherdata.toString());
//				getFinalPrice(jsonObject);
            getFinalPrice(otherdata);
        } else {
            if (bundle.containsKey("total_info")) {
                cartTotalInfo = (CartTotalInfo) bundle.getSerializable("total_info");
//			updateCartInfo();
                itemsCountText.setText("" + productList.size());
                totalItemsText.setText("Total (" + productList.size() + " items)");
                double price = 0;
                for (int i = 0; i < productList.size(); i++) {
                    if (!TextUtils.isEmpty(productList.get(i).getSaleprice())) {

                        price += Double.parseDouble(productList.get(i).getSaleprice())*Integer.parseInt(productList.get(i).getQuantity());
                    } else {
                        price += Double.parseDouble(productList.get(i).getRetailprice())*Integer.parseInt(productList.get(i).getQuantity());
                    }
                }
                subtotalText.setText("$" + CommonUtility.getFormatedNum(price));
                cartProductList.removeAllViews();
                cartProductList.addView(new CartProductsUI(mContext, productList).getView());
            }
            list.add("YYYY");
            for (int i = 2015; i < 2049; i++) {
                list.add(Integer.toString(i));
            }
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_list_item_1, list);
            adapter2.setDropDownViewResource(android.R.layout.simple_list_item_1);
            yearSpinner.setAdapter(adapter2);
//		if (((HomeActivity)mContext).checkInternet())
//		getCartId();
        }
    }

    @Override
    public void onClick(View v) {
        Log.w("FragmentPlaceMO","onClick()");
        Animation animationTop = AnimationUtils.loadAnimation(mContext, R.anim.rotate_arrow_top);
        Animation animationBottom = AnimationUtils.loadAnimation(mContext, R.anim.rotate_arrow_bottom);
        switch (v.getId()) {
            case R.id.placemyOrderLayout:
                if (isAddressSelected && isCardSelected)
                    callPurchase();
                break;
            case R.id.paypalLayout:
                if (isAddressSelected && checkInternet())
                    ((HomeActivity) mContext).startPaypal();
                else
                    AlertUtils.showToast(mContext, R.string.alert_address_not_selected_for_paypal);
                break;
            case R.id.addCardLayout:
                if (addNewCardLayout.getVisibility() == View.VISIBLE) {
                    addNewCardLayout.setVisibility(View.GONE);
                } else {
                    addNewCardLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.addAddressLayout:
                if (addNewAddressLayout.getVisibility() == View.VISIBLE) {
                    addNewAddressLayout.setVisibility(View.GONE);
                } else {
                    addNewAddressLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.shippingAddressLayout:
                String email=UserPreference.getInstance().getEmail();
                        String username=UserPreference.getInstance().getUserName();
                System.out.print(email +username);
             //   if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                    if ( UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                    CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                    createAccountDialog.setCanceledOnTouchOutside(false);
                    createAccountDialog.show();
                }
                else {
                    if (address_arrow.getTag() != null && address_arrow.getTag().equals("set")) {
                        address_arrow.startAnimation(animationTop);
                        address_arrow.setTag("reset");
                        addAddressLayout.setVisibility(View.GONE);
                        addressListLayout.setVisibility(View.GONE);
                        addNewAddressLayout.setVisibility(View.GONE);
                    } else {
                        address_arrow.startAnimation(animationBottom);
                        address_arrow.setTag("set");
                        addAddressLayout.setVisibility(View.VISIBLE);
                        addressListLayout.setVisibility(View.VISIBLE);
                        if (isLoadAddressList && checkInternet())
                            getAddressList();
                    }
              }

                break;
            case R.id.addPaymentMethodLayout:
                if (paymentMethodDropImage.getTag() != null && paymentMethodDropImage.getTag().equals("set")) {
                    paymentMethodDropImage.startAnimation(animationTop);
                    paymentMethodDropImage.setTag("reset");
                    addCardLayout.setVisibility(View.GONE);
                    cardListLayout.setVisibility(View.GONE);
                    cardViewLine.setVisibility(View.GONE);
                    addNewCardLayout.setVisibility(View.GONE);
                } else {
                    paymentMethodDropImage.startAnimation(animationBottom);
                    paymentMethodDropImage.setTag("set");
                    addCardLayout.setVisibility(View.VISIBLE);
                    cardListLayout.setVisibility(View.VISIBLE);
                    cardViewLine.setVisibility(View.VISIBLE);
                    if (isLoadCardList && checkInternet())
                        getCardList();
                }
                break;
            case R.id.doneBtn:
                validateUserInput();
                break;
            case R.id.doneAddressBtn:
                validateUserInputAddress();
                break;
            case R.id.learnmoreText:
                addFragment(new FragmentPurchaseGuarantee());
                break;
            case R.id.whyTextView:
                addFragment(new FragmentLearnMoreOutsideUS());
            default:
                break;
        }
    }

    private void validateAddressAndCard() {
        Log.w("FragmentPlaceMO","validateAddressAndCard()");
        if (!isAddressSelected && !isCardSelected) {
            shippingAddressLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_gray));
            addPaymentMethodLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_gray));
            AlertUtils.showToast(mContext, R.string.alert_address_and_card_not_selected_for_card);
        } else if (!isAddressSelected) {
            shippingAddressLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_gray));
            addPaymentMethodLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
            AlertUtils.showToast(mContext, R.string.alert_address_not_selected_for_card);
        } else if (!isCardSelected) {
            shippingAddressLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
            addPaymentMethodLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_gray));
            AlertUtils.showToast(mContext, R.string.alert_card_not_selected_for_card);
        } else {
            if (card != null && checkInternet()) {
                shippingAddressLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
                addPaymentMethodLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
                changePlaceOrderColor();
            }
        }
    }

    private void changePlaceOrderColor() {
        Log.w("FragmentPlaceMO","changePlaceOrderColor()");
        try {
            if (isAddressSelected && isCardSelected && placemyOrderLayout != null) {
                placemyOrderLayout.setBackgroundColor(getResources().getColor(R.color.btn_green));
                placemyOrderLayout.setClickable(true);
            } else if (placemyOrderLayout != null)
                placemyOrderLayout.setBackgroundColor(getResources().getColor(R.color.btn_gray));
            if (shippingAddressLayout != null)
                shippingAddressLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePlaceOrderColorPayment() {
        if (isAddressSelected && isCardSelected)
            placemyOrderLayout.setBackgroundColor(getResources().getColor(R.color.btn_green));
        else
            placemyOrderLayout.setBackgroundColor(getResources().getColor(R.color.btn_gray));

        if (!isCardSelected)
            addPaymentMethodLayout.setBackgroundColor(mContext.getResources().getColor(R.color.tab_selected_new));
        else
            addPaymentMethodLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
    }

    public void getCardList() {
        Log.w("FragmentPlaceMO","getCardList()");
        progressBarDialog = new ProgressBarDialog(mContext);
        progressBarDialog.show();
        final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                progressBarDialog.dismiss();
                if (object != null) {
                    CardInfoRes cardInfoRes = (CardInfoRes) object;
                    cardListLayout.removeAllViews();
                    cardListLayout.addView(new PaymentCardsList(mContext, cardInfoRes.getData(), fragmentPlaceMyOrder, card).getView());
                    isLoadCardList = false;
                    changePlaceOrderColor();
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                progressBarDialog.dismiss();
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

    public void getAddressList() {

        Log.w("FragmentPlaceMO","getAddressList()");
        progressBarDialog = new ProgressBarDialog(mContext);
        progressBarDialog.show();
        final AddressApi addressApi = new AddressApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                progressBarDialog.dismiss();
                if (object != null) {
                    AddressRes addressRes = (AddressRes) object;
                    addressListLayout.removeAllViews();
                    addressListLayout.addView(new ShippingAddressList(mContext, addressRes.getData(), fragmentPlaceMyOrder, address).getView());
                    isLoadAddressList = false;
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                progressBarDialog.dismiss();
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

    String itemCount = "1";
    private String apartment;

    // TODO: 10/24/2016 : this is added from extending googlewallet class the, class and overriden methods should be removed
    @Override
    protected Fragment getResultTargetFragment() {
        // TODO Auto-generated method stub
        return null;
    }





    private void updateCartInfo() {
        String blank = "";
        if (cartTotalInfo != null) {
            itemsCountText.setText(cartTotalInfo.getItem_count());
            totalItemsText.setText("Total (" + cartTotalInfo.getItem_count() + " items)");
            subtotalText.setText(cartTotalInfo.getSub_total().equals(blank) ? "$0.00" : "$" + CommonUtility.getFormatedNum(cartTotalInfo.getSub_total()));
            taxText.setText(cartTotalInfo.getEst_tax().equals(blank) ? "$0.00" : "$" + CommonUtility.getFormatedNum(cartTotalInfo.getEst_tax()));
            shippingText.setText(cartTotalInfo.getEst_shippping().equals(blank) ? "$0.00" : "$" + CommonUtility.getFormatedNum(cartTotalInfo.getEst_shippping()));
            handlingText.setText(cartTotalInfo.getHandling().equals(blank) ? "$0.00" : "$" + CommonUtility.getFormatedNum(cartTotalInfo.getHandling()));
            totalText.setText(cartTotalInfo.getTotal_price().equals(blank) ? "$0.00" : "$" + CommonUtility.getFormatedNum(cartTotalInfo.getTotal_price()));
        }
    }

    public void setAddress(Address address) {
        Log.w("FragmentPlaceMO","setAddress()");
        this.address = address;
//		for (int i = 0; i < cartProducts.size(); i++) {
        cardAndShippingDetail.setShipping_title(address.getTitle());
        cardAndShippingDetail.setShipping_first_name(address.getFirstname());
        cardAndShippingDetail.setShipping_last_name(address.getLastname());
        cardAndShippingDetail.setShipping_city(address.getCity_town());
        cardAndShippingDetail.setShipping_country(address.getCountry());
        cardAndShippingDetail.setShipping_address(address.getStreet_address() + " " + address.getApartment());
        cardAndShippingDetail.setShipping_zip(address.getZip_code());
        cardAndShippingDetail.setShipping_telephone(address.getTel());
        cardAndShippingDetail.setShipping_state(address.getState());
        cardAndShippingDetail.setBilling_title(address.getTitle());
        cardAndShippingDetail.setBilling_first_name(address.getFirstname());
        cardAndShippingDetail.setBilling_last_name(address.getLastname());
        cardAndShippingDetail.setBilling_city(address.getCity_town());
        cardAndShippingDetail.setBilling_country(address.getCountry());
        cardAndShippingDetail.setBilling_address(address.getStreet_address() + " " + address.getApartment());
        cardAndShippingDetail.setBilling_zip(address.getZip_code());
        cardAndShippingDetail.setBilling_telephone(address.getTel());
        cardAndShippingDetail.setBilling_state(address.getState());
        cardAndShippingDetail.setEmail(UserPreference.getInstance().getEmail());
//		}
        for (int i = 0; i < productList.size(); i++) {
            productList.get(i).setFreeShipping(false);
        }
        if (((HomeActivity) mContext).checkInternet())
            getEstimate();
    }

    public void setCard(Card card) {
        Log.w("FragmentPlaceMO","setCard()");
        if (card != null) {
            this.card = card;
//			for (int i = 0; i < cartProducts.size(); i++) {
            cardAndShippingDetail.setCard_number(CommonUtility.DecryptCreditCard(card.getCard_number()));
            cardAndShippingDetail.setCard_type(card.getCardtype());
            cardAndShippingDetail.setCard_name(card.getName_on_card());
            cardAndShippingDetail.setCvv(card.getCvv());
            cardAndShippingDetail.setExpiry_date_month(card.getExpiration_date().split("/")[0]);
            cardAndShippingDetail.setExpiry_date_year(card.getExpiration_date().split("/")[1]);
//			}
            validateAddressAndCard();
        }
        changePlaceOrderColor();
    }

    private void getEstimate() {
        Log.w("FragmentPlaceMO","getEstimate()");
        progressBarPlaceOrder.setProgress(0);
        loadingBar.setVisibility(View.VISIBLE);
        placemyOrderLayout.setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                int num = r.nextInt(24 - 1) + 1;
                progressBarPlaceOrder.setProgress(num);
            }
        }, 700);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                int num = r.nextInt(47 - 25) + 25;
                progressBarPlaceOrder.setProgress(num);
            }
        }, 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                int num = r.nextInt(70 - 48) + 48;
                progressBarPlaceOrder.setProgress(num);
            }
        }, 3000);

        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("successabc:   " + object);
                try {
                    subtotal = "0";
                    tax = "0";
                    shipping = "0";
                    finalprice = "0";
                    JSONObject jsonObject = new JSONObject(object.toString());
                    JSONObject estimates = jsonObject.getJSONObject("estimates");
                    Iterator keys = estimates.keys();

                    while (keys.hasNext()) {
                        // loop to get the dynamic key
                        String currentDynamicKey = (String) keys.next();
                        // get the value of the dynamic key
                        JSONObject currentDynamicValue = estimates.getJSONObject(currentDynamicKey);
                        // do something here with the value...
                        JSONObject prices = currentDynamicValue.getJSONObject("prices");
                        subtotal = Double.toString(Double.parseDouble(subtotal) + Double.parseDouble(prices.getString("subtotal").replace("$", "")));
                        shipping = Double.toString(Double.parseDouble(shipping) + Double.parseDouble(prices.getString("shipping_price").replace("$", "")));
                        tax = Double.toString(Double.parseDouble(tax) + Double.parseDouble(prices.getString("sales_tax").replace("$", "")));
                        finalprice = Double.toString(Double.parseDouble(finalprice) + Double.parseDouble(prices.getString("final_price").replace("$", "")));
                        for (int i = 0; i < productList.size(); i++) {
                            if (currentDynamicKey.equals(productList.get(i).getSiteId())) {
                                updateProductPrice(productList.get(i).getProductcart_id(), prices.getString("subtotal").replace("$", ""),
                                        prices.getString("shipping_price").replace("$", ""), prices.getString("sales_tax").replace("$", ""), productList.get(i).getMd5(), productList.get(i).getSiteId());
                                if (prices.getString("shipping_price").equals("$0.00"))
                                    productList.get(i).setFreeShipping(true);
                            }
                        }
                    }
                    UserPreference.getInstance().setFinalPrice(finalprice);
                    updateCartInfoViaTwoTap();
                    loadingBar.setVisibility(View.GONE);
                    placemyOrderLayout.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
//		twoTapApi.getCartEstimates(cartProducts, cartid);
        twoTapApi.getCartEstimates(cartList, cartid, cardAndShippingDetail);
        twoTapApi.execute();
    }

    private void updateProductPrice(String productCartId, String price, String shiping, String productTax, String md5, String siteId) {
        Log.w("FragmentPlaceMO","updateProductPrice()");
        CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {

            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        cartApi.updateproductPrice(UserPreference.getInstance().getUserID(), productCartId, price, shiping, productTax, md5, siteId);
        cartApi.execute();
    }

    public void callPurchase() {
        Log.w("FragmentPlaceMO","callPurchase()");
//		loadingBar.setVisibility(View.VISIBLE);
        dialog = new ProgressDialog(mContext);
        dialog.setMessage("Please wait");
        dialog.show();
        dialog.setCancelable(false);
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    Syso.info("result:  " + jsonObject);
                    if (jsonObject.has("purchase_id")) {
                        purchaseId = jsonObject.getString("purchase_id");
                        UserPreference.getInstance().setPurchaseId(purchaseId);
                        if (((HomeActivity) mContext).checkInternet())
                            callServerForConfirmation(purchaseId, "pending");
                        String msg = "We sent the order to the merchants, we will send you a follow up notice once your order status is confirmed.";
                        OrderProcessingDialog orderProcessingDialog = new OrderProcessingDialog(mContext, msg, fragmentPlaceMyOrder, purchaseId, cartid);
                        orderProcessingDialog.show();
                    } else {
                        dialog.dismiss();
                        CommonUtility.showAlert(mContext, jsonObject.getString("description"));
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    AlertUtils.showToast(mContext, "Failed to get status");
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				loadingBar.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
//		twoTapApi.purchaseApi2(cartid, productUrls);
        twoTapApi.purchaseApi2(cartid, cartList, cardAndShippingDetail);
//		twoTapApi.getMethod();
        twoTapApi.execute();
    }

    public void confirmPuchase(final String purchase_id) {
        Log.w("FragmentPlaceMO","confirmPurchase()");
//		if (dialog!=null && !dialog.isShowing()) {
        Syso.info("uuuuuuuuuuuuu >>>>> in confirmPuchase");
        if (isShowAlerts) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage("Processing & Confirming Your Order");
            dialog.show();
            dialog.setCancelable(false);
        }
//		}
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                Syso.info("uuuuuuuuuuuuu >>>>> in handleOnSuccess of confirmPuchase");
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    Syso.info("result:  " + jsonObject);
                    GCMAlarmReceiver.setAlarmForOrderNotification(purchase_id, mContext);
                    if (isShowAlerts ? ((HomeActivity) mContext).checkInternet() : true) {
                        callServerForConfirmation(purchase_id, "confirmed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        twoTapApi.confirmPurchase(purchase_id);
        twoTapApi.execute();
    }


    public void purchaseStatus(String purchase_id) {
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    purchaseId = jsonObject.getString("purchase_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        twoTapApi.purchaseStatus(purchase_id);
        twoTapApi.execute();
    }

    public void getFinalPrice(JSONObject jsonObject) {
        try {
            finalprice = "0";
            shipping = "0";
            tax = "0";
            JSONObject twotapreply = jsonObject.getJSONObject("twotapreply");
            JSONObject sites = twotapreply.getJSONObject("sites");
            Iterator keys = sites.keys();

            while (keys.hasNext()) {
                // loop to get the dynamic key
                String currentDynamicKey = (String) keys.next();
                // get the value of the dynamic key
                JSONObject currentDynamicValue = sites.getJSONObject(currentDynamicKey);
                // do something here with the value...
                JSONObject prices = currentDynamicValue.getJSONObject("prices");
                if (prices.has("shipping_price"))
                    shipping = Double.toString(StringUtils.getDoubleValue(shipping) + StringUtils.getDoubleValue(prices.getString("shipping_price").replace("$", "")));
                if (prices.has("sales_tax"))
                    tax = Double.toString(StringUtils.getDoubleValue(tax) + StringUtils.getDoubleValue(prices.getString("sales_tax").replace("$", "")));
                if (prices.has("final_price"))
                    finalprice = Double.toString(StringUtils.getDoubleValue(finalprice) + StringUtils.getDoubleValue(prices.getString("final_price").replace("$", "")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String finalvalue = "$" + finalprice;
//		String finalvalue = "Shipping Price: $"+shipping+"\n"+"Tax: $"+tax+"\n"+"Final Price: $"+finalprice+"\n";
        OrderReadyForConfirmDialog confirmDialog = new OrderReadyForConfirmDialog(mContext, finalvalue, fragmentPlaceMyOrder, purchaseId);
        confirmDialog.show();
    }

    public double getFinalPrice(String string) {
        Log.w("FragmentPlaceMO","getFinalPrice()");
        double price = 0;
        try {
            JSONObject jsonObject = new JSONObject(string);
            price = jsonObject.getDouble("finalprice");
            OrderReadyForConfirmDialog confirmDialog = new OrderReadyForConfirmDialog(mContext, "$" + price, fragmentPlaceMyOrder, purchaseId);
            confirmDialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return price;
    }

    public void callServerForConfirmation(final String purchase_id, final String status) {
        Log.w("FragmentPlaceMO","callServerForConfirmation()");
        UpdateCartApi twoTapApi = new UpdateCartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                Syso.info("uuuuuuuuuuuuu >>>>> in handleOnSuccess of callServerForConfirmation");
                if (dialog != null)
                    dialog.dismiss();
                if (status.equals("pending")) {
//					purchaseStatus(purchase_id);
                    changePlaceOrderColor();
                } else if (status.equals("confirmed")) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (isShowAlerts ? ((HomeActivity) mContext).checkInternet() : true) {
                        createCart();
                        UserPreference.getInstance().setCartCount("0");
                        try {
                            ((HomeActivity) mContext).refreshCartCount();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (isShowAlerts) {
                            ((HomeActivity) mContext).loadFragment(new FragmentDiscoverNew());
                        }
                        purchaseId = "";
                        UserPreference.getInstance().setPurchaseId("");
                        UserPreference.getInstance().setFinalPrice("");
                        UserPreference.getInstance().setIsNotificationClicked(false);
                        Syso.info("uuuuuuuuuuuuu >>>>> final done");
                    }
                    if (isShowAlerts)
                        CommonUtility.showAlert(mContext, mContext.getResources().getString(R.string.order_success_text));
                } else if (status.equals("cancel")) {
                    Syso.info("uuuuuuuuuuuuu >>>>> order canceled successfully");
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (isShowAlerts)
                    CommonUtility.showAlert(mContext, mContext.getResources().getString(R.string.order_fail_text));

                if (status.equals("confirmed")) {
                    if (isShowAlerts ? ((HomeActivity) mContext).checkInternet() : true) {
                        createCart();
                        UserPreference.getInstance().setCartCount("0");
                        if (isShowAlerts) {
                            ((HomeActivity) mContext).refreshCartCount();
                            ((HomeActivity) mContext).loadFragment(new FragmentDiscoverNew());
                        }
                        purchaseId = "";
                        UserPreference.getInstance().setPurchaseId("");
                        UserPreference.getInstance().setFinalPrice("");
                        UserPreference.getInstance().setIsNotificationClicked(false);
                        Syso.info("uuuuuuuuuuuuu >>>>> final done");
                    }

                }
            }
        });
        twoTapApi.updatecarttwotapstatus(UserPreference.getInstance().getUserID(), purchase_id, cartid, finalprice, status);
        twoTapApi.execute();
    }

    public void createCart() {
        Log.w("FragmentPlaceMO","createCart()");
//		progressBarDialog = new ProgressBarDialog(context);
//		progressBarDialog.show();
        final CartApi cartApi = new CartApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
//				progressBarDialog.dismiss();
                CartRes cartRes = (CartRes) object;
                UserPreference.getInstance().setCartID(cartRes.getCart_id());
//				loadFragment(new FragmentDiscover());
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {
//				progressBarDialog.dismiss();
            }
        });
        cartApi.createCart(UserPreference.getInstance().getUserID());
        cartApi.execute();

    }

    private void updateCartInfoViaTwoTap() {
        Log.w("FragmentPlaceMO","updateCartInfoViaTwoTap()");
        itemsCountText.setText("" + productList.size());
        totalItemsText.setText("Total (" + productList.size() + " items)");
//		if(cartTotalInfo!=null){
        subtotalText.setText("$" + CommonUtility.getFormatedNum(subtotal));
        taxText.setText("$" + CommonUtility.getFormatedNum(tax));
        shippingText.setText("$" + CommonUtility.getFormatedNum(shipping));
        totalText.setText("$" + CommonUtility.getFormatedNum(finalprice));
//		}
        handlingText.setText("$0.00");
        cartProductList.removeAllViews();
        cartProductList.addView(new CartProductsUI(mContext, productList).getView());
    }

    private void validateUserInput() {
        Log.w("FragmentPlaceMO","validateUserInput()");
        boolean isValid = true;
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();
        String cardHolderName = cardHolderNameEditText.getText().toString().trim();
        String expiryMonth = monthSpinner.getSelectedItem().toString();
        String expiryYear = yearSpinner.getSelectedItem().toString();
        String cardtype = Luhn.getCardTypeinString(cardNumber);
        b_firstname = b_firstNameEditText.getText().toString().trim();
        b_lastname = b_lastNameEditText.getText().toString().trim();
        b_streetname = b_streetNameEditText.getText().toString().trim();
        b_city = b_cityEditText.getText().toString().trim();
        b_zipcode = b_zipEditText.getText().toString().trim();
        b_country = b_countryEditText.getSelectedItem().toString();
        b_countryForLob = getCountryForLob();
        if (b_country.equalsIgnoreCase("United States of America")) {
            int pos = b_stateSpinnerUS.getSelectedItemPosition();
            b_state = getResources().getStringArray(R.array.statesUSACodes)[pos];
//            b_state = b_stateSpinnerUS.getSelectedItem().toString().trim();
        } else if (b_country.equalsIgnoreCase("Canada")) {
            int pos = b_stateSpinnerCanada.getSelectedItemPosition();
            b_state = getResources().getStringArray(R.array.statesCanadaCodes)[pos];
//            b_state = b_stateSpinnerCanada.getSelectedItem().toString().trim();
        }
        if (cardNumber.length() == 0) {
            isValid = false;
            cardNumberEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_cardnumber_blank);
        } else if (!Luhn.isCardValid(cardNumber)) {
            isValid = false;
            cardNumberEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_cardnumber_short);
        } else if (cardHolderName.length() == 0) {
            isValid = false;
            cardHolderNameEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_cardholdername_blank);
        } else if (cvv.length() < 3) {
            isValid = false;
            cvvEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_cvv_blank);
        } else if (expiryMonth.length() == 0 || expiryMonth.contains("MM")) {
            isValid = false;
            AlertUtils.showToast(mContext, R.string.alert_expirymonth_blank);
        } else if (expiryYear.length() == 0 || expiryYear.contains("YYYY")) {
            isValid = false;
            AlertUtils.showToast(mContext, R.string.alert_expiryyear_blank);
        } else if (TextUtils.isEmpty(cardtype)) {
            isValid = false;
            AlertUtils.showToast(mContext, R.string.alert_card_invalid_blank);
        } else if (b_firstname.length() == 0) {
            isValid = false;
            b_firstNameEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_firstname_blank);
        } else if (b_lastname.length() == 0) {
            isValid = false;
            b_lastNameEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_lastname_blank);
        } else if (b_streetname.length() == 0) {
            isValid = false;
            b_streetNameEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_streetname_blank);
        } else if (b_city.length() == 0) {
            isValid = false;
            b_cityEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_city_blank);
        } else if (b_state.length() == 0) {
            isValid = false;
            if (b_country.equalsIgnoreCase("United States of America"))
                b_stateSpinnerUS.requestFocus();
            else if (country.equalsIgnoreCase("Canada"))
                b_stateSpinnerCanada.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_state_blank);
        } else if (b_zipcode.length() == 0) {
            isValid = false;
            b_zipEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_zip_blank);
        } else if (b_country.length() == 0) {
            isValid = false;
            b_countryEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_countryname_blank);
        } else if (!checkValidZipCode(b_zipcode)) {
            isValid = false;
            b_zipEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_invalid_zipcode);
        }
        if (isValid && ((HomeActivity) mContext).checkInternet()) {
            CommonUtility.hideSoftKeyboard(mContext);
            String expiryDate = monthSpinner.getSelectedItem() + "/" + yearSpinner.getSelectedItem();
            Card card = new Card();
            card.setCard_number(cardNumber);
            card.setCardtype(cardtype);
            card.setName_on_card(cardHolderName);
            card.setCvv(cvv);
            card.setExpirationMonth(monthSpinner.getSelectedItem().toString());
            card.setExpirationYear(yearSpinner.getSelectedItem().toString());

            BillingAddress address = new BillingAddress();
            address.setFirstName(b_firstname);
            address.setLastName(b_lastname);
            address.setCity(b_city);
            address.setCountry_code(b_countryForLob);
            address.setLine1(b_streetname);
            address.setPostal_code(b_zipcode);
            address.setState(b_state);
            getAuthTocken();
            validateCard(card, address);
//            addCard(cardNumber, cardHolderName, expiryDate, cvv, cardtype);
        }

    }

    private void addCard(String cardNumber, String cardHolderName, String expiryDate, String cvv, String cardtype) {
        Log.w("FragmentPlaceMO","addCard()");
        if (progressBarDialog == null) {
            progressBarDialog = new ProgressBarDialog(mContext);
            progressBarDialog.show();
        }
        final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (progressBarDialog != null)
                    progressBarDialog.dismiss();
                if (object != null) {
                    CardInfoRes cardInfoRes = (CardInfoRes) object;
                    AlertUtils.showToast(mContext, cardInfoRes.getMessage());
                    getCardList();
                    addNewCardLayout.setVisibility(View.GONE);
                    resetValues();
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (progressBarDialog != null)
                    progressBarDialog.dismiss();
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

    private void resetValues() {
        Log.w("FragmentPlaceMO","resetValues()");
        cardNumberEditText.setText("");
        cardHolderNameEditText.setText("");
        cvvEditText.setText("");
        monthSpinner.setSelection(0);
        yearSpinner.setSelection(0);
        b_streetNameEditText.setText("");
        b_cityEditText.setText("");
        b_zipEditText.setText("");
        b_firstNameEditText.setText("");
        b_lastNameEditText.setText("");
        b_countryEditText.setSelection(0);
        b_stateSpinnerUS.setSelection(0);
        b_stateSpinnerCanada.setSelection(0);
    }

    private void validateUserInputAddress() {
        Log.w("FragmentPlaceMO","validateUserInputAddress()");
        boolean isValid = true;
        title = titleSpinner.getSelectedItem().toString();
        firstname = firstNameEditText.getText().toString().trim();
        lastname = lastNameEditText.getText().toString().trim();
        streetname = streetNameEditText.getText().toString().trim();
        city = cityEditText.getText().toString().trim();
        zipcode = zipEditText.getText().toString().trim();
        country = countryEditText.getSelectedItem().toString();
        countryForLob = getCountryForLob();
        if (country.equalsIgnoreCase("United States of America"))
            state = stateSpinnerUS.getSelectedItem().toString().trim();
        else if (country.equalsIgnoreCase("Canada"))
            state = stateSpinnerCanada.getSelectedItem().toString().trim();
        telephone = telephoneEditText.getText().toString().trim();
        apartment = apartmentEditText.getText().toString().trim();
        if (TextUtils.isEmpty(apartment)) {
            apartment = "";
        }
        if (firstname.length() == 0) {
            isValid = false;
            firstNameEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_firstname_blank);
        } else if (lastname.length() == 0) {
            isValid = false;
            lastNameEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_lastname_blank);
        } else if (streetname.length() == 0) {
            isValid = false;
            streetNameEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_streetname_blank);
        } else if (city.length() == 0) {
            isValid = false;
            cityEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_city_blank);
        } else if (state.length() == 0) {
            isValid = false;
            if (country.equalsIgnoreCase("United States of America"))
                stateSpinnerUS.requestFocus();
            else if (country.equalsIgnoreCase("Canada"))
                stateSpinnerCanada.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_state_blank);
        } else if (zipcode.length() == 0) {
            isValid = false;
            zipEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_zip_blank);
        } else if (country.length() == 0) {
            isValid = false;
            countryEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_countryname_blank);
        } else if (!checkValidZipCode(zipcode)) {
            isValid = false;
            zipEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_invalid_zipcode);
        } else if (telephone.length() == 0) {
            isValid = false;
            telephoneEditText.requestFocus();
            AlertUtils.showToast(mContext, R.string.alert_telephone_blank);
        }
        if (isValid && ((HomeActivity) mContext).checkInternet()) {
            CommonUtility.hideSoftKeyboard(mContext);
            validateAddressviaLOB();
        }
    }

    private String getCountryForLob() {
        if (countryEditText.getSelectedItemPosition() == 0) {
            countryForLob = "US";
        } else if (countryEditText.getSelectedItemPosition() == 1) {
            countryForLob = "CA";
        }
        return countryForLob;
    }

    private void validateAddressviaLOB() {
        Log.w("FragmentPlaceMO","validateAddressviaLOB()");

        new AsyncLOB().execute();


        /*

        progressBarDialog = new ProgressBarDialog(mContext);
        progressBarDialog.show();

        final LOBApi addressApi = new LOBApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Log.w("LOBApi","handleOnSuccess");
                progressBarDialog.dismiss();
                if (object != null) {
                    try {
                        JSONObject addressRes = new JSONObject(object.toString());
                        Log.e("addressre", addressRes.toString());
                        if (addressRes.toString().contains("The address you entered was found but more information is needed (such as an apartment, suite, or box number) to match to a specific address")) {
                            AddressVerificationDialog verificationDialog = new AddressVerificationDialog(mContext, "More information is needed(such as an apartment, suite, or box number)");
                            verificationDialog.show();
                            //AlertUtils.showToast(mContext, "More information is needed(such as an apartment, suite, or box number)");
                        } else if (addressRes.has("address")) {
                            sendDataToServer();
                        } else if (addressRes.has("error")) {
                            String msg = addressRes.getJSONObject("error").getString("message");
                            AddressVerificationDialog verificationDialog = new AddressVerificationDialog(mContext, msg);
                            verificationDialog.show();
                            //AlertUtils.showToast(mContext, msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Log.w("LOBApi","handleOnFailure");
                progressBarDialog.dismiss();
                Log.w("LOBAPI","handleOnFailure()");
                AddressVerificationDialog verificationDialog = new AddressVerificationDialog(mContext, "Address not found or seems to be invalid");
                verificationDialog.show();
                //AlertUtils.showToast(mContext, "Address not found or seems to be invalid");
            }
        });
        addressApi.validateAddress(streetname, apartment, state, city, zipcode, countryForLob);
        addressApi.execute();

        progressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                addressApi.cancel();
            }
        });
    */
    }


    private class AsyncLOB extends AsyncTask<String, String, String> {

        String d = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                Lob.setApiVersion("2017-06-16");
                final LobClient client = AsyncLobClient.createDefault("live_bf92e586ac52066021d03ab6ade48ce5227");
                //Log.w("Verifiying Address: ", streetname+""+city+""+state+"+"+zipcode);
                final USVerificationResponse addressResponse = client.verifyUSAddress(USVerificationRequest.builder()
                        .primary_line(streetname)
                        .secondary_line(apartment)
                        .city(city)
                        .state(state)
                        .zipCode(zipcode)
                        .build()).get();
                //Log.w("Verified Address: ", ""+addressResponse.getLastLine());
                //Log.w("Verified Address: ", ""+addressResponse.getDeliverability());
                //Log.w("Verified Address: ", ""+addressResponse.getComponents().toString());
                d = addressResponse.getDeliverability();

            }catch(Exception e) {
                e.printStackTrace();
            }
           return d;
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("no_match")){
                AlertUtils.showToast(mContext, "Invalid Address.");
            } else if(result.equals("deliverable_missing_secondary")){
                AlertUtils.showToast(mContext, "Missing Apt/Unit Number.");
            } else if(result.equals("undeliverable")){
                AlertUtils.showToast(mContext, "Address is currently undeliverable.");
            } else if(result.equals("deliverable_extra_secondary")){
                AlertUtils.showToast(mContext, "Incorrect or unnecessary UNIT number");
            } else {
                sendDataToServer();
            }

        }
    }


    private boolean checkValidZipCode(String zipcode) {
        Log.w("FragmentPlaceMO","checkValidZipCode()");
        if (countryEditText.getSelectedItemPosition() == 0) {
            if (ValidZipcode.isValidPostalUSCode(zipcode)) {
                return true;
            }
        } else if (countryEditText.getSelectedItemPosition() == 1) {
            if (ValidZipcode.isValidPostalCanadaCode(zipcode)) {
                return true;
            }
        }
        return false;
    }

    private void sendDataToServer() {
        Log.w("FragmentPlaceMO","sentDataToServer()");
        progressBarDialog = new ProgressBarDialog(mContext);
        progressBarDialog.show();

        final AddressApi addressApi = new AddressApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                progressBarDialog.dismiss();
                if (object != null) {
                    AddressRes addressRes = (AddressRes) object;
                    AlertUtils.showToast(mContext, addressRes.getMessage());
                    getAddressList();
                    addNewAddressLayout.setVisibility(View.GONE);
                    resetAddressValues();
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                progressBarDialog.dismiss();
                if (object != null) {
                    AddressRes response = (AddressRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        addressApi.addAddress(UserPreference.getInstance().getUserID(), title,
                firstname, lastname, state, telephone, streetname, city, zipcode, country, apartment);
        addressApi.execute();

        progressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                addressApi.cancel();
            }
        });
    }


    protected void resetAddressValues() {
        Log.w("FragmentPlaceMO","resetAddressValues()");
        streetNameEditText.setText("");
        cityEditText.setText("");
        zipEditText.setText("");
        firstNameEditText.setText("");
        lastNameEditText.setText("");
        telephoneEditText.setText("");
        titleSpinner.setSelection(0);
        countryEditText.setSelection(0);
        stateSpinnerUS.setSelection(0);
        stateSpinnerCanada.setSelection(0);
    }

    private void getCartId() {
        progressBarDialog = new ProgressBarDialog(mContext);
        progressBarDialog.show();
        loadingBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isAddressSelected && !isCardSelected) {
                    shippingAddressLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
                    addPaymentMethodLayout.setBackgroundColor(mContext.getResources().getColor(R.color.btn_green));
                }
            }
        }, 500);
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   " + object);
                try {

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Random r = new Random();
                            int num = r.nextInt(33 - 1) + 1;
                            progressBarPlaceOrder.setProgress(num);
                        }
                    }, 1000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Random r = new Random();
                            int num = r.nextInt(67 - 34) + 34;
                            progressBarPlaceOrder.setProgress(num);
                        }
                    }, 2000);

                    JSONObject jsonObject = new JSONObject(object.toString());
                    cartid = (String) jsonObject.get("cart_id");
                    runnable = new Runnable() {

                        @Override
                        public void run() {
                            if (i <= 1) {
                                getStatus(cartid);
                            }

                        }
                    };
                    handler.postDelayed(runnable, 3000);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        List<String> products = new ArrayList<String>();
        for (int i = 0; i < productList.size(); i++) {
            products.add(productList.get(i).getProducturl());
        }
        twoTapApi.getCartId(products);
        twoTapApi.execute();
    }

    private void getStatus(String cart_id) {
        Log.w("FragmentPlaceMO","getStatus()");
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                progressBarDialog.dismiss();
                Syso.info("success:   " + object);
                getData(object);
                SelectCardAndAddressDialog cardAndAddressDialog = new SelectCardAndAddressDialog(mContext);
                cardAndAddressDialog.show();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                progressBarDialog.dismiss();
            }
        });
        twoTapApi.getCartStatus(cart_id);
        twoTapApi.execute();
    }

    protected void getData(Object object) {
        Log.w("FragmentPlaceMO","getData()");
        try {
            JSONObject jsonObject = new JSONObject(object.toString());
            JSONObject sites = jsonObject.getJSONObject("sites");
            Iterator keys = sites.keys();

            while (keys.hasNext()) {
                String siteId = "";
                List<String> fitList = new ArrayList<String>();
                List<String> optionList = new ArrayList<String>();
                List<String> colorsList = new ArrayList<String>();
                List<String> affiliateUrlList = new ArrayList<String>();
                List<String> md5 = new ArrayList<String>();
                List<String> sizes = new ArrayList<String>();
                List<String> quantities = new ArrayList<String>();
                List<JSONObject> md5Value = new ArrayList<JSONObject>();
                CartProduct cartProduct = new CartProduct();
                // loop to get the dynamic key
                String currentDynamicKey = (String) keys.next();
                siteId = currentDynamicKey;
                cartProduct.setSite_id(currentDynamicKey);
                // get the value of the dynamic key
                JSONObject currentDynamicValue = sites.getJSONObject(currentDynamicKey);

                // do something here with the value...
                JSONObject add_to_cart = currentDynamicValue.getJSONObject("add_to_cart");
                Syso.info("add_to_cart   " + add_to_cart);
                Iterator data = add_to_cart.keys();

                while (data.hasNext()) {
                    // loop to get the dynamic key
                    String currentKey = (String) data.next();
                    Syso.info("currentKey   " + currentKey);
                    md5.add(currentKey);
                    // get the value of the dynamic key
                    JSONObject value = add_to_cart.getJSONObject(currentKey);
                    // do something here with the value...
                    Syso.info("value   " + value);
                    md5Value.add(value);
                    productUrls.add(value.getString("url"));
                }
                if (md5.size() == 1) {
                    cartProduct.setPRODUCT_MD5(md5.get(0));
                    for (int i = 0; i < productList.size(); i++) {
                        if (md5Value.get(0).getString("original_url").equals(productList.get(i).getProducturl())) {
                            productList.get(i).setSiteId(siteId);
                            if (!TextUtils.isEmpty(productList.get(i).getSize())) {
                                cartProduct.setSize(productList.get(i).getSize());
                            } else
                                cartProduct.setSize(productList.get(i).getSelected_size());
                            if (!TextUtils.isEmpty(productList.get(i).getColor())) {
                                cartProduct.setColor(productList.get(i).getColor());
                            } else
                                cartProduct.setColor(productList.get(i).getSelected_color());
                            if (!TextUtils.isEmpty(productList.get(i).getOption())) {
                                cartProduct.setOption(productList.get(i).getOption());
                            } else
                                cartProduct.setOption("");
                            if (!TextUtils.isEmpty(productList.get(i).getFit())) {
                                cartProduct.setFit(productList.get(i).getFit());
                            } else
                                cartProduct.setFit("");
                            if (!TextUtils.isEmpty(productList.get(i).getQuantity())) {
                                cartProduct.setQuantity(productList.get(i).getQuantity());
                            }
                            cartProduct.setAffiliateUrl(productList.get(i).getAffiliateurl());
                            cartProduct.setShipping_option(productList.get(i).getShipping_option());
                        }
                    }
                } else {
                    cartProduct.setMd5(md5);
                    for (int i = 0; i < productList.size(); i++) {
                        for (int j = 0; j < md5Value.size(); j++) {
                            if (md5Value.get(j).getString("original_url").equals(productList.get(i).getProducturl())) {
                                productList.get(i).setSiteId(siteId);
                                if (!TextUtils.isEmpty(productList.get(i).getSize())) {
                                    sizes.add(productList.get(i).getSize());
                                } else {
                                    sizes.add(productList.get(i).getSelected_size());
                                }
                                if (!TextUtils.isEmpty(productList.get(i).getColor())) {
                                    colorsList.add(productList.get(i).getColor());
                                } else
                                    colorsList.add(productList.get(i).getSelected_color());
                                if (!TextUtils.isEmpty(productList.get(i).getOption())) {
                                    optionList.add(productList.get(i).getOption());
                                } else
                                    optionList.add("");
                                if (!TextUtils.isEmpty(productList.get(i).getFit())) {
                                    fitList.add(productList.get(i).getFit());
                                } else
                                    fitList.add("");
                                if (!TextUtils.isEmpty(productList.get(i).getQuantity()))
                                    quantities.add(productList.get(i).getQuantity());
                                affiliateUrlList.add(productList.get(i).getAffiliateurl());
                            }
                        }
                    }
                    cartProduct.setSizes(sizes);
                    cartProduct.setFitList(fitList);
                    cartProduct.setColorsList(colorsList);
                    cartProduct.setQuantities(quantities);
                    cartProduct.setAffiliateUrlList(affiliateUrlList);
                    cartProduct.setOptionList(optionList);
                    cartProduct.setShipping_option(productList.get(i).getShipping_option());
                }
                cartProducts.add(cartProduct);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadingBar.setVisibility(View.GONE);
        placemyOrderLayout.setVisibility(View.VISIBLE);
    }

    //    ======================================= validate ===============================================
    public void getAuthTocken() {
        Log.w("FragmentPlaceMO","getAuthToken()");
        progressBarDialog = new ProgressBarDialog(mContext);
        progressBarDialog.show();
        boolean condition1 = UserPreference.getInstance().getAuthTimeStamp() == 0;
        boolean condition2 = System.currentTimeMillis() > (UserPreference.getInstance().getAuthTimeStamp() + UserPreference.getInstance().getAuthExpireTime());
        if (condition1 || condition2) {
            if (checkInternet()) {
                OAuthWebService authWebService = new OAuthWebService(Constants.WebConstants.PAYPAL_AUTH_URL, new ServiceCallback() {
                    @Override
                    public void handleOnSuccess(Object object) {
                        Syso.info("1234567890 in handleOnSuccess>>" + object);
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
                        Syso.info("1234567890 in handleOnFailure>>" + object);
                        if (object != null)
                            AlertUtils.showToast(mContext, object.toString());
                    }
                });
                authWebService.execute();
            }
        }
    }

    public void validateCard(final Card card, BillingAddress address) {
        Log.w("FragmentPlaceMO","validateCard()");
        if (progressBarDialog == null) {
            progressBarDialog = new ProgressBarDialog(mContext);
            progressBarDialog.show();
        }
        if (checkInternet()) {
            OAuthWebService authWebService = new OAuthWebService(Constants.WebConstants.PAYPAL_PAYMENT_URL, new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    Syso.info("1234567890 in handleOnSuccess>>" + object);
                    try {
                        JSONObject jsonObj = new JSONObject(object.toString());
                        if (jsonObj.optString("state").equalsIgnoreCase("approved")) {
                            JSONObject jsonObj2 = jsonObj.getJSONArray("transactions").getJSONObject(0);
                            String id = jsonObj2.getJSONArray("related_resources").getJSONObject(0).getJSONObject("authorization").optString("id");
                            Syso.info("12345678 authorization id >>>" + id);
                            addCard(card.getCard_number(), card.getName_on_card(), card.getExpirationMonth() + "/" + card.getExpirationYear(), card.getCvv(), card.getCardtype());
                            voidAuthorization(id);
                        } else {
                            if (progressBarDialog != null)
                                progressBarDialog.dismiss();
                            AlertUtils.showToast(mContext, "Please enter valid card");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    Syso.info("1234567890 in handleOnFailure>>" + object);
                    if (progressBarDialog != null)
                        progressBarDialog.dismiss();

                    if (object != null)
                        AlertUtils.showToast(mContext, object.toString());
                    else
                        AlertUtils.showToast(mContext, "Please enter valid card");
                }
            });
            authWebService.setRequest(card, address);
            authWebService.execute();
        }
    }

    public void voidAuthorization(String authId) {
        Log.w("FragmentPlaceMO","voidAuthorization()");
        if (checkInternet2()) {
            OAuthWebService authWebService = new OAuthWebService(Constants.WebConstants.PAYPAL_VOID_URL + authId + "/void", new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    Syso.info("1234567890 in handleOnSuccess>>" + object);
                    try {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {
                    Syso.info("1234567890 in handleOnFailure>>" + object);
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
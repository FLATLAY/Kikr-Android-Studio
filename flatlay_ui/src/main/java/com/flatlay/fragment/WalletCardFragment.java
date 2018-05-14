package com.flatlay.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.OrderProcessingDialog;
import com.flatlay.utility.CardType;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.Luhn;
import com.flatlaylib.api.CardInfoApi;
import com.flatlaylib.api.KikrCreditsApi;
import com.flatlaylib.bean.Card;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CardInfoRes;
import com.flatlaylib.service.res.KikrCreditsRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RachelDi on 4/30/18.
 */

public class WalletCardFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private TextView new_payment_text, balance_text, redeem_text, contact_button, new_payment_text2;
    private LinearLayout cards_scroll2, flatlay_card2, cards_list_layout;
    private ImageView flatlay_card;
    private RelativeLayout overflow_layout9, overflow_layout9_2;
    private ScrollView cards_list_view;
    private String kikrCredit = "0";
    protected RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    protected RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    private String kikrCredit100String = "";
    private double kikrCreditOriginal = 0, kikrCredit100 = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.wallet_card_layout, null);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.new_payment_text:

                ((HomeActivity) mContext).myAddFragment(new WalletNewCardFragment());
                break;
            case R.id.flatlay_card:
                flatlay_card.setVisibility(View.GONE);
                flatlay_card2.setVisibility(View.VISIBLE);
                break;
            case R.id.flatlay_card2:
                flatlay_card.setVisibility(View.VISIBLE);
                flatlay_card2.setVisibility(View.GONE);
                break;
            case R.id.new_payment_text2:
                ((HomeActivity) mContext).myAddFragment(new WalletNewCardFragment());
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        new_payment_text = (TextView) mainView.findViewById(R.id.new_payment_text);
        new_payment_text.setTypeface(FontUtility.setMontserratLight(mContext));
        cards_scroll2 = (LinearLayout) mainView.findViewById(R.id.cards_scroll2);
        flatlay_card = (ImageView) mainView.findViewById(R.id.flatlay_card);
        flatlay_card2 = (LinearLayout) mainView.findViewById(R.id.flatlay_card2);
        balance_text = (TextView) mainView.findViewById(R.id.balance_text);
        balance_text.setTypeface(FontUtility.setMontserratLight(mContext));
        redeem_text = (TextView) mainView.findViewById(R.id.redeem_text);
        redeem_text.setTypeface(FontUtility.setMontserratLight(mContext));
        contact_button = (TextView) mainView.findViewById(R.id.contact_button);
        contact_button.setTypeface(FontUtility.setMontserratLight(mContext));
        overflow_layout9 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout9);
        overflow_layout9_2 = (RelativeLayout) mainView.findViewById(R.id.overflow_layout9_2);
        new_payment_text2 = (TextView) mainView.findViewById(R.id.new_payment_text2);
        new_payment_text2.setTypeface(FontUtility.setMontserratLight(mContext));
        cards_list_view = (ScrollView) mainView.findViewById(R.id.cards_list_view);
        cards_list_layout = (LinearLayout) mainView.findViewById(R.id.cards_list_layout);
        getCardList();
        getKikrCredits();
        overflow_layout9.setVisibility(View.VISIBLE);
        overflow_layout9_2.setVisibility(View.GONE);

    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        new_payment_text.setOnClickListener(this);
        flatlay_card.setOnClickListener(this);
        flatlay_card2.setOnClickListener(this);
        new_payment_text2.setOnClickListener(this);

    }

    public void getCardList() {
        flatlay_card.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (CommonUtility.getDeviceWidth(mContext)) * 51 / 100));
        final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    CardInfoRes cardInfoRes = (CardInfoRes) object;
                    List<Card> temp = new ArrayList<>();
                    temp = cardInfoRes.getData();
                    setCardList(temp);
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

    protected void setCardList(final List<Card> data) {
        cards_list_layout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        flatlay_card2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (CommonUtility.getDeviceWidth(mContext)) * 51 / 100));
        RelativeLayout layout3 = (RelativeLayout) inflater.inflate(R.layout.layout_card_detail, null);
        ((TextView) layout3.findViewById(R.id.edit_text)).setTypeface(FontUtility.setMontserratLight(mContext));
        ((TextView) layout3.findViewById(R.id.detail1)).setTypeface(FontUtility.setMontserratLight(mContext));
        ((TextView) layout3.findViewById(R.id.detail1)).setText("FLATLAY Card");
        ((TextView) layout3.findViewById(R.id.detail3)).setTypeface(FontUtility.setMontserratLight(mContext));
        if (kikrCredit.length() <= 5)
            ((TextView) layout3.findViewById(R.id.detail3)).setText(kikrCredit + " credits = $ " + kikrCredit100String + "\n");
        else
            ((TextView) layout3.findViewById(R.id.detail3)).setText(kikrCredit + " credits\n=\n$ " + kikrCredit100String + "\n");

        ((TextView) layout3.findViewById(R.id.bottom_text)).setTypeface(FontUtility.setMontserratLight(mContext));
        ((TextView) layout3.findViewById(R.id.bottom_text)).setText("Set as default payment method");
        ((ImageView) layout3.findViewById(R.id.card_image)).setImageResource(R.drawable.card1_small);
        cards_list_layout.addView(layout3, params2);
        params2.setMargins(0, 40, 0, 0);
        for (int i = 0; i < data.size(); i++) {
            RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.layout_wallet_card, null);
            ImageView cardImageView = (ImageView) layout.findViewById(R.id.cardImageView);
            setCardImage(data.get(i).getCard_number(), cardImageView);
            cardImageView.setTag(i);
            RelativeLayout layout2 = (RelativeLayout) inflater.inflate(R.layout.layout_card_detail, null);
            ((TextView) layout2.findViewById(R.id.bottom_text)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout2.findViewById(R.id.detail3)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout2.findViewById(R.id.detail2)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout2.findViewById(R.id.detail1)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout2.findViewById(R.id.edit_text)).setTypeface(FontUtility.setMontserratLight(mContext));
            ((TextView) layout2.findViewById(R.id.detail2)).setText(data.get(i).getName_on_card());
            String[] arr = data.get(i).getExpiration_date().split("/");
            if (arr.length >= 2) {
                ((TextView) layout2.findViewById(R.id.detail3)).setText(arr[0] + " / " + arr[1] + "\n");
            }
            ((TextView) layout2.findViewById(R.id.bottom_text)).setText("Set as default payment method");
            setCardImage2(data.get(i).getCard_number(), (ImageView) layout2.findViewById(R.id.card_image), (TextView) layout2.findViewById(R.id.detail1), (TextView) layout2.findViewById(R.id.detail2), (TextView) layout2.findViewById(R.id.detail3));
            cards_scroll2.addView(layout, params);
            params2.setMargins(0, 40, 0, 0);
            cards_list_layout.addView(layout2, params2);
            final String card_id = data.get(i).getCard_id();
            final View childView1 = layout;
            final View childView2 = layout2;
            layout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = "Are you sure you want ot delete this card?";
                    OrderProcessingDialog orderProcessingDialog = new OrderProcessingDialog(mContext, msg, new OrderProcessingDialog.MyListener() {
                        @Override
                        public void onClickButton() {
                            deleteCard(card_id, childView1, childView2);
                        }
                    });
                    orderProcessingDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                    orderProcessingDialog.show();
                }
            });
            cardImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    overflow_layout9.setVisibility(View.GONE);
                    overflow_layout9_2.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void deleteCard(String cardId, final View view1, final View view2) {

        final CardInfoApi service = new CardInfoApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    cards_scroll2.removeView(view1);
                    cards_list_layout.removeView(view2);
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    CardInfoRes response = (CardInfoRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }

            }
        });
        service.removeCard(UserPreference.getInstance().getUserID(), cardId);
        service.execute();
    }

    private void getKikrCredits() {
        final KikrCreditsApi creditsApi = new KikrCreditsApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                final KikrCreditsRes kikrCreditsRes = (KikrCreditsRes) object;
                if (kikrCreditsRes != null) {
                    kikrCreditOriginal = StringUtils.getDoubleValue(kikrCreditsRes.getAmount());
                    kikrCredit = new DecimalFormat("#,##0").format(new Double(kikrCreditOriginal));
                    kikrCredit100 = kikrCreditOriginal / 100;
                    kikrCredit100String = new DecimalFormat("#,###0.00").format(new Double(kikrCredit100));
                    balance_text.setText("Current Balance: " + kikrCredit + " credits");
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    KikrCreditsRes response = (KikrCreditsRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        creditsApi.getKikrCredits(UserPreference.getInstance().getUserID());
        creditsApi.execute();
    }

    private void setCardImage(String card_number1, ImageView imageView) {
        String card_number = CommonUtility.DecryptCreditCard(card_number1);
        if (card_number1.equals("Add")) {
            return;
        } else if (card_number1.equals("paypal")) {
            imageView.setImageResource(R.drawable.paypal_1);
        } else if (!card_number1.equals("flatlay") && Luhn.isCardValid(card_number)) {
            CardType cardtype = Luhn.getCardType(card_number);
            if (cardtype == null) {
                imageView.setImageResource(R.drawable.ic_connect_card_unknown);
            } else if (cardtype.equals(CardType.VISA)) {
                imageView.setImageResource(R.drawable.ic_connect_card_visa);
            } else if (cardtype.equals(CardType.AMERICAN_EXPRESS)) {
                imageView.setImageResource(R.drawable.ic_connect_card_american);
            } else if (cardtype.equals(CardType.MASTER_CARD)) {
                imageView.setImageResource(R.drawable.ic_connect_card_master);
            } else if (cardtype.equals(CardType.DISCOVER)) {
                imageView.setImageResource(R.drawable.ic_connect_card_discover);
            }
        } else
            imageView.setImageResource(R.drawable.ic_connect_card_unknown);
    }

    private void setCardImage2(String card_number1, ImageView imageView, TextView detail1, TextView detail2, TextView detail3) {
        String card_number = CommonUtility.DecryptCreditCard(card_number1);
        if (card_number1.equals("Add")) {
            return;
        } else if (card_number1.equals("paypal")) {
            imageView.setImageResource(R.drawable.paypal_small);
        } else if (card_number1.equals("flatlay")) {
            imageView.setImageResource(R.drawable.card1_small);
        } else if (Luhn.isCardValid(card_number)) {
            CardType cardtype = Luhn.getCardType(card_number);
            if (cardtype == null) {
                imageView.setImageResource(R.drawable.ic_connect_card_unknown);
            } else if (cardtype.equals(CardType.VISA)) {
                detail1.setText("VISA" + " (" + card_number.substring(card_number.length() - 4, card_number.length()) + ") ");
                imageView.setImageResource(R.drawable.visa_small);
            } else if (cardtype.equals(CardType.AMERICAN_EXPRESS)) {
                detail1.setText("American Express" + " (" + card_number.substring(card_number.length() - 4, card_number.length()) + ") ");
                imageView.setImageResource(R.drawable.ic_card_american);
            } else if (cardtype.equals(CardType.MASTER_CARD)) {
                detail1.setText("Mastercard" + " (" + card_number.substring(card_number.length() - 4, card_number.length()) + ") ");
                imageView.setImageResource(R.drawable.mastercard_small);
            } else if (cardtype.equals(CardType.DISCOVER)) {
                detail1.setText("Discover" + " (" + card_number.substring(card_number.length() - 4, card_number.length()) + ") ");
                imageView.setImageResource(R.drawable.ic_card_discover);
            }
        } else
            imageView.setImageResource(R.drawable.ic_connect_card_unknown);
    }

}

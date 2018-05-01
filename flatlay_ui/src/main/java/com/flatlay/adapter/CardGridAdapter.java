package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.utility.CardType;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.Luhn;
import com.flatlaylib.bean.Card;

import java.util.List;

/**
 * Created by RachelDi on 4/5/18.
 */

public class CardGridAdapter extends BaseAdapter {

    private FragmentActivity mContext;
    private List<Card> data;
    private LayoutInflater mInflater;
    private CardGridListener listener;


    public CardGridAdapter(FragmentActivity context, List<Card> data,CardGridListener listener) {
        super();
        this.mContext = context;
        this.data = data;
        this.listener=listener;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Card> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Card getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_card_grid, null);
            viewholder = new ViewHolder();
            viewholder.cardImage = (ImageView) convertView.findViewById(R.id.cardImage);
            viewholder.card_num_text = (TextView) convertView.findViewById(R.id.card_num_text);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.card_num_text.setTypeface(FontUtility.setMontserratLight(mContext));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (CommonUtility.getDeviceWidth(mContext)) / 10);
//        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (CommonUtility.getDeviceWidth(mContext)) / 10);
        layoutParams.gravity= Gravity.CENTER;
        layoutParams.setMargins(0,15,0,0);
//        layoutParams2.gravity= Gravity.CENTER;
//        layoutParams2.setMargins(0,15,0,0);
        if (data.size() > position && !getItem(position).getCard_number().equals("Add")
                && !getItem(position).getCard_number().equals("paypal")
                && !getItem(position).getCard_number().equals("flatlay")) {
            viewholder.cardImage.setLayoutParams(layoutParams);
            viewholder.cardImage.setVisibility(View.VISIBLE);
            viewholder.card_num_text.setVisibility(View.VISIBLE);
            setImage(viewholder.cardImage,getItem(position).getCard_number(),viewholder.card_num_text);
        }else if(data.size() > position && getItem(position).getCard_number().equals("paypal")){
            viewholder.cardImage.setLayoutParams(layoutParams);
            viewholder.cardImage.setVisibility(View.VISIBLE);
            viewholder.card_num_text.setVisibility(View.INVISIBLE);
            viewholder.cardImage.setImageResource(R.drawable.paypal_1);
        }else if(data.size() > position && getItem(position).getCard_number().equals("flatlay")){
            viewholder.cardImage.setLayoutParams(layoutParams);
            viewholder.cardImage.setVisibility(View.VISIBLE);
            viewholder.card_num_text.setVisibility(View.INVISIBLE);
            viewholder.cardImage.setImageResource(R.drawable.card1_small);
        }else if(data.size() > position && getItem(position).getCard_number().equals("Add")){
            viewholder.cardImage.setLayoutParams(layoutParams);
            viewholder.cardImage.setVisibility(View.GONE);
            viewholder.card_num_text.setText("Add\nCard");
            viewholder.card_num_text.setPadding(5,5,5,5);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickButton(position);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private ImageView cardImage;
        //        private LinearLayout follow_layout;
        private TextView card_num_text;
    }

    private void setImage(ImageView view, String card_number1, TextView text){
        String card_number = CommonUtility.DecryptCreditCard(card_number1);
        if (Luhn.isCardValid(card_number)) {
            CardType cardtype = Luhn.getCardType(card_number);
            if (cardtype == null) {
                view.setImageResource(R.drawable.ic_connect_card_unknown);
            } else if (cardtype.equals(CardType.VISA)) {
                text.setText(card_number.substring(card_number.length() - 4, card_number.length()));
                view.setImageResource(R.drawable.visa_small);
            } else if (cardtype.equals(CardType.AMERICAN_EXPRESS)) {
                text.setText(card_number.substring(card_number.length() - 4, card_number.length()));
                view.setImageResource(R.drawable.ic_card_american);
            } else if (cardtype.equals(CardType.MASTER_CARD)) {
                text.setText(card_number.substring(card_number.length() - 4, card_number.length()));
                view.setImageResource(R.drawable.mastercard_small);
            } else if (cardtype.equals(CardType.DISCOVER)) {
                text.setText(card_number.substring(card_number.length() - 4, card_number.length()));
                view.setImageResource(R.drawable.ic_card_discover);
            }
        } else
            view.setImageResource(R.drawable.ic_connect_card_unknown);
    }

    public interface CardGridListener { // create an interface

        void onClickButton(int position);
    }

}



package com.kikr.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.fragment.FragmentPlaceMyOrder;
import com.kikr.utility.CardType;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.Luhn;
import com.kikrlib.bean.Card;

public class PaymentCardsList {
	private FragmentActivity mContext;
	private List<Card> data;
	private LayoutInflater mInflater;
	private List<CheckBox> checkBoxs=new ArrayList<CheckBox>();
	private FragmentPlaceMyOrder fragmentPlaceMyOrder;
	private Card selectedCard;
	
	public PaymentCardsList(FragmentActivity context, List<Card> data, FragmentPlaceMyOrder fragmentPlaceMyOrder,Card selectedCard) {
		super();
		this.mContext = context;
		this.data = (ArrayList<Card>) data;
		this.fragmentPlaceMyOrder = fragmentPlaceMyOrder;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.selectedCard=selectedCard;
	}
	
	public View getView() {
		LinearLayout ll =  new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < data.size(); i++) {
			View convertView = (LinearLayout) mInflater.inflate(R.layout.adapter_payment_cards,null);
			TextView cardNumber = (TextView) convertView.findViewById(R.id.cardNumber);
			ImageView cardImage = (ImageView) convertView.findViewById(R.id.cardImage);
			CheckBox cardCheckBox = (CheckBox) convertView.findViewById(R.id.cardCheckBox);
			String number=CommonUtility.DecryptCreditCard(data.get(i).getCard_number());
			setCardImage(number, cardImage);
			if (!TextUtils.isEmpty(number)) {
				cardNumber.setText("Card #"+number.substring(number.length()-4, number.length()));
			}
			if(selectedCard!=null&&selectedCard.getCard_id().equals(data.get(i).getCard_id())){
				cardCheckBox.setChecked(true);
			}
			cardCheckBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox checkBox = (CheckBox) v;
					System.out.println("12345 in on click " + checkBox);
					for (int i = 0; i < checkBoxs.size(); i++) {
						if (!checkBoxs.get(i).equals(checkBox))
							checkBoxs.get(i).setChecked(false);
					}
				fragmentPlaceMyOrder.setCard(getSelectedCard());
//				fragmentPlaceMyOrder.changePlaceOrderColorPayment();
				}
			});
			checkBoxs.add(cardCheckBox);
			System.out.println("12345 in on add "+cardCheckBox);
//			CommonUtility.setImage(mContext, data.get(i).getProductimageurl(), cardImage, R.drawable.dum_list_item_product);
			ll.addView(convertView);
			convertView.setTag(i);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					
				}
			});
		}
		return ll;
	}
	
	private void setCardImage(String card_number, ImageView imageView) {
		if (card_number!=null&&Luhn.isCardValid(card_number)) {
			CardType cardtype = Luhn.getCardType(card_number);
			if(cardtype==null){
				imageView.setImageResource(R.drawable.ic_card_unknown);
			}else if (cardtype.equals(CardType.VISA)) {
				imageView.setImageResource(R.drawable.ic_card_visa);
			} else if (cardtype.equals(CardType.AMERICAN_EXPRESS)) {
				imageView.setImageResource(R.drawable.ic_card_american);
			} else if (cardtype.equals(CardType.MASTER_CARD)) {
				imageView.setImageResource(R.drawable.ic_card_master);
			} else if (cardtype.equals(CardType.DISCOVER)) {
				imageView.setImageResource(R.drawable.ic_card_discover);
			}
		} else
			imageView.setImageResource(R.drawable.ic_card_unknown);
	}
	
	public Card getSelectedCard(){
		for(int i=0;i<checkBoxs.size();i++){
			if(checkBoxs.get(i).isChecked()){
			fragmentPlaceMyOrder.isCardSelected=true;
			return data.get(i);
			}else
			fragmentPlaceMyOrder.isCardSelected=false;
		}
		return null;
	}
	
}
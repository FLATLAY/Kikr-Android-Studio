package com.kikr.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.dialog.RemoveCardDialog;
import com.kikr.fragment.FragmentCardInfo;
import com.kikr.fragment.FragmentSettings;
import com.kikr.utility.CardType;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.Luhn;
import com.kikrlib.api.CardInfoApi;
import com.kikrlib.bean.Card;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CardInfoRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class SettingsCardList {
	private FragmentActivity mContext;
	private List<Card> data;
	private LayoutInflater mInflater;
	private FragmentSettings fragmentSettings;
	private ProgressBarDialog progressBarDialog;
	private SettingsCardList settingsCardList;
	
	public SettingsCardList(FragmentActivity context, List<Card> data, FragmentSettings fragmentSettings) {
		super();
		this.mContext = context;
		this.fragmentSettings = fragmentSettings;
		settingsCardList=this;
		this.data = (ArrayList<Card>) data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView() {
		LinearLayout ll =  new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < data.size(); i++) {
			View convertView = (LinearLayout) mInflater.inflate(R.layout.adapter_settings_payment_cards,null);
			TextView cardNumber = (TextView) convertView.findViewById(R.id.cardNumber);
			ImageView cardImage = (ImageView) convertView.findViewById(R.id.cardImage);
			final LinearLayout cardDeleteImage = (LinearLayout) convertView.findViewById(R.id.cardDeleteImage);
			String cardnum=CommonUtility.DecryptCreditCard(data.get(i).getCard_number());
			setCardImage(cardnum, cardImage);
			String cardText="";
			if(cardnum.length()>4){
				cardText="Card #"+cardnum.substring(cardnum.length()-4, cardnum.length());
			}else{
				cardText="Card #"+cardnum;
			}
			cardNumber.setText(cardText);
			ll.addView(convertView);
			convertView.setTag(i);
			cardDeleteImage.setTag(i);
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					((HomeActivity) mContext).addFragment(new FragmentCardInfo(data.get((Integer) view.getTag()),fragmentSettings));
				}
			});
			cardDeleteImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if(((HomeActivity) mContext).checkInternet()){
						RemoveCardDialog removeCardDialog = new RemoveCardDialog(mContext, data.get((Integer) view.getTag()).getCard_id(),settingsCardList);
						removeCardDialog.show();
					}
				}
			});
		}
		return ll;
	}
	
	private void setCardImage(String card_number, ImageView imageView) {
		if (Luhn.isCardValid(card_number)) {
			CardType cardtype = Luhn.getCardType(card_number);
			Syso.info(cardtype);
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

	public void deleteCard(String cardId) {
		progressBarDialog = new ProgressBarDialog(mContext);
		progressBarDialog.show();
		final CardInfoApi service = new CardInfoApi(new ServiceCallback() {
		
		

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				if(object!=null){
					fragmentSettings.getCardList(false);
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception,Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>"+object);
				if(object!=null){
					CardInfoRes response=(CardInfoRes) object;
					AlertUtils.showToast(mContext,response.getMessage());
				}else{
					AlertUtils.showToast(mContext,R.string.invalid_response);
				}
				
			}
		});
		service.removeCard(UserPreference.getInstance().getUserID(), cardId);
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}
	
}
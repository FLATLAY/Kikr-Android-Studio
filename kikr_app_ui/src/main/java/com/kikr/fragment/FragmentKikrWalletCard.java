package com.kikr.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.ui.ProgressBarDialog;
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
import com.personagraph.api.PGAgent;

import java.util.List;




public class FragmentKikrWalletCard extends BaseFragment implements OnClickListener {
	private ImageView walletImage;
	private Animation animation;
	private RelativeLayout layout1;
	private RelativeLayout layout2;
	private HorizontalScrollView horizontalScrollView;
	private int[] pos = new int[] { R.drawable.ic_card_kikr, R.drawable.ic_connect_bank,R.drawable.ic_card_google, R.drawable.ic_connect_card };
	private View lastView;
	
	private Activity context;
	private View mainView;
	private ImageView connect_bank,connect_card,google_wallet,kikr_wallet;
	private TextView addCard;
	
	private RelativeLayout lastLayout,kikr_wallet_card_layout;
	private LinearLayout walletListMainLayout;
	private ProgressBarDialog progressBarDialog;
	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		CommonUtility.noTitleActivity(context);
//		setContentView(R.layout.kiker_wallet_main);
//		getUiObject();
//		setClickListener();
//		setAnimation();
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if(CommonUtility.isOnline(mContext))
			PGAgent.logEvent("DISCOVER_SCREEN_OPEND");
		context = this.getActivity();
		mainView = inflater.inflate(R.layout.kiker_wallet_cards, null);
		setAnimation();
		return mainView;
	}
	
	@Override
	public void initUI(Bundle savedInstanceState) {
		walletImage = (ImageView) mainView.findViewById(R.id.walletImage);
		connect_bank = (ImageView) mainView.findViewById(R.id.connect_bank);
		connect_card = (ImageView) mainView.findViewById(R.id.connect_card);
		google_wallet = (ImageView) mainView.findViewById(R.id.google_wallet);
		kikr_wallet = (ImageView) mainView.findViewById(R.id.kikr_wallet);
		layout1 = (RelativeLayout) mainView.findViewById(R.id.layout1);
		layout2 = (RelativeLayout) mainView.findViewById(R.id.layout2);
		addCard=(TextView) mainView.findViewById(R.id.addCard);
		kikr_wallet_card_layout= (RelativeLayout) mainView.findViewById(R.id.kikr_wallet_card_layout);
		walletListMainLayout= (LinearLayout) mainView.findViewById(R.id.walletListMainLayout);
		walletListMainLayout.setOrientation(LinearLayout.VERTICAL);
		horizontalScrollView = (HorizontalScrollView) mainView.findViewById(R.id.horizontalScrollView);
		horizontalScrollView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});		
		getAndSetWidth();
	}

	@Override
	public void setClickListener() {
		connect_bank.setOnClickListener(this);
		kikr_wallet.setOnClickListener(this);
		google_wallet.setOnClickListener(this);
		connect_card.setOnClickListener(this);
		addCard.setOnClickListener(this);
		walletImage.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {

	}
	public void initData() {
		if(checkInternet())
			getCardList();
	}
	@Override
	public void refreshData(Bundle bundle) {
		
	}
	
	@Override
	public void onClick(View v) {
		
//		cardClick(v);
		switch (v.getId()) {
		case R.id.kikrCardImageView:
			walletImage.setVisibility(View.VISIBLE);
			break;
		case R.id.connect_bank:
//			openBank(v);
		case R.id.connect_card:
			break;
		case R.id.google_wallet:
			break;
		case R.id.kikr_wallet:
			addFragment(new FragmentCreditRedeemDetailPage());
//			cardClick(kikr_wallet);
			break;
		case R.id.addCard:
			((HomeActivity) mContext).addFragment(new FragmentCardInfo(this));
			break;
		case R.id.walletImage:
			break;
		}
	}

	private void getAndSetWidth() {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
				height);
		layout1.setLayoutParams(params);
		layout2.setLayoutParams(params);
	}

	private void setAnimation() {
		animation = AnimationUtils.loadAnimation(context,R.anim.wallet_up_and_right);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				setSecondAnim();
			}
		});
	}

	protected void setSecondAnim() {
		// TODO Auto-generated method stub
		AnimationSet animation=new AnimationSet(false);
		animation.setFillAfter(true);
		RotateAnimation rotateAnimation=new RotateAnimation(0, 90, 225, 225);
		rotateAnimation.setDuration(100);
		rotateAnimation.setStartOffset(200);
		rotateAnimation.setFillAfter(true);
		animation.addAnimation(rotateAnimation);
		
//		Animation animation2 = AnimationUtils.loadAnimation(context,R.anim.wallet_right_and_down);
		walletImage.startAnimation(animation);
	}

	private void setLocation(View v) {
		int x = 36;
		int w = v.getWidth();
		int h = v.getHeight();
		int[] pos = new int[2];
		v.getLocationOnScreen(pos);
		System.out.println("0>" + pos[0] + " 1>" + pos[1]);
		// (int l, int t, int r, int b)
		walletImage.layout(pos[0], pos[1] - x, pos[0] + w, pos[1] + h - x);
	}
	

	private void cardClick(final View v) {
		lastView = v;
		Drawable drawable=((ImageView) v).getDrawable();
		walletImage.setImageDrawable(drawable);
//		walletImage.setImageResource(pos[Integer.parseInt((String) v.getTag())]);
		setLocation(v);
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setLocation(v);
				System.out.println("tag>>" + v.getTag());
				// walletImage.setVisibility(View.VISIBLE);
				v.startAnimation(animation);
				// v.setVisibility(View.INVISIBLE);
			}
		};
		Handler handler = new Handler();
		handler.postDelayed(runnable, 1);

		// walletImage.startAnimation(animation);
	}
	
	public void getCardList() {
		progressBarDialog = new ProgressBarDialog(mContext);
		progressBarDialog.show();
		final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				if (object != null) {
					CardInfoRes cardInfoRes = (CardInfoRes) object;
					if(cardInfoRes.getData().size()>0)
						setCardList(cardInfoRes.getData());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					CardInfoRes cardInfoRes=(CardInfoRes) object;
					AlertUtils.showToast(mContext, cardInfoRes.getMessage());
				}else{
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		cardInfoApi.getCardList(UserPreference.getInstance().getUserID());
		cardInfoApi.execute();
	}
/**
 * This method will create custom card list
 * @param data : list of added card
 */
	protected void setCardList(final List<Card> data) {
		//no need to remove first card, its kikr card
		if(walletListMainLayout.getChildCount()>2)
			walletListMainLayout.removeViews(2, walletListMainLayout.getChildCount()-2);
		lastLayout=kikr_wallet_card_layout;
		LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutParams params;
		for(int i=0;i<data.size();i++){
			RelativeLayout layout=(RelativeLayout) inflater.inflate(R.layout.layout_wallet_card, null);
		//	LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			 params= new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_TOP, lastLayout.getId());
		//	params.setMargins(0, 510, 0, 510);
			ImageView cardImageView = (ImageView) layout.findViewById(R.id.cardImageView);
			setCardImage(data.get(i).getCard_number(), cardImageView);
			cardImageView.setTag(i);
			cardImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((HomeActivity) mContext).addFragment(new FragmentCardInfo(data.get((Integer) v.getTag()),FragmentKikrWalletCard.this));
//					cardClick(v);
				}
			});
			walletListMainLayout.addView(layout, params);
			lastLayout=layout;
			lastLayout.setId(i+1);
			if(i==(data.size()-1)) {
				params.setMargins(0, 1, 0, 200);
			}
			Syso.info("Adding layout : "+layout +" , "+lastLayout.getId());
		}


	}
//	/**
//	 * This m
//	 * @param card_number
//	 * @param imageView
//	 */
	private void setCardImage(String card_number1, ImageView imageView) {
		String card_number= CommonUtility.DecryptCreditCard(card_number1);
		if (Luhn.isCardValid(card_number)) {
			CardType cardtype = Luhn.getCardType(card_number);
			if(cardtype==null){
				imageView.setImageResource(R.drawable.ic_connect_card_unknown);
			}else if (cardtype.equals(CardType.VISA)) {
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

}

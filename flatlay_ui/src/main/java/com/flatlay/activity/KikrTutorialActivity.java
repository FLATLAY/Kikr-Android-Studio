package com.flatlay.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.KikrCreditsApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.KikrCreditsRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import org.json.JSONObject;

import java.util.List;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class KikrTutorialActivity extends BaseActivity implements OnClickListener{

	private ImageView kikrCardImageView,kikrPocketImageView,kikrIconImageView;
	private TextView kikrInfoTextView,kikrRewordTextView,learnMoreText;
	private boolean mIsCardAnimated=false;
	private RelativeLayout parentRelativeLayout;
	private int mTotalCircle=16;
	private LayoutInflater mInflater;
	private int y;
	private int x;
	private int[] fromXDelta;
	private int[] fromYDelta;
	private int[] toXDelta;
	private int[] toYDelta;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		UserPreference.getInstance().setCurrentScreen(Screen.CardScreen);
		setContentView(R.layout.activity_kikr_tutorial);
		addCircleViews();
		craeteArry();
		System.out.println("width>"+CommonUtility.getDeviceWidth(context));
		System.out.println("height>"+CommonUtility.getDeviceHeight(context));
		updateScreen(Screen.CardScreen);
		AppConstants.isFromTutorial = true;
	}
	

	private void craeteArry() {
		// TODO Auto-generated method stub
		y=CommonUtility.getDeviceHeight(context)/11;
		x=CommonUtility.getDeviceWidth(context)/5;
		fromXDelta=new int[]{0*x,1*x,2*x,3*x,0*x,1*x,2*x,3*x,0*x,1*x,2*x,3*x,0*x,1*x,2*x,3*x,4*x};
		fromYDelta=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		toXDelta = new int[]{0*x,1*x,2*x,3*x,0*x,1*x,2*x,3*x,0*x,1*x,2*x,3*x,0*x,1*x,2*x,3*x,4*x};
		toYDelta = new int[]{-1*y,-2*y,-1*y,-2*y,-3*y,-4*y,-3*y,-4*y,-5*y,-6*y,-5*y,-6*y,-7*y,-8*y,-7*y,-8*y};
	}


	@Override
	public void initLayout() {
		kikrCardImageView=(ImageView) findViewById(R.id.kikrCardImageView);
		kikrPocketImageView=(ImageView) findViewById(R.id.kikrPocketImageView);
		kikrInfoTextView=(TextView) findViewById(R.id.kikrInfoTextView);
		parentRelativeLayout=(RelativeLayout) findViewById(R.id.parentRelativeLayout);
		kikrRewordTextView=(TextView) findViewById(R.id.kikrRewordTextView);
		kikrIconImageView=(ImageView) findViewById(R.id.kikrIconImageView);
		learnMoreText = (TextView) findViewById(R.id.learnMoreText);
		kikrRewordTextView.setText(Html.fromHtml(getResources().getString(R.string.tutorial_kikr_reward)));
	}

	@Override
	public void setupData() {
		mInflater=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//		kikrInfoTextView.setText(getResources().getString(R.string.tutorial_kikr_lines)+Html.fromHtml("<sup><small>TM</small></sup>"));
	}

	@Override
	public void headerView() {
		hideHeader();
	}

	@Override
	public void setUpTextType() {
//		kikrInfoTextView.setTypeface(FontUtility.setProximanovaLight(context));
//		kikrRewordTextView.setTypeface(FontUtility.setProximanovaLight(context));
	}

	@Override
	public void setClickListener() {
		kikrCardImageView.setOnClickListener(this);
		parentRelativeLayout.setOnClickListener(this);
		learnMoreText.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.kikrCardImageView:
			if(!mIsCardAnimated){
				mIsCardAnimated=true;
				animateCard();
			}
			break;
		case R.id.parentRelativeLayout:
			startActivity(HomeActivity.class);
			finish();
			break;
		case R.id.learnMoreText:
			startActivity(KikrLearnMoreActivity.class);
			break;
		}
	}

	private void animateCard() {
		kikrCardImageView.startAnimation(AnimationUtils.loadAnimation(context,R.anim.kikr_up_and_down));
		Handler handler=new Handler();
		Runnable runnable=new Runnable() {
			@Override
			public void run() {
				System.out.println("in run");
				kikrPocketImageView.setVisibility(View.VISIBLE);
				kikrInfoTextView.setVisibility(View.VISIBLE);
//				setCircleAnimationView();
				setHandler();
			}
			
		};
		handler.postDelayed(runnable, 400);
	}
	
	protected void setHandler() {
		Handler handler=new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(HomeActivity.class);
				finish();
			}
		}, 600);
	}


	private void setCircleAnimationView() {
		Handler handler=new Handler();
		Runnable runnable=new Runnable() {
			@Override
			public void run() {
				kikrPocketImageView.setVisibility(View.GONE);
				kikrIconImageView.setVisibility(View.GONE);
				kikrRewordTextView.setVisibility(View.GONE);
				parentRelativeLayout.setVisibility(View.VISIBLE);
				setCircleAnimation(); 
			}			
		};
		handler.postDelayed(runnable, 600);
	}
	
	private void addCircleViews() {
		for(int i=0;i<mTotalCircle;i++){
			View v=mInflater.inflate(R.layout.layout_kikr_bubbles, null);
			if(i==10)
				((ImageView)v.findViewById(R.id.circle)).setImageResource(R.drawable.ic_kikr_circle);
			parentRelativeLayout.addView(v);
		}
	}
	
	private void setCircleAnimation() {
		for(int i=0;i<parentRelativeLayout.getChildCount();i++){
			AnimationSet animationSet=new AnimationSet(false);
			TranslateAnimation translateAnimation=new TranslateAnimation(fromXDelta[i], toXDelta[i], fromYDelta[i],toYDelta[i]);
			translateAnimation.setDuration(1500);
			translateAnimation.setFillAfter(true);
			animationSet.addAnimation(translateAnimation);
			List<Animation> animations=animationSet.getAnimations();
			View v=parentRelativeLayout.getChildAt(i);
			v.startAnimation(animations.get(0));
		}
	}
	
	@Override
	public void onBackPressed() {
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Branch branch = Branch.getInstance();
		branch.initSession(new Branch.BranchReferralInitListener() {

			@Override
			public void onInitFinished(JSONObject referringParams, BranchError error) {
				if (error == null) {
//					Log.e("Referred and Referrer ID", "Referred id: " + UserPreference.getInstance().getUserID() + ":Referrer id: "+ LandingActivity.referred_userid);
					
					if(!LandingActivity.referred_userid.equals("-1") && !LandingActivity.referred_userid.equalsIgnoreCase(UserPreference.getInstance().getUserID())) {
						addKikrCredits(LandingActivity.referred_userid);
						Branch.getInstance(getApplicationContext()).userCompletedAction("reffered_event");
					}
					
				}
				else {
					Log.e("MyApp", error.getMessage());
				}
			}
		}, this.getIntent().getData(), this);
	}
	
	private void addKikrCredits(final String user_id) {
		final KikrCreditsApi creditsApi = new KikrCreditsApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				final KikrCreditsRes kikrCreditsRes = (KikrCreditsRes) object;
				//Toast.makeText(context, "Credits sent to referral!", Toast.LENGTH_LONG).show();
//				Log.e("Successfully sent kikr credits to", kikrCreditsRes.getMessage() + user_id);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					KikrCreditsRes response = (KikrCreditsRes) object;
					//AlertUtils.showToast(context, response.getMessage());
					Log.e("failed response", response.getMessage());
				} else {
					AlertUtils.showToast(context, R.string.invalid_response);
				}
//				Log.e("Failed sent kikr credits to", "failed sent kikr credits");
			}
		});
		creditsApi.addKikrCredit(user_id);
		creditsApi.execute();
	}
	
}

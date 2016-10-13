package com.kikr.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;

public class HelpPressMenuDialog extends Dialog{

	private FragmentActivity mContext;
	private ImageView img1;
	private RelativeLayout parentRL;
    private RelativeLayout mainRL;
	private RelativeLayout LikeRL;
	private RelativeLayout collectionRL;
	private RelativeLayout cartRL;
	private RelativeLayout shareRL;

	private TextView likeTV;
	private TextView collectionTV;
	private TextView cartTV;
	private TextView shareTV;
	private boolean isAnimationCompleted = false;
	
	public HelpPressMenuDialog(FragmentActivity context) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}

	private void init() {
		setContentView(R.layout.helpscreen_presshere_menu);
		setCanceledOnTouchOutside(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCancelable(true);
		img1 = (ImageView) findViewById(R.id.img1);
		
		parentRL=(RelativeLayout)findViewById(R.id.parentRL);
		mainRL=(RelativeLayout)findViewById(R.id.mainRL);
		LikeRL=(RelativeLayout) findViewById(R.id.likeRL);
		collectionRL=(RelativeLayout) findViewById(R.id.discoverRL);
		cartRL=(RelativeLayout) findViewById(R.id.checkoutRL);
		shareRL=(RelativeLayout) findViewById(R.id.shareRL);
		
		
		mainRL.setVisibility(View.GONE);
		LikeRL.setVisibility(View.GONE);
		collectionRL.setVisibility(View.GONE);
		cartRL.setVisibility(View.GONE);
		shareRL.setVisibility(View.GONE);
		
		mainRL.setVisibility(View.VISIBLE);
		startMainRLFadeAnimation(mainRL);
//		
//		mainRL.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				// TODO Auto-generated method stub
//				LikeRL.setVisibility(View.VISIBLE);
//				startLikeArcAnimation(0.0f,70.0f,0.0f,-180.0f);
//				return false;
//			}
//		});
		
		img1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*HelpOpenMenuDialog helpOpenMenuDialog = new HelpOpenMenuDialog(mContext);
				helpOpenMenuDialog.show();
				dismiss();*/
				
				/*startLikeArcAnimation();
				startDisArcAnimation();
				startCheckArcAnimation();
				startShareArcAnimation();*/
			}
		});
		
		
		parentRL.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("","Touch------");
				if(isAnimationCompleted)
					dismiss();
			   
			}
		});
	}
	
	 private void startLikeArcAnimation(float toX,float fromX,float toY,float fromY){
	        TranslateAnimation animation = new TranslateAnimation(0.0f, fromX,
	                0.0f, fromY);
	        animation.setDuration(300);
	        animation.setFillAfter(true);
	        LikeRL.startAnimation(animation);
	        animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					collectionRL.setVisibility(View.VISIBLE);
					
					startDisArcAnimation(0.0f,200.0f,0.0f,-80.0f);	
				}
			});
	    }


	    private void startDisArcAnimation(float toX,float fromX,float toY,float fromY){
	        TranslateAnimation animation = new TranslateAnimation(0.0f, fromX,
	                0.0f, fromY);
	        animation.setDuration(500);
	        animation.setFillAfter(true);
	        collectionRL.startAnimation(animation);
	        
	        animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					cartRL.setVisibility(View.VISIBLE);
					
					startCheckArcAnimation(0.0f,190.0f,0.0f,80.0f);
						
				}
			});
	    }

	    private void startCheckArcAnimation(float toX,float fromX,float toY,float fromY){
	        TranslateAnimation animation = new TranslateAnimation(0.0f,fromX,
	                0.0f, fromY);
	        animation.setDuration(500);
	        animation.setFillAfter(true);
	        cartRL.startAnimation(animation);
	        
	        animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					shareRL.setVisibility(View.VISIBLE);
					
					startShareArcAnimation(0.0f,100.0f,0.0f,210.0f);
				}
			});
	    }
	    private void startShareArcAnimation(float toX,float fromX,float toY,float fromY){
	        TranslateAnimation animation = new TranslateAnimation(0.0f, fromX,
	                0.0f, fromY);
	        animation.setDuration(700);
	        animation.setFillAfter(true);
	        shareRL.startAnimation(animation);
	        animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					isAnimationCompleted = true;
				}
			});
	    }
	    
	   
	    private void startMainRLFadeAnimation(RelativeLayout myLayout){
	    	
	        Animation fadeOut = new AlphaAnimation(1, 0);
	        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
	        fadeOut.setStartOffset(500 + 500);
	        fadeOut.setDuration(500);
                    
	        myLayout.startAnimation(fadeOut);
	        
	        fadeOut.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
				
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					
					LikeRL.setVisibility(View.VISIBLE);
					startLikeArcAnimation(0.0f,70.0f,0.0f,-180.0f);
					
					/*startDisArcAnimation();
					startCheckArcAnimation();
					startShareArcAnimation();*/
					
				}
			});
	        
	    }
	    
}

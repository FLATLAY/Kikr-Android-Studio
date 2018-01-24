package com.flatlay.dialog;

import android.app.Dialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
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
import com.flatlay.utility.FontUtility;

public class HelpPressMenuDialog extends Dialog {

    private FragmentActivity mContext;
    private ImageView img1;
    private RelativeLayout parentRL;
    private RelativeLayout mainRL;
    private RelativeLayout LikeRL;
    private RelativeLayout collectionRL;
    private RelativeLayout cartRL;
    private RelativeLayout shareRL;
    private RelativeLayout storeRL;

    private TextView likeTV;
    private TextView collectionTV;
    private TextView cartTV;
    private TextView shareTV;
    private TextView storeTV;
    private boolean isAnimationCompleted = false;

    public HelpPressMenuDialog(FragmentActivity context) {
        super(context, R.style.AdvanceDialogTheme);
        mContext = context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void init() {
        setContentView(R.layout.helpscreen_presshere_menu);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCancelable(true);
        img1 = (ImageView) findViewById(R.id.img1);

        parentRL = (RelativeLayout) findViewById(R.id.parentRL);
        mainRL = (RelativeLayout) findViewById(R.id.mainRL);
        LikeRL = (RelativeLayout) findViewById(R.id.likeRL);
        collectionRL = (RelativeLayout) findViewById(R.id.discoverRL);
        cartRL = (RelativeLayout) findViewById(R.id.checkoutRL);
        shareRL = (RelativeLayout) findViewById(R.id.shareRL);
        storeRL = (RelativeLayout) findViewById(R.id.storeRL);

        likeTV = (TextView) findViewById(R.id.likeTV);
        likeTV.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        collectionTV = (TextView) findViewById(R.id.discoverTV);
        collectionTV.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        cartTV = (TextView) findViewById(R.id.checkoutTV);
        cartTV.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        shareTV = (TextView) findViewById(R.id.shareTV);
        shareTV.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        storeTV = (TextView) findViewById(R.id.storeTV);
        storeTV.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));

        mainRL.setVisibility(View.GONE);
        LikeRL.setVisibility(View.GONE);
        collectionRL.setVisibility(View.GONE);
        cartRL.setVisibility(View.GONE);
        shareRL.setVisibility(View.GONE);
        storeRL.setVisibility(View.GONE);

        mainRL.setVisibility(View.VISIBLE);
        startMainRLFadeAnimation(mainRL);


//        img1.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                startMainRLFadeAnimation(mainRL);
//            }
//        });


        parentRL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.d("", "Touch------");
                if (isAnimationCompleted)
                    dismiss();

            }
        });
    }

    private void startLikeArcAnimation(float toX, float fromX, float toY, float fromY) {
        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,
                fromX, TranslateAnimation.RELATIVE_TO_PARENT, toX,
                TranslateAnimation.RELATIVE_TO_PARENT, fromY, TranslateAnimation.RELATIVE_TO_PARENT, toY);
        animation.setDuration(500);
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

                startDisArcAnimation(-0.13f, 0.0f, -0.10f, 0.0f);
            }
        });
    }


    private void startDisArcAnimation(float toX, float fromX, float toY, float fromY) {
        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,
                fromX, TranslateAnimation.RELATIVE_TO_PARENT, toX,
                TranslateAnimation.RELATIVE_TO_PARENT, fromY, TranslateAnimation.RELATIVE_TO_PARENT, toY);
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

                startCheckArcAnimation(0.0f, 0.0f, -0.14f, 0.0f);

            }
        });
    }

    private void startCheckArcAnimation(float toX, float fromX, float toY, float fromY) {
        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,
                fromX, TranslateAnimation.RELATIVE_TO_PARENT, toX,
                TranslateAnimation.RELATIVE_TO_PARENT, fromY, TranslateAnimation.RELATIVE_TO_PARENT, toY);
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
                storeRL.setVisibility(View.VISIBLE);

                startStoreArcAnimation(0.13f, 0.0f, -0.10f, 0.0f);
            }
        });
    }

    private void startStoreArcAnimation(float toX, float fromX, float toY, float fromY) {
        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,
                fromX, TranslateAnimation.RELATIVE_TO_PARENT, toX,
                TranslateAnimation.RELATIVE_TO_PARENT, fromY, TranslateAnimation.RELATIVE_TO_PARENT, toY);
        animation.setDuration(500);
        animation.setFillAfter(true);
        storeRL.startAnimation(animation);

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

                startShareArcAnimation(0.18f, 0.0f, 0.0f, 0.0f);
            }
        });
    }


    private void startShareArcAnimation(float toX, float fromX, float toY, float fromY) {
        TranslateAnimation animation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,
                fromX, TranslateAnimation.RELATIVE_TO_PARENT, toX,
                TranslateAnimation.RELATIVE_TO_PARENT, fromY, TranslateAnimation.RELATIVE_TO_PARENT, toY);
        animation.setDuration(500);
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

    private void startMainRLFadeAnimation(RelativeLayout myLayout) {

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
        fadeOut.setStartOffset(500 + 500);
        fadeOut.setDuration(100);
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
                startLikeArcAnimation(-0.18f, 0.0f, 0.0f, 0.0f);
            }
        });
    }
}


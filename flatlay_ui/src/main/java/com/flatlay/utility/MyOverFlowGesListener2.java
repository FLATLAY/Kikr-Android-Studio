package com.flatlay.utility;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.lang.ref.WeakReference;

/**
 * Created by RachelDi on 2/15/18.
 */

public class MyOverFlowGesListener2 extends GestureDetector.SimpleOnGestureListener {

    private Boolean isScrollUpping;

    private boolean isOpened;

    private float initialYPosition = -1, initialLocation = -1;

    private WeakReference<ViewGroup> overflow;

    private Animation aa, bb;

    private ImageView image;


    private GestureDetectorCompat gestureDetectorCompat;

    public MyOverFlowGesListener2(ViewGroup overflow, ImageView image) {
        Log.e("listener-create","1");
        this.overflow = new WeakReference<>(overflow);
        this.gestureDetectorCompat = new GestureDetectorCompat(
                overflow.getContext(), this);
        aa = new RotateAnimation(0.0f, 180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        aa.setRepeatCount(0);
        aa.setFillAfter(true);
        aa.setDuration(400);


        bb = new RotateAnimation(180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        bb.setRepeatCount(0);
        bb.setFillAfter(true);
        bb.setDuration(400);

        this.image = image;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float distanceX, float distanceY) {
        Log.e("listener-scroll","1");
        if (motionEvent2.getRawY() <= initialYPosition) {
            Log.e("listener-scroll","2");
            ViewHelper.setTranslationY(overflow.get(), motionEvent2.getRawY());
            if (motionEvent.getRawY() < motionEvent2.getRawY()) {
                Log.e("listener-scroll","3");
                isScrollUpping = false;
                isOpened = false;
            } else {
                Log.e("listener-scroll","4");
                Log.e("listener-slide","5--"+isOpened);
                isScrollUpping = true;
                isOpened = true;
            }
        }
        return true;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY) {
        Log.e("listener-fling","1");
        if (motionEvent.getRawY() < motionEvent2.getRawY()) {
            Log.e("listener-fling","2");
            slide(initialYPosition);

        } else {
            Log.e("listener-fling","3");
            slide(-350);

        }
        return true;
    }

    public void slide(float position) {
        Log.e("overflow-slide","1");

        ObjectAnimator objectAnimator;
        if(overflow!=null) {

            if (position == -350) {
                this.isOpened = false;
                Log.e("overflow-slide", "2--" + isOpened);
                objectAnimator = ObjectAnimator.ofFloat(overflow.get(), "translationY", initialYPosition);
                image.startAnimation(bb);
            } else {
                Log.e("overflowslide", "3");
                this.isOpened = true;
                objectAnimator = ObjectAnimator.ofFloat(overflow.get(), "translationY", 0);
                image.startAnimation(aa);
            }
            objectAnimator.setInterpolator(new LinearOutSlowInInterpolator());
            objectAnimator.setDuration(800);
            objectAnimator.start();
        }
    }

    View.OnTouchListener motionEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            gestureDetectorCompat.onTouchEvent(motionEvent);
            view.onTouchEvent(motionEvent);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                isOpened = true;
                Log.e("listener-slide","3--"+isOpened);
                if (isScrollUpping != null) {
                    if (isScrollUpping) {
                        slide(0f);
                    } else {
                        slide(initialYPosition);
                    }
                }
            }
            return true;
        }
    };

    public View.OnTouchListener getMotionEvent() {
        Log.e("listener-touch","1");

        return motionEvent;
    }

    public void setInitialYPosition(float initialYPosition) {
        Log.e("listener-ypos","1");

        this.initialYPosition = initialYPosition;
    }

    public boolean isOpened() {
        Log.e("listener-open","1");

        return isOpened;
    }

    public void setIsOpened(boolean isOpened) {
        Log.e("listener-isOpen","1");
        this.isOpened = isOpened;
    }

    public void clearReferences() {
        Log.e("listener-clear","1");
        overflow.clear();
        overflow = null;
        gestureDetectorCompat = null;
    }
}

package com.flatlay.utility;

/**
 * Created by RachelDi on 1/26/18.
 */

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlaylib.utils.Syso;
//import com.sam.bubbleactions.R;

/**
 * Created by sam on 11/2/15.
 */
class MyBubbleView extends LinearLayout {

    // In order to prevent clipping, the bubble starts out smaller than the space it's given
    private static final float DESELECTED_SCALE = 0.6f;

    private static final float SELECTED_SCALE = 0.9f;

    private static final int ANIMATION_DURATION = 150;
    Integer index = -1;
    Context context;
    boolean animatedIn = false;
    MyBubbleActions.Callback callback;
    String[] titles = {"Like", "Add to\nCollection", "Add to\nCart", "View On\nStore Site", "Share"};
    ImageView imageView;
    String currentTitle;

    public MyBubbleView(Context context, int index) {
        super(context);
        this.context = context;
        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.bubble_actions_bubble_item, this, true);
        imageView = (ImageView) findViewById(R.id.bubble_actions_item_icon);
        imageView.setOnDragListener(dragListener);
        imageView.setScaleX(DESELECTED_SCALE);
        imageView.setScaleY(DESELECTED_SCALE);
        this.index = index;
    }

    void resetChildren() {
        imageView.setScaleX(DESELECTED_SCALE);
        imageView.setScaleY(DESELECTED_SCALE);
        imageView.setSelected(false);
    }

    void setIndex(int i) {
        this.index = i;
    }


    public void setTitle(String s) {
        this.currentTitle = s;
    }


    OnDragListener dragListener = new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            // Gotcha: you need to return true for drag end and start
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:

                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if (animatedIn) {
                        imageView.setSelected(true);
                        if (index == 0) {
                            imageView.setImageResource(com.flatlay.R.drawable.likegreen);
                          //  imageView.setBackgroundResource(R.drawable.roundtealbg);
                            MyBubbleActionOverlay.setTitle(titles[0]);
                        }

                        if (index == 1) {
                            imageView.setImageResource(com.flatlay.R.drawable.greenplus);
                         //   imageView.setBackgroundResource(R.drawable.roundtealbg);
                            MyBubbleActionOverlay.setTitle(titles[1]);
                        }

                        if (index == 2) {
                            imageView.setImageResource(com.flatlay.R.drawable.ic_cart_selected);
                          //  imageView.setBackgroundResource(R.drawable.roundtealbg);
                            MyBubbleActionOverlay.setTitle(titles[2]);
                        }

                        if (index == 3) {
                            imageView.setImageResource(com.flatlay.R.drawable.sitegreen);
                           // imageView.setBackgroundResource(R.drawable.roundtealbg);
                            MyBubbleActionOverlay.setTitle(titles[3]);
                        }

                        if (index == 4) {
                            imageView.setImageResource(com.flatlay.R.drawable.sharegreen1);
                          //  imageView.setBackgroundResource(R.drawable.roundtealbg);
                            MyBubbleActionOverlay.setTitle(titles[4]);
                        }

                        ViewCompat.animate(imageView)
                                .scaleX(SELECTED_SCALE)
                                .scaleY(SELECTED_SCALE)
                                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(View view) {
                                        super.onAnimationStart(view);
                                        // textView.setVisibility(VISIBLE);
//                                        ViewCompat.animate(textView)
//                                                .alpha(1f)
//                                                .setListener(null)
//                                                .setDuration(ANIMATION_DURATION);
                                    }
                                })
                                .setDuration(ANIMATION_DURATION);
                    }

                    return animatedIn;
                case DragEvent.ACTION_DRAG_EXITED:
                    if (animatedIn) {
                        imageView.setSelected(false);
                        if (index == 0) {
                            imageView.setImageResource(com.flatlay.R.drawable.small_gray_heart);
                            imageView.setBackgroundResource(com.flatlay.R.drawable.roundblackbg);
                        }
                        if (index == 1) {
                            imageView.setImageResource(com.flatlay.R.drawable.small_gray_plus);
                            imageView.setBackgroundResource(com.flatlay.R.drawable.roundblackbg);
                        }

                        if (index == 2) {
                            imageView.setImageResource(com.flatlay.R.drawable.small_gray_cart);
                            imageView.setBackgroundResource(com.flatlay.R.drawable.roundblackbg);
                        }
                        if (index == 3) {
                            imageView.setImageResource(com.flatlay.R.drawable.small_gray_store);
                            imageView.setBackgroundResource(com.flatlay.R.drawable.roundblackbg);
                        }
                        if (index == 4) {
                            imageView.setImageResource(com.flatlay.R.drawable.small_gray_kopie);
                            imageView.setBackgroundResource(com.flatlay.R.drawable.roundblackbg);
                        }
                            ViewCompat.animate(imageView)
                                .scaleX(DESELECTED_SCALE)
                                .scaleY(DESELECTED_SCALE)
                                .setDuration(ANIMATION_DURATION)
                                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(View view) {
                                        super.onAnimationStart(view);
//                                        ViewCompat.animate(textView)
//                                                .alpha(0f)
//                                                .setListener(new ViewPropertyAnimatorListenerAdapter() {
//                                                    @Override
//                                                    public void onAnimationEnd(View view) {
//                                                        super.onAnimationEnd(view);
//                                                        textView.setVisibility(INVISIBLE);
//                                                    }
//                                                })
//                                                .setDuration(ANIMATION_DURATION);
                                    }
                                });
                    }

                    return animatedIn;
                case DragEvent.ACTION_DROP:
                    callback.doAction();
                    return true;
            }
            return false;
        }
    };
}

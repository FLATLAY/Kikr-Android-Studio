package com.flatlay.utility;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by RachelDi on 1/27/18.
 */

public class MyBubbleView2 extends LinearLayout {

    // In order to prevent clipping, the bubble starts out smaller than the space it's given
    private static final float DESELECTED_SCALE = 0.6f;

    private static final float SELECTED_SCALE = 0.9f;

    private static final int ANIMATION_DURATION = 150;
    Integer index = -1;
    String[] titles = {"Like", "Share"};
    String currentTitle;
    boolean animatedIn = false;
    MyBubbleActions2.Callback callback;
  //  TextView textView;
    ImageView imageView;

    public MyBubbleView2(Context context, int index) {
        super(context);

        setOrientation(VERTICAL);
        LayoutInflater.from(context).inflate(com.sam.bubbleactions.R.layout.bubble_actions_bubble_item, this, true);
       // textView = (TextView) getChildAt(0);
        imageView = (ImageView) getChildAt(1);
        imageView.setOnDragListener(dragListener);
        imageView.setScaleX(DESELECTED_SCALE);
        imageView.setScaleY(DESELECTED_SCALE);
        this.index = index;
    }

    void resetChildren() {
        imageView.setScaleX(DESELECTED_SCALE);
        imageView.setScaleY(DESELECTED_SCALE);
        imageView.setSelected(false);
       // textView.setVisibility(INVISIBLE);
    }

    void setIndex(int i) {
        this.index = i;
    }

    public void setTitle(String s) {
        this.currentTitle = s;
    }


    /**
     * OnDragListener for the ImageView. The correct behavior is only to register a drag enter only
     * if we enter the ImageView (otherwise it would still register a drag enter if we touch the
     * TextView).
     */
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
                            MyBubbleActionOverlay2.setTitle(titles[0]);
                        }
                        if (index == 1) {
                            imageView.setImageResource(com.flatlay.R.drawable.sharegreen1);
                            MyBubbleActionOverlay2.setTitle(titles[1]);
                        }

                        ViewCompat.animate(imageView)
                                .scaleX(SELECTED_SCALE)
                                .scaleY(SELECTED_SCALE)
                                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(View view) {
                                        super.onAnimationStart(view);
//                                        textView.setVisibility(VISIBLE);
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
                        if (index == 0)
                            imageView.setImageResource(com.flatlay.R.drawable.small_gray_heart);
                        if (index == 1)
                            imageView.setImageResource(com.flatlay.R.drawable.small_gray_kopie);
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


package com.flatlay.utility;

/**
 * Created by RachelDi on 1/26/18.
 */

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;

import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlaylib.bean.Product;

/**
 *
 */
class MyBubbleActionOverlay extends FrameLayout {

    /**
     * Ripped straight from v21 AOSP. No idea why this is v21+
     */
    static class BackgroundAlphaTypeEvaluator implements TypeEvaluator<Integer> {

        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int startInt = startValue;
            int startA = (startInt >> 24) & 0xff;
            int startR = (startInt >> 16) & 0xff;
            int startG = (startInt >> 8) & 0xff;
            int startB = startInt & 0xff;

            int endInt = endValue;
            int endA = (endInt >> 24) & 0xff;
            int endR = (endInt >> 16) & 0xff;
            int endG = (endInt >> 8) & 0xff;
            int endB = endInt & 0xff;

            return ((startA + (int) (fraction * (endA - startA))) << 24) |
                    ((startR + (int) (fraction * (endR - startR))) << 16) |
                    ((startG + (int) (fraction * (endG - startG))) << 8) |
                    ((startB + (int) (fraction * (endB - startB))));
        }
    }

    static final int MAX_ACTIONS = 5;

    private static final String TAG = MyBubbleActionOverlay.class.getSimpleName();

    private static final float OVERSHOOT_TENSION = 1.5f;
    private static final int ANIMATION_DURATION = 150;

    private float[] actionStartX = new float[MAX_ACTIONS];
    private float[] actionStartY = new float[MAX_ACTIONS];
    private float[] actionEndX = new float[MAX_ACTIONS];
    private float[] actionEndY = new float[MAX_ACTIONS];
    private OvershootInterpolator overshootInterpolator;
    private ClipData dragData;
    private DragShadowBuilder dragShadowBuilder;
    private float startActionDistanceFromCenter;
    private float stopActionDistanceFromCenter;
    private float bubbleDimension;
    private RectF contentClipRect;
    private ImageView bubbleActionIndicator;
    private int numActions = 0;
    private boolean overlayActive = false;
    private ObjectAnimator backgroundAnimator;
    Context context;
    static TextView tv;

    MyBubbleActionOverlay(Context context) {
        super(context);
        this.context = context;
        contentClipRect = new RectF();
        dragShadowBuilder = new DragShadowBuilder();
        dragData = new ClipData(TAG, new String[]{TAG}, new ClipData.Item(TAG));
        LayoutInflater inflater = LayoutInflater.from(context);
        bubbleActionIndicator = (ImageView) inflater.inflate(R.layout.bubble_actions_indicator, this, false);
        bubbleActionIndicator.setAlpha(0f);
        addView(bubbleActionIndicator, -1);
        overshootInterpolator = new OvershootInterpolator(OVERSHOOT_TENSION);
        Resources resources = getResources();
        int transparentBackgroundColor = resources.getColor(R.color.bubble_actions_background_transparent);
        int darkenedBackgroundColor = resources.getColor(R.color.bubble_actions_background_darkened);
        setBackgroundColor(transparentBackgroundColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            backgroundAnimator = ObjectAnimator.ofArgb(this, "backgroundColor", transparentBackgroundColor, darkenedBackgroundColor);
        }
//        else {
//            backgroundAnimator = ObjectAnimator.ofArgb(this, "backgroundColor", transparentBackgroundColor, darkenedBackgroundColor);
//        }

        backgroundAnimator.setDuration(ANIMATION_DURATION);

        bubbleDimension = (int) getResources().getDimension(R.dimen.bubble_actions_indicator_dimension);
        startActionDistanceFromCenter = getResources().getDimension(R.dimen.bubble_actions_start_distance);
        stopActionDistanceFromCenter = getResources().getDimension(R.dimen.bubble_actions_stop_distance);

        for (int i = 0; i < MAX_ACTIONS; i++) {
            MyBubbleView itemView = new MyBubbleView(getContext(), i);
            itemView.setVisibility(INVISIBLE);
            itemView.setAlpha(0f);
            addView(itemView, -1, new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public static void setTitle(String s) {
        tv.setText(s);
    }

    void setLabelTypeface(Typeface typeface) {
//        for (int i = 1; i <= MAX_ACTIONS; i++) {
//            MyBubbleView itemView = (MyBubbleView) getChildAt(i);
//            itemView.textView.setTypeface(typeface);
//        }
    }

    void setupOverlay(float originX, float originY, MyBubbleActions bubbleActions) {
        tv = new TextView(context);
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setTextSize(52);
        addView(tv);
        numActions = bubbleActions.numActions;
        if (numActions > MAX_ACTIONS) {
            throw new IllegalArgumentException(TAG + ": actions cannot have more than " + MAX_ACTIONS + " actions. ");
        }

        if (bubbleActions.indicator != null) {
            bubbleActionIndicator.setImageDrawable(bubbleActions.indicator);
        } else {
            bubbleActionIndicator.setImageResource(R.drawable.bubble_actions_indicator);
        }

        contentClipRect.set(0, 0, getWidth(), getHeight());
        bubbleActionIndicator.setX(originX - (bubbleActionIndicator.getWidth() / 2.0f));
        bubbleActionIndicator.setY(originY - (bubbleActionIndicator.getHeight() / 2.0f));

        // check if we're too short on any of the sides
        double angleDelta = Math.PI / (numActions - 0.5);
        float requiredSpace = (float) Math.cos(angleDelta) * (stopActionDistanceFromCenter + bubbleDimension);
        boolean leftOk = contentClipRect.contains(originX - requiredSpace, originY);
        boolean rightOk = contentClipRect.contains(originX + requiredSpace, originY);

        // if this statement is true then we don't have enough space on the sides
        if (!leftOk && !rightOk) {
            throw new IllegalStateException(MyBubbleActionOverlay.class.toString() + ": view has no space to expand actions.");
        }

        // Kind of tricky logic
        double startingAngle;
        if (rightOk && leftOk) {
            startingAngle = Math.PI + angleDelta / 2;
        } else if (rightOk) {
            startingAngle = -Math.acos((contentClipRect.left - originX) / (stopActionDistanceFromCenter + bubbleDimension));
        } else {
            startingAngle = -Math.acos((contentClipRect.right - originX) / (stopActionDistanceFromCenter + bubbleDimension)) - (numActions - 1) * angleDelta;
        }

        // this looks a little complicated, but it's necessary to maintain the correct z ordering
        // so that the labels do not appear underneath a bubble
        double angle = startingAngle;
        int actionIndex = 0;
        int start = rightOk ? 0 : numActions - 1;
        int end = rightOk ? numActions : -1;
        int delta = rightOk ? 1 : -1;
        for (int i = start; i != end; i += delta) {
            MyBubbleView bubbleView = (MyBubbleView) getChildAt(i + 1);
            MyBubbleActions.Action action = bubbleActions.actions[actionIndex];
            bubbleView.imageView.setImageDrawable(action.bubble);
            bubbleView.callback = action.callback;
            float halfWidth = bubbleView.getWidth() / 2.0f;
            float halfHeight = bubbleView.getHeight() / 2.0f;
            float cosAngle = (float) Math.cos(angle);
            float sinAngle = (float) Math.sin(angle);
            actionEndX[i] = originX + stopActionDistanceFromCenter * cosAngle - halfWidth;
            actionEndY[i] = originY + stopActionDistanceFromCenter * sinAngle - halfHeight;
            actionStartX[i] = originX + startActionDistanceFromCenter * cosAngle - halfWidth;
            actionStartY[i] = originY + startActionDistanceFromCenter * sinAngle - halfHeight;
            bubbleView.setX(actionStartX[i]);
            bubbleView.setY(actionStartY[i]);
            angle += angleDelta;
            if (delta < 0)
                bubbleView.setIndex(4 - i);
            else
                bubbleView.setIndex(i);
            actionIndex++;
        }
       // if (actionStartX[3] > actionStartX[1]) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = (Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            params.setMargins(10,250,10,250);
            tv.setLayoutParams(params);
            tv.setGravity(Gravity.CENTER);
            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "Montserrat-Medium.otf");
            tv.setTypeface(myTypeface);
//        } else {
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//            params.gravity = (Gravity.TOP | Gravity.LEFT);
//            params.setMargins(10,250,10,250);
//            tv.setLayoutParams(params);
//            tv.setGravity(Gravity.CENTER);
//            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "Montserrat-Medium.otf");
//            tv.setTypeface(myTypeface);
//        }
    }

    void showOverlay() {
        startDrag(dragData, dragShadowBuilder, null, 0);
    }

    boolean dragStarted(DragEvent event) {
        // There is a bug in v17 and below where the text won't appear because it hasn't
        // been measured properly
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            requestLayout();
        }

        if (event.getClipDescription().getLabel().equals(TAG)) {
            if (!overlayActive) {
                overlayActive = true;
                backgroundAnimator.start();
                ViewCompat.animate(bubbleActionIndicator)
                        .alpha(1f)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(null);
                for (int i = 0; i < numActions; i++) {
                    final MyBubbleView child = (MyBubbleView) getChildAt(i + 1);
                    child.setVisibility(VISIBLE);
                    ViewCompat.animate(child)
                            .translationX(actionEndX[i])
                            .translationY(actionEndY[i])
                            .alpha(1f)
                            .setInterpolator(overshootInterpolator)
                            .setListener(new ViewPropertyAnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(View view) {
                                    super.onAnimationEnd(view);
                                    child.animatedIn = true;
                                }
                            })
                            .setDuration(ANIMATION_DURATION);
                }
            }
            return true;
        }

        return false;
    }

    boolean dragEnded(final MyBubbleActions bubbleActions) {
        if (overlayActive) {
            overlayActive = false;
            ViewCompat.animate(bubbleActionIndicator)
                    .alpha(0f)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(View view) {
                            super.onAnimationStart(view);
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            super.onAnimationEnd(view);
                            bubbleActions.hideOverlay();
                        }
                    });

            for (int i = 0; i < numActions; i++) {
                final MyBubbleView child = (MyBubbleView) getChildAt(i + 1);
                //  child.setIndex(i);
                ViewCompat.animate(child)
                        .translationX(actionStartX[i])
                        .translationY(actionStartY[i])
                        .alpha(0f)
                        .setInterpolator(null)
                        .setListener(new ViewPropertyAnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(View view) {
                                super.onAnimationEnd(view);
                                child.setVisibility(INVISIBLE);
                                child.resetChildren();
                                child.animatedIn = false;
                            }
                        })
                        .setDuration(ANIMATION_DURATION);
            }
            backgroundAnimator.reverse();
        }
        return true;
    }
}

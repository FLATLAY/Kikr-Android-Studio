package com.flatlay.utility;

/**
 * Created by RachelDi on 1/26/18.
 */

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sam.bubbleactions.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyBubbleActions {

    private static final String TAG = MyBubbleActions.class.getSimpleName();

    public interface Callback {
        void doAction();
    }

    private ViewGroup root;
    private MyBubbleActionOverlay overlay;
    private Method getLastTouchPoint;
    private Object viewRootImpl;
    private Point touchPoint = new Point();
    private boolean showing = false;
    MyBubbleActions.Action[] actions = new MyBubbleActions.Action[MyBubbleActionOverlay.MAX_ACTIONS];
    int numActions = 0;
    Drawable indicator;

    private MyBubbleActions(ViewGroup root) {
        this.indicator = ResourcesCompat.getDrawable(root.getResources(), R.drawable.bubble_actions_indicator, root.getContext().getTheme());
        this.root = root;
        overlay = new MyBubbleActionOverlay(root.getContext());
        overlay.setOnDragListener(overlayDragListener);
        // Use reflection to get the ViewRootImpl
        try {
            Method method = root.getClass().getMethod("getViewRootImpl");
            viewRootImpl = method.invoke(root);
            getLastTouchPoint = viewRootImpl.getClass().getMethod("getLastTouchPoint", Point.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open up BubbleActions on a view.
     *
     * @param view the view that the BubbleActions are contextually connected to. The
     *             view must have a root view.
     * @return a BubbleActions instance
     */
    public static MyBubbleActions on(View view) {
        View rootView = view.getRootView();
        if (rootView == null) {
            throw new IllegalArgumentException("View argument must have a root view.");
        }

        if (!(rootView instanceof ViewGroup)) {
            throw new IllegalArgumentException("View argument must have a ViewGroup root view");
        }


        return new MyBubbleActions((ViewGroup) rootView);
    }

    /**
     * Set the typeface of the labels for the BubbleActions.
     *
     * @param typeface the typeface to set on the labels
     * @return the BubbleActions instance that called this method
     */
    public MyBubbleActions withTypeface(Typeface typeface) {
        overlay.setLabelTypeface(typeface);
        return this;
    }

    /**
     * Set the indicator drawable. The default is a semi-transparent circle.
     *
     * @param indicatorRes drawable resource id to be drawn indicating what the bubble actions
     *                     are acting on
     * @return the BubbleActions instance that called this method
     */
    public MyBubbleActions withIndicator(int indicatorRes) {
        this.indicator = ResourcesCompat.getDrawable(root.getResources(), indicatorRes, root.getContext().getTheme());
        return this;
    }

    /**
     * Set the indicator drawable. The default is a semi-transparent circle.
     *
     * @param indicator drawable to be drawn indicating what the bubble actions are acting on
     * @return the BubbleActions instance that called this method
     */
    public MyBubbleActions withIndicator(Drawable indicator) {
        this.indicator = indicator;
        return this;
    }

    public MyBubbleActions addAction(CharSequence actionName, int drawableRes, MyBubbleActions.Callback callback) {
        Resources resources = root.getResources();
        Resources.Theme theme = root.getContext().getTheme();
        addAction(actionName, ResourcesCompat.getDrawable(resources, drawableRes, theme), callback);
        return this;
    }

    public MyBubbleActions addAction(CharSequence actionName, Drawable drawable, MyBubbleActions.Callback callback) {
        if (numActions >= actions.length) {
            throw new IllegalStateException(TAG + ": cannot add more than " + MyBubbleActionOverlay.MAX_ACTIONS + " actions.");
        }

        if (drawable == null) {
            throw new IllegalArgumentException(TAG + ": the drawable cannot resolve to null.");
        }

        if (callback == null) {
            throw new IllegalArgumentException(TAG + ": the callback must not be null.");
        }

        actions[numActions] = new MyBubbleActions.Action(actionName, drawable, callback);
        numActions++;

        return this;
    }

    /**
     * Show the bubble actions. Internally this will do 3 things:
     *      1. Add the overlay to the root view
     *      2. Use reflection to get the last touched xy location
     *      3. Animate the overlay in
     */
    public void show() {
        if (showing) {
            return;
        }

        if (overlay.getParent() == null) {
            root.addView(overlay);
        }

        if (ViewCompat.isLaidOut(overlay)) {
            setupAndShow();
        } else {
            overlay.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    setupAndShow();
                    overlay.removeOnLayoutChangeListener(this);
                }
            });
        }
    }

    private void setupAndShow() {
        // use reflection to get the last touched xy location
        try {
            getLastTouchPoint.invoke(viewRootImpl, touchPoint);
            overlay.setupOverlay(touchPoint.x, touchPoint.y, this);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }
        showing = true;
        overlay.showOverlay();
    }

    void hideOverlay() {
        showing = false;
        root.removeView(overlay);
    }

    private View.OnDragListener overlayDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return overlay.dragStarted(event);
                case DragEvent.ACTION_DROP:
                    return overlay.dragEnded(MyBubbleActions.this);
                case DragEvent.ACTION_DRAG_ENDED:
                    return overlay.dragEnded(MyBubbleActions.this);
            }

            return false;
        }
    };

    /**
     * An abstraction of the bubble action. Each action has a name, a drawable for the bubble,
     * as well as a callback.
     */
    static class Action {
        CharSequence actionName;
        Drawable bubble;
        MyBubbleActions.Callback callback;

        private Action(CharSequence actionName, Drawable bubble, MyBubbleActions.Callback callback) {
            this.actionName = actionName;
            this.bubble = bubble;
            this.callback = callback;
        }
    }

}


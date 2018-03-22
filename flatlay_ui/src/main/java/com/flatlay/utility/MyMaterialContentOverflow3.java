package com.flatlay.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flatlay.R;
import com.materialcontentoverflow.TintFloatingActionButton;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by RachelDi on 2/19/18.
 */

public class MyMaterialContentOverflow3 extends FrameLayout {

    private static final String INSTANCE_KEY = "com.materialcontentoverflow.INSTANCE_KEY";
    public static final int LEFT = 0, CENTER = 1, RIGHT = 2;
//    private TintFloatingActionButton fab;
    private FrameLayout contentFrame;
    private ImageView backarrow90;
    private MyOverFlowGesListener2 overflowGestureListener;
    private float initialYPosition;
    private int fabMargin, index = -1, fabTotalHeight;

    public MyMaterialContentOverflow3(Context context) {
        super(context);
        init(context, null, 0, 0);
        Log.e("overflow-create", "1");
    }

    public MyMaterialContentOverflow3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
        Log.e("overflow-create", "2");
    }

    public MyMaterialContentOverflow3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
        Log.e("overflow-create", "3");
    }

    @TargetApi(21)
    public MyMaterialContentOverflow3(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Log.e("overflow-init", "1");
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.MaterialContentOverflow,
                defStyleAttr, 0);

        int buttonDrawable = 0;
        int buttonColor = 0;
        int contentColor = 0;
        int buttonPosition = 0;

        try {
            Log.e("overflow-init", "2");
            buttonDrawable = a.getResourceId(R.styleable.MaterialContentOverflow_buttonDrawable, 0);
            buttonColor = a.getResourceId(R.styleable.MaterialContentOverflow_fabButtonColor, 0);
            contentColor = a.getResourceId(R.styleable.MaterialContentOverflow_contentColor, 0);
            buttonPosition = a.getInt(R.styleable.MaterialContentOverflow_buttonPosition, 0);
        } finally {
            Log.e("overflow-init", "3");

            a.recycle();
            makeView(context, buttonDrawable, buttonColor, contentColor, buttonPosition);
        }
    }

    public void makeView(Context context, int buttonDrawable, int buttonColor, int contentColor, int buttonPosition) {
        Log.e("overflow-makeview", "1");
        FrameLayout contentFrame = createContentFrame(context, contentColor);

//        FloatingActionButton fab = createFab(context, buttonDrawable, buttonColor, buttonPosition);

        FrameLayout layout1 = new FrameLayout(context);
        backarrow90 = new ImageView(context);
        backarrow90.setImageResource(R.drawable.backarrow90);
        layout1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                overflowGestureListener.slide(-350);
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT , 100);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                70, 70);
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        params2.gravity = Gravity.CENTER;
        layout1.addView(backarrow90,params2);
        this.addView(layout1, params);
        overflowGestureListener = new MyOverFlowGesListener2(this, backarrow90);
        layout1.setOnTouchListener(overflowGestureListener.getMotionEvent());

        contentFrame.setOnTouchListener(overflowGestureListener.getMotionEvent());
    }

    private FrameLayout createContentFrame(Context context, int contentColor) {
        Log.e("overflow-framelayout", "1");

        if (contentFrame == null) {
            Log.e("overflow-framelayout", "2");

            contentFrame = new FrameLayout(this.getContext());
            contentFrame.setTag("FRAME");
        }

        int contentElevationInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        LayoutParams contentLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        contentLayoutParams.gravity = Gravity.BOTTOM;

        contentFrame.setBackgroundColor(Color.TRANSPARENT);

        contentFrame.setLayoutParams(contentLayoutParams);

        ViewCompat.setElevation(contentFrame, contentElevationInPixels);

        this.addView(contentFrame);

        return contentFrame;
    }


//    private FloatingActionButton createFab(Context context, int buttonDrawable, int buttonColor, int buttonPosition) {
//        Log.e("overflow-actionbutton", "1");
//
//        fab = new TintFloatingActionButton(context);
//
//        int fabElevationInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
//
//        LayoutParams fabLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        fabMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
//
//        if (buttonPosition == RIGHT) {
//            Log.e("overflow-actionbutton", "2");
//
//            fabLayoutParams.gravity = Gravity.END | Gravity.BOTTOM;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                Log.e("overflow-actionbutton", "3");
//
//                fabLayoutParams.setMarginEnd(fabMargin);
//            } else {
//                Log.e("overflow-actionbutton", "4");
//
//                fabLayoutParams.rightMargin = fabMargin;
//            }
//
//        } else if (buttonPosition == CENTER) {
//            Log.e("overflow-actionbutton", "5");
//
//            fabLayoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
//        } else {
//            Log.e("overflow-actionbutton", "6");
//
//            fabLayoutParams.gravity = Gravity.START | Gravity.BOTTOM;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                Log.e("overflow-actionbutton", "7");
//
//                fabLayoutParams.setMarginStart(fabMargin);
//            } else {
//                Log.e("overflow-actionbutton", "8");
//
//                fabLayoutParams.leftMargin = fabMargin;
//            }
//
//        }
//
//        fabLayoutParams.bottomMargin = fabMargin;
//        fabLayoutParams.topMargin = fabMargin;
//
//        if (buttonDrawable > 0) {
//            Log.e("overflow-actionbutton", "9");
//
//            fab.setImageDrawable(ContextCompat.getDrawable(context, buttonDrawable));
//        }
//
//        if (buttonColor > 0) {
//            Log.e("overflow-actionbutton", "10");
//
//            ViewCompat.setBackgroundTintList(fab, ContextCompat.getColorStateList(context, buttonColor));
//        }
//
//        ViewCompat.setElevation(fab, fabElevationInPixels);
//
//        fab.setLayoutParams(fabLayoutParams);
//
//        fab.setTag("FAB");
//
//        this.addView(fab);
//
//        fab.setVisibility(INVISIBLE);
//
//        return fab;
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("overflow-onmeasure", "1");

        setPadding(0, 0, 0, 0);

        int utilizedWidth = 0;

        int utilizedHeight = 0;

//        MarginLayoutParams fabLayoutParams = (MarginLayoutParams) fab.getLayoutParams();
//
//        measureChildWithMargins(
//                fab,
//                widthMeasureSpec,
//                utilizedWidth,
//                heightMeasureSpec,
//                utilizedHeight);
//
//        utilizedHeight = (fab.getMeasuredHeight() / 2) + fabLayoutParams.topMargin;


        measureChildWithMargins(
                contentFrame,
                widthMeasureSpec,
                utilizedWidth,
                heightMeasureSpec,
                utilizedHeight);

        utilizedHeight = contentFrame.getMeasuredHeight();

        setMeasuredDimension(
                resolveSize(contentFrame.getMeasuredWidth(), widthMeasureSpec),
                resolveSize(utilizedHeight, heightMeasureSpec));

    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        Log.e("overflow-addView", "1");

        if (child.getTag() == null) {
            Log.e("overflow-addView", "2");

            child.setTag("FOO");
        }
        if (!child.getTag().equals("FAB")) {
            Log.e("overflow-addView", "3");

            if (!child.getTag().equals("FRAME")) {
                Log.e("overflow-addView", "4");

                if (contentFrame == null) {
                    Log.e("overflow-addView", "5");

                    contentFrame = new FrameLayout(this.getContext());
                    contentFrame.setTag("FRAME");
                }
                contentFrame.addView(child, index, params);
            } else {
                Log.e("overflow-addView", "6");

                super.addView(child, index, params);
            }
        } else {
            Log.e("overflow-addView", "7");

            super.addView(child, index, params);
        }
        if (child.getTag().equals("FOO")) {
            Log.e("overflow-addView", "8");

            child.setTag(null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e("overflow-layout", "1");

        fabTotalHeight = backarrow90.getHeight() +
                fabMargin + /*bottom margin*/
                fabMargin;  /*top margin*/

        initialYPosition = getInitialYPosition();
        Log.e("overflow-layouttt1", String.valueOf(initialYPosition));

        Log.e("overflow-slide","1--"+overflowGestureListener.isOpened());
        if (overflowGestureListener.isOpened()) {
            Log.e("overflow-layout", "2");

            ViewHelper.setY(this, 0f);
        } else {
            Log.e("overflow-layout", "3");

            ViewHelper.setY(this, initialYPosition);
        }

        if (!overflowGestureListener.isOpened()) {
            Log.e("overflow-layout", "4");

            overflowGestureListener.setInitialYPosition(initialYPosition);
        }

//        if (!isInEditMode()) {
//            Log.e("HI", "hi30");
//
//            //measured on device
//            contentFrame.setPadding(0, fabTotalHeight / 2, 0, 0);
//        } else {
//            Log.e("HI", "hi31");
//
//            //measured on visual editor
//            contentFrame.setPadding(0, fabTotalHeight, 0, 0);
//        }

        Log.e("overflow-onlayout", String.valueOf(changed));

        super.onLayout(changed, left, top, right, bottom);
    }

    private float getInitialYPosition() {
        Log.e("overflow-ypos", "1");

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                fabTotalHeight * 2 / 3 - ((ViewGroup) this.getParent()).getHeight(),
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e("overflow-detached", "1");

        super.onDetachedFromWindow();
        overflowGestureListener.clearReferences();
//        ViewCompat.setBackgroundTintList(fab, null);
//        fab.setImageResource(0);
//        fab.setImageDrawable(null);
//        fab.setImageBitmap(null);
//        fab = null;
        contentFrame = null;
    }

    public void triggerSlide() {
        overflowGestureListener.slide(0);
    }

    public void setOpen() {
        Log.e("overflow-layout", "SETOPEN");

        overflowGestureListener.setIsOpened(true);
    }

    public boolean isOpen(){
        return overflowGestureListener.isOpened();
    }
}

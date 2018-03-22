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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flatlay.R;
import com.materialcontentoverflow.TintFloatingActionButton;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by RachelDi on 2/20/18.
 */

public class MyMaterialContentOverflow4 extends FrameLayout {

    private static final String INSTANCE_KEY = "com.materialcontentoverflow.INSTANCE_KEY";

    public static final int LEFT = 0;

    public static final int CENTER = 1;

    public static final int RIGHT = 2;

//    private TintFloatingActionButton fab;

    private FrameLayout contentFrame;

    private MyOverFlowGesListener overflowGestureListener;
    private int fabTotalHeight;
    private float initialYPosition;
    private int fabMargin;
    private int index = -1;
    private ImageView backarrow90;


    public MyMaterialContentOverflow4(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public MyMaterialContentOverflow4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public MyMaterialContentOverflow4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public MyMaterialContentOverflow4(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.MaterialContentOverflow,
                defStyleAttr, 0);

        int buttonDrawable = 0;
        int buttonColor = 0;
        int contentColor = 0;
        int buttonPosition = 0;

        try {

            buttonDrawable = a.getResourceId(R.styleable.MaterialContentOverflow_buttonDrawable, 0);
            buttonColor = a.getResourceId(R.styleable.MaterialContentOverflow_fabButtonColor, 0);
            contentColor = a.getResourceId(R.styleable.MaterialContentOverflow_contentColor, 0);
            buttonPosition = a.getInt(R.styleable.MaterialContentOverflow_buttonPosition, 0);
        } finally {

            a.recycle();
            makeView(context, buttonDrawable, buttonColor, contentColor, buttonPosition);
        }
    }

    public void makeView(Context context, int buttonDrawable, int buttonColor, int contentColor, int buttonPosition) {

        FrameLayout contentFrame = createContentFrame(context, contentColor);

//        FloatingActionButton fab = createFab(context, buttonDrawable, buttonColor, buttonPosition);
        FrameLayout layout1 = new FrameLayout(context);

        backarrow90 = new ImageView(context);
        backarrow90.setImageResource(R.drawable.backarrow90);
        backarrow90.setOnClickListener(new OnClickListener() {
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
        overflowGestureListener = new MyOverFlowGesListener(this,backarrow90);
        layout1.setOnTouchListener(overflowGestureListener.getMotionEvent());

        contentFrame.setOnTouchListener(overflowGestureListener.getMotionEvent());
    }

    private FrameLayout createContentFrame(Context context, int contentColor) {

        if (contentFrame == null) {

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
//        fab = new TintFloatingActionButton(context);
//
//        int fabElevationInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
//
//        LayoutParams fabLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        fabMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
//
//        if (buttonPosition == RIGHT) {
//            fabLayoutParams.gravity = Gravity.END | Gravity.BOTTOM;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                fabLayoutParams.setMarginEnd(fabMargin);
//            } else {
//                fabLayoutParams.rightMargin = fabMargin;
//            }
//
//        } else if (buttonPosition == CENTER) {
//            fabLayoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
//        } else {
//            fabLayoutParams.gravity = Gravity.START | Gravity.BOTTOM;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                fabLayoutParams.setMarginStart(fabMargin);
//            } else {
//                fabLayoutParams.leftMargin = fabMargin;
//            }
//
//        }
//
//        fabLayoutParams.bottomMargin = fabMargin;
//        fabLayoutParams.topMargin = fabMargin;
//
//        if (buttonDrawable > 0) {
//
//            fab.setImageDrawable(ContextCompat.getDrawable(context, buttonDrawable));
//        }
//
//        if (buttonColor > 0) {
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
        if (child.getTag() == null) {
            child.setTag("FOO");
        }
        if (!child.getTag().equals("FAB")) {
            if (!child.getTag().equals("FRAME")) {
                if (contentFrame == null) {
                    contentFrame = new FrameLayout(this.getContext());
                    contentFrame.setTag("FRAME");
                }
                contentFrame.addView(child, index, params);
            } else {
                super.addView(child, index, params);
            }
        } else {
            super.addView(child, index, params);
        }
        if (child.getTag().equals("FOO")) {
            child.setTag(null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        fabTotalHeight = backarrow90.getHeight() +
                fabMargin + /*bottom margin*/
                fabMargin;  /*top margin*/

        initialYPosition = getInitialYPosition();

        if (overflowGestureListener.isOpened()) {
            ViewHelper.setY(this, -300f);
        } else {
            ViewHelper.setY(this, initialYPosition);
        }

        if (!overflowGestureListener.isOpened()) {
            overflowGestureListener.setInitialYPosition(initialYPosition);
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    private float getInitialYPosition() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                fabTotalHeight*10/5  - ((ViewGroup) this.getParent()).getHeight(),
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onDetachedFromWindow() {
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

    public void triggerHide() {
        overflowGestureListener.slide(-350);
    }

    public void setOpen(){
        overflowGestureListener.setIsOpened(true);
    }

}
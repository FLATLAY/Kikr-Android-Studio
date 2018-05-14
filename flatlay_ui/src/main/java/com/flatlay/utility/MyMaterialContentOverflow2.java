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
import android.widget.TextView;

import com.flatlay.R;
import com.materialcontentoverflow.TintFloatingActionButton;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by RachelDi on 2/15/18.
 */

public class MyMaterialContentOverflow2 extends FrameLayout {

    private static final String INSTANCE_KEY = "com.materialcontentoverflow.INSTANCE_KEY";

    public static final int LEFT = 0;

    public static final int CENTER = 1;

    public static final int RIGHT = 2;

//    private TintFloatingActionButton fab;

    private FrameLayout contentFrame;

    private MyOverFlowGesListener2 overflowGestureListener;
    private int fabTotalHeight;
    private float initialYPosition;
    private int fabMargin;
    private int index = -1;
    private ImageView backarrow90;
    private TextView productDetail;
    private boolean isFocus;


    public MyMaterialContentOverflow2(Context context) {
        super(context);
        init(context, null, 0, 0);

    }

    public MyMaterialContentOverflow2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public MyMaterialContentOverflow2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public MyMaterialContentOverflow2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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


        FrameLayout layout1 = new FrameLayout(context);

        backarrow90 = new ImageView(context);
        backarrow90.setImageResource(R.drawable.backarrow90);
        layout1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (overflowGestureListener.isOpened()) {
                    overflowGestureListener.slide(-350);
                    setClose();
                } else {
                    overflowGestureListener.slide(0);
                    setOpen();
                }
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 100);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                70, 70);
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        params2.gravity = Gravity.CENTER;
        layout1.addView(backarrow90, params2);
        this.addView(layout1, params);
        overflowGestureListener = new MyOverFlowGesListener2(this, backarrow90, new MyOverFlowGesListener2.OnCloseListener() {
            @Override
            public void onClose() {
            }
        });
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


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setPadding(0, 0, 0, 0);

        int utilizedWidth = 0;

        int utilizedHeight = 0;

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

        fabTotalHeight = 70;
        initialYPosition = getInitialYPosition();

        if (overflowGestureListener.isOpened() && !isFocus) {
            ViewHelper.setY(this, 0f);
        } else {
            if (!isFocus)
                ViewHelper.setY(this, initialYPosition + (float) (fabTotalHeight * 1.5));
            else
                ViewHelper.setY(this, initialYPosition + (float) (fabTotalHeight * 1.5));

        }

        if (!overflowGestureListener.isOpened()) {
            if (!isFocus)
                overflowGestureListener.setInitialYPosition((float) (fabTotalHeight * 1.5) - ((ViewGroup) this.getParent()).getHeight());
            else
                overflowGestureListener.setInitialYPosition((float) (fabTotalHeight * 1.5) - ((ViewGroup) this.getParent()).getHeight());


        }

        super.onLayout(changed, left, top, right, bottom);
    }

    private float getInitialYPosition() {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                -((ViewGroup) this.getParent()).getHeight(),
                getResources().getDisplayMetrics());
    }

    public void triggerSlide() {
        overflowGestureListener.slide(0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        overflowGestureListener.clearReferences();

        contentFrame = null;
    }

    public void getFocus() {
        this.isFocus = true;
    }

    public void loseFocus() {
        this.isFocus = false;

    }

    public void setOpen() {
        overflowGestureListener.setIsOpened(true);
    }

    public void setClose() {
        overflowGestureListener.setIsOpened(false);
    }


}

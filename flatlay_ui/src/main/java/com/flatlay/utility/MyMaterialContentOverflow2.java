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

    private TintFloatingActionButton fab;

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
        Log.e("create", "1");

    }

    public MyMaterialContentOverflow2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
        Log.e("create", "2");
    }

    public MyMaterialContentOverflow2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
        Log.e("create", "3");
    }

    @TargetApi(21)
    public MyMaterialContentOverflow2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Log.e("init", "1");
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.MaterialContentOverflow,
                defStyleAttr, 0);

        int buttonDrawable = 0;
        int buttonColor = 0;
        int contentColor = 0;
        int buttonPosition = 0;

        try {
            Log.e("init", "2");
            buttonDrawable = a.getResourceId(R.styleable.MaterialContentOverflow_buttonDrawable, 0);
            buttonColor = a.getResourceId(R.styleable.MaterialContentOverflow_fabButtonColor, 0);
            contentColor = a.getResourceId(R.styleable.MaterialContentOverflow_contentColor, 0);
            buttonPosition = a.getInt(R.styleable.MaterialContentOverflow_buttonPosition, 0);
        } finally {
            Log.e("init", "3");
            a.recycle();
            makeView(context, buttonDrawable, buttonColor, contentColor, buttonPosition);
        }
    }

    public void makeView(Context context, int buttonDrawable, int buttonColor, int contentColor, int buttonPosition) {
        Log.e("makeview", "1");
        FrameLayout contentFrame = createContentFrame(context, contentColor);

        FloatingActionButton fab = createFab(context, buttonDrawable, buttonColor, buttonPosition);
//productDetail=new TextView(context);
//        productDetail.setTextColor(Color.WHITE);
//        productDetail.setText("Product Detail");
//        productDetail
        FrameLayout layout1 = new FrameLayout(context);

        backarrow90 = new ImageView(context);
        backarrow90.setImageResource(R.drawable.backarrow90);
        layout1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (overflowGestureListener.isOpened()) {
                    Log.e("isOpened", "isOpened");
                    overflowGestureListener.slide(-350);
//                    overflowGestureListener.setInitialYPosition(getInitialYPosition());
                    setClose();
                }
                else {
                    Log.e("isOpened", "isNotOpened");
                    overflowGestureListener.slide(0);
                    setOpen();
                }
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT , 100);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                50, 50);
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;
        params2.gravity = Gravity.CENTER;
        layout1.addView(backarrow90,params2);
        this.addView(layout1, params);
        overflowGestureListener = new MyOverFlowGesListener2(this, backarrow90);
        // fab.setOnTouchListener(overflowGestureListener.getMotionEvent());

        contentFrame.setOnTouchListener(overflowGestureListener.getMotionEvent());
    }

    private FrameLayout createContentFrame(Context context, int contentColor) {
        Log.e("framelayout", "1");
        if (contentFrame == null) {
            Log.e("framelayout", "2");
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


    private FloatingActionButton createFab(Context context, int buttonDrawable, int buttonColor, int buttonPosition) {
        Log.e("actionbutton", "1");

        fab = new TintFloatingActionButton(context);

        int fabElevationInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());

        LayoutParams fabLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        fabMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());

        if (buttonPosition == RIGHT) {
            Log.e("actionbutton", "2");
            fabLayoutParams.gravity = Gravity.END | Gravity.BOTTOM;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Log.e("actionbutton", "3");
                fabLayoutParams.setMarginEnd(fabMargin);
            } else {
                Log.e("actionbutton", "4");
                fabLayoutParams.rightMargin = fabMargin;
            }

        } else if (buttonPosition == CENTER) {
            Log.e("actionbutton", "5");
            fabLayoutParams.gravity = Gravity.CENTER | Gravity.BOTTOM;
        } else {
            Log.e("actionbutton", "6");
            fabLayoutParams.gravity = Gravity.START | Gravity.BOTTOM;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Log.e("actionbutton", "7");
                fabLayoutParams.setMarginStart(fabMargin);
            } else {
                Log.e("actionbutton", "8");
                fabLayoutParams.leftMargin = fabMargin;
            }

        }

        fabLayoutParams.bottomMargin = fabMargin;
        fabLayoutParams.topMargin = fabMargin;

        if (buttonDrawable > 0) {
            Log.e("actionbutton", "9");
            fab.setImageDrawable(ContextCompat.getDrawable(context, buttonDrawable));
        }

        if (buttonColor > 0) {
            Log.e("actionbutton", "10");
            ViewCompat.setBackgroundTintList(fab, ContextCompat.getColorStateList(context, buttonColor));
        }

        ViewCompat.setElevation(fab, fabElevationInPixels);

        fab.setLayoutParams(fabLayoutParams);

        fab.setTag("FAB");

        this.addView(fab);

        fab.setVisibility(INVISIBLE);

        return fab;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e("onmeasure", "1");

        setPadding(0, 0, 0, 0);

        int utilizedWidth = getPaddingLeft() + getPaddingRight();

        int utilizedHeight = getPaddingTop() + getPaddingBottom();

        MarginLayoutParams fabLayoutParams = (MarginLayoutParams) fab.getLayoutParams();

        measureChildWithMargins(
                fab,
                widthMeasureSpec,
                utilizedWidth,
                heightMeasureSpec,
                utilizedHeight);

        utilizedHeight += (fab.getMeasuredHeight() / 2) + fabLayoutParams.topMargin;


        measureChildWithMargins(
                contentFrame,
                widthMeasureSpec,
                utilizedWidth,
                heightMeasureSpec,
                utilizedHeight);

        utilizedHeight += contentFrame.getMeasuredHeight();
        Log.e("layouttt3", String.valueOf(contentFrame.getMeasuredWidth()));

        Log.e("layouttt4", String.valueOf(widthMeasureSpec));
        Log.e("layouttt5", String.valueOf(utilizedHeight));
        Log.e("layouttt6", String.valueOf(heightMeasureSpec));

        setMeasuredDimension(
                resolveSize(contentFrame.getMeasuredWidth(), widthMeasureSpec),
                resolveSize(utilizedHeight, heightMeasureSpec));

    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        Log.e("addView", "1");
        if (child.getTag() == null) {
            Log.e("addView", "2");
            child.setTag("FOO");
        }
        if (!child.getTag().equals("FAB")) {
            Log.e("addView", "3");
            if (!child.getTag().equals("FRAME")) {
                Log.e("addView", "4");
                if (contentFrame == null) {
                    Log.e("addView", "5");
                    contentFrame = new FrameLayout(this.getContext());
                    contentFrame.setTag("FRAME");
                }
                contentFrame.addView(child, index, params);
            } else {
                Log.e("addView", "6");
                super.addView(child, index, params);
            }
        } else {
            Log.e("addView", "7");
            super.addView(child, index, params);
        }
        if (child.getTag().equals("FOO")) {
            Log.e("addView", "8");
            child.setTag(null);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.e("layout", "1");
        fabTotalHeight = fab.getHeight() +
                fabMargin + /*bottom margin*/
                fabMargin;  /*top margin*/

        initialYPosition = getInitialYPosition();
        Log.e("layouttt1", String.valueOf(initialYPosition));


        if (overflowGestureListener.isOpened() && !isFocus) {
            Log.e("layout", "2");
            ViewHelper.setY(this, 0f);
        } else {
            Log.e("layout", "3");
            if (!isFocus)
                ViewHelper.setY(this, initialYPosition + (float) (fabTotalHeight * 2.5));
            else
                ViewHelper.setY(this, initialYPosition + (float) (fabTotalHeight * 0.9));

        }

        if (!overflowGestureListener.isOpened()) {
            Log.e("layout", "4");
            if (!isFocus)
                overflowGestureListener.setInitialYPosition((float) (fabTotalHeight * 2.5) - ((ViewGroup) this.getParent()).getHeight());
            else
                overflowGestureListener.setInitialYPosition((float) (fabTotalHeight * 0.9) - ((ViewGroup) this.getParent()).getHeight());

            Log.e("layouttt2", String.valueOf(((ViewGroup) this.getParent()).getHeight()));

        }

        super.onLayout(changed, left, top, right, bottom);
    }

    private float getInitialYPosition() {
        Log.e("ypos", "1");
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                -((ViewGroup) this.getParent()).getHeight(),
                getResources().getDisplayMetrics());
    }

    public void triggerSlide() {
        Log.e("trigger", "1");
        overflowGestureListener.slide(0);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.e("detached", "1");
        super.onDetachedFromWindow();
        overflowGestureListener.clearReferences();
        ViewCompat.setBackgroundTintList(fab, null);
        fab.setImageResource(0);
        fab.setImageDrawable(null);
        fab.setImageBitmap(null);
        fab = null;
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

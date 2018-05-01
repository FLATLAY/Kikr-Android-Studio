package com.flatlay.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.flatlay.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by RachelDi on 3/30/18.
 */

public class MyMaterialContentBottom extends FrameLayout {
    private ImageView backarrow;
    private MyOverFlowGesListener3 listener;
    private FrameLayout contentFrame;
    private int fabTotalHeight;
    private float initialYPosition;
    private OnCloseListener closeListener;

    public MyMaterialContentBottom(@NonNull Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public MyMaterialContentBottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public MyMaterialContentBottom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public MyMaterialContentBottom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            makeView(context);
        }
    }

    public void makeView(Context context) {
        FrameLayout contentFrame = createContentFrame();
        FrameLayout layout1 = new FrameLayout(context);
        backarrow = new ImageView(context);
        backarrow.setImageResource(R.drawable.backarrow90_2);
        layout1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //change
                Log.e("MyOverFlowGesListener3", "bottom"+listener.isOpened());

                if (listener.isOpened())
                    listener.slide(-350);
                else
                    listener.slide(0);

            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 100);
        FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                70, 70);
        params.gravity = Gravity.CENTER | Gravity.TOP;
        params2.gravity = Gravity.CENTER;
        layout1.addView(backarrow, params2);
        this.addView(layout1, params);
        listener = new MyOverFlowGesListener3(this, backarrow, (FragmentActivity) this.getContext(), new MyOverFlowGesListener3.OnCloseListener() {
            @Override
            public void onClose() {
                closeListener.onClose();
            }

            @Override
            public void onOpen() {
                closeListener.onOpen();
            }
        });
        layout1.setOnTouchListener(listener.getMotionEvent());
        contentFrame.setOnTouchListener(listener.getMotionEvent());
    }

    private FrameLayout createContentFrame() {
        if (contentFrame == null) {

            contentFrame = new FrameLayout(this.getContext());
            contentFrame.setTag("FRAME");
        }
        int contentElevationInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

        LayoutParams contentLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        contentLayoutParams.gravity = Gravity.TOP;
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
        Log.e("MyOverFlowGesListener3", "bottom1"+listener.isOpened());

        fabTotalHeight = 70;

        initialYPosition = getInitialYPosition();
        Log.e("MyOverFlowGesListener3", "bottom7:"+initialYPosition);


        if (listener.isOpened()) {
            Log.e("MyOverFlowGesListener3", "bottom2"+listener.isOpened());

          //  ViewHelper.setY(this, initialYPosition + 600);
        } else {
            Log.e("MyOverFlowGesListener3", "bottom3"+listener.isOpened());

            ViewHelper.setY(this, initialYPosition);
        }

        if (!listener.isOpened()) {
            Log.e("MyOverFlowGesListener3", "bottom4"+listener.isOpened());

            listener.setInitialYPosition(initialYPosition);
        }
        Log.e("MyOverFlowGesListener3", "bottom5"+listener.isOpened());


        super.onLayout(changed, left, top, right, bottom);
    }

    private float getInitialYPosition() {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                ((ViewGroup) this.getParent()).getHeight() - 90,
                getResources().getDisplayMetrics());
    }


    @Override
    protected void onDetachedFromWindow() {
        Log.e("MyOverFlowGesListener3", "bottom6"+listener.isOpened());

        super.onDetachedFromWindow();
        listener.clearReferences();
        contentFrame = null;
    }

    public interface OnCloseListener {

        void onClose();
        void onOpen();
    }


    public void setOnCloseListener(OnCloseListener listener){
        this.closeListener=listener;
    }

}

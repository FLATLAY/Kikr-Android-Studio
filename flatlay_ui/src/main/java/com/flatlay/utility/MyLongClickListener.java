package com.flatlay.utility;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;

/**
 * Created by majl on 2017/11/7.
 */
public class MyLongClickListener {

    private static final String TAG = "LongClickUtils";

    /**
     * @param handler
     * @param longClickView
     * @param delayMillis
     * @param longClickListener
     */
    public static void setLongClick(final Handler handler, final View longClickView, final long delayMillis, final OnLongClickListener longClickListener) {
        longClickView.setOnTouchListener(new OnTouchListener() {
            private int TOUCH_MAX = 50;
            private int mLastMotionX;
            private int mLastMotionY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacks(r);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(mLastMotionX - x) > TOUCH_MAX
                                || Math.abs(mLastMotionY - y) > TOUCH_MAX) {
                            handler.removeCallbacks(r);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        handler.removeCallbacks(r);
                        mLastMotionX = x;
                        mLastMotionY = y;
                        handler.postDelayed(r, delayMillis);
                        break;
                }
                return true;
            }

            private Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (longClickListener != null) {
                        longClickListener.onLongClick(longClickView);
                    }
                }
            };
        });
    }

}
package com.flatlay.adapter;

import android.util.Log;

import com.flatlay.R;

/**
 * Created by RachelDi on 1/10/18.
 */

public enum ModleObject2 {
    INTRODUCTIONSCREEN(R.string.intruductionscreen, R.layout.introduction_screen),
    INTRODUCTIONSCREEN2(R.string.intruductionscreen2, R.layout.introduction_screen2),
    INTRODUCTIONSCREEN3(R.string.intruductionscreen3, R.layout.introduction_screen3),
    INTRODUCTIONSCREEN4(R.string.intruductionscreen4, R.layout.introduction_screen4);
    final String TAG = "ModleObject2";

    private int mTitleResId;
    private int mLayoutResId;

    ModleObject2(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
        Log.w(TAG, "ModleObject2");
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}

package com.kikr.adapter;

import com.kikr.R;

/**
 * Created by anupamchugh on 26/12/15.
 */
public enum ModelObject {

    FIRSTSCREEN(R.string.splashfirstscreen, R.layout.splashfirstscreen),
    SECONDSCREEN(R.string.splashsecondscreen, R.layout.splashsecondscreen),
    THIRDSCREEN(R.string.splashthirdscreen, R.layout.splashthirdscreen),
    FOURTHSCREEN(R.string.splashfourthscreen,R.layout.splashfourthscreen);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}

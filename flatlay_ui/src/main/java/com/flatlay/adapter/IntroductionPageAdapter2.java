package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.utility.FontUtility;

/**
 * Created by RachelDi on 1/10/18.
 */

public class IntroductionPageAdapter2 extends PagerAdapter {
    private FragmentActivity mContext;
   // int images[] = new int[]{R.layout.introduction_screen, R.layout.introduction_screen, R.layout.introduction_screen, R.layout.introduction_screen};
    LayoutInflater inflater;
    final String TAG = "IntroductionPageAda2";

    public IntroductionPageAdapter2(FragmentActivity mContext) {
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.w(TAG, "IntroductionPagerAdapter");
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModleObject2 modelObject = ModleObject2.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        TextView detailText = (TextView)layout.findViewById(R.id.detailText);
        TextView titleText = (TextView)layout.findViewById(R.id.titleText);
        detailText.setTypeface(FontUtility.setMontserratLight(mContext));
        titleText.setTypeface(FontUtility.setMontserratRegular(mContext));
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return ModleObject2.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModleObject2 customPagerEnum = ModleObject2.values()[position];

        return mContext.getString(customPagerEnum.getTitleResId());
    }

}

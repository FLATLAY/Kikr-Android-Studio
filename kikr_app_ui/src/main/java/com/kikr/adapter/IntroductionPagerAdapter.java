package com.kikr.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flatlay.R;

public class IntroductionPagerAdapter extends PagerAdapter {
	private FragmentActivity mContext;
	int images[] = new int[]{R.layout.splashfirstscreen,R.layout.splashfirstscreen,R.layout.splashfirstscreen,R.layout.splashfirstscreen};
	//String layoutfile[]=new String[]{R.layout.splashfirstscreen,};
	String colorList[] =new String[]{"#5F737B","#86DABD","#68B8A5","#BEEA61"};
	LayoutInflater inflater;

	public IntroductionPagerAdapter(FragmentActivity mContext) {
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object instantiateItem(ViewGroup collection, int position) {


		ModelObject modelObject = ModelObject.values()[position];
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
		collection.addView(layout);
		return layout;
	}

	@Override
	public void destroyItem(ViewGroup collection, int position, Object view) {
		collection.removeView((View) view);
	}

	@Override
	public int getCount() {
		return ModelObject.values().length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		ModelObject customPagerEnum = ModelObject.values()[position];

		return mContext.getString(customPagerEnum.getTitleResId());
	}

}
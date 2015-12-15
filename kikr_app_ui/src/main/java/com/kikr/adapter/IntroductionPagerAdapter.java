package com.kikr.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentProductDetailWebView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.Product;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

public class IntroductionPagerAdapter extends PagerAdapter {
	private FragmentActivity mContext;
	int images[] = new int[]{R.drawable.carousel_1,R.drawable.carousel_2,R.drawable.carousel_3,R.drawable.carousel_4};
	String colorList[] =new String[]{"#5F737B","#86DABD","#68B8A5","#BEEA61"};
	LayoutInflater inflater;

	public IntroductionPagerAdapter(FragmentActivity mContext) {
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	class ViewHolder {
		ImageView productImage;
		RelativeLayout mainLayout;
	}

	@Override
	public int getCount() {
		return images.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View collection, int position, Object view) {
		((ViewPager) collection).removeView((View) view);
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public Object instantiateItem(View container, final int position) {
		final ViewHolder holder;
		holder = new ViewHolder();
		View collection = inflater.inflate(R.layout.introduction_pager_image, null, false);
		holder.productImage = (ImageView) collection.findViewById(R.id.cat_imges);
		holder.mainLayout = (RelativeLayout) collection.findViewById(R.id.mainLayout);
		holder.productImage.setImageResource(images[position]);
		holder.mainLayout.setBackgroundColor(Color.parseColor(colorList[position]));
		((ViewPager) container).addView(collection, 0);
		return collection;
	}

}

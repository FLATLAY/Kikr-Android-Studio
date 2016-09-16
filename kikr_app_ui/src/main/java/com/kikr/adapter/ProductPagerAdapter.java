package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentProductDetailWebView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.Product;
import com.kikrlib.utils.Syso;

public class ProductPagerAdapter extends PagerAdapter {
	private List<String> productImages = new ArrayList<String>();
	private FragmentActivity mContext;
	private String url;
	private Product product;

	public ProductPagerAdapter(FragmentActivity mContext, List<String> images, String url, Product product) {
		this.mContext = mContext;
		this.productImages = images;
		this.url = url;
		this.product= product;
		Syso.info("instantiateItem>>>>>>>>>>>ProductPagerAdapter");
	}

	class ViewHolder {
		ImageView productImage;
	}

	@Override
	public int getCount() {
		return productImages.size();
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
		Syso.info("instantiateItem>>>>>>>>>>>"+position);
		final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final ViewHolder holder;
		holder = new ViewHolder();
		View collection = inflater.inflate(R.layout.pager_image, null, false);
		holder.productImage = (ImageView) collection.findViewById(R.id.cat_imges);
		CommonUtility.setImage(mContext, productImages.get(position), holder.productImage, R.drawable.dum_list_item_product);
		((ViewPager) container).addView(collection, 0);
		collection.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(((HomeActivity) mContext).checkInternet())
//					((HomeActivity) mContext).addFragment(new FragmentProductDetailWebView(url, product));
			}
		});
		return collection;
	}

}

package com.kikr.ui;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.flatlay.R;
import com.kikr.utility.CommonUtility;

import java.util.List;

public class ImageUI {
	FragmentActivity mContext;
	List<String> images;
	ViewPager viewPager;
	LinearLayout ll;

	public ImageUI(FragmentActivity context, List<String> images, ViewPager viewPager) {
		super();
		this.mContext = context;
		this.images=images;
		this.viewPager = viewPager;
	}

	public View getView() {
		ll =  new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		RelativeLayout.LayoutParams layoutParams =new RelativeLayout.LayoutParams((int) mContext.getResources().getDimension(R.dimen.image_list_size), (int) mContext.getResources().getDimension(R.dimen.image_list_size));
		layoutParams.setMargins(0,0,0,0);
		for (int i=0;i<images.size();i++) {
            RelativeLayout relativeLayout = new RelativeLayout(mContext);
			ImageView iv = new ImageView(mContext);
            ImageView iv2 = new ImageView(mContext);
			if(i==0)
                iv2.setAlpha(1.0f);
            else
                iv2.setAlpha(0.0f);

            iv2.setImageResource(R.drawable.shape);
			CommonUtility.setImage(mContext, images.get(i), iv, R.drawable.dum_list_item_brand);
			iv.setLayoutParams(layoutParams);
            iv2.setLayoutParams(layoutParams);
            relativeLayout.setTag(i);
            relativeLayout.addView(iv);
            relativeLayout.addView(iv2);
            ll.addView(relativeLayout);
            relativeLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int  pos = (int) v.getTag();
					viewPager.setCurrentItem(pos);
					changedAlpha(pos);
				}
			});
		}
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
				changedAlpha(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		return ll;
	}

	private void changedAlpha(int pos) {
		for(int i=0;i<ll.getChildCount();i++){
			if(i==pos)
                ((RelativeLayout) ll.getChildAt(i)).getChildAt(1).setAlpha(1.0f);
			else
                ((RelativeLayout) ll.getChildAt(i)).getChildAt(1).setAlpha(0.0f);
		}
	}
}

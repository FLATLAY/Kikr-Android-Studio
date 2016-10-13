package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.flatlay.R;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.BgImage;

public class LifestyleImageAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	List<String> bgImages = new ArrayList<String>();

	public LifestyleImageAdapter(Activity context, List<String> bgImages) {
		super();
		this.mContext = context;
		this.bgImages = bgImages;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return bgImages.size();
	}

	@Override
	public String getItem(int index) {
		return bgImages.get(index);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_lifestyle_images, null);
			viewHolder = new ViewHolder();
			viewHolder.lifestyleImage = (ImageView) convertView.findViewById(R.id.lifestyleImage);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CommonUtility.setImage(mContext, getItem(position), viewHolder.lifestyleImage, R.drawable.dum_list_item_product);
		return convertView;
	}

	public class ViewHolder {
		ImageView lifestyleImage;
	}

}

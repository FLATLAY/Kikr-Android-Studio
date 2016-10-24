package com.flatlay.activity;

import java.util.ArrayList;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.flatlay.R;
import com.flatlay.utility.CommonUtility;

public class PhotoListAdapter extends BaseAdapter {
	private FragmentActivity mContext;
	private ArrayList<String> mPhotoList;
	private int mWidth;
	private int mHeight;
	
	public PhotoListAdapter(FragmentActivity context) {
		mContext = context;
	}
	
	public void setData(ArrayList<String> data) {
		mPhotoList = data;
	}
	
	public void setLayoutParam(int width, int height) {
		mWidth 	= width;
		mHeight = height;
	}
	
	@Override
	public int getCount() {
		return (mPhotoList == null) ? 0 : mPhotoList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageIv;
		if (convertView == null) {
			imageIv = new ImageView(mContext);
			imageIv.setLayoutParams(new GridView.LayoutParams(mWidth, mHeight));
            imageIv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageIv.setPadding(0, 0, 0, 0); 
		} else {
			imageIv = (ImageView) convertView;
		}
		CommonUtility.setImage(mContext, mPhotoList.get(position), imageIv, R.drawable.dum_list_item_product);
		return imageIv;
	}
}
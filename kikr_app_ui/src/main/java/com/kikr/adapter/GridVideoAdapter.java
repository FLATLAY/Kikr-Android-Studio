package com.kikr.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kikr.R;

public class GridVideoAdapter extends BaseAdapter {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<Bitmap> bitmaps;

	public GridVideoAdapter(FragmentActivity context, List<Bitmap> bitmaps) {
		super();
		this.mContext = context;
		this.bitmaps = bitmaps;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setData(List<Bitmap> data){
		this.bitmaps=data;
	}

	@Override
	public int getCount() {
		return bitmaps.size();
	}

	@Override
	public Bitmap getItem(int index) {
		return bitmaps.get(index);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewholder;
		if (convertView==null) {
			convertView = mInflater.inflate(R.layout.adapter_grid_video,null);
			viewholder = new ViewHolder();
			viewholder.gridItem = (ImageView) convertView.findViewById(R.id.gridItem);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		if (getItem(position)!=null) {
			viewholder.gridItem.setImageBitmap(getItem(position));
		}
		return convertView;
	}
	
	public class ViewHolder {
		ImageView gridItem;
	}
	
}

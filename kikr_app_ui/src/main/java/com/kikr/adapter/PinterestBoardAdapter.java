package com.kikr.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.utility.CommonUtility;
import com.pinterest.android.pdk.PDKBoard;

import java.util.ArrayList;
import java.util.List;

public class PinterestBoardAdapter extends BaseAdapter {

	private Activity mContext;
	private LayoutInflater mInflater;
	List<PDKBoard> bgImages= new ArrayList<>();

	public PinterestBoardAdapter(Activity context, List<PDKBoard> bgImages) {
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
	public PDKBoard getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_pinterest_board_images, null);
			viewHolder = new ViewHolder();
			viewHolder.lifestyleImage = (ImageView) convertView.findViewById(R.id.lifestyleImage);
			viewHolder.boardName = (TextView) convertView.findViewById(R.id.boardName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.boardName.setText(getItem(position).getName());
		CommonUtility.setImage(mContext, getItem(position).getImageUrl(), viewHolder.lifestyleImage, R.drawable.dum_list_item_product);
		return convertView;
	}

	public class ViewHolder {
		ImageView lifestyleImage;
		TextView boardName;
	}

}

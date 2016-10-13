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
import android.widget.TextView;

import com.flatlay.R;
import com.kikrlib.bean.BankList;

public class AddBankAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private ArrayList<BankList> data;

	public AddBankAdapter(Activity context, List<BankList> data) {
		super();
		this.mContext = context;
		this.data = (ArrayList<BankList>) data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return data.size();
	}

	@Override
	public BankList getItem(int index) {
		return data.get(index);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_add_bank, null);
			viewHolder = new ViewHolder();
			viewHolder.bank_image = (com.kikr.ui.RoundImageView) convertView.findViewById(R.id.bank_image);
			viewHolder.bank_name = (TextView) convertView.findViewById(R.id.bank_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.bank_name.setText(getItem(position).getName());
//		viewHolder.bank_image.setImageResource(mContext.getResources().getIdentifier(images.get(position), "drawable",mContext.getPackageName()));
		return convertView;
	}

	public class ViewHolder {
		ImageView bank_image;
		TextView bank_name;
	}

}

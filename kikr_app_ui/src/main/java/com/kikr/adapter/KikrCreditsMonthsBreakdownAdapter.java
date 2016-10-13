package com.kikr.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikrlib.bean.Detail;

import java.util.ArrayList;
import java.util.List;

public class KikrCreditsMonthsBreakdownAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Detail> data = new ArrayList<Detail>();

	public KikrCreditsMonthsBreakdownAdapter(Activity context, List<Detail> data) {
		super();
		this.mContext = context;
		this.data = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Detail getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_kikr_credits_list, null);
			viewHolder = new ViewHolder();
			viewHolder.month_name = (TextView) convertView.findViewById(R.id.month_name);
			viewHolder.views_count = (TextView) convertView.findViewById(R.id.views_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(getItem(position).getType())) 
		viewHolder.month_name.setText(getItem(position).getReason());
		else
		viewHolder.month_name.setText("Unknown");
		if(!TextUtils.isEmpty(getItem(position).getAmount()))
		viewHolder.views_count.setText(getItem(position).getAmount()+" Credits");
		else
		viewHolder.views_count.setText("0 Credits");
		if (position%2==0) {
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.btn_greendark));
		}
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				addFragment(new FragmentKikrCreditMonthBreakdown(getItem(position).getMonth_name(),getItem(position).getYear_name(),UserPreference.getInstance().getUserID()));
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView month_name,views_count;
	}
	
	public void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}

}

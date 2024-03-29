package com.flatlay.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentKikrCreditMonthBreakdown;
import com.flatlaylib.bean.Credits;
import com.flatlaylib.db.UserPreference;

import java.util.ArrayList;
import java.util.List;

public class KikrCreditsMonthsAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Credits> data = new ArrayList<Credits>();

	public KikrCreditsMonthsAdapter(Activity context, List<Credits> data) {
		super();
		this.mContext = context;
		this.data = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.w("Activity","KikrCreditsMonthsAdapter");
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Credits getItem(int index) {
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
		if (!TextUtils.isEmpty(getItem(position).getMonth_name())) 
		viewHolder.month_name.setText(getItem(position).getMonth_name());
		else
		viewHolder.month_name.setText("Unknown");
		if(!TextUtils.isEmpty(getItem(position).getCredit()))
		viewHolder.views_count.setText(getItem(position).getCredit()+" Credits = $" + String.format("%.2f", Integer.parseInt(getItem(position).getCredit())/100.0));
		else
		viewHolder.views_count.setText("0 Credits = $0.00");
		if (position%2==0) {
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.btn_greendark));
		}
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addFragment(new FragmentKikrCreditMonthBreakdown(getItem(position).getMonth_name(),getItem(position).getYear_name(),UserPreference.getInstance().getUserID()));
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

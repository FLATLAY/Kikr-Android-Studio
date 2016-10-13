package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

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
import com.kikr.fragment.FragmentActivityPage;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.ActivityMonthList;
import com.kikrlib.bean.User;

public class ActivityMonthsAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<ActivityMonthList> data = new ArrayList<ActivityMonthList>();
	private User user;

	public ActivityMonthsAdapter(Activity context, List<ActivityMonthList> data, User user) {
		super();
		this.mContext = context;
		this.data = data;
		this.user = user;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public ActivityMonthList getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_activity_months, null);
			viewHolder = new ViewHolder();
			viewHolder.month_name = (TextView) convertView.findViewById(R.id.month_name);
			viewHolder.views_count = (TextView) convertView.findViewById(R.id.views_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(getItem(position).getMonth_name())&&!TextUtils.isEmpty(getItem(position).getYear_name())&&!TextUtils.isEmpty(getItem(position).getPayout())) 
		viewHolder.month_name.setText("Month: "+getItem(position).getMonth_name()+" "+getItem(position).getYear_name()+"\nPayout: "+CommonUtility.getFormatedNum(getItem(position).getPayout()+" Credits"));
		else
		viewHolder.month_name.setText("Unknown\nPayout: 0 Credits");
		if(!TextUtils.isEmpty(getItem(position).getCollection_views())&&!TextUtils.isEmpty(getItem(position).getProduct_views()))
		viewHolder.views_count.setText("Product Views: "+getItem(position).getProduct_views()+"\nCollection Views: "+getItem(position).getCollection_views());
		else
		viewHolder.views_count.setText("Product Views: 0\n Collection Views: 0");
//		if()
//		viewHolder.payout_text.setText("Payout: $"+getItem(position).getPayout());
//		else
//		viewHolder.payout_text.setText("Payout: $0");
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				addFragment(new FragmentActivityPage(getItem(position).getMonth_name(),getItem(position).getYear_name(),user));
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

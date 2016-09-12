package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentActivityPage;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.ActivityMonthList;
import com.kikrlib.bean.CollectionMonthDetailList;
import com.kikrlib.bean.User;

public class KikrCreditMonthAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> data = new ArrayList<String>();
	private User user;

	public KikrCreditMonthAdapter(Activity mContext,List<String> data) {
		super();
		this.mContext = mContext;
		this.data = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public String getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_breakdown, null);
			viewHolder = new ViewHolder();
			viewHolder.description = (TextView) convertView.findViewById(R.id.description);
			viewHolder.total_credits = (TextView) convertView.findViewById(R.id.total_credits);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.description.setText("referred john@yahoo.com");
		viewHolder.total_credits.setText("25 credits");
		
		return convertView;
	}

	public class ViewHolder {
		TextView description,total_credits;
	}
	
	public void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}

}

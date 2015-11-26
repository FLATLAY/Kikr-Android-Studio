package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.dialog.DialogCallback;

public class FitListAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> fitList = new ArrayList<String>();
	private DialogCallback dialogCallback;
	int selectedPos=-1;

	public FitListAdapter(Context context,DialogCallback dialogCallback, List<String> fitList, String selectedFit) {
		super();
		this.mContext = context;
		this.dialogCallback = dialogCallback;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(int i=0;i<fitList.size();i++){
			if(fitList.get(i).equals(selectedFit))
				selectedPos=i;
		}
		this.fitList = fitList;
	}

	@Override
	public int getCount() {
		return fitList.size();
	}

	@Override
	public Object getItem(int index) {
		return 0;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_color_list, null);
			viewHolder = new ViewHolder();
			viewHolder.colorName = (TextView) convertView.findViewById(R.id.colorName);
			viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(position==selectedPos)
			viewHolder.checkBox.setChecked(true);
		else
			viewHolder.checkBox.setChecked(false);
		
		viewHolder.colorName.setText(fitList.get(position));
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(((HomeActivity)mContext).checkInternet())
					dialogCallback.setFit(fitList.get(position));
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView colorName;
		CheckBox checkBox;
	}

}

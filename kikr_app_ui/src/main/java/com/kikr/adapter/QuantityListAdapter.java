package com.kikr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentEditPurchaseItem;
import com.kikrlib.bean.CollectionList;

public class QuantityListAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<String> quantity = new ArrayList<String>();
	private FragmentEditPurchaseItem editPurchaseItem;
	
	public QuantityListAdapter(Context context, FragmentEditPurchaseItem editPurchaseItem) {
		super();
		this.mContext = context;
		this.editPurchaseItem = editPurchaseItem;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		quantity.add("1");
		quantity.add("2");
		quantity.add("3");
		quantity.add("4");
		quantity.add("5");
		quantity.add("6");
		quantity.add("7");
	}
	
	public void setData(List<CollectionList> data){
//		this.data = data;
	}

	@Override
	public int getCount() {
		return 7;
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
			convertView = mInflater.inflate(R.layout.adapter_quantity_list, null);
			viewHolder = new ViewHolder();
			viewHolder.quantityText = (TextView) convertView.findViewById(R.id.quantityText);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.quantityText.setText(quantity.get(position));
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(((HomeActivity)mContext).checkInternet())
					editPurchaseItem.setQuantity(quantity.get(position));
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView quantityText;
	}

}

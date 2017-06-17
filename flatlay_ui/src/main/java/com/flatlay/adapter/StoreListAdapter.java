package com.flatlay.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentStoreDeals;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.NearByDeal;
import com.flatlaylib.db.DatabaseHelper;
import com.flatlaylib.db.dao.FavoriteDealsDAO;

public class StoreListAdapter extends BaseAdapter {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<NearByDeal> data;
	final FavoriteDealsDAO dao = new FavoriteDealsDAO(DatabaseHelper.getDatabase());

	public StoreListAdapter(FragmentActivity context, List<NearByDeal> data) {
		super();
		this.mContext = context;
		this.data = (ArrayList<NearByDeal>) data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.w("Activity","StoreListAdapter");
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public NearByDeal getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_deals, null);
			viewHolder = new ViewHolder();
			viewHolder.offer_image = (ImageView) convertView.findViewById(R.id.offer_image);
			viewHolder.offer_title = (TextView) convertView.findViewById(R.id.offer_title);
			viewHolder.favorite_image = (ImageView) convertView.findViewById(R.id.favorite_image);
			viewHolder.merchant_name = (TextView) convertView.findViewById(R.id.merchant_name);
			viewHolder.expiry_time = (TextView) convertView.findViewById(R.id.expiry_time);
			viewHolder.view = (View) convertView.findViewById(R.id.view);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.favorite_image.setVisibility(View.GONE);
		viewHolder.expiry_time.setVisibility(View.GONE);
		viewHolder.offer_title.setVisibility(View.GONE);
		viewHolder.view.setVisibility(View.GONE);
		viewHolder.merchant_name.setText(getItem(position).getName());
		CommonUtility.setImage(mContext, getItem(position).getImg(), viewHolder.offer_image, R.drawable.dum_list_item_product);
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((HomeActivity) mContext).checkInternet())
					addFragment(new FragmentStoreDeals(getItem(position).getName(),getItem(position).getImg()));
			}
		});
		return convertView;
	}

	public class ViewHolder {
		private ImageView offer_image,favorite_image;
		private TextView offer_title,merchant_name,expiry_time;
		private View view;
	}

	public void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
}

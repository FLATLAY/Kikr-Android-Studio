package com.flatlay.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentProductDetailWebView;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.bean.TopDeals;
import com.kikrlib.db.DatabaseHelper;
import com.kikrlib.db.dao.FavoriteDealsDAO;

public class DealListAdapter extends BaseAdapter {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<TopDeals> data;
	private List<String> favoriteDealsIdList;
	private boolean isFavoriteList;
	final FavoriteDealsDAO dao = new FavoriteDealsDAO(DatabaseHelper.getDatabase());

	public DealListAdapter(FragmentActivity context, List<TopDeals> data, boolean isFavoriteList) {
		super();
		this.mContext = context;
		this.data = (ArrayList<TopDeals>) data;
		this.isFavoriteList = isFavoriteList;
		favoriteDealsIdList = dao.getDealIdList();
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public TopDeals getItem(int index) {
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
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.merchant_name.setText(getItem(position).getMerchantName());
		if (CommonUtility.getDateDifference(getItem(position).getExpiryTime())!=null) {
			viewHolder.expiry_time.setText(CommonUtility.getDateDifference(getItem(position).getExpiryTime()));
		}
		viewHolder.offer_title.setText(getItem(position).getTitle());
		CommonUtility.setImage(mContext, getItem(position).getImagelink(), viewHolder.offer_image, R.drawable.dum_list_item_product);
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(((HomeActivity) mContext).checkInternet())
					addFragment(new FragmentProductDetailWebView(getItem(position)));
			}
		});
		if (favoriteDealsIdList.contains(getItem(position).getDealid())) {
			viewHolder.favorite_image.setImageResource(R.drawable.ic_deals_favorite);
		} else {
			viewHolder.favorite_image.setImageResource(R.drawable.ic_deals_unfavorite);
		}
		
		viewHolder.favorite_image.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!getItem(position).isFavorite()) {
				getItem(position).setFavorite(true);
				dao.insert(getItem(position));
				favoriteDealsIdList = dao.getDealIdList();
				notifyDataSetChanged();
			} else {
				getItem(position).setFavorite(false);
				dao.delete(getItem(position).getDealid());
				favoriteDealsIdList = dao.getDealIdList();
				if (isFavoriteList) {
					data.remove(position);
				}
				notifyDataSetChanged();
			}
		}
		});
		return convertView;
	}

	public class ViewHolder {
		private ImageView offer_image,favorite_image;
		private TextView offer_title,merchant_name,expiry_time;
	}

	public void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
}

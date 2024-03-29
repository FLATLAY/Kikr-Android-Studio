package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.Inspiration;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.flatlay.R.id.imageView;

public class InspirationGridAdapter extends BaseAdapter{

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<Inspiration> inspirations;

	public InspirationGridAdapter(FragmentActivity context, List<Inspiration> inspirations) {
		super();
		this.mContext = context;
		this.inspirations = inspirations;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.w("Activity","InspirationGridAdapter");
	}

	public void setData(List<Inspiration> data){
		this.inspirations=data;
	}

	@Override
	public int getCount() {
		return inspirations.size();
	}

	@Override
	public Inspiration getItem(int index) {
		return inspirations.get(index);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewholder;
		if (convertView==null) {
			convertView = mInflater.inflate(R.layout.adapter_inspiration_grid,null);
			viewholder = new ViewHolder();
			viewholder.inspirationImage = (ImageView) convertView.findViewById(R.id.inspirationImage);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		CommonUtility.setImage(mContext, getItem(position).getInspiration_image(), viewholder.inspirationImage, R.drawable.dum_list_item_product);
		//Picasso.with(mContext).load(getItem(position).getInspiration_image()).transform(new RoundedTransformation(125, 4)).into(viewholder.inspirationImage);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.w("InspirationGridAdapter","onClick()");
				if (((HomeActivity) mContext).checkInternet()) {
					addFragment(new FragmentInspirationDetail(getItem(position),true));
				}
			}
		});
		return convertView;
	}
	
	public class ViewHolder {
		private ImageView inspirationImage;
	}
	
	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
	
}
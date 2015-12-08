package com.kikr.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentInspirationDetail;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.ui.InspirationProductUI;
import com.kikr.ui.RoundImageView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.Product;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class InspirationGridAdapter extends BaseAdapter{

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<Inspiration> inspirations;

	public InspirationGridAdapter(FragmentActivity context, List<Inspiration> inspirations) {
		super();
		this.mContext = context;
		this.inspirations = inspirations;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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

package com.kikr.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import android.widget.Button;
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

public class InspirationAdapter extends BaseAdapter{

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<Inspiration> inspirations;
	private boolean isViewAll;

	public InspirationAdapter(FragmentActivity context, List<Inspiration> inspirations, boolean isViewAll) {
		super();
		this.mContext = context;
		this.inspirations = inspirations;
		this.isViewAll = isViewAll;
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
			convertView = mInflater.inflate(R.layout.adapter_inspiration_large,null);
			viewholder = new ViewHolder();
			viewholder.inspirationImage = (ImageView) convertView.findViewById(R.id.inspirationImage);
			viewholder.userImage = (RoundImageView) convertView.findViewById(R.id.userImage);
			viewholder.userName = (TextView) convertView.findViewById(R.id.userName);
			viewholder.inspirationTime = (TextView) convertView.findViewById(R.id.inspirationTime);
			viewholder.productLayout = (HorizontalScrollView) convertView.findViewById(R.id.productLayout);
			viewholder.productInflaterLayout = (LinearLayout) convertView.findViewById(R.id.productInflaterLayout);
			viewholder.user_profile_layout=(LinearLayout) convertView.findViewById(R.id.user_profile_layout);
			viewholder.progressBar=(ProgressBar) convertView.findViewById(R.id.progressBar);
			viewholder.noProductTextView=(TextView) convertView.findViewById(R.id.noProductTextView);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		if (!isViewAll && position>0) {
			viewholder.user_profile_layout.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(getItem(position).getUsername())) {
			viewholder.userName.setText(getItem(position).getUsername());
		} else {
			viewholder.userName.setText("Unknown");
		}
		Calendar calLocal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		
//		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
//		df2.setTimeZone(TimeZone.getDefault());
		Calendar calServer  = Calendar.getInstance();
		try {
//			Log.e("local",calLocal.getTime().toLocaleString());
			Log.e("date from server >>>",getItem(position).getDateadded() + "");
			Log.e("date converted local >>>",df.parse(getItem(position).getDateadded())+ "");
			Date dd = df.parse(getItem(position).getDateadded());
//			Log.e("date converted local 222 >>>",df2.format(dd)+ "");
			calServer.setTime(dd);

//			calServer.setTime(df.parse(getItem(position).getDateadded()));
//			calServer.setTimeZone(TimeZone.getDefault());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		viewholder.inspirationTime.setText(CommonUtility.calculateTimeDiff(calServer, calLocal));
		CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewholder.userImage, R.drawable.dum_user);
		CommonUtility.setImage(mContext, getItem(position).getInspiration_image(), viewholder.inspirationImage, R.drawable.dum_list_item_product);
		List<Product> data = getItem(position).getProducts();
		viewholder.productInflaterLayout.removeAllViews();
		if (data!=null && data.size()>0) {
			viewholder.noProductTextView.setVisibility(View.GONE);
			viewholder.productInflaterLayout.addView(new InspirationProductUI(mContext, getItem(position),convertView).getView());
		}else{
			viewholder.noProductTextView.setVisibility(View.VISIBLE);
		}
		viewholder.productLayout.scrollTo(0, 0);
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((HomeActivity) mContext).checkInternet()) {
					addFragment(new FragmentInspirationDetail(getItem(position),true));
				}
			}
		});
		viewholder.user_profile_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (((HomeActivity) mContext).checkInternet()) {
					addFragment(new FragmentProfileView(getItem(position).getUser_id(), "no"));
					
				}
			}
		});
		return convertView;
	}
	
	public class ViewHolder {
		private ImageView inspirationImage;
		private RoundImageView userImage;
		private HorizontalScrollView productLayout;
		private LinearLayout productInflaterLayout;
		private TextView noProductTextView,userName,inspirationTime;
		private ProgressBar progressBar;
		private LinearLayout user_profile_layout;
	}
	
	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
	
}

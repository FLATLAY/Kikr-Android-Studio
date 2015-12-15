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
import com.kikr.dialog.CreateAccountDialog;
import com.kikr.fragment.FragmentInspirationDetail;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.ui.InspirationProductUI;
import com.kikr.ui.RoundImageView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.InspirationSectionApi;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.InspirationRes;

public class InspirationAdapter extends BaseAdapter{

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<Inspiration> inspirations;
	private boolean isViewAll;
	String likeId = "";

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
			viewholder.follower_count = (TextView) convertView.findViewById(R.id.follower_count);
			viewholder.collection_count = (TextView) convertView.findViewById(R.id.collection_count);
			viewholder.follow_btn = (ImageView) convertView.findViewById(R.id.follow_btn_img);
			viewholder.descriptionTextView = (TextView) convertView.findViewById(R.id.descriptionTextView);
			viewholder.likeCount = (TextView) convertView.findViewById(R.id.likeCount);
			viewholder.commentCount = (TextView) convertView.findViewById(R.id.commentCount);
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
		if (!TextUtils.isEmpty(getItem(position).getFollower_count())) {
			viewholder.follower_count.setText("Followers\n"+getItem(position).getFollower_count());
		} else {
			viewholder.follower_count.setText("Followers\n0");
		}
		if (!TextUtils.isEmpty(getItem(position).getCollection_count())) {
			viewholder.collection_count.setText("Collections\n"+getItem(position).getCollection_count());
		} else {
			viewholder.collection_count.setText("Collections\n0");
		}
		if (!TextUtils.isEmpty(getItem(position).getIs_followed())) {
			if (getItem(position).getIs_followed().equals("yes")) {
			}else{
			}
		}
		viewholder.commentCount.setText(getItem(position).getComment_count());
		viewholder.likeCount.setText(getItem(position).getLike_count());
		if (!TextUtils.isEmpty(getItem(position).getLike_id()))
			likeId = getItem(position).getLike_id();
		if (TextUtils.isEmpty(likeId))
			viewholder.likeCount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flame_logo_36, 0, 0);
		else
			viewholder.likeCount.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flame_logo_36, 0, 0);
		if (!TextUtils.isEmpty(getItem(position).getDescription()))
			viewholder.descriptionTextView.setText(getItem(position).getDescription());
		viewholder.likeCount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
					CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
					createAccountDialog.show();
				} else {
					if (((HomeActivity) mContext).checkInternet()) {
						likeInspiration(v, getItem(position).getInspiration_id());
					}
				}
			}
		});
		if(getItem(position).getIs_followed()!=null&&getItem(position).getIs_followed().equals("yes")){
			viewholder.follow_btn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_follow_category_tick));
		}else{
			viewholder.follow_btn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_add_collection));
		}
		viewholder.follow_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getItem(position).getIs_followed()!=null) {
					if (getItem(position).getIs_followed().equals("no")) {
						getItem(position).setIs_followed("yes");
						notifyDataSetChanged();
						((HomeActivity) mContext).followUser(getItem(position).getUser_id());
					} else {
						getItem(position).setIs_followed("no");
						notifyDataSetChanged();
						((HomeActivity) mContext).unFollowUser(getItem(position).getUser_id());
					}
				}
			}
		});
		Calendar calLocal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		
//		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
//		df2.setTimeZone(TimeZone.getDefault());
		Calendar calServer  = Calendar.getInstance();
		try {
//			Log.e("local",calLocal.getTime().toLocaleString());
			Log.e("date from server >>>",getItem(position).getDateadded() + "");
//			Log.e("date converted local >>>",df.parse(getItem(position).getDateadded())+ "");
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
//			viewholder.noProductTextView.setVisibility(View.VISIBLE);
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
		private TextView follower_count,collection_count;
		private ImageView follow_btn;
		private TextView descriptionTextView,likeCount,commentCount;
	}
	
	private void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}


	private void likeInspiration(View likeCount,final String id) {
		final TextView v = (TextView) likeCount.findViewById(R.id.likeCount);
		final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				InspirationRes inspirationRes=(InspirationRes) object;
				likeId=inspirationRes.getLike_id();

				if (TextUtils.isEmpty(likeId)){
					v.setText((getInt(v.getText().toString().trim())-1)+"");
					v.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flame_logo_36, 0, 0);
				}
				else {
					v.setText((getInt(v.getText().toString().trim())+1)+"");
					v.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_flame_logo_36, 0, 0);
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				v.setVisibility(View.VISIBLE);
			}
		});
		if (TextUtils.isEmpty(likeId))
			inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), id, "inspiration");
		else
			inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), likeId);
		inspirationSectionApi.execute();
	}


	private int getInt(String image_width) {
		try{
			return Integer.parseInt(image_width);
		}catch(Exception e){
			return 0;
		}
	}


}

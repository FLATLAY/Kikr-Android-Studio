package com.flatlay.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.bean.FollowUser;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class KikrUserListAdapter extends BaseAdapter implements ServiceCallback {

	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<FollowUser> data;
	private ProgressBarDialog mProgressBarDialog;

	public KikrUserListAdapter(FragmentActivity context, List<FollowUser> data) {
		super();
		this.mContext = context;
		this.data =  data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.w("Activity","KikrUserListAdapter");
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public FollowUser getItem(int index) {
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
			convertView = mInflater.inflate(R.layout.adapter_kikr_user_list, null);
			viewHolder = new ViewHolder();
			viewHolder.user_image = (RoundImageView) convertView.findViewById(R.id.user_image);
			viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			viewHolder.follow_btn = (Button) convertView.findViewById(R.id.follow_btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(getItem(position).getUsername())) {
			viewHolder.user_name.setText(getItem(position).getUsername());
		} else {
			viewHolder.user_name.setText("Unknown");
		}
		CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewHolder.user_image, R.drawable.dum_user);
		viewHolder.follow_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				followUser(getItem(position).getId());
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.frame_container, new FragmentProfileView(getItem(position).getId(),getItem(position).getIs_followed())).commit();
			}
		});
		return convertView;
	}

	public class ViewHolder {
		RoundImageView user_image;
		TextView user_name;
		Button follow_btn;
	}
	private void followUser(String id) {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final FollowUserApi followUserApi = new FollowUserApi(this);
		followUserApi.followUser(UserPreference.getInstance().getUserID(),id);
		followUserApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				followUserApi.cancel();
			}
		});
	}
	
	@Override
	public void handleOnSuccess(Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnSuccess>>" + object);
		FollowUserRes followUserRes = (FollowUserRes) object;
		AlertUtils.showToast(mContext, R.string.user_followed_successfully);
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			FollowUserRes response = (FollowUserRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}		
	}
}

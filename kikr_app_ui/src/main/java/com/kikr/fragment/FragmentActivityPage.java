package com.kikr.fragment;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.adapter.ActivityPageAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.ActivityApi;
import com.kikrlib.bean.CollectionMonthDetailList;
import com.kikrlib.bean.User;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ActivityRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentActivityPage extends BaseFragment implements OnClickListener {
	private View mainView;
	private ListView activity_list;
	private ImageView user_profile_image,bgProfileLayout;
	private TextView user_profile_name,total_payout_text,month_name_heading;
	private ProgressBarDialog mProgressBarDialog;
	private String month="",year="";
	private ActivityPageAdapter activityPageAdapter;
	private User user;
	private TextView kikr_commision_text;
	
	public FragmentActivityPage(String month, String year, User user) {
		this.month = month;
		this.year = year;
		this.user = user;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_activity_page, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		activity_list = (ListView) mainView.findViewById(R.id.activity_list);
		user_profile_image = (ImageView) mainView.findViewById(R.id.user_profile_image);
		bgProfileLayout = (ImageView) mainView.findViewById(R.id.bgProfileLayout);
		user_profile_name = (TextView) mainView.findViewById(R.id.user_profile_name);
		month_name_heading = (TextView) mainView.findViewById(R.id.month_name_heading);
		total_payout_text = (TextView) mainView.findViewById(R.id.total_payout_text);
		kikr_commision_text = (TextView) mainView.findViewById(R.id.kikr_commission_text);
		kikr_commision_text.setPaintFlags(kikr_commision_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		setDetails();
	}

	private void setDetails() {
		if (user != null) {
			user_profile_name.setText(user.getUser_name());
			if (!user.getProfile_pic().equalsIgnoreCase(""))
				CommonUtility.setImage(mContext, user.getProfile_pic(),user_profile_image, R.drawable.dum_user);
			if (!user.getBackground_pic().equalsIgnoreCase(""))
				CommonUtility.setImage(mContext, user.getBackground_pic(),bgProfileLayout, R.drawable.dum_user);
		}
		month_name_heading.setText(month+" Activity");
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		kikr_commision_text.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
		if (checkInternet()) 
			getActivityDetails();
	}

	private void getActivityDetails() {

		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final ActivityApi activityApi = new ActivityApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				hideDataNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				ActivityRes activityRes = (ActivityRes) object;
				List<CollectionMonthDetailList> data = activityRes.getCollection_list();
				if (!TextUtils.isEmpty(activityRes.getMonth_activity())) 
				total_payout_text.setText(month+" Activity = $"+CommonUtility.getFormatedNum(activityRes.getTotal_payout()));
				else
				total_payout_text.setText(month+" Activity = $0.00");
				if (data.size() == 0)
					showDataNotFound();
				else if (data.size() > 0) {
					hideDataNotFound();
					activityPageAdapter = new ActivityPageAdapter(mContext,data,month,year,user);
					activity_list.setAdapter(activityPageAdapter);
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					ActivityRes response = (ActivityRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		activityApi.getActivityMonthDetail(UserPreference.getInstance().getUserID(), month, year);
		activityApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				activityApi.cancel();
			}
		});
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.kikr_commission_text:
			this.addFragment(new FragmentKikrCreditsScreen());
			break;
		
		}
	}

}

package com.kikr.fragment;

import java.util.ArrayList;
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
import com.kikr.adapter.KikrCreditMonthAdapter;
import com.kikr.adapter.KikrCreditsMonthsBreakdownAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.ActivityApi;
import com.kikrlib.api.KikrCreditsApi;
import com.kikrlib.bean.ActivityMonthList;
import com.kikrlib.bean.CollectionMonthDetailList;
import com.kikrlib.bean.Credits;
import com.kikrlib.bean.Detail;
import com.kikrlib.bean.User;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ActivityRes;
import com.kikrlib.service.res.KikrCreditsRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentKikrCreditMonthBreakdown extends BaseFragment implements OnClickListener {
	private View mainView;
	private ListView kikrcreditmonthbreakdown_list;
	private TextView month_name_heading;
	private ProgressBarDialog mProgressBarDialog;
	private String month="",year="", userid="";
	private User user;
	private KikrCreditsMonthsBreakdownAdapter kikrCreditsMonthsBreakdownAdapter;
	
	public FragmentKikrCreditMonthBreakdown(String month, String year, String userid) {
		this.month = month;
		this.year = year;
		this.userid = userid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_kikrcreditmonthbreakdown, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		kikrcreditmonthbreakdown_list = (ListView) mainView.findViewById(R.id.kikrcreditmonthbreakdown_list);
		month_name_heading = (TextView) mainView.findViewById(R.id.month_name_heading);
		setDetails();
	}

	private void setDetails() {
		month_name_heading.setText(month + " " + year + " Kikr Credits Breakdown");
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
	}

	@Override
	public void setData(Bundle bundle) {
		if (checkInternet()) {
			getKikrCreditBreakdownDetails();
		}
			
	}

	private void getKikrCreditBreakdownDetails() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final KikrCreditsApi kikrCreditsApi = new KikrCreditsApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				hideDataNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				KikrCreditsRes kikrRes = (KikrCreditsRes) object;
				List<Detail> data = kikrRes.getDetail();
				
				if (data.size() == 0)
					showDataNotFound();
				else if (data.size() > 0) {
					hideDataNotFound();
					kikrCreditsMonthsBreakdownAdapter = new KikrCreditsMonthsBreakdownAdapter(mContext, data);
					kikrcreditmonthbreakdown_list.setAdapter(kikrCreditsMonthsBreakdownAdapter);
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					KikrCreditsRes response = (KikrCreditsRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		kikrCreditsApi.getKikrCreditsMonthBreakdown(UserPreference.getInstance().getUserID(), month, year);
		kikrCreditsApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				kikrCreditsApi.cancel();
			}
		});
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		}
	}

}

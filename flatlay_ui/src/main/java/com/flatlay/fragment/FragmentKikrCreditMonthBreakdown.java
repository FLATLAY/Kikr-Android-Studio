package com.flatlay.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.KikrCreditsMonthsBreakdownAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlaylib.api.KikrCreditsApi;
import com.flatlaylib.bean.Detail;
import com.flatlaylib.bean.User;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.KikrCreditsRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.util.List;

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
				hideProductNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				KikrCreditsRes kikrRes = (KikrCreditsRes) object;
				List<Detail> data = kikrRes.getDetail();
				
				if (data.size() == 0)
					showProductNotFound();
				else if (data.size() > 0) {
					hideProductNotFound();
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
	public void showProductNotFound() {
		try {
			LinearLayout layout = (LinearLayout) mainView.findViewById(R.id.itemNotFound);
			layout.setVisibility(View.VISIBLE);
			TextView textView = (TextView) layout.findViewById(R.id.noDataFoundTextView);
			textView.setText("Result not found.");
		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}
	}

	public void hideProductNotFound() {
		try {
			LinearLayout layout = (LinearLayout) mainView.findViewById(R.id.itemNotFound);
			layout.setVisibility(View.GONE);
			TextView textView = (TextView) layout.findViewById(R.id.noDataFoundTextView);
			textView.setText("Result not found.");
		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		}
	}

}

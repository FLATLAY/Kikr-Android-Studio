package com.kikr.fragment;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.adapter.ActivityMonthsAdapter;
import com.kikr.adapter.KikrCreditsMonthsAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.ActivityApi;
import com.kikrlib.api.KikrCreditsApi;
import com.kikrlib.bean.ActivityMonthList;
import com.kikrlib.bean.Credits;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ActivityRes;
import com.kikrlib.service.res.KikrCreditsRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.StringUtils;
import com.kikrlib.utils.Syso;

public class FragmentKikrCreditsScreen extends BaseFragment implements OnClickListener {
	private View mainView;
	private ListView months_list;
	private TextView total_payout_text;
	private ProgressBarDialog mProgressBarDialog;
	private KikrCreditsMonthsAdapter creditsMonthsAdapter;
	private TextView kikr_commission_text,pendingCredits;

	public FragmentKikrCreditsScreen() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_kikr_credits_list, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		months_list = (ListView) mainView.findViewById(R.id.months_list);
		total_payout_text = (TextView) mainView.findViewById(R.id.total_payout_text);
		kikr_commission_text = (TextView) mainView.findViewById(R.id.kikr_commission_text);
		kikr_commission_text.setPaintFlags(kikr_commission_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		pendingCredits = (TextView) mainView.findViewById(R.id.pendingCredits);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		kikr_commission_text.setOnClickListener(this);
		pendingCredits.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
		if (checkInternet()) 
			getActivityList();
	}

	private void getActivityList() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final KikrCreditsApi creditsApi = new KikrCreditsApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				hideDataNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				KikrCreditsRes kikrCreditsRes = (KikrCreditsRes) object;
				List<Credits> data = kikrCreditsRes.getActivity_month_list();
				if (data.size() == 0)
					showDataNotFound();
				else if (data.size() > 0) {
					hideDataNotFound();
					creditsMonthsAdapter = new KikrCreditsMonthsAdapter(mContext,data);
					months_list.setAdapter(creditsMonthsAdapter);
					Syso.info("========= total pending credit>>>>"+kikrCreditsRes.getTotal_pending_credit());
					setPendingCredits(kikrCreditsRes.getTotal_pending_credit());
				}
				getKikrCredits();
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
		creditsApi.getKikrCreditsList(UserPreference.getInstance().getUserID());
		creditsApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				creditsApi.cancel();
			}
		});
	}
	

	private void getKikrCredits() {
		final KikrCreditsApi creditsApi = new KikrCreditsApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				KikrCreditsRes kikrCreditsRes = (KikrCreditsRes) object;
				Log.e("kikrcreditsres ammount",kikrCreditsRes.getAmount() + "");
				if (StringUtils.getDoubleValue(kikrCreditsRes.getAmount()) > 0) 
				total_payout_text.setText(kikrCreditsRes.getAmount() + " Credits = $" + String.format("%.2f", StringUtils.getDoubleValue(kikrCreditsRes.getAmount())/100.0));
				else
				total_payout_text.setText("0 Credits = $0.00");
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					KikrCreditsRes response = (KikrCreditsRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		creditsApi.getKikrCredits(UserPreference.getInstance().getUserID());
		creditsApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				creditsApi.cancel();
			}
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.kikr_commission_text:
			addFragment(new FragmentLearnMore());
		case R.id.pendingCredits:
			addFragment(new FragmentPendingCreditDetails());
		break;
		}
	}

	private void setPendingCredits(String data){
		if(TextUtils.isEmpty(data)){
			pendingCredits.setText("Pending Credits : 0");
		}else{
			pendingCredits.setText("Pending Credits : "+data);
		}
	}

}

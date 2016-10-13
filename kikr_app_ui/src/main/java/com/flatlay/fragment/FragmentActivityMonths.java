package com.flatlay.fragment;

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

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.ActivityMonthsAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.api.ActivityApi;
import com.kikrlib.bean.ActivityMonthList;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ActivityRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import java.util.List;

public class FragmentActivityMonths extends BaseFragment implements OnClickListener {
    private View mainView;
    private ListView months_list;
    private ImageView user_profile_image, bgProfileLayout;
    private TextView user_profile_name, total_payout_text;
    private ProgressBarDialog mProgressBarDialog;
    private ActivityMonthsAdapter ActivityMonthsAdapter;
    private TextView kikr_commission_text;
    private TextView myActivityButton;

    public FragmentActivityMonths() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_activity_months, null);
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        months_list = (ListView) mainView.findViewById(R.id.months_list);
        user_profile_image = (ImageView) mainView.findViewById(R.id.user_profile_image);
        bgProfileLayout = (ImageView) mainView.findViewById(R.id.bgProfileLayout);
        user_profile_name = (TextView) mainView.findViewById(R.id.user_profile_name);
        total_payout_text = (TextView) mainView.findViewById(R.id.total_payout_text);
        kikr_commission_text = (TextView) mainView.findViewById(R.id.kikr_commission_text);
        kikr_commission_text.setPaintFlags(kikr_commission_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        myActivityButton = (TextView) mainView.findViewById(R.id.btn_activity);
        myActivityButton.setBackgroundResource(R.drawable.backbutton);
        myActivityButton.setPadding(5, 5, 5, 5);
     //   myActivityButton.setText("My Collections");
        myActivityButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        kikr_commission_text.setOnClickListener(this);
        myActivityButton.setOnClickListener(this);
    }

    @Override
    public void setData(Bundle bundle) {
        if (checkInternet())
            getActivityList();
    }

    private void getActivityList() {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final ActivityApi activityApi = new ActivityApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                mProgressBarDialog.dismiss();
                hideDataNotFound();
                Syso.info("In handleOnSuccess>>" + object);
                ActivityRes activityRes = (ActivityRes) object;
                List<ActivityMonthList> data = activityRes.getActivity_month_list();
                if (!TextUtils.isEmpty(activityRes.getUser_info().getUser_name()))
                    user_profile_name.setText(activityRes.getUser_info().getUser_name());
                if (!TextUtils.isEmpty(activityRes.getUser_info().getProfile_pic()))
                    CommonUtility.setImage(mContext, activityRes.getUser_info().getProfile_pic(), user_profile_image, R.drawable.dum_user);
                if (!TextUtils.isEmpty(activityRes.getUser_info().getBackground_pic()))
                    CommonUtility.setImage(mContext, activityRes.getUser_info().getBackground_pic(), bgProfileLayout, R.drawable.dum_user);
                if (!TextUtils.isEmpty(activityRes.getTotal_payout()))
                    total_payout_text.setText("Total Activity = " + CommonUtility.getFormatedNum(activityRes.getTotal_payout() + " Credits"));
                else
                    total_payout_text.setText("Total Activity = 0 Credits");
                if (data.size() == 0)
                    showDataNotFound();
                else if (data.size() > 0) {
                    hideDataNotFound();
                    ActivityMonthsAdapter = new ActivityMonthsAdapter(mContext, data, activityRes.getUser_info());
                    months_list.setAdapter(ActivityMonthsAdapter);
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
        activityApi.getActivityListByMonth(UserPreference.getInstance().getUserID());
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
            case R.id.btn_activity:
                ((HomeActivity) mContext).hideActionBar();
                mContext.getSupportFragmentManager().popBackStack();
                break;
            case R.id.kikr_commission_text:
                this.addFragment(new FragmentKikrCreditsScreen());
                break;
        }
    }

}

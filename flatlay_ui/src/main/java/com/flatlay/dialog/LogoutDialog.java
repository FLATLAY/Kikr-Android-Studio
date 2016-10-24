package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.Session;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.sessionstore.SessionStore;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.LogoutApi;
import com.flatlaylib.db.DatabaseHelper;
import com.flatlaylib.db.HelpPreference;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.db.dao.FavoriteDealsDAO;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;

import io.branch.referral.Branch;

public class LogoutDialog extends Dialog{
	private TextView cancelTextView,okTextView;
	private HomeActivity homeActivity;
	private Context mContext;

	public LogoutDialog(Context mContext,HomeActivity homeActivity) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.homeActivity = homeActivity;
		this.mContext=mContext;
		init();
	}

	public LogoutDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_logout);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		okTextView = (TextView) findViewById(R.id.okTextView);
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 if (homeActivity.checkInternet()) {
					Branch.getInstance().logout();
					logoutUser();
					UserPreference.getInstance().clearAllData();
					HelpPreference.getInstance().clearAllData();
					Session session = Session.getActiveSession();
					if(session!=null)
						 session.closeAndClearTokenInformation();
					SessionStore.resetTwitterLogin(mContext);
					FavoriteDealsDAO dao = new FavoriteDealsDAO(DatabaseHelper.getDatabase());
					dao.delete();

					Instagram mInstagram  = new Instagram(mContext, AppConstants.INSTAGRAM_CLIENT_ID, AppConstants.INSTAGRAM_CLIENT_SECRET, AppConstants.INSTAGRAM_REDIRECT_URI);
					InstagramSession mInstagramSession	= mInstagram.getSession();
					if(mInstagramSession.isActive())
						mInstagramSession.reset();
				    homeActivity.showLoginHome();
				}
			}
		});
	}

	private void logoutUser() {
		LogoutApi logoutApi = new LogoutApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				
			}
		});
		logoutApi.logoutUser(UserPreference.getInstance().getUserID(), CommonUtility.getDeviceTocken(mContext));
		logoutApi.execute();
	}
	
}

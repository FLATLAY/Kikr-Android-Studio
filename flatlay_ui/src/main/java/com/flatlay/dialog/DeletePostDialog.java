package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentDiscoverNew;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlaylib.api.EditInspirationApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CommonRes;
import com.flatlaylib.utils.AlertUtils;

public class DeletePostDialog extends Dialog{
	private FragmentActivity mContext;
	private TextView cancelTextView,okTextView;
	private Inspiration inspiration;
	private ProgressBarDialog mProgressBarDialog;

	public DeletePostDialog(FragmentActivity mContext, Inspiration inspiration) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.inspiration = inspiration;
		init();
	}

	public DeletePostDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_delete_post);
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
				deletePost();
				dismiss();
			}
		});
	}
	
	
	private void deletePost() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final EditInspirationApi editInspirationApi = new EditInspirationApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				AlertUtils.showToast(mContext, "Post deleted successfully");
//				((HomeActivity)mContext).loadFragment(new FragmentInspirationSection());
				((HomeActivity)mContext).loadFragment(new FragmentDiscoverNew(2,false));
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				if (object != null) {
					CommonRes response = (CommonRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		editInspirationApi.deletePost(UserPreference.getInstance().getUserID(), inspiration.getInspiration_id());
		editInspirationApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				editInspirationApi.cancel();
			}
		});		
	}
	
	
}

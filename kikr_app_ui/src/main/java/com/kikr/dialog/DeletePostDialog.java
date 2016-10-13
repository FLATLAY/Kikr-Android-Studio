package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentDiscoverNew;
import com.kikr.ui.ProgressBarDialog;
import com.kikrlib.api.EditInspirationApi;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CommonRes;
import com.kikrlib.utils.AlertUtils;

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

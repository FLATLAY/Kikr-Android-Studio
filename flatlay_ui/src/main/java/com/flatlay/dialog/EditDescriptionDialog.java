package com.flatlay.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.EditInspirationApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CommonRes;
import com.flatlaylib.utils.AlertUtils;

public class EditDescriptionDialog extends Dialog{
	private FragmentActivity mContext;
	private EditText editText;
	private ProgressBarDialog mProgressBarDialog;
	private Inspiration inspiration;

	public EditDescriptionDialog(FragmentActivity context, Inspiration inspiration) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		this.inspiration = inspiration;
		init();
	}
	
	private void init() {	
		setContentView(R.layout.dialog_edit_description);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		editText= (EditText) findViewById(R.id.desriptionEditTaxt);
		if (!TextUtils.isEmpty(inspiration.getDescription())) {
			editText.setText(inspiration.getDescription());
			editText.setSelection(editText.getText().length());
		}
		TextView cancelTextView=(TextView) findViewById(R.id.cancelTextView);
		TextView okTextView=(TextView) findViewById(R.id.okTextView);
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CommonUtility.hideKeypad(mContext, editText);
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CommonUtility.hideKeypad(mContext, editText);
				validate();
			}
		});
	}

	protected void validate() {
		String description=editText.getText().toString().trim();
		if(description.length()==0){
			AlertUtils.showToast(mContext, "Enter Description");
			return;
		}else
			updateDescription(description);
	}

	private void updateDescription(final String description) {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final EditInspirationApi editInspirationApi = new EditInspirationApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				CommonRes commonRes = (CommonRes) object;
				AlertUtils.showToast(mContext, commonRes.getMessage());
				inspiration.setDescription(description);
				((HomeActivity)mContext).setDescription();
				dismiss();
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
		editInspirationApi.editDescription(UserPreference.getInstance().getUserID(), description, inspiration.getInspiration_id());
		editInspirationApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				editInspirationApi.cancel();
			}
		});		
	}

	
}

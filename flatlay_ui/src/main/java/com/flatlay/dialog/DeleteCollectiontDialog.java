package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class DeleteCollectiontDialog extends Dialog{
	private Context mContext;
	private TextView cancelTextView,okTextView,messageTextView;
	private ProgressBarDialog mProgressBarDialog;
	private String collection_id;
	private String user_id;
	private FragmentProfileView fragmentProfileView;
	
	public DeleteCollectiontDialog(Context context,FragmentProfileView fragmentProfileView,String userId, String collectionId) {
		super(context, R.style.AdvanceDialogTheme);
		collection_id=collectionId;
		user_id=userId;
		mContext=context;
		this.fragmentProfileView=fragmentProfileView;
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
		messageTextView=(TextView) findViewById(R.id.messageTextView);
		okTextView.setText("Yes");
		messageTextView.setText(mContext.getResources().getString(R.string.collection_delete_message));
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(((HomeActivity)mContext).checkInternet()){
					dismiss();
					deleteCollection();
				}
			}
			
		});
	}
	
	private void deleteCollection() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final CollectionApi collectionApi = new CollectionApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				AlertUtils.showToast(mContext, mContext.getResources().getString(R.string.alert_collection_deleted));
//				Stack<String> mFragmentStack=((HomeActivity)mContext).mFragmentStack;
//				for(int i=0;i<mFragmentStack.size();i++){
//					Fragment currentFragment = ((HomeActivity)mContext).getSupportFragmentManager().findFragmentByTag(mFragmentStack.peek());
//					if(currentFragment instanceof FragmentProfileView)
//						((FragmentProfileView)currentFragment).refreshProfile();
//				}
				fragmentProfileView.refreshProfile();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					CollectionApiRes response = (CollectionApiRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		collectionApi.deleteCollection(collection_id,user_id);
		collectionApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				collectionApi.cancel();
			}
		});		
	}
}

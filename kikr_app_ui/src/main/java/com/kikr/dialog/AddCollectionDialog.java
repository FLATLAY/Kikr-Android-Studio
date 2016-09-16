package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.AppConstants;
import com.kikrlib.api.AddCollectionApi;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.AddCollectionApiRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class AddCollectionDialog extends Dialog  implements ServiceCallback{
	private Context mContext;
	private EditText add_collectionEditText;
	private TextView cancelTextView,okTextView;
	private ProgressBarDialog mProgressBarDialog;
//	private FragmentDiscover fragmentDiscover;
	private String productId;
	private CollectionListDialog collectionListDialog;

	public AddCollectionDialog(Context context,String productId, CollectionListDialog collectionListDialog) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		this.productId = productId;
		this.collectionListDialog = collectionListDialog;
//		this.fragmentDiscover = fragmentDiscover;
		init();
	}

	public AddCollectionDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_add_collection);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		add_collectionEditText = (EditText) findViewById(R.id.add_collectionEditText);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		okTextView = (TextView) findViewById(R.id.okTextView);
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				add_collectionEditText.setText("");
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				validateUserInput();
			}
		});
	}

	private void validateUserInput() {
		boolean isValid = true;
		String collectionname = add_collectionEditText.getText().toString().trim();
		if (collectionname.length() == 0) {
			isValid = false;
			add_collectionEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_collection_blank);
		}else if(collectionname.length()>AppConstants.COLLECTION_NAME_MAX_LENGTH){
			isValid = false;
			add_collectionEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_collection_length);
		}
		if (isValid&&((HomeActivity)mContext).checkInternet()) {
			addCollection(add_collectionEditText.getText().toString().trim());
		}
	}

	private void addCollection(String collectionname) {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final AddCollectionApi collectionApi = new AddCollectionApi(this);
		collectionApi.addNewCollection(collectionname);
		collectionApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				collectionApi.cancel();
			}
		});		
	}
	
	@Override
	public void handleOnSuccess(Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnSuccess>>" + object);
			AddCollectionApiRes collectionApiRes=(AddCollectionApiRes) object;
			AlertUtils.showToast(mContext, collectionApiRes.getMessage());
			dismiss();
			collectionListDialog.getCollectionList();
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			AddCollectionApiRes response = (AddCollectionApiRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}
}

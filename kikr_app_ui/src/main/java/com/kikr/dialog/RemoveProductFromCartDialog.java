package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentUserCart;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CallBack;
import com.kikrlib.api.CartApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CartRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class RemoveProductFromCartDialog extends Dialog{
	private TextView cancelTextView,okTextView;
	private String id;
	private ProgressBarDialog mProgressBarDialog;
	private Context mContext;
	CallBack callBack;
	String products;
	private FragmentUserCart fragmentUserCart;

	public RemoveProductFromCartDialog(Context mContext) {
		super(mContext, R.style.AdvanceDialogTheme);
		init();
	}
	
	public RemoveProductFromCartDialog(Context mContext,String products) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.products = products;
		init();
	}

	public RemoveProductFromCartDialog(Context context,String id, FragmentUserCart fragmentUserCart, CallBack callBack) {
		super(context, R.style.AdvanceDialogTheme);
		this.id = id;
		this.mContext = context;
		this.callBack=callBack;
		this.fragmentUserCart = fragmentUserCart;
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_remove_product_from_cart);
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
				if(((HomeActivity)mContext).checkInternet()){
					fragmentUserCart.removeFromCart(id);
//				removeFromCart();
					//((HomeActivity) mContext).onBackPressed();

				dismiss();
				}
			}
		});
	}

	public void removeFromCart(){
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final CartApi cartApi = new CartApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				AlertUtils.showToast(mContext, "Product removed successfully");
				callBack.onSuccess();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					CartRes response = (CartRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		cartApi.removeFromCart(UserPreference.getInstance().getUserID(),id);
		cartApi.execute();
		mProgressBarDialog.setCancelable(false);
		
//		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				cartApi.cancel();
//			}
//		});
	}
	
}

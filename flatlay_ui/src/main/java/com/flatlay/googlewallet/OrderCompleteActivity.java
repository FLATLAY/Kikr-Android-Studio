package com.flatlay.googlewallet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.wallet.FullWallet;
import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;


public class OrderCompleteActivity extends BaseActivity implements OnClickListener{
	private ProgressBarDialog progressBarDialog;
	private FullWallet mFullWallet;
	private TextView return_to_shopping;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtility.noTitleActivity(this);
        setContentView(R.layout.activity_order_complete);
        mFullWallet = getIntent().getParcelableExtra(Constants.EXTRA_FULL_WALLET);
    }

	@Override
	public void initLayout() {
		// TODO Auto-generated method stub
		return_to_shopping=(TextView) findViewById(R.id.return_to_shopping);
	}

	@Override
	public void setupData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void headerView() {
		// TODO Auto-generated method stub
		setBackHeader();
		getLeftTextView().setOnClickListener(this);
		getHomeImageView().setOnClickListener(this);
	}

	@Override
	public void setUpTextType() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClickListener() {
		// TODO Auto-generated method stub
		return_to_shopping.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch(v.getId()){
	      case R.id.leftTextView:
	    	  createCart();
	    	  break;
	      case R.id.homeImageView:
	    	  createCart();
	    	  break;
	      case R.id.return_to_shopping:
	    	  createCart();
	    	  break;
	      }
	}

	public void createCart() {
		progressBarDialog = new ProgressBarDialog(context);
		progressBarDialog.show();
		final CartApi cartApi = new CartApi(
				new ServiceCallback() {
					@Override
					public void handleOnSuccess(Object object) {
						progressBarDialog.dismiss();
						CartRes cartRes = (CartRes) object;
						UserPreference.getInstance().setCartID(cartRes.getCart_id());
						 goToHome();
				    	 startActivity(HomeActivity.class);
					}

					@Override
					public void handleOnFailure(ServiceException exception,Object object) {
						progressBarDialog.dismiss();
					}
				});
		cartApi.createCart(UserPreference.getInstance().getUserID());
		cartApi.execute();
	}
	
	@Override
	public void onBackPressed() {
		createCart();
	}
}

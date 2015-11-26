package com.kikr.googlewallet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.wallet.FullWallet;
import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.CartApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CartRes;


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

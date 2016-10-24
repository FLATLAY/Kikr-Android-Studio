package com.flatlay.googlewallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.flatlay.KikrApp;
import com.flatlay.R;
import com.flatlay.utility.CommonUtility;

public class CheckoutActivity extends BikestoreFragmentActivity implements android.view.View.OnClickListener {

    private static final int REQUEST_CODE_MASKED_WALLET = 1001;
    private SupportWalletFragment mWalletFragment;
    private int mItemId;
    private  Tracker mTracker = null;
    public static final String SCREEN_NAME = "Checkout Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtility.noTitleActivity(this);
        setContentView(R.layout.activity_checkout);

        mTracker = ((KikrApp) getApplication()).getTracker(KikrApp.TrackerName.GLOBAL_TRACKER);
        mTracker.setScreenName(SCREEN_NAME);

        mTracker.send(new HitBuilders.AppViewBuilder().build());

        mItemId = getIntent().getIntExtra(Constants.EXTRA_ITEM_ID, 0);
        createAndAddWalletFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // retrieve the error code, if available
        int errorCode = -1;
        if (data != null) {
            errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
        }
        switch (requestCode) {
            case REQUEST_CODE_MASKED_WALLET:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT)
                                .setCheckoutStep(1)
                                .setCheckoutOptions("GoogleWallet");

                        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                                .addProduct(Constants.ITEMS_FOR_SALE[mItemId].toProduct())
                                .setProductAction(productAction);

                        mTracker.send(builder.build());

                        MaskedWallet maskedWallet =
                                data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                        launchConfirmationPage(maskedWallet);
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    default:
                        handleError(errorCode);
                        break;
                }
                break;
            case WalletConstants.RESULT_ERROR:
                handleError(errorCode);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra(WalletConstants.EXTRA_ERROR_CODE)) {
            int errorCode = intent.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, 0);
            handleError(errorCode);
        }
    }

    @Override
    public void onClick(View v) {
      switch(v.getId()){
      case R.id.leftTextView:
    	  finish();
    	  break;
      case R.id.homeImageView:
    	  goToHome();
    	  break;
      case R.id.payPalButton:
    	  break;
      }
    }
    
    

    private void createAndAddWalletFragment() {
//        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
//                .setBuyButtonText(BuyButtonText.BUY_WITH_GOOGLE)
//                .setBuyButtonWidth(Dimension.MATCH_PARENT);
//
//        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
//                .setEnvironment(AppConstants.GOOGLE_WALLET_ENVIRONMENT)
//                .setFragmentStyle(walletFragmentStyle)
//                .setTheme(WalletConstants.THEME_HOLO_LIGHT)
//                .setMode(WalletFragmentMode.BUY_BUTTON)
//                .build();
//        mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
//
//        // Now initialize the Wallet Fragment
//        String accountName = ((KikrApp) getApplication()).getAccountName();
//        MaskedWalletRequest maskedWalletRequest =WalletUtil.createMaskedWalletRequest(new CartTotalInfo());
//        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
//                .setMaskedWalletRequest(maskedWalletRequest)
//                .setMaskedWalletRequestCode(REQUEST_CODE_MASKED_WALLET)
//                .setAccountName(accountName);
//        mWalletFragment.initialize(startParamsBuilder.build());
//
//        // add Wallet fragment to the UI
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.dynamic_wallet_button_fragment, mWalletFragment)
//                .commit();
    }

    private void launchConfirmationPage(MaskedWallet maskedWallet) {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(Constants.EXTRA_ITEM_ID, mItemId);
        intent.putExtra(Constants.EXTRA_MASKED_WALLET, maskedWallet);
        startActivity(intent);
    }

    @Override
    public Fragment getResultTargetFragment() {
        return null;
    }

	@Override
	public void initLayout() {
		
		
	}

	@Override
	public void setupData() {
		
		
	}

	@Override
	public void headerView() {
		setBackHeader();
		getLeftTextView().setOnClickListener(this);
		getHomeImageView().setOnClickListener(this);
	}

	@Override
	public void setUpTextType() {
		
		
	}

	@Override
	public void setClickListener() {
		
		
	}

}

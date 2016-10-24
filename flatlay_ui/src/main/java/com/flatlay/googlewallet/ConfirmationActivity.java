package com.flatlay.googlewallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

import com.flatlay.KikrApp;
import com.flatlay.R;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.utils.Syso;

public class ConfirmationActivity extends BikestoreFragmentActivity implements OnClickListener{

    private static final int REQUEST_CODE_CHANGE_MASKED_WALLET = 1002;
    private SupportWalletFragment mWalletFragment;
    private MaskedWallet mMaskedWallet;
    private TextView mLoyaltyLabel;
    private TextView mLoyaltyText;

    Tracker mTracker = null;
    public static final String SCREEN_NAME = "Confirmation Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtility.noTitleActivity(this);
        mTracker = ((KikrApp) getApplication())
                .getTracker(KikrApp.TrackerName.GLOBAL_TRACKER);
        mTracker.setScreenName(SCREEN_NAME);
        mTracker.send(new HitBuilders.AppViewBuilder().build());

        mMaskedWallet = getIntent().getParcelableExtra(Constants.EXTRA_MASKED_WALLET);
        setContentView(R.layout.activity_confirmation);
        mLoyaltyText = (TextView) findViewById(R.id.text_loyalty);
        mLoyaltyLabel = (TextView) findViewById(R.id.label_loyalty);
        createAndAddWalletFragment();
        displayLoyaltyInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // no need to show login menu on confirmation screen
        return false;
    }

    private void createAndAddWalletFragment() {
    	try{
        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                .setMaskedWalletDetailsTextAppearance(
                        R.style.BikestoreWalletFragmentDetailsTextAppearance)
                .setMaskedWalletDetailsHeaderTextAppearance(
                        R.style.BikestoreWalletFragmentDetailsHeaderTextAppearance)
                .setMaskedWalletDetailsBackgroundColor(
                        getResources().getColor(R.color.white))
//                .setMaskedWalletDetailsButtonBackgroundResource(
//                        R.drawable.bikestore_btn_default_holo_light)
                .setMaskedWalletDetailsLogoTextColor(
                        getResources().getColor(R.color.gray));

        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                .setEnvironment(AppConstants.GOOGLE_WALLET_ENVIRONMENT)
                .setFragmentStyle(walletFragmentStyle)
                .setTheme(WalletConstants.THEME_HOLO_LIGHT)
                .setMode(WalletFragmentMode.SELECTION_DETAILS)
                .build();
        mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);

        // Now initialize the Wallet Fragment
        String accountName = ((KikrApp) getApplication()).getAccountName();
        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWallet(mMaskedWallet)
                .setMaskedWalletRequestCode(REQUEST_CODE_CHANGE_MASKED_WALLET)
                .setAccountName(accountName);
        mWalletFragment.initialize(startParamsBuilder.build());

        // add Wallet fragment to the UI
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dynamic_wallet_masked_wallet_fragment, mWalletFragment)
                .commit();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    private void displayLoyaltyInformation() {
//        LoyaltyWalletObject[] loyaltyWalletObjects = mMaskedWallet.getLoyaltyWalletObjects();
//        if (loyaltyWalletObjects != null && loyaltyWalletObjects.length > 0) {
//            // display only the first one here
//            mLoyaltyLabel.setVisibility(View.VISIBLE);
//            mLoyaltyText.setVisibility(View.VISIBLE);
//            mLoyaltyText.setText(Util.formatLoyaltyWalletObject(loyaltyWalletObjects[0]));
//        } else {
//            mLoyaltyLabel.setVisibility(View.GONE);
//            mLoyaltyText.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	try{
    	Syso.info("In onActivityResult>>"+requestCode+","+resultCode+","+data);
    	//GCMIntentService.dumpIntent(data);
    	writeData(data);
        switch (requestCode) {
            case REQUEST_CODE_CHANGE_MASKED_WALLET:
            	//GCMIntentService.dumpIntent(data);
                if (resultCode == Activity.RESULT_OK &&data.hasExtra(WalletConstants.EXTRA_MASKED_WALLET)) {

                    ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT)
                            .setCheckoutStep(1)
                            .setCheckoutOptions("GoogleWallet");

                    HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder()
                            .setProductAction(productAction)
                            .setAction("Change Payment")
                            .setCategory("Checkout");

                    mTracker.send(builder.build());
                    mMaskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                    ((FullWalletConfirmationButtonFragment) getResultTargetFragment())
                            .updateMaskedWallet(mMaskedWallet);
                    displayLoyaltyInformation();
                }
                  // you may also want to use the new masked wallet data here, say to recalculate
                  // shipping or taxes if shipping address changed
                break;
            case WalletConstants.RESULT_ERROR:
                int errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, 0);
                handleError(errorCode);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    private void writeData(Intent data) {
		
    	if(data.hasExtra(WalletConstants.EXTRA_FULL_WALLET)){
//    		data.getExtras(WalletConstants.EXTRA_FULL_WALLET);
    	}
	}

	@Override
    public Fragment getResultTargetFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.full_wallet_confirmation_button_fragment);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 switch(v.getId()){
	      case R.id.leftTextView:
	    	  finish();
	    	  break;
	      case R.id.homeImageView:
	    	  goToHome();
	    	  break;
	      }
	}
}

package com.kikr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kikr.R;

public class DumFragmentGoogleWallet extends FragmentBaseGoogleWallet {
//	private SupportWalletFragment mWalletFragment;
	String itemCount="1";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dum_googlewallet, null);
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		createAndAddWalletFragment();
	}


	@Override
	public void setData(Bundle bundle) {

	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {

	}

	private void createAndAddWalletFragment() {
//		WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
//				.setBuyButtonText(BuyButtonText.BUY_WITH_GOOGLE)
//				.setBuyButtonWidth(Dimension.MATCH_PARENT);
//
//		WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions
//				.newBuilder()
//				.setEnvironment(AppConstants.GOOGLE_WALLET_ENVIRONMENT)
//				.setFragmentStyle(walletFragmentStyle)
//				.setTheme(WalletConstants.THEME_HOLO_LIGHT)
//				.setMode(WalletFragmentMode.BUY_BUTTON).build();
//		mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
//
//		// Now initialize the Wallet Fragment
//		String accountName = ((KikrApp) mContext.getApplication()).getAccountName();
//		MaskedWalletRequest maskedWalletRequest = WalletUtil
//				.createMaskedWalletRequest(Constants.getDummyToatlObject());
//		WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams
//				.newBuilder()
//				.setMaskedWalletRequest(maskedWalletRequest)
//				.setMaskedWalletRequestCode(
//						AppConstants.REQUEST_CODE_MASKED_WALLET)
//				.setAccountName(accountName);
//		mWalletFragment.initialize(startParamsBuilder.build());
//
//		// add Wallet fragment to the UI
//		mContext.getSupportFragmentManager().beginTransaction()
//				.replace(R.id.dynamic_wallet_button_fragment, mWalletFragment)
//				.commit();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// retrieve the error code, if available
//		System.out.println("1234567 DumFragmentGoogleWallet.onActivityResult()");
//		int errorCode = -1;
//		if (data != null) {
//			errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
//		}
//		switch (requestCode) {
//		case AppConstants.REQUEST_CODE_MASKED_WALLET:
//			switch (resultCode) {
//			case Activity.RESULT_OK:
//				MaskedWallet maskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
//				launchConfirmationPage(maskedWallet);
//				break;
//			case Activity.RESULT_CANCELED:
//				break;
//			default:
//				handleError(errorCode);
//				break;
//			}
//			break;
//		case WalletConstants.RESULT_ERROR:
//			handleError(errorCode);
//			break;
//		default:
//			super.onActivityResult(requestCode, resultCode, data);
//			break;
//		}
	}

//	private void launchConfirmationPage(MaskedWallet maskedWallet) {
//		Intent intent = new Intent(mContext, ConfirmationActivity.class);
//		intent.putExtra(Constants.EXTRA_ITEM_ID, 0);
//		intent.putExtra(Constants.EXTRA_ITEM_DETAIL, Constants.getDummyToatlObject());
//		intent.putExtra(Constants.EXTRA_MASKED_WALLET, maskedWallet);
//		startActivity(intent);
//	}

	@Override
	public Fragment getResultTargetFragment() {
		return null;
	}

}

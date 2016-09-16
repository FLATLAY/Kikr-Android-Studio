package com.kikr.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.kikr.BaseFragment;
import com.kikr.googlewallet.FullWalletConfirmationButtonFragment;
import com.kikr.googlewallet.LoginFragment;

public abstract class FragmentBaseGoogleWallet extends BaseFragment{

	 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 System.out.println("1234567 FragmentBaseGoogleWallet.onActivityResult()");
	        switch (requestCode) {
	            case FullWalletConfirmationButtonFragment.REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET:
	            case FullWalletConfirmationButtonFragment.REQUEST_CODE_RESOLVE_ERR:
//	            case PromoAddressLookupFragment.REQUEST_CODE_RESOLVE_ADDRESS_LOOKUP:
//	            case PromoAddressLookupFragment.REQUEST_CODE_RESOLVE_ERR:
	            case LoginFragment.REQUEST_CODE_RESOLVE_ERR:
	                Fragment fragment = getResultTargetFragment();
	                if (fragment != null) {
	                    fragment.onActivityResult(requestCode, resultCode, data);
	                }
	                break;
	            default:
	                super.onActivityResult(requestCode, resultCode, data);
	                break;
	        }
	    }


	    protected void handleError(int errorCode) {
//	        switch (errorCode) {
//	            case WalletConstants.ERROR_CODE_SPENDING_LIMIT_EXCEEDED:
//	                Toast.makeText(mContext, getString(R.string.spending_limit_exceeded, errorCode),Toast.LENGTH_LONG).show();
//	                break;
//	            case WalletConstants.ERROR_CODE_INVALID_PARAMETERS:
//	            case WalletConstants.ERROR_CODE_AUTHENTICATION_FAILURE:
//	            case WalletConstants.ERROR_CODE_BUYER_ACCOUNT_ERROR:
//	            case WalletConstants.ERROR_CODE_MERCHANT_ACCOUNT_ERROR:
//	            case WalletConstants.ERROR_CODE_SERVICE_UNAVAILABLE:
//	            case WalletConstants.ERROR_CODE_UNSUPPORTED_API_VERSION:
//	            case WalletConstants.ERROR_CODE_UNKNOWN:
//	            default:
//	                // unrecoverable error
//	                String errorMessage = getString(R.string.google_wallet_unavailable) + "\n" +getString(R.string.error_code, errorCode);
//	                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
//	                break;
//	        }
	    }
	    
	    protected abstract Fragment getResultTargetFragment();
}

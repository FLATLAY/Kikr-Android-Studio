package com.flatlay.googlewallet;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.flatlay.BaseActivity;

public abstract class BikestoreFragmentActivity extends BaseActivity {

    protected static final int REQUEST_USER_LOGIN = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FullWalletConfirmationButtonFragment.REQUEST_CODE_RESOLVE_LOAD_FULL_WALLET:
            case FullWalletConfirmationButtonFragment.REQUEST_CODE_RESOLVE_ERR:
//          case PromoAddressLookupFragment.REQUEST_CODE_RESOLVE_ADDRESS_LOOKUP:
//          case PromoAddressLookupFragment.REQUEST_CODE_RESOLVE_ERR:
            case LoginFragment.REQUEST_CODE_RESOLVE_ERR:
                Fragment fragment = getResultTargetFragment();
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case REQUEST_USER_LOGIN:
                if (resultCode == RESULT_OK) {
                    ActivityCompat.invalidateOptionsMenu(this);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    protected void handleError(int errorCode) {

    }
    
    protected abstract Fragment getResultTargetFragment();

}

package com.flatlay.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.flatlay.BaseFragment;

public abstract class FragmentBaseGoogleWallet extends BaseFragment{

	 @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 System.out.println("1234567 FragmentBaseGoogleWallet.onActivityResult()");
		  super.onActivityResult(requestCode, resultCode, data);

	    }


	    protected void handleError(int errorCode) {
	    }
	    
	    protected abstract Fragment getResultTargetFragment();
}

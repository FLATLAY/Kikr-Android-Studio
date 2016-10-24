package com.flatlay.fragment;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentSupport extends BaseFragment implements OnClickListener{
	TextView helpTextView,customerSupportTextView,termsTextView,privacyTextView,legalTextView, kikr_learn_more, textViewOutside;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_support, null);
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		helpTextView=(TextView) getView().findViewById(R.id.helpTextView);
		customerSupportTextView=(TextView) getView().findViewById(R.id.customerSupportTextView);
		termsTextView=(TextView) getView().findViewById(R.id.termsTextView);
		privacyTextView=(TextView) getView().findViewById(R.id.privacyTextView);
		legalTextView=(TextView) getView().findViewById(R.id.legalTextView);
		kikr_learn_more=(TextView) getView().findViewById(R.id.kikr_learn_more);
		textViewOutside= (TextView)getView().findViewById(R.id.textViewOutside);
	}

	@Override
	public void setData(Bundle bundle) {
		setTextType();
	}

	private void setTextType() {
		helpTextView.setTypeface(FontUtility.setProximanovaLight(mContext));
		customerSupportTextView.setTypeface(FontUtility.setProximanovaLight(mContext));
		termsTextView.setTypeface(FontUtility.setProximanovaLight(mContext));
		privacyTextView.setTypeface(FontUtility.setProximanovaLight(mContext));
		legalTextView.setTypeface(FontUtility.setProximanovaLight(mContext));
		kikr_learn_more.setTypeface(FontUtility.setProximanovaLight(mContext));
		kikr_learn_more.setPaintFlags(kikr_learn_more.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		textViewOutside.setTypeface(FontUtility.setProximanovaLight(mContext));
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		customerSupportTextView.setOnClickListener(this);
		termsTextView.setOnClickListener(this);
		privacyTextView.setOnClickListener(this);
		legalTextView.setOnClickListener(this);
		kikr_learn_more.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.customerSupportTextView:
			if(((HomeActivity) mContext).checkInternet())
			//	sendEmail();
			//initShareIntent();
			share();
			break;
		case R.id.termsTextView:
			if(((HomeActivity) mContext).checkInternet())
				addFragment(new FragmentProductDetailWebView(AppConstants.TERMS_OF_USE_URL));
			break;
		case R.id.privacyTextView:
			if(((HomeActivity) mContext).checkInternet())
				addFragment(new FragmentProductDetailWebView(AppConstants.PRIVACY_POLICY_URL));
			break;
		case R.id.legalTextView:
			if(((HomeActivity) mContext).checkInternet())
			addFragment(new FragmentProductDetailWebView(AppConstants.LEGAL_URL));
		//	readfile();
			break;
		case R.id.kikr_learn_more:
				addFragment(new FragmentLearnMoreOutsideUS());
			break;
		default:
			break;
		}
	}



	private void sendEmail() {
		String[] TO = {"support@icetech-inc.com"};
	      Intent emailIntent = new Intent(Intent.ACTION_SEND);
	      emailIntent.setType("message/rfc822");
	  //    emailIntent.setData(Uri.parse("mailto:"));

	      emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
	      emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FLATLAY - I need help!");
	   //   emailIntent.putExtra(Intent.EXTRA_TEXT, getBody());
	      

	      try {
	         startActivity(Intent.createChooser(emailIntent, "Choose an Email client..."));
	         Log.i("Finished sending email...", "");
	      } catch (android.content.ActivityNotFoundException ex) {
	    	  AlertUtils.showToast(mContext, "There is no email client installed");
	      }
	}

	
	private void initShareIntent() {
	    boolean found = false;
	    Intent share = new Intent(android.content.Intent.ACTION_SEND);
	    String[] TO = {"support@icetech-inc.com"};
	    share.setType("message/rfc822");

	    // gets the list of intents that can be loaded.
	    List<ResolveInfo> resInfo = mContext.getPackageManager().queryIntentActivities(share, 0);
	    if (!resInfo.isEmpty()){
	        for (ResolveInfo info : resInfo) {
	            if (info.activityInfo.packageName.toLowerCase().contains("mail") || 
	                    info.activityInfo.name.toLowerCase().contains("mail") ) {
	            	share.putExtra(Intent.EXTRA_EMAIL, TO);
	            	share.putExtra(Intent.EXTRA_SUBJECT, "FLATLAY - I need help!");
	                share.setPackage(info.activityInfo.packageName);
	                found = true;
	                break;
	            }
	        }
	        if (!found)
	            return;

	        startActivity(Intent.createChooser(share, "Choose an Email client..."));
	    }
	}
	
	private void share() {
	    List<Intent> targetedShareIntents = new ArrayList<Intent>();
	    Intent share = new Intent(android.content.Intent.ACTION_SEND);
	    String[] TO = {"support@icetech-inc.com"};
	    share.setType("message/rfc822");
	    List<ResolveInfo> resInfo = mContext.getPackageManager().queryIntentActivities(share, 0);
	    if (!resInfo.isEmpty()){
	        for (ResolveInfo info : resInfo) {
	            Intent targetedShare = new Intent(android.content.Intent.ACTION_SEND);
	            targetedShare.setType("message/rfc822"); // put here your mime type

	            if (info.activityInfo.packageName.toLowerCase().contains("mail") || 
	                    info.activityInfo.name.toLowerCase().contains("mail")) {
	            	targetedShare.putExtra(Intent.EXTRA_EMAIL, TO);
	            	targetedShare.putExtra(Intent.EXTRA_SUBJECT, "FLATLAY - I need help!");
	            	targetedShare.setPackage(info.activityInfo.packageName);
	                targetedShareIntents.add(targetedShare);
	            }
	        }

	        Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
	        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
	        startActivity(chooserIntent);
	    }
	}
	
	private String getBody() {
		String body="";
		String name=UserPreference.getInstance().getUserName();
		System.out.println("12345 name:"+name);
		body="User Id: "+UserPreference.getInstance().getUserID();
		if(!name.equals(""))
			body+=", User Name :"+name;
		return body;
	}

}

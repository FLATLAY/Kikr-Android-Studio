package com.kikr.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.kikr.BaseFragment;
import com.kikr.R;

public class FragmentPurchaseGuarantee extends BaseFragment implements OnClickListener {
	private View mainView;
	private TextView txtHere;
	private TextView txtContact;
	
	public FragmentPurchaseGuarantee() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_purchase_guarantee, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		txtHere = (TextView) mainView.findViewById(R.id.txtHere);
		txtContact = (TextView) mainView.findViewById(R.id.txtContact);
		txtContact.setPaintFlags(txtContact.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		
		txtHere.setMovementMethod(LinkMovementMethod.getInstance());
		txtHere.setText("Please contact the merchant directly.");
//		txtHere.setText("Please contact the merchant directly. We provide a list of contact information for our merchants here.", BufferType.SPANNABLE);
//		Spannable mySpannable = (Spannable)txtHere.getText();
//		ClickableSpan myClickableSpan = new ClickableSpan() {
//			@Override
//			public void onClick(View widget) {
//				addFragment(new FragmentMerchants());
//			}
//		};
//		mySpannable.setSpan(myClickableSpan, 97, 101, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.black));
//		mySpannable.setSpan(fcs, 97, 101, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		txtContact.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtContact:
			addFragment(new FragmentSupport());
		}
	}

}

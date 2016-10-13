package com.kikr.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.flatlay.R;
import com.kikr.activity.HomeActivity;

public class FragmentPurchaseGuarantee extends BaseFragment implements OnClickListener {
	private View mainView;
	private TextView txtHere,learnflatlay;
	private TextView txtContact;
	private LinearLayout learnmorelayout;
     private Button back;
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
		back=(Button) mainView.findViewById(R.id.back);
		txtContact = (TextView) mainView.findViewById(R.id.txtContact);
		txtContact.setPaintFlags(txtContact.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		learnmorelayout=(LinearLayout)mainView.findViewById(R.id.learnmorelayout);
		learnflatlay=(TextView) mainView.findViewById(R.id.learnflatlay);
		learnflatlay.setPaintFlags(txtContact.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
		learnmorelayout.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtContact:
			addFragment(new FragmentSupport());
			break;
			case R.id.learnmorelayout:
				addFragment(new FragmentLearnMoreOutsideUS());
				break;
			case R.id.back:
				((HomeActivity)mContext).onBackPressed();
				break;
		}
	}

}

package com.flatlay.ibeacon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.googlewallet.CheckoutActivity;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.api.IbeaconApi;
import com.kikrlib.bean.IbeaconMessage;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;

public class BeaconMessageActivity extends BaseActivity implements OnClickListener{
	private ImageView  productImageView;
	private TextView productTitleTextView,descriptionTextView;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_beacon_message);
		recordUserActivity();
	}
	
	private void setDataMessage() {
		if(getIntent().hasExtra("data")){
//			String fullMessage="";
			IbeaconMessage message=(IbeaconMessage) getIntent().getSerializableExtra("data");
//			String id=message.getId();
			String msg=message.getMessage();
			String description=message.getDescription();
			String image=message.getImage();
//			String smallImage=message.getSmallimage();
			url=message.getUrl();
			if(msg!=null)
				productTitleTextView.setText(msg);
			else
				productTitleTextView.setText("No message");
			if(image!=null)
			CommonUtility.setImage(context, image, productImageView, R.drawable.dum_list_item_brand);

			if(description!=null)
				descriptionTextView.setText(description);	
//			else
//				descriptionTextView.setText("No description found");	
		}
	}

	@Override
	public void initLayout() {
		productImageView = (ImageView) findViewById(R.id.productImageView);
		productTitleTextView = (TextView) findViewById(R.id.productTitleTextView);
		descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
	}

	@Override
	public void setupData() {
		setDataMessage();
	}

	@Override
	public void headerView() {
		setBackHeader();
		getLeftTextView().setOnClickListener(this);
		getHomeImageView().setOnClickListener(this);
	}

	@Override
	public void setUpTextType() {}

	@Override
	public void setClickListener() {
		productImageView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftTextView:
			Intent i=new Intent(this,HomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(i);
			finish();
			break;
		case R.id.homeImageView:
			goToHome();
			break;
		case R.id.checkoutButton:
			startActivity(CheckoutActivity.class);
			break;
		case R.id.descriptionTextView:
			break;
		case R.id.productImageView:
			rediectToBrowser();
			break;
		default:
			break;
		}
	}
	private void rediectToBrowser() {
		recordUserClickedActivity();
		if(url!=null){
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(browserIntent);
		}
	}
	
	private void recordUserActivity() {
		IbeaconApi ibeaconApi=new IbeaconApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				
				
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				
				
			}
		});
		IbeaconMessage message=(IbeaconMessage) getIntent().getSerializableExtra("data");
		String id=message.getId();
		ibeaconApi.campaignViewed(UserPreference.getInstance().getUserID(), id);
		ibeaconApi.execute();
	}

	private void recordUserClickedActivity() {
		IbeaconApi ibeaconApi=new IbeaconApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				
				
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				
				
			}
		});
		IbeaconMessage message=(IbeaconMessage) getIntent().getSerializableExtra("data");
		String id=message.getId();
		ibeaconApi.campaignActionTaken(UserPreference.getInstance().getUserID(), id);
		ibeaconApi.execute();
	}
}

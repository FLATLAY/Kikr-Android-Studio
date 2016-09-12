package com.kikr.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.dialog.RemoveAddressDialog;
import com.kikr.fragment.FragmentSettings;
import com.kikr.fragment.FragmentShippingInfo;
import com.kikrlib.api.AddressApi;
import com.kikrlib.bean.Address;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.AddressRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class SettingsAddressList {
	FragmentActivity mContext;
	List<Address> data;
	LayoutInflater mInflater;
	private FragmentSettings fragmentSettings;
	private SettingsAddressList addressList;
	private ProgressBarDialog progressBarDialog;
	
	public SettingsAddressList(FragmentActivity context, List<Address> data, FragmentSettings fragmentSettings) {
		super();
		this.mContext = context;
		this.fragmentSettings = fragmentSettings;
		addressList=this;
		this.data = (ArrayList<Address>) data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView() {
		LinearLayout ll =  new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < data.size(); i++) {
			View convertView = (LinearLayout) mInflater.inflate(R.layout.adapter_settings_shipping_address,null);
			TextView addressText = (TextView) convertView.findViewById(R.id.addressText);
			final LinearLayout addressEditImage = (LinearLayout) convertView.findViewById(R.id.addressEditImage);
			View lineView=convertView.findViewById(R.id.lineView);
			addressText.setText(data.get(i).getTitle() + " "
					+ data.get(i).getFirstname() + " "
					+ data.get(i).getLastname() + "\n"
					+ data.get(i).getStreet_address() + " " + data.get(i).getApartment() +", "
					+ data.get(i).getCity_town() + ", "
					+ data.get(i).getState() + ", "
					+ data.get(i).getCountry() + ", "
					+ data.get(i).getZip_code());
			ll.addView(convertView);
			convertView.setTag(i);
			addressEditImage.setTag(i);
			if(i==data.size()-1)
				lineView.setVisibility(View.GONE);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					((HomeActivity) mContext).addFragment(new FragmentShippingInfo(data.get((Integer) view.getTag()),fragmentSettings));
				}
			});
			addressEditImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if(((HomeActivity) mContext).checkInternet()){
						RemoveAddressDialog removeAddressDialog = new RemoveAddressDialog(mContext, data.get((Integer) view.getTag()).getAddress_id(),addressList);
						removeAddressDialog.show();
					}
				}
			});
		}
		return ll;
	}
	
	public void deleteAddress(String addressId) {

		progressBarDialog = new ProgressBarDialog(mContext);
		progressBarDialog.show();
		final AddressApi service = new AddressApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>"+object);
				if(object!=null){
					fragmentSettings.getAddressList(false);
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception,Object object) {
				progressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>"+object);
				if(object!=null){
					AddressRes response=(AddressRes) object;
					AlertUtils.showToast(mContext,response.getMessage());
				}else{
					AlertUtils.showToast(mContext,R.string.invalid_response);
				}
				
			}
		});
		service.removeAddress(UserPreference.getInstance().getUserID(), addressId);
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}
	
}

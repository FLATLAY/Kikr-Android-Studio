package com.flatlay.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentPlaceMyOrder;
import com.flatlay.fragment.FragmentShippingInfo;
import com.kikrlib.bean.Address;

import java.util.ArrayList;
import java.util.List;

public class ShippingAddressList {
	FragmentActivity mContext;
	List<Address> data;
	LayoutInflater mInflater;
	List<RadioButton> radioButtons=new ArrayList<RadioButton>();
	private FragmentPlaceMyOrder fragmentPlaceMyOrder;
	Address selectedAddress;
	
	public ShippingAddressList(FragmentActivity context, List<Address> data, FragmentPlaceMyOrder fragmentPlaceMyOrder, Address selectedAddress) {
		super();
		this.mContext = context;
		this.data = (ArrayList<Address>) data;
		this.fragmentPlaceMyOrder=fragmentPlaceMyOrder;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.selectedAddress=selectedAddress;
	}
	
	public View getView() {
		LinearLayout ll =  new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);
		for (int i = 0; i < data.size(); i++) {
			View convertView = (LinearLayout) mInflater.inflate(R.layout.adapter_shipping_address,null);
			TextView addressText = (TextView) convertView.findViewById(R.id.addressText);
			ImageView editAddress = (ImageView) convertView.findViewById(R.id.editAddress);
			RadioButton addressRadioButton = (RadioButton) convertView.findViewById(R.id.addressRadioButton);
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
			editAddress.setTag(i);
			if(selectedAddress!=null&&selectedAddress.getAddress_id().equals(data.get(i).getAddress_id())){
				addressRadioButton.setBackgroundColor(Color.WHITE);
				addressRadioButton.setChecked(true);
			}
			addressRadioButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View view) {
					RadioButton button=(RadioButton) view;
					for(int i=0;i<radioButtons.size();i++){
						if(!radioButtons.get(i).equals(button)){
							radioButtons.get(i).setChecked(false);
						}else {
//							button.setChecked(!button.isChecked());
						}
					}
					if (((HomeActivity) mContext).checkInternet()) {
						getSelectedAddress();
						fragmentPlaceMyOrder.setAddress(getSelectedAddress());
//						fragmentPlaceMyOrder.getTotalInfo(getSelectedAddress().getAddress_id());
//						fragmentPlaceMyOrder.changePlaceOrderColor();
					}
					
				}
			});
			radioButtons.add(addressRadioButton);
			editAddress.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					((HomeActivity)mContext).addFragment(new FragmentShippingInfo(data.get((Integer) view.getTag()), fragmentPlaceMyOrder));
				}
			});
		}
		return ll;
	}
	
	public Address getSelectedAddress() {
		for (int i = 0; i < radioButtons.size(); i++) {
			if (radioButtons.get(i).isChecked()) {
				fragmentPlaceMyOrder.isAddressSelected = true;
				return data.get(i);
			} else
			fragmentPlaceMyOrder.isAddressSelected = false;
		}
		return null;
	}
}

package com.flatlay.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.AddressVerificationDialog;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.ValidZipcode;
import com.flatlaylib.api.AddressApi;
import com.flatlaylib.api.LOBApi;
import com.flatlaylib.bean.Address;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.AddressRes;
import com.flatlaylib.utils.AlertUtils;

public class FragmentShippingInfo extends BaseFragment implements
		OnClickListener, ServiceCallback {
	private View mainView;
	private EditText  streetNameEditText, apartmentEditText, cityEditText,
			zipEditText, firstNameEditText, lastNameEditText,telephoneEditText;
	private Spinner countryEditText,titleSpinner, stateSpinner;
	private Button doneBtn;
	private String streetnumber="", streetname, city, zipcode, country, firstname,lastname,state,telephone,countryForLob;
	private ProgressBarDialog mProgressBarDialog;
	private Address address;
	private BaseFragment baseFragment;
	private String title;
	private String apartment;
String address_line1="";
	public FragmentShippingInfo(Address address, BaseFragment baseFragment) {
		this.address = address;
		this.baseFragment = baseFragment;
	}

	// public FragmentShippingInfo() {
	//
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_shipping_info, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
//		streetNumberEditText = (EditText) mainView.findViewById(R.id.streetNumberEditText);
		streetNameEditText = (EditText) mainView.findViewById(R.id.streetNameEditText);
		apartmentEditText = (EditText) mainView.findViewById(R.id.apartmentEditText);
		cityEditText = (EditText) mainView.findViewById(R.id.cityEditText);
		zipEditText = (EditText) mainView.findViewById(R.id.zipEditText);
		firstNameEditText = (EditText) mainView.findViewById(R.id.firstNameEditText);
		lastNameEditText = (EditText) mainView.findViewById(R.id.lastNameEditText);
		stateSpinner = (Spinner) mainView.findViewById(R.id.stateSpinner);
		telephoneEditText = (EditText) mainView.findViewById(R.id.telephoneEditText);
		countryEditText = (Spinner) mainView.findViewById(R.id.countryEditText);
		titleSpinner = (Spinner) mainView.findViewById(R.id.titleSpinner);
		doneBtn = (Button) mainView.findViewById(R.id.doneBtn);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		doneBtn.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
		if (address != null) {
//			streetNumberEditText.setText(address.getStreet_number());
			streetNameEditText.setText(address.getStreet_address());
			cityEditText.setText(address.getCity_town());
			zipEditText.setText(address.getZip_code());
			firstNameEditText.setText(address.getFirstname());
			lastNameEditText.setText(address.getLastname());
			telephoneEditText.setText(address.getTel());
			apartmentEditText.setText(address.getApartment());
			if (address.getCountry().equals("United States of America")) {
				countryEditText.setSelection(0);
				String[] stateUSArray = getResources().getStringArray(R.array.statesUSA);
				
				for (int i = 0; i < stateUSArray.length; i++) {           
				    if(stateUSArray[i].equalsIgnoreCase(address.getState()))
				    	stateSpinner.setSelection(i);
				}
			}else if(address.getCountry().equals("Canada"))
				countryEditText.setSelection(1);
			if (address.getTitle().equals("Mr.")) {
				titleSpinner.setSelection(0);
			}else if(address.getTitle().equals("Miss")){
				titleSpinner.setSelection(1);
			} else if(address.getTitle().equals("Mrs."))
				titleSpinner.setSelection(1);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.doneBtn:
			validateUserInput();
			break;
		default:
			break;
		}
	}

	private void validateUserInput() {
		boolean isValid = true;
		title = titleSpinner.getSelectedItem().toString();
		firstname = firstNameEditText.getText().toString().trim();
		lastname = lastNameEditText.getText().toString().trim();
		streetname = streetNameEditText.getText().toString().trim();
		city = cityEditText.getText().toString().trim();
		zipcode = zipEditText.getText().toString().trim();
		country = countryEditText.getSelectedItem().toString();
		state = stateSpinner.getSelectedItem().toString().trim();
		telephone = telephoneEditText.getText().toString().trim();
		apartment = apartmentEditText.getText().toString().trim();
		countryForLob = getCountryForLob();
		if (TextUtils.isEmpty(apartment)) {
			apartment = "";
		}
		if (firstname.length() == 0) {
			isValid = false;
			firstNameEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_firstname_blank);
		}
		else if (lastname.length() == 0) {
			isValid = false;
			lastNameEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_lastname_blank);
		} 
		else if (streetname.length() == 0) {
			isValid = false;
			streetNameEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_streetname_blank);
		} else if (city.length() == 0) {
			isValid = false;
			cityEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_city_blank);
		} else if (state.length() == 0) {
			isValid = false;
			stateSpinner.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_state_blank);
		}else if (zipcode.length() == 0) {
			isValid = false;
			zipEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_zip_blank);
		} else if (country.length() == 0) {
			isValid = false;
			countryEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_countryname_blank);
		} else if (!checkValidZipCode()) {
			isValid = false;
			zipEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_invalid_zipcode);
		}else if (telephone.length()==0) {
			isValid = false;
			telephoneEditText.requestFocus();
			AlertUtils.showToast(mContext, R.string.alert_telephone_blank);
		}

		if (isValid && ((HomeActivity) mContext).checkInternet()) {
			CommonUtility.hideSoftKeyboard(mContext);
			validateAddressviaLOB();
		}
	}

	private String getCountryForLob() {
		if(countryEditText.getSelectedItemPosition()==0){
			countryForLob = "US";
		}else if(countryEditText.getSelectedItemPosition()==1){
			countryForLob = "CA";
		}
		return countryForLob;
	}

	private boolean checkValidZipCode() {
		if (countryEditText.getSelectedItemPosition() == 0) {
			if (ValidZipcode.isValidPostalUSCode(zipcode)) {
				return true;
			}
		} else if (countryEditText.getSelectedItemPosition() == 1) {
			if (ValidZipcode.isValidPostalCanadaCode(zipcode)) {
				return true;
			}
		}
		return false;
	}

	
	private void validateAddressviaLOB() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();

		final LOBApi addressApi = new LOBApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				if (object != null) {
					try {
						JSONObject addressRes = new JSONObject(object.toString());
						Log.e("addressre", addressRes.toString());
						if (addressRes.toString().contains("The address you entered was found but more information is needed (such as an apartment, suite, or box number) to match to a specific address")) {
							AddressVerificationDialog verificationDialog = new AddressVerificationDialog(mContext, "More information is needed(such as an apartment, suite, or box number)");
							verificationDialog.show();
							//AlertUtils.showToast(mContext, "More information is needed(such as an apartment, suite, or box number)");
						} else if (addressRes.has("address")) {
							sendDataToServer();
						} else if (addressRes.has("error")) {
							String msg = addressRes.getJSONObject("error").getString("message");
							AddressVerificationDialog verificationDialog = new AddressVerificationDialog(mContext, msg);
							verificationDialog.show();
							//AlertUtils.showToast(mContext, msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				AddressVerificationDialog verificationDialog = new AddressVerificationDialog(mContext, "Address not found or seems to be invalid");
				verificationDialog.show();
				//AlertUtils.showToast(mContext, "Address not found or seems to be invalid");
				}
		});
		addressApi.validateAddress(streetname,apartment, state, city, zipcode, countryForLob);
		addressApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				addressApi.cancel();
			}
		});
	}

	private void sendDataToServer() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();

		final AddressApi addressApi = new AddressApi(this);
		if (address != null) {
			addressApi.updateAddress(UserPreference.getInstance().getUserID(),
					address.getAddress_id(),title, firstname, lastname, state, telephone, streetname,
					city, zipcode, country,apartment);
		} else {
			addressApi.addAddress(UserPreference.getInstance().getUserID(),title,
					firstname, lastname, state, telephone, streetname, city, zipcode, country,apartment);
		}
		addressApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				addressApi.cancel();
			}
		});
	}

	@Override
	public void handleOnSuccess(Object object) {
		mProgressBarDialog.dismiss();
		if (object != null) {
			AddressRes addressRes = (AddressRes) object;
			AlertUtils.showToast(mContext, addressRes.getMessage());
			((HomeActivity) mContext).onBackPressed();
			if (baseFragment != null) {
				if (baseFragment instanceof FragmentSettings)
					((FragmentSettings) baseFragment).getAddressList(false);
				if (baseFragment instanceof FragmentPlaceMyOrder) {
					((FragmentPlaceMyOrder) baseFragment).getAddressList();
				}
			}
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		mProgressBarDialog.dismiss();
		if (object != null) {
			AddressRes response = (AddressRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}

}
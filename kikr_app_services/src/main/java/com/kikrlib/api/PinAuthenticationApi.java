package com.kikrlib.api;

import java.util.List;

import org.apache.http.NameValuePair;

import com.google.gson.JsonParseException;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.PinAuthResponse;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class PinAuthenticationApi extends AbsService {


	public PinAuthenticationApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
	public void authanticatPin(String pin){
		this.METHOD_NAME=WebConstants.HOST_FILE+"pinauthentication/"+pin+"/"+UserPreference.getInstance().getUserID();
	}
	
	public void resendPin(){
		this.METHOD_NAME=WebConstants.HOST_FILE+"resendpin/"+UserPreference.getInstance().getUserID();
	}
	
	@Override
	public String getActionName() {
		// TODO Auto-generated method stub
		return METHOD_NAME;
	}
	
	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		 return WebConstants.HTTP_METHOD_GET;
	}

	@Override
	public List<NameValuePair> getNameValueRequest() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In PinAuthenticationApi processResponse>>"+response);
		try {
			PinAuthResponse userResponse = JsonUtils.fromJson(response, PinAuthResponse.class);
			if(userResponse.getCode().equals(WebConstants.SUCCESS_CODE)){
				isValidResponse = true;
			}
			serviceResponse=userResponse;
		} 
		catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}

}

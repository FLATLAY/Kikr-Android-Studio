package com.kikrlib.api;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.JsonParseException;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.BankListRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class BankListApi extends AbsService{
	
	private String requestValue;
	private String requestType;
	
	public BankListApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
	public void getBankList() {
		
		this.METHOD_NAME=WebConstants.HOST_FILE+"banklist";
		requestType=WebConstants.HTTP_METHOD_GET;
	}

	@Override
	public String getActionName() {
		return METHOD_NAME;
	}

	@Override
	public String getMethod() {
		return requestType;
	}
	
	@Override
	public List<NameValuePair> getNameValueRequest() {
		
		return null;
	}

	@Override
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
	}
	
	@Override
	public String getJsonRequest() {
		try {
			JSONArray array=new JSONArray(requestValue);
			return array.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			JSONArray array=new JSONArray();
			return array.toString();
		}
	}

	
	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>"+response);
		try {
			BankListRes bankListresponse = JsonUtils.fromJson(response, BankListRes.class);
			if(bankListresponse.getCode().equals(WebConstants.SUCCESS_CODE)){
				isValidResponse = true;
			}
			serviceResponse=bankListresponse;
		} 
		catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		}catch (NullPointerException e){
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

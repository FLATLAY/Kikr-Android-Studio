package com.flatlaylib.api;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.GetUUIDRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class GetUUIDApi extends AbsService{
	
	private String requestValue;
	
	public GetUUIDApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
	public void getUUIDList(String appId) {
		
		this.METHOD_NAME=WebConstants.HOST_FILE+"getuuids/"+appId;
	}

	@Override
	public String getActionName() {
		return METHOD_NAME;
	}

	@Override
	public String getMethod() {
		return WebConstants.HTTP_METHOD_GET;
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
		Syso.info("In RegisterUserApi processResponse46>>"+response);
		try {
			GetUUIDRes uuidResponse = JsonUtils.fromJson(response, GetUUIDRes.class);
			if(uuidResponse.getCode().equals(WebConstants.SUCCESS_CODE)){
				isValidResponse = true;
			}
			serviceResponse=uuidResponse;
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

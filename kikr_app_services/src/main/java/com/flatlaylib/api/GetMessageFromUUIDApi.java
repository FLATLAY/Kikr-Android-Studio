package com.flatlaylib.api;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.GetMessageRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class GetMessageFromUUIDApi extends AbsService {

	private String requestValue;

	public GetMessageFromUUIDApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
	public void getMessage(String appId,String uuid,String major,String minor,String userId){
		this.METHOD_NAME=WebConstants.HOST_FILE+"getmessage/"+appId+"/"+uuid+"/"+major+"/"+minor+"/"+userId;
	}
	
	
	@Override
	public String getMethod() {
		 return WebConstants.HTTP_METHOD_GET;
	}

	@Override
	public String getActionName() {
		return METHOD_NAME;
	}

	@Override
	public List<NameValuePair> getNameValueRequest() {
		return null;
	}

	@Override
	public String getJsonRequest() {
		try {
			JSONArray array = new JSONArray(requestValue);
			return array.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			JSONArray array = new JSONArray();
			return array.toString();
		}
	}

	@Override
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			GetMessageRes messageRes = JsonUtils.fromJson(response,GetMessageRes.class);
			if (messageRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = messageRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

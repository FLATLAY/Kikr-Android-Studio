package com.flatlaylib.api;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.flatlaylib.bean.DealToken;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class OnlineDealApi  extends AbsService{
	
	private String requestValue;
	private String requestType;
	private String token;
    private String condition="";
	
	public OnlineDealApi(ServiceCallback serviceCallback,String token,String condition) {
		super();
		this.mServiceCallback = serviceCallback;
		this.token=token;
		this.condition=condition;
	}

	public void getTocken() {
		this.METHOD_NAME = "https://api.rakutenmarketing.com/coupon/1.0?"+condition;
		requestType = WebConstants.HTTP_METHOD_POST;
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
		return "Bearer "+token;
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			DealToken activityRes = JsonUtils.fromJson(response,DealToken.class);
			if (TextUtils.isEmpty(activityRes.getError())) {
				isValidResponse = true;
			}
			serviceResponse = activityRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}
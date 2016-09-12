package com.kikrlib.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.kikrlib.bean.DealToken;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class TokenApi extends AbsService{
	
	private String requestValue;
	private String requestType;

	public TokenApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getTocken() {
		this.METHOD_NAME = "https://api.rakutenmarketing.com/token";
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
		List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
		requestParams.add(new BasicNameValuePair("grant_type","password"));
		requestParams.add(new BasicNameValuePair("username","alisammour"));
		requestParams.add(new BasicNameValuePair("password","Linkshare12"));
		requestParams.add(new BasicNameValuePair("scope", "3198835"));
		return requestParams;
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
		return "Basic MG1DbVgzc2hkMzQ0dGlXZkg0T2g1akdsY1A0YTpoNm0zVHpDalZ0Vk9aNmJpUF9vb1l0UUE1YUVh";
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

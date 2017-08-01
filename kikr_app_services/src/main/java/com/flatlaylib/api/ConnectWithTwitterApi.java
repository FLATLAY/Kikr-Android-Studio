package com.flatlaylib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.ConnectWithTwitterRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class ConnectWithTwitterApi extends AbsService{
	
	private String requestValue;
	private String requestType;
	
	public ConnectWithTwitterApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
	public void connectWithTwitter(String twitter_id,String description,String language,String location
			,String profile_image_url,String name,String screen_name,String status,String time_zone) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "connectwithtwitter";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", UserPreference.getInstance().getUserID());
		comment.put("twitterid", twitter_id);
		comment.put("description", description);
		comment.put("language",language);
		comment.put("location", location);
		comment.put("profile_image_url", profile_image_url);
		comment.put("name", name);
		comment.put("screen_name", screen_name);
		comment.put("status", status);
		comment.put("time_zone", time_zone);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
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
			JSONArray array=new JSONArray(requestValue);
			return array.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			JSONArray array=new JSONArray();
			return array.toString();
		}
	}
	@Override
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
	}
	
	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse57>>" + response);
		try {
			ConnectWithTwitterRes walletPinResponse = JsonUtils.fromJson(response, ConnectWithTwitterRes.class);
			if (walletPinResponse.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = walletPinResponse;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

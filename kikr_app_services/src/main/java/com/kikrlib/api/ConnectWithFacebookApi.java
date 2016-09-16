package com.kikrlib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.ConnectWithFacebookRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class ConnectWithFacebookApi extends AbsService{
	
	private String requestValue;
	private String requestType;
	
	public ConnectWithFacebookApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
	public void connectWithFacebook(String facebook_id,String gender,String birthday,String profile_link
			,String location,String name,String username) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "connectwithfb";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", UserPreference.getInstance().getUserID());
		comment.put("facebookid", facebook_id);
		comment.put("gender", gender);
		comment.put("birthday", birthday);
		comment.put("profile_link", profile_link);
		comment.put("location", location);
		comment.put("name", name);
		comment.put("username", username);
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
		Syso.info("In ConnectWithFacebookRes processResponse>>" + response);
		try {
			ConnectWithFacebookRes facebookRes = JsonUtils.fromJson(response, ConnectWithFacebookRes.class);
			if (facebookRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = facebookRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

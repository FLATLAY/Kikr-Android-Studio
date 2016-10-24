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
import com.flatlaylib.service.res.ChangePasswordRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class ChangePasswordApi extends AbsService {

	private String requestValue;
	private String requestType;

	public ChangePasswordApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
	public void checkCurrent(String user_id,String password) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "checkpassword";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("password", password);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void changeLoginPass(String user_id,String password) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "changepassword";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("password", password);
		Map[] maps=new Map[]{comment};
		Gson gson=new Gson();
		requestValue=gson.toJson(maps);
	}
	
	public void checkPasswordCreated(String user_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "passwordexist";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		Map[] maps=new Map[]{comment};
		Gson gson=new Gson();
		requestValue=gson.toJson(maps);
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
	public String getMethod() {
		return requestType;
	}

	@Override
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
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
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			ChangePasswordRes userResponse = JsonUtils.fromJson(response,ChangePasswordRes.class);
			if (userResponse.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = userResponse;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

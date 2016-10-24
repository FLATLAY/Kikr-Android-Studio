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
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class FollowUserApi extends AbsService {

	private String requestValue;
	private String requestType;

	public FollowUserApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getAllKikrUserList(String userid,String page_no,String gender) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getuserlist";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", userid);
		comment.put("pagenum", page_no);
		comment.put("gender", gender);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void followUser(String userid,String following_user_id) {

		this.METHOD_NAME = WebConstants.HOST_FILE + "followuser";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id",userid);
		comment.put("following_user_id", following_user_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void unFollowUser(String userid,String following_user_id) {

		this.METHOD_NAME = WebConstants.HOST_FILE + "unfollowuser";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id",userid);
		comment.put("following_user_id", following_user_id);
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
			FollowUserRes followUserRes = JsonUtils.fromJson(response,FollowUserRes.class);
			if (followUserRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = followUserRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

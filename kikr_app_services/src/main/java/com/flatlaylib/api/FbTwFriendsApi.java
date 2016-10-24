package com.flatlaylib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.flatlaylib.bean.FbUser;
import com.flatlaylib.bean.TwitterFriendList;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.FbTwFriendsRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class FbTwFriendsApi extends AbsService {

	private String requestValue;
	private String requestType;

	public FbTwFriendsApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getFriendsList(String user_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getfriends";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void addTwitterFriend(String user_id, List<TwitterFriendList> list) {
		try {
			this.METHOD_NAME = WebConstants.HOST_FILE + "addtwitterfriends";
			requestType = WebConstants.HTTP_METHOD_POST;
			Gson gson1 = new Gson();
			String requestValue1 = gson1.toJson(list);
			JSONArray array = new JSONArray(requestValue1);
			JSONObject object = new JSONObject();
			object.put("user_id", user_id);
			object.put("friend_list", array);
			JSONArray array2 = new JSONArray();
			array2.put(object);
			requestValue = array2.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addFacebookFriend(String user_id, List<FbUser> list) {
		try {
			this.METHOD_NAME = WebConstants.HOST_FILE + "addfbfriends";
			requestType = WebConstants.HTTP_METHOD_POST;
			Gson gson1 = new Gson();
			String requestValue1 = gson1.toJson(list);
			JSONArray array = new JSONArray(requestValue1);
			JSONObject object = new JSONObject();
			object.put("user_id", user_id);
			object.put("friend_list", array);
			JSONArray array2 = new JSONArray();
			array2.put(object);
			requestValue = array2.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
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
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			FbTwFriendsRes fbTwFriendsRes = JsonUtils.fromJson(response,FbTwFriendsRes.class);
			if (fbTwFriendsRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = fbTwFriendsRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

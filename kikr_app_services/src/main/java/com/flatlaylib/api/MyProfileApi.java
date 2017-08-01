package com.flatlaylib.api;

import android.util.Log;

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
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class MyProfileApi extends AbsService {

	private String requestValue;
	private String requestType;

	public MyProfileApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getUserProfileDetail(String user_id,String viewer_id) {
		Log.w("MyProfileApi","getUserProfileDetail");
		this.METHOD_NAME = WebConstants.HOST_FILE + "getprofile";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("viewer_id", viewer_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void getCollectionDetail(String user_id, String following_id, String collection_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getcollectiondetail";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("following_id", following_id);
		comment.put("collection_id", collection_id);
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
		Syso.info("In RegisterUserApi processResponse17>>" + response);
		try {
            Log.w("MyProfileApi","processResponse 1");
			MyProfileRes myProfileRes = JsonUtils.fromJson(response,MyProfileRes.class);
			if (myProfileRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
                Log.w("MyProfileApi","processResponse 4");
				isValidResponse = true;
			}
			serviceResponse = myProfileRes;
		} catch (JsonParseException e) {
            Log.w("MyProfileApi","processResponse 2");
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
            Log.w("MyProfileApi","processResponse 3");
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

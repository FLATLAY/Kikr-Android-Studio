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
import com.kikrlib.service.res.ActivityRes;
import com.kikrlib.service.res.UpdateCurrentScreenRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class UpdateCurrentScreenApi extends AbsService {

	private String requestValue;
	private String requestType;

	public UpdateCurrentScreenApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void updateScreen(String user_id, String current_screen) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "updatecurrentscreen";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("current_screen", current_screen);
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
			UpdateCurrentScreenRes activityRes = JsonUtils.fromJson(response,UpdateCurrentScreenRes.class);
			if (activityRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
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

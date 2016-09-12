package com.kikrlib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.CommonRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class EditInspirationApi extends AbsService {

	private String requestValue;
	private String requestType;

	public EditInspirationApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void editDescription(String user_id, String inspiration_id,String description) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "updateinspirationdescription";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("description", description);
		comment.put("inspiration_id", inspiration_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
	}

	public void removeTag(String user_id,String inspiration_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "removegeneraltag";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("inspiration_id", inspiration_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
	}
	
	public void removeProductsTags(String user_id,String inspiration_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "removeproducttag";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("inspiration_id", inspiration_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
	}
	
	public void addTag(String user_id, String inspiration_id, String item_type, String item_id, String item_name, String item_xy) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "addinspirationtag";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("inspiration_id", inspiration_id);
		comment.put("item_type", item_type);
		comment.put("item_id", item_id);
		comment.put("item_name", item_name);
		comment.put("item_xy", item_xy);
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
	}
	
	public void deletePost(String user_id,String inspiration_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "deleteinspirationpost";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("inspiration_id", inspiration_id);
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
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
//			JSONArray array = new JSONArray(requestValue);
			return requestValue;
		} catch (Exception e) {
			e.printStackTrace();
			JSONArray array = new JSONArray();
			return array.toString();
		}
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			CommonRes commonRes = JsonUtils.fromJson(response,CommonRes.class);
			if (commonRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = commonRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

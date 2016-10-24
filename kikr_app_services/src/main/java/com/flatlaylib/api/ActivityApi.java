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
import com.flatlaylib.service.res.ActivityRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class ActivityApi extends AbsService {

	private String requestValue;
	private String requestType;

	public ActivityApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getActivityListByMonth(String user_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "activitylistbymonth";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void getActivityMonthDetail(String user_id,String month, String year) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "activitymonthdetail";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("month", month);
		comment.put("year", year);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void getCollectionDetail(String user_id, String month, String year, String collection_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getcollectiondetail";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("month", month);
		comment.put("year", year);
		comment.put("collection_id", collection_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void addProductView(String user_id, String product_id, String collection_source) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "addview";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("product_id", product_id);
		comment.put("collection_source", collection_source);
		comment.put("type", "product");
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void addCollectionView(String user_id, String collection_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "addview";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("type", "collection");
		comment.put("collection_id", collection_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void addPayout(String user_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "addpayout";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
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
			ActivityRes activityRes = JsonUtils.fromJson(response,ActivityRes.class);
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

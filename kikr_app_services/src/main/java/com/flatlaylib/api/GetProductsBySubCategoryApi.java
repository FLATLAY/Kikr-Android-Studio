package com.flatlaylib.api;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.GetProductsByCategoryRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class GetProductsBySubCategoryApi extends AbsService {

	private String requestValue;
	private String requestType;

	public GetProductsBySubCategoryApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getCategory(String user_id,String cat1) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getcategoriesprosp";
		requestType = WebConstants.HTTP_METHOD_POST;
		requestValue = "{\"user_id\":\""+user_id+"\""+cat1+"}";
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
		Syso.info("In RegisterUserApi processResponse37>>" + response);
		try {
			GetProductsByCategoryRes activityRes = JsonUtils.fromJson(response,GetProductsByCategoryRes.class);
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

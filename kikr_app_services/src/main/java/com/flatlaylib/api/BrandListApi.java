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
import com.flatlaylib.service.res.BrandListRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class BrandListApi extends AbsService {

	private String requestValue;
	private String requestType;

	public BrandListApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getBrandList(String pagenum) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getbrands";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", UserPreference.getInstance().getUserID());
		comment.put("pagenum", pagenum);
		Map[] maps=new Map[]{comment};
		Gson gson=new Gson();
		requestValue=gson.toJson(maps);
	}

	public void addBrands(String brand_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "adduserbrand";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", UserPreference.getInstance().getUserID());
		comment.put("brand_id", brand_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void deleteBrand(String brand_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "deleteuserbrand";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", UserPreference.getInstance().getUserID());
		comment.put("brand_id", brand_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
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
		Syso.info("In RegisterUserApi processResponse52>>" + response);
		try {
			BrandListRes userResponse = JsonUtils.fromJson(response,BrandListRes.class);
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

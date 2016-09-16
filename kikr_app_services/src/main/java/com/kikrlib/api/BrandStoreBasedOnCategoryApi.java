package com.kikrlib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.BrandStoreRes;
import com.kikrlib.service.res.ProductBasedOnBrandRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class BrandStoreBasedOnCategoryApi extends AbsService {

	private String requestValue;
	private String requestType;

	public BrandStoreBasedOnCategoryApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getBrandsStoresByCategory(String cat_id,String page) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getbrandandstorebyctegory";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
	//	comment.put("user_id", user_id);
		comment.put("cat_id", cat_id);
		comment.put("pagenum", page);
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
			//JSONArray array = new JSONArray(requestValue);
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
			BrandStoreRes brandRes = JsonUtils.fromJson(response,BrandStoreRes.class);
			if (brandRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = brandRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

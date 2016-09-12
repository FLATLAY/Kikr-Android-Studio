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
import com.kikrlib.service.res.CollectionApiRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class CollectionApi extends AbsService {

	private String requestValue;
	private String requestType;

	public CollectionApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void addProductInCollection(String user_id,String collection_id, String product_id,String from_user_id,String from_collection_id, String product_image) {
		
		this.METHOD_NAME = WebConstants.HOST_FILE+ "addproducttocollection";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("collection_id", collection_id);
		comment.put("product_id", product_id);
		comment.put("from_user_id", from_user_id);
		comment.put("from_collection_id", from_collection_id);
		comment.put("product_image", product_image);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void renameCollection(String user_id,String collection_id, String collection_name) {
		this.METHOD_NAME = WebConstants.HOST_FILE+ "editcollection";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("collection_id", collection_id);
		comment.put("collection_name", collection_name);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void getCollectionList(String user_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getusercollection";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void deleteProductFromCollection(String user_id,String collection_id, String product_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE+ "deleteproductfromcollection";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("collection_id", collection_id);
		comment.put("product_id", product_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void deleteCollection(String collection_id,String user_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "deletecollection";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("collection_id", collection_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	@Override
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
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
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			CollectionApiRes collectionApiRes = JsonUtils.fromJson(response,CollectionApiRes.class);
			if (collectionApiRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = collectionApiRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

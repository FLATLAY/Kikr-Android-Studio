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
import com.flatlaylib.service.res.AddressRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class AddressApi extends AbsService {

	private String requestValue;
	private String requestType;

	public AddressApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
	public void getAddressList(String user_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getaddresslist";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	
	public void addAddress(String user_id, String title, String firstname, String lastname, String state, String tel, String street_address, String city_town, String zip_code, String country, String apartment) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "addaddress";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("title", title);
		comment.put("firstname", firstname);
		comment.put("lastname", lastname);
		comment.put("state", state);
		comment.put("tel", tel);
		comment.put("street_address", street_address);
		comment.put("city_town", city_town);
		comment.put("zip_code", zip_code);
		comment.put("country", country);
		comment.put("apartment", apartment);
		Map[] maps=new Map[]{comment};
		Gson gson=new Gson();
		requestValue=gson.toJson(maps);
	}
	
	public void updateAddress(String user_id,String address_id, String title, String firstname, String lastname, String state, String tel, String street_address, String city_town, String zip_code, String country, String apartment) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "updateaddress";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("address_id", address_id);
		comment.put("title", title);
		comment.put("firstname", firstname);
		comment.put("lastname", lastname);
		comment.put("state", state);
		comment.put("tel", tel);
		comment.put("street_address", street_address);
		comment.put("city_town", city_town);
		comment.put("zip_code", zip_code);
		comment.put("country", country);
		comment.put("apartment", apartment);
		Map[] maps=new Map[]{comment};
		Gson gson=new Gson();
		requestValue=gson.toJson(maps);
	}
	
	public void removeAddress(String user_id,String address_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "removeaddress";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("address_id", address_id);
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
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
	}

	@Override
	public String getMethod() {
		return requestType;
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
		Syso.info("In RegisterUserApi processResponse53>>" + response);
		try {
			AddressRes userResponse = JsonUtils.fromJson(response,AddressRes.class);
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

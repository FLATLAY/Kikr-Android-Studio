package com.kikrlib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.WalletPinRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class WalletPinApi extends AbsService {

	private String requestValue;
	private String requestMethod;

	public WalletPinApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void createKikrWalletPin(String user_id, String kikrpin) {

		this.METHOD_NAME = WebConstants.HOST_FILE + "createkikrwalletpin/";
		requestMethod = WebConstants.HTTP_METHOD_POST;

		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("kikrpin", kikrpin);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void authenticateKikrWalletPin(String user_id, String kikrpin) {

		this.METHOD_NAME = WebConstants.HOST_FILE + "kikrpinauthentication/"
				+ kikrpin + "/" + user_id;
		requestMethod = WebConstants.HTTP_METHOD_GET;
	}

	public void forgotKikrWalletPin(String user_id) {

		this.METHOD_NAME = WebConstants.HOST_FILE + "forgotkikrpin";
		requestMethod = WebConstants.HTTP_METHOD_POST;

		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void checkKikrWalletPin(String user_id) {

		this.METHOD_NAME = WebConstants.HOST_FILE + "kikrpinexist";
		requestMethod = WebConstants.HTTP_METHOD_POST;

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
		return requestMethod;
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
			WalletPinRes commonRes = JsonUtils.fromJson(response, WalletPinRes.class);
			if (commonRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = commonRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		}

	}

}

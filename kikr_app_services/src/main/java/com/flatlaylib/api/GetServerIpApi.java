package com.flatlaylib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.ServerIpRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class GetServerIpApi extends AbsService {

	private String requestValue;
	private String requestType;

	public GetServerIpApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	
	public void getServerIp(String user_ip) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getserverip";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_ip", user_ip);
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
	public MultipartEntityBuilder getMultipartRequest() {
		return null;
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
			ServerIpRes serverIpRes = JsonUtils.fromJson(response,ServerIpRes.class);
			if (serverIpRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = serverIpRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

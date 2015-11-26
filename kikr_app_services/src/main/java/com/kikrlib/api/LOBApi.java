package com.kikrlib.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.JsonParseException;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.StringUtils;
import com.kikrlib.utils.Syso;

public class LOBApi extends AbsService {

	private String requestValue;
	private String requestType;
	private List<NameValuePair> list = new ArrayList<NameValuePair>();

	public LOBApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void validateAddress(String address_line1,String address_line2,String address_state,String address_city, String address_zip,String address_country) {
		this.METHOD_NAME = "https://api.lob.com/v1/verify";
		requestType = WebConstants.HTTP_METHOD_POST;
		list.add(new BasicNameValuePair("address_line1", address_line1));
		list.add(new BasicNameValuePair("address_line2", address_line2));
		list.add(new BasicNameValuePair("address_state", address_state));
		list.add(new BasicNameValuePair("address_city", address_city));
		list.add(new BasicNameValuePair("address_zip", address_zip));
		list.add(new BasicNameValuePair("address_country", address_country));
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
	public String getHost() {
		return "";
	}

	@Override
	public List<NameValuePair> getNameValueRequest() {
		return list;
	}
	
	@Override
	public String getHeader() {
		return StringUtils.getBase64EncodedString();
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			isValidResponse = true;
			serviceResponse = response;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

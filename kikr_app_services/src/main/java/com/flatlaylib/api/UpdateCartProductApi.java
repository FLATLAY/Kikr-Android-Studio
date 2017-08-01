package com.flatlaylib.api;

import com.google.gson.JsonParseException;
import com.flatlaylib.bean.ProductRequiredOption;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.CommonRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import java.util.List;

public class UpdateCartProductApi extends AbsService {

	private String requestValue;
	private String requestType;

	public UpdateCartProductApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void updateProductValues(String user_id,String productcart_id,List<ProductRequiredOption> list) {
		this.METHOD_NAME = "updateproductvariables";
		requestType = WebConstants.HTTP_METHOD_POST;
		String value="";
		for (int i = 0; i < list.size(); i++) {
			value +="{\"name\":\""
					+ list.get(i).getName() + "\","
					+ "\"value\":\""
					+ list.get(i).getValue() + "\"}";
			if (i != list.size() - 1)
				value += ",";
		}
		String finalValue = "[{\"user_id\":\""+ user_id +"\",\"productcart_id\":\""+ productcart_id +"\",\"selected_values\":[" +value+"]}]";
		requestValue = finalValue;
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
//			JSONArray array = new JSONArray(requestValue);
			return requestValue;
		} catch (Exception e) {
			e.printStackTrace();
			JSONArray array = new JSONArray();
			return array.toString();
		}
	}


	@Override
	public String getHeader() {
		String oth="Bearer " + UserPreference.getInstance().getAccessToken();
		return oth;
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse22>>" + response);
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

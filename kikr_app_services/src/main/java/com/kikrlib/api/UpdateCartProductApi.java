package com.kikrlib.api;

import com.google.gson.JsonParseException;
import com.kikrlib.bean.ProductRequiredOption;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.CommonRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

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
		Syso.info("In RegisterUserApi processResponse>>" + response);
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

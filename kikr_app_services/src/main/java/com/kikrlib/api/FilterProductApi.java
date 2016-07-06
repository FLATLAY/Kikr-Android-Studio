package com.kikrlib.api;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonParseException;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.ProductBasedOnBrandRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class FilterProductApi extends AbsService {

	private String requestValue;
	private String requestType;

	public FilterProductApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
//	{
//		  "user_id": "180",
//		  "type": "store",
//		  "name": "Lids",
//		  "page": "0",
//		  "filter": {
//		    "type": "onsale",
//		    "pricemin": "0",
//		    "pricemax": "4"
//		  }
//		}

	public void getKikrCredits(String user_id,String type,String name,String page,String filterType,String pricemin,String pricemax) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getproducts";
		requestType = WebConstants.HTTP_METHOD_POST;
		try{
			JSONObject object = new JSONObject();
			object.put("user_id", user_id);
			object.put("type", type);
			object.put("name", name);
			object.put("page", page);
			JSONObject object2 = new JSONObject();
			object2.put("type", filterType);
			object2.put("pricemin", pricemin);
			object2.put("pricemax", pricemax);
			object.put("filter", object2);
			requestValue = object.toString();
			Syso.info("Filter request>>>>>>>"+requestValue);
		}catch(Exception e){
			e.printStackTrace();
		}
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
			ProductBasedOnBrandRes brandRes = JsonUtils.fromJson(response,ProductBasedOnBrandRes.class);
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

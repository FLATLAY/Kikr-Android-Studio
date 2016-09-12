package com.kikrlib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.db.AppPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.BrandListRes;
import com.kikrlib.service.res.CardInfoRes;
import com.kikrlib.service.res.CartRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class CardInfoApi extends AbsService {

	private String requestValue;
	private String requestType;

	public CardInfoApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
	
	public void getCardList(String user_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getcardlist";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void addCard(String user_id,String card_number,String name_on_card,String expiration_date,String cvv, String cardtype) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "addcreditcard";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("card_number", card_number);
		comment.put("name_on_card", name_on_card);
		comment.put("expiration_date", expiration_date);
		comment.put("cvv", cvv);
		comment.put("cardtype", cardtype);
		Map[] maps=new Map[]{comment};
		Gson gson=new Gson();
		requestValue=gson.toJson(maps);
	}

	public void removeCard(String user_id,String card_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "removecreditcard";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("card_id", card_id);
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
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			CardInfoRes userResponse = JsonUtils.fromJson(response,CardInfoRes.class);
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

package com.kikrlib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.bean.ProductRequiredOption;
import com.kikrlib.db.AppPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.BrandListRes;
import com.kikrlib.service.res.CartRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class TwoTapRequestResApi extends AbsService {

	private String requestValue;
	private String requestType;

	public TwoTapRequestResApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}
//	cartApi.addToCart(UserPreference.getInstance().getUserID(), product.getId(), quantityTextView.getText().toString(),UserPreference.getInstance().getCartID(),fromUserId,fromCollection,selectedSize,selectedColor);
	public void updateTwoTapRequest(String user_id,String request,String response,String link) {
		this.METHOD_NAME = WebConstants.HOST_FILE+"twotaprequests";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("req", request);
		comment.put("res", response);
		comment.put("link", link);
		Gson gson=new Gson();
		requestValue=gson.toJson(comment);
		Syso.info("ukkkkkkkkkkkk updateTwoTapRequest>>>"+requestValue);
	}
	
	
	@Override
	public String getActionName() {
		return METHOD_NAME;
	}

	@Override
	public List<NameValuePair> getNameValueRequest() {
		return null;
	}
	
//	@Override
//	public String getHost() {
//		// TODO Auto-generated method stub
//		return "http://api.kikr.io/";
//	}

	@Override
	public String getMethod() {
		return requestType;
	}

	@Override
	public String getJsonRequest() {
			return requestValue;
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("ukkkkkkkkkkkk  In updateTwoTapRequest processResponse>>" + response);
	}
}

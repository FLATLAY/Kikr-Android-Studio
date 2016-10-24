package com.flatlaylib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.google.gson.Gson;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.Syso;

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
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("ukkkkkkkkkkkk  In updateTwoTapRequest processResponse>>" + response);
	}
}

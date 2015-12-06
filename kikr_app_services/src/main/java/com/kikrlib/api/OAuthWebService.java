package com.kikrlib.api;

import com.kikrlib.bean.BillingAddress;
import com.kikrlib.bean.Card;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.utils.Constants;
import com.kikrlib.utils.Syso;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class OAuthWebService extends AbsService {

	String actionUrl;
	String jsonRequest;
	public OAuthWebService(String actionUrl, ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
		this.actionUrl = actionUrl;
	}

	public void setRequest(Card card,BillingAddress billingAddress){
		if(card!=null&&billingAddress!=null) {
			jsonRequest = "{\"intent\":\"authorize\",\"payer\":{\"payment_method\":\"credit_card\",\"funding_instruments\":" +
					"[{\"credit_card\":{\"number\":\"" + card.getCard_number() + "\",\"type\":\"" + card.getCardtype() + "\"," +
					"\"expire_month\":" + card.getExpirationMonth() + ",\"expire_year\":" + card.getExpirationYear() + ",\"cvv2\":\"" + card.getCvv()
					+ "\",\"first_name\":\"" + billingAddress.getFirstName() + "\",\"last_name\":\"" + billingAddress.getLastName() + "\"," +
					"\"billing_address\":{\"line1\":\"" + billingAddress.getLine1() + "\",\"city\":\"" + billingAddress.getCity() + "\"," +
					"\"state\":\"" + billingAddress.getState() + "\",\"postal_code\":\"" + billingAddress.getPostal_code() + "\",\"country_code\":\"" + billingAddress.getCountry_code() + "\"}}}]}," +
					"\"transactions\":[{\"amount\":{\"total\":\"0.01\",\"currency\":\"USD\",\"details\":{\"subtotal\":\"0.01\",\"tax\":\"0.00\",\"shipping\":\"0.00\"}}}]}";
		}else{
			jsonRequest = "";
		}
	}

	public void setRequest1(){
		jsonRequest = "{\"intent\":\"authorize\",\"payer\":{\"payment_method\":\"credit_card\",\"funding_instruments\":[{\"credit_card\":{\"number\":\"4417119669820331\",\"type\":\"visa\",\"expire_month\":11,\"expire_year\":2018,\"cvv2\":\"874\",\"first_name\":\"Betsy\",\"last_name\":\"Buyer\",\"billing_address\":{\"line1\":\"111 First Street\",\"city\":\"Saratoga\",\"state\":\"CA\",\"postal_code\":\"95070\",\"country_code\":\"US\"}}}]},\"transactions\":[{\"amount\":{\"total\":\"0.01\",\"currency\":\"USD\",\"details\":{\"subtotal\":\"0.01\",\"tax\":\"0.00\",\"shipping\":\"0.00\"}}}]}";
	}
	@Override
	public String getActionName() {
			return this.METHOD_NAME = actionUrl;
	}

	@Override
	public String getHost() {
		return "";
	}

	@Override
	public String getMethod() {
		return Constants.WebConstants.HTTP_METHOD_POST;
	}

	@Override
	public List<NameValuePair> getNameValueRequestForOAuth() {
		List<NameValuePair> requestParams = new ArrayList<NameValuePair>();
		requestParams.add(new BasicNameValuePair("grant_type", "client_credentials"));
		Syso.info("========= requestParams : " + requestParams);
		return requestParams;
	}

	@Override
	public String getHeader() {
		String header = "Bearer "+ UserPreference.getInstance().getAccessToken();
		Syso.info("Header>>>> "+header);
		return header;
	}

	@Override
	public String getJsonRequest() {
		return jsonRequest;
	}

	@Override
	protected void processResponse(String response) {
		isValidResponse = true;
		try {
			serviceResponse = response;
			Syso.info("123456789============= OAuth Result : "+response);
			JSONObject jsonObj=new JSONObject(response);
			if(jsonObj.has("error"))
			{
				isValidResponse = false;
				serviceResponse=jsonObj.optString("error");
			}
		} catch (Exception e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}

	@Override
	public List<NameValuePair> getNameValueRequest() {
		return null;
	}

}

package com.kikrlib.api;

import java.util.List;

import org.apache.http.NameValuePair;

import com.google.gson.JsonParseException;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.ProductFeedRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class ProductFeedApi extends AbsService{
	
	
	public ProductFeedApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getProductList(String user_id, String pageno) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getfeed" + "/"+ user_id + "/" + pageno;
	}

	public void getBrandList(String user_id, String pageno) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getbrands" + "/"+ user_id + "/" + pageno;
	}

	public void getProductsBasedOnBrandList(String user_id, String pageno,String brand_name) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getproductsbrand" + "/"+ user_id + "/" + brand_name + "/" + pageno;
	}

	@Override
	public String getActionName() {
		return METHOD_NAME;
	}

	@Override
	public String getMethod() {
		return WebConstants.HTTP_METHOD_GET;
	}
	
	
	@Override
	public List<NameValuePair> getNameValueRequest() {
		return null;
	}
	
	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>"+response);
		try {
			ProductFeedRes productListResponse = JsonUtils.fromJson(response, ProductFeedRes.class);
			if(productListResponse.getCode().equals(WebConstants.SUCCESS_CODE)){
				isValidResponse = true;
			}
			serviceResponse=productListResponse;
		} 
		catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		}catch (NullPointerException e){
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

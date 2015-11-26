package com.kikrlib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.ProductBasedOnBrandRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class ProductBasedOnBrandApi extends AbsService{
	private String requestValue;
	
	public ProductBasedOnBrandApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getProductsBasedOnBrandList(String user_id, String pageno,String brand_name) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getproductsbybrand";
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("brandname", brand_name);
		comment.put("pagenum", pageno);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void getProductsBasedOnStore(String user_id, String page,String storename,String storeid) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getproductsbystore";
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("storename", storename);
		comment.put("pagenum", page);
		comment.put("storeid", storeid);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void getProductsBasedOnUser(String user_id, String page,String viewer_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getproductsbyuser";
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("viewer_id", viewer_id);
		comment.put("pagenum", page);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void getProductsBasedOnCollectionList(String user_id, String pageno,String collection_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getproductsbycollection";
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("collection_id", collection_id);
		comment.put("pagenum", pageno);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void getProductsBasedOnFilter(String user_id, String type, String name, String pageno, String filtertype, String pricemin, String pricemax, String filterId) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getproducts";
		Map<String, Object> comment = new HashMap<String, Object>();
		comment.put("user_id", user_id);
		comment.put("type", type);
		comment.put("name", name);
		comment.put("pagenum", pageno);
		comment.put("id", filterId);

		Map<String, String> filterObj = new HashMap<String, String>();
		filterObj.put("type", filtertype);
		filterObj.put("pricemin", pricemin);
		filterObj.put("pricemax", pricemax);
		comment.put("filter", filterObj);
		
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
		Syso.info("uuuuuuuuuuuuuu getProductsBasedOnFilter Request>>>"+requestValue);
	}

	@Override
	public String getActionName() {
		return METHOD_NAME;
	}

	@Override
	public String getMethod() {
		return WebConstants.HTTP_METHOD_POST;
	}
	
	@Override
	public List<NameValuePair> getNameValueRequest() {
		return null;
	}
	
	@Override
	public String getJsonRequest() {
		try {
		//	JSONArray array = new JSONArray(requestValue);
		//	return array.toString();
			return requestValue;
		} catch (Exception e) {
			e.printStackTrace();
			JSONArray array = new JSONArray();
			return array.toString();
		}
	}
	
	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>"+response);
		try {
			ProductBasedOnBrandRes productBasedOnBrandRes = JsonUtils.fromJson(response, ProductBasedOnBrandRes.class);
			if(productBasedOnBrandRes.getCode().equals(WebConstants.SUCCESS_CODE)){
				isValidResponse = true;
			}
			serviceResponse=productBasedOnBrandRes;
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

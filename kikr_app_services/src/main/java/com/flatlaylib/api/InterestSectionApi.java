package com.flatlaylib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.InterestSectionRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

public class InterestSectionApi extends AbsService {

	private String requestValue;
	private String requestType;

	public InterestSectionApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getStoreList(String pagenum) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getstores";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id",UserPreference.getInstance().getUserID());
		comment.put("pagenum",pagenum);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void getStoreListFollowed(String user_id, String pagenum, String viewer_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getstores";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id",user_id);
		comment.put("pagenum",pagenum);
		comment.put("onlyfollowed","yes");
		comment.put("viewer_id",viewer_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void followStore(String userid,String store_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "adduserstore";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id",userid);
		comment.put("store_id", store_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void unFollowStore(String userid,String store_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "deleteuserstore";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id",userid);
		comment.put("store_id", store_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void searchUser(String userid,String search_text,String pagenum) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "searchuser";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", userid);
		comment.put("search_type", "username");
		comment.put("search_text", search_text);
		comment.put("pagenum", pagenum);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void searchBrand(String userid,String search_text,String pagenum) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "searchbrand";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", userid);
		comment.put("search_text", search_text);
		comment.put("pagenum", pagenum);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void searchStore(String userid,String search_text,String pagenum) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "searchstore";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", userid);
		comment.put("search_text", search_text);
		comment.put("pagenum", pagenum);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void searchCategory(String userid,String search_text,String pagenum) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "searchcategory";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", userid);
		comment.put("search_text", search_text);
		comment.put("pagenum", pagenum);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void getCategoryList(String pagenum) {
		this.METHOD_NAME=WebConstants.HOST_FILE+"getcategories";
		requestType=WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", UserPreference.getInstance().getUserID());
		comment.put("pagenum", pagenum);
		Map[] maps=new Map[]{comment};
		Gson gson=new Gson();
		requestValue=gson.toJson(maps);
	}

	public void getBrandList(String pagenum) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getbrands";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", UserPreference.getInstance().getUserID());
		comment.put("pagenum", pagenum);
		Map[] maps=new Map[]{comment};
		Gson gson=new Gson();
		requestValue=gson.toJson(maps);
	}
	
	public void getBrandListFollowed(String user_id, String pagenum, String viewer_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getbrands";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("pagenum", pagenum);
		comment.put("onlyfollowed","yes");
		comment.put("viewer_id",viewer_id);
		Map[] maps=new Map[]{comment};
		Gson gson=new Gson();
		requestValue=gson.toJson(maps);
	}
	
	public void getAllKikrUserList(String userid,String page_no,String gender) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getuserlist";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", userid);
		comment.put("pagenum", page_no);
		comment.put("gender", gender);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
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
		Syso.info("In RegisterUserApi processResponse1>>" + response);
		try {
			InterestSectionRes interestSectionRes = JsonUtils.fromJson(response,InterestSectionRes.class);
			if (interestSectionRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = interestSectionRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

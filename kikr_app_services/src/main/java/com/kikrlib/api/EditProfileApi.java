package com.kikrlib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.ActivityRes;
import com.kikrlib.service.res.EditProfileRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;
import com.kikrlib.utils.StringUtils;

public class EditProfileApi extends AbsService {

	private String requestValue;
	private String requestType;
	private MultipartEntityBuilder builder;

	public EditProfileApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void addUsername(String user_id,String username) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "addusername";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("username", username);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}

	public void uploadImage(String user_id,byte[] profile_pic) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "uploaduserimageprofile";
		requestType = WebConstants.HTTP_METHOD_POST;
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody("user_id",user_id);
		if (profile_pic != null) {
			builder.addBinaryBody("profile_pic", profile_pic, ContentType.create("image/png"),StringUtils.getRandomImageName());
		}
		this.builder = builder;
	}

	public void updateProfileImage(String user_id, String username,String profile_pic) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "updateprofileprofilepic";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("username", username);
		comment.put("profile_pic", profile_pic);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void uploadBgImage(String user_id,byte[] background_pic) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "uploadbackgroundimage";
		requestType = WebConstants.HTTP_METHOD_POST;
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody("user_id",user_id);
		if (background_pic != null) {
			builder.addBinaryBody("background_pic", background_pic, ContentType.create("image/png"),StringUtils.getRandomImageName());
		}
		this.builder = builder;
	}
	
	public void updateBgImageUrl(String user_id,String background_pic) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "updatebackgroundimage";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("background_pic", background_pic);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(maps);
	}
	
	public void getBgImageUrlList(String user_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getimagelist";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
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
	public MultipartEntityBuilder getMultipartRequest() {
		return builder;
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
			EditProfileRes activityRes = JsonUtils.fromJson(response,EditProfileRes.class);
			if (activityRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = activityRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

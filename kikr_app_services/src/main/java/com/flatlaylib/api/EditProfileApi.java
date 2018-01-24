package com.flatlaylib.api;

import android.util.Log;

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
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.EditProfileRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;
import com.flatlaylib.utils.StringUtils;

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

	public void updateUserSocial(String user_id, String fb, String ins, String twi, String pin, String tube){
		Log.e("1122334", UserPreference.getInstance().getEmail());
		this.METHOD_NAME = WebConstants.HOST_FILE + "updateuser";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("instagram", ins);
		comment.put("pinterest", pin);
		comment.put("facebook", fb);
		comment.put("twitter", twi);
		comment.put("youtube", tube);
		//Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
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

//	public void getProfileByUserName(String username){
//		this.METHOD_NAME = WebConstants.HOST_FILE + "0/"+"username";
//		requestType = WebConstants.HTTP_METHOD_POST;
//		Map<String, String> comment = new HashMap<String, String>();
//		comment.put("username", username);
//		Map[] maps = new Map[] { comment };
//		Gson gson = new Gson();
//		requestValue = gson.toJson(maps);
//
//	}
//
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
			//JSONArray array = new JSONArray(requestValue);
			//return array.toString();
			return requestValue;
		} catch (Exception e) {
			e.printStackTrace();
			JSONArray array = new JSONArray();
			return array.toString();
		}
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse44>>" + response);
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

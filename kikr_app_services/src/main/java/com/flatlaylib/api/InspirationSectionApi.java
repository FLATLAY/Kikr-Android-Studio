package com.flatlaylib.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.flatlaylib.bean.TaggedItem;
import com.flatlaylib.bean.TaggedProducts;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;

import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class InspirationSectionApi extends AbsService {

	private String requestValue;
	private String requestType;
	private MultipartEntityBuilder builder;

	public InspirationSectionApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void uploadImage(String user_id,byte[] inspiration_pic,String is_image,String description,TaggedItem taggedItem,TaggedProducts taggedProducts,String image_width,String image_height, String imageUrl) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "uploadinspirationpic";
		requestType = WebConstants.HTTP_METHOD_POST;
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody("description",description);
		builder.addTextBody("user_id",user_id);
		builder.addTextBody("item_type",taggedItem.getSelectedItemType());
		builder.addTextBody("item_name",taggedItem.getSelectedItemName());
		builder.addTextBody("item_id",taggedItem.getSelectedItem());
		builder.addTextBody("item_xy","0,0");
		builder.addTextBody("product_id",taggedItem.getSelectedItem());
		builder.addTextBody("product_xy","");
		builder.addTextBody("product_name",taggedItem.getSelectedItemName());
		builder.addTextBody("is_image",is_image);
		builder.addTextBody("image_width",image_width);
		builder.addTextBody("image_height",image_height);
		Log.w("InspirationSectionApi", "ALL DETAILS:"+image_height+":"+image_width);
		Syso.info("wwwwwwwwwww >>>>"+taggedItem.getSelectedItemType());
		Syso.info("wwwwwwwwwww >>>>"+taggedItem.getSelectedItemName());
		Syso.info("wwwwwwwwwww >>>>"+taggedItem.getSelectedItem());
		Syso.info("wwwwwwwwwww >>>>"+taggedItem.getSelectedItemXY());
		Syso.info("wwwwwwwwwww >>>>"+taggedProducts.getSelectedProducts());
		Syso.info("wwwwwwwwwww >>>>"+taggedProducts.getSelectedProductsId());
		Syso.info("wwwwwwwwwww >>>>"+taggedProducts.getSelectedProductsXY());
		Syso.info("wwwwwwwwwww >>>>"+is_image);
		
		if (inspiration_pic != null) {
			builder.addBinaryBody("inspiration_pic", inspiration_pic, ContentType.create("image/png"),StringUtils.getRandomImageName());
		}else if(imageUrl!=null){
			builder.addTextBody("image_url",imageUrl);
		}
		this.builder = builder;
	}

	public void getInspirationDetail(String user_id,String id, String type) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getcommentsandlikedetail";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("id", id);
		comment.put("type", type);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
	}
	
	public void getProductsByInspiration(String user_id, String inspiration_id, String pagenum) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getproductsbyinspiration";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("inspiration_id", inspiration_id);
		comment.put("pagenum", pagenum);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
	}
	
	public void postComment(String user_id, String id, String type,String comment) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "postcomment";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comments = new HashMap<String, String>();
		comments.put("user_id", user_id);
		comments.put("id", id);
		comments.put("type", type);
		comments.put("comment", comment);
		Map[] maps = new Map[] { comments };
		Gson gson = new Gson();
		requestValue = gson.toJson(comments);
	}
	
	public void postLike(String user_id, String id,String type) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "postlike";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("type", type);
		comment.put("id", id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
	}
	
	public void removeLike(String user_id,String like_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "removelike";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("like_id", like_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
	}

	public void removeComment(String user_id,String comment_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "removecomment";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("comment_id", comment_id);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
	}

	@Override
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
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
	public MultipartEntityBuilder getMultipartRequest() {
		return builder;
	}

	@Override
	public List<NameValuePair> getNameValueRequest() {

		return null;
	}

	@Override
	public String getJsonRequest() {
		try {
//			JSONArray array = new JSONArray(requestValue);
			return requestValue;
		} catch (Exception e) {
			e.printStackTrace();
			JSONArray array = new JSONArray();
			return array.toString();
		}
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse41>>" + response);
		try {
			InspirationRes inspirationRes = JsonUtils.fromJson(response,InspirationRes.class);
			if (inspirationRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = inspirationRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

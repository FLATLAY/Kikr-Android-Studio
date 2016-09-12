package com.kikrlib.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.InspirationFeedRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InspirationFeedApi extends AbsService {

	private String requestValue;
	private String requestType;

	public InspirationFeedApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getInspirationFeed(String user_id,boolean viewAll,String pagenum,String viewer_id) {
		this.METHOD_NAME = WebConstants.HOST_FILE + "getinspirationwithproducts";
		requestType = WebConstants.HTTP_METHOD_POST;
		Map<String, String> comment = new HashMap<String, String>();
		comment.put("user_id", user_id);
		comment.put("viewer_id", viewer_id);
		comment.put("view_all", viewAll?"yes":"no");
		comment.put("pagenum", pagenum);
		Map[] maps = new Map[] { comment };
		Gson gson = new Gson();
		requestValue = gson.toJson(comment);
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
	public String getHeader() {
		return "Bearer " + UserPreference.getInstance().getAccessToken();
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			InspirationFeedRes inspirationFeedRes = JsonUtils.fromJson(response,InspirationFeedRes.class);
			if (inspirationFeedRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
				isValidResponse = true;
			}
			serviceResponse = inspirationFeedRes;
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}
}

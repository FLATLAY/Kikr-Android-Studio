package com.kikrlib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.bean.SearchStoreBrandUserRes;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.SearchRes;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

public class SearchAllApi extends AbsService {

    private String requestValue;
    private String requestType;

    public SearchAllApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void searchStoreBrandPeople(String user_id, String search_text, String pagenum) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "searchstorebranduser";
        requestType = WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("search_text", search_text.trim());
        comment.put("pagenum", pagenum);
        // Map[] maps=new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(comment);
    }


    @Override
    public String getActionName() {
        return METHOD_NAME;
    }

    @Override
    public List<NameValuePair> getNameValueRequest() {
        return null;
    }

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
        Syso.info("In RegisterUserApi processResponse>>" + response);
        try {
            SearchStoreBrandUserRes userResponse = JsonUtils.fromJson(response, SearchStoreBrandUserRes.class);
            if (userResponse.getCode().equals(WebConstants.SUCCESS_CODE)) {
                isValidResponse = true;
            }
            serviceResponse = userResponse;
        } catch (JsonParseException e) {
            Syso.error(e);
            isValidResponse = false;
        }
    }
}


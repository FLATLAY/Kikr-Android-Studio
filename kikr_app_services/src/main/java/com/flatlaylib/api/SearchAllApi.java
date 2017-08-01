package com.flatlaylib.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.flatlaylib.bean.SearchStoreBrandUserRes;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

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
        Syso.info("In RegisterUserApi processResponse20>>" + response);
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


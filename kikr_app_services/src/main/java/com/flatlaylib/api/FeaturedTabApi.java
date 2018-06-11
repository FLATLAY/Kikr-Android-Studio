package com.flatlaylib.api;

import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.FeaturedTabApiRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeaturedTabApi extends AbsService {

    private String requestValue;
    private String requestType;

    public FeaturedTabApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void getFeaturedTabData(String user_id, String pagenum) {
        this.METHOD_NAME = WebConstants.HOST_FILE + "getfeaturedtabdata";
        requestType = WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("pagenum", pagenum);
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
    public String getHeader() {
        return "Bearer " + UserPreference.getInstance().getAccessToken();
    }

    @Override
    public List<NameValuePair> getNameValueRequest() {
        return null;
    }

    @Override
    public String getJsonRequest() {
        try {
            return requestValue;
        } catch (Exception e) {
            e.printStackTrace();
            JSONArray array = new JSONArray();
            return array.toString();
        }
    }

    @Override
    protected void processResponse(String response) {
        Syso.info("In RegisterUserApi processResponse16>>" + response);
        try {
            response = response.replace("\"longproductdesc\":[]", "\"longproductdesc\":\"null\"");
            response = response.replace("\"shortproductdesc\":[]", "\"shortproductdesc\":\"null\"");

            FeaturedTabApiRes featuredTabApiRes = JsonUtils.fromJson(response, FeaturedTabApiRes.class);

            if (featuredTabApiRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
                isValidResponse = true;
            }
            serviceResponse = featuredTabApiRes;
        } catch (JsonParseException e) {
            Syso.error(e);
            isValidResponse = false;
        } catch (NullPointerException e) {
            Syso.error(e);
            isValidResponse = false;
        }

    }
}

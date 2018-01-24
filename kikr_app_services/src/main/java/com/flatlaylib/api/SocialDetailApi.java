package com.flatlaylib.api;

/**
 * Created by RachelDi on 1/18/18.
 */
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.flatlaylib.service.res.SocialDetailRes;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;
public class SocialDetailApi extends AbsService {
    private String requestValue;
    private String requestType;

    public SocialDetailApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }


    public void getProfileByUserName(String username){
        this.METHOD_NAME = WebConstants.HOST_FILE + "getprofilebyusername/0/" + username;
        requestType = WebConstants.HTTP_METHOD_GET;
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
        Syso.info("In RegisterUserApi processResponse17>>" + response);
        try {
            Log.w("MyProfileApi","processResponse 1");
            SocialDetailRes mysocialRes = JsonUtils.fromJson(response,SocialDetailRes.class);
            if (mysocialRes.getCode().equals(WebConstants.SUCCESS_CODE)) {
                Log.w("MyProfileApi","processResponse 4");
                isValidResponse = true;
            }
            serviceResponse = mysocialRes;
        } catch (JsonParseException e) {
            Log.w("MyProfileApi","processResponse 2");
            Syso.error(e);
            isValidResponse = false;
        } catch (NullPointerException e) {
            Log.w("MyProfileApi","processResponse 3");
            Syso.error(e);
            isValidResponse = false;
        }
    }
}


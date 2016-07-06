package com.kikrlib.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.res.RegisterUserResponse;
import com.kikrlib.utils.Constants;
import com.kikrlib.utils.Constants.WebConstants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginUserApi extends AbsService {

    private String requestValue;

    public LoginUserApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void loginViaEmail(String email, String password, String phone_model, String device_token, String device_type, String device_id) {

        this.METHOD_NAME = WebConstants.HOST_FILE + "loginviaemail";
        this.serviceName = "login";
        Constants.email = email;
        Constants.pwd = password;


        Map<String, String> comment = new HashMap<String, String>();
        comment.put("email", email);
        comment.put("password", password);
        comment.put("phone_model", phone_model);
        comment.put("device_token", device_token);
        comment.put("device_type", device_type);
        comment.put("device_id", device_id);

        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
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
            RegisterUserResponse userResponse = JsonUtils.fromJson(response, RegisterUserResponse.class);
            if (userResponse.getCode().equals(WebConstants.SUCCESS_CODE)) {
                isValidResponse = true;
                String userId = userResponse.getId();
                String token = userResponse.gettoken();
                //String mAccesstoken=userResponse.getAccessToken();
                UserPreference.getInstance().setUserID(userId);
                UserPreference.getInstance().setAccessToken(token);
            }
            serviceResponse = userResponse;
        } catch (JsonParseException e) {
            Syso.error(e);
            isValidResponse = false;
        }

    }


}

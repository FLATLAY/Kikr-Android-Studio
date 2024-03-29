package com.flatlaylib.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterUserApi extends AbsService {

    private String requestValue;
    private String requestType;

    public RegisterUserApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void registerViaEmail(String email, String password, String gender, String phone_model, String device_token, String current_screen, String device_type, String device_id) {

        this.METHOD_NAME = WebConstants.HOST_FILE + "registerviaemail";
        requestType = WebConstants.HTTP_METHOD_POST;

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("email", email);
        comment.put("password", password);
        comment.put("phone_model", phone_model);
        comment.put("device_token", device_token);
        comment.put("gender", gender);
        comment.put("current_screen", current_screen);
        comment.put("device_type", device_type);
        comment.put("device_id", device_id);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void registerViaNumber(String user_id, String phone_no) {

        this.METHOD_NAME = WebConstants.HOST_FILE + "registerviaphonenum";
        requestType = WebConstants.HTTP_METHOD_POST;

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("phone_num", phone_no);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void registerViaSocial(String social, String id, String gender, String phone_model, String device_token, String current_screen, String device_type, String device_id) {
        Log.w("RegisterUserApi","registerViaSocial()");
        this.METHOD_NAME = WebConstants.HOST_FILE + "registerviasocial";
        requestType = WebConstants.HTTP_METHOD_POST;

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("social", social);
        comment.put("id", id);
        comment.put("gender", gender);
        comment.put("phone_model", phone_model);
        comment.put("device_token", device_token);
        comment.put("current_screen", current_screen);
        comment.put("device_type", device_type);
        comment.put("device_id", device_id);


        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void registerViaFbSocial(String email,String social, String id, String gender, String phone_model, String device_token, String current_screen, String device_type, String device_id) {
        Log.w("RegisterUserApi","registerViaFbSocial()");
        this.METHOD_NAME = WebConstants.HOST_FILE + "registerviasocial";
        requestType = WebConstants.HTTP_METHOD_POST;

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("social", social);
        comment.put("id", id);
        comment.put("gender", gender);
        comment.put("phone_model", phone_model);
        comment.put("device_token", device_token);
        comment.put("current_screen", current_screen);
        comment.put("device_type", device_type);
        comment.put("email", email);
        comment.put("device_id", device_id);
        comment.put("social_token", WebConstants.SOICIAL_TOKEN);

        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void updateEmail(String user_id, String email) {

        this.METHOD_NAME = WebConstants.HOST_FILE + "updateemail/";
        requestType = WebConstants.HTTP_METHOD_POST;

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", user_id);
        comment.put("email", email);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void forgotPassword(String email) {

        this.METHOD_NAME = WebConstants.HOST_FILE + "forgotpassword";
        requestType = WebConstants.HTTP_METHOD_POST;

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("email", email);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void resetPassword(String email, String resetpin, String password) {

        this.METHOD_NAME = WebConstants.HOST_FILE + "resetpassword";
        requestType = WebConstants.HTTP_METHOD_POST;

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("email", email);
        comment.put("reset_code", resetpin);
        comment.put("password", password);
        Map[] maps = new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void registerGeustUser(String device_token, String current_screen, String device_id) {
//		[{"device_token":"4huy6u885gghy678945ffgh896jhkjn345oh8e36l","current_screen":"UserNameScreen"}]
        this.METHOD_NAME = WebConstants.HOST_FILE + "registerguestuser";
        requestType = WebConstants.HTTP_METHOD_POST;

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("device_token", device_token);
        comment.put("current_screen", current_screen);
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
        return requestType;
    }

    @Override
    public List<NameValuePair> getNameValueRequest() {

        return null;
    }
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
        Syso.info("In RegisterUserApi processResponse6>>" + response);
        try {
            RegisterUserResponse userResponse = JsonUtils.fromJson(response, RegisterUserResponse.class);
            if (userResponse.getCode().equals(WebConstants.SUCCESS_CODE)) {
                Log.w("RegisterUserApi","processResponse 1");
                isValidResponse = true;
//                String userId = userResponse.getId();
//                String token = userResponse.gettoken();
//                //String mAccesstoken=userResponse.getAccessToken();
//                UserPreference.getInstance().setUserID(userId);
//                UserPreference.getInstance().setAccessToken(token);
            }
            serviceResponse = userResponse;
        } catch (JsonParseException e) {
            Log.w("RegisterUserApi","processResponse 2");
            Syso.error(e);
            isValidResponse = false;
        } catch (NullPointerException e) {
            Log.w("RegisterUserApi","processResponse 3");
            Syso.error(e);
            isValidResponse = false;
        }
    }
}

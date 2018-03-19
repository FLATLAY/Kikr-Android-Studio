package com.flatlaylib.api;

import android.util.Log;

import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.GeneralSearchRes;
import com.flatlaylib.service.res.InterestSectionRes;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RachelDi on 3/15/18.
 */

public class GeneralSearchApi extends AbsService {

    private String requestValue;
    private String requestType;

    public GeneralSearchApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void generalSearch(String search_text, int pagenum) {
        this.METHOD_NAME = Constants.WebConstants.HOST_FILE + "search";
        requestType = Constants.WebConstants.HTTP_METHOD_POST;
        Map<String, Object> comment = new HashMap<String, Object>();
        comment.put("pagenum",pagenum );
        comment.put("search_text", search_text);
        comment.put("user_id", UserPreference.getInstance().getUserID());
        Map[] maps = new Map[]{comment};
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
    public String getHeader() {
        return "Bearer " + UserPreference.getInstance().getAccessToken();
    }

    @Override
    public String getJsonRequest() {
        try {
//            JSONArray array = new JSONArray(requestValue);
//            return array.toString();

            return requestValue;
        } catch (JsonParseException e) {
            e.printStackTrace();
            JSONArray array = new JSONArray();
            return array.toString();
        }
    }

    @Override
    protected void processResponse(String response) {
        Syso.info("In RegisterUserApi processResponse1>>" + response);
        try {
            Log.e("RegisterUserApi", "??");

            GeneralSearchRes generalSearchRes = JsonUtils.fromJson(response, GeneralSearchRes.class);
            Log.e("RegisterUserApi", generalSearchRes.getCode());
            Log.e("RegisterUserApi", Constants.WebConstants.SUCCESS_CODE);

            if (generalSearchRes.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {
                Log.e("RegisterUserApi", "!!");
                isValidResponse = true;
            }
            serviceResponse = generalSearchRes;
        } catch (JsonParseException e) {
            Syso.error(e);
            Log.e("RegisterUserApi", "@@");

            isValidResponse = false;
        } catch (NullPointerException e) {
            Log.e("RegisterUserApi", "++");
            Syso.error(e);
            isValidResponse = false;
        }
    }
}
package com.flatlaylib.api;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.FeaturedTabApiRes;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.reflect.TypeToken;

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

//            JSONObject object = null;
//            JSONObject json = null;
//            JSONArray arr_products;
//            try {
//                json = new JSONObject(response);
//                JSONArray array = json.getJSONArray("data");
//                for (int i = 0; i < array.length(); i++) {
//                    object = array.getJSONObject(i);
//
//                    try {
//                        JSONArray arr_feed = object.getJSONArray("inspiration_feed");
//                        for (int j = 0; j < arr_feed.length(); j++) {
//                            object = arr_feed.getJSONObject(i);
//                            arr_products = object.getJSONArray("poducts");
//                            if (arr_products.length() == 0) {
//                                object.put("poducts",new String());
//                            }
//                        }
//
//                    } catch (Exception ex) {
//                        Syso.info(ex.getMessage());
//                        object.put("poducts",new JSONArray());
//                    }
//                }
//
//            } catch (JSONException e) {
//
//                e.printStackTrace();
//
//            }
//            if (json != null)
//                response = json.toString();

            System.out.println(response);

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

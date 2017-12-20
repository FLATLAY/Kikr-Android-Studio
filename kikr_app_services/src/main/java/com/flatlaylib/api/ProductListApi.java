package com.flatlaylib.api;

import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.res.ProductListRes;
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
 * Created by RachelDi on 12/8/17.
 */

public class ProductListApi extends AbsService {

    private String requestValue;
    private String requestType;

    public ProductListApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void getProductList(String pagenum) {
        this.METHOD_NAME = Constants.WebConstants.HOST_FILE + "getproducts";
        requestType = Constants.WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", UserPreference.getInstance().getUserID());
        comment.put("pagenum", pagenum);
        Map[] maps=new Map[]{comment};
        Gson gson=new Gson();
        requestValue=gson.toJson(maps);
    }

    public void addProducts(String product_id) {
        this.METHOD_NAME = Constants.WebConstants.HOST_FILE + "adduserproduct";
        requestType = Constants.WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", UserPreference.getInstance().getUserID());
        comment.put("product_id", product_id);
        Map[] maps = new Map[] { comment };
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
    }

    public void deleteProduct(String product_id) {
        this.METHOD_NAME = Constants.WebConstants.HOST_FILE + "deleteuserproduct";
        requestType = Constants.WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("user_id", UserPreference.getInstance().getUserID());
        comment.put("product_id", product_id);
        Map[] maps = new Map[] { comment };
        Gson gson = new Gson();
        requestValue = gson.toJson(maps);
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
        Syso.info("In RegisterUserApi processResponse52>>" + response);
        try {
            ProductListRes userResponse = JsonUtils.fromJson(response,ProductListRes.class);
            if (userResponse.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {
                isValidResponse = true;
            }
            serviceResponse = userResponse;
        } catch (JsonParseException e) {
            Syso.error(e);
            isValidResponse = false;
        }
    }
}
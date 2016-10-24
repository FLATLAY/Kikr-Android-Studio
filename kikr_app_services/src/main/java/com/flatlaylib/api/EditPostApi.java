package com.flatlaylib.api;

import com.google.gson.Gson;
import com.flatlaylib.bean.ServerResponse;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.Syso;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anshumaan on 5/9/2016.
 */
public class EditPostApi extends AbsService {

    private String requestValue;
    private String requestType;

    public EditPostApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void updateDescription(String inspiration_id, String user_id, String newDescription) {

        this.METHOD_NAME = Constants.WebConstants.HOST_FILE + "updateinspirationdescription";
        requestType = Constants.WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        //  comment.put("user_id", UserPreference.getInstance().getUserID());
        //   comment.put("inspiration_id", UserPreference.getInstance().getUserID());
        comment.put("user_id", user_id);
        comment.put("inspiration_id", inspiration_id);
        comment.put("description", newDescription);

        //Map[] maps=new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(comment);
    }

    public void removeGeneralTag(String inspiration_id, String user_id) {

        this.METHOD_NAME = Constants.WebConstants.HOST_FILE + "removegeneraltag";
        requestType = Constants.WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        //  comment.put("user_id", UserPreference.getInstance().getUserID());
        //   comment.put("inspiration_id", UserPreference.getInstance().getUserID());
        comment.put("user_id", user_id);
        comment.put("inspiration_id", inspiration_id);


        //Map[] maps=new Map[]{comment};
        Gson gson = new Gson();
        requestValue = gson.toJson(comment);
    }

    public void removeProductTag(String inspiration_id, String user_id) {

        this.METHOD_NAME = Constants.WebConstants.HOST_FILE + "removeproducttag";
        requestType = Constants.WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        //  comment.put("user_id", UserPreference.getInstance().getUserID());
        //   comment.put("inspiration_id", UserPreference.getInstance().getUserID());
        comment.put("user_id", user_id);
        comment.put("inspiration_id", inspiration_id);


        //Map[] maps=new Map[]{comment};
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

    public String getJsonRequest() {
//        try {
//            JSONArray array=new JSONArray(requestValue);
//            return array.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//            JSONArray array=new JSONArray();
//            return array.toString();
//        }
        return requestValue;
    }

    @Override
    protected void processResponse(String response) {
        Syso.info("In RegisterUserApi processResponse>>" + response);
        try {

            ServerResponse serverResponse= JsonUtils.fromJson(response,ServerResponse.class);

            if (serverResponse.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {
                isValidResponse = true;
            }
            serviceResponse = serverResponse;
        } catch (Exception e) {
            Syso.error(e);
            isValidResponse = false;
        }

    }
}

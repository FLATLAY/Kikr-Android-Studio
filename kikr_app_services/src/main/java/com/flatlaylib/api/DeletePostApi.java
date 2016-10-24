package com.flatlaylib.api;

import com.google.gson.Gson;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.Syso;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tycho on 4/12/2016.
 */
public class DeletePostApi extends AbsService {

    private String requestValue;
    private String requestType;

    public DeletePostApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void removePost(String inspiration_id  ,String user_id) {

        this.METHOD_NAME= WebConstants.HOST_FILE +"deleteinspirationpost";
        requestType= Constants.WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
      //  comment.put("user_id", UserPreference.getInstance().getUserID());
     //   comment.put("inspiration_id", UserPreference.getInstance().getUserID());
        comment.put("user_id", user_id);
        comment.put("inspiration_id", inspiration_id);

        //Map[] maps=new Map[]{comment};
        Gson gson=new Gson();
        requestValue=gson.toJson(comment);
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
            JSONObject jsonObject=new JSONObject(response);
            String coderesponse=jsonObject.getString("code");

            if (coderesponse.equals(WebConstants.SUCCESS_CODE)) {
                isValidResponse = true;
            }
            serviceResponse = coderesponse;
        } catch (Exception e) {
            Syso.error(e);
            isValidResponse = false;
        }

    }
}

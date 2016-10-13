package com.kikrlib.api;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.kikrlib.bean.FollowingKikrModel;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.AbsService;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.utils.Constants;
import com.kikrlib.utils.JsonUtils;
import com.kikrlib.utils.Syso;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tycho on 5/24/2016.
 */
public class MessageCenterApi extends AbsService {
    private String requestValue;
    private String requestType;

    public MessageCenterApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void followinginstagram(String pagenum  ,String user_id) {

        this.METHOD_NAME= Constants.WebConstants.HOST_FILE +"getmessages";
        requestType= Constants.WebConstants.HTTP_METHOD_POST;
        Map<String, String> comment = new HashMap<String, String>();
        //  comment.put("user_id", UserPreference.getInstance().getUserID());
        //   comment.put("inspiration_id", UserPreference.getInstance().getUserID());
        comment.put("user_id", user_id);
        comment.put("pagenum", pagenum);

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
            FollowingKikrModel inspirationFeedRes = JsonUtils.fromJson(response, FollowingKikrModel.class);
            if (inspirationFeedRes.getCode().equals(Constants.WebConstants.SUCCESS_CODE)) {
                isValidResponse = true;
            }
            serviceResponse = inspirationFeedRes;
        } catch (JsonParseException e) {
            Syso.error(e);
            isValidResponse = false;
        } catch (NullPointerException e) {
            Syso.error(e);
            isValidResponse = false;
        }

    }
}

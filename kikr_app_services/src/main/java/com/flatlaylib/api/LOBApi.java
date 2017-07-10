package com.flatlaylib.api;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.JsonParseException;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;

public class LOBApi extends AbsService {

    private String requestValue;
    private String requestType;
    private List<NameValuePair> list = new ArrayList<NameValuePair>();

    public LOBApi(ServiceCallback serviceCallback) {
        super();
        this.mServiceCallback = serviceCallback;
    }

    public void validateAddress(String address_line1, String address_line2, String address_state, String address_city, String address_zip, String address_country) {
        this.METHOD_NAME = "https://api.lob.com/v1/us_verifications";
        requestType = WebConstants.HTTP_METHOD_POST;

        String address = "";
        if (address_line2.length() > 0) {
            address = "#" + address_line2 + " " + address_line1;
        } else {
            address = address_line1;
        }

        list.add(new BasicNameValuePair("primary_line", address));
        //list.add(new BasicNameValuePair("primary_line", address_line2));
        list.add(new BasicNameValuePair("city", address_city));
        list.add(new BasicNameValuePair("state", address_state));
        list.add(new BasicNameValuePair("zipCode", address_zip));
        //list.add(new BasicNameValuePair("address_country", address_country));
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
    public String getHost() {
        return "";
    }

    @Override
    public List<NameValuePair> getNameValueRequest() {
        return list;
    }

    @Override
    public String getHeader() {
        return StringUtils.getBase64EncodedString();
    }

    @Override
    protected void processResponse(String response) {
        Log.w("LOBApi","processResponse"+response);
        try {
            isValidResponse = true;
            serviceResponse = response;
        } catch (JsonParseException e) {
            Syso.error(e);
            isValidResponse = false;
        } catch (NullPointerException e) {
            Syso.error(e);
            isValidResponse = false;
        }
    }
}

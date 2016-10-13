package com.kikrlib.bean;

/**
 * Created by anshumaan on 5/9/2016.
 */
public class ServerResponse{


    /**
     * code : 0001
     * message : The provided data doesn't exist
     */

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

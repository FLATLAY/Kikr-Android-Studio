package com.kikrlib.bean;

import java.util.List;

/**
 * Created by pushpendra on 5/30/2016.
 */
public class FollowingKikrModel {


    /**
     * code : 0000
     * data : [{"id":"1687","message":"Ali is following you now","type":"follow","sender_id":"2","dateadded":"2016-06-14 04:30:07","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"},{"id":"1680","message":"sdfasdfadf is following you now","type":"follow","sender_id":"1423","dateadded":"2016-06-13 04:13:03","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"},{"id":"1666","message":"You just got 25 Kikr Credits! ","type":"invite","sender_id":"0","dateadded":"2016-06-09 18:39:59","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"},{"id":"1629","message":"You just got 25 Kikr Credits! ","type":"invite","sender_id":"0","dateadded":"2016-05-25 07:57:08","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"},{"id":"1623","message":"You just got 25 Kikr Credits! ","type":"invite","sender_id":"0","dateadded":"2016-05-25 07:32:00","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"},{"id":"1622","message":"You just got 25 Kikr Credits! ","type":"invite","sender_id":"0","dateadded":"2016-05-25 07:21:36","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"},{"id":"1616","message":"You just got 25 Kikr Credits! ","type":"invite","sender_id":"0","dateadded":"2016-05-25 07:20:45","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"},{"id":"1130","message":"Anshumaan is following you now","type":"follow","sender_id":"646","dateadded":"2016-05-08 10:01:40","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"},{"id":"1128","message":"Anshumaan is following you now","type":"follow","sender_id":"646","dateadded":"2016-05-08 10:00:01","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"},{"id":"1127","message":"Anshumaan is following you now","type":"follow","sender_id":"646","dateadded":"2016-05-08 09:55:29","htmlcontent":"","readstatus":"0","img":"http://flat-lay.com/images/A_nobg.png","img_sender":"http://flat-lay.com/images/A_nobg.png","username_sender":"morteza"}]
     */

    private String code;
    /**
     * id : 1687
     * message : Ali is following you now
     * type : follow
     * sender_id : 2
     * dateadded : 2016-06-14 04:30:07
     * htmlcontent :
     * readstatus : 0
     * img : http://flat-lay.com/images/A_nobg.png
     * img_sender : http://flat-lay.com/images/A_nobg.png
     * username_sender : morteza
     */

    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String message;
        private String type;
        private String sender_id;
        private String dateadded;
        private String htmlcontent;
        private String readstatus;
        private String img;
        private String img_sender;
        private String username_sender;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSender_id() {
            return sender_id;
        }

        public void setSender_id(String sender_id) {
            this.sender_id = sender_id;
        }

        public String getDateadded() {
            return dateadded;
        }

        public void setDateadded(String dateadded) {
            this.dateadded = dateadded;
        }

        public String getHtmlcontent() {
            return htmlcontent;
        }

        public void setHtmlcontent(String htmlcontent) {
            this.htmlcontent = htmlcontent;
        }

        public String getReadstatus() {
            return readstatus;
        }

        public void setReadstatus(String readstatus) {
            this.readstatus = readstatus;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg_sender() {
            return img_sender;
        }

        public void setImg_sender(String img_sender) {
            this.img_sender = img_sender;
        }

        public String getUsername_sender() {
            return username_sender;
        }

        public void setUsername_sender(String username_sender) {
            this.username_sender = username_sender;
        }
    }
}



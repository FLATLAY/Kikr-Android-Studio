package com.flatlaylib.bean;

import java.util.List;

/**
 * Created by pushpendra on 5/30/2016.
 */
public class FollowingKikrModel {


    /**
     * code : 0000
     * data : [{"id":"2337","message":"anshumaansingh20002810 liked your inspiration post","type":"likeinsp","tbl_user_idsend":"1471","dateadded":"2016-08-24 16:04:03","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1471\",\"otherdata\":{\"inspiration_id\":\"1404\"}}"},{"id":"2336","message":"anshumaansingh20002810 liked your inspiration post","type":"likeinsp","tbl_user_idsend":"1471","dateadded":"2016-08-24 16:03:43","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1471\",\"otherdata\":{\"inspiration_id\":\"1405\"}}"},{"id":"2335","message":"dani liked your inspiration post","type":"likeinsp","tbl_user_idsend":"1470","dateadded":"2016-08-24 05:43:18","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1470\",\"otherdata\":{\"inspiration_id\":\"1405\"}}"},{"id":"2334","message":"dani liked your inspiration post","type":"likeinsp","tbl_user_idsend":"1470","dateadded":"2016-08-24 05:43:11","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1470\",\"otherdata\":{\"inspiration_id\":\"1404\"}}"},{"id":"2331","message":"dani is following you now","type":"follow","tbl_user_idsend":"1470","dateadded":"2016-08-24 05:36:50","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1470\"}"},{"id":"2328","message":"sandra commented on your inspiration post","type":"commentinsp","tbl_user_idsend":"1469","dateadded":"2016-08-24 05:32:29","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1469\",\"otherdata\":{\"inspiration_id\":\"1405\"}}"},{"id":"2327","message":"sandra liked your inspiration post","type":"likeinsp","tbl_user_idsend":"1469","dateadded":"2016-08-24 05:32:13","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1469\",\"otherdata\":{\"inspiration_id\":\"1405\"}}"},{"id":"2326","message":"sandra is following you now","type":"follow","tbl_user_idsend":"1469","dateadded":"2016-08-24 05:32:02","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1469\"}"},{"id":"2324","message":"sandra-ra is following you now","type":"follow","tbl_user_idsend":"1469","dateadded":"2016-08-24 05:22:00","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1469\"}"},{"id":"2323","message":"Ali liked your inspiration post","type":"likeinsp","tbl_user_idsend":"1467","dateadded":"2016-08-24 02:58:15","htmlcontent":"","tbl_user_idrec":"1467","readstatus":"0","img":"https://graph.facebook.com/10107821760146840/picture?type=large","extras":"{\"user_idrec\":\"1467\",\"user_idsend\":\"1467\",\"otherdata\":{\"inspiration_id\":\"1404\"}}"}]
     */

    private String code;
    /**
     * id : 2337
     * message : anshumaansingh20002810 liked your inspiration post
     * type : likeinsp
     * tbl_user_idsend : 1471
     * dateadded : 2016-08-24 16:04:03
     * htmlcontent :
     * tbl_user_idrec : 1467
     * readstatus : 0
     * img :
     * extras : {"user_idrec":"1467","user_idsend":"1471","otherdata":{"inspiration_id":"1404"}}
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
        private String tbl_user_idsend;
        private String dateadded;
        private String htmlcontent;
        private String tbl_user_idrec;
        private String readstatus;
        private String img;
        private String extras;

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

        public String getTbl_user_idsend() {
            return tbl_user_idsend;
        }

        public void setTbl_user_idsend(String tbl_user_idsend) {
            this.tbl_user_idsend = tbl_user_idsend;
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

        public String getTbl_user_idrec() {
            return tbl_user_idrec;
        }

        public void setTbl_user_idrec(String tbl_user_idrec) {
            this.tbl_user_idrec = tbl_user_idrec;
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

        public String getExtras() {
            return extras;
        }

        public void setExtras(String extras) {
            this.extras = extras;
        }
    }
}



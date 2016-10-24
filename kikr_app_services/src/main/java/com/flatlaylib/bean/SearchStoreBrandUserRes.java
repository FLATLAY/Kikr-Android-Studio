package com.flatlaylib.bean;

import java.util.List;

/**
 * Created by anshumaan on 2/18/2016.
 */
public class SearchStoreBrandUserRes {

    /**
     * code : 0000
     * stores : [{"id":"124396","name":"UGG Australia","description":"","img":"http://api.kikr.io/uploads/storebrandimg/Travel.jpg","is_followed":"no"}]
     * users : [{"id":"54","name":"Ali","description":"hdhd#jjdjdjdjdndmdkmskdmdmdmdmsmdmsmzm","img":"http://graph.facebook.com/10105366747142580/picture?type=large","is_followed":"no"},{"id":"57","name":"SnkrFrkr","description":"","img":"http://api.kikr.io//uploads/profile/57_1443745664.profile_pic","is_followed":"no"},{"id":"553","name":"AnaClara","description":"","img":"http://graph.facebook.com/10203741569219047/picture?type=large","is_followed":"no"},{"id":"561","name":"catalina16","description":"","img":"","is_followed":"no"},{"id":"597","name":"alisamm84","description":"","img":"","is_followed":"no"}]
     * brands : [{"id":"38","name":"Fergalicious","description":"","img":"http://api.kikr.io/uploads/storebrandimg/Fergalicious.jpg","logo":"http://api.kikr.io/uploads/storebrandlogo/fergalicious.jpg","is_followed":"no"},{"id":"81","name":"california","description":"","img":"http://api.kikr.io/uploads/storebrandimg/California.jpg","logo":"http://api.kikr.io/uploads/storebrandlogo/california.jpg","is_followed":"no"},{"id":"129","name":"Canali","description":"","img":"http://api.kikr.io/uploads/storebrandimg/Canali.jpg","logo":"http://api.kikr.io/uploads/storebrandlogo/canali.jpg","is_followed":"no"}]
     */

    private String code;
    /**
     * id : 124396
     * name : UGG Australia
     * description :
     * img : http://api.kikr.io/uploads/storebrandimg/Travel.jpg
     * is_followed : no
     */

    private List<StoresEntity> stores;
    /**
     * id : 54
     * name : Ali
     * description : hdhd#jjdjdjdjdndmdkmskdmdmdmdmsmdmsmzm
     * img : http://graph.facebook.com/10105366747142580/picture?type=large
     * is_followed : no
     */

    private List<UsersEntity> users;
    /**
     * id : 38
     * name : Fergalicious
     * description :
     * img : http://api.kikr.io/uploads/storebrandimg/Fergalicious.jpg
     * logo : http://api.kikr.io/uploads/storebrandlogo/fergalicious.jpg
     * is_followed : no
     */

    private List<BrandsEntity> brands;

    public void setCode(String code) {
        this.code = code;
    }

    public void setStores(List<StoresEntity> stores) {
        this.stores = stores;
    }

    public void setUsers(List<UsersEntity> users) {
        this.users = users;
    }

    public void setBrands(List<BrandsEntity> brands) {
        this.brands = brands;
    }

    public String getCode() {
        return code;
    }

    public List<StoresEntity> getStores() {
        return stores;
    }

    public List<UsersEntity> getUsers() {
        return users;
    }

    public List<BrandsEntity> getBrands() {
        return brands;
    }

    public static class StoresEntity {
        private String id;
        private String name;
        private String description;
        private String img;
        private String is_followed;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setIs_followed(String is_followed) {
            this.is_followed = is_followed;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImg() {
            return img;
        }

        public String getIs_followed() {
            return is_followed;
        }
    }

    public static class UsersEntity {
        private String id;
        private String name;
        private String description;
        private String img;
        private String is_followed;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setIs_followed(String is_followed) {
            this.is_followed = is_followed;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImg() {
            return img;
        }

        public String getIs_followed() {
            return is_followed;
        }
    }

    public static class BrandsEntity {
        private String id;
        private String name;
        private String description;
        private String img;
        private String logo;
        private String is_followed;

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public void setIs_followed(String is_followed) {
            this.is_followed = is_followed;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImg() {
            return img;
        }

        public String getLogo() {
            return logo;
        }

        public String getIs_followed() {
            return is_followed;
        }
    }
}

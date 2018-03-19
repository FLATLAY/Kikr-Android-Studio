package com.flatlaylib.bean;

/**
 * Created by RachelDi on 3/15/18.
 */

public class UserResult {
    String id;
    String name;
    String description;
    String img;
    String collections_count;
    String prodcount;
    String followers_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCollections_count() {
        return collections_count;
    }

    public void setCollections_count(String collections_count) {
        this.collections_count = collections_count;
    }

    public String getProdcount() {
        return prodcount;
    }

    public void setProdcount(String prodcount) {
        this.prodcount = prodcount;
    }

    public String getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(String followers_count) {
        this.followers_count = followers_count;
    }

    public String getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(String is_followed) {
        this.is_followed = is_followed;
    }

    String is_followed;

}

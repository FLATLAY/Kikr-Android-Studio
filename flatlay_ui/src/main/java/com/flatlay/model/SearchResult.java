package com.flatlay.model;

/**
 * Created by anshumaan on 2/19/2016.
 */
public class SearchResult implements Item {

    private String username;
    private String profile_pic;
    private String is_selected = "no";
    private String is_followedbyviewer;
    private String id;
    private String name;
    private String description;
    private String img;
    private String logo;
    private String is_followed;
    private String section_name;

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(String is_selected) {
        this.is_selected = is_selected;
    }

    public String getIs_followedbyviewer() {
        return is_followedbyviewer;
    }

    public void setIs_followedbyviewer(String is_followedbyviewer) {
        this.is_followedbyviewer = is_followedbyviewer;
    }

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIs_followed() {
        return is_followed;
    }

    public void setIs_followed(String is_followed) {
        this.is_followed = is_followed;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}

package com.flatlay.model;

/**
 * Created by anshumaan on 5/2/2016.
 */
public class InstagramImage {
    String low_resolution_url;
    String high_resolution_url;
    String thumbnail_url;


    public String getLow_resolution_url() {
        return low_resolution_url;
    }

    public void setLow_resolution_url(String low_resolution_url) {
        this.low_resolution_url = low_resolution_url;
    }

    public String getHigh_resolution_url() {
        return high_resolution_url;
    }

    public void setHigh_resolution_url(String high_resolution_url) {
        this.high_resolution_url = high_resolution_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
}

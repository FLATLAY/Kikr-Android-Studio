package com.kikr.model;

/**
 * Created by anshumaan on 2/19/2016.
 */
public class SectionItem implements Item{
    private final String title;

    public SectionItem(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}

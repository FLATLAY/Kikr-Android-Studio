package com.kikr.utility;

import com.kikr.model.InstagramImage;

import java.util.ArrayList;

public interface InstagramCallBack {
	void setProfilePic(String url);
	void setPictureList(ArrayList<String> photoList);
	void setPictureListPost(ArrayList<InstagramImage> photoList);
}

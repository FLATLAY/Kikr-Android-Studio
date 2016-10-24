package com.flatlay.utility;

import com.flatlay.model.InstagramImage;

import java.util.ArrayList;

public interface InstagramCallBack {
	void setProfilePic(String url);
	void setPictureList(ArrayList<String> photoList);
	void setPictureListPost(ArrayList<InstagramImage> photoList);
}

package com.kikrlib.bean;

import java.io.Serializable;

public class IbeaconMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -455356589191879981L;
	String id;
	String message;
	String description;
	String image;
	String smallimage;
	String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getSmallimage() {
		return smallimage;
	}
	public void setSmallimage(String smallimage) {
		this.smallimage = smallimage;
	}
}

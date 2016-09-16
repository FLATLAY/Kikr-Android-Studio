package com.kikr.twitter;

import java.io.Serializable;

public class OauthItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2921790622590722343L;
	private String friendName;
	private String friendId;
	private String friendImage;
	
	
	
	public String getFriendImage() {
		return friendImage;
	}
	public void setFriendImage(String friendImage) {
		this.friendImage = friendImage;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	

}

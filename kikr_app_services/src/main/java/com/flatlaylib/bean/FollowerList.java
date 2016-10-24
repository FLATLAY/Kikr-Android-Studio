package com.flatlaylib.bean;

public class FollowerList {
	String id;
	String username;
	String profile_pic;
	String is_followed;
	
	public String getIs_followed() {
		return is_followed;
	}

	public void setIs_followed(String is_followed) {
		this.is_followed = is_followed;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the profile_pic
	 */
	public String getProfile_pic() {
		return profile_pic;
	}
	/**
	 * @param profile_pic the profile_pic to set
	 */
	public void setProfile_pic(String profile_pic) {
		this.profile_pic = profile_pic;
	}
	
}

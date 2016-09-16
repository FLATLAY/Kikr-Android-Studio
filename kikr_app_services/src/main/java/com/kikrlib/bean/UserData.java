package com.kikrlib.bean;


public class UserData {
	String id;
	String username;
	String profile_pic;
	String background_pic;
	String is_followed;
	String description;
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the is_followed
	 */
	public String getIs_followed() {
		return is_followed;
	}

	/**
	 * @param is_followed the is_followed to set
	 */
	public void setIs_followed(String is_followed) {
		this.is_followed = is_followed;
	}

	/**
	 * @return the background_pic
	 */
	public String getBackground_pic() {
		return background_pic;
	}

	/**
	 * @param background_pic the background_pic to set
	 */
	public void setBackground_pic(String background_pic) {
		this.background_pic = background_pic;
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

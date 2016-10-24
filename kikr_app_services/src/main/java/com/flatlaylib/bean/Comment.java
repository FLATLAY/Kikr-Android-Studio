package com.flatlaylib.bean;

public class Comment {

	String user_id;
	String username;
	String comment;
	String comment_id;
	String profile_pic;

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

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return username;
	}
	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.username = user_name;
	}
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the comment_id
	 */
	public String getComment_id() {
		return comment_id;
	}
	/**
	 * @param comment_id the comment_id to set
	 */
	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}
	
	
}

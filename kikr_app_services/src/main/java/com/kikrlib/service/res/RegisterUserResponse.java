package com.kikrlib.service.res;

public class RegisterUserResponse {
	String code;
	String user_id;
	String message;
	String current_screen;
	String username;
	String email;
	String profile_pic;
	String background_pic;
	String cart_id;

	
	/**
	 * @return the cart_id
	 */
	public String getCart_id() {
		return cart_id;
	}

	/**
	 * @param cart_id the cart_id to set
	 */
	public void setCart_id(String cart_id) {
		this.cart_id = cart_id;
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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

	/**
	 * @return the current_screen
	 */
	public String getCurrent_screen() {
		return current_screen;
	}
	
	/**
	 * @param current_screen the current_screen to set
	 */
	public void setCurrent_screen(String current_screen) {
		this.current_screen = current_screen;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getId() {
		return user_id;
	}
	public void setId(String id) {
		this.user_id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "RegisterUserResponse[code: "+code+" user_id:"+user_id+" message:"+message+"]";
	}
}

package com.flatlaylib.service.res;

public class ChangePasswordRes {

	String code;
	String message;
	String password_created;

	/**
	 * @return the password_created
	 */
	public String getPassword_created() {
		return password_created;
	}

	/**
	 * @param password_created the password_created to set
	 */
	public void setPassword_created(String password_created) {
		this.password_created = password_created;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}

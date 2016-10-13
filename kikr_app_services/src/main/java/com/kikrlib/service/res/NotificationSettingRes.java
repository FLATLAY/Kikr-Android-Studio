package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.NotificationSetting;

public class NotificationSettingRes {
	String code;
	String message;
	List<NotificationSetting> data;
	String email;
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
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
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the data
	 */
	public List<NotificationSetting> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<NotificationSetting> data) {
		this.data = data;
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
	
}

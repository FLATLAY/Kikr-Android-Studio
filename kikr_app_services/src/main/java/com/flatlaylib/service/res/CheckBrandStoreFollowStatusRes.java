package com.flatlaylib.service.res;

import com.flatlaylib.bean.BrandList;

public class CheckBrandStoreFollowStatusRes {

	String code;
	String message;
	BrandList data;
	
	/**
	 * @return the data
	 */
	public BrandList getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(BrandList data) {
		this.data = data;
	}
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
	
}

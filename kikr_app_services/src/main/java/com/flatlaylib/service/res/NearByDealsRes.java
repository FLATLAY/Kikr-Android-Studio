package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.NearByDeal;

public class NearByDealsRes {

	String code;
	String message;
	List<NearByDeal> data;
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
	public List<NearByDeal> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<NearByDeal> data) {
		this.data = data;
	}
	
}

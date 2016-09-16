package com.kikrlib.service.res;

import com.kikrlib.bean.CartTotalInfo;

public class GetCartInfoRes {

	String code;
	CartTotalInfo total_info;
	String message;
	
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
	 * @return the total_info
	 */
	public CartTotalInfo getTotal_info() {
		return total_info;
	}
	/**
	 * @param total_info the total_info to set
	 */
	public void setTotal_info(CartTotalInfo total_info) {
		this.total_info = total_info;
	}
	
}

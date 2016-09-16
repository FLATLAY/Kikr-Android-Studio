package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.Categories;

public class GetProductsByCategoryRes {

	String code;
	String message;
	List<Categories> data;
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
	public List<Categories> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<Categories> data) {
		this.data = data;
	}
	
}

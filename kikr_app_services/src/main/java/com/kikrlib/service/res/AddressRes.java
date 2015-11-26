package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.Address;

public class AddressRes {
	String code;
	String message;
	List<Address> data;
	String address_id;
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
	public List<Address> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<Address> data) {
		this.data = data;
	}
	/**
	 * @return the address_id
	 */
	public String getAddress_id() {
		return address_id;
	}
	/**
	 * @param address_id the address_id to set
	 */
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	
	
	
	
}

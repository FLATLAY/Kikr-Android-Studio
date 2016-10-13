package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.CartTotalInfo;
import com.kikrlib.bean.Product;

public class CartRes {

	String code;
	String message;
	List<Product> data;
	List<CartTotalInfo> total_info;
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
	 * @return the total_info
	 */
	public List<CartTotalInfo> getTotal_info() {
		return total_info;
	}
	/**
	 * @param total_info the total_info to set
	 */
	public void setTotal_info(List<CartTotalInfo> total_info) {
		this.total_info = total_info;
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
	/**
	 * @return the data
	 */
	public List<Product> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<Product> data) {
		this.data = data;
	}
	

	

}

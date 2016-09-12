package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.BrandStoreItem;

public class BrandStoreRes {

	String code;
	String message;
	List<BrandStoreItem> stores;
	List<BrandStoreItem> brands;
	
	
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
	 * @return the store
	 */
	public List<BrandStoreItem> getStores() {
		return stores;
	}
	/**
	 * @param data the data to set
	 */
	public void setStores(List<BrandStoreItem> stores) {
		this.stores =stores;
	}

	public List<BrandStoreItem> getBrands() {
		return brands;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<BrandStoreItem> brands) {
		this.brands = brands;
	}
}

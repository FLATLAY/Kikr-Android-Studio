package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.ProductFeedItem;

public class ProductFeedRes {

	String code;
	String message;
	List<ProductFeedItem> data;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<ProductFeedItem> getData() {
		return data;
	}

	public void setData(List<ProductFeedItem> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

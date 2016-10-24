package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.Product;

public class ProductListRes {

	String code;
	String message;
	List<Product> data;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Product> getData() {
		return data;
	}

	public void setData(List<Product> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

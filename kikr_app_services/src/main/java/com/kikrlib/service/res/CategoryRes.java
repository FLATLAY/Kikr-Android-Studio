package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.Category;

public class CategoryRes {

	String code;
	String message;
	List<Category> data;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<Category> getData() {
		return data;
	}

	public void setData(List<Category> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

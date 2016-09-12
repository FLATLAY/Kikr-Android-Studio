package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.BrandList;

public class BrandListRes {

	String code;
	String message;
	List<BrandList> data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<BrandList> getData() {
		return data;
	}

	public void setData(List<BrandList> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

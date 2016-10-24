package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.CollectionList;

public class CollectionApiRes {

	String code;
	String message;
	List<CollectionList> data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<CollectionList> getCollection() {
		return data;
	}

	public void setCollection(List<CollectionList> collection) {
		this.data = collection;
	}
}

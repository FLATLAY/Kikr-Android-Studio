package com.kikrlib.service.res;

import com.kikrlib.bean.CollectionList;

public class AddCollectionApiRes {

	String code;
	String message;
	CollectionList data;

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

	public CollectionList getCollection() {
		return data;
	}

	public void setCollection(CollectionList collection) {
		this.data = collection;
	}
}

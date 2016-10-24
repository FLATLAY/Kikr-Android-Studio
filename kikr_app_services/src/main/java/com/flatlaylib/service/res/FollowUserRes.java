package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.FollowUser;

public class FollowUserRes {

	String code;
	String message;
	List<FollowUser> data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<FollowUser> getData() {
		return data;
	}

	public void setData(List<FollowUser> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

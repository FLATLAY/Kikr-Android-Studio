package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.BankList;
import com.kikrlib.bean.Category;

public class BankListRes {

	String code;
	String message;
	List<BankList> data;
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<BankList> getData() {
		return data;
	}

	public void setData(List<BankList> data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

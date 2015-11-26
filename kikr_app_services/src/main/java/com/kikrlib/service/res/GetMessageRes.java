package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.IbeaconMessage;

public class GetMessageRes {

	String code;
	String message;
	List<IbeaconMessage> message_list;

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

	public List<IbeaconMessage> getMessage_list() {
		return message_list;
	}

	public void setMessage_list(List<IbeaconMessage> message_list) {
		this.message_list = message_list;
	}

	

}

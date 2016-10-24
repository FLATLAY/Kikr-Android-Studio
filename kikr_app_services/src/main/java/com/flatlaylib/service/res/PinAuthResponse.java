package com.flatlaylib.service.res;

public class PinAuthResponse {
	String code;
	String message;
	
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
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "PinAuthResponse[code: "+code+" message:"+message+"]";
	}
}

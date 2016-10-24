package com.flatlaylib.service.res;

public class LoginResponse {
	String code;
	String id;
	String message;
	String session_token;
	
	public String getSession_token() {
		return session_token;
	}
	public void setSession_token(String session_token) {
		this.session_token = session_token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
		return "RegisterUserResponse[code: "+code+" user_id:"+id+" message:"+message+" session_token:"+session_token+"]";
	}
}

package com.kikrlib.service.res;


public class BraintreePaymentRes {

	String code;
	String message;
	String client_token;
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the client_token
	 */
	public String getClient_token() {
		return client_token;
	}
	/**
	 * @param client_token the client_token to set
	 */
	public void setClient_token(String client_token) {
		this.client_token = client_token;
	}

}

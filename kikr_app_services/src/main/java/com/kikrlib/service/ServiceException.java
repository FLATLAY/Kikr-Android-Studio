package com.kikrlib.service;

/***
 * An Exception defined as a common hood for this library exceptions. The class
 * composed of erorrCode and errorMessage.<BR>
 * <BR>
 * 
 * 1. errorCode - It is used for handling business exception.<BR>
 * <BR>
 * 
 * 2. errorMessage - Library provides error message for each exception.
 * 
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 8953715425904956887L;
	private long errorCode = 0;
	private String errorMessage = "";

	public ServiceException(long errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = message;
	}

	public long getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}

	public void setErrorCode(long errorCode) {
		this.errorCode = errorCode;
	}

}

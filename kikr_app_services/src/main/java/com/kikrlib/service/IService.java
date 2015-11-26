package com.kikrlib.service;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntityBuilder;


/**
 * 
 * A contract interface which an action/service has to implement to communicate
 * through {@link ServiceCommunicator}
 * 
 */
public interface IService {
	
	
	/**
	 * Use this function to provide httpMethod type. 
	 * @return "POST" or "GET" 
	 */
	public String getMethod();
	
	
	public String getHost();
	

	public String getActionName();

	/**
	 * Use this function to create request body for request
	 * 
	 * @return
	 */
	public List<NameValuePair> getNameValueRequest();
	
	
	/**
	 * Use this function to create MultipartEntity body for request
	 * 
	 * @return
	 */
	public MultipartEntityBuilder getMultipartRequest();
	
	/**
	 * Use this function to create Json body for request
	 * 
	 * @return
	 */
	public String getJsonRequest();
	

	/**
	 * Use this call back function to handle response of asynchronous request.
	 * 
	 * @param response
	 */
	public void handleResponse(String response);

	/**
	 * Use this call back function to handle exception occurs during execution
	 * of asynchronous request
	 * 
	 * @param exception
	 */
	public void handleException(ServiceException exception);

	public String getHeader();

}

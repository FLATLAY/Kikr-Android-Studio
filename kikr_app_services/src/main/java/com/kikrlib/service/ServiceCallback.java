package com.kikrlib.service;



/**
 * A contract interface which a <code>Class</code> instance must implement in
 * order to get service callback in response to <code>service</code> execution.
 * <p>
 * Along with success and failure callback, it will give response as a parameter
 * to callback method
 * 
 * @see AbsService
 * 
 */
public interface ServiceCallback {

	/**
	 * Callback method which indicates successful service execution.
	 * 
	 * This method will be called after successful completion of service execution
	 * and which can be used to handle post service execution functionality
	 * 
	 * @param object
	 *            Response of successful service execution
	 */
	public void handleOnSuccess(Object object);

	/**
	 * Callback method which indicates failure service execution.
	 * 
	 * This method will be called in case service execution has any exception<br>
	 * 
	 * 
	 * @param exception
	 *            {@link ServiceException}
	 * @param object
	 *            Response of failure service execution along with exception
	 */

	public void handleOnFailure(ServiceException exception, Object object);

}

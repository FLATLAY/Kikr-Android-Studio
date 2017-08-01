package com.flatlaylib.service;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import android.content.Context;
import android.util.Log;

import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.Syso;

import java.util.List;

/**
 * An Abstract service class which is responsible for service inputs
 * and service execution
 */
public abstract class AbsService implements IService {

    protected String METHOD_NAME = "";
    protected ServiceCallback mServiceCallback;
    protected Object serviceResponse = null;
    protected String serviceName;
    protected Context context;
    protected ServiceException serviceException = null;
    private ServiceCommunicator mComms = null;
    protected boolean isValidResponse = false;


    /**
     * This is generic method, which invokes {@link ServiceCommunicator} and
     * perform service execution
     * <p/>
     * In case of validation exception, service will delegate exception to
     * handles's {@link ServiceCallback #handleOnFailure(Exception)}
     */
    public void execute() {
        try {
            mComms = new ServiceCommunicator(this);
            mComms.connect();
        } catch (ServiceException se) {
            mServiceCallback.handleOnFailure(se, null);
            return;
        } catch (Exception e) {
            mServiceCallback.handleOnFailure(new ServiceException(WebConstants.NETWORK_ERROR, WebConstants.NETWORK_MESSAGE), null);
        }
    }

    /**
     * This is generic method, which invokes {@link ServiceCommunicator} and
     * will cancel service execution if running.
     */
    public void cancel() {
        if (mComms != null) {
            mComms.disconnect();
        }
    }

    @Override
    public String getHost() {
        return Constants.getServerIp();
//		return WebConstants.HOST_API;
    }


    @Override
    public String getMethod() {
        return WebConstants.HTTP_METHOD_POST;
    }

    @Override
    public MultipartEntityBuilder getMultipartRequest() {
        return null;
    }

    /**
     * This method needs to get override from services. which wants to process
     * service response
     */

    protected void processResponse(String response) {
        Log.w("AbsService","processResponse()");
    }

    @Override
    public void handleResponse(String response) {
        Syso.info("In handleResponse>>" + response);
        Log.w("my-App","In AbsService handleResponse");
        Syso.info("httpResponse", response);
        Log.w("AbsService","handleResponse 1:"+isValidResponse);
        processResponse(response);
        Log.w("AbsService","handleResponse 2:"+isValidResponse);
        if (isValidResponse) {
            Log.w("AbsService","if:"+isValidResponse);
            mServiceCallback.handleOnSuccess(serviceResponse);
        } else {
            if (serviceException == null) {
                Log.w("AbsService","else:"+isValidResponse);
                serviceException = new ServiceException(WebConstants.FAILED_ERROR, WebConstants.INVALID_RESPONSE_MESSAGE);
            }
            mServiceCallback.handleOnFailure(serviceException, serviceResponse);
        }
    }


    @Override
    public void handleException(ServiceException exception) {
        mServiceCallback.handleOnFailure(exception, null);
    }

    @Override
    public String getJsonRequest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getServiceName() {
        return serviceName;
    }

    @Override
    public String getHeader() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<NameValuePair> getNameValueRequestForOAuth() {
        return null;
    }

}
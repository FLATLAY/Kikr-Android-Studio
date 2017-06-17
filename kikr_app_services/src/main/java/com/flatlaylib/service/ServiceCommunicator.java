package com.flatlaylib.service;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.res.CommonRes;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.JsonUtils;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * This class is responsible for creating {@link AsyncTask} and executing
 * communication through that Async-task.
 */
public class ServiceCommunicator {

    private String TAG = this.getClass().getSimpleName();
    private IService service;
    private ServiceException exception;
    private ExecuteServiceTask mExecuteServiceTask;

    public ServiceCommunicator(IService service) {
        this.service = service;
    }

    protected void connect() {
        mExecuteServiceTask = new ExecuteServiceTask();
        mExecuteServiceTask.execute();
        Syso.info(TAG, "ServiceCommunicator = connect");
    }

    protected void disconnect() {
        if (mExecuteServiceTask != null) {
            mExecuteServiceTask.cancel(true);
        }
        Syso.info(TAG, "ServiceCommunicator = disconnect");
    }

    private class ExecuteServiceTask extends AsyncTask<String, Void, String> {

        private String mResponse = null;

        @Override
        protected String doInBackground(String... arg) {

            try {

                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;

                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, WebConstants.CONNECTIONTIMEOUT);
                HttpConnectionParams.setSoTimeout(httpParameters, WebConstants.CONNECTIONTIMEOUT);

                HttpClient httpClient = getNewHttpClient(httpParameters);
                String auth = UserPreference.getInstance().getUserID() + " , " + UserPreference.getInstance().getAccessToken();
                Log.w("my-App","Http Request");
                String urlString = service.getHost() + service.getActionName();
                String token=UserPreference.getInstance().getAccessToken();
                Syso.info(TAG + " urlString = ", urlString);
                Syso.debug(TAG, urlString);

                if (service.getMethod().equalsIgnoreCase(WebConstants.HTTP_METHOD_POST)) {
                    HttpPost httpPost = new HttpPost(urlString);

                    if (service.getNameValueRequest() != null) {
                        Log.w("my-App","#####################First");
                        Syso.info(TAG + " RequestParams = ", service.getNameValueRequest().toString());
                        if (service.getHeader() != null) {
                            httpPost = new HttpPost(service.getActionName());
                            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                            httpPost.setHeader("Authorization", service.getHeader());
                        }

                        httpPost.setEntity(new UrlEncodedFormEntity(service.getNameValueRequest()));
                    } else if (service.getMultipartRequest() != null) {
                        Log.w("my-App","#######################Second");
                        Syso.info(TAG + " RequestParams = ", service.getMultipartRequest().build().toString());

                        httpPost.setEntity(service.getMultipartRequest().build());
                        if (service.getHeader() != null) {

                            httpPost.setHeader("Authorization", service.getHeader());

                        }
                    } else if (service.getJsonRequest() != null) {
                        if (service.getServiceName() != null && service.getServiceName().equals("login")) {
                            Log.w("my-App","###################Third");
                            Syso.info(TAG + " RequestParams = ", service.getJsonRequest().toString());
                            StringEntity se = new StringEntity(service.getJsonRequest());
                            httpPost.setEntity(se);
                            httpPost.setHeader("Accept", "application/json");
                            httpPost.setHeader("Content-type", "application/json");

                            String base64EncodedCredentials = "Basic " + Base64.encodeToString(
                                    (Constants.email.trim() + ":" + Constants.pwd.trim()).getBytes(),
                                    Base64.NO_WRAP);
                            httpPost.setHeader("Authorization", base64EncodedCredentials);


                        } else {
                            Log.w("my-App","###################Fourth");
                            Syso.info(TAG + " RequestParams = ", service.getJsonRequest().toString());

                            //Log.w("my-app","social_token"+service.getJsonRequest());

                            Object json = new JSONTokener(service.getJsonRequest()).nextValue();
                            JSONObject jObj = null;
                            if (json instanceof JSONObject)
                            {
                                jObj = new JSONObject(service.getJsonRequest());
                                //Log.w("my-app","In here 1");
                            }
                            else if (json instanceof JSONArray)
                            {
                                JSONArray arr = new JSONArray(service.getJsonRequest());
                                jObj = arr.getJSONObject(0);
                                //Log.w("my-app","In here 2");

                            }
                            //JSONArray arr = new JSONArray(service.getJsonRequest());
                            //JSONObject jObj = arr.getJSONObject(0);
                            StringEntity se;
                            if (jObj.has("social_token"))
                            {
                                String temp_token = jObj.getString("social_token");
                                //Log.w("my-app", "social_token:" + temp_token);
                                jObj.put("social_token", "EAACJtDlQuJ4BALu4uNO16mc4hbsVS0DZBtGdHyX7tjktZCTtfZBEbW8oUdflwsypguEK6nggh1gRcwpYZAPzYDuCiLR1DOXAZBVUZBCHQ4hL7DTaw0PZCUGc0gWslQEySslnOwVRmiRx3HPUaSsK5LJcqxzRBr950QZD");
                                //Log.w("my-app", jObj.toString());
                                //Adding till log statement
                                JSONArray jsonArray = new JSONArray();
                                jsonArray.put(jObj);
                                //Log.w("my-app","In here 3"+jsonArray);
                                se = new StringEntity(jsonArray.toString());
                            }
                            else
                            {
                                se = new StringEntity(service.getJsonRequest());
                            }

                            //StringEntity se = new StringEntity(service.getJsonRequest());
                            httpPost.setEntity(se);
                            httpPost.setHeader("Accept", "application/json");
                            httpPost.setHeader("Content-type", "application/json");

                            if (service.getHeader() != null) {

                                httpPost.setHeader("Authorization", service.getHeader());

                            }
                        }
                    } else if (service.getNameValueRequestForOAuth() != null) {
                        httpClient = (DefaultHttpClient) getNewHttpClient(httpParameters);
                        httpPost.setHeader("Authorization", StringUtils.getBase64EncodedString(WebConstants.PAYPAL_CLIENT, WebConstants.PAYPAL_SECRET));
                        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                        httpPost.setHeader("Accept", "application/json");
                        httpPost.setHeader("Accept-Language", "en_US");

                        httpPost.setEntity(new UrlEncodedFormEntity(service.getNameValueRequestForOAuth(), HTTP.UTF_8));
//						Syso.info("==== request string : "+service.getNameValueRequestForOAuth());
//						Syso.info("==== response string : "+httpResponse.getStatusLine().getStatusCode()+"====: "+EntityUtils.toString( httpResponse.getEntity()));
                    }
                    //Log.w("my-App","httpPost"+httpPost);
                    httpResponse = httpClient.execute(httpPost);
                    //Log.w("my-App","httpResponse"+httpResponse);
                } else if (service.getMethod().equalsIgnoreCase(WebConstants.HTTP_METHOD_PUT)) {
                    HttpPut httpPut = new HttpPut(urlString);
                    if (service.getJsonRequest() != null) {
                        Log.w("my-App","#####################Fifth");
                        Syso.info(TAG + " RequestParams = ", service.getJsonRequest().toString());
                        StringEntity se = new StringEntity(service.getJsonRequest());
                        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
                        httpPut.setEntity(se);
                        httpPut.setHeader("Accept", "application/json");
                        httpPut.setHeader("Content-type", "application/json");
                        if (service.getHeader() != null) {

                            httpPut.setHeader("Authorization", service.getHeader());

                        }

                    }
                    httpResponse = httpClient.execute(httpPut);
                    //Log.w("my-App","This two");
                } else {
                    HttpGet httpGet = new HttpGet(urlString);

                    httpResponse = httpClient.execute(httpGet);
                }
                //Log.w("my-App1","test"+httpResponse);
                Syso.info(TAG + " Response = ", httpResponse);
                Syso.info(TAG + " StatusCode = ", httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200 || (service.getHeader() != null && httpResponse.getStatusLine().getStatusCode() == 201)) {
                    //Log.w("my-App3","test"+httpResponse);
                    httpEntity = httpResponse.getEntity();
                    //Log.w("my-App4","test"+httpEntity);
                    mResponse = EntityUtils.toString(httpEntity);
                    //Log.w("my-App5","test"+mResponse);
                } else if (httpResponse.getStatusLine().getStatusCode() == 400 ||
                        httpResponse.getStatusLine().getStatusCode() == 401 ||
                        httpResponse.getStatusLine().getStatusCode() == 500) {

                    httpEntity = httpResponse.getEntity();
                    String response = EntityUtils.toString(httpEntity);
                    try {
                        CommonRes signinResponse = JsonUtils.fromJson(response, CommonRes.class);
                        exception = new ServiceException(httpResponse.getStatusLine().getStatusCode(), signinResponse.getMessage());
                    } catch (JsonParseException e) {
                        Syso.error(e);
                        exception = new ServiceException(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());
                    }
                    mResponse = null;
                } else {
                    exception = new ServiceException(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());
                }

            } catch (ProtocolException e) {
                Syso.error(e.toString());
                exception = new ServiceException(WebConstants.NETWORK_ERROR, WebConstants.NETWORK_MESSAGE);

            } catch (IOException e) {
                Syso.error(e.toString());
                exception = new ServiceException(WebConstants.NETWORK_ERROR, WebConstants.NETWORK_MESSAGE);

            } catch (Exception e) {
                Syso.error(e.toString());
                exception = new ServiceException(WebConstants.NETWORK_ERROR, WebConstants.NETWORK_MESSAGE);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            //service.handleException(new ServiceException(WebConstants.NETWORK_ERROR, exception.getErrorMessage()));
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Syso.info("Response From Server: " + result);
            if (exception != null) {
                Syso.error(exception.getErrorMessage());
                service.handleException(exception);
            } else
                Log.w("my-App","Going into handle reponse"+mResponse);
                service.handleResponse(mResponse);
        }
    }

    public HttpClient getNewHttpClient(HttpParams params) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);
            // these 2 lines for time out 30 sec.
            HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
            HttpConnectionParams.setSoTimeout(params, 30 * 1000);
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            e.printStackTrace();
            return new DefaultHttpClient();
        }
    }

    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    try {
                        chain[0].checkValidity();
                    } catch (Exception e) {
                        throw new CertificateException("Certificate not valid or trusted.");
                    }
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host,
                    port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
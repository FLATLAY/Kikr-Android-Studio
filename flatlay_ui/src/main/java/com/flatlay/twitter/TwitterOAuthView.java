/*
 * Copyright (C) 2012-2013 Neo Visionaries Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flatlay.twitter;


import com.flatlay.R;
import com.flatlaylib.utils.AlertUtils;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class TwitterOAuthView extends WebView
{
    private static final String TAG = "TwitterOAuthView";
    private Context mContext;
    private static final boolean DEBUG = false;


    public enum Result
    {
        SUCCESS, CANCELLATION, REQUEST_TOKEN_ERROR, AUTHORIZATION_ERROR, ACCESS_TOKEN_ERROR
    }


    public interface Listener
    {
        void onSuccess(TwitterOAuthView view, AccessToken accessToken);


        void onFailure(TwitterOAuthView view, Result result);
    }


    private TwitterOAuthTask twitterOAuthTask;


    private boolean cancelOnDetachedFromWindow = true;


    private boolean isDebugEnabled = DEBUG;


    public TwitterOAuthView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext=context;
        // Additional initialization.
        init();
    }


    public TwitterOAuthView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext=context;
        // Additional initialization.
        init();
    }


    public TwitterOAuthView(Context context)
    {
        super(context);
        mContext=context;
        // Additional initialization.
        init();
    }


    private void init()
    {
        WebSettings settings = getSettings();

        // Not use cache.
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // Enable JavaScript.
        settings.setJavaScriptEnabled(true);

        // Enable zoom control.
        settings.setBuiltInZoomControls(true);

        // Scroll bar
        setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
    }


    public void start(String consumerKey, String consumerSecret,
            String callbackUrl, boolean dummyCallbackUrl,
            Listener listener)
    {
        if (consumerKey == null || consumerSecret == null || callbackUrl == null || listener == null)
        {
            throw new IllegalArgumentException();
        }
        Boolean dummy = Boolean.valueOf(dummyCallbackUrl);

        TwitterOAuthTask oldTask;
        TwitterOAuthTask newTask;

        synchronized (this)
        {
            oldTask = twitterOAuthTask;
            newTask = new TwitterOAuthTask();
            twitterOAuthTask = newTask;
        }

        cancelTask(oldTask);

        newTask.execute(consumerKey, consumerSecret, callbackUrl, dummy, listener);
    }


    public void cancel()
    {
        TwitterOAuthTask task;

        synchronized (this)
        {
            task = twitterOAuthTask;
            twitterOAuthTask = null;
        }

        cancelTask(task);
    }


    private void cancelTask(TwitterOAuthTask task)
    {
        if (task == null)
        {
            return;
        }

        if (task.isCancelled() == false)
        {
            if (isDebugEnabled())
            {
                Log.d(TAG, "Cancelling a task.");
            }

            task.cancel(true);
        }

        synchronized (task)
        {
            if (isDebugEnabled())
            {
                Log.d(TAG, "Notifying a task of cancellation.");
            }

            task.notify();
        }
    }


    public boolean isDebugEnabled()
    {
        return isDebugEnabled;
    }


    public void setDebugEnabled(boolean enabled)
    {
        isDebugEnabled = enabled;
    }


    public boolean isCancelOnDetachedFromWindow()
    {
        return cancelOnDetachedFromWindow;
    }


    public void setCancelOnDetachedFromWindow(boolean enabled)
    {
        cancelOnDetachedFromWindow = enabled;
    }


    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();

        if (isCancelOnDetachedFromWindow())
        {
            cancel();
        }
    }


    private class TwitterOAuthTask extends AsyncTask<Object, Void, Result>
    {
        private String callbackUrl;
        private boolean dummyCallbackUrl;
        private Listener listener;
        private Twitter twitter;
        private RequestToken requestToken;
        private volatile boolean authorizationDone;
        private volatile String verifier;
        private AccessToken accessToken;


        private boolean checkCancellation(String context)
        {
            if (isCancelled() == false)
            {
                return false;
            }

            if (isDebugEnabled())
            {
                Log.d(TAG, "Cancellation was detected in the context of " + context);
            }

            return true;
        }


        @Override
        protected void onPreExecute()
        {
            TwitterOAuthView.this.setWebViewClient(new LocalWebViewClient());
        }


        @Override
        protected Result doInBackground(Object... args)
        {
            if (checkCancellation("doInBackground() [on entry]"))
            {
                return Result.CANCELLATION;
            }

            String consumerKey = (String)args[0];
            String consumerSecret = (String)args[1];
            callbackUrl = (String)args[2];
            dummyCallbackUrl = (Boolean)args[3];
            listener = (Listener)args[4];

            if (isDebugEnabled())
            {
                debugDoInBackground(args);
            }

            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(consumerKey, consumerSecret);

            requestToken = getRequestToken();
            if (requestToken == null)
            {
                return Result.REQUEST_TOKEN_ERROR;
            }

            authorize();

            boolean cancelled = waitForAuthorization();
            if (cancelled)
            {
                return Result.CANCELLATION;
            }

            if (verifier == null)
            {
                return Result.AUTHORIZATION_ERROR;
            }

            if (checkCancellation("doInBackground() [before getAccessToken()]"))
            {
                return Result.CANCELLATION;
            }

            accessToken = getAccessToken();
            if (accessToken == null)
            {
                return Result.ACCESS_TOKEN_ERROR;
            }

            return Result.SUCCESS;
        }


        private void debugDoInBackground(Object... args)
        {
            Log.d(TAG, "CONSUMER KEY = " + (String)args[0]);
            Log.d(TAG, "CONSUMER SECRET = " + (String)args[1]);
            Log.d(TAG, "CALLBACK URL = " + (String)args[2]);
            Log.d(TAG, "DUMMY CALLBACK URL = " + (Boolean)args[3]);

            System.setProperty("twitter4j.debug", "true");
        }


        @Override
        protected void onProgressUpdate(Void... values)
        {
            if (checkCancellation("onProgressUpdate()"))
            {
                return;
            }

            String url = requestToken.getAuthorizationURL();

            if (isDebugEnabled())
            {
                Log.d(TAG, "Loading the authorization URL: " + url);
            }

            TwitterOAuthView.this.loadUrl(url);
        }


        @Override
        protected void onPostExecute(Result result)
        {
            if (isDebugEnabled())
            {
                Log.d(TAG, "onPostExecute: result = " + result);
            }

            if (result == null)
            {
                result = Result.CANCELLATION;
            }

            if (result == Result.SUCCESS)
            {
                fireOnSuccess();
            }
            else
            {
                fireOnFailure(result);
            }

            clearTaskReference();
        }


        @Override
        protected void onCancelled()
        {
            super.onCancelled();

            fireOnFailure(Result.CANCELLATION);

            clearTaskReference();
        }


        private void fireOnSuccess()
        {
            if (isDebugEnabled())
            {
                Log.d(TAG, "Calling Listener.onSuccess");
            }
            if(listener!=null)
            	listener.onSuccess(TwitterOAuthView.this, accessToken);
            else
            	AlertUtils.showToast(mContext, "Please try again");
        }


        private void fireOnFailure(Result result)
        {
            if (isDebugEnabled())
            {
                Log.d(TAG, "Calling Listener.onFailure, result = " + result);
            }

            // Call onFailure() method of the listener.
            if(listener!=null)
            	listener.onFailure(TwitterOAuthView.this, result);
            else
            	AlertUtils.showToast(mContext, "Please try again");
        }


        private void clearTaskReference()
        {
            synchronized (TwitterOAuthView.this)
            {
                if (TwitterOAuthView.this.twitterOAuthTask == this)
                {
                    TwitterOAuthView.this.twitterOAuthTask = null;
                }
            }
        }


        private RequestToken getRequestToken()
        {
            try
            {
                RequestToken token = twitter.getOAuthRequestToken();

                if (isDebugEnabled())
                {
                    Log.d(TAG, "Got a request token.");
                }

                return token;
            }
            catch (TwitterException e)
            {
                e.printStackTrace();
                Log.e(TAG, "Failed to get a request token.", e);

                return null;
            }
        }


        private void authorize()
        {
            publishProgress();
        }


        private boolean waitForAuthorization()
        {
            while (authorizationDone == false)
            {
                if (checkCancellation("waitForAuthorization()"))
                {
                    return true;
                }

                synchronized (this)
                {
                    try
                    {
                        if (isDebugEnabled())
                        {
                            Log.d(TAG, "Waiting for the authorization step to be done.");
                        }

                        this.wait();
                    }
                    catch (InterruptedException e)
                    {
                        if (isDebugEnabled())
                        {
                            Log.d(TAG, "Interrupted while waiting for the authorization step to be done.");
                        }
                    }
                }
            }

            if (isDebugEnabled())
            {
                Log.d(TAG, "Finished waiting for the authorization step to be done.");
            }

            return false;
        }


        private void notifyAuthorization()
        {
            authorizationDone = true;

            synchronized (this)
            {
                if (isDebugEnabled())
                {
                    Log.d(TAG, "Notifying that the authorization step was done.");
                }

                this.notify();
            }
        }


        private class LocalWebViewClient extends WebViewClient
        {

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.notification_error_ssl_cert_invalid);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);

                if (Build.VERSION.SDK_INT < 11)
                {
                    boolean stop = shouldOverrideUrlLoading(view, url);

                    if (stop)
                    {
                        stopLoading();
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if (url.startsWith(callbackUrl) == false)
                {
                    return false;
                }

                if (isDebugEnabled())
                {
                    Log.d(TAG, "Detected the callback URL: " + url);
                }

                Uri uri = Uri.parse(url);

                verifier = uri.getQueryParameter("oauth_verifier");

                if (isDebugEnabled())
                {
                    Log.d(TAG, "oauth_verifier = " + verifier);
                }

                notifyAuthorization();

                return dummyCallbackUrl;
            }
        }


        private AccessToken getAccessToken()
        {
            try
            {
                AccessToken token = twitter.getOAuthAccessToken(requestToken, verifier);

                if (isDebugEnabled())
                {
                    Log.d(TAG, "Got an access token for " + token.getScreenName());
                }

                return token;
            }
            catch (TwitterException e)
            {
                e.printStackTrace();
                Log.e(TAG, "Failed to get an access token.", e);

                return null;
            }
        }
    }
}
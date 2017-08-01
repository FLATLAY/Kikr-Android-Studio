package com.flatlay.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.flatlay.R;

public class DealWebViewActivity extends Activity {

	private WebView webView;
	private ProgressBar progressBarWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("Activity:","DealWebViewActivity");
		setContentView(R.layout.fragment_product_detail_webview);
		webView = (WebView) findViewById(R.id.webview1);
		String url = getIntent().getStringExtra("data");
		progressBarWebView = (ProgressBar) findViewById(R.id.progressBarWebView);
		setUpWebViewDefaults(webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
		webView.setWebViewClient(new MyWebViewClient());
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setUpWebViewDefaults(WebView webView) {
		WebSettings settings = webView.getSettings();

		// Enable Javascript
		settings.setJavaScriptEnabled(true);

		// Use WideViewport and Zoom out if there is no viewport defined
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);

		// Enable pinch to zoom without the zoom buttons
		settings.setBuiltInZoomControls(true);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			// Hide the zoom controls for HONEYCOMB+
			settings.setDisplayZoomControls(false);
		}

		// Enable remote debugging via chrome://inspect
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			WebView.setWebContentsDebuggingEnabled(true);
		}
	}

	private class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			progressBarWebView.setVisibility(View.GONE);
			DealWebViewActivity.this.progressBarWebView.setProgress(100);
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			progressBarWebView.setVisibility(View.VISIBLE);
			DealWebViewActivity.this.progressBarWebView.setProgress(0);
			super.onPageStarted(view, url, favicon);
		}
	}
}

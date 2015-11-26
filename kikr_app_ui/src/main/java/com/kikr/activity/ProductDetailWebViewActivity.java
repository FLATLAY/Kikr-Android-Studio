package com.kikr.activity;

import java.util.List;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.TopDeals;
import com.kikrlib.db.DatabaseHelper;
import com.kikrlib.db.dao.FavoriteDealsDAO;

public class ProductDetailWebViewActivity extends BaseActivity implements OnClickListener {

	private View mainView;
	private WebView webView;
	private ProgressBar progressBarWebView;
	private LinearLayout add_favorite_layout;
	private ImageView favorite_image;
	private TopDeals deals;
	private String url;
	private List<String> favoriteDealsIdList;
	final FavoriteDealsDAO dao = new FavoriteDealsDAO(DatabaseHelper.getDatabase());
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.fragment_product_detail_webview);
		
		this.url = getIntent().getExtras().getString("product_url");
		Log.e("url value product webview", url);
		
		webView = (WebView) findViewById(R.id.webview1);
		add_favorite_layout = (LinearLayout) findViewById(R.id.add_favorite_layout);
		favorite_image = (ImageView) findViewById(R.id.favorite_image);
		progressBarWebView = (ProgressBar) findViewById(R.id.progressBarWebView);
		setUpWebViewDefaults(webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
		webView.setWebViewClient(new MyWebViewClient());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.favorite_image:
			if (!deals.isFavorite()) {
				deals.setFavorite(true);
				dao.insert(deals);
				favorite_image.setImageResource(R.drawable.ic_deals_favorite);
			} else {
				deals.setFavorite(false);
				dao.delete(deals.getDealid());
				favoriteDealsIdList = dao.getDealIdList();
				favorite_image.setImageResource(R.drawable.ic_deals_unfavorite);
			}
			break;
		case R.id.leftTextView:
			finish();
			break;
		}
	}

	@Override
	public void initLayout() {
		
	}

	@Override
	public void setupData() {
		if (deals != null) {
			add_favorite_layout.setVisibility(View.VISIBLE);
			favoriteDealsIdList = dao.getDealIdList();
			if (favoriteDealsIdList.contains(deals.getDealid())) {
				favorite_image.setImageResource(R.drawable.ic_deals_favorite);
			} else {
				favorite_image.setImageResource(R.drawable.ic_deals_unfavorite);
			}
		}
	}

	@Override
	public void headerView() {
		setBackHeader();
		getLeftTextView().setOnClickListener(this);
	}

	@Override
	public void setClickListener() {
		// TODO Auto-generated method stub
		
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			progressBarWebView.setVisibility(View.GONE);
			ProductDetailWebViewActivity.this.progressBarWebView.setProgress(100);
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			progressBarWebView.setVisibility(View.VISIBLE);
			ProductDetailWebViewActivity.this.progressBarWebView.setProgress(0);
			super.onPageStarted(view, url, favicon);
		}
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
 
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            // Hide the zoom controls for HONEYCOMB+
            settings.setDisplayZoomControls(false);
        }
        
        // Enable remote debugging via chrome://inspect
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

	@Override
	public void setUpTextType() {
		// TODO Auto-generated method stub
		
	}
}

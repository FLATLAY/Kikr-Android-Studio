package com.flatlay.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.TopDeals;
import com.flatlaylib.db.DatabaseHelper;
import com.flatlaylib.db.dao.FavoriteDealsDAO;

import java.io.File;
import java.util.List;

public class FragmentProductDetailWebView extends BaseFragment implements OnClickListener {
	private View mainView;
	private WebView webView;
	private ProgressBar progressBarWebView;
	private LinearLayout add_favorite_layout;
	private ImageView favorite_image;
	private TopDeals deals;
	private String url;
	private List<String> favoriteDealsIdList;
	final FavoriteDealsDAO dao = new FavoriteDealsDAO(DatabaseHelper.getDatabase());
	private ImageButton btnBubble;
	private GestureDetector gestureDetector;
	Product product;
	Boolean cameFromSupportPage = false;
	private TextView description, learnmore, dismiss, usekikr;
	private LinearLayout overlay;
	private boolean clickedDismiss = false;
	
	public FragmentProductDetailWebView(TopDeals deals) {
		this.deals = deals;
		url = deals.getLink();
		this.cameFromSupportPage = true; //came from deal
	}
	//FragmentSupport
	public FragmentProductDetailWebView(String url) {
		this.url = url;
		this.cameFromSupportPage = true;
	}
	
	public FragmentProductDetailWebView(String url, Product product) {
		this.url = url;
		this.product = product;
		this.cameFromSupportPage = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_product_detail_webview,null);
		return mainView;
	}
	
	private class MyWebViewClient extends WebViewClient {

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
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.endsWith(".pdf"))
			{
				String googleDocs = "https://docs.google.com/viewer?url=";
				view.loadUrl(googleDocs + url);
				// Load "url" in google docs
			}
			else
			{
				// Load all other urls normally.
				view.loadUrl(url);
			}
		//	view.loadUrl(url);


			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			progressBarWebView.setVisibility(View.GONE);
			FragmentProductDetailWebView.this.progressBarWebView.setProgress(100);

			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			progressBarWebView.setVisibility(View.VISIBLE);
			FragmentProductDetailWebView.this.progressBarWebView.setProgress(0);
			super.onPageStarted(view, url, favicon);
		}
	}
	
	@Override
	public void initUI(Bundle savedInstanceState) {
		webView = (WebView) mainView.findViewById(R.id.webview1);
		add_favorite_layout = (LinearLayout) mainView.findViewById(R.id.add_favorite_layout);
		favorite_image = (ImageView) mainView.findViewById(R.id.favorite_image);
		progressBarWebView = (ProgressBar) mainView.findViewById(R.id.progressBarWebView);
		setUpWebViewDefaults(webView);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) //required for running javascript on android 4.1 or later
		{
			webView.getSettings().setAllowFileAccessFromFileURLs(true);
			webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		}

		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.getSettings().setAppCacheEnabled(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);

		File f = new File(url);
	/*	if (f.exists() == true) {
			Log.e(TAG, "Valid :" + url);
		} else {
			Log.e(TAG, "InValid :" + url);
		}*/

		webView.loadUrl(url);
		webView.setWebViewClient(new MyWebViewClient());
		btnBubble = (ImageButton) mainView.findViewById(R.id.btnBubble);
		description = (TextView) mainView.findViewById(R.id.thirdparty);
		learnmore = (TextView) mainView.findViewById(R.id.learnMore);
		dismiss = (TextView) mainView.findViewById(R.id.dismiss);
		usekikr = (TextView) mainView.findViewById(R.id.usekikr);
		overlay = (LinearLayout) mainView.findViewById(R.id.overlay);
	//	learnmore.setTypeface(null, Typeface.BOLD);
		//learnmore.setPaintFlags(learnmore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		learnmore.setText(Html.fromHtml("<b><u>Learn More</u></b>"));
		clickedDismiss = false;
		if(!cameFromSupportPage) {
			Animation slide_down = AnimationUtils.loadAnimation(mContext,
		            R.anim.slidedown);
			overlay.startAnimation(slide_down);
			overlay.setVisibility(View.VISIBLE);
			
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if(cameFromSupportPage)
						btnBubble.setVisibility(View.GONE);
					else
						btnBubble.setVisibility(View.VISIBLE);
					
				}
			}, 1000);

		} else {
			btnBubble.setVisibility(View.GONE);
		}
				
		
		gestureDetector = new GestureDetector(mContext, new SingleTapConfirm());
		
		btnBubble.setOnTouchListener(new View.OnTouchListener() {
			  private int initialX;
			  private int initialY;
			  private float initialTouchX;
			  private float initialTouchY;
			  DisplayMetrics metrics = getResources().getDisplayMetrics();
			  int windowWidth = metrics.widthPixels;
			  int windowHeight = metrics.heightPixels;

			  @Override 
			  public boolean onTouch(View v, MotionEvent event) {
				  //if(clickedDismiss)
				  if (gestureDetector.onTouchEvent(event)) {
					  if(((HomeActivity)mContext).isMenuShowing()){
	  					((HomeActivity)mContext).hideContextMenu();
	  				  }else{
	  					((HomeActivity)mContext).showContextMenuNoMidCircle(product,new UiUpdate(){
	  
	  						@Override
	  						public void updateUi() {
//	  							TextView likeCountTextView=(TextView) v.findViewById(R.id.likeCountTextView);
//	  							likeCountTextView.setText(TextUtils.isEmpty(data.get((Integer) v.getTag()).getLike_info().getLike_count())?"0":data.get((Integer) v.getTag()).getLike_info().getLike_count());
	  						}});
	  				  }
	                    return true;
	                } else {
	                    // your code for move and drag	             
					    switch (event.getAction()) {
					      case MotionEvent.ACTION_DOWN:
					        initialX = (int) btnBubble.getX();
					        initialY = (int) btnBubble.getY();
					        initialTouchX = event.getRawX();
					        initialTouchY = event.getRawY();
					        return true;
					      case MotionEvent.ACTION_UP:
					        return true;
					      case MotionEvent.ACTION_MOVE:
					    	  int newX = initialX + (int) (event.getRawX() - initialTouchX);
					          int newY = initialY + (int) (event.getRawY() - initialTouchY);
					          
					          if(newX <= 0) {
					        	  btnBubble.setX(0);
					        	  return true;
					          }
					        	  
					          if(newX + btnBubble.getWidth() >= windowWidth) {
					        	  btnBubble.setX(webView.getWidth() - btnBubble.getWidth());
					        	  return true;
					          }
					        	  
					          if(newY <=0 ) {
					        	  btnBubble.setY(0);
					        	  return true;
					          }
					        	  
					          if(newY + btnBubble.getHeight() >= webView.getHeight()) {
					        	  btnBubble.setY(webView.getHeight() - btnBubble.getHeight());
					        	  return true;
					          }
					        	 
					          btnBubble.setX(initialX + (int) (event.getRawX() - initialTouchX));
					          btnBubble.setY(initialY + (int) (event.getRawY() - initialTouchY));
				    			
					        return true;
					    }
	                }
			    return false;
			  }
		});
	}
	
	private class SingleTapConfirm extends SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }


	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		favorite_image.setOnClickListener(this);
		learnmore.setOnClickListener(this);
		dismiss.setOnClickListener(this);
		usekikr.setTypeface(FontUtility.setProximanovaLight(mContext));
		description.setTypeface(FontUtility.setProximanovaLight(mContext));
		learnmore.setTypeface(FontUtility.setProximanovaLight(mContext));
		dismiss.setTypeface(FontUtility.setProximanovaLight(mContext));
	}

	@Override
	public void setData(Bundle bundle) {
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
		case R.id.learnMore:
			addFragment(new FragmentLearnMore());
			break;
		case R.id.dismiss:
			Animation slide_up = AnimationUtils.loadAnimation(mContext,
		            R.anim.slideup);
			overlay.startAnimation(slide_up);
			overlay.setVisibility(View.INVISIBLE);
			//btnBubble.setClickable(true);
			clickedDismiss = true;
			break;
		}
	}
	
//Convenience method to set some generic defaults for a
    
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
        
    
}

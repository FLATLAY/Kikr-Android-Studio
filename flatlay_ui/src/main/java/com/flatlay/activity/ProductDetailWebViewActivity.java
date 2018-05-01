package com.flatlay.activity;

import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.menu.ArcLayout;
import com.flatlay.menu.ContextMenuView;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow4;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.TopDeals;
import com.flatlaylib.db.DatabaseHelper;
import com.flatlaylib.db.dao.FavoriteDealsDAO;
import com.google.gson.Gson;

public class ProductDetailWebViewActivity extends BaseActivity implements OnClickListener {

    private WebView webView;
    private ProgressBar progressBarWebView;
    private RelativeLayout contactLayout;
    private LinearLayout backIconLayout, infoLayout, add_favorite_layout;
    private ImageView favorite_image, backIcon, roundLogo, btnBubble;
    private TopDeals deals;
    private TextView text2, text3, text7, text8, text4, text5, text6, text9, contacttext1,
            contacttext2, contacttext3, contacttext4, contacttext8, lableTextView;
    private String url, jsonMyObject;
    private List<String> favoriteDealsIdList;
    private MyMaterialContentOverflow4 overflow1;
    private int index = 0;
    public final static String TAG="ProductDetailWeb";

    //    private ScrollView infoLayout;
    private GestureDetector gestureDetector;
    private ContextMenuView contextMenuBg;
    private View centarlView;
    private float lastX = 0, lastY = 0;
    private Product product;

    final FavoriteDealsDAO dao = new FavoriteDealsDAO(DatabaseHelper.getDatabase());

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.w(TAG,"ProductDetailWebViewActivity");
        CommonUtility.noTitleActivity(context);
        setContentView(R.layout.fragment_product_detail_webview);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonMyObject = extras.getString("data");
        }
        this.product = new Gson().fromJson(jsonMyObject, Product.class);
        this.url = product.getProducturl();
        webView = (WebView) findViewById(R.id.webview1);
        add_favorite_layout = (LinearLayout) findViewById(R.id.add_favorite_layout);
        favorite_image = (ImageView) findViewById(R.id.favorite_image);
        progressBarWebView = (ProgressBar) findViewById(R.id.progressBarWebView);
        setUpWebViewDefaults(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyWebViewClient());
        hideHeader();
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
//            case R.id.backIconLayout:
//                finish();
//                break;

            case R.id.text8:
                overflow1.triggerHide();
                break;

            case R.id.contacttext8:
                overflow1.triggerHide();
                break;

            case R.id.text7:
                if (index == 0) {
                    roundLogo.setVisibility(View.GONE);
                    text2.setText("CREDITS");
                    text4.setVisibility(View.VISIBLE);
                    text4.setText("1 credit for every $1 you spend.\n\n5 credits on every" +
                            "$1 spent by others on your collection.\n\nGet" +
                            "rewarded when users save products\nfrom your collections & " +
                            "influence new purchases.\n\n");
                    text3.setText("Flatlay Purchase Guarantee");
                    index = 1;
                    overflow1.setOpen();
                } else if (index == 1) {
                    text2.setText("FLATLAY PURCHASE GUARANTEE");
                    text4.setText("We at Flatlay work tirelessly to bring you name brand products from authentic " +
                            "merchants. You purchase directly from the merchants so you get the same return & " +
                            "exchanges policy as on the merchant'swebsite, but with the benefits of " +
                            "Flatlay rewards.\n\nFulfillment & shipping of all products are handled by the merchant, " +
                            "not by Flatlay.");
                    text5.setVisibility(View.VISIBLE);
                    text5.setText("Have a question about your order status, shipping or " +
                            "return/exchanges?");
                    text6.setVisibility(View.VISIBLE);
                    text6.setText("Please contact merchant directly.");
                    text7.setText(Html.fromHtml("<b><u>Contact Us</u></b>"));
                    text7.setTypeface(FontUtility.setMontserratLight(this));
                    text3.setText("Have a question about the app?");
                    text9.setVisibility(View.VISIBLE);
                    text9.setText("If you have trouble contacting a merchant directly, " +
                            "please don't hesitate to reach out to our team for assistance.");
                    index = 2;
                    overflow1.setOpen();
                } else {
                    infoLayout.setVisibility(View.GONE);
                    contactLayout.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.contacttext1:
                break;

            case R.id.contacttext2:
                break;

            case R.id.contacttext3:
                break;

            case R.id.contacttext4:
                break;
        }
    }

    @Override
    public void initLayout() {
        contextMenuBg = (ContextMenuView) findViewById(R.id.contextMenuBg);
        btnBubble = (ImageView) findViewById(R.id.btnBubble);
        text2 = (TextView) findViewById(R.id.text2);
        text2.setTypeface(FontUtility.setMontserratRegular(this));
        text3 = (TextView) findViewById(R.id.text3);
        text3.setTypeface(FontUtility.setMontserratRegular(this));
        text4 = (TextView) findViewById(R.id.text4);
        text4.setTypeface(FontUtility.setMontserratLight(this));
        text5 = (TextView) findViewById(R.id.text5);
        text5.setTypeface(FontUtility.setMontserratRegular(this));
        text6 = (TextView) findViewById(R.id.text6);
        text6.setTypeface(FontUtility.setMontserratLight(this));
        text7 = (TextView) findViewById(R.id.text7);
        text7.setTypeface(FontUtility.setMontserratLight(this));
        text7.setText(Html.fromHtml("<b><u>Learn More</u></b>"));
        text8 = (TextView) findViewById(R.id.text8);
        text8.setTypeface(FontUtility.setMontserratLight(this));
        text9 = (TextView) findViewById(R.id.text9);
        text9.setTypeface(FontUtility.setMontserratLight(this));
        backIcon = (ImageView) findViewById(R.id.backIconn);

        infoLayout = (LinearLayout) findViewById(R.id.infoLayout);
        overflow1 = (MyMaterialContentOverflow4) findViewById(R.id.overflow1);
        roundLogo = (ImageView) findViewById(R.id.roundlogo);
        contactLayout = (RelativeLayout) findViewById(R.id.contactLayout);
        backIconLayout = (LinearLayout) findViewById(R.id.backIconLayout);
        backIconLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        contacttext1 = (TextView) findViewById(R.id.contacttext1);
        contacttext1.setTypeface(FontUtility.setMontserratLight(this));
        contacttext2 = (TextView) findViewById(R.id.contacttext2);
        contacttext2.setTypeface(FontUtility.setMontserratLight(this));
        contacttext3 = (TextView) findViewById(R.id.contacttext3);
        contacttext3.setTypeface(FontUtility.setMontserratLight(this));
        contacttext4 = (TextView) findViewById(R.id.contacttext4);
        contacttext4.setTypeface(FontUtility.setMontserratLight(this));
        contacttext8 = (TextView) findViewById(R.id.contacttext8);
        contacttext8.setTypeface(FontUtility.setMontserratLight(this));

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());

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
                    if (isMenuShowing()) {
                        hideContextMenu();
                    } else {
                        showContextMenuNoMidCircle(product, new UiUpdate() {
                            @Override
                            public void updateUi() {

                            }
                        });
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

                            if (newX <= 0) {
                                btnBubble.setX(0);
                                return true;
                            }

                            if (newX + btnBubble.getWidth() >= windowWidth) {
                                btnBubble.setX(webView.getWidth() - btnBubble.getWidth());
                                return true;
                            }

                            if (newY <= 0) {
                                btnBubble.setY(0);
                                return true;
                            }

                            if (newY + btnBubble.getHeight() >= webView.getHeight()) {
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
        text8.setOnClickListener(this);
        contacttext8.setOnClickListener(this);
        text7.setOnClickListener(this);
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
            ProductDetailWebViewActivity.this.progressBarWebView.setProgress(100);
            super.onPageFinished(view, url);
            overflow1.triggerSlide();
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

        settings.setJavaScriptEnabled(true);

        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        settings.setBuiltInZoomControls(true);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            settings.setDisplayZoomControls(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    @Override
    public void setUpTextType() {
        // TODO Auto-generated method stub
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }

    }

    public boolean isMenuShowing() {
        return contextMenuBg.getVisibility() == View.VISIBLE ? true : false;
    }

    public void hideContextMenu() {
        contextMenuBg.setVisibility(View.GONE);
    }

    public void showContextMenuNoMidCircle(Product product, UiUpdate uiUpdate) {
        contextMenuBg.setVisibility(View.VISIBLE);
        contextMenuBg.setProduct(product, this);
        contextMenuBg.setUiUpdate(uiUpdate);
        contextMenuBg.unSelectIcons();
        showCentralOptionNoMidCircle();
    }

    public void showCentralOptionNoMidCircle() {
        if (centarlView == null) {
            ImageView view = new ImageView(context);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    (int) getResources().getDimension(R.dimen.menuChildSize),
                    (int) getResources().getDimension(R.dimen.menuChildSize));
            view.setLayoutParams(params);
            centarlView = view;
            contextMenuBg.addView(centarlView);
        }
        centarlView.setVisibility(View.INVISIBLE);
        centarlView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        float widht = centarlView.getMeasuredWidth();
        float height = centarlView.getMeasuredHeight();
        centarlView.setX((float) (lastX - (0.3 * widht)));
        centarlView.setY((float) (lastY - 1.2 * height));
        contextMenuBg.setXY(lastX, lastY);
    }

    public void showLableTextView(String value, float x, float y) {
        if (lableTextView == null) {
            lableTextView = new TextView(context);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lableTextView.setLayoutParams(params);
            lableTextView.setBackgroundResource(R.drawable.black_round_ract);
            lableTextView.setTextColor(Color.WHITE);
            lableTextView.setTypeface(FontUtility.setMontserratLight(context));
            contextMenuBg.addView(lableTextView);
        }
        lableTextView.setText(value);
        lableTextView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int widht = lableTextView.getMeasuredWidth();
        int height = lableTextView.getMeasuredHeight();
        ArcLayout arcLayout = contextMenuBg.getArcLayout();
        if (arcLayout != null) {
            lableTextView.setX(arcLayout.getX() + x - widht / 2);
            lableTextView.setY(arcLayout.getY() + y
                    - getResources().getDimension(R.dimen.lable_distance));
        }

        lableTextView.setVisibility(View.VISIBLE);
    }

    public void hideLableTextView() {
        if (lableTextView != null) {
            lableTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        float x = ev.getX();
        float y = ev.getY();
        lastX = x;
        lastY = y;
        if (isMenuShowing()) {
            return contextMenuBg.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

}


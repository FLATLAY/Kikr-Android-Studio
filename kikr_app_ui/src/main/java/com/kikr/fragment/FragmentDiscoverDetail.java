package com.kikr.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kikr.BaseFragment;
import com.kikr.KikrApp;
import com.kikr.KikrApp.TrackerName;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.activity.ProductDetailWebViewActivity;
import com.kikr.adapter.ProductPagerAdapter;
import com.kikr.dialog.CartOverLoadDialog;
import com.kikr.dialog.CollectionListDialog;
import com.kikr.dialog.ColourDialog;
import com.kikr.dialog.CreateAccountDialog;
import com.kikr.dialog.DialogCallback;
import com.kikr.dialog.FitDialog;
import com.kikr.dialog.OptionDialog;
import com.kikr.dialog.QuantityDialog;
import com.kikr.dialog.RemoveProductsFromCartDialog;
import com.kikr.dialog.SizeDialog;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.ui.RoundImageView;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.FontUtility;
import com.kikr.utility.UiUpdate;
import com.kikrlib.api.ActivityApi;
import com.kikrlib.api.CartApi;
import com.kikrlib.api.TwoTapApi;
import com.kikrlib.bean.CartProduct;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProductChildOption;
import com.kikrlib.bean.ProductMainOption;
import com.kikrlib.bean.ProductRequiredOption;
import com.kikrlib.bean.TwoTapProductDetails;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CartRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.viewpagerindicator.CirclePageIndicator;

public class FragmentDiscoverDetail extends BaseFragment implements OnClickListener, DialogCallback{
	private Button mCheckoutButton; 
	private View mainView;
	private Product product;
	private ViewPager productImageView;
	private ImageView addImageView;
	private TextView productTitleTextView,productRegularPriceTextView,productYourPriceTextView;
	private TextView descriptionTextView,addToCartTextView,saleTextView, viewDetailsText;
//	private boolean readCondition = true;
//	private String readLess = "<font color=#5bbaad>...read less</font>";
//	private String readMore ="<font color=#5bbaad>...read more</font>";
	private ProgressBar progressBarCart;
	private ProgressBarDialog mProgressBarDialog;
	private LinearLayout colorLayout, sizeLayout,fitLayout,optionLayout;
	private TextView colorTextView, sizeTextView, quantityTextView,optionTextView,fitTextView;
	private QuantityDialog quantityDialog;
	private ColourDialog colourDialog;
	private OptionDialog optionDialog;
	private FitDialog fitDialog;
	private SizeDialog sizeDialog;
	private String selectedSize="",selectedColor="";
	private String selectedOption="",selectedFit="";
	private TextView brandNameTextView,addToCollectionText,likeCountText;
	private ImageView addToCartText,shareImage;
	private RoundImageView brandImage;
	private UiUpdate uiUpdate;
	private String cartid,description ;
	private List<JSONArray> sizes = new ArrayList<JSONArray>();
	private CirclePageIndicator mIndicator;
	private ViewPager mPager;
	private ProductPagerAdapter mAdapter;
	private Handler handler = new Handler();
	private Runnable runnable;
	private int i = 0;
	private List<String> sizearray = new ArrayList<String>();
	private List<String> productMultipleImages = new ArrayList<String>();
	private List<String> colors = new ArrayList<String>();
	private List<String> sizeForSelectedColor = new ArrayList<String>();
	private List<String> optionList = new ArrayList<String>();
	private List<String> fitList = new ArrayList<String>();
	private LinearLayout loadingBar;
	private ProgressBar progressBar;
	private TextView txtMessageBar;
	private List<TwoTapProductDetails> productDetails = new ArrayList<TwoTapProductDetails>();
	private List<Product> productLists = new ArrayList<Product>();
	boolean isLoadingData = true, isSelectOptionRequired = false, isProductExist = true; 
	
	public FragmentDiscoverDetail(UiUpdate uiUpdate) {
		this.uiUpdate=uiUpdate;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.activity_discover_detail, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		mCheckoutButton = (Button) mainView.findViewById(R.id.checkoutButton);
		productImageView = (ViewPager) mainView.findViewById(R.id.productImageView);
		addImageView = (ImageView) mainView.findViewById(R.id.addImageView);
		addToCartTextView = (TextView) mainView.findViewById(R.id.addToCartTextView);
		productTitleTextView = (TextView) mainView.findViewById(R.id.productTitleTextView);
		productRegularPriceTextView = (TextView) mainView.findViewById(R.id.productRegularPriceTextView);
		productYourPriceTextView = (TextView) mainView.findViewById(R.id.productYourPriceTextView);
		descriptionTextView = (TextView) mainView.findViewById(R.id.descriptionTextView);
		progressBarCart = (ProgressBar) mainView.findViewById(R.id.progressBarCart);
		saleTextView=(TextView) mainView.findViewById(R.id.saleTextView);
		colorLayout=(LinearLayout) mainView.findViewById(R.id.colorLayout);
		sizeLayout=(LinearLayout) mainView.findViewById(R.id.sizeLayout);
		fitLayout=(LinearLayout) mainView.findViewById(R.id.fitLayout);
		fitTextView=(TextView) mainView.findViewById(R.id.fitTextView);
		optionTextView=(TextView) mainView.findViewById(R.id.optionTextView);
		optionLayout=(LinearLayout) mainView.findViewById(R.id.optionLayout);
		colorTextView=(TextView) mainView.findViewById(R.id.colorTextView);
		sizeTextView=(TextView) mainView.findViewById(R.id.sizeTextView);
		quantityTextView=(TextView) mainView.findViewById(R.id.quantityTextView);
		brandNameTextView=(TextView) mainView.findViewById(R.id.brandNameTextView);
		addToCollectionText=(TextView) mainView.findViewById(R.id.addToCollectionText);
		likeCountText=(TextView) mainView.findViewById(R.id.likeCountText);
		addToCartText=(ImageView) mainView.findViewById(R.id.addToCartText);
		shareImage=(ImageView) mainView.findViewById(R.id.shareImage);
		brandImage = (RoundImageView) mainView.findViewById(R.id.brandImage);
		mPager = (ViewPager) mainView.findViewById(R.id.zoom_viewpagerimagepager);
		mIndicator = (CirclePageIndicator) mainView.findViewById(R.id.indicator);
		mPager.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					arg0.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}
		});
		loadingBar = (LinearLayout) mainView.findViewById(R.id.loadingBar);
		
		viewDetailsText = (TextView) mainView.findViewById(R.id.viewDetailsWebView);
		progressBar = (ProgressBar) mainView.findViewById(R.id.progressBar);
		progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.btn_green), android.graphics.PorterDuff.Mode.MULTIPLY);
		txtMessageBar = (TextView) mainView.findViewById(R.id.txtMessageBar);
		txtMessageBar.setTextColor(getResources().getColor(R.color.btn_green));
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		mCheckoutButton.setOnClickListener(this);
		productImageView.setOnClickListener(this);
		addImageView.setOnClickListener(this);
		addToCartTextView.setOnClickListener(this);
//		descriptionTextView.setOnClickListener(this);
		addToCollectionText.setOnClickListener(this);
		likeCountText.setOnClickListener(this);
		addToCartText.setOnClickListener(this);
		shareImage.setOnClickListener(this);
		colorTextView.setOnClickListener(this);
		sizeTextView.setOnClickListener(this);
		optionTextView.setOnClickListener(this);
		fitTextView.setOnClickListener(this);
		viewDetailsText.setOnClickListener(this);
		viewDetailsText.setTypeface(FontUtility.setProximanovaLight(mContext));
		
		viewDetailsText.setTypeface(null, Typeface.BOLD);
		productRegularPriceTextView.setTypeface(null, Typeface.BOLD);
		productYourPriceTextView.setTypeface(null, Typeface.BOLD);
	}

	@Override
	public void setData(Bundle bundle) {
		product=(Product) bundle.getSerializable("data");
		productLists.add(product);
		double productPrice = Double.parseDouble(product.getRetailprice());
		if(!TextUtils.isEmpty(product.getSaleprice())&&!product.getSaleprice().equals("0")&&!product.getSaleprice().equals(product.getRetailprice())){
			productPrice = Double.parseDouble(product.getSaleprice());
		}
		
		com.google.android.gms.analytics.ecommerce.Product prod = new com.google.android.gms.analytics.ecommerce.Product()
		    .setId(product.getId())
		    .setName(product.getProductname())
		    .setCategory(product.getPrimarycategory())
		    .setBrand(product.getBrand())
		    .setPrice(productPrice);
		HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
		    .addImpression(prod, "Product Impression");

		Tracker t = ((KikrApp) mContext.getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Discover Detail");
		t.send(builder.build());
		setDetails();
		if(!TextUtils.isEmpty(product.getFrom_user_id())&&!TextUtils.isEmpty(product.getFrom_collection_id())&&checkInternet2()&&!product.getFrom_user_id().equals(UserPreference.getInstance().getUserID())){
			addProductView();
		}
		getCartId();
	}
	

	private void getCartId() {
//		mProgressBarDialog = new ProgressBarDialog(mContext);
//		mProgressBarDialog.show();
		isLoadingData = true;
		loadingBar.setVisibility(View.VISIBLE);
		TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("success:   "+ object);
				try {
					JSONObject jsonObject = new JSONObject(object.toString());
					cartid = (String) jsonObject.get("cart_id");
					runnable = new Runnable() {
						
						@Override
						public void run() {
							if (i<=1) {
								getStatus(cartid);
							}
						}
					};
					handler.postDelayed(runnable, 3000);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				
			}
		});
		List<String> products = new ArrayList<String>();
		products.add(product.getProducturl());
		twoTapApi.getCartId(products);
		twoTapApi.execute();
	}
	
	private void getStatus(final String cart_id) {
		TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("success:   "+ object);
//				getData(object);
				try{
					JSONObject object2=new JSONObject(object.toString());
					String message=object2.getString("message");
					if(message.equalsIgnoreCase("has_failures")||message.equalsIgnoreCase("done")){
						getData(object);
					}else{
						getStatus(cart_id);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				Syso.info("failed:   "+ object);
				//mProgressBarDialog.dismiss();
			}
		});
		twoTapApi.getCartStatus(cart_id);
		twoTapApi.execute();
	}
	
	protected void getData(Object object) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		try {
			JSONObject jsonObject = new JSONObject(object.toString());
			JSONObject sites;
			try {
				sites = jsonObject.getJSONObject("sites");
				Iterator keys = sites.keys();
				while (keys.hasNext()) {
					// loop to get the dynamic key
					String currentDynamicKey = (String) keys.next(); //site id
					// get the value of the dynamic key
					JSONObject currentDynamicValue = sites.getJSONObject(currentDynamicKey);
					// do something here with the value...
					JSONObject add_to_cart = null;
					JSONObject failed_to_add_to_cart = null;
					try {
						add_to_cart = currentDynamicValue.getJSONObject("add_to_cart");
							Syso.info("add_to_cart   " + add_to_cart);
							Iterator data = add_to_cart.keys();
							while (data.hasNext()) {
								// loop to get the dynamic key
								String currentKey = (String) data.next(); //product md5
								Syso.info("currentKey   " + currentKey);
								// get the value of the dynamic key
								JSONObject value = add_to_cart.getJSONObject(currentKey);
								// do something here with the value...
								description = value.getString("description");
								
								if(value.has("alt_images")){
									JSONArray alt_images = value.getJSONArray("alt_images");
							        for (int i = 0; i < alt_images.length(); i++) {
										productMultipleImages.add(alt_images.getString(i));
									}
								}else if(value.has("image")){
									productMultipleImages.add(value.getString("image"));
								}
								
								Syso.info("value   " + value);
								try {
									if (value.getString("status").equals("still_processing")) {
										list.clear();
										getStatus(cartid);
									}else{
										value.put("site_id", currentDynamicKey);
										value.put("md5", currentKey);
										list.add(value);
									}
								} catch (Exception e) {
									e.printStackTrace();
									list.clear();
									getStatus(cartid);
								}
							}
//						}
					} catch (Exception e) {
						e.printStackTrace();
						failed_to_add_to_cart = currentDynamicValue.getJSONObject("failed_to_add_to_cart");
						Iterator data = failed_to_add_to_cart.keys();

						while (data.hasNext()) {
							String currentKey = (String) data.next();
							Syso.info("currentKey   " + currentKey);
							JSONObject value = failed_to_add_to_cart.getJSONObject(currentKey);
							Syso.info("value   " + value);
							setFailedProduct();
						}
						e.printStackTrace();
					}
				}
				try{
					JSONArray array = jsonObject.getJSONArray("unknown_urls");
					for(int i=0;i<array.length();i++){
						String url=array.getString(i);
						setFailedProduct();
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		fetchData(list);
	}

	private void fetchData(List<JSONObject> list) {
		for (int i = 0; i < list.size(); i++) {
			TwoTapProductDetails product = new TwoTapProductDetails();
			if (list.get(i)!=null) {
				JSONObject value = list.get(i);
				try {
					product.setOriginal_url(value.getString("original_url"));
				} catch (JSONException e3) {
					e3.printStackTrace();
				}
				try{
					product.setSiteId(value.getString("site_id"));
					product.setMd5(value.getString("md5"));
					product.setUrl(value.getString("url"));
				}catch(Exception e){
					e.printStackTrace();
				}
				try{
					JSONArray arr = value.getJSONArray("required_field_names");
					List<ProductRequiredOption> requiredOptionList = new ArrayList<ProductRequiredOption>();
					for (int j = 0; j < arr.length(); j++) {
						ProductRequiredOption  requiredOption = new ProductRequiredOption();
						requiredOption.setName(arr.getString(j));
						requiredOptionList.add(requiredOption);
					}
					product.setRequiredOptionList(requiredOptionList);
					JSONObject required_field_values = value.getJSONObject("required_field_values");
					product.setProductMainOptionList(getMainOptionList(required_field_values));
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			productDetails.add(product);
		}
		List<String> productToSelectDetail = new ArrayList<String>();
		for (int i = 0; i < productDetails.size(); i++) {		
			List<ProductRequiredOption> list2 = productDetails.get(i).getRequiredOptionList();
			if (list2.size()>1) {  //assuming that quantity will always present so size should be > 1 for selection.
				productToSelectDetail.add(productDetails.get(i).getOriginal_url());
				Syso.info("123456 has data>"+productDetails.get(i).getOriginal_url());
			}else{
				setNoDetailsProduct();
				Syso.info("123456 has no data>"+productDetails.get(i).getOriginal_url());
			}
		}
		for (int i = 0; i < productDetails.size(); i++) {
			for (int j = 0; j < productLists.size(); j++) {
				if (productDetails.get(i).getOriginal_url().equals(productLists.get(j).getProducturl())) {
					productLists.get(j).setRequiredOptions(productDetails.get(i).getRequiredOptionList());
					productLists.get(j).setProductMainOptionList(productDetails.get(i).getProductMainOptionList());
					productLists.get(j).setSiteId(productDetails.get(i).getSiteId());
					productLists.get(j).setMd5(productDetails.get(i).getMd5());
					productLists.get(j).setUrl(productDetails.get(i).getUrl());
				}
			}
		}
		isLoadingData =  false;
		isSelectOptionRequired = productToSelectDetail.size()>0?true:false;	
		setData();
	}

	private void setFailedProduct() {
		isProductExist = false;
	}
	
	private void setNoDetailsProduct() {
		isProductExist = false;
	}

	private List<ProductMainOption> getMainOptionList(JSONObject optJSONObject) {
		Iterator data = optJSONObject.keys();
		List<ProductMainOption> productMainOptionList = new ArrayList<ProductMainOption>();
		try{
			while (data.hasNext()) {
				// loop to get the dynamic key
				String currentKey = (String) data.next();
				ProductMainOption mainOption = new ProductMainOption();
				JSONArray  mainOptionArray = optJSONObject.getJSONArray(currentKey);
				mainOption.setName(currentKey);
				List<ProductChildOption> optionList = new ArrayList<ProductChildOption>();
				for(int j=0; j<mainOptionArray.length(); j++){
					ProductChildOption childOption = new ProductChildOption();
					JSONObject option = mainOptionArray.getJSONObject(j);
					childOption.setExtra_info(option.optString("extra_info"));
					childOption.setImage(option.optString("image"));
					childOption.setPrice(option.optString("price"));
					childOption.setText(option.optString("text"));
					childOption.setValue(option.optString("value"));
					childOption.setDep(getMainOptionList(option.optJSONObject("dep")));
					optionList.add(childOption);
				}
				mainOption.setOptionList(optionList);
				productMainOptionList.add(mainOption);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return productMainOptionList;
	}
	
	protected void getData1(Object object) {
		try{
		JSONObject jsonObject = new JSONObject(object.toString());
		JSONObject add_to_cart = null;
		JSONObject sites = jsonObject.getJSONObject("sites");
		Iterator keys = sites.keys();
	    while(keys.hasNext()) {
	        // loop to get the dynamic key
	        String currentDynamicKey = (String)keys.next();

	        // get the value of the dynamic key
	        JSONObject currentDynamicValue = sites.getJSONObject(currentDynamicKey);

	        // do something here with the value...
			add_to_cart = currentDynamicValue.getJSONObject("add_to_cart");
//	        if (add_to_cart==null) {
//	        	mProgressBarDialog.dismiss();
//				AlertUtils.showToast(mContext, "Product not available. Please try again later");
//				((HomeActivity)mContext).onBackPressed();
//			} else {
//				Syso.info("add_to_cart   "+add_to_cart);
		        Iterator data = add_to_cart.keys();
			    while(data.hasNext()) {
			        // loop to get the dynamic key
			        String currentKey = (String)data.next();
			        Syso.info("currentKey   "+currentKey);

			        // get the value of the dynamic key
			        JSONObject currentValue = add_to_cart.getJSONObject(currentKey);
			        Syso.info("currentValue   "+currentValue);
			        // do something here with the value...
			        description = currentValue.getString("description");
//			        JSONObject required_field_values = currentValue.getJSONObject("required_field_values");
//			        Syso.info("required_field_values  "+required_field_values);
//			        if (required_field_values!=null) {
//						try {
//							JSONArray color = required_field_values
//									.getJSONArray("color");
//							colors.clear();
//							if (color != null) {
//								for (int i = 0; i < color.length(); i++) {
//									colors.add(color.getJSONObject(i).getString(
//											"text"));
//								}
//								for (int i = 0; i < colors.size(); i++) {
//									sizes.add(color.getJSONObject(i)
//											.getJSONObject("dep")
//											.getJSONArray("size"));
//								}
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						try {
//							if(colors.size()==0 && required_field_values!=null) {
//								JSONArray size = required_field_values
//										.getJSONArray("size");
//								sizearray .clear();
//								if (size != null) {
//									for (int i = 0; i < size.length(); i++) {
//										sizearray.add(size.getJSONObject(i)
//												.getString("text"));
//									}
//								}
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						try {
//							if(colors.size()==0 && sizearray.size()==0 && required_field_values!=null) {
//								JSONArray option = required_field_values
//										.getJSONArray("option");
//								optionList .clear();
//								if (option != null) {
//									for (int i = 0; i < option.length(); i++) {
//										optionList.add(option.getJSONObject(i)
//												.getString("text"));
//									}
//								}
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						try {
//							if(colors.size()==0 && sizearray.size()==0 && optionList.size()==0 && required_field_values!=null) {
//								JSONArray fit = required_field_values
//										.getJSONArray("fit");
//								fitList .clear();
//								if (fit != null) {
//									for (int i = 0; i < fit.length(); i++) {
//										fitList.add(fit.getJSONObject(i)
//												.getString("text"));
//										}
//									List<JSONArray> color = new ArrayList<JSONArray>();
//									for (int i = 0; i < fit.length(); i++) {
//										color.add(fit.getJSONObject(i)
//												.getJSONObject("dep")
//												.getJSONArray("color"));
//									}
//									for (int i = 0; i < color.size(); i++) {
//										colors.add(color.get(i).getJSONObject(i).getString("text"));
//										sizes.add(color.get(i).getJSONObject(i)
//												.getJSONObject("dep")
//												.getJSONArray("size"));
//									}
//									}
//								}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
			        JSONArray alt_images = currentValue.getJSONArray("alt_images");
			        for (int i = 0; i < alt_images.length(); i++) {
						productMultipleImages.add(alt_images.getString(i));
							}
						}
					}
//				}
//			}
		}
	catch (JSONException e){
		e.printStackTrace();
	}
		 setData();
	}

	private void setData() {
		if (productMultipleImages.size()==0 && i==0) {
			i=1;
			handler.postDelayed(runnable, 3000);
		} else{
			loadingBar.setVisibility(View.GONE);
//			mProgressBarDialog.dismiss();
			if (productMultipleImages.size()>0) {
				Syso.info("productMultipleImages "+productMultipleImages);
				mPager.setVisibility(View.VISIBLE);
				productImageView.setVisibility(View.GONE);
				if(productMultipleImages.size() > 1)
					productMultipleImages.remove(0);
				mAdapter = new ProductPagerAdapter(mContext, productMultipleImages,product.getProducturl(), product);
				mPager.setAdapter(mAdapter);
				mIndicator.setViewPager(mPager);
			}else{
				mPager.setVisibility(View.GONE);
				productImageView.setVisibility(View.VISIBLE);
				productMultipleImages.add(product.getProductimageurl());
				mAdapter = new ProductPagerAdapter(mContext, productMultipleImages,product.getProducturl(), product);
				productImageView.setAdapter(mAdapter);
				//CommonUtility.setImage(mContext, product.getProductimageurl(), productImageView, R.drawable.dum_list_item_brand);
			}
		if (!TextUtils.isEmpty(description)) {
        	descriptionTextView.setText(Html.fromHtml(description));
		} else {
			descriptionTextView.setText(getDescription());
		}
//		if(colors.size()>0){
//			colorLayout.setVisibility(View.VISIBLE);
//			colorTextView.setText(colors.get(0));
//			selectedColor=colors.get(0);
//			setSizeForSelectedColor(0);
//		}
//		if(optionList.size()>0){
//			optionLayout.setVisibility(View.VISIBLE);
//			optionTextView.setText(optionList.get(0));
//			selectedOption=optionList.get(0);
//		}
//		if(fitList.size()>0){
//			fitLayout.setVisibility(View.VISIBLE);
//			fitTextView.setText(fitList.get(0));
//			selectedFit=fitList.get(0);
//		}
//		if(sizeForSelectedColor.size()>0){
//			sizeLayout.setVisibility(View.VISIBLE);
//			sizeTextView.setText(sizeForSelectedColor.get(0));
//			selectedSize=sizeForSelectedColor.get(0);
//		}else if(sizearray.size()>0){
//			sizeLayout.setVisibility(View.VISIBLE);
//			sizeTextView.setText(sizearray.get(0));
//			selectedSize=sizearray.get(0);
//		}
	}		
	}

	private void setDetails() {
		productRegularPriceTextView.setText(" $"+CommonUtility.getFormatedNum(product.getRetailprice()));
		if(!TextUtils.isEmpty(product.getSaleprice())&&!product.getSaleprice().equals("0")&&!product.getSaleprice().equals(product.getRetailprice())){
			saleTextView.setVisibility(View.VISIBLE);
			productYourPriceTextView.setText(" $"+CommonUtility.getFormatedNum(product.getSaleprice()));
			productRegularPriceTextView.setPaintFlags(productRegularPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}
		productTitleTextView.setText(product.getProductname());
//      CommonUtility.setImage(mContext, product.getProductimageurl(), brandImage, R.drawable.dum_list_item_brand);
		productMultipleImages.add(product.getProductimageurl());
		mAdapter = new ProductPagerAdapter(mContext, productMultipleImages,product.getProducturl(), product);
		productImageView.setAdapter(mAdapter);
	//	CommonUtility.setImage(mContext, product.getProductimageurl(), productImageView, R.drawable.dum_list_item_brand);
		descriptionTextView.setText(getDescription());	
//		descriptionTextView.setText(Html.fromHtml(productList.getLongproductdesc()+readMore));	
//		if(product.getColor()!=null&&product.getColor().length()>0){
//			colorLayout.setVisibility(View.VISIBLE);
//			colorTextView.setText(product.getColor().split(",")[0]);
//			colorTextView.setOnClickListener(this);
//			selectedColor=product.getColor().split(",")[0];
//		}
//		if(product.getSize()!=null&&product.getSize().length()>0){
//			sizeLayout.setVisibility(View.VISIBLE);
//			sizeTextView.setText(product.getSize().split(",")[0]);
//			sizeTextView.setOnClickListener(this);
//			selectedSize=product.getSize().split(",")[0];
//		}
		quantityTextView.setOnClickListener(this);
		brandNameTextView.setText(product.getMerchantname());
		likeCountText.setText(CommonUtility.getInt(product.getLike_info().getLike_count())+"");
		if(TextUtils.isEmpty(product.getLike_info().getLike_id()))
			likeCountText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_flame_logo, 0);
		else
			likeCountText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_flame_logo, 0);
		if(!TextUtils.isEmpty(product.getBrand_image()))
			CommonUtility.setImage(mContext, product.getBrand_image(), brandImage, R.drawable.dum_list_item_product);
	}

//	private String getSalePrice(String price) {
//		try{
//			double d=Double.parseDouble(price);
//			return new DecimalFormat("##.##").format(d);
//		}catch(Exception exception){
//			return price;
//		}	
//	}

	private void setSizeForSelectedColor(int position) {
		sizeForSelectedColor.clear();
		if (sizes.size()>0) {
			for (int i = 0; i < sizes.get(position).length(); i++) {
			try {
				sizeForSelectedColor.add(sizes.get(position).getJSONObject(i).getString("text"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}		
		}
	}

	private CharSequence getDescription() {
		int sDescLength=product.getShortproductdesc().length();
		int lDescpLength=product.getLongproductdesc().length();
		if(sDescLength!=0&&lDescpLength!=0){
			return sDescLength<lDescpLength?product.getShortproductdesc():product.getLongproductdesc();
		}else{
			return lDescpLength==0?product.getShortproductdesc():product.getLongproductdesc();
		}
//		return product.getShortproductdesc().length()<product.getLongproductdesc().length()?product.getShortproductdesc():product.getLongproductdesc();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkoutButton:
			if(checkInternet()){
//				addFragment(new DumFragmentGoogleWallet());
				addProductToCart(true);
//				startActivity(CheckoutActivity.class);
			}
			break;
		case R.id.addImageView:
			if(((HomeActivity) mContext).checkInternet()){
				if( UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == ""){
					CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
					createAccountDialog.show();
			}else{
				CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, product);
				collectionListDialog.show();
			}
				
			}
			break;
		case R.id.addToCartTextView:
			if(((HomeActivity) mContext).checkInternet()){
				addProductToCart(false);
			}
			break;
		case R.id.productImageView:
			if(((HomeActivity) mContext).checkInternet() && productMultipleImages.size()==0)
				getCartId();
//				addFragment(new FragmentProductDetailWebView(product.getProducturl()));
			break;
		case R.id.optionTextView:
			optionDialog = new OptionDialog(mContext,this,optionList,product.getOption());
			optionDialog.show();
			break;
		case R.id.fitTextView:
			fitDialog = new FitDialog(mContext,this,fitList,product.getFit());
			fitDialog.show();
			break;
		case R.id.colorTextView:
			colourDialog = new ColourDialog(mContext,this,colors,product.getSelected_color());
			colourDialog.show();
			break;
		case R.id.sizeTextView:
			if (sizeForSelectedColor.size()>0) {
				sizeDialog = new SizeDialog(mContext,this,sizeForSelectedColor,product.getSelected_size());
				sizeDialog.show();
			}else if(sizearray.size()>0){
				sizeDialog = new SizeDialog(mContext,this,sizearray,product.getSelected_size());
				sizeDialog.show();
			}
			break;
		case R.id.quantityTextView:
			if(product.getQuantity()==null)
				product.setQuantity("1");
			quantityDialog = new QuantityDialog(mContext,this,product);
			quantityDialog.show();
			break;
		case R.id.addToCollectionText:
			if(((HomeActivity) mContext).checkInternet()){
				CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, product);
				collectionListDialog.show();
			}
			break;
		case R.id.likeCountText:
			if(((HomeActivity) mContext).checkInternet())
				((HomeActivity) mContext).likeInspiration(product,new UiUpdate() {
					
					@Override
					public void updateUi() {
						try{
							likeCountText.setText(CommonUtility.getInt(product.getLike_info().getLike_count())+"");
							if(TextUtils.isEmpty(product.getLike_info().getLike_id()))
								likeCountText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_flame_logo, 0);
							else
								likeCountText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_flame_logo, 0);
							if(uiUpdate!=null){
								uiUpdate.updateUi();
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
			break;
		case R.id.addToCartText:
			if(((HomeActivity) mContext).checkInternet()){
				addProductToCart(false);
			}
			break;
		case R.id.shareImage:
			((HomeActivity) mContext).shareProduct(product);
			break;	
		case R.id.viewDetailsWebView:
			addFragment(new FragmentProductDetailWebView(product.getAffiliateurlforsharing(), product));
			break;
		default:
			break;
		}
	}

	public void addToCartFromEdit(boolean isFromBuy){
		isSelectOptionRequired = false;
		addProductToCart(isFromBuy);
	}
	
	public void addProductToCart(final boolean isFromBuy) {
		
		if(isLoadingData)
		{
			AlertUtils.showToast(mContext, "Please wait, while required data is loading...");
			return;
		}else if(!isProductExist){
			AlertUtils.showToast(mContext, "Product is not available");
			return;
		}else if(isSelectOptionRequired){
			AlertUtils.showToast(mContext, "Please select required values");
			((HomeActivity)mContext).addFragment(new FragmentEditPurchaseItem(productLists.get(0),this,isFromBuy));
			return;
		}
		
//		if(isFromBuy){
			mProgressBarDialog=new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
//		}
//		else{
//			progressBarCart.setVisibility(View.VISIBLE);
//			addToCartTextView.setVisibility(View.GONE);
//		}
		final CartApi cartApi = new CartApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
//				if(isFromBuy){
					mProgressBarDialog.dismiss();
//				}else{
//					addToCartTextView.setVisibility(View.VISIBLE);
//					progressBarCart.setVisibility(View.GONE);
//				}
				if (object != null) {
					CartRes response = (CartRes) object;
					if(isFromBuy)
						addFragment(new FragmentUserCart());
					else
						AlertUtils.showToast(mContext, response.getMessage());
				}
				UserPreference.getInstance().incCartCount();
				((HomeActivity)mContext).refreshCartCount();
				
				double productPrice = Double.parseDouble(product.getRetailprice());
				if(!TextUtils.isEmpty(product.getSaleprice())&&!product.getSaleprice().equals("0")&&!product.getSaleprice().equals(product.getRetailprice())){
					productPrice = Double.parseDouble(product.getSaleprice());
				}
				com.google.android.gms.analytics.ecommerce.Product prod = new com.google.android.gms.analytics.ecommerce.Product()
				    .setId(product.getId())
				    .setName(product.getProductname())
				    .setCategory(product.getPrimarycategory())
				    .setBrand(product.getBrand())
			    .setPrice(productPrice);
				HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
				    .addImpression(prod, "Product Added To Cart");
			
				Tracker t = ((KikrApp) mContext.getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Add Product to Cart");
				t.send(builder.build());
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
//				if(isFromBuy){
					mProgressBarDialog.dismiss();
//				}else{
//					progressBarCart.setVisibility(View.GONE);
//					addToCartTextView.setVisibility(View.VISIBLE);
//				}
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					CartRes response = (CartRes) object;
					if(isFromBuy)
						addFragment(new FragmentUserCart());
					else
						AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}	
			}
		});
		String fromUserId=product.getFrom_user_id()==null?"":product.getFrom_user_id();
		String fromCollection=product.getFrom_collection_id()==null?"":product.getFrom_collection_id();
		cartApi.addToCart(UserPreference.getInstance().getUserID(), product.getId(), quantityTextView.getText().toString().trim(),UserPreference.getInstance().getCartID(),fromUserId,fromCollection,selectedSize,selectedColor,productLists.get(0).getSelected_values());
		cartApi.execute();
	}

	@Override
	public void setColor(String color,int position) {
		colourDialog.dismiss();
		colorTextView.setText(color);
		selectedColor=color;
		product.setSelected_color(color);
		setSizeForSelectedColor(position);
	}

	@Override
	public void setSize(String size) {
		sizeDialog.dismiss();
		sizeTextView.setText(size);
		selectedSize=size;
		product.setSelected_size(size);
	}

	@Override
	public void setQuantity(String quantity) {
		quantityDialog.dismiss();
		quantityTextView.setText(quantity);
		product.setQuantity(quantity);
	}
	
	private void addProductView() {
		final ActivityApi activityApi = new ActivityApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				
			}

			@Override
			public void handleOnFailure(ServiceException exception,
					Object object) {
				
			}
		});
		String fromUserId = product.getFrom_user_id() == null ? "" : product.getFrom_user_id();
		String fromCollection = product.getFrom_collection_id() == null ? "": product.getFrom_collection_id();
		activityApi.addProductView(UserPreference.getInstance().getUserID(), product.getId(), fromCollection);
		activityApi.execute();
	}

	@Override
	public void setOption(String option) {
		optionDialog.dismiss();
		optionTextView.setText(option);
		selectedOption=option;
		product.setOption(option);
		updateOption(option);
	}

	private void updateOption(String option) {
		final CartApi cartApi = new CartApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				
			}

			@Override
			public void handleOnFailure(ServiceException exception,
					Object object) {
				
			}
		});
		cartApi.updateproductoption(UserPreference.getInstance().getUserID(), product.getProductcart_id(), option);
		cartApi.execute();
	}

	@Override
	public void setFit(String fit) {
		fitDialog.dismiss();
		sizeTextView.setText(fit);
		selectedSize=fit;
		product.setFit(fit);
		updateFit(fit);
	}
	
	private void updateFit(String fit) {
		final CartApi cartApi = new CartApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				
			}

			@Override
			public void handleOnFailure(ServiceException exception,Object object) {
				
			}
		});
		cartApi.updateproductfit(UserPreference.getInstance().getUserID(), product.getProductcart_id(), fit);
		cartApi.execute();
	}

	@Override
	public void setOption(String option, int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addOption(ProductChildOption child) {
		// TODO Auto-generated method stub
		
	}
}

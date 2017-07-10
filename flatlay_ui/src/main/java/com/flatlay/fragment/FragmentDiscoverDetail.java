package com.flatlay.fragment;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.flatlay.BaseFragment;
import com.flatlay.KikrApp;
import com.flatlay.KikrApp.TrackerName;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.ProductPagerAdapter;
import com.flatlay.dialog.CollectionListDialog;
import com.flatlay.dialog.ProductDetailOptionDialog;
import com.flatlay.ui.ImageUI;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.api.ActivityApi;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.TwoTapApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProductChildOption;
import com.flatlaylib.bean.ProductMainOption;
import com.flatlaylib.bean.ProductRequiredOption;
import com.flatlaylib.bean.TwoTapProductDetails;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FragmentDiscoverDetail extends BaseFragment implements OnClickListener{
	private Button mCheckoutButton; 
	private View mainView;
	private Product product;
	private ViewPager productImageView;
	private ImageView viewDetailsText,likeCountTextImage;
	private TextView productTitleTextView,productRegularPriceTextView,productYourPriceTextView;
	private TextView descriptionTextView;
	private ProgressBarDialog mProgressBarDialog;
	private String selectedSize="",selectedColor="";
	private TextView brandNameTextView,addToCollectionText,likeCountText;
	private UiUpdate uiUpdate;
	private String cartid,description ;
	private ProductPagerAdapter mAdapter;
	private Handler handler = new Handler();
	private Runnable runnable;
	private int i = 0;
	private List<String> productMultipleImages = new ArrayList<String>();
	private LinearLayout loadingBar;
	private ProgressBar progressBar;
	private TextView txtMessageBar;
	private List<TwoTapProductDetails> productDetails = new ArrayList<TwoTapProductDetails>();
	private List<Product> productLists = new ArrayList<Product>();
	boolean isLoadingData = true, isSelectOptionRequired = false, isProductExist = true; 
	private ScrollView descriptionScrollView;
	RelativeLayout descriptionLayout;
	private ImageView descriptionArrow;
	private boolean isDescriptionUp = false;
	private LinearLayout imageListLayout;
	private RelativeLayout imageLayout;
	LinearLayout descriptionContainer;

	public FragmentDiscoverDetail(UiUpdate uiUpdate) {
		this.uiUpdate=uiUpdate;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.activity_discover_details, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		descriptionContainer=(LinearLayout)mainView.findViewById(R.id.descriptionContainer);
		mCheckoutButton = (Button) mainView.findViewById(R.id.checkoutButton);
		productImageView = (ViewPager) mainView.findViewById(R.id.productImageView);
		productTitleTextView = (TextView) mainView.findViewById(R.id.productTitleTextView);
		productRegularPriceTextView = (TextView) mainView.findViewById(R.id.productRegularPriceTextView);
		productYourPriceTextView = (TextView) mainView.findViewById(R.id.productYourPriceTextView);
		descriptionTextView = (TextView) mainView.findViewById(R.id.descriptionTextView);
		brandNameTextView=(TextView) mainView.findViewById(R.id.brandNameTextView);
		addToCollectionText=(TextView) mainView.findViewById(R.id.addToCollectionText);
		likeCountText=(TextView) mainView.findViewById(R.id.likeCountText);
		likeCountTextImage = (ImageView) mainView.findViewById(R.id.likeCountTextImage);
		descriptionArrow= (ImageView) mainView.findViewById(R.id.descriptionArrow);
		descriptionScrollView= (ScrollView) mainView.findViewById(R.id.descriptionScrollView);
		descriptionLayout=(RelativeLayout)mainView.findViewById(R.id.descriptionLayout);
		imageListLayout= (LinearLayout) mainView.findViewById(R.id.imageListLayout);
		imageLayout = (RelativeLayout) mainView.findViewById(R.id.imageLayout);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,CommonUtility.getDeviceHeight(mContext)-(int)mContext.getResources().getDimension(R.dimen.image_detail_height));
		imageLayout.setLayoutParams(layoutParams);
//		RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) descriptionLayout.getLayoutParams();
//		layoutParams2.setMargins(0, CommonUtility.getDeviceHeight(mContext) - (int) mContext.getResources().getDimension(R.dimen.image_detail_height), 0, 0);
//		descriptionLayout.setLayoutParams(layoutParams2);
//		descriptionArrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_gray_arrow));
		descriptionLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});



		productImageView.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					arg0.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}
		});
		loadingBar = (LinearLayout) mainView.findViewById(R.id.loadingBar);
		viewDetailsText = (ImageView) mainView.findViewById(R.id.viewDetailsWebView);
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
		addToCollectionText.setOnClickListener(this);
		likeCountTextImage.setOnClickListener(this);
		viewDetailsText.setOnClickListener(this);
		descriptionArrow.setOnClickListener(this);
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
		    //.setBrand(product.getBrand())
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
		isProductExist = true;
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

	private void setData() {
		if (productMultipleImages.size()==0 && i==0) {
			i=1;
			handler.postDelayed(runnable, 3000);
		} else{
			loadingBar.setVisibility(View.GONE);
			if (productMultipleImages.size()>0) {
				Syso.info("productMultipleImages "+productMultipleImages);
				if(productMultipleImages.size() > 1)
					productMultipleImages.remove(0);
				mAdapter = new ProductPagerAdapter(mContext, productMultipleImages,product.getProducturl(), product);
				productImageView.setAdapter(mAdapter);
				imageListLayout.addView(new ImageUI(mContext,productMultipleImages,productImageView).getView());
			}
		if (!TextUtils.isEmpty(description)) {
        	descriptionTextView.setText(Html.fromHtml(description));
		} else {
			descriptionTextView.setText(getDescription());
		}
	}
	}

	private void setDetails() {
		productRegularPriceTextView.setText(" $"+CommonUtility.getFormatedNum(product.getRetailprice()));
		if(!TextUtils.isEmpty(product.getSaleprice())&&!product.getSaleprice().equals("0")&&!product.getSaleprice().equals(product.getRetailprice())){
			productYourPriceTextView.setText(" $"+CommonUtility.getFormatedNum(product.getSaleprice()));
			productRegularPriceTextView.setPaintFlags(productRegularPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}
		productTitleTextView.setText(product.getProductname());
		productMultipleImages.add(product.getProductimageurl());
		mAdapter = new ProductPagerAdapter(mContext, productMultipleImages,product.getProducturl(), product);
		productImageView.setAdapter(mAdapter);
		descriptionTextView.setText(getDescription());
		brandNameTextView.setText(product.getMerchantname());
		likeCountText.setText(CommonUtility.getInt(product.getLike_info().getLike_count())+"");
		if(TextUtils.isEmpty(product.getLike_info().getLike_id())) {

			likeCountTextImage.setImageResource(R.drawable.ic_heart_outline_grey);
		}else {

			likeCountTextImage.setImageResource(R.drawable.ic_heart_red);

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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkoutButton:
			if(checkInternet()){
				addProductToCart(true, "1");
			}
			break;
		case R.id.descriptionArrow:
			if (isDescriptionUp) {

				isDescriptionUp=false;
				productTitleTextView.setSingleLine(true);
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) descriptionLayout.getLayoutParams();
				layoutParams.setMargins(0, CommonUtility.getDeviceHeight(mContext) - (int) mContext.getResources().getDimension(R.dimen.image_detail_height), 0, 0);

				descriptionLayout.setLayoutParams(layoutParams);
				descriptionArrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_gray_arrow));
				descriptionContainer.setVisibility(View.GONE);

				descriptionLayout.setOnTouchListener(null);
			}else{
				isDescriptionUp=true;
				productTitleTextView.setSingleLine(false);
				descriptionLayout.setOnTouchListener(null);

				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) descriptionLayout.getLayoutParams();
				layoutParams.setMargins(0, CommonUtility.getDeviceHeight(mContext) - (int) mContext.getResources().getDimension(R.dimen.description_detail_height), 0, 0);
				descriptionLayout.setLayoutParams(layoutParams);
				descriptionArrow.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_down_gray_arrow));
				descriptionContainer.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.productImageView:
			if(((HomeActivity) mContext).checkInternet() && productMultipleImages.size()==0)
				getCartId();
			break;
		case R.id.addToCollectionText:
			if(((HomeActivity) mContext).checkInternet()){
				CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, product);
				collectionListDialog.show();
			}
			break;
		case R.id.likeCountTextImage:
			if(((HomeActivity) mContext).checkInternet())
				((HomeActivity) mContext).likeInspiration(product,new UiUpdate() {
					
					@Override
					public void updateUi() {
						try{
							likeCountText.setText(CommonUtility.getInt(product.getLike_info().getLike_count()) + "");
							if(TextUtils.isEmpty(product.getLike_info().getLike_id())) {

								likeCountTextImage.setImageResource(R.drawable.ic_heart_outline_grey);
							}
							else {

								likeCountTextImage.setImageResource(R.drawable.ic_heart_red);
							}
							if(uiUpdate!=null){
								uiUpdate.updateUi();
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				});
			break;
		case R.id.viewDetailsWebView:
			ProductDetailOptionDialog productDetailOptionDialog = new ProductDetailOptionDialog(mContext,product);
			productDetailOptionDialog.show();
			break;
		default:
			break;
		}
	}

	public void addToCartFromEdit(boolean isFromBuy, String quantity){
		isSelectOptionRequired = false;
		Log.w("addToCartFromEdit",""+quantity);
		addProductToCart(isFromBuy, quantity);
	}
	
	public void addProductToCart(final boolean isFromBuy, String quantity) {
		
		if(isLoadingData)
		{
			AlertUtils.showToast(mContext, "Please wait, while required data is loading...");
			return;
		}else if(!isProductExist){
			AlertUtils.showToast(mContext, "Product is not available");
			return;
		}else if(isSelectOptionRequired){
			String i,j,k;
			//  ((HomeActivity) mContext).onBackPressed();
			i= product.getProductcart_id();
			j=i;
			k=j;

			AlertUtils.showToast(mContext, "Please select required values");
			((HomeActivity)mContext).addFragment(new FragmentEditPurchaseItem(productLists.get(0),this,isFromBuy));
			return;
		}
			mProgressBarDialog=new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
		final CartApi cartApi = new CartApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
					mProgressBarDialog.dismiss();
				if (object != null) {
					CartRes response = (CartRes) object;
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
				    //.setBrand(product.getBrand())
			    .setPrice(productPrice);
				HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
				    .addImpression(prod, "Product Added To Cart");
			
				Tracker t = ((KikrApp) mContext.getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Add Product to Cart");
				t.send(builder.build());
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
					mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					CartRes response = (CartRes) object;
						AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}	
			}
		});
		String fromUserId=product.getFrom_user_id()==null?"":product.getFrom_user_id();
		String fromCollection=product.getFrom_collection_id()==null?"":product.getFrom_collection_id();
		Log.w("FragmentDiscoverDetail",""+quantity);
		cartApi.addToCart(UserPreference.getInstance().getUserID(), product.getId(), quantity,UserPreference.getInstance().getCartID(),fromUserId,fromCollection,selectedSize,selectedColor,productLists.get(0).getSelected_values());
		cartApi.execute();
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

}

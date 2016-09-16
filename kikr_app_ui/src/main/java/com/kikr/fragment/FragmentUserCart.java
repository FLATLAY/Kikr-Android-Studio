package com.kikr.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.ProductAction;
import com.kikr.BaseFragment;
import com.kikr.KikrApp;
import com.kikr.KikrApp.TrackerName;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.CartListAdapter;
import com.kikr.dialog.CartOverLoadDialog;
import com.kikr.dialog.RemoveProductsFromCartDialog;
import com.kikr.dialog.WelcomeDialog;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.FontUtility;
import com.kikrlib.api.CartApi;
import com.kikrlib.api.CheckPointsStatusApi;
import com.kikrlib.api.TwoTapApi;
import com.kikrlib.bean.CartProduct;
import com.kikrlib.bean.CartTotalInfo;
import com.kikrlib.bean.CartTwotapData;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProductChildOption;
import com.kikrlib.bean.ProductMainOption;
import com.kikrlib.bean.ProductRequiredOption;
import com.kikrlib.bean.TwoTapProductDetails;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CartRes;
import com.kikrlib.service.res.CheckPointStatusRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class FragmentUserCart extends BaseFragment implements OnClickListener, ServiceCallback, OnItemClickListener {

	private View mainView;
	private ListView cartItemsList;
	private ProgressBarDialog mProgressBarDialog;
	private List<Product> productLists;
	private CartTotalInfo cartTotalInfo;
	private LinearLayout checkout_layout;//,subtotal_layout;
	private TextView proceed_text;//subtotalText
	private TextView helpEmptyCartText;
	private List<CartProduct> cartProducts = new ArrayList<CartProduct>();
	private String cartid;
	private Runnable runnable;
	private Handler handler = new Handler();
	private CartListAdapter cartListAdapter;
	private String status = "";
	private List<TwoTapProductDetails> productDetails = new ArrayList<TwoTapProductDetails>();
	private ProgressBar progressBarCart;
	private List<Product> productsNotAvailable = new ArrayList<Product>();
	private List<Product> productsToSelectDetails = new ArrayList<Product>();
	//	private FrameLayout loadingBar;
	private TextView txtMessageBar;
	FragmentUserCart fragmentUserCart;
	private List<CartTwotapData> cartTwotapDataList = new ArrayList<CartTwotapData>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_user_cart, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		fragmentUserCart = this;
		cartItemsList = (ListView) mainView.findViewById(R.id.cartItemsList);
		checkout_layout = (LinearLayout) mainView.findViewById(R.id.checkout_layout);
//		subtotal_layout = (LinearLayout) mainView.findViewById(R.id.subtotal_layout);
//		subtotalText = (TextView) mainView.findViewById(R.id.subtotalText);
		proceed_text = (TextView) mainView.findViewById(R.id.proceed_text);
		helpEmptyCartText = (TextView) mainView.findViewById(R.id.helpEmptyCartText);
		progressBarCart = (ProgressBar) mainView.findViewById(R.id.progressBarCart);
//		loadingBar = (FrameLayout) mainView.findViewById(R.id.loadingBar);
		txtMessageBar = (TextView) mainView.findViewById(R.id.txtMessageBar);
		txtMessageBar.setTypeface(FontUtility.setProximanovaLight(mContext));
	}

	private void getPointsStatus() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		CheckPointsStatusApi checkPointsStatusApi = new CheckPointsStatusApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnSuccess>>" + object);
				CheckPointStatusRes pointStatusRes = (CheckPointStatusRes) object;
				status = pointStatusRes.getStatus();
				getCartList();
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
			}
		});
		checkPointsStatusApi.checkcartpointstatus(UserPreference.getInstance().getUserID());
		checkPointsStatusApi.execute();
	}

	public void getCartList() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.setMessage("Contacting Merchants For Availability");
		mProgressBarDialog.setCanceledOnTouchOutside(false);
		mProgressBarDialog.setCancelable(false);
		mProgressBarDialog.show();

		final CartApi cartApi = new CartApi(this);
		cartApi.getCartList(UserPreference.getInstance().getUserID());
		cartApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				cartApi.cancel();
			}
		});
	}

	@Override
	public void handleOnSuccess(Object object) {
		try {
			//mProgressBarDialog.dismiss();
			Syso.info("In handleOnSuccess>>" + object);
			CartRes cartRes = (CartRes) object;
			productLists = cartRes.getData();
			if (cartRes.getTotal_info() != null && cartRes.getTotal_info().size() > 0)
				cartTotalInfo = cartRes.getTotal_info().get(0);
			if (productLists.size() == 0) {
				showEmptyCart();
			} else if (productLists.size() > 0) {
			//	proceedtoorder();
				ProductAction productAction = new ProductAction(ProductAction.ACTION_CHECKOUT)
						.setCheckoutStep(1);
				HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
						.setProductAction(productAction);

				Tracker t = ((KikrApp) mContext.getApplication()).getTracker(TrackerName.APP_TRACKER);
				t.setScreenName("Fragment User Cart");
				t.send(builder.build());
				UserPreference.getInstance().setCartCount(String.valueOf(productLists.size()));
				((HomeActivity) mContext).refreshCartCount();
				try { //used due to user may be press back and response returned later
					cartListAdapter = new CartListAdapter(mContext, productLists, FragmentUserCart.this, false, productDetails);
					cartItemsList.setAdapter(cartListAdapter);
				} catch (Exception e) {
					e.printStackTrace();
				}
				checkout_layout.setClickable(false);
//				loadingBar.setVisibility(View.VISIBLE);

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Random r = new Random();
						int num = r.nextInt(15 - 1) + 1;
						progressBarCart.setProgress(num);
					}
				}, 1500);

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Random r = new Random();
						int num = r.nextInt(34 - 16) + 16;
						progressBarCart.setProgress(num);
					}
				}, 2500);

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Random r = new Random();
						int num = r.nextInt(59 - 35) + 35;
						progressBarCart.setProgress(num);
					}
				}, 3500);

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Random r = new Random();
						int num = r.nextInt(65 - 60) + 60;
						progressBarCart.setProgress(num);
					}
				}, 4500);

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Random r = new Random();
						int num = r.nextInt(75 - 66) + 66;
						progressBarCart.setProgress(num);
					}
				}, 5500);

			}
			if (productLists.size() > 10) {
				CartOverLoadDialog cartOverLoadDialog = new CartOverLoadDialog(mContext);
				cartOverLoadDialog.show();
			}
			if (((HomeActivity) mContext).checkInternet())
				getCartId(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showEmptyCart() {
		try {
			if (mProgressBarDialog.isShowing())
				mProgressBarDialog.dismiss();
			LinearLayout layout = (LinearLayout) getView().findViewById(R.id.emptyCartText);
			layout.setVisibility(View.VISIBLE);
//			loadingBar.setVisibility(View.GONE);
			checkout_layout.setVisibility(View.VISIBLE);
			proceed_text.setText("Go Fill Me Up!");
			checkout_layout.setBackgroundColor(getResources().getColor(R.color.btn_gray));
			checkout_layout.setClickable(false);
//			subtotal_layout.setVisibility(View.GONE);
			cartItemsList.setVisibility(View.GONE);
			TextView textView = (TextView) getView().findViewById(R.id.noDataFoundTextView);
			textView.setText(getResources().getString(R.string.empty_cart_text));
			if (status.equals("yes")) {
				helpEmptyCartText.setVisibility(View.VISIBLE);
			} else
				helpEmptyCartText.setVisibility(View.GONE);
		} catch (NullPointerException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			CartRes response = (CartRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
			showEmptyCart();
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.checkout_layout:

				if (productLists != null && productLists.size() > 0 && productLists.size() < 11) {
					alertForSelectFields();
				} else if (productLists.size() > 0) {
					CartOverLoadDialog cartOverLoadDialog = new CartOverLoadDialog(mContext);
					cartOverLoadDialog.show();
				} else if (productLists.size() == 0) {
					showEmptyCart();
				}
				break;
			default:
				break;
		}
	}

	private void alertForSelectFields() {
		productsToSelectDetails.clear();
		for (int i = 0; i < productLists.size(); i++) {
			if (productLists.get(i).getRequiredOptions().size() > 1) {  //excluding quantity
				if (productLists.get(i).getSelected_values() != null) {
					for (int j = 0; j < productLists.get(i).getSelected_values().size(); j++) {
						if (TextUtils.isEmpty(productLists.get(i).getSelected_values().get(j).getValue())) {
							productsToSelectDetails.add(productLists.get(i));
							break;
						}
					}
				} else {
					productsToSelectDetails.add(productLists.get(i));
				}
			}
//			if (productLists.get(i).isSelectdetails()) {
//				productsToSelectDetails.add(productLists.get(i));
//			}
		}
		if (productsToSelectDetails.size() > 0) {
			CartOverLoadDialog cartOverLoadDialog = new CartOverLoadDialog(mContext, productsToSelectDetails, FragmentUserCart.this);
			cartOverLoadDialog.show();
		} else {
			getCartId(true);
		}
		proceedtoorder();
	}
	private void proceedtoorder() {
		cartListAdapter.notifyDataSetChanged();
		boolean isListEmpty = false;
		for (int i = 0; i < productLists.size(); i++) {
			if (productLists.get(i).isSelectdetails()) {
				isListEmpty = true;
				checkout_layout.setClickable(false);
				WelcomeDialog welcome = new WelcomeDialog(mContext);
				welcome.show();
				break;
			}
		}
		if (!isListEmpty) {
			checkout_layout.setClickable(true);
		}



	}
	@Override
	public void setData(Bundle bundle) {
	//	initData();
	}
	public void initData() {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (checkInternet()) {
					getPointsStatus();
				}
			}
		}, 100);

	}


	public void refreshCart() {
		setCheckoutBtn();
	}


	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void setClickListener() {
		checkout_layout.setOnClickListener(this);
		cartItemsList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		if(productDetails.size()>0){
//		for (int i = 0; i <productDetails.size(); i++) {
//			if (productLists.get(position).getProducturl().equals(productDetails.get(i).getOriginal_url())) {
//				addFragment(new FragmentEditPurchaseItem(FragmentUserCart.this,productDetails.get(i),productLists.get(position)));
//			}
//		}
//		}else{
//		addFragment(new FragmentEditPurchaseItem(productLists.get(position),FragmentUserCart.this));
//		}
	}

	private void getCartId(final boolean isProceed) {
		if (isProceed) {
			mProgressBarDialog = new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
		}
		TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("success:   " + object);

				try {
					JSONObject jsonObject = new JSONObject(object.toString());
					cartid = (String) jsonObject.get("cart_id");
					if (isProceed) {
						mProgressBarDialog.dismiss();
						Bundle b = new Bundle();
						b.putSerializable("total_info", cartTotalInfo);
						FragmentPlaceMyOrder order = new FragmentPlaceMyOrder(productLists, cartid);
						order.setArguments(b);
						addFragment(order);
					} else {
						runnable = new Runnable() {

							@Override
							public void run() {
								getStatus(cartid);
							}
						};
						handler.postDelayed(runnable, 5000);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				if (isProceed) {
					mProgressBarDialog.dismiss();
				}
			}
		});
		List<String> products = new ArrayList<String>();
		for (int i = 0; i < productLists.size(); i++) {
			products.add(productLists.get(i).getProducturl());
		}
		twoTapApi.getCartId(products);
		twoTapApi.execute();
	}

	boolean isLoading = false;

	private synchronized void getStatus(final String cart_id) {
		if (!isLoading)
			isLoading = true;
		else
			return;
		productDetails.clear();
		TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
//				mProgressBarDialog.dismiss();
				isLoading = false;
				Syso.infoFull("getStatus success:   " + object);
				try {
					JSONObject object2 = new JSONObject(object.toString());
					String message = object2.getString("message");
					if (message.equalsIgnoreCase("has_failures") || message.equalsIgnoreCase("done")) {
						getData(object);
					} else {
						getStatus(cart_id);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
//				getData(object);
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
//				mProgressBarDialog.dismiss();
				isLoading = false;
			}
		});
		twoTapApi.getCartStatus(cart_id);
		twoTapApi.execute();
	}

	protected synchronized void getData(Object object) {
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
//						if (add_to_cart.getString("status").equals("still_processing")) {
//							list.clear();
//							getStatus(cartid);
//						} else {
						Syso.info("add_to_cart   " + add_to_cart);
						Iterator data = add_to_cart.keys();

						while (data.hasNext()) {
							// loop to get the dynamic key
							String currentKey = (String) data.next(); //product md5
							Syso.info("currentKey   " + currentKey);
							// get the value of the dynamic key
							JSONObject value = add_to_cart.getJSONObject(currentKey);
							// do something here with the value...
							Syso.info("value   " + value);
							try {
								if (value.getString("status").equals("still_processing")) {
									list.clear();
									getStatus(cartid);
								} else {
//										CartTwotapData twotapData = new CartTwotapData();
//										JSONArray arr = value.getJSONArray("required_field_names");
//										List<String> fieldlist = new ArrayList<String>();
//										for (int i = 0; i < arr.length(); i++) {
//											fieldlist.add(arr.getString(i));
//										}
//										twotapData.setRequired_fields_names(fieldlist);
//										JSONObject object2 = value.getJSONObject("required_field_values");
//										String url=value.getString("original_url");
//										object2.put("original_url", url);
//										twotapData.setRequired_field_values(object2);
									value.put("site_id", currentDynamicKey);
									value.put("md5", currentKey);
									list.add(value);
//										cartTwotapDataList.add(twotapData);
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
							// loop to get the dynamic key
							String currentKey = (String) data.next();
							Syso.info("currentKey   " + currentKey);
							// get the value of the dynamic key
							JSONObject value = failed_to_add_to_cart
									.getJSONObject(currentKey);
							// do something here with the value...
							Syso.info("value   " + value);
							setFailedProduct(value.getString("original_url"));
						}
						e.printStackTrace();
					}

				}
				try {
					JSONArray array = jsonObject.getJSONArray("unknown_urls");
					for (int i = 0; i < array.length(); i++) {
						String url = array.getString(i);
						setFailedProduct(url);
					}
				} catch (Exception e) {
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

	private void setFailedProduct(String url) {
//		String url="";
//		try {
//			url = failed_to_add_to_cart.getString("original_url");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		for (int i = 0; i < productLists.size(); i++) {
			if (url.equals(productLists.get(i).getProducturl())) {
				productLists.get(i).setProductNotAvailable(true);
				cartListAdapter.notifyDataSetChanged();
			}
		}
	}

	private void setNoDetailsProduct(String url) {
		for (int i = 0; i < productLists.size(); i++) {
			if (url.equals(productLists.get(i).getProducturl())) {
				productLists.get(i).setNoDetails(true);
				cartListAdapter.notifyDataSetChanged();
			}
		}
	}

	private synchronized void fetchData(List<JSONObject> list) {
		for (int i = 0; i < list.size(); i++) {
			TwoTapProductDetails product = new TwoTapProductDetails();
			if (list.get(i) != null) {
				JSONObject value = list.get(i);
				try {
					product.setOriginal_url(value.getString("original_url"));
				} catch (JSONException e3) {
					e3.printStackTrace();
				}
				try {
					product.setSiteId(value.getString("site_id"));
					product.setMd5(value.getString("md5"));
					product.setUrl(value.getString("url"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					JSONArray arr = value.getJSONArray("required_field_names");
					List<ProductRequiredOption> requiredOptionList = new ArrayList<ProductRequiredOption>();
					for (int j = 0; j < arr.length(); j++) {
						ProductRequiredOption requiredOption = new ProductRequiredOption();
						requiredOption.setName(arr.getString(j));
						requiredOptionList.add(requiredOption);
					}
					product.setRequiredOptionList(requiredOptionList);
					JSONObject required_field_values = value.getJSONObject("required_field_values");
					product.setProductMainOptionList(getMainOptionList(required_field_values));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			productDetails.add(product);
		}
		List<String> productToSelectDetail = new ArrayList<String>();
		for (int i = 0; i < productDetails.size(); i++) {
//			if (productDetails.get(i).getColorList()!=null && productDetails.get(i).getColorList().size()>0 ||
//				productDetails.get(i).getSizeList()!=null && productDetails.get(i).getSizeList().size()>0 ||
//				productDetails.get(i).getFitList()!=null && productDetails.get(i).getFitList().size()>0 ||
//				productDetails.get(i).getColorsArray()!=null && productDetails.get(i).getColorsArray().size()>0 ||
//				productDetails.get(i).getOptionList()!=null && productDetails.get(i).getOptionList().size()>0  ) {

			List<ProductRequiredOption> list2 = productDetails.get(i).getRequiredOptionList();
			if (list2.size() > 1) {  //assuming that quantity will always present so size should be > 1 for selection.
				productToSelectDetail.add(productDetails.get(i).getOriginal_url());
				Syso.info("123456 has data>" + productDetails.get(i).getOriginal_url());
			} else {
				setNoDetailsProduct(productDetails.get(i).getOriginal_url());
				Syso.info("123456 has no data>" + productDetails.get(i).getOriginal_url());
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

		for (int i = 0; i < productToSelectDetail.size(); i++) {
			for (int j = 0; j < productLists.size(); j++) {
				if (productToSelectDetail.get(i).equals(productLists.get(j).getProducturl())) {
					try {
						productLists.get(j).setSelectdetails(true);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}

		for (int i = 0; i < productLists.size(); i++) {
			if (productLists.get(i).isSelectdetails() || productLists.get(i).isNoDetails()) {
				//do nothing
			} else {
				productLists.get(i).setProductNotAvailable(true);
			}
		}

		for (int z = 0; z < productLists.size(); z++) {
			if (productLists.get(z).isProductNotAvailable())
				productsNotAvailable.add(productLists.get(z));
		}
		for (int z = 0; z < productLists.size(); z++) {
			if (productLists.get(z).isSelectdetails()) {
				if (productLists.get(z).getSelected_values() != null && productLists.get(z).getSelected_values().size() > 0) {
					productLists.get(z).setSelectdetails(false);
//					productsToSelectDetails.add(productLists.get(z));
				} else {
					if (productLists.get(z).getRequiredOptions().size() > 1) {
						productsToSelectDetails.add(productLists.get(z));
						productLists.get(z).setSelectdetails(true);
					} else {
						productLists.get(z).setSelectdetails(false);
					}
				}
			}
		}
		cartListAdapter.notifyDataSetChanged();
		if (productsNotAvailable.size() > 0 && fragmentUserCart != null && fragmentUserCart.isVisible()) {
			RemoveProductsFromCartDialog dialog = new RemoveProductsFromCartDialog(mContext, productsNotAvailable, FragmentUserCart.this);
			dialog.show();
		} else if (productsNotAvailable.size() == 0 && productsToSelectDetails.size() > 0 && fragmentUserCart != null && fragmentUserCart.isVisible()) {
			CartOverLoadDialog dialog2 = new CartOverLoadDialog(mContext, productsToSelectDetails, FragmentUserCart.this);
			dialog2.show();
		}
		cartListAdapter.notifyDataSetChanged();
		Syso.info("abcd:  " + productDetails + "   abcd:  " + productDetails.size());

//		loadingBar.setVisibility(View.GONE);
		checkout_layout.setVisibility(View.VISIBLE);

		mProgressBarDialog.dismiss();
		setCheckoutBtn();
	}

	private void setCheckoutBtn() {
		cartListAdapter.notifyDataSetChanged();
		boolean isListEmpty = false;
		for (int i = 0; i < productLists.size(); i++) {
			if (productLists.get(i).isSelectdetails()) {
				isListEmpty = true;
				break;
			}
		}
		if (!isListEmpty) {
			checkout_layout.setBackgroundColor(getResources().getColor(R.color.header_background));
			checkout_layout.setClickable(true);
		}
		//checkout_layout.setClickable(true);


	}

	private List<ProductMainOption> getMainOptionList(JSONObject optJSONObject) {
		Iterator data = optJSONObject.keys();
		List<ProductMainOption> productMainOptionList = new ArrayList<ProductMainOption>();
		try {
			while (data.hasNext()) {
				// loop to get the dynamic key
				String currentKey = (String) data.next();
				ProductMainOption mainOption = new ProductMainOption();
				JSONArray mainOptionArray = optJSONObject.getJSONArray(currentKey);
				mainOption.setName(currentKey);
				List<ProductChildOption> optionList = new ArrayList<ProductChildOption>();
				for (int j = 0; j < mainOptionArray.length(); j++) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productMainOptionList;
	}

	public void removeProducts() {
		for (int i = 0; i < productsNotAvailable.size(); i++) {
			removeFromCart(productsNotAvailable.get(i).getProductcart_id());
		}
		if (productLists.size() == 0) {
			showEmptyCart();
		} else if (productsToSelectDetails.size() > 0 && fragmentUserCart != null && fragmentUserCart.isVisible()) {
			CartOverLoadDialog dialog2 = new CartOverLoadDialog(mContext, productsToSelectDetails, FragmentUserCart.this);
			dialog2.show();
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			cartListAdapter.notifyDataSetChanged();
			boolean isListEmpty = false;
			for (int i = 0; i < productLists.size(); i++) {
				if (productLists.get(i).isSelectdetails()) {
					isListEmpty = true;
					break;
				}
			}
			if (!isListEmpty) {
				checkout_layout.setBackgroundColor(getResources().getColor(R.color.header_background));
			}
		}
	}

	public void removeFromCart(final String id) {

		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();

		final CartApi cartApi = new CartApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				for (int i = 0; i < productLists.size(); i++) {
					if (id.equals(productLists.get(i).getProductcart_id())) {
						productLists.remove(i);
					}
				}
				UserPreference.getInstance().decCartCount();
				((HomeActivity) mContext).refreshCartCount();
				cartListAdapter.notifyDataSetChanged();
				if (productLists.size() == 0) {
					showEmptyCart();
				}
				mProgressBarDialog.dismiss();
				addFragment(new CartFragmentTab());
//				AlertUtils.showToast(mContext, "Product removed successfully");


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
		cartApi.removeFromCart(UserPreference.getInstance().getUserID(), id);
		cartApi.execute();
	}
}
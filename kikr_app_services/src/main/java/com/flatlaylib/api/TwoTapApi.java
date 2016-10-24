package com.flatlaylib.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.flatlaylib.bean.CartProduct;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.AbsService;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.Constants.WebConstants;
import com.flatlaylib.utils.Syso;

public class TwoTapApi extends AbsService {

	private String requestValue;
	private String requestType;

	public TwoTapApi(ServiceCallback serviceCallback) {
		super();
		this.mServiceCallback = serviceCallback;
	}

	public void getCartId(List<String> products) {
		this.METHOD_NAME = "/cart?public_token=c0b4e971010a2618f9075744336db6";
		requestType = WebConstants.HTTP_METHOD_POST;
		String value="";
		for(int i=0;i<products.size();i++){
			value+="\""+products.get(i)+"\"";
			if(i!=products.size()-1)
				value+=",";
		}
		String finalValue="{\"products\":["+value+"]}";
		requestValue = finalValue;
	}
	
//	public void getCartEstimates(List<CartProduct> cartProducts,String cart_id) {
//		this.METHOD_NAME = "/cart/estimates?public_token=c0b4e971010a2618f9075744336db6";
//		requestType = WebConstants.HTTP_METHOD_POST;
//		String value="";
//		for (int i = 0; i < cartProducts.size(); i++) {
//			value += "\""+cartProducts.get(i).getSite_id() + "\":" + "{\"addToCart\":"
//					+ "{" + getMD5(cartProducts.get(i))
//					+ "},\"noauthCheckout\":" + "{\"shipping_zip\":\""
//					+ cartProducts.get(i).getShipping_zip()
//					+ "\",\"shipping_city\":\""
//					+ cartProducts.get(i).getShipping_city()
//					+ "\",\"shipping_state\":\""
//					+ cartProducts.get(i).getShipping_state() + "\"},"
//					+ "\"shipping_option\":\""
//					+ cartProducts.get(i).getShipping_option() + "\"}";
//			if (i != cartProducts.size() - 1)
//				value += ",";
//		}
//		String finalValue = "{\"cart_id\":\""+ cart_id +"\",\"fields_input\":{" +value+"}}";
//		requestValue = finalValue;
//	}
	
	private String getMD5(CartProduct cartProduct) {
		String value = "";
		if (!TextUtils.isEmpty(cartProduct.getPRODUCT_MD5())) {
			value = "\""+cartProduct.getPRODUCT_MD5()+ "\":"
					+ "{\"size\":\"" + cartProduct.getSize() + "\"}";
		}else{
			for (int i = 0; i < cartProduct.getMd5().size(); i++) {
				value += "\""+cartProduct.getMd5().get(i) + "\":{\"size\":\"" + getSize(cartProduct.getSizes().get(i)) + "\"}";
				if (i!=cartProduct.getMd5().size()-1) {
					value += ",";
				}
			}
		}
		return value;
	}

	private String getSize(String string) {
		if (!TextUtils.isEmpty(string)) {
			return string;
		} else {
			return "";
		}
	}

	public void getCartStatus(String cartid) {
		this.METHOD_NAME = "/cart/status?public_token=c0b4e971010a2618f9075744336db6&cart_id="+cartid;
		this.requestType = WebConstants.HTTP_METHOD_GET;
	}
	
//	public void purchaseApi(List<CartProduct> cartProducts,String cart_id,List<String> products) {
//		this.METHOD_NAME = "/purchase?public_token=c0b4e971010a2618f9075744336db6";
//		requestType = WebConstants.HTTP_METHOD_POST;
//		String value="";
//		for (int i = 0; i < cartProducts.size(); i++) {
//			value += "\""+cartProducts.get(i).getSite_id() + "\":" + "{\"addToCart\":"
//					+ "{" + getMD5ForPurchase(cartProducts.get(i))
//					+ "},\"noauthCheckout\":" + "{\"shipping_zip\":\""
//					+ cartProducts.get(i).getShipping_zip()
//					+ "\",\"shipping_city\":\""
//					+ cartProducts.get(i).getShipping_city()
//					+ "\",\"email\":\""
//					+ cartProducts.get(i).getEmail()
//					+ "\",\"shipping_title\":\""
//					+ cartProducts.get(i).getShipping_title()
//					+ "\",\"shipping_first_name\":\""
//					+ cartProducts.get(i).getShipping_first_name()
//					+ "\",\"shipping_last_name\":\""
//					+ cartProducts.get(i).getShipping_last_name()
//					+ "\",\"shipping_address\":\""
//					+ cartProducts.get(i).getShipping_address()
//					+ "\",\"shipping_country\":\""
//					+ cartProducts.get(i).getShipping_country()
//					+ "\",\"shipping_telephone\":\""
//					+ cartProducts.get(i).getShipping_telephone()
//					+ "\",\"billing_title\":\""
//					+ cartProducts.get(i).getBilling_title()
//					+ "\",\"billing_first_name\":\""
//					+ cartProducts.get(i).getBilling_first_name()
//					+ "\",\"billing_last_name\":\""
//					+ cartProducts.get(i).getBilling_last_name()
//					+ "\",\"billing_address\":\""
//					+ cartProducts.get(i).getBilling_address()
//					+ "\",\"billing_city\":\""
//					+ cartProducts.get(i).getBilling_city()
//					+ "\",\"billing_state\":\""
//					+ cartProducts.get(i).getBilling_state()
//					+ "\",\"billing_country\":\""
//					+ cartProducts.get(i).getBilling_country()
//					+ "\",\"billing_zip\":\""
//					+ cartProducts.get(i).getBilling_zip()
//					+ "\",\"billing_telephone\":\""
//					+ cartProducts.get(i).getBilling_telephone()
//					+ "\",\"card_type\":\""
//					+ cartProducts.get(i).getCard_type()
//					+ "\",\"card_number\":\""
//					+ cartProducts.get(i).getCard_number()
//					+ "\",\"card_name\":\""
//					+ cartProducts.get(i).getCard_name()
//					+ "\",\"expiry_date_year\":\""
//					+ cartProducts.get(i).getExpiry_date_year()
//					+ "\",\"expiry_date_month\":\""
//					+ cartProducts.get(i).getExpiry_date_month()
//					+ "\",\"cvv\":\""
//					+ cartProducts.get(i).getCvv()
//					+ "\",\"shipping_state\":\""
//					+ cartProducts.get(i).getShipping_state() + "\"}}";
//			if (i != cartProducts.size() - 1)
//				value += ",";
//		}
//		String productValue="";
//		for(int i=0;i<products.size();i++){
//			productValue+="\""+products.get(i)+"\"";
//			if(i!=products.size()-1)
//				productValue+=",";
//		}
//		String finalValue = "{\"cart_id\":\"" + cart_id
//				+ "\",\"fields_input\":{" + value + "},\"products\":["
//				+ productValue + "],\"affiliate_links\":{"
//				+ getAffiliateForPurchase(cartProducts) + "}"
//				+ ",\"confirm\":{\"method\":\"http\",\"http_finished_url\":"
//				+ "\""+Constants.WebConstants.DEFAULT_IP+"twotapcallbackhttp_finished_url"
//				+ "\",\"http_confirm_url\":"
//				+ "\""+Constants.WebConstants.DEFAULT_IP+"twotapcallbackhttp_confirm_url" + "\"}}";
//		requestValue = finalValue;
//		Syso.infoFull(finalValue);
//	}
//	
	private String getMD5ForPurchase(CartProduct cartProduct) {
		String value = "";
		if (!TextUtils.isEmpty(cartProduct.getPRODUCT_MD5())) {
			value = "\""+cartProduct.getPRODUCT_MD5()+ "\":"
					+ "{" +
					"\"quantity\":\"" + cartProduct.getQuantity() + "\"" +
					getSizeForProduct(cartProduct.getSize())+
					getcolorForProduct(cartProduct.getColor())+
					getOptionForProduct(cartProduct.getOption())+
					getFitForProduct(cartProduct.getFit())+
							"}";
		}else{
			for (int i = 0; i < cartProduct.getMd5().size(); i++) {
				value += "\""+cartProduct.getMd5().get(i) + "\":{" +
						"\"quantity\":\"" + getSize(cartProduct.getQuantities().get(i)) + "\"" +
						getSizeForProduct(cartProduct.getSizes().get(i)) +
						getcolorForProduct(cartProduct.getColorsList().get(i))  +
						getOptionForProduct(cartProduct.getOptionList().get(i))  +
						getFitForProduct(cartProduct.getFitList().get(i))  +
						"}";
				if (i!=cartProduct.getMd5().size()-1) {
					value += ",";
				}
			}
		}
		return value;
	}
	
	private String getcolorForProduct(String color) {
		if (!TextUtils.isEmpty(color)) {
			return ",\"color\":\"" + color + "\"";
		} else
			return "";
	}

	private String getSizeForProduct(String size) {
		if (!TextUtils.isEmpty(size)) {
			return ",\"size\":\"" + size + "\"";
		} else
			return "";
	}
	
	private String getFitForProduct(String fit) {
		if (!TextUtils.isEmpty(fit)) {
			return ",\"fit\":\"" + fit + "\"";
		} else
			return "";
	}
	
	private String getOptionForProduct(String option) {
		if (!TextUtils.isEmpty(option)) {
			return ",\"option\":\"" + option + "\"";
		} else
			return "";
	}

	private String getAffiliateForPurchase(HashMap<String, List<Product>> cartList) {
		String value = "";
		int i = -1;
		for (String key : cartList.keySet()) {
			i++;
			List<Product> list = cartList.get(key);
			if (list.size() == 1) {
				value += "\"" + list.get(0).getSiteId() + "\":\""
						+ list.get(0).getAffiliateurl() + "\"";
			} else {
				value += "\"" + list.get(0).getSiteId() + "\":{"
						+ getList(list) + "}";
			}
			if (i != cartList.size() - 1) {
				value += ",";
			}
		}
		Syso.info("abcd:  " + value);
		return value;
	}
	
	private String getList(List<Product> list) {
		String value="";
		for (int j = 0; j < list.size(); j++) {
			value += "\""+list.get(j).getMd5() + "\":\"" + getSize(list.get(j).getAffiliateurl()) + "\"";
			if (j!=list.size()-1) {
				value += ",";
			}
		}
		return value;
	}

	private String getAffiliateForPurchase(List<CartProduct> cartProducts) {
		String value = "";
		for (int i = 0; i < cartProducts.size(); i++) {
			if (!TextUtils.isEmpty(cartProducts.get(i).getAffiliateUrl())) {
				value += "\"" + cartProducts.get(i).getSite_id() + "\":\""
						+ cartProducts.get(i).getAffiliateUrl() + "\"";
			} else {
				value += "\"" + cartProducts.get(i).getSite_id() + "\":{"
						+ getList(cartProducts.get(i)) + "}";
			}
			if (i != cartProducts.size() - 1) {
				value += ",";
			}
		}
		Syso.info("abcd:  " + value);
		return value;
	}

	private String getList(CartProduct cartProduct){
		String value="";
		for (int j = 0; j < cartProduct.getAffiliateUrlList().size(); j++) {
			value += "\""+cartProduct.getMd5().get(j) + "\":\"" + getSize(cartProduct.getAffiliateUrlList().get(j)) + "\"";
			if (j!=cartProduct.getMd5().size()-1) {
				value += ",";
			}
		}
		return value;
	}
	
	public void confirmPurchase(String purchase_id) {
		this.METHOD_NAME = "/purchase/confirm?private_token=735e119963888568752d36dc1fb6be4ef9de5d0143c2c05d5f";
		requestType = WebConstants.HTTP_METHOD_POST;
		String finalValue="{\"purchase_id\":\""+purchase_id+"\"}";
		requestValue = finalValue;
	}
	
	public void purchaseStatus(String purchase_id) {
		requestType = WebConstants.HTTP_METHOD_GET;
		this.METHOD_NAME = "/purchase/status?public_token=c0b4e971010a2618f9075744336db6&purchase_id="+purchase_id;
	}
	
	@Override
	public String getActionName() {
		return METHOD_NAME;
	}

	@Override
	public String getHost() {
		return "https://api.twotap.com/v1.0";
	}
	
	@Override
	public String getMethod() {
		return requestType;
	}

	@Override
	public List<NameValuePair> getNameValueRequest() {
		return null;
	}

	@Override
	public String getJsonRequest() {
		try {
//			JSONArray array = new JSONArray(requestValue);
			return requestValue;
		} catch (Exception e) {
			e.printStackTrace();
			JSONArray array = new JSONArray();
			return array.toString();
		}
	}

	@Override
	protected void processResponse(String response) {
		Syso.info("In RegisterUserApi processResponse>>" + response);
		try {
			isValidResponse = true;
			serviceResponse = response;
			TwoTapRequestResApi api = new TwoTapRequestResApi(new ServiceCallback() {
				@Override
				public void handleOnSuccess(Object object) {}
				
				@Override
				public void handleOnFailure(ServiceException exception, Object object) {}
			});
			api.updateTwoTapRequest(UserPreference.getInstance().getUserID(), requestValue, response, METHOD_NAME);
			api.execute();
		} catch (JsonParseException e) {
			Syso.error(e);
			isValidResponse = false;
		} catch (NullPointerException e) {
			Syso.error(e);
			isValidResponse = false;
		}
	}

	public void purchaseApi2(String cartid,HashMap<String, List<Product>> cartList,CartProduct cardAndShippingDetail) {
		this.METHOD_NAME = "/purchase?public_token=c0b4e971010a2618f9075744336db6";
		requestType = WebConstants.HTTP_METHOD_POST;
		String value="";
		int size=-1;
		for(String siteId : cartList.keySet()){
			size++;
			value += "\""+siteId + "\":" + "{\"addToCart\":"
					+ "{" + getMD5ForPurchase(cartList.get(siteId))
					+ "},\"noauthCheckout\":" + "{\"shipping_zip\":\""
					+ cardAndShippingDetail.getShipping_zip()
					+ "\",\"shipping_city\":\""
					+ cardAndShippingDetail.getShipping_city()
					+ "\",\"email\":\""
					+ cardAndShippingDetail.getEmail()
					+ "\",\"shipping_title\":\""
					+ cardAndShippingDetail.getShipping_title()
					+ "\",\"shipping_first_name\":\""
					+ cardAndShippingDetail.getShipping_first_name()
					+ "\",\"shipping_last_name\":\""
					+ cardAndShippingDetail.getShipping_last_name()
					+ "\",\"shipping_address\":\""
					+ cardAndShippingDetail.getShipping_address()
					+ "\",\"shipping_country\":\""
					+ cardAndShippingDetail.getShipping_country()
					+ "\",\"shipping_telephone\":\""
					+ cardAndShippingDetail.getShipping_telephone()
					+ "\",\"billing_title\":\""
					+ cardAndShippingDetail.getBilling_title()
					+ "\",\"billing_first_name\":\""
					+ cardAndShippingDetail.getBilling_first_name()
					+ "\",\"billing_last_name\":\""
					+ cardAndShippingDetail.getBilling_last_name()
					+ "\",\"billing_address\":\""
					+ cardAndShippingDetail.getBilling_address()
					+ "\",\"billing_city\":\""
					+ cardAndShippingDetail.getBilling_city()
					+ "\",\"billing_state\":\""
					+ cardAndShippingDetail.getBilling_state()
					+ "\",\"billing_country\":\""
					+ cardAndShippingDetail.getBilling_country()
					+ "\",\"billing_zip\":\""
					+ cardAndShippingDetail.getBilling_zip()
					+ "\",\"billing_telephone\":\""
					+ cardAndShippingDetail.getBilling_telephone()
					+ "\",\"card_type\":\""
					+ cardAndShippingDetail.getCard_type()
					+ "\",\"card_number\":\""
					+ cardAndShippingDetail.getCard_number()
					+ "\",\"card_name\":\""
					+ cardAndShippingDetail.getCard_name()
					+ "\",\"expiry_date_year\":\""
					+ cardAndShippingDetail.getExpiry_date_year()
					+ "\",\"expiry_date_month\":\""
					+ cardAndShippingDetail.getExpiry_date_month()
					+ "\",\"cvv\":\""
					+ cardAndShippingDetail.getCvv()
					+ "\",\"shipping_state\":\""
					+ cardAndShippingDetail.getShipping_state() + "\"}}";
			if (size != cartList.keySet().size() - 1)
				value += ",";
		}
		String productValue="";
		List<String> products = getProductUrlList(cartList);
		for(int i=0;i<products.size();i++){
			productValue+="\""+products.get(i)+"\"";
			if(i!=products.size()-1)
				productValue+=",";
		}
		
		String AfflietUrl=getAffiliateForPurchase(cartList);
//		List<String> AfflietUrlList = getAfflietUrlList(cartList);
//		for(int i=0;i<AfflietUrlList.size();i++){
//			AfflietUrl+="\""+AfflietUrlList.get(i)+"\"";
//			if(i!=AfflietUrlList.size()-1)
//				AfflietUrl+=",";
//		}
		
		
		String finalValue = "{\"cart_id\":\"" + cartid
				+ "\",\"fields_input\":{" + value + "},\"products\":["
				+ productValue + "],\"affiliate_links\":{"
				+ AfflietUrl + "}"
				+ ",\"confirm\":{\"method\":\"http\",\"http_finished_url\":"
				+ "\""+Constants.WebConstants.DEFAULT_IP+"twotapcallbackhttp_finished_url"
				+ "\",\"http_confirm_url\":"
				+ "\""+Constants.WebConstants.DEFAULT_IP+"twotapcallbackhttp_confirm_url" + "\"}}";
		requestValue = finalValue;
		Syso.infoFull(finalValue);
	}

	private List<String> getProductUrlList(HashMap<String, List<Product>> cartList) {
		List<String> list = new ArrayList<String>();
		for(String key : cartList.keySet()){
			List<Product> list2 = cartList.get(key);
			for(Product product : list2){
				list.add(product.getUrl());
			}
		}
		return list;
	}
	
	private List<String> getAfflietUrlList(HashMap<String, List<Product>> cartList) {
		List<String> list = new ArrayList<String>();
		for(String key : cartList.keySet()){
			List<Product> list2 = cartList.get(key);
			for(Product product : list2){
				list.add(product.getAffiliateurl());
			}
		}
		return list;
	}

	private String getMD5ForPurchase(List<Product> list) {
		String value = "";
			for (int i = 0; i < list.size(); i++) {
				Product product = list.get(i);
				value += "\""+product.getMd5() + "\":{" +
						"\"quantity\":\"" + product.getQuantity() + "\"" +
						getOtherData(product) +"}";
				if (i!=list.size()-1) {
					value += ",";
				}
			}
		return value;
	}

	private String getOtherData(Product product) {
		String allValue = "";
		if(product.getSelected_values()!=null){
			for(int i=0;i<product.getSelected_values().size();i++){
				String name = product.getSelected_values().get(i).getName();
				String value = product.getSelected_values().get(i).getValue();
				allValue =allValue+",\""+name+"\":\"" + value + "\"";
			}
		}
		return allValue;
	}

	public void getCartEstimates(HashMap<String, List<Product>> cartList, String cartid, CartProduct cardAndShippingDetail) {
		this.METHOD_NAME = "/cart/estimates?public_token=c0b4e971010a2618f9075744336db6";
		requestType = WebConstants.HTTP_METHOD_POST;
		String value="";
		int size=-1;
		for(String siteId : cartList.keySet()){
			size++;
			value += "\""+siteId + "\":" + "{\"addToCart\":"
					+ "{" + getMD5ForPurchase(cartList.get(siteId))
					+ "},\"noauthCheckout\":" + "{\"shipping_zip\":\""
					+ cardAndShippingDetail.getShipping_zip()
					+ "\",\"shipping_city\":\""
					+ cardAndShippingDetail.getShipping_city()
					+ "\",\"shipping_state\":\""
					+ cardAndShippingDetail.getShipping_state() + "\"},"
					+ "\"shipping_option\":\""
					+ getOption(cartList.get(siteId)) + "\"}";
			if (size != cartList.size() - 1)
				value += ",";
		}
		String finalValue = "{\"cart_id\":\""+ cartid +"\",\"fields_input\":{" +value+"}}";
		requestValue = finalValue;
	}

	private String getOption(List<Product> list) {
		if(list.size()>0){
			return list.get(0).getShipping_option();
		}
		return "cheapest";
	}

	private String getOption(CartProduct cardAndShippingDetail) {
		// TODO Auto-generated method stub
		return cardAndShippingDetail.getShipping_option()==null?"cheapest":cardAndShippingDetail.getShipping_option();
	}
}

package com.kikrlib.bean;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

	/**
	 * 
	 */
	String id;
	String productname;
	String skunumber;
	String primarycategory;
	String secondarycategory;
	String producturl;
	String productimageurl;
	String shortproductdesc;
	String longproductdesc;
	String discount;
	String discounttype;
	String saleprice;
	String retailprice;
	//String brand;
	String shippingcost;
	String keywords;
	String manufacturename;
	String availability;
	String shippinginfo;
	String pixel;
	String merchantid;
	String merchantname;

	String quantity;
	String color;
	String cart_id;

	String from_user_id;
	String from_collection_id;

	String productcart_id;
	String tbl_product_id;

	String size;
	String selected_size;
	String selected_color;

	// String liked_id;
	// String like_count;
	String brand_image;
	LikeInfo like_info;
	String affiliateurl;
	String option;
	String fit;
	boolean productNotAvailable = false;
	boolean selectdetails = false;
	boolean noDetails = false;
	boolean isFreeShipping = false;
	String siteId;
	String shipping_option = "cheapest";
	String md5;
	String url;
	String affiliateurlforsharing;
	String views;
	String buys;

	List<ProductRequiredOption> selected_values;
	List<ProductRequiredOption> requiredOptions;
	List<ProductMainOption> productMainOptionList;

	public String getViews() {
		return views;
	}

	public void setViews(String views) {
		this.views = views;
	}

	public String getBuys() {
		return buys;
	}

	public void setBuys(String buys) {
		this.buys = buys;
	}

	public String getAffiliateurlforsharing() {
		return affiliateurlforsharing;
	}

	public void setAffiliateurlforsharing(String affiliateurlforsharing) {
		this.affiliateurlforsharing = affiliateurlforsharing;
	}

	/**
	 * @return the shipping_option
	 */
	public String getShipping_option() {
		return shipping_option;
	}

	/**
	 * @param shipping_option
	 *            the shipping_option to set
	 */
	public void setShipping_option(String shipping_option) {
		this.shipping_option = shipping_option;
	}

	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId
	 *            the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	/**
	 * @return the isFreeShipping
	 */
	public boolean isFreeShipping() {
		return isFreeShipping;
	}

	/**
	 * @param isFreeShipping
	 *            the isFreeShipping to set
	 */
	public void setFreeShipping(boolean isFreeShipping) {
		this.isFreeShipping = isFreeShipping;
	}

	public boolean isNoDetails() {
		return noDetails;
	}

	public void setNoDetails(boolean noDetails) {
		this.noDetails = noDetails;
	}

	/**
	 * @return the selectdetails
	 */
	public boolean isSelectdetails() {
		return selectdetails;
	}

	/**
	 * @param selectdetails
	 *            the selectdetails to set
	 */
	public void setSelectdetails(boolean selectdetails) {
		this.selectdetails = selectdetails;
	}

	/**
	 * @return the productNotAvailable
	 */
	public boolean isProductNotAvailable() {
		return productNotAvailable;
	}

	/**
	 * @param productNotAvailable
	 *            the productNotAvailable to set
	 */
	public void setProductNotAvailable(boolean productNotAvailable) {
		this.productNotAvailable = productNotAvailable;
	}

	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param option
	 *            the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * @return the fit
	 */
	public String getFit() {
		return fit;
	}

	/**
	 * @param fit
	 *            the fit to set
	 */
	public void setFit(String fit) {
		this.fit = fit;
	}

	/**
	 * @return the affiliateurl
	 */
	public String getAffiliateurl() {
		return affiliateurl;
	}

	/**
	 * @param affiliateurl
	 *            the affiliateurl to set
	 */
	public void setAffiliateurl(String affiliateurl) {
		this.affiliateurl = affiliateurl;
	}

	/**
	 * @return the like_info
	 */
	public LikeInfo getLike_info() {
		return like_info;
	}

	/**
	 * @param like_info
	 *            the like_info to set
	 */
	public void setLike_info(LikeInfo like_info) {
		this.like_info = like_info;
	}

	/**
	 * @return the brand_image
	 */
	public String getBrand_image() {
		return brand_image;
	}

	/**
	 * @param brand_image
	 *            the brand_image to set
	 */
	public void setBrand_image(String brand_image) {
		this.brand_image = brand_image;
	}

	/**
	 * @return the liked_id
	 */

	/**
	 * @return the selected_size
	 */
	public String getSelected_size() {
		return selected_size;
	}

	/**
	 * @param selected_size
	 *            the selected_size to set
	 */
	public void setSelected_size(String selected_size) {
		this.selected_size = selected_size;
	}

	/**
	 * @return the selected_color
	 */
	public String getSelected_color() {
		return selected_color;
	}

	/**
	 * @param selected_color
	 *            the selected_color to set
	 */
	public void setSelected_color(String selected_color) {
		this.selected_color = selected_color;
	}

	/**
	 * @return the from_user_id
	 */
	public String getFrom_user_id() {
		return from_user_id;
	}

	/**
	 * @return the productcart_id
	 */
	public String getProductcart_id() {
		return productcart_id;
	}

	/**
	 * @param productcart_id
	 *            the productcart_id to set
	 */
	public void setProductcart_id(String productcart_id) {
		this.productcart_id = productcart_id;
	}

	/**
	 * @return the tbl_product_id
	 */
	public String getTbl_product_id() {
		return tbl_product_id;
	}

	/**
	 * @param tbl_product_id
	 *            the tbl_product_id to set
	 */
	public void setTbl_product_id(String tbl_product_id) {
		this.tbl_product_id = tbl_product_id;
	}

	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}

	/**
	 * @param from_user_id
	 *            the from_user_id to set
	 */
	public void setFrom_user_id(String from_user_id) {
		this.from_user_id = from_user_id;
	}

	/**
	 * @return the from_collection_id
	 */
	public String getFrom_collection_id() {
		return from_collection_id;
	}

	/**
	 * @param from_collection_id
	 *            the from_collection_id to set
	 */
	public void setFrom_collection_id(String from_collection_id) {
		this.from_collection_id = from_collection_id;
	}

	/**
	 * @return the cart_id
	 */
	public String getCart_id() {
		return cart_id;
	}

	/**
	 * @param cart_id
	 *            the cart_id to set
	 */
	public void setCart_id(String cart_id) {
		this.cart_id = cart_id;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the productname
	 */
	public String getProductname() {
		return productname;
	}

	/**
	 * @param productname
	 *            the productname to set
	 */
	public void setProductname(String productname) {
		this.productname = productname;
	}

	/**
	 * @return the skunumber
	 */
	public String getSkunumber() {
		return skunumber;
	}

	/**
	 * @param skunumber
	 *            the skunumber to set
	 */
	public void setSkunumber(String skunumber) {
		this.skunumber = skunumber;
	}

	/**
	 * @return the primarycategory
	 */
	public String getPrimarycategory() {
		return primarycategory;
	}

	/**
	 * @param primarycategory
	 *            the primarycategory to set
	 */
	public void setPrimarycategory(String primarycategory) {
		this.primarycategory = primarycategory;
	}

	/**
	 * @return the secondarycategory
	 */
	public String getSecondarycategory() {
		return secondarycategory;
	}

	/**
	 * @param secondarycategory
	 *            the secondarycategory to set
	 */
	public void setSecondarycategory(String secondarycategory) {
		this.secondarycategory = secondarycategory;
	}

	/**
	 * @return the producturl
	 */
	public String getProducturl() {
		return producturl;
	}

	/**
	 * @param producturl
	 *            the producturl to set
	 */
	public void setProducturl(String producturl) {
		this.producturl = producturl;
	}

	/**
	 * @return the productimageurl
	 */
	public String getProductimageurl() {
		return productimageurl;
	}

	/**
	 * @param productimageurl
	 *            the productimageurl to set
	 */
	public void setProductimageurl(String productimageurl) {
		this.productimageurl = productimageurl;
	}

	/**
	 * @return the shortproductdesc
	 */
	public String getShortproductdesc() {
		return shortproductdesc;
	}

	/**
	 * @param shortproductdesc
	 *            the shortproductdesc to set
	 */
	public void setShortproductdesc(String shortproductdesc) {
		this.shortproductdesc = shortproductdesc;
	}

	/**
	 * @return the longproductdesc
	 */
	public String getLongproductdesc() {
		return longproductdesc;
	}

	/**
	 * @param longproductdesc
	 *            the longproductdesc to set
	 */
	public void setLongproductdesc(String longproductdesc) {
		this.longproductdesc = longproductdesc;
	}

	/**
	 * @return the discount
	 */
	public String getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            the discount to set
	 */
	public void setDiscount(String discount) {
		this.discount = discount;
	}

	/**
	 * @return the discounttype
	 */
	public String getDiscounttype() {
		return discounttype;
	}

	/**
	 * @param discounttype
	 *            the discounttype to set
	 */
	public void setDiscounttype(String discounttype) {
		this.discounttype = discounttype;
	}

	/**
	 * @return the saleprice
	 */
	public String getSaleprice() {
		return saleprice;
	}

	/**
	 * @param saleprice
	 *            the saleprice to set
	 */
	public void setSaleprice(String saleprice) {
		this.saleprice = saleprice;
	}

	/**
	 * @return the retailprice
	 */
	public String getRetailprice() {
		return retailprice;
	}

	/**
	 * @param retailprice
	 *            the retailprice to set
	 */
	public void setRetailprice(String retailprice) {
		this.retailprice = retailprice;
	}

	/**
	 * @return the brand
	 */
//	public String getBrand() {
//		return brand;
//	}
//
//	/**
//	 * @param brand
//	 *            the brand to set
//	 */
//	public void setBrand(String brand) {
//		this.brand = brand;
//	}

	/**
	 * @return the shippingcost
	 */
	public String getShippingcost() {
		return shippingcost;
	}

	/**
	 * @param shippingcost
	 *            the shippingcost to set
	 */
	public void setShippingcost(String shippingcost) {
		this.shippingcost = shippingcost;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * @return the manufacturename
	 */
	public String getManufacturename() {
		return manufacturename;
	}

	/**
	 * @param manufacturename
	 *            the manufacturename to set
	 */
	public void setManufacturename(String manufacturename) {
		this.manufacturename = manufacturename;
	}

	/**
	 * @return the availability
	 */
	public String getAvailability() {
		return availability;
	}

	/**
	 * @param availability
	 *            the availability to set
	 */
	public void setAvailability(String availability) {
		this.availability = availability;
	}

	/**
	 * @return the shippinginfo
	 */
	public String getShippinginfo() {
		return shippinginfo;
	}

	/**
	 * @param shippinginfo
	 *            the shippinginfo to set
	 */
	public void setShippinginfo(String shippinginfo) {
		this.shippinginfo = shippinginfo;
	}

	/**
	 * @return the pixel
	 */
	public String getPixel() {
		return pixel;
	}

	/**
	 * @param pixel
	 *            the pixel to set
	 */
	public void setPixel(String pixel) {
		this.pixel = pixel;
	}

	/**
	 * @return the merchantid
	 */
	public String getMerchantid() {
		return merchantid;
	}

	/**
	 * @param merchantid
	 *            the merchantid to set
	 */
	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	/**
	 * @return the merchantname
	 */
	public String getMerchantname() {
		return merchantname;
	}

	/**
	 * @param merchantname
	 *            the merchantname to set
	 */
	public void setMerchantname(String merchantname) {
		this.merchantname = merchantname;
	}

	public List<ProductMainOption> getProductMainOptionList() {
		return productMainOptionList;
	}

	public void setProductMainOptionList(
			List<ProductMainOption> productMainOptionList) {
		this.productMainOptionList = productMainOptionList;
	}


	public List<ProductRequiredOption> getSelected_values() {
		return selected_values;
	}

	public void setSelected_values(List<ProductRequiredOption> selected_values) {
		this.selected_values = selected_values;
	}

	public List<ProductRequiredOption> getRequiredOptions() {
		return requiredOptions;
	}

	public void setRequiredOptions(List<ProductRequiredOption> requiredOptions) {
		this.requiredOptions = requiredOptions;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	
}

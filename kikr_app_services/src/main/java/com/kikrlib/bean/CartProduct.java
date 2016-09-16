package com.kikrlib.bean;

import java.util.List;

public class CartProduct {

	String site_id;
	String PRODUCT_MD5;
	String size;
	String shipping_zip;
	String shipping_city;
	String shipping_state;
	String shipping_option;
	List<String> md5;
	List<String> sizes;
	String affiliateUrl;
	List<String> affiliateUrlList;
	String quantity;
	List<String> quantities;
	String email;
	String shipping_title;
    String shipping_first_name;
    String shipping_last_name;
    String shipping_address;
    String shipping_country;
    String shipping_telephone;
    String billing_title;
    String billing_first_name;
    String billing_last_name;
    String billing_address;
    String billing_city;
    String billing_state;
    String billing_country;
    String billing_zip;
    String billing_telephone;
    String card_type;
    String card_number;
    String card_name;
    String expiry_date_year;
    String expiry_date_month;
    String cvv;
    String color;
    List<String> colorsList;
    String option;
    List<String> optionList;
    String fit;
    List<String> fitList;
    
	/**
	 * @return the option
	 */
	public String getOption() {
		return option;
	}
	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
		this.option = option;
	}
	/**
	 * @return the optionList
	 */
	public List<String> getOptionList() {
		return optionList;
	}
	/**
	 * @param optionList the optionList to set
	 */
	public void setOptionList(List<String> optionList) {
		this.optionList = optionList;
	}
	/**
	 * @return the fit
	 */
	public String getFit() {
		return fit;
	}
	/**
	 * @param fit the fit to set
	 */
	public void setFit(String fit) {
		this.fit = fit;
	}
	/**
	 * @return the fitList
	 */
	public List<String> getFitList() {
		return fitList;
	}
	/**
	 * @param fitList the fitList to set
	 */
	public void setFitList(List<String> fitList) {
		this.fitList = fitList;
	}
	/**
	 * @return the colorsList
	 */
	public List<String> getColorsList() {
		return colorsList;
	}
	/**
	 * @param colorsList the colorsList to set
	 */
	public void setColorsList(List<String> colorsList) {
		this.colorsList = colorsList;
	}
	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}
	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	/**
	 * @return the affiliateUrl
	 */
	public String getAffiliateUrl() {
		return affiliateUrl;
	}
	/**
	 * @param affiliateUrl the affiliateUrl to set
	 */
	public void setAffiliateUrl(String affiliateUrl) {
		this.affiliateUrl = affiliateUrl;
	}
	/**
	 * @return the affiliateUrlList
	 */
	public List<String> getAffiliateUrlList() {
		return affiliateUrlList;
	}
	/**
	 * @param affiliateUrlList the affiliateUrlList to set
	 */
	public void setAffiliateUrlList(List<String> affiliateUrlList) {
		this.affiliateUrlList = affiliateUrlList;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the shipping_title
	 */
	public String getShipping_title() {
		return shipping_title;
	}
	/**
	 * @param shipping_title the shipping_title to set
	 */
	public void setShipping_title(String shipping_title) {
		this.shipping_title = shipping_title;
	}
	/**
	 * @return the shipping_first_name
	 */
	public String getShipping_first_name() {
		return shipping_first_name;
	}
	/**
	 * @param shipping_first_name the shipping_first_name to set
	 */
	public void setShipping_first_name(String shipping_first_name) {
		this.shipping_first_name = shipping_first_name;
	}
	/**
	 * @return the shipping_last_name
	 */
	public String getShipping_last_name() {
		return shipping_last_name;
	}
	/**
	 * @param shipping_last_name the shipping_last_name to set
	 */
	public void setShipping_last_name(String shipping_last_name) {
		this.shipping_last_name = shipping_last_name;
	}
	/**
	 * @return the shipping_address
	 */
	public String getShipping_address() {
		return shipping_address;
	}
	/**
	 * @param shipping_address the shipping_address to set
	 */
	public void setShipping_address(String shipping_address) {
		this.shipping_address = shipping_address;
	}
	/**
	 * @return the shipping_country
	 */
	public String getShipping_country() {
		return shipping_country;
	}
	/**
	 * @param shipping_country the shipping_country to set
	 */
	public void setShipping_country(String shipping_country) {
		this.shipping_country = shipping_country;
	}
	/**
	 * @return the shipping_telephone
	 */
	public String getShipping_telephone() {
		return shipping_telephone;
	}
	/**
	 * @param shipping_telephone the shipping_telephone to set
	 */
	public void setShipping_telephone(String shipping_telephone) {
		this.shipping_telephone = shipping_telephone;
	}
	/**
	 * @return the billing_title
	 */
	public String getBilling_title() {
		return billing_title;
	}
	/**
	 * @param billing_title the billing_title to set
	 */
	public void setBilling_title(String billing_title) {
		this.billing_title = billing_title;
	}
	/**
	 * @return the billing_first_name
	 */
	public String getBilling_first_name() {
		return billing_first_name;
	}
	/**
	 * @param billing_first_name the billing_first_name to set
	 */
	public void setBilling_first_name(String billing_first_name) {
		this.billing_first_name = billing_first_name;
	}
	/**
	 * @return the billing_last_name
	 */
	public String getBilling_last_name() {
		return billing_last_name;
	}
	/**
	 * @param billing_last_name the billing_last_name to set
	 */
	public void setBilling_last_name(String billing_last_name) {
		this.billing_last_name = billing_last_name;
	}
	/**
	 * @return the billing_address
	 */
	public String getBilling_address() {
		return billing_address;
	}
	/**
	 * @param billing_address the billing_address to set
	 */
	public void setBilling_address(String billing_address) {
		this.billing_address = billing_address;
	}
	/**
	 * @return the billing_city
	 */
	public String getBilling_city() {
		return billing_city;
	}
	/**
	 * @param billing_city the billing_city to set
	 */
	public void setBilling_city(String billing_city) {
		this.billing_city = billing_city;
	}
	/**
	 * @return the billing_state
	 */
	public String getBilling_state() {
		return billing_state;
	}
	/**
	 * @param billing_state the billing_state to set
	 */
	public void setBilling_state(String billing_state) {
		this.billing_state = billing_state;
	}
	/**
	 * @return the billing_country
	 */
	public String getBilling_country() {
		return billing_country;
	}
	/**
	 * @param billing_country the billing_country to set
	 */
	public void setBilling_country(String billing_country) {
		this.billing_country = billing_country;
	}
	/**
	 * @return the billing_zip
	 */
	public String getBilling_zip() {
		return billing_zip;
	}
	/**
	 * @param billing_zip the billing_zip to set
	 */
	public void setBilling_zip(String billing_zip) {
		this.billing_zip = billing_zip;
	}
	/**
	 * @return the billing_telephone
	 */
	public String getBilling_telephone() {
		return billing_telephone;
	}
	/**
	 * @param billing_telephone the billing_telephone to set
	 */
	public void setBilling_telephone(String billing_telephone) {
		this.billing_telephone = billing_telephone;
	}
	/**
	 * @return the card_type
	 */
	public String getCard_type() {
		return card_type;
	}
	/**
	 * @param card_type the card_type to set
	 */
	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}
	/**
	 * @return the card_number
	 */
	public String getCard_number() {
		return card_number;
	}
	/**
	 * @param card_number the card_number to set
	 */
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	/**
	 * @return the card_name
	 */
	public String getCard_name() {
		return card_name;
	}
	/**
	 * @param card_name the card_name to set
	 */
	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}
	/**
	 * @return the expiry_date_year
	 */
	public String getExpiry_date_year() {
		return expiry_date_year;
	}
	/**
	 * @param expiry_date_year the expiry_date_year to set
	 */
	public void setExpiry_date_year(String expiry_date_year) {
		this.expiry_date_year = expiry_date_year;
	}
	/**
	 * @return the expiry_date_month
	 */
	public String getExpiry_date_month() {
		return expiry_date_month;
	}
	/**
	 * @param expiry_date_month the expiry_date_month to set
	 */
	public void setExpiry_date_month(String expiry_date_month) {
		this.expiry_date_month = expiry_date_month;
	}
	/**
	 * @return the cvv
	 */
	public String getCvv() {
		return cvv;
	}
	/**
	 * @param cvv the cvv to set
	 */
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}
	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	/**
	 * @return the quantities
	 */
	public List<String> getQuantities() {
		return quantities;
	}
	/**
	 * @param quantities the quantities to set
	 */
	public void setQuantities(List<String> quantities) {
		this.quantities = quantities;
	}
	/**
	 * @return the sizes
	 */
	public List<String> getSizes() {
		return sizes;
	}
	/**
	 * @param sizes the sizes to set
	 */
	public void setSizes(List<String> sizes) {
		this.sizes = sizes;
	}
	/**
	 * @return the md5
	 */
	public List<String> getMd5() {
		return md5;
	}
	/**
	 * @param md5 the md5 to set
	 */
	public void setMd5(List<String> md5) {
		this.md5 = md5;
	}
	/**
	 * @return the site_id
	 */
	public String getSite_id() {
		return site_id;
	}
	/**
	 * @param site_id the site_id to set
	 */
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	/**
	 * @return the pRODUCT_MD5
	 */
	public String getPRODUCT_MD5() {
		return PRODUCT_MD5;
	}
	/**
	 * @param pRODUCT_MD5 the pRODUCT_MD5 to set
	 */
	public void setPRODUCT_MD5(String pRODUCT_MD5) {
		PRODUCT_MD5 = pRODUCT_MD5;
	}
	/**
	 * @return the size
	 */
	public String getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(String size) {
		this.size = size;
	}
	/**
	 * @return the shipping_zip
	 */
	public String getShipping_zip() {
		return shipping_zip;
	}
	/**
	 * @param shipping_zip the shipping_zip to set
	 */
	public void setShipping_zip(String shipping_zip) {
		this.shipping_zip = shipping_zip;
	}
	/**
	 * @return the shipping_city
	 */
	public String getShipping_city() {
		return shipping_city;
	}
	/**
	 * @param shipping_city the shipping_city to set
	 */
	public void setShipping_city(String shipping_city) {
		this.shipping_city = shipping_city;
	}
	/**
	 * @return the shipping_state
	 */
	public String getShipping_state() {
		return shipping_state;
	}
	/**
	 * @param shipping_state the shipping_state to set
	 */
	public void setShipping_state(String shipping_state) {
		this.shipping_state = shipping_state;
	}
	/**
	 * @return the shipping_option
	 */
	public String getShipping_option() {
		return shipping_option;
	}
	/**
	 * @param shipping_option the shipping_option to set
	 */
	public void setShipping_option(String shipping_option) {
		this.shipping_option = shipping_option;
	}
	
}

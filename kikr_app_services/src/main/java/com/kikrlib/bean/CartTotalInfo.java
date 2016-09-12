package com.kikrlib.bean;

import java.io.Serializable;

public class CartTotalInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5379482947245023166L;
	String sub_total;
	String est_tax;
	String est_shippping;
	String total_price;
	String handling;
	String item_count;
	
	/**
	 * @return the item_count
	 */
	public String getItem_count() {
		return item_count;
	}
	/**
	 * @param item_count the item_count to set
	 */
	public void setItem_count(String item_count) {
		this.item_count = item_count;
	}
	/**
	 * @return the sub_total
	 */
	public String getSub_total() {
		return sub_total;
	}
	/**
	 * @param sub_total the sub_total to set
	 */
	public void setSub_total(String sub_total) {
		this.sub_total = sub_total;
	}
	/**
	 * @return the handling
	 */
	public String getHandling() {
		return handling;
	}
	/**
	 * @param handling the handling to set
	 */
	public void setHandling(String handling) {
		this.handling = handling;
	}
	/**
	 * @return the est_tax
	 */
	public String getEst_tax() {
		return est_tax;
	}
	/**
	 * @param est_tax the est_tax to set
	 */
	public void setEst_tax(String est_tax) {
		this.est_tax = est_tax;
	}
	/**
	 * @return the est_shippping
	 */
	public String getEst_shippping() {
		return est_shippping;
	}
	/**
	 * @param est_shippping the est_shippping to set
	 */
	public void setEst_shippping(String est_shippping) {
		this.est_shippping = est_shippping;
	}
	/**
	 * @return the total_price
	 */
	public String getTotal_price() {
		return total_price;
	}
	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	
}

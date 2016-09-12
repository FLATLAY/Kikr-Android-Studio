package com.kikrlib.bean;

public class Orders {

	String date;
	String id;
	String status;
	String cart_id;
	String price;
	String tax;
	String shipping;
	String finalcartprice;
	/**
	 * @return the order_date
	 */
	public String getOrder_date() {
		return date;
	}

	/**
	 * @param order_date the order_date to set
	 */
	public void setOrder_date(String order_date) {
		this.date = order_date;
	}
	/**
	 * @return the order_id
	 */
	public String getOrder_id() {
		return id;
	}
	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.id = order_id;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the status
	 */
	public String getCartId() {
		return cart_id;
	}
	/**
	 * @param status the status to set
	 */
	public void setCartId(String cartid) {
		this.cart_id = cartid;
	}

	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getTax() {
		return tax;
	}
	public void setTax(String tax) {
		this.tax = tax;
	}
	public String getShipping() {
		return shipping;
	}
	public void setShipping(String shipping) {
		this.shipping = shipping;
	}
	public String getFinalcartprice() {
		return finalcartprice;
	}
	public void setFinalcartprice(String finalcartprice) {
		this.finalcartprice = finalcartprice;
	}
	
	
}

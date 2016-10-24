package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.Orders;
import com.flatlaylib.bean.Product;

public class OrderRes {
	
	String code;
	String message;
	List<Orders> data;
	String price;
	String tax;
	String shipping;
	String status;
	String quantity;
	String id;
	List<Product> product;
	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}
	/**
	 * @return the product
	 */
	public List<Product> getProduct() {
		return product;
	}
	/**
	 * @param product the product to set
	 */
	public void setProduct(List<Product> product) {
		this.product = product;
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the data
	 */
	public List<Orders> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<Orders> data) {
		this.data = data;
	}
	/**
	 * @return the total
	 */
	public String getTotal() {
		return price;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.price = total;
	}
	/**
	 * @return the tax
	 */
	public String getTax() {
		return tax;
	}
	/**
	 * @param tax the tax to set
	 */
	public void setTax(String tax) {
		this.tax = tax;
	}
	/**
	 * @return the shipping
	 */
	public String getShipping() {
		return shipping;
	}
	/**
	 * @param shipping the shipping to set
	 */
	public void setShipping(String shipping) {
		this.shipping = shipping;
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

}

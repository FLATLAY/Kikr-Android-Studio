package com.kikrlib.bean;

import java.util.List;

public class ProductFeedItem {

	String item_id;
	String item_name;
	String item_image;
	String profile_pic;
	List<Product> products;
	String type;
	boolean isLoadMore=true;
	int pagenum=0;
	boolean isLoading=false;
	
	/**
	 * @return the profile_pic
	 */
	public String getProfile_pic() {
		return profile_pic;
	}

	/**
	 * @param profile_pic the profile_pic to set
	 */
	public void setProfile_pic(String profile_pic) {
		this.profile_pic = profile_pic;
	}

	/**
	 * @return the isLoading
	 */
	public boolean isLoading() {
		return isLoading;
	}

	/**
	 * @param isLoading the isLoading to set
	 */
	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	/**
	 * @return the pagenum
	 */
	public int getPagenum() {
		return pagenum;
	}

	/**
	 * @param pagenum the pagenum to set
	 */
	public void setPagenum(int pagenum) {
		this.pagenum = pagenum;
	}

	/**
	 * @return the isLoadMore
	 */
	public boolean isLoadMore() {
		return isLoadMore;
	}

	/**
	 * @param isLoadMore the isLoadMore to set
	 */
	public void setLoadMore(boolean isLoadMore) {
		this.isLoadMore = isLoadMore;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the item_id
	 */
	public String getItem_id() {
		return item_id;
	}

	/**
	 * @param item_id
	 *            the item_id to set
	 */
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	/**
	 * @return the item_name
	 */
	public String getItem_name() {
		return item_name;
	}

	/**
	 * @param item_name
	 *            the item_name to set
	 */
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	/**
	 * @return the item_image
	 */
	public String getItem_image() {
		return item_image;
	}

	/**
	 * @param item_image
	 *            the item_image to set
	 */
	public void setItem_image(String item_image) {
		this.item_image = item_image;
	}

	/**
	 * @return the products
	 */
	public List<Product> getProducts() {
		return products;
	}

	/**
	 * @param products
	 *            the products to set
	 */
	public void setProducts(List<Product> products) {
		this.products = products;
	}

}

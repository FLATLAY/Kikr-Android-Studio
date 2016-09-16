package com.kikrlib.bean;

import java.util.List;

public class CollectionMonthDetailList {

	String collection_name;
	String last_update;
	String product_views;
	String items_in_collection;
	String payout;
	List<CollectionImages> collection_images;
	String collection_id;
	String collection_views;
	String items_save;
	String payout_pending;

	public String getLast_update() {
		return last_update;
	}

	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}

	public String getPayout_pending() {
		return payout_pending;
	}

	public void setPayout_pending(String payout_pending) {
		this.payout_pending = payout_pending;
	}

	/**
	 * @return the items_save
	 */
	public String getItems_save() {
		return items_save;
	}
	/**
	 * @param items_save the items_save to set
	 */
	public void setItems_save(String items_save) {
		this.items_save = items_save;
	}
	/**
	 * @return the collection_name
	 */
	public String getCollection_name() {
		return collection_name;
	}
	/**
	 * @param collection_name the collection_name to set
	 */
	public void setCollection_name(String collection_name) {
		this.collection_name = collection_name;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return last_update;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.last_update = date;
	}
	/**
	 * @return the product_views
	 */
	public String getProduct_views() {
		return product_views;
	}
	/**
	 * @param product_views the product_views to set
	 */
	public void setProduct_views(String product_views) {
		this.product_views = product_views;
	}
	/**
	 * @return the items_in_collection
	 */
	public String getItems_in_collection() {
		return items_in_collection;
	}
	/**
	 * @param items_in_collection the items_in_collection to set
	 */
	public void setItems_in_collection(String items_in_collection) {
		this.items_in_collection = items_in_collection;
	}
	/**
	 * @return the payout
	 */
	public String getPayout() {
		return payout;
	}
	/**
	 * @param payout the payout to set
	 */
	public void setPayout(String payout) {
		this.payout = payout;
	}
	/**
	 * @return the collection_images
	 */
	public List<CollectionImages> getCollection_images() {
		return collection_images;
	}
	/**
	 * @param collection_images the collection_images to set
	 */
	public void setCollection_images(List<CollectionImages> collection_images) {
		this.collection_images = collection_images;
	}
	/**
	 * @return the collection_id
	 */
	public String getCollection_id() {
		return collection_id;
	}
	/**
	 * @param collection_id the collection_id to set
	 */
	public void setCollection_id(String collection_id) {
		this.collection_id = collection_id;
	}
	/**
	 * @return the collection_views
	 */
	public String getCollection_views() {
		return collection_views;
	}
	/**
	 * @param collection_views the collection_views to set
	 */
	public void setCollection_views(String collection_views) {
		this.collection_views = collection_views;
	}

}

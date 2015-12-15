package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.ActivityMonthList;
import com.kikrlib.bean.CollectionMonthDetailList;
import com.kikrlib.bean.CollectionProduct;
import com.kikrlib.bean.User;

public class ActivityRes {

	String code;
	String message;
	String total_payout;
	String month_name;
	String month_activity;
	User user_info;
	String collection_name;
	String last_update;
	String pages_view;
	String total_buys;
	String collection_payout;
	String collection_total;
	String payout;
	String collection_view;
	String product_views;
	List<CollectionProduct> product_list;
	List<ActivityMonthList> activity_month_list;
	List<CollectionMonthDetailList> collection_list;
	/**
	 * @return the last_update
	 */
	public String getLast_update() {
		return last_update;
	}

	/**
	 * @param last_update the last_update to set
	 */
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}

	/**
	 * @return the collection_view
	 */
	public String getCollection_view() {
		return collection_view;
	}

	/**
	 * @param collection_view the collection_view to set
	 */
	public void setCollection_view(String collection_view) {
		this.collection_view = collection_view;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
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
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the total_payout
	 */
	public String getTotal_payout() {
		return total_payout;
	}

	/**
	 * @param total_payout
	 *            the total_payout to set
	 */
	public void setTotal_payout(String total_payout) {
		this.total_payout = total_payout;
	}

	/**
	 * @return the month_name
	 */
	public String getMonth_name() {
		return month_name;
	}

	/**
	 * @param month_name
	 *            the month_name to set
	 */
	public void setMonth_name(String month_name) {
		this.month_name = month_name;
	}

	/**
	 * @return the month_activity
	 */
	public String getMonth_activity() {
		return month_activity;
	}

	/**
	 * @param month_activity
	 *            the month_activity to set
	 */
	public void setMonth_activity(String month_activity) {
		this.month_activity = month_activity;
	}

	/**
	 * @return the activity_month_list
	 */
	public List<ActivityMonthList> getActivity_month_list() {
		return activity_month_list;
	}

	/**
	 * @param activity_month_list
	 *            the activity_month_list to set
	 */
	public void setActivity_month_list(
			List<ActivityMonthList> activity_month_list) {
		this.activity_month_list = activity_month_list;
	}

	/**
	 * @return the collection_list
	 */
	public List<CollectionMonthDetailList> getCollection_list() {
		return collection_list;
	}

	/**
	 * @param collection_list
	 *            the collection_list to set
	 */
	public void setCollection_list(
			List<CollectionMonthDetailList> collection_list) {
		this.collection_list = collection_list;
	}

	/**
	 * @return the user_info
	 */
	public User getUser_info() {
		return user_info;
	}

	/**
	 * @param user_info
	 *            the user_info to set
	 */
	public void setUser_info(User user_info) {
		this.user_info = user_info;
	}

	/**
	 * @return the collection_name
	 */
	public String getCollection_name() {
		return collection_name;
	}

	/**
	 * @param collection_name
	 *            the collection_name to set
	 */
	public void setCollection_name(String collection_name) {
		this.collection_name = collection_name;
	}

	/**
	 * @return the pages_view
	 */
	public String getPages_view() {
		return pages_view;
	}

	/**
	 * @param pages_view
	 *            the pages_view to set
	 */
	public void setPages_view(String pages_view) {
		this.pages_view = pages_view;
	}

	/**
	 * @return the total_buys
	 */
	public String getTotal_buys() {
		return total_buys;
	}

	/**
	 * @param total_buys
	 *            the total_buys to set
	 */
	public void setTotal_buys(String total_buys) {
		this.total_buys = total_buys;
	}

	/**
	 * @return the collection_payout
	 */
	public String getCollection_payout() {
		return collection_payout;
	}

	/**
	 * @param collection_payout
	 *            the collection_payout to set
	 */
	public void setCollection_payout(String collection_payout) {
		this.collection_payout = collection_payout;
	}

	/**
	 * @return the collection_total
	 */
	public String getCollection_total() {
		return collection_total;
	}

	/**
	 * @param collection_total
	 *            the collection_total to set
	 */
	public void setCollection_total(String collection_total) {
		this.collection_total = collection_total;
	}

	/**
	 * @return the product_list
	 */
	public List<CollectionProduct> getProduct_list() {
		return product_list;
	}

	/**
	 * @param product_list
	 *            the product_list to set
	 */
	public void setProduct_list(List<CollectionProduct> product_list) {
		this.product_list = product_list;
	}

}

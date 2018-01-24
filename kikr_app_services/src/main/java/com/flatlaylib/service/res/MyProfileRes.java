package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.CollectionImages;
import com.flatlaylib.bean.FollowerList;
import com.flatlaylib.bean.ItemList;
import com.flatlaylib.bean.ProfileCollectionList;
import com.flatlaylib.bean.UserData;

public class MyProfileRes {

	String code;
	String message;
	String collection_name;
	String date;
	String pages_view;
	String total_buys;
	String collection_payout;
	String collection_total;
	List<ProfileCollectionList> collection_list;
	List<CollectionImages> collection_images;
	List<ItemList> item_list;
	List<FollowerList> followers_list;
	List<FollowerList> following_list;
	List<UserData> user_data;

	String instagram;
	String facebook;
	String twitter;
	String pinterest;
	String youtube;

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getPinterest() {
		return pinterest;
	}

	public void setPinterest(String pinterest) {
		this.pinterest = pinterest;
	}

	public String getYoutube() {
		return youtube;
	}

	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}

	/**
	 * @return the user_data
	 */
	public List<UserData> getUser_data() {
		return user_data;
	}

	/**
	 * @param user_data the user_data to set
	 */
	public void setUser_data(List<UserData> user_data) {
		this.user_data = user_data;
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
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * @return the collection_list
	 */
	public List<ProfileCollectionList> getCollection_list() {
		return collection_list;
	}

	/**
	 * @param collection_list
	 *            the collection_list to set
	 */
	public void setCollection_list(List<ProfileCollectionList> collection_list) {
		this.collection_list = collection_list;
	}

	/**
	 * @return the collection_images
	 */
	public List<CollectionImages> getCollection_images() {
		return collection_images;
	}

	/**
	 * @param collection_images
	 *            the collection_images to set
	 */
	public void setCollection_images(List<CollectionImages> collection_images) {
		this.collection_images = collection_images;
	}

	/**
	 * @return the item_list
	 */
	public List<ItemList> getItem_list() {
		return item_list;
	}

	/**
	 * @param item_list
	 *            the item_list to set
	 */
	public void setItem_list(List<ItemList> item_list) {
		this.item_list = item_list;
	}

	/**
	 * @return the followers_list
	 */
	public List<FollowerList> getFollowers_list() {
		return followers_list;
	}

	/**
	 * @param followers_list
	 *            the followers_list to set
	 */
	public void setFollowers_list(List<FollowerList> followers_list) {
		this.followers_list = followers_list;
	}

	/**
	 * @return the following_list
	 */
	public List<FollowerList> getFollowing_list() {
		return following_list;
	}

	/**
	 * @param following_list
	 *            the following_list to set
	 */
	public void setFollowing_list(List<FollowerList> following_list) {
		this.following_list = following_list;
	}

}

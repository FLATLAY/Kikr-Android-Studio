package com.flatlaylib.bean;

import java.util.List;

public class InterestSection {
	String id;
	String name;
	String img;
	String is_followed;
	String username;
	String profile_pic;
	String is_selected="no";
	String logo;
	String is_followedbyviewer;

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public String getItem_description() {
		return item_description;
	}

	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getItem_image() {
		return item_image;
	}

	public void setItem_image(String item_image) {
		this.item_image = item_image;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(String followers_count) {
		this.followers_count = followers_count;
	}

	public String getCollections_count() {
		return collections_count;
	}

	public void setCollections_count(String collections_count) {
		this.collections_count = collections_count;
	}

	String item_id;
	String item_description;
	String item_name;
	String item_image;
	String type;
	String followers_count;
	String collections_count;

	public List<Product> getProducts() {
		return products;
	}

//	public void setProducts(List<Product> products) {
//		this.products = products;
//	}

	public List<Inspiration> getInspiration_feed() {
		return inspiration_feed;
	}

	public void setInspiration_feed(List<Inspiration> inspiration_feed) {
		this.inspiration_feed = inspiration_feed;
	}

	List<Product> products;
	List<Inspiration> inspiration_feed;

	public String getIs_followedbyviewer() {
		return is_followedbyviewer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public void setIs_followedbyviewer(String is_followedbyviewer) {
		this.is_followedbyviewer = is_followedbyviewer;

	}
	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}
	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
	/**
	 * @return the is_selected
	 */
	public String getIs_selected() {
		return is_selected;
	}
	/**
	 * @param is_selected the is_selected to set
	 */
	public void setIs_selected(String is_selected) {
		this.is_selected = is_selected;
	}
	/**
	 * @return the id
	 */

	/**
	 * @return the is_followed
	 */
	public String getIs_followed() {
		return is_followed;
	}
	/**
	 * @param is_followed the is_followed to set
	 */
	public void setIs_followed(String is_followed) {
		this.is_followed = is_followed;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
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



}

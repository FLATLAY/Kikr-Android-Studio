package com.kikrlib.bean;

import java.util.List;

public class FeaturedTabData {

	String item_id;
	String item_description;
	String profile_pic;
	String item_name;
	String item_image;
	String type;
	List<Product> products;
	List<Inspiration> inspiration_feed;
	
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getDescription() {
		return item_description;
	}
	public void setDescription(String description) {
		this.item_description = description;
	}
	public String getProfile_pic() {
		return profile_pic;
	}
	public void setProfile_pic(String profile_pic) {
		this.profile_pic = profile_pic;
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
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Inspiration> getInspiration_feed() {
		return inspiration_feed;
	}
	public void setInspiration_feed(List<Inspiration> inspiration_feed) {
		this.inspiration_feed = inspiration_feed;
	}

}

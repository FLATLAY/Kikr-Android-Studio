package com.kikrlib.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductChildOption implements Serializable{
	private String value;
	private String text;
	private String price;
	private String image;
	private String extra_info;
	private List<ProductMainOption> dep = new ArrayList<ProductMainOption>();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getExtra_info() {
		return extra_info;
	}

	public void setExtra_info(String extra_info) {
		this.extra_info = extra_info;
	}

	public List<ProductMainOption> getDep() {
		return dep;
	}

	public void setDep(List<ProductMainOption> dep) {
		this.dep = dep;
	}

}

package com.kikrlib.bean;

public class BrandList {
	
	String id;
	String name;
	String img;
	String is_followed;
	String logo;
	
	
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


}

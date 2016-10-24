package com.flatlaylib.bean;

import java.io.Serializable;

public class TopDeals implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String title;
	String latlng;
	String dealid;
	String description;
	String link;
	String imagelink;
	String merchantName;
	String expiryTime;
	boolean isFavorite=false;

	/**
	 * @return the isFavorite
	 */
	public boolean isFavorite() {
		return isFavorite;
	}
	/**
	 * @param isFavorite the isFavorite to set
	 */
	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	/**
	 * @return the merchantName
	 */
	public String getMerchantName() {
		return merchantName;
	}
	/**
	 * @param merchantName the merchantName to set
	 */
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	/**
	 * @return the expiryTime
	 */
	public String getExpiryTime() {
		return expiryTime;
	}
	/**
	 * @param expiryTime the expiryTime to set
	 */
	public void setExpiryTime(String expiryTime) {
		this.expiryTime = expiryTime;
	}
	/**
	 * @return the imagelink
	 */
	public String getImagelink() {
		return imagelink;
	}
	/**
	 * @param imagelink the imagelink to set
	 */
	public void setImagelink(String imagelink) {
		this.imagelink = imagelink;
	}
	/**
	 * @return the latlng
	 */
	public String getLatlng() {
		return latlng;
	}
	/**
	 * @param latlng the latlng to set
	 */
	public void setLatlng(String latlng) {
		this.latlng = latlng;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the dealid
	 */
	public String getDealid() {
		return dealid;
	}
	/**
	 * @param dealid the dealid to set
	 */
	public void setDealid(String dealid) {
		this.dealid = dealid;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	
}

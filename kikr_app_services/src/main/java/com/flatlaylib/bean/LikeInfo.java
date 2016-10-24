package com.flatlaylib.bean;

import java.io.Serializable;

public class LikeInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8895768432622451068L;
	String like_count;
	String like_id;
	/**
	 * @return the like_count
	 */
	public String getLike_count() {
		return like_count;
	}
	/**
	 * @param like_count the like_count to set
	 */
	public void setLike_count(String like_count) {
		this.like_count = like_count;
	}
	/**
	 * @return the like_id
	 */
	public String getLike_id() {
		return like_id;
	}
	/**
	 * @param like_id the like_id to set
	 */
	public void setLike_id(String like_id) {
		this.like_id = like_id;
	}
}

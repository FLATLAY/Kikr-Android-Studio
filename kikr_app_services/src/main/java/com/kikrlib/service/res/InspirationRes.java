package com.kikrlib.service.res;

import java.util.List;

import com.kikrlib.bean.Comment;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.Product;

public class InspirationRes {
	String code;
	String message;
	String comment_count;
	String like_count;
	String like_id;
	List<Comment> comments;
	List<Product> data;
	Inspiration inspiration;
	String id;
	String Image_url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImage_url() {
		return Image_url;
	}

	public void setImage_url(String image_url) {
		Image_url = image_url;
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	/**
	 * @return the inspiration
	 */
	public Inspiration getInspiration() {
		return inspiration;
	}
	/**
	 * @param inspiration the inspiration to set
	 */
	public void setInspiration(Inspiration inspiration) {
		this.inspiration = inspiration;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the messsage
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param messsage the messsage to set
	 */
	public void setMessage(String messsage) {
		this.message = messsage;
	}
	/**
	 * @return the comment_count
	 */
	public String getComment_count() {
		return comment_count;
	}
	/**
	 * @param comment_count the comment_count to set
	 */
	public void setComment_count(String comment_count) {
		this.comment_count = comment_count;
	}
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
	/**
	 * @return the comment
	 */
	public List<Comment> getComment() {
		return comments;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(List<Comment> comment) {
		this.comments = comment;
	}
	/**
	 * @return the data
	 */
	public List<Product> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<Product> data) {
		this.data = data;
	}
	
	
	
}

package com.kikrlib.bean;

import java.util.List;

public class ProfileCollectionList {
	String name;
	String id;
	String last_update;
	List<CollectionImages> collection_images;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	
	

}

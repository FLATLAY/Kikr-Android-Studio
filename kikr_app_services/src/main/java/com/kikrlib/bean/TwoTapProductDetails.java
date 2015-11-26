package com.kikrlib.bean;

import java.util.List;

import org.json.JSONArray;

public class TwoTapProductDetails {
	List<String> colorList;
	List<JSONArray> sizeArray;
	List<JSONArray> colorsArray;
	List<String> fitList;
	List<String> optionList;
	List<String> sizeList;
	String original_url;
	List<ProductRequiredOption> requiredOptionList;
	List<ProductMainOption> productMainOptionList;
	String siteId;
	String md5;
	String url;
	/**
	 * @return the colorsArray
	 */
	public List<JSONArray> getColorsArray() {
		return colorsArray;
	}

	/**
	 * @param colorsArray
	 *            the colorsArray to set
	 */
	public void setColorsArray(List<JSONArray> colorsArray) {
		this.colorsArray = colorsArray;
	}

	public String getOriginal_url() {
		return original_url;
	}

	public void setOriginal_url(String original_url) {
		this.original_url = original_url;
	}

	/**
	 * @return the sizeArray
	 */
	public List<JSONArray> getSizeArray() {
		return sizeArray;
	}

	/**
	 * @param sizeArray
	 *            the sizeArray to set
	 */
	public void setSizeArray(List<JSONArray> sizeArray) {
		this.sizeArray = sizeArray;
	}

	/**
	 * @return the sizeList
	 */
	public List<String> getSizeList() {
		return sizeList;
	}

	/**
	 * @param sizeList
	 *            the sizeList to set
	 */
	public void setSizeList(List<String> sizeList) {
		this.sizeList = sizeList;
	}

	/**
	 * @return the colorList
	 */
	public List<String> getColorList() {
		return colorList;
	}

	/**
	 * @param colorList
	 *            the colorList to set
	 */
	public void setColorList(List<String> colorList) {
		this.colorList = colorList;
	}

	/**
	 * @return the fitList
	 */
	public List<String> getFitList() {
		return fitList;
	}

	/**
	 * @param fitList
	 *            the fitList to set
	 */
	public void setFitList(List<String> fitList) {
		this.fitList = fitList;
	}

	/**
	 * @return the optionList
	 */
	public List<String> getOptionList() {
		return optionList;
	}

	/**
	 * @param optionList
	 *            the optionList to set
	 */
	public void setOptionList(List<String> optionList) {
		this.optionList = optionList;
	}

	public List<ProductRequiredOption> getRequiredOptionList() {
		return requiredOptionList;
	}

	public void setRequiredOptionList(
			List<ProductRequiredOption> requiredOptionList) {
		this.requiredOptionList = requiredOptionList;
	}

	public List<ProductMainOption> getProductMainOptionList() {
		return productMainOptionList;
	}

	public void setProductMainOptionList(
			List<ProductMainOption> productMainOptionList) {
		this.productMainOptionList = productMainOptionList;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}

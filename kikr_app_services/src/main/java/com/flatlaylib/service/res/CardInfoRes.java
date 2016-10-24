package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.Card;

public class CardInfoRes {
	String code;
	String message;
	List<Card> data;
	String card_id;
	
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
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the data
	 */
	public List<Card> getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(List<Card> data) {
		this.data = data;
	}
	/**
	 * @return the card_id
	 */
	public String getCard_id() {
		return card_id;
	}
	/**
	 * @param card_id the card_id to set
	 */
	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}
	
	
	
}

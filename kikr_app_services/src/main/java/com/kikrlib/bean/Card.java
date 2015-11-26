package com.kikrlib.bean;

public class Card {

	String card_id;
	String card_number;
	String name_on_card;
	String expiration_date;
	String cvv;
	String cardtype;
	
	/**
	 * @return the cardtype
	 */
	public String getCardtype() {
		return cardtype;
	}
	/**
	 * @param cardtype the cardtype to set
	 */
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
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
	/**
	 * @return the card_number
	 */
	public String getCard_number() {
		return card_number;
	}
	/**
	 * @param card_number the card_number to set
	 */
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	/**
	 * @return the name_on_card
	 */
	public String getName_on_card() {
		return name_on_card;
	}
	/**
	 * @param name_on_card the name_on_card to set
	 */
	public void setName_on_card(String name_on_card) {
		this.name_on_card = name_on_card;
	}
	/**
	 * @return the expiration_date
	 */
	public String getExpiration_date() {
		return expiration_date;
	}
	/**
	 * @param expiration_date the expiration_date to set
	 */
	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}
	/**
	 * @return the cvv
	 */
	public String getCvv() {
		return cvv;
	}
	/**
	 * @param cvv the cvv to set
	 */
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	
	
}

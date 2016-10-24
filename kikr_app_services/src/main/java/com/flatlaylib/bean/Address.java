package com.flatlaylib.bean;

public class Address {
	String address_id;
	String firstname;
	String lastname;
	String state;
	String tel;
	String street_address;
	String city_town;
	String zip_code;
	String country;
	boolean isSelected = false;
	String title;
	String apartment;
	
	/**
	 * @return the apratment
	 */
	public String getApartment() {
		return apartment;
	}
	/**
	 * @param apratment the apratment to set
	 */
	public void setApartment(String apratment) {
		this.apartment = apratment;
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
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}
	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}
	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}
	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	/**
	 * @return the address_id
	 */
	public String getAddress_id() {
		return address_id;
	}
	/**
	 * @param address_id the address_id to set
	 */
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}
	/**
	 * @return the street_number
	 */
//	public String getStreet_number() {
//		return street_number;
//	}
//	/**
//	 * @param street_number the street_number to set
//	 */
//	public void setStreet_number(String street_number) {
//		this.street_number = street_number;
//	}
	/**
	 * @return the street_address
	 */
	public String getStreet_address() {
		return street_address;
	}
	/**
	 * @param street_address the street_address to set
	 */
	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}
	/**
	 * @return the city_town
	 */
	public String getCity_town() {
		return city_town;
	}
	/**
	 * @param city_town the city_town to set
	 */
	public void setCity_town(String city_town) {
		this.city_town = city_town;
	}
	/**
	 * @return the zip_code
	 */
	public String getZip_code() {
		return zip_code;
	}
	/**
	 * @param zip_code the zip_code to set
	 */
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

}

package com.kikrlib.bean;

import java.util.List;

import org.json.JSONObject;

public class CartTwotapData {
	
	List<String> required_fields_names;
	JSONObject required_field_values;
	
	/**
	 * @return the required_fields_names
	 */
	public List<String> getRequired_fields_names() {
		return required_fields_names;
	}
	/**
	 * @param required_fields_names the required_fields_names to set
	 */
	public void setRequired_fields_names(List<String> required_fields_names) {
		this.required_fields_names = required_fields_names;
	}
	/**
	 * @return the required_field_values
	 */
	public JSONObject getRequired_field_values() {
		return required_field_values;
	}
	/**
	 * @param required_field_values the required_field_values to set
	 */
	public void setRequired_field_values(JSONObject required_field_values) {
		this.required_field_values = required_field_values;
	}
	
	

}

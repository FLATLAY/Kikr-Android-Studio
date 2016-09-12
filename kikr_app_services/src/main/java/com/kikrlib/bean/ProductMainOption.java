package com.kikrlib.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductMainOption implements Serializable{
	private String name;
	private List<ProductChildOption> optionList = new ArrayList<ProductChildOption>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ProductChildOption> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<ProductChildOption> optionList) {
		this.optionList = optionList;
	}

}

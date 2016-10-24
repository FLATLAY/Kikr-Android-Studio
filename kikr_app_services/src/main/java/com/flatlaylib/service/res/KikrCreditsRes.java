package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.Credits;
import com.flatlaylib.bean.Detail;

public class KikrCreditsRes {

	String code;
	String message;
	String amount;
	List<Credits> activity_month_list;
	List<Detail> detail;
	String total_pending_credit;

	public String getTotal_pending_credit() {
		return total_pending_credit;
	}

	public void setTotal_pending_credit(String total_pending_credit) {
		this.total_pending_credit = total_pending_credit;
	}

	public List<Detail> getDetail() {
		return detail;
	}
	public void setDetail(List<Detail> detail) {
		this.detail = detail;
	}
	/**
	 * @return the activity_month_list
	 */
	public List<Credits> getActivity_month_list() {
		return activity_month_list;
	}
	/**
	 * @param activity_month_list the activity_month_list to set
	 */
	public void setActivity_month_list(List<Credits> activity_month_list) {
		this.activity_month_list = activity_month_list;
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
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
}

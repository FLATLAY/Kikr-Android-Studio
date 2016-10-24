package com.flatlaylib.service.res;

import java.util.List;

import com.flatlaylib.bean.Uuid;
import com.flatlaylib.bean.AbsBindObject;

public class GetUUIDRes extends AbsBindObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1333800160998251043L;
	String code;
	String message;
	List<Uuid> uuid_list;
	
	public String getCode() {
		return code;
	}
	
	public List<Uuid> getUuid_list() {
		return uuid_list;
	}
	
	public void setUuid_list(List<Uuid> uuid_list) {
		this.uuid_list = uuid_list;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}

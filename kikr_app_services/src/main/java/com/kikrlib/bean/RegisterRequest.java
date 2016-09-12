package com.kikrlib.bean;

public class RegisterRequest {
	
		String email;
		String password;
		String phone_model;
		String device_token;
		
		public RegisterRequest(String email,String password,String phone_model,String device_token) {
			this.email=email;
			this.password=password;
			this.phone_model=phone_model;
			this.device_token=device_token;
		}
		
		@Override
		public String toString() {
			return "UserRequest [name:"+email+" pass:"+password+" phonemodel"+phone_model+" device_token"+device_token+"]";
		}
	

}

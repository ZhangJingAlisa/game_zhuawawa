package com.zhua.game.service.user.vo;

public class UserStatus {

	private String meno;
	private String token;
	
	public UserStatus(String meno){
		this.meno = meno;
	}
	
	public UserStatus(){
		
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public String getMeno() {
		return meno;
	}

	public void setMeno(String meno) {
		this.meno = meno;
	}
	
	
	
}

package com.zhua.game.service.pay.vo;

public class PayVO {

	private String token;
	private String code;
	
	private String appId;
	private String timeStamp;
	private String nonceStr;
	private String pack;
	private String signType;
	private String paySign;
	private int    totalFee;
	
	public PayVO(){
		
	}
	
	
	
	
    public int getTotalFee() {
		return totalFee;
	}




	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}




	public String getAppId() {
		return appId;
	}



	public void setAppId(String appId) {
		this.appId = appId;
	}



	public String getTimeStamp() {
		return timeStamp;
	}



	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}



	public String getNonceStr() {
		return nonceStr;
	}



	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}



	public String getPack() {
		return pack;
	}



	public void setPack(String pack) {
		this.pack = pack;
	}



	public String getSignType() {
		return signType;
	}



	public void setSignType(String signType) {
		this.signType = signType;
	}



	public String getPaySign() {
		return paySign;
	}



	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}



	public PayVO(String token,String code){
		this.token = token;
		this.code = code;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	
	
}

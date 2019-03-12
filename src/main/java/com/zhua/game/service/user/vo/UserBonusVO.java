package com.zhua.game.service.user.vo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class UserBonusVO {

	private int    money;
	private int    payMoney;
	private String payUserName;//
	
	@JSONField(format="yyyy-MM-dd")
	private Date ctime;
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(int payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayUserName() {
		return payUserName;
	}
	public void setPayUserName(String payUserName) {
		this.payUserName = payUserName;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
    	
	
	
}

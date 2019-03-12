package com.zhua.game.service.user.vo;

public class UserRecommendVO {

	private String userId;
	private String name;
	private String image;
	private int    people;
	private int    money; //可提现
	private int    moneyAll; //总金额
	private int    ranking;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getPeople() {
		return people;
	}
	public void setPeople(int people) {
		this.people = people;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public int getMoneyAll() {
		return moneyAll;
	}
	public void setMoneyAll(int moneyAll) {
		this.moneyAll = moneyAll;
	}
	
	
	
}

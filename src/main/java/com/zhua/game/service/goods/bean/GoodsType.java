package com.zhua.game.service.goods.bean;

public enum GoodsType {

	//0精品 1彩妆 2饰品
	JINPING(0,"精品"),
	CAIZHUANG(1,"彩妆"),
	SHIPIN(2,"饰品");
	
	private final int id;
	private final String show;

	private GoodsType(int id, String show) {
		this.id = id;
		this.show = show;
	}

	public int getId() {
		return id;
	}

	public String getShow() {
		return show;
	} 
	
}

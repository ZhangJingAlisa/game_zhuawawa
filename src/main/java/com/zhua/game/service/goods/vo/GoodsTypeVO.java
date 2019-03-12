package com.zhua.game.service.goods.vo;

import java.util.List;

public class GoodsTypeVO {

	private String typeName;
	private int    typeNumber;
	private List<GoodsVO> goods;
	public List<GoodsVO> getGoods() {
		return goods;
	}
	public void setGoods(List<GoodsVO> goods) {
		this.goods = goods;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getTypeNumber() {
		return typeNumber;
	}
	public void setTypeNumber(int typeNumber) {
		this.typeNumber = typeNumber;
	}
	
	
	
}

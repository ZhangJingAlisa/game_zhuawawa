package com.zhua.game.entity;

import java.util.Date;

/**
 * tb_goods
 * @author liuyijiang
 *
 */
public class GoodsEntity {

	private String id;
	private String name; 
	private String tag;   //规格
	private int    type;	//0精品 1彩妆 2饰品
	private int    gold;    //需要金币
	private int    price;   //价格
	private String memo;   //描述材质
	private String image;   //图片
	private String showImage; //展示图片
	private int    probability;
	private int    status;//0 上架  1下架 
	private String buy;
	private Date   ctime;
	private Date   utime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getGold() {
		return gold;
	}
	public void setGold(int gold) {
		this.gold = gold;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getCtime() {
		return ctime;
	}
	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}
	public Date getUtime() {
		return utime;
	}
	public void setUtime(Date utime) {
		this.utime = utime;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getShowImage() {
		return showImage;
	}
	public void setShowImage(String showImage) {
		this.showImage = showImage;
	}
	public int getProbability() {
		return probability;
	}
	public void setProbability(int probability) {
		this.probability = probability;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getBuy() {
		return buy;
	}
	public void setBuy(String buy) {
		this.buy = buy;
	}
	
	
	
	
}

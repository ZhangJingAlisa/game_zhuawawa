package com.zhua.game.entity;

import java.util.Date;
/**
 * tb_gold
 * @author liuyijiang
 *
 */
public class GoldEntity {

	private String id;
	private int    goldNumber;
	private int    price;//分
	private String url;
	private Date   ctime;
	private Date   utime;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getGoldNumber() {
		return goldNumber;
	}
	public void setGoldNumber(int goldNumber) {
		this.goldNumber = goldNumber;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
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
	
	
	
}

package com.zhua.game.entity;

import java.util.Date;
/**
 * tb_user_level
 * @author liuyijiang
 *
 */
public class UserLevelEntity {

	private String id;
	private String userId;
	private int    level;// 默认是1级用户
	private int    payCount;//购买了几次
	private int    probability;//概率
	private int    bizhong;//必中充值后有异常必中概率
	private Date   ctime;
	private Date   utime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPayCount() {
		return payCount;
	}
	public void setPayCount(int payCount) {
		this.payCount = payCount;
	}
	public int getProbability() {
		return probability;
	}
	public void setProbability(int probability) {
		this.probability = probability;
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
	public int getBizhong() {
		return bizhong;
	}
	public void setBizhong(int bizhong) {
		this.bizhong = bizhong;
	}
	
	
}

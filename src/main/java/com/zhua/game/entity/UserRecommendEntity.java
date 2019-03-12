package com.zhua.game.entity;

import java.util.Date;
/**
 * tb_user_recommend
 * @author liuyijiang
 *
 */
public class UserRecommendEntity {

	private String id;
	private String userId;
	private String recommendUserId;
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
	public String getRecommendUserId() {
		return recommendUserId;
	}
	public void setRecommendUserId(String recommendUserId) {
		this.recommendUserId = recommendUserId;
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

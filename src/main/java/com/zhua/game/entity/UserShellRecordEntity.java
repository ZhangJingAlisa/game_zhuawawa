package com.zhua.game.entity;

/**
 * tb_shell_record
 * @author liuyijiang
 *
 */
public class UserShellRecordEntity {

	private String id;
	private String userId; //userId + date 联合唯一
	private String shellTime;
	
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
	public String getShellTime() {
		return shellTime;
	}
	public void setShellTime(String shellTime) {
		this.shellTime = shellTime;
	}
	
}

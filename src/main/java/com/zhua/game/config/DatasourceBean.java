package com.zhua.game.config;
/**
 * 数据库配�?
 * @author liuyijiang
 *
 */
public class DatasourceBean {
 
	private String url;
	private String driverClassName;
	private String username;
	private String password;
	private int    maximumConnection;
	private int    minimumConnection;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getMaximumConnection() {
		return maximumConnection;
	}
	public void setMaximumConnection(int maximumConnection) {
		this.maximumConnection = maximumConnection;
	}
	public int getMinimumConnection() {
		return minimumConnection;
	}
	public void setMinimumConnection(int minimumConnection) {
		this.minimumConnection = minimumConnection;
	}
	
	
	
}

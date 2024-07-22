package com.hiremesh.interviewbot.common;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserBean implements Serializable {
	private static final long serialVersionUID = 8123925027L;
	private long userId;
	private String userName;
	private String role;
	private String emailId;
	private Map userConfig;
	private transient String password;
	private int activeFlag;
	
	public UserBean() {
		super();
		this.userConfig = new LinkedHashMap<String, Object>();
	}

	public UserBean(long userId, String userName, String role, String emailId, Map userConfig, 
			int activeFlag, String password) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.role = role;
		this.emailId = emailId;
		this.userConfig = userConfig;
		this.activeFlag = activeFlag;
		this.password = password;
	}

	public UserBean(long userId, String userName, String role, String emailId, Map userConfig, int activeFlag) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.role = role;
		this.emailId = emailId;
		this.userConfig = userConfig;
		this.activeFlag = activeFlag;
	}

	public UserBean(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
		this.userConfig = new LinkedHashMap<String, Object>();
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Map getUserConfig() {
		return userConfig;
	}

	public void setUserConfig(Map userConfig) {
		this.userConfig = userConfig;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}

}

package com.im.bo;

public class ActiveUserBo {
	private String userId;
	private String lastActiveDate;
	private String lastActiveSecs;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLastActiveDate() {
		return lastActiveDate;
	}
	public void setLastActiveDate(String lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}
	public String getLastActiveSecs() {
		return lastActiveSecs;
	}
	public void setLastActiveSecs(String lastActiveSecs) {
		this.lastActiveSecs = lastActiveSecs;
	}
}

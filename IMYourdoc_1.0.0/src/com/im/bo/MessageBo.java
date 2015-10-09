package com.im.bo;

public class MessageBo {
	private	String messageId;
	private String filePath;
	private String content;
	private String timeStamp;
	private String fileType;
	private String from;
	private String to;
	private String status;
	private String chatType;
	private String groupMemberId;
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the chatType
	 */
	public String getChatType() {
		return chatType;
	}
	/**
	 * @param chatType the chatType to set
	 */
	public void setChatType(String chatType) {
		this.chatType = chatType;
	}
	/**
	 * @return the groupMemberId
	 */
	public String getGroupMemberId() {
		return groupMemberId;
	}
	/**
	 * @param groupMemberId the groupMemberId to set
	 */
	public void setGroupMemberId(String groupMemberId) {
		this.groupMemberId = groupMemberId;
	}
}

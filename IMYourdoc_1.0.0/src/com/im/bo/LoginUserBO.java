package com.im.bo;

public class LoginUserBO {
	
	private int userId;
	private String username;
	private  String password;
	private  String license_key;
	private  String status;
	private  String login_token;
	private  String user_pin;
	private  boolean isDeviceRegisterRequired;
	private  String user_type;
	private  String name;
	private  byte[] profileImage;
	private  String device_type;
	private  String device_id;
	private  String seq_ques;
	private  String seq_ans;
	private  boolean isPatient = false;
	private  boolean isProvider = false;
	private  boolean isStaff = false;
	
	
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public String getLicense_key() {
		return license_key;
	}
	public void setLicense_key(String license_key) {
		this.license_key = license_key;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLogin_token() {
		return login_token;
	}
	public void setLogin_token(String login_token) {
		this.login_token = login_token;
	}
	public String getUser_pin() {
		return user_pin;
	}
	public void setUser_pin(String user_pin) {
		this.user_pin = user_pin;
	}
	public boolean isDeviceRegisterRequired() {
		return isDeviceRegisterRequired;
	}
	public void setDeviceRegisterRequired(boolean isDeviceRegisterRequired) {
		this.isDeviceRegisterRequired = isDeviceRegisterRequired;
	}
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getSeq_ques() {
		return seq_ques;
	}
	public void setSeq_ques(String seq_ques) {
		this.seq_ques = seq_ques;
	}
	public String getSeq_ans() {
		return seq_ans;
	}
	public void setSeq_ans(String seq_ans) {
		this.seq_ans = seq_ans;
	}
	public boolean isPatient() {
		return isPatient;
	}
	public void setPatient(boolean isPatient) {
		this.isPatient = isPatient;
	}
	public boolean isProvider() {
		return isProvider;
	}
	public void setProvider(boolean isProvider) {
		this.isProvider = isProvider;
	}
	public boolean isStaff() {
		return isStaff;
	}
	public void setStaff(boolean isStaff) {
		this.isStaff = isStaff;
	} 
	

}

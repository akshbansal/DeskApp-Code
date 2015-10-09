package com.im.bo;

public class PatientRegisterBO {
	/**
	 * @author megha 
	 * admin details
	 */

	private String username;
	private String password;
	private String pin;
	private String first_name;
	private String last_name;
	private String email;
	private String security_question;
	private String security_answer;
	private String device_id;
	private String zip;
	private String phone;
	private String privacy_enabled;

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
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @param pin the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}

	/**
	 * @return the security_question
	 */
	public String getSecurity_question() {
		return security_question;
	}

	/**
	 * @param security_question the security_question to set
	 */
	public void setSecurity_question(String security_question) {
		this.security_question = security_question;
	}

	/**
	 * @return the security_answer
	 */
	public String getSecurity_answer() {
		return security_answer;
	}

	/**
	 * @param security_answer the security_answer to set
	 */
	public void setSecurity_answer(String security_answer) {
		this.security_answer = security_answer;
	}

	/**
	 * @return the device_id
	 */
	public String getDevice_id() {
		return device_id;
	}

	/**
	 * @param device_id the device_id to set
	 */
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	/**
	 * @return the privacy_enabled
	 */
	public String getPrivacy_enabled() {
		return privacy_enabled;
	}

	/**
	 * @param privacy_enabled the privacy_enabled to set
	 */
	public void setPrivacy_enabled(String privacy_enabled) {
		this.privacy_enabled = privacy_enabled;
	}

	
	

}

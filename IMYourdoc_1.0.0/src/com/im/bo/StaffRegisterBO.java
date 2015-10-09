package com.im.bo;

public class StaffRegisterBO {
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
	private String primary_hosp_id;
	private String secondary_hosp_id;// according to the name of the hospital
	private String practiseType;
	private String  privacy_enabled;
	private String designation;
	private String jobTitle;
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

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
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

	public String getSecurity_question() {
		return security_question;
	}

	public void setSecurity_question(String security_question) {
		this.security_question = security_question;
	}

	public String getSecurity_answer() {
		return security_answer;
	}

	public void setSecurity_answer(String security_answer) {
		this.security_answer = security_answer;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
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


	public String getPrimary_hosp_id() {
		return primary_hosp_id;
	}

	public void setPrimary_hosp_id(String primary_hosp_id) {
		this.primary_hosp_id = primary_hosp_id;
	}

	/**
	 * @return the practiseType
	 */
	public String getPractiseType() {
		return practiseType;
	}

	/**
	 * @param practiseType the practiseType to set
	 */
	public void setPractiseType(String practiseType) {
		this.practiseType = practiseType;
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

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation the designation to set
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the secondary_hosp_id
	 */
	public String getSecondary_hosp_id() {
		return secondary_hosp_id;
	}

	/**
	 * @param secondary_hosp_id the secondary_hosp_id to set
	 */
	public void setSecondary_hosp_id(String secondary_hosp_id) {
		this.secondary_hosp_id = secondary_hosp_id;
	}


}

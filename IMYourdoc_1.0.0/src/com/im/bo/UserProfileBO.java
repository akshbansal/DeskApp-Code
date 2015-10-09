package com.im.bo;

import java.util.ArrayList;

public class UserProfileBO {
	private String name;
	private String username;
	private String phoneNo;
	private String emailId;
	private String userType;
	private String userStatus;
	private String pic_no;
	private String zip;
	private String pin;
	private String seq_ques;
	private String seq_ans;
	private String privacy_enabled;
	private String practise_type;
	private String primary_hospital;
	private String primary_hosp_id;
	private String designation;
	private String jobTitle;
	public ArrayList<HospitalBO> secondary_hospital;
	private String sec_hospital_selected;
	private String session;
	private String profileImage;
	public UserProfileBO(){
		
	}
	public UserProfileBO(String name,String username,String phoneNo,String emailId, String userType, String userStatus){
		this.name = name;
		this.username = username;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
		this.userType = userType;
		this.userStatus = userStatus;
		
	}
	public UserProfileBO(String name,String username,String phoneNo,String emailId, 
			String userType, String userStatus,String pic_no,String privacy_enabled,String practise_type,
			String primary_hospital,ArrayList<HospitalBO> secondary_hospital,String session){
		
		this.name = name;
		this.username = username;
		this.phoneNo = phoneNo;
		this.emailId = emailId;
		this.userType = userType;
		this.userStatus = userStatus;
		this.pic_no = pic_no;
		this.privacy_enabled = privacy_enabled;
		this.practise_type = practise_type;
		this.primary_hospital = primary_hospital;
		this.setSecondary_hospital(secondary_hospital);
		this.session = session;
		
	}
	
	
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getPrivacy_enabled() {
		return privacy_enabled;
	}
	public String getPic_no() {
		return pic_no;
	}
	public void setPic_no(String pic_no) {
		this.pic_no = pic_no;
	}
	public void setPrivacy_enabled(String privacy_enabled) {
		this.privacy_enabled = privacy_enabled;
	}
	public String getPractise_type() {
		return practise_type;
	}
	public void setPractise_type(String practise_type) {
		this.practise_type = practise_type;
	}
	public String getPrimary_hospital() {
		return primary_hospital;
	}
	public void setPrimary_hospital(String primary_hospital) {
		this.primary_hospital = primary_hospital;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	/**
	 * @return the secondary_hospital
	 */
	public ArrayList<HospitalBO> getSecondary_hospital() {
		return secondary_hospital;
	}
	/**
	 * @param secondary_hospital the secondary_hospital to set
	 */
	public void setSecondary_hospital(ArrayList<HospitalBO> secondary_hospital) {
		this.secondary_hospital = secondary_hospital;
	}
	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}
	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
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
	 * @return the seq_ques
	 */
	public String getSeq_ques() {
		return seq_ques;
	}
	/**
	 * @param seq_ques the seq_ques to set
	 */
	public void setSeq_ques(String seq_ques) {
		this.seq_ques = seq_ques;
	}
	/**
	 * @return the seq_ans
	 */
	public String getSeq_ans() {
		return seq_ans;
	}
	/**
	 * @param seq_ans the seq_ans to set
	 */
	public void setSeq_ans(String seq_ans) {
		this.seq_ans = seq_ans;
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
	 * @return the sec_hospital_selected
	 */
	public String getSec_hospital_selected() {
		return sec_hospital_selected;
	}
	/**
	 * @param sec_hospital_selected the sec_hospital_selected to set
	 */
	public void setSec_hospital_selected(String sec_hospital_selected) {
		this.sec_hospital_selected = sec_hospital_selected;
	}
	/**
	 * @return the profileImage
	 */
	public String getProfileImage() {
		return profileImage;
	}
	/**
	 * @param profileImage the profileImage to set
	 */
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	/**
	 * @return the primary_hosp_id
	 */
	public String getPrimary_hosp_id() {
		return primary_hosp_id;
	}
	/**
	 * @param primary_hosp_id the primary_hosp_id to set
	 */
	public void setPrimary_hosp_id(String primary_hosp_id) {
		this.primary_hosp_id = primary_hosp_id;
	}
	
	
	
	
}

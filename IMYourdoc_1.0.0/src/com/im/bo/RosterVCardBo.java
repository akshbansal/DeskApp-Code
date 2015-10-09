package com.im.bo;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

import com.im.utils.Util;

public class RosterVCardBo {

	private String name;
	private String firstName;
	private String lastName;
	private String userId;
	private String groupJid;
	private String groupMemberId;
	private String email;
	private String primarHospital;
	private String prim_hosp_id;
	private String sec_hosp_id;
	private String isPrimary;
	private byte[] profileImage;
	private String designation; // role in vcard
	private String jobTitle;
	private String practiseType;
	private Presence presence;
	private ItemType subscription;
	private String userType;
	private List<RosterVCardBo> memberList;
	private String bday = "0";
	private boolean isGroup = false;
	private boolean groupHasPatient = false;
	private String groupSubject;
	private boolean isGroupOwner = false;
	private String creationDate;
	private boolean isJoined = false;
	private String membership;
	
	public JPanel panel;
	public JLabel title,subtitle;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}

	public Presence getPresence() {
		return presence;
	}

	public void setPresence(Presence presence) {
		this.presence = presence;
	}

	/**
	 * @return the primarHospital
	 */
	public String getPrimarHospital() {
		return primarHospital;
	}

	/**
	 * @param primarHospital
	 *            the primarHospital to set
	 */
	public void setPrimarHospital(String primarHospital) {
		this.primarHospital = primarHospital;
	}

	/**
	 * @return the isPrimary
	 */
	public String getIsPrimary() {
		return isPrimary;
	}

	/**
	 * @param isPrimary
	 *            the isPrimary to set
	 */
	public void setIsPrimary(String isPrimary) {
		this.isPrimary = isPrimary;
	}

	/**
	 * @return the sec_hosp_id
	 */
	public String getSec_hosp_id() {
		return sec_hosp_id;
	}

	/**
	 * @param sec_hosp_id
	 *            the sec_hosp_id to set
	 */
	public void setSec_hosp_id(String sec_hosp_id) {
		this.sec_hosp_id = sec_hosp_id;
	}

	/**
	 * @return the prim_hosp_id
	 */
	public String getPrim_hosp_id() {
		return prim_hosp_id;
	}

	/**
	 * @param prim_hosp_id
	 *            the prim_hosp_id to set
	 */
	public void setPrim_hosp_id(String prim_hosp_id) {
		this.prim_hosp_id = prim_hosp_id;
	}

	/**
	 * @return the designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * @param designation
	 *            the designation to set
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
	 * @param jobTitle
	 *            the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return the practiseType
	 */
	public String getPractiseType() {
		return practiseType;
	}

	/**
	 * @param practiseType
	 *            the practiseType to set
	 */
	public void setPractiseType(String practiseType) {
		this.practiseType = practiseType;
	}

	/**
	 * @return the subscription
	 */
	public ItemType getSubscription() {
		return subscription;
	}

	/**
	 * @param subscription
	 *            the subscription to set
	 */
	public void setSubscription(ItemType subscription) {
		this.subscription = subscription;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return the memberList
	 */
	public List<RosterVCardBo> getMemberList() {
		return memberList;
	}

	/**
	 * @param memberList
	 *            the memberList to set
	 */
	public void setMemberList(List<RosterVCardBo> memberList) {
		this.memberList = memberList;
	}

	/**
	 * @return the bday
	 */
	public String getBday() {
		return bday;
	}

	/**
	 * @param bday
	 *            the bday to set
	 */
	public void setBday(String bday) {
		this.bday = bday;
	}

	/**
	 * @return the isGroup
	 */
	public boolean isGroup() {
		return isGroup;
	}

	/**
	 * @param isGroup
	 *            the isGroup to set
	 */
	public void setIsGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	/**
	 * @return the groupHasPatient
	 */
	public boolean groupHasPatient() {
		return groupHasPatient;
	}

	/**
	 * @param groupHasPatient
	 *            the groupHasPatient to set
	 */
	public void setGroupHasPatient(boolean groupHasPatient) {
		this.groupHasPatient = groupHasPatient;
	}

	/**
	 * @return the groupSubject
	 */
	public String getGroupSubject() {
		return groupSubject;
	}

	/**
	 * @param groupSubject
	 *            the groupSubject to set
	 */
	public void setGroupSubject(String groupSubject) {
		this.groupSubject = groupSubject;
	}

	/**
	 * @return the isGroupOwner
	 */
	public boolean isGroupOwner() {
		return isGroupOwner;
	}

	/**
	 * @param isGroupOwner
	 *            the isGroupOwner to set
	 */
	public void setGroupOwner(boolean isGroupOwner) {
		this.isGroupOwner = isGroupOwner;
	}

	/**
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the isJoined
	 */
	public boolean isJoined() {
		return isJoined;
	}

	/**
	 * @param isJoined
	 *            the isJoined to set
	 */
	public void setJoined(boolean isJoined) {
		this.isJoined = isJoined;
	}

	/**
	 * @return the groupJid
	 */
	public String getGroupJid() {
		return groupJid;
	}

	/**
	 * @param groupJid
	 *            the groupJid to set
	 */
	public void setGroupJid(String groupJid) {
		this.groupJid = groupJid;
	}

	/**
	 * @return the groupMemberId
	 */
	public String getGroupMemberId() {
		return groupMemberId;
	}

	/**
	 * @param groupMemberId
	 *            the groupMemberId to set
	 */
	public void setGroupMemberId(String groupMemberId) {
		this.groupMemberId = groupMemberId;
	}

	/**
	 * @return the membership
	 */
	public String getMembership() {
		return membership;
	}

	/**
	 * @param membership the membership to set
	 */
	public void setMembership(String membership) {
		this.membership = membership;
	}

	/*@Override
	public boolean equals(Object obj) {
		return this.userId.equals(((RosterVCardBo) obj).userId);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}*/
	
	public void refresh()
	{
		System.out.println("Refresh called");
		if(this.title!=null)
		{
		StringBuffer titleBuffer = new StringBuffer();
		titleBuffer.append(getJobTitle() == null ? "" : getJobTitle());
		String title = titleBuffer.toString();
		if (title.length() > 20) {
			title = title.substring(0, 20) + "...";
		}
		
		this.title.setText("<html><font style='color:#9CCD21;'>" + getName() + "</font><br/>" + title + "</html>");
		
		BufferedImage icon = Util.getProfileImg(userId);
		if(icon!=null)
		{
			
			try {
				JLabel tmp = Util.combine(icon, false, 60, 60);
				this.title.setIcon(tmp.getIcon());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		}
	}
}

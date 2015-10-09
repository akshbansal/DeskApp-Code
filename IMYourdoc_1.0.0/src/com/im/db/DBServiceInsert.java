package com.im.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.im.bo.ActiveUserBo;
import com.im.bo.ConversationBo;
import com.im.bo.GroupBo;
import com.im.bo.LoginUserBO;
import com.im.bo.MessageBo;
import com.im.bo.MessageSyncBo;
import com.im.bo.RosterVCardBo;
import com.im.utils.Constants;
import com.im.utils.EncryptDecryptData;
import com.im.utils.XmppUtils;

public class DBServiceInsert {

	Connection conn;
	EncryptDecryptData encryptDecryptData;
	public DBServiceInsert() {
		conn = DBConnection.dbConnection();
		encryptDecryptData = new EncryptDecryptData();
	}

	public boolean insertUserTable(LoginUserBO loginBo) {
		PreparedStatement pstmt = null;
		try {
		
			pstmt = conn.prepareStatement("insert or replace into LOGIN_USER(USERNAME,LOGIN_TOKEN,USER_PIN,"
					+ "USER_TYPE,NAME,PROFILE_IMAGE, SEQ_QUES,SEQ_ANS) values(?,?,?,?,?,?,?,?)");

			pstmt.setString(1, loginBo.getUsername().split("@")[0]);
			pstmt.setString(2, loginBo.getLogin_token());
			pstmt.setString(3, loginBo.getUser_pin());
			String usertype = loginBo.getUser_type();
			if(StringUtils.isEmpty(usertype))
			{
				if(loginBo.isProvider()){
					usertype = "Physician";
				}
				else if(loginBo.isStaff())
				{
					usertype = "Staff";
				}
				else if(loginBo.isPatient())
				{
					usertype = "Patient";
				}
				else
				{
					usertype ="group";
				}
					
			}
			pstmt.setString(4, usertype);
			pstmt.setString(5, loginBo.getName());
			pstmt.setBytes(6,  loginBo.getProfileImage());
			pstmt.setString(7, loginBo.getSeq_ques());
			pstmt.setString(8, loginBo.getSeq_ans());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized boolean insertRosterTable(RosterVCardBo vcard) {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn.prepareStatement("insert or replace into ROSTER_TABLE(ROSTER_JID,EMAIL_ID,NAME,PROFILE_IMAGE, USER_TYPE,IS_GROUP,"
					+ "IS_JOINED,JOB_TITLE ,DESIGNATION, SUBSCRIPTION, USER_ID) values(?,?,?,?,?,?,?,?,?,?,(select USER_ID from LOGIN_USER"
					+ " where USERNAME =?))");

			pstmt.setString(1, vcard.getUserId().split("@")[0]);
			pstmt.setString(2, vcard.getEmail());
			pstmt.setString(3, vcard.getName());
			pstmt.setBytes(4, vcard.getProfileImage());
			pstmt.setString(5, vcard.getUserType()==null?"":vcard.getUserType());
			pstmt.setBoolean(6, vcard.isGroup());
			pstmt.setBoolean(7, true);
			pstmt.setString(8, vcard.getJobTitle()==null?"":vcard.getJobTitle());
			pstmt.setString(9, vcard.getDesignation()==null?"":vcard.getDesignation());
			pstmt.setString(10, vcard.getSubscription().toString());
			pstmt.setString(11, Constants.loggedinuserInfo.username.split("@")[0]);
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	public synchronized boolean insertRosterTableGroup(RosterVCardBo vcard) {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn.prepareStatement("insert or replace into ROSTER_TABLE(ROSTER_JID,EMAIL_ID,NAME,PROFILE_IMAGE, USER_TYPE,IS_GROUP,"
					+ "JOB_TITLE ,DESIGNATION, SUBSCRIPTION,HAS_PATIENT ,SUBJECT,IS_JOINED,USER_ID) values(?,?,?,?,?,?,?,?,?,?,?,?,(select USER_ID from LOGIN_USER"
					+ " where USERNAME =?))");

			pstmt.setString(1, vcard.getUserId().split("@")[0]);
			pstmt.setString(2, vcard.getEmail()==null?"":vcard.getEmail());
			pstmt.setString(3, vcard.getName()==null?"":vcard.getName());
			pstmt.setBytes(4, vcard.getProfileImage());
			pstmt.setString(5, vcard.getUserType()==null?"":vcard.getUserType());
			pstmt.setBoolean(6, vcard.isGroup());
			pstmt.setString(7, vcard.getJobTitle()==null?"":vcard.getJobTitle());
			pstmt.setString(8, vcard.getDesignation()==null?"":vcard.getDesignation());
			pstmt.setString(9, vcard.getSubscription()==null?"both":vcard.getSubscription().toString());
			pstmt.setBoolean(10, vcard.groupHasPatient());
			//pstmt.setBoolean(11, vcard.isGroupOwner());
			String subject = vcard.getGroupSubject();
			if(StringUtils.isEmpty(subject))
			{
				subject = vcard.getName();
			}
			if(StringUtils.isEmpty(subject)){
				subject = vcard.getUserId().split("@")[0];
			}
			pstmt.setString(11, vcard.getGroupSubject()==null?vcard.getName():vcard.getGroupSubject());
			pstmt.setBoolean(12, vcard.isJoined());
			pstmt.setString(13 , Constants.loggedinuserInfo.username.split("@")[0]);
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * SQL injection safe batch
	 * @param vcard
	 * @return
	 */
	
	public  void insertGroupTable(RosterVCardBo vcard) {
		PreparedStatement pstmt = null;
		boolean statusFinal= false;
		List<RosterVCardBo> list = vcard.getMemberList();
		
		try {
			
				for (RosterVCardBo vcardMember : list) {
					synchronized (vcardMember) {
						insertGroupMemberTable(vcardMember,vcard.getUserId().split("@")[0]);
				}
			}
				
//					String query = "insert into GROUP_MEMBER_TABLE(GROUP_MEMBER_ID,GROUP_MEMBER_TYPE,"
//							+ "CREATION_DATE,IS_JOINED,GROUP_OWNER,USER_ID,ROSTER_ID) values(?,?,?,?,?,(select USER_ID from LOGIN_USER"
//							+ " where USERNAME =?),(select ROSTER_ID from ROSTER_TABLE"
//							+ " where ROSTER_JID =?))";
//					pstmt = conn.prepareStatement(query);
//					pstmt.setString(1, vcardMember.getUserId().split("@")[0]);
//					pstmt.setString(2, vcardMember.getUserType()==null?"":vcardMember.getUserType());
//					String time = vcard.getUserId().split("_")[1];
//					pstmt.setString(3, time.split("@")[0]);
//					pstmt.setBoolean(4, vcard.isJoined());
//					pstmt.setBoolean(5, vcardMember.isGroupOwner());
//					pstmt.setString(6, Constants.loggedinuserInfo.username.split("@")[0]);
//					pstmt.setString(7, vcard.getUserId().split("@")[0]);
////					pstmt.executeUpdate();
//					pstmt.addBatch();
//				int[] status = pstmt.executeBatch();
//				for(int s:status){
//					if(s>=0){
//						statusFinal = true;
//					}
//					System.out.println(s);
//				}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public synchronized boolean insertGroupMemberTable(RosterVCardBo vcard,String roomId) {
		PreparedStatement pstmt = null;
		try {
				conn = DBConnection.dbConnection();
				pstmt = conn.prepareStatement("insert or replace into GROUP_MEMBER_TABLE(GROUP_MEMBER_ID,GROUP_MEMBER_TYPE,"
						+ "CREATION_DATE,MEMBER_SHIP,USER_ID,ROSTER_ID) values(?,?,?,?,(select USER_ID from LOGIN_USER"
						+ " where USERNAME =?),(select ROSTER_ID from ROSTER_TABLE where ROSTER_JID =?))");
				pstmt.setString(1, vcard.getUserId().split("@")[0]);
				pstmt.setString(2, vcard.getUserType()==null?"":vcard.getUserType());
				String time = roomId.split("_")[1];
				pstmt.setString(3, time.split("@")[0]);
				if(vcard.getUserId().split("@")[0].equals(roomId.split("_")[0])){
					pstmt.setString(4, "Owner");
				}
				else
				{
					pstmt.setString(4, "Member");
				}
				pstmt.setString(5, Constants.loggedinuserInfo.username.split("@")[0]);
				pstmt.setString(6, roomId.split("@")[0]);
				pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	public  boolean insertActiveUsers(ActiveUserBo activeUserBo){
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT or replace INTO ACTIVE_USERS (BUDDY_JID,LAST_ACTIVE_DATE,LAST_ACTIVE_SECS) VALUES (?,?,?);";
			conn = DBConnection.dbConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, activeUserBo.getUserId());
			pstmt.setString(2, activeUserBo.getLastActiveDate());
			pstmt.setString(3,activeUserBo.getLastActiveSecs());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	public  boolean insertActiveGroups(GroupBo activeUserBo){
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT or replace INTO ACTIVE_USERS (BUDDY_JID,LAST_ACTIVE_DATE,LAST_ACTIVE_SECS) VALUES (?,?,?);";
			conn = DBConnection.dbConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, activeUserBo.getRoomid());
			pstmt.setString(2, activeUserBo.getLastActiveDate());
			pstmt.setString(3,activeUserBo.getLastActiveSecs());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	public boolean insertActiveConversation(ConversationBo conversationBo){
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			String sql = "INSERT or replace INTO CONVERSATION_SESSION (WITH,START) VALUES (?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, conversationBo.getWith());
			pstmt.setString(2,  conversationBo.getStart());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	public boolean insertMessageTable(MessageBo messageBo) {
		PreparedStatement pstmt = null;
		try {
			String sql = "INSERT INTO MESSAGE_TABLE (MESSAGE_ID,CONTENT,MESSAGE_FROMJID,MESSAGE_TOJID,FILE_TYPE,FILE_URL,CHAT_STATUS,"
					+ " CHAT_TYPE,CHAT_DATE_OF_CREATION,CHAT_GROUPMEMBER_ID) VALUES (?,?,?,?,?,?,?,?,?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, messageBo.getMessageId());
			pstmt.setString(2,  messageBo.getContent());
			pstmt.setString(3,messageBo.getFrom());
			pstmt.setString(4,messageBo.getTo());
			pstmt.setString(5, messageBo.getFileType());
			pstmt.setString(6, messageBo.getFilePath());
			pstmt.setString(7 , messageBo.getStatus());
			pstmt.setString(8 , messageBo.getChatType());
			pstmt.setString(9 , messageBo.getTimeStamp());
			pstmt.setString(10 , messageBo.getGroupMemberId());
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	public boolean insertMessageSyncTable(MessageSyncBo messageSyncBo) {
		PreparedStatement pstmt = null;
		boolean status = false; 
		try {
			conn = DBConnection.dbConnection();
				String body = messageSyncBo.getContent();
				String msgID = messageSyncBo.getMessageID();
				String from = messageSyncBo.getMessageFrom();
				String to = messageSyncBo.getMessageTo();
				String fileType=""; 
				String filePath = "";
					fileType = messageSyncBo.getFileType();
					filePath = messageSyncBo.getFileUrl();
				String timeStamp = messageSyncBo.getChatDate();
				String sql = "INSERT or replace INTO MESSAGE_TABLE_SYNC (MESSAGE_ID,MESSAGE_SECS,CONTENT,MESSAGE_FROMJID,"
						+ "MESSAGE_TOJID,FILE_TYPE,"
						+ "FILE_URL,CHAT_DATE_OF_CREATION,MESSAGE_WITH) VALUES (?,?,?,?,?,?,?,?,?);";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,msgID);
				pstmt.setString(2,messageSyncBo.getSeconds());
				pstmt.setString(3,body);
				pstmt.setString(4,from);
				pstmt.setString(5,to);
				pstmt.setString(6,fileType);
				pstmt.setString(7,filePath);
				pstmt.setString(8,timeStamp);
				pstmt.setString(9,messageSyncBo.getWith());
				pstmt.executeUpdate();
				status = true;
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		} finally {
			try {
				if(pstmt!=null)
					pstmt.close();
				if(conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return status;
	}
	
}

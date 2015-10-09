package com.im.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.packet.RosterPacket.ItemType;

import com.im.bo.ActiveUserBo;
import com.im.bo.ConversationBo;
import com.im.bo.LoginUserBO;
import com.im.bo.MessageSyncBo;
import com.im.bo.RosterVCardBo;
import com.im.utils.Constants;
import com.im.utils.EncryptDecryptData;
import com.im.utils.XmppUtils;

public class DBServiceResult {

	private Connection conn;
	EncryptDecryptData encryptDecryptData;
	public DBServiceResult() {
		conn = DBConnection.dbConnection();
		encryptDecryptData = new EncryptDecryptData();
	}

	public LoginUserBO getLoginUserDetails(String username) {
		LoginUserBO loginBo = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from LOGIN_USER where username ='" + username + "';");
			while (resultSet.next()) {
				loginBo	= new LoginUserBO();
				loginBo.setUsername((resultSet.getString("USERNAME")));
				loginBo.setName((resultSet.getString("NAME")));
				loginBo.setLogin_token(resultSet.getString("LOGIN_TOKEN"));
				loginBo.setUser_pin((resultSet.getString("USER_PIN")));
				loginBo.setUser_type((resultSet.getString("USER_TYPE")));
				loginBo.setProfileImage(resultSet.getBytes("PROFILE_IMAGE"));
				loginBo.setSeq_ques((resultSet.getString("SEQ_QUES")));
				loginBo.setSeq_ans((resultSet.getString("SEQ_ANS")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				resultSet.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return loginBo;
	}
	
	public synchronized RosterVCardBo getRosterUserDetails(String rosterJid) {
		RosterVCardBo rosterbo = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		if(rosterJid.contains("@"))
			rosterJid = rosterJid.split("@")[0];
		try {
			conn = DBConnection.dbConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from ROSTER_TABLE where ROSTER_JID='"+(rosterJid)+
					"' and USER_ID = (select USER_ID from LOGIN_USER where USERNAME = '"+
					(Constants.loggedinuserInfo.username.split("@")[0])+"');");
			while (resultSet.next()) {
				rosterbo = new RosterVCardBo();
				rosterbo.setUserId((resultSet.getString("ROSTER_JID")));
				rosterbo.setName((resultSet.getString("NAME")));
				rosterbo.setEmail((resultSet.getString("EMAIL_ID")));
				rosterbo.setUserType((resultSet.getString("USER_TYPE")));
				rosterbo.setIsGroup(resultSet.getBoolean("IS_GROUP"));
				rosterbo.setGroupHasPatient(resultSet.getBoolean("HAS_PATIENT"));
				rosterbo.setGroupSubject((resultSet.getString("SUBJECT")==null?"":resultSet.getString("SUBJECT")));
				rosterbo.setJobTitle((resultSet.getString("JOB_TITLE")));
				rosterbo.setProfileImage(resultSet.getBytes("PROFILE_IMAGE"));
				rosterbo.setDesignation((resultSet.getString("DESIGNATION")));
				rosterbo.setSubscription(ItemType.valueOf(resultSet.getString("SUBSCRIPTION")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rosterbo;
	}
	public RosterVCardBo getRosterGroupDetails(String rosterJid) {
		RosterVCardBo rosterbo = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		if(rosterJid.contains("@"))
			rosterJid = rosterJid.split("@")[0];
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from ROSTER_TABLE where ROSTER_JID='"+(rosterJid)+
					"' and USER_ID = (select USER_ID from LOGIN_USER where USERNAME = ' and IS_GROUP = 1"+
					(Constants.loggedinuserInfo.username.split("@")[0])+"');");
			while (resultSet.next()) {
				rosterbo = new RosterVCardBo();
				rosterbo.setUserId((resultSet.getString("ROSTER_JID")));
				rosterbo.setName((resultSet.getString("NAME")));
				rosterbo.setEmail((resultSet.getString("EMAIL_ID")));
				rosterbo.setUserType((resultSet.getString("USER_TYPE")));
				rosterbo.setIsGroup(resultSet.getBoolean("IS_GROUP"));
				rosterbo.setGroupHasPatient(resultSet.getBoolean("HAS_PATIENT"));
				rosterbo.setGroupSubject((resultSet.getString("SUBJECT")==null?"":resultSet.getString("SUBJECT")));
				rosterbo.setJobTitle((resultSet.getString("JOB_TITLE")));
				rosterbo.setProfileImage(resultSet.getBytes("PROFILE_IMAGE"));
				Map<String,String> map = XmppUtils.roomUserIds(rosterJid+"@newconversation.imyourdoc.com");
				List<RosterVCardBo> list = new ArrayList<RosterVCardBo>();
				for(String uid:map.keySet()){
					RosterVCardBo vcard = XmppUtils.getVCardBo(uid);
					list.add(vcard);
				}
				rosterbo.setMemberList(list);
				rosterbo.setDesignation((resultSet.getString("DESIGNATION")));
				rosterbo.setSubscription(ItemType.valueOf(resultSet.getString("SUBSCRIPTION")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (resultSet != null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rosterbo;
	}
	public List<RosterVCardBo> getGroupDetailsAll(String rosterJid) {
		RosterVCardBo rosterbo = null;
		List<RosterVCardBo> list = new ArrayList<RosterVCardBo>();
		Statement stmt = null;
		ResultSet resultSet = null;
		if(rosterJid.contains("@newconversation.imyourdoc.com"))
			rosterJid = rosterJid.split("@")[0];
		try {
			stmt = conn.createStatement();
			
			resultSet = stmt.executeQuery("select R.ROSTER_JID, R.NAME,R.SUBJECT,R.SUBSCRIPTION,R.USER_TYPE,R.IS_GROUP, G.GROUP_JID,"
					+ "G.GROUP_MEMBER_ID,G.CREATION_DATE,G.IS_JOINED,"
					+ " G.GROUP_OWNER from GROUP_MEMBER_TABLE G, ROSTER_TABLE R where G.GROUP_JID = R.ROSTER_JID "
					+ " and G.IS_JOINED = 1 AND R.IS_JOINED = 1"
					+ " and G.GROUP_JID = '"+(rosterJid)+"' and  G.USER_ID = "
					+ "(select USER_ID from LOGIN_USER where USERNAME = '"
					+ (Constants.loggedinuserInfo.username.split("@")[0])+"');");
			while (resultSet.next()) {
				rosterbo = new RosterVCardBo();
				rosterbo.setUserId((resultSet.getString(1)));
				rosterbo.setName((resultSet.getString(2)));
				rosterbo.setGroupSubject((resultSet.getString(3)));
				rosterbo.setSubscription(ItemType.valueOf(resultSet.getString(4)));
				rosterbo.setUserType((resultSet.getString(5)));
				rosterbo.setIsGroup(resultSet.getBoolean(6));
				rosterbo.setGroupJid((resultSet.getString(7)));
				rosterbo.setGroupMemberId((resultSet.getString(8)));
				rosterbo.setCreationDate(resultSet.getString(9));
				rosterbo.setJoined(resultSet.getBoolean(10));
				list.add(rosterbo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public RosterVCardBo getGroupDetailsForLoginUser(String roomid) {
		RosterVCardBo rosterbo = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		if(roomid.contains("@newconversation.imyourdoc.com"))
			roomid = roomid.split("@")[0];
		try {
			stmt = conn.createStatement();
			
			resultSet = stmt.executeQuery("select R.ROSTER_JID, R.NAME,R.SUBJECT,R.SUBSCRIPTION,R.USER_TYPE,R.IS_GROUP, G.GROUP_JID,"
					+ "G.GROUP_MEMBER_ID,G.CREATION_DATE,G.IS_JOINED,"
					+ " G.GROUP_OWNER from GROUP_MEMBER_TABLE G, ROSTER_TABLE R where G.GROUP_JID = R.ROSTER_JID "
					+ " and G.IS_JOINED = 1 AND R.IS_JOINED = 1"
					+ " and G.GROUP_MEMBER_ID = '"+(Constants.loggedinuserInfo.username.split("@")[0])+"' "
					+ " and G.GROUP_JID = '"+(roomid)+"' and  G.USER_ID = "
					+ "(select USER_ID from LOGIN_USER where USERNAME = '"
					+ (Constants.loggedinuserInfo.username.split("@")[0])+"');");
			while (resultSet.next()) {
				rosterbo = new RosterVCardBo();
				rosterbo.setUserId((resultSet.getString(1)));
				rosterbo.setName((resultSet.getString(2)));
				rosterbo.setGroupSubject((resultSet.getString(3)));
				rosterbo.setSubscription(ItemType.valueOf(resultSet.getString(4)));
				rosterbo.setUserType((resultSet.getString(5)));
				rosterbo.setIsGroup(resultSet.getBoolean(6));
				rosterbo.setGroupJid((resultSet.getString(7)));
				rosterbo.setGroupMemberId((resultSet.getString(8)));
				rosterbo.setCreationDate(resultSet.getString(9));
				rosterbo.setJoined(resultSet.getBoolean(10));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(resultSet!=null)
				resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rosterbo;
	}
	
	public synchronized List<RosterVCardBo> getGroupDetails(String rosterJid) {
		List<RosterVCardBo> list = new ArrayList<RosterVCardBo>();
		RosterVCardBo rosterbo = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		if(rosterJid.contains("@"))
			rosterJid = rosterJid.split("@")[0];
		try {
			conn = DBConnection.dbConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from GROUP_MEMBER_TABLE where ROSTER_ID = (SELECT ROSTER_ID FROM ROSTER_TABLE WHERE ROSTER_JID='"+(rosterJid)+"') "
					+ " AND USER_ID = (SELECT USER_ID FROM LOGIN_USER WHERE USERNAME='"+
					(Constants.loggedinuserInfo.username.split("@")[0])+"');");
			while (resultSet.next()) {
				rosterbo = new RosterVCardBo();
				rosterbo.setUserId((resultSet.getString("GROUP_MEMBER_ID")));
				//rosterbo.setName((resultSet.getString("NAME")));
				//rosterbo.setEmail((resultSet.getString("EMAIL_ID")));
				rosterbo.setUserType((resultSet.getString("GROUP_MEMBER_TYPE")));
				//rosterbo.setJobTitle((resultSet.getString("JOB_TITLE")));
				//rosterbo.setProfileImage(resultSet.getBytes("PROFILE_IMAGE"));
				//rosterbo.setDesignation((resultSet.getString("DESIGNATION")));]
				//rosterbo.setSubscription(ItemType.valueOf(resultSet.getString("SUBSCRIPTION")));
				rosterbo.setMembership(resultSet.getString("MEMBER_SHIP"));
				list.add(rosterbo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(resultSet!=null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	public String getChatStatus(String messageId){
		Statement stmt = null;
		ResultSet resultSet = null;
		String status = "";
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select CHAT_STATUS from MESSAGE_TABLE where MESSAGE_ID = '"+messageId+"';");
			while (resultSet.next()) {
				status = resultSet.getString("CHAT_STATUS");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(resultSet!=null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return status;
	}
	public synchronized List<RosterVCardBo> getRosterList(String username, String type) {
		List<RosterVCardBo> list = new ArrayList<RosterVCardBo>();
		Statement stmt = null;
		ResultSet resultSet = null;
		
		try {
			conn = DBConnection.dbConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from ROSTER_TABLE where user_id= "
				     + "(select user_id from login_user where username='" + (username) + "') AND USER_TYPE = '" + (type) + "';");
			while (resultSet.next()) {
				RosterVCardBo vcard = new RosterVCardBo();
				vcard.setUserId((resultSet.getString("ROSTER_JID")));
				vcard.setEmail((resultSet.getString("EMAIL_ID")));
				vcard.setName((resultSet.getString("NAME")));
				vcard.setProfileImage(resultSet.getBytes("PROFILE_IMAGE"));
				vcard.setUserType((resultSet.getString("USER_TYPE")));
				vcard.setIsGroup(resultSet.getBoolean("IS_GROUP"));
				vcard.setJobTitle((resultSet.getString("JOB_TITLE")));
				vcard.setDesignation((resultSet.getString("DESIGNATION")));
				vcard.setSubscription(ItemType.valueOf(resultSet.getString("SUBSCRIPTION")));
				list.add(vcard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(resultSet!=null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public synchronized List<RosterVCardBo> getRosterListAll(String username) {
		List<RosterVCardBo> list = new ArrayList<RosterVCardBo>();
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
				stmt = conn.createStatement();
				resultSet = stmt.executeQuery("select * from ROSTER_TABLE where is_joined = 1 and is_group = 0 and user_id= "
					     + "(select user_id from login_user where username='" + (username) + "') ;");
				while (resultSet.next()) {
					RosterVCardBo vcardContacts = new RosterVCardBo();
					vcardContacts.setUserId((resultSet.getString("ROSTER_JID")));
					vcardContacts.setEmail((resultSet.getString("EMAIL_ID")));
					vcardContacts.setName((resultSet.getString("NAME")));
					vcardContacts.setProfileImage(resultSet.getBytes("PROFILE_IMAGE"));
					
					vcardContacts.setIsGroup(resultSet.getBoolean("IS_GROUP"));
					vcardContacts.setJobTitle((resultSet.getString("JOB_TITLE")));
					vcardContacts.setDesignation((resultSet.getString("DESIGNATION")));
					vcardContacts.setSubscription(ItemType.valueOf(resultSet.getString("SUBSCRIPTION")));
					vcardContacts.setUserType((resultSet.getString("USER_TYPE")));
					vcardContacts.setGroupSubject((resultSet.getString("SUBJECT")==null?"":resultSet.getString("SUBJECT")));
					vcardContacts.setGroupHasPatient(resultSet.getBoolean("HAS_PATIENT"));
					//vcard.setGroupOwner(resultSet.getBoolean("GROUP_OWNER"));
					vcardContacts.setJoined(resultSet.getBoolean("IS_JOINED"));
					list.add(vcardContacts);
				}
				stmt2 = conn.createStatement();
				resultSet2 = stmt2.executeQuery("select * from ROSTER_TABLE where is_joined =1 and is_group = 1 and user_id= "
					     + "(select user_id from login_user where username='" + (username) + "') ;");
				while (resultSet2.next()) {
					RosterVCardBo vcardGroups = new RosterVCardBo();
					vcardGroups.setUserId((resultSet2.getString("ROSTER_JID")));
					vcardGroups.setEmail((resultSet2.getString("EMAIL_ID")));
					vcardGroups.setName((resultSet2.getString("NAME")));
					vcardGroups.setProfileImage(resultSet2.getBytes("PROFILE_IMAGE"));
					
					vcardGroups.setIsGroup(resultSet2.getBoolean("IS_GROUP"));
					vcardGroups.setJobTitle((resultSet2.getString("JOB_TITLE")));
					vcardGroups.setDesignation((resultSet2.getString("DESIGNATION")));
					vcardGroups.setSubscription(ItemType.valueOf(resultSet2.getString("SUBSCRIPTION")));
					vcardGroups.setUserType((resultSet2.getString("USER_TYPE")));
					vcardGroups.setGroupSubject((resultSet2.getString("SUBJECT")==null?"":resultSet2.getString("SUBJECT")));
					vcardGroups.setGroupHasPatient(resultSet2.getBoolean("HAS_PATIENT"));
					//vcard.setGroupOwner(resultSet.getBoolean("GROUP_OWNER"));
					vcardGroups.setJoined(resultSet2.getBoolean("IS_JOINED"));
					list.add(vcardGroups);
				}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	public List<ActiveUserBo> getActiveUsersFromDb() {
		List<ActiveUserBo> list = new ArrayList<ActiveUserBo>();
		Statement stmt = null;
		ResultSet resultSet = null;
		
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from ACTIVE_USERS;");
			while (resultSet.next()) {
				ActiveUserBo vcard = new ActiveUserBo();
				vcard.setUserId((resultSet.getString("BUDDY_JID")));
				vcard.setLastActiveDate((resultSet.getString("LAST_ACTIVE_DATE")));
				vcard.setLastActiveSecs((resultSet.getString("LAST_ACTIVE_SECS")));
				list.add(vcard);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	public List<ConversationBo> getConversationSessionFromDb() {
		List<ConversationBo> list = new ArrayList<ConversationBo>();
		Statement stmt = null;
		ResultSet resultSet = null;
		
		try {
			conn = DBConnection.dbConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from CONVERSATION_SESSION;");
			while (resultSet.next()) {
				ConversationBo convBo = new ConversationBo();
				convBo.setWith((resultSet.getString("WITH")));
				convBo.setStart((resultSet.getString("START")));
				list.add(convBo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(resultSet!=null)
					resultSet.close();
				if (stmt != null)
					stmt.close();
				
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public synchronized List<MessageSyncBo> getSyncMessagesDB(String with) {
		List<MessageSyncBo> list = new ArrayList<MessageSyncBo>();
		Statement stmt = null;
		ResultSet resultSet = null;
		Connection conn = null;
		try {
			conn = DBConnection.dbConnection();
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery("select * from MESSAGE_TABLE_SYNC where MESSAGE_WITH='" + with + "';");
			while (resultSet.next()) {
				MessageSyncBo messageSyncBo = new MessageSyncBo();
				messageSyncBo.setWith(resultSet.getString("MESSAGE_WITH"));
				messageSyncBo.setSeconds(resultSet.getString("MESSAGE_SECS"));
				messageSyncBo.setContent(resultSet.getString("CONTENT"));
				messageSyncBo.setMessageID(resultSet.getString("MESSAGE_ID"));
				messageSyncBo.setMessageFrom(resultSet.getString("MESSAGE_FROMJID"));
				messageSyncBo.setMessageTo(resultSet.getString("MESSAGE_TOJID"));
				messageSyncBo.setChatDate(resultSet.getString("CHAT_DATE_OF_CREATION"));
				messageSyncBo.setFileType(resultSet.getString("FILE_TYPE"));
				messageSyncBo.setFileUrl(resultSet.getString("FILE_URL"));
				list.add(messageSyncBo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(resultSet, stmt, conn);
		}
		return list;
	}
	
	private static void closeAll(ResultSet resultSet, Statement stmt, Connection conn) {
		try {
			if (resultSet != null)
				resultSet.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

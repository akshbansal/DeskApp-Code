package com.im.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.im.bo.LoginUserBO;
import com.im.bo.MessageBo;
import com.im.bo.RosterVCardBo;
import com.im.utils.Constants;
import com.im.utils.EncryptDecryptData;

public class DBServiceUpdate {

	Connection conn;
	EncryptDecryptData encryptDecryptData;

	public DBServiceUpdate() {
		conn = DBConnection.dbConnection();
		encryptDecryptData = new EncryptDecryptData();
	}

	public boolean updateData(LoginUserBO loginBo) {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn
					.prepareStatement("UPDATE  LOGIN_USER SET LOGIN_TOKEN=?,USER_PIN=?,"
							+ "USER_TYPE=?,NAME=?,PROFILE_IMAGE =?, SEQ_QUES = ?,SEQ_ANS = ? WHERE USERNAME =?");

			pstmt.setString(1, loginBo.getLogin_token());
			pstmt.setString(2,
					(loginBo.getUser_pin()));
			pstmt.setString(3,
					(loginBo.getUser_type()));
			pstmt.setString(4,
					(loginBo.getName()));
			pstmt.setBytes(5, loginBo.getProfileImage());
			pstmt.setString(6,
					(loginBo.getSeq_ques()));
			pstmt.setString(7,
					(loginBo.getSeq_ans()));
			pstmt.setString(8,
					(loginBo.getUsername()));
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

	public boolean updateRosterTable(RosterVCardBo vcard) {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn
					.prepareStatement("UPDATE ROSTER_TABLE SET EMAIL_ID=?,PROFILE_IMAGE=?, USER_TYPE=?,IS_GROUP=?,"
							+ "JOB_TITLE=? ,DESIGNATION=?, SUBSCRIPTION=? WHERE ROSTER_JID =? AND USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");

			pstmt.setString(1,
					(vcard.getEmail()));
			pstmt.setBytes(2, vcard.getProfileImage());
			pstmt.setString(3,
					(vcard.getUserType()));
			pstmt.setBoolean(4, vcard.isGroup());
			pstmt.setString(5, (vcard
					.getJobTitle() == null ? "" : vcard.getJobTitle()));
			pstmt.setString(6, (vcard
					.getDesignation() == null ? "" : vcard.getDesignation()));
			pstmt.setString(7, vcard.getSubscription().toString());
			pstmt.setString(8,
					(vcard.getUserId()));
			pstmt.setString(9, (Constants.loggedinuserInfo.username
							.split("@")[0]));
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

	public boolean updateMessageStatusTable(String status, String msgId,String groupmemberID) {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn.prepareStatement("UPDATE MESSAGE_TABLE SET CHAT_STATUS='"+status+"' , CHAT_GROUPMEMBER_ID='"+groupmemberID+"' WHERE MESSAGE_ID ='"+msgId+"';");
/*
			pstmt.setString(1, status);
			pstmt.setString(2, groupmemberID);
			pstmt.setString(3, msgId);*/
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

	public boolean updateRosterTableGroup(RosterVCardBo vcard) {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn
					.prepareStatement("UPDATE ROSTER_TABLE SET EMAIL_ID=?,PROFILE_IMAGE=?, USER_TYPE=?,IS_GROUP=?,"
							+ "JOB_TITLE=? ,DESIGNATION=?, SUBSCRIPTION=?,HAS_PATIENT=?,SUBJECT=?,IS_JOINED=? WHERE ROSTER_JID =? AND USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");

			pstmt.setString(1, (vcard
					.getUserId().split("@")[0]));
			pstmt.setString(2, (vcard
					.getEmail() == null ? "" : vcard.getEmail()));
			pstmt.setString(3, (vcard
					.getName() == null ? "" : vcard.getName()));
			pstmt.setBytes(4, vcard.getProfileImage());
			pstmt.setString(5, (vcard
					.getUserType() == null ? "" : vcard.getUserType()));
			pstmt.setBoolean(6, vcard.isGroup());
			pstmt.setString(7, (vcard
					.getJobTitle() == null ? "" : vcard.getJobTitle()));
			pstmt.setString(8, (vcard
					.getDesignation() == null ? "" : vcard.getDesignation()));
			pstmt.setString(9, vcard.getSubscription().toString());
			pstmt.setBoolean(10, vcard.groupHasPatient());
			// pstmt.setBoolean(11, vcard.isGroupOwner());
			pstmt.setString(11, (vcard
					.getGroupSubject() == null ? "" : vcard.getGroupSubject()));
			pstmt.setBoolean(12, vcard.isJoined());
			pstmt.setString(13, (Constants.loggedinuserInfo.username
							.split("@")[0]));
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

	public boolean DeleteGroup(String RoomId) {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn
					.prepareStatement("delete from ROSTER_TABLE where is_group = 1 AND ROSTER_JID=? AND USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");
			pstmt.setString(1, RoomId.split("@")[0]);
			pstmt.setString(2, (Constants.loggedinuserInfo.username
							.split("@")[0]));

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
	public boolean dropRosterTable() {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn
					.prepareStatement("delete from ROSTER_TABLE where is_group = 0 AND USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");
			pstmt.setString(1, (Constants.loggedinuserInfo.username
							.split("@")[0]));

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
	public boolean clearAllData() {
		Statement pstmt = null;
		try {

			String queries[] = {

			"DROP TABLE IF EXISTS LOGIN_USER;",
			"DROP TABLE IF EXISTS ROSTER_TABLE;",
			"DROP TABLE IF EXISTS GROUP_MEMBER_TABLE;",
			"DROP TABLE IF EXISTS ACTIVE_USERS;",
			"DROP TABLE IF EXISTS CONVERSATION_SESSION;",
			"DROP TABLE IF EXISTS MESSAGE_TABLE_SYNC;",
			"DROP TABLE IF EXISTS MESSAGE_TABLE ;" };

			conn = DBConnection.dbConnection();
			pstmt = conn.createStatement();
			for (String query : queries) {
				pstmt.addBatch(query);
			}

			pstmt.executeBatch();

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

	public boolean clearAllData(String username) {
		PreparedStatement pstmt = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn
					.prepareStatement("delete from ROSTER_TABLE where  USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");
			pstmt.setString(1, (username));

			pstmt.executeUpdate();

			conn = DBConnection.dbConnection();
			pstmt1 = conn
					.prepareStatement("delete from GROUP_MEMBER_TABLE where USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");
			pstmt1.setString(1, (username));

			pstmt1.executeUpdate();

			conn = DBConnection.dbConnection();
			pstmt2 = conn
					.prepareStatement("delete from MESSAGE_TABLE where MESSAGE_FROMJID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? ) OR MESSAGE_TOJID = (SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");
			pstmt2.setString(1, (username));
			pstmt2.setString(2, (username));

			pstmt2.executeUpdate();

			conn = DBConnection.dbConnection();
			pstmt3 = conn
					.prepareStatement("delete from LOGIN_USER where USERNAME = ?;");
			pstmt3.setString(1, (username));
			pstmt3.executeUpdate();

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

	public boolean deleteMemberFromGroup(String roomId, String userId) {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			pstmt = conn
					.prepareStatement("delete from GROUP_MEMBER_TABLE where GROUP_MEMBER_ID=? AND USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? ) AND ROSTER_ID =(SELECT"
							+ " ROSTER_ID FROM ROSTER_TABLE WHERE ROSTER_JID=? ) ;");
			pstmt.setString(1,
					(userId.split("@")[0]));
			pstmt.setString(2,
					(roomId.split("@")[0]));
			pstmt.setString(3, (Constants.loggedinuserInfo.username
							.split("@")[0]));
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

	public boolean updateTableOnLeave(String roomId, String userID) {
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		try {
			conn = DBConnection.dbConnection();
			if (roomId.contains("@newconversation.imyourdoc.com"))
				roomId = roomId.split("@")[0];
			pstmt = conn
					.prepareStatement("UPDATE ROSTER_TABLE SET IS_JOINED = 0 WHERE ROSTER_JID =? AND USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");

			pstmt.setString(1, (roomId));
			pstmt.setString(2,(userID.split("@")[0]));
			pstmt.executeUpdate();

			pstmt2 = conn
					.prepareStatement("UPDATE GROUP_MEMBER_TABLE SET IS_JOINED = 0 WHERE GROUP_JID = ? "
							+ "AND GROUP_MEMBER_ID =?"
							+ "AND USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");

			pstmt2.setString(1, (roomId));
			pstmt2.setString(2,
					(userID.split("@")[0]));
			pstmt2.setString(3,
					(userID.split("@")[0]));
			pstmt2.executeUpdate();

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

	public boolean updateTableOnChangeSubject(String roomId, String subject) {
		PreparedStatement pstmt = null;
		try {
			conn = DBConnection.dbConnection();
			if (roomId.contains("@newconversation.imyourdoc.com"))
				roomId = roomId.split("@")[0];
			pstmt = conn
					.prepareStatement("UPDATE ROSTER_TABLE SET SUBJECT = ? WHERE ROSTER_JID =? AND USER_ID = "
							+ "(SELECT USER_ID FROM LOGIN_USER WHERE USERNAME=? );");

			pstmt.setString(1, (subject));
			pstmt.setString(2,
					(roomId.split("@")[0]));
			pstmt.setString(3, (Constants.loggedinuserInfo.username
							.split("@")[0]));
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

	public boolean insertGroupTable(RosterVCardBo vcard) {
		Statement stmt = null;
		try {
			conn = DBConnection.dbConnection();
			stmt = conn.createStatement();
			for (RosterVCardBo vcardMember : vcard.getMemberList()) {
				String sql = "INSERT INTO GROUP_MEMBER_TABLE "
						+ "(GROUP_JID,GROUP_MEMBER_ID,GROUP_MEMBER_TYPE,ROSTER_ID,USER_ID) VALUES("
						+ "'" + vcard.getUserId() + "'," + "'"
						+ vcardMember.getUserId() + "'," + "'"
						+ vcardMember.getUserType() + "',"
						+ "',(SELECT USER_ID FROM ROSTER_TABLE WHERE"
						+ " ROSTER_ID ='" + vcard.getUserId() + "')"
						+ "',(SELECT USER_ID FROM LOGIN_USER WHERE"
						+ " USERNAME ='"
						+ Constants.loggedinuserInfo.username.split("@")[0]
						+ "')" + " )";
				stmt.executeUpdate(sql);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean insertMessageTable(MessageBo messageBo) {
		Statement stmt = null;
		try {
			conn = DBConnection.dbConnection();
			stmt = conn.createStatement();
			String sql = "INSERT INTO MESSAGE_TABLE "
					+ "(MESSAGE_ID,CONTENT,MESSAGE_FROMJID,MESSAGE_TOJID,FILE_TYPE,FILE_URL,CHAT_STATUS,"
					+ " CHAT_TYPE,CHAT_DATE_OF_CREATION,"
					+ " CHAT_GROUPMEMBER_ID) VALUES " + "('"
					+ messageBo.getMessageId()
					+ "',"
					+ "('"
					+ messageBo.getContent()
					+ "',"
					+ "('"
					+ messageBo.getFrom()
					+ "',"
					+ "('"
					+ messageBo.getTo()
					+ "',"
					+ "('"
					+ messageBo.getFileType()
					+ "',"
					+ "('"
					+ messageBo.getFilePath()
					+ "',"
					+ "('"
					+ messageBo.getStatus()
					+ "',"
					+ "('"
					+ messageBo.getChatType()
					+ "',"
					+ "('"
					+ messageBo.getTimeStamp()
					+ "',"
					+ "('"
					+ messageBo.getGroupMemberId() + "'" + ");";
			stmt.executeUpdate(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
}

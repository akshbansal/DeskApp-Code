package com.im.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class DBServiceCreate {

	static Connection conn;

	public DBServiceCreate() {
		conn = DBConnection.dbConnection();
	}

	public boolean createAllTable() {
		Statement pstmt = null;
		boolean statusFinal= false;
		try {
			List<String> queries = getAllTabelQuaries();
			
			conn = DBConnection.dbConnection();
			pstmt = conn.createStatement();
			for (String query : queries) {
				pstmt.addBatch(query);
			}
			int[] status = pstmt.executeBatch();
			for(int s:status){
				if(s>=0){
					statusFinal = true;
				}
				System.out.println(s);
			}
			
			return statusFinal;
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

	private List<String> getAllTabelQuaries() {
		List<String> quaries = new LinkedList<String>();
		quaries.add("CREATE TABLE IF NOT EXISTS LOGIN_USER " + "(USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " USERNAME   TEXT    NOT NULL UNIQUE, " + " LOGIN_TOKEN TEXT NOT NULL," + " USER_PIN TEXT NOT NULL,"
				+ " USER_TYPE TEXT NOT NULL," + " NAME TEXT NOT NULL," + " PROFILE_IMAGE BLOB NOT NULL," + " SEQ_QUES TEXT NOT NULL,"
				+ " SEQ_ANS TEXT NOT NULL);");

		quaries.add("CREATE TABLE IF NOT EXISTS ROSTER_TABLE " + "(ROSTER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " ROSTER_JID   TEXT  NOT NULL UNIQUE, " + " EMAIL_ID TEXT ,NAME TEXT NOT NULL," + " PROFILE_IMAGE BLOB ,"
				+ " USER_TYPE TEXT NOT NULL," + " IS_GROUP   BOOLEAN NOT NULL,HAS_PATIENT BOOLEAN,"
			    + " SUBJECT TEXT, " + " JOB_TITLE TEXT ," + " DESIGNATION TEXT ,"
				+ " SUBSCRIPTION TEXT," + " USER_ID INTEGER NOT NULL,IS_JOINED BOOLEAN, GROUP_CREATION_DATE TEXT, GROUP_CREATION_SECS TEXT," 
			   // + " UNIQUE (ROSTER_JID, USER_ID),"
				+ " FOREIGN KEY(USER_ID) REFERENCES LOGIN_USER(USER_ID) );");

		quaries.add("CREATE TABLE IF NOT EXISTS GROUP_MEMBER_TABLE " + "(GROUP_MEMBER_ID TEXT NOT NULL," + " GROUP_MEMBER_TYPE TEXT NOT NULL,"
				+ " USER_ID INTEGER NOT NULL,ROSTER_ID INTEGER NOT NULL," + " CREATION_DATE TEXT ,MEMBER_SHIP TEXT NOT NULL,"
				+ " PRIMARY KEY (ROSTER_ID, GROUP_MEMBER_ID)"
				+ " FOREIGN KEY(USER_ID) REFERENCES LOGIN_USER(USER_ID),FOREIGN KEY(ROSTER_ID) REFERENCES ROSTER_TABLE(ROSTER_ID)  ON DELETE CASCADE );");

		quaries.add("CREATE TABLE IF NOT EXISTS MESSAGE_TABLE " + "(MESSAGE_ID TEXT NOT NULL ,"
				+ " CONTENT  TEXT  NOT NULL , " + " MESSAGE_FROMJID TEXT," + " MESSAGE_TOJID TEXT NOT NULL," + " FILE_TYPE TEXT NOT NULL,"
				+ " FILE_URL TEXT NOT NULL," + " CHAT_STATUS TEXT NOT NULL," + " CHAT_TYPE TEXT NOT NULL,"
				+ " CHAT_DATE_OF_CREATION TEXT NOT NULL," + " CHAT_GROUPMEMBER_ID TEXT,"
				+ " PRIMARY KEY (MESSAGE_ID, CHAT_GROUPMEMBER_ID));");
		
		quaries.add("CREATE TABLE IF NOT EXISTS MESSAGE_TABLE_SYNC " + "(MESSAGE_ID PRIMARY KEY,MESSAGE_WITH TEXT ,MESSAGE_SECS TEXT,"
				+ " CONTENT  TEXT  NOT NULL , " + " MESSAGE_FROMJID TEXT," + " MESSAGE_TOJID TEXT NOT NULL," + " FILE_TYPE TEXT NOT NULL,"
				+ " FILE_URL TEXT NOT NULL," + " CHAT_DATE_OF_CREATION TEXT NOT NULL"+");");
		
		quaries.add("CREATE TABLE IF NOT EXISTS ACTIVE_USERS " + "(BUDDY_JID  TEXT  NOT NULL , " + " LAST_ACTIVE_DATE TEXT,"
				+ " LAST_ACTIVE_SECS TEXT, "
				+ " PRIMARY KEY (BUDDY_JID, LAST_ACTIVE_DATE) );");
		
		quaries.add("CREATE TABLE IF NOT EXISTS CONVERSATION_SESSION (WITH TEXT NOT NULL,START TEXT,"
				+ " PRIMARY KEY (WITH, START) );");
		
		/*quaries.add("CREATE TABLE IF NOT EXISTS MESSAGE_READBY_TABLE " + "(MESSAGE_ID TEXT NOT NULL ,"
				+ " CONTENT  TEXT  NOT NULL , " + " MESSAGE_FROMJID TEXT," + " MESSAGE_TOJID TEXT NOT NULL," + " FILE_TYPE TEXT NOT NULL,"
				+ " FILE_URL TEXT NOT NULL," + " CHAT_STATUS TEXT NOT NULL," + " CHAT_TYPE TEXT NOT NULL,"
				+ " CHAT_DATE_OF_CREATION TEXT NOT NULL," + " CHAT_GROUPMEMBER_ID TEXT);");*/
		return quaries;
	}
}

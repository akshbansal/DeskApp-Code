package com.im.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;

import com.im.utils.Constants;

public class DBConnection_1 {
	
	private static Connection conn = null;
	public static Connection dbConnection() {
		try {
			/**
			 * db url added in config file so that can be changed in future.
			 */
			ResourceBundle rb = ResourceBundle
					.getBundle("com.im.properties.IMProperties");
			String dbDriver = rb.getString("dbDriver");
			String userDirectory = FileUtils.getUserDirectoryPath()+File.separator+"imyourdoc"+File.separator+"db";
			File file = new File(userDirectory);
			if(!file.exists()){
				file.mkdirs();
			}
			
			String dbUrl = file.getPath()+File.separator+"imyourdocDb_"+Constants.loggedinuserInfo.username.split("@")[0]+".db";
			File file2  = new File(dbUrl);
//			file2.createNewFile();
		
			Class.forName(dbDriver);
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);
			//conn.setAutoCommit(false);
			
			if (!conn.isClosed())
				//System.out.println("Opened database successfully");

			return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		return conn;
	}
}

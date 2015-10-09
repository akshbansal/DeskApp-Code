
package com.im.utils;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.im.bo.RosterVCardBo;
import com.im.common.LoaderWindow;
import com.im.patientscreens.UserWelcomeScreen;

public final class Constants {
	
	private Constants() {
		throw new AssertionError();
	}
	public static boolean apiIsConnected = true;
	public static JDialog childWindowOpened=null;
	public static JLabel typeStatus;
	public static String pendingRequestCount = "";
	public static boolean isLogout = false;
	public static double APP_VERSON = 1.0;
	
	public static JDialog ComposeParent;
	public static boolean threadsFinish = false;
	public static UserWelcomeScreen currentWelcomeScreen;
	public static final String IMAGE_PATH = "/images";
	public static boolean IS_DIALOG = false;
	public static int HAS_MORE = 0;
	public static boolean IS_ADMIN_LOGIN = false;
	public static Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	public static String PASSWORD;
	public static Frame PARENT;
	public static JFrame mainFrame;
	public static ArrayList<String> PRACTISE_LIST;
	public static String ADMIN_LOGIN_TOKEN;
	public static ArrayList<String> DESIGNATION_LIST;
	public static ArrayList<String> JOB_TITLE_LIST;
	public static boolean IS_ADDED_TO_PANEL = false;
	public static TrayIcon TRAY_ICON;
	public static final class loggedinuserInfo {
		public static String username;
		public static String password;
		public static String license_key;
		public static String status;
		public static String login_token;
		public static String user_pin;
		public static boolean isDeviceRegisterRequired;
		public static String user_type;
		public static String name;
		public static byte[] profileImage;
		public static String device_type;
		public static String device_id;
		public static String seq_ques;
		public static String seq_ans;
		public static boolean isPatient = false;
		public static boolean isProvider = false;
		public static boolean isStaff = false;
	}
	
	public static final String REMOVE_CHAT_MSG = "~$^^xxx*xxx~$^^";
	public static LoaderWindow loader = new LoaderWindow((JFrame) PARENT);
	public static String currentChatWindowUSERID;
	public static String roomId;
	public static boolean showConsole = false;
	public static String currentGroupOpened;
	public static String groupMemeber = "";
	public static String notifiedGroup ="";

}

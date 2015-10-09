package com.im.utils;

import java.awt.SystemTray;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import com.im.json.SendInvitationJSON;

public class PendingRequestThread extends TimerTask{
	String name;
	JFrame parent;
	public PendingRequestThread(String name,JFrame parent) {
		this.name = name;
		this.parent = parent;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SendInvitationJSON json = new SendInvitationJSON();
		String response = json.getRequestCount();
		JSONObject obj;
		try {
			obj = new JSONObject(response);
			String err_code = obj.getString("err-code");
			if(err_code.equals("1")){
				String count  = obj.getString("count");
				if(count.equals("0")){
					Constants.pendingRequestCount = "";
				}
				else
				{
					Constants.pendingRequestCount = "("+count+")";
				}
			}
			if(err_code.equals("600")){
				Constants.loggedinuserInfo.login_token = null;
				//Util.disposeLogoutMenu(parent);
//				parent.dispose();
				stopTimer();
				//parent.dispose();
//				if(Constants.isLogout == false && Constants.loggedinuserInfo.login_token==null){
//				JOptionPane.showMessageDialog(Constants.mainFrame, "<html><center>You have logged in to another device. "
//						+ "<br/>For your security, this device has been logged out.<br/> "
//						+ "Please log in again to use this device </center></html>");
//					SystemTray tray = SystemTray.getSystemTray();
//					if(tray.getTrayIcons().equals(Constants.TRAY_ICON)){
//                		tray.remove(Constants.TRAY_ICON);
//                		Constants.IS_ADDED_TO_PANEL = false;
//                		Constants.TRAY_ICON = null;
//                	}
//					
//				}
//				else if(Constants.isLogout == true){
//					stopTimer();
//					parent.dispose();
//				}
			//	Util.logout();
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			 Constants.apiIsConnected = false;
			e1.printStackTrace();
		}
	}
	private void stopTimer(){
		this.cancel();
	}

}

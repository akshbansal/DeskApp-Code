package com.im.utils;

import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.im.json.HTTPReqestHandler;
import com.im.json.HTTPRequestThreads;
import com.im.json.SendInvitationJSON;

public class PushNotificationThread extends TimerTask{
	String name;
	JFrame parent;
	private ResourceBundle rb;
	public PushNotificationThread(String name,JFrame parent) {
		this.name = name;
		this.parent = parent;
		  rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
	}

	public void pushNotification(final String name) {
		
		
		JSONObject jsonObject = new JSONObject();

		  try {
		   jsonObject.put("method", rb.getString("deskNotification"));
		   jsonObject.put("login_token", Constants.loggedinuserInfo.login_token);
		HTTPRequestThreads thread=new HTTPRequestThreads(jsonObject, new HTTPReqestHandler() {
			
			@Override
			public void didRetry(HTTPRequestThreads thread) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void didFinish(HTTPRequestThreads thread, String response) {
				
				try{
				JSONObject obj = new JSONObject(response);
				String err_code = obj.getString("err-code");
				if(err_code.equals("600")){
					//do no thing
					
					
//					parent.dispose();
					stopTimer();
					parent.dispose();
					Constants.loggedinuserInfo.login_token = null;
					if(Constants.isLogout == false && Constants.loggedinuserInfo.login_token==null){
						Util.disposeLogoutMenu(parent);
					JOptionPane.showMessageDialog(Constants.mainFrame, "<html><center>You have logged in to another device. "
							+ "<br/>For your security, this device has been logged out.<br/> "
							+ "Please log in again to use this device </center></html>");
						SystemTray tray = SystemTray.getSystemTray();
						if(tray.getTrayIcons().equals(Constants.TRAY_ICON)){
                    		tray.remove(Constants.TRAY_ICON);
                    		Constants.IS_ADDED_TO_PANEL = false;
                    		Constants.TRAY_ICON = null;
                    	}
						
					}
					stopTimer();
				}
				else if(err_code.equals("1")){
					JSONArray arr = obj.getJSONArray("notifications");
//					Map<String,String> list = (Map<String,String>)arr.getJSONObject(0);
//					for(String map:list.keySet()){
//						System.out.println(map);
//					}
					if(!arr.isNull(0)){
//					if(arr.getJSONObject(0)!=null){
						
						if(Constants.showConsole) System.out.println("?>>>>>>>"+arr.getJSONObject(0).get("message"));
						if(arr.getJSONObject(0).get("message").toString().contains("request")){
							new PopUpMessage(0,(JFrame) Constants.mainFrame,name,false,"","").makeUI(arr.getJSONObject(0).get("message").toString());
							//new PopUpMessage(0).makeUI(arr.getJSONObject(0).get("message").toString());
							//JOptionPane.showMessageDialog(Constants.PARENT, arr.getJSONObject(0).get("message").toString());
							Constants.currentWelcomeScreen.refreshRightBox();
						}
						else if(arr.getJSONObject(0).get("extra")!=null)
						{
//							{"err-code":"1","notifications":[{"extra":{"roomJID":"harminder3_1439886181@newconversation.imyourdoc.com"
//								,"action":"open_room"},"message":"You have a message waiting for you on IM Your Doc from harminder3"
//										+ " in group."}]}
							JSONObject obj2 = (JSONObject) arr.getJSONObject(0).get("extra");
							
							System.out.println("/?////??"+obj2);
							try {
								if(obj2.get("action").toString().equalsIgnoreCase("open_room")){
									XmppUtils.getNotifiedGroup(obj2.get("roomJID").toString());
									String userid = obj2.get("roomJID").toString();
									//Constants.notifiedGroup = userid;
									//XmppUtils.listen2Group(userid, null);
									
									//new PopUpMessage(0,(JFrame) Constants.mainFrame,userid,true,userid,"").makeUI(arr.getJSONObject(0).get("message").toString());
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
//						else{
//							new PopUpMessage(0,(JFrame) Constants.mainFrame,name).makeUI(arr.getJSONObject(0).get("message").toString());
//							Constants.currentWelcomeScreen.refreshRightBox();
//						}
//						for(int i=0;i<arr.length();i++){
//							if(arr.getJSONObject(i).get("message")!=null)
//								new PopUpMessage(0,(JFrame) Constants.PARENT).makeUI(arr.getJSONObject(i).get("message").toString());
//							if(arr.getJSONObject(i).getJSONArray("extra")!=null){
//								JSONArray arr2 = obj.getJSONArray("extra");
//								for(int j=0;j<arr2.length();j++){
//									if(arr.getJSONObject(j).get("action").equals("open_roster")) {
//										Constants.currentWelcomeScreen.refreshRightBox();
//									}
//								}
//							}
//						}
							
//					}
					if(Constants.showConsole) System.out.println("?????"+arr);
//					[{"extra":{"user_type":"Physician","user_name":"meghaph12","action":"open_roster"},
//					"message":"megha ph has approved your add request. You may begin your secure messaging."},
//					{"extra":{"user_type":"Patient","user_name":"meghapat123","action":"add_invitation"},
//					"message":"meghapat123 has sent you an add request on IM Your Doc. Please accept to begin secure messaging."}]
					
						TrayIcon trayIcon = Constants.TRAY_ICON;
						//trayIcon.displayMessage(obj.getString("notification"),"IM YOUR DOC", MessageType.INFO);
//						{"message":"Test 4","extra":{"action":"open_announcement","ann_id":"8"}}
//						 response {"err-code":"1","notifications":[{"message":"megha ph has approved your add request.
//						You may begin your secure messaging.","extra":{"action":"open_roster","user_name":"meghaph12","user_type":"Physician"}}]}
						Gson gson = new Gson();
					
//						List<Map<String, String>> responseList = (List<Map<String, String>>) obj.get(response);
//						System.out.println(responseList);
						
						
						Toolkit.getDefaultToolkit().beep();
						//									JRootPane frame = getRootPane();
						//									JFrame f = (JFrame) frame.getParent();
						//									f.toFront();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Constants.apiIsConnected = false;
			}
				
			}
			
			@Override
			public void didFailed(HTTPRequestThreads thread, Exception e) {
				// TODO Auto-generated method stub
				
			}
		}, false);
		
		  }catch(Exception e)
		  {
			  Constants.apiIsConnected = false;
			  e.printStackTrace();
		  }
		
	/*	SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

			@Override
			protected Void doInBackground() throws Exception {
				try {
					
					SendInvitationJSON invite = new SendInvitationJSON();
					String response = invite.getDesktopNotifications();
					JSONObject obj = new JSONObject(response);
					String err_code = obj.getString("err-code");
					if(err_code.equals("600")){
						//do no thing
						
						
//						parent.dispose();
						stopTimer();
						parent.dispose();
						Constants.loggedinuserInfo.login_token = null;
						if(Constants.isLogout == false && Constants.loggedinuserInfo.login_token==null){
							Util.disposeLogoutMenu(parent);
						JOptionPane.showMessageDialog(Constants.mainFrame, "<html><center>You have logged in to another device. "
								+ "<br/>For your security, this device has been logged out.<br/> "
								+ "Please log in again to use this device </center></html>");
							SystemTray tray = SystemTray.getSystemTray();
							if(tray.getTrayIcons().equals(Constants.TRAY_ICON)){
	                    		tray.remove(Constants.TRAY_ICON);
	                    		Constants.IS_ADDED_TO_PANEL = false;
	                    		Constants.TRAY_ICON = null;
	                    	}
							
						}
						stopTimer();
					}
					else if(err_code.equals("1")){
						JSONArray arr = obj.getJSONArray("notifications");
//						Map<String,String> list = (Map<String,String>)arr.getJSONObject(0);
//						for(String map:list.keySet()){
//							System.out.println(map);
//						}
						if(!arr.isNull(0)){
//						if(arr.getJSONObject(0)!=null){
							
							if(Constants.showConsole) System.out.println("?>>>>>>>"+arr.getJSONObject(0).get("message"));
							if(arr.getJSONObject(0).get("message").toString().contains("request")){
								new PopUpMessage(0,(JFrame) Constants.mainFrame,name,false,"").makeUI(arr.getJSONObject(0).get("message").toString());
								//new PopUpMessage(0).makeUI(arr.getJSONObject(0).get("message").toString());
								//JOptionPane.showMessageDialog(Constants.PARENT, arr.getJSONObject(0).get("message").toString());
								Constants.currentWelcomeScreen.refreshRightBox();
							}
//							else{
//								new PopUpMessage(0,(JFrame) Constants.mainFrame,name).makeUI(arr.getJSONObject(0).get("message").toString());
//								Constants.currentWelcomeScreen.refreshRightBox();
//							}
//							for(int i=0;i<arr.length();i++){
//								if(arr.getJSONObject(i).get("message")!=null)
//									new PopUpMessage(0,(JFrame) Constants.PARENT).makeUI(arr.getJSONObject(i).get("message").toString());
//								if(arr.getJSONObject(i).getJSONArray("extra")!=null){
//									JSONArray arr2 = obj.getJSONArray("extra");
//									for(int j=0;j<arr2.length();j++){
//										if(arr.getJSONObject(j).get("action").equals("open_roster")) {
//											Constants.currentWelcomeScreen.refreshRightBox();
//										}
//									}
//								}
//							}
								
//						}
						if(Constants.showConsole) System.out.println("?????"+arr);
//						[{"extra":{"user_type":"Physician","user_name":"meghaph12","action":"open_roster"},
//						"message":"megha ph has approved your add request. You may begin your secure messaging."},
//						{"extra":{"user_type":"Patient","user_name":"meghapat123","action":"add_invitation"},
//						"message":"meghapat123 has sent you an add request on IM Your Doc. Please accept to begin secure messaging."}]
						System.out.println(obj.getJSONArray("notifications"));
							TrayIcon trayIcon = Constants.TRAY_ICON;
							//trayIcon.displayMessage(obj.getString("notification"),"IM YOUR DOC", MessageType.INFO);
//							{"message":"Test 4","extra":{"action":"open_announcement","ann_id":"8"}}
//							 response {"err-code":"1","notifications":[{"message":"megha ph has approved your add request.
//							You may begin your secure messaging.","extra":{"action":"open_roster","user_name":"meghaph12","user_type":"Physician"}}]}
							Gson gson = new Gson();
						
//							List<Map<String, String>> responseList = (List<Map<String, String>>) obj.get(response);
//							System.out.println(responseList);
							System.out.println(obj.getJSONArray("notifications"));
							
							Toolkit.getDefaultToolkit().beep();
							//									JRootPane frame = getRootPane();
							//									JFrame f = (JFrame) frame.getParent();
							//									f.toFront();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		};
		worker.execute();*/
	}
	private void stopTimer(){
		this.cancel();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		pushNotification(name);
	}

}

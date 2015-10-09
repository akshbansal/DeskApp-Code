package com.im.utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.lang.RandomStringUtils;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.json.JSONException;
import org.json.JSONObject;

import com.im.bo.ConversationBo;
import com.im.bo.CustomComparator;
import com.im.bo.CustomComparatorMessage;
import com.im.bo.MessageSyncBo;
import com.im.bo.RosterVCardBo;
import com.im.chat.UserChat;
import com.im.db.DBServiceInsert;
import com.im.json.SendInvitationJSON;


public class RetreiveMessageXML extends Thread{
	private String jid;
	private String start;
	String packetId = RandomStringUtils.randomAlphanumeric(6);
	static List<String> list = new ArrayList<String>();
	static Map<String,List<MessageSyncBo>> mapList = new HashMap<String,List<MessageSyncBo>>();
	List<MessageSyncBo> msgList =null;
	public RetreiveMessageXML(){
		
	}
	public RetreiveMessageXML(String jid,String start){
		this.jid = jid;
		this.start = start;
		
	}
    public void run(){
        try {
            getConversationList(jid,start);
         //   getXmlForParsing();
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   

    
    private void getConversationList(final String jid,final String start) {
    	
    	try {
		//	sleep(1000);
			if(XmppUtils.connection.isAuthenticated())
			{
			new GetRetrieveChatThread(XmppUtils.connection, jid,start,
					new GetRetrieveChatThread.getConversationListListner() {
				@Override
				public void didReciveIQ(RetriveChatIQ iq, String Jid,String start) {
//					System.out.println("JID:: "+jid+" Start:: "+start);
//					System.out.println("RetriveChatIQ;"+iq.list);
					  String jid ="";
					   String secs ="";
					   String body = "";
					   int count = 0;
					List<HashMap<String, String>> listOfHashmap = iq.list;
					   for (int a =0; a<listOfHashmap.size();a++){
						   count=count+1;
						   HashMap<String, String> tmpData = (HashMap<String, String>) listOfHashmap.get(a);
						   for(int i = 0; i< tmpData.size();i++){
							   jid  = tmpData.get("jid");
							   secs = tmpData.get("secs");
							   body = tmpData.get("body");
						   }
						   if(XmppUtils.isJSONValid(body)){
							   try{
							   JSONObject obj = new JSONObject(body);
//							   if(!obj.getString("to").endsWith("@newconversation.imyourdoc.com")||!obj.getString("to").contains("_")){
								String msgID = obj.getString("messageID");
								String content = obj.getString("content");
								String from = obj.getString("from");
								String to = obj.getString("to");
								String fileType=""; 
								String filePath = "";
								jid = jid==null?obj.getString("to"):jid;
								if(obj.has("file_type"))
									fileType = obj.getString("file_type");
								if(obj.has("file_path"))
									filePath =  obj.getString("file_path");
								String timeStamp = obj.getString("timestamp");
								 MessageSyncBo messageSyncBo = new MessageSyncBo();
								   messageSyncBo.setWith(jid);
								   messageSyncBo.setSeconds(secs);
								   messageSyncBo.setContent(content);
								   messageSyncBo.setMessageID(msgID);
								   messageSyncBo.setChatDate(timeStamp);
								   messageSyncBo.setMessageFrom(from);
								   messageSyncBo.setMessageTo(to);
								   messageSyncBo.setFileType(fileType);
								   messageSyncBo.setFileUrl(filePath);
								   if(!list.contains(msgID)){
									   list.add(msgID);
									   synchronized (messageSyncBo) {
										   new DBServiceInsert().insertMessageSyncTable(messageSyncBo);
									   }
										  
									   msgList = mapList.get(jid);
									   if(msgList==null)
										   msgList = new ArrayList<MessageSyncBo>();
									   msgList.add(messageSyncBo);
									   getMessageSyncMap().put(jid,msgList);
									   }
							   }
							   catch(Exception e1){
								   e1.printStackTrace();
							   }
						   }
						   
					   }
					   if(count == listOfHashmap.size()){
						   List<MessageSyncBo> listOfMessages = getMessageSyncMap().get(jid);
					   if (!listOfMessages.isEmpty()) {
							Collections.sort(listOfMessages, new CustomComparatorMessage());
							
							Iterator<MessageSyncBo> itr = listOfMessages.iterator();
							while(itr.hasNext()){
								MessageSyncBo msgSyncbo  = itr.next();
								if(msgSyncbo.getFileUrl()==null)
									System.out.println(msgSyncbo.getContent());
								 RosterVCardBo vcardBo = null;
								 String content = msgSyncbo.getContent();
								 String from = msgSyncbo.getMessageFrom();
								jid = msgSyncbo.getWith();
								 if(jid!=null)
									   vcardBo = XmppUtils.getVCardBo(jid.split("@")[0].toLowerCase());
								   if(!Constants.REMOVE_CHAT_MSG.equals(content)){
										
										try {
												//XmppUtils.generateChartPanels(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));
												UserChat userChat = UserChat.getUserChat(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));//new UserChat(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));
												userChat.addMsgInBoxChatState(msgSyncbo,start);
											
										} catch (JSONException e) {
											e.printStackTrace();
										}
								   }
								   else if (Constants.REMOVE_CHAT_MSG.equals(content)) {
											new SendInvitationJSON().ReportOnCloseConversation(org.jivesoftware.smack.util.StringUtils.parseBareAddress(from));
									
									} 
							}
					   }
					  }
				}
			});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    private static Map<String, List<MessageSyncBo>> getMessageSyncMap() {
		if (null == mapList) {
			mapList = new HashMap<>();
		}
		return mapList;
	}
}



package com.im.utils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import com.im.bo.ConversationBo;
import com.im.bo.CustomComparatorConversation;
import com.im.bo.MessageSyncBo;
import com.im.db.DBServiceInsert;


public class ChatStateXML extends Thread{
	private String jid;
	private String lastActiveDate;
	private static List<ConversationBo> conversationList = new ArrayList<ConversationBo>();
	static Map<String,List<ConversationBo>> mapList = new HashMap<String,List<ConversationBo>>();
	public ChatStateXML(){
		
	}
	public ChatStateXML(String jid,String lastActiveDate){
		this.jid = jid;
		this.lastActiveDate = lastActiveDate;
	}
    public void run(){
        try {
            getConversationList(jid);
          //  getXmlForParsing();
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getConversationList(final String jid) {
    	try {
			//sleep(1000);
			if(XmppUtils.connection.isAuthenticated())
			{
			new GetConversationThread(XmppUtils.connection, jid,
					new GetConversationThread.getConversationListListner() {
				@Override
				public void didReciveIQ(ChatIQ iq, String Jid) {
					//System.out.println("Chat IQ"+iq.list);
					int count =0;
					List<HashMap<String, String>> listOfHashmap = iq.list;
					  String jid = "";
					  String start ="";
					   for (int a =0; a<listOfHashmap.size();a++){
						   count = count+1;
						   HashMap<String, String> tmpData = (HashMap<String, String>) listOfHashmap.get(a);
						   for(int i = 0; i< tmpData.size();i++){

							   jid  = tmpData.get("with");
							   start = tmpData.get("start");
						   }
							   try{
								   if(jid.contains("@imyourdoc.com") && !jid.contains("_")){
									   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
									   Date date = dateFormat.parse(lastActiveDate);
									   Date date2 = dateFormat.parse(start);
										if(date2.compareTo(date)>=0){
//											System.out.println("Date2 is after Date");
											if(!jid.contains("@")){
												jid = jid+"@imyourdoc.com";
											}
											 ConversationBo convBo = new ConversationBo();
								               convBo.setWith(jid);
								               convBo.setStart(start);
								               conversationList = mapList.get(jid);
											   if(conversationList==null)
												   conversationList = new ArrayList<ConversationBo>();
											   conversationList.add(convBo);
											   getMessageSyncMap().put(jid,conversationList);
											   synchronized (convBo) {
												   new DBServiceInsert().insertActiveConversation(convBo);
											   }
											 
												
										}
									  
								   }
							   }catch(Exception e){
								   System.out.println("chat state::"+e.getMessage());
								   e.printStackTrace();
							   }
						   
					   }
					   try{
						   if(count==listOfHashmap.size()){
							   List<ConversationBo> list = getMessageSyncMap().get(jid);
							   Collections.sort(list, new CustomComparatorConversation());
							   Iterator<ConversationBo> itr = list.iterator();
							   int finalCount = 0;
							   while(itr.hasNext()){
								   ConversationBo bo = itr.next();
								  // 2015-08-10T07:47:40.036Z
								   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									
//									System.out.println("Yesterday's date = "+ cal.getTime());
									Calendar cal = Calendar.getInstance();
									cal.add(Calendar.DAY_OF_MONTH, -2);
									Date date = format.parse(lastActiveDate);
									String newDateString = format.format(cal.getTime());
									SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
									Date newDate = format2.parse(newDateString);
								   Thread thread2 = new RetreiveMessageXML(bo.getWith(),bo.getStart());
								   thread2.start();
							   }
							
						   }
					   }
					   catch(Exception e){
						   System.out.println(e.getMessage());
					   }
					   
					   
				}
			});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    private static Map<String, List<ConversationBo>> getMessageSyncMap() {
		if (null == mapList) {
			mapList = new HashMap<>();
		}
		return mapList;
	}
}



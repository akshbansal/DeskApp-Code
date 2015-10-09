package com.im.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class ChatIQ_1 extends IQ {

	public static final String NAMESPACE = "urn:xmpp:archive";
	public List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	@Override
	public String getChildElementXML() {
		// TODO Auto-generated method stub
		StringBuilder xml=new StringBuilder("<list xmlns=\"urn:xmpp:archive\">");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			HashMap<String, String> chat = (HashMap<String, String>) iterator.next();
			xml.append("<chat start=\""+chat.get("start")+"\" with=\""+chat.get("with")+"\" />");
			
		}
		xml.append("</list>");
		return xml.toString();
	}
	public void addFrom(String secs,String jid )
	{
		HashMap<String, String> from=new   HashMap<>();
		
		from.put("secs",secs);
		from.put("jid",jid);
		
		list.add(from);
	
	}
	public void addChat(String with,String start)
	{
		HashMap<String, String> chat=new   HashMap<>();
		
		chat.put("with",with);
		chat.put("start",start);
		list.add(chat);
	}
	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		return super.toXML();
	}
	
	public static class ChatIQProvider implements IQProvider {
		
		@Override
		public IQ parseIQ(XmlPullParser parser) throws Exception {
			  ChatIQ iqPacket = new ChatIQ();

		        boolean done = false;
		        
		        while (!done) {
		            int eventType = parser.next();
		            
		            if (eventType == XmlPullParser.START_TAG) {
		                String elementName = parser.getName();
		                String namespace = parser.getNamespace();
		                
		                if(parser.getName().equals("chat"))
		                {
		                	String with=null,start=null;
		                	
		                	for (int i=0; i<parser.getAttributeCount(); i++) {
			                    	if(parser.getAttributeName(i).equals("with"))
			                    		{
			                    			with=parser.getAttributeValue(i);
			                    		}
			                    	if(parser.getAttributeName(i).equals("start"))
		                    		{
		                    			start=parser.getAttributeValue(i);
		                    		}
		                	
		                	}
		  	              iqPacket.addChat(with, start);
		                }
		                
		                }
		            
		            else if (eventType == XmlPullParser.END_TAG) {
		            	//System.out.println("END TAG"+parser.getName());
		                if (parser.getName().equals("list")) {
		                	 done = true;
		                }
		                
		            }
		        }
		  
		      //  System.out.println(iqPacket.toXML());
		        return iqPacket;
			
		}
		public  ChatIQProvider() {
			
		}
		static ChatIQProvider provider=null;
		public static ChatIQProvider getInstanse() {
			
			if(provider==null)
				provider=new ChatIQProvider();
			return provider;
		}
	};
	
}

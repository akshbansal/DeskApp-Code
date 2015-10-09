package com.im.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class RetriveChatIQ extends IQ {

	public static final String NAMESPACE = "urn:xmpp:archive";
	public List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	@Override
	public String getChildElementXML() {
		// TODO Auto-generated method stub
		StringBuilder xml=new StringBuilder("<retrieve xmlns=\"urn:xmpp:archive\">");
		Iterator<HashMap<String, String>> iterator = list.iterator();
		while ( iterator.hasNext()) {
		//	HashMap<String, String> chat = (HashMap<String, String>) iterator.next();
			Map<String, String> from = (HashMap<String, String>) iterator.next();

			xml.append("< secs=\""+from.get("secs")+"\" jid=\""+from.get("jid")+"\"body=\""+from.get("body")+"\" />");
			
		}
		xml.append("</retrieve>");
		return xml.toString();
	}
	public void from(String secs,String jid,String body)
	{
		HashMap<String, String> from=new   HashMap<>();
		
		from.put("secs",secs);
		from.put("jid",jid);
		from.put("body",body);

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
		
	public static class RetriveChatIQProvider implements IQProvider {
		
		@Override
		public IQ parseIQ(XmlPullParser parser) throws Exception {
			  RetriveChatIQ iqPacket = new RetriveChatIQ();

		        boolean done = false;
		        String secs=null,jid=null,body=null;
		        while (!done) {
		            try{

		            int eventType = parser.next();
		            if (eventType == XmlPullParser.START_TAG) {

		                if(parser.getName().equals("from")||parser.getName().equals("to"))
		                {
		                	for (int i=0; i<parser.getAttributeCount(); i++) {
			                    	if(parser.getAttributeName(i).equals("secs"))
			                    		{
					              //  	String secs=null,jid=null;

			                    			secs=parser.getAttributeValue(i);
			                    		//	System.out.println("Secs:-"+secs);
			                    		}
			                    	if(parser.getAttributeName(i).equals("jid"))
		                    		{
		                    			jid = parser.getAttributeValue(i);
		                    	//	System.out.println("jid:-"+jid);

		                    		}
			                    
		                	}           
		                	
		                //	iqPacket.addFrom(secs,jid);
		                }
		                	 if(parser.getName().equals("body"))
			                {
		                		 try{
		                			 body = parser.nextText();
		                		 }
		                		 catch(Exception e){
		                			 System.out.println("inner exception::"+e.getMessage());
		                		 }
			                }
	                	
		            	}
		            
		            else if (eventType == XmlPullParser.END_TAG) {
		            	//System.out.println("END TAG"+parser.getName());
		            	
		            	if (parser.getName().equals("from")||parser.getName().equals("to")) {
		                	
		                	 iqPacket.from(secs, jid, body);
		            	}
		            	if (parser.getName().equals("chat")) {
		                	 done = true;
		            }
		        }
		        }
		            catch(Exception e){
		            	if(e.getMessage().equalsIgnoreCase("stream closed")){
		            		System.out.println("outer exception::"+e.getMessage());
		            		break;
		            	}
           			 
           		 }
		}
		      //  System.out.println(iqPacket.toXML());
		        return iqPacket;
			
		}
		public  RetriveChatIQProvider() {
			
		}
		static RetriveChatIQProvider provider=null;
		public static RetriveChatIQProvider getInstance() {
			
			if(provider==null)
				provider=new RetriveChatIQProvider();
			return provider;
		}
	}

	
}

package com.im.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

public class RetriveChatIQ_1 extends IQ {

	public static final String NAMESPACE = "urn:xmpp:archive";

	public List<HashMap<String, String>> list=new ArrayList<HashMap<String,String>>();
	@Override
	public String getChildElementXML() {
		// TODO Auto-generated method stub
		StringBuilder xml=new StringBuilder("<chat xmlns=\"urn:xmpp:archive\">");
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
		//	HashMap<String, String> chat = (HashMap<String, String>) iterator.next();
			HashMap<String, String> from = (HashMap<String, String>) iterator.next();

			xml.append("<from secs=\""+from.get("secs")+"\" jid=\""+from.get("jid")+"\"body=\""+from.get("body")+"\" />");
			
		}


		xml.append("</chat>");
		return xml.toString();
	}
//	public void addBody(String body )
//	{
//		HashMap<String, String> body1=new   HashMap<>();
//		
//		body1.put("body",body);
//		
//		list.add(body1);
//	
//	}
//	public void addFrom(String secs,String jid )
//	{
//		HashMap<String, String> from=new   HashMap<>();
//		
//		from.put("secs",secs);
//		from.put("jid",jid);
//		
//		list.add(from);
//	
//	}
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
			  RetriveChatIQ_1 iqPacket = new RetriveChatIQ_1();

		        boolean done = false;
		        String secs=null,jid=null,body=null;
		        while (!done) {
		            

		            int eventType = parser.next();
		            if (eventType == XmlPullParser.START_TAG) {
	                	

	                    	

		                if(parser.getName().equals("from")||parser.getName().equals("to"))
		                {

		                	
		                	for (int i=0; i<parser.getAttributeCount(); i++) {
			                    	if(parser.getAttributeName(i).equals("secs"))
			                    		{
					              //  	String secs=null,jid=null;

			                    			secs=parser.getAttributeValue(i);
			                    			System.out.println("Secs:-"+secs);
			                    		}
			                    	if(parser.getAttributeName(i).equals("jid"))
		                    		{
		                    			jid=parser.getAttributeValue(i);
		                    		System.out.println("jid:-"+jid);

		                    		}
			                    	
			                    	
			                    
		                	}           
		                	
		                //	iqPacket.addFrom(secs,jid);

		                	
		                }
		                	 if(parser.getName().equals("body"))
			                {
		                		body=parser.nextText();
                    			System.out.println("body:-"+body);
        		           //     iqPacket.addBody(body);


		                }

//		            	
//		                String elementName = parser.getName();
//		                String namespace = parser.getNamespace();
//		                System.out.println("element"+elementName);
//		                System.out.println("namespace"+namespace);

                
	                	
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
		      //  System.out.println(iqPacket.toXML());
		        return iqPacket;
			
		}
		public  RetriveChatIQProvider() {
			
		}
		static RetriveChatIQProvider provider=null;
		public static RetriveChatIQProvider getInstanse() {
			
			if(provider==null)
				provider=new RetriveChatIQProvider();
			return provider;
		}
	}

	
}

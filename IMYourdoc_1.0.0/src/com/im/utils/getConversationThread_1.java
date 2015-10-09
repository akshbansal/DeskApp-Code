package com.im.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.RandomStringUtils;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;




public class getConversationThread_1 extends Thread 
{
	XMPPConnection connection;
	String jid;
	String packetID;
	//CustomPacketReader reader;
	getConversationListListner listner;
	static ExecutorService backgroundPool=Executors.newFixedThreadPool(1);
	
	public getConversationThread_1(XMPPConnection connection,String jid,getConversationListListner listner) {
	
		this.connection=connection;
		this.jid=jid;
		this.listner=listner;
		
		//reader=new CustomPacketReader(this.connection);
	
		this.packetID=RandomStringUtils.randomAlphabetic(10);
		this.connection.addPacketListener(new PacketListener() {
			
			@Override
			public void processPacket(Packet packet) {
				// TODO Auto-generated method stub
				System.out.println("...."+packet.toXML());
				if(getConversationThread_1.this.listner!=null)
				{
					getConversationThread_1.this.listner.didReciveIQ((ChatIQ)packet, getConversationThread_1.this.jid);
					
				}
			}
		}, new PacketIDFilter(packetID));
		
		backgroundPool.submit(this);
		
	}

	@Override
	public void run() {
	
		
		IQ packet=new IQ(){
    		
	    	
			// TODO Auto-generated method stub
	    	String xml=getChildElementXML();
	    	

			@Override
			public String getChildElementXML() {
				// TODO Auto-generated method stub
				
				String xml="<retrieve xmlns='urn:xmpp:archive' with='"+jid+"' "
						+ "start='2015-08-04T11:29:07.249Z'> <set xmlns='http://jabber.org/protocol/rsm'> </set>   </retrieve>";
				
				return xml; 
			}
	    	};
	    	
	    	packet.setPacketID(packetID);
	    	//if(this.connection.isAuthenticated())
	    	{
	    	
	    		this.connection.sendPacket(packet);
	    	
	    		
	    	
		super.run();
	}
	/**
	 * @param args
	 */
	}
	public interface getConversationListListner
	{
		public void didReciveIQ(ChatIQ iq,String Jid);
	}

}

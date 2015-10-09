package com.im.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.RandomStringUtils;
import com.im.utils.ChatIQ;
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

import com.sun.net.ssl.internal.ssl.Provider;


public class GetConversationThread extends Thread 
{
	XMPPConnection connection;
	String jid;
	String packetID;
	getConversationListListner listner;
	static ExecutorService backgroundPool=Executors.newFixedThreadPool(1);
	
	public GetConversationThread(XMPPConnection connection,String jid,getConversationListListner listner) {
	
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
				if(GetConversationThread.this.listner!=null)
				{
					GetConversationThread.this.listner.didReciveIQ((ChatIQ)packet, GetConversationThread.this.jid);
					
				}
			}
		}, new PacketIDFilter(packetID));
		
		backgroundPool.submit(this);
		//this.start();
		
	}
	@Override
	public void run() {
		IQ packet=new IQ(){
			// TODO Auto-generated method stub
	    	String xml=getChildElementXML();
			@Override
			public String getChildElementXML() {
				// TODO Auto-generated method stub
				String xml="<list xmlns='urn:xmpp:archive' with='"+jid+"'> "
						+ "<set xmlns='http://jabber.org/protocol/rsm'></set></list>";
				return xml; 
			}
	    	};
	    	packet.setPacketID(packetID);
	    	//if(this.connection.isAuthenticated())
	    	{
	    		this.connection.sendPacket(packet);
	    	
	    		
	    	}
		super.run();
	}
	/**
	 * @param args
	 */

	public interface getConversationListListner
	{
		public void didReciveIQ(ChatIQ iq,String Jid);
	}
}

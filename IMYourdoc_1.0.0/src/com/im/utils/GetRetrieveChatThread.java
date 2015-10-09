package com.im.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.RandomStringUtils;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;


public class GetRetrieveChatThread extends Thread 
{
	XMPPConnection connection;
	String jid;
	String packetId;
	String start;
	//CustomPacketReader reader;
	getConversationListListner listner;
	static ExecutorService backgroundPool=Executors.newFixedThreadPool(1);
	
	public GetRetrieveChatThread(XMPPConnection connection,String jid,String start,getConversationListListner listner) {
	
		this.connection=connection;
		this.jid=jid;
		this.listner=listner;
		
		this.start = start;
		this.packetId=RandomStringUtils.randomAlphabetic(6);
		
		//reader=new CustomPacketReader(this.connection);
	
		//this.packetID=RandomStringUtils.randomAlphabetic(10);
		this.connection.addPacketListener(new PacketListener() {
			
			@Override
			public void processPacket(Packet packet) {
				// TODO Auto-generated method stub
				
				if(GetRetrieveChatThread.this.listner!=null)
				{
					GetRetrieveChatThread.this.listner.didReciveIQ((RetriveChatIQ)packet, GetRetrieveChatThread.this.jid
							,GetRetrieveChatThread.this.start);
					
				}
			}
		}, new PacketIDFilter(packetId));
		backgroundPool.submit(this);
		//this.start();
		
	}

	@Override
	public void run() {
	
		
		IQ packet=new IQ(){
    		
	    	
			// TODO Auto-generated method stub

			@Override
			public String getChildElementXML() {
				// TODO Auto-generated method stub
				
				String xml="<retrieve xmlns='urn:xmpp:archive' with='"+jid+"' start='"+start+"'>"
						+ "<set xmlns='http://jabber.org/protocol/rsm'> </set></retrieve>";
				
				return xml; 
			}
	    	};
	    	
	    	packet.setPacketID(packetId);
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
		public void didReciveIQ(RetriveChatIQ iq,String Jid,String start);
	}
}

package com.im.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

public class testpacketxml_bckp {

	private XMPPConnection xmppConnection;
	IQ packet = null;

	public boolean connect(String server, int port, String s) {
		ConnectionConfiguration config = new ConnectionConfiguration(server,
				port, s);
		config.setNotMatchingDomainCheckEnabled(true);
		config.setSelfSignedCertificateEnabled(true);
		config.setSASLAuthenticationEnabled(true);
		config.setDebuggerEnabled(true);
		config.setReconnectionAllowed(true);
		// config.setCompressionEnabled(true);
		xmppConnection = new XMPPConnection(config);
		try {
			xmppConnection.connect();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public void disconnect() {
		if (xmppConnection != null) {
			xmppConnection.disconnect();

		}
	}

	public boolean login(String username, String password) {
		boolean isLogin = false;
		boolean isconnected = connect("xmpp.imyourdoc.com", 5222,
				"imyourdoc.com");
		try {
			if (isconnected) {
				xmppConnection.login(username, password);
				isLogin = true;
			}
		} catch (Exception e) {
			isLogin = false;
		}
		return isLogin;

	}

	public void run() {
		try {
			boolean login = login("harminder3@imyourdoc.com", "Mind@123");
			// getXmlForParsing();

			final String packetId = RandomStringUtils.randomAlphanumeric(6);
			if (login) {
				if (xmppConnection.isConnected()
						&& xmppConnection.isAuthenticated()) {
					Packet packet = new Packet() {

						@Override
						public String toXML() {
							// TODO Auto-generated method stub
							String xml = "<iq id=\""
									+ packetId
									+ "\" type='get'><list xmlns='urn:xmpp:archive'  with='"
									+ "harmsing2@imyourdoc.com'><set xmlns='http://jabber.org/protocol/rsm'>"
									+ "<max>30</max></set></list></iq>";
							
							String xml2 = "<iq type='get' id='page1'>"+
							  "<retrieve xmlns='urn:xmpp:archive'"+
							            "with='harmsing2@imyourdoc.com'"+
							            "start='2015-08-20T13:03:27.805Z'>"+
							    "<set xmlns='http://jabber.org/protocol/rsm'>"+
							    "</set></retrieve></iq>";
							
							
								
							return xml;
						}
					};
					xmppConnection.sendPacket(packet);
				}
				
				PacketFilter myFilter = new IQTypeFilter(
						org.jivesoftware.smack.packet.IQ.Type.RESULT) {
					@Override
					public boolean accept(Packet packet) {
						return packetId.equals(packet.getPacketID());
					}
				};
				
				xmppConnection.addPacketWriterListener(new PacketListener() {

					@Override
					public void processPacket(Packet paramPacket) {
						// TODO Auto-generated method stub
						 IQ iq = (IQ)paramPacket;
						System.out.println(packetId);
						System.out.println(paramPacket.getPacketID());
						// if(paramPacket.getPacketID().equals(packetId))
						System.out.println(iq.toXML());
					}
				}, myFilter);
				xmppConnection.addPacketListener(new PacketListener() {

					@Override
					public void processPacket(Packet paramPacket) {
						// TODO Auto-generated method stub
						 IQ iq = (IQ)paramPacket;
						System.out.println(packetId);
						System.out.println(paramPacket.getPacketID());
						// if(paramPacket.getPacketID().equals(packetId))
						System.out.println(iq.toXML());
					}
				}, myFilter);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception {
		testpacketxml_bckp gtd = new testpacketxml_bckp();
		gtd.run();

	}
}

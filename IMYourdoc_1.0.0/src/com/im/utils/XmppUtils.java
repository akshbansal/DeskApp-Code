package com.im.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TimeZone;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.packet.RosterPacket;
import org.jivesoftware.smack.packet.RosterPacket.Item;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.ChatState;
import org.jivesoftware.smackx.ChatStateManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.MessageEventManager;
import org.jivesoftware.smackx.MessageEventNotificationListener;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.jivesoftware.smackx.packet.MUCInitialPresence;
import org.jivesoftware.smackx.packet.MessageEvent;
import org.jivesoftware.smackx.packet.VCard;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import com.im.bo.GroupBo;
import com.im.bo.MessageSyncBo;
import com.im.bo.RosterVCardBo;
import com.im.chat.UserChat;
import com.im.common.IAction;
import com.im.db.DBServiceInsert;
import com.im.db.DBServiceResult;
import com.im.db.DBServiceUpdate;
import com.im.groupChat.UserChatGroup;
import com.im.json.SendInvitationJSON;
import com.im.patientscreens.UserWelcomeScreen;

public class XmppUtils {

	public static XMPPConnection connection;
	public static Roster roster;
	static MessageEventManager eventManager;
	public static Collection<RosterEntry> rosterEntries;
	public static VCard myVCard;
	private static ConnectionConfiguration config;
	private static final int packetReplyTimeout = 500000; // millis
	private static ResourceBundle rb;
	private static Map<String, MultiUserChat> multiUserChatMap;
	public static Map<String, List<RosterVCardBo>> rosterVCardBoListMap;
	public static Map<String, List<RosterVCardBo>> rosterVCardBoListMap2;
	public static List<RosterVCardBo> groupVCardBOList;
	public static Map<String, RosterVCardBo> rosterVCardBoMap;
	public static Map<String, List<JLabel>> statusLabels;
	public static Map<String, List<String>> pendingReadMsg = new HashMap<>();
	private static String username;
	private static String password;
	public static List<Message> oldMessages = new ArrayList<Message>();
	public static Map<String, List<RosterVCardBo>> readByUserMap;
	private static List<RosterVCardBo> listUserReadBy;
	 private static List<String> recievedMessagePacketIds = new ArrayList<String>();
	

	private XmppUtils() {

	}
	static{
		ProviderManager manager=ProviderManager.getInstance();
        manager.addIQProvider("list", "urn:xmpp:archive", ChatIQ.ChatIQProvider.getInstanse());
        manager.addIQProvider("chat", "urn:xmpp:archive", RetriveChatIQ.RetriveChatIQProvider.getInstance());
	}
	public static XMPPConnection getImyourDocConnection() throws XMPPException {
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		return getConnection(rb.getString("XMPP_URL"), 443, rb.getString("XMPP_SERVICE"));
	}

	public static XMPPConnection getFacebookConnection() throws XMPPException {
		return getConnection("chat.facebook.com", 5223, "");
	}

	public static XMPPConnection getConnection(final String server, final int port, final String mainServer) throws XMPPException {
		int count =0;
		int count2 =0;
		if (connection == null || !connection.isConnected()) {
			SmackConfiguration.setPacketReplyTimeout(packetReplyTimeout);
			if (mainServer == null || mainServer.equals("")) {
				config = new ConnectionConfiguration(server, port);
			} else {
				config = new ConnectionConfiguration(server, port, mainServer);
			}
			// config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
			config.setNotMatchingDomainCheckEnabled(true);
			config.setSelfSignedCertificateEnabled(true);

			config.setSASLAuthenticationEnabled(true);
			config.setDebuggerEnabled(false);
			config.setReconnectionAllowed(true);
			// config.setCompressionEnabled(true);
			connection = new XMPPConnection(config);
			try {
				connection.connect();
			} catch (XMPPException e) {
				System.out.println(e.getMessage());
				 for(int i =0;i<3;i++){
						count = count+1;
						 if (!connection.isConnected())
						 {
							 getConnection(rb.getString("XMPP_URL"), 443, rb.getString("XMPP_SERVICE"));
						 }
					}
				 if(count==3){
					 for(int i =0;i<2;i++){
						 count2 = count2+1;
						 if (!connection.isConnected())
							 getConnection(rb.getString("XMPP_URL"), 5222, rb.getString("XMPP_SERVICE"));
					 }
				 	}
					if(count2==2 && !connection.isConnected()){
					 Constants.PARENT.setEnabled(true);
					 Constants.loader.setVisible(false);
						 JOptionPane
							.showMessageDialog(Constants.PARENT,
									"<html><center>We could not find internet on this device or antivirus is not <br/>"
									+ "letting IM Your Doc to connect with internet. Please check.</center></html>");
					}
				}

			if (connection.isConnected()) {
				if (Constants.showConsole)
					System.out.println("conecteddddddd");
			}
			if (Constants.showConsole)
				System.out.println("Connected: " + connection.isConnected());
		}

		return connection;

		// chatManager = xmppConnectionBO.getConnection().getChatManager();
		// messageListener = new MyMessageListener();
	}

	public static Boolean doLogin(final String loginUsername, String loginPassword, XMPPConnection connectionA) throws XMPPException {
		if (Constants.showConsole)
			System.out.println("username and password----> " + loginUsername + " " + loginPassword);
		username = loginUsername;
		password = loginPassword;
		if (connectionA == null) {
			connection = getImyourDocConnection();
		} else {
			connection = connectionA;
		}
		try {
			if (connection != null && connection.isConnected()) {
				
				PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
				connection.addPacketListener(new PacketListener() {
					@Override
					public void processPacket(Packet packet) {
						final Message message = (Message) packet;
						final String fromName = StringUtils.parseBareAddress(message.getFrom());
						if (message.getBody() != null) {
							packet.setPacketID(message.getPacketID());

							System.out.println("before old Chat Text Recieved " + message.getBody() + " from " + fromName);
							String body = message.getBody();
							String subject = message.getSubject();
							final String from = fromName.split("@")[0];
							/*
							 * RosterVCardBo vCardBo = new
							 * DBServiceResult().getRosterUserDetails(from);
							 * if(vCardBo ==null){ vCardBo =
							 * getVCardBo(fromName); }
							 */
							try {
								String content = "";
								if (isJSONValid(body)) {
									JSONObject obj = new JSONObject(body);
									if (subject == null)
										content = obj.getString("content");
									else
										content = "File Recieved";
								} 

								oldMessages.add(message);
								System.out.println("sks jk");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}, filter);
				if (!connection.isAuthenticated()) {
					try{
						connection.login(loginUsername.trim(), password.trim());
					}
					catch(Exception e){
						System.out.println("login exception::"+e.getMessage());
					}
				}
				setStatus(true, "ONLINE");
				VCard card = new VCard();
				card.load(connection);
				if (card.getAvatar() != null)
					Constants.loggedinuserInfo.profileImage = card.getAvatar();
				/*
				 * <vCard xmlns='vcard-temp'><FN>ali ali</FN> <ROLE>ARNP</ROLE>
				 * <DESC>Abdominal Radiology | Radiology-Diagnostic</DESC>
				 * <JABBERID>[{&quot;name&quot;:&quot;AVERA SACRED HEART
				 * HOSPITAL&quot;,
				 * &quot;hosp_id&quot;:&quot;25&quot;,&quot;is_primary
				 * &quot;:&quot;Y&quot;}, {&quot;name&quot;:&quot;ST JOSEPH'S
				 * MEDICAL
				 * CENTER&quot;,&quot;hosp_id&quot;:&quot;21&quot;,&quot;
				 * is_primary&quot;:&quot;N&quot;}]</JABBERID>
				 * <TITLE>Administrative</TITLE> <NICKNAME>ali ali</NICKNAME>
				 * <ADR><WORK/><PCODE>21</PCODE> <POBOX>ST JOSEPH'S MEDICAL
				 * CENTER</POBOX> <STREET>N</STREET> </ADR> </vCard>
				 */

				PacketFilter filter2 = new IQTypeFilter(IQ.Type.RESULT);

				connection.addPacketListener(new PacketListener() {
					@Override
					public void processPacket(Packet packet) {
						if (Constants.showConsole)
							System.out.println("roster update message");
						//System.out.println(packet.getXmlns());
						}
				}, filter2);
			
			
				MultiUserChat.addInvitationListener(connection, new InvitationListener() {
					@Override
					public void invitationReceived(XMPPConnection conn, String room, String inviter, String reason, String password,
							Message message) {
						// MultiUserChat muc2 = new MultiUserChat(conn, room);
						try {
							MultiUserChat muc = new MultiUserChat(conn, room);

							RoomInfo info = MultiUserChat.getRoomInfo(conn, room);
							if (info != null) {
								if (!muc.isJoined()) {
									muc.join(Constants.loggedinuserInfo.username.split("@")[0]);
								}
									RosterVCardBo bo = createGroupVCardBO(room, info.getSubject(), roomUserIds(room), true);
									//new DBServiceInsert().insertRosterTableGroup(bo);
									generateChartPanels(bo, UserWelcomeScreen.centerBox.getWidth());
							}
							// addUserListener(room);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*private static void addOldMessagesToPanel() {
		if (oldMessages != null) {
			if (!oldMessages.isEmpty()) {
				ListIterator<Message> itr = oldMessages.listIterator();
				while (itr.hasNext()) {
					Message message = itr.next();
					String body = message.getBody();
					String subject = message.getSubject();
					String fromName = message.getFrom();
					final String from = fromName.split("@")[0];
					RosterVCardBo vCardBo = new DBServiceResult().getRosterUserDetails(from);
					if (vCardBo == null) {
						vCardBo = getVCardBo(fromName);
					}
					try {
						String content = "";
						if (isJSONValid(body)) {
							JSONObject obj = new JSONObject(body);
							if (subject == null)
								content = obj.getString("content");
							else
								content = "File Recieved";
						} else {
							content = message.getBody();
						}

						if (!content.equals(Constants.REMOVE_CHAT_MSG)) {
							try {
								System.out.println("inside addOldpanelmethod " + content);
								// WelcomeUtils.openChatWindow(vCardBo, false);
								UserWelcomeScreen.addMessages(vCardBo, content, true, message.getPacketID());
								// generateChartPanels(vCardBo, (int)(
								// Constants.SCREEN_SIZE.getWidth()*0.60));
								UserChat userChat = UserChat.getUserChat(vCardBo, (int) (Constants.SCREEN_SIZE.getWidth() * 0.60));
								userChat.addMsgInBox(message,false);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}*/


	public static void getGroups(final GroupBo groupBo) throws XMPPException {
			try{
				final RoomInfo info = MultiUserChat.getRoomInfo(connection, groupBo.getRoomid());
				if (info != null) {
					String roomId= groupBo.getRoomid();
					if(!roomId.contains("@"))
					{
						roomId  = roomId +"@newconversation.imyourdoc.com";
					}
					joinGroup(roomId,true,groupBo.getLastActiveDate(),groupBo.getLastActiveSecs());
				}
				
				final Map<String,String> memberList = roomUserIds(groupBo.getRoomid());
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						// TODO Auto-generated method stub
						RosterVCardBo newVcard = createGroupVCardBO(groupBo.getRoomid(), info.getSubject(), memberList, true);
						// RosterVCardBo vcard = new
						RosterVCardBo bo = new DBServiceResult().getRosterUserDetails(groupBo.getRoomid().split("@")[0]);
						try {
							if(bo==null){
								synchronized (newVcard) {
									new DBServiceInsert().insertRosterTableGroup(newVcard);
								}
								
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						generateChartPanels(newVcard, UserWelcomeScreen.centerBox.getWidth());
						try {
							synchronized (newVcard) {
								new DBServiceInsert().insertGroupTable(newVcard);
							}
								
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
					}

				};
				worker.execute();
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
	}

	public static void getNotifiedGroup(final String roomid) throws XMPPException {
		System.out.println("????" + roomid);
		String subject ="";
		MultiUserChat muc = new MultiUserChat(connection,roomid);
		final RoomInfo info = MultiUserChat.getRoomInfo(connection, roomid);
		if(info!=null){
			subject = info.getSubject();
			if(org.apache.commons.lang.StringUtils.isEmpty(subject)){
				subject = roomid.split("@")[0];
			}
		}
		else
		{
			subject = roomUsersStr(roomid);
		}
				 if (!muc.isJoined()) {
					 muc.join(Constants.loggedinuserInfo.username.split("@")[0]);
				 }
			final Map<String,String> memberList = roomUserIds(roomid);
			RosterVCardBo newVcard = createGroupVCardBO(roomid, subject, memberList, true);
					// RosterVCardBo vcard = new
					RosterVCardBo vcard = new  DBServiceResult().getRosterUserDetails(roomid.split("@")[0]);
					if(vcard == null){
						new DBServiceInsert().insertRosterTableGroup(newVcard);
						
					}
					try {
						new DBServiceInsert().insertGroupTable(newVcard);
					} catch (Exception e) {
						e.printStackTrace();
					}
					generateChartPanels(newVcard, UserWelcomeScreen.centerBox.getWidth());
					listen2Group(roomid, null,false);
					
	
	}

	/*public static void getGroups() {
		groupVCardBOList = null;
		SwingWorker<Void, Void> worker2 = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				Collection<HostedRoom> rooms = MultiUserChat.getHostedRooms(connection, "newconversation.imyourdoc.com");
				// List<RosterVCardBo> listGroups =
				// DeserialiseFile.deserializeFile();
				for (final HostedRoom room : rooms) {
					RoomInfo info = MultiUserChat.getRoomInfo(connection, room.getJid());
					// System.out.println("r00000ooom:---" + info.getRoom());
					SimpleDateFormat dateformat = new SimpleDateFormat("d MMM h:mm a ");
					Date date = new Date();
					final MultiUserChat muc = new MultiUserChat(connection, room.getJid());
					if (info != null) {
						if (!muc.isJoined()) {
							muc.join(username.split("@")[0]);
						}
					}
					final List<RosterVCardBo> memberList = roomUserIds(muc);

					
					 * Collection<Affiliate> affList =
					 * getMUCInstance(room.getJid()).getMembers(); for(Affiliate
					 * affMember: affList){
					 * System.out.println(affMember.getJid()); RosterVCardBo
					 * vcard = getVCardBo(affMember.getJid());
					 * memberList.add(vcard); }
					 

					
					 * if(listGroups==null){ listGroups = new
					 * ArrayList<RosterVCardBo>(); listGroups.add(vcard);
					 * SerializeFile.SerializeObject(listGroups); }
					 
					// if(listGroups!=null){
					// if(listGroups.contains(vcard)){
					// if (!muc.isJoined()) {
					// muc.join(username.split("@")[0]);
					// }
					// }
					// }

					// RosterVCardBo listAll = new
					// DBServiceResult().getRosterUserDetails(room.getJid().split("@")[0]);
					// if (listAll == null) {
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							// TODO Auto-generated method stub
							RosterVCardBo newVcard = createGroupVCardBO(room.getJid(), roomUsersStr(muc), memberList, true);
							// RosterVCardBo vcard = new
							// DBServiceResult().getRosterUserDetails(room.getJid().split("@")[0]);
							try {
								new DBServiceInsert().insertRosterTableGroup(newVcard);
							} catch (Exception e) {
								e.printStackTrace();
							}
							generateChartPanels(newVcard, UserWelcomeScreen.centerBox.getWidth());
							return null;
						}

					};
					worker.execute();

					// new DBServiceInsert().insertGroupTable(vcard);
					// }
					// if (!getGroupVCardBOList().contains(vcard)) {
					// getGroupVCardBOList().add(vcard);
					// // SerializeFile.SerializeObject(getGroupVCardBOList());
					// }

				}
				return null;
			}
		};
		worker2.execute();
	}*/

	public static void setStatus(boolean available, String status) {
		Presence.Type type = available ? Type.available : Type.unavailable;
		Presence presence = new Presence(type);
		presence.setStatus(status);
		connection.sendPacket(presence);
	}

	public static VCard myVCard() {
		if (null == myVCard) {
			myVCard = new VCard();
			try {
				myVCard.load(connection);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		return myVCard;
	}

	/**
	 * @auhor megha
	 *        <p>
	 *        set profile picture of a user who is login in vcard
	 *        </p>
	 * @param image
	 * @return
	 */
	public static boolean setProfilePicture(byte[] image) {
		VCard card = myVCard();
		try {
			card.setAvatar(image);
			card.save(connection);
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static byte[] getProfilePicture() {
		return myVCard().getAvatar();
	}

	public static void destroy() {
		if (connection != null && connection.isConnected()) {
			connection.disconnect();
		}
	}

	public static void addSubscription() {

	}

	public static Roster getRoster() {
		if (null == roster)
			roster = connection.getRoster();
		return roster;
	}

	public static Collection<RosterEntry> getRosterEntry() {
		if (null == rosterEntries)
			rosterEntries = getRoster().getEntries();

		return rosterEntries;
	}

	/**
	 * @author megha
	 * @param type
	 * @param searchTxt
	 *            <p>
	 *            get roster list with vcard data
	 *            </p>
	 * @return
	 */
	public static List<RosterVCardBo> getVCardList(String type, String searchTxt) {
		List<RosterVCardBo> list = new DBServiceResult().getRosterList(username.split("@")[0], type);
		if (list.isEmpty())
			list = getRosterVCardBoListMap().get(type);
		if (list == null) {
			list = new ArrayList<>();
			for (final RosterEntry entry : getRosterEntry()) {
				for (RosterGroup group : entry.getGroups()) {
					if ("all".equalsIgnoreCase(type) || group.getName().equalsIgnoreCase(type)) {
						final RosterVCardBo vCardBo = getVCardBo(entry.getUser().toLowerCase(), entry, type);
						generateChartPanels(vCardBo, UserWelcomeScreen.centerBox.getWidth());
						
						if (vCardBo.getSubscription() != null) {
							if (vCardBo.getSubscription().equals(ItemType.both)) {
								list.add(vCardBo);
								Thread t = new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										try {
											synchronized (vCardBo) {
												new DBServiceInsert().insertRosterTable(vCardBo);
												
											}
											Thread.sleep(10000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
								}});
								t.start();
							}
						}
						
					}
				}

			}
			setMessageEventManager();
			getRosterVCardBoListMap().put(type, list);
		}
		if (null != searchTxt && !"".equals(searchTxt)) {
			List<RosterVCardBo> filterList = new ArrayList<RosterVCardBo>();
			for (RosterVCardBo cardBo : list) {
				String lname = "";
				String[] names = cardBo.getName().split(" ");
				String fname = names[0];

				try {
					lname = names[1];
				} catch (Exception e) {
					lname = "";
				}
				String designation = cardBo.getDesignation();
				String title = cardBo.getJobTitle();

				boolean isSubscription = ItemType.both.equals(cardBo.getSubscription());
				boolean isFirstName = (!org.apache.commons.lang.StringUtils.isEmpty(fname) && fname.toLowerCase().startsWith(
						searchTxt.toLowerCase())) ? true : false;
				boolean isLastName = (!org.apache.commons.lang.StringUtils.isEmpty(lname) && lname.toLowerCase().startsWith(
						searchTxt.toLowerCase())) ? true : false;
				boolean isTitle = (!org.apache.commons.lang.StringUtils.isEmpty(title) && title.toLowerCase().startsWith(
						searchTxt.toLowerCase())) ? true : false;

				if (isSubscription && (isFirstName || isLastName || isTitle)) {
					filterList.add(cardBo);
				}
			}
			return filterList;
		}

		return list;
	}

	public static List<RosterVCardBo> getAllVCards(String searchTxt, boolean withGroup) throws XMPPException {
		List<RosterVCardBo> list = new ArrayList<RosterVCardBo>();
		List<RosterVCardBo> filterList = new ArrayList<RosterVCardBo>();
		if (list.isEmpty()) {
			List<RosterVCardBo> rosterList = new DBServiceResult().getRosterListAll(username.split("@")[0]);
			// Map<String, List<RosterVCardBo>> rosterVCardBoListMap =
			// getRosterVCardBoListMap();
			// for (List<RosterVCardBo> boList : rosterVCardBoListMap.values())
			// {
			list.addAll(rosterList);
			// }
			if (withGroup) {
				// getGroups();
				// list.addAll(getGroupVCardBOList());
			}
		}
		if (null != searchTxt && !"".equals(searchTxt)) {
			for (RosterVCardBo cardBo : list) {
				String[] names = null;
				String lname = "";
				String fname = "";
				if (!cardBo.getName().equals(""))
					names = cardBo.getName().split(" ");
				else
					names = cardBo.getUserId().split("@");

				fname = names[0];

				try {
					lname = names[1];
				} catch (Exception e) {
					lname = "";
				}
				String designation = cardBo.getDesignation();
				String title = cardBo.getJobTitle();

				boolean isSubscription = ItemType.both.equals(cardBo.getSubscription());
				boolean isFirstName = (!org.apache.commons.lang.StringUtils.isEmpty(fname) && fname.toLowerCase().startsWith(
						searchTxt.toLowerCase())) ? true : false;
				boolean isLastName = (!org.apache.commons.lang.StringUtils.isEmpty(lname) && lname.toLowerCase().startsWith(
						searchTxt.toLowerCase())) ? true : false;
				boolean isTitle = (!org.apache.commons.lang.StringUtils.isEmpty(title) && title.toLowerCase().startsWith(
						searchTxt.toLowerCase())) ? true : false;

				if (isSubscription && (isFirstName || isLastName || isTitle)) {
					filterList.add(cardBo);
				}
			}
			return filterList;
		}
		return list;
	}

	private static Map<String, List<RosterVCardBo>> getRosterVCardBoListMap() {
		if (null == rosterVCardBoListMap) {
			rosterVCardBoListMap = new HashMap<>();
		}
		return rosterVCardBoListMap;
	}

	public static Map<String, List<RosterVCardBo>> getReadByUser() {
		if (readByUserMap == null)
			readByUserMap = new HashMap<String, List<RosterVCardBo>>();
		return readByUserMap;
	}

	private static List<RosterVCardBo> getGroupVCardBOList() {
		if (null == groupVCardBOList) {
			groupVCardBOList = new ArrayList<RosterVCardBo>();
		}
		return groupVCardBOList;
	}

	public static void generateChartPanels(final RosterVCardBo vCard, final int centerBoxWidth) {
		// Runnable runnable = new Runnable() {
		// @Override
		// public void run() {
		// List<RosterVCardBo> memberList = vCard.getMemberList();
		// if(memberList)
		// memberList = new
		// DBServiceResult().getGroupDetailsAll(roomJid.split("@")[0]);
		if (vCard != null) {
			if (vCard.getMemberList() != null) {
				// Constants.isGroupChat = true;
				UserChatGroup.getUserChat(vCard, centerBoxWidth);
			} else {
				// Constants.isGroupChat = false;
				UserChat.getUserChat(vCard, centerBoxWidth);
			}
		}

		// }
		// };
		// new Thread(runnable).start();
	}

	public static void setVCardBo(String userId, RosterVCardBo vCardBo) {
		// if (!userId.contains("@"))
		// userId = userId + "@imyourdoc.com";
		if (null == rosterVCardBoMap)
			rosterVCardBoMap = new HashMap<>();
		rosterVCardBoMap.put(userId, vCardBo);
	}

	public static RosterVCardBo getVCardBo(String userId) {
		Roster roster = connection.getRoster();
		RosterEntry entry = roster.getEntry(userId);
		return getVCardBo(userId, entry, "");
	}

	public static boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
			return true;
		} catch (JSONException ex) {
			return false;
		}
	}

	/*public static RosterVCardBo getVCardBo(String userId, RosterEntry entry, String type) {
		RosterVCardBo vCardBo = new RosterVCardBo();
		if (null == rosterVCardBoMap)
			rosterVCardBoMap = new HashMap<>();
		if (rosterVCardBoMap.get(userId) != null)
			return rosterVCardBoMap.get(userId);

		VCard card = new VCard();
		try {
			vCardBo.setUserId(userId);
			if (entry != null)
				vCardBo.setEmail(entry.getName().split(" ")[0].toLowerCase() + "@imyourdoc.com");

			if (userId.endsWith("@imyourdoc.com")) {
				// if(!userId.contains("@imyourdoc.com"))
				// userId = userId +"@imyourdoc.com";
				card.load(connection, userId);
				String jbID = card.getField("JABBERID");
				String bday = card.getField("BDAY");
				vCardBo.setBday(bday);
				vCardBo.setIsGroup(false);
				// String XMLVCARD = card.getChildElementXML();
				if (jbID != null) {
					JSONArray array = new JSONArray(jbID);
					for (int j = 0; j < array.length(); j++) {
						JSONObject object = array.getJSONObject(j);
						String name_hospital = object.getString("name");
						String hosp_id = object.getString("hosp_id");
						String is_Primary = object.getString("is_primary");
						// db.addHospitals(userId, hosp_id, name_hospital,
						// is_Primary);
					}
				}
				// System.out.println(card.getPropertyNames());
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(card.getChildElementXML()));
				Document doc = db.parse(is);
				NodeList nodes = doc.getElementsByTagName("vCard");
				for (int i = 0; i < nodes.getLength(); i++) {
					Element element = (Element) nodes.item(i);
					NodeList nodeRole = element.getElementsByTagName("ROLE");
					Element role = (Element) nodeRole.item(0);
					if (role != null)
						vCardBo.setDesignation(getCharacterDataFromElement(role));

					NodeList nodeTitle = element.getElementsByTagName("TITLE");
					Element title = (Element) nodeTitle.item(0);
					if (title != null)
						vCardBo.setJobTitle(getCharacterDataFromElement(title));

					NodeList nodePractise = element.getElementsByTagName("DESC");
					Element practiseType = (Element) nodePractise.item(0);
					if (practiseType != null)
						vCardBo.setPractiseType(getCharacterDataFromElement(practiseType));
				}
				Presence presence = getRoster().getPresence(userId);

				vCardBo.setPresence(presence);
				// vCardBo.setName(StringUtils.parseBareAddress(presence.getFrom()));
				if (entry != null && entry.getName() != null)
					vCardBo.setName(entry.getName());
				else if (entry != null) {
					if (entry.getUser() != null)
						vCardBo.setName(entry.getUser().split("@")[0]);
					else
						vCardBo.setName(userId.split("@")[0]);

				}
				if (type != null) {
					if (!type.equals(""))
						vCardBo.setUserType(type);
				}
				if (entry != null)
					vCardBo.setSubscription(entry.getType());
				// vCardBo.setLastName(entryName[1] != null ? (" " +
				// entryName[1]) :
				// "");
				// if (card.getNickName() != null) {}
				// vCardBo.setEmail(card.getEmailWork());
				if (card.getAvatar() != null)
					vCardBo.setProfileImage(card.getAvatar());
				else {
					vCardBo.setProfileImage(Util.bufferImgToByteArray(Util.getProfileImg(userId)));
				}
			} else {
				vCardBo.setBday("0");
				vCardBo.setName(userId.split("@")[0]);
				vCardBo.setIsGroup(true);
				vCardBo.setJoined(true);

			}
			if (null == rosterVCardBoMap)
				rosterVCardBoMap = new HashMap<>();
			rosterVCardBoMap.put(userId, vCardBo);
		} catch (JSONException | XMPPException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return vCardBo;
	}*/
static ExecutorService backgroundPool=Executors.newFixedThreadPool(1);
	
	public static RosterVCardBo getVCardBo(final String userId, final RosterEntry entry, final String type) {
		final RosterVCardBo vCardBo = new RosterVCardBo();
		if (null == rosterVCardBoMap)
			rosterVCardBoMap = new HashMap<>();
		if (rosterVCardBoMap.get(userId) != null)
			return rosterVCardBoMap.get(userId);

		final VCard card = new VCard();
		try {
			vCardBo.setUserId(userId);
			if (entry != null)
				vCardBo.setEmail(entry.getName().split(" ")[0].toLowerCase() + "@imyourdoc.com");

			if (userId.endsWith("@imyourdoc.com")||!userId.contains("_")) {
				// if(!userId.contains("@imyourdoc.com"))
				// userId = userId +"@imyourdoc.com";
				vCardBo.setIsGroup(false);
				vCardBo.setBday("0");
				vCardBo.setDesignation("");
				
				vCardBo.setJobTitle("");
				if(entry!=null){
					if(entry.getName().length()>0)
					{
						vCardBo.setName(entry.getName());
					}
				}
				else
				{
				vCardBo.setName(userId.split("@")[0]);
				}
				vCardBo.setUserType(type);
			
				
				//vCardBo.setProfileImage(Util.bufferImgToByteArray(Util.getProfileImg(userId)));
				
				if (entry != null)
					vCardBo.setSubscription(entry.getType());
				
				
				Thread fetchVCard=new Thread(new Runnable() {
					
					@Override
					public void run() {
						try{
							if(!userId.contains("@")){
								try{
									card.load(connection, userId+"@imyourdoc.com");
								}
								catch(Exception e){
									System.out.println(e.getMessage());
									if(e.getMessage().equalsIgnoreCase("Connection is not authenticated")){
											connection.login(username, password);
									}
								}
							}
						
							else{
								card.load(connection, userId);		
							}
						String jbID = card.getField("JABBERID");
						String bday = card.getField("BDAY");
						vCardBo.setBday(bday);
						
						// String XMLVCARD = card.getChildElementXML();
						if (jbID != null) {
							JSONArray array = new JSONArray(jbID);
							for (int j = 0; j < array.length(); j++) {
								JSONObject object = array.getJSONObject(j);
								String name_hospital = object.getString("name");
								String hosp_id = object.getString("hosp_id");
								String is_Primary = object.getString("is_primary");
								// db.addHospitals(userId, hosp_id, name_hospital,
								// is_Primary);
							}
						}
						// System.out.println(card.getPropertyNames());
						DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
						InputSource is = new InputSource();
						is.setCharacterStream(new StringReader(card.getChildElementXML()));
						Document doc = db.parse(is);
						NodeList nodes = doc.getElementsByTagName("vCard");
						for (int i = 0; i < nodes.getLength(); i++) {
							Element element = (Element) nodes.item(i);
							NodeList nodeRole = element.getElementsByTagName("ROLE");
							Element role = (Element) nodeRole.item(0);
							if (role != null)
								vCardBo.setDesignation(getCharacterDataFromElement(role));

							NodeList nodeTitle = element.getElementsByTagName("TITLE");
							Element title = (Element) nodeTitle.item(0);
							if (title != null)
								vCardBo.setJobTitle(getCharacterDataFromElement(title));

							NodeList nodePractise = element.getElementsByTagName("DESC");
							Element practiseType = (Element) nodePractise.item(0);
							if (practiseType != null)
								vCardBo.setPractiseType(getCharacterDataFromElement(practiseType));
						}
						Presence presence = getRoster().getPresence(userId);

						vCardBo.setPresence(presence);
						// vCardBo.setName(StringUtils.parseBareAddress(presence.getFrom()));
						if (entry != null && entry.getName() != null)
							vCardBo.setName(entry.getName());
						else if (entry != null) {
							if (entry.getUser() != null)
								vCardBo.setName(entry.getUser().split("@")[0]);
							else
								vCardBo.setName(userId.split("@")[0]);

						}
						if (type != null) {
							if (!type.equals(""))
								vCardBo.setUserType(type);
						}
						if (entry != null)
							vCardBo.setSubscription(entry.getType());
						// vCardBo.setLastName(entryName[1] != null ? (" " +
						// entryName[1]) :
						// "");
						// if (card.getNickName() != null) {}
						// vCardBo.setEmail(card.getEmailWork());
						if (card.getAvatar() != null)
							vCardBo.setProfileImage(card.getAvatar());
						else {
							vCardBo.setProfileImage(Util.bufferImgToByteArray(Util.getProfileImg(userId)));
						}
						
						vCardBo.refresh();
						
					}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
					
					
				});
				
				backgroundPool.submit(fetchVCard);
		/*		
				card.load(connection, userId);
				String jbID = card.getField("JABBERID");
				String bday = card.getField("BDAY");
				vCardBo.setBday(bday);
				
				// String XMLVCARD = card.getChildElementXML();
				if (jbID != null) {
					JSONArray array = new JSONArray(jbID);
					for (int j = 0; j < array.length(); j++) {
						JSONObject object = array.getJSONObject(j);
						String name_hospital = object.getString("name");
						String hosp_id = object.getString("hosp_id");
						String is_Primary = object.getString("is_primary");
						// db.addHospitals(userId, hosp_id, name_hospital,
						// is_Primary);
					}
				}
				// System.out.println(card.getPropertyNames());
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(card.getChildElementXML()));
				Document doc = db.parse(is);
				NodeList nodes = doc.getElementsByTagName("vCard");
				for (int i = 0; i < nodes.getLength(); i++) {
					Element element = (Element) nodes.item(i);
					NodeList nodeRole = element.getElementsByTagName("ROLE");
					Element role = (Element) nodeRole.item(0);
					if (role != null)
						vCardBo.setDesignation(getCharacterDataFromElement(role));

					NodeList nodeTitle = element.getElementsByTagName("TITLE");
					Element title = (Element) nodeTitle.item(0);
					if (title != null)
						vCardBo.setJobTitle(getCharacterDataFromElement(title));

					NodeList nodePractise = element.getElementsByTagName("DESC");
					Element practiseType = (Element) nodePractise.item(0);
					if (practiseType != null)
						vCardBo.setPractiseType(getCharacterDataFromElement(practiseType));
				}
				Presence presence = getRoster().getPresence(userId);

				vCardBo.setPresence(presence);
				// vCardBo.setName(StringUtils.parseBareAddress(presence.getFrom()));
				if (entry != null && entry.getName() != null)
					vCardBo.setName(entry.getName());
				else if (entry != null) {
					if (entry.getUser() != null)
						vCardBo.setName(entry.getUser().split("@")[0]);
					else
						vCardBo.setName(userId.split("@")[0]);

				}
				if (type != null) {
					if (!type.equals(""))
						vCardBo.setUserType(type);
				}
				if (entry != null)
					vCardBo.setSubscription(entry.getType());
				// vCardBo.setLastName(entryName[1] != null ? (" " +
				// entryName[1]) :
				// "");
				// if (card.getNickName() != null) {}
				// vCardBo.setEmail(card.getEmailWork());
				if (card.getAvatar() != null)
					vCardBo.setProfileImage(card.getAvatar());
				else {
					vCardBo.setProfileImage(Util.bufferImgToByteArray(Util.getProfileImg(userId)));
				}
			*/
			
			} else {
				vCardBo.setBday("0");
				vCardBo.setName(userId.split("@")[0]);
				vCardBo.setIsGroup(true);
				vCardBo.setJoined(true);

			}
			if (null == rosterVCardBoMap)
				rosterVCardBoMap = new HashMap<>();
			rosterVCardBoMap.put(userId, vCardBo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vCardBo;
	}

	public static Chat createChat(String to, MessageListener messageListener, IAction iAction) throws XMPPException {
		ChatManager chatManager = connection.getChatManager();
		Chat chat = chatManager.createChat(to, messageListener);
		setReceivingConnection(to, iAction);
		return chat;
	}

	public static MultiUserChat createChatGroup(String roomId, MessageListener messageListener, IAction iAction) throws XMPPException {
		roomId = getGroupChatId(roomId);
		MultiUserChat multiUserChat = new MultiUserChat(connection, roomId);
		// setReceivingConnection(to, iAction);
		multiUserChat.createMessage();
		//listen2Group(roomId, iAction,false);
		return multiUserChat;
	}

	/**
	 * @author megha
	 * @param roomId
	 * @throws XMPPException
	 *             <p>
	 *             creates instance for MUC
	 *             </p>
	 */
	public static MultiUserChat getMUCInstance(String roomId) {
		if (!roomId.contains("@")) {
			roomId = roomId + "@newconversation.imyourdoc.com";
		}
		if (multiUserChatMap == null)
			multiUserChatMap = new HashMap<String, MultiUserChat>();
		MultiUserChat multiUserChat = multiUserChatMap.get(roomId);
		if (multiUserChat == null) {
			multiUserChat = new MultiUserChat(connection, roomId);
			multiUserChatMap.put(roomId, multiUserChat);
		}
		return multiUserChat;
	}

	public static Map<String,String> roomUserIds(String Roomid) {
		if(!Roomid.contains("@")){
			Roomid  = Roomid +"@newconversation.imyourdoc.com";
		}
		Map<String,String> members = new HashMap<String,String>();
		try {
			String membersResponse = new SendInvitationJSON().getRoomMembers(Roomid);
			JSONObject object = new JSONObject(membersResponse);
			JSONArray array = object.getJSONArray("members");
			for(int i=0;i<array.length();i++){
				members.put(array.getJSONObject(i).getString("jid"), array.getJSONObject(i).getString("membership"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return members;
	}

	public static String roomUsersStr(String RoomId) {
		StringBuilder userStr = new StringBuilder("");
		try {
			Map<String,String> map = roomUserIds(RoomId);
			for (String uid : map.keySet()) {
				if (userStr.toString().equals(""))
					userStr.append(uid.split("@")[0]);
				else
					userStr.append("," + uid.split("@")[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userStr.toString();
	}

	/**
	 * @author megha
	 * @param roomId
	 * @throws XMPPException
	 *             <p>
	 *             creates room for group chat
	 *             </p>
	 */
	public static void joinGroup(String roomId,boolean isOld,String lastActiveDate,String lastActiveSecs) {
		try {
			// roomId = getGroupChatId(roomId);
			DiscussionHistory history = new DiscussionHistory();
//			2015-08-13 03:20:02
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(lastActiveDate);
//			System.out.println("Yesterday's date = "+ cal.getTime());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -5);
			String newDateString = format.format(cal.getTime());
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			Date newDate = format2.parse(newDateString);
//			long daySeconds = (cal.get(Calendar.SECOND) +
//	                   cal.get(Calendar.MINUTE) * 60 +
//	                   cal.get(Calendar.HOUR) * 3600);
			int seconds = (int)(newDate.getTime()/1000);
			
			history.setSeconds(172800);
			//history.setMaxStanzas(20);
			if(!roomId.contains("@"))
				roomId = roomId + "@newconversation.imyourdoc.com";
			final String room = roomId;
			MultiUserChat multiUserChat = new MultiUserChat(connection, roomId);
			RoomInfo info = MultiUserChat.getRoomInfo(connection, roomId);
			if (info != null) {
				if (!multiUserChat.isJoined()) {
					multiUserChat.join(username.split("@")[0],"",history,10000L);
					// multiUserChat.destroy("", username);
				}
				
				
			}
			listen2GroupOld(roomId,null, true);
			// RosterVCardBo vCard = XmppUtils.getVCardBo(roomId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static boolean leaveGroup(String roomId, String userId) {
		String roomid = roomId;
		String subject = "";
		if (!roomid.contains("@newconversation.imyourdoc.com")) {
			roomid = roomid + "@newconversation.imyourdoc.com";
		}
		MultiUserChat muc = new MultiUserChat(connection,roomid);
		try {
			RoomInfo info = MultiUserChat.getRoomInfo(connection, roomId);
			if(info!=null){
				subject = info.getSubject();
				if(org.apache.commons.lang.StringUtils.isEmpty(subject)){
					subject = roomId.split("@")[0];
				}
			}
			else
			{
				subject = roomUsersStr(roomid);
			}
			if (!muc.isJoined()) {
				muc.join(username);
			}
			Message message = new Message(roomid, Message.Type.groupchat);
			RosterVCardBo vcard = createGroupVCardBO(roomid, subject, roomUserIds(roomId), false);
			String bday = vcard.getBday();

			if (bday == null) {
				bday = "0";
			}
			if (bday.equals("0")) {
				message.setBody("Left the room");
				message.setPacketID("REMOVE_REQUEST");
			} else if (bday.equals("1")) {
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
				format.setTimeZone(TimeZone.getTimeZone("UTC"));
				JSONObject object = new JSONObject();
				object.put("messageID", "REMOVE_REQUEST");
				object.put("content", "Left the room");
				object.put("from", Constants.loggedinuserInfo.username);
				object.put("timestamp", format.format(date));
				object.put("to", roomId);
				message.setBody(object.toString());
				message.setProperty("message_version", "2.0");
			}
			muc.sendMessage(message);
			muc.leave();
			boolean leave = new DBServiceUpdate().updateTableOnLeave(roomId, userId);
			if (leave)
				UserWelcomeScreen.removeMsg(vcard.getUserId());
			else
				System.out.println("Error..............");
			return true;
		} catch (XMPPException | JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean destroyGroup(String roomId) {
		if(!roomId.contains("@")){
			roomId = roomId+"@newconversation.imyourdoc.com";
		}
		MultiUserChat muc = new MultiUserChat(connection, roomId);
		try {
			if (!muc.isJoined()) {
				muc.join(username.split("@")[0]);
			}
			Message message = new Message(roomId, Message.Type.groupchat);
			RosterVCardBo vcard = createGroupVCardBO(roomId, roomUsersStr(roomId), roomUserIds(roomId), false);
			String bday = vcard.getBday();

			if (bday == null) {
				bday = "0";
			}
			if (bday.equals("0")) {
				message.setBody("DELETE_ROOM");
				message.setPacketID("IMYOURDOC_DELETE");
			} else if (bday.equals("1")) {
				Date date = new Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
				format.setTimeZone(TimeZone.getTimeZone("UTC"));
				JSONObject object = new JSONObject();
				object.put("messageID", "IMYOURDOC_DELETE");
				object.put("content", "DELETE_ROOM");
				object.put("from", Constants.loggedinuserInfo.username);
				object.put("timestamp", format.format(date));
				object.put("to", roomId);
				message.setBody(object.toString());
				message.setProperty("message_version", "2.0");
			}
			muc.sendMessage(message);
			muc.destroy("", username);
			new DBServiceUpdate().DeleteGroup(roomId);
			return true;
		} catch (XMPPException | JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static RosterVCardBo createGroupVCardBO(String roomId, String name, Map<String,String> memberIds, boolean isJoined) throws XMPPException {
		boolean hasPatient = false;
		List<RosterVCardBo> memberList = new ArrayList<RosterVCardBo>();
		BufferedImage img = null;
		RosterVCardBo vCardBo = new RosterVCardBo();
		if(!roomId.contains("@")){
			roomId = roomId+"@newconversation.imyourdoc.com";
		}
		RoomInfo info = MultiUserChat.getRoomInfo(connection, roomId);
		if(info!=null)
		{
			name = info.getSubject();
		}
		vCardBo.setName(name);
		vCardBo.setUserType("group");
		vCardBo.setGroupSubject(name);
		vCardBo.setSubscription(ItemType.both);
		vCardBo.setUserId(roomId);
		vCardBo.setIsGroup(true);
		vCardBo.setGroupSubject(name);
		
		for(String id:memberIds.keySet()){
			RosterVCardBo bo = getVCardBo(id);
			memberList.add(bo);
		}
		
		vCardBo.setMemberList(memberList);
		vCardBo.setJoined(isJoined);

		for (RosterVCardBo vcard : memberList) {
			if ("patient".equalsIgnoreCase(vcard.getUserType())) {
				vCardBo.setGroupHasPatient(true);
				hasPatient = true;
				
			}
			if (vcard.getUserId().split("@")[0].equals(roomId.split("_")[0])) {
				vCardBo.setGroupOwner(true);
			}
			// RosterVCardBo listGroup = new
			// DBServiceResult().getGroupDetails(roomId.split("@")[0],
			// vc.getUserId().split("@")[0]);
			// if (listGroup == null) {
			
			// }
		}

		try {
			if (hasPatient == true) {
				img = ImageIO.read(XmppUtils.class.getResource(Constants.IMAGE_PATH + "/" + "red_group_profile.png"));
			} else {
				img = ImageIO.read(XmppUtils.class.getResource(Constants.IMAGE_PATH + "/" + "group_profile.png"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		// RosterVCardBo listAll = new
		// DBServiceResult().getRosterUserDetails(roomId.split("@")[0]);
		// if (listAll == null) {
		//new DBServiceInsert().insertRosterTableGroup(vCardBo);
		// } else {
		//
		// }

		vCardBo.setProfileImage(Util.bufferImgToByteArray(img));
		setVCardBo(roomId, vCardBo);
		Util.setProfileImg(roomId, img);
		return vCardBo;
	}

	

public static void listen2GroupOld(final String roomId, final IAction iAction,final boolean isOld) {
		
		PacketFilter filter = new MessageTypeFilter(Message.Type.groupchat);
		connection.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				RosterVCardBo vCardBo = null;
				final Message message = (Message) packet;
				boolean isAllreadyShown = recievedMessagePacketIds.contains(message.getPacketID());
				if (!isAllreadyShown) {
					recievedMessagePacketIds.add(message.getPacketID());
					String from = message.getFrom();
					String fromId = from.substring(from.indexOf("/") + 1);
					SimpleDateFormat dateformat = new SimpleDateFormat("d MMM hh:mm:ss a");
					Date date = null;
					final DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
					if (delay != null) {
						date = delay.getStamp();
						if (Constants.showConsole)
							System.out.println(date);
					} else {
						date = new Date();
					}
					JLabel labelTime = new JLabel();
					labelTime.setText(dateformat.format(date));
					final String fromName = StringUtils.parseBareAddress(from);
					try {
						if (message.getBody() != null) {
							packet.setPacketID(message.getPacketID());
							//Constants.currentChatWindowUSERID = from.split("@")[0];
							
							// Create a MessageEvent Package and add it to the
							// message
							Constants.groupMemeber = fromId+"@imyourdoc.com";
							
							String body = message.getBody();
							String subject = message.getSubject();
							RoomInfo info = MultiUserChat.getRoomInfo(connection, fromName);
							MultiUserChat muc = new MultiUserChat(connection, fromName);
							if(!muc.isJoined())
								muc.join(Constants.loggedinuserInfo.username.split("@")[0]);
							String groupSubject = "";
							if(info!=null){
								groupSubject  = info.getSubject();
								if(org.apache.commons.lang.StringUtils.isEmpty(groupSubject))
								{
									groupSubject = roomUsersStr(fromName);
									
								}
							}
							else
							{
								groupSubject = fromName.split("@")[0];
							}
							vCardBo = createGroupVCardBO(fromName.split("@")[0], groupSubject, roomUserIds(fromName), true);

							// if(vCardBo ==null){
							//
							// }roomUserIds(fromName)
							Map<String, String> map = roomUserIds(fromName);
							for(String ids:map.keySet()){
								RosterVCardBo bo = XmppUtils.getVCardBo(ids);
								if(bo.getUserType()!=null){
									if(bo.getUserType().equalsIgnoreCase("patient")){
										vCardBo.setGroupHasPatient(true);
									}
								}
							}
							vCardBo.setIsGroup(true);
							
							try {
								String content = "";
								if (isJSONValid(body)) {
									JSONObject obj = new JSONObject(body);
									if (subject == null)
										content = obj.getString("content");
									else
										content = "File Recieved";
								} 
								else
								{
									content = message.getBody();
									
								}
								
								 MessageSyncBo messageSyncBo = new MessageSyncBo();
								 messageSyncBo.setWith(fromName);
								 messageSyncBo.setContent(content);
								 messageSyncBo.setMessageID(message.getPacketID());
								 messageSyncBo.setChatDate(dateformat.format(date));
								 messageSyncBo.setMessageFrom(from);
								 messageSyncBo.setMessageTo(message.getTo());
								 if(subject!=null){
									 String fileType = String.valueOf(message.getProperty("file_type"));
									 messageSyncBo.setFileType(fileType);
									 messageSyncBo.setFileUrl(subject);
								 }
								 else
								 {
									 messageSyncBo.setFileType("");
									 messageSyncBo.setFileUrl("");
								 }
													
								if(content.equals("DELETE ROOM")){
									if(!fromId.split("@")[0].equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0])){
										new DBServiceUpdate().DeleteGroup(fromName.split("@")[0]);
										UserWelcomeScreen.removeMsg(fromName.split("@")[0]);
										UserChatGroup.removeUserChat(vCardBo, false);
										UserWelcomeScreen.centerBox.removeAll();
										UserWelcomeScreen.centerBox.add(WelcomeUtils.welcomeBox());
										UserWelcomeScreen.centerBox.repaint();
										UserWelcomeScreen.centerBox.revalidate();
									}
								}
								else if(content.equals("Left the room"))
								{
									if(!fromId.equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0]))
									{
										new DBServiceUpdate().deleteMemberFromGroup(fromName.split("@")[0],fromId);
										UserWelcomeScreen.addMessages(vCardBo, fromId+" left the room", false, message.getPacketID(),dateformat.format(date),false);
									}
								}
									try {
										//if(!fromId.split("@")[0].equals(Constants.loggedinuserInfo.username.split("@")[0])){
										XmppUtils.generateChartPanels(vCardBo, (int) (Constants.SCREEN_SIZE.getWidth() * 0.60));
										UserChatGroup userChat = UserChatGroup.getUserChat(vCardBo,
												(int) (Constants.SCREEN_SIZE.getWidth() * 0.60));
										userChat.addMsgInBoxChatState(messageSyncBo,dateformat.format(date));
										if(!content.equals(Constants.REMOVE_CHAT_MSG))
											System.out.println("Group Chat Text Recieved older chat " + message.getBody() + " from " + fromId);
										//}
									} catch (Exception e) {
										e.printStackTrace();
									}
								
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							// String from = fromName.split("@")[0];
							// iAction.doAction(message, null);

							// RosterVCardBo vCardBo = new
							// DBServiceResult().getRosterUserDetails(from.split("@")[0]);
							// if(vCardBo ==null){
							// vCardBo = getVCardBo(from.split("@")[0]);
							// }
							// try {
							// String content = "";
							// if (isJSONValid(body)) {
							// JSONObject obj = new JSONObject(body);
							// if (subject == null)
							// content = obj.getString("content");
							// else
							// content = "File Recieved";
							// } else {
							// content = message.getBody();
							// }
							//
							// if (!content.equals(Constants.REMOVE_CHAT_MSG)) {
							// try {
							// System.out.println("inside listen2group"+content);
							// //WelcomeUtils.openChatWindow(vCardBo, false);
							// UserWelcomeScreen.addMessages(vCardBo, content,
							// true,message.getPacketID());
							// //generateChartPanels(vCardBo, (int)(
							// Constants.SCREEN_SIZE.getWidth()*0.60));
							// UserChatGroup userChat =
							// UserChatGroup.getUserChat(vCardBo, (int)(
							// Constants.SCREEN_SIZE.getWidth()*0.60));
							// userChat.addMsgInBox(message);
							// } catch (Exception e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }
							//
							//
							// }
							//
							// }
							// catch(Exception e){
							//
							// }

						}
					} catch (Exception e) {

					}
				}
			}
		}, filter);
	}

	public static void listen2Group(final String roomId, final IAction iAction,final boolean isOld) {
		
		PacketFilter filter = new MessageTypeFilter(Message.Type.groupchat);
		// final MessageEventManager messageEventManager = new
		// MessageEventManager(connection);
		connection.addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet packet) {
				final Message message = (Message) packet;
				boolean isAllreadyShown = recievedMessagePacketIds.contains(message.getPacketID());
				if (!isAllreadyShown) {
					recievedMessagePacketIds.add(message.getPacketID());
					String from = message.getFrom();
					final String fromId = from.substring(from.indexOf("/") + 1);
					SimpleDateFormat dateformat = new SimpleDateFormat("d MMM hh:mm:ss a");
					Date date = null;
					final DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
					if (delay != null) {
						date = delay.getStamp();
						if (Constants.showConsole)
							System.out.println(date);
					} else {
						date = new Date();
					}
					final String fromName = StringUtils.parseBareAddress(from);
					try {
						if (!(fromId.equals(Constants.loggedinuserInfo.username.split("@")[0])) && message.getBody() != null) {
							
							//Constants.currentChatWindowUSERID = from.split("@")[0];
							
							// Create a MessageEvent Package and add it to the
							// message
							Constants.groupMemeber = fromId+"@imyourdoc.com";
							Constants.currentChatWindowUSERID = fromName.split("@")[0];
							if (!org.apache.commons.lang.StringUtils.isEmpty(Constants.currentChatWindowUSERID)
									&& Constants.currentChatWindowUSERID.equals(fromName.split("@")[0])) {
								getMsgEventManager().sendDisplayedNotification(fromId + "@imyourdoc.com", message.getPacketID());
							} else {
								getPendingReadMsgList(fromId + "@imyourdoc.com").add(message.getPacketID());
							}

							RosterVCardBo vCardBo = null;
							String body = " " + message.getBody();
							String subject = message.getSubject();
							RoomInfo info = MultiUserChat.getRoomInfo(connection, fromName);
							MultiUserChat muc = new MultiUserChat(connection, fromName);
							if(!muc.isJoined())
								muc.join(Constants.loggedinuserInfo.username.split("@")[0]);
							String groupSubject = "";
							if(info!=null){
								groupSubject  = info.getSubject();
								if(org.apache.commons.lang.StringUtils.isEmpty(groupSubject))
								{
									groupSubject = roomUsersStr(fromName);
									
								}
							}
							else
							{
								groupSubject = fromName.split("@")[0];
							}
							vCardBo = createGroupVCardBO(fromName.split("@")[0], groupSubject, roomUserIds(fromName), true);

							// if(vCardBo ==null){
							//
							// }roomUserIds(fromName)
							Map<String, String> map = roomUserIds(fromName);
							for(String ids:map.keySet()){
								RosterVCardBo bo = XmppUtils.getVCardBo(ids);
								if(bo.getUserType()!=null){
									if(bo.getUserType().equalsIgnoreCase("patient")){
										vCardBo.setGroupHasPatient(true);
									}
								}
							}
							vCardBo.setIsGroup(true);
							
							try {
								String content = "";
								if (isJSONValid(body)) {
									JSONObject obj = new JSONObject(body);
									if (subject == null)
										content = obj.getString("content");
									else
										content = "File Recieved";
								} 
								else
								{
									content = message.getBody();
									
								}
									System.out.println("Group Chat Text Recieved now " + message.getBody() + " from " + fromId);
								if (!content.equals(Constants.REMOVE_CHAT_MSG) && !content.equals("DELETE_ROOM") && !content.equals("Left the room") && subject==null) {
									UserWelcomeScreen.addMessages(vCardBo, content, true, message.getPacketID(),dateformat.format(date),false);
								}
								else if(subject!=null){
									if(subject.startsWith("https://")){
										UserWelcomeScreen.addMessages(vCardBo, "File Received", true, message.getPacketID(),dateformat.format(date),false);
									}
								}
								else if(body.equals("DELETE ROOM")){
									new DBServiceUpdate().DeleteGroup(fromName.split("@")[0]);
									UserWelcomeScreen.removeMsg(fromName.split("@")[0]);
									UserChatGroup.removeUserChat(vCardBo, false);
									UserWelcomeScreen.centerBox.removeAll();
									UserWelcomeScreen.centerBox.add(WelcomeUtils.welcomeBox());
									UserWelcomeScreen.centerBox.repaint();
									UserWelcomeScreen.centerBox.revalidate();
								}
								else if(body.equals("Left the room")){
									new DBServiceUpdate().deleteMemberFromGroup(fromName.split("@")[0],fromId);
									UserWelcomeScreen.addMessages(vCardBo, fromId+" left the room", false, message.getPacketID(),dateformat.format(date),false);
									}
									try {
									
										UserChatGroup userChat = UserChatGroup.getUserChat(vCardBo,
												(int) (Constants.SCREEN_SIZE.getWidth() * 0.60));
										userChat.addMsgInBox(message,isOld,null);
										SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

											@Override
											protected Void doInBackground() throws Exception {
												if(!isOld)
													new SendInvitationJSON().updateMessageReceivers(message.getPacketID(), fromName,
															fromId + "@imyourdoc.com");
												return null;
											}
										};
										worker.execute();
									} catch (Exception e) {
										e.printStackTrace();
									}
								
							} catch (JSONException e) {
								e.printStackTrace();
							}
							
							// String from = fromName.split("@")[0];
							// iAction.doAction(message, null);

							// RosterVCardBo vCardBo = new
							// DBServiceResult().getRosterUserDetails(from.split("@")[0]);
							// if(vCardBo ==null){
							// vCardBo = getVCardBo(from.split("@")[0]);
							// }
							// try {
							// String content = "";
							// if (isJSONValid(body)) {
							// JSONObject obj = new JSONObject(body);
							// if (subject == null)
							// content = obj.getString("content");
							// else
							// content = "File Recieved";
							// } else {
							// content = message.getBody();
							// }
							//
							// if (!content.equals(Constants.REMOVE_CHAT_MSG)) {
							// try {
							// System.out.println("inside listen2group"+content);
							// //WelcomeUtils.openChatWindow(vCardBo, false);
							// UserWelcomeScreen.addMessages(vCardBo, content,
							// true,message.getPacketID());
							// //generateChartPanels(vCardBo, (int)(
							// Constants.SCREEN_SIZE.getWidth()*0.60));
							// UserChatGroup userChat =
							// UserChatGroup.getUserChat(vCardBo, (int)(
							// Constants.SCREEN_SIZE.getWidth()*0.60));
							// userChat.addMsgInBox(message);
							// } catch (Exception e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }
							//
							//
							// }
							//
							// }
							// catch(Exception e){
							//
							// }

						}
					} catch (Exception e) {

					}
				}
			}
		}, filter);
	}

	/**
	 * author megha
	 * 
	 * @param roomId
	 * @param userId
	 *            <p>
	 *            add single user to the group
	 *            </p>
	 * @return
	 */
	public static boolean deleteUserFromGroup(String roomId, String userId) {
		try {
			if (!roomId.contains("@")) {
				roomId = roomId + "@newconversation.imyourdoc.com";
			}
			if (!userId.contains("@")) {
				userId = userId + "@imyourdoc.com";
			}
			MultiUserChat muc = getMUCInstance(roomId);
			if (!muc.isJoined())
				muc.join(username.split("@")[0]);
			muc.revokeMembership(userId);
			new DBServiceUpdate().deleteMemberFromGroup(roomId.split("@")[0], userId.split("@")[0]);
			return true;
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * author megha
	 * 
	 * @param roomId
	 * @param userId
	 *            <p>
	 *            add single user to the group
	 *            </p>
	 * @return
	 */
	public static boolean addUserToGroup(String roomId, String userId) {
		try {
			if (!roomId.contains("@")) {
				roomId = roomId + "@newconversation.imyourdoc.com";
			}
			MultiUserChat muc = getMUCInstance(roomId);
			if (!muc.isJoined())
				muc.join(username.split("@")[0]);
			if (!userId.contains("@")) {
				userId = userId + "@imyourdoc.com";
			}
			muc.grantMembership(userId);
			// List<RosterVCardBo> members = roomUserIds(muc);
			RosterVCardBo userBO = new DBServiceResult().getRosterUserDetails(userId.split("@")[0]);
			if (userBO == null) {
				userBO = getVCardBo(userId);
			}
			// userBO.setJoined(true);
			// userBO.setMemberList(members);
			synchronized (userBO) {
				new DBServiceInsert().insertGroupMemberTable(userBO, roomId);
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @author megha
	 * @param roomJid
	 * @param userId
	 * @param memberList
	 * @return
	 */
	public static boolean createGroup(String roomJid, String userId, List<RosterVCardBo> memberList, String defaultSubject) {
		boolean ceateStatus = false;
		try {
			MultiUserChat mucc = new MultiUserChat(connection, roomJid);
			mucc.create(roomJid);
			mucc.join(userId);
			mucc.changeAvailabilityStatus(roomJid, Mode.available);
			mucc.changeSubject(defaultSubject);
			Form form = mucc.getConfigurationForm();
			form.setTitle(userId);
			// form.getField(arg0);
//			if (memberList.isEmpty())
//				memberList = new DBServiceResult().getGroupDetailsAll(roomJid.split("@")[0]);
			/*if (memberList.isEmpty())
				memberList = roomUserIds(roomJid);*/
			Form submitForm = form.createAnswerForm();
			Iterator<FormField> fields = form.getFields();
			while (fields.hasNext()) {
				FormField field = (FormField) fields.next();
				if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
					submitForm.setDefaultAnswer(field.getVariable());
				}
			}

			List<String> owners = new ArrayList<String>();
			owners.add(username);
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			List<String> grantList = new ArrayList<String>();

			for (RosterVCardBo bo : memberList) {
				String userIdMember = bo.getUserId();
				if (!userIdMember.contains("@imyourdoc.com"))
					userIdMember = userIdMember + "@imyourdoc.com";
				grantList.add(userIdMember);
			}
			mucc.grantMembership(grantList);
			mucc.sendConfigurationForm(submitForm);

			try {
				Collection<Occupant> adminlist = mucc.getModerators();
				if (Constants.showConsole)
					System.out.println("Occupant Members : " + adminlist);

				for (Iterator<Occupant> iterator = adminlist.iterator(); iterator.hasNext();) {
					Occupant occupant = (Occupant) iterator.next();
					String string[] = occupant.getJid().split("/");
					if (Constants.showConsole)
						System.out.println(string[0]);
				}

				/*Collection<Affiliate> memberlist = mucc.getMembers();
				if (Constants.showConsole)
					System.out.println("Affiliate Members : " + memberlist);*/
				// memberListDatas = new ArrayList<MemberListData>();
				/*Iterator<Affiliate> iteratorAffiliate = memberlist.iterator();
				while (iteratorAffiliate.hasNext()) {
					Affiliate affiliate = iteratorAffiliate.next();
					String string2 = affiliate.getJid();
					// if (Constants.showConsole)

					System.out.println("Members : " + string2);
				}*/
				RosterVCardBo userBO = createGroupVCardBO(roomJid, defaultSubject, roomUserIds(roomJid), true);// getVCardBo(roomJid.split("@")[0]);
				List<RosterVCardBo> finalList = new ArrayList<RosterVCardBo>();
				for (String s : grantList) {
					RosterVCardBo bo = new DBServiceResult().getRosterUserDetails(s.split("@")[0]);
					if (bo == null)
						bo = getVCardBo(s.split("@")[0]);
					if (bo.getUserType() != null) {
						if (bo.getUserType().equalsIgnoreCase("patient")) {
							userBO.setGroupHasPatient(true);
						}
					}
					finalList.add(bo);
				}

				
				ceateStatus = true;
				RosterVCardBo vcard = new DBServiceResult().getRosterUserDetails(roomJid.split("@")[0]);
				if (vcard == null) {
					userBO.setMemberList(finalList);
					userBO.setGroupOwner(true);
					userBO.setJoined(true);
					synchronized (userBO) {
						new DBServiceInsert().insertRosterTableGroup(userBO);
						new DBServiceInsert().insertGroupTable(userBO);
					}
					generateChartPanels(userBO, UserWelcomeScreen.centerBox.getWidth());
					// WelcomeUtils.openChatWindow(userBO, true);
				} else {
					// new DBServiceInsert().insertGroupTable(userBO);
					List<RosterVCardBo> list = new DBServiceResult().getGroupDetailsAll(roomJid.split("@")[0]);
					vcard.setMemberList(list);
					// WelcomeUtils.openChatWindow(vcard, true);
					generateChartPanels(vcard, UserWelcomeScreen.centerBox.getWidth());
				}
				// if (!getGroupVCardBOList().contains(vcard)) {
				// getGroupVCardBOList().add(vcard);
				// SerializeFile.SerializeObject(getGroupVCardBOList());
				// }

			} catch (Exception e) {
				e.printStackTrace();
				ceateStatus = false;
			}
			// obj.put("imageUrl", imagurl);
			// obj.put("title", title);
			// mucc.changeSubject(obj.toString());

		} catch (Exception e) {
			// ceateStatus = false;
			e.printStackTrace();
			ceateStatus = false;
		}
		return ceateStatus;
	}

	/**
	 * @author megha
	 * @param roomId
	 * @throws XMPPException
	 *             <p>
	 *             change roomID for group chat
	 *             </p>
	 */
	public static String changeRoomId(String roomId, String changedName) {
		try {
			if (!roomId.contains("@")) {
				roomId = roomId + "@newconversation.imyourdoc.com";
			}
			MultiUserChat muc = new MultiUserChat(connection, roomId);
			// getMUCInstance(roomId);
			RoomInfo info = MultiUserChat.getRoomInfo(connection, roomId);
			if(info!=null){
				if (!muc.isJoined()) {
					muc.join(Constants.loggedinuserInfo.username.split("@")[0]);
				}
				Message message = new Message();
				message.setType(Message.Type.groupchat);
				message.setSubject(changedName);
				message.setTo(roomId);
				message.setFrom(Constants.loggedinuserInfo.username);
				muc.sendMessage(message);
//				ChatManager chatManager = connection.getChatManager();
//				Chat chat = chatManager.createChat(roomId, new MessageListener() {
//					
//					@Override
//					public void processMessage(Chat arg0, Message arg1) {
//						// TODO Auto-generated method stub
//						
//					}
//				});
//				chat.sendMessage(message);
				//muc.changeSubject(changedName);
				// muc.changeSubject(changedName);
				new DBServiceUpdate().updateTableOnChangeSubject(roomId.split("@")[0], changedName);
				return changedName;
			}
			return "";
		} catch (XMPPException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static void send(String message, String to, MessageListener messageListener) throws XMPPException {
		if (Constants.showConsole)
			System.out.println("message to be sent,,,," + message + "...to.." + to);
		ChatManager chatManager = connection.getChatManager();
		Chat chat = chatManager.createChat(to, messageListener);
		chat.sendMessage(message);
		// setReceivingConnection();
	}

	public static void setReceivingConnection(final String to, final IAction iAction) {
		if (connection.isAuthenticated()) {
			final RosterVCardBo bo = getVCardBo(to.split("@")[0]);
			// Add a packet listener to get messages sent to us Message.Type.
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			
			connection.addPacketListener(new PacketListener() {
				@Override
				public void processPacket(Packet packet) {
					final Message message = (Message) packet;
					final String fromName = StringUtils.parseBareAddress(message.getFrom());
					if (to.equals(fromName) && message.getBody() != null) {
						Constants.currentChatWindowUSERID = fromName.split("@")[0];						
						System.out.println("New Chat Text Recieved " + message.getBody() + " from " + fromName);
						getPendingReadMsgList(fromName).add(message.getPacketID());
						//msg2.setType(Message.Type.chat);

						// Create a MessageEvent Package and add it to the
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

							@Override
							protected Void doInBackground() throws Exception {
								new SendInvitationJSON().updateMessageReceivers(message.getPacketID(), fromName,
										Constants.loggedinuserInfo.username);
								return null;
							}

						};// message
						worker.execute();
					
						String body = " " + message.getBody();
						String subject = message.getSubject();
						String from = fromName.split("@")[0];
						
						iAction.doAction(message, null);
					}
				}
			}, filter);
		}
	}

	private static List<String> getPendingReadMsgList(String userId) {
		List<String> pendingReadMsgList = pendingReadMsg.get(userId);
		if (pendingReadMsgList == null) {
			pendingReadMsgList = new ArrayList<>();
			pendingReadMsg.put(userId, pendingReadMsgList);
		}
		return pendingReadMsgList;
	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof org.w3c.dom.CharacterData) {
			org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData) child;
			return cd.getData();
		}
		return "";
	}

	public static MessageEventManager getMsgEventManager() {
		if (null == eventManager)
			eventManager = new MessageEventManager(XmppUtils.connection);
		return eventManager;
	}

	public static void setMessageEventManager() {
		getMsgEventManager();
		// MessageEventManager.addNotificationsRequests(message,
		// true, true, true, true);
		// messageEventManager.sendCancelledNotification(buddyJID,
		// message.getPacketID());

		// Presence presence = connection.getRoster().getPresence(buddyJID);

		eventManager.addMessageEventNotificationListener(new MessageEventNotificationListener() {

			@Override
			public void offlineNotification(String userJDId, String arg1) {
				// System.out.println(userJDId);
				if (Constants.showConsole)
					System.out.println("Offline--" + arg1);
				// statusLabel.setText("Sending");
				// send notification after 30 seconds

			}

			@Override
			public void displayedNotification(String userJDId, String arg1) {
				if (null == statusLabels)
					statusLabels = new HashMap<>();
				String userId = "";
				System.out.println("Displayed--" + userJDId);
				System.out.println(arg1);
				// if (userJDId.contains("@imyourdoc.com")) {
				// userId = userJDId.split("/")[0];
				// }
				// else{
				// userId = userJDId;
				// }
				/*
				 * else if (userJDId.contains("@newconversation.imyourdoc.com"))
				 * { if (userJDId.contains("/")) userId =
				 * userJDId.split("/")[1]; else userId =
				 * Constants.loggedinuserInfo.username.split("@")[0]; userId =
				 * userId + "@imyourdoc.com"; }
				 */
				new DBServiceUpdate().updateMessageStatusTable("Read", arg1, userJDId.split("@")[0]);
				List<JLabel> list = statusLabels.get(arg1);
				if (list != null) {
					for (JLabel jLabel : list) {
						if (jLabel != null) {
							// if(!"Uploading".equalsIgnoreCase(jLabel.getText())){
							String status = new DBServiceResult().getChatStatus(arg1);
							jLabel.setText(status);
							// }
						}
					}
				}
			}

			@Override
			public void deliveredNotification(String userJDId, String arg1) {
				if (null == statusLabels)
					statusLabels = new HashMap<>();
				String userId = "";
				System.out.println("Delivered--" + userJDId);
				System.out.println(arg1);
				// if (userJDId.contains("@imyourdoc.com")) {
				// userId = userJDId.split("/")[0];
				// }
				// else{
				// userId = userJDId;
				// }
				/*
				 * else if (userJDId.contains("@newconversation.imyourdoc.com"))
				 * { if (userJDId.contains("/")) userId =
				 * userJDId.split("/")[1]; else userId =
				 * Constants.loggedinuserInfo.username.split("@")[0]; userId =
				 * userId + "@imyourdoc.com"; }
				 */
				new DBServiceUpdate().updateMessageStatusTable("Delivered", arg1, userJDId.split("@")[0]);
				List<JLabel> list = statusLabels.get(arg1);
				if (list != null) {
					for (JLabel jLabel : list) {
						if (jLabel != null) {
							// if(!"Uploading".equalsIgnoreCase(jLabel.getText())){
							String status = new DBServiceResult().getChatStatus(arg1);
							jLabel.setText(status);
							// }
						}
					}
				}

				// jLabel.setText("Delivered");
			}

			@Override
			public void composingNotification(String userJDId, String arg1) {
				// System.out.println(userJDId);
				// if(Constants.showConsole)
				// System.out.println("Composing--" + arg1);
				if (userJDId.contains("/") && userJDId.contains("@newconversation.imyourdoc.com"))
					userJDId = userJDId.split("/")[1];
				else
					userJDId = userJDId.split("@")[0];
				// Constants.STATUS = "typing";
				// JLabel labelType = Constants.typeStatus;
				String currentChatId = Constants.currentChatWindowUSERID != null ? Constants.currentChatWindowUSERID.split("@")[0] : "";
				if (userJDId.equals(currentChatId) && !Constants.loggedinuserInfo.username.equals(userJDId))
					Constants.typeStatus.setText(userJDId + " is typing..");
			}

			@Override
			public void cancelledNotification(String userJDId, String arg1) {
				// System.out.println(userJDId);
				// System.out.println(arg1);
				if (userJDId.contains("/") && userJDId.contains("@newconversation.imyourdoc.com"))
					userJDId = userJDId.split("/")[1];
				else
					userJDId = userJDId.split("@")[0];
				String currentChatId = Constants.currentChatWindowUSERID != null ? Constants.currentChatWindowUSERID.split("@")[0] : "";
				if (userJDId.equals(currentChatId) && !Constants.loggedinuserInfo.username.equals(userJDId))
					Constants.typeStatus.setText("");
				// Constants.STATUS = "Cancelled";
			}
		});
	}

	public static void setStatusLabel(final String messageId, final JLabel jLabel) {
		jLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		jLabel.setForeground(Color.gray);
		jLabel.setText("Sending");
		if (null == statusLabels)
			statusLabels = new HashMap<>();
		List<JLabel> statusList = statusLabels.get(messageId);
		if (statusList == null)
			statusList = new ArrayList<>();
		statusList.add(jLabel);
		statusLabels.put(messageId, statusList);
	}

	public static void sendPendingReadStatus(String userId) {
		List<String> pendingReadMsgs = pendingReadMsg.get(userId);
		if (pendingReadMsgs != null) {
			for (Iterator<String> iter = pendingReadMsgs.listIterator(); iter.hasNext();) {
				String packetId = iter.next();
				if (packetId != null) {
					getMsgEventManager().sendDisplayedNotification(userId, packetId);
					iter.remove();
				}
			}
		}
	}

	public static String getDateStemp(Message message) {
		Date date = null;
		DelayInformation delay = message != null ? (DelayInformation) message.getExtension("x", "urn:xmpp:delay") : null;
		if (delay != null)
			date = delay.getStamp();
		else
			date = new Date();

		return new SimpleDateFormat("d MMM h:mm a ").format(date);
	}

	public static String getChatId(String id) {
		if (!id.contains("@"))
			id += "@imyourdoc.com";
		return id;
	}

	public static String getGroupChatId(String id) {
		if (!id.contains("@"))
			id += "@newconversation.imyourdoc.com";
		return id;
	}
}

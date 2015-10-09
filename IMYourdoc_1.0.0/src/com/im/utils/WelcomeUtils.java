package com.im.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.SubjectUpdatedListener;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.jivesoftware.smackx.packet.VCard;
import org.json.JSONException;
import org.json.JSONObject;

import sun.swing.StringUIClientPropertyKey;

import com.im.bo.ActiveUserBo;
import com.im.bo.ConversationBo;
import com.im.bo.CustomComparator;
import com.im.bo.CustomComparatorConversation;
import com.im.bo.CustomComparatorMessage;
import com.im.bo.GroupBo;
import com.im.bo.LoginUserBO;
import com.im.bo.MessageSyncBo;
import com.im.bo.RosterVCardBo;
import com.im.chat.UserChat;
import com.im.common.LightScrollPane;
import com.im.common.Members;
import com.im.db.DBServiceInsert;
import com.im.db.DBServiceResult;
import com.im.groupChat.DummyChatScreen;
import com.im.groupChat.UserChatGroup;
import com.im.json.SendInvitationJSON;
import com.im.login.Login;
import com.im.login.MenuScroller;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.user.ViewUserProfile;
import com.oracle.jrockit.jfr.UseConstantPool;

public class WelcomeUtils {
	private final static String IMAGE_PATH = "/images";
	public static JPanel welcomeBox = null;
	public static Map<String, JPanel> chatPanelMap = null;
	public static Map<String, JPanel> chatPanelMapGroup = null;
	public static JLabel loginUserProfilePic;
	public static BufferedImage loginUserProfileIcon;
 //	private static JLabel profilePic = null;
	private static int threadCount =0;
	public static Map<String, RosterVCardBo> rosterVcardAllUsersMap;
	private static JPanel panelToAddUsers;
	private static Box panel = Box.createHorizontalBox();
	private static JTextField composeTextField;
	private static String name = "";
	private static String title = "";
	public static JLabel profile = new JLabel();
	private static int count = 0;
	public static Box getRosterList(Box jPanel, List<RosterVCardBo> vCardList, String groupName) throws IOException {
		JLabel profilePic = null;
		if (!vCardList.isEmpty()) {
			Collections.sort(vCardList, new CustomComparator());
			for (final RosterVCardBo vCard : vCardList) {
				
				String userId = vCard.getUserId();
				BufferedImage icon = Util.getProfileImg(userId);
				if (icon == null) {
					icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));

				}
				profilePic = Util.combine(icon, false, 60, 60);
				JPanel profilePanel = new JPanel(new BorderLayout());

				final Box horBox = Box.createHorizontalBox();
				Box vbox = Box.createVerticalBox();
				StringBuffer namebuffer = new StringBuffer();
				namebuffer.append(vCard.getName());
				if (vCard.getDesignation() != null && vCard.getDesignation().trim().equals("")) {
					namebuffer.append(vCard.getDesignation() == null ? "" : ", " + vCard.getDesignation());
				}
				String name = namebuffer.toString();
				StringBuffer titleBuffer = new StringBuffer();
				titleBuffer.append(vCard.getJobTitle() == null ? "" : vCard.getJobTitle());
				String title = titleBuffer.toString();
				if (title.length() > 20) {
					title = title.substring(0, 20) + "...";
				}
				profilePic.setText("<html><font style='color:#9CCD21;'>" + name + "</font><br/>" + title + "</html>");
				profilePic.setIconTextGap(20);
				profilePanel.setOpaque(true);
				profilePanel.setBackground(Color.white);
				profilePanel.add(profilePic, BorderLayout.WEST);
				profilePic.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				vbox.setOpaque(true);
				horBox.setAlignmentX(SwingConstants.LEFT);
				horBox.add(profilePanel, BorderLayout.WEST);
				horBox.setOpaque(true);
				horBox.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.width * 0.20),
						(int) (Constants.SCREEN_SIZE.height * 0.10)));
				horBox.setBackground(Color.white);
				horBox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				horBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
				horBox.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent arg0) {
						if (Constants.showConsole)
							System.out.println("mouse clicked on userId : " + vCard.getUserId());
						WelcomeUtils.openChatWindow(vCard, false);
						if (Constants.ComposeParent != null) {
							Constants.ComposeParent.dispose();
							Constants.ComposeParent = null;
							Constants.PARENT.setEnabled(true);
							Constants.PARENT.toFront();
						}
					}
				});
				JPanel pfinal = new JPanel();
				pfinal.add(horBox);
				pfinal.setOpaque(true);
				pfinal.setBackground(Color.white);
				jPanel.add(pfinal);
				vCard.panel=profilePanel;
				vCard.title=profilePic;
			}
		} else {
			JLabel noMessgage = new JLabel("<HTML><I><br>No Users to display</br></I></HTML>");
			noMessgage.setOpaque(true);
			noMessgage.setBackground(Color.white);
			noMessgage.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
			JPanel messagePanel = new JPanel();
			// if no list
			noMessgage.setForeground(Color.gray);
			messagePanel.setOpaque(true);
			messagePanel.setBackground(Color.white);
			messagePanel.add(noMessgage, BorderLayout.CENTER);
			jPanel.add(messagePanel);
		}

		jPanel.setOpaque(true);
		jPanel.setBackground(null);

		return jPanel;
	}

	public static void openChatWindowCompose(String userId, boolean isComposeMessage) throws XMPPException {
		rosterVcardAllUsersMap = new HashMap<String, RosterVCardBo>();
		composeTextField = new JTextField(10) {
			private static final long serialVersionUID = 1L;

			@Override
			public void addNotify() {
				super.addNotify();
				requestFocus();
			}
		};
		JLabel labelTo = new JLabel("To: ");
		labelTo.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		composeTextField.setBorder(BorderFactory.createEmptyBorder());
		composeTextField.setFont(new Font(Font.decode("CentraleSansRndBold").getFontName(), Font.PLAIN, 20));
		composeTextField.setOpaque(false);
		composeTextField.setBackground(Color.white);
		composeTextField.setForeground(Color.gray);
		panelToAddUsers = new JPanel(new FlowLayout());
		panelToAddUsers.setOpaque(true);
		panelToAddUsers.setBackground(Color.white);
		if (!userId.equals("")) {
			final RosterVCardBo vCard = XmppUtils.getVCardBo(userId);
			try {
				addUsersToPanel(vCard);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		MouseListener sendTextFieldClick = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent ae) {
				if (rosterVcardAllUsersMap != null) {
					if (rosterVcardAllUsersMap.size() == 1) {
						for (RosterVCardBo vcard : rosterVcardAllUsersMap.values()) {
							openChatWindow(vcard, true);
							rosterVcardAllUsersMap = null;
						}
					} else if (rosterVcardAllUsersMap.size() > 1) {
						
						JPanel loaderPanel = Util.loaderPanel("Creating Group..");
						UserWelcomeScreen.centerBox.removeAll();
						UserWelcomeScreen.centerBox.add(loaderPanel);
						UserWelcomeScreen.centerBox.revalidate();
						UserWelcomeScreen.centerBox.repaint();
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

							@Override
							protected Void doInBackground() throws Exception {
								SimpleDateFormat dateformat = new SimpleDateFormat("ddMMyyhmmss");
								SimpleDateFormat dateformat2= new SimpleDateFormat("d MMM hh:mm a");
								SimpleDateFormat dateformat3= new SimpleDateFormat("d MMM hh:mm:ss a");
								Date date = new Date();
								String userid = Constants.loggedinuserInfo.username;
								if (userid.contains("@")) {
									userid = userid.split("@")[0];
								}
								String roomJid = XmppUtils.getGroupChatId(userid + "_" + dateformat.format(new Date()));
								List<RosterVCardBo> memberIds = new ArrayList<RosterVCardBo>();
								memberIds.addAll(rosterVcardAllUsersMap.values());
								StringBuffer defaultSubject = new StringBuffer();
								Collection<RosterVCardBo> listBo = rosterVcardAllUsersMap.values();
								Iterator<RosterVCardBo> itr = listBo.iterator();
								int count = 0;
								while (itr.hasNext()) {
									count++;
									if (count == listBo.size()) {
										defaultSubject.append(itr.next().getName());
									} else {
										defaultSubject.append(itr.next().getName() + ",");
									}

								}
								boolean createdGroup = XmppUtils.createGroup(roomJid, userid, memberIds, defaultSubject.toString());
								if (createdGroup) {
									// RosterVCardBo vCardBo =
									// XmppUtils.createGroupVCardBO(roomJid,
									// roomUsersStr(), memberIds,true);
									RosterVCardBo bo = new DBServiceResult().getRosterUserDetails(roomJid.split("@")[0]);
									bo.setMemberList(memberIds);
									try {
										
										UserWelcomeScreen.addMessages(bo, "Group Chat", false,"",dateformat3.format(date),false);
										openChatWindow(bo, true);
										rosterVcardAllUsersMap = null;
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								else
								{
									JPanel errorCreatingGroup = new JPanel();
									errorCreatingGroup.setOpaque(true);
									errorCreatingGroup.setBackground(null);
									JLabel label = new JLabel(new ImageIcon(WelcomeUtils.class
											.getResource(Constants.IMAGE_PATH + "/"
													+ "error.png")));
									label.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 15));
									label.setText("Error creating group, Please try again!");
									label.setHorizontalTextPosition(JLabel.CENTER);
									label.setVerticalTextPosition(JLabel.BOTTOM);
									label.setForeground(Color.gray.darker());
									errorCreatingGroup.add(label, BorderLayout.CENTER);
									UserWelcomeScreen.centerBox.removeAll();
									UserWelcomeScreen.centerBox.add(errorCreatingGroup);
									UserWelcomeScreen.centerBox.revalidate();
									UserWelcomeScreen.centerBox.repaint();
								}
								return null;
							}
							
						};
						worker.execute();
						
					}
				}
			}
		};

		composeTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField textfield = ((JTextField) e.getSource());
				String text = ((JTextField) e.getSource()).getText();
				try {
					if (!text.equals("")) {
						List<RosterVCardBo> list = XmppUtils.getAllVCards(text, true);
						showListUsersMenu(e, list);
						textfield.requestFocus();
				}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});
		composeTextField.addFocusListener(new FocusListener() 
			 {
			   @Override
			   public void focusLost(FocusEvent e) {
			    // TODO Auto-generated method stub
			   }
			   @Override
			   public void focusGained(FocusEvent e) {
			    // TODO Auto-generated method stub
			    composeTextField.setCaretPosition(composeTextField.getText().length());
			    composeTextField.setSelectionStart(composeTextField.getText().length());
			   }
			  });
		JPanel topBar = new JPanel(new BorderLayout());
		topBar.setOpaque(true);
		topBar.setBackground(Color.white);
		topBar.add(Box.createHorizontalGlue());
		panel = Box.createHorizontalBox();
		panel.setOpaque(true);
		panel.setBackground(Color.white);
		panel.add(labelTo, BorderLayout.WEST);

		JScrollPane scp = new JScrollPane(panelToAddUsers);
		scp.setPreferredSize(new Dimension(500, 60));
		// scp.setMinimumSize(new Dimension(50, 80));
		scp.setBorder(null);
		JScrollBar verticalScrollBar = scp.getVerticalScrollBar();
		JScrollBar horizontalScrollBar = scp.getHorizontalScrollBar();
		horizontalScrollBar.setUI(new MyScrollBarUI());
		horizontalScrollBar.setBackground(null);
		horizontalScrollBar.setVisible(false);
		horizontalScrollBar.setOpaque(true);
		verticalScrollBar.setUI(new MyScrollBarUI());
		verticalScrollBar.setBackground(null);
		verticalScrollBar.setVisible(false);
		verticalScrollBar.setOpaque(true);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(composeTextField);
		panel.add(Box.createHorizontalStrut((int) (Constants.PARENT.getWidth() * 0.30)));
		// panel.add(okButton, BorderLayout.EAST);

		topBar.add(panel, BorderLayout.WEST);
		topBar.add(Box.createHorizontalGlue());

		JPanel topBarPanel = new JPanel(new BorderLayout());
		topBarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		topBarPanel.add(topBar);
		topBarPanel.setBackground(null);
		topBarPanel.setPreferredSize(new Dimension((int) (Constants.PARENT.getWidth() * 0.50), 60));
		Box vBox = Box.createVerticalBox();
		vBox.setBackground(null);
		vBox.setOpaque(true);
		vBox.add(scp, BorderLayout.NORTH);
		vBox.add(topBarPanel, BorderLayout.NORTH);
		vBox.add(DummyChatScreen.getUserChat(null, UserWelcomeScreen.centerBox.getWidth(), sendTextFieldClick));

		JPanel chatPanel = new JPanel();
		chatPanel.add(vBox);
		chatPanel.setOpaque(true);
		chatPanel.setBackground(Color.white);
		UserWelcomeScreen.centerBox.setOpaque(true);
		UserWelcomeScreen.centerBox.setBackground(Color.white);
		UserWelcomeScreen.centerBox.removeAll();
		UserWelcomeScreen.centerBox.add(chatPanel);
		UserWelcomeScreen.centerBox.revalidate();
		UserWelcomeScreen.centerBox.repaint();
		// pack();

	}

	private static void addUsersToPanel(final RosterVCardBo vCard) throws IOException {
		final Box boxAdd = Box.createHorizontalBox();
		Box hbox = Box.createHorizontalBox();
		BufferedImage icon = null;
		boolean isDefalutImage = false;
		final JPanel panelname = new JPanel();
		String name = vCard.getName();
		if (name.length() > 20) {
			name = name.substring(0, 20);
		}
			if (vCard.isGroup() == false) {
				icon = Util.getProfileImg(vCard.getUserId());
				if (icon == null) {
					icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));
					isDefalutImage = true;
				}
			}
		JLabel labelname = Util.combineCompose(icon, true, 40, 40,isDefalutImage);
		labelname.setText(name);
		labelname.setForeground(Color.white.brighter());
		panelname.setName(vCard.getUserId());
		//panelname.setSize(new Dimension(200, 20));
		labelname.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		hbox.add(labelname);
		panelname.setOpaque(true);
		panelname.setBackground(Color.decode("#535456"));
//		panelname.setBorder(new TextBubbleBorder(Color.darkGray, 2, 2, 0));
		JLabel cross = new JLabel("X");
		cross.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		cross.setForeground(Color.red);
//		hbox.add(cross);

		// scBox.setMaximumSize(new Dimension((int)
		// (Constants.SCREEN_SIZE.getWidth() * 0.20), 50));
		// scBox.setPreferredSize(new Dimension((int)
		// (Constants.SCREEN_SIZE.getWidth() * 0.5), 50));
		panelname.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				boxAdd.remove(panelname);
				boxAdd.repaint();
				boxAdd.revalidate();
				// scBox.repaint();
				// scBox.revalidate();
				panelToAddUsers.repaint();
				panelToAddUsers.revalidate();
				rosterVcardAllUsersMap.remove(vCard.getUserId());
			}
		});

		panelname.add(hbox);
		if (rosterVcardAllUsersMap.get(vCard.getUserId()) == null) {
			rosterVcardAllUsersMap.put(vCard.getUserId(), vCard);
			composeTextField.setText("");
			composeTextField.requestFocus();
			boxAdd.add(panelname);
			// scBox.repaint();
			// scBox.revalidate();
			boxAdd.repaint();
			boxAdd.revalidate();
			panelToAddUsers.add(boxAdd);
			panelToAddUsers.repaint();
			panelToAddUsers.revalidate();
		}

		composeTextField.setText("");
		composeTextField.requestFocus();
		panel.repaint();
		panel.revalidate();
	}

	private static String roomUsersStr() {
		Map<String, RosterVCardBo> vCardMap = rosterVcardAllUsersMap;
		StringBuilder userStr = new StringBuilder("");
		if (vCardMap != null) {
			for (String ids : vCardMap.keySet()) {
				if (userStr.toString().equals(""))
					userStr.append(ids.split("@")[0]);
				else
					userStr.append("," + ids.split("@")[0]);
			}
		}
		return userStr.toString();
	}

	public static void openChatWindow(final RosterVCardBo vCard, boolean isComposeMessage) {
		JLabel profilePic = new JLabel();
		if (vCard != null) {
			String userId = vCard.getUserId();
			if(userId.contains("@")){
					userId = userId.split("@")[0];
				}
			BufferedImage icon = null;

			if (null == chatPanelMap) {
				chatPanelMap = new HashMap<String, JPanel>();
			}
			JPanel chatPanel = chatPanelMap.get(userId);
			if (null == chatPanel) {
				JPanel profilePanel = new JPanel();
				try {
					if (vCard.getMemberList()==null) {
						icon = Util.getProfileImg(userId);
						if (icon == null) {
							icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));
						}
					} else {
						if (vCard.groupHasPatient() == false) {
							icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"group_profile.png"));
						} else {
							icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"red_group_profile.png"));
						}
					}
					profilePic = Util.combine(icon, false, 60, 60);
				} catch (Exception e) {
					e.printStackTrace();
				}
				final StringBuffer namebuffer = new StringBuffer();

				if (vCard.getMemberList()!=null) {
					try {
						int count = 0;
						String uid = vCard.getUserId();
						if(!uid.contains("@")){
							uid = uid+"@newconversation.imyourdoc.com";
						}
						MultiUserChat muc  = new MultiUserChat(XmppUtils.connection,uid);
						if(!muc.isJoined())
							muc.join(Constants.loggedinuserInfo.username.split("@")[0]);
						muc.addSubjectUpdatedListener(new SubjectUpdatedListener() {

							@Override
							public void subjectUpdated(String subject, String from) {
								vCard.setGroupSubject(subject);
							}
						});
								String subject = "";
								if(!StringUtils.isEmpty(vCard.getGroupSubject())){
									subject = vCard.getGroupSubject();
								}
								else if(!StringUtils.isEmpty(vCard.getName())){
									subject = vCard.getName();
								}
								else {
									subject = uid.split("@")[0];
								}
								namebuffer.append(subject);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					namebuffer.append(vCard.getName() == "" ? vCard.getUserId().split("@")[0] : vCard.getName());
					if (vCard.getDesignation() != null && vCard.getDesignation().trim().equals("")) {
						namebuffer.append(vCard.getDesignation() == null ? "" : ", " + vCard.getDesignation());
					}
				}
				name = namebuffer.toString();
				if (name.length() > 30) {
					name = name.substring(0, 30) + "...";
				}
				StringBuffer titleBuffer = new StringBuffer();
				titleBuffer.append(vCard.getJobTitle() == null ? "" : vCard.getJobTitle());
				title = titleBuffer.toString();
				if (title.length() > 20) {
					title = title.substring(0, 20) + "...";
				}
				profilePic.setText("<html><font style='color:#9CCD21;'>" + name + "</font><br/>" + title + "</html>");
				profilePic.repaint();
				profilePic.revalidate();
				profilePic.setIconTextGap(5);
				profilePic.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				profilePanel.setOpaque(true);
				profilePanel.setBackground(null);
				profilePanel.add(profilePic);
				profilePic.setCursor(new Cursor(Cursor.HAND_CURSOR));
				profilePic.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent me) {
						if (vCard.getMemberList()==null) {
							
							Constants.loader.setVisible(true);
							Constants.PARENT.setEnabled(false);
							SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
								@Override
								protected Void doInBackground() throws Exception {
									ViewUserProfile userProfile = new ViewUserProfile(Constants.PARENT, vCard.getUserId().split("@")[0]);
									userProfile.setVisible(true);
									Constants.loader.setVisible(false);
									Constants.PARENT.setEnabled(true);
									return null;
								}

							};
							worker.execute();
							// showPopupMemberList(me, vCard.getMemberList());
							
						} else {
							String uid = vCard.getUserId();
							
							 if(!uid.contains("@newconversation.imyourdoc.com"))
							 {
								 uid = uid+"@newconversation.imyourdoc.com"; 
							 }
							 
							List<RosterVCardBo> members = new DBServiceResult().getGroupDetails(uid.split("@")[0]);
//							if(members.isEmpty())
//							 Map<String,String> members = XmppUtils.roomUserIds(uid);
							Members compose = new Members(Constants.mainFrame, members, "Group Members", vCard.getUserId());
							compose.setVisible(true);
						}

					}
				});
				JLabel moreOptions = new JLabel(new ImageIcon(((new ImageIcon(WelcomeUtils.class.getResource(IMAGE_PATH
						+ "/more_options.png"))).getImage()).getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);

				moreOptions.setPreferredSize(new Dimension(60, 60));
				moreOptions.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent ae) {
						showPopup(ae, vCard);
					}
				});

				JPanel topBar = new JPanel(new BorderLayout());
				topBar.setOpaque(true);
				topBar.setBackground(Color.white);
				topBar.add(Box.createHorizontalGlue());
				final Box panel = Box.createHorizontalBox();
				panel.setOpaque(true);
				panel.setBackground(Color.white);
				final JLabel profilePicNew = profilePic;
				panel.add(profilePicNew);
				if (vCard.getMemberList()!=null) {
					panel.add(Box.createHorizontalStrut(50));
				}
				final JLabel labelEditGroup = new JLabel(new ImageIcon(((new ImageIcon(WelcomeUtils.class.getResource(IMAGE_PATH
						+ "/editGroup.png"))).getImage()).getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);
				labelEditGroup.setCursor(new Cursor(Cursor.HAND_CURSOR));
				if (vCard.getMemberList() == null) {
					panel.add(Box.createHorizontalStrut(50));
					// panel.add(txtField);
				} else if (vCard.getMemberList()!=null && vCard.getUserId().split("_")[0].equals(Constants.loggedinuserInfo.username.split("@")[0])) {
					panel.add(Box.createHorizontalStrut(50));
					panel.add(labelEditGroup);
					panel.repaint();
					panel.revalidate();
					
					labelEditGroup.addMouseListener(new MouseAdapter() {

						@Override
						public void mouseClicked(MouseEvent e) {
							
							panel.remove(labelEditGroup);
							panel.repaint();
							panel.revalidate();
							profilePicNew.setText("");
							profilePicNew.repaint();
							profilePicNew.revalidate();
							final JTextField textField = new JTextField(20);

							textField.setBorder(BorderFactory.createEmptyBorder());
							textField.setFont(new Font(Font.decode("CentraleSansRndBold").getFontName(), Font.PLAIN, 20));
							textField.setOpaque(false);
							textField.setBackground(Color.white);
							textField.setForeground(Color.gray);
							panel.add(Box.createHorizontalStrut(50),1);
							panel.add(textField, 2);
							textField.requestFocus();
							panel.repaint();
							panel.revalidate();
							textField.addKeyListener(new KeyAdapter() {

								@Override
								public void keyPressed(KeyEvent ke) {

									if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
										final String text = ((JTextField) ke.getSource()).getText();
										if (text.equals("")) {
											profilePicNew.setText("<html><font style='color:#9CCD21;'>" + name + "</font><br/>" + title
													+ "</html>");
											panel.remove(textField);
											panel.repaint();
											panel.revalidate();
											panel.add(labelEditGroup);
											panel.repaint();
											panel.revalidate();
										} else {
											// JPanel loaderPanel =
											// Util.loaderPanel("Loading..");
											// UserWelcomeScreen.centerBox.setOpaque(true);
											// UserWelcomeScreen.centerBox.setBackground(Color.white);
											// UserWelcomeScreen.centerBox.removeAll();
											// UserWelcomeScreen.centerBox.add(loaderPanel,
											// BorderLayout.CENTER);
											// UserWelcomeScreen.centerBox.revalidate();
											// UserWelcomeScreen.centerBox.repaint();

											// SwingWorker<Void, Void> worker =
											// new SwingWorker<Void, Void>() {
											//
											// @Override
											// protected Void doInBackground()
											// throws Exception {
											String changeRoomNameId = XmppUtils.changeRoomId(vCard.getUserId(), text);
											if (!StringUtils.isEmpty(changeRoomNameId)) {
												List<RosterVCardBo> memberList = vCard.getMemberList();
												// RosterVCardBo bo =
												// XmppUtils.createGroupVCardBO(vCard.getUserId(),
												// text, memberList,true);
												rosterVcardAllUsersMap = null;
												XmppUtils.groupVCardBOList = null;
												SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

													@Override
													protected Void doInBackground() throws Exception {
													//	XmppUtils.getGroups();
														return null;
													}
													
												};
												worker.execute();
												panel.remove(textField);
												RosterVCardBo boo = new DBServiceResult()
														.getRosterUserDetails(vCard.getUserId().split("@")[0]);
												profilePicNew.setText(boo.getGroupSubject() == null ? text : boo.getGroupSubject());
												profilePicNew.setForeground(Color.decode("#9CCD21"));
												panel.repaint();
												panel.revalidate();
												panel.add(labelEditGroup);
												panel.repaint();
												panel.revalidate();
												try {
													openChatWindow(boo, false);
													UserWelcomeScreen.removeMsg(vCard.getUserId().split("@")[0]);
													SimpleDateFormat dateformat2= new SimpleDateFormat("d MMM hh:mm:ss a");
													Date date = new Date();
													UserWelcomeScreen.addMessages(boo, "Admin changed Subject", false,changeRoomNameId,dateformat2.format(date),false);
												} catch (IOException e) {
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}
											} else {

											}
											// return null;
											// }
											// };
											// worker.execute();
										}
									}
								}
							});

						}
					});
				}

				topBar.add(panel, BorderLayout.WEST);
				topBar.add(moreOptions, BorderLayout.EAST);
				topBar.add(Box.createHorizontalGlue());

				JPanel topBarPanel = new JPanel(new BorderLayout());
				topBarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				topBarPanel.add(topBar);
				topBarPanel.setBackground(null);
				topBarPanel.setPreferredSize(new Dimension((int) (Constants.PARENT.getWidth() * 0.30), 80));
				Box vBox = Box.createVerticalBox();
				vBox.add(topBarPanel, BorderLayout.NORTH);
				Constants.currentChatWindowUSERID = vCard.getUserId();
				if (vCard.getMemberList() != null) {
					// Constants.isGroupChat = true;
					
					vBox.add(UserChatGroup.getUserChat(vCard, UserWelcomeScreen.centerBox.getWidth()));
				} else {
					// Constants.isGroupChat = false;
				
					vBox.add(UserChat.getUserChat(vCard, UserWelcomeScreen.centerBox.getWidth()));
				}
				chatPanel = new JPanel();
				chatPanel.add(vBox);
				chatPanel.setOpaque(true);
				chatPanel.setBackground(Color.white);
				chatPanelMap.put(userId, chatPanel);
			}
			UserWelcomeScreen.centerBox.setOpaque(true);
			UserWelcomeScreen.centerBox.setBackground(null);
			UserWelcomeScreen.centerBox.removeAll();
			UserWelcomeScreen.centerBox.add(chatPanel);
			UserWelcomeScreen.centerBox.revalidate();
			UserWelcomeScreen.centerBox.repaint();
			// pack();
		} else {

		}
	}

	private static void showListUsersMenu(KeyEvent ae, List<RosterVCardBo> vCardList) throws XMPPException, IOException {
		JPopupMenu menu = new JPopupMenu();
		MenuScroller.setScrollerFor(menu, 5);
		JLabel profilePic = null;
		int count = 0;
		BufferedImage icon = null;
		Collections.sort(vCardList, new CustomComparator());
		for (final RosterVCardBo vCard : vCardList) {
			
			final String userId = vCard.getUserId();
			if (rosterVcardAllUsersMap.get(vCard.getUserId()) == null) {
				if (vCard.getMemberList()==null) {
					icon = Util.getProfileImg(userId);
					if (icon == null) {
						icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));
					}
				} else {
					if (vCard.groupHasPatient() == false) {
						icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"group_profile.png"));
					} else {
						icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"red_group_profile.png"));
					}

				}

				profilePic = Util.combine(icon, false, 60, 60);
				String menuItemName = "";
				if (vCard.getName() != null) {
					menuItemName = vCard.getName();
					
				} else if (!StringUtils.isEmpty(vCard.getGroupSubject())) {
					menuItemName = vCard.getGroupSubject();
				} else {
					menuItemName = vCard.getUserId().split("@")[0];
				}
				if(StringUtils.isEmpty(menuItemName)){
					menuItemName = vCard.getUserId().split("@")[0];
				}
				if (menuItemName.length() > 20)
					menuItemName = menuItemName.substring(0, 19) + "...";
				JMenuItem menuItem = new JMenuItem(menuItemName, profilePic.getIcon());
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (vCard.isGroup()) {
							try {
								// SwingWorker<Void, Void> worker = new
								// SwingWorker<Void, Void>(){
								//
								// @Override
								// protected Void doInBackground()
								// throws Exception {
								//System.out.println(vCard.getUserId());
								String userId = vCard.getUserId();
								if (!(userId.contains("@newconversation.imyourdoc.com"))) {
									userId = userId + "@newconversation.imyourdoc.com";
								}
								MultiUserChat muc = new MultiUserChat(XmppUtils.connection, userId);
								if (!muc.isJoined()) {
									muc.join(Constants.loggedinuserInfo.username.split("@")[0]);
								}
								Map<String,String> listMembers = XmppUtils.roomUserIds(userId);
								List<RosterVCardBo> members = new ArrayList<RosterVCardBo>();
								for(String uid:listMembers.keySet()){
									RosterVCardBo bo = XmppUtils.getVCardBo(uid);
									members.add(bo);
								}
								vCard.setMemberList(members);
								// return null;
								// }
								//
								// };
								// worker.execute();
								//XmppUtils.joinGroup(userId);
								WelcomeUtils.openChatWindow(vCard, true);
								//UserWelcomeScreen.addMessages(vCard, "Group Chat", false,"");
							} catch (Exception xe) {
								xe.printStackTrace();
							}
						} else {
							try {
								addUsersToPanel(vCard);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

					}
				});

				menuItem.setOpaque(false);
				menuItem.setBorderPainted(false);
				menuItem.setFocusPainted(false);
				menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
				menuItem.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
				menuItem.setBackground(Color.white);
				menuItem.setForeground(Color.decode("#9CCD21"));
				menuItem.setArmed(false);
				menuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

					}
				});
				menu.add(menuItem);
				count = count + 1;
				if (count != vCardList.size()) {
					menu.addSeparator();
				}
			}
		}

		ae.getComponent().requestFocus();
		Component b = (Component) ae.getSource();

		// Get the location of the point 'on the screen'
		Point p = b.getLocationOnScreen();
		menu.show(b, 0, 0);
		menu.setLocation(p.x, p.y + b.getHeight());
		menu.setOpaque(true);
		menu.setAutoscrolls(true);
		menu.setBackground(Color.white);
	}

	private static void showPopupTextField(KeyEvent ae, String searchText, String currentUserId) throws XMPPException, IOException {

		JPopupMenu menu = new JPopupMenu();
		MenuScroller.setScrollerFor(menu, 5);
		int count = 0;
		if (!StringUtils.isEmpty(searchText)) {
			List<RosterVCardBo> vCardList = XmppUtils.getAllVCards(searchText, false);
			JLabel profilePic = null;
			BufferedImage icon = null;
			Collections.sort(vCardList, new CustomComparator());
			for (final RosterVCardBo vCard : vCardList) {
				
				final String userId = vCard.getUserId();
				if (userId != currentUserId) {
					icon = Util.getProfileImg(userId);
					if (icon == null) {
						icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));
					}

					profilePic = Util.combine(icon, false, 60, 60);

					JMenuItem menuItem = new JMenuItem(vCard.getName() == null ? vCard.getUserId().split("@")[0] : vCard.getName(),
							profilePic.getIcon());
					menuItem.setOpaque(false);
					menuItem.setBorderPainted(false);
					menuItem.setFocusPainted(false);
					menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
					menuItem.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
					menuItem.setBackground(Color.white);
					menuItem.setForeground(Color.decode("#9CCD21"));
					menuItem.setArmed(false);
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

						}
					});
					menu.add(menuItem);
					count = count + 1;
					if (count != vCardList.size()) {
						menu.addSeparator();
					}
				}
			}
		}
		ae.getComponent().requestFocus();
		Component b = (Component) ae.getSource();

		// Get the location of the point 'on the screen'
		Point p = b.getLocationOnScreen();
		menu.show(b, 0, 0);
		menu.setLocation(p.x, p.y + b.getHeight());
		menu.setOpaque(true);
		menu.setAutoscrolls(true);
		menu.setPopupSize(100, 100);
		menu.setBackground(Color.white);
	}

	private static void showPopup(MouseEvent ae, final RosterVCardBo vcard) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem closeConversation = new JMenuItem("Close", new ImageIcon(WelcomeUtils.class.getResource(Constants.IMAGE_PATH
				+ "/"+"close_conversation.png")));
		// Get the event source
		closeConversation.setOpaque(true);
		closeConversation.setBorderPainted(false);
		closeConversation.setFocusPainted(false);
		closeConversation.setCursor(new Cursor(Cursor.HAND_CURSOR));
		closeConversation.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		closeConversation.setBackground(Color.decode("#9CCD21"));
		closeConversation.setForeground(Color.white.brighter());

		JMenuItem AddUser = new JMenuItem("Add User", new ImageIcon(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"add_chat.png")));
		// Get the event source
		AddUser.setOpaque(true);
		AddUser.setBorderPainted(false);
		AddUser.setFocusPainted(false);
		AddUser.setCursor(new Cursor(Cursor.HAND_CURSOR));
		AddUser.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		AddUser.setBackground(Color.decode("#9CCD21"));
		AddUser.setForeground(Color.white.brighter());

		JMenuItem deleteGroup = new JMenuItem("Delete Group", new ImageIcon(WelcomeUtils.class.getResource(Constants.IMAGE_PATH
				+ "/"+"delete_group.png")));
		// Get the event source
		deleteGroup.setOpaque(true);
		deleteGroup.setBorderPainted(false);
		deleteGroup.setFocusPainted(false);
		deleteGroup.setCursor(new Cursor(Cursor.HAND_CURSOR));
		deleteGroup.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		deleteGroup.setBackground(Color.decode("#9CCD21"));
		deleteGroup.setForeground(Color.white.brighter());

		JMenuItem leaveGroup = new JMenuItem("Leave Group", new ImageIcon(WelcomeUtils.class.getResource(Constants.IMAGE_PATH
				+ "/"+"delete_group.png")));
		// Get the event source
		leaveGroup.setOpaque(true);
		leaveGroup.setBorderPainted(false);
		leaveGroup.setFocusPainted(false);
		leaveGroup.setCursor(new Cursor(Cursor.HAND_CURSOR));
		leaveGroup.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		leaveGroup.setBackground(Color.decode("#9CCD21"));
		leaveGroup.setForeground(Color.white.brighter());

		JMenuItem groupCloseConversation = new JMenuItem("Close", new ImageIcon(WelcomeUtils.class.getResource(Constants.IMAGE_PATH
				+ "/"+"close_conversation.png")));
		// Get the event source
		groupCloseConversation.setOpaque(true);
		groupCloseConversation.setBorderPainted(false);
		groupCloseConversation.setFocusPainted(false);
		groupCloseConversation.setCursor(new Cursor(Cursor.HAND_CURSOR));
		groupCloseConversation.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		groupCloseConversation.setBackground(Color.decode("#9CCD21"));
		groupCloseConversation.setForeground(Color.white.brighter());

		JMenuItem groupMembers = new JMenuItem("Group Members", new ImageIcon(WelcomeUtils.class.getResource(Constants.IMAGE_PATH
				+ "/"+"profile_click.png")));
		// Get the event source
		groupMembers.setOpaque(true);
		groupMembers.setBorderPainted(false);
		groupMembers.setFocusPainted(false);
		groupMembers.setCursor(new Cursor(Cursor.HAND_CURSOR));
		groupMembers.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		groupMembers.setBackground(Color.decode("#9CCD21"));
		groupMembers.setForeground(Color.white.brighter());

		JMenuItem addMember = new JMenuItem("Add Member", new ImageIcon(WelcomeUtils.class.getResource(Constants.IMAGE_PATH
				+ "/"+"profile_click.png")));
		// Get the event source
		addMember.setOpaque(true);
		addMember.setBorderPainted(false);
		addMember.setFocusPainted(false);
		addMember.setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMember.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		addMember.setBackground(Color.decode("#9CCD21"));
		addMember.setForeground(Color.white.brighter());

		// groupMembers.addMouseListener(new MouseAdapter() {
		//
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// try {
		// showPopupMemberList(e, vcard.getMemberList());
		// } catch (XMPPException e1) {
		// e1.printStackTrace();
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// }
		// });
		//
		if (vcard.getMemberList()!=null) {
			if (vcard.getUserId().split("_")[0].equals(Constants.loggedinuserInfo.username.split("@")[0])) {
				menu.add(deleteGroup);
			} else {
				menu.add(leaveGroup);
			}
			menu.addSeparator();
			menu.add(groupMembers);
			menu.addSeparator();
			menu.add(closeConversation);
			// menu.addSeparator();
			// menu.add(groupMembers);
		} else {
			menu.add(AddUser);
			menu.addSeparator();
			menu.add(closeConversation);
		}
		groupMembers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String uid = vcard.getUserId();
				 if(!uid.contains("@newconversation.imyourdoc.com")){ 
					 uid = uid+"@newconversation.imyourdoc.com"; }
				 MultiUserChat muc = new MultiUserChat(XmppUtils.connection,uid);
				 List<RosterVCardBo> members = new DBServiceResult().getGroupDetails(uid.split("@")[0]);
				// XmppUtils.roomUserIds(XmppUtils.getMUCInstance(uid));
				// for (RosterVCardBo users : vcard.getMemberList()) {
				// // RosterVCardBo bo = XmppUtils.getVCardBo(users);
				// members.add(users);
				// }
				Members compose = new Members(Constants.mainFrame, members, "Group Members", vcard.getUserId());
				compose.setVisible(true);

			}
		});
		addMember.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String uid = vcard.getUserId();
				 if(!uid.contains("@newconversation.imyourdoc.com"))
				 {
					 uid =	 uid+"@newconversation.imyourdoc.com"; 
				}
				 MultiUserChat muc = new MultiUserChat(XmppUtils.connection,uid);
				 List<RosterVCardBo> members = new DBServiceResult().getGroupDetails(uid.split("@")[0]);
				// XmppUtils.roomUserIds(XmppUtils.getMUCInstance(uid));
				// for (RosterVCardBo users : vcard.getMemberList()) {
				// // RosterVCardBo bo = XmppUtils.getVCardBo(users);
				// members.add(users);
				// }
				Members compose = new Members(Constants.mainFrame, members, "Add Members", vcard.getUserId());
				compose.setVisible(true);

			}
		});
		AddUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("clicked..........");
				try {
					openChatWindowCompose(vcard.getUserId(), true);
				} catch (XMPPException e1) {
					e1.printStackTrace();
				}
			}
		});
		leaveGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println("Do in background:: for id:-->" + vcard.getUserId());
				boolean leaveGroup = XmppUtils.leaveGroup(vcard.getUserId(), Constants.loggedinuserInfo.username);
				if (leaveGroup) {
					rosterVcardAllUsersMap = null;
					XmppUtils.groupVCardBOList = null;
					UserChatGroup.removeUserChat(vcard, false);
					UserWelcomeScreen.removeMsg(vcard.getUserId());
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

						@Override
						protected Void doInBackground() throws Exception {
							//XmppUtils.getGroups();
							return null;
						}
						
					};
					worker.execute();

				}

			}
		});
		deleteGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int status = JOptionPane
						.showConfirmDialog(Constants.mainFrame, "<html><center>Do you really want to delete this group?</center></html>",
								"Delete Group", JOptionPane.YES_NO_OPTION);
				if (status == JOptionPane.YES_OPTION) {
					if (Constants.showConsole)
						System.out.println("Do in background:: for id:-->" + vcard.getUserId());
					boolean isDestroyed = XmppUtils.destroyGroup(vcard.getUserId());
					if (isDestroyed == true) {
						rosterVcardAllUsersMap = null;
						XmppUtils.groupVCardBOList = null;
						UserChatGroup.removeUserChat(vcard, false);
						UserWelcomeScreen.removeMsg(vcard.getUserId());
						UserWelcomeScreen.showHomeView();
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

							@Override
							protected Void doInBackground() throws Exception {
							//	XmppUtils.getGroups();
								return null;
							}
							
						};
						worker.execute();
					} else {
						JOptionPane.showMessageDialog(Constants.mainFrame,
								"There some problem in deleting group, please check internet connection!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		AddUser.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

			}
		});
		// Show the JPopupMenu via program

		// Parameter desc
		// ----------------
		// this - represents current frame
		// 0,0 is the co ordinate where the popup
		// is shown
		closeConversation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int status = JOptionPane.showConfirmDialog(null, "<html><center>Closing the conversation will remove "
						+ "it from your inbox,<br/>Are you sure you want to close?</center></html>", "Close Conversation",
						JOptionPane.YES_NO_OPTION);
				if (status == JOptionPane.YES_OPTION) {
					if (Constants.showConsole)
						System.out.println("Do in background:: for id:-->" + vcard.getUserId());
					if (vcard.isGroup() == false)
						UserChat.removeUserChat(vcard.getUserId(), true);
					else
						UserChatGroup.removeUserChat(vcard, true);
				}
			}
		});
		Component b = (Component) ae.getSource();

		// Get the location of the point 'on the screen'
		Point p = b.getLocationOnScreen();
		menu.show(b, 0, 0);
		menu.setLocation(p.x, p.y + b.getHeight());
		menu.setOpaque(true);
		menu.setBackground(Color.decode("#9CCD21"));
	}

	private static void showPopupMemberList(MouseEvent ae, List<RosterVCardBo> memberList) throws XMPPException, IOException {
		JPopupMenu menu = new JPopupMenu();
		menu.setLabel("Members");
		String members = "";
		int count = 0;
		for (RosterVCardBo card : memberList) {
			count = count + 1;

			// RosterVCardBo card = XmppUtils.rosterVCardBoMap.get(members);
			if (card != null) {
				members = card.getUserId();
				if (members.contains("@"))
					members = members.split("@")[0];
				BufferedImage icon = Util.getProfileImg(members);
				if (icon == null) {
					icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));
				}
				JLabel userPic = Util.combine(icon, false, 60, 60);
				final JMenuItem member = new JMenuItem(card.getName(), userPic.getIcon());
				// Get the event source
				member.setOpaque(false);
				member.setBorderPainted(false);
				member.setFocusPainted(false);
				member.setCursor(new Cursor(Cursor.HAND_CURSOR));
				member.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
				member.setBackground(Color.white);
				member.setForeground(Color.decode("#9CCD21"));
				member.setArmed(false);
				menu.add(member);
				if (count != memberList.size()) {
					menu.addSeparator();
				}

			} else {
				VCard vcard = new VCard();
				vcard.load(XmppUtils.connection, members);
				if (members.contains("@"))
					members = members.split("@")[0];
				BufferedImage icon = Util.getProfileImg(members);
				if (icon == null) {
					icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));
				}
				JLabel userPic = Util.combine(icon, false, 60, 60);
				final JMenuItem member = new JMenuItem(members, userPic.getIcon());
				// Get the event source
				member.setOpaque(false);
				member.setBorderPainted(false);
				member.setFocusPainted(false);
				member.setCursor(new Cursor(Cursor.HAND_CURSOR));
				member.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
				member.setBackground(Color.white);
				member.setForeground(Color.decode("#9CCD21"));
				member.setArmed(false);
				menu.add(member);
				if (count != memberList.size()) {
					menu.addSeparator();
				}
				UIManager.put("MenuItem.selectionBackground", Color.GREEN);
			}
		}
		Component b = (Component) ae.getSource();

		Point p = b.getLocationOnScreen();
		menu.show(b, 0, 0);
		menu.setLocation(p.x, p.y + b.getHeight());
		menu.setOpaque(true);
		menu.setBackground(Color.white);
		menu.setAutoscrolls(true);
	}

	public static JPanel chatBox() {
		JPanel chatBox = new JPanel();
		return chatBox;
	}

	public static JPanel welcomeBox() throws IOException {
		if (null == welcomeBox) {
			welcomeBox = new JPanel();
			final Box panel = Box.createVerticalBox();
			JPanel profileNamePanel = new JPanel();
			JPanel profilePicPanel = new JPanel();
			
			// new JLabel(new ImageIcon(((new
			// ImageIcon(IMAGE_PATH+"/welcome_image.png")).getImage()).getScaledInstance(400,200,
			// java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
			
			// from imuserlogin
			profile.setText("<html><center>Welcome!<br/>" + Constants.loggedinuserInfo.name + "</html>");
			profile.setForeground(Color.decode("#9CCD21"));
			profile.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 24));
			JLabel contactHereLabel = new JLabel();
			contactHereLabel.setText("<html>Want to compose a new message? there are two ways.</html>");
			contactHereLabel.setForeground(Color.gray);
			contactHereLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 15));
			// from imuserlogin
			// BufferedImage rounded = Util.makeRoundedCorner(icon, 100);
			// ResultSet rs = new
			// DBServiceResult().getLoginUserDetails(Constants.loggedinuserInfo.username.split("@")[0]);
			LoginUserBO loginUserBO = new DBServiceResult().getLoginUserDetails(Constants.loggedinuserInfo.username.split("@")[0]);
			if (loginUserBO != null) {
				byte[] image = loginUserBO.getProfileImage();
				InputStream in = new ByteArrayInputStream(image);
				BufferedImage bImageFromConvert = ImageIO.read(in);
				loginUserProfileIcon = bImageFromConvert;
			} else {
				loginUserProfileIcon = Util.getProfileImg(Constants.loggedinuserInfo.username);
			}
			if (loginUserProfileIcon == null || loginUserProfileIcon.getColorModel().hasAlpha()) {
				loginUserProfileIcon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));

			}
			loginUserProfilePic = Util.combine(loginUserProfileIcon, false, 200, 200);
			profilePicPanel.add(loginUserProfilePic);
			profileNamePanel.add(profile);
			profileNamePanel.setOpaque(true);
			profileNamePanel.setBackground(null);
			profilePicPanel.setOpaque(true);
			profilePicPanel.setBackground(null);
			profileNamePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
			panel.add(profilePicPanel, BorderLayout.CENTER);
			panel.add(profileNamePanel, BorderLayout.CENTER);
			
			
			Box welcomeBoxCenter = Box.createHorizontalBox();
			JLabel welcomeLabelLeft = new JLabel(new ImageIcon(WelcomeUtils.class.getResource(IMAGE_PATH+ "/left_graphic2.png")));
			JLabel welcomeLabelCenter = new JLabel(new ImageIcon(WelcomeUtils.class.getResource(IMAGE_PATH+ "/or_graphic2.png")));
			
			JLabel welcomeLabelRight =  new JLabel(new ImageIcon(WelcomeUtils.class.getResource(IMAGE_PATH+ "/right_graphic2.png")));
			
			welcomeLabelLeft.setText("<html> Click on the \"Compose new\" icon and <br/>type in the name of your contact.</html>");
			welcomeLabelLeft.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
			welcomeLabelLeft.setHorizontalTextPosition(JLabel.CENTER);
			welcomeLabelLeft.setVerticalTextPosition(JLabel.BOTTOM);
			welcomeLabelLeft.setForeground(Color.gray.darker());
			
			welcomeLabelRight.setText("<html>Select a person from one of your contact <br/> lists to start a conversation with them.</html>");
			welcomeLabelRight.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
			welcomeLabelRight.setHorizontalTextPosition(JLabel.CENTER);
			welcomeLabelRight.setVerticalTextPosition(JLabel.BOTTOM);
			welcomeLabelRight.setForeground(Color.gray.darker());
			
			welcomeBoxCenter.add(Box.createHorizontalStrut(5));
			welcomeBoxCenter.add(welcomeLabelLeft,BorderLayout.WEST);
			welcomeBoxCenter.add(welcomeLabelCenter,BorderLayout.CENTER);
			welcomeBoxCenter.add(welcomeLabelRight,BorderLayout.EAST);
			
			
			
			
			
			JPanel welcomePanel = new JPanel();
			welcomePanel.add(welcomeBoxCenter, BorderLayout.CENTER);
			welcomePanel.setOpaque(true);
			welcomePanel.setBackground(null);

			JPanel contactherePanel = new JPanel();
			contactherePanel.add(contactHereLabel, BorderLayout.CENTER);
			contactherePanel.setOpaque(true);
			contactherePanel.setBackground(null);
			// panel.add(Box.createVerticalStrut((int)
			// (Constants.SCREEN_SIZE.getHeight() * 0.065)));
			panel.add(contactherePanel, BorderLayout.CENTER);
			panel.add(welcomeBoxCenter, BorderLayout.CENTER);
			// panel.add(Box.createVerticalStrut((int)
			// (Constants.SCREEN_SIZE.getHeight() * 0.065)));
			
			panel.setOpaque(true);
			panel.setBackground(Color.white);
			// welcomeBox.setPreferredSize(new Dimension((int)
			// (Constants.SCREEN_SIZE.getWidth() * 0.60), (int)
			// (Constants.SCREEN_SIZE
			// .getHeight() * 0.90)));
			// panel.setPreferredSize(new Dimension((int)
			// (Constants.SCREEN_SIZE.getWidth()*0.60),
			// (int)(Constants.SCREEN_SIZE.getHeight() * 0.80)));
			// profilePic.setVerticalAlignment(SwingConstants.CENTER);
			// profilePic.setHorizontalAlignment(SwingConstants.CENTER);
			welcomeBox.add(panel, BorderLayout.CENTER);

			welcomeBox.setOpaque(true);
			welcomeBox.setBackground(Color.white);
		}
		return welcomeBox;
	}
	public static void downloadRosterList(final String groupName, final Box vBox, final String searchTxt) throws IOException {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

			@Override
			protected Void doInBackground() throws Exception {
				final Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
					try {
						vBox.removeAll();
						JPanel loaderPanel = Util.loaderPanel("Loading..");
						vBox.add(loaderPanel);
						vBox.revalidate();
						vBox.repaint();
						
						List<RosterVCardBo> vCardList = XmppUtils.getVCardList(groupName, searchTxt);
						vBox.removeAll();
						getRosterList(vBox, vCardList, groupName);

						vBox.revalidate();
						vBox.repaint();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				});
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
		worker.execute();
	}
	public static void updateList() throws IOException {
		ExecutorService backgroundPool=Executors.newFixedThreadPool(4);
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

			@Override
			protected Void doInBackground() throws Exception {
				
					
				final Thread thread4 = new Thread(new Runnable() {
					@Override
					public void run() {
							
							Thread thread = new Thread(new Runnable() {
								@Override
								public void run() {
									synchronized (Login.activeUserIds) {
										for(final ActiveUserBo activeUserBo:Login.activeUserIds)
										{
											Calendar cal = Calendar.getInstance();
											cal.add(Calendar.DAY_OF_MONTH, -2);
											SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
											String date = format.format(cal.getTime());
											Thread thread2 = new ChatStateXML(activeUserBo.getUserId(),date);
											thread2.start();
//											try {
//												Thread.sleep(1000);
//											} catch (InterruptedException e) {
//												// TODO Auto-generated catch block
//												e.printStackTrace();
//											}
										}
									}
								}
							});
							thread.start();
				}
				});
				thread4.start();
				try {
					thread4.join();
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}	
				final Thread thread2 = new Thread(new Runnable() {
					
					@Override
					 public void run() {
						try {
							List<GroupBo> list = Login.ActiveGroupIds;
							System.out.println(list.size());
								synchronized (list) {
									for(GroupBo groupbo:list){
										XmppUtils.getGroups(groupbo);
									}
//									try {
//										Thread.sleep(1000);
//									} catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
							}
							
						} catch (XMPPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				thread2.start();
				try {
					thread2.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
				/*final Thread thread5 = new Thread(new Runnable() {
					
					@Override
					 public void run() {
						try {
							
							if(!StringUtils.isEmpty(Constants.notifiedGroup)){
								XmppUtils.getNotifiedGroup(Constants.notifiedGroup);
							}
							
						} catch (XMPPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				thread5.start();
				try {
					thread5.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	*/
				final Thread thread3 = new Thread(new Runnable() {
					@Override
					public void run() {
						addOldMessagesToPanel();
					}
				});
				thread3.start();
				try {
					thread3.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				final TimerTask task = new TimerTask() {
					@Override
					public void run() {
						// task to run goes here
						
						if(!thread2.isAlive() && !thread4.isAlive() && !thread4.isAlive() ){
							System.out.println(thread2.isAlive());
							UserWelcomeScreen.messageBox.remove(UserWelcomeScreen.loaderPanel);
							cancel();
						}
					}
				};
				Timer timer = new Timer();
				long delay = 300000;
				long intevalPeriod = 1 * 100000000;
				// schedules the task to be run in an interval
				timer.scheduleAtFixedRate(task, delay, intevalPeriod);
				
				/*Thread thread5 = new Thread(new Runnable() {
					@Override
					public void run() {
							
							Thread thread = new Thread(new Runnable() {
								@Override
								public void run() {
									synchronized (Login.activeUserIds) {
									for(ActiveUserBo bo:Login.activeUserIds){
										threadCount = threadCount+1;
										List<MessageSyncBo> list = new DBServiceResult().getSyncMessagesDB(bo.getUserId());
										   if (!list.isEmpty()) {
												Collections.sort(list, new CustomComparatorMessage());
												
												Iterator<MessageSyncBo> itr = list.iterator();
												while(itr.hasNext()){
													String jid="";
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
															UserChat userChat = UserChat.getUserChat(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));//new UserChat(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));
															try {
																XmppUtils.generateChartPanels(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));
																userChat.addMsgInBoxChatState(msgSyncbo,msgSyncbo.getChatDate());
															} catch (JSONException e) {
																e.printStackTrace();
															}
													   }
													   else if (Constants.REMOVE_CHAT_MSG.equals(content)) {
																new SendInvitationJSON().ReportOnCloseConversation(org.jivesoftware.smack.util.StringUtils.parseBareAddress(from));
															//UserChat.removeUserChat(from.split("@")[0], false);
														} 
												}
										   }
									}
									if(threadCount == Login.activeUserIds.size())
									{
										Constants.threadsFinish = true;
									}
								}
								}
							});
							thread.start();
							
				}
				});
				thread5.start();
				try {
					thread5.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					e.printStackTrace();
				}	*/
				
				
				return null;
			}
			@Override
			protected void done() {
				System.out.println("done all tasks");
				//UserWelcomeScreen.messageBox.remove(UserWelcomeScreen.loaderPanel);
			}
		};
		worker.execute();
	
			System.out.println("worker state........."+worker.getState());
		/*Thread thread5 = new Thread(new Runnable() {
				
				@Override
				public void run() {
					Map<String,List<MessageSyncBo>> msgMap = RetreiveMessageXML.mapList;
					for(Entry<String, List<MessageSyncBo>> entry : msgMap.entrySet()) {
						  String jid = entry.getKey();
						  List<MessageSyncBo> msgList = entry.getValue();
						  Collections.sort(msgList, new CustomComparatorMessage());
						  for (MessageSyncBo msgSyncbo : msgList) {
							  RosterVCardBo vcardBo = null;
								 String content = msgSyncbo.getContent();
								 String from = msgSyncbo.getMessageFrom();
								 String start = msgSyncbo.getChatDate();
								jid = msgSyncbo.getWith();
								 if(jid!=null)
									   vcardBo = XmppUtils.getVCardBo(jid.split("@")[0].toLowerCase());
								   if(!Constants.REMOVE_CHAT_MSG.equals(content)){
										UserChat userChat = UserChat.getUserChat(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));//new UserChat(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));
										try {
											XmppUtils.generateChartPanels(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));
											userChat.addMsgInBoxChatState(msgSyncbo,start);
										} catch (JSONException e) {
											e.printStackTrace();
										}
								   }
								   else if (Constants.REMOVE_CHAT_MSG.equals(content)) {
											new SendInvitationJSON().ReportOnCloseConversation(org.jivesoftware.smack.util.StringUtils.parseBareAddress(from));
										UserChat.removeUserChat(from.split("@")[0], false);
									} 
						  }
						}
				}
			});
			backgroundPool.submit(thread5);*/
		/*Thread thread5 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(ActiveUserBo bo:Login.activeUserIds){
				List<MessageSyncBo> list = new DBServiceResult().getSyncMessagesDB(bo.getUserId());
					for(MessageSyncBo syncBo:list){
						String jid = bo.getUserId()==null?syncBo.getMessageTo():bo.getUserId();
					  RosterVCardBo vcardBo = null;
					  if(jid!=null)
						   vcardBo = XmppUtils.getVCardBo(jid.split("@")[0]);
							UserChat userChat = UserChat.getUserChat(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));//new UserChat(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));
							try {
								XmppUtils.generateChartPanels(vcardBo, (int)(Constants.SCREEN_SIZE.getWidth()*0.60));
								userChat.addMsgInBoxChatState(syncBo,syncBo.getChatDate());
							} catch (JSONException e) {
								e.printStackTrace();
							}
					}
				}
			}
		});
		backgroundPool.submit(thread5);*/
	}
	
	public static void updateRosterList(final String groupName, final Box vBox, final String searchTxt) throws IOException {
				final Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
					try {
						vBox.removeAll();
						JPanel loaderPanel = Util.loaderPanel("Loading..");
						vBox.add(loaderPanel);
						vBox.revalidate();
						vBox.repaint();
						
							List<RosterVCardBo> vCardList = XmppUtils.getVCardList(groupName, searchTxt);
						vBox.removeAll();
						getRosterList(vBox, vCardList, groupName);

						vBox.revalidate();
						vBox.repaint();
						
					} catch (IOException e) {
						e.printStackTrace();
					} 			}
				});
				thread.start();
	
	}
	private static void addOldMessagesToPanel(){
		List<Message> oldMessages = XmppUtils.oldMessages;
		if(oldMessages!=null){
			if(!oldMessages.isEmpty()){
				ListIterator<Message> itr = oldMessages.listIterator();
				while(itr.hasNext()){
					final Message message = itr.next();
					message.setType(Type.chat);
					Date date=null;
					SimpleDateFormat format = new SimpleDateFormat("d MMM hh:mm:ss a");
					final DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
					if (delay != null) {
						date = delay.getStamp();
					} else {
						date = new Date();
					}
					String body = message.getBody();
					String subject = message.getSubject();
					final String fromName = message.getFrom();
					final String from = fromName.split("@")[0];
					RosterVCardBo vCardBo = new DBServiceResult().getRosterUserDetails(from.toLowerCase());
							
						if(vCardBo ==null){
							vCardBo = XmppUtils.getVCardBo(fromName.split("/")[0].toLowerCase());
					}
						vCardBo.setIsGroup(false);
						vCardBo.setGroupHasPatient(false);
					try {
						String content = "";
						if (XmppUtils.isJSONValid(body)) {
							JSONObject obj = new JSONObject(body);
							if (subject == null)
								content = obj.getString("content");
							else
								content = "File Recieved";
							
							if (!content.equals(Constants.REMOVE_CHAT_MSG)) {
								try {
									
									//WelcomeUtils.openChatWindow(vCardBo, false);
									UserWelcomeScreen.addMessages(vCardBo, content, true,message.getPacketID(),format.format(date),false);
									//generateChartPanels(vCardBo, (int)( Constants.SCREEN_SIZE.getWidth()*0.60));
									UserChat userChat = UserChat.getUserChat(vCardBo, (int)( Constants.SCREEN_SIZE.getWidth()*0.60));
									userChat.addMsgInBox(message,false,null);
									SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

										@Override
										protected Void doInBackground()
												throws Exception {
											new SendInvitationJSON().updateMessageReceivers(message.getPacketID(), fromName, Constants.loggedinuserInfo.username);
											return null;
										}
										
									};
									worker.execute();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			
							
							} 
						} 
					
					
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				}
			}
		}
	}
	
//	public static void updateListGroup(final String groupName, final Box vBox, final String searchTxt) throws IOException {
//		SwingWorker<Void, Void> worker  = new SwingWorker<Void, Void>(){
//
//			@Override
//			protected Void doInBackground() throws Exception {
//				try {
//					vBox.removeAll();
//					JPanel loaderPanel = Util.loaderPanel("Loading..");
//					vBox.add(loaderPanel);
//					vBox.revalidate();
//					vBox.repaint();
//
//					List<RosterVCardBo> vCardList = XmppUtils.getVCardList(groupName, searchTxt);
//					vBox.removeAll();
//					getRosterList(vBox, vCardList, groupName);
//
//					vBox.revalidate();
//					vBox.repaint();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				return null;
//			}
//			
//		};
//		worker.execute();
////		Runnable runnable = new Runnable() {
////			@Override
////			public void run() {
////				
////			}
////		};
////		Thread t = new Thread(runnable);
////		t.setPriority(1);
////		t.start();
//	}

	private static boolean isAdjusting(JComboBox cbInput) {
		if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
			return (Boolean) cbInput.getClientProperty("is_adjusting");
		}
		return false;
	}

	private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
		cbInput.putClientProperty("is_adjusting", adjusting);
	}

	public static void fetchPractiseTypeList(final JTextField txtInput, final List<RosterVCardBo> list) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		setAdjusting(cbInput, false);
		for (RosterVCardBo item : list) {
			if (item.isGroup()) {
				model.addElement(item.getUserId().split("@")[0]);
			} else {
				model.addElement(item.getName());
			}

		}
		cbInput.setSelectedItem(null);
		cbInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isAdjusting(cbInput)) {
					if (cbInput.getSelectedItem() != null) {
						txtInput.setText(cbInput.getSelectedItem().toString());
					}
				}
			}
		});

		txtInput.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				setAdjusting(cbInput, true);
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					if (cbInput.isPopupVisible()) {
						e.setKeyCode(KeyEvent.VK_ENTER);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					e.setSource(cbInput);
					cbInput.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						txtInput.setText(cbInput.getSelectedItem().toString());
						cbInput.setPopupVisible(false);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					cbInput.setPopupVisible(false);
				}
				setAdjusting(cbInput, false);
			}
		});
		txtInput.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateList();
			}

			public void removeUpdate(DocumentEvent e) {
				updateList();
			}

			public void changedUpdate(DocumentEvent e) {
				updateList();
			}

			private void updateList() {
				setAdjusting(cbInput, true);
				model.removeAllElements();
				String input = txtInput.getText();
				if (!input.isEmpty()) {
					for (RosterVCardBo item : list) {
						if (item.isGroup()) {
							if (item.getUserId().toLowerCase().startsWith(input.toLowerCase())) {
								model.addElement(item.getUserId());
							}
						} else {
							if (item.getName().toLowerCase().startsWith(input.toLowerCase())) {
								model.addElement(item.getName());
							}
						}

					}
				}
				cbInput.setPopupVisible(model.getSize() > 0);
				setAdjusting(cbInput, false);
			}
		});
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}

	private static class MyScrollBarUI extends BasicScrollBarUI {
		private static final int SCROLL_BAR_ALPHA_ROLLOVER = 200;
		private static final int SCROLL_BAR_ALPHA = 150;
		private static final int THUMB_BORDER_SIZE = 0;
		private static final int THUMB_SIZE = 8;
		private static final Color THUMB_COLOR = Color.GRAY;

		@Override
		protected JButton createDecreaseButton(int orientation) {
			return new MyScrollBarButton();
		}

		@Override
		protected JButton createIncreaseButton(int orientation) {
			return new MyScrollBarButton();
		}

		@Override
		protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
			c.setBackground(Color.white);
			Dimension zeroDim = new Dimension(8, 8);
			c.setPreferredSize(zeroDim);
			c.setMinimumSize(zeroDim);
			c.setMaximumSize(zeroDim);
		}

		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
			int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
			int orientation = scrollbar.getOrientation();
			int arc = THUMB_SIZE;
			int x = thumbBounds.x + THUMB_BORDER_SIZE;
			int y = thumbBounds.y + THUMB_BORDER_SIZE;

			int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width - (THUMB_BORDER_SIZE * 2);
			width = Math.max(width, THUMB_SIZE);

			int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height - (THUMB_BORDER_SIZE * 2) : THUMB_SIZE;
			height = Math.max(height, THUMB_SIZE);

			Graphics2D graphics2D = (Graphics2D) g.create();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
			graphics2D.fillRoundRect(x, y, width, height, arc, arc);
			graphics2D.setBackground(Color.white);
			graphics2D.dispose();
		}
	}

	private static class MyScrollBarButton extends JButton {
		private static final long serialVersionUID = 1L;

		private MyScrollBarButton() {
			setOpaque(true);
			setFocusable(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setBorder(null);
			setBackground(Color.white);
			Dimension zeroDim = new Dimension(0, 0);
			setPreferredSize(zeroDim);
			setMinimumSize(zeroDim);
			setMaximumSize(zeroDim);
		}
	}
}

package com.im.patientscreens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.packet.VCard;
import org.json.JSONException;
import org.json.JSONObject;

import com.im.bo.ActiveUserBo;
import com.im.bo.MessagePanelBO;
import com.im.bo.MessageSyncBo;
import com.im.bo.RosterVCardBo;
import com.im.chat.UserChat;
import com.im.common.Footer;
import com.im.common.LightScrollPane;
import com.im.common.PinDialog;
import com.im.db.DBServiceInsert;
import com.im.db.DBServiceResult;
import com.im.db.DBServiceUpdate;
import com.im.json.AppCheckVersionJSON;
import com.im.login.FeedBack;
import com.im.login.HideToSystemTray;
import com.im.login.Login;
import com.im.user.SearchUser;
import com.im.utils.ChatStateXML;
import com.im.utils.Constants;
import com.im.utils.PendingRequestThread;
import com.im.utils.PushNotificationThread;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;
import com.im.utils.XmppUtils;

public class UserWelcomeScreen extends JFrame {
	private static final long serialVersionUID = 1L;
	public static List<Map<String, Object>> rightBoxTab;
	private javax.swing.Timer timer;
	private Box rightBox;
	private Box providerlist;
	private Box stafflist;
	private Box patientlist;
	private static int elapsedSeconds;
	private Box hbox;
	private JLabel phyHome;
	private static BufferedImage icon;
	private static JLabel profilePicAddMessages;
	private JLabel messages;
	private JLabel myContacts;
	private JLabel accountSettings;
	private JLabel feedBack;
	private JLabel info;
	private JPanel topProfilePanel;
	public static JLabel profilePic;
	public static Box messageBox;
	private JLabel strip;
	private static JPanel messagePanel;
	private static Box messageList;
	public static Map<String, MessagePanelBO> messageListMap;
	private JPanel messageIconPanel;
	private JLabel messageLabel;
	private Box searchBoxPanel;
	private Box searchPanel;
	private JLabel synch;
	private JLabel compose_message;
	private JLabel searchLabel;
	public static JPanel centerBox;
	private JLabel logoicon;
	private Box secureConnectedPanel;
	static JFrame parent;
	volatile static int msgcount = 0;
	public static JPanel loaderPanel = Util.loaderPanel("Syncing");;
	private static List<String> recievedMessagePacketIds = new ArrayList<String>();

	public UserWelcomeScreen() throws IOException {
		openWelcomeScreen();
		Constants.PARENT = this;
		Constants.mainFrame = this;
		parent = this;
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				CheckWindowStatus();
				// refreshRightBox();
				repaint();
				revalidate();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();

		if (Constants.loggedinuserInfo.login_token == null) {
			t.stop();
		}
		if (Constants.loggedinuserInfo.login_token != null) {
			Timer timerPush = new java.util.Timer(); // At this line a new
														// Thread
			timerPush.schedule(new PushNotificationThread(Constants.loggedinuserInfo.name, this), 0, 1000);
			Timer timerPending = new java.util.Timer(); // At this line a new
														// Thread
			timerPending.schedule(new PendingRequestThread(Constants.loggedinuserInfo.name, this), 0, 1000);
			// delay
		} // in
			// milliseconds
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setExtendedState(JFrame.HIDE_ON_CLOSE);
				if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
					setVisible(false);
				}
			}

			public void windowDeiconified(WindowEvent paramWindowEvent) {
				// TODO Auto-generated method stub
				setExtendedState(JFrame.MAXIMIZED_BOTH);
				if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
					setVisible(true);
				}
			}

		});

		// delay in milliseconds

	}

	public void openWelcomeScreen() throws IOException {
		secureConnectedPanel = Box.createHorizontalBox();
		searchPanel = Box.createHorizontalBox();
		searchBoxPanel = Box.createHorizontalBox();
		strip = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "strip.png"))).getImage()).getScaledInstance(2, 40,
						java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);
		searchLabel = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "search.png"))).getImage()).getScaledInstance(20, 20,
						java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
		searchLabel.setOpaque(true);
		searchLabel.setBackground(null);
		compose_message = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "composer_icon.png"))).getImage()).getScaledInstance(
						25, 25, java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		searchBoxPanel.setBorder(new TextBubbleBorder(Color.lightGray, 2, 2, 0));
		searchBoxPanel.add(Box.createHorizontalStrut(10));
		searchBoxPanel.add(getSearchMsgField("Search Messages"));
		searchBoxPanel.setOpaque(true);
		searchBoxPanel.setBackground(Color.white);
		searchBoxPanel.add(Box.createHorizontalStrut(10));
		searchBoxPanel.add(searchLabel);
		searchPanel.add(Box.createHorizontalStrut(10));
		searchPanel.add(searchBoxPanel);
		searchPanel.setOpaque(true);
		searchPanel.setBackground(Color.decode("#F2F1EA"));
		searchPanel.add(Box.createHorizontalStrut(15));
		searchPanel.add(compose_message);
		compose_message.setCursor(new Cursor(Cursor.HAND_CURSOR));
		compose_message.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {
				// WelcomeUtils.openChatWindow(null);
				// ComposeMessage compose = new ComposeMessage(parent, "All",
				// compose_message);
				// compose.setVisible(true);
				try {
					WelcomeUtils.openChatWindowCompose("", true);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		searchPanel.add(Box.createHorizontalStrut(10));
		searchPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.22), 60));
		searchPanel.setMaximumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.22), 60));
		searchBoxPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.22), 40));
		searchBoxPanel.setMaximumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.22), 40));
		searchBoxPanel.setBackground(Color.white);
		messageList = Box.createVerticalBox();
		messagePanel = new JPanel();
		messagePanel.setOpaque(true);
		messagePanel.setBackground(Color.white);
		//if(!Constants.threadsFinish)
			
		messagePanel.add(messageList, BorderLayout.CENTER);
		LightScrollPane sc = new LightScrollPane(messagePanel);
		// else Ui for list of messages.
		messageIconPanel = new JPanel();
		messageIconPanel.setOpaque(true);
		messageIconPanel.setBackground(Color.decode("#9CCD21"));
		messageBox = Box.createVerticalBox();
		centerBox = new JPanel(new BorderLayout());// Box.createHorizontalBox();
		centerBox.setOpaque(true);
		centerBox.setBackground(Color.white);
		topProfilePanel = new JPanel();
		messageLabel = new JLabel(new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "messages.png")));
		messageLabel.setOpaque(true);
		messageLabel.setBackground(Color.decode("#9CCD21"));
		messageIconPanel.add(messageLabel);
		messageIconPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.22), 60));
		messageIconPanel.setMaximumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.22), 60));
		messageBox.add(messageIconPanel);
		messageBox.add(searchPanel);
		messageBox.add(loaderPanel);
		messageBox.add(sc);
		messageBox.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
		// rightBox.add(messageIconPanel);
		// rightBox.add(searchPanel);
		// rightBox.add(messagePanel);
		logoicon = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "logo_new.png")).getImage())).getScaledInstance(45, 45,
						java.awt.Image.SCALE_FAST)), JLabel.CENTER);
		hbox = Box.createHorizontalBox();
		hbox.setOpaque(true);
		hbox.setBackground(Color.decode("#535456"));
		hbox.add(Box.createHorizontalStrut(10));
		hbox.add(logoicon);
		hbox.add(Box.createHorizontalStrut(20));
		phyHome = new JLabel();
		hbox.add(getLabel(phyHome, "Home", "home.png"));
		hbox.add(Box.createHorizontalStrut(30));
		accountSettings = new JLabel();
		hbox.add(getLabel(accountSettings, "Account Setting", "account_setting.png"));

		hbox.add(Box.createHorizontalStrut(30));
		feedBack = new JLabel();
		hbox.add(getLabel(feedBack, "Feedback", "feedback.png"));
		hbox.add(Box.createHorizontalStrut(30));
		info = new JLabel();
		hbox.add(getLabel(info, "Info", "info.png"));
		hbox.add(Box.createHorizontalStrut(30));
		centerBox.add(WelcomeUtils.welcomeBox(), BorderLayout.CENTER);
		// centerHbox.add(messageBox,FlowLayout.LEFT);
		// centerHbox.add(centerBox,FlowLayout.CENTER);
		// centerHbox.add(rightBox,FlowLayout.RIGHT);
		BufferedImage icon = Util.getProfileImg(Constants.loggedinuserInfo.username);
		if (icon == null) {
			icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "default_pp.png"));

		}
		// BufferedImage rounded = Util.makeRoundedCorner(icon, 100);
		profilePic = Util.combine(icon, true, 60, 60);
		topProfilePanel.setOpaque(true);
		topProfilePanel.setBackground(null);
		topProfilePanel.add(profilePic);
		topProfilePanel.add(Box.createHorizontalStrut(20));
		topProfilePanel.add(strip);
		topProfilePanel.add(Box.createHorizontalStrut(10));
		synch = new JLabel();
		topProfilePanel.add(getLabel(synch, "Sync", "sync.png"));
		topProfilePanel.setPreferredSize(new Dimension(200, 70));
		hbox.setPreferredSize(new Dimension(getWidth(), 70));
		hbox.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.30)));
		hbox.add(topProfilePanel);
		setLayout(new BorderLayout());
		setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.98), (int) (Constants.SCREEN_SIZE.getHeight() * 0.95)));
		secureConnectedPanel.setBackground(null);
		secureConnectedPanel.add(new Footer().commonLowerBox(), BorderLayout.CENTER);
		secureConnectedPanel.add(Box.createVerticalStrut(50));
		add(hbox, BorderLayout.NORTH);
		add(centerBox, BorderLayout.CENTER);
		add(messageBox, BorderLayout.WEST);
		add(rightBox(), BorderLayout.EAST);
		add(secureConnectedPanel, BorderLayout.SOUTH);
		getContentPane().setBackground(Color.white);
		// setMinimumSize(new Dimension(800,500));
		int x = (int) ((Constants.SCREEN_SIZE.getWidth()) / 10);
		int y = (int) ((Constants.SCREEN_SIZE.getHeight()) / 10);
		setBounds(x, y, Constants.SCREEN_SIZE.width / 2, Constants.SCREEN_SIZE.height / 2);
		setTitle("IM YOUR DOC- Beta");
		setIconImage((new ImageIcon(getClass().getResource("/images/logo_new.png"))).getImage());
		WelcomeUtils.updateList();
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setVisible(true);

	}

	public Box rightBox() throws IOException {
		if (rightBox == null)
			rightBox = Box.createVerticalBox();
		providerlist = Box.createVerticalBox();// new JPanel();
		stafflist = Box.createVerticalBox();// new JPanel();
		patientlist = Box.createVerticalBox();// new JPanel();

		Box providerListBox = getSearcheableBox("Search Providers", providerlist, "physician");
		Box staffListBox = getSearcheableBox("Search Staff", stafflist, "staff");

		WelcomeUtils.downloadRosterList("physician", providerlist, "");
		WelcomeUtils.downloadRosterList("staff", stafflist, "");

		final JButton providersTab = rightBoxTab("Providers", "provider_on.png", "provider_over.png", true, providerListBox);
		final JButton staffTab = rightBoxTab("Staff", "staff_on.png", "staff_over.png", false, staffListBox);

		JPanel tabsPanel = new JPanel();
		tabsPanel.setOpaque(true);
		tabsPanel.setBackground(Color.decode("#9CCD21"));
		tabsPanel.setPreferredSize(new Dimension((int) (getWidth() * 0.22), 60));
		tabsPanel.setMaximumSize(new Dimension((int) (getWidth() * 0.22), 60));
		tabsPanel.add(providersTab);
		tabsPanel.add(staffTab);

		Box patientListBox = null;
		if (!Constants.loggedinuserInfo.isPatient) {
			patientListBox = getSearcheableBox("Search Patient", patientlist, "patient");
			WelcomeUtils.downloadRosterList("patient", patientlist, "");
			JButton patientsTab = rightBoxTab("Patients", "patient_on.png", "patient_over.png", false, patientListBox);
			tabsPanel.add(patientsTab);
		}

		rightBox.setPreferredSize(new Dimension((int) (getWidth() * 0.22), 40));
		rightBox.setMaximumSize(new Dimension((int) (getWidth() * 0.22), 40));
		rightBox.setBackground(Color.white);
		rightBox.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
		rightBox.add(tabsPanel);
		rightBox.add(providerListBox, 1);
		rightBox.add(staffListBox, 1);
		providerListBox.setVisible(true);
		staffListBox.setVisible(false);

		if (!Constants.loggedinuserInfo.isPatient) {
			rightBox.add(patientListBox, 1);
			patientListBox.setVisible(false);
		}

		return rightBox;
	}

	private Box getSearcheableBox(final String placeholder, Box list, final String sideBtnClick) {
		final Box box = Box.createVerticalBox();
		JLabel searchLabel = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "search.png"))).getImage()).getScaledInstance(20, 20,
						java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
		JLabel add_user = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "add_user.png"))).getImage()).getScaledInstance(25, 25,
						java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		add_user.setCursor(new Cursor(Cursor.HAND_CURSOR));
		if (null != sideBtnClick) {
			add_user.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						SearchUser childFrame = new SearchUser(parent, sideBtnClick);
						if (!childFrame.isVisible())
							childFrame.setVisible(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		JTextField txtField = getSearchField(placeholder);
		Box searchFieldBox = Box.createHorizontalBox();
		searchFieldBox.setBorder(new TextBubbleBorder(Color.lightGray, 2, 2, 0));
		searchFieldBox.setOpaque(true);
		searchFieldBox.setBackground(Color.white);
		searchFieldBox.setPreferredSize(new Dimension((int) (getWidth() * 0.22), 40));
		searchFieldBox.setMaximumSize(new Dimension((int) (getWidth() * 0.22), 40));
		searchFieldBox.add(Box.createHorizontalStrut(10));
		searchFieldBox.add(txtField);
		searchFieldBox.add(Box.createHorizontalStrut(10));
		searchFieldBox.add(searchLabel);

		Box searchBox = Box.createHorizontalBox();
		searchBox.add(Box.createHorizontalStrut(10));
		searchBox.add(searchFieldBox);
		searchBox.setOpaque(true);
		searchBox.setBackground(Color.decode("#F2F1EA"));
		searchBox.add(Box.createHorizontalStrut(15));
		if (Constants.loggedinuserInfo.isProvider && !placeholder.equalsIgnoreCase("Search Patient"))
			searchBox.add(add_user);
		else if (Constants.loggedinuserInfo.isPatient || Constants.loggedinuserInfo.isStaff) {
			searchBox.add(add_user);
		}
		searchBox.add(Box.createHorizontalStrut(10));
		searchBox.setPreferredSize(new Dimension((int) (getWidth() * 0.22), 60));
		searchBox.setMaximumSize(new Dimension((int) (getWidth() * 0.22), 60));
		// searchPanel.add(Box.createHorizontalStrut(10));

		txtField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				JTextField textField = (JTextField) e.getSource();
				String text = textField.getText();
				try {
					// if (null != text && !"".equals(text)){
					if (placeholder.contains("Providers")) {
						WelcomeUtils.updateRosterList("physician", providerlist, text);
					} else if (placeholder.contains("Patient")) {
						WelcomeUtils.updateRosterList("patient", patientlist, text);
					} else if (placeholder.contains("Staff")) {
						WelcomeUtils.updateRosterList("staff", stafflist, text);
					}
					// }
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// textField.setText(text.toUpperCase());
			}

			public void keyTyped(KeyEvent e) {

			}

			public void keyPressed(KeyEvent e) {

			}
		});

		box.add(searchBox);
		// list.setLayout(new GridLayout(0, 1, 1, 1));
		// Box panelList = Box.createVerticalBox();//new JPanel(new
		// GridLayout(0, 1, 1, 1));
		// panelList.add(list);
		JPanel finalPanel = new JPanel();
		finalPanel.add(list);
		finalPanel.setOpaque(true);
		finalPanel.setBackground(Color.white);
		LightScrollPane sc = new LightScrollPane(finalPanel);
		box.add(sc);
		return box;
	}

	private JButton rightBoxTab(final String key, String imgPath, final String imgOverPath, boolean isSelected, Box listPanel) {
		if (null == rightBoxTab)
			rightBoxTab = new ArrayList<>();

		final JButton jButton = new JButton(new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + imgPath)));
		jButton.setPreferredSize(new Dimension(46, 46));
		jButton.setOpaque(false);
		jButton.setContentAreaFilled(false);
		jButton.setBorderPainted(false);
		jButton.setFocusPainted(false);
		jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jButton.setSelected(isSelected);
		jButton.setPressedIcon(new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + imgOverPath)));
		jButton.setSelectedIcon(new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + imgOverPath)));
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Map<String, Object> tab : rightBoxTab) {
					String tabKey = (String) tab.get("key");
					JButton tabBtn = (JButton) tab.get("btn");
					Box box = (Box) tab.get("view");

					if (tabKey.equalsIgnoreCase(key)) {
						tabBtn.setSelected(true);
						tabBtn.setIcon(new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + imgOverPath)));
						box.setVisible(true);
					} else {
						if (tabKey.equalsIgnoreCase("Providers")) {
							tabBtn.setIcon(new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "provider_on.png")));
						} else if (tabKey.equalsIgnoreCase("Patients")) {
							tabBtn.setIcon(new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "patient_on.png")));
						} else if (tabKey.equalsIgnoreCase("Staff")) {
							tabBtn.setIcon(new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + "staff_on.png")));
						}
						tabBtn.setSelected(false);
						box.setVisible(false);
					}
				}
			}
		});

		Map<String, Object> map = new HashMap<>();
		map.put("key", key);
		map.put("btn", jButton);
		map.put("view", listPanel);
		rightBoxTab.add(map);
		return jButton;
	}

	// private void rightRosterList(final String groupName, final Box container,
	// final Box panel, final String searchTxt) throws IOException {
	// Runnable runnable = new Runnable() {
	// @Override
	// public void run() {
	// try {
	// updateList(groupName, container, panel, searchTxt);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// };
	// new Thread(runnable).start();
	// }
	//
	//
	// private void updateList(final String groupName, final Box container,
	// final Box panel, final String searchTxt) throws IOException{
	// panel.removeAll();
	// JPanel loaderPanel = Util.loaderPanel();
	// panel.add(loaderPanel);
	// container.revalidate();
	// container.repaint();
	//
	// List<RosterVCardBo> vCardList = XmppUtils.getVCardList(groupName,
	// searchTxt);
	// panel.removeAll();
	// WelcomeUtils.getRosterList(panel, vCardList);
	//
	// container.revalidate();
	// container.repaint();
	// }

	private JLabel getLabel(final JLabel label, final String text, String imagePath) {
		// label = new JLabel(new ImageIcon(((new ImageIcon(Constants.IMAGE_PATH
		// + "/"
		// + imagePath)).getImage()).getScaledInstance(15, 15,
		// java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);
		label.setIcon(new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/" + imagePath)));
		label.setText(text);
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label.setForeground(Color.white.brighter());
		label.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		if (label.getText().equalsIgnoreCase("home")) {
			label.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					showHomeView();
				}
			});
		}
		if (label.getText().equalsIgnoreCase("info")) {
			label.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					try {

						showPopup(e);

						// InfoDialog dd = new InfoDialog(parent, label);
						// if (!dd.isVisible())
						// dd.setVisible(true);
						// else
						// dd.setVisible(false);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		if (label.getText().equalsIgnoreCase("sync")) {
			label.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					Constants.currentWelcomeScreen.refreshRightBox();
				}
			});
		}
		if (label.getText().equalsIgnoreCase("Account Setting")) {
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					// task to run goes here
					int count = 0;
//					if (count != Constants.pendingRequestCount)
//						count = Constants.pendingRequestCount;
//					if (count != 0) {
						accountSettings.setText(text + Constants.pendingRequestCount );
						accountSettings.repaint();
						accountSettings.revalidate();
//					}
				}
			};
			Timer timer = new Timer();
			long delay = 0;
			long intevalPeriod = 1 * 10;
			// schedules the task to be run in an interval
			timer.scheduleAtFixedRate(task, delay, intevalPeriod);
			label.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					parent.setEnabled(false);
					PinDialog pin = new PinDialog(parent, true);
					pin.setVisible(true);
				}
			});
		}
		if (label.getText().equalsIgnoreCase("feedback")) {
			label.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						FeedBack feedback = new FeedBack(parent);
						feedback.setVisible(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		return label;
	}

	private static void showPopup(MouseEvent ae) {
		JPopupMenu menu = new JPopupMenu();
		JMenuItem appVersion = new JMenuItem("App Version 1.1");
		// Get the event source
		menu.setBackground(Color.decode("#9CCD21"));
		menu.setOpaque(true);
		appVersion.setOpaque(true);
		appVersion.setBorderPainted(false);
		appVersion.setFocusPainted(false);
		appVersion.setCursor(new Cursor(Cursor.HAND_CURSOR));
		appVersion.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		appVersion.setBackground(Color.decode("#9CCD21"));
		appVersion.setForeground(Color.white.brighter());

		JMenuItem logout = new JMenuItem("Logout");
		// Get the event source
		logout.setOpaque(true);
		logout.setBorderPainted(false);
		logout.setFocusPainted(false);
		logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
		logout.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		logout.setBackground(Color.decode("#9CCD21"));
		logout.setForeground(Color.white.brighter());
		Component b = (Component) ae.getSource();

		// Get the location of the point 'on the screen'
		Point p = b.getLocationOnScreen();
		menu.add(appVersion);
		menu.addSeparator();
		menu.add(logout);
		appVersion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					String response = new AppCheckVersionJSON().checkAppVersion("1.1");
					JSONObject obj = new JSONObject(response);
					String err_code = obj.getString("err-code");
					if (err_code.equals("800")) {
						System.out.println(obj.getString("message"));
						parent.setEnabled(false);
						int status = JOptionPane.showConfirmDialog(parent, "<html><center>A whole new and improved IM Your Doc is "
								+ "now available.<br/>To continue using the app, you must update first!</center></html>",
								"Update App Version", JOptionPane.OK_CANCEL_OPTION);
						if (status == JOptionPane.OK_OPTION) {
							Desktop.getDesktop().browse(new URL(obj.getString("url")).toURI());
						}
					} else {
						JOptionPane.showMessageDialog(parent, "Your app is already updated!");
					}
				} catch (JSONException | IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}

			}
		});
		logout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int status = JOptionPane.showConfirmDialog(null,
						"<html><center>All conversations will be removed from your inbox at log out.<br/> "
								+ "Are you sure you want to continue?</center></html>", "HIPAA Warning!", JOptionPane.YES_NO_OPTION);
				if (status == JOptionPane.YES_OPTION) {
					Constants.isLogout = true;
					Constants.loader.setVisible(true);
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

						@Override
						protected Void doInBackground() throws Exception {
							boolean statusLogout = Util.logout();
							if (statusLogout == true) {
								Constants.loader.setVisible(false);
								Util.disposeLogoutMenu(parent);
							}
							return null;
						}

					};
					worker.execute();
				}
			}
		});
		menu.show(b, 0, 0);
		menu.setLocation(p.x, p.y + b.getHeight());
	}

	private JTextField getSearchMsgField(String placeholder) {
		if (null == messageListMap)
			messageListMap = new HashMap<>();
		JTextField searchField = getSearchField(placeholder);
		searchField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String text = ((JTextField) e.getSource()).getText();
				messageList.removeAll();
				for (String key : messageListMap.keySet()) {
					if (StringUtils.isEmpty(text) || key.toLowerCase().startsWith(text.toLowerCase())
							|| messageListMap.get(key).getMsg().trim().startsWith(text.toLowerCase())) {
						messageList.add(messageListMap.get(key).getjPanel());
					}
				}
				messageList.repaint();
				messageList.revalidate();
			}
		});
		return searchField;
	}

	private JTextField getSearchField(String placeholder) {
		JTextField searchField = new JTextField(20);
		searchField.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		PromptSupport.setPrompt(placeholder, searchField);
		searchField.setBorder(null);
		return searchField;
	}

	public static void addMessages(final RosterVCardBo vcard, String msg, boolean doHighlight, String msgId,String timeLabel,boolean isChatstate) throws IOException {
		boolean isAllreadyShown = recievedMessagePacketIds.contains(msgId);
		if (!isAllreadyShown) {
			
			if(vcard.getMemberList()==null){
				addMessages(vcard, vcard.getName(), vcard.getGroupSubject(), vcard.getUserType(), vcard.isGroup(),
						vcard.groupHasPatient(), msg, doHighlight,msgId,timeLabel,isChatstate);
			}
			else
			{
				addMessages(vcard, vcard.getGroupMemberId(), vcard.getGroupSubject(), vcard.getUserType(), vcard.isGroup(),
						vcard.groupHasPatient(), msg, doHighlight,msgId,timeLabel,isChatstate);
			}
			recievedMessagePacketIds.add(msgId);
			String uid = vcard.getUserId();
			if (!vcard.isGroup()) {
				if (!uid.contains("@")) {
					uid = uid + "@imyourdoc.com";
				}
			}
		}
	}

	public static void addMessages( final RosterVCardBo vcard, String userName, String groupSubject, String userType, final boolean isGroup,
			final boolean hasGroupPatient, String msg, final boolean doHighlight,final String msgId,String timeLabel, final boolean isChatstate) throws IOException {
		SimpleDateFormat dateformat = new SimpleDateFormat("d MMM hh:mm:ss a");
		SimpleDateFormat dateformat2 = new SimpleDateFormat("d MMM hh:mm a");
		final JLabel username = new JLabel();
		String userId = vcard.getUserId();
		if (userId.contains("@")) {
			userId = userId.split("@")[0];
		}
		final String userid = userId;
		String nameToShow = "";
		if (null == messageListMap)
			messageListMap = new HashMap<>();
		final JPanel panel = new JPanel(new BorderLayout());
		final Box hBox = Box.createHorizontalBox();
		Box vBox = Box.createVerticalBox();
		if (vcard.getMemberList() == null) {
			icon = Util.getProfileImg(userid);
			if (icon == null) {
				icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "default_pp.png"));
			}
		} else {
			if (hasGroupPatient == false) {
				icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "group_profile.png"));
			} else {
				icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "red_group_profile.png"));
			}
		}
		// if(!userType.equalsIgnoreCase("group")){

		// }
		// else{
		// icon = ImageIO.read(WelcomeUtils.class
		// .getResource(Constants.IMAGE_PATH + "/group_profile.png"));
		// }
		profilePicAddMessages = Util.combine(icon, false, 60, 60);
		if (vcard.getMemberList() != null) {
			if (groupSubject != null && !groupSubject.equals(""))
				nameToShow = groupSubject;
			else
				nameToShow = userName;
		} else {
			nameToShow = userName;
		}
		if(StringUtils.isEmpty(nameToShow)){
			nameToShow = vcard.getName();
			if(StringUtils.isEmpty(nameToShow)){
				nameToShow = vcard.getUserId().split("@")[0];
			}
		}
		
		if(Constants.SCREEN_SIZE.getWidth()<=1366){
		if (msg.length() > 25)
			msg = msg.substring(0, 25) + "..";
		
			if (nameToShow.length() > 8) {
				nameToShow = nameToShow.substring(0, 8) + "..";
			}
		}
		else if(Constants.SCREEN_SIZE.getWidth()>1366){
		{
			if (msg.length() > 35)
				msg = msg.substring(0, 35) + "...";
			}
			if (nameToShow.length() > 15) {
				nameToShow = nameToShow.substring(0, 15) + "..";
			}
		}
		
//		Date date = null;
//		try {
//			date = dateformat.parse(timeLabel);
//		} catch (ParseException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		Date date = null;
		try {
			date = dateformat.parse(timeLabel);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		username.setText("<html><div style='min-width:200px;'><font style='font-size:15px;font-weight:bold;font-family:sans-sarif;"
				+ "sarif;CentraleSansRndMedium;color:#9CCD21'>&nbsp;&nbsp;&nbsp;&nbsp;" + nameToShow + "</font>"
				+ "<font style='font-size:8px;color:gray;font-weight:bold;font-family:sans-sarif;sarif;" + "CentraleSansRndMedium'>&nbsp;"
				+ dateformat2.format(date) + "</font></div><font style='font-size:12px;font-weight:bold;color:gray;font-family:sans-sarif;sarif;CentraleSansRndMedium'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
				+ msg + "</font></html>");
		// username.setFont(new
		// Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN,
		// 18));
		// username.setForeground(Color.decode("#9CCD21"));
		// JLabel msgLabel = new JLabel(msg);
		JTextArea msgLabel = new JTextArea();

		// msgLabel.setPreferredSize(new Dimension((int)
		// (Constants.PARENT.getWidth() * 0.05),
		// (int) (Constants.PARENT.getHeight() * 0.10)));
		msgLabel.setText("  " + msg.trim());
		msgLabel.setForeground(Color.white);
		msgLabel.setEditable(false);
		msgLabel.setCursor(null);
		msgLabel.setOpaque(false);
		msgLabel.setFocusable(false);
		msgLabel.setLineWrap(true);
		msgLabel.setWrapStyleWord(true);
		msgLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		msgLabel.setForeground(Color.gray);
		msgLabel.setBackground(null);
		JPanel usernamePanel = new JPanel(new BorderLayout());
		usernamePanel.add(username, BorderLayout.WEST);
		JLabel time = new JLabel("  " + timeLabel);
		time.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 12));
		time.setForeground(Color.gray);
		// usernamePanel.add(time, BorderLayout.EAST);
		usernamePanel.setOpaque(true);
		usernamePanel.setBackground(null);

		JPanel messagePanel = new JPanel(new GridLayout(0, 1));
		messagePanel.add(msgLabel, BorderLayout.WEST);
		messagePanel.setOpaque(true);
		messagePanel.setBackground(null);
		vBox.add(Box.createVerticalStrut(5));
		vBox.add(username, BorderLayout.WEST);
		// vBox.add(messagePanel);
		// vBox.setPreferredSize(new Dimension(400,40));
		JPanel vbPanel = new JPanel();
		vbPanel.setOpaque(true);
		vbPanel.setBackground(null);
		final JPanel profilePicPanel = new JPanel();

		profilePicPanel.add(profilePicAddMessages);
		// profilePicPanel.setMaximumSize(new Dimension(60,65));
		profilePicAddMessages.setVerticalTextPosition(JLabel.NORTH);
		profilePicAddMessages.setHorizontalTextPosition(JLabel.RIGHT);
		profilePicAddMessages.setIconTextGap(-10);
		profilePicAddMessages.setForeground(Color.red);
		profilePicAddMessages.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		if (userType != null && userType.equalsIgnoreCase("patient")) {
			username.setForeground(Color.red);
		} else {
			username.setForeground(Color.decode("#9CCD21"));
		}
		username.setForeground(Color.decode("#9CCD21"));
		// profilePic.setText("<html>"+vcard.getName()+"<br/><font style='color:gray;size:small;'>"+msg+"</html>");
		profilePicPanel.setOpaque(true);
		profilePicPanel.setBackground(null);
		vbPanel.add(username);

		hBox.add(profilePicPanel, BorderLayout.WEST);
		hBox.add(vbPanel, BorderLayout.CENTER);
		hBox.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.20),
				(int) (Constants.SCREEN_SIZE.getHeight() * 0.10)));
		hBox.setMaximumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.20), (int) (Constants.SCREEN_SIZE.getHeight() * 0.10)));

		panel.add(hBox);
		// hBox.setLocation(0, 0);
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		panel.setOpaque(true);
		panel.setBackground(null);

		panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		hBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//for(String msgids:recievedMessagePacketIds){
				if(!vcard.isGroup() || vcard.getMemberList()==null)
				{
					if(isChatstate==false){
					new DBServiceUpdate().updateMessageStatusTable("Read", msgId, vcard.getUserId().split("@")[0]);
						XmppUtils.getMsgEventManager().sendDisplayedNotification(userid+"@imyourdoc.com",msgId);
					//}
						XmppUtils.sendPendingReadStatus(userid+"@imyourdoc.com");
					}
				}
			//	XmppUtils.sendPendingReadStatus(userid);
				panel.setBackground(Color.white);
				panel.setOpaque(true);
				try {
					//RosterVCardBo vCard = new DBServiceResult().getRosterUserDetails(userid);
//					if (vCard == null) {
//						try {
//							vCard = XmppUtils.getVCardBo(userid);
//						} catch (Exception e1) {
//							e1.printStackTrace();
//						}
//					}
					if (isGroup == false) {
						icon = Util.getProfileImg(userid);
						if (icon == null) {
							icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "default_pp.png"));
						}
					} else {
						if (hasGroupPatient == false) {
							icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "group_profile.png"));
						} else {
							icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "red_group_profile.png"));
						}
					}

					profilePicAddMessages = Util.combine(icon, false, 60, 60);

					profilePicAddMessages.repaint();
					profilePicPanel.removeAll();
					profilePicPanel.add(profilePicAddMessages);
					profilePicPanel.repaint();
					// profilePicAddMessages.revalidate();
					if(Constants.currentChatWindowUSERID!=null){
						if(Constants.currentChatWindowUSERID.split("@")[0].equals(userid)){
							msgcount = 0;
						}
					}
					panel.repaint();
					panel.revalidate();
					username.setForeground(Color.decode("#9CCD21"));
					WelcomeUtils.openChatWindow(vcard, false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		if (messageList == null) {
			messageList = Box.createVerticalBox();
		}
		messageList.removeAll();
		messageList.add(panel);
		MessagePanelBO messagePanelBO = new MessagePanelBO(panel, msg);
		messageListMap.put(userid, messagePanelBO);
		for (String key : messageListMap.keySet()) {
			if (!userid.equals(key)) {
				messageList.add(messageListMap.get(key).getjPanel());
			}
		}
		if (Constants.mainFrame.isFocused()) {
			if (doHighlight) {
				String userOpen = Constants.currentChatWindowUSERID;
				if (userOpen == null)
					userOpen = "";
					if (!userid.split("@")[0].equals(userOpen.split("@")[0]) || Constants.mainFrame.getExtendedState() == JFrame.ICONIFIED) {
						if(isChatstate == false)
							msgcount = msgcount + 1;
						icon = Util.getProfileImg(userId);
						if (isGroup == false) {
							icon = Util.getProfileImg(userid);
							if (icon == null) {
								icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "default_pp.png"));
							}
						} else {
							if (hasGroupPatient == false) {
								icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "group_profile.png"));
							} else {
								icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "red_group_profile.png"));
							}
						}
						if(isChatstate == false)
							profilePicAddMessages = Util.combineGreen(icon, 60, 60);
						else
							profilePicAddMessages = Util.combine(icon,false, 60, 60);
						final JLabel jcount = new JLabel(new ImageIcon(((new ImageIcon(
								UserWelcomeScreen.class.getResource("/images/secure_icon_red.png"))).getImage()).getScaledInstance(20, 20,
								java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
						GridBagConstraints gbc = new GridBagConstraints();
						gbc.weightx = 4.0;
						gbc.fill = GridBagConstraints.BASELINE;
						gbc.gridwidth = GridBagConstraints.SOUTH;
						jcount.setForeground(Color.white);
						jcount.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
						if(isChatstate == false){
							profilePicAddMessages.setText(String.valueOf(msgcount));
						}
						profilePicAddMessages.setForeground(Color.red.brighter());
						profilePicAddMessages.repaint();
						profilePicAddMessages.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
						profilePicPanel.removeAll();
						profilePicPanel.add(profilePicAddMessages);
						profilePicAddMessages.setIconTextGap(-8);
						profilePicAddMessages.setHorizontalTextPosition(SwingConstants.RIGHT);
						profilePicAddMessages.setVerticalTextPosition(SwingConstants.TOP);
						profilePicAddMessages.setBackground(null);
						int w = profilePicAddMessages.getWidth();
						int h = profilePicAddMessages.getHeight();
						int iw = jcount.getWidth();
						int ih = jcount.getHeight();
						int overlap = iw / 8;
						int x0 = (w - iw - (5) * overlap) / 8;
						int y = (h - ih) / 4;
						int x = x0 + 1 * overlap;
						// jcount.setBounds(x, y, w, h);
						// jcount.setComponentZOrder(profilePicAddMessages,2);
						// profilePicPanel.add(jcount);
						profilePicPanel.repaint();
						profilePicPanel.revalidate();
						profilePicPanel.setOpaque(true);
						// profilePicAddMessages.revalidate();
						panel.repaint();
						panel.revalidate();
						if(isChatstate == false)
							Util.highlight(panel, username);
					}
			}
		}
		messagePanel.repaint();
		messagePanel.revalidate();
		// Util.highlight(panel);
		// JLabel noMessage = new
		// JLabel("<HTML><I><br>No Messages to display</br></I></HTML>");
		// noMessage.setOpaque(true);
		// noMessage.setBackground(Color.white);
		// noMessage.setForeground(Color.gray);
		// noMessage.setFont(new
		// Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN,
		// 14));
	}

	// public static void addMessagesGroup(final String roomId, String msg,
	// String timeLabel, boolean doHighlight) throws IOException {
	//
	// if (null == messageListMap)
	// messageListMap = new HashMap<>();
	// final JPanel panel = new JPanel();
	// final Box hBox = Box.createHorizontalBox();
	// Box vBox = Box.createVerticalBox();
	// icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH +
	// "/group_profile.png"));
	//
	// profilePicAddMessages = Util.combine(icon, false, 60, 60);
	// final JLabel RoomIDLabel = new JLabel("  " + roomId);
	// RoomIDLabel.setFont(new
	// Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN,
	// 18));
	// RoomIDLabel.setForeground(Color.decode("#9CCD21"));
	// // JLabel msgLabel = new JLabel(msg);
	// JTextArea msgLabel = new JTextArea();
	// if (msg.length() > 30)
	// msg = msg.substring(0, 25) + "...";
	// // msgLabel.setPreferredSize(new Dimension((int)
	// // (Constants.PARENT.getWidth() * 0.05),
	// // msgLabel.setPreferredSize(new Dimension((int)
	// // (Constants.PARENT.getWidth() * 0.05),
	// // (int) (Constants.PARENT.getHeight() * 0.10)));
	// msgLabel.setText("  " + msg.trim());
	// msgLabel.setForeground(Color.white);
	// msgLabel.setEditable(false);
	// msgLabel.setCursor(null);
	// msgLabel.setOpaque(false);
	// msgLabel.setFocusable(false);
	// msgLabel.setLineWrap(true);
	// msgLabel.setWrapStyleWord(true);
	// msgLabel.setFont(new
	// Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN,
	// 18));
	// msgLabel.setForeground(Color.gray);
	// msgLabel.setBackground(null);
	// JPanel usernamePanel = new JPanel(new GridLayout(1, 2));
	// usernamePanel.add(RoomIDLabel, BorderLayout.WEST);
	// JLabel time = new JLabel("  " + timeLabel);
	// time.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(),
	// Font.PLAIN, 12));
	// time.setForeground(Color.gray);
	// usernamePanel.add(time, BorderLayout.EAST);
	// usernamePanel.setOpaque(true);
	// usernamePanel.setBackground(null);
	//
	// JPanel messagePanel = new JPanel(new GridLayout(0, 1));
	// messagePanel.add(msgLabel, BorderLayout.WEST);
	// messagePanel.setOpaque(true);
	// messagePanel.setBackground(null);
	// vBox.add(Box.createVerticalStrut(5));
	// vBox.add(usernamePanel, BorderLayout.WEST);
	// vBox.add(messagePanel);
	// // vBox.setPreferredSize(new Dimension(400,40));
	// JPanel vbPanel = new JPanel();
	// vbPanel.setOpaque(true);
	// vbPanel.setBackground(null);
	// final JPanel profilePicPanel = new JPanel();
	//
	// profilePicPanel.add(profilePicAddMessages);
	// // profilePicPanel.setMaximumSize(new Dimension(60,65));
	// profilePicAddMessages.setVerticalTextPosition(JLabel.NORTH);
	// profilePicAddMessages.setHorizontalTextPosition(JLabel.RIGHT);
	// profilePicAddMessages.setIconTextGap(-10);
	// profilePicAddMessages.setForeground(Color.red);
	// profilePicAddMessages.setFont(new
	// Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN,
	// 18));
	// RoomIDLabel.setForeground(Color.decode("#9CCD21"));
	// //
	// profilePic.setText("<html>"+vcard.getName()+"<br/><font style='color:gray;size:small;'>"+msg+"</html>");
	// profilePicPanel.setOpaque(true);
	// profilePicPanel.setBackground(null);
	// vbPanel.add(vBox);
	// hBox.add(profilePicPanel, BorderLayout.WEST);
	// hBox.add(vbPanel);
	// hBox.setPreferredSize(new Dimension((int) (Constants.PARENT.getWidth() *
	// 0.20), (int) (Constants.PARENT.getHeight() * 0.10)));
	// hBox.setMaximumSize(new Dimension((int) (Constants.PARENT.getWidth() *
	// 0.20), (int) (Constants.PARENT.getHeight() * 0.10)));
	//
	// panel.add(hBox);
	// hBox.setLocation(0, 0);
	// panel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0,
	// Color.LIGHT_GRAY));
	// panel.setOpaque(true);
	// panel.setBackground(null);
	//
	// panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
	// panel.addMouseListener(new MouseAdapter() {
	// @Override
	// public void mouseClicked(MouseEvent e) {
	// panel.setBackground(Color.white);
	// panel.setOpaque(true);
	//
	// try {
	// icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH +
	// "/group_profile.png"));
	// profilePicAddMessages = Util.combine(icon, false, 60, 60);
	// profilePicAddMessages.repaint();
	// profilePicPanel.removeAll();
	// profilePicPanel.add(profilePicAddMessages);
	// profilePicPanel.repaint();
	// // profilePicAddMessages.revalidate();
	// Constants.COUNT_MESSAGE = 0;
	// panel.repaint();
	// panel.revalidate();
	// WelcomeUtils.openChatWindowGroup(ComposeMessage.rosterVCardBoListMap,
	// false);
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	//
	// }
	// });
	// // messageList.setPreferredSize(new Dimension((int)
	// // (Constants.SCREEN_SIZE.getWidth()*0.20), 60));
	//
	// messageList.removeAll();
	//
	// messageList.add(panel);
	//
	// MessagePanelBO messagePanelBO = new MessagePanelBO(panel, msg);
	// messageListMap.put(roomId, messagePanelBO);
	// for (String key : messageListMap.keySet()) {
	// if (!roomId.equals(key)) {
	// messageList.add(messageListMap.get(key).getjPanel());
	// }
	// }
	// if (doHighlight) {
	// if(Constants.showConsole) System.out.println("??" + roomId);
	// if(Constants.showConsole) System.out.println(">>" + Constants.USERID);
	// String roomIDOpen = Constants.roomId;
	// if (roomIDOpen != null) {
	// if (!roomId.equals(roomIDOpen) || Constants.mainFrame.getExtendedState()
	// == JFrame.ICONIFIED) {
	// Constants.COUNT_MESSAGE++;
	// icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH +
	// "/group_profile.png"));
	//
	// profilePicAddMessages = Util.combineGreen(icon, 60, 60);
	// final JLabel jcount = new JLabel(new ImageIcon(((new ImageIcon(
	// UserWelcomeScreen.class.getResource("/images/secure_icon_red.png"))).getImage()).getScaledInstance(20,
	// 20,
	// java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
	// GridBagConstraints gbc = new GridBagConstraints();
	// gbc.weightx = 4.0;
	// gbc.fill = GridBagConstraints.BASELINE;
	// gbc.gridwidth = GridBagConstraints.SOUTH;
	// jcount.setForeground(Color.white);
	// jcount.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
	// profilePicAddMessages.setText(String.valueOf(Constants.COUNT_MESSAGE));
	// profilePicAddMessages.setForeground(Color.red.brighter());
	// profilePicAddMessages.repaint();
	// profilePicAddMessages.setFont(new
	// Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN,
	// 18));
	// profilePicPanel.removeAll();
	// profilePicPanel.add(profilePicAddMessages);
	// profilePicAddMessages.setIconTextGap(-8);
	// profilePicAddMessages.setHorizontalTextPosition(SwingConstants.RIGHT);
	// profilePicAddMessages.setVerticalTextPosition(SwingConstants.TOP);
	// profilePicAddMessages.setBackground(null);
	// int w = profilePicAddMessages.getWidth();
	// int h = profilePicAddMessages.getHeight();
	// int iw = jcount.getWidth();
	// int ih = jcount.getHeight();
	// int overlap = iw / 8;
	// int x0 = (w - iw - (5) * overlap) / 8;
	// int y = (h - ih) / 4;
	// int x = x0 + 1 * overlap;
	// // jcount.setBounds(x, y, w, h);
	// // jcount.setComponentZOrder(profilePicAddMessages,2);
	// // profilePicPanel.add(jcount);
	// profilePicPanel.repaint();
	// profilePicPanel.revalidate();
	// profilePicPanel.setOpaque(true);
	// // profilePicAddMessages.revalidate();
	// panel.repaint();
	// panel.revalidate();
	// Util.highlight(panel, RoomIDLabel);
	// }
	// }
	// }
	// messagePanel.repaint();
	// messagePanel.revalidate();
	// }
	public static void removeMsg(String userId) {
		messageList.removeAll();
		if (messageListMap == null) {
			showHomeView();
		} else {
			messageListMap.remove(userId);
			for (String key : messageListMap.keySet()) {
				messageList.add(messageListMap.get(key).getjPanel());
			}
			messagePanel.repaint();
			messagePanel.revalidate();
		}
	}

	public static void showHomeView() {
		try {
			centerBox.removeAll();
			centerBox.add(WelcomeUtils.welcomeBox(), BorderLayout.CENTER);
			centerBox.revalidate();
			centerBox.repaint();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void CheckWindowStatus() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				// Use the WindowAdapter class to intercept only the window
				// closing event
				addWindowListener(new WindowAdapter() {
					@Override
					public void windowIconified(WindowEvent e) {
						if (!(SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX)) {
							new HideToSystemTray(parent);
						}
						if (Constants.showConsole)
							System.out.println("starting....");
						startTimer();
						elapsedSeconds = 50;
					}

					@Override
					public void windowDeiconified(WindowEvent e) {
						if (elapsedSeconds <= 0) {
							timer.stop();
							PinDialog pin = new PinDialog(parent, false);
							pin.setVisible(true);
							elapsedSeconds = 50;
						} else if (elapsedSeconds < 0) {
							elapsedSeconds = 50;
						}
						if(!StringUtils.isEmpty(Constants.currentChatWindowUSERID)){
							if(!Constants.currentChatWindowUSERID.contains("_")){
								if(!Constants.currentChatWindowUSERID.contains("@")){
									XmppUtils.sendPendingReadStatus(Constants.currentChatWindowUSERID+"@imyourdoc.com");
								}
								else
								{
									XmppUtils.sendPendingReadStatus(Constants.currentChatWindowUSERID);
								}
									
							}
						}
					}
				});

				return null;
			}

		};
		worker.execute();
	}

	public void startTimer() {
		timer = new javax.swing.Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (elapsedSeconds > 0)
					elapsedSeconds--;
				else if (elapsedSeconds == 0) {
					timer.stop();
				}
				// System.out.println(elapsedSeconds);
			}

		});
		timer.start();
	}

	public void refreshRightBox() {
		try {
			XmppUtils.roster = null;
			XmppUtils.rosterEntries = null;
			XmppUtils.rosterVCardBoListMap = null;
			XmppUtils.rosterVCardBoMap = null;
			new DBServiceUpdate().dropRosterTable();
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					// XmppUtils.getGroups();
					return null;
				}

			};
			worker.execute();
			rightBox.removeAll();
			Util.userProfileImgMap = null;
			rightBox();
			rightBox.repaint();
			rightBox.revalidate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

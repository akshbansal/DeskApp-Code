package com.im.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.im.bo.ActiveUserBo;
import com.im.bo.AdminBO;
import com.im.bo.GroupBo;
import com.im.bo.LoginUserBO;
import com.im.bo.MessageSyncBo;
import com.im.bo.RosterVCardBo;
import com.im.chat.UserChat;
import com.im.common.ConfigureAccountDialog;
import com.im.common.TopPanel;
import com.im.db.DBServiceCreate;
import com.im.db.DBServiceInsert;
import com.im.db.DBServiceResult;
import com.im.db.DBServiceUpdate;
import com.im.json.AuthenticateJSON;
import com.im.json.DeviceRegistrationJSON;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.utils.ChatStateXML;
import com.im.utils.Constants;
import com.im.utils.FindMACAddress;
import com.im.utils.HospitalRelatedList;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.XmppUtils;
import com.im.validationListeners.SpecialCharacterListener;

public class Login extends JFrame implements KeyListener {

	/**
	 * @author megha
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private Box box;
	Point point = new Point();
	boolean resizing = false;
	private JTextField textUsername;
	private JPasswordField textPassword;
	private JButton loginButton;
	private JLabel labelUsername;
	private JLabel labelPassword;
	private JLabel topIcon;
	private JPanel topPanel;
	private JPanel loginButtonPanel;
	private JPanel forgetPasswordPanel;
	private JLabel forgetPassword;
	private JPanel signUpPanel;
	private JLabel signUp;
	private JPanel needHelpPanel;
	private JLabel needHelp;
	private JPanel usernamepanel;
	private JPanel passwordpanel;
	private static JSONObject jsonResponse = null;
	private static JFrame parent;
	private static int count = 0;
	public static List<GroupBo> ActiveGroupIds = new ArrayList<GroupBo>();
	public static List<ActiveUserBo> activeUserIds = new ArrayList<ActiveUserBo>();
	public Login() {
		parent = this;
		Constants.PARENT = parent;
		setUndecorated(true);
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 1, 1, 0));
	}

	public void initComponents() throws IOException, FontFormatException {

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Fonts/Typedepot_CentraleSansRndMedium.ttf")));

		forgetPasswordPanel = new JPanel();
		needHelpPanel = new JPanel();
		signUpPanel = new JPanel();
		loginButtonPanel = new JPanel();
		labelPassword = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource("/images/password_icon.png"))).getImage()).getScaledInstance(40, 40,
						java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);
		topPanel = new JPanel();
		labelUsername = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource("/images/username_icon.png"))).getImage()).getScaledInstance(40, 40,
						java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);

		Box usernameBox = Box.createHorizontalBox();
		usernamepanel = new JPanel();// Box.createHorizontalBox();
		usernamepanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.70), 70));
		usernamepanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
		usernamepanel.setOpaque(true);
		usernamepanel.setBackground(Color.white);
		passwordpanel = new JPanel();// Box.createHorizontalBox();
		passwordpanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.70), 70));
		passwordpanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
		passwordpanel.setOpaque(true);
		passwordpanel.setBackground(Color.white);
		panel = new JPanel();
		box = Box.createVerticalBox();
		textUsername = new JTextField(10){
			private static final long serialVersionUID = 1L;
			@Override
			public void addNotify() {
	            super.addNotify();
	            requestFocus();
	        }
		};
		// textUsername.setText("meghapat123");
		textUsername.addFocusListener(new SpecialCharacterListener());
		textUsername.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 24));
		forgetPassword = new JLabel("Forgot Password?");
		forgetPassword.setFont(new Font(Font.decode("CentraleSansRndBold").getFontName(), Font.PLAIN, 20));
		needHelp = new JLabel("Need Help?");
		needHelp.setFont(new Font(Font.decode("CentraleSansRndBold").getFontName(), Font.PLAIN, 20));
		signUp = new JLabel("<html><u>Sign Up Now!</u></html>");
		signUp.setForeground(Color.darkGray);
		signUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
		signUp.addMouseListener(new OpenSignUpOptionDialog(this));

		signUp.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 24));
		needHelp.setOpaque(true);
		needHelp.setBackground(null);
		needHelp.setForeground(Color.lightGray);
		needHelp.setCursor(new Cursor(Cursor.HAND_CURSOR));
		needHelp.addMouseListener(new NeedHelpOpenListener(this));
		forgetPassword.setOpaque(true);
		forgetPassword.setBackground(null);
		forgetPassword.setForeground(Color.LIGHT_GRAY);
		forgetPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
		forgetPassword.addMouseListener(new OpenForgetPassword(this));
		textPassword = new JPasswordField(10);
		char echo = '*';
		textPassword.setEchoChar(echo);
		// textPassword.setText("Aa12345");
		textPassword.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 24));
		PromptSupport.setPrompt("Username", textUsername);
		PromptSupport.setPrompt("Password", textPassword);
		topIcon = new JLabel(new ImageIcon(((new ImageIcon(getClass().getResource("/images/logo.png"))).getImage()).getScaledInstance(200,
				250, java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
		loginButton = new JButton("Login");
		// loginButton.setIcon(icon);
		loginButton.setOpaque(true);
		BufferedImage master = ImageIO.read(getClass().getResource("/images/submit_btn.png"));
		Image scaled = master.getScaledInstance((int) (Constants.SCREEN_SIZE.getWidth() * 0.80), 50, java.awt.Image.SCALE_SMOOTH);
		loginButton.setIcon(new ImageIcon(scaled));
		loginButton.addActionListener(new LoginActionListener());

		getRootPane().setDefaultButton(loginButton);
		loginButton.setFocusable(true);

		loginButton.registerKeyboardAction(loginButton.getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_FOCUSED);
		loginButtonPanel.setOpaque(true);
		loginButton.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 25));
		// loginButton.setBorder(new TextBubbleBorder(Color.decode("#9CCD21"),
		// 2, 5, 0));
		loginButtonPanel.setBackground(Color.white);
		loginButton.setOpaque(true);
		loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// loginButton.setBackground(Color.decode("#9CCD21"));
		// loginButton.setPreferredSize(new Dimension(600, 30));\
		// loginButtonPanel.setPreferredSize(new Dimension((int)
		// (Constants.SCREEN_SIZE.getWidth()*0.60), 40));
		Util.setTransparentBtn(loginButton);
		loginButton.setHorizontalTextPosition(SwingConstants.CENTER);
		loginButton.setVerticalAlignment(SwingConstants.CENTER);
		loginButton.setForeground(Color.white.brighter());
		// usernamepanel.add(Box.createHorizontalStrut(200));
		usernameBox.add(labelUsername);
		usernameBox.add(Box.createHorizontalStrut(5));
		usernameBox.add(textUsername);

		usernamepanel.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.05)));
		usernamepanel.add(usernameBox);
		textUsername.setForeground(Color.gray);
		textUsername.setBorder(null);
		textPassword.setBorder(null);
		Box passwordBox = Box.createHorizontalBox();
		// labelPassword.setMaximumSize(new Dimension(30, 30));
		// labelPassword.setBackground(Color.white);
		//

		passwordBox.add(labelPassword, BorderLayout.CENTER);
		passwordBox.add(Box.createHorizontalStrut(5));
		passwordBox.add(textPassword, BorderLayout.CENTER);
		passwordpanel.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.05)));
		passwordpanel.add(passwordBox);
		topPanel.setBackground(null);
		box.add(Box.createVerticalStrut(20));
		topPanel.add(topIcon);
		box.add(topPanel, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut(20));
		textUsername.setOpaque(true);
		textUsername.setBackground(null);

		box.add(usernamepanel, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut((int) (Constants.SCREEN_SIZE.getHeight() * 0.01)));
		box.add(passwordpanel, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut((int) (Constants.SCREEN_SIZE.getHeight() * 0.01)));
		loginButtonPanel.add(loginButton, BorderLayout.CENTER);
		box.add(loginButtonPanel, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut((int) (Constants.SCREEN_SIZE.getHeight() * 0.01)));
		forgetPasswordPanel.setBackground(null);
		forgetPasswordPanel.add(forgetPassword, BorderLayout.CENTER);

		needHelpPanel.setBackground(null);
		needHelpPanel.add(needHelp, BorderLayout.CENTER);
		signUpPanel.setBackground(null);
		signUpPanel.add(signUp, BorderLayout.CENTER);
		box.add(forgetPasswordPanel, BorderLayout.CENTER);
		box.add(needHelpPanel, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut(10));
		box.add(signUpPanel, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut(10));
		panel.add(box);
		panel.setBackground(Color.white.brighter());
		getContentPane().setBackground(Color.white);
		add(panel, BorderLayout.CENTER);
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 2, 2, 0));
		add(TopPanel.topButtonPanel(this, true), BorderLayout.NORTH);
		int x = (Constants.SCREEN_SIZE.width) / 10;
		int y = (Constants.SCREEN_SIZE.height) / 25;
		setBounds(x, y, Constants.SCREEN_SIZE.width / 2, Constants.SCREEN_SIZE.height / 2);

		setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.width * 0.80), (int) (Constants.SCREEN_SIZE.height * 0.80)));

		JFrame.setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.ICONIFIED);
		// initComponents();
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				resizing = getCursor().equals(Cursor.getDefaultCursor()) ? false : true;
				if (!e.isMetaDown()) {
					point.x = e.getX();
					point.y = e.getY();
				}
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (resizing) {
					Point pt = e.getPoint();
					setSize(getWidth() + pt.x - point.x, getHeight());
					point.x = pt.x;
				} else if (!e.isMetaDown()) {
					Point p = getLocation();
					setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
				}
			}
		});
		pack();
		setTitle("IM YOUR DOC");
		setResizable(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo_welcome.png")));
		setVisible(true);
		setDefaultCloseOperation(JFrame.ICONIFIED);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// new HideToSystemTray(this);
	}

	/*
	 * public static void main(String[] args) throws IOException,
	 * FontFormatException { Login login = new Login(); login.initComponents();
	 * login.setVisible(true); login.setTitle("IM YOUR DOC"); login.setFont(new
	 * Font(Font.decode("CentraleSansRndMedium") .getFontName(), Font.PLAIN,
	 * 10)); // login.setExtendedState(MAXIMIZED_BOTH); Thread t = new
	 * Thread(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * HospitalRelatedList hospitalRelatedList = new HospitalRelatedList(); try
	 * { Constants.PRACTISE_LIST =
	 * hospitalRelatedList.getPractiseListResponse(); Constants.DESIGNATION_LIST
	 * = hospitalRelatedList.getDesignationListResponse();
	 * Constants.JOB_TITLE_LIST = hospitalRelatedList.getTitleListResponse(); }
	 * catch (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * } }); t.start(); login.setMinimumSize(new Dimension(1000, 600));
	 * login.setLocation(200, 30); JFrame.setDefaultLookAndFeelDecorated(true);
	 * new HideToSystemTray(login);
	 * login.setDefaultCloseOperation(JFrame.ICONIFIED); }
	 */

	private class LoginActionListener implements ActionListener, KeyListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			final String usernameTxt = textUsername.getText();
			final String passwordTxt = textPassword.getText();
			loginListener(usernameTxt,passwordTxt);

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				final String usernameTxt = textUsername.getText();
				final String passwordTxt = textPassword.getText();
				loginListener(usernameTxt,passwordTxt);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				final String usernameTxt = textUsername.getText();
				final String passwordTxt = textPassword.getText();
				loginListener(usernameTxt,passwordTxt);
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public static void loginListener(final String usernameTxt,final String passwordTxt) {
		
		// String status = "Hello Everyone.";
		
		if (Util.isStringEmpty(usernameTxt) || Util.isStringEmpty(passwordTxt)) {
			JOptionPane.showMessageDialog(parent.getRootPane(), "All Fields are mandatory", "Username Blank", JOptionPane.ERROR_MESSAGE);
		} else {
			parent.setEnabled(false);
			Constants.loader.setVisible(true);
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
				@Override
				protected Void doInBackground() throws Exception {

					String response = "";
					String err_code = "";
					try {
						FindMACAddress macAddress = new FindMACAddress();
						Constants.loggedinuserInfo.username = usernameTxt;
						Constants.loggedinuserInfo.password = passwordTxt;
						Constants.loggedinuserInfo.device_id = macAddress.DeviceIDOfSystem();
						Constants.loggedinuserInfo.device_type = "web";

						String username_xmpp = usernameTxt;
						String password_xmpp = passwordTxt;
						StringBuffer username = new StringBuffer();
						AuthenticateJSON login = new AuthenticateJSON();
						response = login.doAuthentication();
						if (!StringUtils.isEmpty(response)) {
							jsonResponse = new JSONObject(response);
							err_code = jsonResponse.getString("err-code");
							if (!username_xmpp.endsWith("@imyourdoc.com"))
								username.append(username_xmpp + "@imyourdoc.com");
							else
								username.append(username_xmpp);
							if (err_code.equals("1")) {
								
								if (!jsonResponse.getString("user_type").equalsIgnoreCase("Hospital Admin")) {
									Constants.loggedinuserInfo.name = jsonResponse.getString("name");
									Constants.loggedinuserInfo.user_type = jsonResponse.getString("user_type");
									Constants.loggedinuserInfo.user_pin = jsonResponse.getString("user_pin");
									Constants.loggedinuserInfo.login_token = jsonResponse.getString("login_token");
									Constants.loggedinuserInfo.seq_ques = jsonResponse.getString("seq_question");
									Constants.loggedinuserInfo.seq_ans = jsonResponse.getString("seq_answer");
									
//									SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>()
//									{
	//
//										@Override
//										protected Void doInBackground()
//												throws Exception {
											/*LoginUserBO loginBo = new LoginUserBO();
											loginBo.setLogin_token(jsonResponse.getString("login_token"));
											loginBo.setName(jsonResponse.getString("name"));
											if(jsonResponse.getString("user_type").equalsIgnoreCase("physician"))
													loginBo.setProvider(true);
											else if(jsonResponse.getString("user_type").equalsIgnoreCase("patient"))
													loginBo.setPatient(true);
											else if(jsonResponse.getString("user_type").equalsIgnoreCase("staff"))
													loginBo.setStaff(true);
											
											loginBo.setUsername(usernameTxt);
											loginBo.setUser_type(jsonResponse.getString("user_type"));
											BufferedImage img = Util.getProfileImg(usernameTxt);
											loginBo.setProfileImage(Util.bufferImgToByteArray(img));
											loginBo.setUser_pin(jsonResponse.getString("user_pin"));
											loginBo.setSeq_ques(jsonResponse.getString("seq_question"));
											loginBo.setSeq_ans(jsonResponse.getString("seq_answer"));
											new DBServiceCreate().createAllTable();
											LoginUserBO loginUserBO = new DBServiceResult().getLoginUserDetails(usernameTxt);
										//	if(loginUserBO==null)
											new DBServiceInsert().insertUserTable(loginBo);*/
											
//										}
//									};
//									worker.execute();
									
									Util.setUserTypeToConstant(Constants.loggedinuserInfo.user_type);
									if (jsonResponse.getString("user_type").equalsIgnoreCase("Front Desk Admin")) {
										AdminBO bo = new AdminBO();
										bo.setUserName(jsonResponse.getString("user_name"));
										bo.setLogin_token(jsonResponse.getString("login_token"));
										bo.setUser_type(jsonResponse.getString("user_type"));
										bo.setName(jsonResponse.getString("name"));
										bo.setUser_pin(jsonResponse.getString("user_pin"));
										bo.setSeq_question(jsonResponse.getString("seq_question"));
										bo.setSeq_answer(jsonResponse.getString("seq_answer"));
										Constants.ADMIN_LOGIN_TOKEN = jsonResponse.getString("login_token");
										Constants.IS_ADMIN_LOGIN = true;
									} else {
										Constants.IS_ADMIN_LOGIN = false;
									}
									boolean isDoLogin = false;
									if (jsonResponse.getBoolean("required_register_device") == true) {
										parent.setEnabled(true);
										Constants.loader.setVisible(false);

										JTextField enterDeviceName = new JTextField(10);
										int value = JOptionPane.showConfirmDialog(parent, enterDeviceName,
												"Please provide a name for this device.", JOptionPane.OK_CANCEL_OPTION);
										if (value == JOptionPane.OK_OPTION) {
											if (enterDeviceName.getText().equals("")) {
												JOptionPane.showMessageDialog(parent.getRootPane(), "Please enter device name");
											} else {
												String response2 = DeviceRegistrationJSON.doDeviceRegisteration(enterDeviceName.getText());
												JSONObject jsonResponse2 = new JSONObject(response2);
												if (jsonResponse2.getString("err-code").equals("700")) {
													JOptionPane.showMessageDialog(parent.getRootPane(), "Password is incorrect!");
													// doLogin(username.toString(), password_xmpp);
												} else if (jsonResponse2.getString("err-code").equals("404")) {
													Constants.loader.setVisible(true);
													parent.setEnabled(false);
													JOptionPane.showMessageDialog(parent.getRootPane(), jsonResponse2.getString("message"));
												} else if (jsonResponse2.getString("err-code").equals("1")) {
													JOptionPane.showMessageDialog(parent.getRootPane(), jsonResponse2.getString("message"));
													Constants.loader.setVisible(true);
													parent.setEnabled(false);
													isDoLogin = true;
												}
											}
										}
									}
									if (jsonResponse.getString("required_config").equals("1")) {
										Constants.loader.setVisible(false);
										ConfigureAccountDialog configure = new ConfigureAccountDialog(parent, username.toString().split("@")[0]);
										configure.setVisible(true);
										isDoLogin = false;
									} 
									else{
										isDoLogin = true;
									}
									if(isDoLogin) {
										JSONArray groups = jsonResponse.getJSONArray("active_conversations");
										if(groups!=null)
										{
											for(int i= 0;i<groups.length();i++)
											{
												String objJid =  groups.getJSONObject(i).getString("jid");
													if(objJid.endsWith("@newconversation.imyourdoc.com")||objJid.contains("_")){
														objJid = objJid.split("@")[0]+"@newconversation.imyourdoc.com";
														GroupBo groupbo = new GroupBo();
														groupbo.setRoomid(objJid);
														groupbo.setLastActiveDate(groups.getJSONObject(i).getString("lastActive"));
														groupbo.setLastActiveSecs(groups.getJSONObject(i).getString("last_active_sec"));
														ActiveGroupIds.add(groupbo);
														synchronized (groupbo) {
															new DBServiceInsert().insertActiveGroups(groupbo);
														}
													}
													else
													{
														ActiveUserBo activeuser = new ActiveUserBo();
														if(objJid.contains("@"))
														{
															objJid = objJid.split("@")[0];
														}
														activeuser.setUserId(objJid+"@imyourdoc.com");
														activeuser.setLastActiveDate(groups.getJSONObject(i).getString("lastActive"));
														activeuser.setLastActiveSecs(groups.getJSONObject(i).getString("last_active_sec"));
														
														activeUserIds.add(activeuser);
														synchronized (activeuser) {
															new DBServiceInsert().insertActiveUsers(activeuser);
														}
														
														
													}
											}
										}
										doLogin(username.toString(), password_xmpp);
									}
								} else {
									Constants.loader.setVisible(false);
									parent.setEnabled(true);
									JOptionPane
											.showMessageDialog(parent.getRootPane(), "You are Hospital Admin, "
													+ "not authorized to access this application! ");
								}
							} else if (err_code.equals("700")) {
								parent.setEnabled(true);
								Constants.loader.setVisible(false);
								JOptionPane.showMessageDialog(parent.getContentPane(), "Username or password are not correct",
										"Username Not Correct", JOptionPane.INFORMATION_MESSAGE);
							} else if (err_code.equals("404")) {
								parent.setEnabled(true);
								Constants.loader.setVisible(false);
								JOptionPane.showMessageDialog(parent.getContentPane(), jsonResponse.get("message"),
										"Username Not found", JOptionPane.INFORMATION_MESSAGE);
							}
						}

						/*else
						{
							for(int i =0;i<2;i++){
								count = count+1;
								loginListener(usernameTxt,passwordTxt);
							}
							if(count==2){
							 JOptionPane.showMessageDialog(parent.getRootPane(),
									 "<html><center>Could not connect to IMYourDoc's API Server. <br/>"
									 + "Please show this message to your network administrator."
									 + "<br/> Port 443 TCP blocked for api.imyourdoc.com</center></html>",
									 "Error", JOptionPane.ERROR_MESSAGE);
							}
							
						}*/

					} catch (Exception e1) {
						// do nothing
						
						 for(int i =0;i<2;i++){
								count = count+1;
								loginListener(usernameTxt,passwordTxt);
							}
							if(count==2){
							 parent.setEnabled(true);
							 Constants.loader.setVisible(false);
							 JOptionPane.showMessageDialog(parent.getRootPane(),
									 "<html><center>Could not connect to IMYourDoc's API Server. <br/>"
									 + "Please show this message to your network administrator."
									 + "<br/> Port 443 TCP blocked for api.imyourdoc.com</center></html>",
									 "Error", JOptionPane.ERROR_MESSAGE);
							}
						e1.printStackTrace();
					}
					return null;
				}

			};
			worker.execute();
		}
	}

	public static void doLogin(final String username, final String Password) throws XMPPException, IOException {

		System.out.println("login done...");
		// commented the xmpp login for the time being as not configured on new
		// apist
		
				try {
					Constants.loggedinuserInfo.username = username;
					Constants.loggedinuserInfo.password = Password;
					LoginUserBO loginBo = new LoginUserBO();
					loginBo.setLogin_token(Constants.loggedinuserInfo.login_token);
					loginBo.setName(Constants.loggedinuserInfo.name);
					if(Constants.loggedinuserInfo.user_type.equalsIgnoreCase("physician"))
							loginBo.setProvider(true);
					else if(Constants.loggedinuserInfo.user_type.equalsIgnoreCase("patient"))
							loginBo.setPatient(true);
					else if(Constants.loggedinuserInfo.user_type.equalsIgnoreCase("staff"))
							loginBo.setStaff(true);
					
					loginBo.setUsername(Constants.loggedinuserInfo.username.split("@")[0]);
					loginBo.setUser_type(Constants.loggedinuserInfo.user_type);
					BufferedImage img = Util.getProfileImg(Constants.loggedinuserInfo.username.split("@")[0]);
					if(img!=null)
						loginBo.setProfileImage(Util.bufferImgToByteArray(img));
					loginBo.setUser_pin(jsonResponse.getString("user_pin"));
					loginBo.setSeq_ques(jsonResponse.getString("seq_question"));
					loginBo.setSeq_ans(jsonResponse.getString("seq_answer"));
					
					new DBServiceUpdate().clearAllData();
					boolean isCreated =	new DBServiceCreate().createAllTable();
				//	if(loginUserBO==null)
				if(isCreated){
					boolean isInserted = new DBServiceInsert().insertUserTable(loginBo);
					if(isInserted)
					{
						try{
							Boolean isLogin = XmppUtils.doLogin(username, Password, XmppUtils.getImyourDocConnection());
							if (isLogin) {
								
								Constants.currentWelcomeScreen = new UserWelcomeScreen();
								Constants.currentWelcomeScreen.setVisible(true);
								Constants.loader.setVisible(false);
								parent.setEnabled(true);
								parent.dispose();
								SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
	
									@Override
									protected Void doInBackground() throws Exception {
										
										
										HospitalRelatedList hospitalRelatedList = new HospitalRelatedList();
										try{
										if(Constants.PRACTISE_LIST==null)
											Constants.PRACTISE_LIST = hospitalRelatedList.getPractiseListResponse();
										if(Constants.DESIGNATION_LIST ==null)
											Constants.DESIGNATION_LIST = hospitalRelatedList.getDesignationListResponse();
										if(Constants.JOB_TITLE_LIST ==null)
											Constants.JOB_TITLE_LIST = hospitalRelatedList.getTitleListResponse();
										}
										catch(Exception e){
											e.printStackTrace();
										}
										return null;
									}
								};
								worker.execute();
								
								
								
							}
						}
						catch(Exception e){
//							parent.setEnabled(true);
//							Constants.loader.setVisible(false);
//							JOptionPane.showMessageDialog(parent.getRootPane(), "<html>Unable to connect to imyourdoc.com<br>"
//									+ "Please check internet connection.</br></html>", "Error", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
						
							}
					} else {
//						parent.setEnabled(true);
//						Constants.loader.setVisible(false);
//						JOptionPane.showMessageDialog(parent.getRootPane(), "<html>Unable to connect to imyourdoc.com<br>"
//								+ "Please check internet connection.</br></html>", "Error", JOptionPane.ERROR_MESSAGE);
					}
				
				
				} catch (Exception ex) {
//					parent.setEnabled(true);
//					Constants.loader.setVisible(false);
//					JOptionPane.showMessageDialog(parent.getRootPane(), "<html>Unable to connect to imyourdoc.com<br>"
//							+ "Please check internet connection.</br></html>", "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			
	}

	private class OpenSignUpOptionDialog implements MouseListener {
		JFrame frame;

		private OpenSignUpOptionDialog(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

			UserTypeDialog dialog = new UserTypeDialog(frame);
			dialog.setVisible(true);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}

	private class OpenForgetPassword extends MouseAdapter {
		JFrame frame;

		private OpenForgetPassword(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			ForgetPassword forgetPassword = new ForgetPassword(frame);
			forgetPassword.setVisible(true);
		}
	}

	private class NeedHelpOpenListener extends MouseAdapter {
		JFrame frame;

		private NeedHelpOpenListener(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			NeedHelp needHelp = new NeedHelp(frame);
			needHelp.setVisible(true);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			final String usernameTxt = textUsername.getText();
			final String passwordTxt = textPassword.getText();
			loginListener(usernameTxt,passwordTxt);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			final String usernameTxt = textUsername.getText();
			final String passwordTxt = textPassword.getText();
			loginListener(usernameTxt,passwordTxt);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
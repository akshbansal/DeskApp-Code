
package com.im.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.im.bo.DeviceDetailBO;
import com.im.bo.PendingRequestBO;
import com.im.bo.RosterVCardBo;
import com.im.bo.UserProfileBO;
import com.im.json.DeviceManagementJSON;
import com.im.json.SendInvitationJSON;
import com.im.json.UserProfileJSON;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.user.UserProfile;
import com.im.utils.Constants;
import com.im.utils.HospitalRelatedList;
import com.im.utils.ImageUtils;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;
import com.im.utils.XmppUtils;
import com.im.validationListeners.CheckEmailValidation;

public class AccountSettingsDialog extends JDialog {
	private JFrame parent;
	private static ResourceBundle rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
	private static final String PENDING_REQUESTS = "pendingRequest";
	private static final String PROFILE_PICTURE = "profilePicture";
	private static final String PERSONAL_INFO = "personalInfo";
	private static final String EMAIL = "email";
	private static final String DEVICE_MANAGE = "deviceManagement";
	private static final String NOTIFICATION_METHOD = "notificationMethod";
	private static final String SECURITY = "security";
	private static final String LAST_UPDATE = "lastUpdate";
	private JLabel topIcon;
	private JPanel topPanel;
	private ArrayList<PendingRequestBO> pendingrequests = null;
	final HospitalRelatedList list = new HospitalRelatedList("");
	Point point = new Point();
	boolean resizing = false;
	BufferedImage icon = null;;
	private static final String NOTIFICATION_SETTINGS = "notificationSettings";
	private static final JLabel arrow = new JLabel(new ImageIcon(((new ImageIcon(
			AccountSettingsDialog.class.getResource("/images/arrow_over.png"))).getImage()).getScaledInstance(14, 14,
			java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);
	private String emailText;
	private String firstname = "";
	private String privacyEnabled = "";
	private String lastname = "";
	private String title = "";
	private String designation = "";
	private String zip = "";
	private String phone = "";
	private String sec_ques = "";
	private String primaryHosp = "";
	private String primaryHosp_id = "";
	private String secHosp = "";
	private String secHosp_id = "";
	private String sec_ans;
	private String practice_type = "";
	private JPanel rightPanel;
	private JLabel profile;

	JDialog thisParent;
	JPanel rightBox;
	private JRadioButton radioButtonYes;
	private JRadioButton radioButtonNo;

	public AccountSettingsDialog(JFrame parent) {
		super(parent, "Account Settings");
		Constants.IS_DIALOG = true;
		this.parent = parent;
		setUndecorated(true);
		toFront();
		parent.setEnabled(false);
		setModal(true);
	        //setUndecorated(true);
	        //getRootPane().setWindowDecorationStyle(JRootPane.ERROR_DIALOG);
	    setModalityType(ModalityType.APPLICATION_MODAL);
	    parent.toBack();
		thisParent = this;
		Constants.childWindowOpened = this;
		// Constants.PARENT = parent;
		rightPanel = new JPanel();
		try {
			initAccountSettingsBox();
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
			// setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initAccountSettingsBox() {

		topIcon = new JLabel(new ImageIcon(((new ImageIcon(getClass().getResource("/images/logo.png"))).getImage()).getScaledInstance(200,
				250, java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
		topPanel = new JPanel();
		topPanel.add(topIcon, BorderLayout.CENTER);
		UserProfile profile = new UserProfile();
		UserProfileBO profileBo = profile.viewLoginUserProfile(Constants.loggedinuserInfo.login_token);
		emailText = profileBo.getEmailId();
		firstname = profileBo.getName().split(" ")[0];
		privacyEnabled = profileBo.getPrivacy_enabled();
		String[] nameArray = profileBo.getName().split(" ");

		if (nameArray.length > 1) {
			lastname = profileBo.getName().split(" ")[1];
		}

		title = profileBo.getJobTitle();
		designation = profileBo.getDesignation();
		zip = profileBo.getZip();
		phone = profileBo.getPhoneNo();
		sec_ques = profileBo.getSeq_ques();
		sec_ans = profileBo.getSeq_ans();
		practice_type = profileBo.getPractise_type();
		primaryHosp = profileBo.getPrimary_hospital();
		if (profileBo.getSecondary_hospital() != null)
			secHosp = profileBo.getSecondary_hospital().get(0).getHospital_name();
		primaryHosp_id = profileBo.getPrimary_hosp_id();
		JPanel top = TopPanel.topButtonPanelForAccountDialog(thisParent, "Accounts Settings");

		top.setMaximumSize(new Dimension(getWidth(), 40));
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 1, 2, 0));
		add(top, BorderLayout.NORTH);
		add(mainPanel(), BorderLayout.WEST);
		// setMinimumSize(new Dimension(600, 500));
		setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.width * 0.70), (int) (Constants.SCREEN_SIZE.height * 0.90)));
		parent.toFront();
		toFront();
		setLocationRelativeTo(parent);
		Constants.loader.setVisible(false);
		setTitle("Account Settings");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo_welcome.png")));

	}

	private Box mainPanel() {
		final Box mainPanel = Box.createHorizontalBox();
		mainPanel.setPreferredSize(new Dimension(getWidth(), getHeight()));
		// mainPanel.add(Box.createHorizontalStrut(2));
		mainPanel.add(LeftBox(), BorderLayout.WEST);
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				// TODO Auto-generated method stub
				mainPanel.add(rightBox(), BorderLayout.CENTER);
				return null;
			}

		};
		worker.execute();
		mainPanel.setOpaque(true);
		mainPanel.setBackground(Color.white);
		return mainPanel;
	}

	private JPanel rightBox() {

		JPanel panel = showRightPanel(PERSONAL_INFO, arrow);
		panel.setOpaque(true);
		panel.setBackground(null);
		panel.setMaximumSize(new Dimension((int) (getWidth() * (0.60)), getHeight()));
		return panel;
	}

	private Box LeftBox() {
		JPanel leftPanel = new JPanel();
		Box leftVbox = Box.createVerticalBox();
		leftVbox.add(titleLabel("Requests"));
		leftVbox.add(linkLabel("Pending Requests", true, PENDING_REQUESTS, arrow));
		leftVbox.add(titleLabel("Personal Settings"));
		leftVbox.add(linkLabel("Profile Picture", false, PROFILE_PICTURE, arrow));
		leftVbox.add(linkLabel("Personal Info", false, PERSONAL_INFO, arrow));
		leftVbox.add(linkLabel("Email Address", true, EMAIL, arrow));
		leftVbox.add(titleLabel("Security Settings"));
		leftVbox.add(linkLabel("Device Management", false, DEVICE_MANAGE, arrow));
		// leftVbox.add(linkLabel("Notification Method", false,
		// NOTIFICATION_METHOD,arrow));
		leftVbox.add(linkLabel("Security", true, SECURITY, arrow));
		// leftVbox.add(titleLabel("Other Settings"));
		// leftVbox.add(linkLabel("Last Updated Location", false,
		// LAST_UPDATE,arrow));
		// leftVbox.add(linkLabel("Notification Settings", false,
		// NOTIFICATION_SETTINGS,arrow));

		leftVbox.setOpaque(true);
		leftVbox.setBackground(null);
		leftPanel.add(leftVbox);
		leftPanel.setOpaque(true);
		leftVbox.setMaximumSize(new Dimension((int) (getWidth() * (0.40)), getHeight()));
		leftVbox.setBackground(Color.decode("#535456"));
		return leftVbox;
	}

	private Box titleLabel(String label) {
		Box box = Box.createHorizontalBox();
		JLabel jLabel = new JLabel(label);
		jLabel.setBackground(null);
		jLabel.setForeground(Color.decode("#9CCD21"));
		jLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		box.setBackground(null);
		box.setOpaque(true);
		box.add(Box.createHorizontalStrut(6));
		box.add(jLabel, BorderLayout.WEST);
		box.add(Box.createHorizontalStrut(20));
		box.setMaximumSize(new Dimension(getWidth(), getHeight() / 2));
		return box;
	}

	private Box linkLabel(final String label, boolean isLast, final String view, final JLabel arrow) {
		final Box box = Box.createHorizontalBox();
		final JLabel jLabel = new JLabel(label);
		jLabel.setBackground(null);
		jLabel.setForeground(Color.white.brighter());
		jLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		jLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		box.setBackground(null);
		box.setOpaque(true);
		box.add(Box.createHorizontalStrut(10));
		box.add(jLabel, BorderLayout.WEST);
		box.add(Box.createHorizontalStrut(20));
		box.add(arrow);
		arrow.setVisible(false);
		box.setMaximumSize(new Dimension(getWidth(), getHeight() / 2));

		if (label.equals("Pending Requests")) {
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					// task to run goes here
					// TODO Auto-generated method stub
					int count = 0;
//					if (count != Constants.pendingRequestCount)
//						count = Constants.pendingRequestCount;
//					if (count != 0) {
					jLabel.setText(label + Constants.pendingRequestCount );
//					}
					jLabel.repaint();
					jLabel.revalidate();
				}
			};
			Timer timer = new Timer();
			long delay = 0;
			long intevalPeriod = 1 * 10;
			// schedules the task to be run in an interval
			timer.scheduleAtFixedRate(task, delay, intevalPeriod);
		}

		box.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					// jLabel.setForeground(Color.red.brighter());
					box.setRequestFocusEnabled(true);
					box.setBackground(Color.decode("#9CCD21"));
					showRightPanel(view, arrow);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				try {
					box.setBackground(Color.decode("#9CCD21"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				try {
					box.setBackground(null);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				box.setBackground(null);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				box.setBackground(Color.decode("#9CCD21"));
			}
		});
		if (isLast)
			box.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		return box;
	}

	private JPanel showRightPanel(String view, JLabel arrow) {
		if (PENDING_REQUESTS.equalsIgnoreCase(view)) {
			try {
				rightBox = showPendingRequests();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// Constants.loader.setVisible(true);
			//
			// SwingWorker<JPanel , Void> worker = new SwingWorker<JPanel,
			// Void>(){
			//
			// @Override
			// protected JPanel doInBackground() throws Exception {
			// // rightBox.removeAll();
			// rightBox = showPendingRequests();
			// //rightBox.repaint();
			// Constants.loader.setVisible(false);
			// return rightBox;
			// }
			//
			// };
			// worker.execute();

		} else if (PROFILE_PICTURE.equalsIgnoreCase(view)) {
			try {
				rightBox = showProfilePicture();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (PERSONAL_INFO.equalsIgnoreCase(view)) {
			rightBox = showPersonalInfoOptions();
		} else if (EMAIL.equalsIgnoreCase(view)) {
			rightBox = showEmailOptions();
		} else if (DEVICE_MANAGE.equalsIgnoreCase(view)) {
			try {
				rightBox = showDeviceList();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (NOTIFICATION_METHOD.equalsIgnoreCase(view)) {

		} else if (SECURITY.equalsIgnoreCase(view)) {
			rightBox = showSecuritySettingsInfoOptions();
		} else if (LAST_UPDATE.equalsIgnoreCase(view)) {

		} else if (NOTIFICATION_SETTINGS.equalsIgnoreCase(view)) {
			arrow.setVisible(true);
			rightBox = new JPanel();
		} else {
			rightBox = new JPanel();
		}

		rightPanel.removeAll();
		rightPanel.add(rightBox);
		rightPanel.repaint();
		rightPanel.revalidate();
		return rightPanel;
	}

	private JPanel showEmailOptions() {
		JPanel button = new JPanel();
		Box vbBox = Box.createVerticalBox();
		final JTextField textEmail = new JTextField(20);
		final JLabel emailLabel = new JLabel("*Email: ");
		final JButton EditEmail = new JButton("Edit Email");
		final JButton UpdateEmail = new JButton("Update Email");
		EditEmail.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		UpdateEmail.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		UpdateEmail.setForeground(Color.white);
		EditEmail.setForeground(Color.white);

		BufferedImage master;
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH + "/"+"submit_btn.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			EditEmail.setIcon(new ImageIcon(scaled));
			UpdateEmail.setIcon(new ImageIcon(scaled));
			Util.setTransparentBtn(EditEmail);
			Util.setTransparentBtn(UpdateEmail);
			// final JButton reject = new JButton("Reject");
			// reject.setOpaque(true);
			// reject.setBorderPainted(false);
			// reject.setFocusPainted(false);
			// reject.setBackground(Color.decode("#9CCD21"));
			EditEmail.setForeground(Color.white.brighter());
			UpdateEmail.setForeground(Color.white.brighter());
			EditEmail.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
			UpdateEmail.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		textEmail.setBorder(null);
		textEmail.setEditable(false);
		textEmail.setOpaque(true);
		textEmail.setBackground(null);
		textEmail.setBorder(null);
		textEmail.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textEmail.setForeground(Color.gray);
		// button.setBackground(Color.decode("#9CCD21"));
		button.setOpaque(true);
		button.add(EditEmail, 0);
		UpdateEmail.setVisible(false);
		button.add(UpdateEmail, 0);
		vbBox.add(Box.createVerticalStrut(10));
		button.setOpaque(true);
		button.setBackground(null);
		topPanel.setOpaque(true);
		topPanel.setBackground(null);
		vbBox.add(topPanel, BorderLayout.NORTH);
		vbBox.add(Box.createVerticalStrut(50));
		vbBox.add(getLabelFieldEmail(emailLabel, textEmail, emailText));
		vbBox.add(Box.createVerticalStrut(10));
		vbBox.add(button);
		EditEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				textEmail.setEditable(true);
				EditEmail.setVisible(false);
				UpdateEmail.setVisible(true);
				textEmail.requestFocus();
				textEmail.addFocusListener(new CheckEmailValidation(null));
				UpdateEmail.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						String err_code = "";
						String response = "";
						JSONObject jsonObject;
						UserProfileJSON profileJSON = new UserProfileJSON();
						if (textEmail.getText().equals("")) {
							JOptionPane.showMessageDialog(null, "Email can not be blank.");
						} else {
							response = profileJSON.updateUserEmailId(Constants.loggedinuserInfo.login_token, textEmail.getText());

							try {
								jsonObject = new JSONObject(response);
								err_code = jsonObject.getString("err-code");
								if (err_code.equals("600")) {
									JOptionPane.showMessageDialog(null, "Your login session expired.Please login again.");
									Util.disposeLogoutMenu(parent);
									parent.dispose();
									thisParent.dispose();
								} else if (err_code.equals("300")) {
									JOptionPane.showMessageDialog(null, "Unable to proceed.Please try later.");
								} else if (err_code.equals("1")) {
									JOptionPane.showMessageDialog(null, "Email Updated successfully.");
									emailText = textEmail.getText();
									textEmail.repaint();
									textEmail.revalidate();

								}
							} catch (JSONException e1) {
								e1.printStackTrace();
							}
						}
					}

				});

			}
		});
		JPanel finalPanel = new JPanel();
		finalPanel.add(vbBox);
		finalPanel.setOpaque(true);
		finalPanel.setBackground(null);
		return finalPanel;
	}

	private JPanel showPersonalInfoOptions() {
		final UserProfileBO bo = new UserProfileBO();
		JPanel button = new JPanel();
		Box vbBox = Box.createVerticalBox();
		final JTextField textFirstName = new JTextField(20);
		final JTextField textLastName = new JTextField(20);
		final JTextField textTitle = new JTextField(20);
		final JTextField textDesignation = new JTextField(20);
		final JTextField textPhone = new JTextField(20);
		final JTextField textZip = new JTextField(20);
		final JTextField textPrimayHospital = new JTextField(20);
		final JTextField textSecondaryHospital = new JTextField(20);
		final JTextField textPractiseType = new JTextField(20);
		final JTextField textSecQues = new JTextField(20);
		final JTextField textSecAns = new JTextField(20);

		JLabel labelFirstname = new JLabel("<html><font style='text-align:left'>*First<br/>Name</font></html>");
		JLabel labelLastname = new JLabel("<html><font style='text-align:left'>*Last<br/>Name</font></html>");
		JLabel labelTitle = new JLabel("<html><font style='text-align:left'>Job<br/>Title</font></html>");
		JLabel labelDesignation = new JLabel("<html><font style='text-align:left'>Designation</font></html>");
		JLabel labelPracticeType = new JLabel("<html><font style='text-align:left'>*Practice<br/>Type</font></html>");
		JLabel labelPhone = new JLabel("<html><font style='text-align:left'>*Phone</font></html>");
		JLabel labelZip = new JLabel("<html><font style='text-align:left'>*Zip</font></html>");
		JLabel labelPrimaryHosp = new JLabel("<html><font style='text-align:left'>*Primary<br/>Network</font></html>");
		JLabel labelSecondaryHosp = new JLabel("<html><font style='text-align:left'>Secondary<br/>Network</font></html>");
		JLabel labelSecQues = new JLabel("<html><font style='text-align:left'>*Security<br/>Question</font></html>");
		JLabel labelSecAns = new JLabel("<html><font style='text-align:left'>*Security<br/>Answer</font></html>");

		textFirstName.setText(firstname);
		textFirstName.setEditable(false);
		textFirstName.setOpaque(true);
		textFirstName.setBackground(null);
		textFirstName.setBorder(null);
		textFirstName.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textFirstName.setForeground(Color.gray);

		textLastName.setText(lastname);
		textLastName.setEditable(false);
		textLastName.setOpaque(true);
		textLastName.setBackground(null);
		textLastName.setBorder(null);
		textLastName.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textLastName.setForeground(Color.gray);

		textTitle.setText(title.trim());
		textTitle.setEditable(false);
		textTitle.setOpaque(true);
		textTitle.setBackground(null);
		textTitle.setBorder(null);
		textTitle.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textTitle.setForeground(Color.gray);

		textDesignation.setText(designation);
		textDesignation.setEditable(false);
		textDesignation.setOpaque(true);
		textDesignation.setBackground(null);
		textDesignation.setBorder(null);
		textDesignation.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textDesignation.setForeground(Color.gray);

		textZip.setText(zip);
		textZip.setEditable(false);
		textZip.setOpaque(true);
		textZip.setBackground(null);
		textZip.setBorder(null);
		textZip.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textZip.setForeground(Color.gray);

		textPhone.setText(phone);
		textPhone.setEditable(false);
		textPhone.setOpaque(true);
		textPhone.setBackground(null);
		textPhone.setBorder(null);
		textPhone.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textPhone.setForeground(Color.gray);

		textSecQues.setText(sec_ques);
		textSecQues.setEditable(false);
		textSecQues.setOpaque(true);
		textSecQues.setBackground(null);
		textSecQues.setBorder(null);
		textSecQues.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textSecQues.setForeground(Color.gray);

		textSecAns.setText(sec_ans);
		textSecAns.setEditable(false);
		textSecAns.setOpaque(true);
		textSecAns.setBackground(null);
		textSecAns.setBorder(null);
		textSecAns.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textSecAns.setForeground(Color.gray);

		textPractiseType.setText(practice_type);
		textPractiseType.setEditable(false);
		textPractiseType.setOpaque(true);
		textPractiseType.setBackground(null);
		textPractiseType.setBorder(null);
		textPractiseType.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textPractiseType.setForeground(Color.gray);
		textPrimayHospital.setText(primaryHosp);
		textSecondaryHospital.setText(secHosp);

		textSecondaryHospital.setEditable(false);
		textSecondaryHospital.setOpaque(true);
		textSecondaryHospital.setBackground(null);
		textSecondaryHospital.setBorder(null);
		textSecondaryHospital.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textSecondaryHospital.setForeground(Color.gray);

		
		
		
		textPrimayHospital.setEditable(false);
		textPrimayHospital.setOpaque(true);
		textPrimayHospital.setBackground(null);
		textPrimayHospital.setBorder(null);
		textPrimayHospital.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textPrimayHospital.setForeground(Color.gray);

		// final Map<String, String> secondryHospIds = new HashMap<String,
		// String>();
		// new SearchField().makeSearchField(textSecNW,
		// rb.getString("http_url_login"), rb.getString("hospital_list"),
		// "hospitals_list",
		// new ISearchField() {
		//
		// @Override
		// public void onSelect(int index, String value) {
		// if(Constants.showConsole) System.out.println("Selected Id:(" + index
		// + ") " + secondryHospIds.get(value));
		// bo.setOther_hospitals(secondryHospIds.get(value));
		// }
		//
		// @Override
		// public void loopStart() {
		// secondryHospIds.clear();
		// }
		//
		// @Override
		// public String fieldValue(Map<String, String> map) {
		// String display = map.get("name") + "," + map.get("city");
		// secondryHospIds.put(display, map.get("hosp_id"));
		// return display;
		// }
		// });

		final JButton EditProfile = new JButton("Edit Profile");
		final JButton updateProfile = new JButton("Update Profile");
		updateProfile.setVisible(false);
		EditProfile.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		updateProfile.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		updateProfile.setForeground(Color.white);
		EditProfile.setForeground(Color.white);
		updateProfile.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		EditProfile.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		Util.setTransparentBtn(EditProfile);
		Util.setTransparentBtn(updateProfile);
		BufferedImage master;
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH + "/"+"submit_btn.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			EditProfile.setIcon(new ImageIcon(scaled));
			updateProfile.setIcon(new ImageIcon(scaled));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		button.add(EditProfile, 0);
		button.add(updateProfile, 0);
		button.setBackground(null);
		button.setOpaque(true);
		vbBox.add(getLabelField(labelFirstname, textFirstName, firstname));
		vbBox.add(getLabelField(labelLastname, textLastName, lastname));
		if (Constants.loggedinuserInfo.isProvider || Constants.loggedinuserInfo.isStaff) {
			vbBox.add(getLabelField(labelTitle, textTitle, title));
			vbBox.add(getLabelField(labelDesignation, textDesignation, designation));
			vbBox.add(getLabelField(labelPracticeType, textPractiseType, practice_type));
			vbBox.add(getLabelField(labelPrimaryHosp, textPrimayHospital, primaryHosp));
			vbBox.add(getLabelField(labelSecondaryHosp, textSecondaryHospital, secHosp));

			ArrayList<String> practiseTypeList = Constants.PRACTISE_LIST;
			ArrayList<String> jobTitleList = Constants.JOB_TITLE_LIST;
			ArrayList<String> designationList = Constants.DESIGNATION_LIST;

			if (null != practiseTypeList)
				fetchPractiseTypeList(textPractiseType, practiseTypeList);
			if (null != jobTitleList)
				fetchJobTitleList(textTitle, jobTitleList);
			if (null != designationList)
				fetchDesignationList(textDesignation, designationList);
		}
		vbBox.add(getLabelField(labelZip, textZip, zip));
		vbBox.add(getLabelField(labelPhone, textPhone, phone));
		vbBox.add(getLabelField(labelSecQues, textSecQues, sec_ques));
		vbBox.add(getLabelField(labelSecAns, textSecAns, sec_ans));

		final Box entreePanel = Box.createHorizontalBox();// new JPanel(new
															// GridLayout(1,
															// 2,5,2));
		final ButtonGroup entreeGroup = new ButtonGroup();
		JLabel labelPrivacyEnabled = new JLabel("<html><font style='text-align:left'>*Privacy<br/>Enabled</font></html>");
		labelPrivacyEnabled.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		entreePanel.add(labelPrivacyEnabled, BorderLayout.WEST);

		radioButtonYes = new JRadioButton("Yes");
		radioButtonNo = new JRadioButton("No");
		entreeGroup.add(radioButtonYes);
		entreeGroup.add(radioButtonNo);

		radioButtonYes.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		radioButtonNo.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		radioButtonYes.setForeground(Color.gray);
		radioButtonNo.setForeground(Color.gray);
		if (privacyEnabled.equals("1")) {
			radioButtonYes.setSelected(true);
		} else {
			radioButtonNo.setSelected(true);
		}
		entreePanel.setBackground(Color.white);
		JPanel panel = new JPanel(new GridLayout(0, 2));
		panel.add(radioButtonYes);
		panel.add(radioButtonNo);
		radioButtonYes.setOpaque(true);
		radioButtonYes.setBackground(Color.white);
		radioButtonYes.setEnabled(false);
		radioButtonNo.setOpaque(true);
		radioButtonNo.setBackground(Color.white);
		radioButtonNo.setEnabled(false);
		panel.setOpaque(true);
		panel.setBackground(null);
		entreePanel.add(Box.createHorizontalStrut(50));
		entreePanel.add(panel);
		final JPanel entreBoxPanel = new JPanel(new BorderLayout());
		entreBoxPanel.add(entreePanel);
		entreBoxPanel.setOpaque(true);
		entreBoxPanel.setBackground(null);
		// panelfinal.add(panel);

		entreBoxPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.35), 60));
		entreBoxPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray));
		entreBoxPanel.setEnabled(false);
		vbBox.add(Box.createVerticalStrut(20));
		vbBox.add(entreePanel);
		vbBox.add(Box.createVerticalStrut(20));

		// vbBox.add(button, BorderLayout.SOUTH);
		final Map<String, String> primaryHospIds = new HashMap<String, String>();
		new SearchField().makeSearchField(textPrimayHospital, rb.getString("http_url_login"), rb.getString("hospital_list"), "hospitals_list",
			new ISearchField() {
				
				@Override
				public void onSelect(int index, String value) {
					if(Constants.showConsole) System.out.println("Selected Id-------:(" + index + ") " + primaryHospIds.get(value));
					bo.setPrimary_hosp_id(primaryHospIds.get(value));
				}
				
				@Override
				public void loopStart() {
					primaryHospIds.clear();
				}
				
				@Override
				public String fieldValue(Map<String, String> map) {
					String display = map.get("name") + "," + map.get("city");
					primaryHospIds.put(display, map.get("hosp_id"));
					primaryHosp = display;
					return display;
				}
			});
		
		final Map<String, String> secondryHospIds = new HashMap<String, String>();
		new SearchField().makeSearchField(textSecondaryHospital, rb.getString("http_url_login"), rb.getString("hospital_list"), "hospitals_list",
			new ISearchField() {
				
				@Override
				public void onSelect(int index, String value) {
					if(Constants.showConsole) System.out.println("Selected Id:(" + index + ") " + secondryHospIds.get(value));
					bo.setSec_hospital_selected(secondryHospIds.get(value));
				}
				
				@Override
				public void loopStart() {
					secondryHospIds.clear();
				}
				
				@Override
				public String fieldValue(Map<String, String> map) {
					String display = map.get("name") + "," + map.get("city");
					secondryHospIds.put(display, map.get("hosp_id"));
					secHosp = display;
					return display;
				}
			});
		EditProfile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textFirstName.setEditable(true);
				textFirstName.requestFocus();
				textLastName.setEditable(true);
				textTitle.setEditable(true);
				textDesignation.setEditable(true);
				textPractiseType.setEditable(true);
				textZip.setEditable(true);
				textPrimayHospital.setEditable(true);
				textPhone.setEditable(true);
				textSecQues.setEditable(true);
				textSecAns.setEditable(true);
				EditProfile.setVisible(false);
				updateProfile.setVisible(true);
				radioButtonYes.setEnabled(true);
				radioButtonNo.setEnabled(true);
				textPrimayHospital.setEditable(true);
				textSecondaryHospital.setEditable(true);
				
			}
		});
		updateProfile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Constants.loggedinuserInfo.isProvider || Constants.loggedinuserInfo.isStaff) {
					if (checkEmpty(textFirstName, "First Name is required") && checkEmpty(textLastName, "Last Name is required")
							&& checkEmpty(textPhone, "Phone number is required") && checkEmpty(textZip, "Zip Code is required")
							&& checkEmpty(textSecQues, "Security Question is required")
							&& checkEmpty(textSecAns, "Security Answer is required")
							&& checkEmpty(textPractiseType, "Practice Type is required")) {

						if (checkInt(textPhone, "Phone", 10) && checkInt(textZip, "Zip Code", 5)) {

							
							bo.setName(textFirstName.getText() + " " + textLastName.getText());
							bo.setDesignation(textDesignation.getText());
							bo.setJobTitle(textTitle.getText());
							bo.setPractise_type(textPractiseType.getText());
							bo.setZip(textZip.getText());
							bo.setPhoneNo(textPhone.getText());
							bo.setSeq_ques(textSecQues.getText());
							bo.setSeq_ans(textSecAns.getText());

							if (radioButtonYes.isSelected()) {
								bo.setPrivacy_enabled("1");
							} else if (radioButtonNo.isSelected()) {
								bo.setPrivacy_enabled("0");
							}
							
							// bo.setPrimary_hospital(primary_hospital);
							firstname = textFirstName.getText();
							lastname = textLastName.getText();
							title = textTitle.getText();
							designation = textDesignation.getText();
							zip = textZip.getText();
							phone = textPhone.getText();
							sec_ques = textSecQues.getText();
							sec_ans = textSecAns.getText();
							practice_type = textPractiseType.getText();
							if (radioButtonYes.isSelected()) {
								privacyEnabled = "1";
								radioButtonYes.setSelected(true);
							} else if (radioButtonNo.isSelected()) {
								privacyEnabled = "0";
								radioButtonNo.setSelected(true);
							}
							textFirstName.repaint();
							textFirstName.revalidate();

							textLastName.repaint();
							textLastName.revalidate();

							textTitle.repaint();
							textTitle.revalidate();

							textDesignation.repaint();
							textDesignation.revalidate();

							textZip.repaint();
							textZip.revalidate();

							textPhone.repaint();
							textPhone.revalidate();

							textSecQues.repaint();
							textSecQues.revalidate();

							textSecAns.repaint();
							textSecAns.revalidate();

							textPractiseType.repaint();
							textPractiseType.revalidate();

							textPrimayHospital.repaint();
							textPrimayHospital.revalidate();
							SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

								@Override
								protected Void doInBackground() throws Exception {
									rightPanel.removeAll();
									JPanel loaderPanel = Util.loaderPanel("Updating please wait..");
									rightPanel.add(loaderPanel,BorderLayout.CENTER);
									rightPanel.revalidate();
									rightPanel.repaint();

									
									String err_code = "";
									String response = "";
									JSONObject jsonObject;
									UserProfileJSON profileJSON = new UserProfileJSON();
									response = profileJSON.updateLoginUserProfile(Constants.loggedinuserInfo.login_token, bo);

									try {
										jsonObject = new JSONObject(response);
										err_code = jsonObject.getString("err-code");
										if (err_code.equals("600")) {
											JOptionPane.showMessageDialog(thisParent, "Your login session expired.Please login again.");
											Util.disposeLogoutMenu(parent);
											parent.dispose();
											thisParent.dispose();
										} else if (err_code.equals("300")) {
											JOptionPane.showMessageDialog(thisParent, "Unable to proceed.Please try later.");
										} else if (err_code.equals("1")) {

											// dispose();
											// removeAll();
											// initAccountSettingsBox();
											JOptionPane.showMessageDialog(thisParent, "User Profile Updated successfully.");
											Constants.loggedinuserInfo.name = firstname +" "+lastname;
											WelcomeUtils.profile.setText("<html><center>Welcome!<br/>" + Constants.loggedinuserInfo.name + "</html>");
											WelcomeUtils.welcomeBox().repaint();
											WelcomeUtils.welcomeBox().revalidate();
											rightPanel.removeAll();
											rightPanel.add(showPersonalInfoOptions());
											rightPanel.revalidate();
											rightPanel.repaint();
											

										}
									} catch (JSONException e1) {
										e1.printStackTrace();
									}
									return null;

								}
							};
							worker.execute();

						}
					}
				} else {
					if (checkEmpty(textFirstName, "First Name is required") && checkEmpty(textLastName, "Last Name is required")
							&& checkEmpty(textPhone, "Phone number is required") && checkEmpty(textZip, "Zip Code is required")
							&& checkEmpty(textSecQues, "Security Question is required")
							&& checkEmpty(textSecAns, "Security Answer is required")) {

								if (checkInt(textPhone, "Phone", 10) && checkInt(textZip, "Zip Code", 5)) {

									final UserProfileBO bo = new UserProfileBO();
									bo.setName(textFirstName.getText() + " " + textLastName.getText());
									bo.setDesignation(textDesignation.getText());
									bo.setJobTitle(textTitle.getText());
									bo.setPractise_type(textPractiseType.getText());
									bo.setZip(textZip.getText());
									bo.setPhoneNo(textPhone.getText());
									bo.setSeq_ques(textSecQues.getText());
									bo.setSeq_ans(textSecAns.getText());
									ButtonModel model = (ButtonModel) entreeGroup.getSelection();
									if (Constants.showConsole)
										System.out.println(model.getActionCommand());
									if (radioButtonYes.isSelected()) {
										bo.setPrivacy_enabled("1");
									} else if (radioButtonNo.isSelected()) {
										bo.setPrivacy_enabled("0");
									}
									// bo.setPrimary_hospital(primary_hospital);
									firstname = textFirstName.getText();
									lastname = textLastName.getText();
									title = textTitle.getText();
									designation = textDesignation.getText();
									zip = textZip.getText();
									phone = textPhone.getText();
									sec_ques = textSecQues.getText();
									sec_ans = textSecAns.getText();
									practice_type = textPractiseType.getText();
									if (radioButtonYes.isSelected()) {
										privacyEnabled = "1";
										radioButtonYes.setSelected(true);
									} else if (radioButtonNo.isSelected()) {
										privacyEnabled = "0";
										radioButtonNo.setSelected(true);
									}
									textFirstName.repaint();
									textFirstName.revalidate();

									textLastName.repaint();
									textLastName.revalidate();

									textTitle.repaint();
									textTitle.revalidate();

									textDesignation.repaint();
									textDesignation.revalidate();

									textZip.repaint();
									textZip.revalidate();

									textPhone.repaint();
									textPhone.revalidate();

									textSecQues.repaint();
									textSecQues.revalidate();

									textSecAns.repaint();
									textSecAns.revalidate();

									textPractiseType.repaint();
									textPractiseType.revalidate();
									Constants.loader.setVisible(true);
									SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

										@Override
										protected Void doInBackground() throws Exception {
											String err_code = "";
											String response = "";
											JSONObject jsonObject;
											UserProfileJSON profileJSON = new UserProfileJSON();
											response = profileJSON.updateLoginUserProfile(Constants.loggedinuserInfo.login_token, bo);

											try {
												jsonObject = new JSONObject(response);
												err_code = jsonObject.getString("err-code");
												if (err_code.equals("600")) {
													Constants.loader.setVisible(false);
													JOptionPane.showMessageDialog(null, "Your login session expired.Please login again.");
													Util.disposeLogoutMenu(parent);
													parent.dispose();
													thisParent.dispose();
												} else if (err_code.equals("300")) {
													Constants.loader.setVisible(false);
													JOptionPane.showMessageDialog(null, "Unable to proceed.Please try later.");
												} else if (err_code.equals("1")) {

													// dispose();
													// removeAll();
													// initAccountSettingsBox();
													Constants.loader.setVisible(false);
													JOptionPane.showMessageDialog(null, "User Profile Updated successfully.");

												}
											} catch (JSONException e1) {
												e1.printStackTrace();
											}
											return null;

										}
									};
									worker.execute();

								}
					}
				}

			}
		});
		// firstNamePanel.setPreferredSize(new Dimension(500,30));

		JPanel finalPanel = new JPanel(new GridLayout(0, 1, 2, 2));
		JPanel panelBox = new JPanel();
		panelBox.add(vbBox);
		panelBox.setOpaque(true);
		panelBox.setBackground(Color.white);
		LightScrollPane sc = new LightScrollPane(panelBox);
		sc.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.40), (int) (Constants.SCREEN_SIZE.getHeight() * 0.70)));
		sc.setOpaque(true);
		sc.setBackground(Color.white);
		finalPanel.add(sc);
		finalPanel.add(button);

		finalPanel.setOpaque(true);
		finalPanel.setBackground(null);
		return finalPanel;
	}

	private JPanel showSecuritySettingsInfoOptions() {
		JPanel finalPanel = AccountSettings.getInstance().showAccountSetting(sec_ques, sec_ans, thisParent);
		return finalPanel;
	}

	private JPanel showProfilePicture() throws MalformedURLException, IOException {
		final Box vbBox = Box.createVerticalBox();
		final JPanel profilePicPanel = new JPanel();
		icon = Util.getProfileImg(Constants.loggedinuserInfo.username);
		// BufferedImage rounded = Util.makeRoundedCorner(icon, 100);
		if (icon == null) {
			icon = ImageIO.read(getClass().getResource("/images/default_pp.png"));
		}

		profile = Util.combine(icon, false, 200, 200);
		profilePicPanel.add(profile, BorderLayout.CENTER);
		profilePicPanel.setOpaque(true);
		profilePicPanel.setBackground(null);
		Box selectPictureButtonPanel = Box.createHorizontalBox();
		JButton SelectButton = new JButton("Select Photo");
		final JButton UpdatePicture = new JButton("Update Picture");

		SelectButton.setForeground(Color.white);
		BufferedImage master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH + "/"+"submit_btn.png"));
		Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
		SelectButton.setIcon(new ImageIcon(scaled));
		UpdatePicture.setIcon(new ImageIcon(scaled));
		UpdatePicture.setForeground(Color.white);
		UpdatePicture.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		SelectButton.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		UpdatePicture.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		SelectButton.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		Util.setTransparentBtn(SelectButton);
		Util.setTransparentBtn(UpdatePicture);
		selectPictureButtonPanel.add(SelectButton);
		UpdatePicture.setVisible(false);
		selectPictureButtonPanel.add(Box.createHorizontalStrut(20));
		selectPictureButtonPanel.add(UpdatePicture);
		selectPictureButtonPanel.setOpaque(true);
		selectPictureButtonPanel.setBackground(null);
		// selectPictureButtonPanel.setMaximumSize(new Dimension(200, 50));

		

		vbBox.add(profilePicPanel);
		vbBox.add(Box.createVerticalStrut(20));
		vbBox.add(selectPictureButtonPanel);
		vbBox.setPreferredSize(new Dimension((int) (getWidth() * 0.60), (int) (getHeight() * 0.50)));
		final JPanel finalPanel = new JPanel();
		finalPanel.add(vbBox);
		finalPanel.setOpaque(true);
		finalPanel.setBackground(null);
		
		
		SelectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				// For Directory
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				// For File
				// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int rVal = fileChooser.showOpenDialog(getRootPane());
				if (rVal == JFileChooser.APPROVE_OPTION) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							try {

								File file = fileChooser.getSelectedFile();
								icon = ImageIO.read(file);
								profile = new JLabel(new ImageIcon(((new ImageIcon(icon)).getImage()).getScaledInstance(200, 200,
										java.awt.Image.SCALE_FAST)), JLabel.CENTER);
								profile.repaint();
								profile.revalidate();
								profilePicPanel.removeAll();
								profilePicPanel.add(profile);
								profilePicPanel.repaint();
								profilePicPanel.revalidate();
								UpdatePicture.setVisible(true);
								
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});
				}
			}
		});
		UpdatePicture.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// update vcard of login user
				
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						rightPanel.removeAll();
						JPanel loaderPanel = Util.loaderPanel("Updating please wait..");
						rightPanel.add(loaderPanel,BorderLayout.CENTER);
						rightPanel.repaint();
						rightPanel.revalidate();

						
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						try {
							BufferedImage img;
							if (icon.getWidth() > 150 && icon.getHeight() > 150) {
								img = ImageUtils.resizeImage(icon, ImageUtils.IMAGE_JPEG, 150, 150);
								ImageIO.write(img, "jpg", baos);
							} else {
								ImageIO.write(icon, "jpg", baos);
							}
							byte[] imageInByte = baos.toByteArray();
							boolean uploadImage = XmppUtils.setProfilePicture(imageInByte);
							if (uploadImage == true) {
								JOptionPane.showMessageDialog(getParent(), "Profile picture updated successfully");
								Util.setProfileImg(Constants.loggedinuserInfo.username, icon);
								rightPanel.removeAll();
								rightPanel.add(finalPanel,BorderLayout.CENTER);
								rightPanel.repaint();
								rightPanel.revalidate();
							
//								profile = Util.combine(icon, false, 100, 100);
//								profile.repaint();
//								profile.revalidate();
								BufferedImage newProfileImg = Util
										.getProfileImg(Constants.loggedinuserInfo.username);
								// WelcomeUtils.loginUserProfilePic
								// = newIcon;
								BufferedImage imgCenter = Util.combine2(icon, false, 200, 200);
								WelcomeUtils.loginUserProfilePic.setIcon(new ImageIcon(imgCenter));
								WelcomeUtils.loginUserProfilePic.repaint();
								WelcomeUtils.loginUserProfilePic.revalidate();
								BufferedImage imgTop = Util.combine2(icon, true, 60, 60);
								UserWelcomeScreen.profilePic.setIcon(new ImageIcon(imgTop));
								UserWelcomeScreen.profilePic.repaint();
								UserWelcomeScreen.profilePic.revalidate();
							} else {
								JOptionPane.showMessageDialog(getParent(),
										"Problem in uploading picture Please try again");
								
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						return null;
					}

				};
				worker.execute();

			}
		});
		
		return finalPanel;
	}

	private JPanel pendingRequestPanel() throws IOException, JSONException {
		JPanel panel = new JPanel();
		pendingrequests = list.getPendingRequestsResponse();
		Box box = Box.createVerticalBox();
		if (!pendingrequests.isEmpty()) {
			for (final PendingRequestBO bo : pendingrequests) {
				Box vbox = Box.createVerticalBox();
				final Box hbox = Box.createHorizontalBox();
				final Box hbox1 = Box.createHorizontalBox();
				final JPanel username = new JPanel();
				final JLabel labelusername = new JLabel(bo.getUser_name());
				final JLabel profile;
				JPanel profilePanel = new JPanel();
				JLabel labelstatus = new JLabel(bo.getStatus());
				final JButton accept = new JButton("Accept");
				final JButton reject = new JButton("Decline");
				final JButton flagged = new JButton("Flagged");
				BufferedImage master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH + "/"+"submit_btn.png"));
				Image scaled = master.getScaledInstance(100, 50, java.awt.Image.SCALE_SMOOTH);
				accept.setIcon(new ImageIcon(scaled));
				reject.setIcon(new ImageIcon(scaled));
				flagged.setIcon(new ImageIcon(scaled));
				Util.setTransparentBtn(accept);
				Util.setTransparentBtn(reject);
				Util.setTransparentBtn(flagged);
				// final JButton reject = new JButton("Reject");
				// reject.setOpaque(true);
				// reject.setBorderPainted(false);
				// reject.setFocusPainted(false);
				// reject.setBackground(Color.decode("#9CCD21"));
				accept.setForeground(Color.white.brighter());
				flagged.setForeground(Color.white.brighter());
				reject.setForeground(Color.white.brighter());
				accept.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
				flagged.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
				reject.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
				accept.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				flagged.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				reject.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				labelusername.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				accept.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						Constants.loader.setVisible(true);
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

							@Override
							protected Void doInBackground() throws Exception {

								if (bo.getFlagged() == true) {
									try {
										AcceptRequest(bo.getUser_name(), "approved");

									} catch (JSONException e) {
										e.printStackTrace();
									}
								} else if (bo.getFlagged() == false) {
									AcceptRequest(bo.getUser_name(), "approved");
								}
								return null;
							}

						};
						worker.execute();
					}
				});
				flagged.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						Constants.loader.setVisible(true);
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

							@Override
							protected Void doInBackground() throws Exception {

								if (bo.getFlagged() == true) {
									try {
										AcceptRequest(bo.getUser_name(), "flagged");
										Constants.loader.setVisible(false);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								return null;
							}

						};
						worker.execute();
					}
				});
				reject.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							if (bo.getFlagged() == true) {
								if (!hbox.isVisible()) {
									hbox.setVisible(true);
								} else {
									hbox.setVisible(false);
								}
							} else if (bo.getFlagged() == false) {
								AcceptRequest(bo.getUser_name(), "declined");
							}

							/*
							 * reject.setVisible(false);
							 * accept.setText("Accept again");
							 */
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				BufferedImage icon = Util.getProfileImg(bo.getUser_name());
				if (icon == null) {
					icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));

				}
				profile = Util.combine(icon, false, 50, 50);
				profilePanel.add(profile);
				profilePanel.setOpaque(true);
				profilePanel.setBackground(Color.white);
				username.add(labelusername);
				username.setOpaque(true);
				username.setBackground(Color.white);
				JPanel panelhbox = new JPanel();
				panelhbox.setOpaque(true);
				panelhbox.setBackground(Color.white);

				panelhbox.add(profile);
				panelhbox.add(labelusername);
				panelhbox.add(hbox1);
				panelhbox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				panelhbox.setPreferredSize(new Dimension((int) (getWidth() * 0.60), 80));
				hbox1.setOpaque(true);
				hbox.setOpaque(true);
				hbox.setBackground(Color.white);
				hbox.add(accept);
				hbox.add(reject);
				if (bo.getFlagged() == true) {
					hbox.add(flagged);
				}
				vbox.setOpaque(true);
				vbox.add(panelhbox, BorderLayout.WEST);
				vbox.add(hbox, BorderLayout.EAST);
				hbox.setVisible(false);

				panel.setOpaque(true);
				panel.setBackground(null);
				panel.add(vbox, BorderLayout.WEST);
				vbox.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (!hbox.isVisible()) {
							hbox.setVisible(true);
						} else {
							hbox.setVisible(false);
						}
					}
				});
				box.add(vbox, BorderLayout.WEST);
			}
		} else {
			JPanel panelNoUser = new JPanel();
			JLabel labelNoUser = new JLabel("No Pending Request");
			labelNoUser.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
			panelNoUser.add(labelNoUser);
			panelNoUser.setOpaque(true);
			panelNoUser.setBackground(null);
			// vbBox.remove(1);
			box.add(panelNoUser);
		}
		panel.setOpaque(true);
		// vbBox.remove(1);
		panel.setBackground(Color.white);
		JPanel finalPanel = new JPanel();
		finalPanel.add(box, BorderLayout.WEST);
		finalPanel.setOpaque(true);
		finalPanel.setBackground(Color.white);
		return finalPanel;
	}

	private JPanel declinedRequestPanel() throws IOException, JSONException {
		JPanel panel = new JPanel();
		pendingrequests = list.getDeclinedRequestsResponse();
		Box box = Box.createVerticalBox();
		if (!pendingrequests.isEmpty()) {
			for (final PendingRequestBO bo : pendingrequests) {
				Box vbox = Box.createVerticalBox();
				final Box hbox = Box.createHorizontalBox();
				final Box hbox1 = Box.createHorizontalBox();
				final JPanel username = new JPanel();
				final JLabel labelusername = new JLabel(bo.getUser_name());
				final JLabel profile;
				JPanel profilePanel = new JPanel();
				JLabel labelstatus = new JLabel(bo.getStatus());
				final JButton accept = new JButton("Accept");
				final JButton reject = new JButton("Decline");
				final JButton flagged = new JButton("Flagged");
				BufferedImage master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH + "/"+"submit_btn.png"));
				Image scaled = master.getScaledInstance(100, 50, java.awt.Image.SCALE_SMOOTH);
				accept.setIcon(new ImageIcon(scaled));
				reject.setIcon(new ImageIcon(scaled));
				flagged.setIcon(new ImageIcon(scaled));
				Util.setTransparentBtn(accept);
				Util.setTransparentBtn(reject);
				Util.setTransparentBtn(flagged);
				// final JButton reject = new JButton("Reject");
				// reject.setOpaque(true);
				// reject.setBorderPainted(false);
				// reject.setFocusPainted(false);
				// reject.setBackground(Color.decode("#9CCD21"));
				accept.setForeground(Color.white.brighter());
				flagged.setForeground(Color.white.brighter());
				reject.setForeground(Color.white.brighter());
				accept.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
				flagged.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
				reject.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
				accept.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				flagged.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				reject.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				labelusername.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				accept.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
					//	Constants.loader.setVisible(true);
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

							@Override
							protected Void doInBackground() throws Exception {

								if (bo.getFlagged() == true) {
									try {
										AcceptRequest(bo.getUser_name(), "approved");
									//	Constants.loader.setVisible(false);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								} else if (bo.getFlagged() == false) {
									AcceptRequest(bo.getUser_name(), "approved");
									//Constants.loader.setVisible(false);
								}
								return null;
							}

						};
						worker.execute();
					}
				});
				flagged.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						//Constants.loader.setVisible(true);
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

							@Override
							protected Void doInBackground() throws Exception {

								if (bo.getFlagged() == true) {
									try {
										AcceptRequest(bo.getUser_name(), "flagged");
										//Constants.loader.setVisible(false);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								return null;
							}

						};
						worker.execute();
					}
				});
				reject.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							if (bo.getFlagged() == true) {
								if (!hbox.isVisible()) {
									hbox.setVisible(true);
								} else {
									hbox.setVisible(false);
								}
							} else {
								AcceptRequest(bo.getUser_name(), "declined");
								//Constants.loader.setVisible(false);

							}
							/*
							 * reject.setVisible(false);
							 * accept.setText("Accept again");
							 */
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				BufferedImage icon = Util.getProfileImg(bo.getUser_name());
				if (icon == null) {
					icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));

				}
				profile = Util.combine(icon, false, 50, 50);
				profilePanel.add(profile);
				profilePanel.setOpaque(true);
				profilePanel.setBackground(Color.white);
				username.add(labelusername);
				username.setOpaque(true);
				username.setBackground(Color.white);
				JPanel panelhbox = new JPanel();
				panelhbox.setOpaque(true);
				panelhbox.setBackground(Color.white);

				panelhbox.add(profile);
				panelhbox.add(labelusername);
				panelhbox.add(hbox1);
				panelhbox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
				panelhbox.setPreferredSize(new Dimension((int) (getWidth() * 0.60), 80));
				hbox1.setOpaque(true);
				hbox.setOpaque(true);
				hbox.setBackground(Color.white);
				hbox.add(accept);
				hbox.add(reject);
				if (bo.getFlagged() == true) {
					hbox.add(flagged);
				}
				vbox.setOpaque(true);
				vbox.add(panelhbox, BorderLayout.WEST);
				vbox.add(hbox, BorderLayout.EAST);
				hbox.setVisible(false);

				panel.setOpaque(true);
				panel.setBackground(null);
				panel.add(vbox, BorderLayout.WEST);
				vbox.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (!hbox.isVisible()) {
							hbox.setVisible(true);
						} else {
							hbox.setVisible(false);
						}
					}
				});
				box.add(vbox, BorderLayout.WEST);
			}
		} else {
			JPanel panelNoUser = new JPanel();
			JLabel labelNoUser = new JLabel("No Declined Request");
			labelNoUser.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
			panelNoUser.add(labelNoUser);
			panelNoUser.setOpaque(true);
			panelNoUser.setBackground(null);
			// vbBox.remove(1);
			box.add(panelNoUser);
		}
		panel.setOpaque(true);
		// vbBox.remove(1);
		panel.setBackground(Color.white);
		JPanel finalPanel = new JPanel();
		finalPanel.add(box, BorderLayout.WEST);
		finalPanel.setOpaque(true);
		finalPanel.setBackground(Color.white);
		return finalPanel;
	}

	private JPanel showPendingRequests() throws IOException {
		final Box vbBox = Box.createVerticalBox();

		try {

			final JButton pending = new JButton("Pending");
			final JButton decline = new JButton("Declined");

			pending.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
			decline.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
			BufferedImage master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH + "/"+"submit_over.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			pending.setIcon(new ImageIcon(scaled));
			decline.setIcon(new ImageIcon(scaled));
			decline.setForeground(Color.decode("#9CCD21"));
			Util.setTransparentBtn(pending);
			Util.setTransparentBtn(decline);
			pending.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
			decline.setHorizontalTextPosition(SwingConstants.HORIZONTAL);

			JPanel panelPending = pendingRequestPanel();
			panelPending.setOpaque(true);
			panelPending.setBackground(Color.white);
			JPanel panelDecline = declinedRequestPanel();
			panelDecline.setOpaque(true);
			panelDecline.setBackground(Color.white);
			final LightScrollPane sc = new LightScrollPane(panelPending);
			sc.setOpaque(true);
			sc.setBackground(Color.white);
			final LightScrollPane sc1 = new LightScrollPane(panelDecline);
			sc.setVisible(true);
			sc1.setOpaque(true);
			sc1.setBackground(Color.white);
			sc1.setVisible(false);
			pending.setSelected(true);
			try {
				master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH + "/"+"submit_btn.png"));
				Image scaled2 = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
				pending.setSelectedIcon(new ImageIcon(scaled2));
				pending.setForeground(Color.white);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			pending.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					decline.setSelected(false);
					pending.setSelected(true);
					decline.setForeground(Color.decode("#9CCD21"));
					pending.setForeground(Color.white.brighter());

					sc.setVisible(true);
					sc1.setVisible(false);

				}
			});

			decline.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					decline.setSelected(true);
					pending.setSelected(false);
					BufferedImage master;
					try {
						master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH + "/"+"submit_btn.png"));
						Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
						decline.setSelectedIcon(new ImageIcon(scaled));
						pending.setForeground(Color.decode("#9CCD21"));
						decline.setForeground(Color.white.brighter());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					sc.setVisible(false);
					sc1.setVisible(true);
				}
			});
			JPanel buttonPanel = new JPanel();
			buttonPanel.add(pending);
			buttonPanel.add(decline);
			buttonPanel.setOpaque(true);
			buttonPanel.setBackground(null);
			topIcon = new JLabel(new ImageIcon(((new ImageIcon(getClass().getResource("/images/logo.png"))).getImage()).getScaledInstance(
					200, 250, java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
			topPanel = new JPanel();
			topPanel.add(topIcon, BorderLayout.CENTER);
			topPanel.setOpaque(true);
			topPanel.setBackground(null);
			vbBox.add(topPanel);
			vbBox.add(Box.createVerticalStrut(5));
			vbBox.add(buttonPanel);
			sc1.setPreferredSize(new Dimension((int) (getWidth() * 0.60), (int) (getHeight() * 0.50)));
			sc.setPreferredSize(new Dimension((int) (getWidth() * 0.60), (int) (getHeight() * 0.50)));
			vbBox.add(sc, BorderLayout.CENTER);
			vbBox.add(sc1, BorderLayout.CENTER);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// vbBox.setPreferredSize(new Dimension((int) (getWidth() * 0.60), (int)
		// (getHeight() * 0.50)));
		JPanel finalPanel = new JPanel();
		finalPanel.removeAll();
		finalPanel.add(vbBox);
		finalPanel.repaint();
		finalPanel.setOpaque(true);
		finalPanel.setBackground(null);
		return finalPanel;
	}

	private JPanel showDeviceList() throws IOException {
		try {
			int count = 1;
			topIcon = new JLabel(new ImageIcon(((new ImageIcon(getClass().getResource("/images/logo.png"))).getImage()).getScaledInstance(
					200, 250, java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
			topPanel = new JPanel();
			topPanel.add(topIcon, BorderLayout.CENTER);
			topPanel.setOpaque(true);
			topPanel.setBackground(null);

			Box vbox = Box.createVerticalBox();
			List<DeviceDetailBO> deviceList = new HospitalRelatedList("").getDeviceList();
			/*
			 * data = new Object[deviceList.size()][9]; for (int i = 0; i <
			 * deviceList.size(); i++) { DeviceDetailBO deviceBo =
			 * deviceList.get(i); data[i][0] = i + 1; data[i][1] =
			 * deviceBo.getDevice_id(); data[i][2] = deviceBo.getDevice_name();
			 * data[i][3] = deviceBo.getStatus();
			 */

			if (!deviceList.isEmpty()) {
				for (final DeviceDetailBO deviceBo : deviceList) {
					if (deviceBo.getStatus().equalsIgnoreCase("active")) {
						final Box hbox = Box.createHorizontalBox();
						final JPanel deviceNamePanel = new JPanel(new GridLayout());
						String deviceName = deviceBo.getDevice_name();
						String labelText = String.format("<html><div style=\"width:%dpx;margin:5px;\">%s</div><br><html>", 200, deviceName);
						final JLabel labelDeviceName = new JLabel(deviceName);
						labelDeviceName.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
						final JLabel labelCount = new JLabel(String.valueOf(count));
						labelCount.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
						hbox.add(labelCount);
						hbox.add(Box.createHorizontalStrut(10));
						hbox.add(labelDeviceName);
						// hbox.setPreferredSize(new Dimension(200, 50));
						deviceNamePanel.setOpaque(true);
						deviceNamePanel.add(hbox, BorderLayout.WEST);
						deviceNamePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray));
						deviceNamePanel.setBackground(Color.white);
						vbox.add(deviceNamePanel, BorderLayout.WEST);
						labelDeviceName.setForeground(Color.decode("#9CCD21"));
						labelCount.setForeground(Color.decode("#9CCD21"));
						deviceNamePanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
						deviceNamePanel.addMouseListener(new MouseAdapter() {

							@Override
							public void mouseExited(MouseEvent e) {
								deviceNamePanel.setBackground(Color.WHITE);
								labelDeviceName.setForeground(Color.decode("#9CCD21"));
								labelCount.setForeground(Color.decode("#9CCD21"));
							}

							@Override
							public void mouseEntered(MouseEvent e) {
								labelDeviceName.setForeground(Color.white);
								labelCount.setForeground(Color.white);
								deviceNamePanel.setBackground(Color.decode("#9CCD21"));
							}

							@Override
							public void mouseClicked(MouseEvent e) {
								int status = 0;
								if (deviceBo.getStatus().equalsIgnoreCase("active")) {
									status = JOptionPane.showConfirmDialog(null, "Do you really want to block this device?",
											"Confirm Block", JOptionPane.YES_NO_OPTION);
								} else {
									status = JOptionPane.showConfirmDialog(null, "Do you really want to unblock this device?",
											"Confirm Unblock", JOptionPane.YES_NO_OPTION);
								}

								if (status == JOptionPane.YES_OPTION) {
									DeviceManagementJSON json = new DeviceManagementJSON();
									String response = json.getDeviceManagement(deviceBo.getDevice_id(), deviceBo.getStatus());
									try {
										JSONObject obj = new JSONObject(response);
										String err_code = obj.getString("err-code");
										if (err_code.equals("600")) {
											JOptionPane.showMessageDialog(thisParent, "Your login session expired.Please login again.");
											Util.disposeLogoutMenu(parent);
											parent.dispose();
											thisParent.dispose();
										} else if (err_code.equals("300")) {
											JOptionPane.showMessageDialog(thisParent, obj.getString("message"));
										} else if (err_code.equals("1")) {
											JOptionPane.showMessageDialog(thisParent, obj.getString("message"));
											JPanel panel = showRightPanel(DEVICE_MANAGE, arrow);
											panel.repaint();
										}
									} catch (JSONException e1) {
										e1.printStackTrace();
									}

								}
							}
						});
						count++;
					}
				}
			}
			LightScrollPane sc = new LightScrollPane(vbox);
			sc.setPreferredSize(new Dimension(500, 100));
			JPanel finalPanel = new JPanel(new GridLayout(0, 1));
			finalPanel.add(topPanel, BorderLayout.NORTH);
			finalPanel.add(sc);
			finalPanel.setOpaque(true);
			finalPanel.setBackground(null);
			return finalPanel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private JPanel getLabelField(JLabel label, JTextField textField, String value) {
		Box panel = Box.createHorizontalBox();
		JPanel panelfinal = new JPanel(new GridLayout(1,2,1,1));
		label.setForeground(Color.black);
		label.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textField.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		textField.setOpaque(true);
		textField.setForeground(Color.gray);
		textField.setText(value);
		textField.setCaretPosition(0);
		textField.setHorizontalAlignment(JTextField.LEADING);
		// textField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		// textPanel.setPreferredSize(new Dimension((int)
		// (Constants.SCREEN_SIZE.getWidth()*0.20),50));
		panelfinal.add(label);
		panelfinal.add(textField);
		panelfinal.setOpaque(true);
		panelfinal.setBackground(null);
		panelfinal.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.35), 60));
		panelfinal.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray));
		return panelfinal;
	}
	private JPanel getLabelFieldEmail(JLabel label, JTextField textField, String value) {
		Box panel = Box.createHorizontalBox();
		JPanel panelfinal = new JPanel(new GridLayout(1,2,1,1));
		label.setForeground(Color.black);
		label.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textField.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		textField.setOpaque(true);
		textField.setForeground(Color.gray);
		textField.setText(value);
		textField.setColumns(30);
		textField.setCaretPosition(0);
		textField.setHorizontalAlignment(JTextField.LEADING);
		// textField.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		// textPanel.setPreferredSize(new Dimension((int)
		// (Constants.SCREEN_SIZE.getWidth()*0.20),50));
		panel.add(label);
		panel.add(Box.createHorizontalStrut(50));
		panel.add(textField);
		panelfinal.add(panel);
		panelfinal.setOpaque(true);
		panelfinal.setBackground(null);
		panelfinal.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.35), 60));
		panelfinal.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray));
		return panelfinal;
	}
	private void AcceptRequest(String username, String status) throws JSONException {
		String response;
		String err_code;
		SendInvitationJSON jsonResponse = new SendInvitationJSON();
		response = jsonResponse.respondToInvitation(username, status);
		JSONObject jsonObj = new JSONObject(response);
		err_code = jsonObj.getString("err-code");

		if (err_code.equals("600")) {
			Constants.loader.setVisible(false);
			JOptionPane.showMessageDialog(thisParent, "Your login session expired.Please login again.");
			Util.disposeLogoutMenu(parent);
			parent.dispose();
			thisParent.dispose();
		} else if (err_code.equals("700")) {
			Constants.loader.setVisible(false);
			JOptionPane.showMessageDialog(getParent(), jsonObj.getString("message"));
		} else if (err_code.equals("404")) {
			Constants.loader.setVisible(false);
			JOptionPane.showMessageDialog(getParent(), jsonObj.getString("message"));
		} else if (err_code.equals("1")) {
			JOptionPane.showMessageDialog(getParent(), jsonObj.getString("message"));
			Runnable run = new Runnable() {
				@Override
				public void run() {
					try {
						rightBox = showPendingRequests();
						rightBox.repaint();
						rightBox.revalidate();
						rightPanel.removeAll();
						rightPanel.add(rightBox);
						rightPanel.repaint();
						rightPanel.revalidate();
						Constants.currentWelcomeScreen.refreshRightBox();
						Constants.loader.setVisible(false);
						if (Constants.showConsole)
							if (Constants.showConsole)
								System.out.println("added..");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			Thread t = new Thread(run);
			t.start();
		}
		if (err_code.equals("300")) {
			JOptionPane.showMessageDialog(getParent(), jsonObj.getString("message"));
		}

	}

	private static boolean isAdjusting(JComboBox cbInput) {
		if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
			return (Boolean) cbInput.getClientProperty("is_adjusting");
		}
		return false;
	}

	private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
		cbInput.putClientProperty("is_adjusting", adjusting);
	}

	public static void fetchPractiseTypeList(final JTextField txtInput, final ArrayList<String> items) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		setAdjusting(cbInput, false);
		for (String item : items) {
			model.addElement(item);
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
					for (String item : items) {
						if (item.toLowerCase().startsWith(input.toLowerCase())) {
							model.addElement(item);
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

	public static void fetchDesignationList(final JTextField txtInput, final ArrayList<String> items) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		setAdjusting(cbInput, false);
		for (String item : items) {
			model.addElement(item);
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
					for (String item : items) {
						if (item.toLowerCase().startsWith(input.toLowerCase())) {
							model.addElement(item);
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

	public static void fetchJobTitleList(final JTextField txtInput, final ArrayList<String> items) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		setAdjusting(cbInput, false);
		for (String item : items) {
			model.addElement(item);
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
					for (String item : items) {
						if (item.toLowerCase().startsWith(input.toLowerCase())) {
							model.addElement(item);
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

	private boolean checkEmpty(JTextField field, String msg) {
		if (Util.isStringEmpty(field.getText())) {
			JOptionPane.showMessageDialog(getRootPane(), msg);
			field.requestFocus();
			return false;
		}
		return true;
	}

	private boolean checkInt(JTextField field, String label, int count) {
		StringBuilder sb = new StringBuilder();
		boolean ok = true;
		char[] chars;
		try {
			// int value = Integer.parseInt(field.getText().trim().toString());
			String str = field.getText().toString();
			chars = str.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				try {
					Integer.parseInt(String.valueOf(chars[i]));
				} catch (NumberFormatException exc) {
					sb.append(label + " is not valid, it should have only digits(0-9);");
					JOptionPane.showMessageDialog(getRootPane(), sb.toString());// ,
																				// it
																				// should
																				// have
																				// only
																				// digits(0-9).");
					field.requestFocus();
					field.setText("");
					ok = false;
					break;
				}
			}
			if (ok == true) {
				int length = chars.length;
				if (count != 0 && (length < count || length > 15) && label.equalsIgnoreCase("phone")) {
					sb.append(label + " should be minimum of " + count + " and maximum of 15 digits.");
					JOptionPane.showMessageDialog(getRootPane(), sb.toString());
					ok = false;
				} else if (count != 0 && count != length) {
					sb.append(label + " should be of " + count + " digits.");
					JOptionPane.showMessageDialog(getRootPane(), sb.toString());
					ok = false;
				} else {
					ok = true;
				}
			}
			return ok;
		} catch (Exception e) {

			return false;
		}
	}
}

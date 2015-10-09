package com.im.physicianscreens;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.swingx.prompt.PromptSupport;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.im.bo.PhysicianRegisterBO;
import com.im.common.ISearchField;
import com.im.common.SearchField;
import com.im.common.TopPanel;
import com.im.json.AuthenticateJSON;
import com.im.json.PhysicianRegistrationJSON;
import com.im.json.TermsAndConditionsPhysician;
import com.im.json.TermsAndConditionsStaff;
import com.im.login.Login;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.utils.Constants;
import com.im.utils.EmailValidator;
import com.im.utils.FindMACAddress;
import com.im.utils.HospitalRelatedList;
import com.im.utils.PasswordValidation;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.XmppUtils;

public class PhysicianRegister extends JFrame {
	private PhysicianRegisterBO bo = new PhysicianRegisterBO();
	private static ResourceBundle rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
	private JFrame parent;
	private JPanel step1;
	private JPanel step2;
	private JPanel step3;
	private JButton step1Button;
	private JButton step2Button;
	private JButton step3Button;
	String phosp_selected = "";
	String shosp_selected = "";
	Point point = new Point();
	boolean resizing = false;
	private static boolean fetchingListPrimary = false;
	private static boolean fetchingListSecondary = false;
	private final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]");
	JFrame thisParent;
	public PhysicianRegister(JFrame parent) {
		Constants.IS_DIALOG = true;
		this.parent = parent;
		setUndecorated(true);
		thisParent = this;
		parent.setEnabled(false);
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 1, 2, 0));
		//Constants.PARENT = parent;
		try {
			initRegister();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initRegister() throws IOException {
		add(TopPanel.topButtonPanelForDialog(this, parent), BorderLayout.NORTH);
		add(vBox(), BorderLayout.CENTER);
		//		add(new Footer().loginLowerBox(), BorderLayout.SOUTH);
		setTitle("Provider Registration");
		setIconImage(new ImageIcon(getClass().getResource("/images/logoicon.png")).getImage());
		int x = (Constants.SCREEN_SIZE.width) / 8;
		int y = (Constants.SCREEN_SIZE.height) / 8;
		setBounds(x, y, Constants.SCREEN_SIZE.width / 2, Constants.SCREEN_SIZE.height / 2);
		setMinimumSize(new Dimension((int) (Math.round(Constants.SCREEN_SIZE.width * 0.60)),
			(int) (Math.round(Constants.SCREEN_SIZE.height * 0.70))));
		setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		getContentPane().setBackground(Color.white);
		setLocationRelativeTo(parent);
		setVisible(true);
		getContentPane().addMouseListener(new MouseAdapter() {
		      public void mousePressed(MouseEvent e) {
		        resizing = getCursor().equals(Cursor.getDefaultCursor())? false:true;
		        if(!e.isMetaDown()){
		          point.x = e.getX();
		          point.y = e.getY();
		        }
		      }
		    });
		getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
		      public void mouseDragged(MouseEvent e) {
		        if(resizing){
		          Point pt = e.getPoint();
		          setSize(getWidth()+pt.x - point.x,getHeight());
		          point.x = pt.x;
		        }
		        else if(!e.isMetaDown()){
		        Point p = getLocation();
		        setLocation(p.x + e.getX() - point.x,
		            p.y + e.getY() - point.y);
		        }
		      }
		    });
	}
	
	private Box vBox() {
		Box box = Box.createVerticalBox();
		box.setOpaque(true);
		step1 = step1Panel();
		step2 = step2Panel();
		step3 = step3Panel();
		
		step2.setVisible(false);
		step3.setVisible(false);
		box.setBackground(null);
		box.add(topBox(), BorderLayout.NORTH);
		box.add(step1, BorderLayout.CENTER);
		box.add(step2, BorderLayout.CENTER);
		box.add(step3, BorderLayout.CENTER);
		return box;
	}
	
	private JPanel topBox() {
		Box topBox = Box.createHorizontalBox();
		JPanel panel = new JPanel();
		JLabel lineGreen = new JLabel(new ImageIcon(
			((new ImageIcon(getClass().getResource("/images/lineGreen.png"))).getImage()).getScaledInstance(50, 5,
				java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		JLabel lineGray = new JLabel(new ImageIcon(
			((new ImageIcon(getClass().getResource("/images/linegray.png"))).getImage())
				.getScaledInstance(40, 5, java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		JLabel lineGray2 = new JLabel(new ImageIcon(
			((new ImageIcon(getClass().getResource("/images/linegray.png"))).getImage())
				.getScaledInstance(40, 5, java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		JLabel lineGray3 = new JLabel(new ImageIcon(
			((new ImageIcon(getClass().getResource("/images/linegray.png"))).getImage())
				.getScaledInstance(40, 5, java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		step1Button = new JButton(new ImageIcon(getClass().getResource("/images/step1_green.png")));
		step2Button = new JButton(new ImageIcon(getClass().getResource("/images/step2_grey.png")));
		step3Button = new JButton(new ImageIcon(getClass().getResource("/images/step3_grey.png")));
		//step4Button = new JButton(new ImageIcon("images/step4_grey.png"));
		
		Util.setTransparentBtn(step1Button);
		Util.setTransparentBtn(step2Button);
		Util.setTransparentBtn(step3Button);
		
		topBox.add(step1Button);
		topBox.add(lineGray);
		topBox.add(step2Button);
		topBox.add(lineGray2);
		topBox.add(step3Button);
		//		topBox.add(lineGray3);
		//		topBox.add(step4Button);
		panel.setBackground(null);
		panel.setOpaque(true);
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
		panel.setPreferredSize(new Dimension(650, 50));
		panel.add(topBox);
		return panel;
	}
	
	private JPanel step1Panel() {
		Box vBox = Box.createVerticalBox();
		JButton next = new JButton(new ImageIcon(getClass().getResource("/images/next.png")));
		JPanel nextPanel = new JPanel();
		Util.setTransparentBtn(next);
		vBox.setBackground(null);
		vBox.setOpaque(true);
		vBox.add(Box.createVerticalStrut(10));
		final JTextField textFirstname = getTextField("Enter Your First Name*", vBox);
		final JTextField textLastName = getTextField("Enter Your Last Name*", vBox);
		final JTextField textUsername = getTextField("Enter Your User Name*", vBox);
		final JTextField textPhone = getTextField("Enter Your Phone number*", vBox);
		final JTextField textEmail = getTextField("Enter Your EmailId*", vBox);
		final JTextField textZip = getTextField("Enter Zip Code*", vBox);
		
		//		textFirstname.addFocusListener(new SpecialCharacterListener());
		//		textLastName.addFocusListener(new SpecialCharacterListener());
		//		textPhone.addFocusListener(new PhoneCheckListener(10, "Phone no"));
		//		textUsername.addFocusListener(new SpecialCharacterListener());
		//		textEmail.addFocusListener(new CheckEmailValidation(getRootPane()));
		//		textZip.addFocusListener(new PhoneCheckListener(5, "Zip"));
		
		nextPanel.add(next, BorderLayout.CENTER);
		nextPanel.setOpaque(true);
		nextPanel.setBackground(null);
		
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkEmpty(textFirstname, "First Name is required") && checkEmpty(textLastName, "Last Name is required")
					&& checkEmpty(textUsername, "Username is required") && checkEmpty(textPhone, "Phone number is required")
					&& checkEmpty(textEmail, "EmailId is required") && checkEmpty(textZip, "Zip Code is required")) {
					
					if (checkSpecialChar(textFirstname) && checkSpecialChar(textLastName) && checkSpecialChar(textUsername)
						&& checkEmail(textEmail) && checkInt(textPhone, "Phone", 10) && checkInt(textZip, "Zip Code", 5)) {
						
						
						String response = new AuthenticateJSON().checkUserAvailability(textUsername.getText());
						try {
							JSONObject obj = new JSONObject(response);
							String err_code = obj.getString("err-code");
							if(err_code.equals("1")){
								JOptionPane.showMessageDialog(getRootPane(), obj.getString("message"));
								bo.setFirst_name(textFirstname.getText());
								bo.setLast_name(textLastName.getText());
								bo.setUsername(textUsername.getText());
								bo.setEmail(textEmail.getText());
								bo.setPhone(textPhone.getText());
								bo.setZip(textZip.getText());
								step2.setVisible(true);
								step1.setVisible(false);
								step2Button.setSelected(true);
								step2Button.setSelectedIcon(new ImageIcon(getClass().getResource("/images/step2_green.png")));
							}
							else if(err_code.equals("700")){
								JOptionPane.showMessageDialog(getRootPane(), obj.getString("message"));
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						
					
					}
				}
			}
		});
		
		vBox.add(Box.createVerticalStrut(15));
		vBox.add(nextPanel);
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(null);
		panel.add(vBox);
		
		return panel;
	}
	
	private JPanel step2Panel() {
		Box vBox = Box.createVerticalBox();
		Box buttonBox = Box.createHorizontalBox();
		JButton next = new JButton(new ImageIcon(getClass().getResource("/images/next.png")));
		JButton back = new JButton(new ImageIcon(getClass().getResource("/images/previous.png")));
		JPanel btnPanel = new JPanel();
		Util.setTransparentBtn(next);
		Util.setTransparentBtn(back);
		
		vBox.setBackground(null);
		vBox.setOpaque(true);
		vBox.add(Box.createVerticalStrut(10));
		final JTextField textNpi = getTextField("Enter NPI*", vBox);
		final JTextField textPin = getTextField("Create 4 Digit Pin*", vBox);
		final JPasswordField textPassword = (JPasswordField) getTextField("Create Password*", vBox, new JPasswordField(20));
		final JPasswordField textConfirmPassword = (JPasswordField) getTextField("Enter Confirm Password*", vBox, new JPasswordField(20));
		final JTextField textSecQues = getTextField("Security Question*", vBox);
		final JTextField textSecAns = getTextField("Security Answer*", vBox);
		char echo = '*';
		textPassword.setEchoChar(echo);
		textConfirmPassword.setEchoChar(echo);
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkEmpty(textNpi, "NPI is required") && checkEmpty(textPin, "Pin is required")
					&& checkEmpty(textPassword, "Password is required") && checkEmpty(textConfirmPassword, "Confirm Password is required.")
					&& checkEmpty(textSecQues, "Security Question is required") && checkEmpty(textSecAns, "Security Answer is required")) {
					
					if (checkInt(textNpi, "NPI", 10) && checkInt(textPin, "Pin", 4) && checkNewPassword(textPassword)
						&& checkMatch(textPassword, textConfirmPassword, "Confirm Password must same as New Password.")) {
						bo.setNPI(textNpi.getText());
						bo.setPin(textPin.getText());
						bo.setPassword(textPassword.getText());
						bo.setSecurity_question(textSecQues.getText());
						bo.setSecurity_answer(textSecAns.getText());
						step3.setVisible(true);
						step1.setVisible(false);
						step2.setVisible(false);
						step3Button.setSelected(true);
						step3Button.setSelectedIcon(new ImageIcon(getClass().getResource("/images/step3_green.png")));
					}
				}
			}
		});
		
		buttonBox.add(back);
		buttonBox.add(Box.createHorizontalStrut(20));
		buttonBox.add(next);
		buttonBox.setOpaque(true);
		buttonBox.setBackground(null);
		btnPanel.add(buttonBox, BorderLayout.CENTER);
		buttonBox.add(back);
		buttonBox.add(Box.createHorizontalStrut(20));
		buttonBox.add(next);
		buttonBox.setOpaque(true);
		buttonBox.setBackground(null);
		
		btnPanel.add(buttonBox, BorderLayout.CENTER);
		btnPanel.setOpaque(true);
		btnPanel.setBackground(null);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				step1.setVisible(true);
				step2.setVisible(false);
				step3.setVisible(false);
			}
		});
		
		vBox.add(Box.createVerticalStrut(15));
		vBox.add(btnPanel);
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(null);
		panel.add(vBox);
		
		return panel;
	}
	
	private JPanel step3Panel() {
		Box vBox = Box.createVerticalBox();
		Box buttonBox = Box.createHorizontalBox();
		JButton next = new JButton(new ImageIcon(getClass().getResource("/images/next.png")));
		JButton back = new JButton(new ImageIcon(getClass().getResource("/images/previous.png")));
		JPanel btnPanel = new JPanel();
		Util.setTransparentBtn(next);
		Util.setTransparentBtn(back);
		
		vBox.setBackground(null);
		vBox.setOpaque(true);
		vBox.add(Box.createVerticalStrut(10));
		
		final JTextField textPractiseType = getTextField("Enter Practice Type*", vBox);
		final JTextField textPrimaryNW = getTextField("Primary Network*", vBox);
		final JTextField textSecNW = getTextField("Secondary Network", vBox);
		final JTextField textJobTitle = getTextField("Enter Job Title", vBox);
		final JTextField textDesignation = getTextField("Enter Your Designation", vBox);
		
		final Map<String, String> primaryHospIds = new HashMap<String, String>();
		new SearchField().makeSearchField(textPrimaryNW, rb.getString("http_url_login"), rb.getString("hospital_list"), "hospitals_list",
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
					return display;
				}
			});
		
		final Map<String, String> secondryHospIds = new HashMap<String, String>();
		new SearchField().makeSearchField(textSecNW, rb.getString("http_url_login"), rb.getString("hospital_list"), "hospitals_list",
			new ISearchField() {
				
				@Override
				public void onSelect(int index, String value) {
					if(Constants.showConsole) System.out.println("Selected Id:(" + index + ") " + secondryHospIds.get(value));
					bo.setOther_hospitals(secondryHospIds.get(value));
				}
				
				@Override
				public void loopStart() {
					secondryHospIds.clear();
				}
				
				@Override
				public String fieldValue(Map<String, String> map) {
					String display = map.get("name") + "," + map.get("city");
					secondryHospIds.put(display, map.get("hosp_id"));
					return display;
				}
			});
		
		//		fetchPrimaryHospitalList(textPrimaryNW);
		//		fetchSecondaryHospitalList(textSecNW);
		ArrayList<String> practiseTypeList = Constants.PRACTISE_LIST;
		ArrayList<String> jobTitleList = Constants.JOB_TITLE_LIST;
		ArrayList<String> designationList = Constants.DESIGNATION_LIST;
		
		if (null != practiseTypeList)
			fetchPractiseTypeList(textPractiseType, practiseTypeList);
		if (null != jobTitleList)
			fetchJobTitleList(textJobTitle, jobTitleList);
		if (null != designationList)
			fetchDesignationList(textDesignation, designationList);
		
		buttonBox.add(back);
		buttonBox.add(Box.createHorizontalStrut(20));
		buttonBox.add(next);
		buttonBox.setOpaque(true);
		buttonBox.setBackground(null);
		btnPanel.add(buttonBox, BorderLayout.CENTER);
		buttonBox.add(back);
		buttonBox.add(Box.createHorizontalStrut(20));
		buttonBox.add(next);
		buttonBox.setOpaque(true);
		buttonBox.setBackground(null);
		
		btnPanel.add(buttonBox, BorderLayout.CENTER);
		btnPanel.setOpaque(true);
		btnPanel.setBackground(null);
		
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (checkEmpty(textPractiseType, "Practice Type is required") && checkEmpty(textPrimaryNW, "Primary Network is required")
					) {
					
					phosp_selected = textPrimaryNW.getText();
					//bo.setPrimary_hosp_id(new HospitalRelatedList().getHospitalSelectedID(textPrimaryNW.getText()));
					shosp_selected = textSecNW.getText();
					bo.setPractice_type(textPractiseType.getText());
					bo.setDesignation(textDesignation.getText());
					bo.setJobTitle(textJobTitle.getText());
					//bo.setOther_hospitals(new HospitalRelatedList().getHospitalSelectedID(textSecNW.getText()));
//					try {
////						bo.setPrimary_hosp_id(new HospitalRelatedList().getHospitalSelectedID(phosp_selected));
////						bo.setOther_hospitals(new HospitalRelatedList().getHospitalSelectedID(shosp_selected));
//					} catch (JSONException e1) {
//						e1.printStackTrace();
//					}
					TermsAndConditionsPhysician browser = new TermsAndConditionsPhysician("physician", bo,parent,thisParent);
					browser.setVisible(true);
				/*	JSONObject jsonResponse;
					PhysicianRegistrationJSON physicianJSON = new PhysicianRegistrationJSON(bo);
					String returnMessage = physicianJSON.doPatientRegistration();
					try {
						jsonResponse = new JSONObject(returnMessage);
						if (jsonResponse.getString("err-code").equals("700")) {
							JOptionPane.showMessageDialog(getRootPane(), jsonResponse.getString("message"));
						} else if (jsonResponse.getString("err-code").equals("1")) {
							JOptionPane.showMessageDialog(getRootPane(), "Awesome! Your account has successfully registered.");
							AuthenticateJSON login = new AuthenticateJSON();
							FindMACAddress macAddress = new FindMACAddress();
							StringBuffer username = new StringBuffer();
							Constants.loggedinuserInfo.name =  bo.getFirst_name() + " "+bo.getLast_name();
							Constants.loggedinuserInfo.username = bo.getUsername();
							Constants.loggedinuserInfo.password = bo.getPassword();
							Constants.loggedinuserInfo.device_id = macAddress.DeviceIDOfSystem();
							Constants.loggedinuserInfo.device_type = "web";
							String response = login.doAuthentication();
							String err_code = "";
							if (response != null) {
								jsonResponse = new JSONObject(response);
								err_code = jsonResponse.getString("err-code");
							}
							
							if (!bo.getUsername().endsWith("@imyourdoc.com"))
								username.append(bo.getUsername() + "@imyourdoc.com");
							else
								username.append(bo.getUsername());
							if (err_code.equals("1")) {
								//					 	XmppUtils.doLogin(username_xmpp, password_xmpp, null);
								Constants.loggedinuserInfo.user_type = jsonResponse.getString("user_type");
								Constants.loggedinuserInfo.user_pin = jsonResponse.getString("user_pin");
								Constants.loggedinuserInfo.login_token = jsonResponse.getString("login_token");
								Constants.loggedinuserInfo.seq_ques = jsonResponse.getString("seq_question");
								Constants.loggedinuserInfo.seq_ans = jsonResponse.getString("seq_answer");
								Login loginuser = new Login();
								loginuser.doLogin(username.toString(), bo.getPassword());
								parent.dispose();
								thisParent.dispose();
							}
							
						} else if (jsonResponse.getString("err-code").equals("404")) {
							JOptionPane.showMessageDialog(getRootPane(), "Practice Type or Job Title or Designation Not Found. ");
							
						}
					} catch (Exception ex) {
						System.out.println(ex.getMessage());
					}*/
				}
			}
		});
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				step2.setVisible(true);
				step3.setVisible(false);
				step1.setVisible(false);
			}
		});
		vBox.add(Box.createVerticalStrut(15));
		vBox.add(btnPanel);
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(null);
		panel.add(vBox);
		return panel;
	}
	////-----------fetching hospital list to be shown in the combobox
	private static boolean isAdjusting(JComboBox cbInput) {
		if (cbInput.getClientProperty("is_adjusting") instanceof Boolean) {
			return (Boolean) cbInput.getClientProperty("is_adjusting");
		}
		return false;
	}
	
	private static void setAdjusting(JComboBox cbInput, boolean adjusting) {
		cbInput.putClientProperty("is_adjusting", adjusting);
	}
	
	private static void fetchPrimaryHospitalList(final JTextField txtInput) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		setAdjusting(cbInput, false);
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
		txtInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent paramKeyEvent) {}
			
			@Override
			public void keyReleased(KeyEvent paramKeyEvent) {
				if (!fetchingListPrimary && isValidKey(paramKeyEvent.getKeyCode())) {
					updateList();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent paramKeyEvent) {}
			
			private void updateList() {
				String input = txtInput.getText();
				if (!input.isEmpty()) {
					getHospitalList(input);
				}
			}
			
			private void getHospitalList(final String name) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							fetchingListPrimary = true;
							setAdjusting(cbInput, true);
							model.removeAllElements();
							cbInput.setPopupVisible(false);
							model.addElement("Loading...");
							cbInput.setPopupVisible(true);
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("method", rb.getString("hospital_list"));
							jsonObject.put("name", name);
							String jsonResponse = Util.getHTTPResponseStr(rb.getString("http_url_login"), jsonObject.toString());
							if(Constants.showConsole) System.out.println("----->" + jsonResponse);
							
							Gson gson = new Gson();
							Map<String, List<Map<String, String>>> javaObj = gson.fromJson(jsonResponse, Map.class);
							List<Map<String, String>> hospitalsList = javaObj.get("hospitals_list");
							model.removeAllElements();
							cbInput.setPopupVisible(false);
							if (null != hospitalsList) {
								model.addElement(name);
								for (Map<String, String> map : hospitalsList) {
									model.addElement(map.get("name") + "," + map.get("city"));
								}
							}
							cbInput.setPopupVisible(model.getSize() > 0);
							setAdjusting(cbInput, false);
							txtInput.requestFocus();
							txtInput.setText("");
							fetchingListPrimary = false;
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
					}
				};
				Thread t = new Thread(runnable);
				t.start();
			}
			
			private boolean isValidKey(int key) {
				return ((key >= KeyEvent.VK_0) && (key <= KeyEvent.VK_CLOSE_BRACKET)) || (key <= KeyEvent.VK_BACK_SPACE)
					|| (key <= KeyEvent.VK_SPACE || (key <= KeyEvent.VK_DELETE));
			}
		});
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
	}
	
	public static void fetchSecondaryHospitalList(final JTextField txtInput) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		
		final JComboBox cbInput = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		cbInput.setLightWeightPopupEnabled(true);
		
		setAdjusting(cbInput, false);
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
		txtInput.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent paramKeyEvent) {}
			
			@Override
			public void keyReleased(KeyEvent paramKeyEvent) {
				if (!fetchingListSecondary && isValidKey(paramKeyEvent.getKeyCode())) {
					updateList();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent paramKeyEvent) {}
			
			private void updateList() {
				String input = txtInput.getText();
				if (!input.isEmpty()) {
					getHospitalList(input);
				}
			}
			
			private void getHospitalList(final String name) {
				final ResourceBundle rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							fetchingListSecondary = true;
							setAdjusting(cbInput, true);
							model.removeAllElements();
							cbInput.setPopupVisible(false);
							model.addElement("Loading...");
							cbInput.setPopupVisible(true);
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("method", rb.getString("hospital_list"));
							jsonObject.put("name", name);
							String jsonResponse = Util.getHTTPResponseStr(rb.getString("http_url_login"), jsonObject.toString());
							if(Constants.showConsole) System.out.println("----->" + jsonResponse);
							
							Gson gson = new Gson();
							Map<String, List<Map<String, String>>> javaObj = gson.fromJson(jsonResponse, Map.class);
							List<Map<String, String>> hospitalsList = javaObj.get("hospitals_list");
							model.removeAllElements();
							cbInput.setPopupVisible(false);
							if (null != hospitalsList) {
								model.addElement(name);
								for (Map<String, String> map : hospitalsList) {
									model.addElement(map.get("name") + "," + map.get("city"));
								}
							}
							cbInput.setPopupVisible(model.getSize() > 0);
							setAdjusting(cbInput, false);
							txtInput.requestFocus();
							txtInput.setText("");
							fetchingListSecondary = false;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				Thread t = new Thread(runnable);
				t.start();
			}
			
			private boolean isValidKey(int key) {
				return ((key >= KeyEvent.VK_0) && (key <= KeyEvent.VK_CLOSE_BRACKET)) || (key <= KeyEvent.VK_BACK_SPACE)
					|| (key <= KeyEvent.VK_SPACE || (key <= KeyEvent.VK_DELETE));
			}
		});
		txtInput.setLayout(new BorderLayout());
		txtInput.add(cbInput, BorderLayout.SOUTH);
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
	
	private void doLogin(String username, String Password) throws XMPPException, IOException {
		if(Constants.showConsole) System.out.println("login done...");
		// commented the xmpp login for the time being as not configured on new
		// api
		Boolean isLogin = XmppUtils.doLogin(username, Password, XmppUtils.getImyourDocConnection());
		
		try {
			if (isLogin) {
				Constants.loggedinuserInfo.username = username;
				Constants.loggedinuserInfo.password = Password;
				UserWelcomeScreen welcomeScreen = new UserWelcomeScreen();//UserWelcomeScreen.getInstance();
				Constants.currentWelcomeScreen = welcomeScreen;
				welcomeScreen.setVisible(true);
				parent.dispose();
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Problem in connection, Please check connection");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		/*
		 * jframe.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		 * jframe.dispose(); AdminPage.getInstance().initComponents();
		 */
	}
	
	private JTextField getTextField(String placeholder, Box container) {
		return getTextField(placeholder, container, new JTextField(20));
	}
	
	private JTextField getTextField(String placeholder, Box container, JTextField textfield) {
		JPanel panel = new JPanel();
		PromptSupport.setPrompt(placeholder, textfield);
		textfield.setBorder(null);
		textfield.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		panel.add(Box.createHorizontalStrut(100));
		panel.add(textfield, BorderLayout.CENTER);
		panel.setOpaque(true);
		panel.setBackground(null);
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		panel.setPreferredSize(new Dimension(500, 30));
		container.add(panel);
		container.add(Box.createVerticalStrut(5));
		return textfield;
	}
	
	private boolean checkEmpty(JTextField field, String msg) {
		if (Util.isStringEmpty(field.getText())) {
			JOptionPane.showMessageDialog(getRootPane(), msg);
			field.requestFocus();
			return false;
		}
		return true;
	}
	
	private boolean checkSpecialChar(JTextField field) {
		String text = field.getText();
		int strLen = text.length();
	    boolean isWhiteSpace = false;
	    for (int i = 0; i < strLen; i++) {
	      if (Character.isWhitespace(text.charAt(i))) {
	    	  isWhiteSpace = true;
	      }
	    }
	  if (hasSpecialChar.matcher(text).find()) {
			JOptionPane.showMessageDialog(getRootPane().getParent(), "Special characters i.e. !,@,#, etc. are not allowed\n");
			field.requestFocus();
			field.setText("");
			return false;
		}
	  else if (isWhiteSpace == true) {
			JOptionPane.showMessageDialog(getRootPane().getParent(), "Space is not allowed in Username\n");
			field.requestFocus();
			field.setText("");
			return false;
		}
		return true;
	}
	
	private boolean checkEmail(JTextField field) {
		EmailValidator emailValidator = new EmailValidator();
		if (!emailValidator.validate(field.getText().trim())) {
			JOptionPane.showMessageDialog(getRootPane(), "Invalid Email Id");
			field.requestFocus();
			return false;
		}
		return true;
	}
	
	private boolean checkNewPassword(JTextField field) {
		PasswordValidation passwordCheck = new PasswordValidation(field.getText());
		String passwordCheckMessage = passwordCheck.validateNewPass();
		if (!passwordCheckMessage.equals("")) {
			JOptionPane.showMessageDialog(getRootPane(), passwordCheckMessage);
			field.requestFocus();
			return false;
		}
		return true;
	}
	
	private boolean checkMatch(JTextField field, JTextField field2, String msg) {
		if (!field.getText().equals(field2.getText())) {
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
			//int value = Integer.parseInt(field.getText().trim().toString());
			String str = field.getText().toString();
			chars = str.toCharArray();
			for ( int i = 0; i < chars.length; i++ ) {
				 try {
	              Integer.parseInt( String.valueOf( chars[i] ) );
	                } catch ( NumberFormatException exc ) {
	                    sb.append(label + " is not valid, it should have only digits(0-9);");
	        			JOptionPane.showMessageDialog(getRootPane(), sb.toString());//, it should have only digits(0-9).");
	        			field.requestFocus();
	        			field.setText("");
	        			ok = false;
	        			break;
	                }
			}
			if(ok == true){
				int length = chars.length;
				if (count != 0 && (length<count || length>15) && label.equalsIgnoreCase("phone"))
				{
					sb.append(label +" should be minimum of "+count+" and maximum of 15 digits.");
					JOptionPane.showMessageDialog(getRootPane(), sb.toString());
					ok = false;
				}
				else if (count != 0 && count!=length)
				{
					sb.append(label +" should be of "+count+" digits.");
					JOptionPane.showMessageDialog(getRootPane(), sb.toString());
					ok = false;
				}
				else{
					ok = true;
				}
			}
			return ok;
		} catch (Exception e) {
			
			return false;
		}
	}
}

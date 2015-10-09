package com.im.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.prompt.PromptSupport;
import org.json.JSONObject;

import com.im.bo.ForgetPasswordBo;
import com.im.common.Footer;
import com.im.common.TopPanel;
import com.im.json.PasswordSettingsJSON;
import com.im.utils.Constants;
import com.im.utils.EmailValidator;
import com.im.utils.EncryptDecryptData;
import com.im.utils.PasswordValidation;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.validationListeners.CheckEmailValidation;
import com.im.validationListeners.ConfirmPasswordListener;
import com.im.validationListeners.PasswordListener;
import com.im.validationListeners.SpecialCharacterListener;

public class ForgetPassword extends JFrame{
	private final Pattern hasSpecialChar = Pattern.compile("[^a-zA-Z0-9 ]");
	private ForgetPasswordBo forgetPasswordBO = new ForgetPasswordBo();
	private JFrame parent;
	private JPanel step1;
	private JPanel step2; 
	JTextField textUsername = new JTextField(20);
	private JPanel step3;
	private JButton step1Button ;
	private JButton step2Button ;
	private JButton step3Button ;
	private static String security_ques; 
	private String security_ans;
	Point point = new Point();
	boolean resizing = false;
	private Box box;
	EncryptDecryptData encryptDecryptData = new EncryptDecryptData();
	public ForgetPassword(JFrame parent){
		Constants.IS_DIALOG = true;
		this.parent = parent ;
		setUndecorated(true);
		parent.setEnabled(false);
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY,1,2,0));
		//Constants.PARENT = parent;
		try {
			initRegister();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void initRegister() throws IOException{
		step1 = step1Panel();
		step2 = step2Panel();
		step3 = step3Panel();
		add(TopPanel.topButtonPanelForDialog(this,parent),BorderLayout.NORTH);
		add(vBox(),BorderLayout.CENTER);
		//add(new Footer().loginLowerBox(), BorderLayout.SOUTH);
		setTitle("Forgot Password");
		setIconImage(new ImageIcon(getClass().getResource("/images/logoicon.png")).getImage());
		  int x = (Constants.SCREEN_SIZE.width)/8;
			 int y = (Constants.SCREEN_SIZE.height)/8;
			setBounds(x,y,Constants.SCREEN_SIZE.width/2,Constants.SCREEN_SIZE.height/2);
		    setMinimumSize(new Dimension((int) (Math.round(Constants.SCREEN_SIZE.width * 0.70)), (int) (Math.round(Constants.SCREEN_SIZE.height * 0.80))));
		
		setResizable(false);
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
	private Box vBox(){
		box = Box.createVerticalBox();
		box.setOpaque(true);
		
		//step4 = step4Panel();
		
		step2.setVisible(false);
		step3.setVisible(false);
		//step4.setVisible(false);
		box.setBackground(null);
		box.add(topBox(),BorderLayout.NORTH);
		box.add(step1,BorderLayout.CENTER);
		//box.add(step2,BorderLayout.CENTER);
		//box.add(step3,BorderLayout.CENTER);
		//box.add(step4,BorderLayout.CENTER);
		return box;
	}
	private JPanel topBox(){
		JPanel topPanel = new JPanel();
		JLabel topIcon = new JLabel(new ImageIcon(((new ImageIcon(getClass().getResource("/images/logo.png"))).getImage())
								.getScaledInstance(150, 200,java.awt.Image.SCALE_SMOOTH)));
		
		Box topVBox = Box.createVerticalBox();
		Box topBox = Box.createHorizontalBox();
		JPanel panel =new JPanel();
		JLabel lineGreen = new JLabel(new ImageIcon(((
				new ImageIcon(getClass().getResource("/images/lineGreen.png"))).getImage()).getScaledInstance
				(50,5, java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		JLabel lineGray = new JLabel(new ImageIcon(((
				new ImageIcon(getClass().getResource("/images/linegray.png"))).getImage()).getScaledInstance
				(40,5, java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		JLabel lineGray2 = new JLabel(new ImageIcon(((
				new ImageIcon(getClass().getResource("/images/linegray.png"))).getImage()).getScaledInstance
				(40,5, java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		JLabel lineGray3 = new JLabel(new ImageIcon(((
				new ImageIcon(getClass().getResource("/images/linegray.png"))).getImage()).getScaledInstance
				(40,5, java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		topIcon.setBackground(null);
		topPanel.setOpaque(true);
		topPanel.setBackground(null);
		topPanel.add(topIcon, BorderLayout.CENTER);
		step1Button = new JButton(new ImageIcon(getClass().getResource("/images/step1_green.png")));
		step2Button = new JButton(new ImageIcon(getClass().getResource("/images/step2_grey.png")));
		step3Button = new JButton(new ImageIcon(getClass().getResource("/images/step3_grey.png")));
		//step4Button = new JButton(new ImageIcon("images/step4_grey.png"));
//		
		Util.setTransparentBtn(step1Button);
		Util.setTransparentBtn(step2Button);
		Util.setTransparentBtn(step3Button);
//		step1Button.setOpaque(true);
//		step1Button.setBorder(null);
//		step1Button.setBorderPainted(false);
//		step1Button.setFocusPainted(false);
//		step1Button.setBackground(null);
//		
//		step2Button.setOpaque(true);
//		step2Button.setBorder(null);
//		step2Button.setFocusPainted(false);
//		step2Button.setBackground(null);
//		step2Button.setBorderPainted(false);
//		
//		step3Button.setOpaque(true);
//		step3Button.setBorder(null);
//		step3Button.setFocusPainted(false);
//		step3Button.setBackground(null);
//		step3Button.setBorderPainted(false);
		
//		step4Button.setOpaque(true);
//		step4Button.setBorder(null);
//		step4Button.setFocusPainted(false);
//		step4Button.setBackground(null);
		topBox.add(step1Button);
		topBox.add(lineGray);
		topBox.add(step2Button);
		topBox.add(lineGray2);
		topBox.add(step3Button);
		topVBox.add(topPanel);
		topVBox.add(Box.createVerticalStrut(10));
		topVBox.add(topBox);
//		topBox.add(lineGray3);
//		topBox.add(step4Button);
		panel.setBackground(null);
		panel.setOpaque(true);
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray));
//		panel.setPreferredSize(new Dimension(650, 250));
		panel.add(topVBox);
		return panel;
	}
	
	private JPanel step1Panel(){
		Box step1Box= Box.createVerticalBox();
		JPanel step1Panel = new JPanel();
		JButton next = new JButton(new ImageIcon(getClass().getResource("/images/next.png")));
		JPanel nextPanel = new JPanel();
		JPanel uname = new JPanel();
		
		JPanel email = new JPanel();
		
		
		PromptSupport.setPrompt("Enter Your User Name*", textUsername);
		textUsername.setBorder(null);
//		textUsername.addFocusListener(new SpecialCharacterListener());
		textUsername.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 16));
		uname.add(Box.createHorizontalStrut(100));
		uname.add(textUsername,BorderLayout.CENTER);
		uname.setOpaque(true);
		uname.setBackground(null);
		uname.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		uname.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60),40));
		
		final JTextField textEmail = new JTextField(20);
		PromptSupport.setPrompt("Enter Your Email Id*", textEmail);
		textEmail.setBorder(null);
	//	textEmail.addFocusListener(new CheckEmailValidation(getRootPane()));
		textEmail.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 16));
		email.add(Box.createHorizontalStrut(100));
		email.add(textEmail,BorderLayout.CENTER);
		email.setOpaque(true);
		email.setBackground(null);
		email.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		email.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60),40));
		
		nextPanel.add(next, BorderLayout.CENTER);
		nextPanel.setOpaque(true);
		nextPanel.setBackground(null);
		
		next.setOpaque(true);
		next.setBackground(null);
		next.setBorderPainted(false);
		next.setBorder(null);
		next.setFocusPainted(false);
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// TODO Auto-generated method stub
				try{
					if(Util.isStringEmpty(textUsername.getText()) && Util.isStringEmpty(textEmail.getText())){
						textUsername.requestFocus();
						JOptionPane.showMessageDialog(getRootPane(),"All fields are required");
						return;
					}
					if(Util.isStringEmpty(textUsername.getText())){
						textUsername.requestFocus();
						JOptionPane.showMessageDialog(getRootPane(),"Username is required");
						return;
					}
					 
					  if(!Util.isStringEmpty(textUsername.getText())){
						 
						    String text = textUsername.getText();
						    int strLen = text.length();
						    boolean isWhiteSpace = false;
						    for (int i = 0; i < strLen; i++) {
						      if (Character.isWhitespace(text.charAt(i))) {
						    	  isWhiteSpace = true;
						      }
						    }
						    if (hasSpecialChar.matcher(text).find()) {
								JOptionPane.showMessageDialog(getRootPane().getParent(), "Special characters i.e. !,@,#, etc. are not allowed\n");
								textUsername.requestFocus();
								textUsername.setText("");
							}
						  else if (isWhiteSpace == true) {
								JOptionPane.showMessageDialog(getRootPane().getParent(), "Space is not allowed in Username\n");
								textUsername.requestFocus();
								textUsername.setText("");
							}
						}
					if(!Util.isStringEmpty(textEmail.getText())){
						String EmailID = textEmail.getText();
						EmailValidator emailValidator = new EmailValidator();
						if (!EmailID.equals("")) {
							if (!emailValidator.validate(EmailID.trim())) {
								System.out.print("Invalid Email ID");
								JOptionPane.showMessageDialog(textEmail.getRootPane(), "Invalid Email Id");
								textEmail.requestFocus();
								return;
								}
							}
					}
					else if(!Util.isStringEmpty(textUsername.getText())){
						forgetPasswordBO.setUsername(textUsername.getText());
					}
					if(Util.isStringEmpty(textEmail.getText())){
						textEmail.requestFocus();
						JOptionPane.showMessageDialog(getRootPane(),"Email is required");
						return;
					}
					else
					{
						 String response = new PasswordSettingsJSON().forgetPasswordStep1(textUsername.getText(), textEmail.getText());
						 JSONObject jsonObject = new JSONObject(response);
						
						 String err_code = "";
						 if(jsonObject!=null){
							 err_code = jsonObject.getString("err-code");
							 if(err_code.equals("1")){
								forgetPasswordBO.setUid(encryptDecryptData.decodeStringData(jsonObject.getString("uid")));
								forgetPasswordBO.setSecurity_answer(encryptDecryptData.decodeStringData(jsonObject.getString("security_answer")));
								forgetPasswordBO.setSecurity_question(encryptDecryptData.decodeStringData(jsonObject.getString("security_question")));
								security_ques = encryptDecryptData.decodeStringData(jsonObject.getString("security_question"));
								security_ans = encryptDecryptData.decodeStringData(jsonObject.getString("security_answer"));
//								step2.setVisible(true);
//								step1.setVisible(false);
								box.remove(1);;
								box.add(step2Panel(),BorderLayout.CENTER);
								box.repaint();
								box.revalidate();
								step2Button.setSelected(true);
								step2Button.setSelectedIcon(new ImageIcon(getClass().getResource("/images/step2_green.png")));
							 }
							 else if(err_code.equals("300")){
								 JOptionPane.showMessageDialog(null,"Unable to proceed.Please try again later.");
							 }
							 else if(err_code.equals("404")){
								 JOptionPane.showMessageDialog(null,"User name or email not found.");
							 }
						 }
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
			} 
		});
		
		step1Box.setBackground(null);
		step1Box.setOpaque(true);
		step1Box.add(Box.createVerticalStrut(10));
		step1Box.add(uname);
		step1Box.add(Box.createVerticalStrut(10));
		step1Box.add(email);
		step1Box.add(Box.createVerticalStrut(50));
		step1Box.add(nextPanel);
		step1Panel.setOpaque(true);
		step1Panel.setBackground(null);
		step1Panel.add(step1Box);
		
		return step1Panel;
	}
	private JPanel step2Panel(){
		JPanel step1Panel2 = new JPanel();
		
		Box step2Box= Box.createVerticalBox();
		JButton next = new JButton(new ImageIcon(getClass().getResource("/images/next.png")));
		JButton back = new JButton(new ImageIcon(getClass().getResource("/images/previous.png")));
		Box buttonBox = Box.createHorizontalBox();
		JPanel nextPanel = new JPanel();
		
		JPanel securityQues = new JPanel();
		JPanel securityAns = new JPanel();
		
		
		final JTextField textSecQues = new JTextField(20);
		textSecQues.setBorder(null);
		textSecQues.setText(security_ques);
		textSecQues.repaint();
		textSecQues.setBackground(null);
		textSecQues.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 16));
		securityQues.add(Box.createHorizontalStrut(100));
		//textSecQues.setText(bo.getSecurity_question()==null?"":bo.getSecurity_question());
		textSecQues.setEditable(false);
		securityQues.add(textSecQues,BorderLayout.CENTER);
		securityQues.setOpaque(true);
		securityQues.setBackground(null);
		securityQues.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		securityQues.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60),40));

		
		final JPasswordField textSecAns = new JPasswordField(20);
		PromptSupport.setPrompt("Security Answer*", textSecAns);
		char echo = '*';
		textSecAns.setEchoChar(echo);
		textSecAns.setBorder(null);
		textSecAns.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 16));
		securityAns.add(Box.createHorizontalStrut(100));
		securityAns.add(textSecAns,BorderLayout.CENTER);
		securityAns.setOpaque(true);
		securityAns.setBackground(null);
		securityAns.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		securityAns.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60),40));
		
		buttonBox.add(back);
		buttonBox.add(Box.createHorizontalStrut(20));
		buttonBox.add(next);
		buttonBox.setOpaque(true);
		buttonBox.setBackground(null);
		
		nextPanel.add(buttonBox,BorderLayout.CENTER);
		nextPanel.setOpaque(true);
		nextPanel.setBackground(null);
		
		next.setOpaque(true);
		next.setBackground(null);
		next.setBorderPainted(false);
		next.setBorder(null);
		next.setFocusPainted(false);
		
		back.setOpaque(true);
		back.setBackground(null);
		back.setBorderPainted(false);
		back.setBorder(null);
		back.setFocusPainted(false);
		
		
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// TODO Auto-generated method stub
				
				 if(Util.isStringEmpty(textSecAns.getText())){
					textSecAns.requestFocus();
					JOptionPane.showMessageDialog(getRootPane(),"Security Answer is required");
					return;
				}
				else {
						System.out.println();
						if(textSecAns.getText().equals(security_ans)){
//							step3.setVisible(true);
//							step1.setVisible(false);
//							step2.setVisible(false);
							box.remove(1);;
							box.add(step3Panel(),BorderLayout.CENTER);
							box.repaint();
							box.revalidate();
							step3Button.setSelected(true);
							step3Button.setSelectedIcon(new ImageIcon(getClass().getResource("/images/step3_green.png")));
						}
						else{
							JOptionPane.showMessageDialog(null, "Security answer mismatch!");
						}
				}
			
			} 
		});
		back.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				box.remove(1);;
				box.add(step1Panel(),BorderLayout.CENTER);
				box.repaint();
				box.revalidate();
			}
		});
		step2Box.setBackground(null);
		step2Box.setOpaque(true);
		step2Box.add(Box.createVerticalStrut(10));
		step2Box.add(securityQues);
		step2Box.add(Box.createVerticalStrut(5));
		step2Box.add(securityAns);
		step2Box.add(Box.createVerticalStrut(50));
		step2Box.add(nextPanel);
		step1Panel2.setOpaque(true);
		step1Panel2.setBackground(null);
		step1Panel2.add(step2Box);
		
		
		return step1Panel2;
	}
	private JPanel step3Panel(){
		final EncryptDecryptData encryptDecryptData = new EncryptDecryptData();
		JPanel step1Panel3 = new JPanel();
		Box step3Box= Box.createVerticalBox();
		JButton next = new JButton(new ImageIcon(getClass().getResource("/images/next.png")));
		JButton back = new JButton(new ImageIcon(getClass().getResource("/images/previous.png")));
		Box buttonBox = Box.createHorizontalBox();
		JPanel nextPanel = new JPanel();
		JPanel password = new JPanel();
		JPanel confirmpassword = new JPanel();

		final JPasswordField textPassword = new JPasswordField(20);
		char echo = '*';
		textPassword.setEchoChar(echo);
		PromptSupport.setPrompt("Create Password*", textPassword);
		textPassword.setBorder(null);
//		textPassword.addFocusListener(new PasswordListener(getRootPane()));
		textPassword.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 16));
		password.add(Box.createHorizontalStrut(100));
		password.add(textPassword,BorderLayout.CENTER);
		password.setOpaque(true);
		password.setBackground(null);
		password.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		password.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60),40));

		
		final JPasswordField textConfirmPassword = new JPasswordField(20);
		textConfirmPassword.setEchoChar(echo);
		PromptSupport.setPrompt("Confirm Password*", textConfirmPassword);
		textConfirmPassword.setBorder(null);
//		textConfirmPassword.addFocusListener(new ConfirmPasswordListener(getRootPane()));
		textConfirmPassword.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 16));
		confirmpassword.add(Box.createHorizontalStrut(100));
		confirmpassword.add(textConfirmPassword,BorderLayout.CENTER);
		confirmpassword.setOpaque(true);
		confirmpassword.setBackground(null);
		confirmpassword.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		confirmpassword.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60),40));
		
		buttonBox.add(back);
		buttonBox.add(Box.createHorizontalStrut(20));
		buttonBox.add(next);
		buttonBox.setOpaque(true);
		buttonBox.setBackground(null);
		
		nextPanel.add(buttonBox,BorderLayout.CENTER);
		nextPanel.setOpaque(true);
		nextPanel.setBackground(null);
		
		next.setOpaque(true);
		next.setBackground(null);
		next.setBorderPainted(false);
		next.setBorder(null);
		next.setFocusPainted(false);
		
		back.setOpaque(true);
		back.setBackground(null);
		back.setBorderPainted(false);
		back.setBorder(null);
		back.setFocusPainted(false);
		
		
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// TODO Auto-generated method stub
				String validMessage = "";
				if(Util.isStringEmpty(textPassword.getText()) && Util.isStringEmpty(textConfirmPassword.getText())){
					JOptionPane.showMessageDialog(getParent(),"All fields are required");
					textPassword.requestFocus();
					return;
				}
				
				 if(Util.isStringEmpty(textPassword.getText())){
					JOptionPane.showMessageDialog(getParent(),"Password is required");
					textPassword.requestFocus();
					return;
				}
				 if(Util.isStringEmpty(textConfirmPassword.getText())){
						JOptionPane.showMessageDialog(getParent(),"Confirm Password is required");
						textConfirmPassword.requestFocus();
						return;
					}
				 if(!Util.isStringEmpty(textPassword.getText())){
					PasswordValidation	passwordCheck = new PasswordValidation(textPassword.getText());
					validMessage = passwordCheck.validateNewPass();
					if(!validMessage.equals("")){
						JOptionPane.showMessageDialog(getParent(),validMessage);
						return;
					}
					
				}
				
				 if(!(textPassword.getText().equals(textConfirmPassword.getText()))){
//					PasswordValidation	passwordCheck = new PasswordValidation(textPassword.getText(),textConfirmPassword.getText());
//					validMessage = passwordCheck.validateConfirmPass();
					JOptionPane.showMessageDialog(getParent(),"Passwords do not match.");
					return;
				}		
				else {
					try{
							 String response = new PasswordSettingsJSON().
									 forgetPasswordStep2(encryptDecryptData.encodeStringData(forgetPasswordBO.getUid()),
											 encryptDecryptData.encodeStringData(textPassword.getText()));
							 JSONObject jsonObject = new JSONObject(response);
							
							 String err_code = "";
							 if(jsonObject!=null){
								 err_code = jsonObject.getString("err-code");
								 if(err_code.equals("1")){
									 JOptionPane.showMessageDialog(null,"Password updated successfully");
									 dispose();
									 parent.setEnabled(true);
									 Constants.loader.setVisible(true);
									 SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

										@Override
										protected Void doInBackground()
												throws Exception {
											String username = textUsername.getText();
//											 if(!username.contains("@imyourdoc.com")){
//												 username = username +"@imyourdoc.com";
//											 }
											Login.loginListener(username, textPassword.getText());
											return null;
										}
										 
									 };
									 worker.execute();
								 }
								 else if(err_code.equals("300")){
									 JOptionPane.showMessageDialog(null,"Unable to proceed.Please try again later.");
								 }
								 else if(err_code.equals("500")){
									 JOptionPane.showMessageDialog(null,"Duplicate password not allowed.");
								 }
						}
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
					}
				} 
		});
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				box.remove(1);;
				box.add(step2Panel(),BorderLayout.CENTER);
				box.repaint();
				box.revalidate();
			}
		});
		step3Box.setBackground(null);
		step3Box.setOpaque(true);
		step3Box.add(Box.createVerticalStrut(10));
		step3Box.add(password);
		step3Box.add(Box.createVerticalStrut(5));
		step3Box.add(confirmpassword);
		step3Box.add(Box.createVerticalStrut(50));
		step3Box.add(nextPanel);
		step1Panel3.setOpaque(true);
		step1Panel3.setBackground(null);
		step1Panel3.add(step3Box);
		return step1Panel3;
	}
}

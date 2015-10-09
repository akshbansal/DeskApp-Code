package com.im.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.json.JSONException;
import org.json.JSONObject;

import com.im.json.PasswordSettingsJSON;
import com.im.utils.Constants;
import com.im.utils.EncryptDecryptData;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.validationListeners.ConfirmPasswordListener;
import com.im.validationListeners.PasswordListener;
import com.im.validationListeners.PhoneCheckListener;

public class AccountSettings extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static AccountSettings accountsSettings = new AccountSettings();
	private JPasswordField newPasswordField;
	private JPasswordField confirmPasswordField;
	private JPasswordField oldPasswordField;
	
	private JTextField textOldPin;
	private JTextField textNewPin;
	private JTextField textSecurityQuestion;
	private JPasswordField textSecurityAnswer; 
	private JLabel topIcon;
	private JPanel topPanel;
	private Box passwordPanel;
	private Box pinPanel;
	
	private JLabel confirmPassword;
	private JLabel oldPassword;
	private JLabel newPassword;
	
	private JLabel oldPin;
	private JLabel newPin;
	private JLabel securityQuestion;
	private JLabel securityAnswer;
	
	private JButton buttonOpenPassword;
	private JButton buttonOpenPin;
	
	private JButton buttonChangePassword;
	private JButton buttonChangePin;
	private Box topHbox;
	private Box vBox;
	public static AccountSettings getInstance(){
		return accountsSettings;
	}



	public JPanel showAccountSetting(String sec_ques,String sec_ans,JDialog parent) {
		topIcon = new JLabel(new ImageIcon(((new ImageIcon(getClass().getResource("/images/logo.png"))).getImage())
								.getScaledInstance(200, 250,java.awt.Image.SCALE_SMOOTH)),JLabel.HORIZONTAL);
		topPanel = new JPanel();
		topPanel.add(topIcon, BorderLayout.CENTER);
		topPanel.setOpaque(true);
		topPanel.setBackground(Color.white);
		oldPasswordField = new JPasswordField(20);
		newPasswordField = new JPasswordField(20);
		confirmPasswordField = new JPasswordField(20);
		
		textNewPin = new JTextField(20);
		textOldPin = new JTextField(20);
		textSecurityQuestion = new JTextField(20);
		textSecurityAnswer = new JPasswordField(20);
		char echo = '*';
		textSecurityAnswer.setEchoChar(echo);
		oldPasswordField.setEchoChar(echo);
		newPasswordField.setEchoChar(echo);
		confirmPasswordField.setEchoChar(echo);
		passwordPanel = Box.createVerticalBox();
		pinPanel = Box.createVerticalBox();
		
		
		confirmPassword = new JLabel("<html>*Confirm<br>Password</br></html>");
		oldPassword = new JLabel("<html>*Old<br>Password</br></html>");
		newPassword = new JLabel("<html>*New<br>Password</br></html>");
		
		oldPin = new JLabel("*Old Pin");
		newPin = new JLabel("*New Pin");
		securityQuestion = new JLabel("<html>Security<br>Question</br></html>");
		securityAnswer  = new JLabel("<html>*Security<br>Answer</br></html>");
		
		
		textOldPin.addFocusListener(new PhoneCheckListener(4, "Old Pin"));
		textNewPin.addFocusListener(new PhoneCheckListener(4, "New Pin"));
		topHbox = Box.createHorizontalBox();
		vBox = Box.createVerticalBox();
		oldPasswordField.addFocusListener(new PasswordListener(getRootPane()));
		newPasswordField.addFocusListener(new PasswordListener(getRootPane()));
		confirmPasswordField.addFocusListener(new ConfirmPasswordListener(getRootPane()));
		
		buttonOpenPassword = new JButton("Change Password");
//		buttonOpenPassword.setOpaque(true);
//		buttonOpenPassword.setBackground(Color.white);
//		buttonOpenPassword.setBorder(new TextBubbleBorder(Color.decode("#9CCD21"),2,5,0));
//		buttonOpenPassword.setForeground(Color.decode("#9CCD21"));
		buttonOpenPassword.setForeground(Color.decode("#9CCD21"));
		buttonOpenPassword.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		Util.setTransparentBtn(buttonOpenPassword);
		BufferedImage master;
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			buttonOpenPassword.setIcon(new ImageIcon(scaled));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		buttonOpenPassword.setSelected(true);
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_over.png"));
			Image scaled2 = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			buttonOpenPassword.setSelectedIcon(new ImageIcon(scaled2));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		buttonOpenPin = new JButton("Change Pin");
		buttonOpenPin.setForeground(Color.white.brighter());
		buttonOpenPin.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		Util.setTransparentBtn(buttonOpenPin);
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			buttonOpenPin.setIcon(new ImageIcon(scaled));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		buttonChangePassword = new JButton("Update Password");
		buttonChangePassword.setForeground(Color.white.brighter());
		buttonChangePassword.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		Util.setTransparentBtn(buttonChangePassword);
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			buttonChangePassword.setIcon(new ImageIcon(scaled));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		buttonChangePin = new JButton("Update Pin");
		buttonChangePin.setForeground(Color.white.brighter());
		buttonChangePin.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		Util.setTransparentBtn(buttonChangePin);
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			buttonChangePin.setIcon(new ImageIcon(scaled));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		buttonChangePassword.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		buttonChangePin.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		buttonOpenPassword.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		buttonOpenPin.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		passwordPanel.add(getLabelField(oldPassword, oldPasswordField, ""));
		passwordPanel.add(getLabelField(newPassword, newPasswordField, ""));
		passwordPanel.add(getLabelField(confirmPassword, confirmPasswordField, ""));
		//passwordPanel.add(Box.createVerticalStrut(20));
		JPanel passwordButtonPanel = new JPanel();
		passwordButtonPanel.setOpaque(true);
		passwordButtonPanel.setBackground(null);
		passwordButtonPanel.add(buttonChangePassword,BorderLayout.CENTER);
		passwordPanel.add(passwordButtonPanel,BorderLayout.CENTER);
		
		
		pinPanel.add(getLabelField(oldPin, textOldPin, ""));
		pinPanel.add(getLabelField(newPin, textNewPin, ""));
		pinPanel.add(getLabelField(securityQuestion, textSecurityQuestion, sec_ques));
		textSecurityQuestion.setEditable(false);
		textSecurityQuestion.setOpaque(true);
		textSecurityQuestion.setBackground(null);
		pinPanel.add(getLabelField(securityAnswer, textSecurityAnswer, ""));
		//pinPanel.add(Box.createVerticalStrut(20));
		JPanel pinButtonPanel = new JPanel();
		pinButtonPanel.setOpaque(true);
		pinButtonPanel.setBackground(null);
		pinButtonPanel.add(buttonChangePin,BorderLayout.CENTER);
		pinPanel.add(pinButtonPanel,BorderLayout.CENTER);
		pinPanel.setVisible(false);
		topHbox.add(buttonOpenPassword);
		topHbox.add(Box.createHorizontalStrut(10));
		topHbox.add(buttonOpenPin);
		buttonOpenPassword.setFocusPainted(false);
		buttonOpenPin.setFocusPainted(false);
		buttonChangePin.addActionListener(new ChangePinListener(parent));
		buttonChangePassword.addActionListener(new ChangePasswordListener(parent));
		buttonOpenPassword.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!passwordPanel.isVisible())
						passwordPanel.setVisible(true);
					pinPanel.setVisible(false);
					buttonOpenPin.setSelected(false);
					buttonOpenPassword.setSelected(true);
					buttonOpenPassword.setBackground(Color.white);
					buttonOpenPassword.setBorder(new TextBubbleBorder(Color.decode("#9CCD21"),2,5,0));
					buttonOpenPassword.setForeground(Color.decode("#9CCD21"));
					
					buttonOpenPin.setForeground(Color.white);
			}
		});
        buttonOpenPin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if(!pinPanel.isVisible())
						pinPanel.setVisible(true);
				passwordPanel.setVisible(false);
				buttonOpenPin.setSelected(true);
				buttonOpenPassword.setSelected(false);
				buttonOpenPin.setBackground(Color.white);
				buttonOpenPin.setBorder(new TextBubbleBorder(Color.decode("#9CCD21"),2,5,0));
				buttonOpenPin.setForeground(Color.decode("#9CCD21"));
				
				buttonOpenPassword.setBackground(Color.decode("#9CCD21"));
				buttonOpenPassword.setBorder(new TextBubbleBorder(Color.decode("#9CCD21"),2,5,0));
				buttonOpenPassword.setForeground(Color.white.brighter());
				try {
					BufferedImage master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_over.png"));
					Image scaled2 = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
					buttonOpenPin.setSelectedIcon(new ImageIcon(scaled2));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		 });
        vBox.add(topPanel);
        JPanel panelTopBox = new JPanel();
        panelTopBox.add(topHbox);
        panelTopBox.setOpaque(true);
        panelTopBox.setBackground(Color.white);
        vBox.add(panelTopBox);
        vBox.add(passwordPanel,2);
        vBox.add(pinPanel,2);
        JPanel finalPanel = new  JPanel();
        finalPanel.add(vBox);
        finalPanel.setOpaque(true);
        finalPanel.setBackground(null);
        return finalPanel;
	}
	
	private JPanel getLabelField(JLabel label, JTextField textField,String value) {
		Box panel = Box.createHorizontalBox();
		JPanel panelfinal = new JPanel();
		label.setForeground(Color.black);
		label.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textField.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		textField.setOpaque(true);
		textField.setText(value);
		textField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		panel.add(label, BorderLayout.WEST);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(textField, BorderLayout.EAST);
		panelfinal.setOpaque(true);
		panelfinal.setBackground(null);
		panelfinal.add(panel);
		panelfinal.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray));
		return panelfinal;
	}
	private class ChangePasswordListener implements ActionListener{
		JDialog parent;
		public ChangePasswordListener(JDialog parent) {
			this.parent = parent;
		}
		String response="";String err_code="";
		JSONObject jsonObject=null;
		EncryptDecryptData encryptData = new EncryptDecryptData();
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(oldPasswordField.getText().toString().equals("")||newPasswordField.getText().toString().equals("")
					||confirmPasswordField.getText().equals("")){
				JOptionPane.showMessageDialog(parent, "Enter all fields");
			}
			else{
				Constants.loader.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

					@Override
					protected Void doInBackground() throws Exception {
						try {
							response = new PasswordSettingsJSON().updatePassword(encryptData.encodeStringData(oldPasswordField.getText()), encryptData.encodeStringData(newPasswordField.getText()));
							jsonObject = new JSONObject(response);
							err_code = jsonObject.getString("err-code");
							if(err_code.equals("600")){
								Constants.loader.setVisible(false);
								JOptionPane.showMessageDialog(parent, "Your login session expired.Please login again.");
							}
							else if(err_code.equals("300")){
								Constants.loader.setVisible(false);
								JOptionPane.showMessageDialog(parent, "Unable to proceed.Please try again later.");
							}
							else if(err_code.equals("500")){
								Constants.loader.setVisible(false);
								JOptionPane.showMessageDialog(parent, "Duplicate password not allowed.");
							}else if(err_code.equals("200")){
								Constants.loader.setVisible(false);
								JOptionPane.showMessageDialog(parent, "Please choose different password this password is used by you.");
							}
							else if(err_code.equals("404")){
								//
								Constants.loader.setVisible(false);
								JOptionPane.showMessageDialog(parent, "Invalid Old Password.");
							}
							else if(err_code.equals("1")){
								// Password updated
								Constants.loader.setVisible(false);
								JOptionPane.showMessageDialog(parent, "Your Password updated successfully.");
								oldPasswordField.setText("");
								newPasswordField.setText("");
								confirmPasswordField.setText("");
							}
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						return null;
					}
					
				};
				worker.execute();
			}
			
		}
		
	}
	private class ChangePinListener implements ActionListener{
		JDialog parent;
		public ChangePinListener(JDialog parent) {
			this.parent = parent;
		}
		String response="";String err_code="";
		JSONObject jsonObject=null;
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(textOldPin.getText().equals("")||textNewPin.getText().equals("")
					||textSecurityAnswer.getText().equals("")){
				JOptionPane.showMessageDialog(parent, "Enter all fields.");
			}
			else{
				Constants.loader.setVisible(true);
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

					@Override
					protected Void doInBackground() throws Exception {
					try {
						response = new PasswordSettingsJSON().updatePin(textOldPin.getText(), textNewPin.getText(), textSecurityAnswer.getText());
						jsonObject = new JSONObject(response);
						err_code = jsonObject.getString("err-code");
						if(err_code.equals("600")){
							Constants.loader.setVisible(false);
							JOptionPane.showMessageDialog(parent, "Your login session expired.Please login again.");
						}
						else if(err_code.equals("300")){
							Constants.loader.setVisible(false);
							JOptionPane.showMessageDialog(parent, "Unable to proceed.Please try again later.");
						}
						else if(err_code.equals("500")){
							Constants.loader.setVisible(false);
							JOptionPane.showMessageDialog(parent, "Incorrect user pin or security answer");
						}
						else if(err_code.equals("404")){
							Constants.loader.setVisible(false);
							JOptionPane.showMessageDialog(parent, "Username not found");
						}
						else if(err_code.equals("1")){
							// Password updated
							Constants.loader.setVisible(false);
							Constants.loggedinuserInfo.user_pin = textNewPin.getText();
							JOptionPane.showMessageDialog(parent, "Your Pin updated successfully.");
							textOldPin.setText("");
							textNewPin.setText("");
							textSecurityAnswer.setText("");
						}
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
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


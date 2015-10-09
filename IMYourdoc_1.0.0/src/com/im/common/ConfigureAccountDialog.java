package com.im.common;

import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.JTextComponent;

import org.jdesktop.swingx.prompt.PromptSupport;
import org.json.JSONObject;

import com.im.json.AuthenticateJSON;
import com.im.json.LogoutJSON;
import com.im.login.Login;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.utils.Constants;
import com.im.utils.PasswordValidation;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;
import com.im.utils.XmppUtils;

public class ConfigureAccountDialog extends JFrame {
	private JFrame parent;
	LoaderWindow loader = new LoaderWindow(Constants.mainFrame);
	private Point point = new Point();
	boolean resizing = false;
	private JFrame thisDialog;
	private String username;
	public ConfigureAccountDialog(JFrame parent,String username) {
		this.parent = parent;
		thisDialog = this;
        this.username = username;
		setUndecorated(true);
		int x = 0;
		int y = 0;
		setBounds(x,y,parent.getWidth()/2,parent.getHeight()/2);
		setMinimumSize(new Dimension(parent.getWidth()/2,parent.getHeight()-80));
		setLocationRelativeTo(parent);
		doConfigure();
		parent.setEnabled(false);
		toFront();
		setTitle("Account Configuration");
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo_welcome.png")));
	}
	public void doConfigure(){
		Box vbBox = Box.createVerticalBox();
		final JTextField textNewPass = new JPasswordField(10);
		JPanel textNewPassPanel = new JPanel(new GridLayout());
		textNewPass.setBorder(BorderFactory.createEmptyBorder());
		textNewPassPanel.add(textNewPass,BorderLayout.CENTER);
		textNewPassPanel.setBackground(null);
		textNewPassPanel.setOpaque(true);
		textNewPassPanel.setPreferredSize(new Dimension(50,40));
		textNewPassPanel.setMaximumSize(new Dimension(400,100));
		textNewPass.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 18));
		textNewPass.setForeground(Color.gray);
		textNewPassPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		PromptSupport.setPrompt("*Enter your new password", textNewPass);
		
		final JTextField textConfirmPass = new JPasswordField(10);
		JPanel textConfirmPassPanel = new JPanel(new GridLayout());
		textConfirmPass.setBorder(BorderFactory.createEmptyBorder());
		textConfirmPassPanel.add(textConfirmPass,BorderLayout.CENTER);
		textConfirmPassPanel.setBackground(null);
		textConfirmPassPanel.setOpaque(true);
		textConfirmPass.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 18));
		textConfirmPass.setForeground(Color.gray);
		textConfirmPassPanel.setPreferredSize(new Dimension(50,40));
		textConfirmPassPanel.setMaximumSize(new Dimension(400,100));
		textConfirmPassPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		PromptSupport.setPrompt("*Confirm password", textConfirmPass);
		
		final JTextField textPin = new JTextField(10);
		JPanel textPinPanel = new JPanel(new GridLayout());
		textPin.setBorder(BorderFactory.createEmptyBorder());
		textPinPanel.add(textPin,BorderLayout.CENTER);
		textPinPanel.setBackground(null);
		textPin.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 18));
		textPin.setForeground(Color.gray);
		textPinPanel.setOpaque(true);
		textPinPanel.setPreferredSize(new Dimension(50,50));
		textPinPanel.setMaximumSize(new Dimension(400,100));
		textPinPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		PromptSupport.setPrompt("*Enter your PIN", textPin);
		
		final JTextField textConfirmPin = new JTextField(10);
		JPanel textConfirmPinPanel = new JPanel(new GridLayout());
		textConfirmPin.setBorder(BorderFactory.createEmptyBorder());
		textConfirmPinPanel.add(textConfirmPin,BorderLayout.CENTER);
		textConfirmPinPanel.setBackground(null);
		textConfirmPin.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 18));
		textConfirmPin.setForeground(Color.gray);
		textConfirmPinPanel.setOpaque(true);
		textConfirmPinPanel.setPreferredSize(new Dimension(50,40));
		textConfirmPinPanel.setMaximumSize(new Dimension(400,100));
		textConfirmPinPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		PromptSupport.setPrompt("*Confirm PIN", textConfirmPin);
		
		
		final JTextField textSecurityQues = new JTextField(10);
		JPanel textSecQuesPanel = new JPanel(new GridLayout());
		textSecurityQues.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 18));
		textSecurityQues.setForeground(Color.gray);
		textSecurityQues.setBorder(BorderFactory.createEmptyBorder());
		textSecQuesPanel.add(textSecurityQues,BorderLayout.CENTER);
		textSecQuesPanel.setBackground(null);
		textSecQuesPanel.setOpaque(true);
		textSecQuesPanel.setPreferredSize(new Dimension(50,40));
		textSecQuesPanel.setMaximumSize(new Dimension(400,100));
		textSecQuesPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		PromptSupport.setPrompt("*Enter Security Question", textSecurityQues);
		
		final JTextField textSecurityAns = new JTextField(10);
		JPanel textSecAnsPanel = new JPanel(new GridLayout());
		textSecurityAns.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 18));
		textSecurityAns.setForeground(Color.gray);
		textSecurityAns.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		textSecAnsPanel.add(textSecurityAns,BorderLayout.CENTER);
		textSecAnsPanel.setBackground(null);
		textSecAnsPanel.setOpaque(true);
		textSecAnsPanel.setPreferredSize(new Dimension(50,40));
		textSecAnsPanel.setMaximumSize(new Dimension(400,100));
		textSecAnsPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.LIGHT_GRAY));
		PromptSupport.setPrompt("*Enter your Security Answer", textSecurityAns);
		
		JButton buttonConfigure = new JButton("Configure");
		buttonConfigure.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 20));
		buttonConfigure.setForeground(Color.white);
		buttonConfigure.setHorizontalTextPosition(SwingConstants.CENTER);
		
		BufferedImage master;
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			buttonConfigure.setIcon(new ImageIcon(scaled));
			Util.setTransparentBtn(buttonConfigure);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		buttonConfigure.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
					@Override
					protected Void doInBackground() throws Exception {
						PasswordValidation passvalidate = new PasswordValidation(textNewPass.getText());
						PasswordValidation passvalidate2 = new PasswordValidation(textConfirmPass.getText());
						if(textPin.getText().equals("")||textConfirmPin.getText().equals("")
								||textNewPass.getText().equals("")||textConfirmPass.getText().equals("")||
								textSecurityQues.equals("")||textSecurityAns.getText().equals("")){
							JOptionPane.showMessageDialog(thisDialog, "All fields are mandatory");
						}
						else if(!textPin.getText().equals(textConfirmPin.getText())){
							JOptionPane.showMessageDialog(thisDialog, "PIN and confirm PIN fields dont match ");
						}
						else if(!textNewPass.getText().equals(textConfirmPass.getText())){
							JOptionPane.showMessageDialog(thisDialog, "Password and confirm password fields dont match ");
						}
						else if(!passvalidate.validateNewPass().equals("")){
							JOptionPane.showMessageDialog(thisDialog, passvalidate.validateNewPass());
						}
						else if(!passvalidate2.validateNewPass().equals("")){
							JOptionPane.showMessageDialog(thisDialog, passvalidate2.validateNewPass());
						}
						else if(!checkPin(textPin)){
							JOptionPane.showMessageDialog(thisDialog,"Pin should have only 4 digits [0-9]");
						}
						else if(!checkPin(textConfirmPin)){
							JOptionPane.showMessageDialog(thisDialog,"Pin should have only 4 digits [0-9]");
						}
						else{
							loader.setVisible(true);
							AuthenticateJSON json = new AuthenticateJSON();
							String response = json.doConfigureAccount(username, textPin.getText(), 
									textNewPass.getText(), textSecurityQues.getText(), textSecurityAns.getText());
							JSONObject jsonObject = new JSONObject(response);
							String err_code = jsonObject.getString("err-code");
							if(err_code.equals("1")){
								loader.setVisible(false);
								JOptionPane.showMessageDialog(thisDialog, "Your pofile configured successfully.");
								loader.setVisible(true);
								SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

									@Override
									protected Void doInBackground()
											throws Exception {
										Login.doLogin(username, textNewPass.getText());
										thisDialog.dispose();
										//loader.setVisible(false);
										return null;
									}
									
								};
								worker.execute();
							}
							else if(err_code.equals("300")){
								loader.setVisible(false);
								JOptionPane.showMessageDialog(thisDialog, "Unable to proceed.Please try again later.");
							}
							else if(err_code.equals("500")){
								loader.setVisible(false);
								JOptionPane.showMessageDialog(thisDialog, "Duplicate password not allowed.");
							}
							else if(err_code.equals("404")){
								loader.setVisible(false);
								JOptionPane.showMessageDialog(thisDialog, "User name not found.");
							}
						}
						
						return null;
					}
					
				};
				worker.execute();
			}
		});
		vbBox.add(Box.createVerticalStrut(5));
		JPanel bottom = new JPanel();
		bottom.setBackground(null);
		bottom.setOpaque(true);
		bottom.add(buttonConfigure,BorderLayout.CENTER);
		add(TopPanel.topButtonPanelForAccountDialog(this, "Account Configuration"),BorderLayout.NORTH);
		JLabel topIcon = new JLabel(
				new ImageIcon(
						((new ImageIcon(getClass().getResource("/images/logo.png"))).getImage())
								.getScaledInstance(200, 250,
										java.awt.Image.SCALE_SMOOTH)),
				JLabel.HORIZONTAL);
			JPanel	topPanel = new JPanel();
				topPanel.add(topIcon, BorderLayout.CENTER);
				topPanel.setOpaque(true);
				topPanel.setBackground(null);
		vbBox.add(topPanel);
		vbBox.add(Box.createVerticalStrut(5));
		vbBox.add(textNewPassPanel);
		vbBox.add(textConfirmPassPanel);
		vbBox.add(textPinPanel);
		vbBox.add(textConfirmPinPanel);
		vbBox.add(textSecQuesPanel);
		vbBox.add(textSecAnsPanel);
		vbBox.add(Box.createVerticalStrut(10));
		vbBox.add(bottom);
		JPanel panel = new JPanel(new BorderLayout());
		//panel.setPreferredSize(new Dimension(400,200));
		panel.add(vbBox,BorderLayout.CENTER);
		panel.setBackground(Color.white);
		panel.setOpaque(true);
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
		add(panel);
	}
	
	private boolean checkPin(JTextField textField){
	    String fieldValue = textField.getText();
	    boolean isInteger = false;
	    boolean status=false;
	// TODO Auto-generated method stub
	    if(!fieldValue.equals("")){
		    try{
		    	int nn = Integer.parseInt(fieldValue);
		    	if(Constants.showConsole) System.out.println(nn);
		    	isInteger = true;
		    	
		    }
		    catch (Exception e){
		    	textField.requestFocus();
		    	textField.setText("");
		    	//JOptionPane.showMessageDialog(textField.getRootPane(),"Pin should be of 4 digits only");
		    	isInteger = false;
		    }
	   }
	    if(isInteger== true){
	    	if (fieldValue.trim().length() > 4 ){
	    		//JOptionPane.showMessageDialog(textField.getRootPane(),"Pin should be of 4 digits only");
	    		textField.requestFocus();
	    		status = false;
	    	}
	    	else if (fieldValue.trim().length() < 4 ){
	    		//JOptionPane.showMessageDialog(textField.getRootPane(),"Pin should be of 4 digits only");
	    		textField.requestFocus();
	    		status = false;
	    	}
	    	else {
	    		status = true;
	    	}
	    }
	    return status;
	}
}

package com.im.user;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.json.JSONException;
import org.json.JSONObject;

import com.im.bo.RosterVCardBo;
import com.im.common.LightScrollPane;
import com.im.common.TopPanel;
import com.im.json.SendInvitationJSON;
import com.im.json.UserProfileJSON;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.utils.Constants;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;
import com.im.utils.XmppUtils;

public class AddUser extends JFrame {
	Frame parent;
	String username;
	 BufferedImage image ;
	 String privacy_enabled;
	 String type="";
	 JLabel profileImage;
	Point point = new Point();
	boolean resizing = false;
	 JFrame thisParent ;
	public AddUser(Frame parent,String username) {
		Constants.IS_DIALOG = true;
		this.parent = parent ;
		this.username = username;
		if(parent!=null){
			parent.setEnabled(false);
			
		}
		thisParent = this;
		setUndecorated(true);
		toFront();
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY,1,2,0));
		try {
			initUI();
		} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
				
	}
 public void initUI() throws JSONException, MalformedURLException, IOException{
	 
//	 Constants.loader.setVisible(true);
//	 thisParent.setEnabled(false);
	 final String loginToken = Constants.loggedinuserInfo.login_token;
	 final JPanel mainPanel = new JPanel();
	 final Box mainVbox  = Box.createVerticalBox();
	 final Box hHospBox = Box.createHorizontalBox();
	 final JPanel imagePanel = new JPanel();
	 final JPanel namePanel = new JPanel();
	 final JPanel desigPanel = new JPanel();
	 final JPanel jobTitlePanel = new JPanel();
	 final JPanel userNamePanel = new JPanel();
	 final JPanel emailPanel = new JPanel();
	 final JPanel phonePanel = new JPanel();
	 
	 
	 final JPanel buttonPanel = new JPanel();
	 final JPanel practicePanel = new JPanel();
	 final JPanel primaryHospPanel = new JPanel();
	 final JPanel addMePanel = new JPanel();
	
				 String response="";
				 String err_code="";
				 final JLabel  name = new JLabel("");
				 final JLabel jobTitle =  new JLabel("");;
				 final JLabel user_name =  new JLabel("");
				 final JLabel email =  new JLabel("");
				 final JLabel phone =  new JLabel("");
				 final JLabel practiceType =  new JLabel("");
				 final JLabel primaryHosp =  new JLabel("");
				 final JPanel linePanel = new JPanel();
				 //other hosp pending right now.
				 final JButton addMe = new JButton("Add Me");
				 final JButton removeUser = new JButton("Remove User");
				 UserProfileJSON userProfileJson = new UserProfileJSON();
				 response = userProfileJson.getUserProfile(username, loginToken);
				 final JSONObject obj = new JSONObject(response);
				
				 if(obj!=null){
					 err_code = obj.getString("err-code");
					 if(err_code.equals("1")){
//						 Constants.loader.setVisible(false);
//						 	thisParent.setEnabled(true);
						 System.out.println(obj.getString("name"));
						 try {
								//profileImage = new JLabel(new ImageIcon(image));
								 ///BufferedImage rounded = Util.makeRoundedCorner(image, 100);
								image = Util.getProfileImg(username);
								if(image==null ){
									image =  ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png")); 
									
								}
								 profileImage = Util.combine(image, false, 150, 150);
								 StringBuffer nameDesig = new StringBuffer();
								 StringBuffer practiceTypeBuffer = new StringBuffer();
								 StringBuffer primaryHospitalBuffer = new StringBuffer();
								 
								 nameDesig.append(obj.getString("name"));
								 if(!obj.getString("designation").equals("")){
									 nameDesig.append(", "+obj.getString("designation"));
								 }
								 if(!obj.getString("practice_type").equals("")){
									 practiceTypeBuffer.append(obj.getString("practice_type"));
								 }
								 if(!obj.getString("primary_hospital").equals("")){
									 primaryHospitalBuffer.append(obj.getString("primary_hospital"));
								 }
								 name.setText(nameDesig.toString());
								 name.setForeground(Color.decode("#9CCD21"));
								 name.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
								 jobTitle.setText(obj.getString("job_title"));
								 jobTitle.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
								 user_name.setText("("+obj.getString("user_name")+")");
								 user_name.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
								 type = obj.getString("type");
								 email.setText(obj.getString("email"));
								 email.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
								 phone.setText(obj.getString("phone"));
								 phone.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
								 privacy_enabled = obj.getString("privacy_enabled");
								 if(!type.equalsIgnoreCase("patient")){
									 practiceType.setText("<html><center>Practice Type: <br/>"+practiceTypeBuffer.toString()+"</html>");
									 practiceType.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
									 practiceType.setIcon(new ImageIcon(getClass().getResource("/images/practiceType_icon.png")));
									 practiceType.setForeground(Color.gray);
									 practicePanel.setOpaque(true);
									 practicePanel.setBackground(null);
									 practicePanel.add(practiceType);
									 primaryHosp.setText("<html><center>Primary Network <br/>"+primaryHospitalBuffer.toString()+"</html>");
									 primaryHosp.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
									 primaryHosp.setIcon(new ImageIcon(getClass().getResource("/images/hospital_primary.png")));
									 primaryHosp.setForeground(Color.gray);
									 primaryHospPanel.setOpaque(true);
									 primaryHospPanel.setBackground(null);
									 primaryHospPanel.add(primaryHosp);
								 }
								 namePanel.add(name);
								 namePanel.setOpaque(true);
								 namePanel.setBackground(null);
								 userNamePanel.add(user_name);
								 userNamePanel.setOpaque(true);
								 userNamePanel.setBackground(null);
								 
								 emailPanel.add(email);
								 emailPanel.setOpaque(true);
								 emailPanel.setBackground(null);
								 
								 phonePanel.add(phone);
								 phonePanel.setOpaque(true);
								 phonePanel.setBackground(null);
								 
								 jobTitlePanel.add(jobTitle);
								 jobTitlePanel.setOpaque(true);
								 jobTitlePanel.setBackground(null);
								
								 addMe.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
								 addMe.setBorder(new TextBubbleBorder(Color.decode("#9CCD21"),2,5,0));
								 if(obj.getBoolean("request_already_sent")==true ){
									 addMe.setText("Invitation Sent");
									 addMe.setEnabled(false);
									 BufferedImage master;
										try {
											master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_over.png"));
											Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
											addMe.setDisabledIcon(new ImageIcon(scaled));
											addMe.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
											removeUser.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
											addMe.setForeground(Color.decode("#9CCD21"));
											removeUser.setForeground(Color.white);
											Util.setTransparentBtn(addMe);
											removeUser.setIcon(new ImageIcon(scaled));
											Util.setTransparentBtn(removeUser);
										
										} catch (IOException e2) {
											e2.printStackTrace();
										}
								 }
								 BufferedImage master;
									try {
										master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
										Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
										addMe.setIcon(new ImageIcon(scaled));
										addMe.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
										removeUser.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
										addMe.setForeground(Color.white);
										removeUser.setForeground(Color.white);
										Util.setTransparentBtn(addMe);
										removeUser.setIcon(new ImageIcon(scaled));
										Util.setTransparentBtn(removeUser);
									} catch (IOException e2) {
										e2.printStackTrace();
									}
								 removeUser.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
								 addMe.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										String sendInvite = AddUserToRoster(username);
										if(!sendInvite.equals(""))
										{
											JOptionPane.showMessageDialog(getRootPane(), "Your invitation has been sent!");
											dispose();
										}
											
									}
								});
								 
								 addMePanel.add(addMe);
								 addMePanel.setBackground(null);
								 linePanel.setBackground(Color.white);
								 linePanel.setOpaque(true);
								 linePanel.setPreferredSize(new Dimension(getWidth(),2));
								 linePanel.setBorder(BorderFactory.createMatteBorder(0,0, 2,0, Color.LIGHT_GRAY));
								 imagePanel.setBackground(null);
								 imagePanel.add(profileImage);
								 if(!type.equalsIgnoreCase("patient")){
								 hHospBox.add(practicePanel);
								 practicePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, Color.LIGHT_GRAY));
								 hHospBox.add(primaryHospPanel);
								 }
								 hHospBox.setOpaque(true);
								 hHospBox.setBackground(Color.white);
							} catch (JSONException | IOException e) {
								e.printStackTrace();
							}
						
						 mainVbox.add(imagePanel);
						 mainVbox.add(Box.createVerticalStrut(20));
						 mainVbox.add(namePanel);
						 mainVbox.add(Box.createVerticalStrut(5));
						 mainVbox.add(jobTitlePanel);
						 mainVbox.add(Box.createVerticalStrut(5));
						 mainVbox.add(userNamePanel);
						 mainVbox.add(Box.createVerticalStrut(5));
						 if(privacy_enabled.equals("0")){
						 mainVbox.add(phonePanel);
						 mainVbox.add(Box.createVerticalStrut(5));
						 mainVbox.add(emailPanel);
						 }
						 mainVbox.add(Box.createVerticalStrut(5));
						 mainVbox.add(addMePanel);
						 mainVbox.add(Box.createVerticalStrut(10));
						 
						 if(!type.equalsIgnoreCase("patient")){
						 mainVbox.add(linePanel);
						 mainVbox.add(Box.createVerticalStrut(10));
						 mainVbox.add(hHospBox);
					 	 }
						 mainPanel.add(mainVbox,BorderLayout.CENTER);
					 }
					 	LightScrollPane sc = new LightScrollPane(mainPanel);
						int x = (Constants.SCREEN_SIZE.width)/8;
						int y = (Constants.SCREEN_SIZE.height)/6;
						setBounds(x,y,Constants.SCREEN_SIZE.width/2,Constants.SCREEN_SIZE.height/2);
					 	mainVbox.setOpaque(true);
					 	mainVbox.setBackground(null);
					 	mainPanel.setOpaque(true);
					 	mainPanel.setBackground(Color.white);
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
						sc.addMouseListener(new MouseAdapter() {
						      public void mousePressed(MouseEvent e) {
						        resizing = getCursor().equals(Cursor.getDefaultCursor())? false:true;
						        if(!e.isMetaDown()){
						          point.x = e.getX();
						          point.y = e.getY();
						        }
						      }
						    });
						sc.addMouseMotionListener(new MouseMotionAdapter() {
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
						setLocationRelativeTo(getParent());
						getContentPane().setBackground(Color.white);
						setTitle("User Profile");
						setIconImage(new ImageIcon(getClass().getResource("/images/logoicon.png")).getImage());
						setMinimumSize(new Dimension((int) (Math.round(Constants.SCREEN_SIZE.width * 0.60)), (int) (Math.round(Constants.SCREEN_SIZE.height * 0.70))));
						add(TopPanel.topButtonPanelForDialog(this, (JFrame) parent),BorderLayout.NORTH);
						add(sc,BorderLayout.CENTER);
						pack();
				 }
				 
			
		   	
 }
 	private String AddUserToRoster(String userName){
 		String response = "";
 		SendInvitationJSON inviteJSON = new SendInvitationJSON();
 		
 		 try {
 			response = inviteJSON.sendInvite(userName);
			JSONObject obj = new JSONObject(response);
			
			String err_code = obj.getString("err-code");
			if(err_code.equals("404")){
				return "User name not found";
			} 
			else if(err_code.equals("600")){
				return "Your login session expired.Please login again.";
			}
			else if(err_code.equals("700")){
				return "This feature is disabled. Please contact customer service.";
			}
			else if(err_code.equals("300")){
				return "Unable to proceed.Please try again later.";
			}
			else if(err_code.equals("1")){
				return obj.getString("message");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return "";
 }
 	private String RemoveUserFromRoster(String userName){
 		String response = "";
 		SendInvitationJSON inviteJSON = new SendInvitationJSON();
 		
 		 try {
 			response = inviteJSON.removeUser(userName);
			JSONObject obj = new JSONObject(response);
			
			String err_code = obj.getString("err-code");
			 if(err_code.equals("600")){
				return "Your login session expired.Please login again.";
			}
			else if(err_code.equals("300")){
				return "Unable to proceed.Please try again later.";
			}
			else if(err_code.equals("1")){
				return obj.getString("message");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return "";
 }
}

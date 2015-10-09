package com.im.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.im.common.Footer;
import com.im.common.TopPanel;
import com.im.utils.Constants;
import com.im.utils.TextBubbleBorder;

public class NeedHelp extends JFrame {
	/**
	 * 
	 */
	private Box box;
	private static final long serialVersionUID = 1L;
	private JLabel labelEmail;
	private JLabel labelContact;
	
	private JPanel emailPanel;
	private JPanel contactPanel;
	private JTextField textEmail;
	private JTextField textContact;
	private JLabel topIcon;
	Frame parent;
	Point point = new Point();
	boolean resizing = false;
	public NeedHelp(Frame parent) {
		// TODO Auto-generated constructor stub
		try {
			Constants.IS_DIALOG = true;
			this.parent = parent ;
			parent.setEnabled(false);
			setUndecorated(true);
			Constants.PARENT = parent;
			initHelp();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initHelp() throws IOException{
		emailPanel = new JPanel();
		contactPanel = new JPanel();
		topIcon = new JLabel(
				new ImageIcon(
						((new ImageIcon(getClass().getResource("/images/logo.png"))).getImage())
								.getScaledInstance(200, 250,
										java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(null);
		logoPanel.setOpaque(true);
		logoPanel.add(topIcon,BorderLayout.CENTER);
		labelEmail = new JLabel(new ImageIcon(((new ImageIcon(
				getClass().getResource("/images/email.png"))).getImage()).getScaledInstance(50,
				40, java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);
		labelEmail.setHorizontalAlignment(SwingConstants.CENTER);
		labelEmail.setOpaque(true);
		labelEmail.setBackground(null);
		
		emailPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60), 50));
		emailPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.lightGray));
		labelEmail.setOpaque(true);
		labelEmail.setBackground(Color.white);
		
		labelContact = new JLabel(new ImageIcon(((new ImageIcon(
				getClass().getResource("/images/contact.png"))).getImage()).getScaledInstance(50,
				40, java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);
		
		labelContact.setHorizontalAlignment(SwingConstants.CENTER);
		labelContact.setOpaque(true);
		labelContact.setBackground(null);
		// Box.createHorizontalBox();
		contactPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60), 50));
		contactPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0,
				Color.lightGray));
		labelContact.setOpaque(true);
		labelContact.setBackground(Color.white);
		
		textEmail = new JTextField(20);
		textContact = new JTextField(20);
		textEmail.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 20));
		textEmail.setText("support@imyourdoc.com");
		textContact.setText("1(800) 409 - 8078");
		textContact.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 20));
		textEmail.setForeground(Color.LIGHT_GRAY);
		textContact.setForeground(Color.LIGHT_GRAY);
		textEmail.setBorder(null);
		textContact.setBorder(null);
		textEmail.setEditable(false);
		textContact.setEditable(false);
		textEmail.setOpaque(true);
		textEmail.setBackground(null);
		textContact.setBackground(null);
		
		emailPanel.add(Box.createHorizontalStrut(100));
		emailPanel.add(labelEmail, BorderLayout.CENTER);
		emailPanel.add(Box.createHorizontalStrut(5));
		emailPanel.add(textEmail, BorderLayout.CENTER);
		
		contactPanel.add(Box.createHorizontalStrut(100));
		contactPanel.add(labelContact, BorderLayout.CENTER);
		contactPanel.add(Box.createHorizontalStrut(5));
		contactPanel.add(textContact, BorderLayout.CENTER);
		
		box = Box.createVerticalBox();
		box.add(logoPanel, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut(50));
		box.add(emailPanel, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut(10));
		box.add(contactPanel, BorderLayout.CENTER);
		box.add(Box.createVerticalStrut(10));
		emailPanel.setBackground(null);
		emailPanel.setOpaque(true);
		contactPanel.setOpaque(true);
		contactPanel.setBackground(null);
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.add(box);
		panel.setBackground(Color.white.brighter());
		getContentPane().setBackground(Color.white);
		add(panel, BorderLayout.CENTER);
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 2, 2, 0));
		add(TopPanel.topButtonPanelForDialog(this,(JFrame) parent),BorderLayout.NORTH);
		//add(new Footer().loginLowerBox(), BorderLayout.SOUTH);
		setTitle("Need Help");
		setIconImage(new ImageIcon(getClass().getResource("/images/logoicon.png")).getImage());
		  int x = (Constants.SCREEN_SIZE.width)/8;
			 int y = (Constants.SCREEN_SIZE.height)/8;
			  setBounds(x,y,Constants.SCREEN_SIZE.width/2,Constants.SCREEN_SIZE.height/2);
		    setMinimumSize(new Dimension((int) (Math.round(Constants.SCREEN_SIZE.width * 0.60)), (int) (Math.round(Constants.SCREEN_SIZE.height * 0.70))));
		setLocationRelativeTo(getParent());
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
	
}

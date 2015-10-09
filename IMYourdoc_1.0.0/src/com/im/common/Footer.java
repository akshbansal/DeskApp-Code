package com.im.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.im.utils.Constants;
import com.im.utils.Util;
import com.im.utils.XmppUtils;

public class Footer {

	public Box loginLowerBox() throws IOException {

		JLabel labelSecureLogin = new JLabel("Securely Connected");
		labelSecureLogin.setForeground(Color.DARK_GRAY);
		labelSecureLogin.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		labelSecureLogin.setIcon(new ImageIcon(getClass().getResource("/images/secure_icon.png")));
		
	
		labelSecureLogin.setVerticalAlignment(SwingConstants.CENTER);
		labelSecureLogin.setHorizontalAlignment(SwingConstants.LEFT);
		Box lowerBox =Box.createVerticalBox();
		lowerBox.add(labelSecureLogin,BorderLayout.WEST);
		lowerBox.add(Box.createHorizontalStrut(20));
		lowerBox.add(Box.createVerticalStrut(20));
		lowerBox.setBackground(null);
		return lowerBox;
	}
	
	public Box commonLowerBox() throws IOException {
		Box box = Box.createHorizontalBox();
		final JLabel labelSecureLogin = new JLabel("Securely Connected");
		labelSecureLogin.setForeground(Color.DARK_GRAY);
		labelSecureLogin.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		labelSecureLogin.setIcon(new ImageIcon(getClass().getResource("/images/secure_icon.png")));
		JPanel lowerBox = new JPanel(new BorderLayout());//Box.createHorizontalBox();
		
		TimerTask task = new TimerTask() {
		      @Override
		      public void run() {
		        // task to run goes here
						  if(!XmppUtils.connection.isConnected() && Constants.apiIsConnected == false){
					    	  labelSecureLogin.setText("Not Connected");
					    	  labelSecureLogin.setIcon(new ImageIcon(getClass().getResource("/images/secure_icon_red.png")));
					    	  labelSecureLogin.repaint();
					    	  labelSecureLogin.revalidate();
						  }
						  else{
							  labelSecureLogin.setIcon(new ImageIcon(getClass().getResource("/images/secure_icon.png")));
							  labelSecureLogin.setText("Securely Connected");
							  labelSecureLogin.revalidate();
						  }
		      }
		    };
		    Timer timer = new Timer();
		    long delay = 0;
		    long intevalPeriod = 1 * 1000; 
		    // schedules the task to be run in an interval 
		    timer.scheduleAtFixedRate(task, delay,
		                                intevalPeriod);
		
		labelSecureLogin.setVerticalAlignment(SwingConstants.CENTER);
		labelSecureLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lowerBox.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.decode("#F2F1EA")));
		lowerBox.add(labelSecureLogin,BorderLayout.CENTER);
		lowerBox.setBackground(null);
		box.add(lowerBox,BorderLayout.WEST);
		box.add(Box.createVerticalStrut(30));
		return box;
	}
	public Box commonLowerBoxRed() throws IOException {
		Box box = Box.createHorizontalBox();
		JLabel labelSecureLogin = new JLabel("Not connected");
		labelSecureLogin.setForeground(Color.DARK_GRAY);
		labelSecureLogin.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		labelSecureLogin.setIcon(new ImageIcon(getClass().getResource("/images/secure_icon.png")));
		JPanel lowerBox = new JPanel(new BorderLayout());//Box.createHorizontalBox();
		labelSecureLogin.setVerticalAlignment(SwingConstants.CENTER);
		labelSecureLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lowerBox.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.decode("#F2F1EA")));
		lowerBox.add(labelSecureLogin,BorderLayout.CENTER);
		lowerBox.setBackground(null);
		box.add(lowerBox,BorderLayout.WEST);
		box.add(Box.createVerticalStrut(30));
		return box;
	}
}

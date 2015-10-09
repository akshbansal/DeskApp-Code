package com.im.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;

import com.im.login.WelcomeScreen;
import com.im.utils.Constants;
import com.im.utils.Util;
import com.im.utils.XmppUtils;

public class TopPanel {
	static JButton maxButton;
	static JButton minButton;
	static JButton cross;
	public TopPanel() {
		// TODO Auto-generated constructor stub
		
	}

	public static JPanel topButtonPanel(final JFrame frame,final boolean isLoginScreen){
		
		JPanel top = new JPanel(new BorderLayout());
		Box buttonPanel = Box.createHorizontalBox();
		top.setBackground(null);
		top.setOpaque(true);

		buttonPanel.setBackground(null);
		buttonPanel.setOpaque(true);

		cross = new JButton("X");
		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		Util.setTransparentBtn(cross);
		Util.setTransparentBtn(maxButton);
		Util.setTransparentBtn(minButton);
		cross.setMaximumSize(new Dimension(40, 40));
		cross.setMinimumSize(new Dimension(40, 40));
		cross.setPreferredSize(new Dimension(40,40));
		
		cross.setBackground(Color.red);
		cross.setForeground(Color.white.brighter());
		cross.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 20));
		
		cross.setOpaque(true);
		cross.setBorderPainted(false);
		cross.setBorder(null);
		cross.setFocusPainted(false);
		minButton.setMaximumSize(new Dimension(40, 40));
		minButton.setMinimumSize(new Dimension(40, 40));
		minButton.setPreferredSize(new Dimension(40, 40));
		minButton.setBackground(Color.decode("#ABAAAF"));
		minButton.setOpaque(true);
		minButton.setFocusPainted(false);
		minButton.setBorderPainted(false);
		minButton.setBorder(null);

		maxButton.setMaximumSize(new Dimension(40, 40));
		maxButton.setMinimumSize(new Dimension(40, 40));
		maxButton.setPreferredSize(new Dimension(40, 40));
		maxButton.setBackground(Color.decode("#ABAAAF"));
		maxButton.setOpaque(true);
		maxButton.setFocusPainted(false);
		maxButton.setBorderPainted(false);
		maxButton.setBorder(null);
		minButton.setVisible(false);
		maxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				minButton.setVisible(true);
				maxButton.setVisible(false);
			}
		});

		minButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.NORMAL);
				minButton.setVisible(false);
				maxButton.setVisible(true);
			}
		});
		cross.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(isLoginScreen)
				{
					System.exit(0);
					Runtime.getRuntime().exit(0);
				}
				else{
					frame.setExtendedState(JFrame.ICONIFIED);
				}
			}
		});
		buttonPanel.add(minButton);
		buttonPanel.add(maxButton);
		buttonPanel.add(cross);
		top.add(buttonPanel, BorderLayout.EAST);
		return top;
	}
	public static JPanel topBarForInfo(final JFrame frame){
		JPanel top = new JPanel(new BorderLayout());
		Box buttonPanel = Box.createHorizontalBox();
		top.setBackground(null);
		top.setOpaque(true);

		buttonPanel.setBackground(null);
		buttonPanel.setOpaque(true);
		cross = new JButton("X");
		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		Util.setTransparentBtn(cross);
		Util.setTransparentBtn(maxButton);
		Util.setTransparentBtn(minButton);
		cross.setMaximumSize(new Dimension(30,30));
		cross.setMinimumSize(new Dimension(30,30));
		cross.setPreferredSize(new Dimension(30,30));
		
		cross.setBackground(Color.red.darker());
		cross.setForeground(Color.white.brighter());
		cross.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.BOLD, 16));
		
		cross.setOpaque(true);
		cross.setBorderPainted(false);
		cross.setBorder(null);
		cross.setFocusPainted(false);
		
		cross.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispose();
			}
		});
		buttonPanel.add(cross);
		top.add(buttonPanel, BorderLayout.EAST);
		return top;
	}
	
	public static JPanel topButtonPanelForAccountDialog(final JFrame frame,final String title){
		JPanel top = new JPanel(new BorderLayout());
		JLabel labelTitle = new JLabel("<html><center><br/>"+title+"</center></html>");
		labelTitle.setBackground(null);
		labelTitle.setOpaque(true);
		labelTitle.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 24));
		cross = new JButton("X");
		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		Util.setTransparentBtn(cross);
		Util.setTransparentBtn(maxButton);
		Util.setTransparentBtn(minButton);
		labelTitle.setForeground(Color.white.brighter());
		labelTitle.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		Box buttonPanel = Box.createHorizontalBox();
		labelTitle.setHorizontalTextPosition(JLabel.CENTER);
		labelTitle.setVerticalTextPosition(JLabel.CENTER);
		top.setBackground(Color.decode("#9CCD21"));
		top.setOpaque(true);
		
		buttonPanel.setBackground(null);
		buttonPanel.setOpaque(true);
		
//		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
//		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		cross.setMaximumSize(new Dimension(40, 40));
		cross.setMinimumSize(new Dimension(40, 40));
		cross.setPreferredSize(new Dimension(40, 40));

		cross.setBackground(Color.red);
		cross.setForeground(Color.white.brighter());
		cross.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 20));
		cross.setOpaque(true);
		cross.setFocusPainted(false);
		cross.setBorder(null);

/*//		minButton.setMaximumSize(new Dimension(30, 30));
//		minButton.setMinimumSize(new Dimension(30, 30));
//		minButton.setPreferredSize(new Dimension(30, 30));
//		minButton.setBackground(Color.decode("#ABAAAF"));
//		minButton.setOpaque(true);
//		minButton.setFocusPainted(false);
//		minButton.setBorder(null);
//
//		maxButton.setMaximumSize(new Dimension(30, 30));
//		maxButton.setMinimumSize(new Dimension(30, 30));
//		maxButton.setPreferredSize(new Dimension(30, 30));
//		maxButton.setBackground(Color.decode("#ABAAAF"));
//		maxButton.setOpaque(true);
//		maxButton.setFocusPainted(false);
//		maxButton.setBorder(null);
//		minButton.setVisible(false);
		maxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				minButton.setVisible(true);
				maxButton.setVisible(false);
			}
		});

		minButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.NORMAL);
				minButton.setVisible(false);
				maxButton.setVisible(true);
			}
		});*/
		cross.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setVisible(false);
				Constants.PARENT.setEnabled(true);
				Constants.PARENT.toFront();
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
				else
				{
					if(!title.toLowerCase().contains("_")){
						if(!title.toLowerCase().contains("@")){
							XmppUtils.sendPendingReadStatus(title.toLowerCase()+"@imyourdoc.com");
						}
						else
						{
							XmppUtils.sendPendingReadStatus(title.toLowerCase());
						}
					}
				}
			}
		});
//		buttonPanel.add(minButton);
//		buttonPanel.add(maxButton);
		buttonPanel.add(cross);
		JPanel panelTitle = new JPanel();
		panelTitle.add(labelTitle);
		labelTitle.setVerticalTextPosition(SwingConstants.CENTER);
//		panelTitle.setAlignmentY(SwingConstants.VERTICAL);
		panelTitle.setOpaque(true);
		panelTitle.setBackground(null);
		top.add(buttonPanel, BorderLayout.EAST);
		top.add(panelTitle,BorderLayout.CENTER);
		return top;
	}
	public static JPanel topButtonPanelForTermsDialog(final JFrame frame,String title,final JFrame parent){
		JPanel top = new JPanel(new BorderLayout());
		JLabel labelTitle = new JLabel("<html><center><br/>"+title+"</center></html>");
		labelTitle.setBackground(null);
		labelTitle.setOpaque(true);
		labelTitle.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 24));
		cross = new JButton("X");
		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		Util.setTransparentBtn(cross);
		Util.setTransparentBtn(maxButton);
		Util.setTransparentBtn(minButton);
		labelTitle.setForeground(Color.white.brighter());
		labelTitle.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		Box buttonPanel = Box.createHorizontalBox();
		labelTitle.setHorizontalTextPosition(JLabel.CENTER);
		labelTitle.setVerticalTextPosition(JLabel.CENTER);
		top.setBackground(Color.decode("#9CCD21"));
		top.setOpaque(true);
		
		buttonPanel.setBackground(null);
		buttonPanel.setOpaque(true);

//		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
//		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		cross.setMaximumSize(new Dimension(40, 40));
		cross.setMinimumSize(new Dimension(40, 40));
		cross.setPreferredSize(new Dimension(40, 40));

		cross.setBackground(Color.red);
		cross.setForeground(Color.white.brighter());
		cross.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 20));
		cross.setOpaque(true);
		cross.setFocusPainted(false);
		cross.setBorder(null);

/*//		minButton.setMaximumSize(new Dimension(30, 30));
//		minButton.setMinimumSize(new Dimension(30, 30));
//		minButton.setPreferredSize(new Dimension(30, 30));
//		minButton.setBackground(Color.decode("#ABAAAF"));
//		minButton.setOpaque(true);
//		minButton.setFocusPainted(false);
//		minButton.setBorder(null);
//
//		maxButton.setMaximumSize(new Dimension(30, 30));
//		maxButton.setMinimumSize(new Dimension(30, 30));
//		maxButton.setPreferredSize(new Dimension(30, 30));
//		maxButton.setBackground(Color.decode("#ABAAAF"));
//		maxButton.setOpaque(true);
//		maxButton.setFocusPainted(false);
//		maxButton.setBorder(null);
//		minButton.setVisible(false);
		maxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				minButton.setVisible(true);
				maxButton.setVisible(false);
			}
		});

		minButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.NORMAL);
				minButton.setVisible(false);
				maxButton.setVisible(true);
			}
		});*/
		cross.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setVisible(false);
				parent.setEnabled(true);
				parent.toFront();
			}
		});
//		buttonPanel.add(minButton);
//		buttonPanel.add(maxButton);
		buttonPanel.add(cross);
		JPanel panelTitle = new JPanel();
		panelTitle.add(labelTitle);
		labelTitle.setVerticalTextPosition(SwingConstants.CENTER);
//		panelTitle.setAlignmentY(SwingConstants.VERTICAL);
		panelTitle.setOpaque(true);
		panelTitle.setBackground(null);
		top.add(buttonPanel, BorderLayout.EAST);
		top.add(panelTitle,BorderLayout.CENTER);
		return top;
	}
	public static JPanel topButtonPanelForAccountDialog(final JDialog frame,String title){
		JPanel top = new JPanel(new BorderLayout());
		JLabel labelTitle = new JLabel("<html><center>"+title+"</center></html>");
		labelTitle.setBackground(null);
		labelTitle.setOpaque(true);
		labelTitle.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 24));
		cross = new JButton("X");
		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		Util.setTransparentBtn(cross);
		Util.setTransparentBtn(maxButton);
		Util.setTransparentBtn(minButton);
		labelTitle.setForeground(Color.white.brighter());
		labelTitle.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		Box buttonPanel = Box.createHorizontalBox();
		labelTitle.setVerticalAlignment(SwingConstants.VERTICAL);
		top.setBackground(Color.decode("#9CCD21"));
		top.setOpaque(true);
		
		buttonPanel.setBackground(null);
		buttonPanel.setOpaque(true);

//		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
//		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		cross.setMaximumSize(new Dimension(40, 40));
		cross.setMinimumSize(new Dimension(40, 40));
		cross.setPreferredSize(new Dimension(40, 40));

		cross.setBackground(Color.red);
		cross.setForeground(Color.white.brighter());
		cross.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 20));
		cross.setOpaque(true);
		cross.setFocusPainted(false);
		cross.setBorder(null);

/*//		minButton.setMaximumSize(new Dimension(30, 30));
//		minButton.setMinimumSize(new Dimension(30, 30));
//		minButton.setPreferredSize(new Dimension(30, 30));
//		minButton.setBackground(Color.decode("#ABAAAF"));
//		minButton.setOpaque(true);
//		minButton.setFocusPainted(false);
//		minButton.setBorder(null);
//
//		maxButton.setMaximumSize(new Dimension(30, 30));
//		maxButton.setMinimumSize(new Dimension(30, 30));
//		maxButton.setPreferredSize(new Dimension(30, 30));
//		maxButton.setBackground(Color.decode("#ABAAAF"));
//		maxButton.setOpaque(true);
//		maxButton.setFocusPainted(false);
//		maxButton.setBorder(null);
//		minButton.setVisible(false);
		maxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				minButton.setVisible(true);
				maxButton.setVisible(false);
			}
		});

		minButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.NORMAL);
				minButton.setVisible(false);
				maxButton.setVisible(true);
			}
		});*/
		cross.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Constants.mainFrame.setEnabled(true);
				Constants.mainFrame.toFront();
				frame.setVisible(false);			
			}
		});
		buttonPanel.add(cross);
		JPanel panelTitle = new JPanel();
		panelTitle.add(labelTitle);
		labelTitle.setVerticalTextPosition(SwingConstants.CENTER);
//		panelTitle.setAlignmentY(SwingConstants.VERTICAL);
		panelTitle.setOpaque(true);
		panelTitle.setBackground(null);
		top.add(buttonPanel, BorderLayout.EAST);
		top.add(panelTitle,BorderLayout.CENTER);
		return top;
	}
	public static JPanel topButtonPanelForAccountDialogWithoutCross(String title){
		JPanel top = new JPanel(new BorderLayout());
		JLabel labelTitle = new JLabel("<html><center>"+title+"</center></html>");
		labelTitle.setBackground(null);
		labelTitle.setOpaque(true);
		labelTitle.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 24));
		cross = new JButton("X");
		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		Util.setTransparentBtn(cross);
		Util.setTransparentBtn(maxButton);
		Util.setTransparentBtn(minButton);
		labelTitle.setForeground(Color.white.brighter());
		labelTitle.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		Box buttonPanel = Box.createHorizontalBox();
		labelTitle.setVerticalAlignment(SwingConstants.VERTICAL);
		top.setBackground(Color.decode("#9CCD21"));
		top.setOpaque(true);
		
		buttonPanel.setBackground(null);
		buttonPanel.setOpaque(true);

//		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
//		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		cross.setMaximumSize(new Dimension(40, 40));
		cross.setMinimumSize(new Dimension(40, 40));
		cross.setPreferredSize(new Dimension(40, 40));

		cross.setBackground(Color.red);
		cross.setForeground(Color.white.brighter());
		cross.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 20));
		cross.setOpaque(true);
		cross.setFocusPainted(false);
		cross.setBorder(null);

/*//		minButton.setMaximumSize(new Dimension(30, 30));
//		minButton.setMinimumSize(new Dimension(30, 30));
//		minButton.setPreferredSize(new Dimension(30, 30));
//		minButton.setBackground(Color.decode("#ABAAAF"));
//		minButton.setOpaque(true);
//		minButton.setFocusPainted(false);
//		minButton.setBorder(null);
//
//		maxButton.setMaximumSize(new Dimension(30, 30));
//		maxButton.setMinimumSize(new Dimension(30, 30));
//		maxButton.setPreferredSize(new Dimension(30, 30));
//		maxButton.setBackground(Color.decode("#ABAAAF"));
//		maxButton.setOpaque(true);
//		maxButton.setFocusPainted(false);
//		maxButton.setBorder(null);
//		minButton.setVisible(false);
		maxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				minButton.setVisible(true);
				maxButton.setVisible(false);
			}
		});

		minButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.NORMAL);
				minButton.setVisible(false);
				maxButton.setVisible(true);
			}
		});*/
		
//		buttonPanel.add(minButton);
//		buttonPanel.add(maxButton);
		buttonPanel.add(cross);
		JPanel panelTitle = new JPanel();
		panelTitle.add(labelTitle);
		labelTitle.setVerticalTextPosition(SwingConstants.CENTER);
//		panelTitle.setAlignmentY(SwingConstants.VERTICAL);
		panelTitle.setOpaque(true);
		panelTitle.setBackground(null);
		//top.add(buttonPanel, BorderLayout.EAST);
		top.add(panelTitle,BorderLayout.CENTER);
		return top;
	}
	public static JPanel topButtonPanelForDialog(final JFrame frame,final JFrame parent){
		JPanel top = new JPanel(new BorderLayout());
		Box buttonPanel = Box.createHorizontalBox();
		top.setBackground(null);
		top.setOpaque(true);

		buttonPanel.setBackground(null);
		buttonPanel.setOpaque(true);
		cross = new JButton("X");
		maxButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/maximise.png")));
		minButton = new JButton(new ImageIcon(TopPanel.class.getClass().getResource("/images/minimise.png")));
		Util.setTransparentBtn(cross);
		Util.setTransparentBtn(maxButton);
		Util.setTransparentBtn(minButton);
		cross.setMaximumSize(new Dimension(30, 30));
		cross.setMinimumSize(new Dimension(30, 30));
		cross.setPreferredSize(new Dimension(30, 30));

		cross.setBackground(Color.red);
		cross.setForeground(Color.white.brighter());
		cross.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 20));
		cross.setOpaque(true);
		cross.setFocusPainted(false);
		cross.setBorder(null);

		minButton.setMaximumSize(new Dimension(30, 30));
		minButton.setMinimumSize(new Dimension(30, 30));
		minButton.setPreferredSize(new Dimension(30, 30));
		minButton.setBackground(Color.decode("#ABAAAF"));
		minButton.setOpaque(true);
		minButton.setFocusPainted(false);
		minButton.setBorder(null);

		maxButton.setMaximumSize(new Dimension(30, 30));
		maxButton.setMinimumSize(new Dimension(30, 30));
		maxButton.setPreferredSize(new Dimension(30, 30));
		maxButton.setBackground(Color.decode("#ABAAAF"));
		maxButton.setOpaque(true);
		maxButton.setFocusPainted(false);
		maxButton.setBorder(null);
		minButton.setVisible(false);
		maxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				minButton.setVisible(true);
				maxButton.setVisible(false);
			}
		});

		minButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setExtendedState(JFrame.NORMAL);
				minButton.setVisible(false);
				maxButton.setVisible(true);
			}
		});
		cross.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.dispose();
				parent.toFront();
				parent.setEnabled(true);
			}
		});
		buttonPanel.add(minButton);
		buttonPanel.add(maxButton);
		buttonPanel.add(cross);
		top.add(buttonPanel, BorderLayout.EAST);
		return top;
	}
}

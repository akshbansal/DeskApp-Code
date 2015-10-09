package com.im.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

import com.im.bo.RosterVCardBo;
import com.im.common.TopPanel;
import com.im.db.DBServiceResult;
import com.im.patientscreens.UserWelcomeScreen;

public class PopUpMessage {

    private int height = 0;
    private Rectangle screenRect = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getMaximumWindowBounds();
    private JLabel topIcon;
	private JPanel topPanel;
    private JPanel panel = new JPanel();
    private JLabel text;
    private JLabel messageSMS;
    private String messageFull;
    private static JTextArea textArea = new JTextArea();
    final JButton plus = new JButton("Plus");
    final JButton reply = new JButton("Reply");
    final JButton close = new JButton("Exit");
    final JButton send = new JButton("Send");
    private int secondClick = 0;
    boolean isGroup = false;
    private static JScrollPane scrollPaneTextArea = new JScrollPane(textArea);
    private Dimension panelDimension = new Dimension(500,150);
    int count;
    JFrame parent;
    String groupId;
    String userId;
    String messageId;
    public PopUpMessage(int count,JFrame parent,String userId,boolean isGroup,String groupId,String messageId) {
    	this.count = count;
    	this.parent = parent;
    	this.userId= userId;
    	this.isGroup = isGroup;
    	this.groupId = groupId;
    	this.messageId = messageId;
 	}
    private JFrame dialog = new JFrame() {

        /**
     * 
     */
        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics g) {
            g.setClip(0, 0, getWidth(), height);
            super.paint(g);
        }
    };

    private Timer timer = new Timer(1, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            height += 5;
            if (height == dialog.getHeight()) {
                timer.stop();
            }
            dialog.setLocation(screenRect.width - dialog.getWidth(),
                    (int)(Constants.SCREEN_SIZE.width*0.50)- dialog.getWidth());
            dialog.repaint();
        }
    });

 

	public void makeUI(String message) {
		topIcon = new JLabel(new ImageIcon(((new ImageIcon(getClass().getResource("/images/logo_new.png"))).getImage())
				.getScaledInstance(100, 100,java.awt.Image.SCALE_SMOOTH)),JLabel.HORIZONTAL);
		topPanel = new JPanel();
		topPanel.add(topIcon, BorderLayout.CENTER);
		topPanel.setOpaque(true);
		topPanel.setBackground(Color.white);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(1, 0, 0, 0));
        dialog.setAlwaysOnTop(true);
        dialog.toFront();
        panel.setPreferredSize(panelDimension);
        panel.setBackground(Color.white);
       
        panel.add(topIcon);
        //panel.setLayout(null);
       // dialog.setContentPane(panel);

       

//        plus.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (secondClick == 0) {
//                    FontMetrics fm = panel.getFontMetrics(messageSMS.getFont());
//                    int sizeMessage = fm.stringWidth(messageSMS.getText());
//                    int decallage = (sizeMessage / 90) * 15;
//                    height = dialog.getHeight() + decallage - fm.getHeight()*3;
//                    panel.setPreferredSize(new Dimension(panel.getWidth(),
//                            panel.getHeight() + decallage - fm.getHeight()*3));
//                    messageSMS.setText("<html><P ALIGN=\"JUSTIFY\">"
//                            + messageFull);
//                    dialog.setLocation(dialog.getX(), dialog.getY() - decallage
//                            + fm.getHeight()*3);
//                    close.setBounds(close.getX(), close.getY() + decallage - fm.getHeight()*3, close.getWidth(), close.getHeight());
//                    send.setBounds(send.getX(), send.getY() + decallage - fm.getHeight()*3, send.getWidth(), send.getHeight());
//
//                    reply.setBounds(reply.getX(), reply.getY() + decallage - fm.getHeight()*3, reply.getWidth(), reply.getHeight());
//                    panel.remove(plus);
//                    scrollPaneTextArea.setBounds(scrollPaneTextArea.getX(), scrollPaneTextArea.getY()+ decallage - fm.getHeight()*3, scrollPaneTextArea.getWidth(), scrollPaneTextArea.getHeight());
//                    messageSMS.setBounds(messageSMS.getX(), messageSMS.getY(), messageSMS.getWidth(), decallage);
//                    dialog.pack();
//                }
//            }
//        });
//        plus.setBounds(105, 70, 90, 25);
//
//        send.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                timer2.setInitialDelay(0);
//                timer2.start();
//            }
//        });

        messageFull = message;
        //text = new JLabel("New message from :  " + number);
       //FontMetrics fm = panel.getFontMetrics(text.getFont());
        messageSMS = new JLabel();
       // int textSize = fm.stringWidth(text.getText());

     //   text.setBounds((panelDimension.width - textSize) / 2, 0, textSize, 25);

      //  textSize = fm.stringWidth(messageSMS.getText());
      // if (textSize > 810) {
       //     textSize = 290;
      //  }
//        messageSMS.setBounds((panelDimension.width - textSize) / 2, 20,
//                textSize, fm.getHeight()*3);

       
        //panel.add(reply);
//        if (fm.stringWidth(messageSMS.getText()) > 870) {
//            int sizeChar = fm.stringWidth(messageSMS.getText())
//                    / messageSMS.getText().length();
//            int allowedChar = 870 / sizeChar;
//          
//        }
        String temp = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 200, message);
        messageSMS.setText("<html><center>"+temp+"</center></html>");
        messageSMS.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
        messageSMS.setForeground(Color.red.darker());
        panel.add(messageSMS,BorderLayout.CENTER);
        dialog.getContentPane().setBackground(Color.white);
        dialog.toFront();
        dialog.getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY,2,2,0));
        dialog.add(TopPanel.topButtonPanelForAccountDialog(dialog, userId.toUpperCase().split("@")[0]),BorderLayout.NORTH);
        dialog.getContentPane().addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
				Constants.mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				Constants.mainFrame.toFront();
				Constants.mainFrame.setVisible(true);
				dialog.dispose();
				Constants.mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				if(!isGroup){
					RosterVCardBo vCard = XmppUtils.getVCardBo(userId);//new DBServiceResult().getRosterUserDetails(userId);
					WelcomeUtils.openChatWindow(vCard, false);
					if(!StringUtils.isEmpty(messageId)){
						if(!userId.contains("@")){
							userId = userId+"@imyourdoc.com";
							XmppUtils.getMsgEventManager().sendDisplayedNotification(userId, messageId);
							XmppUtils.sendPendingReadStatus(userId);
						}
						
					}
				}
				else
				{
					RoomInfo info;
					try {
						info = MultiUserChat.getRoomInfo(XmppUtils.connection, groupId);
						String subject = "";
						if(info!=null){
							subject = info.getSubject();
						}
						else
							subject = groupId.split("@")[0];
						RosterVCardBo vCard = XmppUtils.createGroupVCardBO(groupId, subject, XmppUtils.roomUserIds(groupId), true);//new DBServiceResult().getRosterUserDetails(groupId);
						WelcomeUtils.openChatWindow(vCard, false);
						if(!StringUtils.isEmpty(messageId)){
							if(!userId.contains("@")){
								userId = userId+"@imyourdoc.com";
								XmppUtils.getMsgEventManager().sendDisplayedNotification(userId, messageId);
								//XmppUtils.sendPendingReadStatus(userId);
							}
							
						}
					} catch (XMPPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
        dialog.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				dialog.dispose();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				Constants.mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				if(!isGroup){
					RosterVCardBo vCard = new DBServiceResult().getRosterUserDetails(userId);
					WelcomeUtils.openChatWindow(vCard, false);
					if(!StringUtils.isEmpty(messageId)){
						if(!userId.contains("@")){
							userId = userId+"@imyourdoc.com";
							//XmppUtils.getMsgEventManager().sendDisplayedNotification(userId, messageId);
						}
						
					}
					
				}
				else
				{
					RosterVCardBo vCard;
					try {
						MultiUserChat muc = new MultiUserChat(XmppUtils.connection,groupId);
						RoomInfo info = MultiUserChat.getRoomInfo(XmppUtils.connection, groupId);
						String groupSubject = "";
						if(!muc.isJoined())
							muc.join(Constants.loggedinuserInfo.username.split("@")[0]);
						if(info!=null){
							groupSubject = info.getSubject();
							if(org.apache.commons.lang.StringUtils.isEmpty(groupSubject)){
								groupSubject = groupId.split("@")[0];
							}
						}
						else
						{
							groupSubject = XmppUtils.roomUsersStr(groupId);
						}
						
						
						vCard = XmppUtils.createGroupVCardBO(groupId.toLowerCase(), groupSubject,	XmppUtils.roomUserIds(groupId.toLowerCase()), true);
						XmppUtils.generateChartPanels(vCard, UserWelcomeScreen.centerBox.getWidth());
						WelcomeUtils.openChatWindow(vCard, false);
						if(!StringUtils.isEmpty(messageId)){
							if(!userId.contains("@")){
								userId = userId+"@imyourdoc.com";
								XmppUtils.getMsgEventManager().sendDisplayedNotification(userId, messageId);
								//XmppUtils.sendPendingReadStatus(userId);
							}
							
						}
					} catch (XMPPException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}//new DBServiceResult().getRosterUserDetails(groupId);
					
				
				}
			}
		});
        dialog.add(panel);
        dialog.pack();
        dialog.setVisible(true);
        timer.setInitialDelay(0);
        timer.start();
    }
}
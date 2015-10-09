package com.im.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.json.JSONException;
import org.json.JSONObject;

import com.im.bo.MessageBo;
import com.im.bo.MessageSyncBo;
import com.im.bo.RosterVCardBo;
import com.im.common.IAction;
import com.im.common.LightScrollPane;
import com.im.db.DBServiceInsert;
import com.im.json.SendInvitationJSON;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.utils.Constants;
import com.im.utils.EncryptDecryptData;
import com.im.utils.PopUpMessage;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;
import com.im.utils.XmppUtils;

public class UserChat extends JPanel {
	private static final long serialVersionUID = 1L;
	List<String> recievedMessagePacketIds = new ArrayList<String>();
	List<String> SentMessagePacketIds = new ArrayList<String>();
	protected JTextArea textField;
	protected Box box;
	private Chat chat;
	private JButton sendMessageButton;
	private String buddyJID;
	private LightScrollPane scrollPane;
	byte[] image;
	private JLabel labelTimeStamp;
	RosterVCardBo vcard;

	SimpleDateFormat dateformat = new SimpleDateFormat("d MMM hh:mm:ss a ");
	// JLabel statusLabel;
	public static Map<String, UserChat> userChatMap;
	
	public UserChat(RosterVCardBo vcard, int width) {
		super(new GridBagLayout());
		this.buddyJID = vcard.getUserId();
		this.image = vcard.getProfileImage();
		this.vcard = vcard;
		if (!buddyJID.contains("@imyourdoc.com")) {
			buddyJID = buddyJID + "@imyourdoc.com";
		}
		chat = createChat();

		box = Box.createVerticalBox();// new Box(BoxLayout.Y_AXIS);
		box.setBackground(Color.white);
		box.setOpaque(true);
		JPanel panel = new JPanel();
		panel.add(box);
		panel.setOpaque(true);
		panel.setBackground(Color.white);
		scrollPane = new LightScrollPane(panel);
		scrollPane.setBackground(null);
		scrollPane.setOpaque(true);
		// jScrollPane.setViewportView(box);
		textField = new JTextArea(1, 35) {
			private static final long serialVersionUID = 1L;

			@Override
			public void addNotify() {
				super.addNotify();
				requestFocus();
			}
		};
		textField.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textField.setWrapStyleWord(true);
		textField.setLineWrap(true);
		JPanel bottom = bottomBox();
		// GroupLayout layout = new javax.swing.GroupLayout(this);
		// setLayout(layout);
		// layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
		// layout.createSequentialGroup()
		// .addGap(5, 5, 5)
		// .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
		// false)
		// // .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE,
		// 243, Short.MAX_VALUE)
		// .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE,
		// width, Short.MAX_VALUE)
		// .addComponent(bottom)).addContainerGap(15, 300)));
		// layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
		// layout.createSequentialGroup()
		// //.addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE,
		// 162, javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE,
		// 450, javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		// .addComponent(bottom, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
		// javax.swing.GroupLayout.PREFERRED_SIZE)
		// .addGap(0, 5, Short.MAX_VALUE)));

		Box vbox = Box.createVerticalBox();
		vbox.add(scrollPane);
		vbox.setOpaque(true);
		vbox.setBackground(null);
		vbox.add(bottom, BorderLayout.SOUTH);
		
		if(Constants.SCREEN_SIZE.getWidth()>1366){
		vbox.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54),
				(int) (Constants.SCREEN_SIZE.getHeight() * 0.65)));
		vbox.setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54), (int) (Constants.SCREEN_SIZE.getHeight() * 0.65)));
		}
		else if(Constants.SCREEN_SIZE.getWidth()<=1366 && Constants.SCREEN_SIZE.getWidth()>1288)
		{
			vbox.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54),
					(int) (Constants.SCREEN_SIZE.getHeight() * 0.64)));
			vbox.setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54), (int) (Constants.SCREEN_SIZE.getHeight() * 0.64)));
		}
		else if(Constants.SCREEN_SIZE.getWidth()<=1288)
		{
			vbox.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54),
					(int) (Constants.SCREEN_SIZE.getHeight() * 0.60)));
			vbox.setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54), (int) (Constants.SCREEN_SIZE.getHeight() * 0.6)));
		}
		JPanel finalPanel = new JPanel();
		// BoxLayout layout = new BoxLayout(finalPanel, BoxLayout.LINE_AXIS);
		// finalPanel.setLayout(layout);
		if(Constants.SCREEN_SIZE.getWidth()>1366){
			finalPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() *0.54),
					(int) (Constants.SCREEN_SIZE.getHeight() * 0.65)));
			finalPanel.setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54),
					(int) (Constants.SCREEN_SIZE.getHeight() * 0.65)));
			}
			else if(Constants.SCREEN_SIZE.getWidth()<=1366 && Constants.SCREEN_SIZE.getWidth()>1288)
			{
				finalPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54),
						(int) (Constants.SCREEN_SIZE.getHeight() * 0.64)));
				finalPanel.setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54),
						(int) (Constants.SCREEN_SIZE.getHeight() * 0.64)));
			}
			else if(Constants.SCREEN_SIZE.getWidth()<=1288)
			{
				finalPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54),
						(int) (Constants.SCREEN_SIZE.getHeight() * 0.60)));
				finalPanel.setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.54),
						(int) (Constants.SCREEN_SIZE.getHeight() * 0.60)));
			}
		finalPanel.add(vbox);
		finalPanel.setOpaque(true);
		finalPanel.setBackground(null);
		setBackground(Color.white);
		add(finalPanel);
	}

	public static UserChat getUserChat(RosterVCardBo vcard, int width) {
		String userid = vcard.getUserId();
		if (userid.contains("@")) {
			userid = userid.split("@")[0];
		}
		if (null == userChatMap)
			userChatMap = new HashMap<>();
		if (null == userChatMap.get(userid))
			userChatMap.put(userid, new UserChat(vcard, width));
		return userChatMap.get(userid);
	}

	public static void removeUserChat(String buddyJID, boolean sendMsg) {
		if (buddyJID.contains("@")) {
			buddyJID = buddyJID.split("@")[0];
		}
		if (null != userChatMap && null != userChatMap.get(buddyJID)) {
			UserChat userChat = userChatMap.get(buddyJID);
			userChat.box.removeAll();
			userChat.box.revalidate();
			userChat.box.repaint();
			UserWelcomeScreen.removeMsg(buddyJID);
			UserWelcomeScreen.showHomeView();
			try {
				if (sendMsg)
					userChat.sendMessage(Constants.REMOVE_CHAT_MSG, "", "", null, new JLabel(),"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public JPanel bottomBox() {
		JPanel bottomPanel = new JPanel();
		Box bottomBox = Box.createHorizontalBox();
		JLabel labelAttachment = new JLabel(new ImageIcon(
				((new ImageIcon(WelcomeUtils.class.getResource("/images/attachment.png"))).getImage()).getScaledInstance(40, 40,
						java.awt.Image.SCALE_FAST)), JLabel.CENTER);
		/*
		 * textField.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent paramActionEvent) {
		 * if (!textField.getText().trim().equals("") &&
		 * textField.getText().trim() != null) try { sendMessage("", "", "",
		 * null); } catch (IOException e) { e.printStackTrace(); } } });
		 */
		textField.setRequestFocusEnabled(true);
		textField.requestFocus();
		textField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!textField.getText().trim().equals("") && textField.getText().trim() != null) {
						try {
							sendMessage("", "", "", null, new JLabel("Sending"),"");
							XmppUtils.getMsgEventManager().sendCancelledNotification(buddyJID, "");
						} catch (IOException ex) {
							ex.printStackTrace();
							// JOptionPane.showMessageDialog(Constants.mainFrame,
							// "Disconnected from server.");
						}
					}

				} else {
					try {
						XmppUtils.getMsgEventManager().sendComposingNotification(buddyJID, "");
					} catch (Exception ex) {
						ex.printStackTrace();
						// JOptionPane.showMessageDialog(Constants.mainFrame,"Disconnected from server.");
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					XmppUtils.getMsgEventManager().sendComposingNotification(buddyJID, "");
				} catch (Exception ex) {
					ex.printStackTrace();
					// JOptionPane.showMessageDialog(Constants.mainFrame,"Disconnected from server.");
				}

			}

		});
		Action enter = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				textField.append(textField.getText() + "\n");
				textField.setText("");
			}
		};
		textField.getActionMap().put("insert-break", enter);
		labelAttachment.setCursor(new Cursor(Cursor.HAND_CURSOR));
		labelAttachment.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					final JLabel statusLabel = new JLabel("Uploading");
					statusLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
					statusLabel.setForeground(Color.gray);
					final JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("All Supported Documents", "ppt", 
							"doc", "docx", "xls","xlsx", "csv", "pdf", "PDF", 
							"jpg", "png", "bmp", "jpeg", "tiff", "txt"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "bmp", "jpeg", "tiff"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf", "PDF"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft Excel Documents", "xls", "xlsx", "csv"));
					fileChooser
							.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft Word Documents", "doc", "docx", "DOC", "DOCX"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Documents", "txt", "text"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft Power Point Documents", "ppt"));

					int rVal = fileChooser.showOpenDialog(getRootPane());
					if (rVal == JFileChooser.APPROVE_OPTION) {
						if (Constants.showConsole)
							System.out.println(fileChooser.getSelectedFile().toString());
						String url = fileChooser.getSelectedFile().getPath();
						Date date = new Date();
						labelTimeStamp = new JLabel(dateformat.format(date));
						labelTimeStamp.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 12));
						
						// horBox.add(Box.createHorizontalStrut((int)(Constants.SCREEN_SIZE.getWidth()
						// * 0.10)));
						String type = "file";
						BufferedImage imageTosend = null;
						if (url.endsWith(".docx") || url.endsWith(".doc")) {
							imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "word_file.png"));
						} else if (url.endsWith(".xlsx") || url.endsWith(".xls")) {
							imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "excel_file.png"));
						} else if (url.endsWith(".pdf")) {
							imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "pdf_file.png"));
						} else if (url.endsWith(".txt")) {
							imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "text_file.png"));
						} else if (url.endsWith(".ppt")) {
							imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "ppt_file.png"));
						} else if (url.endsWith(".csv")) {
							imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "excel_file.png"));
						} else {
							imageTosend = ImageIO.read(new File(url));
							type="image";
						}
					

						// (Constants.SCREEN_SIZE.getWidth() * 0.10)));
						final String packetId = RandomStringUtils.randomAlphanumeric(6);
						addSendImageInBox("",true,image,imageTosend,url,statusLabel,packetId,type,labelTimeStamp);
						new FileUploadIngThread(fileChooser.getSelectedFile(), fileChooser
								.getSelectedFile(), new FileUploaderListener() {

							@Override
							public void fileUploadingProgress(FileUploadIngThread thread, long progress) {
								// TODO Auto-generated method stub
								System.out.println("Progress " + thread.totalsize + " / " + progress);
								try {
									long loadingProgress = Math.round(((progress / (thread.totalsize * 1.0f))) * 100);
									statusLabel.setText("Uploading  " + loadingProgress + "%");
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override
							public void fileUploadingFailed(FileUploadIngThread thread) {
								// TODO Auto-generated method stub
								statusLabel.setText("Failed");
								new FileUploadIngThread(thread.uploadingFile, "", thread.listner);
							}

							@Override
							public void fileUploadingCompleted(String response, FileUploadIngThread thread) {
								// TODO Auto-generated method stub

								statusLabel.setText("Uploaded");

								String type = "file";
								try {

									JSONObject jsonResponse = new JSONObject(response);
									String url = jsonResponse.getString("url");
									BufferedImage image = null;
									if (url.endsWith(".docx") || url.endsWith(".doc")) {
										image = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "word_file.png"));
									} else if (url.endsWith(".xlsx") || url.endsWith(".xls")) {
										image = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "excel_file.png"));
									} else if (url.endsWith(".pdf")) {
										image = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "pdf_file.png"));
									} else if (url.endsWith(".txt")) {
										image = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "text_file.png"));
									} else if (url.endsWith(".ppt")) {
										image = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "ppt_file.png"));
									} else if (url.endsWith(".csv")) {
										image = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "excel_file.png"));
									} else {
										type = "image";
										image = ImageIO.read(new URL(url));
									}
									if (image != null) {
										String imageString = EncryptDecryptData.encodeToString(image, "png");

										sendMessage(imageString, url, type, image, statusLabel,packetId);
									}
								} catch (Exception e) {

								}
							}

						});
					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		sendMessageButton = new JButton(new ImageIcon(this.getClass().getResource("/images/send_message.png")));
		sendMessageButton.setOpaque(false);
		sendMessageButton.setContentAreaFilled(false);
		sendMessageButton.setBorderPainted(false);
		sendMessageButton.setFocusPainted(false);
		sendMessageButton.setBackground(null);
		sendMessageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (textField.getText().trim() != null && !textField.getText().trim().equals("")) {
						sendMessage("", "", "", null, new JLabel("Sending"),"");
						textField.setCaretPosition(0);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				textField.requestFocus();
				textField.setCaretPosition(0);
			}
		});
		JPanel attach = new JPanel();
		attach.add(labelAttachment);
		attach.setOpaque(true);
		attach.setBackground(null);

		JScrollPane sc = new JScrollPane(textField);
		sc.setBorder(new TextBubbleBorder(Color.lightGray, 2, 2, 0));
		JScrollBar verticalScrollBar = sc.getVerticalScrollBar();
		verticalScrollBar.setUI(new MyScrollBarUI());
		verticalScrollBar.setBackground(null);
		verticalScrollBar.setVisible(false);
		verticalScrollBar.setOpaque(true);
		JPanel textPanel = new JPanel();
		textField.setBorder(BorderFactory.createEmptyBorder());
		textPanel.add(sc);
		textPanel.setBorder(null);
		textPanel.setOpaque(true);
		textPanel.setBackground(null);

		JPanel sendPanel = new JPanel();
		sendPanel.add(sendMessageButton);
		sendPanel.setOpaque(true);
		sendPanel.setBackground(null);

		bottomBox.add(labelAttachment);
		bottomBox.add(sc);
		bottomBox.add(sendMessageButton);
		bottomBox.setOpaque(true);
		bottomBox.setBackground(null);
		Box box = Box.createVerticalBox();
		JPanel statusPanel = new JPanel();
		Constants.typeStatus = new JLabel();
		JLabel label = Constants.typeStatus;
		statusPanel.add(label, BorderLayout.WEST);
		statusPanel.setOpaque(true);
		statusPanel.setBackground(Color.decode("#F2F1EA"));
		box.add(statusPanel, BorderLayout.NORTH);
		box.add(bottomBox, BorderLayout.SOUTH);
		bottomPanel.add(box);
		bottomPanel.setOpaque(true);
		bottomPanel.setBackground(Color.decode("#F2F1EA"));
		bottomPanel.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()), (int) (Constants.SCREEN_SIZE.getHeight() * 0.10)));
		return bottomPanel;
	}

	public void sendMessage(final String imageString, final String url, final String type, Image imageTosend, final JLabel statusLabel,final String packetId)
			throws IOException {

		// System.out.println("Image String "+imageString +"URL "+url +"type "+
		// type +"imageto send"+imageString);

		final Message message = new Message();
		statusLabel.setText("Sending");
		try{
			XmppUtils.getMsgEventManager().sendCancelledNotification(buddyJID, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		final String msgTxt = textField.getText();
		// final JLabel statusLabel = new JLabel("Sending");
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				final Date date = new Date();
				final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
				SimpleDateFormat format2 = new SimpleDateFormat("d MMM hh:mm:ss a");
				format.setTimeZone(TimeZone.getTimeZone("UTC"));
				String bday = vcard.getBday();

				if (bday == null) {
					bday = "0";
				}
				if (!Constants.REMOVE_CHAT_MSG.equals(imageString)) {
					/*if (bday.equals("0")) {
						if (imageString.equals("")) {
							message.setBody(msgTxt);
							message.setFrom(Constants.loggedinuserInfo.username);
							message.setTo(buddyJID);
							statusLabel.setName(message.getPacketID());
							XmppUtils.setStatusLabel(message.getPacketID(), statusLabel);
							// String chatstatus = new
							// DBServiceResult().getChatStatus(message.getPacketID());
							// statusLabel.setText(chatstatus);
							UserWelcomeScreen.addMessages(vcard, msgTxt, false, message.getPacketID());
						} else {
							String packetId = RandomStringUtils.randomAlphanumeric(6);
							message.setFrom(Constants.loggedinuserInfo.username);
							message.setTo(buddyJID);
							message.setPacketID(packetId);
							message.setBody(imageString);
							message.setSubject(url);
							message.setProperty("file_type", type);
							statusLabel.setName(packetId);
							XmppUtils.setStatusLabel(packetId, statusLabel);
							// String chatstatus = new
							// DBServiceResult().getChatStatus(packetId);
							// statusLabel.setText(chatstatus);
							try {
								UserWelcomeScreen.addMessages(vcard, "File Sent", false, packetId);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} */
//						if (bday.equals("1")) {
						if (url.equals("")) {
							JSONObject object = new JSONObject();
							object.put("messageID", message.getPacketID());
							object.put("content", msgTxt);
							object.put("from", Constants.loggedinuserInfo.username);
							object.put("timestamp", format.format(date));
							object.put("to", buddyJID);
							message.setFrom(Constants.loggedinuserInfo.username);
							message.setTo(buddyJID);
							message.setBody(object.toString());
							message.setProperty("message_version", "2.0");
							statusLabel.setName(message.getPacketID());
							XmppUtils.setStatusLabel(message.getPacketID(), statusLabel);
							UserWelcomeScreen.addMessages(vcard, msgTxt, false, message.getPacketID(),format2.format(date),false);
						} else {
							
							message.setPacketID(packetId);
							JSONObject object = new JSONObject();
							object.put("messageID", packetId);
							object.put("content", imageString);
							object.put("from", Constants.loggedinuserInfo.username);
							object.put("timestamp", format.format(date));
							object.put("to", buddyJID);
							object.put("file_path", url);
							object.put("file_type", type);
							message.setFrom(Constants.loggedinuserInfo.username);
							message.setTo(buddyJID);
							message.setBody(object.toString());
							message.setPacketID(packetId);
							message.setProperty("message_version", "2.0");
							message.setSubject(url);
							message.setProperty("file_type", type);

							statusLabel.setName(packetId);
							XmppUtils.setStatusLabel(packetId, statusLabel);
							// String chatstatus = new
							// DBServiceResult().getChatStatus(packetId);
							// statusLabel.setText(chatstatus);
							UserWelcomeScreen.addMessages(vcard, "File Sent", false, packetId,format2.format(date),false);
						}
//					}
				} else {
					
						/*JSONObject object = new JSONObject();
						object.put("messageID", message.getPacketID());
						object.put("content", Constants.REMOVE_CHAT_MSG);
						object.put("from", Constants.loggedinuserInfo.username);
						object.put("timestamp", format.format(date));
						object.put("to", buddyJID);*/
						message.setFrom(Constants.loggedinuserInfo.username);
						message.setTo(buddyJID);
						message.setBody(Constants.REMOVE_CHAT_MSG);
						message.setPacketID("IMYOURDOC_CLOSE");
						//message.setProperty("message_version", "2.0");
						new SendInvitationJSON().ReportOnCloseConversation(buddyJID);
				}

				chat.sendMessage(message);
				MessageBo messageBo = new MessageBo();
				messageBo.setFrom(Constants.loggedinuserInfo.username);
				messageBo.setTo(buddyJID);
				messageBo.setContent(message.getBody() == null ? "" : message.getBody());
				messageBo.setFileType(message.getProperty("file_type") == null ? "" : message.getProperty("file_type").toString());
				messageBo.setFilePath(message.getSubject() == null ? "" : message.getSubject());
				messageBo.setChatType(Message.Type.chat.toString());
				messageBo.setMessageId(message.getPacketID());
				messageBo.setStatus("Sending");
				messageBo.setTimeStamp(format.format(date));
				new DBServiceInsert().insertMessageTable(messageBo);
				Calendar c = Calendar.getInstance();
				c.add(Calendar.SECOND, 50);
				Timer timer = new Timer();
				if(!statusLabel.getText().equalsIgnoreCase("Sent")){
					if(!message.getPacketID().equals("")){
						timer.schedule(new SendNotify(statusLabel, message.getPacketID(),message), 50000);
					}
				}
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

					@Override
					protected Void doInBackground() throws Exception {
						String response = new SendInvitationJSON().SubmitMessage(message.getPacketID(),
								org.jivesoftware.smack.util.StringUtils.parseBareAddress(buddyJID), "Single");
						JSONObject object = new JSONObject(response);
						
//						String err_code = object.getString(key);
						return null;
					}
				};
				worker.execute();
				// XmppUtils.setStatusLabel(buddyJID,new JLabel("Sent"));
				return null;

			}
		};
		worker.execute();
		try {
			Date date = null;
			DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
			if (delay != null) {
				date = delay.getStamp();
				if (Constants.showConsole)
					System.out.println(date);
			} else {
				date = new Date();
			}
			labelTimeStamp = new JLabel(dateformat.format(date));
			labelTimeStamp.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 12));
//			Box horBox = Box.createHorizontalBox();
//			horBox.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
			if (!Constants.REMOVE_CHAT_MSG.equals(imageString)) {
				
				if (imageString.equals("")) {
					addSendMessageInBox(msgTxt,message, image, statusLabel,labelTimeStamp);
				}
				
				textField.setText("");
			}

			


		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// textField.selectAll();
		// textArea.setCaretPosition(textArea.getDocument().getLength());
	}

	private Chat createChat() {
		Chat chat = null;

		try {
			chat = XmppUtils.createChat(buddyJID, new MessageListener() {
				@Override
				public void processMessage(Chat chat, Message message) {
					// addMsgInBox(message);
				}
			}, new IAction() {
				@Override
				public void doAction(Object obj1, Object obj2) {
					Message message = (Message) obj1;
					try {
						addMsgInBox(message,true,null);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return chat;
	}

	/**
	 * @author megha
	 *         <p>
	 *         To show received messages
	 *         </p>
	 * @param message
	 * @throws JSONException
	 */
	public void addMsgInBox(final Message message,boolean playSound,JLabel timestamp) throws JSONException {
		// if (message.getType() == Message.Type.groupchat)
		String from = message.getFrom();
		String body = message.getBody();
		RosterVCardBo vcard = XmppUtils.getVCardBo(from.split("@")[0]);
		JSONObject object = null;
		JLabel labelTimeStamp = null;
		boolean isAllreadyShown = recievedMessagePacketIds.contains(message.getPacketID());
		if (!isAllreadyShown && from.split("@")[0].equals(vcard.getUserId().split("@")[0])) {
			recievedMessagePacketIds.add(message.getPacketID());
			Date date = null;
			String imageUrl = "";
			
			SimpleDateFormat dateformat = new SimpleDateFormat("d MMM hh:mm:ss a");
			final DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
			if (delay != null) {
				date = delay.getStamp();
				if (Constants.showConsole)
					System.out.println(date);
			} else {
				date = new Date();
			}
			if(timestamp!=null){
				labelTimeStamp = timestamp;
			}
			else
			{
				labelTimeStamp = new JLabel(dateformat.format(date));
			}
			

			try {
				if(isJSONValid(body)){
					object = new JSONObject(body);
					if (object.has("file_path") && object.has("file_type")) {
						imageUrl = object.getString("file_path");
						body = object.getString("content");
					} else {
						body = object.getString("content");
					}
				}
//				if (version != null) {
//					if (version.equals("2.0")) {
//						object = new JSONObject(body);
//						if (object.has("file_path") && object.has("file_type")) {
//							imageUrl = object.getString("file_path");
//							body = object.getString("content");
//						} else {
//							body = object.getString("content");
//						}
//					}
//				} else if (subject != null) {
//					if (isJSONValid(subject)) {
//						obj = new JSONObject(subject);
//						imageUrl = obj.getString("url");
//					} else {
//						imageUrl = subject;
//					}
//				}
				if (null != imageUrl && !imageUrl.trim().equals("") && imageUrl.startsWith("https://")) {
					Box horBox = Box.createHorizontalBox();
					BufferedImage image = Util.getProfileImg(vcard.getUserId());
					byte[] imageByte = Util.bufferImgToByteArray(image);
					horBox.add(new ChatPanel(imageByte, labelTimeStamp).getLeftBubble(body, imageUrl, from.split("@")[0], message.getPacketID(),playSound));
					// horBox.add(Box.createGlue());
					horBox.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
					Box vBox = Box.createVerticalBox();
					vBox.add(horBox, BorderLayout.WEST);
					JPanel panelFinal = new JPanel(new BorderLayout());
					panelFinal.setOpaque(true);
					panelFinal.setBackground(null);
					panelFinal.add(vBox, BorderLayout.WEST);
					box.add(panelFinal, BorderLayout.WEST);
					box.repaint();
					box.revalidate();
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							scrollPane.scrollDown();
						}
					});
					//if(timestamp == null){
						UserWelcomeScreen.addMessages(vcard, "File Received", true, message.getPacketID(),labelTimeStamp.getText(),false);
					//}
					//else
					//{
					//	UserWelcomeScreen.addMessages(vcard, "File Received", false, message.getPacketID(),labelTimeStamp.getText(),true);
				//	}
					

				} else if (Constants.REMOVE_CHAT_MSG.equals(body)) {
						
						new SendInvitationJSON().ReportOnCloseConversation(org.jivesoftware.smack.util.StringUtils.parseBareAddress(from));
					if(timestamp==null){
						removeUserChat(from.split("/")[0], false);
						JOptionPane.showMessageDialog(getParent(), "This conversation was closed by "+from.split("@")[0] + ".<br/>Please go to new conversation section to start conversation again");
					
					}
//					else
//					{
//						box.removeAll();
//						box.repaint();
//						box.revalidate();
//					}
				} else if (!StringUtils.isEmpty(body)) {
					Box horBox = Box.createHorizontalBox();
					BufferedImage image = Util.getProfileImg(vcard.getUserId());
					byte[] imageByte = Util.bufferImgToByteArray(image);
					horBox.add(new ChatPanel(imageByte, labelTimeStamp).getLeftBubble(body, "", from.split("@")[0], message.getPacketID(),playSound));
					// horBox.add(Box.createGlue());
					horBox.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
					Box vBox = Box.createVerticalBox();
					vBox.add(horBox, BorderLayout.WEST);
					JPanel panelFinal = new JPanel(new BorderLayout());
					panelFinal.setOpaque(true);
					panelFinal.setBackground(null);
					panelFinal.add(vBox, BorderLayout.WEST);
					box.add(panelFinal, BorderLayout.WEST);
					box.repaint();
					box.revalidate();
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							scrollPane.scrollDown();
						}
					});
					if(timestamp==null){
						UserWelcomeScreen.addMessages(vcard, body, true, message.getPacketID(),labelTimeStamp.getText(),false);
					}
					else
					{
						UserWelcomeScreen.addMessages(vcard, body, false, message.getPacketID(),labelTimeStamp.getText(),true);
						
					}
				
					if(timestamp==null){
						if (!body.equals(Constants.REMOVE_CHAT_MSG)) {
							if (Constants.mainFrame != null) {
								if ((Constants.mainFrame.getExtendedState() == 7) || !(Constants.mainFrame.isFocused())) {
									new PopUpMessage(0, (JFrame) Constants.mainFrame, from.split("@")[0], false, "", message.getPacketID())
											.makeUI("You have a secure message waiting on IM YourDoc from:-" + from.split("@")[0]);
	
								}
							}
	
						}
					}
				}

				
				if(playSound && timestamp==null){
					URL audioFilePath = this.getClass().getResource("/sounds/ripple.wav");
					AudioInputStream audio = AudioSystem.getAudioInputStream(audioFilePath);
					Clip clip = AudioSystem.getClip();
					clip.open(audio);
					clip.start();
					clip.loop(0);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void addSendMessageInBox(String msg,Message message, byte[] image,JLabel statusLabel,JLabel labelTimeStamp) throws JSONException, IOException {
		Box horBox = Box.createHorizontalBox();
		JPanel statusPanel = new JPanel();
		statusPanel.setOpaque(true);
		statusPanel.setBackground(null);
		if(!msg.equals(Constants.REMOVE_CHAT_MSG))
			horBox.add(new ChatPanel(image, labelTimeStamp).getRightBubble(msg, false, null, ""));
		
		statusPanel.add(statusLabel);
		
		Box vBox = Box.createVerticalBox();
		vBox.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
		vBox.add(horBox, BorderLayout.EAST);
		if(!statusLabel.getText().equalsIgnoreCase("Sent")){
			vBox.add(statusPanel, BorderLayout.EAST);
		}
		final JPanel panelFinal = new JPanel(new BorderLayout());
		panelFinal.setOpaque(true);
		panelFinal.setBackground(null);
		panelFinal.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
		panelFinal.add(vBox, BorderLayout.EAST);
		box.add(panelFinal, BorderLayout.EAST);
		box.repaint();
		box.revalidate();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				scrollPane.scrollDown();
			}
		});
		statusLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				// TODO Auto-generated method stub
				if(me.getButton()== MouseEvent.BUTTON3){
					JPopupMenu menu = new JPopupMenu();
					/*JMenuItem menuItemResend = new JMenuItem("Resend");
					menuItemResend.setOpaque(false);
					menuItemResend.setBorderPainted(false);
					menuItemResend.setFocusPainted(false);
					menuItemResend.setCursor(new Cursor(Cursor.HAND_CURSOR));
					menuItemResend.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
					menuItemResend.setBackground(Color.white);
					menuItemResend.setForeground(Color.decode("#9CCD21"));
					menuItemResend.setArmed(false);
					menu.add(menuItemResend);*/
					JMenuItem menuItemDelete = new JMenuItem("Delete");
					menuItemDelete.setOpaque(false);
					menuItemDelete.setBorderPainted(false);
					menuItemDelete.setFocusPainted(false);
					menuItemDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
					menuItemDelete.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
					menuItemDelete.setForeground(Color.white);
					menuItemDelete.setBackground(Color.decode("#9CCD21"));
					menuItemDelete.setArmed(false);
					menuItemDelete.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							box.remove(panelFinal);
							box.repaint();
							box.revalidate();
						}
					});
					menu.add(menuItemDelete);
					me.getComponent().requestFocus();
					Component b = (Component) me.getSource();

					// Get the location of the point 'on the screen'
					Point p = b.getLocationOnScreen();
					menu.show(b, 0, 0);
					menu.setLocation(p.x, p.y + b.getHeight());
					menu.setOpaque(true);
					menu.setAutoscrolls(true);
					menu.setBackground(Color.decode("#9CCD21"));
				}
			}
		});
		
		

	}
	public void addSendImageInBox(String message,boolean isFile,byte[]image,BufferedImage imageTosend,
			String imageUrl,JLabel statusLabel,String messageId,String fileType,JLabel labelTimeStamp) throws JSONException, IOException {
		Box horBox = Box.createHorizontalBox();
		JPanel statusPanel = new JPanel();
		statusPanel.setOpaque(true);
		statusPanel.setBackground(null);
		horBox.add(new ChatPanel(image, labelTimeStamp).getRightBubble("", true, imageTosend, imageUrl));
		statusPanel.add(statusLabel);
		
		Box vBox = Box.createVerticalBox();
		vBox.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
		vBox.add(horBox, BorderLayout.EAST);
		if(!statusLabel.getText().equalsIgnoreCase("Sent")){
			vBox.add(statusPanel, BorderLayout.EAST);
		}
		final JPanel panelFinal = new JPanel(new BorderLayout());
		panelFinal.setOpaque(true);
		panelFinal.setBackground(null);
		panelFinal.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
		panelFinal.add(vBox, BorderLayout.EAST);
		box.add(panelFinal, BorderLayout.EAST);
		box.repaint();
		box.revalidate();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				scrollPane.scrollDown();
			}
		});
		statusLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				// TODO Auto-generated method stub
				if(me.getButton()== MouseEvent.BUTTON3){
					JPopupMenu menu = new JPopupMenu();
					/*JMenuItem menuItemResend = new JMenuItem("Resend");
					menuItemResend.setOpaque(false);
					menuItemResend.setBorderPainted(false);
					menuItemResend.setFocusPainted(false);
					menuItemResend.setCursor(new Cursor(Cursor.HAND_CURSOR));
					menuItemResend.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
					menuItemResend.setBackground(Color.white);
					menuItemResend.setForeground(Color.decode("#9CCD21"));
					menuItemResend.setArmed(false);
					menu.add(menuItemResend);*/
					JMenuItem menuItemDelete = new JMenuItem("Delete");
					menuItemDelete.setOpaque(false);
					menuItemDelete.setBorderPainted(false);
					menuItemDelete.setFocusPainted(false);
					menuItemDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
					menuItemDelete.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
					menuItemDelete.setForeground(Color.white);
					menuItemDelete.setBackground(Color.decode("#9CCD21"));
					menuItemDelete.setArmed(false);
					menuItemDelete.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							box.remove(panelFinal);
							box.repaint();
							box.revalidate();
						}
					});
					menu.add(menuItemDelete);
					me.getComponent().requestFocus();
					Component b = (Component) me.getSource();

					// Get the location of the point 'on the screen'
					Point p = b.getLocationOnScreen();
					menu.show(b, 0, 0);
					menu.setLocation(p.x, p.y + b.getHeight());
					menu.setOpaque(true);
					menu.setAutoscrolls(true);
					menu.setBackground(Color.decode("#9CCD21"));
				}
			}
		});
		if(!statusLabel.getText().equalsIgnoreCase("Sent")){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.SECOND, 50);
			JSONObject object = new JSONObject();
			SimpleDateFormat dateformat = new SimpleDateFormat("d MMM hh:mm:ss a ");
			String imageString = EncryptDecryptData.encodeToString(imageTosend, "png");
			object.put("messageID", messageId);
			object.put("content", imageString);
			object.put("from", Constants.loggedinuserInfo.username);
			object.put("timestamp", dateformat.format(new Date()));
			object.put("to", buddyJID);
			object.put("file_path", imageUrl);
			object.put("file_type", fileType);
			Message newMessage = new Message();
			newMessage.setFrom(Constants.loggedinuserInfo.username);
			newMessage.setTo(buddyJID);
			newMessage.setBody(object.toString());
			newMessage.setPacketID(messageId);
			newMessage.setProperty("message_version", "2.0");
			newMessage.setSubject(imageUrl);
			newMessage.setProperty("file_type", fileType);
		}

	}
	public void addMsgInBoxChatState(MessageSyncBo msgSyncBo,String activeDate) throws JSONException {
	
		String body = msgSyncBo.getContent();
		String from = msgSyncBo.getMessageFrom();
		String to = msgSyncBo.getMessageTo();
		String messageID = msgSyncBo.getMessageID();
		String file_type = msgSyncBo.getFileType();
		String timeStamp = msgSyncBo.getChatDate();
		String file_url = msgSyncBo.getFileUrl();
		boolean isAllreadyShown = SentMessagePacketIds.contains(messageID);
		JLabel labelTimeStamp = new JLabel();
			SimpleDateFormat dateformat = new SimpleDateFormat("d MMM hh:mm:ss a");
			SimpleDateFormat dateformat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Message newMessage = new Message();
			try {
					if(!from.contains("@") && !from.contains("_")){
						from = from+"@imyourdoc.com";
					}
					if(!to.contains("@") && !to.contains("_")){
						to = to+"@imyourdoc.com";
					}
					timeStamp = timeStamp.split(" +0000")[0];
					labelTimeStamp.setText(dateformat.format(dateformat2.parse(timeStamp)));
					
					RosterVCardBo vcardbo = null;
					if(!to.split("@")[0].equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0])){
						vcardbo = XmppUtils.getVCardBo(to.split("@")[0].toLowerCase());
						XmppUtils.generateChartPanels(vcardbo, ((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
						//Constants.currentChatWindowUSERID = to.split("@")[0];
					}
					else if(!from.split("@")[0].equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0])){
						vcardbo = XmppUtils.getVCardBo(from.split("@")[0].toLowerCase());
						XmppUtils.generateChartPanels(vcardbo, ((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
						//Constants.currentChatWindowUSERID = from.split("@")[0];
					}
					if(to.split("@")[0].equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0])){
						JSONObject objectRecieve = new JSONObject();
						
						
						if(StringUtils.isEmpty(file_url)){
							objectRecieve.put("messageID", messageID);
							objectRecieve.put("content", body);
							objectRecieve.put("from", from);
							objectRecieve.put("timestamp", timeStamp);
							objectRecieve.put("to", to);
							newMessage.setPacketID(messageID);
							newMessage.setFrom(from);
							newMessage.setTo(to);
							newMessage.setBody(objectRecieve.toString());
							newMessage.setProperty("message_version", "2.0");
						}
						else
						{
							objectRecieve.put("messageID", messageID);
							objectRecieve.put("content", body);
							objectRecieve.put("from", from);
							objectRecieve.put("timestamp", timeStamp);
							objectRecieve.put("to", to);
							objectRecieve.put("file_path", file_url);
							objectRecieve.put("file_type", file_type);
							newMessage.setPacketID(messageID);
							newMessage.setFrom(from);
							newMessage.setTo(to);
							newMessage.setBody(objectRecieve.toString());
							newMessage.setProperty("message_version", "2.0");
							newMessage.setSubject(file_url);
							newMessage.setProperty("file_type", file_type);
						}
						addMsgInBox(newMessage, false,labelTimeStamp);
					}
					else if(from.split("@")[0].equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0])){
						if(!isAllreadyShown){
							SentMessagePacketIds.add(messageID);
							if(StringUtils.isEmpty(file_url)){
								addSendMessageInBox(body, newMessage, vcardbo.getProfileImage(), new JLabel("Sent"),labelTimeStamp);
							}
							else
							{
								File destDir = FileUtils.getUserDirectory();
								String fileName = file_url.substring(file_url.lastIndexOf("/") + 1, file_url.length());
								try {
									if(fileName.contains(" ")){
										fileName = fileName.trim();
									}
									File finalFile = new File(destDir.getPath()+"/"+fileName);
									String urlstring = file_url;
									urlstring = urlstring.replaceAll(" ","%20");
									URL url = new URL(urlstring);
									
									FileUtils.copyURLToFile(url, finalFile);
									File fileOpen = new File(destDir.getPath()+"/"+fileName);
									String type = "file";
									BufferedImage imageTosend = null;
									if (file_url.endsWith(".docx") || file_url.endsWith(".doc")) {
										imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "word_file.png"));
									} else if (file_url.endsWith(".xlsx") || file_url.endsWith(".xls")) {
										imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "excel_file.png"));
									} else if (file_url.endsWith(".pdf")) {
										imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "pdf_file.png"));
									} else if (file_url.endsWith(".txt")) {
										imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "text_file.png"));
									} else if (file_url.endsWith(".ppt")) {
										imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "ppt_file.png"));
									} else if (file_url.endsWith(".csv")) {
										imageTosend = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/" + "excel_file.png"));
									} else {
										imageTosend = ImageIO.read(fileOpen);//EncryptDecryptData.decodeToImage(body);//
										type="image";
									}
									addSendImageInBox("", true,vcardbo.getProfileImage(), imageTosend, fileOpen.getPath(), new JLabel("Sent"), messageID, type,labelTimeStamp);
									
								} catch (IOException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
								
								
							}
								
							
							}
					}
//				
					if(StringUtils.isNotEmpty(file_type)){
						if(to.equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0]))
							UserWelcomeScreen.addMessages(vcardbo, "File Recieved", false, messageID,labelTimeStamp.getText(),true);
						else if(from.equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0]))
							UserWelcomeScreen.addMessages(vcardbo, "File Sent", false, messageID,labelTimeStamp.getText(),true);
					}
					else
					{
						UserWelcomeScreen.addMessages(vcardbo, body, false, messageID,labelTimeStamp.getText(),true);
					}
					
//					scrollPane.scrollDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
			return true;
		} catch (JSONException ex) {
			return false;
		}
	}

	/*
	 * private void sendFileToUser(String filePath, JLabel statusLabel) throws
	 * ClientProtocolException, IOException, JSONException { HttpClient
	 * httpClient = HttpClientBuilder.create().build(); File file = new
	 * File(filePath); fileUrl = filePath; String type = ""; Path source =
	 * Paths.get(filePath); if
	 * (Files.probeContentType(source).startsWith("image")) { type = "image"; }
	 * else { type = "file"; } MultipartEntity mpEntity = new MultipartEntity();
	 * FileBody fileBody = new FileBody(file, "application/octect-stream");
	 * mpEntity.addPart("upload", fileBody); mpEntity.addPart("login_token", new
	 * StringBody(Constants.loggedinuserInfo.login_token));
	 * mpEntity.addPart("type", new StringBody(type)); HttpPost httppost = new
	 * HttpPost("https://api.imyourdoc.com/serviceFiles.php?method=uploadFile");
	 * httppost.setEntity(mpEntity); // statusLabel = new JLabel("Uploading..");
	 * HttpResponse httpResponse = httpClient.execute(httppost); String err_code
	 * = ""; String getData = EntityUtils.toString(httpResponse.getEntity());
	 * System.out.println("the getting data IS " + "jo "
	 * 
	 * + mpEntity.toString() + "\n" + " response " + getData); String response =
	 * getData; JSONObject jsonResponse = new JSONObject(response); //
	 * statusLabel = new JLabel("Uploading.."); err_code =
	 * jsonResponse.getString("err-code"); // if (err_code.equals("1")) { //
	 * statusLabel = new JLabel("Sent"); // } String url =
	 * jsonResponse.getString("url"); BufferedImage image = null; if
	 * (url.endsWith(".docx") || url.endsWith(".doc")) { image =
	 * ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
	 * "/"+"word_file.png")); } else if (url.endsWith(".xlsx") ||
	 * url.endsWith(".xls")) { image =
	 * ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
	 * "/"+"excel_file.png")); } else if (url.endsWith(".pdf")) { image =
	 * ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
	 * "/"+"pdf_file.png")); } else if (url.endsWith(".txt")) { image =
	 * ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
	 * "/"+"text_file.png")); } else if (url.endsWith(".ppt")) { image =
	 * ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/"+
	 * "ppt_file.png")); } else if (url.endsWith(".csv")) { image =
	 * ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
	 * "/"+"excel_file.png")); } else { image = ImageIO.read(new URL(url)); } if
	 * (image != null) { String imageString =
	 * EncryptDecryptData.encodeToString(image, "png"); sendMessage(imageString,
	 * url, type, image, statusLabel); } // we will get the url in response;
	 * 
	 * 
	 * if (resEntity != null) {
	 * System.out.println(EntityUtils.toString(resEntity)); } if (resEntity !=
	 * null) { resEntity.consumeContent(); }
	 * 
	 * 
	 * // httpClient.getConnectionManager().shutdown(); }
	 */

	private class SendNotify extends TimerTask {
		// This task will execute just once after seven seconds of starting the
		// program
		JLabel label;
		String msgId;
		Message message;
		JPanel panelFinal;
		public SendNotify(JLabel label, String msgId,Message message) {
			this.label = label;
			this.msgId = msgId;
			this.message = message;
//			this.panelFinal = panelFinal;
		}

		public void run() {
			// if(Constants.showConsole)
			// System.out.println("I will be displayed after five seconds" +
			// " ->" + new Date());
			try {
				sendNotify();
			} catch (JSONException e) {
				e.printStackTrace();
				/*if (!(label.getText().equalsIgnoreCase("delivered")) && !(label.getText().equalsIgnoreCase("read"))
						&& !(label.getText().equalsIgnoreCase("uploaded"))){
					label.setText("Not Sent");
					label.setIcon(new ImageIcon(UserChat.class
							.getResource(Constants.IMAGE_PATH + "/"
									+ "error.png")));
				}*/
			}
		}

		public void sendNotify() throws JSONException {
			SendInvitationJSON json = new SendInvitationJSON();
			String response = json.sendNotification(msgId);
			JSONObject obj = new JSONObject(response);
			System.out.println(response);
			int err_code = obj.getInt("err-code");
			if (obj.getInt("err-code") == Integer.parseInt("600")) {
				// JOptionPane.showMessageDialog(getParent(),
				// obj.getString("message"));
				System.out.println(obj.getString("message"));
			} else if (err_code == 1) {
				// JOptionPane.showMessageDialog(getParent(),
				// obj.getString("message"));
				if (obj.getInt("notification_state") == 1 && obj.getBoolean("mail_sent") == true) {
					System.out.println(obj.getString("message"));
					if (!(label.getText().equalsIgnoreCase("delivered")) && !(label.getText().equalsIgnoreCase("read")))
						label.setText("Notification/Email Sent");
				} else if (obj.getInt("notification_state") == 1) {
					if (!(label.getText().equalsIgnoreCase("delivered")) && !(label.getText().equalsIgnoreCase("read")))
						label.setText("Notification already Sent");
				} else if (obj.getInt("notification_state") == 0) {
					if (!(label.getText().equalsIgnoreCase("delivered")) && !(label.getText().equalsIgnoreCase("read"))
							&& !(label.getText().equalsIgnoreCase("uploaded")))
						label.setText("Sending Failed");
					
					/*label.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent me) {
							// TODO Auto-generated method stub
							if(me.getButton()== MouseEvent.BUTTON3){
								System.out.println("RightClick");
								JPopupMenu menu = new JPopupMenu();
								JMenuItem menuItemResend = new JMenuItem("Resend");
								menuItemResend.addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent arg0) {
										// TODO Auto-generated method stub
										try {
											chat.sendMessage(message);
											label.setText("Retrying");
											XmppUtils.setStatusLabel(message.getPacketID(), label);
											Timer timer = new Timer();
											timer.schedule(new SendNotify(label, message.getPacketID(),message,panelFinal), 30000);
										} catch (XMPPException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
									}
								});
								menuItemResend.setOpaque(false);
								menuItemResend.setBorderPainted(false);
								menuItemResend.setFocusPainted(false);
								menuItemResend.setCursor(new Cursor(Cursor.HAND_CURSOR));
								menuItemResend.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
								menuItemResend.setForeground(Color.white);
								menuItemResend.setBackground(Color.decode("#9CCD21"));
								menuItemResend.setArmed(false);
								menu.add(menuItemResend);
								JMenuItem menuItemDelete = new JMenuItem("Delete");
								menuItemDelete.setOpaque(false);
								menuItemDelete.setBorderPainted(false);
								menuItemDelete.setFocusPainted(false);
								menuItemDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
								menuItemDelete.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
								menuItemDelete.setForeground(Color.white);
								menuItemDelete.setBackground(Color.decode("#9CCD21"));
								menuItemDelete.setArmed(false);
								menuItemDelete.addActionListener(new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent e) {
										// TODO Auto-generated method stub
										box.remove(panelFinal);
										box.repaint();
										box.revalidate();
									}
								});
								menu.addSeparator();
								menu.add(menuItemDelete);
								me.getComponent().requestFocus();
								Component b = (Component) me.getSource();

								// Get the location of the point 'on the screen'
								Point p = b.getLocationOnScreen();
								menu.show(b, 0, 0);
								menu.setLocation(p.x, p.y + b.getHeight());
								menu.setOpaque(true);
								menu.setAutoscrolls(true);
								menu.setBackground(Color.decode("#9CCD21"));
							}
						}
					});*/
				}
			}
				

		}
	}

	private static class MyScrollBarUI extends BasicScrollBarUI {
		private static final int SCROLL_BAR_ALPHA_ROLLOVER = 200;
		private static final int SCROLL_BAR_ALPHA = 150;
		private static final int THUMB_BORDER_SIZE = 0;
		private static final int THUMB_SIZE = 8;
		private static final Color THUMB_COLOR = Color.DARK_GRAY;

		@Override
		protected JButton createDecreaseButton(int orientation) {
			return new MyScrollBarButton();
		}

		@Override
		protected JButton createIncreaseButton(int orientation) {
			return new MyScrollBarButton();
		}

		@Override
		protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
			c.setBackground(Color.white);
			Dimension zeroDim = new Dimension(8, 8);
			c.setPreferredSize(zeroDim);
			c.setMinimumSize(zeroDim);
			c.setMaximumSize(zeroDim);
		}

		@Override
		protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
			int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
			int orientation = scrollbar.getOrientation();
			int arc = THUMB_SIZE;
			int x = thumbBounds.x + THUMB_BORDER_SIZE;
			int y = thumbBounds.y + THUMB_BORDER_SIZE;

			int width = orientation == JScrollBar.VERTICAL ? THUMB_SIZE : thumbBounds.width - (THUMB_BORDER_SIZE * 2);
			width = Math.max(width, THUMB_SIZE);

			int height = orientation == JScrollBar.VERTICAL ? thumbBounds.height - (THUMB_BORDER_SIZE * 2) : THUMB_SIZE;
			height = Math.max(height, THUMB_SIZE);

			Graphics2D graphics2D = (Graphics2D) g.create();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2D.setColor(new Color(THUMB_COLOR.getRed(), THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
			graphics2D.fillRoundRect(x, y, width, height, arc, arc);
			graphics2D.setBackground(Color.white);
			graphics2D.dispose();
		}
	}

	private static class MyScrollBarButton extends JButton {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private MyScrollBarButton() {
			setOpaque(true);
			setFocusable(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setBorder(null);
			setBackground(Color.white);
			Dimension zeroDim = new Dimension(0, 0);
			setPreferredSize(zeroDim);
			setMinimumSize(zeroDim);
			setMaximumSize(zeroDim);
		}
	}
}

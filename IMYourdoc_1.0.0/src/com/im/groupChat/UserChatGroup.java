package com.im.groupChat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.json.JSONException;
import org.json.JSONObject;

import com.im.bo.MessageBo;
import com.im.bo.MessageSyncBo;
import com.im.bo.RosterVCardBo;
import com.im.chat.ChatPanel;
import com.im.chat.FileUploadIngThread;
import com.im.chat.FileUploaderListener;
import com.im.common.IAction;
import com.im.common.LightScrollPane;
import com.im.common.Members;
import com.im.db.DBServiceInsert;
import com.im.db.DBServiceResult;
import com.im.db.DBServiceUpdate;
import com.im.json.SendInvitationJSON;
import com.im.login.MenuScroller;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.utils.Constants;
import com.im.utils.EncryptDecryptData;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;
import com.im.utils.XmppUtils;

public class UserChatGroup extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JTextArea textField;
	protected Box box;
	private MultiUserChat multiUserChat;
	List<String> SentMessagePacketIds = new ArrayList<String>();
	private JButton sendMessageButton;
	private String roomId;
	// private String buddyJID;
	private String buddyName;
	private LightScrollPane scrollPane;
	private boolean isFile;
	byte[] image;
	private static String fileUrl;
	private JLabel labelTimeStamp;
	RosterVCardBo vcard;
	SimpleDateFormat dateformat = new SimpleDateFormat("d MMM h:mm a");
	// JLabel statusLabel;
	public static Map<String, UserChatGroup> userChatMap;

	public static UserChatGroup getUserChat(RosterVCardBo vCard, int width) {
		String userId = vCard.getUserId().split("@")[0];
		
		if (null == userChatMap)
			userChatMap = new HashMap<>();
		if (null == userChatMap.get(userId))
			userChatMap.put(userId, new UserChatGroup(vCard, width));
		
		return userChatMap.get(userId);
	}

	public static void removeUserChat(RosterVCardBo vCard, boolean sendMsg) {
		String userId = vCard.getUserId().split("@")[0];
		if (null != userChatMap && null != userChatMap.get(userId)) {
			UserChatGroup userchatGroup = userChatMap.get(userId);
			userchatGroup.box.removeAll();
			userchatGroup.box.revalidate();
			userchatGroup.box.repaint();
			UserWelcomeScreen.removeMsg(userId);
			UserWelcomeScreen.showHomeView();
			try {
				if (sendMsg)
					userchatGroup.sendMessage(Constants.REMOVE_CHAT_MSG, "", "", null, new JLabel(),"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public UserChatGroup(RosterVCardBo vCard, int width) {
		super(new GridBagLayout());
		this.roomId = vCard.getUserId();
		this.vcard = vCard;
		multiUserChat = createChatGroup();
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
		textField.requestFocus();
		textField.setRequestFocusEnabled(true);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!textField.getText().trim().equals("") && textField.getText().trim() != null) {
						try {
							sendMessage("", "", "", null, new JLabel("Sending"),"");
							XmppUtils.getMsgEventManager().sendCancelledNotification(
									roomId + "/" + Constants.loggedinuserInfo.username.split("@")[0], "");
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				} else {
					try {
						XmppUtils.getMsgEventManager().sendComposingNotification(
								roomId + "/" + Constants.loggedinuserInfo.username.split("@")[0], "");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				XmppUtils.getMsgEventManager().sendCancelledNotification(roomId + "/" + Constants.loggedinuserInfo.username.split("@")[0],
						"");
			}

		});
		Action enter = new AbstractAction() {
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
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("All Supported Documents", "ppt", "doc", "docx", "xls",
							"xlsx", "csv", "pdf", "PDF", "jpg", "png", "bmp", "jpeg", "tiff"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "bmp", "jpeg", "tiff"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf", "PDF"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft Excel Documents", "xls", "xlsx", "csv"));
					fileChooser
							.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft Word Documents", "doc", "docx", "DOC", "DOCX"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Documents", "txt", "text"));
					fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Microsoft Power Point Documents", "ppt"));
					int rVal = fileChooser.showOpenDialog(getRootPane());
					if (rVal == JFileChooser.APPROVE_OPTION) {
						String url = fileChooser.getSelectedFile().toString();
						Date date = new Date();
						labelTimeStamp = new JLabel(dateformat.format(date));
						labelTimeStamp.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 12));
						Box horBox = Box.createHorizontalBox();
						isFile = true;
						JPanel statusPanel = new JPanel();
						statusPanel.setOpaque(true);
						statusPanel.setBackground(null);
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
							type = "image";
						}
						final String packetId = RandomStringUtils.randomAlphanumeric(6);
						addSendImageInBox("",true,image,imageTosend,url,statusLabel,packetId,type,labelTimeStamp);
						new FileUploadIngThread(fileChooser.getSelectedFile(), null,
								new FileUploaderListener() {

									@Override
									public void fileUploadingProgress(FileUploadIngThread thread, long progress) {
										// TODO Auto-generated method stub
										try {
											statusLabel.setText("Uploading  " + ((progress / (thread.totalsize * 1.0f))) * 100 + "%");
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									@Override
									public void fileUploadingFailed(FileUploadIngThread thread) {
										// TODO Auto-generated method stub
										statusLabel.setText("Failed");
										FileUploadIngThread newUploader = new FileUploadIngThread(thread.uploadingFile, "", thread.listner);
									}

									@Override
									public void fileUploadingCompleted(String response, FileUploadIngThread thread) {
										// TODO Auto-generated method stub

										statusLabel.setText("Uploaded");

										String type = "file";
										/*
										 * Path source =
										 * Paths.get(thread.uploadingFile
										 * .getAbsolutePath()); try{ if
										 * (Files.probeContentType
										 * (source).startsWith("image")) { type
										 * = "image"; } else { type = "file"; }
										 * } catch(Exception e) {
										 * e.printStackTrace(); }
										 */
										try {

											JSONObject jsonResponse = new JSONObject(response);
											// statusLabel = new
											// JLabel("Uploading..");
											String err_code = jsonResponse.getString("err-code");
											// if (err_code.equals("1")) {
											// statusLabel = new JLabel("Sent");
											// }
											String url = jsonResponse.getString("url");
											BufferedImage image = null;
											if (url.endsWith(".docx") || url.endsWith(".doc")) {
												image = ImageIO.read(this.getClass().getResource(
														Constants.IMAGE_PATH + "/" + "word_file.png"));
											} else if (url.endsWith(".xlsx") || url.endsWith(".xls")) {
												image = ImageIO.read(this.getClass().getResource(
														Constants.IMAGE_PATH + "/" + "excel_file.png"));
											} else if (url.endsWith(".pdf")) {
												image = ImageIO.read(this.getClass().getResource(
														Constants.IMAGE_PATH + "/" + "pdf_file.png"));
											} else if (url.endsWith(".txt")) {
												image = ImageIO.read(this.getClass().getResource(
														Constants.IMAGE_PATH + "/" + "text_file.png"));
											} else if (url.endsWith(".ppt")) {
												image = ImageIO.read(this.getClass().getResource(
														Constants.IMAGE_PATH + "/" + "ppt_file.png"));
											} else if (url.endsWith(".csv")) {
												image = ImageIO.read(this.getClass().getResource(
														Constants.IMAGE_PATH + "/" + "excel_file.png"));
											} else {
												type = "image";
												image = ImageIO.read(new URL(url));
											}
											if (image != null) {
												String imageString = EncryptDecryptData.encodeToString(image, "png");

												sendMessage(imageString, url, type, image, statusLabel,packetId);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								});
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		sendMessageButton = new JButton(new ImageIcon(getClass().getResource("/images/send_message.png")));
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
		bottomPanel.setSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()), (int) (Constants.SCREEN_SIZE.getHeight() * 0.10)));
		return bottomPanel;
	}

	public void sendMessage(final String imageString, final String url, final String type, Image imageTosend, final JLabel statusLabel,final String packetId)
			throws IOException {
		final Message message = new Message();
		final JPanel panelFinal = new JPanel();
		statusLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		statusLabel.setForeground(Color.gray);
		final String msgTxt = textField.getText();
		try {
			Date date = null;
			DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
			if (delay != null) {
				date = delay.getStamp();
			} else {
				date = new Date();
			}
			labelTimeStamp = new JLabel(dateformat.format(date));
			labelTimeStamp.setFont(new Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN, 12));
			Box horBox = Box.createHorizontalBox();
			horBox.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));

			if (!Constants.REMOVE_CHAT_MSG.equals(imageString)) {
				/*JPanel statusPanel = new JPanel();
				statusPanel.setOpaque(true);
				statusPanel.setBackground(null);
				if (imageString.equals("")) {
					horBox.add(new GroupChatPanel(image, labelTimeStamp).getRightBubble(msgTxt, false, null, ""));
					statusPanel.add(Box.createHorizontalStrut((int) (Constants.SCREEN_SIZE.getWidth() * 0.25)));

					// UserWelcomeScreen.addMessages(XmppUtils.getVCardBo(roomId),
					// msgTxt, true);
					// UserWelcomeScreen.addMessagesGroup(roomId, msgTxt,
					// dateformat.format(date), false);
					statusPanel.add(statusLabel, BorderLayout.EAST);
					Box vBox = Box.createVerticalBox();
					vBox.add(horBox);
					vBox.add(statusPanel, BorderLayout.EAST);
					
					panelFinal.setOpaque(true);
					panelFinal.setBackground(null);
					panelFinal.add(vBox);
					box.add(panelFinal, BorderLayout.EAST);
					box.repaint();
					box.revalidate();
					scrollPane.scrollDown();
				}*/

				// else {
				// horBox.add(new ChatPanel(image,
				// labelTimeStamp).getRightBubble("", true, imageTosend));
				// statusPanel.add(Box.createHorizontalStrut((int)
				// (Constants.SCREEN_SIZE.getWidth() * 0.20)));
				// UserWelcomeScreen.addMessages(vcard, "File Sent",
				// dateformat.format(date));
				// }

			}

			Runnable run = new Runnable() {
				@Override
				public void run() {
					try {
						if (!roomId.contains("@")) {
							roomId = roomId + "@newconversation.imyourdoc.com";
						}
						final Date date = new Date();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
						final SimpleDateFormat format2 = new SimpleDateFormat("d MMM hh:mm:ss a");
						format.setTimeZone(TimeZone.getTimeZone("UTC"));
						
						if (!imageString.equals(Constants.REMOVE_CHAT_MSG) && !imageString.equals("DELETE_ROOM")
								&& !imageString.equals("Left the room")) {
							if (url.equals("")) {
								JSONObject object = new JSONObject();
								object.put("messageID", message.getPacketID());
								object.put("content", msgTxt);
								object.put("from", Constants.loggedinuserInfo.username);
								object.put("timestamp", format.format(date));
								object.put("to", roomId);
								message.setFrom(Constants.loggedinuserInfo.username);
								message.setTo(roomId);
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
								object.put("to", roomId);
								object.put("file_path", url);
								object.put("file_type", type);
								message.setFrom(Constants.loggedinuserInfo.username);
								message.setTo(roomId);
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
						} else if (imageString.equals(Constants.REMOVE_CHAT_MSG)){
						/*	JSONObject object = new JSONObject();
							object.put("messageID", message.getPacketID());
							object.put("content", Constants.REMOVE_CHAT_MSG);
							object.put("from", Constants.loggedinuserInfo.username);
							object.put("timestamp", format.format(date));
							object.put("to", roomId);*/
							message.setFrom(Constants.loggedinuserInfo.username);
							message.setTo(roomId);
							message.setPacketID("IMYOURDOC_CLOSE");
							message.setBody(Constants.REMOVE_CHAT_MSG);
							//message.setProperty("message_version", "2.0");
							new SendInvitationJSON().ReportOnCloseConversation(roomId);
							UserWelcomeScreen.removeMsg(roomId.split("@")[0]);
						}
						message.setType(Message.Type.groupchat);
						multiUserChat.sendMessage(message);
						MessageBo messageBo = new MessageBo();
						messageBo.setFrom(Constants.loggedinuserInfo.username);
						messageBo.setTo(roomId);
						messageBo.setContent(message.getBody() == null ? "" : message.getBody());
						messageBo.setFileType(message.getProperty("file_type") == null ? "" : message.getProperty("file_type").toString());
						messageBo.setFilePath(message.getSubject() == null ? "" : message.getSubject());
						messageBo.setChatType(Message.Type.groupchat.toString());
						messageBo.setMessageId(message.getPacketID());
						messageBo.setStatus("Sending");
						messageBo.setTimeStamp(format.format(date));
						new DBServiceInsert().insertMessageTable(messageBo);
						new SendInvitationJSON().SubmitMessage(message.getPacketID(), roomId, "Group");
						Calendar c = Calendar.getInstance();
						c.add(Calendar.SECOND, 30);
						Timer timer = new Timer();
						
						if (!Constants.REMOVE_CHAT_MSG.equals(imageString)) {
							
							if (url.equals("")) {
								addSendMessageInBox(msgTxt,message, image, statusLabel,labelTimeStamp);
							}
							
							textField.setText("");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			Thread t = new Thread(run);
			t.start();
			
			// statusLabel.repaint();
			// box.add(statusPanel, BorderLayout.EAST);
			// box.revalidate();
			// box.repaint();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// textField.selectAll();
		// textArea.setCaretPosition(textArea.getDocument().getLength());
	}
	public void addSendMessageInBox(String msg,Message message, byte[] image,JLabel statusLabel,JLabel labelTimeStamp) throws JSONException, IOException {
		Box horBox = Box.createHorizontalBox();
		JPanel statusPanel = new JPanel();
		statusPanel.setOpaque(true);
		statusPanel.setBackground(null);
		if(!msg.equals(Constants.REMOVE_CHAT_MSG))
			horBox.add(new GroupChatPanel(image, labelTimeStamp).getRightBubble(msg, false, null, ""));
		
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
	private MultiUserChat createChatGroup() {
		MultiUserChat multiUserChat = null;

		try {
			multiUserChat = XmppUtils.createChatGroup(roomId, new MessageListener() {
				@Override
				public void processMessage(Chat chat, Message message) {
					// addMsgInBox(message);
					System.out.println("message:----" + message.getBody());
				}
			}, new IAction() {
				@Override
				public void doAction(Object obj1, Object obj2) {
					Message message = (Message) obj1;
					try {
						addMsgInBox(message,false,null);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return multiUserChat;
	}

	public void addMsgInBox(Message message,boolean isOld,JLabel timestamp) throws JSONException {
		// if (message.getType() == Message.Type.groupchat)
		JLabel labelTimeStamp = null;
		String from = message.getFrom();
		String body = message.getBody();
		JSONObject object = null;
		String subject = message.getSubject();
		String fromUser = "";
		if(from.contains("/"))
			fromUser = from.split("/")[1];
		else
			fromUser = from.split("@")[0];
		String currGroup = from.split("/")[0];
		String curr_user = Constants.loggedinuserInfo.username;
		curr_user = curr_user.split("@")[0];

		
		Date date = null;
		String imageUrl = "";
		SimpleDateFormat dateformat = new SimpleDateFormat("d MMM hh:mm:ss a");
		final DelayInformation delay = (DelayInformation) message.getExtension("x", "jabber:x:delay");
		if (delay != null) {
			date = delay.getStamp();
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
			String version = (String) message.getProperty("message_version");
			if (version != null) {
				if (version.equals("2.0")) {
					if(isJSONValid(body)){
						object = new JSONObject(body);
						if (object.has("file_path") && object.has("file_type")) {
							imageUrl = object.getString("file_path");
							body = object.getString("content");
						} else {
							body = object.getString("content");
						}
						from = object.getString("from");
						currGroup = object.getString("to");
					}
				}
			}
			else
			{
				body = message.getBody();
				if(subject!=null){
					imageUrl = subject;
				}
			}
			if(!from.equals(Constants.loggedinuserInfo.username)){
			if (subject != null) {
				if (!subject.startsWith("https://")) {
					//imageUrl = subject;
					RosterVCardBo vCardBo = null;
					String groupSubject ="";
					RoomInfo info = MultiUserChat.getRoomInfo(XmppUtils.connection, currGroup);
					MultiUserChat muc = new MultiUserChat(XmppUtils.connection, currGroup);
					if(info!=null){
						groupSubject = info.getSubject();
						if(org.apache.commons.lang.StringUtils.isEmpty(subject)){
							groupSubject = roomId.split("@")[0];
						}
					}
					else
					{
						groupSubject = XmppUtils.roomUsersStr(currGroup);
					}
					Map<String,String> members = XmppUtils.roomUserIds(currGroup);
					//if(members.isEmpty())
					vCardBo = XmppUtils.createGroupVCardBO(currGroup, groupSubject, members, true);
					if (Constants.currentChatWindowUSERID.equals(currGroup.split("@")[0])) {
						WelcomeUtils.openChatWindow(vCardBo, false);
						UserWelcomeScreen.removeMsg(currGroup.split("@")[0]);
						UserWelcomeScreen.addMessages(vCardBo, "Admin changed subject", false, 
								message.getPacketID(),dateformat.format(new Date()),false);
					}
					// UserWelcomeScreen.showHomeView();
				}
			}
			
				if (null != imageUrl && !imageUrl.trim().equals("") && imageUrl.startsWith("https://")) {
					Box horBox = Box.createHorizontalBox();
					horBox.add(new GroupChatPanel(image, labelTimeStamp).getLeftBubble("File Received", imageUrl, fromUser,
							message.getPacketID(), currGroup));
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
					XmppUtils.getMsgEventManager().sendDeliveredNotification(fromUser + "@imyourdoc.com", message.getPacketID());
					SwingUtilities.invokeLater(new Runnable() {
	
						@Override
						public void run() {
							scrollPane.scrollDown();
						}
					});
					
					if ((Constants.mainFrame.getExtendedState() == 7) || !(Constants.mainFrame.isFocused())) {
						// new PopUpMessage(0, (JFrame) Constants.mainFrame,
						// fromUser,true,currGroup,message.getPacketID())
						// .makeUI("You have a secure message waiting on IM YourDoc from:-"
						// + fromUser);
						// Constants.currentWelcomeScreen.refreshRightBox();
	
					}
				} else if (Constants.REMOVE_CHAT_MSG.equals(body)) {
					RosterVCardBo vCardBo = null;
					if (Constants.showConsole)
						System.out.println("going for remove with---->" + body + " : " + from);
					
					RoomInfo info = MultiUserChat.getRoomInfo(XmppUtils.connection, currGroup);
					String groupSubject = "";
					if(info!=null){
						groupSubject = info.getSubject();
						if(org.apache.commons.lang.StringUtils.isEmpty(groupSubject)){
							groupSubject = roomId.split("@")[0];
						}
					}
					else
					{
						groupSubject = XmppUtils.roomUsersStr(currGroup);
					}
					if(!isOld){
						vCardBo = XmppUtils.createGroupVCardBO(currGroup.split("@")[0], groupSubject, XmppUtils.roomUserIds(currGroup), true);
						JOptionPane.showMessageDialog(getParent(), "<html><center>This conversation was closed by "+fromUser + ".<br/>Please go to new conversation section to start conversation again</center></html>");
						removeUserChat(vCardBo, false);
						UserWelcomeScreen.centerBox.removeAll();
						UserWelcomeScreen.centerBox.add(WelcomeUtils.welcomeBox());
						UserWelcomeScreen.centerBox.repaint();
						UserWelcomeScreen.centerBox.revalidate();
					}
					
					new SendInvitationJSON().ReportOnCloseConversation(currGroup);
	
				} else if (message.getPacketID().equals("IMYOURDOC_DELETE") && !isOld) {
					RosterVCardBo vCardBo = null;
					JOptionPane.showMessageDialog(getParent(), fromUser + " deleted the group!");
					RoomInfo info = MultiUserChat.getRoomInfo(XmppUtils.connection, currGroup);
					String groupSubject = "";
					if(info!=null){
						groupSubject = info.getSubject();
						if(org.apache.commons.lang.StringUtils.isEmpty(groupSubject)){
							groupSubject = roomId.split("@")[0];
						}
					}
					else
					{
						groupSubject = XmppUtils.roomUsersStr(roomId);
					}
					
						vCardBo = XmppUtils.createGroupVCardBO(currGroup, groupSubject, XmppUtils.roomUserIds(currGroup), true);
					
	
					removeUserChat(vCardBo, false);
					UserWelcomeScreen.centerBox.removeAll();
					UserWelcomeScreen.centerBox.add(WelcomeUtils.welcomeBox());
					UserWelcomeScreen.centerBox.repaint();
					UserWelcomeScreen.centerBox.revalidate();
					WelcomeUtils.rosterVcardAllUsersMap = null;
					XmppUtils.groupVCardBOList = null;
					new DBServiceUpdate().DeleteGroup(currGroup);
				} else if (message.getPacketID().equals("REMOVE_REQUEST")) {
					RosterVCardBo bo = XmppUtils.getVCardBo(fromUser);
					RosterVCardBo bo2 = XmppUtils.getVCardBo(currGroup);
					WelcomeUtils.rosterVcardAllUsersMap.remove(bo.getUserId());
					if(currGroup.split("_")[0].equals(Constants.loggedinuserInfo.username.split("@")[0]) && !isOld){
						XmppUtils.deleteUserFromGroup(currGroup, fromUser);
						JOptionPane.showMessageDialog(getParent(), fromUser + " left the room!");
					}
					
					// WelcomeUtils.openChatWindow(bo2, false);
					XmppUtils.deleteUserFromGroup(currGroup, fromUser + "@imyourdoc.com");
				} else if (!StringUtils.isEmpty(body) && subject == null) {
					Box horBox = Box.createHorizontalBox();
					horBox.add(new GroupChatPanel(image, labelTimeStamp).getLeftBubble(body, "", fromUser, message.getPacketID(), currGroup));
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
					if ((Constants.mainFrame.getExtendedState() == 7) || !(Constants.mainFrame.isFocused())) {
						// new PopUpMessage(0, (JFrame) Constants.mainFrame,
						// fromUser,true,currGroup,message.getPacketID())
						// .makeUI("You have a secure message waiting on IM YourDoc from:-"
						// + fromUser);
						// Constants.currentWelcomeScreen.refreshRightBox();
					}
				}
//				if (!currGroup.contains("/")) {
//					currGroup = currGroup + "/" + Constants.loggedinuserInfo.username.split("@")[0];
//				}
	
				SwingUtilities.invokeLater(new Runnable() {
	
					@Override
					public void run() {
						scrollPane.scrollDown();
					}
				});
				if(!isOld){
					URL audioFilePath = this.getClass().getResource("/sounds/ripple.wav");
					// InputStream inputStream =
					// this.getClass().getResourceAsStream("/sounds/ripple.wav");
					AudioInputStream audio = AudioSystem.getAudioInputStream(audioFilePath);
					Clip clip = AudioSystem.getClip();
					clip.open(audio);
					clip.start();
					clip.loop(0);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addMsgInBoxChatState(MessageSyncBo msgSyncBo,String date) throws JSONException {
		
		String body = msgSyncBo.getContent();
		String from = msgSyncBo.getMessageFrom();
		String to = msgSyncBo.getMessageTo();
		String messageID = msgSyncBo.getMessageID();
		String file_type = msgSyncBo.getFileType();
		String timeStamp = msgSyncBo.getChatDate();
		String file_url = msgSyncBo.getFileUrl();
		String with = msgSyncBo.getWith();
		boolean isAllreadyShown = SentMessagePacketIds.contains(messageID);
		JLabel labelTimeStamp = new JLabel();
			SimpleDateFormat dateformat = new SimpleDateFormat("d MMM hh:mm:ss a");
			SimpleDateFormat dateformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
			Message newMessage = new Message();
			try {
					if(!from.contains("@") && from.contains("_")){
						from = from+"@newconversation.imyourdoc.com";
					}
//					if(!to.contains("@") && !to.contains("_")){
//						to = to+"@imyourdoc.com";
//					}
					timeStamp = timeStamp.split(" +0000")[0];
					labelTimeStamp.setText(date);
					RoomInfo info = MultiUserChat.getRoomInfo(XmppUtils.connection, with);
					String subject = "";
					if(info!=null){
						MultiUserChat muc = new MultiUserChat(XmppUtils.connection, with);
						if(!muc.isJoined())
							muc.join(Constants.loggedinuserInfo.username.split("@")[0]);
						subject = info.getSubject();
						if(StringUtils.isEmpty(subject)){
							subject = from.split("@")[0];
						}
					}
					RosterVCardBo vcardbo = XmppUtils.createGroupVCardBO(from.split("@")[0], subject, XmppUtils.roomUserIds(from.split("@")[0]), true);

					// if(vCardBo ==null){
					//
					// }roomUserIds(fromName)
					Map<String, String> map = XmppUtils.roomUserIds(with);
					for(String ids:map.keySet()){
						RosterVCardBo bo = XmppUtils.getVCardBo(ids);
						if(bo.getUserType()!=null){
							if(bo.getUserType().equalsIgnoreCase("patient")){
								vcardbo.setGroupHasPatient(true);
							}
						}
					}
						vcardbo.setIsGroup(true);
						XmppUtils.generateChartPanels(vcardbo, ((int) (Constants.SCREEN_SIZE.getWidth() * 0.20)));
					
					if(!from.split("/")[1].equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0])){
						JSONObject objectRecieve = new JSONObject();
						
						
						if(StringUtils.isEmpty(file_url)){
							
							objectRecieve.put("messageID", messageID);
							objectRecieve.put("content", body);
							objectRecieve.put("from", from);
							objectRecieve.put("timestamp", dateformat.parse(date));
							objectRecieve.put("to", to);
							newMessage.setPacketID(messageID);
							newMessage.setFrom(from);
							newMessage.setTo(to);
							newMessage.setBody(objectRecieve.toString());
							newMessage.setProperty("message_version", "2.0");
							
								UserWelcomeScreen.addMessages(vcardbo, body, true, messageID,labelTimeStamp.getText(),true);
						}
						else
						{
							UserWelcomeScreen.addMessages(vcardbo, "File Recieved", true, messageID,labelTimeStamp.getText(),true);
							objectRecieve.put("messageID", messageID);
							objectRecieve.put("content", body);
							objectRecieve.put("from", from);
							objectRecieve.put("timestamp",  dateformat2.format(dateformat.parse(date)));
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
						addMsgInBox(newMessage, true,labelTimeStamp);
					}
					else if(from.split("/")[1].equalsIgnoreCase(Constants.loggedinuserInfo.username.split("@")[0])){
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
									addSendImageInBox("", true,vcard.getProfileImage(), imageTosend, fileOpen.getPath(), new JLabel("Sent"), messageID, type,labelTimeStamp);
									
								} catch (IOException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
								
								
							}
								if(!StringUtils.isEmpty(file_type)){
										UserWelcomeScreen.addMessages(vcard, "File Sent", true, messageID,labelTimeStamp.getText(),true);
								}
								else
								{
									if(!body.equals(Constants.REMOVE_CHAT_MSG))
										UserWelcomeScreen.addMessages(vcard, body, true, messageID,labelTimeStamp.getText(),true);
								}
							
							}
					}
//				
					
					
//					scrollPane.scrollDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	public void addSendImageInBox(String message,boolean isFile,byte[]image,BufferedImage imageTosend,
			String imageUrl,JLabel statusLabel,String messageId,String fileType,JLabel labelTimeStamp) throws JSONException, IOException {
		Box horBox = Box.createHorizontalBox();
		JPanel statusPanel = new JPanel();
		statusPanel.setOpaque(true);
		statusPanel.setBackground(null);
		horBox.add(new GroupChatPanel(image, labelTimeStamp).getRightBubble("", true, imageTosend, imageUrl));
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
	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
			return true;
		} catch (JSONException ex) {
			return false;
		}
	}


	private static void showReadByPopup(MouseEvent ae, final List<RosterVCardBo> userslist, final String roomId) throws XMPPException,
			IOException {
		JPopupMenu menu = new JPopupMenu();
		MenuScroller.setScrollerFor(menu, 5);
		menu.setLabel("Read By");

		final JMenuItem memberReadBy = new JMenuItem("Read By");
		// Get the event source
		UIManager.put("MenuItem.background", Color.black);
		memberReadBy.setOpaque(false);
		memberReadBy.setBorderPainted(false);
		memberReadBy.setFocusPainted(false);
		memberReadBy.setCursor(new Cursor(Cursor.HAND_CURSOR));
		memberReadBy.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		memberReadBy.setForeground(Color.decode("#9CCD21"));
		memberReadBy.setArmed(false);

		final JMenuItem member2 = new JMenuItem("Delete");
		// Get the event source
		UIManager.put("MenuItem.background", Color.black);
		member2.setOpaque(false);
		member2.setBorderPainted(false);
		member2.setFocusPainted(false);
		member2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		member2.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		member2.setForeground(Color.decode("#9CCD21"));
		member2.setArmed(false);

		final JMenuItem member3 = new JMenuItem("Forward");
		UIManager.put("MenuItem.background", Color.black);
		// Get the event source
		member3.setOpaque(false);
		member3.setBorderPainted(false);
		member3.setFocusPainted(false);
		member3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		member3.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));

		member3.setForeground(Color.decode("#9CCD21"));
		member3.setArmed(false);

		menu.add(memberReadBy);
		// menu.addSeparator();
		// menu.add(member2);
		// menu.addSeparator();
		// menu.add(member3);

		/*memberReadBy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Constants.mainFrame.setEnabled(false);
				Members compose = new Members(Constants.mainFrame, userslist, "Read By", roomId);
				compose.setVisible(true);
			}
		});*/

		Component b = (Component) ae.getSource();

		Point p = b.getLocationOnScreen();
		menu.show(b, 0, 0);
		menu.setLocation(p.x, p.y + b.getHeight());
		menu.setOpaque(true);
		menu.setBackground(Color.white);
		menu.setAutoscrolls(true);
	}

	private static void showPopupMemberList(MouseEvent ae, List<RosterVCardBo> userslist) throws XMPPException, IOException {
		JPopupMenu menu = new JPopupMenu();
		MenuScroller.setScrollerFor(menu, 5);
		menu.setLabel("Read By");
		for (RosterVCardBo vcard : userslist) {
			BufferedImage icon = Util.getProfileImg(vcard.getUserId());
			if (icon == null) {
				icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/" + "default_pp.png"));
			}
			JLabel userPic = Util.combine(icon, false, 60, 60);
			final JMenuItem member = new JMenuItem(vcard.getName(), userPic.getIcon());
			// Get the event source
			member.setOpaque(false);
			member.setBorderPainted(false);
			member.setFocusPainted(false);
			member.setCursor(new Cursor(Cursor.HAND_CURSOR));
			member.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
			member.setBackground(Color.white);
			member.setForeground(Color.decode("#9CCD21"));
			member.setArmed(false);
			menu.add(member);
			menu.addSeparator();
		}
		Component b = (Component) ae.getSource();

		Point p = b.getLocationOnScreen();
		menu.show(b, 0, 0);
		menu.setLocation(p.x, p.y + b.getHeight());
		menu.setOpaque(true);
		menu.setBackground(Color.white);
		menu.setAutoscrolls(true);
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

	private static class SendNotify extends TimerTask {
		// This task will execute just once after seven seconds of starting the
		// program
		JLabel label;
		String msgId;

		public SendNotify(JLabel label, String msgId) {
			this.label = label;
			this.msgId = msgId;
		}

		public void run() {
			// if(Constants.showConsole)
			// System.out.println("I will be displayed after five seconds" +
			// " ->" + new Date());
			try {
				sendNotify();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		public void sendNotify() throws JSONException {
			SendInvitationJSON json = new SendInvitationJSON();
			String response = json.sendNotification(msgId);
			JSONObject obj = new JSONObject(response);
			int err_code = obj.getInt("err-code");
			if (obj.getInt("err-code") == Integer.parseInt("600")) {
				// JOptionPane.showMessageDialog(getParent(),
				// obj.getString("message"));
				if (Constants.showConsole)
					System.out.println(obj.getString("message"));
			} else if (err_code == 1) {
				// JOptionPane.showMessageDialog(getParent(),
				// obj.getString("message"));
				if (obj.getInt("notification_state") == 1 && obj.getBoolean("mail_sent") == true) {
					if (Constants.showConsole)
						System.out.println(obj.getString("message_id"));
					if (Constants.showConsole)
						System.out.println(obj.getString("message"));
					label.setText("Notification/Email Sent");
				} else if (obj.getInt("notification_state") == 1) {
					if (Constants.showConsole)
						System.out.println(obj.getString("message_id"));
					if (Constants.showConsole)
						System.out.println(obj.getString("message"));
					label.setText("Notification already Sent");
				} else if (obj.getInt("notification_state") == 0) {
					if (Constants.showConsole)
						System.out.println(obj.getString("message_id"));
					if (Constants.showConsole)
						System.out.println(obj.getString("message"));
					if (!(label.getText().equalsIgnoreCase("delivered")) && !(label.getText().equalsIgnoreCase("read"))
							&& !(label.getText().equalsIgnoreCase("uploaded")))
						label.setText("Sending failed");
					// else if(label.getText().equalsIgnoreCase("uploading")){
					// label.setText("File not sent");
					// }
				} else {
					label.setText("Sent");
				}
			}
		}
	}
}

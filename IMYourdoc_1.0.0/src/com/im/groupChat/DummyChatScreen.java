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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.json.JSONException;
import org.json.JSONObject;

import com.im.bo.RosterVCardBo;
import com.im.common.LightScrollPane;
import com.im.login.MenuScroller;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.utils.Constants;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;

public class DummyChatScreen extends JPanel {
	private static final long serialVersionUID = 1L;
	protected JTextArea textField;
	protected Box box;
	private MultiUserChat multiUserChat;
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
	SimpleDateFormat dateformat = new SimpleDateFormat("d MMM h:mm a ");
	// JLabel statusLabel;
	public static Map<RosterVCardBo, DummyChatScreen> userChatMap;

	public static DummyChatScreen getUserChat(RosterVCardBo vCard, int width, MouseListener sendTextFieldClick) {
		if (null == userChatMap)
			userChatMap = new HashMap<>();
		if (null == userChatMap.get(vCard))
			userChatMap.put(vCard, new DummyChatScreen(vCard, width, sendTextFieldClick));

		return userChatMap.get(vCard);
	}


	public DummyChatScreen(RosterVCardBo vCard, int width, MouseListener sendTextFieldClick) {
		super(new GridBagLayout());
		if (vCard != null) {
			this.roomId = vCard.getUserId();
			this.vcard = vCard;
		}
		// multiUserChat = createChatGroup();
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
		textField = new JTextArea(1, 35);
		textField.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		textField.setWrapStyleWord(true);
		textField.setLineWrap(true);
		if (sendTextFieldClick != null)
			textField.addMouseListener(sendTextFieldClick);
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
		vbox.setMinimumSize(new Dimension((int) (Constants.PARENT.getWidth() * 0.52), (int) (Constants.PARENT.getHeight() * 0.62)));
		vbox.setPreferredSize(new Dimension((int) (Constants.PARENT.getWidth() * 0.52), (int) (Constants.PARENT.getHeight() * 0.62)));
		JPanel finalPanel = new JPanel();
		// BoxLayout layout = new BoxLayout(finalPanel, BoxLayout.LINE_AXIS);
		// finalPanel.setLayout(layout);
		finalPanel.setPreferredSize(new Dimension((int) (Constants.PARENT.getWidth() * 0.52), (int) (Constants.PARENT.getHeight() * 0.62)));
		finalPanel.setMinimumSize(new Dimension((int) (Constants.PARENT.getWidth() * 0.52), (int) (Constants.PARENT.getHeight() * 0.62)));
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
		textField.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				if (vcard != null) {
					UserWelcomeScreen.centerBox.setOpaque(true);
					UserWelcomeScreen.centerBox.setBackground(Color.white);
					UserWelcomeScreen.centerBox.removeAll();
					JPanel loaderPanel = Util.loaderPanel("Please Wait...");
					UserWelcomeScreen.centerBox.add(loaderPanel, BorderLayout.CENTER);
					UserWelcomeScreen.centerBox.revalidate();
					UserWelcomeScreen.centerBox.repaint();
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
						@Override
						protected Void doInBackground() throws Exception {
							WelcomeUtils.openChatWindow(vcard, false);
							return null;
						}
					};
					worker.execute();
				}
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
		// labelAttachment.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// try {
		// final JLabel statusLabel = new JLabel("Uploading");
		// statusLabel.setFont(new
		// Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN,
		// 18));
		// statusLabel.setForeground(Color.gray);
		// final JFileChooser fileChooser = new JFileChooser();
		// fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// fileChooser.setAcceptAllFileFilterUsed(false);
		// fileChooser.addChoosableFileFilter(new
		// FileNameExtensionFilter("All Supported Documents", "ppt", "doc",
		// "docx", "xls",
		// "xlsx", "csv", "pdf", "PDF", "jpg", "png", "bmp", "jpeg", "tiff"));
		// fileChooser.addChoosableFileFilter(new
		// FileNameExtensionFilter("Images", "jpg", "png", "bmp", "jpeg",
		// "tiff"));
		// fileChooser.addChoosableFileFilter(new
		// FileNameExtensionFilter("PDF Documents", "pdf", "PDF"));
		// fileChooser.addChoosableFileFilter(new
		// FileNameExtensionFilter("Microsoft Excel Documents", "xls", "xlsx",
		// "csv"));
		// fileChooser
		// .addChoosableFileFilter(new
		// FileNameExtensionFilter("Microsoft Word Documents", "doc", "docx",
		// "DOC", "DOCX"));
		// fileChooser.addChoosableFileFilter(new
		// FileNameExtensionFilter("Text Documents", "txt", "text"));
		// fileChooser.addChoosableFileFilter(new
		// FileNameExtensionFilter("Microsoft Power Point Documents", "ppt"));
		//
		// int rVal = fileChooser.showOpenDialog(getRootPane());
		// if (rVal == JFileChooser.APPROVE_OPTION) {
		// System.out.println(fileChooser.getSelectedFile().toString());
		// String url = fileChooser.getSelectedFile().toString();
		// Date date = new Date();
		// labelTimeStamp = new JLabel(dateformat.format(date));
		// labelTimeStamp.setFont(new
		// Font(Font.decode("CentraleSansRndLight").getFontName(), Font.PLAIN,
		// 12));
		// Box horBox = Box.createHorizontalBox();
		// //
		// horBox.add(Box.createHorizontalStrut((int)(Constants.SCREEN_SIZE.getWidth()
		// // * 0.10)));
		// isFile = true;
		// JPanel statusPanel = new JPanel();
		// statusPanel.setOpaque(true);
		// statusPanel.setBackground(null);
		// BufferedImage imageTosend = null;
		// if (url.endsWith(".docx") || url.endsWith(".doc")) {
		// imageTosend =
		// ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
		// "/word_file.png"));
		// } else if (url.endsWith(".xlsx") || url.endsWith(".xls")) {
		// imageTosend =
		// ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
		// "/excel_file.png"));
		// } else if (url.endsWith(".pdf")) {
		// imageTosend =
		// ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
		// "/pdf_file.png"));
		// } else if (url.endsWith(".txt")) {
		// imageTosend =
		// ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
		// "/text_file.png"));
		// } else if (url.endsWith(".ppt")) {
		// imageTosend =
		// ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
		// "/ppt_file.png"));
		// } else if (url.endsWith(".csv")) {
		// imageTosend =
		// ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH +
		// "/excel_file.png"));
		// } else {
		// imageTosend = ImageIO.read(new File(url));
		// }
		// horBox.add(new GroupChatPanel(image,
		// labelTimeStamp).getRightBubble("", true, imageTosend, url));
		// // statusPanel.add(Box.createHorizontalStrut((int)
		// // (Constants.SCREEN_SIZE.getWidth() * 0.10)));
		// UserWelcomeScreen.addMessages(XmppUtils.getVCardBo(roomId),
		// "File Sent", true);
		// // UserWelcomeScreen.addMessagesGroup(roomId, "File Sent",
		// dateformat.format(date), false);
		// statusPanel.add(statusLabel);
		// Box vBox = Box.createVerticalBox();
		// vBox.add(horBox);
		// vBox.add(statusPanel);
		// JPanel panelFinal = new JPanel(new BorderLayout());
		// panelFinal.setOpaque(true);
		// panelFinal.setBackground(null);
		// panelFinal.add(vBox, BorderLayout.EAST);
		// box.add(panelFinal, BorderLayout.EAST);
		// SwingUtilities.invokeLater(new Runnable() {
		//
		// @Override
		// public void run() {
		// scrollPane.scrollDown();
		// }
		// });
		//
		// // XmppUtils.setStatusLabel(roomId, statusLabel);
		// SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
		// @Override
		// protected Void doInBackground() throws Exception {
		// sendFileToUser(fileChooser.getSelectedFile().toString(),
		// statusLabel);
		// return null;
		// }
		// };
		// worker.execute();
		// // Calendar c = Calendar.getInstance();
		// // c.add(Calendar.SECOND, 30);
		// // Timer timer = new Timer();
		// // timer.schedule(new SendNotify(statusLabel,""),
		// // 30000);
		// // return null;
		// // }
		// // };
		// // worker.execute();
		// }
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		// }
		// });
		sendMessageButton = new JButton(new ImageIcon(getClass().getResource("/images/send_message.png")));
		sendMessageButton.setOpaque(false);
		sendMessageButton.setContentAreaFilled(false);
		sendMessageButton.setBorderPainted(false);
		sendMessageButton.setFocusPainted(false);
		sendMessageButton.setBackground(null);
		/*
		 * sendMessageButton.addActionListener(new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent e) { try { if
		 * (textField.getText().trim() != null &&
		 * !textField.getText().trim().equals("")) { sendMessage("", "", "",
		 * null, new JLabel("Sending")); textField.setCaretPosition(0); } }
		 * catch (IOException e1) { e1.printStackTrace(); }
		 * textField.requestFocus(); textField.setCaretPosition(0); } });
		 */
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

		bottomBox.add(attach);
		bottomBox.add(sc);
		bottomBox.add(sendPanel);
		bottomBox.setOpaque(true);
		bottomBox.setBackground(null);
		Box box = Box.createVerticalBox();
		JPanel statusPanel = new JPanel();
		statusPanel.setOpaque(true);
		statusPanel.setBackground(Color.decode("#F2F1EA"));
		box.add(statusPanel, BorderLayout.NORTH);
		box.add(bottomBox, BorderLayout.SOUTH);
		bottomPanel.add(box);
		bottomPanel.setOpaque(true);
		bottomPanel.setBackground(Color.decode("#F2F1EA"));
		bottomPanel.setSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()), (int) (Constants.SCREEN_SIZE.getHeight() * 0.05)));
		return bottomPanel;
	}



	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
			return true;
		} catch (JSONException ex) {
			return false;
		}
	}


	private static void showReadByPopup(MouseEvent ae, final List<RosterVCardBo> userslist) throws XMPPException, IOException {
		JPopupMenu menu = new JPopupMenu();
		MenuScroller.setScrollerFor(menu,5);
		menu.setLabel("Read By");
		final JMenuItem member = new JMenuItem("Read By");
		// Get the event source
		member.setOpaque(false);
		member.setBorderPainted(false);
		member.setFocusPainted(false);
		member.setCursor(new Cursor(Cursor.HAND_CURSOR));
		member.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		member.setBackground(Color.white);
		member.setForeground(Color.decode("#9CCD21"));
		member.setArmed(false);

		final JMenuItem member2 = new JMenuItem("Delete");
		// Get the event source
		member2.setOpaque(false);
		member2.setBorderPainted(false);
		member2.setFocusPainted(false);
		member2.setCursor(new Cursor(Cursor.HAND_CURSOR));
		member2.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		member2.setBackground(Color.white);
		member2.setForeground(Color.decode("#9CCD21"));
		member2.setArmed(false);

		final JMenuItem member3 = new JMenuItem("Forward");
		// Get the event source
		member3.setOpaque(false);
		member3.setBorderPainted(false);
		member3.setFocusPainted(false);
		member3.setCursor(new Cursor(Cursor.HAND_CURSOR));
		member3.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
		member3.setBackground(Color.white);
		member3.setForeground(Color.decode("#9CCD21"));
		member3.setArmed(false);

		menu.add(member);
		menu.addSeparator();
		menu.add(member2);
		menu.addSeparator();
		menu.add(member3);

		member.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					showPopupMemberList(e, userslist);
				} catch (XMPPException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

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
		menu.setLabel("Read By");
		MenuScroller.setScrollerFor(menu,5);
		for (RosterVCardBo vcard : userslist) {
			BufferedImage icon = Util.getProfileImg(vcard.getUserId());
			if (icon == null) {
				icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));
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
}

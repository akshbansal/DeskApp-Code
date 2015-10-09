package com.im.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.plaf.basic.BasicScrollBarUI;

import org.jdesktop.swingx.prompt.PromptSupport;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.MultiUserChat;

import com.im.bo.CustomComparator;
import com.im.bo.RosterVCardBo;
import com.im.db.DBServiceResult;
import com.im.login.MenuScroller;
import com.im.utils.Constants;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;
import com.im.utils.XmppUtils;

public class Members extends JDialog {
	private static final long serialVersionUID = 1L;
	private final ResourceBundle rb;
	private Frame parent;
	int count = 0;
	String userType;
	JDialog thisParent;
	JPanel panelList = new JPanel();
	private static Map<String, RosterVCardBo> rosterVCardBoListMap;
	Point point = new Point();
	private  List<RosterVCardBo> vCardList;
	boolean resizing = false;
	private String title;
	private String roomId;

	public Members(JFrame parent, List<RosterVCardBo> vCardList, String title, String roomId) {
		super(parent, "Members");
		rb = ResourceBundle.getBundle("com.im.properties.IMProperties");
		this.roomId = roomId;
		Constants.IS_DIALOG = true;
		this.vCardList = vCardList;
		this.parent = parent;
		this.title = title;
		parent.setEnabled(false);
		Constants.PARENT = parent;
		thisParent = this;
		Constants.childWindowOpened = this;
		// setModalityType(ModalityType.TOOLKIT_MODAL);
		setLocationRelativeTo(parent);
		// setLocation(parent.getX() + 10, parentLabel.getY() + 50);
		int x = (int) ((Constants.SCREEN_SIZE.getWidth()) / 4);
		int y = (int) ((Constants.SCREEN_SIZE.getHeight()) / 4);
		setLocation(x, y);
		setUndecorated(true);
		getRootPane().setBackground(Color.white);
		getContentPane().setBackground(Color.white);
		setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.50), (int) (Constants.SCREEN_SIZE.getHeight() * 0.50)));
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 1, 2, 0));
		setTitle(title);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		try {
			initUI();
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					resizing = getCursor().equals(Cursor.getDefaultCursor()) ? false : true;
					if (!e.isMetaDown()) {
						point.x = e.getX();
						point.y = e.getY();
					}
				}
			});
			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {
					if (resizing) {
						Point pt = e.getPoint();
						setSize(getWidth() + pt.x - point.x, getHeight());
						point.x = pt.x;
					} else if (!e.isMetaDown()) {
						Point p = getLocation();
						setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initUI() throws IOException {

		
		final Box vBox = Box.createVerticalBox();
		add(TopPanel.topButtonPanelForAccountDialog((JDialog) this, title), BorderLayout.NORTH);
		
		JPanel loader = Util.loaderPanel("Loading Please wait..");
		vBox.add(loader);
		vBox.repaint();
		vBox.revalidate();
		SwingWorker<Void,Void> worker = new SwingWorker<Void, Void>(){

			@Override
			protected Void doInBackground() throws Exception {
				Box searchBoxPanel = Box.createHorizontalBox();
				JLabel searchLabel = new JLabel(new ImageIcon(
						((new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/"+"search.png"))).getImage()).getScaledInstance(20, 20,
								java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
				searchBoxPanel.setBorder(new TextBubbleBorder(Color.lightGray, 2, 2, 0));
				searchBoxPanel.add(Box.createHorizontalStrut(10));
				searchBoxPanel.add(getSearcheableBox("Search user to add in group", roomId));
				searchBoxPanel.setOpaque(true);
				searchBoxPanel.setBackground(Color.white);
				searchBoxPanel.add(Box.createHorizontalStrut(10));
				searchBoxPanel.add(searchLabel);
				final Box vblist = showMemberList("");
				JPanel list = new JPanel();
				list.add(vblist);
				list.setOpaque(true);
				list.setBackground(Color.white);
				LightScrollPane sc2 = new LightScrollPane(list);
				sc2.setPreferredSize(new Dimension(50, 300));
				vBox.removeAll();
				if (!title.equalsIgnoreCase("Read by")) {
					if (Constants.loggedinuserInfo.username.split("@")[0].equals(roomId.split("_")[0])) {
						vBox.add(getSearcheableBox("Search user to add in group", roomId));
						vBox.add(Box.createVerticalStrut(10));
					}
				}
				vBox.add(sc2);
				vBox.repaint();
				vBox.revalidate();
				
				return null;
			}
			
		};
		worker.execute();
		JPanel finalPanel = new JPanel();
		finalPanel.add(vBox);
		finalPanel.repaint();
		finalPanel.revalidate();
		finalPanel.setOpaque(true);
		finalPanel.setBackground(Color.white);
		add(vBox, BorderLayout.CENTER);
	}

	private Box showMemberList(String newUserId) {
		final Box vblist = Box.createVerticalBox();
		final JTextField txtField = new JTextField(20);
		try {
			final JPanel pfinal = new JPanel();
			final Box jPanel = Box.createVerticalBox();
			JLabel profilePic = null;
			BufferedImage icon = null;
			// if(!newUserId.equals("")){
			// RosterVCardBo vcardNewUser =
			// XmppUtils.getVCardBo(newUserId,"group");
			// if(!vCardList.contains(vcardNewUser))
			// vCardList.add(vcardNewUser);
			// }
			
			if (!vCardList.isEmpty()) {
				// Collections.sort(vCardList, new CustomComparator());
				for (RosterVCardBo member:vCardList) {
					
					final String userId = member.getUserId();
					if(userId != null){
						 icon = Util.getProfileImg(userId);
					}
					//final RosterVCardBo memberbo = XmppUtils.getVCardBo(userId);
					
					if (icon == null) {
						icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));
					}

					profilePic = Util.combine(icon, false, 60, 60);
					JPanel profilePanel = new JPanel(new BorderLayout());

					final Box horBox = Box.createHorizontalBox();
					Box vbox = Box.createVerticalBox();
					StringBuffer namebuffer = new StringBuffer();
					namebuffer.append(userId.split("@")[0]);
					// if (vCard.getDesignation() != null &&
					// vCard.getDesignation().trim().equals("")) {
					// namebuffer.append(vCard.getDesignation() == null ? "" :
					// ", " + vCard.getDesignation());
					// }
					String name = namebuffer.toString();
					StringBuffer titleBuffer = new StringBuffer();
					titleBuffer.append(member.getMembership());
							
				
					String title = titleBuffer.toString();
					if (title.length() > 20) {
						title = title.substring(0, 20) + "...";
					}
					profilePic.setText("<html><font style='color:#9CCD21;'>" + name
							+ "</font><br/><font style='color:gray;font-size:small'>" + title + "</font></html>");
					profilePic.setIconTextGap(20);
					profilePanel.setOpaque(true);
					profilePanel.setBackground(Color.white);
					profilePanel.add(profilePic, BorderLayout.WEST);
					JLabel removeUserFromGroup = new JLabel("X");
					removeUserFromGroup.setForeground(Color.gray);
					removeUserFromGroup.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
					profilePic.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
					vbox.setOpaque(true);
					horBox.setName(userId);
					horBox.setAlignmentX(SwingConstants.LEFT);
					horBox.add(profilePanel, BorderLayout.WEST);
					if (Constants.loggedinuserInfo.username.split("@")[0].equals(roomId.split("_")[0])) {
							if (!(userId.split("@")[0].equals(roomId.split("_")[0]))) {
								horBox.add(removeUserFromGroup, BorderLayout.EAST);
						}
					}
					
					horBox.setOpaque(true);
					horBox.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.width * 0.40),
							(int) (Constants.SCREEN_SIZE.height * 0.10)));
					horBox.setBackground(Color.white);
					horBox.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
					horBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

					jPanel.add(horBox);
					pfinal.setOpaque(true);
					pfinal.setBackground(Color.white);
					pfinal.add(jPanel);
					removeUserFromGroup.addMouseListener(new MouseAdapter() {

						@Override
						public void mouseClicked(MouseEvent e) {
							// TODO Auto-generated method stub
							SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

								@Override
								protected Void doInBackground() throws Exception {
									
									XmppUtils.deleteUserFromGroup(roomId, userId);
									vCardList.remove(XmppUtils.getVCardBo(userId));
									//thisParent.dispose();
									jPanel.remove(horBox);
									jPanel.repaint();
									jPanel.revalidate();
									pfinal.repaint();
									pfinal.revalidate();
									
									/*Members compose = new Members(Constants.mainFrame, members, "Group Members", roomId);
									compose.setVisible(true);*/
									return null;
								}
							};
							worker.execute();
//							
						}
					});
				}
			} else {
				JLabel noMessgage = new JLabel("<HTML><I><br>No Users to display</br></I></HTML>");
				noMessgage.setOpaque(true);
				noMessgage.setBackground(Color.white);
				noMessgage.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
				JPanel messagePanel = new JPanel();
				// if no list
				noMessgage.setForeground(Color.gray);
				messagePanel.setOpaque(true);
				messagePanel.setBackground(Color.white);
				messagePanel.add(noMessgage, BorderLayout.CENTER);
				jPanel.add(messagePanel);
				// pfinal.add(jPanel);
			}

			vblist.removeAll();
			vblist.add(pfinal);
			vblist.revalidate();
			vblist.repaint();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return vblist;
	}

	private Box getSearcheableBox(final String placeholder, final String roomId) {
		final Box box = Box.createVerticalBox();
		final JTextField txtField = getSearchField(placeholder);
		JLabel searchLabel = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/"+"search.png"))).getImage()).getScaledInstance(20, 20,
						java.awt.Image.SCALE_SMOOTH)), JLabel.HORIZONTAL);
		JLabel add_user = new JLabel(new ImageIcon(
				((new ImageIcon(getClass().getResource(Constants.IMAGE_PATH + "/"+"ok_button.png"))).getImage()).getScaledInstance(40, 40,
						java.awt.Image.SCALE_FAST)), JLabel.HORIZONTAL);
		add_user.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		add_user.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if (!txtField.getText().equals("")) {
					
						// Constants.loader.setVisible(true);
						SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

							@Override
							protected Void doInBackground() throws Exception {
								// Constants.loader.setVisible(false);
								String rid = roomId;
								if (!rid.contains("@newconversation.imyourdoc.com")) {
									rid = rid + "@newconversation.imyourdoc.com";
								}
								thisParent.dispose();
								XmppUtils.addUserToGroup(roomId, txtField.getName());
								MultiUserChat muc = new MultiUserChat(XmppUtils.connection,rid);
								List<RosterVCardBo> members = new DBServiceResult().getGroupDetails(rid.split("@")[0]);
								//if(members.isEmpty())
//								List<RosterVCardBo> membersList = new ArrayList<RosterVCardBo>();
//								for(String uid:members.keySet()){
//									RosterVCardBo vcard = XmppUtils.getVCardBo(uid);
//									membersList.add(vcard);
//								}
								Members compose = new Members(Constants.mainFrame, members, "Group Members", rid);
								compose.setVisible(true);
								return null;
							}

						};
						worker.execute();
					} else
						txtField.requestFocus();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		Box searchFieldBox = Box.createHorizontalBox();
		searchFieldBox.setBorder(new TextBubbleBorder(Color.lightGray, 2, 2, 0));
		searchFieldBox.setOpaque(true);
		searchFieldBox.setBackground(Color.white);
		searchFieldBox.setPreferredSize(new Dimension((int) (getWidth() * 0.40), 40));
		searchFieldBox.setMaximumSize(new Dimension((int) (getWidth() * 0.40), 40));
		searchFieldBox.add(Box.createHorizontalStrut(10));
		searchFieldBox.add(txtField);
		searchFieldBox.add(Box.createHorizontalStrut(10));
		searchFieldBox.add(searchLabel);

		Box searchBox = Box.createHorizontalBox();
		searchBox.add(Box.createHorizontalStrut(10));
		searchBox.add(searchFieldBox);
		searchBox.setOpaque(true);
		searchBox.setBackground(Color.WHITE);
		searchBox.add(Box.createHorizontalStrut(15));

		searchBox.add(Box.createHorizontalStrut(10));
		searchBox.setPreferredSize(new Dimension((int) (getWidth() * 0.40), 60));
		searchBox.setMaximumSize(new Dimension((int) (getWidth() * 0.40), 60));
		// searchPanel.add(Box.createHorizontalStrut(10));
		searchBox.add(add_user);
		txtField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField textfield = ((JTextField) e.getSource());
				String text = ((JTextField) e.getSource()).getText();
				try {
					if (!text.equals("")) {
						List<RosterVCardBo> list = XmppUtils.getAllVCards(text, false);
						showListUsersMenu(e, list);
						textfield.requestFocusInWindow();
						textfield.requestFocus();
						textfield.requestFocus(true);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		box.add(searchBox);
		// list.setLayout(new GridLayout(0, 1, 1, 1));
		// Box panelList = Box.createVerticalBox();//new JPanel(new
		// GridLayout(0, 1, 1, 1));
		// panelList.add(list);

		return box;
	}

	private static void showListUsersMenu(KeyEvent ae, List<RosterVCardBo> vCardList) throws XMPPException, IOException {
		final JTextField textField = (JTextField) ae.getSource();
		JPopupMenu menu = new JPopupMenu();
		MenuScroller.setScrollerFor(menu, 5);
		JLabel profilePic = null;
		int count = 0;
		BufferedImage icon = null;
		Collections.sort(vCardList, new CustomComparator());
		for (final RosterVCardBo vCard : vCardList) {
			if (Constants.showConsole)
				System.out.println(vCard.getSubscription());
			final String userId = vCard.getUserId();
			icon = Util.getProfileImg(userId);
			if (icon == null) {
				icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH + "/"+"default_pp.png"));
			}

			profilePic = Util.combine(icon, false, 60, 60);
			final String menuItemName = vCard.getName() == "" ? vCard.getUserId().split("@")[0] : vCard.getName();
			JMenuItem menuItem = new JMenuItem(menuItemName, profilePic.getIcon());
			menuItem.setOpaque(false);
			menuItem.setBorderPainted(false);
			menuItem.setFocusPainted(false);
			menuItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
			menuItem.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 20));
			menuItem.setBackground(Color.white);
			menuItem.setForeground(Color.decode("#9CCD21"));
			menuItem.setArmed(false);
			menuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textField.setText(menuItemName);
					textField.setName(vCard.getUserId());
				}
			});
			menu.add(menuItem);
			count = count + 1;
			if (count != vCardList.size()) {
				menu.addSeparator();
			}
		}

		ae.getComponent().requestFocus();
		Component b = (Component) ae.getSource();

		// Get the location of the point 'on the screen'
		Point p = b.getLocationOnScreen();
		menu.show(b, 0, 0);
		menu.setLocation(p.x, p.y + b.getHeight());
		menu.setOpaque(true);
		menu.setAutoscrolls(true);
		menu.setBackground(Color.white);
	}

	private JTextField getSearchField(String placeholder) {
		final JTextField searchField = new JTextField(20) {
			private static final long serialVersionUID = 1L;

			@Override
			public void addNotify() {
				super.addNotify();
				requestFocus();
			}
		};
		searchField.addFocusListener(new FocusListener() 
		 {
		   @Override
		   public void focusLost(FocusEvent e) {
		    // TODO Auto-generated method stub
		   }
		   @Override
		   public void focusGained(FocusEvent e) {
		    // TODO Auto-generated method stub
			   searchField.setCaretPosition(searchField.getText().length());
		    searchField.setSelectionStart(searchField.getText().length());
		   }
		  });
		searchField.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		PromptSupport.setPrompt(placeholder, searchField);
		searchField.setBorder(null);
		return searchField;
	}

	private String roomUsersStr() {
		Map<String, RosterVCardBo> vCardMap = rosterVCardBoListMap;
		StringBuilder userStr = new StringBuilder("");
		if (vCardMap != null) {
			for (String ids : vCardMap.keySet()) {
				if (userStr.toString().equals(""))
					userStr.append(ids.split("@")[0]);
				else
					userStr.append("," + ids.split("@")[0]);
			}
		}
		return userStr.toString();
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
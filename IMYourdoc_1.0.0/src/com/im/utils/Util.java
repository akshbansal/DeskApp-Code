package com.im.utils;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.json.JSONObject;

import com.im.chat.UserChat;
import com.im.json.LogoutJSON;
import com.im.login.Login;
import com.im.login.combine;
import com.im.patientscreens.UserWelcomeScreen;

//import com.io.common.FooterPanel;
//import com.io.common.Header;

public class Util {

	public static Map<String, BufferedImage> userProfileImgMap;
	private static boolean status = false;

	public static BufferedImage getProfileImg(String userId) {
		userId = getUserId(userId);
		BufferedImage icon = userProfileImgMap.get(userId);
		if (null == icon) {
			try {
				icon = ImageIO.read(new URL(
						"https://api.imyourdoc.com/profilepic.php?user_name="
								+ userId));
				if (null == icon)
					icon = ImageIO.read(Util.class
							.getResource(Constants.IMAGE_PATH+"/"+"default_pp.png"));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (null == userProfileImgMap)
				userProfileImgMap = new HashMap<>();
			userProfileImgMap.put(userId, icon);
		}
		return icon;
	}

	public static void removeProfileImg(String userId) {
		userId = getUserId(userId);
		userProfileImgMap.put(userId, null);
	}

	public static void setProfileImg(String userId, BufferedImage profileImg) {
		userId = getUserId(userId);
		userProfileImgMap.put(userId, profileImg);
	}

	private static String getUserId(String userId) {
		if (userId.contains("@"))
			userId = userId.split("@")[0];
		if (null == userProfileImgMap)
			userProfileImgMap = new HashMap<>();
		return userId;
	}

	public static Boolean isStringEmpty(String string) {
		return !(string != null && !string.isEmpty() && !string.trim()
				.isEmpty());
	}

	public static JLabel anchor(final String link, String txt) {
		JLabel linkLabel = new JLabel(
				"<html><font color=\"blue\" <a href=\"\">" + txt
						+ "</a></html>");
		linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

		linkLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent paramMouseEvent) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseClicked(MouseEvent paramMouseEvent) {
				try {
					Desktop.getDesktop().browse(new URL(link).toURI());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		return linkLabel;
	}

	public static String getHTTPResponseStr(String urlStr, String params) {
		try {
			String urlParameters = params;// "param1=a&param2=b&param3=c";
			byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
			int postDataLength = postData.length;
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setDoOutput(true);
			con.setDoInput(true);
			con.setInstanceFollowRedirects(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.setRequestProperty("charset", "utf-8");
			con.setRequestProperty("Content-Length",
					Integer.toString(postDataLength));
			con.setUseCaches(false);
			try (DataOutputStream wr = new DataOutputStream(
					con.getOutputStream())) {
				wr.write(postData);
			}
			con.connect();
			int status = con.getResponseCode();
			switch (status) {
			case 200:
			case 201:
				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				return sb.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage makeRoundedCorner(BufferedImage image,
			int cornerRadius) {
		BufferedImage scaledImage = Scalr.resize(image, Method.ULTRA_QUALITY,
				90, 90, Scalr.OP_BRIGHTER);
		int w = scaledImage.getWidth();
		int h = scaledImage.getHeight();
		BufferedImage output = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();

		// This is what we want, but it only does hard-clipping, i.e. aliasing
		// g2.setClip(new RoundRectangle2D ...)
		// so instead fake soft-clipping by first drawing the desired clip shape
		// in fully opaque white with antialiasing enabled...
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fill(new RoundRectangle2D.Float(1, 0, w, h, cornerRadius,
				cornerRadius));
		// ... then compositing the image on top,
		// using the white shape from above as alpha source
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(scaledImage, 1, 0, null);

		g2.dispose();

		return output;
	}

	public static JPanel loaderPanel(String loadingText) {
		JPanel loaderPanel = new JPanel();
		loaderPanel.setOpaque(true);
		loaderPanel.setBackground(null);
		JLabel label = new JLabel(new ImageIcon(Util.class
				.getResource(Constants.IMAGE_PATH + "/"
						+ "ajax-loader.gif")));
		label.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 15));
		label.setText(loadingText);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setForeground(Color.gray.darker());
		loaderPanel.add(label, BorderLayout.CENTER);
		return loaderPanel;
	}

	public static void setUserTypeToConstant(String userType) {
		if (null != userType && !"".equals(userType)) {
			if ("physician".equalsIgnoreCase(userType))
				Constants.loggedinuserInfo.isProvider = true;
			else if ("patient".equalsIgnoreCase(userType))
				Constants.loggedinuserInfo.isPatient = true;
			else if ("staff".equalsIgnoreCase(userType))
				Constants.loggedinuserInfo.isStaff = true;
		}
	}

	public static JLabel combine(BufferedImage imageUser, boolean isProfileTop,
			int width, int height) throws IOException {
		final BufferedImage imageTransparent;
		final BufferedImage img2;
		if (!imageUser.getColorModel().hasAlpha())
			img2 = ImageUtils.createHeadlessSmoothBufferedImage(imageUser,
					ImageUtils.IMAGE_PNG, width, height);
		else
			img2 = ImageUtils.resizeImage(imageUser, ImageUtils.IMAGE_PNG,
					width, height);
		if (isProfileTop) {
			imageTransparent = ImageIO.read(combine.class
					.getResource("/images/transparent_top.png"));
		} else {
			imageTransparent = ImageIO.read(combine.class
					.getResource("/images/transparent.png"));
		}
		final BufferedImage scaled = new BufferedImage(img2.getWidth(),
				img2.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = scaled.getGraphics();
		g.drawImage(imageTransparent, 0, 0, scaled.getWidth(),
				scaled.getHeight(), null);
		g.dispose();
		final int xMax = img2.getWidth() - scaled.getWidth();
		final int yMax = img2.getHeight() - scaled.getHeight();
		final JLabel label = new JLabel(new ImageIcon(img2));

		// ActionListener listener = new ActionListener() {
		//
		// Random random = new Random();
		//
		// public void actionPerformed(ActionEvent ae) {
		Graphics g2 = img2.getGraphics();
		int x = xMax;
		int y = yMax;

		g2.drawImage(scaled, x, y, null);
		g2.dispose();
		g2.setClip(null);

		if (isProfileTop)
			label.setOpaque(true);
		label.repaint();
		// JPanel panel = new JPanel();
		// panel.setOpaque(true);
		// panel.setBackground(Color.white);
		// panel.add(label);
		// JFrame f = new JFrame();
		// f.setMinimumSize(new Dimension(500,500));
		// label.setMaximumSize(new Dimension(50, 50));
		// f.add(label);
		// f.getContentPane().setBackground(Color.white);
		// f.setVisible(true);
		return label;
	}
	public static JLabel combineCompose(BufferedImage imageUser, boolean isProfileTop,
			int width, int height,boolean isDefault) throws IOException {
		final BufferedImage imageTransparent;
		final BufferedImage img2;
		if (!imageUser.getColorModel().hasAlpha())
			img2 = ImageUtils.createHeadlessSmoothBufferedImage(imageUser,
					ImageUtils.IMAGE_PNG, width, height);
		else
			img2 = ImageUtils.resizeImage(imageUser, ImageUtils.IMAGE_PNG,
					width, height);
		if (isProfileTop) {
			imageTransparent = ImageIO.read(combine.class
					.getResource("/images/transparent_top.png"));
		} else {
			imageTransparent = ImageIO.read(combine.class
					.getResource("/images/transparent.png"));
		}
		final BufferedImage scaled = new BufferedImage(img2.getWidth(),
				img2.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = scaled.getGraphics();
		g.drawImage(imageTransparent, 0, 0, scaled.getWidth(),
				scaled.getHeight(), null);
		g.dispose();
		final int xMax = img2.getWidth() - scaled.getWidth();
		final int yMax = img2.getHeight() - scaled.getHeight();
		final JLabel label = new JLabel(new ImageIcon(img2));

		// ActionListener listener = new ActionListener() {
		//
		// Random random = new Random();
		//
		// public void actionPerformed(ActionEvent ae) {
		Graphics g2 = img2.getGraphics();
		int x = xMax;
		int y = yMax;

		g2.drawImage(scaled, x, y, null);
		g2.dispose();
		g2.setClip(null);

		if (isProfileTop)
			label.setOpaque(false);
		else 
			if(isDefault)
				label.setOpaque(true);
		label.repaint();
		// JPanel panel = new JPanel();
		// panel.setOpaque(true);
		// panel.setBackground(Color.white);
		// panel.add(label);
		// JFrame f = new JFrame();
		// f.setMinimumSize(new Dimension(500,500));
		// label.setMaximumSize(new Dimension(50, 50));
		// f.add(label);
		// f.getContentPane().setBackground(Color.white);
		// f.setVisible(true);
		return label;
	}

	public static JLabel combineGreen(BufferedImage imageUser, int width,
			int height) throws IOException {
		final BufferedImage imageTransparent;
		final BufferedImage img2;
		if (!imageUser.getColorModel().hasAlpha())
			img2 = ImageUtils.createHeadlessSmoothBufferedImage(imageUser,
					ImageUtils.IMAGE_PNG, width, height);
		else
			img2 = ImageUtils.resizeImage(imageUser, ImageUtils.IMAGE_PNG,
					width, height);
		imageTransparent = ImageIO.read(combine.class
				.getResource("/images/transparent_green.png"));
		final BufferedImage scaled = new BufferedImage(img2.getWidth(),
				img2.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = scaled.getGraphics();
		g.drawImage(imageTransparent, 0, 0, scaled.getWidth(),
				scaled.getHeight(), null);
		g.dispose();
		final int xMax = img2.getWidth() - scaled.getWidth();
		final int yMax = img2.getHeight() - scaled.getHeight();
		final JLabel label = new JLabel(new ImageIcon(img2));

		// ActionListener listener = new ActionListener() {
		//
		// Random random = new Random();
		//
		// public void actionPerformed(ActionEvent ae) {
		Graphics g2 = img2.getGraphics();
		int x = xMax;
		int y = yMax;

		g2.drawImage(scaled, x, y, null);
		g2.dispose();
		g2.setClip(null);

		label.setOpaque(false);
		label.repaint();
		label.revalidate();
		// JPanel panel = new JPanel();
		// panel.setOpaque(true);
		// panel.setBackground(Color.white);
		// panel.add(label);
		// JFrame f = new JFrame();
		// f.setMinimumSize(new Dimension(500,500));
		// label.setMaximumSize(new Dimension(50, 50));
		// f.add(label);
		// f.getContentPane().setBackground(Color.white);
		// f.setVisible(true);
		return label;
	}

	public static BufferedImage combine2(BufferedImage imageUser,
			boolean isProfileTop, int width, int height) throws IOException {
		final BufferedImage imageTransparent;
		final BufferedImage img2;
		if (!imageUser.getColorModel().hasAlpha())
			img2 = ImageUtils.createHeadlessSmoothBufferedImage(imageUser,
					ImageUtils.IMAGE_PNG, width, height);
		else
			img2 = ImageUtils.resizeImage(imageUser, ImageUtils.IMAGE_PNG,
					width, height);
		if (isProfileTop) {
			imageTransparent = ImageIO.read(combine.class
					.getResource("/images/transparent_top.png"));
		} else {
			imageTransparent = ImageIO.read(combine.class
					.getResource("/images/transparent.png"));
		}
		final BufferedImage scaled = new BufferedImage(img2.getWidth(),
				img2.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = scaled.getGraphics();
		g.drawImage(imageTransparent, 0, 0, scaled.getWidth(),
				scaled.getHeight(), null);
		g.dispose();
		final int xMax = img2.getWidth() - scaled.getWidth();
		final int yMax = img2.getHeight() - scaled.getHeight();
		// final JLabel label = new JLabel(new ImageIcon(img2));

		// ActionListener listener = new ActionListener() {
		//
		// Random random = new Random();
		//
		// public void actionPerformed(ActionEvent ae) {
		Graphics g2 = img2.getGraphics();
		int x = xMax;
		int y = yMax;
		g2.drawImage(scaled, x, y, null);
		g2.dispose();
		g2.setClip(null);
		// if(isProfileTop)
		// label.setOpaque(true);
		// label.repaint();
		// JPanel panel = new JPanel();
		// panel.setOpaque(true);
		// panel.setBackground(Color.white);
		// panel.add(label);
		// JFrame f = new JFrame();
		// f.setMinimumSize(new Dimension(500,500));
		// label.setMaximumSize(new Dimension(50, 50));
		// f.add(label);
		// f.getContentPane().setBackground(Color.white);
		// f.setVisible(true);
		return img2;
	}

	public static void highlight(final JPanel jPanel, final JLabel username) {
		// for (int i = 0; i <= 5; i++) {
		jPanel.setOpaque(true);
		jPanel.setBackground(Color.decode("#9CCD21"));

		username.setForeground(Color.white);
		// }
	}

	public static JButton setTransparentBtn(JButton jButton) {
		jButton.setOpaque(true);
		jButton.setContentAreaFilled(false);
		jButton.setBorderPainted(false);
		jButton.setFocusPainted(false);
		jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return jButton;
	}
	
	public static byte[] bufferImgToByteArray(BufferedImage img){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, "jpg", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public static boolean logout() {

		// SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>(){
		//
		// @Override
		// protected Boolean doInBackground() throws Exception {
		LogoutJSON logoutJson = new LogoutJSON();
		String response = "";
		JSONObject jsonResponse = null;
		String err_code = "";
		// logout code
		try {
			response = logoutJson.doLogout();
			jsonResponse = new JSONObject(response);
			err_code = jsonResponse.getString("err-code");
			if (err_code.equals("1")) {
				SystemTray tray = SystemTray.getSystemTray();
				TrayIcon trayIcon = Constants.TRAY_ICON;
				if (tray.getTrayIcons().equals(trayIcon)) {
					tray.remove(trayIcon);
				}
				Constants.loader.setVisible(false);
				// JOptionPane.showMessageDialog(null,
				// "You are logged out successfully.");
				Constants.loggedinuserInfo.login_token = null;
				status = true;
			} else if (err_code.equals("600")) {
				Constants.loader.setVisible(false);
				JOptionPane.showMessageDialog(null,
						"Your login session has already been expired.");
				disposeLogoutMenu(Constants.mainFrame);
				status = false;
			} else if (!XmppUtils.connection.isConnected()) {
				Constants.loader.setVisible(false);
				JOptionPane.showMessageDialog(null, "Server not connected");
				status = false;
			}
		} catch (Exception ex) {
			status = false;
		}
		// return status;
		// }

		// };
		// worker.execute();
		return status;
	}

	public static void disposeAll(JDialog container, JFrame parent) {
		container.dispose();
		parent.dispose();
		TrayIcon trayIcon = Constants.TRAY_ICON;
		SystemTray.getSystemTray().remove(trayIcon);
		WelcomeUtils.chatPanelMap = null;
		WelcomeUtils.welcomeBox = null;
		WelcomeUtils.loginUserProfileIcon = null;
		UserWelcomeScreen.messageListMap = null;
		UserWelcomeScreen.rightBoxTab = null;
		Constants.loggedinuserInfo.login_token = null;
		Constants.loggedinuserInfo.username = null;
		Constants.typeStatus.setText("");

		XmppUtils.connection.disconnect();
		XmppUtils.destroy();
		XmppUtils.connection = null;
		XmppUtils.rosterEntries = null;
		XmppUtils.rosterVCardBoMap = null;
		XmppUtils.eventManager = null;
		XmppUtils.rosterVCardBoListMap = null;
		XmppUtils.groupVCardBOList = null;
		XmppUtils.readByUserMap  = null;
		WelcomeUtils.rosterVcardAllUsersMap = null;
		XmppUtils.myVCard = null;
		XmppUtils.roster = null;
		UserChat.userChatMap = null;
		Login login = new Login();
		try {
			login.initComponents();
			Constants.loader.dispose();
		} catch (IOException | FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Constants.childWindowOpened != null)
			Constants.childWindowOpened.dispose();
		Constants.loggedinuserInfo.password = null;
		Constants.loggedinuserInfo.license_key = null;
		Constants.loggedinuserInfo.status = null;
		Constants.loggedinuserInfo.login_token = null;
		Constants.loggedinuserInfo.user_pin = null;
		Constants.loggedinuserInfo.isDeviceRegisterRequired = false;
		Constants.loggedinuserInfo.user_type = null;
		Constants.loggedinuserInfo.name = null;
		Constants.loggedinuserInfo.profileImage = null;
		Constants.loggedinuserInfo.device_type = null;
		Constants.loggedinuserInfo.device_id = null;
		Constants.loggedinuserInfo.seq_ques = null;
		Constants.loggedinuserInfo.seq_ans = null;
		Constants.loggedinuserInfo.isPatient = false;
		Constants.loggedinuserInfo.isProvider = false;
		Constants.loggedinuserInfo.isStaff = false;
	}

	public static void disposeAll(JFrame container, JFrame parent) {
		container.dispose();
		parent.dispose();
		WelcomeUtils.chatPanelMap = null;
		TrayIcon trayIcon = Constants.TRAY_ICON;
		SystemTray.getSystemTray().remove(trayIcon);
		WelcomeUtils.welcomeBox = null;
		WelcomeUtils.loginUserProfileIcon = null;
		UserWelcomeScreen.messageListMap = null;
		UserWelcomeScreen.rightBoxTab = null;
		Constants.loggedinuserInfo.login_token = null;
		Constants.loggedinuserInfo.username = null;
		Constants.typeStatus.setText("");
		XmppUtils.connection.disconnect();
		XmppUtils.destroy();
		XmppUtils.connection = null;
		XmppUtils.rosterEntries = null;
		XmppUtils.rosterVCardBoMap = null;
		XmppUtils.eventManager = null;
		XmppUtils.rosterVCardBoListMap = null;
		XmppUtils.groupVCardBOList = null;
		XmppUtils.readByUserMap  = null;
		WelcomeUtils.rosterVcardAllUsersMap = null;
		XmppUtils.myVCard = null;
		XmppUtils.roster = null;
		UserChat.userChatMap = null;
		Login login = new Login();
		try {
			login.initComponents();
			Constants.loader.dispose();
		} catch (IOException | FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Constants.childWindowOpened != null)
			Constants.childWindowOpened.dispose();
		Constants.loggedinuserInfo.password = null;
		Constants.loggedinuserInfo.license_key = null;
		Constants.loggedinuserInfo.status = null;
		Constants.loggedinuserInfo.login_token = null;
		Constants.loggedinuserInfo.user_pin = null;
		Constants.loggedinuserInfo.isDeviceRegisterRequired = false;
		Constants.loggedinuserInfo.user_type = null;
		Constants.loggedinuserInfo.name = null;
		Constants.loggedinuserInfo.profileImage = null;
		Constants.loggedinuserInfo.device_type = null;
		Constants.loggedinuserInfo.device_id = null;
		Constants.loggedinuserInfo.seq_ques = null;
		Constants.loggedinuserInfo.seq_ans = null;
		Constants.loggedinuserInfo.isPatient = false;
		Constants.loggedinuserInfo.isProvider = false;
		Constants.loggedinuserInfo.isStaff = false;
	}

	public static void disposeLogoutMenu(JFrame parent) {
		parent.dispose();
		WelcomeUtils.chatPanelMap = null;
		WelcomeUtils.welcomeBox = null;
		Constants.typeStatus.setText("");
		TrayIcon trayIcon = Constants.TRAY_ICON;
		SystemTray.getSystemTray().remove(trayIcon);
		WelcomeUtils.loginUserProfileIcon = null;
		UserWelcomeScreen.messageListMap = null;
		UserWelcomeScreen.rightBoxTab = null;
		Constants.loggedinuserInfo.login_token = null;
		Constants.loggedinuserInfo.username = null;
		XmppUtils.connection.disconnect();
		XmppUtils.eventManager = null;
		XmppUtils.rosterEntries = null;
		XmppUtils.rosterVCardBoMap = null;
		XmppUtils.rosterVCardBoListMap = null;
		XmppUtils.groupVCardBOList = null;
		XmppUtils.readByUserMap  = null;
		WelcomeUtils.rosterVcardAllUsersMap = null;
		XmppUtils.myVCard = null;
		XmppUtils.roster = null;
		UserChat.userChatMap = null;
		Login login = new Login();
		try {
			login.initComponents();
			Constants.loader.dispose();
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		if (Constants.childWindowOpened != null)
			Constants.childWindowOpened.dispose();
		Constants.loggedinuserInfo.password = null;
		Constants.loggedinuserInfo.license_key = null;
		Constants.loggedinuserInfo.status = null;
		Constants.loggedinuserInfo.login_token = null;
		Constants.loggedinuserInfo.user_pin = null;
		Constants.loggedinuserInfo.isDeviceRegisterRequired = false;
		Constants.loggedinuserInfo.user_type = null;
		Constants.loggedinuserInfo.name = null;
		Constants.loggedinuserInfo.profileImage = null;
		Constants.loggedinuserInfo.device_type = null;
		Constants.loggedinuserInfo.device_id = null;
		Constants.loggedinuserInfo.seq_ques = null;
		Constants.loggedinuserInfo.seq_ans = null;
		Constants.loggedinuserInfo.isPatient = false;
		Constants.loggedinuserInfo.isProvider = false;
		Constants.loggedinuserInfo.isStaff = false;
	}
}

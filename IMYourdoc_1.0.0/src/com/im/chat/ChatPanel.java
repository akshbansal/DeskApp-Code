package com.im.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.apache.commons.io.FileUtils;

import com.im.db.DBServiceUpdate;
import com.im.utils.Constants;
import com.im.utils.EncryptDecryptData;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;
import com.im.utils.XmppUtils;

public class ChatPanel {
	private JTextArea messageLbl;
	private JLabel userImageLbl;
	private JPanel msgPanel;
	private JLabel imagesent;
	private JLabel imageRecieved;
	byte[] image;
	JLabel time;
	JLabel labelReceived;
	File fileToSave;
	private boolean fileIsSaved = false;
	
	public ChatPanel(byte[] image, JLabel time) {
		this.image = image;
		this.time = time;
	}
	
	public JPanel getLeftBubble(final String msg,final String imageUrl, final String From,final String messageId,boolean playSound) throws IOException {
	
		msgPanel = new LeftBubble();
		labelReceived = new JLabel("<html><a href=''>Download</a></html>");
		labelReceived.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		/*
		 * messageLbl = new JLabel(labelText) { private static final long
		 * serialVersionUID = 1L; public Dimension getPreferredSize() { // The
		 * label should be of fixed width and the height should be computed //
		 * to exactly fit the text. Dimension preferredSize =
		 * super.getPreferredSize(); return new Dimension(preferredSize.width,
		 * preferredSize.height); } };
		 */
		
		//		messageLbl.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		BufferedImage icon;
		if (image != null) {
			icon = ImageIO.read(new ByteArrayInputStream(image));
		} else {
			icon = ImageIO.read(WelcomeUtils.class.getResource("/images/default_pp.png"));
		}
		userImageLbl = Util.combine(icon, false, 50, 50);
		userImageLbl.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
		//        userImageLbl.setIconTextGap(JLabel.BOTTOM);
		//        userImageLbl.setText(From);
		userImageLbl.setHorizontalTextPosition(JLabel.HORIZONTAL);
		userImageLbl.setOpaque(true);
		userImageLbl.setBackground(null);
		if (imageUrl.trim() != null && imageUrl.trim().startsWith("https://")) {
			BufferedImage img = null;
			if (imageUrl.trim().endsWith(".docx") || imageUrl.trim().endsWith(".doc")) {
				img = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/"+"word_file.png"));
			} else if (imageUrl.trim().endsWith(".xlsx") || imageUrl.trim().endsWith(".xls")) {
				img = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/"+"excel_file.png"));
			} else if (imageUrl.trim().endsWith(".txt")) {
				img = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/"+"text_file.png"));
			} else if (imageUrl.trim().endsWith(".pdf")) {
				img = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/"+"pdf_file.png"));
			} else if (imageUrl.trim().endsWith(".ppt")) {
				img = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/"+"ppt_file.png"));
			}else if (imageUrl.trim().endsWith(".csv")) {
				img = ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH + "/"+"excel_file.png"));
			} 
			else {
				img = ImageIO.read(new URL(imageUrl));//EncryptDecryptData.stringToImage(msg);//
			}
			imageRecieved = new JLabel(new ImageIcon(((new ImageIcon(img)).getImage()).getScaledInstance(100, 100,
				java.awt.Image.SCALE_FAST)), JLabel.CENTER);
			labelReceived.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					
					if (fileIsSaved == false) {
						JLabel clickToDownload = new JLabel(
							"<html>Notice!Downloading this document, will move the document<br/> out of the HIPAA "
								+ " secure, IM Your Doc application.</br> Do you want to continue?</html>");
						int value = JOptionPane.showConfirmDialog(null, clickToDownload, "", JOptionPane.OK_CANCEL_OPTION);
						if (value == JOptionPane.OK_OPTION) {
							final JFileChooser fileChooser = new JFileChooser();
							fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
							final String fileName = imageUrl.trim().substring(imageUrl.trim().lastIndexOf("/") + 1, imageUrl.trim().length());
							//File file = new File(fileChooser.getCurrentDirectory()+"/"+fileName);
							
							fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), fileName));
							
							// fileChooser.setSelectedFile(new File(fileName));
							// Demonstrate "Save" dialog:
							
							int rVal = fileChooser.showSaveDialog(null);
							if (rVal == JFileChooser.APPROVE_OPTION) {
								// to save image code here  
								try {
									
									fileChooser.addPropertyChangeListener(new PropertyChangeListener() {
										public void propertyChange(PropertyChangeEvent evt) {
											if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
												File oldDir = (File) evt.getOldValue();
												File newDir = (File) evt.getNewValue();
												File curDir = fileChooser.getCurrentDirectory();
												if(Constants.showConsole){
													if(Constants.showConsole) System.out.println("oldDir;;;" + oldDir);
													if(Constants.showConsole) System.out.println("newDir;;;" + newDir);
													if(Constants.showConsole) System.out.println("curDir;;;" + curDir);
												}
												fileChooser.setSelectedFile(new File(newDir, fileName));
												//					    		          fileToSave = fileChooser.getSelectedFile();
											}
										}
									});
									fileToSave = fileChooser.getSelectedFile();
									if(Constants.showConsole)
										if(Constants.showConsole) System.out.println("file tosave path;;;;;" + fileToSave);
									saveFileToSystem(msg.trim(), fileName, fileToSave);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							if (rVal == JFileChooser.CANCEL_OPTION) {}
						}
					}
				}
			});
			imageRecieved.setCursor(new Cursor(Cursor.HAND_CURSOR));
			imageRecieved.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					File destDir = FileUtils.getTempDirectory();
					String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
					try {
						if(fileName.contains(" ")){
							fileName = fileName.trim();
						}
						File finalFile = new File(destDir.getPath()+"/"+fileName);
						String urlstring = imageUrl;
						urlstring = urlstring.replaceAll(" ","%20");
						URL url = new URL(urlstring);
						
							FileUtils.copyURLToFile(url, finalFile);
						
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					if(Constants.showConsole) System.out.println(FileUtils.getTempDirectoryPath());
					if (!Desktop.isDesktopSupported()) {
						if(Constants.showConsole) if(Constants.showConsole) System.out.println("Desktop is not supported");
						return;
					}
					
					File fileOpen = new File(destDir.getPath()+"/"+fileName);
					Desktop desktop = Desktop.getDesktop();
					if (fileOpen.exists()) {
						try {
							desktop.open(fileOpen);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						JOptionPane.showMessageDialog(null, "File cannot be opened.", "Warning!",
							JOptionPane.WARNING_MESSAGE);
					}
					}
			});
			Box vbox = Box.createVerticalBox();
			imageRecieved.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			vbox.add(imageRecieved, BorderLayout.CENTER);
			time.setForeground(Color.white);
			time.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 14));
			vbox.add(Box.createHorizontalStrut(5));
			vbox.add(time);
			vbox.add(Box.createHorizontalStrut(5));
			msgPanel.add(vbox);
			
		} else if (null != msg.trim()) {
//			if(!From.contains("@imyourdoc.com"))
//				XmppUtils.getMsgEventManager().sendDisplayedNotification(From+"@imyourdoc.com", messageId);
//			else
//				XmppUtils.getMsgEventManager().sendDisplayedNotification(From, messageId);
			messageLbl = new JTextArea();
			messageLbl.setForeground(Color.white);
			messageLbl.setText(msg.trim());
			messageLbl.setEditable(false);
			messageLbl.setCursor(null);
			messageLbl.setOpaque(false);
			messageLbl.setFocusable(false);
			messageLbl.setLineWrap(true);
			messageLbl.setWrapStyleWord(true);
			messageLbl.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
			messageLbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			Box vbox = Box.createVerticalBox();
			vbox.add(messageLbl, BorderLayout.CENTER);
			time.setForeground(Color.white);
			time.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 14));
			vbox.add(Box.createHorizontalStrut(5));
			vbox.add(time);
			msgPanel.add(vbox);
		}
		
		JPanel finalBox = new JPanel();//Box.createHorizontalBox();
		finalBox.setBackground(Color.white);
		finalBox.setOpaque(true);
		finalBox.add(userImageLbl);
		finalBox.add(msgPanel);
		Box box = Box.createVerticalBox();
		box.add(finalBox);
		if (imageUrl.trim() != null && imageUrl.trim().startsWith("https://")) {
			box.add(labelReceived,BorderLayout.EAST);
		}
		JPanel finalPanel = new JPanel();
		finalPanel.add(box);
		finalPanel.setOpaque(true);
		finalPanel.setBackground(Color.white);
		if(playSound){
			if(Constants.currentChatWindowUSERID!=null){
				if(Constants.currentChatWindowUSERID.split("@")[0].equals(From.split("@")[0])){
					if(Constants.mainFrame.isFocused()){
						new DBServiceUpdate().updateMessageStatusTable("Read", messageId,From.split("@")[0]);
						if(!From.contains("@imyourdoc.com"))
							XmppUtils.getMsgEventManager().sendDisplayedNotification(From+"@imyourdoc.com", messageId);
						else
							XmppUtils.getMsgEventManager().sendDisplayedNotification(From, messageId);
					}
					else
					{
						new DBServiceUpdate().updateMessageStatusTable("Delivered", messageId,From.split("@")[0]);
						if(!From.contains("@imyourdoc.com"))
							XmppUtils.getMsgEventManager().sendDeliveredNotification(From+"@imyourdoc.com", messageId);
						else
							XmppUtils.getMsgEventManager().sendDeliveredNotification(From, messageId);
					}
				}
				else
				{
					new DBServiceUpdate().updateMessageStatusTable("Delivered", messageId,From.split("@")[0]);
					if(!From.contains("@imyourdoc.com"))
						XmppUtils.getMsgEventManager().sendDeliveredNotification(From+"@imyourdoc.com", messageId);
					else
						XmppUtils.getMsgEventManager().sendDeliveredNotification(From, messageId);
				}
			}
		}
		return finalPanel;
	}
	
	public JPanel getRightBubble(String msg, boolean isFile, Image imageToSend,final String url) throws IOException {
		msgPanel = new RightBubble();
		BufferedImage icon = Util.getProfileImg(Constants.loggedinuserInfo.username);
		if (icon == null) {
			icon = ImageIO.read(WelcomeUtils.class.getResource(Constants.IMAGE_PATH +"/"+ "default_pp.png"));
			
		}
		userImageLbl = Util.combine(icon, false, 50, 50);
		if (isFile) {
			BufferedImage img = null;
			//        	messageLbl.setMaximumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.80), 80));
			/*
			 * if(msg.endsWith(".docx")||msg.endsWith(".doc")){ img =
			 * ImageIO.read
			 * (this.getClass().getResource(Constants.IMAGE_PATH+"/word_file.png"
			 * )); } else if(msg.endsWith(".xlsx")||msg.endsWith(".xls")){ img =
			 * ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH+
			 * "/excel_file.png")); } else if(msg.endsWith(".txt")){ img =
			 * ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH+
			 * "/text_file.png")); } else if(msg.endsWith(".pdf")){ img =
			 * ImageIO.read(this.getClass().getResource(Constants.IMAGE_PATH+
			 * "/pdf_file.png")); }else if(msg.endsWith(".ppt")){ img =
			 * ImageIO.read
			 * (this.getClass().getResource(Constants.IMAGE_PATH+"/ppt_file.png"
			 * )); } else{ img = ImageIO.read(new File(msg)); }
			 */
			img = (BufferedImage) imageToSend;
			imagesent = new JLabel(
				new ImageIcon(((new ImageIcon(img)).getImage()).getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER);
			imagesent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			imagesent.setCursor(new Cursor(Cursor.HAND_CURSOR));
			imagesent.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						File finalFile = new File(url);
						if (!Desktop.isDesktopSupported()) {
							if(Constants.showConsole) if(Constants.showConsole) System.out.println("Desktop is not supported");
							return;
						}
						
						//File fileOpen = new File(destDir.getPath()+"/"+fileName);
						Desktop desktop = Desktop.getDesktop();
						if (finalFile.exists()) {
							try {
								desktop.open(finalFile);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							JOptionPane.showMessageDialog(null, "File has been deleted from system.", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						}
//						URL url = new URL(msg);
//						if(!finalFile.exists())
//							FileUtils.copyURLToFile(url, finalFile);
						
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					}
			});
			Box vbox = Box.createVerticalBox();
			vbox.add(imagesent, BorderLayout.CENTER);
			time.setForeground(Color.white);
			time.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 14));
			vbox.add(time);
			vbox.add(Box.createHorizontalStrut(5));
			msgPanel.add(vbox);
			
		} else if (null != msg.trim()) {
			
			messageLbl = new JTextArea();
			messageLbl.setText(msg.trim());
			messageLbl.setEditable(false);
			messageLbl.setCursor(null);
			messageLbl.setOpaque(false);
			messageLbl.setFocusable(false);
			messageLbl.setLineWrap(true);
			messageLbl.setWrapStyleWord(true);
			if (msg.trim().length() > 10) {
				messageLbl.setColumns(15);
				messageLbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			} else {
				messageLbl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			}
			time.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			messageLbl.setForeground(Color.white.brighter());
			//			messageLbl.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
			messageLbl.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
			Box vbox = Box.createVerticalBox();
			vbox.add(messageLbl, BorderLayout.CENTER);
			time.setForeground(Color.white);
			time.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 14));
			vbox.add(time, BorderLayout.EAST);
			msgPanel.add(vbox);
		}
		Box vbox = Box.createVerticalBox();
		vbox.add(msgPanel);
		vbox.add(Box.createVerticalStrut(5));
		Box profileBox = Box.createVerticalBox();
		profileBox.add(userImageLbl, BorderLayout.SOUTH);
		JPanel finalBox = new JPanel();//Box.createHorizontalBox();
		finalBox.setBackground(Color.white);
		finalBox.setOpaque(true);
		finalBox.add(vbox);
		finalBox.add(profileBox);
		JPanel finalPanel = new JPanel();
		finalPanel.add(finalBox);
		finalPanel.setOpaque(true);
		finalPanel.setBackground(Color.white);
		return finalPanel;
		
	}
	
	public void saveFileToSystem(final String fileURL, final String filePath, final File file) throws IOException {
		Constants.loader.setVisible(true);
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			
			@Override
			protected Void doInBackground() throws Exception {
				URL url = new URL(fileURL);
				/*
				 * InputStream inputStream; HttpURLConnection httpConn =
				 * (HttpURLConnection) url.openConnection(); int responseCode =
				 * httpConn.getResponseCode(); String fileName =""; // always
				 * check HTTP response code first if (responseCode ==
				 * HttpURLConnection.HTTP_OK) { String disposition =
				 * httpConn.getHeaderField("Content-Disposition"); String
				 * contentType = httpConn.getContentType(); int contentLength =
				 * httpConn.getContentLength();
				 * 
				 * if (disposition != null) { // extracts file name from header
				 * field int index = disposition.indexOf("filename="); if (index
				 * > 0) { fileName = disposition.substring(index + 10,
				 * disposition.length() - 1); } } else { // extracts file name
				 * from URL fileName =
				 * fileURL.substring(fileURL.lastIndexOf("/") + 1,
				 * fileURL.length()); }
				 * 
				 * // output for debugging purpose only
				 * if(Constants.showConsole) System.out.println("Content-Type = " + contentType);
				 * if(Constants.showConsole) System.out.println("Content-Disposition = " + disposition);
				 * if(Constants.showConsole) System.out.println("Content-Length = " + contentLength);
				 * if(Constants.showConsole) System.out.println("fileName = " + fileName);
				 * 
				 * // opens input stream from the HTTP connection inputStream =
				 * httpConn.getInputStream(); FileOutputStream outputStream =
				 * new FileOutputStream(filePath+"/"+fileName);
				 */
				if(Constants.showConsole) if(Constants.showConsole) System.out.println("fileUrl:----" + fileURL);
				
				String fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
				if(Constants.showConsole) {
					if(Constants.showConsole) System.out.println("fileName:-----" + fileName);
					if(Constants.showConsole) System.out.println("filePath:----" + filePath);
					if(Constants.showConsole) System.out.println("????>>>>>>>>>" + file.getAbsolutePath());
				}
				FileUtils.copyURLToFile(url, file);
				Constants.loader.setVisible(false);
				JOptionPane.showMessageDialog(null, "File Saved successfully");
				labelReceived.setText("<html><a href=''>Open</a></html>");
				labelReceived.repaint();
				fileIsSaved = true;
				labelReceived.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						File fileOpen = file;
						
						//first check if Desktop is supported by Platform or not
						if (!Desktop.isDesktopSupported()) {
							if(Constants.showConsole) if(Constants.showConsole) System.out.println("Desktop is not supported");
							return;
						}
						
						Desktop desktop = Desktop.getDesktop();
						if (fileOpen.exists()) {
							try {
								desktop.open(fileOpen);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							JOptionPane.showMessageDialog(null, "File has been deleted from system.", "Warning!",
								JOptionPane.WARNING_MESSAGE);
						}
					}
				});
				return null;
			}
			
		};
		worker.execute();
	}
}

class LeftBubble extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BufferedImage img;
	
	@Override
	public void paintComponent(Graphics g) {
		try {
			img = ImageIO.read(ChatPanel.class.getResource("/images/black_bubble.png"));
			super.paintComponent(g);
			setOpaque(true);
			setBackground(Color.white);
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class RightBubble extends JPanel {
	private static final long serialVersionUID = 1L;
	BufferedImage img;
	
	@Override
	public void paintComponent(Graphics g) {
		try {
			img = ImageIO.read(this.getClass().getResource("/images/green_bubble.png"));
			super.paintComponent(g);
			setOpaque(true);
			setBackground(Color.white);
			g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
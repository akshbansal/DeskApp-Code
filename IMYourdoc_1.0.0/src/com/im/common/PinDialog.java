
package com.im.common;

import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.text.JTextComponent;

import org.json.JSONObject;

import com.im.json.LogoutJSON;
import com.im.login.Login;
import com.im.patientscreens.UserWelcomeScreen;
import com.im.utils.Constants;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;
import com.im.utils.WelcomeUtils;
import com.im.utils.XmppUtils;

public class PinDialog extends JDialog {
	JFrame parent;
	JLabel labelSession;
	JDialog thisDialog;
	JLabel labelSessionTimeOut;
	boolean isOpeningAccountSetting;
	public PinDialog(JFrame parent,boolean isOpeningAccountSetting) {
		super(parent, "Pin Popup");
		this.parent = parent;
		thisDialog = this;
		this.isOpeningAccountSetting = isOpeningAccountSetting;
        setLayout(new BorderLayout());
        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
            	getContentPane().setBackground(Color.black);
            }
        });
        setLayout(new GridBagLayout());
		setUndecorated(true);
		int x = 0;
		int y = 0;
		//setBounds(x,y,parent.getWidth()/2,parent.getHeight()/2);
		setMinimumSize(new Dimension(parent.getWidth(),parent.getHeight()));
		setLocationRelativeTo(parent);
		parent.setEnabled(false);
		addTransparency();
		showComponents();
		Constants.childWindowOpened =  this;
		toFront();
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAutoRequestFocus(true);
	}
	private void showComponents(){
		BufferedImage master = null;
		JPanel hboxBottomBox = new JPanel(new GridLayout());
		Box vbBox = Box.createVerticalBox();
		if(isOpeningAccountSetting){
			labelSessionTimeOut = new JLabel("<html><center>Pin required</center></html>");
		}else{
			labelSessionTimeOut = new JLabel("<ht"
					+ "ml><center>Session Timed Out</center></html>");
		}
		labelSessionTimeOut.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 20));
		JPanel panelSessionOut = new JPanel();
		panelSessionOut.setOpaque(true);
		panelSessionOut.setBackground(null);
		panelSessionOut.add(labelSessionTimeOut);
		labelSessionTimeOut.setForeground(Color.decode("#9CCD21"));
		final JTextField textPin = new JPasswordField(5);
		textPin.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 20));
		JPanel textPanel = new JPanel(new GridLayout());
		textPin.setBorder(BorderFactory.createEmptyBorder());
		textPanel.add(textPin,BorderLayout.CENTER);
		textPanel.setBackground(null);
		textPanel.setOpaque(true);
		textPanel.setPreferredSize(new Dimension(50,50));
		textPanel.setMaximumSize(new Dimension(200,100));
		textPanel.setBorder(new TextBubbleBorder(Color.gray,2,5,0));
		
		if(isOpeningAccountSetting){
			labelSession = new JLabel("<html><center>Please enter your PIN to continue.</center></html>");
		}
		else{
			labelSession = new JLabel("<html><center>Your Application has timed out.<br/> Please enter your PIN to continue.</center></html>");
		}
		labelSession.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 20));
		JPanel panelSession = new JPanel();
		panelSession.setOpaque(true);
		panelSession.setBackground(null);
		panelSession.add(labelSession);
		labelSession.setForeground(Color.decode("#9CCD21"));
		

		JButton logout = new JButton("LOGOUT");
		logout.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 20));
		JButton ok = new JButton("OK");
		ok.registerKeyboardAction(ok.getActionForKeyStroke(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_FOCUSED);
		getRootPane().setDefaultButton(ok);
		ok.setFocusable(true);
		try {
			 master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		Image scaled1 = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
		Image scaled2 = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
		ok.setIcon(new ImageIcon(scaled1));
		logout.setIcon(new ImageIcon(scaled2));
		
		Util.setTransparentBtn(ok);
		Util.setTransparentBtn(logout);
		ok.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		logout.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		ok.setFont(new Font(Font.decode("CentraleSansRndBold")
				.getFontName(), Font.PLAIN, 20));
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int status = JOptionPane.showConfirmDialog(null, "<html><center>All conversations will be removed from your inbox at log out.<br/> "
						+ "Are you sure you want to continue?</center></html>","HIPAA Warning!",	JOptionPane.YES_NO_OPTION);
				if(status == JOptionPane.YES_OPTION){
					boolean statusLogout = Util.logout();
					if(statusLogout == true){
						Util.disposeAll(thisDialog,Constants.mainFrame);
					}
				}
				
			}
		});
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(textPin.getText().trim().equals("")){
					JOptionPane.showMessageDialog(thisDialog, "Pin is required.");
				}
				else{
					boolean checkPin = checkPin(textPin);
					if(checkPin == true){
						if(textPin.getText().equals(Constants.loggedinuserInfo.user_pin)){
							thisDialog.dispose();
							try {
								Constants.loader.setVisible(true);
								parent.setEnabled(false);
								if(isOpeningAccountSetting){
									SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
										@Override
										protected Void doInBackground() throws Exception {
											if(isOpeningAccountSetting){
												AccountSettingsDialog accountsDialog = new AccountSettingsDialog(parent);
												accountsDialog.initAccountSettingsBox();
												//Constants.loader.setVisible(false);
												accountsDialog.setVisible(true);
												parent.toFront();
												accountsDialog.toFront();
											}
											return null;
										}
									};
									worker.execute();
								}
								else{
									parent.setExtendedState(JFrame.MAXIMIZED_BOTH);
									parent.setEnabled(true);
									parent.toFront();
									Constants.loader.setVisible(false);
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Pin is not correct");
						}
					}
				}
			}
		});
//		Util.setTransparentBtn(ok);
//		Util.setTransparentBtn(logout);
//		thisDialog.addWindowFocusListener(new WindowFocusListener() {
//			
//			@Override
//			public void windowLostFocus(WindowEvent arg0) {
//				// TODO Auto-generated method stub
//				thisDialog.dispose();
//				parent.setEnabled(true);
//			}
//			
//			@Override
//			public void windowGainedFocus(WindowEvent arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		ok.setForeground(Color.white.brighter());
		logout.setForeground(Color.white.brighter());
		logout.setBackground(null);
		logout.setOpaque(true);
		ok.setBackground(null);
		ok.setOpaque(true);
		logout.setBorderPainted(false);
		ok.setBorderPainted(false);
		hboxBottomBox.add(logout);
		hboxBottomBox.setOpaque(true);
		hboxBottomBox.setBackground(Color.white);
		hboxBottomBox.add(ok);
		vbBox.add(Box.createVerticalStrut(5));
		JPanel bottom = new JPanel();
		bottom.setBackground(Color.white);
		bottom.setOpaque(true);
		bottom.add(hboxBottomBox);
		vbBox.add(panelSessionOut);
		vbBox.add(panelSession);
		vbBox.add(Box.createVerticalStrut(5));
		vbBox.add(textPanel);
		vbBox.add(Box.createVerticalStrut(20));
		vbBox.add(bottom,BorderLayout.SOUTH);
		//ok.setPreferredSize(new Dimension(100,100));
		//ok.setMaximumSize(new Dimension(200,50));
//		logout.setMaximumSize(new Dimension(200,50));
		
		//hboxBottomBox.setPreferredSize(new Dimension((int)(Constants.SCREEN_SIZE.getWidth() * 0.10), (int) (Constants.SCREEN_SIZE.getHeight() * 0.10)));
//		hboxBottomBox.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.lightGray));
		JPanel panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension((int) (parent.getWidth()*0.30),(int) (parent.getHeight()*0.30)));
		panel.add(vbBox,BorderLayout.CENTER);
		panel.setBackground(Color.white);
		panel.setOpaque(true);
		panel.setBorder(new TextBubbleBorder(Color.gray,2,2,0));
		add(panel);
	}
	private  void addTransparency() {
		
		 GraphicsEnvironment ge = 
		            GraphicsEnvironment.getLocalGraphicsEnvironment();
		        GraphicsDevice gd = ge.getDefaultScreenDevice();
		        if (!gd.isWindowTranslucencySupported(TRANSLUCENT)) {
		            System.err.println(
		                "Translucency is not supported");
		        }
		        SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		            	//LoaderWindow tw = new LoaderWindow();
		                // Set the window to 55% opaque (45% translucent).
		                setOpacity(0.90f);
		                // Display the window.
		            }
		        });
	}
	private boolean checkPin(JTextField textField){
	    String fieldValue = textField.getText();
	    boolean isInteger = false;
	    boolean status=false;
	// TODO Auto-generated method stub
	    if(!fieldValue.equals("")){
		    try{
		    	int nn = Integer.parseInt(fieldValue);
		    	if(Constants.showConsole) System.out.println(nn);
		    	isInteger = true;
		    	
		    }
		    catch (Exception e){
		    	textField.requestFocus();
		    	textField.setText("");
		    	JOptionPane.showMessageDialog(textField.getRootPane(),"Pin should be of 4 digits only");
		    	isInteger = false;
		    }
	   }
	    if(isInteger== true){
	    	if (fieldValue.trim().length()!= 4 ){
	    		JOptionPane.showMessageDialog(textField.getRootPane(),"Pin should be of 4 digits only");
	    		textField.requestFocus();
	    		status = false;
	    	}
	    	else {
	    		status = true;
	    	}
	    }
	    return status;
	}
	
}

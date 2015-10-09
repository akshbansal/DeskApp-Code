package com.im.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.accessibility.Accessible;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import org.json.JSONException;
import org.json.JSONObject;

import com.im.common.Footer;
import com.im.common.TopPanel;
import com.im.json.AuthenticateJSON;
import com.im.json.TermsAndConditionsFeedBack;
import com.im.utils.Constants;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;

public class FeedBack extends JDialog {
	/**
	 * 
	 */
	private Box box;
	private static final long serialVersionUID = 1L;
	private JLabel labelContent;
	private JLabel labelWebsite;
	private JLabel labelPhone;
	private JLabel feedBackLabel;
	private JLabel termsAndConditionLabel;
	private JTextArea area;
	private JButton submitButton;
	private JPanel submitPanel;
	private JPanel contentPanel;
	private JPanel websitePanel;
	private JPanel phonePanel;
	private JPanel feedBackPanel;
	private JPanel termsPanel;
	Point point = new Point();
	boolean resizing = false;
	private JPanel textAreaPanel;
	private Box vboxText;
	Frame parent;
	Box hbox ;
	JDialog thisDialog;
	public FeedBack(Frame parent) {
		// TODO Auto-generated constructor stub
		super(parent,"Feedback");
		try {
			thisDialog = this;
			Constants.IS_DIALOG = true;
			this.parent = parent ;
			parent.setEnabled(false);
			Constants.childWindowOpened =  this;
			setUndecorated(true);
			Constants.PARENT = parent;
			setModal(true);
	        setModalityType(ModalityType.APPLICATION_MODAL);
			initFeedBack();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initFeedBack() throws IOException{
		box = Box.createVerticalBox();
		hbox = Box.createHorizontalBox();
		area = new JTextArea(5,60);
		labelContent = new JLabel("<html><p>If this is a heath related emergency please dial <b>911.</b></p><br/><br/>"
				+ "<p>For urgent technical help please visit our website.</p>"
				+ "</html>");
		
		labelContent.setForeground(Color.darkGray);
		labelContent.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		
		labelWebsite = new JLabel("<html>Website:"
				+ "<br/>"
				+ "<font style='Color:#9CCD21'>www.imyourdoc.com</html>");
		labelWebsite.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		
		labelPhone = new JLabel("<html>Call us now:"
				+ "<br/>"
				+ "<font style='Color:#9CCD21'>(800) 4098078</html>");
		labelPhone.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		
		feedBackLabel = new JLabel("Please let us know what you think:");
		feedBackLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		
		
		termsAndConditionLabel = new JLabel("Terms And Conditions");
		termsAndConditionLabel.setForeground(Color.decode("#9CCD21"));
		termsAndConditionLabel.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		box.add(Box.createVerticalStrut(10));
		contentPanel = new JPanel();
		contentPanel.add(labelContent);
		contentPanel.setBackground(Color.white);
		contentPanel.setOpaque(true);
		box.add(contentPanel);
		box.add(Box.createVerticalStrut(20));
		
		websitePanel = new JPanel();
		websitePanel.add(labelWebsite);
		websitePanel.setBackground(Color.white);
		websitePanel.setOpaque(true);
		hbox.add(websitePanel,BorderLayout.WEST);
		hbox.setBackground(Color.white);
		hbox.setOpaque(true);
		
		phonePanel = new  JPanel();
		phonePanel.add(labelPhone);
		phonePanel.setBackground(Color.white);
		phonePanel.setOpaque(true);
		hbox.add(phonePanel,BorderLayout.EAST);
		
		box.add(hbox);
		box.add(Box.createVerticalStrut(20));
		feedBackPanel = new JPanel();
		feedBackPanel.add(feedBackLabel);
		feedBackPanel.setBackground(Color.white);
		feedBackPanel.setOpaque(true);
		
		termsPanel = new JPanel();
		termsPanel.add(termsAndConditionLabel);
		termsPanel.setBackground(Color.white);
		termsPanel.setOpaque(true);
		
		
		textAreaPanel = new JPanel();
		textAreaPanel.add(area);
		area.setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 2, 5, 0));
		textAreaPanel.setBackground(Color.white);
		textAreaPanel.setOpaque(true);
		vboxText = Box.createVerticalBox();
		vboxText.add(feedBackPanel,BorderLayout.WEST);
		vboxText.add(textAreaPanel);
		vboxText.setBackground(Color.white);
		vboxText.setOpaque(true);
		box.add(vboxText);
		box.add(Box.createVerticalStrut(20));
		submitButton = new JButton("Submit Feedback");
		

		BufferedImage master;
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
			Image scaled = master.getScaledInstance(200, 50, java.awt.Image.SCALE_SMOOTH);
			submitButton.setIcon(new ImageIcon(scaled));
			Util.setTransparentBtn(submitButton);
			// final JButton reject = new JButton("Reject");
			// reject.setOpaque(true);
			// reject.setBorderPainted(false);
			// reject.setFocusPainted(false);
			// reject.setBackground(Color.decode("#9CCD21"));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		submitPanel = new JPanel();
		submitPanel.add(submitButton);
		submitButton.setOpaque(true);
		submitButton.addActionListener(new SubmitFeedBack());
		submitPanel.setOpaque(true);
		submitButton.setFont(new Font(Font.decode("CentraleSansRndMedium")
				.getFontName(), Font.PLAIN, 18));
		submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		//loginButton.setPreferredSize(new Dimension(600, 30));
		submitButton.setHorizontalTextPosition(SwingConstants.CENTER);
		submitButton.setBackground(null);
		submitButton.setForeground(Color.white.brighter());
		submitPanel.setBackground(null);
		termsAndConditionLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		termsAndConditionLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

					@Override
					protected Void doInBackground() throws Exception {
						TermsAndConditionsFeedBack terms = new TermsAndConditionsFeedBack((JFrame) parent);
						terms.setVisible(true);
						thisDialog.dispose();
						return null;
					}
					
				};
				worker.execute();
			}
		});
		box.add(submitPanel);
		box.add(termsPanel);
		box.setBackground(Color.white);
		box.setOpaque(true);
		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.add(box);
		panel.setBackground(Color.white.brighter());
		getContentPane().setBackground(Color.white.brighter());
		add(panel, BorderLayout.CENTER);
		getRootPane().setBorder(new TextBubbleBorder(Color.LIGHT_GRAY, 2, 2, 0));
		add(TopPanel.topButtonPanelForAccountDialog(this,"Feedback"),BorderLayout.NORTH);
		setTitle("Feedback");
		setIconImage(new ImageIcon(getClass().getResource("/images/logoicon.png")).getImage());
		  int x = (Constants.SCREEN_SIZE.width)/8;
			 int y = (Constants.SCREEN_SIZE.height)/8;
			  setBounds(x,y,Constants.SCREEN_SIZE.width/2,Constants.SCREEN_SIZE.height/2);
		    setMinimumSize(new Dimension((int) (Math.round(Constants.SCREEN_SIZE.width * 0.50)), (int) (Math.round(Constants.SCREEN_SIZE.height * 0.70))));
		setLocationRelativeTo(getParent());
		toFront();
		//setModalityType(ModalityType.TOOLKIT_MODAL);
		parent.toFront();
		getContentPane().addMouseListener(new MouseAdapter() {
		      public void mousePressed(MouseEvent e) {
		        resizing = getCursor().equals(Cursor.getDefaultCursor())? false:true;
		        if(!e.isMetaDown()){
		          point.x = e.getX();
		          point.y = e.getY();
		        }
		      }
		    });
		getContentPane().addMouseMotionListener(new MouseMotionAdapter() {
		      public void mouseDragged(MouseEvent e) {
		        if(resizing){
		          Point pt = e.getPoint();
		          setSize(getWidth()+pt.x - point.x,getHeight());
		          point.x = pt.x;
		        }
		        else if(!e.isMetaDown()){
		        Point p = getLocation();
		        setLocation(p.x + e.getX() - point.x,
		            p.y + e.getY() - point.y);
		        }
		      }
		    });
	}
	private class SubmitFeedBack implements ActionListener{
		public SubmitFeedBack() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			AuthenticateJSON authenticate = new  AuthenticateJSON();
			System.out.println(area.getText());
			if(area.getText().trim().equals("")){
				JOptionPane.showMessageDialog(thisDialog, "<html></center>We didn't see any feedback!<br/>Please let us know what you think!</center></html>");
			}
			else{
				String response = authenticate.submitFeedBack(area.getText());
				try {
					JSONObject jsonObj = new JSONObject(response);
					String err_code = jsonObj.getString("err-code");
					if(err_code.equals("1")){
						JOptionPane.showMessageDialog(getParent(), "Your feedback posted successfully.");
						area.setText("");
					}
					else if(err_code.equals("600")){
						JOptionPane.showMessageDialog(getParent(), "Your login session expired.Please login again.");
					}
					else if(err_code.equals("300")){
						JOptionPane.showMessageDialog(getParent(), "Unable to proceed.Please try again later.");
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
}

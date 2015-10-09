package com.im.json;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;

import org.json.JSONObject;

import com.im.bo.PatientRegisterBO;
import com.im.bo.PhysicianRegisterBO;
import com.im.common.LightScrollPane;
import com.im.common.TopPanel;
import com.im.login.Login;
import com.im.utils.Constants;
import com.im.utils.FindMACAddress;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;

// The Simple Web Browser.
public class TermsAndConditionsPhysician extends JFrame

        implements HyperlinkListener {
    // These are the buttons for iterating through the page list.

    // Page location text field.

    // Editor pane for displaying pages.
    private JEditorPane displayEditorPane;
    Point point = new Point();
	boolean resizing = false;
    // Browser's list of pages that have been visited.
    private ArrayList pageList = new ArrayList();
    private String usertype="";
    // Constructor for Mini Web Browser.
    private JFrame thisDialog;
    public TermsAndConditionsPhysician(String usertype,final PhysicianRegisterBO bo, final JFrame parent, final JFrame thisParent) {
    	      super("Terms and conditions");
    	      this.usertype = usertype;
    	      thisDialog = this;
        // Set window size.
        setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60),(int) (Constants.SCREEN_SIZE.getHeight()*0.60)));
        setLocationRelativeTo(thisParent);
        thisParent.setEnabled(false);
        // Handle closing events.
        addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				 actionGo();
			}
			
			
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				  actionExit();
			}
			
			
        });


        // Set up page display.
        displayEditorPane = new JEditorPane();
        displayEditorPane.setContentType("text/html");
        displayEditorPane.setEditable(false);
        displayEditorPane.addHyperlinkListener(this);
        displayEditorPane.setEnabled(false);
        displayEditorPane.setBackground(Color.white);
        displayEditorPane.setDisabledTextColor(Color.gray.darker());
        getContentPane().setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 16));
        JPanel entreePanel = new JPanel();
        final ButtonGroup entreeGroup = new ButtonGroup();
        JLabel label = new JLabel("I have READ and AGREED to the terms and conditions of the service contract.");
        JRadioButton radioButton;
        entreePanel.add(radioButton = new JRadioButton("I Agree"));
        radioButton.setActionCommand("Agree");
        radioButton.setOpaque(true);
        radioButton.setBackground(null);
        entreeGroup.add(radioButton);
        entreePanel.add(radioButton = new JRadioButton("I Disagree"));
        radioButton.setActionCommand("Disagree");
        entreeGroup.add(radioButton);
        entreePanel.setOpaque(true);
        radioButton.setOpaque(true);
        radioButton.setBackground(null);
        entreePanel.setBackground(Color.white);
        Box vbox = Box.createVerticalBox();
        BufferedImage master = null;
		try {
			master = ImageIO.read(getClass().getResource(Constants.IMAGE_PATH+"/"+"submit_btn.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Image scaled = master.getScaledInstance((int) (Constants.SCREEN_SIZE.getWidth() * 0.30), 50, java.awt.Image.SCALE_SMOOTH);
		JButton submit = new JButton("Submit");
		submit.setForeground(Color.white);
		submit.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
		submit.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent ae) {
		        
		        if(entreeGroup.getSelection()!=null){
		        	String entree = entreeGroup.getSelection().getActionCommand();
			        if(entree.equalsIgnoreCase("Agree")){
			        	Constants.loader.setVisible(true);
			        	SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
							@Override
							protected Void doInBackground() throws Exception {
								JSONObject jsonResponse;
								PhysicianRegistrationJSON physicianJSON = new PhysicianRegistrationJSON(bo);
								String returnMessage = physicianJSON.doPatientRegistration();
								try {
									jsonResponse = new JSONObject(returnMessage);
									if (jsonResponse.getString("err-code").equals("700")) {
										JOptionPane.showMessageDialog(getRootPane(), jsonResponse.getString("message"));
									} else if (jsonResponse.getString("err-code").equals("1")) {
										JOptionPane.showMessageDialog(getRootPane(), "Awesome! Your account has successfully registered.");
										AuthenticateJSON login = new AuthenticateJSON();
										FindMACAddress macAddress = new FindMACAddress();
										StringBuffer username = new StringBuffer();
										Constants.loggedinuserInfo.name =  bo.getFirst_name() + " "+bo.getLast_name();
										Constants.loggedinuserInfo.username = bo.getUsername();
										Constants.loggedinuserInfo.password = bo.getPassword();
										Constants.loggedinuserInfo.device_id = macAddress.DeviceIDOfSystem();
										Constants.loggedinuserInfo.device_type = "web";
										String response = login.doAuthentication();
										String err_code = "";
										if (response != null) {
											jsonResponse = new JSONObject(response);
											err_code = jsonResponse.getString("err-code");
										}
										
										if (!bo.getUsername().endsWith("@imyourdoc.com"))
											username.append(bo.getUsername() + "@imyourdoc.com");
										else
											username.append(bo.getUsername());
										if (err_code.equals("1")) {
											//					 	XmppUtils.doLogin(username_xmpp, password_xmpp, null);
											Constants.loggedinuserInfo.user_type = jsonResponse.getString("user_type");
											Constants.loggedinuserInfo.user_pin = jsonResponse.getString("user_pin");
											Constants.loggedinuserInfo.login_token = jsonResponse.getString("login_token");
											Constants.loggedinuserInfo.seq_ques = jsonResponse.getString("seq_question");
											Constants.loggedinuserInfo.seq_ans = jsonResponse.getString("seq_answer");
											Constants.loggedinuserInfo.isProvider = true;
											Login.doLogin(username.toString(), bo.getPassword());
											parent.dispose();
											thisParent.dispose();
											thisDialog.dispose();
											Constants.loader.setVisible(false);
										}
										
									} else if (jsonResponse.getString("err-code").equals("404")) {
										Constants.loader.setVisible(false);
										JOptionPane.showMessageDialog(getRootPane(), "Practice Type or Job Title or Designation Not Found. ");
										thisParent.setEnabled(true);
										thisDialog.dispose();
										
									}
								} catch (Exception ex) {
									System.out.println(ex.getMessage());
								}
								return null;
							}
			        		
			        	};
			        	worker.execute();
			        }
			        else if(entree.equalsIgnoreCase("Disagree")){
			        	JOptionPane.showMessageDialog(getRootPane(), "<html><center>You do not agree to these terms and conditions,"
			        			+ "<br/> you will not be able to register yourself!</center></html>");
			        	thisDialog.dispose();
			        	thisParent.setEnabled(true);
			        }
		        }
		        else{
		        	JOptionPane.showMessageDialog(getRootPane(), "Select atleast one option.");
		        }
		      }
		    });
		submit.setIcon(new ImageIcon(scaled));
		Util.setTransparentBtn(submit);
		submit.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
		JPanel panelbutton = new JPanel();
		panelbutton.add(submit,BorderLayout.CENTER);
		vbox.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.gray));
		vbox.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.50),(int) (Constants.SCREEN_SIZE.getHeight()*0.20)));
		vbox.add(Box.createVerticalStrut(20));
		vbox.add(label,BorderLayout.CENTER);
		vbox.add(entreePanel,BorderLayout.CENTER);
		vbox.add(submit,BorderLayout.CENTER);
        getContentPane().setLayout(new BorderLayout());
       // getContentPane().add(buttonPanel, BorderLayout.NORTH);
        JLabel labeltitle = new JLabel("Service Contract Agreement");
        labeltitle.setForeground(Color.decode("#9CCD21"));
        JPanel panelTitle = new JPanel();
        panelTitle.add(labeltitle,BorderLayout.CENTER);
        setUndecorated(true);
        getRootPane().setBorder(new TextBubbleBorder(Color.gray,2,2,0));
        getContentPane().add(TopPanel.topButtonPanelForAccountDialog(this, labeltitle.getText()),BorderLayout.NORTH);
//        getContentPane().add(labeltitle);
        add(new LightScrollPane(displayEditorPane),
                BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.white);
        bottomPanel.setOpaque(true);
        bottomPanel.add(vbox);
        getContentPane().add(bottomPanel,BorderLayout.SOUTH);
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

    // Exit this program.
    private void actionExit() {
        System.exit(0);
    }

    // Go back to the page viewed before the current page.
   
    // Load and show the page specified in the location text field.
    private void actionGo() {
    	URL verifiedUrl = null;
    	if(usertype.equalsIgnoreCase("patient")){
    		 verifiedUrl = verifyUrl("https://api.imyourdoc.com/SERVICES_AGREEMENT.php");
    	}
    	else{
    		verifiedUrl = verifyUrl("https://api.imyourdoc.com/HIPAA_BUSINESS_ASSOCIATE_ADDENDUM.php");
    	}
       
        if (verifiedUrl != null) {
            showPage(verifiedUrl, true);
        } else {
            showError("Invalid URL");
        }
    }

    // Show dialog box with error message.
    private void showError(String errorMessage) {
//        JOptionPane.showMessageDialog(this, errorMessage,
//                "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Verify URL format.
    private URL verifyUrl(String url) {
        // Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("https://"))
            return null;

        // Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }

        return verifiedUrl;
    }

  /* Show the specified page and add it to
     the page list if specified. */
    private void showPage(URL pageUrl, boolean addToList) {
        // Show hour glass cursor while crawling is under way.
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        try {
            // Get URL of page currently being displayed.
            URL currentUrl = displayEditorPane.getPage();

            // Load and display specified page.
            displayEditorPane.setPage(pageUrl);

            // Get URL of new page being displayed.
            URL newUrl = displayEditorPane.getPage();

            // Add page to list if specified.
            if (addToList) {
                int listSize = pageList.size();
                if (listSize > 0) {
                    int pageIndex =
                            pageList.indexOf(currentUrl.toString());
                    if (pageIndex < listSize - 1) {
                        for (int i = listSize - 1; i > pageIndex; i--) {
                            pageList.remove(i);
                        }
                    }
                }
                pageList.add(newUrl.toString());
            }

            // Update location text field with URL of current page.

            // Update buttons based on the page being displayed.
        } catch (Exception e) {
            // Show error messsage.
            showError("Unable to load page");
        } finally {
            // Return to default cursor.
            setCursor(Cursor.getDefaultCursor());
        }
    }

  /* Update back and forward buttons based on
     the page being displayed. */

    // Handle hyperlink's being clicked.
    public void hyperlinkUpdate(HyperlinkEvent event) {
        HyperlinkEvent.EventType eventType = event.getEventType();
        if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
            if (event instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent linkEvent =
                        (HTMLFrameHyperlinkEvent) event;
                HTMLDocument document =
                        (HTMLDocument) displayEditorPane.getDocument();
                document.processHTMLFrameHyperlinkEvent(linkEvent);
            } else {
                showPage(event.getURL(), true);
            }
        }
    }

    // Run the Mini Browser.
//    public static void main(String[] args) {
//        MiniBrowser browser = new MiniBrowser("staff");
//        browser.setVisible(true);;
//    }
}
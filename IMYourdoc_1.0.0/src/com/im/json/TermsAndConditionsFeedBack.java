package com.im.json;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.html.*;

import org.json.JSONObject;

import com.im.bo.PatientRegisterBO;
import com.im.common.LightScrollPane;
import com.im.common.TopPanel;
import com.im.login.Login;
import com.im.utils.Constants;
import com.im.utils.FindMACAddress;
import com.im.utils.TextBubbleBorder;
import com.im.utils.Util;

// The Simple Web Browser.
public class TermsAndConditionsFeedBack extends JFrame

        implements HyperlinkListener {
    // These are the buttons for iterating through the page list.

    // Page location text field.

    // Editor pane for displaying pages.
    private JEditorPane displayEditorPane;
    Point point = new Point();
	boolean resizing = false;
    // Browser's list of pages that have been visited.
    private ArrayList pageList = new ArrayList();
    // Constructor for Mini Web Browser.
    private JFrame thisDialog;
    public TermsAndConditionsFeedBack(final JFrame parent) {
    	      super("Terms and conditions");
    	      thisDialog = this;
        // Set window size.
        setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth()*0.60),(int) (Constants.SCREEN_SIZE.getHeight()*0.60)));
        setLocationRelativeTo(parent);
        parent.setEnabled(false);
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
        
        Box vbox = Box.createVerticalBox();
        
		
        getContentPane().setLayout(new BorderLayout());
       // getContentPane().add(buttonPanel, BorderLayout.NORTH);
        JLabel labeltitle = new JLabel("Terms and Conditions");
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
    	URL verifiedUrl  = verifyUrl("https://api.imyourdoc.com/SERVICES_AGREEMENT.php");
    	if(Constants.loggedinuserInfo.user_type.equalsIgnoreCase("patient")){
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
    private void showPage(final URL pageUrl, final boolean addToList) {
        // Show hour glass cursor while crawling is under way.
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
			@Override
			protected Void doInBackground() throws Exception {
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
				return null;
			}
        	
        };
        worker.execute();
       
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
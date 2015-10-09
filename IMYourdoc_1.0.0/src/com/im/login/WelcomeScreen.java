package com.im.login;

// SplashScreen.java
// A simple application to show a title screen in the center of the screen
// for the amount of time given in the constructor.  This class includes
// a sample main() method to test the splash screen, but it's meant for use
// with other applications.
//

import it.sauronsoftware.junique.AlreadyLockedException;
import it.sauronsoftware.junique.JUnique;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;

import org.json.JSONException;
import org.json.JSONObject;

import com.im.db.DBServiceUpdate;
import com.im.json.AppCheckVersionJSON;
import com.im.utils.Constants;
import com.im.utils.HospitalRelatedList;

public class WelcomeScreen extends JWindow {
  private int duration;
  private static JProgressBar pbar;
  public WelcomeScreen(int d) {
    duration = d;
    
  }

  // A simple little method to show a title screen in the center
  // of the screen for the amount of time given in the constructor
  public void showSplash() {
    JPanel content = (JPanel)getContentPane();
    content.setBackground(Color.white);

    // Set the window's bounds, centering the window
    int x = (Constants.SCREEN_SIZE.width)/10;
	 int y = (Constants.SCREEN_SIZE.height)/20;
	  setBounds(x,y,Constants.SCREEN_SIZE.width/2,Constants.SCREEN_SIZE.height/2);
    setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.width * 0.70), (int) (Constants.SCREEN_SIZE.height * 0.80)));
    // Build the splash screen
    JLabel label = new JLabel(new ImageIcon(getClass().getResource("/images/logo.png")));
    JLabel copyrt = new JLabel
      ("<html><font style='color:#9CCD21;size:small;weight:bold;'><center>Beta</center></font><br/>© 2015. All Rights Reserved IM Your Doc, Inc.</html>", JLabel.CENTER);
    copyrt.setFont(new Font(Font.decode("CentraleSansRndBold")
			.getFontName(), Font.PLAIN, 24));
    copyrt.setPreferredSize(new Dimension(Constants.SCREEN_SIZE.width,50));
    Box vbox = Box.createVerticalBox();
    vbox.setOpaque(true);
    JPanel labelPanel = new JPanel();
    labelPanel.add(label);
    labelPanel.setBackground(null);
    labelPanel.setOpaque(true);
    vbox.add(Box.createVerticalStrut((int)(Constants.SCREEN_SIZE.getHeight()*0.01)));
    vbox.setBackground(Color.white);
    vbox.add(labelPanel,BorderLayout.CENTER);
    content.add(vbox,BorderLayout.NORTH);
//    content.add(label, BorderLayout.NORTH);
    pbar = new JProgressBar();
    pbar.setMinimum(0);
    pbar.setMaximum(100);
    pbar.setFont(new Font(Font.decode("CentraleSansRndBold")
			.getFontName(), Font.PLAIN, 20));
    pbar.setString("Loading... please wait");;
    pbar.setStringPainted(true);
    pbar.setForeground(Color.decode("#9CCD21"));
    content.add(copyrt, BorderLayout.CENTER);
    content.add(pbar, BorderLayout.SOUTH);
    pbar.setPreferredSize(new Dimension(600, 30));
    pbar.setBounds(0, 290, 404, 20);
    Color oraRed = new Color(156, 20, 20,  255);
    content.setBorder(BorderFactory.createLineBorder(oraRed, 10));
    // Display it
    setVisible(true);
    Thread t = new Thread() {
    	 
        public void run() {
            int i = 0;
            while (i <= 200) {
                pbar.setValue(i);
                try {
                	sleep(1000);
                	SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){

						@Override
						protected Void doInBackground() throws Exception {
							checkAppVersionInBackground();
							return null;
						}
						
					};
					if(i == 20){                	
						worker.execute();
						break;
					}
                } catch (InterruptedException ex) {
                System.out.println("Error: "+ex.getMessage());
                ex.printStackTrace();
                }
                i++;
            }
        }
    };
    t.start();
    // Wait a little while, maybe while loading resources
    try { Thread.sleep(duration); } catch (Exception e) {}

    setVisible(false);
  }

  public void showSplashAndExit() throws JSONException {
    showSplash();
   // System.exit(0);
    
  }
  private void checkAppVersionInBackground(){
		try {
			String response = new AppCheckVersionJSON().checkAppVersion("1.0");
			JSONObject obj = new JSONObject(response);
			String err_code = obj.getString("err-code");
			
				if(err_code.equals("800")){
					System.out.println(obj.getString("message"));
					int status = JOptionPane.showConfirmDialog(this, "<html><center>New version is here! <br/>You have to update to procede?</center></html>","Update App Version",JOptionPane.YES_NO_OPTION);
					if(status==JOptionPane.YES_OPTION){
						Desktop.getDesktop().browse(new URL(obj.getString("url")).toURI());
					}
					else{
						JOptionPane.showMessageDialog(this, "<html><center>You can not procede without updating the app,<br/>So update it now!</center></html>");
					}
				}
				else{
					 
					 try {
						
						Login login = new Login();
						login.initComponents();
						this.dispose();
					} catch (FontFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
//			else
//			{
//				JOptionPane.showMessageDialog(this, "Your app is already updated!");
//			}
		} catch (JSONException | IOException | URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			try {
				Login login = new Login();
				login.initComponents();
				this.dispose();
			} catch (FontFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
  public static void main(String[] args) {
//	  String id = WelcomeScreen.class.getName();
//		boolean start;
//		try {
//			JUnique.acquireLock(id, null);
//			start = true;
//		} catch (AlreadyLockedException e) {
//			// Application already running.
//			start = false;
//			
//		}
//		if (start) {
			
			 WelcomeScreen splash = new WelcomeScreen(100000);
			    try {
					splash.showSplashAndExit();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
//		}
//		else{
//			JOptionPane.showMessageDialog(null,"IM YourDoc App already running.");
//			for (int i = 0; i < args.length; i++) {
//				JUnique.sendMessage(id, args[i]);
//			}
//		}
  }
  public static void secondaryMain( String args[] ){
	  if(Constants.mainFrame!=null){
		  Constants.mainFrame.setVisible(true);
		  Constants.mainFrame.toFront();
	  }
  }
}
package com.im.login;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.im.db.DBServiceUpdate;
import com.im.utils.Constants;

/**
 *
 * @author megha bali
 *
 */

public class HideToSystemTray {
    private  TrayIcon trayIcon;
    private SystemTray tray;
    JFrame mainframe;
    
    public HideToSystemTray(JFrame frame){
       // super("SystemTray test");
        this.mainframe = frame;
        if(Constants.showConsole) System.out.println("creating instance");
        try{
            if(Constants.showConsole) System.out.println("setting look and feel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            if(Constants.showConsole) System.out.println("Unable to set LookAndFeel");
        }
        if(SystemTray.isSupported()){
            if(Constants.showConsole) System.out.println("system tray supported");
            tray=SystemTray.getSystemTray();

            Image image=Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo_welcome.png"));
            ActionListener exitListener=new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	System.exit(0);
                	Runtime.getRuntime().exit(0);
                	if(Constants.showConsole) System.out.println("Exiting....");
                    
                }
            };
            PopupMenu popup=new PopupMenu();
            MenuItem defaultItem=new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            defaultItem=new MenuItem("Open");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                	mainframe.setVisible(true);
                	mainframe.setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);
            String name = "IMYourDoc_"+Constants.loggedinuserInfo.username;
            trayIcon=new TrayIcon(image, name, popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					mainframe.setVisible(true);
					mainframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
			});
            
        }else{
            if(Constants.showConsole) System.out.println("system tray not supported");
        }
        mainframe.addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                if(e.getNewState()==JFrame.ICONIFIED ){
                    try {
                    	if(!Constants.IS_ADDED_TO_PANEL){
                    		tray.add(trayIcon);
                    		Constants.IS_ADDED_TO_PANEL = true;
                    		Constants.TRAY_ICON = trayIcon;
                    	}
                    	else{
                    		if(Constants.loggedinuserInfo.login_token==null ||Constants.isLogout){
                    			tray.remove(trayIcon);
                        		Constants.IS_ADDED_TO_PANEL = false;
                        		Constants.TRAY_ICON = null;
                    		}
                    	}
                       // mainframe.setVisible(false);
                        if(Constants.showConsole) System.out.println("added to SystemTray minimised");
                    } catch (Exception ex) {
                        if(Constants.showConsole) System.out.println("unable to add to tray");
                    }
                }
                else if(e.getNewState() == JFrame.DISPOSE_ON_CLOSE){
                	tray.remove(trayIcon);
            		Constants.IS_ADDED_TO_PANEL = false;
            		Constants.TRAY_ICON = null;
                }
//           else if(e.getNewState()==7){
//            try{
//            	if(!tray.getTrayIcons().equals(trayIcon)){
//            				tray.add(trayIcon);
//            				Constants.TRAY_ICON = trayIcon;
//            				Constants.IS_ADDED_TO_PANEL = true;
//            	}
//            //mainframe.setVisible(false);
//            if(Constants.showConsole) System.out.println("added to SystemTray2");
//            }catch(AWTException ex){
//            if(Constants.showConsole) System.out.println("unable to add to system tray");
//            }
//            }
//         	if(e.getNewState()==JFrame.MAXIMIZED_BOTH ||e.getNewState()==JFrame.NORMAL){
//                    tray.remove(trayIcon);
////            		if(!tray.getTrayIcons().equals(trayIcon))
////						try {
////							 tray.add(trayIcon);
////							 mainframe.setVisible(true);
////			                    if(Constants.showConsole) System.out.println("Tray icon removed");
////						} catch (AWTException e1) {
////							// TODO Auto-generated catch block
////							e1.printStackTrace();
////						}
//                   
//                }
            }
        });
        mainframe.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo_welcome.png")));
        mainframe.setVisible(true);
    }
//    public static void main(String[] args){
//        new HideToSystemTray();
//    }
}
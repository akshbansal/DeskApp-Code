package com.im.common;

import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Dialog.ModalityType;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.im.utils.Constants;

public class LoaderWindow extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public LoaderWindow(JFrame parent) {
		//super("TranslucentWindow");
        setLayout(new GridBagLayout());
      
        int x = (Constants.SCREEN_SIZE.width)/5;
		 int y = (Constants.SCREEN_SIZE.height)/10;
		setBounds(x,y,(int)(getWidth()/2),(int)(getHeight()/2));
		getContentPane().setBackground(Color.black);
		//setModalityType(ModalityType.TOOLKIT_MODAL);
		toFront();
		setUndecorated(true);
		if(Constants.PARENT != null){
			setMinimumSize(new Dimension((int) (parent.getWidth()), (int) (parent.getHeight())));
		}
		else
			setMinimumSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.80), (int) (Constants.SCREEN_SIZE.getHeight() * 0.80)));
		addTransparency();
		JPanel loader = new JPanel();
  		JLabel label = new JLabel(new ImageIcon(LoaderWindow.class.getResource("/images/loaderwhite1.gif")));
  		loader.add(label,BorderLayout.CENTER);
  		loader.setPreferredSize(new Dimension((int) (Constants.SCREEN_SIZE.getWidth() * 0.40), (int) (Constants.SCREEN_SIZE.getHeight() * 0.40)));
  		add(loader);
  		toFront();
  		setAutoRequestFocus(true);
  		loader.setOpaque(true);
  		loader.setBackground(null);
		setLocationRelativeTo(parent);
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
		                setOpacity(0.60f);
		                // Display the window.
		            }
		        });
	}
}

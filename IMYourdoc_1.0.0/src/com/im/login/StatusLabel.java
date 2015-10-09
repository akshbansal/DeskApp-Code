package com.im.login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class StatusLabel extends JLabel implements FTPConnectionListenable {

    private Integer status;
    // Constructor
    StatusLabel(String text) {
        super(text);
        setFont(new Font("Dialog", Font.PLAIN, 10));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        g.setColor(getColor());
        g.fillArc(0, this.getHeight()/4, 8, 8, 0, 360);
    }
    @Override
    public void setText(){
    	setText(setMyText());
    }
    
    @Override
    public void setStatus (final int status) {
        this.status = status;
        if (status !=0) 
        repaint(); 
    }
    private String setMyText(){
    	  switch (status) {
          case FTPConnectionListenable.STATUS_OK:
              return "ok";
          case FTPConnectionListenable.STATUS_WARNING:
              return "Warnng";
          case FTPConnectionListenable.STATUS_ERROR:
              return "Error";
          default: 
              return "final";
    	  }
    }
    private Color getColor () {
        switch (status) {
        case FTPConnectionListenable.STATUS_OK:
            return Color.GREEN;
        case FTPConnectionListenable.STATUS_WARNING:
            return Color.ORANGE;
        case FTPConnectionListenable.STATUS_ERROR:
            return Color.RED;
        default: 
            return Color.PINK;
        }
    }

    public static void main(String[] args) {
      final StatusLabel statusLabel ;
      statusLabel = new StatusLabel("Foo");
      statusLabel.setStatus(FTPConnectionListenable.STATUS_OK);
      new Timer(1000, new ActionListener() {
         int counter = 0;
         @Override
         public void actionPerformed(ActionEvent e) {
            counter++;
            counter %= 4;
            statusLabel.setStatus(counter);
         }
      }).start();

      JOptionPane.showMessageDialog(null, statusLabel);
   }
}

interface FTPConnectionListenable {

   static final int STATUS_ERROR = 0;
   static final int STATUS_WARNING = 1;
   static final int STATUS_OK = 2;
   void setStatus(int status);
   void setText();

}
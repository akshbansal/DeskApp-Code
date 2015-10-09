package com.im.login;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
  
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
  
public class SelfResize extends JDialog {
     
    public SelfResize() {
        setUndecorated( true );
        JPanel p = new JPanel();
        JButton close = new JButton( "Close" );
        close.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                System.exit( 0 );
            }
        } );
        p.add( close );
        getContentPane().add( p, BorderLayout.SOUTH );
        ResizeListener l = new ResizeListener();
        addMouseMotionListener( l );
        setBounds( 300, 300, 300, 300 );
        setVisible( true );
    }
     
    public static void main(String[] args) {
        new SelfResize();
    }
     
    public class ResizeListener extends MouseMotionAdapter {
         
        public void mouseMoved( MouseEvent e ) {
            if ( e.getX() < 10 || e.getX() > ( getWidth() - 10 ) ||
                    e.getY() < 10 || e.getY() > ( getHeight() - 10 ) ) {
                ((JPanel)getContentPane()).setBorder( BorderFactory.createEtchedBorder() );
            }
            else {
                ((JPanel)getContentPane()).setBorder( BorderFactory.createEmptyBorder() );
            }
        }
         
        public void mouseDragged( MouseEvent e ) {
            Rectangle bounds = getBounds();
            Point p = e.getPoint();
            if ( p.x < 10 ) {
                invalidate();
                SwingUtilities.convertPointToScreen( p, getContentPane() );
                int diff = bounds.x - p.x;
                bounds.x = p.x;
                bounds.width += diff;
                setBounds( bounds );
                validate();
            }
            else if ( e.getX() > ( getWidth() - 10 ) ) {
                invalidate();
                bounds.width = p.x;
                setBounds( bounds );
                validate();
            }
            else if ( e.getY() < 10 ) {
                invalidate();
                SwingUtilities.convertPointToScreen( p, getContentPane() );
                int diff = bounds.y - p.y;
                bounds.y = p.y;
                bounds.height += diff;
                setBounds( bounds );
                validate();
            }
            else if ( e.getY() > ( getHeight() - 10 ) ) {
                invalidate();
                bounds.height = p.y;
                setBounds( bounds );
                validate();
            }
        }
    }
}
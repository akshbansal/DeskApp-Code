package com.im.login;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class test {

	public static void main(String[] args) {
		JFrame fr = new JFrame();
		fr.setMinimumSize(new Dimension(400,400));
		
		String text = "snbfmmmmmmmmmmmmmmmmmmmmm mmmmmmmmmmm mmmmmmmmmmmmmm mmmmmmmmmmmmmmm mmmmmmmmmmm mmmmmmmmsx ";
		String labelText = String.format("<html><div style=\"width:%dpx;margin:5px;\">%s</div><br><html>", 100, text);
		try {
			JLabel label = new JLabel(labelText) {
					/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

					public Dimension getPreferredSize() {
					// The label should be of fixed width and the height should be computed
					// to exactly fit the text.
					Dimension preferredSize = super.getPreferredSize();
					return new Dimension(preferredSize.width, preferredSize.height);
					}
					};
			System.out.println(label.getPreferredSize());
			
			label.setHorizontalTextPosition(SwingConstants.HORIZONTAL);
			JPanel panel = new Mypanel();		
			JPanel panel2 = new JPanel();
			panel.add(label);
			panel2.add(panel);
			fr.add(panel2);
			fr.setVisible(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
 class Mypanel extends JPanel{
	 BufferedImage img;
	@Override
	public void paintComponent(Graphics g) {
		try{
			img = ImageIO.read(test.class.getResource("/images/BubbleRight.png"));
	        super.paintComponent(g);
	        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }
	}
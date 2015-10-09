package com.im.login;

import javax.swing.*;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.net.URL;

import javax.imageio.ImageIO;

import com.im.utils.Constants;
import com.im.utils.ImageUtils;
import com.im.utils.Util;

public class combine {
	private static boolean isTransparent;
    combine(BufferedImage bg, BufferedImage fg) {
        final BufferedImage scaled = new BufferedImage(
            bg.getWidth(),bg.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics g = scaled.getGraphics();
        g.drawImage(fg,0,0,scaled.getWidth(),scaled.getHeight(),null);
        g.dispose();

        final int xMax = bg.getWidth()-scaled.getWidth();
        final int yMax = bg.getHeight()-scaled.getHeight();

        final JLabel label = new JLabel(new ImageIcon(bg));

//        ActionListener listener = new ActionListener() {
//
//            Random random = new Random();
//
//            public void actionPerformed(ActionEvent ae) {
                Graphics g2 = bg.getGraphics();
                int x = xMax;
                int y = yMax;

                g2.drawImage( scaled,x, y, null );
              
                g2.dispose();
                label.repaint();
                JPanel panel = new JPanel(new BorderLayout());
                panel.setOpaque(true);
                panel.setBackground(Color.white);
                panel.add(label);
                JFrame f = new JFrame();
                f.setMinimumSize(new Dimension(500,500));
                f.add(panel);
                f.getContentPane().setBackground(Color.white);
                f.setVisible(true);
//            }
//        };
//
//        Timer timer = new Timer(1200, listener);
//        timer.start();

      //  JOptionPane.showMessageDialog(null, label);
    }

    public static void main(String[] args) throws Exception {
       // URL url1 = new URL("http://pscode.org/media/stromlo1.jpg");
        final BufferedImage image1 = ImageIO.read(combine.class.getResource("/images/transparent.png"));

       // URL url2 = new URL("http://pscode.org/media/stromlo2.jpg");
        final BufferedImage image2 = Util.getProfileImg("rickys");
        
        final BufferedImage img2 = ImageUtils.resizeImage(image2, ImageUtils.IMAGE_PNG,50,50);
        Graphics g = img2.createGraphics();
        g.drawImage(img2, 0, 0, null);
        g.dispose();
        System.out.println(image2.getColorModel().hasAlpha());
        //Create the frame on the event dispatching thread
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
            		new combine(img2, image1);
            }
        });
    }
}
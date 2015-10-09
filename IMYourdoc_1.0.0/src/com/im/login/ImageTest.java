package com.im.login;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.im.utils.Constants;
  
public class ImageTest
{
    public static void main(String[] args)
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new JScrollPane(new ImageTestPanel()));
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}
  
class ImageTestPanel extends JPanel
{
    public ImageTestPanel()
    {
        BufferedImage[] jpgs = loadImages();
        BufferedImage[] pngs = createNewImages(jpgs);
        makeLabels(pngs);
        setBackground(Color.white);
    }
  
    private void makeLabels(BufferedImage[] images)
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
  
        for(int i = 0; i < images.length; i++)
        {
            ImageLabel label = new ImageLabel(images[i], 0.2f);
            if(i > 0)
            {
                int dx = images[i - 1].getWidth()/2;
                gbc.insets = new Insets(0, -dx, 0, 0);
            }
            add(label, gbc);
        }
    }
  
    private BufferedImage[] createNewImages(BufferedImage[] opaque)
    {
        BufferedImage[] transparent = new BufferedImage[2];
  
        for(int i = 0; i < opaque.length; i++)
        {
            int w = opaque[i].getWidth();
            int h = opaque[i].getHeight();
            transparent[i] = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = transparent[i].createGraphics();
            g2.drawImage(opaque[i], null, 0, 0);
            g2.dispose();
        }
        return transparent;
    }
  
    private BufferedImage[] loadImages()
    {
        String[] fileNames = {
            "logo", "logo"
        };
        BufferedImage[] images = new BufferedImage[fileNames.length];
        for(int i = 0; i < images.length; i++)
            try
            {
                URL url = getClass().getResource("/images/" + fileNames[i] + ".png");
                images[i] = ImageIO.read(url);
            }
            catch(MalformedURLException mue)
            {
                if(Constants.showConsole) System.out.println("Bad URL: " + mue.getMessage());
            }
            catch(IOException ioe)
            {
                if(Constants.showConsole) System.out.println("Read trouble: " + ioe.getMessage());
            }
            return images;
    }
}
  
class ImageLabel extends JLabel
{
    BufferedImage image;
    float alpha;
  
    public ImageLabel(BufferedImage image, float alpha)
    {
        this.image = image;
        this.alpha = alpha;
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
    }
  
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(ac);
        g2.drawImage(image, null, 0, 0);
    }
}
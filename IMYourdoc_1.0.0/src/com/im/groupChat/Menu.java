package com.im.groupChat;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicMenuItemUI;

public class Menu {

    public JMenuBar createMenuBar() {
         JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("A Menu");
        menuBar.add(menu);

         GradientPaint p;
         p = new GradientPaint(0, 12, Color.white, 0, 24, Color.pink);
         UIManager.put("MenuItem.selectionBackground", p);
        menuItem = new JMenuItem("1st item: when selected its background is black: wrong");
        menu.add(menuItem);

         UIManager.put("MenuItem.selectionBackground", Color.pink);
        menuItem = new JMenuItem("2nd item: when selected its background is pink: ok");
        menu.add(menuItem);

//        UIManager.put("MenuItem.background", Color.red);
        menuItem = new CMenuItem("3rd item: when selected its background is pink: WRONG!");
        menu.add(menuItem);

        return menuBar;
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        Menu demo = new Menu();
        frame.setJMenuBar(demo.createMenuBar());

        //Display the window.
        frame.setSize(450, 260);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

class CMenuItem extends JMenuItem {
     private static final long serialVersionUID = 1L;

     public CMenuItem(String text) {
          super(text);
          setUI(BUI.createUI(this));
     }
 
     static final class BUI extends BasicMenuItemUI {
         private final static BUI ui = new BUI();
 
         public static BUI createUI(JComponent c) {
             return ui;
         }
 
         @Override
         public void update(Graphics g, JComponent c) {
               if (true || c.isOpaque()) {
//                    System.out.println("update()");
                    Graphics2D g2 = (Graphics2D)g.create();
                    int w = c.getWidth();
                    int h = c.getHeight();
                    GradientPaint p;
                    p = new GradientPaint(0, 0, Color.white, 0, 10, Color.orange);
                    g2.setPaint(p);
                    g2.fillRect(0, 0, w, h);
                    g2.dispose();
               }
               paint(g, c);
          }
     }
}
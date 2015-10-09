package com.im.common;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class MainClass {

  public static void main(final String args[]) {
    JFrame frame = new JFrame("Alignment Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new GridLayout(0, 1));
    JTextField textField = new JTextField("Left");
    textField.setHorizontalAlignment(JTextField.LEFT);
    frame.add(textField);
    textField = new JTextField("Center");
    textField.setHorizontalAlignment(JTextField.CENTER);
    frame.add(textField);
    textField = new JTextField("Right");
    textField.setHorizontalAlignment(JTextField.RIGHT);
    frame.add(textField);
    textField = new JTextField("Leading");
    textField.setHorizontalAlignment(JTextField.LEADING);
    frame.add(textField);
    textField = new JTextField("Trailing");
    textField.setHorizontalAlignment(JTextField.TRAILING);
    frame.add(textField);
    frame.pack();
    frame.setSize(250, (int) frame.getSize().getHeight());
    frame.setVisible(true);

  }
}

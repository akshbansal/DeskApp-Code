package com.im.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;

import com.im.utils.TextBubbleBorder;

public abstract class IMURDocTable {

	abstract protected String[] getColumns();

	abstract protected Object[][] getData();

	abstract protected JTable getTable();

	abstract protected JButton addNewBtn();

	abstract protected MouseListener getMouseListener();

	public JPanel getPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		Box vbox = Box.createVerticalBox();
		JTable table = getTable();
		table.setCellSelectionEnabled(true);
		table.setFillsViewportHeight(true);
		table.setRowHeight(30);
		table.setBackground(Color.white);
		table.setOpaque(true);
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(getMouseListener());
		table.setSelectionBackground(Color.white);
		JScrollPane sc = new JScrollPane(table);
		vbox.add(Box.createVerticalStrut(5));
		vbox.add(sc);
		vbox.add(Box.createVerticalStrut(5));
		sc.setOpaque(true);
		
		vbox.add(Box.createVerticalStrut(5));

		// JPanel mainPanel = new JPanel(new GridLayout(0, 1, 1, 1));
		// mainPanel.add(vbox, BorderLayout.CENTER);
		panel.setOpaque(true);
		panel.setBackground(Color.white);
		panel.add(vbox, BorderLayout.CENTER);
		return panel;
	}

	protected class ImageRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		JLabel jLabel;

		public ImageRenderer(String imgPath) {
			ImageIcon icon = new ImageIcon(imgPath);
			jLabel = new JLabel();
			jLabel.setIcon(icon);
//			 jLabel.setText((String) value);
//			jLabel.setBorder(new TextBubbleBorder(Color.green.darker(), 2, 5, 0));
			jLabel.setBackground(Color.LIGHT_GRAY);
			jLabel.setOpaque(true);
			jLabel.setHorizontalAlignment(CENTER);
			jLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, final int row,
				int column) {
			return jLabel;
		}
	}

	class CheckBoxrender extends DefaultTableCellRenderer {
		JCheckBox checkBox = new JCheckBox();

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, final int row,
				int column) {
			checkBox.setSelected(true);
			checkBox.setBorder(new TextBubbleBorder(Color.green.darker(), 2, 2, 0));

			return checkBox;
		}
	}
	

    
}

package com.im.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.im.utils.Constants;
import com.im.utils.Util;

public class SearchField {
	
	private String searchingName;
	
	public void makeSearchField(final JTextField textField, final String URL, final String method, final String responseRoot,
		final ISearchField iSearchField) {
		final DefaultComboBoxModel model = new DefaultComboBoxModel();
		final JComboBox comboBox = new JComboBox(model) {
			public Dimension getPreferredSize() {
				return new Dimension(super.getPreferredSize().width, 0);
			}
		};
		setAdjusting(comboBox, false);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isAdjusting(comboBox)) {
					if (comboBox.getSelectedItem() != null) {
						textField.setText(comboBox.getSelectedItem().toString());
						iSearchField.onSelect(comboBox.getSelectedIndex(), comboBox.getSelectedItem().toString());
					}
				}
			}
		});
		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent paramKeyEvent) {}
			
			@Override
			public void keyReleased(KeyEvent paramKeyEvent) {
				if (isValidKey(paramKeyEvent.getKeyCode())) {
					updateList();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				setAdjusting(comboBox, true);
//				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//					if (comboBox.isPopupVisible()) {
//						e.setKeyCode(KeyEvent.VK_ENTER);
//					}
//				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
					e.setSource(comboBox);
					comboBox.dispatchEvent(e);
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						textField.setText(comboBox.getSelectedItem().toString());
						comboBox.setPopupVisible(false);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					comboBox.setPopupVisible(false);
				}
				setAdjusting(comboBox, false);
			}
			
			private void updateList() {
				String input = textField.getText();
				if (!input.isEmpty()) {
					getURLList(input);
				}
			}
			
			private void getURLList(final String name) {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							searchingName = name;
							setAdjusting(comboBox, true);
							model.removeAllElements();
							comboBox.setPopupVisible(false);
							model.addElement("Loading...");
							comboBox.setPopupVisible(true);
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("method", method);
							jsonObject.put("name", name);
							String jsonResponse = Util.getHTTPResponseStr(URL, jsonObject.toString());
							if(Constants.showConsole) if(Constants.showConsole) System.out.println("----->" + jsonResponse);
							
							if(Constants.showConsole) if(Constants.showConsole) System.out.println("searchingName : " + searchingName + ", current search on : " + name);
							if (null != searchingName && searchingName.equalsIgnoreCase(name)) {
								Gson gson = new Gson();
								Map<String, List<Map<String, String>>> javaObj = gson.fromJson(jsonResponse, Map.class);
								List<Map<String, String>> responseList = javaObj.get(responseRoot);
								model.removeAllElements();
								comboBox.setPopupVisible(false);
								if (null != responseList) {
									model.addElement(name);
									iSearchField.loopStart();
									for (Map<String, String> map : responseList) {
										model.addElement(iSearchField.fieldValue(map));
									}
								}
								comboBox.setPopupVisible(model.getSize() > 0);
								setAdjusting(comboBox, false);
								textField.requestFocus();
								textField.setText("");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				Thread t = new Thread(runnable);
				t.start();
			}
			
			private boolean isValidKey(int key) {
				return (((key >= KeyEvent.VK_0) && (key <= KeyEvent.VK_CLOSE_BRACKET)) || (key <= KeyEvent.VK_BACK_SPACE) || (key <= KeyEvent.VK_SPACE || (key <= KeyEvent.VK_DELETE)))
					&& key != KeyEvent.VK_ENTER
					&& key != KeyEvent.VK_DOWN
					&& key != KeyEvent.VK_UP
					&& key != KeyEvent.VK_LEFT
					&& key != KeyEvent.VK_RIGHT;
			}
		});
		textField.setLayout(new BorderLayout());
		textField.add(comboBox, BorderLayout.SOUTH);
	}
	
	private static void setAdjusting(JComboBox comboBox, boolean adjusting) {
		comboBox.putClientProperty("is_adjusting", adjusting);
	}
	
	private static boolean isAdjusting(JComboBox comboBox) {
		if (comboBox.getClientProperty("is_adjusting") instanceof Boolean) {
			return (Boolean) comboBox.getClientProperty("is_adjusting");
		}
		return false;
	}
}

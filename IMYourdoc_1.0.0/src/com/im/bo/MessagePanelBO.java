package com.im.bo;

import javax.swing.JPanel;

public class MessagePanelBO {

	private JPanel jPanel;
	private String msg;

	public MessagePanelBO(JPanel jPanel, String msg) {
		this.jPanel = jPanel;
		this.msg = msg;
	}

	public JPanel getjPanel() {
		return jPanel;
	}

	public void setjPanel(JPanel jPanel) {
		this.jPanel = jPanel;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

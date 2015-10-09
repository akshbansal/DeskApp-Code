package com.im.common;

import java.awt.Font;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONException;

import com.im.bo.DeviceDetailBO;
import com.im.utils.HospitalRelatedList;

public class DeviceTable extends IMURDocTable {

	private static DeviceTable deviceTable = new DeviceTable();

	private List<DeviceDetailBO> deviceList;

	private DeviceTable() {

	}

	public static DeviceTable getInstance() {
		return deviceTable;
	}

	@Override
	public String[] getColumns() {
		String[] columns = { "S.No", "DeviceID", "Device Name", "Status" };
		return columns;
	}

	@Override
	public Object[][] getData() {
		Object[][] data = null;
		try {
			deviceList = new HospitalRelatedList("").getDeviceList();
			data = new Object[deviceList.size()][9];
			for (int i = 0; i < deviceList.size(); i++) {
				DeviceDetailBO deviceBo = deviceList.get(i);
				data[i][0] = i + 1;
				data[i][1] = deviceBo.getDevice_id();
				data[i][2] = deviceBo.getDevice_name();
				data[i][3] = deviceBo.getStatus();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

	@Override
	protected JTable getTable() {
		final Class<?>[] columnClass = new Class[] { Integer.class, String.class, String.class, String.class};
		DefaultTableModel model = new DefaultTableModel(getData(), getColumns()) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return columnClass[columnIndex];
			}
		};

		JTable table = new JTable(model);
		table.setFont(new Font(Font.decode("CentraleSansRndMedium").getFontName(), Font.PLAIN, 18));
//		table.getColumnModel().getColumn(7).setCellRenderer(new ImageRenderer("images/icon/userprofile.png"));
//		table.getColumnModel().getColumn(8).setCellRenderer(new ImageRenderer("images/icon/delete.png"));
		return table;
	}

	@Override
	protected JButton addNewBtn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected MouseListener getMouseListener() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

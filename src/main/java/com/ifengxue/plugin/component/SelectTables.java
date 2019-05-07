package com.ifengxue.plugin.component;

import java.util.List;

import javax.swing.*;

import com.ifengxue.plugin.entity.Table;

public class SelectTables {

	private JTable tblTableSchema;
	private JPanel rootComponent;
	private JButton btnSelectOther;
	private JButton btnSelectAll;
	private JButton btnSelectNone;
	private JButton btnCancel;
	private JButton btnGenerate;
	private JButton btnTableParse;
	private JButton btnColumnModify;
	private final List<Table> tableList;

	public SelectTables(List<Table> tableList) {
		this.tableList = tableList;
	}

	public JTable getTblTableSchema() {
		return tblTableSchema;
	}

	public JPanel getRootComponent() {
		return rootComponent;
	}

	public JButton getBtnSelectOther() {
		return btnSelectOther;
	}

	public JButton getBtnSelectAll() {
		return btnSelectAll;
	}

	public JButton getBtnSelectNone() {
		return btnSelectNone;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public JButton getBtnTableParse() {
		return btnTableParse;
	}

	public JButton getBtnColumnModify() {
		return btnColumnModify;
	}

	public JButton getBtnGenerate() {
		return btnGenerate;
	}

	public List<Table> getTableList() {
		return tableList;
	}
}

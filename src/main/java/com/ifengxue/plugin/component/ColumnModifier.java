package com.ifengxue.plugin.component;

import java.util.List;

import javax.swing.*;

import com.ifengxue.plugin.entity.Table;

import lombok.Getter;

@Getter
public class ColumnModifier {
	private JPanel rootComponent;
	private JTable tblTableColumn;
	private JComboBox cmbTableList;
	private JButton cancelButton;
	private JButton doneButton;
	private JPanel btnPanel;
	private JScrollPane scrollpaneTableColumn;

	private final List<Table> tableList;

	public ColumnModifier(List<Table> tableList) {
		this.tableList = tableList;
	}

}

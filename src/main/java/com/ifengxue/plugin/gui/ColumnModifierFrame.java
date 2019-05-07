package com.ifengxue.plugin.gui;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import com.ifengxue.plugin.Holder;
import com.ifengxue.plugin.component.ColumnModifier;
import com.ifengxue.plugin.entity.Column;
import com.ifengxue.plugin.entity.Table;
import com.ifengxue.plugin.i18n.LocaleContextHolder;
import com.ifengxue.plugin.util.WindowUtil;
import com.intellij.openapi.diagnostic.Logger;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ColumnModifierFrame {

	private Logger log = Logger.getInstance(getClass());
	private final JFrame frameHolder;

	public ColumnModifierFrame(List<Table> tableList) {
		frameHolder = new JFrame(LocaleContextHolder.format("form_column_modifier_title"));
		ColumnModifier columnModifier = new ColumnModifier(tableList);
		JComboBox cmbTableList = columnModifier.getCmbTableList();
		cmbTableList.setModel(new DefaultComboBoxModel(tableList.stream().map(e -> e.getTableName()).toArray()) {

		});
		cmbTableList.setSelectedIndex(0);

		JTable tblTableColumn = columnModifier.getTblTableColumn();
		tblTableColumn.setModel(new AbstractTableModel() {

			private static final long serialVersionUID = 1L;
			List<String> columns = Arrays.stream(ColumnInfo.values()).map(e -> e.localKey).collect(Collectors.toList());

			@Override
			public int getRowCount() {
				return tableList.get(cmbTableList.getSelectedIndex()).getColumns().size();
			}

			@Override
			public int getColumnCount() {
				return columns.size();
			}

			@Override
			public String getColumnName(int idx) {
				return columns.get(idx);
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				switch (columnIndex) {
					case 0:
						return ColumnInfo._0_NO.columnType;
					case 1:
						return ColumnInfo._1_NAME.columnType;
					case 2:
						return ColumnInfo._2_DATA_TYPE.columnType;
					default:
						return String.class;
				}
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				int cmbIdx = cmbTableList.getSelectedIndex();
				List<Column> columnList = tableList.get(cmbIdx).getColumns();
				switch (columnIndex) {
					case 0:
						return rowIndex + 1;
					case 1:
						return columnList.get(rowIndex).getColumnName();
					case 2:
						return columnList.get(rowIndex).getJavaDataType();
					default:
						throw new IllegalArgumentException("ex at: " + columnIndex);
				}
			}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				int cmbIdx = cmbTableList.getSelectedIndex();
				switch (columnIndex) {
					case 2:
						tableList.get(cmbIdx).getColumns().get(rowIndex).setJavaDataType(aValue.getClass());
						break;
					default:
						break;
				}
				tblTableColumn.updateUI();
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return columnIndex == 2;
			}
		});
		tblTableColumn.getColumnModel().getColumn(2).setCellEditor(
			new DataTypeComboBox(Arrays.stream(DataType.values()).map(e -> e.getDataType()).toArray()));
		tblTableColumn.getColumnModel().getColumn(2).setCellRenderer(
			new DataTypeComboBoxRenderer(Arrays.stream(DataType.values()).map(e -> e.getDataType()).toArray()));

		tblTableColumn.getColumnModel().getColumn(0).setMaxWidth(30);
		tblTableColumn.getColumnModel().getColumn(1).setMaxWidth(200);
		tblTableColumn.getColumnModel().getColumn(2).setMaxWidth(100);

		frameHolder.setContentPane(columnModifier.getRootComponent());
		frameHolder.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frameHolder.setLocationRelativeTo(WindowUtil.getParentWindow(Holder.getEvent().getProject()));
		frameHolder.pack();
		frameHolder.setVisible(true);

		columnModifier.getCmbTableList().addItemListener(e -> {
			log.debug(e.getItem().toString());
		});

	}

	public static void show(List<Table> tableList) {
		new ColumnModifierFrame(tableList);
	}

	@AllArgsConstructor
	enum ColumnInfo {
		_0_NO(0, Long.class, "No"),
		_1_NAME(1, String.class, "Name"),
		_2_DATA_TYPE(2, Object.class, "Data type"),
		;

		int idx;
		Class<?> columnType;
		String localKey;
	}

	@AllArgsConstructor
	@Getter
	enum DataType {
		STRING(String.class),
		INT(int.class),
		LONG(Long.class),
		DATE(Date.class),
		BIG_DECIMAL(BigDecimal.class),
		FLOAT(float.class),
		DOUBLE(double.class),
		BOOLEAN(boolean.class),
		;
		Class<?> dataType;
	}

	class DataTypeComboBoxRenderer extends JComboBox implements TableCellRenderer {

		public DataTypeComboBoxRenderer(Object[] items) {
			super(items);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				super.setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setSelectedItem(value);
			return this;
		}
	}

	class DataTypeComboBox extends DefaultCellEditor {
		public DataTypeComboBox(Object[] items) {
			super(new JComboBox(items));
		}
	}
}

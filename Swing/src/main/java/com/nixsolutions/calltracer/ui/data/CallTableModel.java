package com.nixsolutions.calltracer.ui.data;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author denis_k
 *         Date: 17.03.2010
 *         Time: 13:30:34
 */
public class CallTableModel extends AbstractTableModel {
	private List<DeletableCall> calls = new ArrayList<DeletableCall>();
	private final static String[] columns = {"Удалить", "Телефон", "Время", "Описание"};

	private final static int DELETE_COL = 0;
	private final static int NUMBER_COL = 1;
	private final static int DATE_COL = 2;
	private final static int DESCRIPTION_COL = 3;

	public void setCalls(List<DeletableCall> calls) {
		this.calls = calls;
	}

	public int getRowCount() {
		return calls.size();
	}

	public int getColumnCount() {
		return 4;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == DELETE_COL) {
			return calls.get(rowIndex).isDelete();
		}

		if (columnIndex == NUMBER_COL) {
			return calls.get(rowIndex).getCall().getPhoneNumber();
		}

		if (columnIndex == DATE_COL) {
			return calls.get(rowIndex).getCall().getCallDate();
		}

		if (columnIndex == DESCRIPTION_COL) {
			return calls.get(rowIndex).getCall().getDescription();
		}

		throw new IllegalArgumentException();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == DELETE_COL) {
			return Boolean.class;
		}

		if (columnIndex == NUMBER_COL) {
			return String.class;
		}

		if (columnIndex == DATE_COL) {
			return Date.class;
		}

		if (columnIndex == DESCRIPTION_COL) {
			return String.class;
		}

		throw new IllegalArgumentException();
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
}

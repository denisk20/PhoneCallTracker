package com.nixsolutions.calltracer.ui.editors;

import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * @author denis_k
 *         Date: 21.03.2010
 *         Time: 22:40:50
 */
public class JXDatePickerEditor extends AbstractCellEditor
		implements TableCellEditor {
	private JXDatePicker picker;


	public JXDatePickerEditor() {
		picker = new JXDatePicker();
		picker.getEditor().setEditable(false);
		picker.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireEditingStopped();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		picker.setDate((Date) value);
		return picker;
	}

	@Override
	public Object getCellEditorValue() {
		return picker.getDate();
	}

}

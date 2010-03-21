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
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        picker = new JXDatePicker((Date) value);
        picker.getEditor().setEditable(false);
        return picker;
    }

    @Override
    public Object getCellEditorValue() {
        return picker.getDate();
    }

}

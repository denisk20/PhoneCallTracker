package com.nixsolutions.calltracer.ui.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.nixsolutions.calltracer.ui.data.CallTableModel;
import com.nixsolutions.calltracer.ui.data.CallTracerData;
import com.nixsolutions.calltracer.ui.data.DateRange;
import com.nixsolutions.calltracer.ui.data.DeletableCall;
import com.nixsolutions.calltracer.ui.enablers.ConditionalEnabler;
import com.nixsolutions.calltracer.ui.enablers.JComponentEnabler;
import com.nixsolutions.calltracer.ui.executors.CallQueryExecutor;
import com.nixsolutions.calltracker.model.Call;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.calendar.DateSelectionModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author denis_k
 *         Date: 12.03.2010
 *         Time: 13:48:36
 */
public class CallTracerUI {
    private JButton addNumber;
    private JButton deleteSelected;
    private JTable dataTable;
    private JCheckBox dateSearchCheckBox;
    private JCheckBox phoneNumberCheckBox;
    private JCheckBox descriptionCheckBox;
    private JRadioButton daysNumberRadioButton;
    private JRadioButton spanRadioButton;
    private JFormattedTextField phoneNumber;
    private JTextField description;
    private JFormattedTextField numberOfDaysField;
    private JPanel searchPanel;
    private JPanel mainPanel;
    private JPanel formPanel;
    private JPanel diapasonPanel;
    private JLabel fromLabel;
    private JLabel toLabel;
    private JPanel buttonPanel;
    private JPanel diapasonSpanPanel;
    private JXDatePicker fromDate;
    private JXDatePicker toDate;
    private ButtonGroup spanRadioGroup;

    private final static int DEFAULT_NUMBER_OF_DAYS = 100;
    private DateRange startDateRange = getDateRangeFromDays(DEFAULT_NUMBER_OF_DAYS);

    private JComponentEnabler dateRangeDaysEnabler;
    private JComponentEnabler dateRangeSpanEnabler;
    private JComponentEnabler phoneNumEnabler;
    private JComponentEnabler descriptionEnabler;

    private ConditionalEnabler diapasonEnabler;

    private CallQueryExecutor callQueryExecutor;

    private Date lastStartDate;
    private Date lastEndDate;

    public CallTracerUI(CallQueryExecutor callQueryExecutor) {
        this.callQueryExecutor = callQueryExecutor;

        fromDate.getEditor().setEditable(false);
        toDate.getEditor().setEditable(false);

        prepareStartData();
        setupDefaultBehaviour();
        setupEnablers();
        initListeners();
        initTableData();

    }

    private void initTableData() {

        CallTracerData data = new CallTracerData();
        getData(data);
        callQueryExecutor.setPhone(data.getPhoneNumber());
        callQueryExecutor.setDescription(data.getDescription());
        DateRange dateRange = getDateRangeFromDays(DEFAULT_NUMBER_OF_DAYS);
        callQueryExecutor.setDateRange(dateRange);

        List<Call> initialResults = callQueryExecutor.queryCalls();
        updateTable(initialResults);
    }

    private DateRange getDateRangeFromDays(int defaultNumberOfDays) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        calendar.add(Calendar.DATE, -defaultNumberOfDays);
        Date start = calendar.getTime();

        return new DateRange(start, currentDate);
    }

    private void initListeners() {
        initButtonListeners();

        initInterfaceListeners();

        initInputListeners();
    }

    private void initInputListeners() {
        fromDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date startDate = fromDate.getDate();
                if (startDate == null) {
                    fromDate.setDate(lastStartDate);
                    return;
                }
                lastStartDate = startDate;
                toDate.getMonthView().setLowerBound(startDate);
                runDateRangeQueryAndUpdateTable(new DateRange(startDate, toDate.getDate()));

            }
        });

        toDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date endDate = toDate.getDate();
                if (endDate == null) {
                    toDate.setDate(lastEndDate);
                    return;
                }
                lastEndDate = endDate;
                fromDate.getMonthView().setUpperBound(endDate);
                runDateRangeQueryAndUpdateTable(new DateRange(fromDate.getDate(), endDate));
            }
        });

        //todo validation
        phoneNumber.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                runPhoneQueryAndUpdateTable();
            }

            public void removeUpdate(DocumentEvent e) {
                runPhoneQueryAndUpdateTable();
            }

            public void changedUpdate(DocumentEvent e) {
                //this should never be called
                throw new UnsupportedOperationException("changedUpdate was called");
            }
        });

        description.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                runDescriptionQueryAndUpdateTable();
            }

            public void removeUpdate(DocumentEvent e) {
                runDescriptionQueryAndUpdateTable();
            }

            public void changedUpdate(DocumentEvent e) {
                //this should never be called
                throw new UnsupportedOperationException("changedUpdate was called");
            }
        });

        //todo add validation
        numberOfDaysField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                runNumberOfDaysQueryAndUpdateTable();
            }

            public void removeUpdate(DocumentEvent e) {
                runNumberOfDaysQueryAndUpdateTable();
            }

            public void changedUpdate(DocumentEvent e) {
                //this should never be called
                throw new UnsupportedOperationException("changedUpdate was called");
            }
        });
    }

    private void initButtonListeners() {
        addNumber.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane pane = new JOptionPane();
                JDialog addCallUi = pane.createDialog("some title");
                addCallUi.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        List<Call> initialResults = callQueryExecutor.queryCalls();
                        updateTable(initialResults);
                    }
                });
                addCallUi.setContentPane(new AddCallUI().getRootPane());
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension screenSize = tk.getScreenSize();
                int screenHeight = screenSize.height;
                int screenWidth = screenSize.width;
                addCallUi.setSize(screenWidth / 2, screenHeight / 2);
                addCallUi.setLocation(screenWidth / 4, screenHeight / 4);
                addCallUi.pack();
                addCallUi.setVisible(true);
            }
        });
        deleteSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    private void runDescriptionQueryAndUpdateTable() {
        String descriptionText = description.getText();
        List<Call> calls = callQueryExecutor.setDescriptionAndQueryCalls(descriptionText);
        updateTable(calls);
    }

    private void runPhoneQueryAndUpdateTable() {
        String phoneQuery = phoneNumber.getText();
        List<Call> calls = callQueryExecutor.setPhoneAndQueryCalls(phoneQuery);
        updateTable(calls);
    }

    private void runNumberOfDaysQueryAndUpdateTable() {
        String daysString = numberOfDaysField.getText();
        int numberOfDays;
        if (daysString == null || daysString.equals("")) {
            numberOfDays = 0;
        } else {
            try {
                numberOfDays = Integer.parseInt(daysString);
            } catch (NumberFormatException e) {
                //error message
                throw e;
            }
        }
        DateRange range = getDateRangeFromDays(numberOfDays);
        List<Call> calls = callQueryExecutor.setDateRangeAndQueryCalls(range);
        updateTable(calls);
    }

    private void runDateRangeQueryAndUpdateTable(DateRange range) {
        List<Call> calls = callQueryExecutor.setDateRangeAndQueryCalls(range);
        updateTable(calls);

    }

    private void initInterfaceListeners() {
        dateSearchCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    diapasonEnabler.enable();
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    diapasonEnabler.disable();
                }
            }
        });
        phoneNumberCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    phoneNumEnabler.enable();
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    phoneNumEnabler.disable();
                }
            }
        });
        descriptionCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    descriptionEnabler.enable();
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    descriptionEnabler.disable();
                }
            }
        });
        daysNumberRadioButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    diapasonEnabler.setSelected(true);
                    dateRangeDaysEnabler.enable();

                    int daysNum = Integer.parseInt(numberOfDaysField.getText());
                    runDateRangeQueryAndUpdateTable(getDateRangeFromDays(daysNum));

                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    diapasonEnabler.setSelected(false);
                    dateRangeDaysEnabler.disable();
                }
            }
        });
        spanRadioButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    diapasonEnabler.setSelected(false);
                    dateRangeSpanEnabler.enable();

                    runDateRangeQueryAndUpdateTable(new DateRange(fromDate.getDate(), toDate.getDate()));
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                    diapasonEnabler.setSelected(true);
                    dateRangeSpanEnabler.disable();
                }
            }
        });
    }

    private void updateTable(List<Call> calls) {
        List<DeletableCall> deletableCalls = convertIntoDeletableCalls(calls);
        CallTableModel tableModel = new CallTableModel();
        tableModel.setCalls(deletableCalls);
        dataTable.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }

    private List<DeletableCall> convertIntoDeletableCalls(List<Call> calls) {
        ArrayList<DeletableCall> deletableCalls = new ArrayList<DeletableCall>();
        for (Call call : calls) {
            DeletableCall deletableCall = new DeletableCall(call);
            deletableCalls.add(deletableCall);
        }

        return deletableCalls;
    }


    private void setupEnablers() {
        dateRangeDaysEnabler = new JComponentEnabler();
        dateRangeDaysEnabler.addElement(numberOfDaysField);

        dateRangeSpanEnabler = new JComponentEnabler();
        dateRangeSpanEnabler.addElement(fromDate);
        dateRangeSpanEnabler.addElement(toDate);

        diapasonEnabler = new ConditionalEnabler(dateRangeDaysEnabler, dateRangeSpanEnabler);
        diapasonEnabler.addElement(daysNumberRadioButton);
        diapasonEnabler.addElement(spanRadioButton);
        diapasonEnabler.setSelected(true);

        phoneNumEnabler = new JComponentEnabler();
        phoneNumEnabler.addElement(phoneNumber);

        descriptionEnabler = new JComponentEnabler();
        descriptionEnabler.addElement(description);
    }

    private void prepareStartData() {
        CallTracerData data = new CallTracerData();
        data.setNumberOfDays(Integer.toString(DEFAULT_NUMBER_OF_DAYS));
        data.setFromDate(startDateRange.getStartDate());
        data.setToDate(startDateRange.getEndDate());
        setData(data);
    }

    //do this only for initial data setup

    public void setData(CallTracerData data) {
        numberOfDaysField.setText(data.getNumberOfDays());

        fromDate.setDate(data.getFromDate());
        fromDate.getMonthView().setUpperBound(data.getToDate());

        toDate.setDate(data.getToDate());
        toDate.getMonthView().setLowerBound(data.getFromDate());

        phoneNumber.setText(data.getPhoneNumber());
        description.setText(data.getDescription());

        lastStartDate = data.getFromDate();
        lastEndDate = data.getToDate();
    }

    public void getData(CallTracerData data) {
        data.setNumberOfDays(numberOfDaysField.getText());
        data.setFromDate(fromDate.getDate());
        data.setToDate(toDate.getDate());
        data.setPhoneNumber(phoneNumber.getText());
        data.setDescription(description.getText());
    }

    private void setupDefaultBehaviour() {
        dateSearchCheckBox.setSelected(true);

        phoneNumber.setEnabled(false);
        description.setEnabled(false);

        daysNumberRadioButton.setSelected(true);

        fromDate.setEnabled(false);
        toDate.setEnabled(false);

    }

    public CallQueryExecutor getCallQueryExecutor() {
        return callQueryExecutor;
    }

    public void setCallQueryExecutor(CallQueryExecutor callQueryExecutor) {
        this.callQueryExecutor = callQueryExecutor;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        formPanel = new JPanel();
        formPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(1, 2, new Insets(2, 2, 0, 0), -1, -1));
        formPanel.add(mainPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 2, 2), -1, -1));
        mainPanel.add(searchPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(369, 72), null, 0, false));
        diapasonPanel = new JPanel();
        diapasonPanel.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        searchPanel.add(diapasonPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(160, 53), null, 0, false));
        daysNumberRadioButton = new JRadioButton();
        daysNumberRadioButton.setText("За последние");
        diapasonPanel.add(daysNumberRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spanRadioButton = new JRadioButton();
        spanRadioButton.setText("Диапазон");
        diapasonPanel.add(spanRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        numberOfDaysField = new JFormattedTextField();
        diapasonPanel.add(numberOfDaysField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(174, 22), null, 0, false));
        diapasonSpanPanel = new JPanel();
        diapasonSpanPanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        diapasonPanel.add(diapasonSpanPanel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(174, 28), null, 0, false));
        toLabel = new JLabel();
        toLabel.setText("по");
        diapasonSpanPanel.add(toLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fromDate = new JXDatePicker();
        diapasonSpanPanel.add(fromDate, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fromLabel = new JLabel();
        fromLabel.setText("С");
        diapasonSpanPanel.add(fromLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        toDate = new JXDatePicker();
        diapasonSpanPanel.add(toDate, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        searchPanel.add(panel1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(160, 26), null, 0, false));
        phoneNumber = new JFormattedTextField();
        panel1.add(phoneNumber, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(183, 22), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        searchPanel.add(panel2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(160, 26), null, 0, false));
        description = new JTextField();
        panel2.add(description, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(106, 22), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        searchPanel.add(panel3, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(103, 106), null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder("Критерии поиска"));
        dateSearchCheckBox = new JCheckBox();
        dateSearchCheckBox.setText("Поиск по дате");
        panel3.add(dateSearchCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(153, 22), null, 0, false));
        phoneNumberCheckBox = new JCheckBox();
        phoneNumberCheckBox.setText("Номер");
        panel3.add(phoneNumberCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(153, 22), null, 0, false));
        descriptionCheckBox = new JCheckBox();
        descriptionCheckBox.setText("Описание");
        panel3.add(descriptionCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(153, 22), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(326, 72), null, 0, false));
        dataTable = new JTable();
        scrollPane1.setViewportView(dataTable);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 2, new Insets(2, 2, 0, 0), -1, -1));
        formPanel.add(buttonPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(192, 9), null, 0, false));
        addNumber = new JButton();
        addNumber.setText("Добавить");
        addNumber.setMnemonic('Д');
        addNumber.setDisplayedMnemonicIndex(0);
        buttonPanel.add(addNumber, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, 16), null, 0, false));
        deleteSelected = new JButton();
        deleteSelected.setText("Удалить");
        deleteSelected.setMnemonic('У');
        deleteSelected.setDisplayedMnemonicIndex(0);
        buttonPanel.add(deleteSelected, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(78, 16), null, 0, false));
        spanRadioGroup = new ButtonGroup();
        spanRadioGroup.add(daysNumberRadioButton);
        spanRadioGroup.add(daysNumberRadioButton);
        spanRadioGroup.add(spanRadioButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return formPanel;
    }
}

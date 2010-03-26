package com.nixsolutions.calltracer.ui.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.nixsolutions.calltracer.ui.data.CallTableModel;
import com.nixsolutions.calltracer.ui.data.CallTracerData;
import com.nixsolutions.calltracer.ui.data.DateRange;
import com.nixsolutions.calltracer.ui.data.DeletableCall;
import com.nixsolutions.calltracer.ui.editors.JXDatePickerEditor;
import com.nixsolutions.calltracer.ui.enablers.ConditionalEnabler;
import com.nixsolutions.calltracer.ui.enablers.JComponentEnabler;
import com.nixsolutions.calltracer.ui.executors.CallQueryExecutor;
import com.nixsolutions.calltracker.model.Call;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXDatePicker;

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
	private final Logger log = Logger.getLogger(getClass());
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
	private JSplitPane splitter;
	private JScrollPane tablePanel;
	private JPanel tableTopPanel;
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

		$$$setupUI$$$();
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
				toDate.getMonthView().setUpperBound(new Date());

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
				Object[] options = {"Удалить",
						"Отменить"};
				int n = JOptionPane.showOptionDialog(formPanel,
						"Все помеченные номера будут безвозвратно удалены!",
						"Удаление номеров",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE,
						null,
						options,
						options[1]);
				if (n == JOptionPane.YES_OPTION) {
					CallTableModel tableColumnModel = (CallTableModel) dataTable.getModel();
					callQueryExecutor.deleteCalls(tableColumnModel.getCalls());
					List<Call> initialResults = callQueryExecutor.queryCalls();
					updateTable(initialResults);
				}
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

					if (daysNumberRadioButton.isSelected()) {
						runNumberOfDaysQueryAndUpdateTable();
					} else {
						Date start = fromDate.getDate();
						Date end = toDate.getDate();
						if (start != null && end != null) {
							runDateRangeQueryAndUpdateTable(new DateRange(start, end));
						} else {
							log.error("Some date was null. Start=" + start + ", end=" + end);
						}
					}
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					diapasonEnabler.disable();

					List<Call> calls = callQueryExecutor.subsractDateRangeAndRunQuery();
					updateTable(calls);
				}
			}
		});
		phoneNumberCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					phoneNumEnabler.enable();
					runPhoneQueryAndUpdateTable();
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					phoneNumEnabler.disable();
					List<Call> calls = callQueryExecutor.substractPhoneAndRunQuery();
					updateTable(calls);
				}
			}
		});
		descriptionCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					descriptionEnabler.enable();
					runDescriptionQueryAndUpdateTable();
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					descriptionEnabler.disable();
					List<Call> calls = callQueryExecutor.substractDescriptionAndRunQuery();
					updateTable(calls);
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
		toDate.getMonthView().setUpperBound(new Date());

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

		dataTable.setDefaultEditor(Date.class, new JXDatePickerEditor());
		dataTable.setDragEnabled(false);
	}

	public CallQueryExecutor getCallQueryExecutor() {
		return callQueryExecutor;
	}

	public void setCallQueryExecutor(CallQueryExecutor callQueryExecutor) {
		this.callQueryExecutor = callQueryExecutor;
	}

	private void createUIComponents() {
		//NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberOfDaysField = new JFormattedTextField();
		//numberOfDaysField.setBackground(new Color(12, 233, 44));
		numberOfDaysField.setFocusLostBehavior(JFormattedTextField.COMMIT);

		phoneNumber = new JFormattedTextField();
		phoneNumber.setFocusLostBehavior(JFormattedTextField.COMMIT);
		splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePanel, searchPanel);
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 *
	 * @noinspection ALL
	 */
	private void $$$setupUI$$$() {
		createUIComponents();
		formPanel = new JPanel();
		formPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
		mainPanel = new JPanel();
		mainPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "fill:d:grow"));
		formPanel.add(mainPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		searchPanel = new JPanel();
		searchPanel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow", "fill:d:grow,top:4dlu:noGrow,fill:d:grow,top:4dlu:noGrow,fill:d:grow"));
		CellConstraints cc = new CellConstraints();
		mainPanel.add(searchPanel, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.FILL));
		diapasonPanel = new JPanel();
		diapasonPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:d:grow,top:4dlu:noGrow,center:d:grow"));
		searchPanel.add(diapasonPanel, cc.xy(3, 1));
		diapasonPanel.setBorder(BorderFactory.createTitledBorder("Дата звонка"));
		daysNumberRadioButton = new JRadioButton();
		daysNumberRadioButton.setText("За последние");
		diapasonPanel.add(daysNumberRadioButton, cc.xy(1, 1));
		spanRadioButton = new JRadioButton();
		spanRadioButton.setText("Диапазон");
		diapasonPanel.add(spanRadioButton, cc.xy(1, 3));
		diapasonPanel.add(numberOfDaysField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
		diapasonSpanPanel = new JPanel();
		diapasonSpanPanel.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow"));
		diapasonPanel.add(diapasonSpanPanel, cc.xy(3, 3));
		toLabel = new JLabel();
		toLabel.setText("по");
		diapasonSpanPanel.add(toLabel, cc.xy(5, 1));
		fromDate = new JXDatePicker();
		diapasonSpanPanel.add(fromDate, cc.xy(3, 1));
		fromLabel = new JLabel();
		fromLabel.setText("С");
		diapasonSpanPanel.add(fromLabel, cc.xy(1, 1));
		toDate = new JXDatePicker();
		diapasonSpanPanel.add(toDate, cc.xy(7, 1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
		diapasonPanel.add(panel1, cc.xy(5, 1));
		final JLabel label1 = new JLabel();
		label1.setText("дней");
		panel1.add(label1, cc.xy(1, 1));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
		searchPanel.add(panel2, cc.xy(3, 3));
		final JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel2.add(panel3, cc.xy(1, 1));
		panel3.setBorder(BorderFactory.createTitledBorder("Номер телефона"));
		panel3.add(phoneNumber, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(183, 22), null, 0, false));
		final JPanel panel4 = new JPanel();
		panel4.setLayout(new FormLayout("fill:d:grow", "center:d:grow"));
		searchPanel.add(panel4, cc.xy(3, 5));
		final JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		panel4.add(panel5, cc.xy(1, 1));
		panel5.setBorder(BorderFactory.createTitledBorder("Описание"));
		description = new JTextField();
		panel5.add(description, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(106, 22), null, 0, false));
		final JPanel panel6 = new JPanel();
		panel6.setLayout(new FormLayout("fill:d:grow", "fill:d:grow,top:4dlu:noGrow,fill:d:grow,top:4dlu:noGrow,fill:d:grow"));
		searchPanel.add(panel6, new CellConstraints(1, 1, 1, 5, CellConstraints.DEFAULT, CellConstraints.FILL, new Insets(0, 0, 10, 0)));
		panel6.setBorder(BorderFactory.createTitledBorder("Критерии поиска"));
		dateSearchCheckBox = new JCheckBox();
		dateSearchCheckBox.setText("Поиск по дате");
		panel6.add(dateSearchCheckBox, cc.xy(1, 1));
		phoneNumberCheckBox = new JCheckBox();
		phoneNumberCheckBox.setText("Номер");
		panel6.add(phoneNumberCheckBox, cc.xy(1, 3));
		descriptionCheckBox = new JCheckBox();
		descriptionCheckBox.setText("Описание");
		panel6.add(descriptionCheckBox, cc.xy(1, 5));
		tableTopPanel = new JPanel();
		tableTopPanel.setLayout(new FormLayout("fill:d:grow", "fill:d:grow"));
		mainPanel.add(tableTopPanel, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 10, 10)));
		tablePanel = new JScrollPane();
		tableTopPanel.add(tablePanel, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		dataTable = new JTable();
		tablePanel.setViewportView(dataTable);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:75px:grow", "center:d:noGrow"));
		formPanel.add(buttonPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(192, 9), null, 0, false));
		addNumber = new JButton();
		addNumber.setText("Добавить");
		buttonPanel.add(addNumber, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
		deleteSelected = new JButton();
		deleteSelected.setText("Удалить");
		buttonPanel.add(deleteSelected, cc.xy(3, 1));
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

package com.nixsolutions.calltracer.ui.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.nixsolutions.calltracer.ui.data.AddCallData;
import com.nixsolutions.calltracker.dao.CallDao;
import com.nixsolutions.calltracker.dao.impl.CallDaoHibernate;
import com.nixsolutions.calltracker.model.Call;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Set;

/**
 * @author denis_k
 *         Date: 19.03.2010
 *         Time: 10:18:57
 */
public class AddCallUI {
    private JPanel formPanel;
    private JFormattedTextField phoneNumberTextField;
    private JTextField descriptionTextField;
    private JButton addButton;
    private JXDatePicker callDateDatePicker;
    private JLabel errorLabel;
    private JLabel sucessLabel;

    CallDaoHibernate callDao = new CallDaoHibernate();

    private final Logger log = Logger.getLogger(getClass());
    private Date lastStartDate;

    public AddCallUI() {

        setupUI();
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sucessLabel.setVisible(false);
                Call call = new Call();
                AddCallData data = new AddCallData();
                getData(data);
                setUpCall(call, data);
                validateCall(call);
                makeCallPersistent(call);
                clearForm();
                sucessLabel.setVisible(true);
            }
        });
        callDateDatePicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date startDate = callDateDatePicker.getDate();
                if (startDate == null) {
                    callDateDatePicker.setDate(lastStartDate);
                    return;
                }
                lastStartDate = startDate;

            }
        });
    }

    private void validateCall(Call call) throws ValidationException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        errorLabel.setText("");

        Set<ConstraintViolation<Call>> constraintViolations =
                validator.validate(call);
        if (constraintViolations.size() > 0) {
            sucessLabel.setVisible(false);
            for (ConstraintViolation cv : constraintViolations) {
                errorLabel.setText(errorLabel.getText() + "\n" + cv.getMessage());
            }
            throw new ValidationException("Validation failed");
        }
    }

    private void setupUI() {
        Date currentDate = new Date();
        lastStartDate = currentDate;
        callDateDatePicker.setDate(currentDate);
        callDateDatePicker.getEditor().setEditable(false);
        callDateDatePicker.getMonthView().setUpperBound(currentDate);
    }

    private void clearForm() {
        final AddCallData clearData = new AddCallData();
        clearData.setDate(new Date());
        setData(clearData);
    }

    private void makeCallPersistent(Call call) {
        try {
            callDao.getSession().beginTransaction();
            callDao.makePersistent(call);
            callDao.getSession().getTransaction().commit();
        } catch (ConstraintViolationException e) {
            //This shouldn't be thrown anymore
            sucessLabel.setVisible(false);
            errorLabel.setText("������� �� ��������!");
            log.error("Can't persist call " + call, e);
            callDao.getSession().getTransaction().rollback();
            throw e;
        } catch (HibernateException e) {
            //todo crete message
            sucessLabel.setVisible(false);
            errorLabel.setText("���������� ��������� �������");
            log.error("Can't persist call " + call, e);
            callDao.getSession().getTransaction().rollback();
            throw e;
        }
    }

    private void setUpCall(Call call, AddCallData data) {
        call.setPhoneNumber(data.getPhoneNumber());
        call.setCallDate(data.getDate());
        call.setDescription(data.getDescription());
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("AddCall");
        frame.setContentPane(new AddCallUI().formPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void setData(AddCallData data) {
        phoneNumberTextField.setText(data.getPhoneNumber());
        callDateDatePicker.setDate(data.getDate());
        descriptionTextField.setText(data.getDescription());
    }

    public void getData(AddCallData data) {
        data.setPhoneNumber(phoneNumberTextField.getText());
        data.setDate(callDateDatePicker.getDate());
        data.setDescription(descriptionTextField.getText());
    }

    public JComponent getRootPane() {
        return formPanel;
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
        formPanel.setLayout(new FormLayout("right:213px:noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:242px:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:58px:noGrow"));
        formPanel.setEnabled(false);
        phoneNumberTextField = new JFormattedTextField();
        phoneNumberTextField.setColumns(9);
        CellConstraints cc = new CellConstraints();
        formPanel.add(phoneNumberTextField, new CellConstraints(1, 5, 1, 1, CellConstraints.LEFT, CellConstraints.TOP, new Insets(10, 10, 0, 0)));
        callDateDatePicker = new JXDatePicker();
        formPanel.add(callDateDatePicker, new CellConstraints(3, 5, 1, 1, CellConstraints.LEFT, CellConstraints.TOP, new Insets(10, 0, 0, 0)));
        descriptionTextField = new JTextField();
        descriptionTextField.setColumns(20);
        formPanel.add(descriptionTextField, new CellConstraints(5, 5, 1, 1, CellConstraints.LEFT, CellConstraints.TOP, new Insets(10, 0, 0, 0)));
        addButton = new JButton();
        addButton.setText("���������");
        formPanel.add(addButton, new CellConstraints(7, 5, 1, 1, CellConstraints.LEFT, CellConstraints.TOP, new Insets(10, 0, 0, 10)));
        errorLabel = new JLabel();
        errorLabel.setForeground(new Color(-65485));
        errorLabel.setHorizontalAlignment(2);
        errorLabel.setHorizontalTextPosition(4);
        errorLabel.setText("");
        formPanel.add(errorLabel, cc.xy(1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        sucessLabel = new JLabel();
        sucessLabel.setEnabled(true);
        sucessLabel.setFont(new Font(sucessLabel.getFont().getName(), Font.BOLD, 14));
        sucessLabel.setForeground(new Color(-13382656));
        sucessLabel.setText("����� ��������");
        sucessLabel.setVisible(false);
        formPanel.add(sucessLabel, new CellConstraints(1, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets(0, 10, 0, 0)));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return formPanel;
    }
}

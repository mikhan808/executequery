package org.executequery.gui.editor;

import com.github.lgooddatepicker.components.DatePicker;
import org.executequery.components.BottomButtonPanel;
import org.executequery.gui.BaseDialog;
import org.executequery.gui.editor.autocomplete.Parameter;
import org.underworldlabs.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class InputParametersDialog extends BaseDialog {

    private List<Parameter> parameters;
    private JScrollPane scrollPanel;
    private JPanel mainPanel;
    private JPanel panel;
    private List<JComponent> componentList;
    private List<JCheckBox> nullBoxes;

    public InputParametersDialog(List<Parameter> parameters) {
        super("Input Parameters", true, true);
        this.parameters = parameters;
        init();
    }

    private void init() {
        mainPanel = new JPanel();
        scrollPanel = new JScrollPane();
        panel = new JPanel();
        scrollPanel.setViewportView(panel);
        panel.setLayout(new GridBagLayout());
        componentList = new ArrayList<>();
        nullBoxes = new ArrayList<>();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.add(scrollPanel, new GridBagConstraints(0, 0,
                1, 1, 1, 0,
                GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5),
                0, 0));
        // empty panel for stretch
        mainPanel.add(new JPanel(), new GridBagConstraints(0, 1, 2, 1,
                1, 1, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        BottomButtonPanel bottomButtonPanel = new BottomButtonPanel(this.isDialog());
        bottomButtonPanel.setOkButtonAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        bottomButtonPanel.setOkButtonText("OK");
        mainPanel.add(bottomButtonPanel, new GridBagConstraints(0, 2,
                1, 1, 1, 0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
                0, 0));
        for (int i = 0; i < parameters.size(); i++)
            addParameter(parameters.get(i));
        addDisplayComponent(mainPanel);
    }

    private void setValueToComponent(Parameter parameter, JComponent component) {
        if (parameter.getValue() != null)
            switch (parameter.getType()) {
                case Types.DATE:
                    ((DatePicker) component).setDate(LocalDate.parse((String) parameter.getValue()));
                    break;
                case Types.TIMESTAMP:
                    ((EQDateTimePicker) component).setDateTimePermissive(LocalDateTime.parse((String) parameter.getValue()));
                    break;
                case Types.TIME:
                    ((EQTimePicker) component).setTime(LocalTime.parse((String) parameter.getValue()));
                    break;
                case Types.BOOLEAN:
                    ((RDBCheckBox) component).setStingValue((String) parameter.getValue());
                    break;
                case Types.BINARY:
                case Types.BLOB:
                case Types.LONGVARBINARY:
                case Types.LONGVARCHAR:
                    ((RDBFieldFileChooser) component).setFile(((File) parameter.getValue()));
                    break;
                default:
                    ((JTextField) component).setText((String) parameter.getValue());
                    break;
            }
    }

    private void addParameter(Parameter parameter) {
        int count = componentList.size();
        panel.add(new JLabel(parameter.getName()), new GridBagConstraints(0, count,
                1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5),
                0, 0));
        panel.add(new JLabel(parameter.getTypeName()), new GridBagConstraints(1, count,
                1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5),
                0, 0));
        JComponent component;
        switch (parameter.getType()) {
            case Types.DATE:
                component = new DatePicker();
                break;
            case Types.TIMESTAMP:
                component = new EQDateTimePicker();
                break;
            case Types.TIME:
                component = new EQTimePicker();
                break;
            case Types.BOOLEAN:
                component = new RDBCheckBox();
                break;
            case Types.BINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
            case Types.LONGVARCHAR:
                component = new RDBFieldFileChooser();
                break;
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.SMALLINT:
                component = new NumberTextField();
                break;
            default:
                component = new JTextField(14);
                break;
        }
        setValueToComponent(parameter, component);
        componentList.add(component);
        panel.add(component, new GridBagConstraints(2, count,
                1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
                0, 0));
    }

    private void ok() {
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            JComponent component = componentList.get(i);
            switch (parameter.getType()) {
                case Types.DATE:
                    parameter.setValue(((DatePicker) component).getDateStringOrEmptyString());
                    break;
                case Types.TIMESTAMP:
                    parameter.setValue(((EQDateTimePicker) component).getStringValue());
                    break;
                case Types.TIME:
                    parameter.setValue(((EQTimePicker) component).getStringValue());
                    break;
                case Types.BOOLEAN:
                    parameter.setValue(((RDBCheckBox) component).getStringValue());
                    break;
                case Types.BINARY:
                case Types.BLOB:
                case Types.LONGVARBINARY:
                case Types.LONGVARCHAR:
                    File file = ((RDBFieldFileChooser) component).getFile();
                    parameter.setValue(file);
                    break;
                default:
                    parameter.setValue(((JTextField) component).getText());
                    break;
            }
        }
        finished();
    }
}

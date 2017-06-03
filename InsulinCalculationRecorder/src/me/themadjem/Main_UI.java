package me.themadjem;


import org.jetbrains.annotations.NotNull;
import themadjem.util.Output;
import themadjem.util.Reader;

import javax.swing.*;

/**
 * @author Jesse Maddox
 *         Copyright 5/28/2017
 */
class Main_UI {
    final String LOG_PATH = "Log.log";
    private final String PROPERTIES_PATH = "data.properties";
    private JPanel panel1;
    private JCheckBox generateIOBCheckBox;
    private JCheckBox logCheckBox;
    private JButton processButton;
    private JTextField icr_field;
    private JCheckBox enableEditingCheckBox;
    private JTextField targetBG_field;
    private JLabel divide_bar;
    private JLabel icr_label;
    private JTextField correction_field;
    private JLabel corr_label;
    private JTextField carbs_field;
    private JTextField bg_field;
    private JLabel bg_label;
    private JLabel carb_label;
    private JCheckBox manualTimeCheckBox;
    private JTextField hour_field;
    private JTextField minute_field;
    private JCheckBox manualDateCheckBox;
    private JTextField month_field;
    private JTextField day_field;
    private JTextField year_field;
    private JCheckBox generateReportCheckBox;
    private JRadioButton AMRadioButton;
    private JRadioButton PMRadioButton;
    private JCheckBox doNotUseBGCheckBox;
    private JRadioButton largePlusRadioButton;
    private JRadioButton negativeRadioButton;
    private JRadioButton traceRadioButton;
    private JRadioButton noRecordRadioButton;
    private JRadioButton smallRadioButton;
    private JRadioButton moderateRadioButton;
    private JRadioButton largeRadioButton;
    private JPanel keytonePanel;
    private JPanel manualPanel;
    private JLabel picLabel;
    private JPanel CalculatorPanel;
    private JPanel LogPanel;
    private JLabel MadeByLabel;
    private JTextPane LogTextPane;
    private JEditorPane insetHelpfulTipsHereEditorPane;
    private JTabbedPane iobTrackerPanel;
    private JPanel iobTrackPanel;
    private JLabel insulinAmt;
    @NotNull
    private final PropertiesUtil props = new PropertiesUtil(PROPERTIES_PATH, "Could not load properties", "Data");


    private final IOB_Tracker tracker;

    /**
     * Constructor
     */
    Main_UI() {
        tracker = new IOB_Tracker(this);
        tracker.start();

        readProperties();
        updateLog();


        UI_Listener listener = new UI_Listener(this);
        processButton.addActionListener(listener);
        enableEditingCheckBox.addActionListener(listener);
        manualDateCheckBox.addActionListener(listener);
        manualTimeCheckBox.addActionListener(listener);

        JFrame frame = new JFrame("Insulin Calculator and Log");
        frame.setContentPane(panel1);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(300, 300);
        frame.setVisible(true);
    }


    /**
     * Loads properties and set variables accordingly
     */
    private void readProperties() {
        try {
            props.loadParams();
        } catch (Exception e) {
            Output.infoBox("Could not load properties", "ERROR");
        }
        icr_field.setText(props.getProperty("ICR", "7"));
        correction_field.setText(props.getProperty("Corr", "30"));
        targetBG_field.setText(props.getProperty("Target", "150"));
        props.saveParamChanges();
    }

    void updateProperties() {
        props.setProperty("ICR", icr_field.getText());
        props.setProperty("Corr", correction_field.getText());
        props.setProperty("Target", targetBG_field.getText());
        props.saveParamChanges();
    }

    void updateLog() {
        try {
            Reader reader = new Reader(LOG_PATH);
            Util.clearPane(getLogTextPane());
            String s = reader.readln();
            while (s != null) {
                s = s.replace(',', '\t');
                s = s.replace(' ', '\t');
                Util.writeln(s, getLogTextPane());
                s = reader.readln();
            }
        } catch (Exception e) {
            Util.writeln("Unable to read Log", getLogTextPane());
            Util.writeln(e.getMessage(), getLogTextPane());
        }
    }

    /**
     * Returns a number based on which of the Keytones ButtonGroup is selected
     *
     * @return keytone code
     */
    int getKeytones() {
        if (negativeRadioButton.isSelected()) return 0;
        if (traceRadioButton.isSelected()) return 1;
        if (smallRadioButton.isSelected()) return 2;
        if (moderateRadioButton.isSelected()) return 3;
        if (largeRadioButton.isSelected()) return 4;
        if (largePlusRadioButton.isSelected()) return 5;
        else return -1;
    }

    //GETTERS - need no documentation

    JTextField getBg_field() {
        return bg_field;
    }

    JTextField getCarbs_field() {
        return carbs_field;
    }

    JTextField getCorrection_field() {
        return correction_field;
    }

    JTextField getIcr_field() {
        return icr_field;
    }

    JTextField getTargetBG_field() {
        return targetBG_field;
    }

    JTextField getHour_field() {
        return hour_field;
    }

    JTextField getMinute_field() {
        return minute_field;
    }

    JTextField getMonth_field() {
        return month_field;
    }

    JTextField getDay_field() {
        return day_field;
    }

    JTextField getYear_field() {
        return year_field;
    }

    JCheckBox getEnableEditingCheckBox() {
        return enableEditingCheckBox;
    }

    JCheckBox getGenerateIOBCheckBox() {
        return generateIOBCheckBox;
    }

    JCheckBox getLogCheckBox() {
        return logCheckBox;
    }

    JCheckBox getManualDateCheckBox() {
        return manualDateCheckBox;
    }

    JCheckBox getManualTimeCheckBox() {
        return manualTimeCheckBox;
    }

    JCheckBox getGenerateReportCheckBox() {
        return generateReportCheckBox;
    }

    JRadioButton getPMRadioButton() {
        return PMRadioButton;
    }

    JCheckBox getDoNotUseBGCheckBox() {
        return doNotUseBGCheckBox;
    }

    private JTextPane getLogTextPane() {
        return LogTextPane;
    }

    JLabel getIOBLab() {
        return insulinAmt;
    }

    public IOB_Tracker getTracker() {
        return tracker;
    }

}

package me.themadjem;

import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Jesse Maddox
 *         Copyright 5/28/2017
 */
class UI_Listener implements ActionListener {

    private final Main_UI m;

    /**
     * Constructor
     * @param ui Main_UI used for referencing methods
     */
    UI_Listener(Main_UI ui) {
        m = ui;
    }

    /**
     * Called whenever an action is performed
     * @param e Event
     */
    @Override
    public void actionPerformed(@NotNull ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase(m.getEnableEditingCheckBox().getActionCommand())) changeDataEditMode();
        if (e.getActionCommand().equalsIgnoreCase(m.getManualTimeCheckBox().getActionCommand())) changeTimeMode();
        if (e.getActionCommand().equalsIgnoreCase(m.getManualDateCheckBox().getActionCommand())) changeDateMode();
        if (e.getActionCommand().equalsIgnoreCase(m.getProcessButton().getActionCommand())) Main.process();

    }

    /**
     * Enables or Disables editing for data fields
     */
    private void changeDataEditMode() {
        if (m.getEnableEditingCheckBox().isSelected()) {
            m.getIcr_field().setEditable(true);
            m.getCorrection_field().setEditable(true);
            m.getTargetBG_field().setEditable(true);
        } else {
            m.getIcr_field().setEditable(false);
            m.getCorrection_field().setEditable(false);
            m.getTargetBG_field().setEditable(false);
        }
    }

    /**
     * Enables or Disables editing for Time fields
     */
    private void changeTimeMode() {
        if (m.getManualTimeCheckBox().isSelected()) {
            m.getHour_field().setEditable(true);
            m.getMinute_field().setEditable(true);
        } else {
            m.getHour_field().setEditable(false);
            m.getMinute_field().setEditable(false);
        }
    }

    /**
     * Enables or Disables editing for Date fields
     */
    private void changeDateMode() {
        if (m.getManualDateCheckBox().isSelected()) {
            m.getMonth_field().setEditable(true);
            m.getDay_field().setEditable(true);
            m.getYear_field().setEditable(true);
        } else {
            m.getMonth_field().setEditable(false);
            m.getDay_field().setEditable(false);
            m.getYear_field().setEditable(false);
        }
    }
}

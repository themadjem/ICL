package me.themadjem;

import org.jetbrains.annotations.NotNull;
import themadjem.util.Output;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Jesse Maddox
 *         Copyright 5/28/2017
 */
class Util {
    private static final NumberFormat NF = new DecimalFormat("00");
    private static final DateTimeFormatter LOG_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");

    /**
     * Returns a String for the given LocalTime
     * EX: 14:30 -> "2:30 PM"
     *
     * @param localTime LocalTime to be formatted
     * @return String of time
     */
    @SuppressWarnings("unused")
    @NotNull
    static String formatTime(@NotNull LocalTime localTime) {
        int hour = localTime.getHour();
        int min = localTime.getMinute();
        String end;
        if (hour >= 12) {
            hour -= 12;
            end = "PM";
        } else {
            end = "AM";
        }
        if (hour < 1) hour = 12;
        return NF.format(hour) + ":" + NF.format(min) + " " + end;
    }

    /**
     * Returns a String for the given LocalDateTime in the format of a Date and Time in "MM/dd/yy hh:mm"
     * EX: 2017-05-31T10:40 -> "05/31/17 10:40"
     *
     * @param localDateTime DateTime to format
     * @return Formatted String
     */
    static String formatDateTimeForLog(@NotNull LocalDateTime localDateTime) {
        return localDateTime.format(LOG_FORMAT);
    }

    /**
     * Returns a String of a given Double limited to two decimal places
     *
     * @param d double
     * @return string with two decimals
     */
    static String formatDec(double d) {
        return String.valueOf(new DecimalFormat("#0.00").format(d));
    }

    /**
     * Returns a parsed integer from a JTextField
     *
     * @param j TextField
     * @return integer from field
     */
    static int getInt(@NotNull JTextField j) {
        String s = j.getText();
        if (s.equalsIgnoreCase("") || s.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            Output.infoBox("Error in parsing int\n" + e.getMessage(), "ERROR");
            return 0;
        }
    }

    /**
     * Appends the given message to a given JTextPane with a given Color
     * Copied from StackOverFlow.com
     *
     * @param textPane pane
     * @param msg      message
     * @param c        color
     */
    private static void appendToPane(@NotNull JTextPane textPane, String msg, @SuppressWarnings("SameParameterValue") Color c) {
        textPane.setEditable(true);
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(aset, false);
        textPane.replaceSelection(msg);
        textPane.setEditable(false);
    }

    /**
     * Clears all text from the given
     *
     * @param textPane pane
     */
    static void clearPane(@NotNull JTextPane textPane) {
        textPane.setEditable(true);
        textPane.setText("");
        textPane.setEditable(false);
    }

    static void writeln(String msg, @NotNull JTextPane t) {
        appendToPane(t, msg, Color.BLACK);
        appendToPane(t, "\n", Color.BLACK);
    }
}

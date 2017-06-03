package me.themadjem;

import org.jetbrains.annotations.NotNull;
import themadjem.util.Output;
import themadjem.util.Writer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * todo: JAVADOC all the things
 *
 * @author Jesse Maddox
 *         Copyright 5/28/2017
 * @version 3.2
 */
class Main {
    private static final String COMMA = ",";
    private static Main_UI m;
    private static double carbs;
    private static double bg;
    private static double icr;
    private static double corr;
    private static double target;
    private static double insulin;
    private static int keytone;
    private static LocalTime localTime;
    private static LocalDateTime dateTime;

    /**
     * Main function called on startup
     *
     * @param args arguments
     */
    public static void main(String[] args) {
        m = new Main_UI();
    }

    /**
     * Processes inputs and produces various results
     */
    static void process() {
        updateVars();
        calculateUnits();
        if (shouldGenReport()) showRes();
        if (shouldLog()) writeToLog();
        if (shouldGenIOB()) genIOBSchedule(insulin);
        m.updateLog();
    }

    /**
     * Reads input fields and updates data within the program for other functions to use
     */
    private static void updateVars() {
        carbs = Util.getInt(m.getCarbs_field());
        bg = Util.getInt(m.getBg_field());
        icr = Util.getInt(m.getIcr_field());
        corr = Util.getInt(m.getCorrection_field());
        target = Util.getInt(m.getTargetBG_field());
        keytone = m.getKeytones();

        localTime = LocalTime.now().plusMinutes(2);
        LocalDate localDate = LocalDate.now();
        if (shouldUseManualTime())
            localTime = getManTime();
        if (shouldUseManualDate())
            localDate = getManDate();
        dateTime = LocalDateTime.of(localDate, localTime);
        m.updateProperties();
    }

    /**
     * Calculates the units of insulin to be given
     * <p>
     * Ignores BG if shouldNotUseBG() == true
     * else
     * calculates correction for bg
     * if not less than 0 then
     * if correction minus iob is less than 0 then
     * set correction to 0
     * else subtract iob from correction
     * </p>
     * <p>
     * Makes sure that the iob will not lower the correction to a negative value but
     * still allows the bg itself to create a negative value
     * </p>
     */
    private static void calculateUnits() {
        if (shouldNotUseBG() || bg < 40) {
            insulin = (int) (carbs / icr);
        } else {
            double bgUnits = ((bg - target) / corr);
            double iob = m.getTracker().getIOB(getTime());
            if (!(bgUnits <= 0)) {
                if (bgUnits - iob < 0) {
                    bgUnits = 0;
                } else {
                    bgUnits -= iob;
                }
            }
            insulin = (int) (carbs / icr + bgUnits);
        }
    }

    /**
     * Writes results in the Log.writeToLog file
     */
    private static void writeToLog() {
        Writer logger = new Writer(m.LOG_PATH, true);
        logger.write(
                Util.formatDateTimeForLog(dateTime), COMMA,
                m.getCarbs_field().getText(), COMMA,
                m.getBg_field().getText(), COMMA, insulin);
        if (keytone != -1) logger.write(COMMA, keytone);
        logger.write("\n");
        logger.save();
    }

    /**
     * @param insulinAmt Amount of insulin to amortize
     */
    private static void genIOBSchedule(double insulinAmt) {
        if (insulinAmt == 0) return;

        double period = 240;
        double insulinPerPeriod = insulinAmt / period;

        LocalTime t = Main.getTime();
        for (int i = 0; i < period; i++) {
            m.getTracker().add(t, insulinAmt);
            insulinAmt -= insulinPerPeriod;
            t = t.plusMinutes(1);
        }
    }

    /**
     * Retrieves from the date fields in the UI
     * Returns a LocalDate Object of the given date
     *
     * @return LocalDate object
     */
    @NotNull
    private static LocalDate getManDate() {
        return LocalDate.of(
                Util.getInt(m.getYear_field()),
                Util.getInt(m.getMonth_field()),
                Util.getInt(m.getDay_field())
        );
    }

    /**
     * Retrieves from the time fields in the UI
     * Returns a LocalTime Object of the given time
     *
     * @return LocalTime object
     */
    @NotNull
    private static LocalTime getManTime() {
        int hour = Util.getInt(m.getHour_field());
        if (hour <= 12 && m.getPMRadioButton().isSelected()) hour += 12;
        return LocalTime.of(
                hour,
                Util.getInt(m.getMinute_field())
        );
    }

    /**
     * Displays the info box with the amount of units to be given
     */
    private static void showRes() {
        Output.infoBox(Util.formatDec(insulin) + " units", "Insulin");
    }

    /**
     * @return true if the "Log Results" checkbox is checked
     */
    private static boolean shouldLog() {
        return m.getLogCheckBox().isSelected();
    }

    /**
     * @return true if the "Generate IOB" checkbox is checked
     */
    private static boolean shouldGenIOB() {
        return m.getGenerateIOBCheckBox().isSelected();
    }

    /**
     * @return true if the "Manual Time" checkbox is checked
     */
    private static boolean shouldUseManualTime() {
        return m.getManualTimeCheckBox().isSelected();
    }

    /**
     * @return true if the "Manual Date" checkbox is checked
     */
    private static boolean shouldUseManualDate() {
        return m.getManualDateCheckBox().isSelected();
    }

    /**
     * @return true if the "Generate Report" checkbox is checked
     */
    private static boolean shouldGenReport() {
        return m.getGenerateReportCheckBox().isSelected();
    }

    /**
     * @return true if the "Do not use BG for calculation" checkbox is checked
     */
    private static boolean shouldNotUseBG() {
        return m.getDoNotUseBGCheckBox().isSelected();
    }

    /**
     * @return datetime object of when results were generated
     */
    @SuppressWarnings("unused")
    private static LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * @return time object of when results were generated
     */
    private static LocalTime getTime() {
        return localTime;
    }
}

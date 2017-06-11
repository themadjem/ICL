package me.themadjem;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 *
 */
class IOB_Tracker extends Thread implements Runnable {

    private final DecimalFormat TIME_FORMAT = new DecimalFormat("#0000");
    private final DecimalFormat INSULIN_FORMAT = new DecimalFormat("#00.00");
    private static final String MONTH = "mo", DAY = "day", YEAR = "yr";
    private static final String HOUR = "hr", MINUTE = "min";
    private static final String ZERO = "0";
    private final Main_UI m;
    private final PropertiesUtil p = new PropertiesUtil("IOB.properties", " suppress inspection \"UnusedProperty\" for whole file\n#Insulin on board");

    /**
     * Constructor
     *
     * @param main_ui reference to the Main_UI
     */
    IOB_Tracker(Main_UI main_ui) {
        m = main_ui;
        try {
            p.loadParams();
            clearFromMidnight();
            clearFromLastUse();
        } catch (Exception e) {
            resetProperties();
        }
    }

    /**
     * Loops and updates the IOB Tab every 15 seconds
     */
    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            updateIOB();
            clearLast();
            try {
                sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Clears the IOB property for the last minute
     */
    private void clearLast() {
        p.setProperty(getKey(LocalTime.now().minusMinutes(1)), ZERO);
    }

    /**
     * Clears the IOB.properties file from the time of last IOB_Tracker update and 1 minute before this is called
     */
    private void clearFromLastUse() {
        LocalTime now = LocalTime.now().minusMinutes(1);//Subtract 1 minute as to not remove the current minute's IOB
        if (LocalDate.of(
                Util.getInt(p.getProperty(MONTH, ZERO)),
                Util.getInt(p.getProperty(DAY, ZERO)),
                Util.getInt(p.getProperty(YEAR, ZERO))).
                equals(LocalDate.now())) {
            LocalTime time = LocalTime.of(
                    Util.getInt(p.getProperty(HOUR)),
                    Util.getInt(p.getProperty(MINUTE)));
            while (!time.equals(LocalTime.of(now.getHour(), now.getMinute()))) {
                p.setProperty(getKey(time), ZERO);
                time = time.plusMinutes(1);
            }
        } else {
            resetProperties();
        }
    }

    /**
     * Clears IOB properties from midnight to one minute before this is called
     */
    private void clearFromMidnight() {
        LocalTime time = LocalTime.of(0, 0);
        LocalTime now = LocalTime.now().minusMinutes(1);
        while (!time.equals(LocalTime.of(now.getHour(), now.getMinute()))) {
            p.setProperty(getKey(time), ZERO);
            time = time.plusMinutes(1);
        }
    }

    /**
     * Updates the IOB Tab with the current minute's IOB
     */
    private void updateIOB() {
        LocalDateTime now = LocalDateTime.now();
        saveDate(now);
        m.getIOBLab().setText(INSULIN_FORMAT.format(new Double(p.getProperty(getKey(Util.getTime(now))))));
        p.saveParamChanges();
    }

    /**
     * Saves the given date into the IOB.properties
     *
     * @param dateTime dateTime to dave
     */
    private void saveDate(@NotNull LocalDateTime dateTime) {
        p.setProperty(HOUR, String.valueOf(dateTime.getHour()));
        p.setProperty(MINUTE, String.valueOf(dateTime.getMinute()));
        p.setProperty(MONTH, String.valueOf(dateTime.getMonthValue()));
        p.setProperty(DAY, String.valueOf(dateTime.getDayOfMonth()));
        p.setProperty(YEAR, String.valueOf(dateTime.getYear()));
    }

    /**
     * adds the given insulin amount to the existing amount for the key made from the given time
     *
     * @param time   time
     * @param amount amount of insulin to add
     */
    void add(@NotNull LocalTime time, double amount) {
        p.setProperty(getKey(time), String.valueOf(new Double(p.getProperty(getKey(time))) + amount));
    }

    /**
     * Resets all IOB times to zero
     */
    private void resetProperties() {
        LocalTime time = LocalTime.of(0, 0);
        for (int i = 0; i < 1440; i++) {
            p.setProperty(getKey(time), ZERO);
            time = time.plusMinutes(1);
        }
        p.saveParamChanges();
    }

    /**
     * Returns the key generated from the given time
     * <p>
     * EX: 10:24 -> 1024 || 15:39 -> 1539
     *
     * @param time time
     * @return key
     */
    private String getKey(@NotNull LocalTime time) {
        return TIME_FORMAT.format(time.getHour() * 100 + time.getMinute());
    }

    /**
     * Gets the IOB of the given time
     *
     * @param time time
     * @return IOB
     */
    double getIOB(@NotNull LocalTime time) {
        return new Double(p.getProperty(getKey(time)));
    }


}

package me.themadjem;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.time.LocalTime;

/**
 *
 */
public class IOB_Tracker extends Thread implements Runnable {

    private final DecimalFormat TIME_FORMAT = new DecimalFormat("#0000");
    private final DecimalFormat INSULIN = new DecimalFormat("#00.00");
    private final Main_UI m;
    @NotNull
    private final PropertiesUtil p = new PropertiesUtil("IOB.properties", "Could not load IOB", "Insulin on board");

    IOB_Tracker(Main_UI main_ui) {
        m = main_ui;
        try {
            p.loadParams();
            clearFromMidnight();
        } catch (Exception e) {
            makeFile();
        }
    }

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

    private void clearLast() {
        p.setProperty(getKey(LocalTime.now().minusMinutes(1)), "0");
    }

    private void clearFromMidnight(){
        LocalTime time = LocalTime.of(0,0);
        LocalTime now = LocalTime.now().minusMinutes(1);
        while (!time.equals(LocalTime.of(now.getHour(), now.getMinute()))){
            p.setProperty(getKey(time),"0");
            time = time.plusMinutes(1);
        }
    }

    private void updateIOB() {
        m.getIOBLab().setText(INSULIN.format(new Double(p.getProperty(getKey(LocalTime.now())))));
        p.saveParamChanges();
    }

    void add(@NotNull LocalTime time, double amount) {
        p.setProperty(getKey(time), String.valueOf(new Double(p.getProperty(getKey(time))) + amount));
    }

    private void makeFile() {
        LocalTime time = LocalTime.of(0, 0);
        for (int i = 0; i < 1440; i++) {
            p.setProperty(getKey(time), "0");
            time = time.plusMinutes(1);
        }
        p.saveParamChanges();
    }

    private String getKey(@NotNull LocalTime time) {
        return TIME_FORMAT.format(time.getHour() * 100 + time.getMinute());
    }

    double getIOB(@NotNull LocalTime time) {
        return new Double(p.getProperty(getKey(time)));
    }


}

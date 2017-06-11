package me.themadjem;

import themadjem.util.Output;

/**
 * Used to create a popup window in a separate thread
 */
class ResultWindow implements Runnable {

    private final double insulin;

    ResultWindow(final double insulin) {this.insulin = insulin;}


    @Override
    public void run() {
        Output.infoBox(Util.formatDec(insulin) + " units", "Insulin");
    }
}

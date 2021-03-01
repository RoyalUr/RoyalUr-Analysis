package com.sothatsit.royalur.analysis.reporting;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A class to hold general formatting rules for
 * reporting the outcomes of analysis targets.
 *
 * @author Paddy Lamont
 */
public class ReportFormatter {

    /** Formats numbers to one decimal place. **/
    private static final NumberFormat oneDPFormatter = new DecimalFormat("#0.0");
    /** Formats numbers to two decimal places. **/
    private static final NumberFormat twoDPFormatter = new DecimalFormat("#0.00");

    /** @return a win percentage formatted as a string. **/
    public static String formatWinPercentage(double winPercentage) {
        return oneDPFormatter.format(winPercentage) + "%";
    }

    /** @return a duration in milliseconds formatted as a string. **/
    public static String formatMSDuration(double durationMS) {
        return twoDPFormatter.format(durationMS) + " ms";
    }

    /** @return a duration in seconds formatted as a string. **/
    public static String formatSecDuration(double durationSeconds) {
        if (durationSeconds < 60)
            return oneDPFormatter.format(durationSeconds) + " sec";

        long minutes = ((long) durationSeconds) / 60;
        long seconds = ((long) durationSeconds) - 60 * minutes;
        return minutes + " min " + seconds + " sec";
    }

    /** @return the mean milliseconds spent per move formatted as a string. **/
    public static String formatMSPerMove(double msPerMove) {
        return twoDPFormatter.format(msPerMove) + " ms/move";
    }

    /** @return the mean moves per game formatted as a string. **/
    public static String formatMovesPerGame(double movesPerGame) {
        return oneDPFormatter.format(movesPerGame) + " moves/game";
    }

    /**
     * @return {@param name} padded to at least a length of {@param length}
     *         by appending the {@param padChar}.
     */
    public static String pad(String name, int length, char padChar) {
        if (name.length() >= length)
            return name;

        StringBuilder builder = new StringBuilder(name);
        while (builder.length() < length) {
            builder.append(padChar);
        }
        return builder.toString();
    }
}

package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.ai.Agent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holds the win statistics of an agent.
 *
 * @author Paddy Lamont
 */
public class AgentStats {

    /** The formatter used to format percentages to one decimal place. **/
    private static final NumberFormat percentageFormatter = new DecimalFormat("#0.0");

    /** The agent that is being tested. **/
    public final Agent agent;
    public final AtomicInteger wonAsLight = new AtomicInteger(0);
    public final AtomicInteger wonAsDark = new AtomicInteger(0);
    public final AtomicInteger lostAsLight = new AtomicInteger(0);
    public final AtomicInteger lostAsDark = new AtomicInteger(0);

    public AgentStats(Agent agent) {
        this.agent = agent;
    }

    /** Mark that this agent won a game. **/
    public void markWin(boolean asLight) {
        (asLight ? wonAsLight : wonAsDark).incrementAndGet();
    }

    /** Mark that this agent lost a game. **/
    public void markLoss(boolean asLight) {
        (asLight ? lostAsLight : lostAsDark).incrementAndGet();
    }

    /** @return the percentage (0 -> 100) of games that this agent won. **/
    public double getWinPercentageAsLight() {
        return calculateWinPercentage(wonAsLight.get(), lostAsLight.get());
    }

    /** @return the percentage (0 -> 100) of games that this agent won. **/
    public double getWinPercentageAsDark() {
        return calculateWinPercentage(wonAsDark.get(), lostAsDark.get());
    }

    /** @return the percentage (0 -> 100) of games that this agent won. **/
    public double getWinPercentage() {
        return calculateWinPercentage(
                wonAsLight.get() + wonAsDark.get(),
                lostAsLight.get() + lostAsDark.get()
        );
    }

    /** @return a summary of the statistics measured about this agent. **/
    public String summarise() {
        String won = percentageFormatter.format(getWinPercentage()) + "%";
        String wonAsLight = percentageFormatter.format(getWinPercentageAsLight()) + "%";
        String wonAsDark = percentageFormatter.format(getWinPercentageAsDark()) + "%";
        return "won " + won + " (" + wonAsLight + " when light, " + wonAsDark + " when dark)";
    }

    /** @return the percentage (0 -> 100) of games that were won. **/
    private static double calculateWinPercentage(int won, int lost) {
        if (won == 0)
            return 0;
        return 100.0d * (double) won / (double) (won + lost);
    }
}

package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.analysis.reporting.ReportFormatter;
import com.sothatsit.royalur.simulation.Agent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Holds the win statistics of an agent.
 *
 * @author Paddy Lamont
 */
public class AgentStats {

    /** The user-friendly name of the agent being tested. **/
    public final String name;
    /** The agent that is being tested. **/
    public final Agent agent;
    /** The number of games the agent won as light. **/
    public final AtomicInteger wonAsLight = new AtomicInteger(0);
    /** The number of games the agent won as dark. **/
    public final AtomicInteger wonAsDark = new AtomicInteger(0);
    /** The number of games the agent lost as light. **/
    public final AtomicInteger lostAsLight = new AtomicInteger(0);
    /** The number of games the agent lost as dark. **/
    public final AtomicInteger lostAsDark = new AtomicInteger(0);
    /** The total number of moves the agent made in its games. **/
    public final AtomicLong movesMade = new AtomicLong(0);
    /** The total number of choices presented to this agent. **/
    public final AtomicLong choices = new AtomicLong(0);
    /** The total amount of time the agent spent deciding its moves. **/
    public final AtomicLong timeSpentNanos = new AtomicLong(0);

    public AgentStats(Agent agent) {
        this(agent.describe(), agent);
    }

    public AgentStats(String name, Agent agent) {
        this.name = name;
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

    /** Records the duration this agent spent making a move. **/
    public void recordMoveMade(long durationNanos, int choicesAvailable) {
        movesMade.incrementAndGet();
        choices.addAndGet(choicesAvailable);
        timeSpentNanos.addAndGet(durationNanos);
    }

    /** @return the percentage (0 -> 100) of games that this agent won. **/
    public double getWinPercentageAsLight() {
        return calcWinPercentage(wonAsLight.get(), lostAsLight.get());
    }

    /** @return the percentage (0 -> 100) of games that this agent won. **/
    public double getWinPercentageAsDark() {
        return calcWinPercentage(wonAsDark.get(), lostAsDark.get());
    }

    /** @return the percentage (0 -> 100) of games that this agent won. **/
    public double getWinPercentage() {
        return calcWinPercentage(
                wonAsLight.get() + wonAsDark.get(),
                lostAsLight.get() + lostAsDark.get()
        );
    }

    /** @return the total number of games played by this agent. **/
    public int getTotalGames() {
        return wonAsLight.get() + wonAsDark.get() + lostAsLight.get() + lostAsDark.get();
    }

    /** @return the mean number of milliseconds this agent spent making each of their moves. **/
    public double getMsPerMove() {
        return (double) timeSpentNanos.get() / (double) movesMade.get() * 1e-6d;
    }

    /** The mean number of moves this agent played per game. **/
    public double getMovesPerGame() {
        return (double) movesMade.get() / (double) getTotalGames();
    }

    /**
     * @return the average number of choices per move presented to the agent.
     */
    public double getChoicesPerMove() {
        return (double) choices.get() / (double) movesMade.get();
    }

    /** @return a summary of the statistics measured about this agent. **/
    public String summariseStats() {
        String won = ReportFormatter.formatWinPercentage(getWinPercentage());
        String wonAsLight = ReportFormatter.formatWinPercentage(getWinPercentageAsLight());
        String wonAsDark = ReportFormatter.formatWinPercentage(getWinPercentageAsDark());
        String winSummary = "won " + won + " (" + wonAsLight + " when light, " + wonAsDark + " when dark)";
        String timeSummary = ReportFormatter.formatMSPerMove(getMsPerMove());
        String gameSummary = ReportFormatter.formatMovesPerGame(getMovesPerGame());
        return winSummary + ", " + timeSummary + ", " + gameSummary;
    }

    /** @return the percentage (0 -> 100) of games that were won. **/
    public static double calcWinPercentage(int won, int lost) {
        if (won == 0)
            return 0;
        return 100.0d * (double) won / (double) (won + lost);
    }

    /** @return the mean win percentage as light for all of the agents. **/
    public static double calcLightWinPercentage(AgentStats[] results) {
        double lightWinPercentageSum = 0;
        for (AgentStats stats : results) {
            lightWinPercentageSum += stats.getWinPercentageAsLight();
        }
        return lightWinPercentageSum / results.length;
    }

    /** @return the mean win percentage as dark for all of the agents. **/
    public static double calcDarkWinPercentage(AgentStats[] results) {
        double darkWinPercentageSum = 0;
        for (AgentStats stats : results) {
            darkWinPercentageSum += stats.getWinPercentageAsDark();
        }
        return darkWinPercentageSum / results.length;
    }
}

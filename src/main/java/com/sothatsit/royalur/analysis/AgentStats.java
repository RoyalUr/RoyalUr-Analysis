package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.simulation.Agent;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Holds the win statistics of an agent.
 *
 * @author Paddy Lamont
 */
public class AgentStats {

    private static final NumberFormat oneDPFormatter = new DecimalFormat("#0.0");
    private static final NumberFormat twoDPFormatter = new DecimalFormat("#0.00");

    /** The agent that is being tested. **/
    public final Agent agent;
    public final AtomicInteger wonAsLight = new AtomicInteger(0);
    public final AtomicInteger wonAsDark = new AtomicInteger(0);
    public final AtomicInteger lostAsLight = new AtomicInteger(0);
    public final AtomicInteger lostAsDark = new AtomicInteger(0);
    public final AtomicInteger movesMade = new AtomicInteger(0);
    public final AtomicLong timeSpentNanos = new AtomicLong(0);

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

    /** Records the duration this agent spent making a move. **/
    public void recordMoveMade(long durationNanos) {
        movesMade.incrementAndGet();
        timeSpentNanos.addAndGet(durationNanos);
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

    /** @return a summary of the statistics measured about this agent. **/
    public String summariseStats() {
        String won = oneDPFormatter.format(getWinPercentage()) + "%";
        String wonAsLight = oneDPFormatter.format(getWinPercentageAsLight()) + "%";
        String wonAsDark = oneDPFormatter.format(getWinPercentageAsDark()) + "%";
        String winSummary = "won " + won + " (" + wonAsLight + " when light, " + wonAsDark + " when dark)";
        String timeSummary = twoDPFormatter.format(getMsPerMove()) + " ms/move";
        String gameSummary = oneDPFormatter.format(getMovesPerGame()) + "moves/game";
        return winSummary + ", " + timeSummary + ", " + gameSummary;
    }

    /** @return the percentage (0 -> 100) of games that were won. **/
    private static double calculateWinPercentage(int won, int lost) {
        if (won == 0)
            return 0;
        return 100.0d * (double) won / (double) (won + lost);
    }
}

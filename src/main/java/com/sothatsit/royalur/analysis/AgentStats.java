package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.ai.Agent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Holds the win statistics of an agent.
 *
 * @author Paddy Lamont
 */
public class AgentStats {

    public final Agent agent;
    public final AtomicInteger won = new AtomicInteger(0);
    public final AtomicInteger lost = new AtomicInteger(0);

    public AgentStats(Agent agent) {
        this.agent = agent;
    }

    public void markWin() {
        won.incrementAndGet();
    }

    public void markLoss() {
        lost.incrementAndGet();
    }

    /** @return the percentage (0 -> 100) of games that this agent won. **/
    public double getWinPercentage() {
        int won = this.won.get();
        int lost = this.lost.get();

        if (won == 0)
            return 0;
        return 100.0d * (double) won / (double) (won + lost);
    }
}

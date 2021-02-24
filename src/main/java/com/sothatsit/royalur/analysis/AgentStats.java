package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.ai.Agent;

/**
 * Holds the win statistics of an agent.
 *
 * @author Paddy Lamont
 */
public class AgentStats {

    public final Agent agent;
    public int won;
    public int lost;

    public AgentStats(Agent agent) {
        this.agent = agent;
    }

    public void markWin() {
        won += 1;
    }

    public void markLoss() {
        lost += 1;
    }

    /** @return the percentage (0 -> 100) of games that this agent won. **/
    public double getWinPercentage() {
        if (won == 0)
            return 0;
        return 100.0d * (double) won / (double) (won + lost);
    }
}

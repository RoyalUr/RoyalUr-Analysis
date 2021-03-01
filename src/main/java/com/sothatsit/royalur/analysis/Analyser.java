package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.simulation.Agent;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * A class dedicated to performing the analysis of The Royal Game of Ur.
 *
 * @author Paddy Lamont
 */
public class Analyser {

    private final GameSimulator simulator = new GameSimulator();
    private final AgentStats[] agents;

    public Analyser(AgentType[] agentTypes) {
        this.agents = new AgentStats[agentTypes.length];
        for (int index = 0; index < agentTypes.length; ++index) {
            AgentType type = agentTypes[index];
            this.agents[index] = new AgentStats(type.name, type.agent);
        }
    }

    public Analyser(Agent[] agents) {
        this.agents = new AgentStats[agents.length];
        for (int index = 0; index < agents.length; ++index) {
            this.agents[index] = new AgentStats(agents[index]);
        }
    }

    /**
     * Shuts down the game simulator.
     */
    public void shutdown() {
        simulator.shutdown();
    }

    /**
     * @return a list of the statistics of all the agents,
     *         sorted in descending order by win percentage.
     */
    public AgentStats[] getAgentStats() {
        Arrays.sort(agents, (one, two) -> Double.compare(two.getWinPercentage(), one.getWinPercentage()));
        return agents;
    }

    /** Prints out a report of all of the agents and their performance. **/
    public void printReport() {
        int maxNameLength = 0;
        for (AgentStats stats : agents) {
            maxNameLength = Math.max(maxNameLength, stats.name.length());
        }

        for (AgentStats stats : getAgentStats()) {
            String agentName = pad(stats.name, maxNameLength);
            System.out.println(agentName + "  - " + stats.summariseStats());
        }
    }

    private static String pad(String name, int length) {
        if (name.length() >= length)
            return name;

        StringBuilder builder = new StringBuilder(name);
        while (builder.length() < length) {
            builder.append(' ');
        }
        return builder.toString();
    }

    /**
     * Simulates all possible game pairings between agents {@param games} times.
     */
    public void simulateGames(int iterations, int reportIntervalSeconds) {
        for (int iteration = 0; iteration < iterations; ++iteration) {
            addOneIterationOfGameTasks();
        }

        int totalGames = simulator.countOutstandingGames();
        CountDownLatch latch = simulator.simulateGames();

        while (latch.getCount() > 0) {
            try {
                if (latch.await(reportIntervalSeconds, TimeUnit.SECONDS))
                    break;
            } catch (InterruptedException e) {
                System.err.println("simulateGames was interrupted");
                return;
            }

            int complete = totalGames - ((int) latch.getCount());
            int percent = (100 * complete) / totalGames;
            System.out.println(" .. " + complete + " / " + totalGames + " (" + percent + "%) games completed");
        }
        System.out.println("Done!");
    }

    /**
     * Simulates one game for every agent pairing, and with each agent as light and dark.
     */
    private void addOneIterationOfGameTasks() {
        for (int first = 0; first < agents.length; ++first) {
            for (int second = first + 1; second < agents.length; ++second) {
                AgentStats firstAgent = agents[first];
                AgentStats secondAgent = agents[second];

                simulator.addGame(firstAgent, secondAgent);
                simulator.addGame(secondAgent, firstAgent);
            }
        }
    }
}

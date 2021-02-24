package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.ai.Agent;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.GameState;

import java.util.Arrays;
import java.util.Comparator;

/**
 * A class dedicated to performing the analysis of The Royal Game of Ur.
 *
 * @author Paddy Lamont
 */
public class Analysis {

    private final AgentStats[] agents;

    public Analysis(Agent[] agents) {
        this.agents = new AgentStats[agents.length];
        for (int index = 0; index < agents.length; ++index) {
            this.agents[index] = new AgentStats(agents[index]);
        }
    }

    /** Prints out a report of all of the agents and their performance. **/
    public void printReport() {
        int maxNameLength = 0;
        for (AgentStats stats : agents) {
            maxNameLength = Math.max(maxNameLength, stats.agent.name.length());
        }

        Arrays.sort(agents, (one, two) -> Double.compare(two.getWinPercentage(), one.getWinPercentage()));
        for (AgentStats stats : agents) {
            String agentName = pad(stats.agent.name, ' ', maxNameLength);
            System.out.println(agentName + "  -  won " + (int) stats.getWinPercentage() + "%");
        }
    }

    private static String pad(String name, char padChar, int length) {
        if (name.length() >= length)
            return name;

        StringBuilder builder = new StringBuilder(name);
        while (builder.length() < length) {
            builder.append(padChar);
        }
        return builder.toString();
    }

    /**
     * Simulates all possible game pairings between agents {@param games} times.
     */
    public void simulateGames(int iterations) {
        for (int iteration = 0; iteration < iterations; ++iteration) {
            simulateAllGames();
        }
    }

    /**
     * Simulates a single game between the given agents,
     * and records the results.
     */
    private void simulateGame(Game game, AgentStats light, AgentStats dark) {
        game.reset();
        game.simulateGame(light.agent, dark.agent);
        if (game.state == GameState.LIGHT_WON) {
            light.markWin();
            dark.markLoss();
        } else {
            dark.markWin();
            light.markLoss();
        }
    }

    /**
     * Simulates one game for every agent pairing, and with each agent as light and dark.
     */
    private void simulateAllGames() {
        Game game = new Game();
        for (int first = 0; first < agents.length; ++first) {
            for (int second = first + 1; second < agents.length; ++second) {
                AgentStats firstAgent = agents[first];
                AgentStats secondAgent = agents[second];

                simulateGame(game, firstAgent, secondAgent);
                simulateGame(game, secondAgent, firstAgent);
            }
        }
    }
}

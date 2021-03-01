package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.ai.*;
import com.sothatsit.royalur.ai.utility.CanonicaliseWinsUtilityFn;
import com.sothatsit.royalur.simulation.Agent;

/**
 * Holds a lot of pre-configured agents to be used in testing and as opponents.
 *
 * @author Paddy Lamont
 */
public enum AgentType {
    RANDOM("Random", new RandomAgent()),
    FIRST_MOVE("First Move", new FirstMoveAgent()),
    LAST_MOVE("Last Move", new LastMoveAgent()),
    GREEDY("Greedy", new GreedyAgent()),
    EXPECTIMAX_DEPTH_3("Expectimax Depth 3", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 3)),
    EXPECTIMAX_DEPTH_5("Expectimax Depth 5", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 5)),
    EXPECTIMAX_DEPTH_7("Expectimax Depth 7", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 7)),
    PANDA_DEPTH_5("Panda Depth 5", new PandaAgent(new CanonicaliseWinsUtilityFn(), 5, 2)),
    PANDA_DEPTH_6("Panda Depth 6", new PandaAgent(new CanonicaliseWinsUtilityFn(), 6, 2)),
    PANDA_DEPTH_7("Panda Depth 7", new PandaAgent(new CanonicaliseWinsUtilityFn(), 7, 2));

    public final String name;
    public final Agent agent;

    AgentType(String name, Agent agent) {
        this.name = name;
        this.agent = agent;
    }

    /** @return {@param types} converted to an array of agents. **/
    public static Agent[] toAgentArray(AgentType[] types) {
        Agent[] agents = new Agent[types.length];
        for (int index = 0; index < types.length; ++index) {
            agents[index] = types[index].agent;
        }
        return agents;
    }
}

package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.ai.*;
import com.sothatsit.royalur.ai.utility.CanonicaliseWinsUtilityFn;
import com.sothatsit.royalur.ai.utility.PiecesAdvancedUtilityFn;
import com.sothatsit.royalur.ai.utility.PrioritiseCenterUtilityFn;
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

    ROYALUR_NET_EASY("RoyalUr.net Easy", new RoyalUrNetAgent(RoyalUrNetAgent.Difficulty.EASY)),
    ROYALUR_NET_MEDIUM("RoyalUr.net Medium", new RoyalUrNetAgent(RoyalUrNetAgent.Difficulty.MEDIUM)),
    ROYALUR_NET_HARD("RoyalUr.net Hard", new RoyalUrNetAgent(RoyalUrNetAgent.Difficulty.HARD)),

    EXPECTIMAX_DEPTH_3("Expectimax Depth 3", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 3)),
    EXPECTIMAX_DEPTH_5("Expectimax Depth 5", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 5)),
    EXPECTIMAX_DEPTH_7("Expectimax Depth 7", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 7)),

    PANDA_DEPTH_5("Panda Depth 5", new PandaAgent(new CanonicaliseWinsUtilityFn(), 5, 2)),
    PANDA_DEPTH_6("Panda Depth 6", new PandaAgent(new CanonicaliseWinsUtilityFn(), 6, 2)),
    PANDA_DEPTH_7("Panda Depth 7", new PandaAgent(new CanonicaliseWinsUtilityFn(), 7, 2)),

    D3_UTILITY_PIECES_ADVANCED(
            "Expectimax Depth 3, Pieces Advanced Utility",
            new ExpectimaxAgent(new PiecesAdvancedUtilityFn(), 3)
    ),
    D3_UTILITY_CANONICALISE_WINS(
            "Expectimax Depth 3, Canonicalise Wins Utility",
            new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 3)
    ),
    D3_UTILITY_PRIORITISE_CENTER_7(
            "Expectimax Depth 3, Prioritise Center 7.0 Utility",
            new ExpectimaxAgent(new PrioritiseCenterUtilityFn(7.0f), 3)
    ),
    D3_UTILITY_PRIORITISE_CENTER_8(
            "Expectimax Depth 3, Prioritise Center 8.0 Utility",
            new ExpectimaxAgent(new PrioritiseCenterUtilityFn(8.0f), 3)
    ),
    D3_UTILITY_PRIORITISE_CENTER_9(
            "Expectimax Depth 3, Prioritise Center 9.0 Utility",
            new ExpectimaxAgent(new PrioritiseCenterUtilityFn(9.0f), 3)
    ),

    D5_UTILITY_CANONICALISE_WINS(
            "Expectimax Depth 5, Canonicalise Wins Utility",
            new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 5)
    ),
    D5_UTILITY_PRIORITISE_CENTER_4(
            "Expectimax Depth 5, Prioritise Center 4.0 Utility",
            new ExpectimaxAgent(new PrioritiseCenterUtilityFn(4f), 5)
    ),
    D5_UTILITY_PRIORITISE_CENTER_5(
            "Expectimax Depth 5, Prioritise Center 5.0 Utility",
            new ExpectimaxAgent(new PrioritiseCenterUtilityFn(5f), 5)
    ),
    D5_UTILITY_PRIORITISE_CENTER_6(
            "Expectimax Depth 5, Prioritise Center 6.0 Utility",
            new ExpectimaxAgent(new PrioritiseCenterUtilityFn(6f), 5)
    ),

    D7_UTILITY_PRIORITISE_CENTER_3(
            "Panda Depth 7, Prioritise Center 2.0 Utility",
            new PandaAgent(new PrioritiseCenterUtilityFn(2.0f), 7, 2)
    ),
    D7_UTILITY_PRIORITISE_CENTER_4(
            "Panda Depth 7, Prioritise Center 3.0 Utility",
            new PandaAgent(new PrioritiseCenterUtilityFn(3.0f), 7, 2)
    ),
    D7_UTILITY_PRIORITISE_CENTER_5(
            "Panda Depth 7, Prioritise Center 4.0 Utility",
            new PandaAgent(new PrioritiseCenterUtilityFn(4.0f), 7, 2)
    );

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

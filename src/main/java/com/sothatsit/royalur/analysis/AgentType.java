package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.ai.*;
import com.sothatsit.royalur.ai.utility.CanonicaliseWinsUtilityFn;
import com.sothatsit.royalur.ai.utility.PiecesAdvancedUtilityFn;
import com.sothatsit.royalur.ai.utility.PrioritiseCenterUtilityFn;
import com.sothatsit.royalur.simulation.Agent;

/**
 * Used to represent a type of agent that can be used to play The Royal Game of Ur.
 * Holds a lot of pre-configured agents to be used in testing and as opponents.
 *
 * @author Paddy Lamont
 */
public final class AgentType {

    // Simple agents.
    public static final AgentType RANDOM = new AgentType("Random", new RandomAgent());
    public static final AgentType FIRST_MOVE = new AgentType("First Move", new FirstMoveAgent());
    public static final AgentType LAST_MOVE = new AgentType("Last Move", new LastMoveAgent());
    public static final AgentType GREEDY = new AgentType("Greedy", new GreedyAgent());

    // RoyalUr.net agents.
    public static final AgentType ROYALUR_NET_EASY = new AgentType(
            "RoyalUr.net Easy", new RoyalUrNetAgent(RoyalUrNetAgent.Difficulty.EASY)
    );
    public static final AgentType ROYALUR_NET_MEDIUM = new AgentType(
            "RoyalUr.net Medium", new RoyalUrNetAgent(RoyalUrNetAgent.Difficulty.MEDIUM)
    );
    public static final AgentType ROYALUR_NET_HARD = new AgentType(
            "RoyalUr.net Hard", new RoyalUrNetAgent(RoyalUrNetAgent.Difficulty.HARD)
    );

    // Expectimax agents.
    public static final AgentType EXPECTIMAX_DEPTH_3 = new AgentType(
            "Expectimax Depth 3", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 3)
    );
    public static final AgentType EXPECTIMAX_DEPTH_5 = new AgentType(
            "Expectimax Depth 5", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 5)
    );
    public static final AgentType EXPECTIMAX_DEPTH_7 = new AgentType(
            "Expectimax Depth 7", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 7)
    );

    // Panda agents.
    public static final AgentType PANDA_DEPTH_5 = new AgentType(
            "Panda Depth 5", new PandaAgent(new CanonicaliseWinsUtilityFn(), 5, 2)
    );
    public static final AgentType PANDA_DEPTH_6 = new AgentType(
            "Panda Depth 6", new PandaAgent(new CanonicaliseWinsUtilityFn(), 6, 2)
    );
    public static final AgentType PANDA_DEPTH_7 = new AgentType(
            "Panda Depth 7", new PandaAgent(new CanonicaliseWinsUtilityFn(), 7, 2)
    );
    public static final AgentType BROWSER_PANDA_DEPTH_7 = new AgentType(
            "Panda Depth 7", new PandaAgent(new PrioritiseCenterUtilityFn(4.0f), 7, 2)
    );

    public final String name;
    public final Agent agent;

    public AgentType(String name, Agent agent) {
        this.name = name;
        this.agent = agent;
    }
}

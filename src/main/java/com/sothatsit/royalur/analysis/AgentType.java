package com.sothatsit.royalur.analysis;

import com.sothatsit.royalur.ai.*;
import com.sothatsit.royalur.ai.utility.AlbanReinforcementUtilityFn;
import com.sothatsit.royalur.ai.utility.CanonicaliseWinsUtilityFn;
import com.sothatsit.royalur.ai.utility.PrioritiseCenterUtilityFn;
import com.sothatsit.royalur.simulation.Agent;

import java.util.function.Function;

/**
 * Used to represent a type of agent that can be used to play The Royal Game of Ur.
 * Holds a lot of pre-configured agents to be used in testing and as opponents.
 *
 * @author Paddy Lamont
 */
@SuppressWarnings("unused")
public final class AgentType {

    // Simple agents.
    public static final AgentType RANDOM = createBasic("Random", new RandomAgent());
    public static final AgentType FIRST_MOVE = createBasic("First Move", new FirstMoveAgent());
    public static final AgentType LAST_MOVE = createBasic("Last Move", new LastMoveAgent());
    public static final AgentType GREEDY = createBasic("Greedy", new GreedyAgent());

    // RoyalUr.net agents.
    public static final AgentType ROYALUR_NET_EASY = createBasic(
            "RoyalUr.net Easy", new RoyalUrNetAgent(RoyalUrNetAgent.Difficulty.EASY)
    );
    public static final AgentType ROYALUR_NET_MEDIUM = createBasic(
            "RoyalUr.net Medium", new RoyalUrNetAgent(RoyalUrNetAgent.Difficulty.MEDIUM)
    );
    public static final AgentType ROYALUR_NET_HARD = createBasic(
            "RoyalUr.net Hard", new RoyalUrNetAgent(RoyalUrNetAgent.Difficulty.HARD)
    );

    // Expectimax agents.
    public static final AgentType EXPECTIMAX_DEPTH_3 = createBasic(
            "Expectimax Depth 3", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 3)
    );
    public static final AgentType EXPECTIMAX_DEPTH_5 = createBasic(
            "Expectimax Depth 5", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 5)
    );
    public static final AgentType EXPECTIMAX_DEPTH_7 = createBasic(
            "Expectimax Depth 7", new ExpectimaxAgent(new CanonicaliseWinsUtilityFn(), 7)
    );

    // Panda agents.
    public static final AgentType PANDA_DEPTH_5 = createBasic(
            "Panda Depth 5", new PandaAgent(new CanonicaliseWinsUtilityFn(), 5, 2)
    );
    public static final AgentType PANDA_DEPTH_6 = createBasic(
            "Panda Depth 6", new PandaAgent(new CanonicaliseWinsUtilityFn(), 6, 2)
    );
    public static final AgentType PANDA_DEPTH_7 = createBasic(
            "Panda Depth 7", new PandaAgent(new CanonicaliseWinsUtilityFn(), 7, 2)
    );
    public static final AgentType BROWSER_PANDA_DEPTH_7 = createBasic(
            "Browser Panda Depth 7", new PandaAgent(new PrioritiseCenterUtilityFn(4.0f), 7, 2)
    );

    // Alban Reinforcement Learning agents.
    public static final AgentType ALBAN_DEPTH_3 = createBasic(
            "Alban Depth 3", new ExpectimaxAgent(new AlbanReinforcementUtilityFn(), 3)
    );
    public static final AgentType ALBAN_DEPTH_5 = createBasic(
            "Alban Depth 5", new ExpectimaxAgent(new AlbanReinforcementUtilityFn(), 5)
    );
    public static final AgentType ALBAN_DEPTH_7 = createBasic(
            "Alban Depth 7", new ExpectimaxAgent(new AlbanReinforcementUtilityFn(), 7)
    );
    public static final AgentType BROWSER_PANDA_ALBAN_DEPTH_7 = createBasic(
            "Browser Panda Alban Depth 7", new PandaAgent(new AlbanReinforcementUtilityFn(), 7, 2)
    );

    // Networked agent.
    public static final AgentType REMOTE = new AgentType("Remote", RemoteAgent::create);

    public final String name;
    private final Function<String[], Agent> agentGenerator;

    public AgentType(String name, Function<String[], Agent> agentGenerator) {
        this.name = name;
        this.agentGenerator = agentGenerator;
    }

    public Agent newAgent(String[] args) {
        return agentGenerator.apply(args);
    }

    public ConfiguredAgentType configure(String[] args) {
        return new ConfiguredAgentType(args, this);
    }

    private static AgentType createBasic(String name, Agent agent) {
        Function<String[], Agent> agentGenerator = (args) -> {
            if (args.length != 0)
                throw new IllegalArgumentException("Expected no arguments to create agent");

            return agent.clone();
        };
        return new AgentType(name, agentGenerator);
    }
}

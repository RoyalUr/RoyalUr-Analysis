package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.ai.ExpectimaxAgent;
import com.sothatsit.royalur.ai.PandaAgent;
import com.sothatsit.royalur.ai.utility.CanonicaliseWinsUtilityFn;
import com.sothatsit.royalur.ai.utility.PiecesAdvancedUtilityFn;
import com.sothatsit.royalur.ai.utility.PrioritiseCenterUtilityFn;
import com.sothatsit.royalur.ai.utility.UtilityFunction;
import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;
import com.sothatsit.royalur.simulation.Agent;

/**
 * A target aimed at comparing the performance
 * when using different utility functions.
 *
 * @author Paddy Lamont
 */
public class UtilityFnsTarget extends Target {

    public static final String NAME = "Utility";
    public static final String DESC = "Measures the performance of different utility functions.";

    public UtilityFnsTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        AgentStats[] results3 = runGames(
                "Measuring the performance of different utility functions at depth 3",
                new AgentType[] {
                        createAgentType(3, new PiecesAdvancedUtilityFn()),
                        createAgentType(4, new CanonicaliseWinsUtilityFn()),
                        createAgentType(3, new PrioritiseCenterUtilityFn(7.0f)),
                        createAgentType(3, new PrioritiseCenterUtilityFn(8.0f)),
                        createAgentType(3, new PrioritiseCenterUtilityFn(9.0f))
                },
                100_000
        );
        AgentStats[] results5 = runGames(
                "Measuring the performance of different utility functions at depth 5",
                new AgentType[] {
                        createAgentType(5, new CanonicaliseWinsUtilityFn()),
                        createAgentType(5, new PrioritiseCenterUtilityFn(4.0f)),
                        createAgentType(5, new PrioritiseCenterUtilityFn(5.0f)),
                        createAgentType(5, new PrioritiseCenterUtilityFn(6.0f))
                },
                1000
        );
        AgentStats[] results7 = runGames(
                "Measuring the performance of different utility functions at depth 7",
                new AgentType[] {
                        createAgentType(7, new CanonicaliseWinsUtilityFn()),
                        createAgentType(7, new PrioritiseCenterUtilityFn(3.0f)),
                        createAgentType(7, new PrioritiseCenterUtilityFn(4.0f)),
                        createAgentType(7, new PrioritiseCenterUtilityFn(5.0f))
                },
                100
        );
        return new TargetResult(this) {
            @Override
            public void print() {
                System.out.println("\n### Results of the " + NAME + " target");
                System.out.println(generateAgentWinPercentageTable(results3));
                System.out.println(generateAgentWinPercentageTable(results5));
                System.out.println(generateAgentWinPercentageTable(results7));
            }
        };
    }

    private static AgentType createAgentType(int depth, UtilityFunction utilityFn) {
        Agent agent;
        if (depth <= 5) {
            agent = new ExpectimaxAgent(utilityFn, depth);
        } else {
            agent = new PandaAgent(utilityFn, depth, 2);
        }
        return new AgentType(agent.name + " Depth " + depth + ", " + utilityFn.name + " Utility", args -> agent);
    }
}

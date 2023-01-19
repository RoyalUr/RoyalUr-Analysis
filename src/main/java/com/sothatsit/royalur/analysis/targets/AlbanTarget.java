package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;
import com.sothatsit.royalur.analysis.Analyser;

/**
 * This target aims to test the strength of Alban's
 * reinforcement learning utility function.
 *
 * @author Paddy Lamont
 */
public class AlbanTarget extends Target {

    public static final String NAME = "Alban";
    public static final String DESC = "Measures the strength of Alban's reinforcement learning agent.";

    public AlbanTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        AgentStats[] resultsDepth3 = runGames(
                "Testing Depth 3",
                new AgentType[] { AgentType.EXPECTIMAX_DEPTH_3, AgentType.ALBAN_DEPTH_3 },
                5000
        );
        AgentStats[] resultsDepth5 = runGames(
                "Testing Depth 5",
                new AgentType[] { AgentType.EXPECTIMAX_DEPTH_5, AgentType.ALBAN_DEPTH_5 },
                500
        );
        AgentStats[] resultsDepth7 = runGames(
                "Testing Depth 7",
                new AgentType[] { AgentType.EXPECTIMAX_DEPTH_7, AgentType.ALBAN_DEPTH_7 },
                50
        );
        AgentStats[] resultsBrowserDepth7 = runGames(
                "Testing Depth 7 using Extreme RoyalUr.net AI",
                new AgentType[] { AgentType.BROWSER_PANDA_DEPTH_7, AgentType.BROWSER_PANDA_ALBAN_DEPTH_7 },
                125
        );

        return new TargetResult(this) {
            @Override
            public void print() {
                System.out.println("\n### Results of the " + NAME + " target");
                System.out.println();
                System.out.println("**Depth 3 Expectimax (10,000 games):**\n");
                Analyser.printAgentsReport(resultsDepth3);
                System.out.println();
                System.out.println(generateAgentWinPercentageTable(resultsDepth3));
                System.out.println();
                System.out.println("**Depth 5 Expectimax (1000 games):**\n");
                Analyser.printAgentsReport(resultsDepth5);
                System.out.println();
                System.out.println(generateAgentWinPercentageTable(resultsDepth5));
                System.out.println();
                System.out.println("**Depth 7 Expectimax (100 games):**\n");
                Analyser.printAgentsReport(resultsDepth7);
                System.out.println();
                System.out.println(generateAgentWinPercentageTable(resultsDepth7));
                System.out.println();
                System.out.println("**Depth 7 using Extreme RoyalUr.net AI (250 games):**\n");
                Analyser.printAgentsReport(resultsBrowserDepth7);
                System.out.println();
                System.out.println(generateAgentWinPercentageTable(resultsBrowserDepth7));
            }
        };
    }
}

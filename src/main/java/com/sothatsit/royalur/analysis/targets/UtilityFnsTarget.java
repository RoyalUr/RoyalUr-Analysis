package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;

/**
 * A target aimed at comparing the performance
 * when using different utility functions.
 *
 * @author Paddy Lamont
 */
public class UtilityFnsTarget extends Target {

    public static final String NAME = "UtilityFns";
    public static final String DESC = "Measures the performance of different utility functions.";

    public UtilityFnsTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        AgentStats[] results = runGames(
                "Measuring the performance of different utility functions",
                new AgentType[] {
                        AgentType.UTILITY_PIECES_ADVANCED,
                        AgentType.UTILITY_CANONICALISE_WINS,
                        AgentType.UTILITY_PRIORITISE_CENTER_05,
                        AgentType.UTILITY_PRIORITISE_CENTER_02,
                        AgentType.UTILITY_PRIORITISE_CENTER_01
                },
                1000
        );
        return new TargetResult(this) {
            @Override
            public void print() {
                System.out.println("\n### Results of the " + NAME + " target");
                System.out.println(generateAgentWinPercentageTable(results));
            }
        };
    }
}

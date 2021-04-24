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
                        AgentType.D3_UTILITY_PIECES_ADVANCED,
                        AgentType.D3_UTILITY_CANONICALISE_WINS,
                        AgentType.D3_UTILITY_PRIORITISE_CENTER_7,
                        AgentType.D3_UTILITY_PRIORITISE_CENTER_8,
                        AgentType.D3_UTILITY_PRIORITISE_CENTER_9
                },
                100_000
        );
        AgentStats[] results5 = runGames(
                "Measuring the performance of different utility functions at depth 5",
                new AgentType[] {
                        AgentType.D5_UTILITY_CANONICALISE_WINS,
                        AgentType.D5_UTILITY_PRIORITISE_CENTER_4,
                        AgentType.D5_UTILITY_PRIORITISE_CENTER_5,
                        AgentType.D5_UTILITY_PRIORITISE_CENTER_6
                },
                1000
        );
        AgentStats[] results7 = runGames(
                "Measuring the performance of different utility functions at depth 7",
                new AgentType[] {
                        AgentType.D7_UTILITY_PRIORITISE_CENTER_3,
                        AgentType.D7_UTILITY_PRIORITISE_CENTER_4,
                        AgentType.D7_UTILITY_PRIORITISE_CENTER_5
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
}

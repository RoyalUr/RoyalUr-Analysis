package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;

/**
 * This target aims to generate data so we can approximate Expectimax depth 7.
 *
 * @author Raphaël Côté
 */
public class ExpectimaxMLTarget extends Target {

    public static final String NAME = "ExpectimaxML";
    public static final String DESC = "This target aims to generate data so we can approximate Expectimax depth 7.";

    public ExpectimaxMLTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        AgentStats[] results = runGames(
                "Testing two good players against one another",
                new AgentType[] { AgentType.EXPECTIMAX_ML_DEPTH_7, AgentType.EXPECTIMAX_DEPTH_7 },
                100
        );

        return new TargetResult(this) {
            @Override
            public void print() {
                System.out.println("\n### Results of the " + NAME + " target");
                System.out.println();
                System.out.println(generateAgentWinPercentageTable(results));
            }
        };
    }
}

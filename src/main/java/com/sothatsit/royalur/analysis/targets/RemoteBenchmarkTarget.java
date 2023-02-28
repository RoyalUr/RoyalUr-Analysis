package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;

/**
 * This target allows testing an agent that is run in another process against Expectimax.
 *
 * @author Raphaël Côté
 */
public class RemoteBenchmarkTarget extends Target {

    public static final String NAME = "RemoteBenchmark";
    public static final String DESC = "This target allows testing an agent that is run in another process against Expectimax.";

    public RemoteBenchmarkTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        AgentStats[] results = runGames(
                "Testing a remote agent against Expectimax depth 7",
                new AgentType[] { AgentType.REMOTE, AgentType.EXPECTIMAX_DEPTH_7 },
                50
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

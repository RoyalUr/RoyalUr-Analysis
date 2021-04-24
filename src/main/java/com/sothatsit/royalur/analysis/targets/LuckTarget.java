package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;

/**
 * A target aimed at measuring the amount of
 * luck involved in The Royal Game of Ur.
 *
 * @author Paddy Lamont
 */
public class LuckTarget extends Target {

    public static final String NAME = "Luck";
    public static final String DESC = "Measures the amount of luck involved in The Royal Game of Ur.";

    public LuckTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        AgentType okay = AgentType.GREEDY;
        AgentType good = AgentType.EXPECTIMAX_DEPTH_3;
        AgentType great = AgentType.PANDA_DEPTH_7;


        AgentStats[] okayVsGood = runGames(
                "Testing an okay player against a good player",
                new AgentType[] { okay, good },
                100_000
        );
        AgentStats[] goodVsGreat = runGames(
                "Testing a good player against a great player",
                new AgentType[] { good, great },
                100
        );
        AgentStats[] okayVsGreat = runGames(
                "Testing an okay player against a great player",
                new AgentType[] { okay, great },
                100
        );

        return new TargetResult(this) {
            @Override
            public void print() {
                System.out.println("\n### Results of the " + NAME + " target");
                System.out.println();
                System.out.println("**Okay Player vs. Good Player:**\n");
                System.out.println(generateAgentWinPercentageTable(okayVsGood));
                System.out.println();
                System.out.println("**Good Player vs. Great Player:**\n");
                System.out.println(generateAgentWinPercentageTable(goodVsGreat));
                System.out.println();
                System.out.println("**Okay Player vs. Great Player:**\n");
                System.out.println(generateAgentWinPercentageTable(okayVsGreat));
            }
        };
    }
}

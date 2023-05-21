package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.ai.LikelihoodAgent;
import com.sothatsit.royalur.ai.utility.AlbanReinforcementUtilityFn;
import com.sothatsit.royalur.ai.utility.CanonicaliseWinsUtilityFn;
import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;

/**
 * This target aims to measure the strength of the
 * RoyalUr.net agents against our own agents.
 *
 * @author Paddy Lamont
 */
public class RoyalUrNetTarget extends Target {

    public static final String NAME = "RoyalUr.net";
    public static final String DESC = "Measures the strength of our agents against the RoyalUr.net agents.";

    public RoyalUrNetTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        AgentStats[] againstExpectimaxDepth3 = runGames(
                "Testing the RoyalUr.net Hard agent against Expectimax Depth 3",
                new AgentType[] {
//                        AgentType.ROYALUR_NET_HARD,
                        AgentType.EXPECTIMAX_DEPTH_3,
                        new AgentType("Likelihood A", args -> new LikelihoodAgent(
                                new AlbanReinforcementUtilityFn(),
                                0.09
                        ))
                },
                10000
        );
        AgentStats[] againstPandaDepth5 = runGames(
                "Testing the RoyalUr.net Hard agent against Panda Depth 5",
                new AgentType[] {
//                        AgentType.ROYALUR_NET_HARD,
                        AgentType.PANDA_DEPTH_5,
                        new AgentType("Likelihood B", args -> new LikelihoodAgent(
                                new AlbanReinforcementUtilityFn(),
                                0.0055
                        ))
                },
                1000
        );
//        AgentStats[] againstPandaDepth7 = runGames(
//                "Testing the RoyalUr.net Hard agent against Panda Depth 7",
//                new AgentType[] {
////                        AgentType.ROYALUR_NET_HARD,
//                        AgentType.BROWSER_PANDA_DEPTH_7,
//                        new AgentType("Likelihood A", args -> new LikelihoodAgent(
//                                new CanonicaliseWinsUtilityFn(),
//                                LikelihoodAgent.calculateDepthThreshold(8)
//                        ))
//                },
//                100
//        );

        return new TargetResult(this) {
            @Override
            public void print() {
                System.out.println("\n### Results of the " + NAME + " target");
                System.out.println();
                System.out.println("**RoyalUr.net Hard vs. Expectimax Depth 3**");
                System.out.println(generateAgentWinPercentageTable(againstExpectimaxDepth3));
                System.out.println();
                System.out.println("**RoyalUr.net Hard vs. Panda Depth 5**");
                System.out.println(generateAgentWinPercentageTable(againstPandaDepth5));
//                System.out.println();
//                System.out.println("**RoyalUr.net Hard vs. Panda Depth 7**");
//                System.out.println(generateAgentWinPercentageTable(againstPandaDepth7));
            }
        };
    }
}

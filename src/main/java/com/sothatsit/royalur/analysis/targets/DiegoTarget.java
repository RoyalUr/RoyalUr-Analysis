package com.sothatsit.royalur.analysis.targets;

import com.sothatsit.royalur.analysis.AgentStats;
import com.sothatsit.royalur.analysis.AgentType;

/**
 * This target aims to calculate metrics used in
 * the paper 'Mathematical analysis of the Royal
 * Game of Ur' by Diego Raposo and Padraig Lamont.
 *
 * Example results:
 *
 * **Number of moves per game for both players:**
 *   212.70 moves
 *
 * **Number of choices per move:**
 *   2.45 choices
 *
 * @author Paddy Lamont
 */
public class DiegoTarget extends Target {

    public static final String NAME = "Diego";
    public static final String DESC = "Measures metrics used in Diego Raposo's paper, " +
            "'Mathematical analysis of the Royal Game of Ur'.";

    public DiegoTarget() {
        super(NAME, DESC);
    }

    @Override
    public TargetResult run() {
        AgentStats[] statistics = runGames(
                "Testing RoyalUr.net Extreme agent against itself",
                new AgentType[] {
                        AgentType.BROWSER_PANDA_DEPTH_7,
                        AgentType.BROWSER_PANDA_DEPTH_7
                },
                1000
        );
        AgentStats agent1 = statistics[0];
        AgentStats agent2 = statistics[1];
        double movesPerGame = agent1.getMovesPerGame() + agent2.getMovesPerGame();
        double choicesPerMove = (agent1.getChoicesPerMove() + agent2.getChoicesPerMove()) / 2;

        return new TargetResult(this) {
            @Override
            public void print() {
                System.out.println("\n### Results of the " + NAME + " target");
                System.out.println();
                System.out.println("**Number of moves per game for both players:**");
                System.out.printf("  %.2f moves\n", movesPerGame);
                System.out.println();
                System.out.println("**Number of choices per move:**");
                System.out.printf("  %.2f choices\n", choicesPerMove);
                System.out.println();
            }
        };
    }
}

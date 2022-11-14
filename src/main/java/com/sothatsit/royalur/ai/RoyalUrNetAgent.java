package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.ai.utility.PiecesAdvancedUtilityFn;
import com.sothatsit.royalur.ai.utility.UtilityFunction;
import com.sothatsit.royalur.simulation.*;

import java.util.Random;

/**
 * This class is a recreation of the AI agent used by RoyalUr.net.
 * It has some known flaws, but it is designed to be fun to play
 * instead of to be as strong as is possible can be, so these
 * flaws are just left unfixed.
 *
 * You can see the RoyalUr.net agent's source code in the
 * <a href="https://github.com/Sothatsit/RoyalUrClient/blob/master/client/js/game/simulation.js">
 *     RoyalUrClient repository on GitHub
 * </a>.
 *
 * The RoyalUrAnalysis Expectimax agent of is actually largely
 * based upon the RoyalUr.net agent!
 *
 * @author Paddy Lamont
 */
public class RoyalUrNetAgent extends Agent {

    /** The difficulty level that this agent should play at. **/
    public final Difficulty difficulty;
    /** The utility function used to score the end-states. **/
    private final UtilityFunction utilityFn;
    /** The maximum depth to search to. **/
    private final int depth;
    /** Game objects to re-use while exploring. **/
    private final Game[] games;
    /** MoveList objects to re-use while exploring. **/
    private final MoveList[] moveLists;
    /** BestMoveAndUtility objects to re-use to return the best moves found. **/
    private final BestMoveAndUtility[] bestMoveAndUtilityObjects;

    public RoyalUrNetAgent(Difficulty difficulty) {
        super("RoyalUr.net");
        this.difficulty = difficulty;
        this.utilityFn = new PiecesAdvancedUtilityFn();
        this.depth = difficulty.depth;
        this.games = new Game[depth + 1];
        this.moveLists = new MoveList[depth + 1];
        this.bestMoveAndUtilityObjects = new BestMoveAndUtility[depth + 1];
        for (int index = 0; index <= depth; ++index) {
            games[index] = new Game();
            moveLists[index] = new MoveList();
            bestMoveAndUtilityObjects[index] = new BestMoveAndUtility();
        }
    }

    @Override
    public Agent clone() {
        return new RoyalUrNetAgent(difficulty);
    }

    private static String toString(float num) {
        String num10Str = Integer.toString((int) (num * 10.0f));
        return num10Str.substring(0, num10Str.length() - 1) + "." + num10Str.substring(num10Str.length() - 1);
    }

    public BestMoveAndUtility calculateBestMoveAndUtility(Game game, int roll, int depth) {
        MoveList legalMoves = moveLists[depth];
        game.findPossibleMoves(roll, legalMoves);

        // This is actually a known bug in the RoyalUrAnalysis agent where it
        // doesn't handle no moves correctly, and so we don't handle them
        // properly here either.
        if (legalMoves.count == 0)
            return this.bestMoveAndUtilityObjects[depth].set(-1, 0);

        int bestMoveFrom = -1;
        float bestUtility = 0;
        Game moveGame = games[depth];
        for (int index = 0; index < legalMoves.count; ++index) {
            int moveFrom = legalMoves.positions[index];
            moveGame.copyFrom(game);
            moveGame.performMove(moveFrom, roll);

            float utility;
            if (depth >= this.depth || moveGame.state.finished) {
                utility = utilityFn.scoreGameState(moveGame);
            } else {
                utility = calculateProbabilityWeightedUtility(moveGame, depth + 1);
            }
            // Correct for if the utility is the utility of the other player.
            utility *= (game.state.isLightActive == moveGame.state.isLightActive ? 1 : -1);

            if (bestMoveFrom == -1 || utility >= bestUtility) {
                bestMoveFrom = moveFrom;
                bestUtility = utility;
            }
        }
        return this.bestMoveAndUtilityObjects[depth].set(bestMoveFrom, bestUtility);
    }

    public float calculateProbabilityWeightedUtility(Game game, int depth) {
        float utility = 0;
        float[] probabilities = Roll.PROBABILITIES;
        for (int roll = 0; roll <= 4; ++roll) {
            float rollUtility = calculateBestMoveAndUtility(game, roll, depth).utility;
            utility += probabilities[roll] * rollUtility;
        }
        return utility;
    }

    @Override
    public int determineMove(Game game, int roll, MoveList legalMoves) {
        return calculateBestMoveAndUtility(game, roll, 1).moveFrom;
    }

    @Override
    public String describe() {
        return name + "(" + difficulty.name + ")";
    }

    /**
     * Holds all of the difficulty levels that are
     * available to play against the RoyalUr.net agent.
     */
    public enum Difficulty {
        EASY("Easy", 1),
        MEDIUM("Medium", 2),
        HARD("Hard", 5);

        public final String name;
        public final int depth;

        Difficulty(String name, int depth) {
            this.name = name;
            this.depth = depth;
        }
    }

    /** Stores a possible move, and its calculated utility. **/
    private static class BestMoveAndUtility {

        /** The position to move from. **/
        public int moveFrom;
        /** The calculated utility of this move. **/
        public float utility;

        public BestMoveAndUtility set(int moveFrom, float utility) {
            this.moveFrom = moveFrom;
            this.utility = utility;
            return this;
        }
    }
}

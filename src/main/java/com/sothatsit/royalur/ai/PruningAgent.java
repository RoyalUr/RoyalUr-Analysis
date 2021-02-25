package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.ai.utility.UtilityFunction;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;
import com.sothatsit.royalur.simulation.Roll;

/**
 * An agent that uses iterative deepening and improbable move pruning,
 * combined with expectimax in order to choose its moves. The improbable
 * move pruning helps this agent reach much higher depths than expectimax.
 *
 * This takes advantage of the fact that searching deeper and deeper tends
 * only to make the utility approximations of each move more accurate,
 * with little chance of them changing drastically at higher depths.
 */
public class PruningAgent extends Agent {

    /** The utility function used to score the end-states. **/
    private final UtilityFunction utilityFn;
    /** The maximum depth to search to. **/
    private final int maxDepth;
    /** The depth to search to before filtering out moves. **/
    private final int filterDepth;
    /** Game objects to re-use while exploring. **/
    private final Game[] games;
    /** MoveList objects to re-use while exploring. **/
    private final MoveList[] moveLists;

    public PruningAgent(UtilityFunction utilityFn, int maxDepth, int filterDepth) {
        super("Pruning-" + maxDepth);
        this.utilityFn = utilityFn;
        this.maxDepth = maxDepth;
        this.filterDepth = filterDepth;
        this.games = new Game[maxDepth + 1];
        this.moveLists = new MoveList[maxDepth + 1];
        for (int index = 0; index <= maxDepth; ++index) {
            games[index] = new Game();
            moveLists[index] = new MoveList();
        }
    }

    @Override
    public Agent clone() {
        return new PruningAgent(utilityFn, maxDepth, filterDepth);
    }

    public float calculateBestMoveUtility(Game precedingGame, int roll, int depth) {
        Game game = games[depth];

        MoveList legalMoves = moveLists[depth];
        precedingGame.findPossibleMoves(roll, legalMoves);

        // If there are no possible moves, we need to swap players.
        if (legalMoves.count == 0 || legalMoves.count == 1) {
            game.copyFrom(precedingGame);

            if (legalMoves.count == 0) {
                game.nextTurn();
            } else {
                game.performMove(legalMoves.positions[0], roll);
            }

            float utility = calculateProbabilityWeightedUtility(game, depth + 1);
            // Correct for if the utility is the utility of the other player.
            utility *= (precedingGame.state.isLightActive == game.state.isLightActive ? 1 : -1);
            return utility;
        }

        float maxUtility = Float.NEGATIVE_INFINITY;
        for (int move = 0; move < legalMoves.count; ++move) {
            game.copyFrom(precedingGame);
            game.performMove(legalMoves.positions[move], roll);

            float utility = calculateProbabilityWeightedUtility(game, depth + 1);
            // Correct for if the utility is the utility of the other player.
            utility *= (precedingGame.state.isLightActive == game.state.isLightActive ? 1 : -1);

            maxUtility = Math.max(maxUtility, utility);
        }
        return maxUtility;
    }

    public float calculateProbabilityWeightedUtility(Game game, int depth) {
        if (game.state.finished || depth >= this.maxDepth)
            return utilityFn.scoreGameState(game);

        float utility = 0;
        for (int roll = 0; roll <= Roll.MAX; ++roll) {
            utility += Roll.PROBABILITIES[roll] * calculateBestMoveUtility(game, roll, depth);
        }
        return utility;
    }

    @Override
    public int determineMove(Game originalGame, int roll, MoveList legalMoves) {
        Game game = games[0];

        float maxUtility = Float.NEGATIVE_INFINITY;
        int maxMove = legalMoves.positions[0];

        for (int index = 0; index < legalMoves.count; ++index) {
            int move = legalMoves.positions[index];
            game.copyFrom(originalGame);
            game.performMove(move, roll);
            float utility = calculateProbabilityWeightedUtility(game, 1);
            // Correct for if the utility is the utility of the other player.
            utility *= (originalGame.state.isLightActive == game.state.isLightActive ? 1 : -1);
            if (utility > maxUtility) {
                maxUtility = utility;
                maxMove = move;
            }
        }
        return maxMove;
    }
}

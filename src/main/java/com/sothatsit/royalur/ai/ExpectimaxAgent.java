package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.ai.utility.UtilityFunction;
import com.sothatsit.royalur.simulation.*;

/**
 * An agent that uses expectimax to determine the best move.
 *
 * @author Paddy Lamont
 */
public class ExpectimaxAgent extends Agent {

    /** The utility function used to score the end-states. **/
    protected final UtilityFunction utilityFn;
    /** The maximum depth to search to. **/
    protected final int depth;
    /** Game objects to re-use while exploring. **/
    protected final Game[] games;
    /** MoveList objects to re-use while exploring. **/
    protected final MoveList[] moveLists;

    public ExpectimaxAgent(UtilityFunction utilityFn, int depth) {
        this("Expectimax", utilityFn, depth);
    }

    protected ExpectimaxAgent(String name, UtilityFunction utilityFn, int depth) {
        super(name);
        this.utilityFn = utilityFn;
        this.depth = depth;
        this.games = new Game[depth + 1];
        this.moveLists = new MoveList[depth + 1];
        for (int index = 0; index <= depth; ++index) {
            games[index] = new Game();
            moveLists[index] = new MoveList();
        }
    }

    @Override
    public ExpectimaxAgent clone() {
        return new ExpectimaxAgent(utilityFn, depth);
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
        if (game.state.finished || depth >= this.depth)
            return utilityFn.scoreGameState(game);

        float utility = 0;
        float[] probabilities = Roll.PROBABILITIES;
        for (int roll = 0; roll <= Roll.MAX; ++roll) {
            utility += probabilities[roll] * calculateBestMoveUtility(game, roll, depth);
        }
        return utility;
    }

    @Override
    public int determineMove(Game originalGame, int roll, MoveList legalMoves) {
        if (legalMoves.count == 0)
            return -1;
        if (legalMoves.count == 1)
            return legalMoves.positions[0];

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

    @Override
    public String describe() {
        return name + "(depth " + depth + ", score by " + utilityFn.name + ")";
    }
}

package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.ai.utility.UtilityFunction;
import com.sothatsit.royalur.simulation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * An agent that cuts off its search when the likelihood of the
 * positions occurring falls beneath a threshold.
 *
 * @author Paddy Lamont
 */
public class LikelihoodAgent extends Agent {

    /** The utility function used to score the end-states. **/
    protected final UtilityFunction utilityFn;
    /** The minimum likelihood threshold to consider a state. **/
    protected final double threshold;
    /** The maximum depth that could possibly be reached. **/
    protected final int depth;
    /** Game objects to re-use while exploring. **/
    protected final Game[] games;
    /** MoveList objects to re-use while exploring. **/
    protected final MoveList[] moveLists;

    public static double calculateDepthThreshold(int depth) {
        return Math.pow(0.375, depth);
    }

    public static int calculateThresholdDepth(double threshold) {
        return (int) Math.ceil(Math.log(threshold) / Math.log(0.375));
    }

    public LikelihoodAgent(UtilityFunction utilityFn, double threshold) {
        this("Likelihood", utilityFn, threshold);
    }

    protected LikelihoodAgent(String name, UtilityFunction utilityFn, double threshold) {
        super(name);
        this.utilityFn = utilityFn;
        this.threshold = threshold;
        this.depth = calculateThresholdDepth(threshold);
        this.games = new Game[depth + 1];
        this.moveLists = new MoveList[depth + 1];
        for (int index = 0; index <= depth; ++index) {
            games[index] = new Game();
            moveLists[index] = new MoveList();
        }
    }

    @Override
    public LikelihoodAgent clone() {
        return new LikelihoodAgent(utilityFn, threshold);
    }

    public float calculateBestMoveUtility(Game precedingGame, int roll, int depth, double likelihood) {
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

            float utility = calculateProbabilityWeightedUtility(game, depth + 1, likelihood);
            // Correct for if the utility is the utility of the other player.
            utility *= (precedingGame.state.isLightActive == game.state.isLightActive ? 1 : -1);
            return utility;
        }

        float maxUtility = Float.NEGATIVE_INFINITY;
        for (int move = 0; move < legalMoves.count; ++move) {
            game.copyFrom(precedingGame);
            game.performMove(legalMoves.positions[move], roll);

            float utility = calculateProbabilityWeightedUtility(game, depth + 1, likelihood);
            // Correct for if the utility is the utility of the other player.
            utility *= (precedingGame.state.isLightActive == game.state.isLightActive ? 1 : -1);

            maxUtility = Math.max(maxUtility, utility);
        }
        return maxUtility;
    }

    public float calculateProbabilityWeightedUtility(Game game, int depth, double likelihood) {
        if (game.state.finished || depth >= this.depth)
            return utilityFn.scoreGameState(game);

        float utility = 0f;
        float[] probabilities = Roll.PROBABILITIES;

        boolean currentUtilityGenerated = false;
        float currentUtility = 0.0f;

        for (int roll = 0; roll <= Roll.MAX; ++roll) {
            float prob = probabilities[roll];
            double newLikelihood = prob * likelihood;

            float rollUtility;
            if (newLikelihood < threshold) {
                if (!currentUtilityGenerated) {
                    currentUtility = utilityFn.scoreGameState(game);
                    currentUtilityGenerated = true;
                }
                rollUtility = currentUtility;
            } else {
                rollUtility = calculateBestMoveUtility(game, roll, depth, likelihood * prob);
            }
            utility += prob * rollUtility;
        }
        return utility;
    }

    @Override
    public int determineMove(Game originalGame, int roll, MoveList legalMoves) {
        if (legalMoves.count == 0)
            return -1;
        if (legalMoves.count == 1)
            return legalMoves.positions[0];

        int maxMove = -1;
        float maxUtility = 0;

        Map<Pos, Float> scores = this.scoreMoves(originalGame, roll, legalMoves);
        for (Map.Entry<Pos, Float> entry : scores.entrySet()) {
            int pos = entry.getKey().pack();
            float utility = entry.getValue();
            if (maxMove == -1 || utility > maxUtility) {
                maxMove = pos;
                maxUtility = utility;
            }
        }
        return maxMove;
    }

    @Override
    public Map<Pos, Float> scoreMoves(Game originalGame, int roll, MoveList legalMoves) {
        Game game = games[0];
        Map<Pos, Float> scores = new HashMap<>();

        for (int index = 0; index < legalMoves.count; ++index) {
            int move = legalMoves.positions[index];
            game.copyFrom(originalGame);
            game.performMove(move, roll);
            float utility = calculateProbabilityWeightedUtility(game, 1, 1.0f);
            // Correct for if the utility is the utility of the other player.
            utility *= (originalGame.state.isLightActive == game.state.isLightActive ? 1 : -1);

            // Add the score to the map.
            scores.put(new Pos(move), utility);
        }
        return scores;
    }

    @Override
    public String describe() {
        return name + "(depth " + depth + ", score by " + utilityFn.name + ")";
    }
}

package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.ai.utility.UtilityFunction;
import com.sothatsit.royalur.simulation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An agent that uses the expectimax algorithm to determine the best move.
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

    protected final Boolean useCache;
    protected static final Map<String, Float> cache = new ConcurrentHashMap<String,Float>();

    public ExpectimaxAgent(UtilityFunction utilityFn, int depth, Boolean useCache) {
        this("Expectimax", utilityFn, depth, useCache);
    }

    protected ExpectimaxAgent(String name, UtilityFunction utilityFn, int depth, Boolean useCache) {
        super(name);
        this.utilityFn = utilityFn;
        this.depth = depth;
        this.useCache = useCache;
        this.games = new Game[depth + 1];
        this.moveLists = new MoveList[depth + 1];
        for (int index = 0; index <= depth; ++index) {
            games[index] = new Game();
            moveLists[index] = new MoveList();
        }
    }

    @Override
    public ExpectimaxAgent clone() {
        return new ExpectimaxAgent(utilityFn, depth, useCache);
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
        String cacheKey = "";
        if (useCache && depth < 5) {
            // TODO: Use a long for a cache key by using the game board state instead of this poor string
            cacheKey = game.toString() + depth;
            if (cache.containsKey(cacheKey)) {
                return cache.get(cacheKey);
            }
        }
        if (game.state.finished || depth >= this.depth)
            return utilityFn.scoreGameState(game);

        float utility = 0;
        float[] probabilities = Roll.PROBABILITIES;
        for (int roll = 0; roll <= Roll.MAX; ++roll) {
            utility += probabilities[roll] * calculateBestMoveUtility(game, roll, depth);
        }
        // TODO: Remove unused cache keys eventually instead of dumb capping
        if (useCache && cache.size() < 2000000 && depth < 5) {
            cache.putIfAbsent(cacheKey, utility);
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
    public Map<Pos, Float> scoreMoves(Game originalGame, int roll, MoveList legalMoves) {
        Game game = games[0];
        Map<Pos, Float> scores = new HashMap<>();

        // We subtract the baseline score to make the scores relative.
        float baselineScore = utilityFn.scoreGameState(originalGame);

        for (int index = 0; index < legalMoves.count; ++index) {
            int move = legalMoves.positions[index];
            game.copyFrom(originalGame);
            game.performMove(move, roll);
            float utility = calculateProbabilityWeightedUtility(game, 1);
            // Correct for if the utility is the utility of the other player.
            utility *= (originalGame.state.isLightActive == game.state.isLightActive ? 1 : -1);

            // Add the score to the map.
            scores.put(new Pos(move), utility - baselineScore);
        }
        return scores;
    }

    @Override
    public String describe() {
        return name + "(depth " + depth + ", score by " + utilityFn.name + ")";
    }
}

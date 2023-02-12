package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.simulation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches evaluations of common board positions.
 */
public class CachedAgent<A extends Agent> extends Agent {

    /**
     * The agent that is being cached.
     */
    public final A agent;

    /**
     * Cached evaluations of game states.
     */
    private final Map<Long, Float> cachedEvaluations;

    /**
     * @param agent The agent that is being cached.
     */
    public CachedAgent(A agent) {
        super("Cached(" + agent.name + ")");
        this.agent = agent;
        this.cachedEvaluations = new HashMap<>();
    }

    /**
     * Cloning is used to avoid multi-threading issues with agents.
     */
    @Override
    public Agent clone() {
        return new CachedAgent<>(agent.clone());
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
    public Map<Pos, Float> scoreMoves(Game game, int roll, MoveList legalMoves) {
        int pieces = 0;
        for (int pos = 0; pos < Pos.MAX; ++pos) {
            if (game.board.get(pos) != 0) {
                pieces += 1;
            }
        }
        if (pieces > 2)
            return agent.scoreMoves(game, roll, legalMoves);

        // Look in the cache for evaluations.
        Game testGame = new Game();
        Map<Pos, Float> scores = null;
        for (int index = 0; index < legalMoves.count; ++index) {
            int pos = legalMoves.positions[index];
            testGame.copyFrom(game);
            testGame.performMove(pos, roll);
            long cacheKey = testGame.toLong();
            Float score = this.cachedEvaluations.get(cacheKey);
            if (score == null) {
                scores = null;
                break;
            }
            if (scores == null) {
                scores = new HashMap<>();
            }
            scores.put(new Pos(pos), score);
        }
        if (scores != null)
            return scores;

        // Generate the evaluations, and cache them.
        scores = agent.scoreMoves(game, roll, legalMoves);
        for (int index = 0; index < legalMoves.count; ++index) {
            int pos = legalMoves.positions[index];
            testGame.copyFrom(game);
            testGame.performMove(pos, roll);
            Float score = scores.get(new Pos(pos));
            if (score == null)
                throw new RuntimeException("Unexpectedly missing evaluation for move...");

            cachedEvaluations.put(testGame.toLong(), score);
        }
        return scores;
    }
}

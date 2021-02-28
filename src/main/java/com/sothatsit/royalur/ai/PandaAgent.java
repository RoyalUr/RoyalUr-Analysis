package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.ai.utility.UtilityFunction;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.Roll;

/**
 * An agent that uses expectimax to determine the best move, but that
 * after a certain depth ignores the possibility of rolling 0's and 4's.
 *
 * @author Paddy Lamont
 */
public class PandaAgent extends ExpectimaxAgent {

    private static final float[] LAZY_PROBABILITIES = {
            0,
            Roll.PROBABILITIES[1] * 16.0f / 14.0f,
            Roll.PROBABILITIES[2] * 16.0f / 14.0f,
            Roll.PROBABILITIES[3] * 16.0f / 14.0f,
            0
    };

    /** The depth to search to while still including rolling 0's and 4's. **/
    private final int fullSearchDepth;

    public PandaAgent(UtilityFunction utilityFn, int depth, int fullSearchDepth) {
        super("Panda", utilityFn, depth);
        this.fullSearchDepth = fullSearchDepth;
    }

    @Override
    public PandaAgent clone() {
        return new PandaAgent(utilityFn, depth, fullSearchDepth);
    }

    @Override
    public float calculateProbabilityWeightedUtility(Game game, int depth) {
        if (game.state.finished || depth >= this.depth)
            return utilityFn.scoreGameState(game);

        int minRoll, maxRoll;
        float[] probabilities;
        if (depth <= fullSearchDepth) {
            minRoll = 0;
            maxRoll = 4;
            probabilities = Roll.PROBABILITIES;
        } else {
            minRoll = 1;
            maxRoll = 3;
            probabilities = LAZY_PROBABILITIES;
        }

        float utility = 0;
        for (int roll = minRoll; roll <= maxRoll; ++roll) {
            utility += probabilities[roll] * calculateBestMoveUtility(game, roll, depth);
        }
        return utility;
    }
}

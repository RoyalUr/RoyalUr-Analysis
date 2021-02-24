package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;

import java.util.Random;

/**
 * An agent that selects random moves.
 *
 * @author Paddy Lamont
 */
public class RandomAgent extends Agent {

    private static final Random RAND = new Random();

    public RandomAgent() {
        super("Random");
    }

    @Override
    public RandomAgent clone() {
        return new RandomAgent();
    }

    @Override
    public int determineMove(Game game, int roll, MoveList legalMoves) {
        return legalMoves.positions[RAND.nextInt(legalMoves.count)];
    }
}

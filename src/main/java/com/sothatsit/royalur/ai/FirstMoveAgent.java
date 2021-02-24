package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;

import java.util.Random;

/**
 * An agent that always selects the first legal move.
 *
 * @author Paddy Lamont
 */
public class FirstMoveAgent extends Agent {

    public FirstMoveAgent() {
        super("First-Move");
    }

    @Override
    public FirstMoveAgent clone() {
        return new FirstMoveAgent();
    }

    @Override
    public int determineMove(Game game, int roll, MoveList legalMoves) {
        return legalMoves.positions[0];
    }
}

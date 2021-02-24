package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;

import java.util.Random;

/**
 * An agent that always selects the last legal move.
 *
 * @author Paddy Lamont
 */
public class LastMoveAgent extends Agent {

    public LastMoveAgent() {
        super("Last-Move");
    }

    @Override
    public int generateMove(Game game, int roll, MoveList legalMoves) {
        return legalMoves.positions[legalMoves.count - 1];
    }
}

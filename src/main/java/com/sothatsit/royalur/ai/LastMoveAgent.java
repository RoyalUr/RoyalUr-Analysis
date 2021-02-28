package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.simulation.Agent;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;

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
    public LastMoveAgent clone() {
        return new LastMoveAgent();
    }

    @Override
    public int determineMove(Game game, int roll, MoveList legalMoves) {
        if (legalMoves.count == 0)
            return -1;

        return legalMoves.positions[legalMoves.count - 1];
    }
}

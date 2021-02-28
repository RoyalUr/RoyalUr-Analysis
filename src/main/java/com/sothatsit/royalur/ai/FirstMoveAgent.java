package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.simulation.Agent;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;

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
        if (legalMoves.count == 0)
            return -1;
        return legalMoves.positions[0];
    }
}

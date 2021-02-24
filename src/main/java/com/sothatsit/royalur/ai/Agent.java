package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;

/**
 * An agent that is able to generate its own moves.
 *
 * @author Paddy Lamont
 */
public abstract class Agent {

    public final String name;

    public Agent(String name) {
        this.name = name;
    }

    public abstract int generateMove(Game game, int roll, MoveList legalMoves);
}

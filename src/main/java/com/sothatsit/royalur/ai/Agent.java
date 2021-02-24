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

    /**
     * Cloning is used to avoid multi-threading issues with agents.
     */
    @Override
    public abstract Agent clone();

    public abstract int determineMove(Game game, int roll, MoveList legalMoves);
}

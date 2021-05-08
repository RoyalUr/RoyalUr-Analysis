package com.sothatsit.royalur.simulation;

import java.util.Map;

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

    /** @return The packed position of the piece to be moved. **/
    public int determineMove(Game game, int roll) {
        MoveList legalMoves = new MoveList();
        game.findPossibleMoves(roll, legalMoves);
        return determineMove(game, roll, legalMoves);
    }

    /** @return The packed position of the piece to be moved. **/
    public abstract int determineMove(Game game, int roll, MoveList legalMoves);

    /**
     * @return A map of the calculated scores of all moves.
     * @throws UnsupportedOperationException if not implemented for this agent.
     */
    public Map<Pos, Float> scoreMoves(Game game, int roll) {
        MoveList legalMoves = new MoveList();
        game.findPossibleMoves(roll, legalMoves);
        return scoreMoves(game, roll, legalMoves);
    }

    /**
     * @return A map of the calculated scores of all moves.
     * @throws UnsupportedOperationException if not implemented for this agent.
     */
    public Map<Pos, Float> scoreMoves(Game game, int roll, MoveList legalMoves) {
        throw new UnsupportedOperationException("scoreMoves is not implemented for this agent");
    }

    /** @return a description of this agent. **/
    public String describe() {
        return name;
    }
}

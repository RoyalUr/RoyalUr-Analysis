package com.sothatsit.royalur.ai;

import com.sothatsit.royalur.simulation.Board;
import com.sothatsit.royalur.simulation.Game;
import com.sothatsit.royalur.simulation.MoveList;
import com.sothatsit.royalur.simulation.Tile;

import java.util.Random;

/**
 * An agent that prioritises capturing pieces, then landing on rosettes,
 * and otherwise will select the last available move.
 *
 * @author Paddy Lamont
 */
public class GreedyAgent extends Agent {

    public GreedyAgent() {
        super("Greedy");
    }

    @Override
    public GreedyAgent clone() {
        return new GreedyAgent();
    }

    @Override
    public int determineMove(Game game, int roll, MoveList legalMoves) {
        Board board = game.board;

        // Check if there are any capturing moves.
        for (int move = legalMoves.count - 1; move >= 0; --move) {
            int destPos = legalMoves.destinations[move];
            if (board.get(destPos) != Tile.EMPTY)
                return legalMoves.positions[move];
        }

        // Check if there are any rosette moves.
        for (int move = legalMoves.count - 1; move >= 0; --move) {
            int destPos = legalMoves.destinations[move];
            if (Tile.isRosette(destPos))
                return legalMoves.positions[move];
        }

        return legalMoves.positions[legalMoves.count - 1];
    }
}

package com.sothatsit.royalur.browser;

import com.sothatsit.royalur.simulation.*;

/**
 * A packet sent to RoyalUrAnalysis, requesting the best move from a position.
 *
 * @author Paddy Lamont
 */
public class MoveRequestPacket {

    public final int depth;
    public final Game state;
    public final int roll;

    private MoveRequestPacket(int depth, Game state, int roll) {
        this.depth = depth;
        this.state = state;
        this.roll = roll;
    }

    /**
     * Reads a move request packet.
     */
    public static MoveRequestPacket read(PacketReader reader) {
        // Read information about the AI to use.
        int depth = reader.nextInt(2);

        // Read the state of the game.
        Game game = new Game();
        game.light.tiles = reader.nextDigit();
        game.light.score = reader.nextDigit();
        game.dark.tiles = reader.nextDigit();
        game.dark.score = reader.nextDigit();
        for (int y = 0; y < 8; ++y) {
            for (int x = 0; x < 3; ++x) {
                int owner = reader.nextDigit();
                int tile = (owner == 1 ? Tile.DARK : (owner == 2 ? Tile.LIGHT : Tile.EMPTY));
                game.board.set(x, y, tile);
            }
        }
        int activePlayerNo = reader.nextDigit();
        game.state = (activePlayerNo == 1 ? GameState.DARK_TURN : GameState.LIGHT_TURN);

        // Read the value of the dice.
        int roll = reader.nextDigit();

        return new MoveRequestPacket(depth, game, roll);
    }
}
